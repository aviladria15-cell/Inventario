package ModeloDAO;


import javax.swing.*;
import java.awt.*;
import Modelo.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import Vista_Gestionar_Proveedor.Gestionar_Proveedor;
import java.sql.*;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import Vista_Gestion_Productos.Vista_Registro_Producto_Liquido;
import Vista_Gestion_Productos.Vista_Registrar_Producto_Solido;
import Vista_Gestion_Productos.Vista_Registrar_Producto_Unidad;
import Vista_GestionInventario.InventarioLiquido;
import  Vista_GestionInventario.InventarioSolido ;
import Vista_GestionInventario.InventarioUnidad;
public class ProductoDao extends ConexiónBD {

    private ResultSet rs;
    private PreparedStatement ps;
  ModeloDAO.InventarioUnidadDAO in = new InventarioUnidadDAO();
    Controlador.controladorVisual ct = new Controlador.controladorVisual();
    // 🔹 Define dos modelos separados
DefaultTableModel modeloProducto = new DefaultTableModel();

DefaultTableModel modeloProductoProveedor = new DefaultTableModel();

    private void limpiarTabla(DefaultTableModel modelo) {
    modelo.setRowCount(0);  // elimina todas las filas
}

 private void limpiarTablaProducto() {
        int fila = modeloProducto.getRowCount();
        while (fila > 0) {
            modeloProducto.removeRow(0);
            fila--;
        }
    }

  
// -------------------------
// Para Gestionar_Proveedor
// -------------------------
public void TituloProductoParaProveedor() {
    String Titulo[] = {"ID", "Nombre", "Categoria", "Marca", "Proveedor"};
    modeloProductoProveedor = new DefaultTableModel();
    modeloProductoProveedor.setColumnIdentifiers(Titulo);
    if (Gestionar_Proveedor.TablaProductoProveedore != null) {
        Gestionar_Proveedor.TablaProductoProveedore.setModel(modeloProductoProveedor);
    }
}

 public void MostrarListaProductoParaProveedor() throws ClassNotFoundException, SQLException {
     // Para mostrar solo TablaProductoProveedore
Gestionar_Proveedor.jScrollPane1.setVisible(false);
Gestionar_Proveedor.jScrollPane2.setVisible(true);

    TituloProductoParaProveedor();

    ArrayList<Producto> listaCargada = ListaProductosmENU();
    modeloProductoProveedor = (DefaultTableModel) Gestionar_Proveedor.TablaProductoProveedore.getModel();

    modeloProductoProveedor.setRowCount(0); // limpiar

    Object[] obj = new Object[7];
    for (Producto producto : listaCargada) {
        obj[0] = producto.getIdProducto();
        obj[1] = producto.getNombre();
        obj[2] = producto.getIdCategoria();
        obj[3] = producto.getIdMarca();
        obj[4] = producto.getIdProveedor();
        modeloProductoProveedor.addRow(obj);
    }

    Gestionar_Proveedor.TablaProductoProveedore.setModel(modeloProductoProveedor);

    // Centrar texto
    centrarTextoTabla(Gestionar_Proveedor.TablaProductoProveedore);

    // Nuevo RowSorter limpio
    TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modeloProductoProveedor);
    Gestionar_Proveedor.TablaProductoProveedore.setRowSorter(sorter);

    // Configuración de botones
    Gestionar_Proveedor.btbActualizarProveedor.setVisible(false);
    Gestionar_Proveedor.btbRegistrarProveedor.setVisible(false);
    Gestionar_Proveedor.btbEliminarProveedor.setVisible(false);
    Gestionar_Proveedor.btbCancelarProveedor.setVisible(false);
    
    Gestionar_Proveedor.btbCancelarProductoSuministrado.setVisible(true);

  
}

  
    /*
                  Productos Liquidos todo relacionado
        |
        |
        |
        |
        |
        |
        |
       \|/
        V
    */

 
DefaultTableModel modeloProductoLiquido = new DefaultTableModel();

public void TituloProductoLiquido() {
    String Titulo[] = {"ID", "Nombre", "Tipo de Liquido", "Viscosidad","Densidad","Presentación", "Categoria", "Marca"};

    modeloProductoLiquido.setColumnIdentifiers(Titulo);
    if (Vista_Registro_Producto_Liquido.TablaRegistroLiquido != null) {
        Vista_Registro_Producto_Liquido.TablaRegistroLiquido.setModel(modeloProductoLiquido);
    }
}



 
    public void registrarProducto(Producto p) throws ClassNotFoundException, SQLException {
        String sql = "INSERT INTO producto (nombre,tipo_Liquido,viscosidad,densidad,presentacion, idCategoria, idMarca) VALUES ( ?, ?, ?,?,?,?,?)";

        p.setNombre(Vista_Registro_Producto_Liquido.txtNombreProductoLiquido.getText());
        
        p.setTipo_Liquido(Vista_Registro_Producto_Liquido.txtTipodeLiquido.getText());
        
        p.setViscosidad(Vista_Registro_Producto_Liquido.txtViscosidad.getText());
        
        p.setPresentacion(Vista_Registro_Producto_Liquido.txtPresentacion.getText());
        p.setDensidad(Vista_Registro_Producto_Liquido.jComboDensidad.getSelectedItem().toString());
       

        Marca marcaSeleccionada = (Marca) Vista_Registro_Producto_Liquido.jComboMarca.getSelectedItem();
        int idMarca = marcaSeleccionada.getIdMarca();

        Categoria categoriaSeleccionada = (Categoria) Vista_Registro_Producto_Liquido.jComboCategoria.getSelectedItem();
        int idCategoria = categoriaSeleccionada.getIdCategoria();

        try {
            this.conectar();
            ps = this.con.prepareStatement(sql);
            ps.setString(1, p.getNombre());
            ps.setString(2,p.getTipo_Liquido());
            ps.setString(3, p.getViscosidad());
            ps.setString(4, p.getDensidad());
            ps.setString(5,p.getPresentacion());
            ps.setInt(6, idCategoria);
            ps.setInt(7, idMarca);
            if (ps.executeUpdate() > 0) {
                         JOptionPane.showMessageDialog(null, "Registro exitoso");
                         
                         MostrarProductosLiquidos();
                        Vista_Registro_Producto_Liquido.txtNombreProductoLiquido.setText("");
                         Vista_Registro_Producto_Liquido.txtTipodeLiquido.setText("");
                         Vista_Registro_Producto_Liquido.txtViscosidad.setText("");
                         Vista_Registro_Producto_Liquido.txtPresentacion.setText("");
                         
                         
                     
                        
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al registrar producto: " + e.getMessage());
        }
    }
    
public void MostrarProductosLiquidos() throws ClassNotFoundException, SQLException {
    TituloProductoLiquido();
    limpiarTabla(modeloProductoLiquido);

    ArrayList<Producto> ListaLiquido = ListaProductosLiquidos();

    // Quitar temporalmente el sorter
    Vista_Registro_Producto_Liquido.TablaRegistroLiquido.setRowSorter(null);

    // Modelo
    modeloProductoLiquido = (DefaultTableModel) Vista_Registro_Producto_Liquido.TablaRegistroLiquido.getModel();
    modeloProductoLiquido.setRowCount(0);

    // Llenar
    for (Producto producto : ListaLiquido) {
        modeloProductoLiquido.addRow(new Object[]{
            producto.getIdProducto(),
            producto.getNombre(),
            producto.getTipo_Liquido(),
            producto.getViscosidad(),
            producto.getDensidad(),
            producto.getPresentacion(),
            producto.getIdCategoria(),
            producto.getIdMarca()
            
        });
    }

    Vista_Registro_Producto_Liquido.TablaRegistroLiquido.setModel(modeloProductoLiquido);
    centrarTextoTabla( Vista_Registro_Producto_Liquido.TablaRegistroLiquido);

    // Ahora recién activar el sorter
    TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modeloProductoLiquido);
    Vista_Registro_Producto_Liquido.TablaRegistroLiquido.setRowSorter(sorter);
}


       
  private ArrayList<Producto> ListaProductosLiquidos() throws ClassNotFoundException, SQLException {
    ArrayList<Producto> Lista = new ArrayList<>();
    String sql = "SELECT p.idProducto, p.nombre, p.tipo_Liquido, p.viscosidad,p.densidad, p.presentacion, "
               + "c.nombre AS nombreCategoria, "
               + "m.nombre AS nombreMarca "
               + "FROM producto p "
               + "INNER JOIN categoria c ON p.idCategoria = c.idCategoria "
               + "INNER JOIN marca m ON p.idMarca = m.idMarca "
               + "WHERE p.tipo_producto = 'LIQUIDO'"
             + "ORDER BY p.idProducto";   // 👈 solo líquidos

    try {
        this.conectar();
        ps = this.con.prepareStatement(sql);
        rs = ps.executeQuery();

        while (rs.next()) {
            Producto p = new Producto();
            p.setIdProducto(rs.getInt("idProducto"));
            p.setNombre(rs.getString("nombre"));
            p.setTipo_Liquido(rs.getString("tipo_Liquido"));
            p.setViscosidad(rs.getString("viscosidad"));
            p.setDensidad(rs.getString("densidad"));
            p.setPresentacion(rs.getString("presentacion"));
            // Traemos los nombres de las FK
            p.setIdCategoria(rs.getString("nombreCategoria"));
            p.setIdMarca(rs.getString("nombreMarca"));
          

            Lista.add(p);
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error en mostrar la lista de productos líquidos: " + e.getMessage());
    }
    return Lista;
}

  
    public void ActualizarProductoLiquido(int IdProducto) throws ClassNotFoundException {
        
        Producto p = new Producto();
        p.setNombre(Vista_Registro_Producto_Liquido.txtNombreProductoLiquido.getText());
        
        p.setTipo_Liquido(Vista_Registro_Producto_Liquido.txtTipodeLiquido.getText());
        
        p.setViscosidad(Vista_Registro_Producto_Liquido.txtViscosidad.getText());
        
        p.setPresentacion(Vista_Registro_Producto_Liquido.txtPresentacion.getText());
     
      // p.setIdProducto (Integer.parseInt(Registrar_Productoss.txtIdLiquido.getText()));
       
       p.setDensidad(Vista_Registro_Producto_Liquido.jComboDensidad.getSelectedItem().toString());


        Marca marcaSeleccionada = (Marca) Vista_Registro_Producto_Liquido.jComboMarca.getSelectedItem();
        int idMarca = marcaSeleccionada.getIdMarca();

        Categoria categoriaSeleccionada = (Categoria) Vista_Registro_Producto_Liquido.jComboCategoria.getSelectedItem();
        int idCategoria = categoriaSeleccionada.getIdCategoria();
    

        String sql = "UPDATE producto SET nombre=?,tipo_Liquido=?, viscosidad=?,densidad=?,presentacion=?,idCategoria=?,idMarca=?  WHERE idProducto=?";

        try {
            this.conectar();
            ps = this.con.prepareStatement(sql);

            ps.setString(1, p.getNombre());
            ps.setString(2, p.getTipo_Liquido());
            ps.setString(3, p.getViscosidad());
            ps.setString(4, p.getDensidad());
            ps.setString(5, p.getPresentacion());
            ps.setInt(6, idCategoria);
            ps.setInt(7, idMarca);
            ps.setInt(8, IdProducto);

            if (ps.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(null, "actualizacion correcta de producto");
                MostrarProductosLiquidos();
                
               

             
            } else {
                JOptionPane.showMessageDialog(null, " Diablo");
            }
            
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar producto" + e.getMessage());
        }
    }

    
 public void agregarFiltroBusquedaLiquido() {
    if (Vista_Registro_Producto_Liquido.txtBuscarProductoLiquido != null && Vista_Registro_Producto_Liquido.TablaRegistroLiquido != null) {

        // Listener persistente
        Vista_Registro_Producto_Liquido.txtBuscarProductoLiquido.getDocument().addDocumentListener(new DocumentListener() {

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
                String texto = Vista_Registro_Producto_Liquido.txtBuscarProductoLiquido.getText().trim();

                // Obtener siempre el modelo actual
                DefaultTableModel modeloActual = (DefaultTableModel) Vista_Registro_Producto_Liquido.TablaRegistroLiquido.getModel();

                // Verificar o crear sorter
                TableRowSorter<?> sorter = (TableRowSorter<?>) Vista_Registro_Producto_Liquido.TablaRegistroLiquido.getRowSorter();
                if (sorter == null || sorter.getModel() != modeloActual) {
                    sorter = new TableRowSorter<>(modeloActual);
                    Vista_Registro_Producto_Liquido.TablaRegistroLiquido.setRowSorter(sorter);
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

 
public void EliminarProductoLiquido(int idProducto) throws ClassNotFoundException, SQLException {
    String sql = "DELETE FROM producto WHERE idProducto=?";

    try {
        this.conectar();
        ps = this.con.prepareStatement(sql);
        ps.setInt(1, idProducto);

        if (ps.executeUpdate() >0) {
            JOptionPane.showMessageDialog(null, "Eliminación exitosa");
        }

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al eliminar este producto: " + e.getMessage());
    }
}


public void ActulizarProductoLiquido() throws ClassNotFoundException, SQLException {
    int fila = Vista_Registro_Producto_Liquido.TablaRegistroLiquido.getSelectedRow();

    if (fila == -1) {
        JOptionPane.showMessageDialog(null, "Debe seleccionar un producto para Actualizar");
        return;
    }

    int idProducto = Integer.parseInt(Vista_Registro_Producto_Liquido.TablaRegistroLiquido.getValueAt(fila, 0).toString()
    );

    int confirmar = JOptionPane.showConfirmDialog(
        null,
        "¿Estás seguro de actualizar este producto?",
        "Confirmar actualización",
        JOptionPane.YES_NO_OPTION
    );

    if (confirmar == JOptionPane.YES_OPTION) {
        ActualizarProductoLiquido(idProducto);
        
         Vista_Registro_Producto_Liquido.txtNombreProductoLiquido.setText("");
        Vista_Registro_Producto_Liquido.txtTipodeLiquido.setText("");
        Vista_Registro_Producto_Liquido.txtViscosidad.setText("");
        Vista_Registro_Producto_Liquido.txtPresentacion.setText("");
        
        
         Vista_Registro_Producto_Liquido.btbEliminar.setVisible(true);
            Vista_Registro_Producto_Liquido.btbActualizar.setVisible(true);
            Vista_Registro_Producto_Liquido.btbRegistrar.setVisible(true);
            Vista_Registro_Producto_Liquido.btbConfirmarActualizacion.setVisible(false);
            Vista_Registro_Producto_Liquido.btbCancelarActualizacion.setVisible(false);
            
MostrarProductosLiquidos();


    }
}
public void EliminarProductoLiquido() throws ClassNotFoundException, SQLException {
    int fila = Vista_Registro_Producto_Liquido.TablaRegistroLiquido.getSelectedRow();

    if (fila == -1) {
        JOptionPane.showMessageDialog(null, "Debe seleccionar un producto para eliminarlo");
        return;
    }

    int idProducto = Integer.parseInt(Vista_Registro_Producto_Liquido.TablaRegistroLiquido.getValueAt(fila, 0).toString()
    );

    int confirmar = JOptionPane.showConfirmDialog(
        null,
        "¿Estás seguro de eliminar este producto?",
        "Confirmar eliminación",
        JOptionPane.YES_NO_OPTION
    );

    if (confirmar == JOptionPane.YES_OPTION) {
        EliminarProductoLiquido(idProducto);
MostrarProductosLiquidos();

// refrescar tabla
    }
}




/*
                           Productos Solido
        |
        |
        |
        |
        |
        |
        |
       \|/
        V
*/







DefaultTableModel modeloSolido = new  DefaultTableModel();



public void TituloProductoSolido() {
    String Titulo[] = {
        "ID", 
        "Nombre", 
        "Condición", 
        "Compatibilidad",
        "Unidad de Medida", 
        "Categoria", 
        "Marca" 
        
    };

    modeloSolido.setColumnIdentifiers(Titulo);

    if (Vista_Registrar_Producto_Solido.TablaSolido != null) {
        Vista_Registrar_Producto_Solido.TablaSolido.setModel(modeloSolido);

        // 👇 aplicar centrado de datos después de setear el modelo
     
    }
}


  
public void centrarTextoTabla(JTable tabla) {
    DefaultTableCellRenderer centrado = new DefaultTableCellRenderer();
    centrado.setHorizontalAlignment(SwingConstants.CENTER);
   
    for (int i = 0; i < tabla.getColumnModel().getColumnCount(); i++) {
        tabla.getColumnModel().getColumn(i).setCellRenderer(centrado);
    }
}



private void limpiarTablaProductoSolido() {
        int fila = modeloSolido.getRowCount();
        while (fila > 0) {
            modeloSolido.removeRow(0);
            fila--;
        }
    }

   






private void limpiarTablaProductoUnida() {
        int fila = modeloUnidad.getRowCount();
        while (fila > 0) {
            modeloUnidad.removeRow(0);
            fila--;
        }
    }

  
    public  void RegistrarProductoSolido ( Producto P) throws  ClassNotFoundException,SQLException {
        
       P.setNombre(Vista_Registrar_Producto_Solido.txtNombreProductoSolido.getText());
       P.setCondicion(Vista_Registrar_Producto_Solido.txtCondicionSolido.getText());
       P.setCompartibilidad(Vista_Registrar_Producto_Solido.txtSolidoCompartibilidad.getText());
       P.setUnida_De_Medida(Vista_Registrar_Producto_Solido.jComboUnidaSolido.getSelectedItem().toString());
        
        /*
          Proveedor proveedorSeleccionado = (Proveedor) ComboxBoxProveedor.getSelectedItem();
        int idProveedor = proveedorSeleccionado.getIdProveedor();
*/
        Marca marcaSeleccionada = (Marca) Vista_Registrar_Producto_Solido.jComboMarca.getSelectedItem();
        int idMarca = marcaSeleccionada.getIdMarca();

        Categoria categoriaSeleccionada = (Categoria) Vista_Registrar_Producto_Solido.jComboCategoria.getSelectedItem();
        int idCategoria = categoriaSeleccionada.getIdCategoria();

        
        
        
        
       String sql = "insert into producto (nombre,condicion,compatibilidad,unidad_De_Medidad,idCategoria,idMarca) values (?,?,?,?,?,?)";
        
        try {
            this.conectar();
            
            ps = this.con.prepareStatement(sql);
            
           ps.setString(1,P.getNombre());
           ps.setString(2, P.getCondicion());
           ps.setString(3, P.getCompartibilidad());
           ps.setString(4, P.getUnida_De_Medida());
           ps.setInt(5, idCategoria);
           ps.setInt(6, idMarca);

            
            if (ps.executeUpdate() > 0) {
               JOptionPane.showMessageDialog(null, "Registro Exitoso");
               MostrarProductosSolidos();
              Vista_Registrar_Producto_Solido.txtNombreProductoSolido.setText("");
              Vista_Registrar_Producto_Solido.txtCondicionSolido.setText("");
           Vista_Registrar_Producto_Solido.txtSolidoCompartibilidad.setText("");
           
               
            }  else {
                JOptionPane.showMessageDialog(null, "Error al Registrar");
            }
           
           
           
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,  "Error al registrar el producto Solido " + e.getMessage());
        }
        
        
    }
    
    private ArrayList<Producto> ListaProductosSolidos() throws ClassNotFoundException, SQLException {
    ArrayList<Producto> Lista = new ArrayList<>();
    String sql = "SELECT p.idProducto, p.nombre AS nombre, p.condicion, p.compatibilidad, p.unidad_De_Medidad, "
               + "c.nombre AS nombreCategoria, "
               + "m.nombre AS nombreMarca "
               + "FROM producto p "
               + "INNER JOIN categoria c ON p.idCategoria = c.idCategoria "
               + "INNER JOIN marca m ON p.idMarca = m.idMarca "
            
               + "WHERE p.tipo_producto = 'SOLIDO'"
               + "ORDER BY p.idProducto";   // 👈 solo sólidos

    try {
        this.conectar();
        ps = this.con.prepareStatement(sql);
        rs = ps.executeQuery();

        while (rs.next()) {
            Producto p = new Producto();
            p.setIdProducto(rs.getInt("idProducto"));
            p.setNombre(rs.getString("nombre"));  // viene como nombreSolido en BD
            p.setCondicion(rs.getString("condicion"));
            p.setCompartibilidad(rs.getString("compatibilidad"));
            p.setUnida_De_Medida(rs.getString("unidad_De_Medidad"));

            // Traemos los nombres de las FK
            p.setIdCategoria(rs.getString("nombreCategoria"));
            p.setIdMarca(rs.getString("nombreMarca"));
           // p.setIdProveedor(rs.getString("nombreProveedor"));

            Lista.add(p);
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error en mostrar la lista de productos sólidos: " + e.getMessage());
    }
    return Lista;
}

   
    
    public void MostrarProductosSolidos() throws ClassNotFoundException, SQLException {

    // Configurar títulos de la tabla
    TituloProductoSolido();
    limpiarTablaProductoSolido();
    limpiarTabla(modeloSolido);

    ArrayList<Producto> ListaSolidos = ListaProductosSolidos();

    // Quitar temporalmente el sorter para evitar el error
   Vista_Registrar_Producto_Solido.TablaSolido.setRowSorter(null);

    // Obtener modelo
    modeloSolido = (DefaultTableModel)Vista_Registrar_Producto_Solido.TablaSolido.getModel();

    // Limpiar la tabla antes de llenarla
    modeloSolido.setRowCount(0);

    // Llenar con los datos
    for (Producto producto : ListaSolidos) {
        modeloSolido.addRow(new Object[]{
            producto.getIdProducto(),
            producto.getNombre(),
            producto.getCondicion(),
            producto.getCompartibilidad(),
            producto.getUnida_De_Medida(),
            producto.getIdCategoria(),
            producto.getIdMarca()
          
        });
    }

Vista_Registrar_Producto_Solido.TablaSolido.setModel(modeloSolido);
    centrarTextoTabla(Vista_Registrar_Producto_Solido.TablaSolido);

    // Activar sorter después de llenar
    TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modeloSolido);
    Vista_Registrar_Producto_Solido.TablaSolido.setRowSorter(sorter);
}

    
    
    public void ActualizarProductoSolido(int IdProducto) throws ClassNotFoundException {
        
        Producto p = new Producto();
        
          p.setNombre(Vista_Registrar_Producto_Solido.txtNombreProductoSolido.getText());
       p.setCondicion(Vista_Registrar_Producto_Solido.txtCondicionSolido.getText());
       p.setCompartibilidad(Vista_Registrar_Producto_Solido.txtSolidoCompartibilidad.getText());
       p.setUnida_De_Medida(Vista_Registrar_Producto_Solido.jComboUnidaSolido.getSelectedItem().toString());
       
     //  p.setIdProducto (Integer.parseInt(Registrar_Productoss.txtIdSolido.getText()));
/*
        Proveedor proveedorSeleccionado = (Proveedor) Registrar_Productoss.ComboxBoxProveedor.getSelectedItem();
        int idProveedor = proveedorSeleccionado.getIdProveedor();*/

        Marca marcaSeleccionada = (Marca)Vista_Registrar_Producto_Solido.jComboMarca.getSelectedItem();
        int idMarca = marcaSeleccionada.getIdMarca();

        Categoria categoriaSeleccionada = (Categoria) Vista_Registrar_Producto_Solido.jComboCategoria.getSelectedItem();
        int idCategoria = categoriaSeleccionada.getIdCategoria();
    

        String sql = "UPDATE producto SET nombre=?,condicion=?, compatibilidad=?,unidad_De_Medidad=?,idCategoria=?,idMarca=?  WHERE idProducto=?";

        try {
            this.conectar();
            ps = this.con.prepareStatement(sql);

            ps.setString(1, p.getNombre());
            ps.setString(2, p.getCondicion());
            ps.setString(3, p.getCompartibilidad());
            ps.setString(4, p.getUnida_De_Medida());
            ps.setInt(5, idCategoria);
            ps.setInt(6, idMarca);
            ps.setInt(7, IdProducto);

            if (ps.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(null, "actualizacion correcta de producto");
                MostrarProductosSolidos();
                Vista_Registrar_Producto_Solido.btbConfirmarActulizacion.setVisible(false);
          Vista_Registrar_Producto_Solido.btbCancelarActuailizacion.setVisible(false);
       Vista_Registrar_Producto_Solido.btbEliminarSolido.setVisible(true);
          Vista_Registrar_Producto_Solido.btbActualizarSolido.setVisible(true);
          Vista_Registrar_Producto_Solido.btbRegistrarProductoSolido.setVisible(true);
       Vista_Registrar_Producto_Solido.txtNombreProductoSolido.setText("");
          Vista_Registrar_Producto_Solido.txtSolidoCompartibilidad.setText("");
        Vista_Registrar_Producto_Solido.txtCondicionSolido.setText("");
               

             
            } else {
                JOptionPane.showMessageDialog(null, " Diablo");
            }
            
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar producto" + e.getMessage());
        }
    }

    
    

public void EliminarProductooSolido(int idProducto) throws ClassNotFoundException, SQLException {
    String sql = "DELETE FROM producto WHERE idProducto=?";

    try {
        this.conectar();
        ps = this.con.prepareStatement(sql);
        ps.setInt(1, idProducto);

        if (ps.executeUpdate() >0) {
            JOptionPane.showMessageDialog(null, "Eliminación exitosa");
        }

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al eliminar este producto: " + e.getMessage());
    }
}

public void Actulizar_ProductoSolido() throws ClassNotFoundException, SQLException {
    int fila = Vista_Registrar_Producto_Solido.TablaSolido.getSelectedRow();

    if (fila == -1) {
        JOptionPane.showMessageDialog(null, "Debe seleccionar un producto para eliminarlo");
        return;
    }

    int idProducto = Integer.parseInt(Vista_Registrar_Producto_Solido.TablaSolido.getValueAt(fila, 0).toString()
    );

    int confirmar = JOptionPane.showConfirmDialog(
        null,
        "¿Estás seguro de Actualizar este producto?",
        "Confirmar actualizacionción",
        JOptionPane.YES_NO_OPTION
    );

    if (confirmar == JOptionPane.YES_OPTION) {
        ActualizarProductoSolido(idProducto);
MostrarProductosSolidos();

// refrescar tabla
    }
}

public void EliminarProductoSolido() throws ClassNotFoundException, SQLException {
    int fila = Vista_Registrar_Producto_Solido.TablaSolido.getSelectedRow();

    if (fila == -1) {
        JOptionPane.showMessageDialog(null, "Debe seleccionar un producto para eliminarlo");
        return;
    }

    int idProducto = Integer.parseInt(Vista_Registrar_Producto_Solido.TablaSolido.getValueAt(fila, 0).toString()
    );

    int confirmar = JOptionPane.showConfirmDialog(
        null,
        "¿Estás seguro de eliminar este producto?",
        "Confirmar eliminación",
        JOptionPane.YES_NO_OPTION
    );

    if (confirmar == JOptionPane.YES_OPTION) {
        EliminarProductooSolido(idProducto);
MostrarProductosSolidos();

// refrescar tabla
    }
}

  public void agregarFiltroBusquedaProductoSolido() {
    if (Vista_Registrar_Producto_Solido.txtBuscarProducto != null && Vista_Registrar_Producto_Solido.TablaSolido != null) {

        // Listener persistente
      Vista_Registrar_Producto_Solido.txtBuscarProducto.getDocument().addDocumentListener(new DocumentListener() {

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
                String texto =Vista_Registrar_Producto_Solido.txtBuscarProducto.getText().trim();

                // Obtener siempre el modelo actual
                DefaultTableModel modeloActual = (DefaultTableModel) Vista_Registrar_Producto_Solido.TablaSolido.getModel();

                // Verificar o crear sorter
                TableRowSorter<?> sorter = (TableRowSorter<?>) Vista_Registrar_Producto_Solido.TablaSolido.getRowSorter();
                if (sorter == null || sorter.getModel() != modeloActual) {
                    sorter = new TableRowSorter<>(modeloActual);
                    Vista_Registrar_Producto_Solido.TablaSolido.setRowSorter(sorter);
                }

                // Aplicar o limpiar filtro
                if (texto.isEmpty()) {
                    sorter.setRowFilter(null);
                } else {
                    try {
                        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(texto)));
                    } catch (PatternSyntaxException ex) {
                        sorter.setRowFilter(null); // evita errores con caracteres especiales
                    }
                }
            }
        });
    }
}

/*
  Producto por Unidad

        |
        |
        |
        |
        |
        |
        |
       \|/
        V
*/

DefaultTableModel modeloUnidad = new DefaultTableModel();
public void TituloProductoUnidad() {
    String Titulo[] = {
        "ID", 
        "Nombre", 
        "Número serial", 
        "Compatibilidad",
        "Unidad de Medida", 
        "Especificaciones",
        "Categoria", 
        "Marca", 
       
    };

    modeloUnidad.setColumnIdentifiers(Titulo);

    if (Vista_Registrar_Producto_Unidad.TablaUnidad != null) {
       Vista_Registrar_Producto_Unidad.TablaUnidad.setModel(modeloUnidad);

        // 👇 aplicar centrado de datos después de setear el modelo
       
    }
}


    public void ActualizarProductoUnidad(int idProductoUnidad) throws ClassNotFoundException {
   
        Modelo.Producto p = new Producto();
// Capturamos los valores del formulario
    p.setNombre(Vista_Registrar_Producto_Unidad.txtNombreUnidad.getText());
    p.setNumeroSerial(Vista_Registrar_Producto_Unidad.txtNumeroSerial.getText()); // <-- NUEVO
    p.setCompartibilidad(Vista_Registrar_Producto_Unidad.txtCompartibilidadUnidad.getText());
    p.setUnida_De_Medida(Vista_Registrar_Producto_Unidad.JcomoBoxUnidadDeMedida.getSelectedItem().toString());
    p.setEspecificaciones(Vista_Registrar_Producto_Unidad.txtEspecificacionTecnica.getText()); // <-- NUEVO
 // p.setIdProducto(Integer.parseInt(Registrar_Productoss.txtIDUnidad.getText()));

  /*  // Relación con tablas foráneas
    Proveedor proveedorSeleccionado = (Proveedor) Registrar_Productoss.ComboxBoxProveedor.getSelectedItem();
    int idProveedor = proveedorSeleccionado.getIdProveedor();
*/
    Marca marcaSeleccionada = (Marca) Vista_Registrar_Producto_Unidad.JcomoBoxMarca.getSelectedItem();
    int idMarca = marcaSeleccionada.getIdMarca();

    Categoria categoriaSeleccionada = (Categoria) Vista_Registrar_Producto_Unidad.jComboxCategoria.getSelectedItem();
    int idCategoria = categoriaSeleccionada.getIdCategoria();

    // SQL actualizado
    String sql = "UPDATE producto SET nombre=?, numero_serial=?, compatibilidad=?, unidad_De_Medidad=?, especificaciones=?, idCategoria=?,  idMarca=? WHERE idProducto=?";

    try {
        this.conectar();
        ps = this.con.prepareStatement(sql);

        ps.setString(1, p.getNombre());
        ps.setString(2, p.getNumeroSerial());         // nuevo
        ps.setString(3, p.getCompartibilidad());
        ps.setString(4, p.getUnida_De_Medida());
        ps.setString(5, p.getEspecificaciones());     // nuevo
        ps.setInt(6, idCategoria);
        ps.setInt(7, idMarca);
        ps.setInt(8, idProductoUnidad);

        if (ps.executeUpdate() > 0) {
            JOptionPane.showMessageDialog(null, "Actualización correcta del producto");
            MostrarProductosUnidad();
            Vista_Registrar_Producto_Unidad.txtNombreUnidad.setText("");
            Vista_Registrar_Producto_Unidad.txtCompartibilidadUnidad.setText("");
            Vista_Registrar_Producto_Unidad.txtEspecificacionTecnica.setText("");
            Vista_Registrar_Producto_Unidad.txtNumeroSerial.setText("");
        
        } else {
            JOptionPane.showMessageDialog(null, "No se pudo actualizar el producto");
        }

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al actualizar producto: " + e.getMessage());
    }
}

    public void ActualizarProducdoUnidad() throws ClassNotFoundException, SQLException {
    int fila = Vista_Registrar_Producto_Unidad.TablaUnidad.getSelectedRow();

    if (fila == -1) {
        JOptionPane.showMessageDialog(null, "Debe seleccionar un producto para actualizarlo");
        return;
    }

    int idProducto = Integer.parseInt(Vista_Registrar_Producto_Unidad.TablaUnidad.getValueAt(fila, 0).toString()
    );

    int confirmar = JOptionPane.showConfirmDialog(
        null,
        "¿Estás seguro de actualizar este producto?",
        "Confirmar actualización",
        JOptionPane.YES_NO_OPTION
    );

    if (confirmar == JOptionPane.YES_OPTION) {
        ActualizarProductoUnidad(idProducto);
MostrarProductosUnidad();

// refrescar tabla
    }
}

   public void MostrarProductosUnidad() throws ClassNotFoundException, SQLException {

    // Configurar títulos de la tabla
    TituloProductoUnidad();
    limpiarTablaProductoUnida();
    limpiarTabla(modeloUnidad);

    ArrayList<Producto> ListaUnidad = ListaProductosUnidad();

    // Quitar sorter antes de llenar para evitar errores
  Vista_Registrar_Producto_Unidad.TablaUnidad.setRowSorter(null);

    // Obtener el modelo actual
    modeloUnidad = (DefaultTableModel)Vista_Registrar_Producto_Unidad.TablaUnidad.getModel();

    // Limpiar modelo antes de cargar datos
    modeloUnidad.setRowCount(0);

    // Llenar la tabla
    for (Producto producto : ListaUnidad) {
        modeloUnidad.addRow(new Object[]{
            producto.getIdProducto(),
            producto.getNombre(),
            producto.getNumeroSerial(),
            producto.getCompartibilidad(),
            producto.getUnida_De_Medida(),
            producto.getEspecificaciones(),
            producto.getIdCategoria(),
            producto.getIdMarca(),
        });
    }

    // Reasignar el modelo y centrar texto
    Vista_Registrar_Producto_Unidad.TablaUnidad.setModel(modeloUnidad);
    centrarTextoTabla(Vista_Registrar_Producto_Unidad.TablaUnidad);

    // Crear y asignar el sorter DESPUÉS de llenar los datos
    TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modeloUnidad);
Vista_Registrar_Producto_Unidad.TablaUnidad.setRowSorter(sorter);
}

    
    
    
    private ArrayList<Producto> ListaProductosUnidad() throws ClassNotFoundException, SQLException {
    ArrayList<Producto> Lista = new ArrayList<>();
    String sql ="SELECT p.idProducto, p.nombre, p.numero_serial, p.compatibilidad, "
               + "p.unidad_De_Medidad, p.especificaciones, "
               + "c.nombre AS nombreCategoria, "
               + "m.nombre AS nombreMarca "
               + "FROM producto p "
               + "INNER JOIN categoria c ON p.idCategoria = c.idCategoria "
               + "INNER JOIN marca m ON p.idMarca = m.idMarca "
               
               + "WHERE p.tipo_producto = 'UNIDAD' "
               + "ORDER BY p.idProducto";
    try {
        this.conectar();
        ps = this.con.prepareStatement(sql);
        rs = ps.executeQuery();

        while (rs.next()) {
            Producto p = new Producto();
            p.setIdProducto(rs.getInt("idProducto"));
            p.setNombre(rs.getString("nombre"));
            p.setNumeroSerial(rs.getString("numero_serial"));
            p.setCompartibilidad(rs.getString("compatibilidad"));
            p.setUnida_De_Medida(rs.getString("unidad_De_Medidad"));
            p.setEspecificaciones(rs.getString("especificaciones"));

            // Traemos los nombres de las FK
            p.setIdCategoria(rs.getString("nombreCategoria"));
            p.setIdMarca(rs.getString("nombreMarca"));
           

            Lista.add(p);
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error en mostrar la lista de productos por unidad: " + e.getMessage());
    }
    return Lista;
}

    
    
    
    public  void RegistarUnidad(Producto p) throws  ClassNotFoundException,SQLException{
   
     p.setNombre(Vista_Registrar_Producto_Unidad.txtNombreUnidad.getText());
     p.setNumeroSerial(Vista_Registrar_Producto_Unidad.txtNumeroSerial.getText());
     p.setCompartibilidad(Vista_Registrar_Producto_Unidad.txtCompartibilidadUnidad.getText());
     p.setUnida_De_Medida(Vista_Registrar_Producto_Unidad.JcomoBoxUnidadDeMedida.getSelectedItem().toString());
     p.setEspecificaciones(Vista_Registrar_Producto_Unidad.txtEspecificacionTecnica.getText());
        
   /*   Proveedor proveedorSeleccionado = (Proveedor) Registrar_Productoss.ComboxBoxProveedor.getSelectedItem();
        int idProveedor = proveedorSeleccionado.getIdProveedor();
*/
        Marca marcaSeleccionada = (Marca) Vista_Registrar_Producto_Unidad.JcomoBoxMarca.getSelectedItem();
        int idMarca = marcaSeleccionada.getIdMarca();

        Categoria categoriaSeleccionada = (Categoria) Vista_Registrar_Producto_Unidad.jComboxCategoria.getSelectedItem();
        int idCategoria = categoriaSeleccionada.getIdCategoria();   
        
        String sql = "insert into producto (nombre,numero_serial,compatibilidad,unidad_De_Medidad,especificaciones,idCategoria,idMarca) values (?,?,?,?,?,?,?)";
        
        try {
            this.conectar();
            
           ps = this.con.prepareStatement(sql);
           
           ps.setString(1,p.getNombre());
           ps.setString(2, p.getNumeroSerial());
           ps.setString(3,p.getCompartibilidad());
           ps.setString(4, p.getUnida_De_Medida());
           ps.setString(5, p.getEspecificaciones());
           ps.setInt(6, idCategoria);
           ps.setInt(7, idMarca);
           
            if (ps.executeUpdate() > 0) {
               JOptionPane.showMessageDialog(null, "Registro Exitoso");
               MostrarProductosUnidad();
               
               Vista_Registrar_Producto_Unidad.txtNombreUnidad.setText("");
              Vista_Registrar_Producto_Unidad.txtCompartibilidadUnidad.setText("");
              
              Vista_Registrar_Producto_Unidad.txtEspecificacionTecnica.setText("");
               Vista_Registrar_Producto_Unidad.txtNumeroSerial.setText("");
            }
            
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al registrar " + e.getMessage());
        }
        
        
        
    }
  
    

    
    
public void EliminarProductooUnidadd(int idProducto) throws ClassNotFoundException, SQLException {
    String sql = "DELETE FROM producto WHERE idProducto=?";

    try {
        this.conectar();
        ps = this.con.prepareStatement(sql);
        ps.setInt(1, idProducto);

        if (ps.executeUpdate() >0) {
            JOptionPane.showMessageDialog(null, "Eliminación exitosa");
        }

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al eliminar este producto: " + e.getMessage());
    }
}



public void EliminarProductoUnidad() throws ClassNotFoundException, SQLException {
    int fila = Vista_Registrar_Producto_Unidad.TablaUnidad.getSelectedRow();

    if (fila == -1) {
        JOptionPane.showMessageDialog(null, "Debe seleccionar un producto para eliminarlo");
        return;
    }

    int idProducto = Integer.parseInt(Vista_Registrar_Producto_Unidad.TablaUnidad.getValueAt(fila, 0).toString()
    );

    int confirmar = JOptionPane.showConfirmDialog(
        null,
        "¿Estás seguro de eliminar este producto?",
        "Confirmar eliminación",
        JOptionPane.YES_NO_OPTION
    );

    if (confirmar == JOptionPane.YES_OPTION) {
        EliminarProductooUnidadd(idProducto);
MostrarProductosUnidad();

// refrescar tabla
    }
}




public void agregarFiltroBusquedaProductoUnidad() {
    if (Vista_Registrar_Producto_Unidad.txtBuscarProductoUnidad != null && Vista_Registrar_Producto_Unidad.TablaUnidad != null) {

        // Listener persistente
        Vista_Registrar_Producto_Unidad.txtBuscarProductoUnidad.getDocument().addDocumentListener(new DocumentListener() {

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
                String texto = Vista_Registrar_Producto_Unidad.txtBuscarProductoUnidad.getText().trim();

                // Obtener siempre el modelo actual
                DefaultTableModel modeloActual = (DefaultTableModel)Vista_Registrar_Producto_Unidad.TablaUnidad.getModel();

                // Verificar o crear sorter
                TableRowSorter<?> sorter = (TableRowSorter<?>) Vista_Registrar_Producto_Unidad.TablaUnidad.getRowSorter();
                if (sorter == null || sorter.getModel() != modeloActual) {
                    sorter = new TableRowSorter<>(modeloActual);
                    Vista_Registrar_Producto_Unidad.TablaUnidad.setRowSorter(sorter);
                }

                // Aplicar o limpiar filtro
                if (texto.isEmpty()) {
                    sorter.setRowFilter(null);
                } else {
                    try {
                        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(texto)));
                    } catch (PatternSyntaxException ex) {
                        sorter.setRowFilter(null); // evita errores con caracteres especiales
                    }
                }
            }
        });
    }
}

    
    
    
    
    
    
    
    
public ArrayList<Producto> ListaProductos() throws ClassNotFoundException, SQLException {
    ArrayList<Producto> Lista = new ArrayList<>();
    String sql = "SELECT \n" +
    "    p.`idProducto`, \n" +
    "    p.`nombre` AS `nombreProducto`,\n" +
    "    c.`nombre` AS `nombreCategoria`,\n" +
    "    m.`nombre` AS `nombreMarca`,\n" +
    "    pr.`nombre` AS `nombreProveedor`,\n" +
    "    i.`Cantidad`,\n" +
    "    i.`precio_Unitario`,\n" +
    "    i.`fecha_ingreso`\n" +
    "FROM `producto` p\n" +
    "INNER JOIN `categoria` c ON p.`idCategoria` = c.`idCategoria`\n" +
    "INNER JOIN `marca` m ON p.`idMarca` = m.`idMarca`\n" +
    "INNER JOIN `proveedor` pr ON p.`idProveedor` = pr.`idProveedor`\n" +
    "INNER JOIN `inventario` i ON p.`idProducto` = i.`idProducto` \n" +
    "ORDER BY p.`idProducto`";  
    // Para ver los últimos ingresados primero, usa: ORDER BY p.`idProducto` DESC
    try {
        this.conectar();
        ps = this.con.prepareStatement(sql);
        rs = ps.executeQuery();

        while (rs.next()) {
            Producto p = new Producto();
          
            
            p.setIdProducto(rs.getInt("idProducto"));
            p.setNombre(rs.getString("nombreProducto"));
            p.setIdCategoria(rs.getString("nombreCategoria")); // ⚠️ este campo parece no ser "idCategoria", sino "nombreCategoria"
            p.setIdMarca(rs.getString("nombreMarca"));
            p.setIdProveedor(rs.getString("nombreProveedor"));
            p.setCantidad(rs.getInt("Cantidad"));
            p.setPrecio_Unitario(rs.getInt("precio_Unitario"));
            p.setFecha_Ingreso(rs.getString("fecha_ingreso"));
            
            Lista.add(p);
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error en mostrar la lista de productos: " + e.getMessage());
    }
    return Lista;
}




public ArrayList<Producto> ListaProductosmENU() throws ClassNotFoundException, SQLException {
    ArrayList<Producto> Lista = new ArrayList<>();
   String sql = "SELECT \n" +
    "    p.`idProducto`, \n" +
    "    p.`nombre` AS `nombreProducto`,\n" +
    "    c.`nombre` AS `nombreCategoria`,\n" +
    "    m.`nombre` AS `nombreMarca`,\n" +
    "    pr.`nombre` AS `nombreProveedor`,\n" +
    "    i.`Cantidad`,\n" +
    "    i.`precio_Unitario`,\n" +
    "    i.`fecha_ingreso`\n" +
    "FROM `producto` p\n" +
    "INNER JOIN `categoria` c ON p.`idCategoria` = c.`idCategoria`\n" +
    "INNER JOIN `marca` m ON p.`idMarca` = m.`idMarca`\n" +
    "INNER JOIN `proveedor` pr ON p.`idProveedor` = pr.`idProveedor`\n" +
    "LEFT JOIN `inventario` i ON p.`idProducto` = i.`idProducto` \n" +
    "ORDER BY p.`idProducto`;";
    // Si quieres del más nuevo al más antiguo: ORDER BY p.`idProducto` DESC

    try {
        this.conectar();
        ps = this.con.prepareStatement(sql);
        rs = ps.executeQuery();

        while (rs.next()) {
            Producto p = new Producto();
          
            
            p.setIdProducto(rs.getInt("idProducto"));
            p.setNombre(rs.getString("nombreProducto"));
            p.setIdCategoria(rs.getString("nombreCategoria")); // ⚠️ este campo parece no ser "idCategoria", sino "nombreCategoria"
            p.setIdMarca(rs.getString("nombreMarca"));
            p.setIdProveedor(rs.getString("nombreProveedor"));
            p.setCantidad(rs.getInt("Cantidad"));
            p.setPrecio_Unitario(rs.getInt("precio_Unitario"));
            p.setFecha_Ingreso(rs.getString("fecha_ingreso"));
            
            Lista.add(p);
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error en mostrar la lista de productos: " + e.getMessage());
    }
    return Lista;
}





// CargarInventario Liquido

    public void cargarProductosEnComboLiquido() throws ClassNotFoundException, SQLException {
        ArrayList<Producto> productos = this.ListaProductosLiquidos();
        if (InventarioLiquido.jComboProductoLiquido != null) {
            InventarioLiquido.jComboProductoLiquido.removeAllItems();
           for (Producto pp : productos) {
                InventarioLiquido.jComboProductoLiquido.addItem(pp);
            }
        }
    }

    public void agregarBuscadorProducto() {
        if (InventarioLiquido.txtBuscarProducto != null) {
            InventarioLiquido.txtBuscarProducto.getDocument().addDocumentListener(new DocumentListener() {
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
    String texto = InventarioLiquido.txtBuscarProducto.getText().toLowerCase();

    try {
        ArrayList<Producto> lista = this.ListaProductosLiquidos();
      InventarioLiquido.jComboProductoLiquido.removeAllItems();

        for (Producto p : lista) {
            // Concatenamos los campos en un solo texto para facilitar la búsqueda
            String data = (p.getNombre() != null ? p.getNombre().toLowerCase() : "") +
                          " " + (p.getTipo_Liquido() != null ? p.getTipo_Liquido().toLowerCase() : "") +
                          " " + (p.getViscosidad() != null ? p.getViscosidad().toLowerCase() : "") +
                          " " + (p.getPresentacion() != null ? p.getPresentacion().toLowerCase() : "");

            if (data.contains(texto)) {
                InventarioLiquido.jComboProductoLiquido.addItem(p);
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}

    public void ElimanarProducto(int idProducto) throws ClassNotFoundException, SQLException {
        String sql = "  DELETE FROM producto WHERE idProducto =?";

        try {
            this.conectar();
            ps = this.con.prepareStatement(sql);
            ps.setInt(1, idProducto);

            if (ps.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(null, "Se eliminó el producto correctamente.");
            } else {
                JOptionPane.showMessageDialog(null, "No se pudo eliminar el producto. Verifique si existe.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar el producto: " + e.getMessage());
        }
    }

   


// Método de apoyo









 // 999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999
  public void cargarProductosEnComboSolido()throws ClassNotFoundException, SQLException {
        ArrayList<Producto> productos = this.ListaProductosSolidos();
        if (InventarioSolido.jComboProductoSolido != null) {
            InventarioSolido.jComboProductoSolido.removeAllItems();
           for (Producto pp : productos) {
                InventarioSolido.jComboProductoSolido.addItem(pp);
            }
        }
    }

  
  
public void cargarProductosEnComboUNIDAD() throws ClassNotFoundException, SQLException {
    ArrayList<Producto> productos = this.ListaProductosUnidad();
    InventarioUnidad.jComboProductoUnidad.removeAllItems();

    for (Producto p : productos) {
        
      InventarioUnidad.jComboProductoUnidad.addItem(p);
    }

    // Renderer para asegurar que se muestre correctamente
    InventarioUnidad.jComboProductoUnidad.setRenderer(new DefaultListCellRenderer() {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            JLabel lbl = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof Producto) {
                Producto producto = (Producto) value;
                lbl.setText(producto.toString());
            }
            return lbl;
        }
    });
}







 public void agregarBuscadorProductoSolidoInvenatario() {
    if (InventarioSolido.txtBuscarProducroSolido != null) {
InventarioSolido.txtBuscarProducroSolido.getDocument().addDocumentListener(new DocumentListener() {
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


    private void actualizarComboBoxSolido() {
    String texto = InventarioSolido.txtBuscarProducroSolido.getText().toLowerCase();

    try {
        ArrayList<Producto> lista = this.ListaProductosSolidos();
        InventarioSolido.jComboProductoSolido.removeAllItems();

        for (Producto p : lista) {
            // Concatenamos los campos en un solo texto para facilitar la búsqueda
            String data = (p.getNombre() != null ? p.getNombre().toLowerCase() : "") +
                          " " + (p.getCondicion() != null ? p.getCondicion().toLowerCase() : "") +
                          " " + (p.getCompartibilidad() != null ? p.getCompartibilidad().toLowerCase() : "") +
                          " " + (p.getUnida_De_Medida() != null ? p.getUnida_De_Medida().toLowerCase() : "");

            if (data.contains(texto)) {
                InventarioSolido.jComboProductoSolido.addItem(p);
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}


public void agregarBuscadorProductoUNIDADINVENTARIO() {
    if (InventarioUnidad.txtBuscarProductoCombox != null) {
        InventarioUnidad.txtBuscarProductoCombox .getDocument().addDocumentListener(new DocumentListener() {
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
    String texto =InventarioUnidad.txtBuscarProductoCombox .getText().toLowerCase();

    try {
        ArrayList<Producto> lista = this.ListaProductosUnidad();
       InventarioUnidad.jComboProductoUnidad.removeAllItems();

        for (Producto p : lista) {
            // Concatenamos los campos en un solo texto para facilitar la búsqueda
            String data = (p.getNombre() != null ? p.getNombre().toLowerCase() : "") +
                          " " + (p.getEspecificaciones() != null ? p.getEspecificaciones().toLowerCase() : "") +
                          " " + (p.getCompartibilidad() != null ? p.getCompartibilidad().toLowerCase() : "") +
                          " " + (p.getUnida_De_Medida() != null ? p.getUnida_De_Medida().toLowerCase() : "");

            if (data.contains(texto)) {
                InventarioUnidad.jComboProductoUnidad.addItem(p);
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}






}

















