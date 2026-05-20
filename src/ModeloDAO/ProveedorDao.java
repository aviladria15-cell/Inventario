/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ModeloDAO;

import Modelo.ConexiónBD;
import Modelo.Proveedor;
import java.sql.*;
import Vista_Gestionar_Proveedor.Gestionar_Proveedor;
import Vista_GestionInventario.InventarioLiquido;
import Vista_GestionInventario.InventarioSolido;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import Vista_GestionInventario.InventarioUnidad;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
public class ProveedorDao extends ConexiónBD {

    private ResultSet rs;
    private PreparedStatement ps;

    
    DefaultTableModel modeloProveedor = new DefaultTableModel();

    // TITULO TABLA
    public void TituloProveedor() {
        String Titulo[] = {"IdProveedor", "Nombre", "Teléfono", "Dirección", "Email", "RIF"};
        modeloProveedor.setColumnIdentifiers(Titulo);
        Gestionar_Proveedor.TablaProveedor.setModel(modeloProveedor);
    }

    // CENTRAR TEXTO TABLA
    public void centrarTextoTabla(JTable tabla) {
        DefaultTableCellRenderer centrado = new DefaultTableCellRenderer();
        centrado.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
        for (int i = 0; i < tabla.getColumnCount(); i++) {
            tabla.getColumnModel().getColumn(i).setCellRenderer(centrado);
        }
    }

    // LIMPIAR TABLA
    public void limpiarTablaProveedor() {
        int fila = modeloProveedor.getRowCount();
        if (fila > 0) {
            for (int i = 0; i < fila; i++) {
                modeloProveedor.removeRow(0);
            }
        }
    }
public void limpiarTablaProveedorr() {
    DefaultTableModel modelo = (DefaultTableModel) Gestionar_Proveedor.TablaProveedor.getModel();
    modelo.setRowCount(0); // borra las filas existentes
}




    // MOSTRAR PROVEEDORES EN TABLA
    public void MostrarListaDeProveedor( ) throws ClassNotFoundException, SQLException {
        
      
Gestionar_Proveedor.jScrollPane1.setVisible(true);
Gestionar_Proveedor.jScrollPane2.setVisible(false);
  
 TituloProveedor();
  limpiarTablaProveedorr();
  
        ArrayList<Proveedor> lista = ListaProveedor();
        modeloProveedor = (DefaultTableModel) Gestionar_Proveedor.TablaProveedor.getModel();

        Object[] obj = new Object[6]; // Cambiado a 6 porque hay 6 columnas, no 7

        for (Proveedor p : lista) {
            obj[0] = p.getIdProveedor();
            obj[1] = p.getNombre();
            obj[2] = p.getTelefono();
            obj[3] = p.getDireccion();
            obj[4] = p.getEmail();
            obj[5] = p.getRFC();

            modeloProveedor.addRow(obj);
        }

        Gestionar_Proveedor.TablaProveedor.setModel(modeloProveedor);
         centrarTextoTabla(Gestionar_Proveedor.TablaProveedor);
        
    }

    // ✅ MÉTODO AHORA ES PUBLIC PARA USARSE EN OTROS MÉTODOS
    public ArrayList<Proveedor> ListaProveedor() throws ClassNotFoundException, SQLException {
        ArrayList<Proveedor> lisProveedor = new ArrayList<>();

        String sql = "SELECT * FROM proyecto.proveedor";

        try {
            this.conectar();
            ps = this.con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Proveedor p = new Proveedor();
                p.setIdProveedor(rs.getInt("idProveedor"));
                p.setNombre(rs.getString("nombre"));
                p.setTelefono(rs.getString("Telefono"));
                p.setDireccion(rs.getString("Direccion"));
                p.setEmail(rs.getString("email"));
                p.setRFC(rs.getString("RFC"));

                lisProveedor.add(p);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error en mostrar la lista de proveedor: " + e.getMessage());
        } finally {
            this.cerrarCn();
        }

        return lisProveedor;
    }
/*
   public void cargarProveedoresEnCombo() throws ClassNotFoundException, SQLException {
    ArrayList<Proveedor> proveedores = this.ListaProveedor();

    // Usamos LinkedHashSet para eliminar duplicados y mantener el orden
    Set<Proveedor> proveedoresUnicos = new LinkedHashSet<>(proveedores);

    // Limpiamos el ComboBox
    Registrar_Productoss.ComboxBoxProveedor.removeAllItems();

    // Agregamos solo proveedores únicos
    for (Proveedor p : proveedoresUnicos) {
        Registrar_Productoss.ComboxBoxProveedor.addItem(p);
    }
}*/
   
   
public void configurarCampoTelefono() {
    // Prefijo actual
    final String[] prefijo = {""};

    // Estado: campo bloqueado hasta elegir operadora
    final boolean[] habilitado = {false};

    // 🔕 Ocultar mensaje al inicio
    Gestionar_Proveedor.lblMensajeOperadora.setVisible(false);

    // 🔒 Filtro del campo de texto
    ((javax.swing.text.AbstractDocument) Gestionar_Proveedor.txtTelefonoProveedor.getDocument())
            .setDocumentFilter(new javax.swing.text.DocumentFilter() {

        @Override
        public void remove(DocumentFilter.FilterBypass fb, int offset, int length) throws BadLocationException {
            if (!habilitado[0]) return; // bloquear si no hay operadora
            if (offset < prefijo[0].length()) return;
            super.remove(fb, offset, length);
        }

        @Override
        public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                throws BadLocationException {
            if (!habilitado[0]) return; // bloquear si no hay operadora

            // Solo números
            if (text != null && !text.matches("\\d*")) return;
            if (offset < prefijo[0].length()) return;

            super.replace(fb, offset, length, text, attrs);
        }

        @Override
        public void insertString(DocumentFilter.FilterBypass fb, int offset, String string, AttributeSet attr)
                throws BadLocationException {
            if (!habilitado[0]) return; // bloquear si no hay operadora
            if (string != null && !string.matches("\\d*")) return;
            if (offset < prefijo[0].length()) return;
            super.insertString(fb, offset, string, attr);
        }
    });

    // 🧠 Detectar intento de escritura sin operadora seleccionada
    Gestionar_Proveedor.txtTelefonoProveedor.addKeyListener(new java.awt.event.KeyAdapter() {
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
                int pos = Gestionar_Proveedor.txtTelefonoProveedor.getCaretPosition();
                if (pos <= prefijo[0].length() &&
                    (e.getKeyCode() == java.awt.event.KeyEvent.VK_BACK_SPACE ||
                     e.getKeyCode() == java.awt.event.KeyEvent.VK_DELETE)) {
                    e.consume();
                }
            }
        }
    });

    // 🎯 Acción del ComboBox de operadora
    Gestionar_Proveedor.ComboBoxOperadora.addActionListener(e -> {
        String seleccion = (String) Gestionar_Proveedor.ComboBoxOperadora.getSelectedItem();
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
            Gestionar_Proveedor.txtTelefonoProveedor.setText("");
        }

        // Actualizar prefijo actual
        prefijo[0] = nuevoPrefijo;

        // Mantener los números después del prefijo si existen
        String textoActual = Gestionar_Proveedor.txtTelefonoProveedor.getText();
        String soloNumeros = "";
        if (!textoActual.isEmpty() && textoActual.matches("^\\d{4}.*")) {
            soloNumeros = textoActual.substring(4);
        }

        // Combinar prefijo y números
        String nuevoTexto = prefijo[0].isEmpty() ? soloNumeros : prefijo[0] + soloNumeros;
        Gestionar_Proveedor.txtTelefonoProveedor.setText(nuevoTexto);
        Gestionar_Proveedor.txtTelefonoProveedor.setCaretPosition(prefijo[0].length());
    });

    // 🧩 Inicialización al abrir la ventana
    javax.swing.SwingUtilities.invokeLater(() -> {
        ocultarMensaje();
        Gestionar_Proveedor.txtTelefonoProveedor.setText("");
        Gestionar_Proveedor.txtTelefonoProveedor.setCaretPosition(0);
    });
}

/** 🟡 Mostrar mensaje de error en lblMensajeOperadora */
private void mostrarMensaje(String msg) {
    Gestionar_Proveedor.lblMensajeOperadora.setText(msg);
    Gestionar_Proveedor.lblMensajeOperadora.setVisible(true);
}

/** 🟢 Ocultar mensaje */
private void ocultarMensaje() {
    Gestionar_Proveedor.lblMensajeOperadora.setText("");
    Gestionar_Proveedor.lblMensajeOperadora.setVisible(false);
}

    // GUARDAR PROVEEDOR
    public void GuardaProveedor(Proveedor p) throws ClassNotFoundException, SQLException {
        p.setNombre(Gestionar_Proveedor.txtNombreProveedor.getText());
        p.setTelefono(Gestionar_Proveedor.txtTelefonoProveedor.getText());
        p.setDireccion(Gestionar_Proveedor.txtDireccionProveedor.getText());
        p.setEmail(Gestionar_Proveedor.txtEmailProveedor.getText());
        p.setRFC(Gestionar_Proveedor.txtRFCProveedor.getText());

        String sql = "INSERT INTO proveedor (idProveedor, nombre, Telefono, Direccion, email, RFC) VALUES (?,?,?,?,?,?)";

        try {
            this.conectar();
            ps = this.con.prepareStatement(sql);
            ps.setInt(1, 0);
            ps.setString(2, p.getNombre());
            ps.setString(3, p.getTelefono());
            ps.setString(4, p.getDireccion());
            ps.setString(5, p.getEmail());
            ps.setString(6, p.getRFC());

            int guarda = ps.executeUpdate();

            if (guarda > 0) {
                
              MostrarListaDeProveedor();
                JOptionPane.showMessageDialog(null, "Registro de proveedor exitoso");
                
                Gestionar_Proveedor.txtNombreProveedor.setText("");
                Gestionar_Proveedor.txtDireccionProveedor.setText("");
                Gestionar_Proveedor.txtRFCProveedor.setText("");
                Gestionar_Proveedor.txtTelefonoProveedor.setText("");
                Gestionar_Proveedor.txtEmailProveedor.setText("");
               
    
            }
            

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al registrar proveedor: " + e.getMessage());
        }
    }
    
    
    
    
    
    
    public void agregarFiltroBusqueda() {
    if (Gestionar_Proveedor.txtBuscar != null && Gestionar_Proveedor.TablaProveedor != null) {

        // Listener persistente
        Gestionar_Proveedor.txtBuscar.getDocument().addDocumentListener(new DocumentListener() {

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
                String texto = Gestionar_Proveedor.txtBuscar.getText().trim();

                // Obtener siempre el modelo actual
                DefaultTableModel modeloActual = (DefaultTableModel) Gestionar_Proveedor.TablaProveedor.getModel();

                // Verificar o crear sorter
                TableRowSorter<?> sorter = (TableRowSorter<?>) Gestionar_Proveedor.TablaProveedor.getRowSorter();
                if (sorter == null || sorter.getModel() != modeloActual) {
                    sorter = new TableRowSorter<>(modeloActual);
                    Gestionar_Proveedor.TablaProveedor.setRowSorter(sorter);
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

    
    
    
    
   public  void ActualizarProveedor(Proveedor p)throws  ClassNotFoundException,SQLException {
       
   p.setNombre(Gestionar_Proveedor.txtNombreProveedor.getText());
   p.setTelefono(Gestionar_Proveedor.txtTelefonoProveedor.getText());
   p.setDireccion(Gestionar_Proveedor.txtDireccionProveedor.getText());
   p.setEmail(Gestionar_Proveedor.txtEmailProveedor.getText());
   p.setRFC(Gestionar_Proveedor.txtRFCProveedor.getText());
   p.setIdProveedor(Integer.parseInt(Gestionar_Proveedor.txtID.getText()));
       
       
     String SQL = "UPDATE proveedor SET Nombre=?,Telefono=?,Direccion=?,email=?,RFC=? WHERE idProveedor=?";
     
       try {
           this.conectar();
           ps=this.con.prepareStatement(SQL);
           
          
           ps.setString(1, p.getNombre());
           ps.setString(2, p.getTelefono());
           ps.setString(3, p.getDireccion());
           ps.setString(4, p.getEmail());
           ps.setString(5, p.getRFC());
            ps.setInt(6, p.getIdProveedor());
           if (ps.executeUpdate() > 0 ) {
               
               JOptionPane.showMessageDialog(null,"Actualizacion de proveedor exitosa");
               MostrarListaDeProveedor();
                  Gestionar_Proveedor.txtNombreProveedor.setText("");
                Gestionar_Proveedor.txtDireccionProveedor.setText("");
                Gestionar_Proveedor.txtRFCProveedor.setText("");
                Gestionar_Proveedor.txtTelefonoProveedor.setText("");
                Gestionar_Proveedor.txtEmailProveedor.setText("");
              
               
           } else {
               JOptionPane.showMessageDialog(null, " No hay vida");
           }
           
           
       } catch (SQLException e) {
           JOptionPane.showMessageDialog(null, " Error al Actualizar Proveedor");
           
       }
       
           
   }
    
    
    
  public  void EliminarProveedorSinoTieneProductoSumunistrado (int IdProveedor) throws  ClassNotFoundException,SQLException{
      
      String SLQ = "DELETE FROM proveedor WHERE idProveedor=?";
      
      try {
          this.conectar();
          ps = this.con.prepareStatement(SLQ);
          ps.setInt(1, IdProveedor);
          
          if (ps.executeLargeUpdate() > 0) {
              JOptionPane.showMessageDialog(null, " Eliminacion exitosa");
              
          }
          
      } catch (SQLException e) {
          JOptionPane.showMessageDialog(null, " No se puede eliminar este proveedor porque tiene productos suministrado activo");
          
      }
      
      
      
  }
    
    
    
    
    public  void EliminarProveedorBajoSeguridad() throws ClassNotFoundException, SQLException{
     
        int fila = Gestionar_Proveedor.TablaProveedor.getSelectedRow();
       
        if (fila == -1) {
            
            JOptionPane.showMessageDialog(null, " Debe seleccionar un proveedor para eliminarlo");
            return;
        }
        
        int idProveedor = Integer.parseInt(Gestionar_Proveedor.TablaProveedor.getValueAt(fila, 0).toString());
        
        
        int Confirmar = JOptionPane.showConfirmDialog(null, "Estas seguro de eliminar este Proveedor", "Confirme si estas seguro",JOptionPane.YES_NO_OPTION);
        
        if (Confirmar == JOptionPane.YES_OPTION) {

            EliminarProveedorSinoTieneProductoSumunistrado(idProveedor);
            MostrarListaDeProveedor();
        }
        
        
    }
    
    
    
    

 public void cargarProveedoresInventarioLiquido() throws ClassNotFoundException, SQLException {
    ArrayList<Proveedor> proveedores = this.ListaProveedor();

    // Usamos LinkedHashSet para evitar duplicados
    Set<Proveedor> proveedoresUnicos = new LinkedHashSet<>(proveedores);

    // Limpiamos el ComboBox
 InventarioLiquido.jComboProveedor.removeAllItems();

    // Cargamos proveedores únicos
    for (Proveedor p : proveedoresUnicos) {
       InventarioLiquido.jComboProveedor.addItem(p);
    }
}

    
    
 
public void agregarBuscadorProveedorInventarioLiquido() {
    if (InventarioLiquido.txtBuscarProveedor != null) {
        InventarioLiquido.txtBuscarProveedor.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                actualizarComboBoxInventarioLiquido();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                actualizarComboBoxInventarioLiquido();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                actualizarComboBoxInventarioLiquido();
            }
        });
    }
}


    private void actualizarComboBoxInventarioLiquido() {
    String texto = InventarioLiquido.txtBuscarProveedor.getText().toLowerCase();

    try {
        // Lista completa de productos
        ArrayList<Proveedor> lista = this.ListaProveedor();

        // Limpiar ComboBox
        InventarioLiquido.jComboProveedor.removeAllItems();

        // Agregar coincidencias
        for (Proveedor p : lista) {
            if (p.getNombre().toLowerCase().contains(texto)) {
            InventarioLiquido.jComboProveedor.addItem(p);
            }
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
}

    
    
 // InventarioSOLIDO
    
    
    public void cargarProveedoresInventarioSolido() throws ClassNotFoundException, SQLException {
    ArrayList<Proveedor> proveedores = this.ListaProveedor();

    // Usamos LinkedHashSet para evitar duplicados
    Set<Proveedor> proveedoresUnicos = new LinkedHashSet<>(proveedores);

    // Limpiamos el ComboBox
 InventarioSolido.jComboBoxProveedor.removeAllItems();

    // Cargamos proveedores únicos
    for (Proveedor p : proveedoresUnicos) {
       InventarioSolido.jComboBoxProveedor.addItem(p);
    }
}

    
    
 
public void agregarBuscadorProveedorInventarioSOLIDO() {
    if (InventarioSolido.txtBuscarProveedor != null) {
        InventarioSolido.txtBuscarProveedor.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                actualizarComboBoxInventarioSOLIDO();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                actualizarComboBoxInventarioSOLIDO();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                actualizarComboBoxInventarioSOLIDO();
            }
        });
    }
}


    private void actualizarComboBoxInventarioSOLIDO() {
    String texto = InventarioSolido.txtBuscarProveedor.getText().toLowerCase();

    try {
        // Lista completa de productos
        ArrayList<Proveedor> lista = this.ListaProveedor();

        // Limpiar ComboBox
        InventarioSolido.jComboBoxProveedor.removeAllItems();

        // Agregar coincidencias
        for (Proveedor p : lista) {
            if (p.getNombre().toLowerCase().contains(texto)) {
            InventarioSolido.jComboBoxProveedor.addItem(p);
            }
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
}


 
 
 
 // InventarioUnidad
    
     
    public void cargarProveedoresInventarioUnidad() throws ClassNotFoundException, SQLException {
    ArrayList<Proveedor> proveedores = this.ListaProveedor();

    // Usamos LinkedHashSet para evitar duplicados
    Set<Proveedor> proveedoresUnicos = new LinkedHashSet<>(proveedores);

    // Limpiamos el ComboBox
 InventarioUnidad.jComboBoxProveedor.removeAllItems();

    // Cargamos proveedores únicos
    for (Proveedor p : proveedoresUnicos) {
       InventarioUnidad.jComboBoxProveedor.addItem(p);
    }
}

    
    
 
public void agregarBuscadorProveedorInventarioUnidad() {
    if (InventarioUnidad.txtBuscarProveedor != null) {
        InventarioUnidad.txtBuscarProveedor.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                actualizarComboBoxInventarioUnidad();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                actualizarComboBoxInventarioUnidad();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                actualizarComboBoxInventarioUnidad();
            }
        });
    }
}


    private void actualizarComboBoxInventarioUnidad() {
    String texto = InventarioUnidad.txtBuscarProveedor.getText().toLowerCase();

    try {
        // Lista completa de productos
        ArrayList<Proveedor> lista = this.ListaProveedor();

        // Limpiar ComboBox
        InventarioUnidad.jComboBoxProveedor.removeAllItems();

        // Agregar coincidencias
        for (Proveedor p : lista) {
            if (p.getNombre().toLowerCase().contains(texto)) {
            InventarioUnidad.jComboBoxProveedor.addItem(p);
            }
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
}


 
 
 
 
    
}

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
  


  
    
    
    
    
    
    
    
    
    
    

