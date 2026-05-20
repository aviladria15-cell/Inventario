package Validacion;

import Vista_Usuari_Empleado.frm_GestionarEmpleado;
import java.util.Date;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;
import Modelo.Empleado;
import Modelo.Usuario;
import ModeloDAO.EmpleadoDAO;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.time.ZoneId;
import Vista_Usuari_Empleado.AsignarUsuario;
import ModeloDAO.UsurioDAO;
import java.time.Period;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class Validar_Formulario_Empleado extends EmpleadoDAO {

    EmpleadoDAO E = new EmpleadoDAO();
    UsurioDAO U = new  UsurioDAO();
    
public void inicializarValidaciones() {
    // --- Ocultar todas las etiquetas de error al inicio ---
    ocultarError(frm_GestionarEmpleado.lblValidarNombre);
    ocultarError(frm_GestionarEmpleado.lblValidarApellido);
    ocultarError(frm_GestionarEmpleado.lblValidarCedula);
    ocultarError(frm_GestionarEmpleado.lblValidarTelefono);
    ocultarError(frm_GestionarEmpleado.lblValidarCorreo);
    ocultarError(frm_GestionarEmpleado.lblValidarFecha);

    // --- APLICAR FILTROS DE ENTRADA (bloquean caracteres no permitidos) ---
    aplicarFiltroNombreApellido(frm_GestionarEmpleado.txtNombre);
    aplicarFiltroNombreApellido(frm_GestionarEmpleado.txtApellido);
    aplicarFiltroCedula(frm_GestionarEmpleado.txtCedula);
    aplicarFiltroTelefono(frm_GestionarEmpleado.txtTelefono);

    // --- VALIDACIÓN EN TIEMPO REAL (muestra mensajes de error) ---
    frm_GestionarEmpleado.txtNombre.getDocument().addDocumentListener(new SimpleDocumentListener(this::validarNombre));
    frm_GestionarEmpleado.txtApellido.getDocument().addDocumentListener(new SimpleDocumentListener(this::validarApellido));
    frm_GestionarEmpleado.txtCedula.getDocument().addDocumentListener(new SimpleDocumentListener(this::validarCedula));
    frm_GestionarEmpleado.txtTelefono.getDocument().addDocumentListener(new SimpleDocumentListener(this::validarTelefono));

    // --- Correo: ya tienes fijarCorreoGmail(), lo mantenemos ---
    fijarCorreoGmail();

    // --- Fecha de nacimiento ---
    try {
        if (frm_GestionarEmpleado.jDateNacimiento != null && frm_GestionarEmpleado.jDateNacimiento.getDateEditor() != null) {
            frm_GestionarEmpleado.jDateNacimiento.getDateEditor().addPropertyChangeListener("date", evt -> validarFechaNacimiento());
        }
    } catch (Exception ex) {
        
    }
}




// Filtro: Nombre y Apellido (solo letras, espacios, acentos, ñ)
private void aplicarFiltroNombreApellido(JTextField campo) {
    ((AbstractDocument) campo.getDocument()).setDocumentFilter(new DocumentFilter() {
        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            if (string != null && string.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]*")) {
                super.insertString(fb, offset, string, attr);
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (text != null && text.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]*")) {
                super.replace(fb, offset, length, text, attrs);
            }
        }
    });
}

// Filtro: Cédula (solo números, máx 8 dígitos)
private void aplicarFiltroCedula(JTextField campo) {
    ((AbstractDocument) campo.getDocument()).setDocumentFilter(new DocumentFilter() {
        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            String nuevoTexto = construirTexto(fb, offset, length, text);
            if (nuevoTexto.matches("\\d*") && nuevoTexto.length() <= 8) {
                super.replace(fb, offset, length, text, attrs);
            }
        }

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            String nuevoTexto = construirTexto(fb, offset, 0, string);
            if (nuevoTexto.matches("\\d*") && nuevoTexto.length() <= 8) {
                super.insertString(fb, offset, string, attr);
            }
        }

        private String construirTexto(FilterBypass fb, int offset, int length, String text) throws BadLocationException {
            String actual = fb.getDocument().getText(0, fb.getDocument().getLength());
            return actual.substring(0, offset) + text + actual.substring(offset + length);
        }
    });
}

    // ------------------- CORREO -------------------
    private void fijarCorreoGmail() {
        frm_GestionarEmpleado.txtEmail.setText("");

        AbstractDocument doc = (AbstractDocument) frm_GestionarEmpleado.txtEmail.getDocument();
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
                    SwingUtilities.invokeLater(() -> frm_GestionarEmpleado.txtEmail.setCaretPosition(pos));
                }
            }

            private void validarCorreoTexto(FilterBypass fb) throws BadLocationException {
                String text = fb.getDocument().getText(0, fb.getDocument().getLength());
                String nombre = text.replace("@gmail.com", "");
                if (nombre.isEmpty()) {
                    frm_GestionarEmpleado.lblValidarCorreo.setText("Debes escribir la dirreción del correo");
                    frm_GestionarEmpleado.lblValidarCorreo.setVisible(true);
                } else if (!nombre.matches("^[a-zA-Z0-9._-]+$")) {
                    frm_GestionarEmpleado.lblValidarCorreo.setText("Dirección del no válido para correo");
                    frm_GestionarEmpleado.lblValidarCorreo.setVisible(true);
                } else {
                    frm_GestionarEmpleado.lblValidarCorreo.setVisible(false);
                }
            }
        });
    }

    private void validarCorreo() {
        String correo = frm_GestionarEmpleado.txtEmail.getText().trim();
        if (correo.isEmpty() || correo.equals("@gmail.com")) {
            frm_GestionarEmpleado.lblValidarCorreo.setText("La dirreción del correo no puede estar vacío");
            frm_GestionarEmpleado.lblValidarCorreo.setVisible(true);
        } else if (!correo.matches("^[a-zA-Z0-9._-]+@gmail\\.com$")) {
            frm_GestionarEmpleado.lblValidarCorreo.setText("Dirreción del no válido (debe ser @gmail.com)");
            frm_GestionarEmpleado.lblValidarCorreo.setVisible(true);
        } else {
            frm_GestionarEmpleado.lblValidarCorreo.setVisible(false);
        }
    }


// Filtro: Teléfono (solo números, empieza con 04, máx 11 dígitos)
private void aplicarFiltroTelefono(JTextField campo) {
    ((AbstractDocument) campo.getDocument()).setDocumentFilter(new DocumentFilter() {
        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            String nuevoTexto = construirTexto(fb, offset, length, text);
            if (esValidoTelefono(nuevoTexto)) {
                super.replace(fb, offset, length, text, attrs);
            }
        }

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            String nuevoTexto = construirTexto(fb, offset, 0, string);
            if (esValidoTelefono(nuevoTexto)) {
                super.insertString(fb, offset, string, attr);
            }
        }

        private String construirTexto(FilterBypass fb, int offset, int length, String text) throws BadLocationException {
            String actual = fb.getDocument().getText(0, fb.getDocument().getLength());
            return actual.substring(0, offset) + text + actual.substring(offset + length);
        }

        private boolean esValidoTelefono(String texto) {
            if (!texto.matches("\\d*")) return false;
            if (texto.isEmpty()) return true;
            if (texto.length() > 11) return false;
            return texto.startsWith("04") || texto.equals("0") || texto.equals("04");
        }
    });
}
    
    
    
    private void validarNombre() {
    String nombre = frm_GestionarEmpleado.txtNombre.getText().trim();
    if (nombre.isEmpty()) {
        mostrarError(frm_GestionarEmpleado.lblValidarNombre, "El Nombre no puede estar vacío");
    } else {
        ocultarError(frm_GestionarEmpleado.lblValidarNombre);
    }
}

private void validarApellido() {
    String apellido = frm_GestionarEmpleado.txtApellido.getText().trim();
    if (apellido.isEmpty()) {
        mostrarError(frm_GestionarEmpleado.lblValidarApellido, "El Apellido no puede estar vacío");
    } else {
        ocultarError(frm_GestionarEmpleado.lblValidarApellido);
    }
}

private void validarCedula() {
    String cedula = frm_GestionarEmpleado.txtCedula.getText().trim();
    if (cedula.isEmpty()) {
        mostrarError(frm_GestionarEmpleado.lblValidarCedula, "La cédula no puede estar vacía");
    } else if (cedula.length() < 7) {
        mostrarError(frm_GestionarEmpleado.lblValidarCedula, "Mínimo 7 dígitos");
    } else if (cedula.length() > 8) {
        mostrarError(frm_GestionarEmpleado.lblValidarCedula, "Máximo 8 dígitos");
    } else {
        ocultarError(frm_GestionarEmpleado.lblValidarCedula);
    }
}

private void validarTelefono() {
    String telefono = frm_GestionarEmpleado.txtTelefono.getText().trim();
    if (telefono.isEmpty()) {
        mostrarError(frm_GestionarEmpleado.lblValidarTelefono, "El teléfono no puede estar vacío");
    } else if (!telefono.startsWith("04")) {
        mostrarError(frm_GestionarEmpleado.lblValidarTelefono, "Debe comenzar con 04");
    } else if (telefono.length() != 11) {
        mostrarError(frm_GestionarEmpleado.lblValidarTelefono, "Debe tener 11 dígitos");
    } else {
        ocultarError(frm_GestionarEmpleado.lblValidarTelefono);
    }
}

private void validarFechaNacimiento() {
    Date fecha = frm_GestionarEmpleado.jDateNacimiento.getDate();
    
    if (fecha == null) {
        mostrarError(frm_GestionarEmpleado.lblValidarFecha, "Seleccione una fecha de nacimiento");
        return;
    }

    LocalDate fechaNacimiento = fecha.toInstant()
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate();

    int edad = Period.between(fechaNacimiento, LocalDate.now()).getYears();

    if (edad < 18) {
        mostrarError(frm_GestionarEmpleado.lblValidarFecha, "Debe tener al menos 18 años para registrarse");
    } else {
        ocultarError(frm_GestionarEmpleado.lblValidarFecha);
    }
}

// Métodos auxiliares para etiquetas
private void mostrarError(JLabel label, String mensaje) {
    label.setText(mensaje);
    label.setVisible(true);
}

private void ocultarError(JLabel label) {
    label.setVisible(false);
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

  // ------------------- VALIDAR TODO -------------------
    public boolean validarFormulario() {
        validarNombre();
        validarApellido();
        validarCedula();
        validarTelefono();
        validarFechaNacimiento();
        validarCorreo();

        return !frm_GestionarEmpleado.lblValidarNombre.isVisible()
                && !frm_GestionarEmpleado.lblValidarApellido.isVisible()
                && !frm_GestionarEmpleado.lblValidarCedula.isVisible()
                && !frm_GestionarEmpleado.lblValidarTelefono.isVisible()
                && !frm_GestionarEmpleado.lblValidarCorreo.isVisible()
                && !frm_GestionarEmpleado.lblValidarFecha.isVisible();
    }

  // ------------------- REGISTRAR EMPLEADO -------------------
public boolean RegistrarEmpleadoValidado() throws ClassNotFoundException, SQLException {
    
    if (!validarFormulario()) {
        JOptionPane.showMessageDialog(null, "Corrige los campos marcados antes de registrar.");
        return false;                    // ← Falló validación
    }

    Empleado e = new Empleado();
    e.setNombre(frm_GestionarEmpleado.txtNombre.getText().trim());
    e.setApellido(frm_GestionarEmpleado.txtApellido.getText().trim());
    e.setCedula(frm_GestionarEmpleado.txtCedula.getText().trim());
    e.setTelefono(frm_GestionarEmpleado.txtTelefono.getText().trim());
    e.setEmail(frm_GestionarEmpleado.txtEmail.getText().trim());
    e.setCargo((String) frm_GestionarEmpleado.ComboxCargo.getSelectedItem());

    // Fecha de nacimiento
    Date fechaSeleccionada = frm_GestionarEmpleado.jDateNacimiento.getDate();
    if (fechaSeleccionada == null) {
        frm_GestionarEmpleado.lblValidarFecha.setText("Debe seleccionar una fecha");
        frm_GestionarEmpleado.lblValidarFecha.setVisible(true);
        return false;                    // ← Falló fecha
    }

    java.sql.Date sqlDate = new java.sql.Date(fechaSeleccionada.getTime());
    e.setFechaNacimiento(sqlDate);

    // Guardar en BD
    try {
        E.GuardarEmpleado(e);
        return true;                     // ← ¡ÉXITO! Todo correcto

    } catch (Exception ex) {
        JOptionPane.showMessageDialog(null, "Error al guardar empleado: " + ex.getMessage());
        return false;                    // ← Falló al guardar
    }
}
    
    
     // ------------------- REGISTRAR EMPLEADO -------------------
    public void ActualizarEmpleadoValidadoActualizar(frm_GestionarEmpleado P) throws ClassNotFoundException, SQLException {
        if (!validarFormulario()) {
            JOptionPane.showMessageDialog(null, "Corrige los campos marcados antes de actualizar el empleado.");
            return;
        }

        Empleado e = new Empleado();
        e.setNombre(frm_GestionarEmpleado.txtNombre.getText().trim());
        e.setApellido(frm_GestionarEmpleado.txtApellido.getText().trim());
        e.setCedula(frm_GestionarEmpleado.txtCedula.getText().trim());
        e.setTelefono(frm_GestionarEmpleado.txtTelefono.getText().trim());
        e.setEmail(frm_GestionarEmpleado.txtEmail.getText().trim());
        e.setCargo((String) frm_GestionarEmpleado.ComboxCargo.getSelectedItem());

        // --- Convertir la fecha seleccionada a java.sql.Date y asignarla al empleado ---
        Date fechaSeleccionada = frm_GestionarEmpleado.jDateNacimiento.getDate();
        if (fechaSeleccionada == null) {
            frm_GestionarEmpleado.lblValidarFecha.setText("Debe seleccionar una fecha");
            frm_GestionarEmpleado.lblValidarFecha.setVisible(true);
            return;
        }
        java.sql.Date sqlDate = new java.sql.Date(fechaSeleccionada.getTime());
        // e.setFechaNacimiento debe aceptar java.util.Date o java.sql.Date
        e.setFechaNacimiento(sqlDate);

        // Guardar en BD y capturar errores
        try {
            E.ActualizarEmpleado(e);
            
        
          
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al  Actualizar empleado: " + ex.getMessage());
        } }
        
        
    
    
    



   // ------------------- INICIALIZAR VALIDACIONES DE ASIGNACIÓN DE USUARIO -------------------
public void inicializarValidacionesAsignarUsuario() {
    // Ocultar etiquetas de error al iniciar
    AsignarUsuario.lblNombreUsuario.setVisible(false);
    AsignarUsuario.lblNombreClave.setVisible(false);
    AsignarUsuario.lblNombreNivelAcceso.setVisible(false);

    // ------------------- LISTENERS PARA CAMPOS -------------------
    // Nombre de Usuario
    AsignarUsuario.txtNombreUsuario.getDocument().addDocumentListener(new DocumentListener() {
        public void insertUpdate(DocumentEvent e) { validarNombreUsuario(); }
        public void removeUpdate(DocumentEvent e) { validarNombreUsuario(); }
        public void changedUpdate(DocumentEvent e) { validarNombreUsuario(); }
    });

    // Clave
    AsignarUsuario.txtClave.getDocument().addDocumentListener(new DocumentListener() {
        public void insertUpdate(DocumentEvent e) { validarClave(); }
        public void removeUpdate(DocumentEvent e) { validarClave(); }
        public void changedUpdate(DocumentEvent e) { validarClave(); }
    });

    // Nivel de Acceso (ComboBox)
    AsignarUsuario.ComboxAsignarUsuario.addActionListener(e -> validarNivelAcceso());
}

// ------------------- VALIDACIONES DE CAMPOS -------------------

// Nombre de Usuario → solo letras, 5 a 20 caracteres
private void validarNombreUsuario() {
    String nombreUsuario = AsignarUsuario.txtNombreUsuario.getText().trim();

    if (nombreUsuario.isEmpty()) {
        AsignarUsuario.lblNombreUsuario.setText("El nombre de usuario no puede estar vacío");
        AsignarUsuario.lblNombreUsuario.setVisible(true);
    } else if (!nombreUsuario.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ]+$")) {
        AsignarUsuario.lblNombreUsuario.setText("Solo se permiten letras");
        AsignarUsuario.lblNombreUsuario.setVisible(true);
    } else if (nombreUsuario.length() < 5 || nombreUsuario.length() > 20) {
        AsignarUsuario.lblNombreUsuario.setText("Debe tener entre 5 y 20 letras");
        AsignarUsuario.lblNombreUsuario.setVisible(true);
    } else {
        AsignarUsuario.lblNombreUsuario.setVisible(false);
    }
}


// Clave → validación paso a paso, solo muestra el PRIMER error
private void validarClave() {
    String clave = AsignarUsuario.txtClave.getText().trim();

    // 1. ¿Vacía?
    if (clave.isEmpty()) {
        AsignarUsuario.lblNombreClave.setText("La clave no puede estar vacía");
        AsignarUsuario.lblNombreClave.setVisible(true);
        return;
    }

    // 2. ¿Longitud entre 8 y 20?
    if (clave.length() < 8) {
        AsignarUsuario.lblNombreClave.setText("Mínimo 8 caracteres");
        AsignarUsuario.lblNombreClave.setVisible(true);
        return;
    }
    if (clave.length() > 20) {
        AsignarUsuario.lblNombreClave.setText("Máximo 20 caracteres");
        AsignarUsuario.lblNombreClave.setVisible(true);
        return;
    }

    // 3. ¿Tiene al menos 1 mayúscula?
    if (!clave.matches(".*[A-Z].*")) {
        AsignarUsuario.lblNombreClave.setText("Falta al menos 1 MAYÚSCULA");
        AsignarUsuario.lblNombreClave.setVisible(true);
        return;
    }

    // 4. ¿Tiene al menos 1 minúscula?
    if (!clave.matches(".*[a-z].*")) {
        AsignarUsuario.lblNombreClave.setText("Falta al menos 1 minúscula");
        AsignarUsuario.lblNombreClave.setVisible(true);
        return;
    }

    // 5. ¿Tiene al menos 1 número?
    if (!clave.matches(".*\\d.*")) {
        AsignarUsuario.lblNombreClave.setText("Falta al menos 1 NÚMERO");
        AsignarUsuario.lblNombreClave.setVisible(true);
        return;
    }

    // 6. ¿Tiene al menos 1 carácter especial?
    if (!clave.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")) {
        AsignarUsuario.lblNombreClave.setText("Falta 1 símbolo (!@#$%^&* etc.)");
        AsignarUsuario.lblNombreClave.setVisible(true);
        return;
    }

    // Si pasa todo → ocultar error
    AsignarUsuario.lblNombreClave.setVisible(false);
}


// Nivel de acceso → obligatorio
private void validarNivelAcceso() {
    String nivelAcceso = AsignarUsuario.ComboxAsignarUsuario.getSelectedItem() != null
            ? AsignarUsuario.ComboxAsignarUsuario.getSelectedItem().toString().trim()
            : "";

    if (nivelAcceso.isEmpty() || nivelAcceso.equals("Seleccione") || nivelAcceso.equalsIgnoreCase("Seleccione un nivel")) {
        AsignarUsuario.lblNombreNivelAcceso.setText("Debe seleccionar un nivel de acceso");
        AsignarUsuario.lblNombreNivelAcceso.setVisible(true);
    } else {
        AsignarUsuario.lblNombreNivelAcceso.setVisible(false);
    }
}

// ------------------- VALIDAR TODO EL FORMULARIO -------------------
public boolean validarFormularioAsignarUsuario() {
    validarNombreUsuario();
    validarClave();
    validarNivelAcceso();

    return !AsignarUsuario.lblNombreUsuario.isVisible()
            && !AsignarUsuario.lblNombreClave.isVisible()
            && !AsignarUsuario.lblNombreNivelAcceso.isVisible();
}

// ------------------- REGISTRAR USUARIO VALIDADO -------------------
public boolean registrarUsuarioValidado(AsignarUsuario AA) {
    
    if (!validarFormularioAsignarUsuario()) {
        JOptionPane.showMessageDialog(null, "Corrige los campos marcados antes de registrar.");
        return false;
    }

    Usuario u = new Usuario();
    u.setNombreUsuario(AsignarUsuario.txtNombreUsuario.getText().trim());
    u.setClave(AsignarUsuario.txtClave.getText().trim());
    u.setNivel_Acceso(AsignarUsuario.ComboxAsignarUsuario.getSelectedItem().toString());

    try {
        U.GuardarUsuario(u);
       
        
        return true;                     // Éxito

    } catch (Exception ex) {
        JOptionPane.showMessageDialog(null, "Error al registrar usuario: " + ex.getMessage());
        return false;                    // Falló
    }
}
 
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
