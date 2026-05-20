package Controlador;

import Funciones.Alerte_De_Stock;
import Funciones.BCV;
import java.awt.Font;
import javax.swing.UIManager;
import Vista_Usuari_Empleado.frm_GestionarEmpleado;
import Vista_Usuari_Empleado.Login_Ingreso;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime; // Importación necesaria para la hora
import javax.swing.Timer;
import Vista_Usuari_Empleado.Menu_Sistema;
import javax.swing.JOptionPane;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import java.awt.Color;
import java.awt.Insets;
import ModeloDAO.ProveedorDao;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class controladorVisual {

    Funciones.Alerte_De_Stock alerta = new Alerte_De_Stock();

    public void Menu_Empleado(frm_GestionarEmpleado Em) {
        Em.setVisible(true);
    }

    public void CerrarSeccion(Menu_Sistema M) {
        M.setVisible(false);
        Login_Ingreso.txtPasContraseña.setText("");
        Login_Ingreso.txtUsuario.setText("");
    }

    // Este metodo para iniciar la hora en la vista del menu del sistema
    public void IniciarHora() {
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LocalDateTime ahora = LocalDateTime.now();
                DateTimeFormatter formato = DateTimeFormatter.ofPattern(
                        "EEEE, dd 'de' MMMM 'de' yyyy    hh:mm:ss a", Locale.forLanguageTag("es-ES"));
                Menu_Sistema.lblHora.setText(ahora.format(formato));
            }
        });
        timer.start();
    }

    // Este metodo para inicar la hora en el menu del login
    public void IniciarHoraLogin() {
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LocalDateTime ahora = LocalDateTime.now();
                DateTimeFormatter formato = DateTimeFormatter.ofPattern(
                        "EEEE, dd 'de' MMMM 'de' yyyy    hh:mm:ss a", Locale.forLanguageTag("es-ES"));
                Login_Ingreso.lblHora.setText(ahora.format(formato));
            }
        });
        timer.start();
    }

    // --- Se eliminó la llave que causaba el error aquí ---

    // Este metodo da estilo al sistema (interfaz macOS)
    public void Estilo() {
        try {
            // Look & Feel moderno estilo macOS
            UIManager.setLookAndFeel(new FlatMacLightLaf());
            FlatLaf.registerCustomDefaultsSource("recursos");
            FlatLaf.setUseNativeWindowDecorations(true);

            // Fuente elegante y legible
            UIManager.put("defaultFont", new Font("San Francisco", Font.PLAIN, 15));

            // Curvatura general
            int arc = 18;
            UIManager.put("Component.arc", arc);
            UIManager.put("Button.arc", arc);
            UIManager.put("TextComponent.arc", arc);
            UIManager.put("ProgressBar.arc", arc);

            // Colores principales estilo Apple
            Color accent = new Color(0, 122, 255);
            Color accentHover = new Color(0, 122, 255, 30);
            Color accentBorder = new Color(0, 122, 255, 160);
            Color panelBg = new Color(242, 242, 247);
            
            UIManager.put("Panel.background", panelBg);

            // ---- Botones ----
            UIManager.put("Button.background", Color.WHITE);
            UIManager.put("Button.foreground", new Color(28, 28, 30));
            UIManager.put("Button.padding", new Insets(12, 24, 12, 24));
            UIManager.put("Button.hoverBackground", accentHover);
            UIManager.put("Button.hoverBorderColor", accentBorder);

            // ---- Tablas ----
            UIManager.put("Table.gridColor", new Color(210, 210, 215));
            UIManager.put("Table.background", Color.WHITE);
            UIManager.put("Table.alternateRowColor", new Color(0, 191, 255));
            UIManager.put("Table.selectionBackground", accent);
            UIManager.put("Table.selectionForeground", Color.WHITE);
            UIManager.put("TableHeader.background", new Color(242, 242, 247));

        } catch (Exception e) {
            System.err.println("Error al aplicar estilo Apple-like");
            e.printStackTrace();
        }
    }
} // <--- Esta es la llave final que cierra la clase