/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ModeloDAO;

import  Modelo.*;
import Vista_Gestion_Categoria.frm_Categoria;

import java.sql.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;

import Vista_Gestion_Productos.Vista_Registro_Producto_Liquido;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import Vista_Gestion_Productos.Vista_Registrar_Producto_Unidad;
import Vista_Gestion_Productos.Vista_Registrar_Producto_Solido;

public class CategoriaDao  extends ConexiónBD{


PreparedStatement ps;
ResultSet rs;
Categoria c = new  Categoria();
Controlador.controladorVisual ct = new  Controlador.controladorVisual();



 DefaultTableModel modeloCategoria = new DefaultTableModel();
    
DefaultTableModel modeloProducto = new DefaultTableModel();
    
    
    
    // Metodo para los titulos de la tabla empleado con el modelo de la tabla
      public void TituloCategoriaProducto (){
   
    String Titulo [] = { " Producto "};
    modeloProducto.setColumnIdentifiers(Titulo);
   frm_Categoria.TablaProductoEnCategoria.setModel(modeloProducto);
 
    
}

public void TituloCategoria (){
     
    String Titulo [] = { "ID"," Categoria "};
    modeloCategoria.setColumnIdentifiers(Titulo);
   frm_Categoria.tablaCategoria.setModel(modeloCategoria);
 
    
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
private void limpiarTablaCategoria (){
 int fila = modeloCategoria.getRowCount();
    if (fila > 0) {
        for (int i = 0; i < fila; i++) {
            modeloCategoria.removeRow(0);
        }
        
    }
    
}

 private void limpiarTabla(DefaultTableModel modelo) {
    modelo.setRowCount(0);  // elimina todas las filas
}

public  void  EscondertxtEnCAtegoria (){
    
 frm_Categoria.lblId.setVisible(false);
 frm_Categoria.txtId.setVisible(false);
    frm_Categoria.btbConfirmarActualizacion.setVisible(false);
    frm_Categoria.btbCancelar.setVisible(false);
    frm_Categoria.btbActualizarVerdadero.setVisible(true);
    frm_Categoria.btbEliminar.setVisible(true);
    frm_Categoria.btbRegistrar.setVisible(true);
    frm_Categoria.txtNombreCategoria.setText("");
      
    
}

public void MostrarCategorias() throws ClassNotFoundException, SQLException {
    TituloCategoria();
    limpiarTablaCategoria();
    
    ArrayList<Categoria> Lista = ListadeCategoria(); // ← ahora viene ordenada por ID
    
    modeloCategoria = (DefaultTableModel) frm_Categoria.tablaCategoria.getModel();
    modeloCategoria.setRowCount(0);
    frm_Categoria.tablaCategoria.setRowSorter(null);

    Object[] obj = new Object[2];

    for (int i = 0; i < Lista.size(); i++) {
        obj[0] = Lista.get(i).getIdCategoria();  // ID real
        obj[1] = Lista.get(i).getNombre();       // Nombre
        modeloCategoria.addRow(obj);
    }

    frm_Categoria.tablaCategoria.setModel(modeloCategoria);
    centrarTextoTabla(frm_Categoria.tablaCategoria);

    // Ordenamiento normal (el usuario puede ordenar por ID o por Nombre)
    TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modeloCategoria);
    frm_Categoria.tablaCategoria.setRowSorter(sorter);
}


public  void RegistrarCategoria () throws ClassNotFoundException,SQLException{
    
 c.setNombre(Vista_Gestion_Categoria.frm_Categoria.txtNombreCategoria.getText());

 
 String sql = "INSERT INTO categoria (Nombre) values (?)";
 
 
    try {
        this.conectar();
        ps = this.con.prepareStatement(sql);
        ps.setString(1,c.getNombre());
        if (ps.executeUpdate() > 0) {
            JOptionPane.showMessageDialog(null,"Registro exitoso de categoria");
            frm_Categoria.txtNombreCategoria.setText("");
            MostrarCategorias();
            
        }
        
        
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al registrar Categoria" + e.getMessage());
    }   
}




public ArrayList<Categoria> ListadeCategoria() throws ClassNotFoundException, SQLException {
    ArrayList<Categoria> lista = new ArrayList<>();
    
    // CAMBIO CLAVE: ordenar por idCategoria, NO por Nombre
    String sql = "SELECT idCategoria, Nombre FROM categoria ORDER BY idCategoria ASC";
    
    try {
        this.conectar();
        ps = this.con.prepareStatement(sql);
        rs = ps.executeQuery();
        
        while (rs.next()) {
            Categoria ct = new Categoria();
            ct.setIdCategoria(rs.getInt("idCategoria"));
            ct.setNombre(rs.getString("Nombre"));
            lista.add(ct);
        }
        
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al obtener categorías: " + e.getMessage());
    } finally {
        this.cerrarCn();
    }
    
    return lista;
}

public  void EliminarCategoria ( int IdCategoria) throws  ClassNotFoundException,SQLException {
    
    
    
    String sql = "DELETE FROM categoria WHERE idCategoria = ?";
    
    try {
        this.conectar();
       ps = this.con.prepareStatement(sql);
       
       ps.setInt(1,IdCategoria);
       
        if (ps.executeUpdate() > 0) {
            JOptionPane.showMessageDialog(null," Categoria eliminada exitosamente");
            
        }
        
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, " No puedes eliminar esta categoria por tiene productos " );
    } finally {
        this.cerrarCn();
    }
    
    
}


public  void ElimanarConseguridad () throws ClassNotFoundException, SQLException{
    
  int fila = frm_Categoria.tablaCategoria.getSelectedRow();
  
  
    if (fila == -1) {
        JOptionPane.showMessageDialog(null, "Debe seleccionar una categoria si la desea eliminar ");
       
        return;
    }
    
    int idCategoria = Integer.parseInt(frm_Categoria.tablaCategoria.getValueAt(fila, 0).toString());
    
    int Confirmar = JOptionPane.showConfirmDialog(null, "Estas seguro de eliminar esta categoria","Confirmar Eliminacion",JOptionPane.YES_NO_OPTION);
    
    
    if (Confirmar == JOptionPane.YES_OPTION) {
        
        EliminarCategoria(idCategoria);
        MostrarCategorias();
        
    }
   
    
    
    
}




public  void ActualizarCategoria (Categoria c) throws  ClassNotFoundException,SQLException {
    
    c.setNombre(frm_Categoria.txtNombreCategoria.getText());
    c.setIdCategoria(Integer.parseInt((frm_Categoria.txtId.getText())));
    
 String sql = "UPDATE categoria SET Nombre = ? WHERE idCategoria=?";   
 
    try {
        this.conectar();
        ps = this.con.prepareStatement(sql);
        ps.setString(1, c.getNombre());
        ps.setInt(2, c.getIdCategoria());
        
        if (ps.executeUpdate() > 0) {
            JOptionPane.showMessageDialog(null, " Actualizacion exitosa"); 
            EscondertxtEnCAtegoria();
            MostrarCategorias();
            
           cargarComboCategorias();
             
        } 
        
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al actualizar la categoria "  + e.getMessage());
    }
    
    
}

public void cargarComboCategorias() throws ClassNotFoundException, SQLException {
    // Consulta limpia: solo nombres de categorías, sin JOIN → nunca se repiten
    String sql = "SELECT Nombre FROM categoria ORDER BY Nombre ASC";
    
    frm_Categoria.ComboxProductoEnCategoria.removeAllItems();
   frm_Categoria.ComboxProductoEnCategoria.addItem("Seleccione una categoría"); // Opción por defecto
    
    try {
        this.conectar();
        ps = con.prepareStatement(sql);
        rs = ps.executeQuery();
        
        while (rs.next()) {
            String nombreCategoria = rs.getString("Nombre");
            frm_Categoria.ComboxProductoEnCategoria.addItem(nombreCategoria);
            centrarTextoTabla(frm_Categoria.TablaProductoEnCategoria);
        }
        
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al cargar categorías en el filtro: " + e.getMessage());
    } finally {
        this.cerrarCn();
    }
}

private int obtenerIdCategoriaSeleccionada() throws SQLException {
    String nombreSeleccionado = (String) frm_Categoria.ComboxProductoEnCategoria.getSelectedItem();
    
    if (nombreSeleccionado == null || 
        nombreSeleccionado.equals("Seleccione una categoría") || 
        nombreSeleccionado.trim().isEmpty()) {
        return -1; // No hay categoría válida seleccionada
    }
    
    String sql = "SELECT idCategoria FROM categoria WHERE Nombre = ?";
    int id = -1;
    
    try {
        this.conectar();
        ps = con.prepareStatement(sql);
        ps.setString(1, nombreSeleccionado);
        rs = ps.executeQuery();
        
        if (rs.next()) {
            id = rs.getInt("idCategoria");
        }
        
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error al obtener ID de categoría: " + e.getMessage());
    } finally {
        this.cerrarCn();
    }
    
    return id;
}

public void filtrarProductosPorCategoria() throws ClassNotFoundException, SQLException {
    String nombreSeleccionado = (String) frm_Categoria.ComboxProductoEnCategoria.getSelectedItem();
    
    // Limpiar la tabla de productos
    DefaultTableModel modelo = (DefaultTableModel) frm_Categoria.TablaProductoEnCategoria.getModel();
    modelo.setRowCount(0);
    
    // Validar selección
    if (nombreSeleccionado == null || 
        nombreSeleccionado.trim().isEmpty() || 
        nombreSeleccionado.equals("Seleccione una categoría") ||
        nombreSeleccionado.equals("Seleccionar categoría...")) {
        return;
    }
    
    String nombreLimpio = nombreSeleccionado.trim();
    
    try {
        this.conectar();
        
        // 1. Obtener idCategoria por nombre (ignorando mayúsculas y espacios)
        String sqlCategoria = "SELECT idCategoria FROM categoria WHERE TRIM(UPPER(Nombre)) = ?";
        ps = con.prepareStatement(sqlCategoria);
        ps.setString(1, nombreLimpio.toUpperCase());
        rs = ps.executeQuery();
        
        if (!rs.next()) {
            JOptionPane.showMessageDialog(null, 
                "Categoría no encontrada: '" + nombreSeleccionado + "'");
            return;
        }
        
        int idCategoria = rs.getInt("idCategoria");
        
        // 2. Traer SOLO el nombre de los productos de esa categoría
        String sqlProductos = "SELECT Nombre FROM producto WHERE idCategoria = ? ORDER BY Nombre";
        ps = con.prepareStatement(sqlProductos);
        ps.setInt(1, idCategoria);
        rs = ps.executeQuery();
        
        boolean hayProductos = false;
        while (rs.next()) {
            hayProductos = true;
            modelo.addRow(new Object[]{ rs.getString("Nombre") }); // Solo el nombre
        }
        
        if (!hayProductos) {
            JOptionPane.showMessageDialog(null, 
                "La categoría '" + nombreSeleccionado + "' no tiene productos aún.");
        }
        
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al filtrar productos: " + e.getMessage());
    } finally {
        this.cerrarCn();
    }
}



public void CargarComboxBoxCategorialIQUIDO() throws ClassNotFoundException, SQLException {
    ArrayList<Categoria> categorias = this.ListadeCategoria();

    // Usamos LinkedHashSet para eliminar duplicados manteniendo el orden
    Set<Categoria> categoriasUnicas = new LinkedHashSet<>(categorias);

    // Limpiamos el ComboBox
Vista_Registro_Producto_Liquido.jComboCategoria.removeAllItems();

    // Agregamos solo categorías únicas
    for (Categoria c : categoriasUnicas) {
       Vista_Registro_Producto_Liquido.jComboCategoria.addItem(c);
    }
}




public void CargarComboxBoxCategoriSolido() throws ClassNotFoundException, SQLException {
    ArrayList<Categoria> categorias = this.ListadeCategoria();

    // Usamos LinkedHashSet para evitar duplicados y mantener el orden
    Set<Categoria> categoriasUnicas = new LinkedHashSet<>(categorias);

    // Limpiamos el ComboBox
    Vista_Registrar_Producto_Solido.jComboCategoria.removeAllItems();

    // Agregamos solo categorías únicas
    for (Categoria c : categoriasUnicas) {
        Vista_Registrar_Producto_Solido.jComboCategoria.addItem(c);
    }
}


public void CargarComboxBoxCategoriUnidad() throws ClassNotFoundException, SQLException {
    ArrayList<Categoria> categorias = this.ListadeCategoria();

    // Usamos LinkedHashSet para evitar duplicados y mantener el orden
    Set<Categoria> categoriasUnicas = new LinkedHashSet<>(categorias);

    // Limpiamos el ComboBox
    Vista_Registrar_Producto_Unidad.jComboxCategoria.removeAllItems();

    // Agregamos solo categorías únicas
    for (Categoria c : categoriasUnicas) {
       Vista_Registrar_Producto_Unidad.jComboxCategoria.addItem(c);
    }
}




public void agregarBuscadorCategoriaLiquido() {
    if (Vista_Registro_Producto_Liquido.txtBuscarCategoria != null) {
        Vista_Registro_Producto_Liquido.txtBuscarCategoria.getDocument().addDocumentListener(new DocumentListener() {
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
    String texto = Vista_Registro_Producto_Liquido.txtBuscarCategoria.getText().toLowerCase();

    try {
        // Lista completa de productos
        ArrayList<Categoria> lista = this.ListadeCategoria();

        // Limpiar ComboBox
        Vista_Registro_Producto_Liquido.jComboCategoria.removeAllItems();

        // Agregar coincidencias
        for (Categoria p : lista) {
            if (p.getNombre().toLowerCase().contains(texto)) {
                Vista_Registro_Producto_Liquido.jComboCategoria.addItem(p);
            }
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
}

public void agregarFiltroBusqueda() {
    // Agregamos un solo listener persistente al campo de búsqueda
    frm_Categoria.txtBuscar.getDocument().addDocumentListener(new DocumentListener() {

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
            String texto = frm_Categoria.txtBuscar.getText().trim();

            // Siempre obtener el modelo actual de la tabla
            DefaultTableModel modeloActual = (DefaultTableModel) frm_Categoria.tablaCategoria.getModel();

            // Verificar si el sorter actual coincide con el modelo, si no, crear uno nuevo
            TableRowSorter<?> sorter = (TableRowSorter<?>) frm_Categoria.tablaCategoria.getRowSorter();
            if (sorter == null || sorter.getModel() != modeloActual) {
                sorter = new TableRowSorter<>(modeloActual);
                frm_Categoria.tablaCategoria.setRowSorter(sorter);
            }

            // Aplicar o limpiar el filtro según el texto
            if (texto.isEmpty()) {
                sorter.setRowFilter(null);
            } else {
                try {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(texto)));
                } catch (PatternSyntaxException ex) {
                    sorter.setRowFilter(null); // evita error si el usuario escribe símbolos especiales
                }
            }
        }
    });
}


      
      

public void agregarBuscadorCategoriaSolido() {
    if (Vista_Registrar_Producto_Solido.txtBuscarCategoria != null) {
        Vista_Registrar_Producto_Solido.txtBuscarCategoria.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                actualizarComboSolido();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                actualizarComboSolido();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                actualizarComboSolido();
            }
        });
    }
}


    private void actualizarComboSolido() {
    String texto =Vista_Registrar_Producto_Solido.txtBuscarCategoria.getText().toLowerCase();

    try {
        // Lista completa de productos
        ArrayList<Categoria> lista = this.ListadeCategoria();

        // Limpiar ComboBox
        Vista_Registrar_Producto_Solido.jComboCategoria.removeAllItems();

        // Agregar coincidencias
        for (Categoria p : lista) {
            if (p.getNombre().toLowerCase().contains(texto)) {
              Vista_Registrar_Producto_Solido.jComboCategoria.addItem(p);
            }
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
}


public void agregarBuscadorCategoriaUnida() {
    if (Vista_Registrar_Producto_Unidad.txtBuscarCategoria != null) {
        Vista_Registrar_Producto_Unidad.txtBuscarCategoria.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                actualizarComboUnidad();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                actualizarComboUnidad();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                actualizarComboUnidad();
            }
        });
    }
}


    private void actualizarComboUnidad() {
    String texto =Vista_Registrar_Producto_Unidad.txtBuscarCategoria.getText().toLowerCase();

    try {
        // Lista completa de productos
        ArrayList<Categoria> lista = this.ListadeCategoria();

        // Limpiar ComboBox
        Vista_Registrar_Producto_Unidad.jComboxCategoria.removeAllItems();

        // Agregar coincidencias
        for (Categoria p : lista) {
            if (p.getNombre().toLowerCase().contains(texto)) {
              Vista_Registrar_Producto_Unidad.jComboxCategoria.addItem(p);
            }
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
}

    
}
