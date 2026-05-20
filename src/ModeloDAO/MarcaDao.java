
package ModeloDAO;

import  Modelo.*;

import java.sql.*;
import Vista_Gestion_Marca.Gestionar_Marca;
import Vista_Usuari_Empleado.Menu_Sistema;
import java.util.ArrayList;
import java.util.Comparator;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import Vista_Gestion_Productos.Vista_Registro_Producto_Liquido;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.SwingConstants;
import Vista_Gestion_Productos.Vista_Registrar_Producto_Solido;
import Vista_Gestion_Productos.Vista_Registrar_Producto_Unidad;

public class MarcaDao  extends  ConexiónBD{
    
    PreparedStatement ps;
ResultSet rs;



Marca m = new Marca();

 DefaultTableModel modeloMarca = new DefaultTableModel();
    
 DefaultTableModel modeloFitral = new DefaultTableModel();
    
    public void TituloMarca (){
   
    String Titulo [] = { "ID","  MARCA  ", " PAIS"};
    modeloMarca.setColumnIdentifiers(Titulo);
  Gestionar_Marca.TablaMarca.setModel(modeloMarca);
 
    
}
    
     
    public void TituloMarcaFitarl(){
   
    String Titulo [] = { " PRODUCTO "  };
    modeloFitral.setColumnIdentifiers(Titulo);
  Gestionar_Marca.TablaProductoPorMarca.setModel(modeloFitral);
        centrarTextoTabla(Gestionar_Marca.TablaProductoPorMarca);
 
    
}
  
   public void centrarTextoTabla(JTable tabla) {
    DefaultTableCellRenderer centrado = new DefaultTableCellRenderer();
    centrado.setHorizontalAlignment(SwingConstants.CENTER); // mejor usar SwingConstants

    // Recorremos todas las columnas visibles
    for (int i = 0; i < tabla.getColumnModel().getColumnCount(); i++) {
        tabla.getColumnModel().getColumn(i).setCellRenderer(centrado);
    }
}

 // Metodo para limpiar la tabla  empleado 
private void limpiarTablaMarca(){
 int fila = modeloMarca.getRowCount();
    if (fila > 0) {
        for (int i = 0; i < fila; i++) {
            modeloMarca.removeRow(0);
        }
        
    }
    
}
    
      
    private void limpiarTabla(DefaultTableModel modelo) {
    modelo.setRowCount(0);  // elimina todas las filas
}
  


public  void RegistrarMarca () throws ClassNotFoundException,SQLException {
    
    m.setNombre(Gestionar_Marca.txtNombre.getText());
    m.setPaisOrigen(Gestionar_Marca.txtPais.getText());
    
    
    String sql = " INSERT INTO marca (Nombre,PaisOrigen) values (?,?)";
    
    try {
        this.conectar();
        ps = this.con.prepareStatement(sql);
        
        ps.setString(1,m.getNombre());
        ps.setString(2,m.getPaisOrigen());
        
        if (ps.executeUpdate() > 0 ) {
            JOptionPane.showMessageDialog(null, " Registro exitoso de marca");
           Gestionar_Marca.txtNombre.setText("");
           Gestionar_Marca.txtPais.setText("");
           cargarComboMarcasFitaral();
            
          MostrarMARCA();
          
        } 
        
        
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, " Error al registrar Marca  " + e.getMessage());
    }
      
}




private ArrayList<Marca> ListaDeMarca() throws ClassNotFoundException, SQLException {
    ArrayList<Marca> lista = new ArrayList<>();
    
    // ORDENAR POR ID (esto es lo que querías)
    String sql = "SELECT idMarca, Nombre, PaisOrigen FROM marca ORDER BY idMarca ASC";
    
    try {
        this.conectar();
        ps = this.con.prepareStatement(sql);
        rs = ps.executeQuery();
        
        while (rs.next()) {
            Marca mr = new Marca();
            mr.setIdMarca(rs.getInt("idMarca"));
            mr.setNombre(rs.getString("Nombre"));
            mr.setPaisOrigen(rs.getString("PaisOrigen"));
            lista.add(mr);
        }
        
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al cargar marcas: " + e.getMessage());
    } finally {
        this.cerrarCn();
    }
    
    return lista;
}
 
 
 
  

public void MostrarMARCA() throws ClassNotFoundException, SQLException {
    TituloMarca();
    limpiarTablaMarca();
    
    ArrayList<Marca> lista = ListaDeMarca();
    
    modeloMarca = (DefaultTableModel) Gestionar_Marca.TablaMarca.getModel();
    modeloMarca.setRowCount(0);
    Gestionar_Marca.TablaMarca.setRowSorter(null);

    for (Marca marca : lista) {
        Object[] fila = {
            marca.getIdMarca(),
            marca.getNombre(),
            marca.getPaisOrigen()
            // NO agregues producto aquí → ya no existe en el modelo
        };
        modeloMarca.addRow(fila);
    }

    Gestionar_Marca.TablaMarca.setModel(modeloMarca);
    centrarTextoTabla(Gestionar_Marca.TablaMarca);

    TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modeloMarca);
    Gestionar_Marca.TablaMarca.setRowSorter(sorter);
}



public void cargarComboMarcasFitaral() throws ClassNotFoundException, SQLException {
    Gestionar_Marca.comboxMarca.removeAllItems();
    Gestionar_Marca.comboxMarca.addItem("Seleccione una marca");
    
    String sql = "SELECT Nombre FROM marca ORDER BY Nombre ASC";
    
    try {
        this.conectar();
        ps = this.con.prepareStatement(sql);
        rs = ps.executeQuery();
        
        while (rs.next()) {
            Gestionar_Marca.comboxMarca.addItem(rs.getString("Nombre"));
        }
        
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al cargar marcas en combo: " + e.getMessage());
    } finally {
        this.cerrarCn();
    }
}



public void filtrarProductosPorMarca() throws ClassNotFoundException, SQLException {
    String nombreMarca = (String) Gestionar_Marca.comboxMarca.getSelectedItem();
    
    // Limpiar tabla
    DefaultTableModel modelo = (DefaultTableModel) Gestionar_Marca.TablaProductoPorMarca.getModel();
    modelo.setRowCount(0);
    
    if (nombreMarca == null || nombreMarca.equals("Seleccione una marca")) {
        return;
    }
    
    try {
        this.conectar();
        
        // 1. Obtener idMarca por nombre
        String sqlId = "SELECT idMarca FROM marca WHERE TRIM(UPPER(Nombre)) = ?";
        ps = con.prepareStatement(sqlId);
        ps.setString(1, nombreMarca.trim().toUpperCase());
        rs = ps.executeQuery();
        
        if (!rs.next()) {
            JOptionPane.showMessageDialog(null, "Marca no encontrada.");
            return;
        }
        
        int idMarca = rs.getInt("idMarca");
        
        // 2. Traer solo nombres de productos de esa marca
        String sqlProd = "SELECT Nombre FROM producto WHERE idMarca = ? ORDER BY Nombre";
        ps = con.prepareStatement(sqlProd);
        ps.setInt(1, idMarca);
        rs = ps.executeQuery();
        
        boolean hay = false;
        while (rs.next()) {
            hay = true;
            modelo.addRow(new Object[]{ rs.getString("Nombre") });
        }
        
        if (!hay) {
            JOptionPane.showMessageDialog(null, 
                "La marca '" + nombreMarca + "' no tiene productos registrados.");
        }
        
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al filtrar productos: " + e.getMessage());
    } finally {
        this.cerrarCn();
    }
}



public void CargarComboxBoxMarcaLiquido() throws ClassNotFoundException, SQLException {
    ArrayList<Marca> listaMarca = this.ListaDeMarca();

    // Usamos TreeSet para ordenar y eliminar duplicados (orden alfabético)
    Set<Marca> marcasOrdenadas = new TreeSet<>(
        Comparator.comparing(m -> m.Nombre.toLowerCase())
    );

    marcasOrdenadas.addAll(listaMarca);

    // Limpiamos el ComboBox
    Vista_Registro_Producto_Liquido.jComboMarca.removeAllItems();

    // Agregamos marcas ordenadas y únicas
    for (Marca mt : marcasOrdenadas) {
        Vista_Registro_Producto_Liquido.jComboMarca.addItem(mt);
    }
}

    
    public  void EliminarMarca(int idMarca) throws ClassNotFoundException,SQLException{
        
        
         String sql = "DELETE FROM marca WHERE idMarca = ?";
         
         try {
            this.conectar();
            ps = this.con.prepareStatement(sql);
            
            ps.setInt(1, idMarca);
           
             if (ps.executeUpdate() > 0 ) {
                 JOptionPane.showMessageDialog(null, "Se elimino Correctamenete esta marca");
                 
             }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, " No se puede eliminar esta marca porque hay producto activo con esta marca " );
            
        } finally {
             this.cerrarCn();
         }
        
      
    }



public  void EliminarMarcaConseguridad () throws ClassNotFoundException, SQLException{
    
   int fila = Gestionar_Marca.TablaMarca.getSelectedRow();
   
    if (fila == -1) {
        JOptionPane.showMessageDialog(null, " Debe seleccionar una fila si desea eliminar una marca");
        
        return;
    }  
           
    
    
    int idMarca = Integer.parseInt(Gestionar_Marca.TablaMarca.getValueAt(fila, 0).toString());
    
    
    int Confirmar = JOptionPane.showConfirmDialog(null, "Estas seguro de eliminar esta marca", "Confirma Eliminacion",JOptionPane.YES_NO_OPTION);
    
    if (Confirmar == JOptionPane.YES_OPTION) {
        
        EliminarMarca(idMarca);
        MostrarMARCA();
        
    }
   
    
}

  

public  void ActualizarMarca (Marca mt) throws  ClassNotFoundException,SQLException {
    
    
   mt.setNombre(Gestionar_Marca.txtNombre.getText());
  mt.setPaisOrigen(Gestionar_Marca.txtPais.getText());
   mt.setIdMarca(Integer.parseInt(Gestionar_Marca.txtId.getText()));
   
   String SQL = "UPDATE marca SET Nombre=?,PaisOrigen =? WHERE idMarca=?";
   
    try {
        this.conectar();
        ps = this.con.prepareStatement(SQL);
        
        ps.setString(1, mt.getNombre());
        ps.setString(2,mt.getPaisOrigen());
        ps.setInt(3, mt.getIdMarca());
       
        if (ps.executeUpdate() >0) {
            JOptionPane.showMessageDialog(null, "Actualizacion Correcta");
            
              Gestionar_Marca.txtNombre.setText("");
              Gestionar_Marca.txtPais.setText(" ");
              Gestionar_Marca.lblId.setVisible(false);
              Gestionar_Marca.txtId.setVisible(false);
              Gestionar_Marca.btbConfirmarActualizacion.setVisible(false);
              Gestionar_Marca.btbRegistrar.setVisible(true);
              Gestionar_Marca.btbActualizarAntes.setVisible(true);
              Gestionar_Marca.btbEliminar.setVisible(true);
              Gestionar_Marca.btbCancelar.setVisible(false);
    
            MostrarMARCA();
            
        } else {
            JOptionPane.showMessageDialog(null," Erro");
        }
        
        
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al actualizar marca " + e.getMessage());
    }
    
 
}

  public  void EsconderTxtLblEnMarca (){
    
 Gestionar_Marca.lblId.setVisible(false);
 Gestionar_Marca.txtId.setVisible(false);
 Gestionar_Marca.btbConfirmarActualizacion.setVisible(false);
 Gestionar_Marca.btbCancelar.setVisible(false);

}







public void agregarBuscadorMarcaLiquido() {
    if (Vista_Registro_Producto_Liquido.txtBuscarMarca != null) {
        Vista_Registro_Producto_Liquido.txtBuscarMarca.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                actualizarComboBox();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                actualizarComboBox();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                actualizarComboBox();
            }
        });
    }
}


    private void actualizarComboBox() {
    String texto = Vista_Registro_Producto_Liquido.txtBuscarMarca.getText().toLowerCase();

    try {
        // Lista completa de productos
        ArrayList<Marca> lista = this.ListaDeMarca();

        // Limpiar ComboBox
        Vista_Registro_Producto_Liquido.jComboMarca.removeAllItems();

        // Agregar coincidencias
        for (Marca p : lista) {
            if (p.getNombre().toLowerCase().contains(texto)) {
                Vista_Registro_Producto_Liquido.jComboMarca.addItem(p);
            }
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
}

    
    
    
public void agregarFiltroBusqueda() {
    Gestionar_Marca.txtBuscar.getDocument().addDocumentListener(new DocumentListener() {

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
            String texto = Gestionar_Marca.txtBuscar.getText().trim();

            DefaultTableModel modeloActual = (DefaultTableModel) Gestionar_Marca.TablaMarca.getModel();

            TableRowSorter<?> sorter = (TableRowSorter<?>) Gestionar_Marca.TablaMarca.getRowSorter();
            if (sorter == null || sorter.getModel() != modeloActual) {
                sorter = new TableRowSorter<>(modeloActual);
                Gestionar_Marca.TablaMarca.setRowSorter(sorter);
            }

            if (texto.isEmpty()) {
                sorter.setRowFilter(null);
            } else {
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto));
            }
        }
    });
}


    
public void agregarBuscadorMarcaSolido() {
    if (Vista_Registrar_Producto_Solido.txtBuscarMarca != null) {
        Vista_Registrar_Producto_Solido.txtBuscarMarca.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                actualizarComboBoxsOLIDO();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                actualizarComboBoxsOLIDO();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                actualizarComboBoxsOLIDO();
            }
        });
    }
}


    private void actualizarComboBoxsOLIDO() {
    String texto = Vista_Registrar_Producto_Solido.txtBuscarMarca.getText().toLowerCase();

    try {
        // Lista completa de productos
        ArrayList<Marca> lista = this.ListaDeMarca();

        // Limpiar ComboBox
        Vista_Registrar_Producto_Solido.jComboMarca.removeAllItems();

        // Agregar coincidencias
        for (Marca p : lista) {
            if (p.getNombre().toLowerCase().contains(texto)) {
                Vista_Registrar_Producto_Solido.jComboMarca.addItem(p);
            }
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
}

    public void CargarComboxBoxMacarSolido() throws ClassNotFoundException, SQLException {
    ArrayList<Marca> listaMarca = this.ListaDeMarca();

    // Usamos HashSet para evitar duplicados (basado en equals y hashCode)
    Set<Marca> marcasUnicas = new LinkedHashSet<>(listaMarca);

    // Limpiamos el ComboBox
    Vista_Registrar_Producto_Solido.jComboMarca.removeAllItems();

    // Agregamos solo marcas únicas
    for (Marca mt : marcasUnicas) {
        Vista_Registrar_Producto_Solido.jComboMarca.addItem(mt);
    }
}

    
public void agregarBuscadorMarcaUnidad() {
    if (Vista_Registrar_Producto_Unidad.txtBuscarMarca != null) {
        Vista_Registrar_Producto_Unidad.txtBuscarMarca.getDocument().addDocumentListener(new DocumentListener() {
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


    private void actualizarComboBoxUnidad() {
    String texto = Vista_Registrar_Producto_Unidad.txtBuscarMarca.getText().toLowerCase();

    try {
        // Lista completa de productos
        ArrayList<Marca> lista = this.ListaDeMarca();

        // Limpiar ComboBox
        Vista_Registrar_Producto_Unidad.JcomoBoxMarca.removeAllItems();

        // Agregar coincidencias
        for (Marca p : lista) {
            if (p.getNombre().toLowerCase().contains(texto)) {
                Vista_Registrar_Producto_Unidad.JcomoBoxMarca.addItem(p);
            }
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
}

    public void CargarComboxBoxMacarUnidad() throws ClassNotFoundException, SQLException {
    ArrayList<Marca> listaMarca = this.ListaDeMarca();

    // Usamos HashSet para evitar duplicados (basado en equals y hashCode)
    Set<Marca> marcasUnicas = new LinkedHashSet<>(listaMarca);

    // Limpiamos el ComboBox
    Vista_Registrar_Producto_Unidad.JcomoBoxMarca.removeAllItems();

    // Agregamos solo marcas únicas
    for (Marca mt : marcasUnicas) {
        Vista_Registrar_Producto_Unidad.JcomoBoxMarca.addItem(mt);
    }
}


    
}
