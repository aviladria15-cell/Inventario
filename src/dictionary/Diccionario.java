/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dictionary;

import java.io.*;
import java.util.*;

public class Diccionario {
    private static Set<String> palabras = new HashSet<>();

    // Cargar diccionario desde archivo .dic
    public static void cargarDiccionario(String ruta) {
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (!linea.isEmpty() && !linea.startsWith("#")) {
                    palabras.add(linea);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Verifica si la palabra está en el diccionario
    public static boolean existePalabra(String texto) {
        return palabras.contains(texto);
    }

    // Corrige buscando la más parecida
    public static String corregir(String texto) {
        if (palabras.contains(texto)) {
            return texto; // ya existe en el diccionario
        }

        String sugerencia = null;
        int distanciaMinima = Integer.MAX_VALUE;

        for (String palabra : palabras) {
            int distancia = levenshtein(texto.toLowerCase(), palabra.toLowerCase());
            if (distancia < distanciaMinima) {
                distanciaMinima = distancia;
                sugerencia = palabra;
            }
        }

        // Solo corregimos si la palabra se parece lo suficiente
        if (sugerencia != null && distanciaMinima <= 2) {
            return sugerencia;
        }

        return texto;
    }

    // Distancia de Levenshtein
    private static int levenshtein(String a, String b) {
        int[][] dp = new int[a.length() + 1][b.length() + 1];

        for (int i = 0; i <= a.length(); i++) dp[i][0] = i;
        for (int j = 0; j <= b.length(); j++) dp[0][j] = j;

        for (int i = 1; i <= a.length(); i++) {
            for (int j = 1; j <= b.length(); j++) {
                int costo = (a.charAt(i - 1) == b.charAt(j - 1)) ? 0 : 1;
                dp[i][j] = Math.min(
                    Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1),
                    dp[i - 1][j - 1] + costo
                );
            }
        }

        return dp[a.length()][b.length()];
    }
}
