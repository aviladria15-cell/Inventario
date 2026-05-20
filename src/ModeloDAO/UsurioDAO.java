
package ModeloDAO;
import java.sql.*;
import Modelo.ConexiónBD;
import Modelo.Usuario;
import Vista_Usuari_Empleado.AsignarUsuario;
import javax.swing.JOptionPane;
import Modelo.Empleado;
import Funciones.Enviar_Codigo_Correo;
import java.time.LocalDateTime;  
import org.mindrot.jbcrypt.BCrypt;
import Vista_Usuari_Empleado.Consultar_Usuario;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;
import  Vista_Usuari_Empleado.RecuperarContrasena;

/**
 *
 * @author Adrian
 */
public class UsurioDAO extends  ConexiónBD{
  Empleado e = new Empleado();
      
  public  static  int idEmpleadoReciente;
      private ResultSet rs;
    private PreparedStatement ps;
  
private int idUsuario = -1;
private String codigoGenerado = null;
private LocalDateTime codigoExpiracion;


   Controlador.controladorVisual ct = new  Controlador.controladorVisual();
     DefaultTableModel modeloUsuario = new DefaultTableModel();
    ModeloDAO.EmpleadoDAO De = new ModeloDAO.EmpleadoDAO();
    
    
    
     public void TituloUsuario (){
   
    String Titulo [] = {"IdUsuario",  "Nombre Usuario", "Nivel de Acceso","Estado","Empleado","Apellido"};
    modeloUsuario.setColumnIdentifiers(Titulo);
 Consultar_Usuario.TablaUsuario.setModel(modeloUsuario);
    
}

 public  void centrarTextoTabla(JTable tabla) {
        DefaultTableCellRenderer centrado = new DefaultTableCellRenderer();
        centrado.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
        for (int i = 0; i < tabla.getColumnCount(); i++) {
            tabla.getColumnModel().getColumn(i).setCellRenderer(centrado);
        }
    }
 
  
private void limpiarTablaUsuario (){
 int fila = modeloUsuario.getRowCount();
    if (fila > 0) {
        for (int i = 0; i < fila; i++) {
            modeloUsuario.removeRow(0);
        }
        
    }
    
}
    
    
    
    private ArrayList ListaUsuario () throws SQLException,ClassNotFoundException {
        
        ArrayList <Usuario> lisUsuario = new ArrayList<>();
        
        String sql = " SELECT \n" +
"            u.IdUsuario,\n" +
"            u.NombreUsuario,\n" +
"            u.Nivel_Acceso,\n" +
"            u.Estado,\n" +
"            e.nombre AS nombreEmpleado,\n" +
"            e.apellido\n "+
"        FROM proyecto.usuario u\n" +
"        INNER JOIN proyecto.empleado e ON u.IdEmpleado = e.IdEmpleado";
        
        try {
            this.conectar();
            ps = this.con.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) { 
                Usuario u = new Usuario();
               
                u.setIdUsuario(rs.getInt("IdUsuario"));
                u.setNombreUsuario(rs.getString("NombreUsuario"));
                u.setNivel_Acceso(rs.getString("Nivel_Acceso"));
                u.setEstado(rs.getString("estado"));
                u.setEmpleado(rs.getString("nombreEmpleado"));
                u.setClave(rs.getString("apellido"));
              
                
                lisUsuario.add(u);
            }
        } catch (SQLException e) {
            
            JOptionPane.showMessageDialog(null, "Error en mostrar la lista de Usuario" + e.getMessage());
        } finally {
            
            this.cerrarCn();
        }
        
       return lisUsuario;
        
        
    }
    
    
    
    public void MostrarListaUsuario() throws SQLException, ClassNotFoundException {
    limpiarTablaUsuario();

    ArrayList<Usuario> lisUsuario = ListaUsuario();
    modeloUsuario = (DefaultTableModel) Consultar_Usuario.TablaUsuario.getModel();
    
modeloUsuario.setRowCount(0);
  Consultar_Usuario.TablaUsuario.setRowSorter(null);
  
    Object[] obj = new Object[6];

    for (int i = 0; i < lisUsuario.size(); i++) {
       
        obj[0] = lisUsuario.get(i).getIdUsuario();
        obj[1] = lisUsuario.get(i).getNombreUsuario();
        obj[2] = lisUsuario.get(i).getNivel_Acceso();
        obj[3] = lisUsuario.get(i).getEstado();
        obj[4] = lisUsuario.get(i).getEmpleado();
        obj [5] = lisUsuario.get(i).getClave();

        modeloUsuario.addRow(obj);
    }
}

    
    
    
    
    
    
    
    
    
    public void  GuardarUsuario (Usuario u) throws ClassNotFoundException {
      u.setNombreUsuario(AsignarUsuario.txtNombreUsuario.getText());
     String clavePlana = AsignarUsuario.txtClave.getText();
      u.setNivel_Acceso((String) AsignarUsuario.ComboxAsignarUsuario.getSelectedItem());
      u.setEstado(AsignarUsuario.ComboxEstadoUsuario.getSelectedItem().toString());
     String claveCifrada = BCrypt.hashpw(clavePlana, BCrypt.gensalt(12));
      String Query = "INSERT INTO usuario (idUsuario,NombreUsuario,Clave,Nivel_Acceso,estado,idEmpleado) values (?,?,?,?,?,?)";
      
        try {
            this.conectar();
            ps = this.con.prepareStatement(Query);
            ps.setInt(1,0);
            ps.setString(2,u.getNombreUsuario());
            ps.setString(3,claveCifrada);
            ps.setString(4, u.getNivel_Acceso());
            ps.setString(5, u.getEstado());
            ps.setInt(6,idEmpleadoReciente);
            if (ps.executeUpdate()> 0 ) {
                JOptionPane.showMessageDialog(null,"Registro exitoso");
                De.MostrarListaEmpleado();
                
            }
        } catch (SQLException e) {
            
            JOptionPane.showMessageDialog(null,"Error al registrar Usuario" + e.getMessage());
        }
        
        
    }
    
    
    
    public  void ElimarUsu (int idUsuario) throws  ClassNotFoundException, SQLException{
        
        
        String sql = "DELETE FROM usuario WHERE idUsuario = ?";
        try {
            this.conectar();
            ps = this.con.prepareStatement(sql);
            ps.setInt(1,idUsuario);
          if (ps.executeUpdate() > 0) {
              JOptionPane.showMessageDialog(null,"Usuario eliminado correctamente");
              
          }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al Eliminar usuario"+ e.getMessage());
            
        } finally {
            this.cerrarCn();
        }
        
        
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
   public  void ElimanerUsuario () throws ClassNotFoundException{
       
    int fila = Consultar_Usuario.TablaUsuario.getSelectedRow();
       
       if (fila == -1) {
           JOptionPane.showMessageDialog(null,"Por favor Ingrese un Usuario a Eliminar");
            return;
       }
  
       int idUsuario = Integer.parseInt(Consultar_Usuario.TablaUsuario.getValueAt(fila,0).toString());
       int Confirma = JOptionPane.showConfirmDialog(null,"Esta seguro de eliminar este Usuario?", "Confimar Eliminacion",JOptionPane.YES_NO_OPTION);
       
       if ( Confirma == JOptionPane.YES_OPTION) {
           
           try {
               
               ElimarUsu(idUsuario);
               
           } catch (SQLException e) {
               JOptionPane.showMessageDialog(null, "Error a eliminar el usuario " + e.getMessage());
           }
           
           
           try {
               MostrarListaUsuario();
           } catch (SQLException e) {
            
               
           }
           
           
           
       }
       
       
   }
   
    
    
 
public  void ActulizarUsuario (Usuario u) throws  ClassNotFoundException,SQLException {
    
    u.setIdUsuario(Integer.parseInt(Consultar_Usuario.txtIdUsuarioEstado.getText()));
    u.setEstado(Consultar_Usuario.jComboEstadoUsuario.getSelectedItem().toString());
    u.setNivel_Acceso(Consultar_Usuario.jComboxNivelAcceso.getSelectedItem().toString());
  
    String SQL = "UPDATE usuario SET estado=?,nivel_acceso=? WHERE idUsuario = ?";
    
    try {
        this.conectar();
        
        ps = this.con.prepareStatement(SQL);
      
        ps.setString(1, u.getEstado());
        ps.setString(2, u.getNivel_Acceso());
        ps.setInt(3,u.getIdUsuario());
        
        int filla = ps.executeUpdate();
        
        if (filla > 0) {
            JOptionPane.showMessageDialog(null, "Usuario Actulizado Correctamente");
            MostrarListaUsuario();
                 Consultar_Usuario.lblNivelDeAcceso.setVisible(false);
    Consultar_Usuario.jComboxNivelAcceso.setVisible(false);
     Consultar_Usuario.lblEstado.setVisible(false);
      Consultar_Usuario.jComboEstadoUsuario.setVisible(false);
      Consultar_Usuario.txtIdUsuarioEstado.setVisible(false);
      Consultar_Usuario.btbCancelarActualizacion.setVisible(false);
      Consultar_Usuario.btbConfirmarActulizacion.setVisible(false);
     Consultar_Usuario.btbActualizar.setVisible(true);
           
       
        } 
        
    } catch (Exception e) {    
        JOptionPane.showMessageDialog(null, "Error al Actulizar Usuario"+ e.getMessage()); 
        
    } finally {
        this.cerrarCn();
    }
  
    
    
    
}






public void agregarFiltroBusqueda() {
    TableRowSorter<DefaultTableModel> filtro = new TableRowSorter<>(modeloUsuario);
    Consultar_Usuario.TablaUsuario.setRowSorter(filtro);

    Consultar_Usuario.txtBuscar.getDocument().addDocumentListener(new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            buscar(filtro);
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            buscar(filtro);
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            buscar(filtro);
        }

        private void buscar(TableRowSorter<DefaultTableModel> filtro) {
            String texto = Consultar_Usuario.txtBuscar.getText();
            if (texto.trim().length() == 0) {
                filtro.setRowFilter(null);
            } else {
                filtro.setRowFilter(RowFilter.regexFilter("(?i)" + texto)); // (?i) ignora mayúsculas/minúsculas
            }
        }
    });
}



   
  public void eventoLblRecuperar(JLabel lbl, JFrame ventanaActual) {

    // Color inicial y estilo del label
    lbl.setForeground(new Color(0, 255, 0));
    lbl.setCursor(new Cursor(Cursor.HAND_CURSOR));

    lbl.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent evt) {
            RecuperarContrasena menu = new RecuperarContrasena();
            menu.setVisible(true);
            ventanaActual.dispose(); // Cierra la ventana actual
        }

        @Override
        public void mouseEntered(MouseEvent evt) {
            lbl.setForeground(Color.YELLOW);
            lbl.setText("<html><u>¿Olvidaste tu contraseña?</u></html>");
        }

        @Override
        public void mouseExited(MouseEvent evt) {
            lbl.setForeground(new Color(0, 255, 0));
            lbl.setText("¿Olvidaste tu contraseña?");
        }
    });
}

   
  
 public  void EnconderUsuario (){
     
   
   RecuperarContrasena.lblNombreUsuario.setVisible(false);
   RecuperarContrasena.lblCorreo.setVisible(false);
   RecuperarContrasena.txtNombreUsuarioRecuperar.setVisible(false);
   RecuperarContrasena.txtCorreoRecuperar.setVisible(false);
 
   RecuperarContrasena.btbVerificar.setVisible(false);
   
    
   RecuperarContrasena.lblCodigoDeVerificacion.setVisible(false);
   RecuperarContrasena.txtCodigoDeVerificacion.setVisible(false);
   
   RecuperarContrasena.  lblNuevaContrasena.setVisible(true);
       RecuperarContrasena.  txtNuevaContrase.setVisible(true);
       RecuperarContrasena.  lblConfirmaContrasena.setVisible(true);
        RecuperarContrasena. txtConfirmarContrsena.setVisible(true);
       RecuperarContrasena.  btbActualizarContrsena.setVisible(true);
        
        
      RecuperarContrasena.btbConfirmar.setVisible(false);
     
 }
  
  
  
 

public void VerificarUsuario() throws ClassNotFoundException {
    String nombreUsuario = RecuperarContrasena.txtNombreUsuarioRecuperar.getText().trim();
    String correo = RecuperarContrasena.txtCorreoRecuperar.getText().trim();

    String sql = "SELECT u.idUsuario FROM usuario u " +
                 "INNER JOIN empleado e ON u.idEmpleado = e.idEmpleado " +
                 "WHERE u.nombreUsuario = ? AND e.email = ?";

    try {
        this.conectar();
        ps = this.con.prepareStatement(sql);
        ps.setString(1, nombreUsuario);
        ps.setString(2, correo);
        rs = ps.executeQuery();

        if (rs.next()) {
            idUsuario = rs.getInt("idUsuario");

            // ✅ Generar código y fecha de expiración
            codigoGenerado = String.valueOf((int) (Math.random() * 900000) + 100000);
            codigoExpiracion = LocalDateTime.now().plusMinutes(5);

            // ✅ Enviar correo
           Enviar_Codigo_Correo.enviarCodigoRapido(correo, nombreUsuario,codigoGenerado);

          //    CodigoVerificacion();
          

        } else {
            JOptionPane.showMessageDialog(null, "Los datos no coinciden con ningún usuario registrado.");
        }

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al verificar usuario: " + e.getMessage());
    }
}

  
  
  

  
  public void ActualizarContrase() throws ClassNotFoundException {
   String nueva = RecuperarContrasena.txtNuevaContrase.getText().trim();
    String confirmar = RecuperarContrasena.txtConfirmarContrsena.getText().trim();

   

    // CIFRAR LA NUEVA CONTRASEÑA CON JBCrypt
    String nuevaCifrada = BCrypt.hashpw(nueva, BCrypt.gensalt(12));
    try {
        this.conectar();
        String sql = "UPDATE usuario SET clave = ? WHERE idUsuario = ?";
        ps = this.con.prepareStatement(sql);
        ps.setString(1, nuevaCifrada);
        ps.setInt(2, idUsuario);
        ps.executeUpdate();

     
      
        
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al actualizar contraseña: " + e.getMessage());
    }
}

  
  
  
  public void ConfirmarCodigo() {
    // Obtenemos el código que el usuario escribió
    String codigoIngresado = RecuperarContrasena.txtCodigoDeVerificacion.getText().trim();

    // Validaciones básicas
    if (codigoIngresado.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Debe ingresar el código de verificación.");
        return;
    }

    // Verificamos que el código coincida y no haya expirado
    if (codigoGenerado == null) {
        JOptionPane.showMessageDialog(null, "Primero debes solicitar un código de verificación.");
        return;
    }

    if (codigoIngresado.equals(codigoGenerado)) {
        if (LocalDateTime.now().isBefore(codigoExpiracion)) {
            // ✅ Código correcto y dentro del tiempo permitido
            EnconderUsuario(); // muestra campos de nueva contraseña
            JOptionPane.showMessageDialog(null, " Código verificado correctamente. Ahora ingresa tu nueva contraseña.");
        } else {
            JOptionPane.showMessageDialog(null, "️ El código ha expirado. Solicita uno nuevo.");
        }
    } else {
        JOptionPane.showMessageDialog(null, " El código ingresado no es correcto.");
    }
}

  
  
  
    
}
