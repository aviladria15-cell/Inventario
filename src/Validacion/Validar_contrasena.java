/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Validacion;


import  Vista_Usuari_Empleado.RecuperarContrasena;

import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import javax.swing.JOptionPane;


import ModeloDAO.UsurioDAO;
import Vista_Usuari_Empleado.AsignarUsuario;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JTextField;

public class Validar_contrasena {
    
    
UsurioDAO Usu = new UsurioDAO();
    
    public  void IniciarValidacion (){
        
        
 
       

        
         RecuperarContrasena.txtNombreUsuarioRecuperar.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { ValidarUsuarioRecupera(); }
            public void removeUpdate(DocumentEvent e) { ValidarUsuarioRecupera(); }
            public void changedUpdate(DocumentEvent e) { ValidarUsuarioRecupera(); }
        });
        
        
        
        
        fijarCorreoGmail();
        
        
        
        
        
    }
    
    
    
  // ------------------- CORREO -------------------
    private void fijarCorreoGmail() {
        RecuperarContrasena.txtCorreoRecuperar.setText("");

        AbstractDocument doc = (AbstractDocument) RecuperarContrasena.txtCorreoRecuperar.getDocument();
        doc.setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(DocumentFilter.FilterBypass fb, int offset, String string, AttributeSet attr)
                    throws BadLocationException {
                super.insertString(fb, offset, string, attr);
                agregarDominio(fb);
                validarCorreoTexto(fb);
            }

            @Override
            public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                    throws BadLocationException {
                super.replace(fb, offset, length, text, attrs);
                agregarDominio(fb);
                validarCorreoTexto(fb);
            }

            @Override
            public void remove(DocumentFilter.FilterBypass fb, int offset, int length)
                    throws BadLocationException {
                super.remove(fb, offset, length);
                agregarDominio(fb);
                validarCorreoTexto(fb);
            }

            private void agregarDominio(DocumentFilter.FilterBypass fb) throws BadLocationException {
                String text = fb.getDocument().getText(0, fb.getDocument().getLength());
                if (!text.endsWith("@gmail.com")) {
                    text = text.replace("@gmail.com", "");
                    fb.replace(0, fb.getDocument().getLength(), text + "@gmail.com", null);
                    int pos = text.length();
                    SwingUtilities.invokeLater(() -> RecuperarContrasena.txtCorreoRecuperar.setCaretPosition(pos));
                }
            }

            private void validarCorreoTexto(DocumentFilter.FilterBypass fb) throws BadLocationException {
                String text = fb.getDocument().getText(0, fb.getDocument().getLength());
                String nombre = text.replace("@gmail.com", "");
                if (nombre.isEmpty()) {
                    RecuperarContrasena.lblValidarCorreo.setText("Debes escribir la dirreción del correo");
                    RecuperarContrasena.lblValidarCorreo.setVisible(true);
                } else if (!nombre.matches("^[a-zA-Z0-9._-]+$")) {
                    RecuperarContrasena.lblValidarCorreo.setText("Dirección del no válido para correo");
                    RecuperarContrasena.lblValidarCorreo.setVisible(true);
                } else {
                    RecuperarContrasena.lblValidarCorreo.setVisible(false);
                }
            }
        });
    }

    private void validarCorreo() {
        String correo = RecuperarContrasena.txtCorreoRecuperar.getText().trim();
        if (correo.isEmpty() || correo.equals("@gmail.com")) {
            RecuperarContrasena.lblValidarCorreo.setText("La dirreción del correo no puede estar vacío");
            RecuperarContrasena.lblValidarCorreo.setVisible(true);
        } else if (!correo.matches("^[a-zA-Z0-9._-]+@gmail\\.com$")) {
            RecuperarContrasena.lblValidarCorreo.setText("Dirreción del no válido (debe ser @gmail.com)");
            RecuperarContrasena.lblValidarCorreo.setVisible(true);
        } else {
            RecuperarContrasena.lblValidarCorreo.setVisible(false);
        }
    }



    private void ValidarUsuarioRecupera(){
        String nombre = RecuperarContrasena.txtNombreUsuarioRecuperar.getText().trim();
        if (nombre.isEmpty()) {
            RecuperarContrasena.lblValidarNombre.setText("El Nombre del usuario no puede estar vacío");
            RecuperarContrasena.lblValidarNombre.setVisible(true);
        } else if (!nombre.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$")) {
            RecuperarContrasena.lblValidarNombre.setText("Solo se permiten letras");
            RecuperarContrasena.lblValidarNombre.setVisible(true);
        } else {
            RecuperarContrasena.lblValidarNombre.setVisible(false);
        }
    
    
    
    
    
    
} 

    
    
    public  boolean  ValidarFormulario() {
        
        
        ValidarUsuarioRecupera();
        validarCorreo();
        
        return !RecuperarContrasena.lblValidarCorreo.isVisible() && !RecuperarContrasena.lblValidarNombre.isVisible();
        
        
    }
    
    
    public  void EnviarCorreoVALIDACION() throws ClassNotFoundException{
        
        if (!ValidarFormulario()) {
            
            JOptionPane.showMessageDialog(null, "Corrige los campoas para enviar el codigo de verificacion");
            return;
            
        }
        
        
       Usu.VerificarUsuario();
        
        
    }
    
    
    
    
   public void InicializarVerificacionCodigo() {
    // TU CÓDIGO ACTUAL (DocumentListener)
    RecuperarContrasena.txtCodigoDeVerificacion.getDocument().addDocumentListener(new DocumentListener() {
        public void insertUpdate(DocumentEvent e) { CodigoVerificacion(); }
        public void removeUpdate(DocumentEvent e) { CodigoVerificacion(); }
        public void changedUpdate(DocumentEvent e) { CodigoVerificacion(); }
    });

    // NUEVO: Bloquear entrada si ya hay 6 dígitos
    RecuperarContrasena.txtCodigoDeVerificacion.addKeyListener(new KeyAdapter() {
        @Override
        public void keyTyped(KeyEvent e) {
            char c = e.getKeyChar();
            String texto = RecuperarContrasena.txtCodigoDeVerificacion.getText();

            // Si ya hay 6 dígitos → bloquear cualquier tecla
            if (texto.length() >= 6) {
                e.consume(); // Ignora la tecla
                return;
            }

            // Solo permitir números (0-9)
            if (!Character.isDigit(c)) {
                e.consume(); // Ignora letras, símbolos, etc.
            }
        }
    });

    // Opcional: Centrar texto y limitar columnas
    RecuperarContrasena.txtCodigoDeVerificacion.setHorizontalAlignment(JTextField.CENTER);
}
    
    
    
    
 private void CodigoVerificacion() {
    String codigo = RecuperarContrasena.txtCodigoDeVerificacion.getText().trim();

    // 1. ¿Está vacío?
    if (codigo.isEmpty()) {
        RecuperarContrasena.lblVerificarCodigo.setText("El código no puede estar vacío");
        RecuperarContrasena.lblVerificarCodigo.setVisible(true);
        return;
    }

    // 2. ¿Tiene exactamente 6 dígitos?
    if (codigo.length() != 6) {
        RecuperarContrasena.lblVerificarCodigo.setText("Debe tener exactamente 6 dígitos");
        RecuperarContrasena.lblVerificarCodigo.setVisible(true);
        return;
    }

    // 3. ¿Son solo números? (por seguridad, aunque ya está bloqueado)
    if (!codigo.matches("\\d{6}")) {
        RecuperarContrasena.lblVerificarCodigo.setText("Solo se permiten números");
        RecuperarContrasena.lblVerificarCodigo.setVisible(true);
        return;
    }

    // Si todo está bien → ocultar error
    RecuperarContrasena.lblVerificarCodigo.setVisible(false);
}
 
 
 
    
    
    
   public  boolean  ValidarCODIGO (){
       
       CodigoVerificacion();
       
       return !RecuperarContrasena.lblVerificarCodigo.isVisible();
       
       
       
       
   }

 public  void Confirmar(){
     
     
     
     if (!ValidarCODIGO()) {
        
         
     }
        
        Usu.ConfirmarCodigo();
    }
 
 
 
 
 public void IniciarValidacionContrasena() {
    RecuperarContrasena.lblValidarContrasena.setVisible(false);
    RecuperarContrasena.lblConfirmarContrasenaValidacion.setVisible(false);

    // Validar mientras escribe en "Nueva contraseña"
    RecuperarContrasena.txtNuevaContrase.getDocument().addDocumentListener(new DocumentListener() {
        public void insertUpdate(DocumentEvent e) { validarClaveL(); }
        public void removeUpdate(DocumentEvent e) { validarClaveL(); }
        public void changedUpdate(DocumentEvent e) { validarClaveL(); }
    });

    // 🔥 NUEVO: validar también cuando cambia el campo "Confirmar contraseña"
    RecuperarContrasena.txtConfirmarContrsena.getDocument().addDocumentListener(new DocumentListener() {
        public void insertUpdate(DocumentEvent e) { validarClaveL(); }
        public void removeUpdate(DocumentEvent e) { validarClaveL(); }
        public void changedUpdate(DocumentEvent e) { validarClaveL(); }
    });
}

 
 
 private void validarClaveL() {
    // Ocultar mensajes antes de cada validación
    RecuperarContrasena.lblValidarContrasena.setVisible(false);
    RecuperarContrasena.lblConfirmarContrasenaValidacion.setVisible(false);

    String clave = RecuperarContrasena.txtNuevaContrase.getText().trim();
    String confirma = RecuperarContrasena.txtConfirmarContrsena.getText().trim();

    // 1️⃣ Campo "Nueva contraseña" vacío
    if (clave.isEmpty()) {
        RecuperarContrasena.lblValidarContrasena.setText("Debes escribir una nueva contraseña");
        RecuperarContrasena.lblValidarContrasena.setVisible(true);
        return;
    }

    // 2️⃣ Campo "Confirmar contraseña" vacío
    

    // 4️⃣ Longitud
    if (clave.length() < 8) {
        RecuperarContrasena.lblValidarContrasena.setText("Mínimo 8 caracteres");
        RecuperarContrasena.lblValidarContrasena.setVisible(true);
        return;
    }
    if (clave.length() > 20) {
        RecuperarContrasena.lblValidarContrasena.setText("Máximo 20 caracteres");
        RecuperarContrasena.lblValidarContrasena.setVisible(true);
        return;
    }

    // 5️⃣ Mayúscula
    if (!clave.matches(".*[A-Z].*")) {
        RecuperarContrasena.lblValidarContrasena.setText("Debe contener al menos 1 MAYÚSCULA");
        RecuperarContrasena.lblValidarContrasena.setVisible(true);
        return;
    }

    // 6️⃣ Minúscula
    if (!clave.matches(".*[a-z].*")) {
        RecuperarContrasena.lblValidarContrasena.setText("Debe contener al menos 1 minúscula");
        RecuperarContrasena.lblValidarContrasena.setVisible(true);
        return;
    }

    // 7️⃣ Número
    if (!clave.matches(".*\\d.*")) {
        RecuperarContrasena.lblValidarContrasena.setText("Debe contener al menos 1 número");
        RecuperarContrasena.lblValidarContrasena.setVisible(true);
        return;
    }

    // 8️⃣ Símbolo
    if (!clave.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")) {
        RecuperarContrasena.lblValidarContrasena.setText("Debe contener al menos 1 símbolo (!@#$%^&* etc.)");
        RecuperarContrasena.lblValidarContrasena.setVisible(true);
        return;
    }
if (confirma.isEmpty()) {
        RecuperarContrasena.lblConfirmarContrasenaValidacion.setText("Debes confirmar la contraseña");
        RecuperarContrasena.lblConfirmarContrasenaValidacion.setVisible(true);
        return;
    }

    // 3️⃣ No coinciden
    if (!clave.equals(confirma)) {
        RecuperarContrasena.lblConfirmarContrasenaValidacion.setText("Las contraseñas no coinciden");
        RecuperarContrasena.lblConfirmarContrasenaValidacion.setVisible(true);
        return;
    }
    // ✅ Todo correcto → ocultar ambos mensajes
    RecuperarContrasena.lblValidarContrasena.setVisible(false);
    RecuperarContrasena.lblConfirmarContrasenaValidacion.setVisible(false);
}

 
 
 public boolean ValidarConytrsenaNueva() {
    validarClaveL();
    return !RecuperarContrasena.lblValidarContrasena.isVisible() 
        && !RecuperarContrasena.lblConfirmarContrasenaValidacion.isVisible(); // ← AMBOS
}

public void NuevaContrasena(RecuperarContrasena ee) throws ClassNotFoundException {
    if (!ValidarConytrsenaNueva()) {
        JOptionPane.showMessageDialog(null, 
            "Corrige los errores antes de actualizar la contraseña.", 
            "Error", JOptionPane.WARNING_MESSAGE);
        return;
    }

    try {
      
        Usu.ActualizarContrase(); // Tu método DAO

        JOptionPane.showMessageDialog(null, 
            "¡Contraseña actualizada con éxito!", 
            "Éxito", JOptionPane.INFORMATION_MESSAGE);
        
        new Vista_Usuari_Empleado.Login_Ingreso().setVisible(true);
         ee.setVisible(false);

   
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(null, 
            "Error al actualizar: " + ex.getMessage(), 
            "Error", JOptionPane.ERROR_MESSAGE);
    }
}
    



}
