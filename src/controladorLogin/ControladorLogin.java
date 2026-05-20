package controladorLogin;

import Controlador.controladorVisual;
import java.sql.*;
import Modelo.*;
import Vista_Usuari_Empleado.Login_Ingreso;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.mindrot.jbcrypt.BCrypt;
import Vista_Usuari_Empleado.Menu_Sistema;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControladorLogin {
    

    ConexiónBD C = new ConexiónBD(); // Usa tu clase de conexión
 

    
    
    public void BtbIniciarActionPerformed( Login_Ingreso L) throws SQLException {
        // VALIDAR ANTES DE INTENTAR AUTENTICAR
        if (!validarLogin()) {
            return; // Si falla validación, no sigue ni muestra errores duplicados
        }

        String resultado = autenticarUsuario();
        switch (resultado) {
            case "activo":
                try {
                    C.conectar();
                    // 1. Obtener menú
                    Menu_Sistema menu = Menu_Sistema.getInstancia();
                    // 2. APLICAR RESTRICCIONES DESDE EL CONTROLADOR
                    aplicarRestriccionesEnMenu(menu);
                    // 3. Mostrar
                    menu.setVisible(true);
                    
                    Menu_Sistema.lblUsuarioENLinea.setText(Usuario.usuarioActual);
                    L.dispose();
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ControladorLogin.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;

            case "no_existe":
                JOptionPane.showMessageDialog(null,
                        "Usuario no registrado o credenciales incorrectas.",
                        "Error de autenticación",
                        JOptionPane.ERROR_MESSAGE);
                break;

            case "inactivo":
                JOptionPane.showMessageDialog(null,
                        "Este usuario se encuentra inactivo y no tiene permitido ingresar al sistema.",
                        "Usuario inactivo",
                        JOptionPane.WARNING_MESSAGE);
                break;

            case "error":
                JOptionPane.showMessageDialog(null,
                        "Ocurrió un error al intentar iniciar sesión. Intente nuevamente.",
                        "Error de conexión",
                        JOptionPane.ERROR_MESSAGE);
                break;
        }
    }

    // -----------------------------
    // VALIDACIONES DE LOGIN (devuelve false y muestra solo UN mensaje)
    // -----------------------------
    private boolean validarLogin() {
        String usuario = Login_Ingreso.txtUsuario.getText().trim();
        String contraseña = Login_Ingreso.txtPasContraseña.getText().trim();

        if (usuario.isEmpty()) {
            JOptionPane.showMessageDialog(null, 
                    "El campo Usuario es requerido.", 
                    "Validación", 
                    JOptionPane.WARNING_MESSAGE);
            Login_Ingreso.txtUsuario.requestFocus();
            return false;
        }
        if (!usuario.matches("[a-zA-Z]+")) {
            JOptionPane.showMessageDialog(null, 
                    "El usuario solo puede contener letras (A-Z).", 
                    "Validación", 
                    JOptionPane.WARNING_MESSAGE);
            Login_Ingreso.txtUsuario.requestFocus();
            return false;
        }
        if (contraseña.isEmpty()) {
            JOptionPane.showMessageDialog(null, 
                    "El campo Contraseña es requerido.", 
                    "Validación", 
                    JOptionPane.WARNING_MESSAGE);
            Login_Ingreso.txtPasContraseña.requestFocus();
            return false;
        }
        if (!contraseña.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")) {
            JOptionPane.showMessageDialog(null, 
                    "Formato de contraseña incorrecto", 
                    "Validación", 
                    JOptionPane.WARNING_MESSAGE);
            Login_Ingreso.txtPasContraseña.requestFocus();
            return false;
        }
        return true;
    }

    // -----------------------------
    // AUTENTICACIÓN EN BD (solo muestra mensajes internos, no duplicados)
    // -----------------------------
    private String autenticarUsuario() {
        String nombreUsuario = Login_Ingreso.txtUsuario.getText().trim();
        String claveIngresada = Login_Ingreso.txtPasContraseña.getText().trim();

        String sql = "SELECT Clave, estado, nivel_acceso FROM usuario WHERE NombreUsuario = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            C.conectar();
            ps = C.getConnection().prepareStatement(sql);
            ps.setString(1, nombreUsuario);
            rs = ps.executeQuery();

            if (rs.next()) {
                String hashAlmacenado = rs.getString("Clave");
                String estado = rs.getString("estado");
                String nivel = rs.getString("nivel_acceso");

                // VERIFICAR CONTRASEÑA
                boolean contraseñaCorrecta = BCrypt.checkpw(claveIngresada, hashAlmacenado);
                if (!contraseñaCorrecta) {
                    return "no_existe"; // Se muestra mensaje único en BtbIniciarActionPerformed
                }

                // VERIFICAR ESTADO
                if (estado != null && estado.equalsIgnoreCase("Inactivo")) {
                    return "inactivo"; // Mensaje único en switch
                }

                // ÉXITO
                Usuario.usuarioActual = nombreUsuario;
                Usuario.nivelActual = (nivel != null) ? nivel.trim() : "Bajo";
               
                
                return "activo";

            } else {
                return "no_existe"; // Mensaje único en switch
            }

        } catch (SQLException e) {
            // Solo regresamos error, el mensaje se muestra en el switch
            return "error";
        } catch (ClassNotFoundException e) {
            return "error";
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                C.cerrarCn();
            } catch (SQLException ex) {
                // Silencioso
            }
        }
    }

    // -----------------------------
    // RESTRICCIONES POR NIVEL
    // -----------------------------
    public void aplicarRestriccionesEnMenu(Menu_Sistema menu) {
        String nivel = Usuario.nivelActual;
        if (nivel == null || nivel.trim().isEmpty()) {
            nivel = "Bajo";
        }
        nivel = nivel.trim().toUpperCase();

        // HABILITAR TODOS POR DEFECTO
      //  menu.btbGestionarMarca.setEnabled(true);
        //menu.btbGestionarProducto.setEnabled(true);
        menu.JMenu_Iniciar_Almacen.setEnabled(true);
        
        menu.Jmenu_Abri_Empleado.setEnabled(true);
        menu.Menu_inicial_proveedor.setEnabled(true);
        menu.jMenu_Inventario.setEnabled(true);
        menu.Menu_Reportes_Sistema.setEnabled(true);
       menu.jMenuLibroContable.setEnabled(true);
        menu.MenuCategoria.setEnabled(true);

        // RESTRINGIR SEGÚN NIVEL
        if (!(nivel.equals("DUEÑO") || nivel.equals("EMPLEADO") || nivel.equals("ALTO"))) {
            menu.Menu_Inicial_Empleado.setEnabled(false);
            menu.Menu_inicial_proveedor.setEnabled(false);
            menu.jMenu_Inventario.setEnabled(false);
            menu.Menu_Reportes_Sistema.setEnabled(false);
            menu.jMenuLibroContable.setEnabled(false);
            
        }
    }

  
   
}