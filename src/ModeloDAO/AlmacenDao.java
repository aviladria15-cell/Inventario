
package ModeloDAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.*;
import Modelo.Almacen;
import Modelo.*;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.swing.JOptionPane;
import Vista_Almacen.Vista_Registrar_Almacen;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import Vista_GestionInventario.InventarioLiquido;
import Vista_GestionInventario.InventarioSolido;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;
import Vista_GestionInventario.InventarioUnidad;

public class AlmacenDao  extends  ConexiónBD{
    
    PreparedStatement ps;
     ResultSet rs;
    

    Almacen A = new Almacen();

Controlador.controladorVisual ct = new  Controlador.controladorVisual();

    
    
    DefaultTableModel modeloAlmacen = new DefaultTableModel();
    

    
    
    
    
    
    // Metodo para los titulos de la tabla empleado con el modelo de la tabla
     public void TituloAlmacen(){
   
    String Titulo [] = { "ID_Ubicación","PASILLO"," ALA  ", " ESTANTE ","NIVEL"," CAPACIDAD " ," PRODUCTO ", " STOCK ACTUAL " };
    modeloAlmacen.setColumnIdentifiers(Titulo);
  Vista_Registrar_Almacen.TablaAlmacen.setModel(modeloAlmacen);
 
    
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
private void limpiarTablaAlmacen (){
 int fila = modeloAlmacen.getRowCount();
    if (fila > 0) {
        for (int i = 0; i < fila; i++) {
            modeloAlmacen.removeRow(0);
        }
        
    }
    
}

    
    
    
    
    
    
    
    
    
    
    
       


public  void RegistrarAlmacen () throws ClassNotFoundException,SQLException {
    
   
    
    
    A.setPasillo(Vista_Registrar_Almacen.txtPasillo.getText());
    
     A.setAla((String) Vista_Registrar_Almacen.jComboBoxAla.getSelectedItem());
    
    A.setEstante(Vista_Registrar_Almacen.txtEstante.getText());
    
    A.setNivel(Integer.parseInt(Vista_Registrar_Almacen.txtNivel.getText()));
    
    A.setCapacidad(Integer.parseInt(Vista_Registrar_Almacen.txtCapacidad.getText()));
    
    String sql = "INSERT INTO almacen (pasillo,ala,estante,nivel,capacidad) values (?,?,?,?,?)";
    
    try {
        
        this.conectar();
        
        ps = this.con.prepareStatement(sql);
        
        ps.setString(1, A.getPasillo());
        ps.setString(2, A.getAla());
        ps.setString(3, A.getEstante());
        ps.setInt(4, A.getNivel());
        ps.setInt(5, A.getCapacidad());
        
        if (ps.executeUpdate() > 0) {
            
            JOptionPane.showMessageDialog(null,"Registro exitoso de almacen");
            MostrarAlmacen();
       Vista_Registrar_Almacen.txtCapacidad.setText("");
             Vista_Registrar_Almacen.txtEstante.setText("");
             Vista_Registrar_Almacen.txtNivel.setText("");
             Vista_Registrar_Almacen.txtPasillo.setText(" ");
            
        }
        
        
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al registrar almacen" + e.getMessage());
    }
    
                       


}



public  void ActualizarAlmacen (int idUbicacion) throws  ClassNotFoundException,SQLException {
    
       
    A.setPasillo(Vista_Registrar_Almacen.txtPasillo.getText());
    
     A.setAla((String) Vista_Registrar_Almacen.jComboBoxAla.getSelectedItem());
    
    A.setEstante(Vista_Registrar_Almacen.txtEstante.getText());
    
    A.setNivel(Integer.parseInt(Vista_Registrar_Almacen.txtNivel.getText()));
    
    A.setCapacidad(Integer.parseInt(Vista_Registrar_Almacen.txtCapacidad.getText()));
    
    //A.setId_Ubicacion(Integer.parseInt(Gestionar_Almacenn.txtIDAlmacen.getText()));
    
    
    
    
    String sql = "  UPDATE almacen SET pasillo=?,ala=?,estante=?,nivel=?,capacidad=?  WHERE id_Ubicacion =?";
    
    try {
        this.conectar();
        
        ps = this.con.prepareStatement(sql);
          ps.setString(1, A.getPasillo());
        ps.setString(2, A.getAla());
        ps.setString(3, A.getEstante());
        ps.setInt(4, A.getNivel());
        ps.setInt(5, A.getCapacidad());
        ps.setInt(6, idUbicacion);
        
        int fila = ps.executeUpdate();
        
        if (fila > 0) {
      
              JOptionPane.showMessageDialog(null, "Actualizacion exitosa");
             
               MostrarAlmacen();
       Vista_Registrar_Almacen.btbEliminar.setVisible(true);
  Vista_Registrar_Almacen.btbActualizar.setVisible(true);
  Vista_Registrar_Almacen.btbRegistrar.setVisible(true);
  
  Vista_Registrar_Almacen.btbCancelarActualizacion.setVisible(false);
  Vista_Registrar_Almacen.btbConfirmarActualizacion.setVisible(false);
  
  Vista_Registrar_Almacen.txtPasillo.setText("");
  Vista_Registrar_Almacen.txtEstante.setText("");
  Vista_Registrar_Almacen.txtCapacidad.setText("");
  Vista_Registrar_Almacen.txtNivel.setText("");
         
              
        } else {
            JOptionPane.showMessageDialog(null, "Error al Actualizar ");
        }
        
        
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, " Error" + e.getMessage());
    }  finally {
        
        this.cerrarCn();
    }
    
}



public  void ActualizarConseguridad (){
      int filaSeleccionada = Vista_Registrar_Almacen.TablaAlmacen.getSelectedRow();

    if (filaSeleccionada == -1) {
       // JOptionPane.showMessageDialog(null, "Por favor seleccione una Ubicación si la desea eliminar");
        return; // 🔴 salir del método, así no sigue ejecutando
    }

    int id_Ubicacion = Integer.parseInt(Vista_Registrar_Almacen.TablaAlmacen.getValueAt(filaSeleccionada, 0).toString()
    );

    int confirmar = JOptionPane.showConfirmDialog(
        null,
        "¿Está seguro de querer actualizar esta ubicación?",
        "Confirmar Actualización",
        JOptionPane.YES_NO_OPTION
    );

    if (confirmar == JOptionPane.YES_OPTION) {
        
          try {
              ActualizarAlmacen(id_Ubicacion);
          } catch (ClassNotFoundException ex) {
              System.getLogger(AlmacenDao.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
          } catch (SQLException ex) {
              System.getLogger(AlmacenDao.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
          }
      
    }
}


public  void EliminarAlmacen ( int Id_Ubicacion) throws  ClassNotFoundException,SQLException {
    
    
    String sql = "DELETE FROM almacen where id_Ubicacion =?";
    
    try {
        this.conectar();
        
        ps = this.con.prepareStatement(sql);
         ps.setInt(1, Id_Ubicacion);
         
         if (ps.executeUpdate() > 0) {
             
             JOptionPane.showMessageDialog(null, " Ubicacion Eliminada correctamente");
            
        }
         
        
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, " Error al Eliminada Alamacen " + e.getMessage());
    }  finally {
        this.cerrarCn();
    }
    
   
    
    
}




    
    
    
    
    
    
    
    
    
    
    
public void EliminarConSeguridad() {
    int filaSeleccionada = Vista_Registrar_Almacen.TablaAlmacen.getSelectedRow();

    if (filaSeleccionada == -1) {
        JOptionPane.showMessageDialog(null, "Por favor seleccione una Ubicación si la desea eliminar");
        return; // 🔴 salir del método, así no sigue ejecutando
    }

    int id_Ubicacion = Integer.parseInt(Vista_Registrar_Almacen.TablaAlmacen.getValueAt(filaSeleccionada, 0).toString()
    );

    int confirmar = JOptionPane.showConfirmDialog(
        null,
        "¿Está seguro de querer eliminar esta ubicación?",
        "Confirmar Eliminación",
        JOptionPane.YES_NO_OPTION
    );

    if (confirmar == JOptionPane.YES_OPTION) {
        try {
            EliminarAlmacen(id_Ubicacion);
            MostrarAlmacen(); // puedes llamar directamente aquí
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(AlmacenDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}












    




public void MostrarAlmacen() throws ClassNotFoundException, SQLException {

    TituloAlmacen();
   

    ArrayList<Almacen> LsitaTerminada = ListaDeAlamcen();

    modeloAlmacen = (DefaultTableModel) Vista_Registrar_Almacen.TablaAlmacen.getModel();
    
    
       TableRowSorter<?> sorterOriginal = null;
    if (Vista_Registrar_Almacen.TablaAlmacen.getRowSorter() != null) {
        sorterOriginal = (TableRowSorter<?>) Vista_Registrar_Almacen.TablaAlmacen.getRowSorter();
        Vista_Registrar_Almacen.TablaAlmacen.setRowSorter(null);
    }
    
    limpiarTablaAlmacen();

    // Ahora necesitamos 7 columnas (id, pasillo, ala, estante, capacidad, producto, stock)
    Object[] obj = new Object[9];

    for (Almacen almacen : LsitaTerminada) {
        obj[0] = almacen.getId_Ubicacion();
        obj[1] = almacen.getPasillo();
        obj[2] = almacen.getAla();
        obj[3] = almacen.getEstante();
        obj [4] = almacen.getNivel();
        obj[5] = almacen.getCapacidad();
        obj[6] = almacen.getProducto();
        obj[7] = almacen.getStock();

        modeloAlmacen.addRow(obj);
    }

    
       if (sorterOriginal != null) {
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modeloAlmacen);
      Vista_Registrar_Almacen.TablaAlmacen.setRowSorter(sorter);
    }
  Vista_Registrar_Almacen.TablaAlmacen.setModel(modeloAlmacen);

    centrarTextoTabla(Vista_Registrar_Almacen.TablaAlmacen);

}













private ArrayList<Almacen> ListaDeAlamcen() throws ClassNotFoundException, SQLException {
    ArrayList<Almacen> ListaCompleta = new ArrayList<>();

    String sql = "SELECT  " +
            "a.id_Ubicacion, " +
            "a.pasillo AS Pasillo_Almacen, " +
            "a.ala, " +
            "a.estante, " +
            "a.nivel, " +
            "a.capacidad, " +
            "p.nombre AS nombreProducto, " +
            "i.Cantidad_Disponible " +
            "FROM almacen a " +
            "LEFT JOIN inventario i ON a.id_Ubicacion = i.id_Ubicacion " +  // relación por id_Ubicacion
            "LEFT JOIN producto p ON i.idProducto = p.idProducto " +
             "ORDER BY a.id_Ubicacion";

    try {
        this.conectar();
        ps = this.con.prepareStatement(sql);
        rs = ps.executeQuery();

        while (rs.next()) {
            Almacen al = new Almacen();
            al.setId_Ubicacion(rs.getInt("id_Ubicacion"));
            al.setPasillo(rs.getString("Pasillo_Almacen"));
            al.setAla(rs.getString("ala"));
            al.setEstante(rs.getString("estante"));
            al.setNivel(rs.getInt("nivel"));
            al.setCapacidad(rs.getInt("capacidad"));
            al.setProducto(rs.getString("nombreProducto"));
            al.setStock(rs.getString("Cantidad_Disponible"));

            ListaCompleta.add(al);
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al obtener la lista del almacén: " + e.getMessage());
    } finally {
        this.cerrarCn();
    }

    return ListaCompleta;
}



        
public void CargarComboxBoxAlmecenInventarioLiquido() throws ClassNotFoundException, SQLException {
    ArrayList<Almacen> almacens = this.ListaDeAlamcen();

    // Usamos LinkedHashSet para eliminar duplicados manteniendo el orden
    Set<Almacen> almacensUnicos = new LinkedHashSet<>(almacens);

    // Limpiamos el ComboBox
    InventarioLiquido.jComboUbicacion.removeAllItems();

    // Agregamos solo combinaciones únicas de pasillo y estante
    for (Almacen al : almacensUnicos) {
        InventarioLiquido.jComboUbicacion.addItem(al);
    }
}

    
    

public void agregarBuscadorAlmacenInventarioLiquido() {
    if (InventarioLiquido.txtBuscarUbicacion!= null) {
       InventarioLiquido.txtBuscarUbicacion.getDocument().addDocumentListener(new DocumentListener() {
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
    String texto = InventarioLiquido.txtBuscarUbicacion.getText().toLowerCase();

    try {
        // Lista completa de almacenes
        ArrayList<Almacen> lista = this.ListaDeAlamcen();

        // Limpiar ComboBox
        InventarioLiquido.jComboUbicacion.removeAllItems();

        // Agregar coincidencias por pasillo, estante, capacidad, nivel o ala
        for (Almacen p : lista) {
            String pasillo   = p.getPasillo()   != null ? p.getPasillo().toLowerCase()   : "";
            String estante   = p.getEstante()   != null ? p.getEstante().toLowerCase()   : "";
            String ala       = p.getAla()       != null ? p.getAla().toLowerCase()       : "";
            String capacidad = String.valueOf(p.getCapacidad());
            String nivel     = String.valueOf(p.getNivel());

            if (pasillo.contains(texto) || 
                estante.contains(texto) || 
                ala.contains(texto) ||
                capacidad.contains(texto) || 
                nivel.contains(texto)) {

               InventarioLiquido.jComboUbicacion.addItem(p);
            }
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
}




public void agregarFiltroBusqueda() {
    // Verifica que el campo de búsqueda y la tabla existan
    if (Vista_Registrar_Almacen.txtBuscarProductoEnTabla != null && Vista_Registrar_Almacen.TablaAlmacen != null) {

        // Agregamos un listener persistente
     Vista_Registrar_Almacen.txtBuscarProductoEnTabla.getDocument().addDocumentListener(new DocumentListener() {

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
                String texto = Vista_Registrar_Almacen.txtBuscarProductoEnTabla.getText().trim();

                // Siempre obtener el modelo actual
                DefaultTableModel modeloActual = (DefaultTableModel) Vista_Registrar_Almacen.TablaAlmacen.getModel();

                // Verificar o crear sorter
                TableRowSorter<?> sorter = (TableRowSorter<?>) Vista_Registrar_Almacen.TablaAlmacen.getRowSorter();
                if (sorter == null || sorter.getModel() != modeloActual) {
                    sorter = new TableRowSorter<>(modeloActual);
                   Vista_Registrar_Almacen.TablaAlmacen.setRowSorter(sorter);
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










private void actualizarComboBoxSolido() {
    String texto = InventarioSolido.txtBuscarUbicacion.getText().toLowerCase();

    try {
        // Lista completa de almacenes
        ArrayList<Almacen> lista = this.ListaDeAlamcen();

        // Limpiar ComboBox
     InventarioSolido.jComboUbicacionSolido.removeAllItems();

        // Agregar coincidencias por pasillo, estante, capacidad, nivel o ala
        for (Almacen p : lista) {
            String pasillo   = p.getPasillo()   != null ? p.getPasillo().toLowerCase()   : "";
            String estante   = p.getEstante()   != null ? p.getEstante().toLowerCase()   : "";
            String ala       = p.getAla()       != null ? p.getAla().toLowerCase()       : "";
            String capacidad = String.valueOf(p.getCapacidad());
            String nivel     = String.valueOf(p.getNivel());

            if (pasillo.contains(texto) || 
                estante.contains(texto) || 
                ala.contains(texto) ||
                capacidad.contains(texto) || 
                nivel.contains(texto)) {

               InventarioSolido.jComboUbicacionSolido.addItem(p);
            }
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
}




        
public void CargarComboxBoxAlmecenSolido() throws ClassNotFoundException, SQLException {
    ArrayList<Almacen> almacens = this.ListaDeAlamcen();

    // Usamos LinkedHashSet para eliminar duplicados manteniendo el orden
    Set<Almacen> almacensUnicos = new LinkedHashSet<>(almacens);
    if (InventarioSolido.jComboUbicacionSolido != null) {
        
    
    // Limpiamos el ComboBox
    InventarioSolido.jComboUbicacionSolido.removeAllItems();

    // Agregamos solo combinaciones únicas de pasillo y estante
    for (Almacen al : almacensUnicos) {
         InventarioSolido.jComboUbicacionSolido.addItem(al);
    }
    }
}

    

public void agregarBuscadorAlmacenSolido() {
    if (InventarioSolido.txtBuscarUbicacion!= null) {
      InventarioSolido.txtBuscarUbicacion.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                actualizarComboBoxSolido();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                actualizarComboBoxSolido();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                actualizarComboBoxSolido();
            }
        });
    }
}





private void actualizarComboBoxUnidad() {
    String texto = InventarioUnidad.txtBuscarUbicacion.getText().toLowerCase();

    try {
        // Lista completa de almacenes
        ArrayList<Almacen> lista = this.ListaDeAlamcen();

        // Limpiar ComboBox
       InventarioUnidad.jComboBoxUbicacion.removeAllItems();

        // Agregar coincidencias por pasillo, estante, capacidad, nivel o ala
        for (Almacen p : lista) {
            String pasillo   = p.getPasillo()   != null ? p.getPasillo().toLowerCase()   : "";
            String estante   = p.getEstante()   != null ? p.getEstante().toLowerCase()   : "";
            String ala       = p.getAla()       != null ? p.getAla().toLowerCase()       : "";
            String capacidad = String.valueOf(p.getCapacidad());
            String nivel     = String.valueOf(p.getNivel());

            if (pasillo.contains(texto) || 
                estante.contains(texto) || 
                ala.contains(texto) ||
                capacidad.contains(texto) || 
                nivel.contains(texto)) {

                 InventarioUnidad.jComboBoxUbicacion.addItem(p);
            }
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
}




        
public void CargarComboxBoxAlmecenuUNIDAD() throws ClassNotFoundException, SQLException {
    ArrayList<Almacen> almacens = this.ListaDeAlamcen();

    // Usamos LinkedHashSet para eliminar duplicados manteniendo el orden
    Set<Almacen> almacensUnicos = new LinkedHashSet<>(almacens);

    // Limpiamos el ComboBox
InventarioUnidad.jComboBoxUbicacion.removeAllItems();

    // Agregamos solo combinaciones únicas de pasillo y estante
    for (Almacen al : almacensUnicos) {
        InventarioUnidad.jComboBoxUbicacion.addItem(al);
    }
}

    

public void agregarBuscadorAlmacenUNIDAD() {
    if (  InventarioUnidad.txtBuscarUbicacion!= null) {
       InventarioUnidad.txtBuscarUbicacion.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                actualizarComboBoxUnidad();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                actualizarComboBoxUnidad();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                actualizarComboBoxUnidad();
            }
        });
    }
}



}
    
    
    
    
    
    
    
    
    
    
    
    
    

    
    
    
    

