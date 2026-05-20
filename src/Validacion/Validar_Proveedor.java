/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Validacion;

import Modelo.Proveedor;
import ModeloDAO.ProveedorDao;
import Validacion.Validar_Formulario_Empleado.SimpleDocumentListener;
import Vista_Gestionar_Proveedor.Gestionar_Proveedor;
import java.sql.SQLException;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.DocumentFilter.FilterBypass;


public class Validar_Proveedor  extends ProveedorDao {

 ProveedorDao pro = new ProveedorDao();
 
 Proveedor p = new Proveedor();
 
 

public void inicializarValidacionesProveedor() {
    // Ocultar etiquetas de validación
    ocultarError(Gestionar_Proveedor.lblNompreProveedorValidacion);
    ocultarError(Gestionar_Proveedor.lblNumeroTelefonoValidacion);
    ocultarError(Gestionar_Proveedor.lblEmailValidacion);
    ocultarError(Gestionar_Proveedor.lblRfcValidadcion);
    ocultarError(Gestionar_Proveedor.lblDirrecionValidacion);

    // === APLICAR FILTROS DE ENTRADA ===
    aplicarFiltroSoloLetras(Gestionar_Proveedor.txtNombreProveedor);
    aplicarFiltroTelefonoProveedor(Gestionar_Proveedor.txtTelefonoProveedor);

    // === VALIDACIÓN VISUAL EN TIEMPO REAL ===
    Gestionar_Proveedor.txtNombreProveedor.getDocument()
        .addDocumentListener(new SimpleDocumentListener(this::validarNombreProveedor));
    
    Gestionar_Proveedor.txtTelefonoProveedor.getDocument()
        .addDocumentListener(new SimpleDocumentListener(this::validarTelefono));

    Gestionar_Proveedor.txtEmailProveedor.getDocument()
        .addDocumentListener(new SimpleDocumentListener(this::validarCorreo));
    
    Gestionar_Proveedor.txtRFCProveedor.getDocument()
        .addDocumentListener(new SimpleDocumentListener(this::validarRif));
    
    Gestionar_Proveedor.txtDireccionProveedor.getDocument()
        .addDocumentListener(new SimpleDocumentListener(this::validarDireccionProveedor));

    // Correo con @gmail.com automático
    fijarCorreoGmail();
}




private void aplicarFiltroSoloLetras(JTextField campo) {
    ((AbstractDocument) campo.getDocument()).setDocumentFilter(new DocumentFilter() {
        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                throws BadLocationException {
            if (string != null && string.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]*")) {
                super.insertString(fb, offset, string, attr);
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                throws BadLocationException {
            if (text != null && text.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]*")) {
                super.replace(fb, offset, length, text, attrs);
            }
        }
    });
}




private void aplicarFiltroTelefonoProveedor(JTextField campo) {
    ((AbstractDocument) campo.getDocument()).setDocumentFilter(new DocumentFilter() {
        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                throws BadLocationException {
            String nuevoTexto = construirTexto(fb, offset, length, text);
            if (esValidoTelefonoProveedor(nuevoTexto)) {
                super.replace(fb, offset, length, text, attrs);
            }
        }

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                throws BadLocationException {
            String nuevoTexto = construirTexto(fb, offset, 0, string);
            if (esValidoTelefonoProveedor(nuevoTexto)) {
                super.insertString(fb, offset, string, attr);
            }
        }

        private String construirTexto(FilterBypass fb, int offset, int length, String text)
                throws BadLocationException {
            String actual = fb.getDocument().getText(0, fb.getDocument().getLength());
            return actual.substring(0, offset) + text + actual.substring(offset + length);
        }

        private boolean esValidoTelefonoProveedor(String texto) {
            if (!texto.matches("\\d*")) return false;
            if (texto.isEmpty()) return true;
            if (texto.length() > 11) return false;
            return texto.startsWith("04") || texto.equals("0") || texto.equals("04");
        }
    });
}


@FunctionalInterface
interface RunnableTask { void run(); }

static class SimpleDocumentListener implements DocumentListener {
    private final RunnableTask task;
    public SimpleDocumentListener(RunnableTask task) { this.task = task; }
    public void insertUpdate(DocumentEvent e) { task.run(); }
    public void removeUpdate(DocumentEvent e) { task.run(); }
    public void changedUpdate(DocumentEvent e) { task.run(); }
}

  private void validarNombreProveedor() {
    String nombre = Gestionar_Proveedor.txtNombreProveedor.getText().trim();
    if (nombre.isEmpty()) {
        mostrarError(Gestionar_Proveedor.lblNompreProveedorValidacion, "El nombre no puede estar vacío");
    } else {
        ocultarError(Gestionar_Proveedor.lblNompreProveedorValidacion);
    }
}

public void validarTelefono() {
    String telefono = Gestionar_Proveedor.txtTelefonoProveedor.getText().trim();
    if (telefono.isEmpty()) {
        mostrarError(Gestionar_Proveedor.lblNumeroTelefonoValidacion, "El teléfono no puede estar vacío");
    } else if (!telefono.startsWith("04")) {
        mostrarError(Gestionar_Proveedor.lblNumeroTelefonoValidacion, "Debe comenzar con 04");
    } else if (telefono.length() != 11) {
        mostrarError(Gestionar_Proveedor.lblNumeroTelefonoValidacion, "Debe tener 11 dígitos");
    } else {
        ocultarError(Gestionar_Proveedor.lblNumeroTelefonoValidacion);
    }
}
      private void fijarCorreoGmail() {
        Gestionar_Proveedor.txtEmailProveedor.setText("");

        AbstractDocument doc = (AbstractDocument) Gestionar_Proveedor.txtEmailProveedor.getDocument();
        doc.setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                    throws BadLocationException {
                super.insertString(fb, offset, string, attr);
                agregarDominio(fb);
                validarCorreoTexto(fb);
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                    throws BadLocationException {
                super.replace(fb, offset, length, text, attrs);
                agregarDominio(fb);
                validarCorreoTexto(fb);
            }

            @Override
            public void remove(FilterBypass fb, int offset, int length)
                    throws BadLocationException {
                super.remove(fb, offset, length);
                agregarDominio(fb);
                validarCorreoTexto(fb);
            }

            private void agregarDominio(FilterBypass fb) throws BadLocationException {
                String text = fb.getDocument().getText(0, fb.getDocument().getLength());
                if (!text.endsWith("@gmail.com")) {
                    text = text.replace("@gmail.com", "");
                    fb.replace(0, fb.getDocument().getLength(), text + "@gmail.com", null);
                    int pos = text.length();
                    SwingUtilities.invokeLater(() -> Gestionar_Proveedor.txtEmailProveedor.setCaretPosition(pos));
                }
            }

            private void validarCorreoTexto(FilterBypass fb) throws BadLocationException {
                String text = fb.getDocument().getText(0, fb.getDocument().getLength());
                String nombre = text.replace("@gmail.com", "");
                if (nombre.isEmpty()) {
                    Gestionar_Proveedor.lblEmailValidacion.setText("Debes escribir el nombre del correo");
                    Gestionar_Proveedor.lblEmailValidacion.setVisible(true);
                } else if (!nombre.matches("^[a-zA-Z0-9._-]+$")) {
                  Gestionar_Proveedor.lblEmailValidacion.setText("Nombre no válido para correo");
                    Gestionar_Proveedor.lblEmailValidacion.setVisible(true);
                } else {
                    Gestionar_Proveedor.lblEmailValidacion.setVisible(false);
                }
            }
        });
    }

    private void validarCorreo() {
        String correo = Gestionar_Proveedor.txtEmailProveedor.getText().trim();
        if (correo.isEmpty() || correo.equals("@gmail.com")) {
            Gestionar_Proveedor.lblEmailValidacion.setText("El correo no puede estar vacío");
            Gestionar_Proveedor.lblEmailValidacion.setVisible(true);
        } else if (!correo.matches("^[a-zA-Z0-9._-]+@gmail\\.com$")) {
            Gestionar_Proveedor.lblEmailValidacion.setText("Correo no válido (debe ser @gmail.com)");
            Gestionar_Proveedor.lblEmailValidacion.setVisible(true);
        } else {
            Gestionar_Proveedor.lblEmailValidacion.setVisible(false);
        }
    }

 private void validarRif() {
    String raw = Gestionar_Proveedor.txtRFCProveedor.getText().trim();
    JTextField txt = Gestionar_Proveedor.txtRFCProveedor;
    JLabel lbl = Gestionar_Proveedor.lblRfcValidadcion;

    lbl.setVisible(false);
    lbl.setText("");

    if (raw.isEmpty()) {
        lbl.setText("Prefijo válido: V/E/J/G/P");
        lbl.setVisible(true);
        return;
    }

    String rif = raw.toUpperCase();

    // ---- Prefijo ----
    char prefijo = rif.charAt(0);
    if (!Character.toString(prefijo).matches("[VEJGP]")) {
        lbl.setText("Prefijo inválido (V/E/J/G/P)");
        lbl.setVisible(true);
        return;
    }

    // ---- Conteo de guiones ----
    int guiones = (int) rif.chars().filter(ch -> ch == '-').count();
    if (guiones > 2) {
        lbl.setText("Demasiados '-'");
        lbl.setVisible(true);
        return;
    }

    // ---- Separar partes ----
    String[] partes = rif.split("-", -1);
    String middle = (partes.length >= 2) ? partes[1] : "";
    String dv = (partes.length == 3) ? partes[2] : "";

    // ---- Validar parte central ----
    if (!middle.isEmpty() && !middle.matches("\\d*")) {
        lbl.setText("Solo números en el centro");
        lbl.setVisible(true);
        return;
    }

    if (middle.length() < 8) {
        lbl.setText("Faltan " + (8 - middle.length()) + " dígitos");
        lbl.setVisible(true);
    } else if (middle.length() > 8) {
        lbl.setText("Máx 8 dígitos");
        lbl.setVisible(true);

        // 🔒 Bloquear exceso sin mutar dentro del evento
        SwingUtilities.invokeLater(() -> {
            int prefLen = rif.indexOf('-') + 9;
            if (prefLen > 0 && prefLen <= rif.length()) {
                txt.setText(rif.substring(0, prefLen));
            }
        });
        return;
    }

    // ---- Validar dígito verificador ----
    if (!dv.isEmpty() && !dv.matches("\\d*")) {
        lbl.setText("El verificador debe ser número");
        lbl.setVisible(true);
        return;
    }

    if (dv.length() > 1) {
        lbl.setText("Solo 1 dígito al final");
        lbl.setVisible(true);

        // 🔒 Bloquear exceso sin mutar dentro del evento
        SwingUtilities.invokeLater(() -> {
            int idx = rif.lastIndexOf('-');
            if (idx != -1 && idx + 2 <= rif.length()) {
                txt.setText(rif.substring(0, idx + 2));
            }
        });
        return;
    }

    // ---- Validar estructura final ----
    if (!rif.matches("^[VEJGP]-\\d{8}-\\d$")) {
        lbl.setText("Formato: J-12345678-9");
        lbl.setVisible(true);
        return;
    }

    // ✅ Todo correcto
    lbl.setVisible(false);
    lbl.setText("");
}


    private void validarDireccionProveedor() {
        String dir = Gestionar_Proveedor.txtDireccionProveedor.getText().trim();
        if (dir.isEmpty()) {
            mostrarError(Gestionar_Proveedor.lblDirrecionValidacion, "La dirección no puede estar vacía");
        } else if (!dir.matches("^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ#.,()\\- ]+$")) {
            mostrarError(Gestionar_Proveedor.lblDirrecionValidacion, "Solo letras, números, punto, coma, #, y paréntesis");
        } else {
            ocultarError(Gestionar_Proveedor.lblDirrecionValidacion);
        }
    }

    // ------------------- FUNCIONES AUXILIARES -------------------
    private void mostrarError(JLabel label, String mensaje) {
        label.setText(mensaje);
        label.setVisible(true);
    }

    private void ocultarError(JLabel label) {
        label.setVisible(false);
    }

    // ------------------- VALIDAR FORMULARIO -------------------
    public boolean validarFormularioProveedor() {
        validarNombreProveedor();
        validarTelefono();
        validarCorreo();
     validarRif();

        validarDireccionProveedor();

        return !Gestionar_Proveedor.lblNompreProveedorValidacion.isVisible()
                && !Gestionar_Proveedor.lblNumeroTelefonoValidacion.isVisible()
                && !Gestionar_Proveedor.lblEmailValidacion.isVisible()
                && !Gestionar_Proveedor.lblRfcValidadcion.isVisible()
                && !Gestionar_Proveedor.lblDirrecionValidacion.isVisible();
    }

    // ------------------- REGISTRAR PROVEEDOR -------------------
    public void registrarProveedorValidado() throws ClassNotFoundException {
        if (!validarFormularioProveedor()) {
            JOptionPane.showMessageDialog(null, "Corrige los errores antes de registrar el proveedor.");
            return;
        }

        try {
            Proveedor p = new Proveedor();
            p.setNombre(Gestionar_Proveedor.txtNombreProveedor.getText().trim());
            p.setTelefono(Gestionar_Proveedor.txtTelefonoProveedor.getText().trim());
            
            // Agrega automáticamente el dominio gmail.com
            String correo = Gestionar_Proveedor.txtEmailProveedor.getText().trim() + "@gmail.com";
            p.setEmail(correo);

            p.setRFC(Gestionar_Proveedor.txtRFCProveedor.getText().trim());
            p.setDireccion(Gestionar_Proveedor.txtDireccionProveedor.getText().trim());

            pro.GuardaProveedor(p);
           
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al registrar proveedor: " + ex.getMessage());
        }
    }

    // ------------------- ACTUALIZAR PROVEEDOR -------------------
    public void actualizarProveedorValidado() throws ClassNotFoundException {
        if (!validarFormularioProveedor()) {
            JOptionPane.showMessageDialog(null, "Corrige los errores antes de actualizar el proveedor.");
            return;
        }

        try {
            Proveedor p = new Proveedor();
            // Si tienes un campo de ID:
            // p.setIdProveedor(Integer.parseInt(Gestionar_Proveedor.txtIdProveedor.getText().trim()));

            p.setNombre(Gestionar_Proveedor.txtNombreProveedor.getText().trim());
            p.setTelefono(Gestionar_Proveedor.txtTelefonoProveedor.getText().trim());
            
            String correo = Gestionar_Proveedor.txtEmailProveedor.getText().trim() + "@gmail.com";
            p.setEmail(correo);

            p.setRFC(Gestionar_Proveedor.txtRFCProveedor.getText().trim());
            p.setDireccion(Gestionar_Proveedor.txtDireccionProveedor.getText().trim());

            pro.ActualizarProveedor(p);
           
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al actualizar proveedor: " + ex.getMessage());
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(null, "ID de proveedor inválido: " + nfe.getMessage());
        }
    }
}


