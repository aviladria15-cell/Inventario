/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ModeloDAO;
import Modelo.Empleado;
import java.sql.*;
import Modelo.ConexiónBD;
import javax.swing.JOptionPane;
import Vista_Usuari_Empleado.frm_GestionarEmpleado;
import Vista_Usuari_Empleado.AsignarUsuario;

import java.util.ArrayList;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.util.logging.Level;
import java.util.logging.Logger;

import  Controlador.controladorVisual;

import java.text.SimpleDateFormat;
import Modelo.Usuario;
import Validacion.Validar_Formulario_Empleado;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;


public class EmpleadoDAO  extends ConexiónBD{

          
    private ResultSet rs;
    private PreparedStatement ps;
    DefaultTableModel modeloEmpleado = new DefaultTableModel();
   
    

    
    Controlador.controladorVisual ct = new  Controlador.controladorVisual();
    
    // Metodo para los titulos de la tabla empleado con el modelo de la tabla
     public void TituloEmpleado (){
   
    String Titulo [] = {"IdEmpleado","Nombre", "Apellido","  Teléfono  ","  Email   ","  Cédula  "," Fecha de Nacimiento "," Cargo Asignado", "Usuario Asignado" };
    modeloEmpleado.setColumnIdentifiers(Titulo);
   frm_GestionarEmpleado.TablaEmpleado.setModel(modeloEmpleado);
 
    
}

     // Metodo para centralizar los datos de la tabla empleado
 public  void centrarTextoTabla(JTable tabla) {
        DefaultTableCellRenderer centrado = new DefaultTableCellRenderer();
        centrado.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
        for (int i = 0; i < tabla.getColumnCount(); i++) {
            tabla.getColumnModel().getColumn(i).setCellRenderer(centrado);
        }
    }
 
 // Metodo para limpiar la tabla  empleado 
private void limpiarTablaEmpleado (){
 int fila = modeloEmpleado.getRowCount();
    if (fila > 0) {
        for (int i = 0; i < fila; i++) {
            modeloEmpleado.removeRow(0);
        }
        
    }
    
}



 
public void MostrarListaEmpleado() throws SQLException, ClassNotFoundException {
    TituloEmpleado();        // Configura las columnas
    limpiarTablaEmpleado();  // Limpia todas las filas

    ArrayList<Empleado> ListaEmpleado = ListaEmpleado();
    modeloEmpleado = (DefaultTableModel) frm_GestionarEmpleado.TablaEmpleado.getModel();

    // Agregar filas
    for (Empleado e : ListaEmpleado) {
        Object[] obj = new Object[9];
        obj[0] = e.getIdEmpleado();
        obj[1] = e.getNombre();
        obj[2] = e.getApellido();
        obj[3] = e.getTelefono();
        obj[4] = e.getEmail();
        obj[5] = e.getCedula();
        obj[6] = e.getFechaNacimiento();
        obj[7] = e.getCargo();
        obj[8] = e.getUsuario();
        modeloEmpleado.addRow(obj);
    }

    // Ya no es necesario hacer setModel(modeloEmpleado)
    // frm_GestionarEmpleado.TablaEmpleado.setModel(modeloEmpleado);

    centrarTextoTabla(frm_GestionarEmpleado.TablaEmpleado);
}

// Metodo para mostrar la lista de empleado en una tabla Acompanado con otro metodo esto es para obtener la liste de empleado
 private ArrayList ListaEmpleado () throws SQLException,ClassNotFoundException{
     
   ArrayList <Empleado> listaEmpleadoo = new ArrayList<>();
   
     String Querry = "SELECT e.idEmpleado, e.Nombre,e.Apellido, e.Cedula,Fecha_Nacimiento,e.email, e.Telefono, e.Cargo, u.NombreUsuario"+
             " FROM empleado e LEFT JOIN usuario u ON e.idEmpleado = u.idEmpleado";
     try {
         this.conectar();
         ps = this.con.prepareStatement(Querry);
         rs = ps.executeQuery();
         
         while (rs.next()) {
        Empleado e = new Empleado();
        
            e.setIdEmpleado(rs.getInt("idEmpleado"));
            e.setNombre(rs.getString("Nombre"));
            e.setApellido(rs.getString("Apellido"));
            e.setTelefono(rs.getString("Telefono"));
           e.setEmail(rs.getString("email"));
           e.setCedula(rs.getString("Cedula"));
           e.setFechaNacimiento(rs.getDate("Fecha_Nacimiento"));
           e.setCargo(rs.getString("Cargo")); 
          e.setUsuario(rs.getString("NombreUsuario"));
           listaEmpleadoo.add(e);
             
         }
     } catch (SQLException e) {
         JOptionPane.showMessageDialog(null, "Error en mostrar la lista de empleado" + e.getMessage());
     } finally {
         this.cerrarCn();
     }
     return listaEmpleadoo;
 }
 
 
 
 
public void configurarCampoTelefono() {
    // Prefijo actual
    final String[] prefijo = {""};

    // Estado: campo bloqueado hasta elegir operadora
    final boolean[] habilitado = {false};

    // 🔕 Ocultar mensaje al inicio
    frm_GestionarEmpleado.lblMensajeOperadora.setVisible(false);

    // 🔒 Filtro del campo de texto
    ((javax.swing.text.AbstractDocument) frm_GestionarEmpleado.txtTelefono.getDocument())
            .setDocumentFilter(new javax.swing.text.DocumentFilter() {

        @Override
        public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
            if (!habilitado[0]) return; // bloquear si no hay operadora
            if (offset < prefijo[0].length()) return;
            super.remove(fb, offset, length);
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                throws BadLocationException {
            if (!habilitado[0]) return; // bloquear si no hay operadora

            // Solo números
            if (text != null && !text.matches("\\d*")) return;
            if (offset < prefijo[0].length()) return;

            super.replace(fb, offset, length, text, attrs);
        }

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                throws BadLocationException {
            if (!habilitado[0]) return; // bloquear si no hay operadora
            if (string != null && !string.matches("\\d*")) return;
            if (offset < prefijo[0].length()) return;
            super.insertString(fb, offset, string, attr);
        }
    });

    // 🧠 Detectar intento de escritura sin operadora seleccionada
    frm_GestionarEmpleado.txtTelefono.addKeyListener(new java.awt.event.KeyAdapter() {
        @Override
        public void keyTyped(java.awt.event.KeyEvent e) {
            if (!habilitado[0]) {
                e.consume(); // no mostrar nada
                mostrarMensaje("Seleccione primero una operadora");
            }
        }

        @Override
        public void keyPressed(java.awt.event.KeyEvent e) {
            if (!habilitado[0]) {
                // bloquea borrar o escribir números
                if (Character.isDigit(e.getKeyChar()) ||
                    e.getKeyCode() == java.awt.event.KeyEvent.VK_BACK_SPACE ||
                    e.getKeyCode() == java.awt.event.KeyEvent.VK_DELETE) {
                    e.consume();
                    mostrarMensaje("Seleccione primero una operadora");
                }
            } else {
                // si ya está habilitado, proteger el prefijo
                int pos = frm_GestionarEmpleado.txtTelefono.getCaretPosition();
                if (pos <= prefijo[0].length() &&
                    (e.getKeyCode() == java.awt.event.KeyEvent.VK_BACK_SPACE ||
                     e.getKeyCode() == java.awt.event.KeyEvent.VK_DELETE)) {
                    e.consume();
                }
            }
        }
    });

    // 🎯 Acción del ComboBox de operadora
    frm_GestionarEmpleado.ComboBoxOperadora.addActionListener(e -> {
        String seleccion = (String) frm_GestionarEmpleado.ComboBoxOperadora.getSelectedItem();
        if (seleccion != null) seleccion = seleccion.trim();

        // 📞 Extraer el prefijo (ej. 0414, 0424, etc.)
        String nuevoPrefijo = "";
        if (seleccion != null && seleccion.matches(".*\\(\\d{4}\\).*")) {
            nuevoPrefijo = seleccion.replaceAll(".*\\((\\d{4})\\).*", "$1");
        }

        if (!nuevoPrefijo.isEmpty()) {
            habilitado[0] = true;
            ocultarMensaje();
        } else {
            habilitado[0] = false;
            frm_GestionarEmpleado.txtTelefono.setText("");
        }

        // Actualizar prefijo actual
        prefijo[0] = nuevoPrefijo;

        // Mantener los números después del prefijo si existen
        String textoActual = frm_GestionarEmpleado.txtTelefono.getText();
        String soloNumeros = "";
        if (!textoActual.isEmpty() && textoActual.matches("^\\d{4}.*")) {
            soloNumeros = textoActual.substring(4);
        }

        // Combinar prefijo y números
        String nuevoTexto = prefijo[0].isEmpty() ? soloNumeros : prefijo[0] + soloNumeros;
        frm_GestionarEmpleado.txtTelefono.setText(nuevoTexto);
        frm_GestionarEmpleado.txtTelefono.setCaretPosition(prefijo[0].length());
    });

    // 🧩 Inicialización al abrir la ventana
    javax.swing.SwingUtilities.invokeLater(() -> {
        ocultarMensaje();
        frm_GestionarEmpleado.txtTelefono.setText("");
        frm_GestionarEmpleado.txtTelefono.setCaretPosition(0);
    });
}

/** 🟡 Mostrar mensaje de error en lblMensajeOperadora */
private void mostrarMensaje(String msg) {
    frm_GestionarEmpleado.lblMensajeOperadora.setText(msg);
    frm_GestionarEmpleado.lblMensajeOperadora.setVisible(true);
}

/** 🟢 Ocultar mensaje */
private void ocultarMensaje() {
    frm_GestionarEmpleado.lblMensajeOperadora.setText("");
    frm_GestionarEmpleado.lblMensajeOperadora.setVisible(false);
}


 
 
   // Metodo de consulta sql para guardar un empleado con todo sus datos  
  public void GuardarEmpleado ( Empleado e )  throws ClassNotFoundException, SQLException {
      
    
      e.setNombre(frm_GestionarEmpleado.txtNombre.getText());
      e.setApellido(frm_GestionarEmpleado.txtApellido.getText());
      e.setTelefono(frm_GestionarEmpleado.txtTelefono.getText());
      e.setCedula(frm_GestionarEmpleado.txtCedula.getText());
      e.setEmail(frm_GestionarEmpleado.txtEmail.getText());
      e.setCargo((String) frm_GestionarEmpleado.ComboxCargo.getSelectedItem());
      
      // si el setter es setFechaNacimiento(java.sql.Date)
SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
String fechaTexto = formato.format(frm_GestionarEmpleado.jDateNacimiento.getDate());
java.sql.Date sqlDate = java.sql.Date.valueOf(fechaTexto);
e.setFechaNacimiento(sqlDate);

  
      String Query = "INSERT INTO empleado(idEmpleado,Nombre,Apellido,Cedula,Fecha_Nacimiento,email,Telefono,Cargo) Values (?,?,?,?,?,?,?,?)";
      
      try {
      this.conectar();
      ps = this.con.prepareStatement(Query,Statement.RETURN_GENERATED_KEYS);
      ps.setInt(1, 0);
      ps.setString(2,e.getNombre());
      ps.setString(3,e.getApellido());
      ps.setString(4, e.getCedula());
     ps.setDate(5, new java.sql.Date(e.getFechaNacimiento().getTime()));
      ps.setString(6, e.getEmail());
      ps.setString(7, e.getTelefono());
      ps.setString(8, e.getCargo());
   
          if (ps.executeUpdate() > 0) {
              rs = ps.getGeneratedKeys();
              if (rs.next()) {
              int idEmpleado = rs.getInt(1);
              UsurioDAO.idEmpleadoReciente = idEmpleado;
              }
              
              JOptionPane.showMessageDialog(null, "Registro exitosos");
              frm_GestionarEmpleado.txtCedula.setText("");
              frm_GestionarEmpleado.txtNombre.setText("");
              frm_GestionarEmpleado.txtApellido.setText("");
              frm_GestionarEmpleado.txtEmail.setText("");
              frm_GestionarEmpleado.txtTelefono.setText("");
              MostrarListaEmpleado();
             
             
              
          }
      
      } catch (SQLException E) {
        JOptionPane.showMessageDialog(null, "Error.! al registrar empleado " + E.getMessage());
      }
  } 
  
  

   
  
  // Metodo de consulta sql para eliminar un empleado
  public  void EliminarEmpleado (int IdEmpleado) throws  ClassNotFoundException, SQLException {
   String sql = "DELETE FROM empleado WHERE idEmpleado = ?"; 
   
      try {
          this.conectar();
          ps = this.con.prepareStatement(sql);
          ps.setInt(1,IdEmpleado);
          if (ps.executeUpdate() > 0) {
              JOptionPane.showMessageDialog(null,"Empleado eliminado correctamente");
              
          }
          
      } catch (SQLException e) {
          
          JOptionPane.showMessageDialog(null, "Error al Eliminar empleado:"+ e.getMessage() );
      } finally {
          
          this.cerrarCn();
      }
      
      
      
  }
  
  
  
  // Metodo selecionar un empleado de la tabla con su Id y Confirma la eliminacion
public  void  Elimanr(){
    int FilaSeleccionda = frm_GestionarEmpleado.TablaEmpleado.getSelectedRow();
  
    if (FilaSeleccionda == -1) {
        JOptionPane.showMessageDialog(null,"Por favor selecciona un empleado a eliminar");
        return;
    }
    int idEmpleado = Integer.parseInt(frm_GestionarEmpleado.TablaEmpleado.getValueAt(FilaSeleccionda,0).toString());
    
    int Confir = JOptionPane.showConfirmDialog(null,"Esta seguro de eliminar este empleado?","Confirmar eliminacion",
            JOptionPane.YES_NO_OPTION);
    
    if (Confir == JOptionPane.YES_OPTION) {
        
        try {
            EliminarEmpleado(idEmpleado);
       
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(EmpleadoDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(EmpleadoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }  
        try {
            MostrarListaEmpleado();
        } catch (SQLException ex) {
            Logger.getLogger(EmpleadoDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(EmpleadoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
         centrarTextoTabla(frm_GestionarEmpleado.TablaEmpleado);
    } 
}



// Metodo para Actulizar el empleado en la base de dato y aplicacion en el formulario de Actualizar empleado 
public  void ActualizarEmpleado (Empleado e )throws  ClassNotFoundException,SQLException  {
  
  e.setNombre(frm_GestionarEmpleado.txtNombre.getText());
  e.setApellido(frm_GestionarEmpleado.txtApellido.getText());
  e.setCedula(frm_GestionarEmpleado.txtCedula.getText());
  e.setFechaNacimiento(frm_GestionarEmpleado.jDateNacimiento.getDate());
  e.setEmail(frm_GestionarEmpleado.txtEmail.getText());
  e.setTelefono(frm_GestionarEmpleado.txtTelefono.getText());
  e.setIdEmpleado(Integer.parseInt(frm_GestionarEmpleado.txtID.getText()));
  e.setCargo((String) frm_GestionarEmpleado.ComboxCargo.getSelectedItem());
       // si el setter es setFechaNacimiento(java.sql.Date)
SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
String fechaTexto = formato.format(frm_GestionarEmpleado.jDateNacimiento.getDate());
java.sql.Date sqlDate = java.sql.Date.valueOf(fechaTexto);
e.setFechaNacimiento(sqlDate);

  
     
  String qsl = "UPDATE empleado SET Nombre=?, Apellido=?,Cedula=?,Fecha_Nacimiento =?,email=?,Telefono=?, Cargo=? WHERE idEmpleado=?";
  
    try {
        this.conectar();       
        ps = this.con.prepareStatement(qsl);
        ps.setString(1,e.getNombre());
        ps.setString(2,e.getApellido());
        ps.setString(3,e.getCedula());
       ps.setDate(4, new java.sql.Date(e.getFechaNacimiento().getTime()));
        ps.setString(5, e.getEmail());
        ps.setString(6, e.getTelefono());
        ps.setString(7,e.getCargo());
        ps.setInt(8, e.getIdEmpleado());
        
       int fila = ps.executeUpdate();
       
        if (fila > 0) {
            
            JOptionPane.showMessageDialog(null,"Empleado Actulizar correctamente");
             MostrarListaEmpleado();
             
            frm_GestionarEmpleado.txtNombre.setText("");
  frm_GestionarEmpleado.txtApellido.setText("");
  frm_GestionarEmpleado.txtCedula.setText("");
  frm_GestionarEmpleado.txtEmail.setText("");
  frm_GestionarEmpleado.txtTelefono.setText("");
  frm_GestionarEmpleado.txtID.setText("");
  frm_GestionarEmpleado.ComboxCargo.addItem("");
  frm_GestionarEmpleado.txtID.setVisible(false);
 frm_GestionarEmpleado.jDateNacimiento.setDate(null);

   frm_GestionarEmpleado.btbConfirmarActualizacion.setVisible(false);
   frm_GestionarEmpleado.btbCancelar.setVisible(false);
 
   frm_GestionarEmpleado.btbCerrar.setVisible(true);
   frm_GestionarEmpleado.btbActualizarEmpleado.setVisible(true);
   frm_GestionarEmpleado.btbRegistrar.setVisible(true);
   frm_GestionarEmpleado.btnEliminarEmpleado.setVisible(true);
            frm_GestionarEmpleado.txtEmail.setText("");
        } else {
            JOptionPane.showMessageDialog(null,"No se encontro el empleado a actulizar");
        }
        
    } catch (Exception E) {
        
        JOptionPane.showMessageDialog(null, "Error al Actulizar Empleado" + E.getMessage());
        
    } finally {
        
        this.cerrarCn();
    }
}







  
  public void agregarFiltroBusqueda() {
    if (frm_GestionarEmpleado.txtBuscar != null && frm_GestionarEmpleado.TablaEmpleado != null) {

        // Listener persistente
        frm_GestionarEmpleado.txtBuscar.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                buscar();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                buscar();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                buscar();
            }

            private void buscar() {
                String texto = frm_GestionarEmpleado.txtBuscar.getText().trim();

                // Obtener siempre el modelo actual
                DefaultTableModel modeloActual = (DefaultTableModel) frm_GestionarEmpleado.TablaEmpleado.getModel();

                // Verificar o crear sorter
                TableRowSorter<?> sorter = (TableRowSorter<?>) frm_GestionarEmpleado.TablaEmpleado.getRowSorter();
                if (sorter == null || sorter.getModel() != modeloActual) {
                    sorter = new TableRowSorter<>(modeloActual);
                    frm_GestionarEmpleado.TablaEmpleado.setRowSorter(sorter);
                }

                // Aplicar o limpiar filtro
                if (texto.isEmpty()) {
                    sorter.setRowFilter(null);
                } else {
                    try {
                        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(texto)));
                    } catch (PatternSyntaxException ex) {
                        sorter.setRowFilter(null); // evita errores si hay símbolos especiales
                    }
                }
            }
        });
    }
}

      
      
      
      
      
  }
















  
    

  
  
  
  
  
  
    

