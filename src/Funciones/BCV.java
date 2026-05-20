/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
// Funciones/BCV.java
package Funciones;


import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;
import java.util.regex.*;
import javax.swing.*;

public class BCV {
    private static final String URL_TCAMBI = "https://www.tcambio.app/";
    private static final String CACHE_FILE = "last_rate.txt";
    private static final double FALLBACK_RATE = 233.04;

    /**
     * MÉTODO PRINCIPAL: Llamar SOLO desde el constructor de la vista
     * Muestra la tasa en el JTextField y se actualiza cada 30 min.
     */
    public static void mostrarTasaEnCampo(JTextField campoTexto) {
        // Carga inicial
        actualizarCampo(campoTexto);

        // Programar actualizaciones cada 30 minutos
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                actualizarCampo(campoTexto);
            }
        }, 30 * 60 * 1000, 30 * 60 * 1000); // 30 min
    }

    // ===================================================================
    // MÉTODO PRIVADO: Actualiza el campo con la tasa
    // ===================================================================
    private static void actualizarCampo(JTextField campo) {
        campo.setText("Cargando...");

        new Thread(() -> {
            double tasa = obtenerTasaConCache();

            SwingUtilities.invokeLater(() -> {
                campo.setText(String.format("%.2f", tasa));
            });
        }).start();
    }

    // ===================================================================
    // OBTENER TASA (online → cache → fallback)
    // ===================================================================
    private static double obtenerTasaConCache() {
        try {
            double tasa = obtenerTasaOnline();
            if (tasa > 1 && tasa < 10000) {
                guardarEnCache(tasa);
                return tasa;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
    null,
    "Sin conexión, usando caché...",
    "Advertencia",
    JOptionPane.WARNING_MESSAGE
);

        }
        return leerDeCache();
    }

    private static double obtenerTasaOnline() throws Exception {
        URL url = new URL(URL_TCAMBI);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(8000);
        conn.setReadTimeout(8000);
        conn.setRequestProperty("User-Agent", "Mozilla/5.0");

        StringBuilder html = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                html.append(linea).append("\n");
            }
        } finally {
            conn.disconnect();
        }

        return extraerTasa(html.toString());
    }

    private static double extraerTasa(String html) {
        Pattern[] patrones = {
            Pattern.compile(">\\s*(\\d+[.,]\\d+)\\s*<strong>"),
            Pattern.compile("USD\\s*[\\-:]?\\s*[\\$]?\\s*(\\d+[.,]\\d+)"),
            Pattern.compile(">\\$?\\s*(\\d+[.,]\\d+)\\s*</"),
            Pattern.compile("data-rate=\"(\\d+[.,]\\d+)\"")
        };

        for (Pattern p : patrones) {
            Matcher m = p.matcher(html);
            if (m.find()) {
                String num = m.group(1).replace(",", ".").trim();
                try {
                    double tasa = Double.parseDouble(num);
                    if (tasa > 1 && tasa < 10000) {
                        return Math.round(tasa * 100.0) / 100.0;
                    }
                } catch (Exception ignored) {}
            }
        }
        return FALLBACK_RATE;
    }

    // ===================================================================
    // CACHE: Guardar y leer
    // ===================================================================
    private static void guardarEnCache(double tasa) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(CACHE_FILE))) {
            pw.println(tasa);
        } catch (Exception ignored) {}
    }

    public static double leerDeCache() {
        File archivo = new File(CACHE_FILE);
        if (archivo.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
                double tasa = Double.parseDouble(br.readLine().trim());
                if (tasa > 1 && tasa < 10000) {
                    return tasa;
                }
            } catch (Exception ignored) {}
        }
        return FALLBACK_RATE;
    }
}





















