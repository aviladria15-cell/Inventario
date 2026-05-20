/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ModeloDAO;
import Controlador.controladorVisual;
import Modelo.ConexiónBD;
import java.sql.*;
import javax.swing.table.DefaultTableModel;
import Modelo.Inventario;
import Vista_GestionInventario.InventarioUnidad;
import javax.swing.JOptionPane;
import  Modelo.Producto;
import java.util.ArrayList;
import Modelo.Almacen;
import Modelo.Proveedor;
import Modelo.Usuario;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import Modelo.cuenta;
import Vista_Almacen.Vista_Salida_Unidad;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;
import  Vista_Almacen.Ajuste_Unidad;

/**
 *
 * @author Adrian
 */
public class InventarioUnidadDAO extends  ConexiónBD{
    
    
    private ResultSet rs;
    
    private PreparedStatement ps;
    
    
    
   

   Inventario iv = new  Inventario();
   

   
 
   
   DefaultTableModel modeloUNIDAD = new DefaultTableModel();
   
   
   DefaultTableModel modeloUNIDADLOTE = new DefaultTableModel();
   
   
 
   
   
   
   public void TituloInventarioUNIDADLote () {
    String Titulo[] = {
        "ID", 
        "Nombre", 
        "Cantidad",
        "Lote",
        "Fecha ingreso", 
        "Costo Unitario", 
        "Costo total",
        "Precio Venta",
        "% Ganancia ",
        "Proveedor"
         
       
        
    };

    modeloUNIDADLOTE.setColumnIdentifiers(Titulo);

    if (InventarioUnidad.TablaLote != null) {
        InventarioUnidad.TablaLote.setModel(modeloUNIDADLOTE);

        // 👇 aplicar centrado de datos después de setear el modelo
        centrarTextoTabla(InventarioUnidad.TablaLote);
    }
}

   
   
    public void TituloInventarioUNIDAD() {
    
    
    String Titulo[] = {
        "ID", 
        "Nombre", 
        "Cantidad Disponible", 
        "Stock Minimo",
        "Stock Máximo", 
        "Ubicación"
        
    };

    modeloUNIDAD.setColumnIdentifiers(Titulo);

    if (InventarioUnidad.TablaUnidad != null) {
      InventarioUnidad.TablaUnidad.setModel(modeloUNIDAD);

        // 👇 aplicar centrado de datos después de setear el modelo
        centrarTextoTabla(InventarioUnidad.TablaUnidad);
    }
} 




     
   
     // Metodo para centralizar los datos de la tabla empleado
 // Metodo para centralizar los datos de la tabla empleado
public void centrarTextoTabla(JTable tabla) {
    DefaultTableCellRenderer centrado = new DefaultTableCellRenderer();
    centrado.setHorizontalAlignment(SwingConstants.CENTER); // mejor usar SwingConstants

    // Recorremos todas las columnas visibles
    for (int i = 0; i < tabla.getColumnModel().getColumnCount(); i++) {
        tabla.getColumnModel().getColumn(i).setCellRenderer(centrado);
    }
}

 
 // Metodo para limpiar la tabla  inventario


 
 
           private void limpiarTabla(DefaultTableModel modelo) {
    modelo.setRowCount(0);  // elimina todas las filas
}
    
 
 
 public void MostrarUNIDADINVENTARIO() throws ClassNotFoundException, SQLException {
    // Cargar títulos y limpiar tabla
  TituloInventarioUNIDAD();  // Asegúrate de que solo tenga las columnas que vamos a mostrar
    
    // Obtener lista de inventario desde la BD (solo líquidos)
    ArrayList<Inventario> ListaTerminada = ListaInventarioUNIDAD();

    // Modelo de la tabla
    modeloUNIDAD = (DefaultTableModel) InventarioUnidad.TablaUnidad.getModel();
    
    TableRowSorter<?> sorterOriginal = null;
    if (InventarioUnidad.TablaUnidad.getRowSorter() != null) {
        sorterOriginal = (TableRowSorter<?>)InventarioUnidad.TablaUnidad.getRowSorter();
      InventarioUnidad.TablaUnidad.setRowSorter(null);
    }
  
     limpiarTabla(modeloUNIDAD);

    // Arreglo para llenar cada fila (solo 6 columnas)
    Object[] obj = new Object[6];

    for (Inventario inv : ListaTerminada) {
        

        obj[0] = inv.getIdinventario();           // ID Inventario
        obj[1] = inv.getProductos();              // Nombre del producto
        obj[2] = inv.getCantidad_Disponible();    // Cantidad disponible
        obj[3] = inv.getStockMinimo();            // Stock mínimo
        obj[4] = inv.getStockMaximo();            // Stock máximo
        obj[5] = inv.getUbicacion();              // Ubicación (Pasillo - Estante)

        // Agregar la fila al modelo
        modeloUNIDAD.addRow(obj);
    }
 if (sorterOriginal != null) {
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modeloUNIDAD);
        InventarioUnidad.TablaUnidad.setRowSorter(sorter);
    }

    // Asignar el modelo actualizado a la tabla
    InventarioUnidad.TablaUnidad.setModel(modeloUNIDAD);

    // Centrar texto en la tabla
    centrarTextoTabla(InventarioUnidad.TablaUnidad);
}
 
 
 
  public void RegistrarStockProductoUNIDAD() throws ClassNotFoundException, SQLException {

    // Validar que los campos no estén vacíos
    if (InventarioUnidad.txtCantidadProducto.getText().trim().isEmpty() ||
        InventarioUnidad.txtStockMaximo.getText().trim().isEmpty() ||
       InventarioUnidad.txtStockMinimo.getText().trim().isEmpty() ||
        InventarioUnidad.txtCostoUnitario.getText().trim().isEmpty()) {
        
        JOptionPane.showMessageDialog(null, "Debes llenar todos los campos numéricos antes de registrar.");
        return; // salir del método
    }
     iv.setPrecio_Venta(Double.parseDouble((InventarioUnidad.txtPrecioVenta.getText())));
    // Ahora sí convertimos porque ya sabemos que no están vacíos
    iv.setCantidad((int) Double.parseDouble(InventarioUnidad.txtCantidadProducto.getText()));
    iv.setStockMaximo((int) Double.parseDouble(InventarioUnidad.txtStockMaximo.getText()));
    iv.setStockMinimo((int) Double.parseDouble(InventarioUnidad.txtStockMinimo.getText()));
   iv.setPrecio_Unitario(Double.parseDouble(InventarioUnidad.txtCostoUnitario.getText()));
           

    iv.setTipo_Producto(InventarioUnidad.txtTipoProductoUnidad.getText());
    
    iv.setPorcentaje(Integer.parseInt(InventarioUnidad.txtPorcentaje.getText()));
    
    cuenta SeleccionadaCUENTA = (cuenta) InventarioUnidad.jComboBoxCuantDebitar.getSelectedItem();

 int idCuenta = SeleccionadaCUENTA.getIdCuenta();
     

 
 cuenta SaleccionCuentaPasivo = (cuenta) InventarioUnidad.jComboBoxCuentaAcreditar.getSelectedItem();
 
 int idCuentaPasivo = SaleccionCuentaPasivo.getIdCuenta();
 
    Almacen AlmacenSeleccionado = (Almacen) InventarioUnidad.jComboBoxUbicacion.getSelectedItem();
    int idUbicacion = AlmacenSeleccionado.getId_Ubicacion();

    Producto productoSeleccionado = (Producto) InventarioUnidad.jComboProductoUnidad.getSelectedItem();
    int idProducdo = productoSeleccionado.getIdProducto();
    
    
      Proveedor proveedorSeleccionado = (Proveedor)  InventarioUnidad.jComboBoxProveedor.getSelectedItem();
        int idProveedor = proveedorSeleccionado.getIdProveedor();
    

    String sql = "INSERT INTO inventario (idProducto,stockMinimo, Cantidad,precio_Unitario,stockMaximo,id_Ubicacion,tipo_producto,Usuario,precio_venta,id_cuenta_inventario,id_cuenta_proveedor,porcentaje,idProveedor) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";

    try {
        this.conectar();
        ps = this.con.prepareStatement(sql);
        ps.setInt(1, idProducdo);
        ps.setInt(2, iv.getStockMinimo());
        ps.setInt(3, iv.getCantidad());
        ps.setDouble(4, iv.getPrecio_Unitario());
        ps.setInt(5, iv.getStockMaximo());
        ps.setInt(6, idUbicacion);
        ps.setString(7, iv.getTipo_Producto());
        ps.setString(8, Usuario.usuarioActual);
        ps.setDouble(9, iv.getPrecio_Venta());
        ps.setInt(10, idCuenta);
        ps.setInt(11, idCuentaPasivo);
        ps.setInt(12, iv.getPorcentaje());
        ps.setInt(13, idProveedor);

        if (ps.executeUpdate() > 0) {
            JOptionPane.showMessageDialog(null, "Registrado con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            MostrarUNIDADINVENTARIO();

            // limpiar campos
            InventarioUnidad.txtCostoUnitario.setText("");
            InventarioUnidad.txtCantidadProducto.setText("");
            InventarioUnidad.txtStockMaximo.setText("");
            InventarioUnidad.txtStockMinimo.setText("");
             InventarioUnidad.txtPrecioVenta.setText("");
           InventarioUnidad.jComboBoxProveedor.setSelectedIndex(0);
           InventarioUnidad.jComboProductoUnidad.setSelectedIndex(0);
           InventarioUnidad.jComboBoxUbicacion.setSelectedIndex(0);
        }

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al registrar  " + e.getMessage());
    }
}


   public void MostrarLoteUNIDAD() throws ClassNotFoundException, SQLException {
    // Cargar títulos
    TituloInventarioUNIDADLote();

    // Asignar modelo primero
    modeloUNIDADLOTE = (DefaultTableModel) InventarioUnidad.TablaLote.getModel();
   
TableRowSorter<?> sorterOriginal = null;
    if (InventarioUnidad.TablaLote.getRowSorter() != null) {
        sorterOriginal = (TableRowSorter<?>)InventarioUnidad.TablaLote.getRowSorter();
      InventarioUnidad.TablaLote.setRowSorter(null);
    }
    
       limpiarTabla(modeloUNIDADLOTE);
    // Obtener lista desde la BD
    ArrayList<Inventario> ListaTerminada = ListaDeLotUnidad();

    Object[] obj = new Object[10];

    for (Inventario inv : ListaTerminada) {
        obj[0] = inv.getIdinventario();          // ID
        obj[1] = inv.getProductos();             // Nombre
        obj[2] = inv.getCantidad_Disponible();   // Cantidad
        obj[3] = inv.getLote();                  // Lote
        obj[4] = inv.getFecha_Ingreso();         // Fecha ingreso
        obj[5] = inv.getPrecio_Unitario();       // Precio unitario
        obj[6] = inv.getCosto_Total();           // Costo total
        obj[7] = inv.getPrecio_Venta();          // Precio venta
        obj [8] = inv.getPorcentaje();
        obj [9] = inv.getIdProveedor();
        modeloUNIDADLOTE.addRow(obj);
    }if (sorterOriginal != null) {
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modeloUNIDADLOTE);
        InventarioUnidad.TablaLote.setRowSorter(sorter);
    }

    InventarioUnidad.TablaLote.setModel(modeloUNIDADLOTE);
    centrarTextoTabla(InventarioUnidad.TablaLote);
}


private ArrayList<Inventario> ListaDeLotUnidad() throws ClassNotFoundException, SQLException {
    ArrayList<Inventario> ListaInventario = new ArrayList<>();

    try {
        String Sql = "SELECT " +
                     "i.id_inventario, " +
                     "p.nombre AS nombreProducto, " +
                    "pr.nombre AS nombreProveedor," +
                     "i.Cantidad_Disponible, " +
                     "i.lote, " +
                     "i.fecha_ingreso, " +
                     "i.precio_Unitario, " +
                     "i.costo_Total, " +
                     "i.precio_venta, " +   // <-- espacio corregido
                     "i.porcentaje " +
                     "FROM inventario i " +
                     "INNER JOIN producto p ON p.idProducto = i.idProducto " +
                     "INNER JOIN proveedor pr ON pr.idProveedor = i.idProveedor " +
                     "WHERE i.tipo_producto = 'UNIDAD'" +
                     "ORDER BY i.id_inventario ";

        this.conectar();
        ps = this.con.prepareStatement(Sql);
        rs = ps.executeQuery();

        while (rs.next()) {
            Inventario inve = new Inventario();
            inve.setIdinventario(rs.getInt("id_inventario"));
            inve.setProductos(rs.getString("nombreProducto"));
            inve.setCantidad_Disponible(rs.getString("Cantidad_Disponible")); // <-- mejor int
            inve.setLote(rs.getString("lote"));
            inve.setFecha_Ingreso(rs.getString("fecha_ingreso"));
            inve.setPrecio_Unitario(rs.getDouble("precio_Unitario"));
            inve.setCosto_Total(rs.getBigDecimal("costo_Total"));
            inve.setPrecio_Venta(rs.getDouble("precio_venta"));
            inve.setPorcentaje(rs.getInt("porcentaje"));
             inve.setIdProveedor(rs.getString("nombreProveedor"));
            ListaInventario.add(inve);
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al obtener la lista del inventario: " + e.getMessage());
    } finally {
        this.cerrarCn();
    }

    return ListaInventario;
}


 private ArrayList<Inventario> ListaInventarioUNIDAD() throws ClassNotFoundException, SQLException {
    ArrayList<Inventario> ListaInventarioo = new ArrayList<>();

    try {
        String Sql = "SELECT " +
                     "i.id_inventario, " +
                     "p.nombre AS nombreProducto, " +
                     "i.Cantidad_Disponible, " +
                     "i.stockMinimo, " +
                     "i.stockMaximo, " +
                    "COALESCE(a.ala, '') AS ala, " +
                 "COALESCE(a.pasillo, '') AS pasillo, " +
                 "COALESCE(a.estante, '') AS estante, " +
                 "COALESCE(a.nivel, '') AS nivel, " +
                 "COALESCE(a.capacidad, '') AS capacidad " +
                     "FROM inventario i " +
                     "INNER JOIN producto p ON p.idProducto = i.idProducto " +
                     "LEFT JOIN almacen a ON a.id_Ubicacion = i.id_Ubicacion " +
                     "WHERE i.tipo_producto = 'UNIDAD'" +
                      "ORDER BY i.id_inventario";

        this.conectar();
        ps = this.con.prepareStatement(Sql);
        rs = ps.executeQuery();

        while (rs.next()) {
            Inventario inve = new Inventario();

            inve.setIdinventario(rs.getInt("id_inventario"));
            inve.setProductos(rs.getString("nombreProducto"));

            // Cantidad Disponible
            int cant = rs.getInt("Cantidad_Disponible");
            inve.setCantidad_Disponible(rs.wasNull() ? "0" : String.valueOf(cant));

            // Stocks
            int stockMin = rs.getInt("stockMinimo");
            inve.setStockMinimo(rs.wasNull() ? 0 : stockMin);

            int stockMax = rs.getInt("stockMaximo");
            inve.setStockMaximo(rs.wasNull() ? 0 : stockMax);

            // ==================== UBICACIÓN LIMPIA ====================
            String ala       = rs.getString("ala");
            String pasillo   = rs.getString("pasillo");
            String estante   = rs.getString("estante");
            String nivel     = rs.getString("nivel");
            String capacidad = rs.getString("capacidad");

            StringBuilder ubicacion = new StringBuilder();

            if (ala != null && !ala.trim().isEmpty()) {
                ubicacion.append("Ala: ").append(ala.trim()).append(" | ");
            }
            if (pasillo != null && !pasillo.trim().isEmpty()) {
                ubicacion.append(" ").append(pasillo.trim()).append(" | ");
            }
            if (estante != null && !estante.trim().isEmpty()) {
                ubicacion.append("Estante ").append(estante.trim()).append(" | ");
            }
            if (nivel != null && !nivel.trim().isEmpty()) {
                ubicacion.append("Nivel ").append(nivel.trim());
            }
            if (capacidad != null && !capacidad.trim().isEmpty()) {
                if (ubicacion.length() > 0) ubicacion.append(" | ");
                ubicacion.append("Cap: ").append(capacidad.trim());
            }

            String ubicFinal = ubicacion.toString().trim();
            // Quitar posible " | " al final
            if (ubicFinal.endsWith(" |")) {
                ubicFinal = ubicFinal.substring(0, ubicFinal.length() - 2);
            }

            inve.setUbicacion(ubicFinal.isEmpty() ? "Sin ubicación asignada" : ubicFinal);
  // Ahora incluye Ala, Pasillo, Estante y Nivel

            ListaInventarioo.add(inve);
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al obtener la lista del inventario: " + e.getMessage());
    } finally {
        this.cerrarCn();
    }

    return ListaInventarioo;
}

 
    

 
public void agregarFiltroBusquedaUnidad() {
    // Verifica que el campo de búsqueda y la tabla existan
    if (InventarioUnidad.txtBuscarProductoEnTABLA != null && InventarioUnidad.TablaUnidad != null) {

        // Agregamos un listener persistente
       InventarioUnidad.txtBuscarProductoEnTABLA.getDocument().addDocumentListener(new DocumentListener() {

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
                String texto = InventarioUnidad.txtBuscarProductoEnTABLA.getText().trim();

                // Siempre obtener el modelo actual
                DefaultTableModel modeloActual = (DefaultTableModel) InventarioUnidad.TablaUnidad.getModel();

                // Verificar o crear sorter
                TableRowSorter<?> sorter = (TableRowSorter<?>) InventarioUnidad.TablaUnidad.getRowSorter();
                if (sorter == null || sorter.getModel() != modeloActual) {
                    sorter = new TableRowSorter<>(modeloActual);
                    InventarioUnidad.TablaUnidad.setRowSorter(sorter);
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




public void agregarFiltroBusquedaUnidadLote() {
    // Verifica que el campo de búsqueda y la tabla existan
    if (InventarioUnidad.txtBuscarLote!= null && InventarioUnidad.TablaLote != null) {

        // Agregamos un listener persistente
       InventarioUnidad.txtBuscarLote.getDocument().addDocumentListener(new DocumentListener() {

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
                String texto = InventarioUnidad.txtBuscarLote.getText().trim();

                // Siempre obtener el modelo actual
                DefaultTableModel modeloActual = (DefaultTableModel) InventarioUnidad.TablaLote.getModel();

                // Verificar o crear sorter
                TableRowSorter<?> sorter = (TableRowSorter<?>) InventarioUnidad.TablaLote.getRowSorter();
                if (sorter == null || sorter.getModel() != modeloActual) {
                    sorter = new TableRowSorter<>(modeloActual);
                    InventarioUnidad.TablaLote.setRowSorter(sorter);
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







public  void ActualizarUNIDADINVENTARIO (int  IdInventario) throws ClassNotFoundException,SQLException{
    
    iv.setStockMaximo((int) Double.parseDouble(InventarioUnidad.txtStockMaximo.getText()));
     iv.setStockMinimo((int) Double.parseDouble(InventarioUnidad.txtStockMinimo.getText()));
    // iv.setIdinventario(Integer.parseInt(InventarioUnidad.txtIDUNIDAD.getText()));
     
     
     Almacen AlmacenSeleccionado = (Almacen) InventarioUnidad.jComboBoxUbicacion.getSelectedItem();
     
     int idUbicacion = AlmacenSeleccionado.getId_Ubicacion();
     
    Producto productoSeleccionado = (Producto) InventarioUnidad.jComboProductoUnidad.getSelectedItem();
    
    int idProducdo = productoSeleccionado.getIdProducto();

String sql = "Update inventario set idProducto=?, stockMinimo =?,stockMaximo=?,id_Ubicacion=? where id_inventario =?";

    try {
        this.conectar();
        ps = this.con.prepareStatement(sql);
        
        ps.setInt(1, idProducdo);
        ps.setInt(2, iv.getStockMinimo());
        ps.setInt(3, iv.getStockMaximo());
        ps.setInt(4, idUbicacion);
        ps.setInt(5, IdInventario);
         
        if (ps.executeUpdate() >0) {
           JOptionPane.showMessageDialog(null, "Datos actualizados", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            MostrarUNIDADINVENTARIO();
            
            InventarioUnidad.btbConfirmarActualizacion.setVisible(false);
  InventarioUnidad.btbCancelarModificaion.setVisible(false);
  
  InventarioUnidad.btbModificar.setVisible(true);
  InventarioUnidad.btbHistorialMovimiento.setVisible(true);
  InventarioUnidad.btbInofrmacionLote.setVisible(true);
  InventarioUnidad.btbRegistrar.setVisible(true);
  InventarioUnidad.txtStockMaximo.setText("");
  InventarioUnidad.txtStockMinimo.setText("");
            
        }
        
  
     
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error " + e.getMessage());
    }


      
}

public void ActualizarUNIDADSeguridad() throws ClassNotFoundException, SQLException {
    int fila = InventarioUnidad.TablaUnidad.getSelectedRow();

    if (fila == -1) {
        JOptionPane.showMessageDialog(null, "Debe seleccionar un producto para Modificarlo");
        return;
    }

    int idInventario = Integer.parseInt(InventarioUnidad.TablaUnidad.getValueAt(fila, 0).toString()
    );

    int confirmar = JOptionPane.showConfirmDialog(
        null,
        "¿Estás seguro de modificar este Lote?",
        "Confirmar modificación",
        JOptionPane.YES_NO_OPTION
    );

    if (confirmar == JOptionPane.YES_OPTION) {
        ActualizarUNIDADINVENTARIO(idInventario);

// refrescar tabla
    }
}


public  void ActualizarLoteUNIDAD(int IdInventario) throws  ClassNotFoundException,SQLException{
    
   //iv.setIdinventario(Integer.parseInt((G.txtIDUNIDAD.getText())));
    iv.setCantidad((int) Double.parseDouble(InventarioUnidad.txtCantidadProducto.getText()));
    iv.setPrecio_Unitario(Double.parseDouble((InventarioUnidad.txtCostoUnitario.getText())));
    iv.setPorcentaje(Integer.parseInt(InventarioUnidad.txtPorcentaje.getText()));
    
   iv.setPrecio_Venta(Double.parseDouble((InventarioUnidad.txtPrecioVenta.getText())));
   
    Proveedor proveedorSeleccionado = (Proveedor)  InventarioUnidad.jComboBoxProveedor.getSelectedItem();
        int idProveedor = proveedorSeleccionado.getIdProveedor();
    
    
    String sql = "UPDATE inventario SET Cantidad=?,precio_Unitario=?, precio_venta=?,porcentaje=?,idProveedor=?  where id_inventario=? ";
    
    try {
        this.conectar();
        ps = this.con.prepareStatement(sql);
        
        ps.setInt(1, iv.getCantidad());
        ps.setDouble(2, iv.getPrecio_Unitario());
        ps.setDouble(3, iv.getPrecio_Venta());
        ps.setInt(4, iv.getPorcentaje());
        ps.setInt(5, idProveedor);
        ps.setInt(6, IdInventario);
        
        if (ps.executeUpdate() > 0) {
               JOptionPane.showMessageDialog(null, "Lote actualizado", "Éxito", JOptionPane.INFORMATION_MESSAGE);
          
        MostrarLoteUNIDAD();
            
         InventarioUnidad.btbModificarLote.setVisible(true);
    InventarioUnidad.btbOcultarLote.setVisible(true);
    
    InventarioUnidad.btbConfirmaModificacionLote.setVisible(false);
    InventarioUnidad.btbCancelarModificacionLote.setVisible(false);
    
    InventarioUnidad.txtCantidadProducto.setText("");
    InventarioUnidad.txtCostoUnitario.setText("");
    InventarioUnidad.txtPrecioVenta.setText("");
            
        }
        
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error " + e.getMessage());
    }
    
    
    
    
}






    public void ActualizarLoteConseguridad (){
        
     int filla = InventarioUnidad.TablaLote.getSelectedRow();
     
         
        
        if (filla == -1) {
        JOptionPane.showMessageDialog(null, "Debe seleccionar un producto para Modificarlo");
        return;
    }

    int idInventario = Integer.parseInt(InventarioUnidad.TablaLote.getValueAt(filla, 0).toString()
    );

    int confirmar = JOptionPane.showConfirmDialog(
        null,
        "¿Estás seguro de modificar este Lote?",
        "Confirmar modificación",
        JOptionPane.YES_NO_OPTION
    );

    if (confirmar == JOptionPane.YES_OPTION) {
         try {
             ActualizarLoteUNIDAD(idInventario);
             

         } catch (ClassNotFoundException ex) {
             System.getLogger(InventarioUnidadDAO.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
         } catch (SQLException ex) {
             System.getLogger(InventarioUnidadDAO.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
         }
    }
        
        
        
    }






    
    private ArrayList<Inventario> ListaInventarioUNidadCom() throws ClassNotFoundException, SQLException {
    ArrayList<Inventario> ListaInventarioo = new ArrayList<>();

    try {
        String Sql = "SELECT " +
                     "i.id_inventario, " +
                      "p.idProducto,"+
                     "p.nombre AS nombreProducto, " +
                     "i.Cantidad_Disponible, " +
                     "i.precio_venta," +
                     "i.stockMinimo, " +
                     "i.stockMaximo, " +
                     "CONCAT('Ala-', a.ala, ' -Pasillo ', a.pasillo, ' -Estante ', a.estante, ' -Nivel ', a.nivel) AS nombreUbicacion " +
                     "FROM inventario i " +
                     "INNER JOIN producto p ON p.idProducto = i.idProducto " +
                     "LEFT JOIN almacen a ON a.id_Ubicacion = i.id_Ubicacion " +
                     "WHERE i.tipo_producto = 'UNIDAD'";

        this.conectar();
        ps = this.con.prepareStatement(Sql);
        rs = ps.executeQuery();

        while (rs.next()) {
            Inventario inve = new Inventario();

            inve.setIdinventario(rs.getInt("id_inventario"));
             inve.setIdProducto(rs.getInt("idProducto"));
            inve.setProductos(rs.getString("nombreProducto"));
            inve.setCantidad_Disponible(rs.getString("Cantidad_Disponible"));
            inve.setPrecio_Venta(rs.getDouble("precio_venta"));
            inve.setStockMinimo(rs.getInt("stockMinimo"));
            inve.setStockMaximo(rs.getInt("stockMaximo"));
            inve.setUbicacion(rs.getString("nombreUbicacion"));  // Ahora incluye Ala, Pasillo, Estante y Nivel

            ListaInventarioo.add(inve);
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al obtener la lista del inventario: " + e.getMessage());
    } finally {
        this.cerrarCn();
    }

    return ListaInventarioo;
}

    
    /**
 * Carga la información completa de un producto UNIDAD en el TextArea
 */
private void cargarInformacionCompletaProductoUnidad(int idInventario) throws SQLException, ClassNotFoundException {
    
    String sql = """
        SELECT 
            p.nombre,
            p.numero_serial,
            p.compatibilidad,
            p.unidad_De_Medidad,
            p.especificaciones,
            m.nombre AS marca,
            i.lote,
            i.Fecha_Vencimiento,
            i.Cantidad_Disponible,
            a.pasillo,
            a.ala,
            a.estante,
            a.nivel
        FROM inventario i
        JOIN producto p ON i.idProducto = p.idProducto
        JOIN marca m ON p.idMarca = m.idMarca
        LEFT JOIN almacen a ON i.id_Ubicacion = a.id_ubicacion
        WHERE i.id_inventario = ?
        """;

    try {
        this.conectar();
        
        try (PreparedStatement ps = this.con.prepareStatement(sql)) {
            ps.setInt(1, idInventario);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    StringBuilder info = new StringBuilder();
                    info.append("===   INFORMACIÓN COMPLETA  ===\n\n");
                    
                    info.append("Producto : ").append(rs.getString("nombre")).append("\n");
                    info.append("Marca : ").append(rs.getString("marca")).append("\n");
                    info.append("Número Serial  : ").append(rs.getString("numero_serial") != null ? rs.getString("numero_serial") : "N/A").append("\n");
                    info.append("Compatibilidad : ").append(rs.getString("compatibilidad") != null ? rs.getString("compatibilidad") : "N/A").append("\n");
                    info.append("Unidad Medida  : ").append(rs.getString("unidad_De_Medidad") != null ? rs.getString("unidad_De_Medidad") : "N/A").append("\n");
                    info.append("Especificaciones: ").append(rs.getString("especificaciones") != null ? rs.getString("especificaciones") : "N/A").append("\n\n");
                    
                    info.append("=== INVENTARIO ===\n");
                    info.append("Lote : ").append(rs.getString("lote") != null ? rs.getString("lote") : "N/A").append("\n\n");
                  //  info.append("Fecha de Vencimiento: ").append(rs.getString("Fecha_Vencimiento") != null ? rs.getString("Fecha_Vencimiento") : "N/A").append("\n");
                   // info.append("Cantidad Disponible : ").append(rs.getInt("Cantidad_Disponible")).append("\n\n");
                    
                    info.append("=== UBICACIÓN ===\n");
                    if (rs.getString("pasillo") != null) {
                        info.append("Pasillo : ").append(rs.getString("pasillo")).append("\n");
                        info.append("Ala : ").append(rs.getString("ala")).append("\n");
                        info.append("Estante : ").append(rs.getInt("estante")).append("\n");
                        info.append("Nivel : ").append(rs.getInt("nivel")).append("\n");
                    } else {
                        info.append("Sin ubicación asignada\n");
                    }

                    Vista_Salida_Unidad.TxtInformacionProducto.setText(info.toString());
                    
                } else {
                    Vista_Salida_Unidad.TxtInformacionProducto.setText("No se encontró información del producto.");
                }
            }
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, 
            "Error al cargar información del producto Unidad:\n" + e.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    } 
}
    
  public void CargarComboxBoxUnidad() throws ClassNotFoundException, SQLException {
    ArrayList<Inventario> listaProducto = this.ListaInventarioUNidadCom();
    
    // Limpiamos el ComboBox
    Vista_Salida_Unidad.jComboBoxProductoUnida.removeAllItems();
    
    // Agregamos los productos
    for (Inventario inve : listaProducto) {
        Vista_Salida_Unidad.jComboBoxProductoUnida.addItem(inve);
    }
    
    // Removemos ActionListeners anteriores para evitar duplicados
    for (ActionListener al : Vista_Salida_Unidad.jComboBoxProductoUnida.getActionListeners()) {
        Vista_Salida_Unidad.jComboBoxProductoUnida.removeActionListener(al);
    }
    
    // Nuevo ActionListener
    Vista_Salida_Unidad.jComboBoxProductoUnida.addActionListener(e -> {
        Inventario seleccionado = (Inventario) Vista_Salida_Unidad.jComboBoxProductoUnida.getSelectedItem();
        
        if (seleccionado != null) {
            try {
                // Actualizar precio de venta
                Vista_Salida_Unidad.txtPrecioVentaUnitario.setText(
                    String.valueOf(seleccionado.getPrecio_Venta())
                );
                
                // Cargar información completa del producto Unidad
                cargarInformacionCompletaProductoUnidad(seleccionado.getIdinventario());
                
            } catch (SQLException | ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(null,
                    "Error al cargar información del producto:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    });
}



  
  
  
  
  
  private void cargarInformacionCompletaProductoUnidadAjuste(int idInventario) throws SQLException, ClassNotFoundException {
    
    String sql = """
        SELECT 
            p.nombre,
            p.numero_serial,
            p.compatibilidad,
            p.unidad_De_Medidad,
            p.especificaciones,
            m.nombre AS marca,
            i.lote,
            i.Fecha_Vencimiento,
            i.Cantidad_Disponible,
            a.pasillo,
            a.ala,
            a.estante,
            a.nivel
        FROM inventario i
        JOIN producto p ON i.idProducto = p.idProducto
        JOIN marca m ON p.idMarca = m.idMarca
        LEFT JOIN almacen a ON i.id_Ubicacion = a.id_ubicacion
        WHERE i.id_inventario = ?
        """;

    try {
        this.conectar();
        
        try (PreparedStatement ps = this.con.prepareStatement(sql)) {
            ps.setInt(1, idInventario);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    StringBuilder info = new StringBuilder();
                    info.append("===   INFORMACIÓN COMPLETA  ===\n\n");
                    
                    info.append("Producto : ").append(rs.getString("nombre")).append("\n");
                    info.append("Marca : ").append(rs.getString("marca")).append("\n");
                    info.append("Número Serial  : ").append(rs.getString("numero_serial") != null ? rs.getString("numero_serial") : "N/A").append("\n");
                    info.append("Compatibilidad : ").append(rs.getString("compatibilidad") != null ? rs.getString("compatibilidad") : "N/A").append("\n");
                    info.append("Unidad Medida  : ").append(rs.getString("unidad_De_Medidad") != null ? rs.getString("unidad_De_Medidad") : "N/A").append("\n");
                    info.append("Especificaciones: ").append(rs.getString("especificaciones") != null ? rs.getString("especificaciones") : "N/A").append("\n\n");
                    
                    info.append("=== INVENTARIO ===\n");
                    info.append("Lote : ").append(rs.getString("lote") != null ? rs.getString("lote") : "N/A").append("\n\n");
                  //  info.append("Fecha de Vencimiento: ").append(rs.getString("Fecha_Vencimiento") != null ? rs.getString("Fecha_Vencimiento") : "N/A").append("\n");
                   // info.append("Cantidad Disponible : ").append(rs.getInt("Cantidad_Disponible")).append("\n\n");
                    
                    info.append("=== UBICACIÓN ===\n");
                    if (rs.getString("pasillo") != null) {
                        info.append("Pasillo : ").append(rs.getString("pasillo")).append("\n");
                        info.append("Ala : ").append(rs.getString("ala")).append("\n");
                        info.append("Estante : ").append(rs.getInt("estante")).append("\n");
                        info.append("Nivel : ").append(rs.getInt("nivel")).append("\n");
                    } else {
                        info.append("Sin ubicación asignada\n");
                    }

                   Ajuste_Unidad.jTextAreaInformacionProducto.setText(info.toString());
                    
                } else {
                      Ajuste_Unidad.jTextAreaInformacionProducto.setText("No se encontró información del producto.");
                }
            }
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, 
            "Error al cargar información del producto Unidad:\n" + e.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    } 
}
    
  public void CargarComboxBoxUnidadaJUSTE() throws ClassNotFoundException, SQLException {
    ArrayList<Inventario> listaProducto = this.ListaInventarioUNidadCom();
    
    // Limpiamos el ComboBox
    Ajuste_Unidad.jComboBoxProductoUnidad.removeAllItems();
    
    // Agregamos los productos
    for (Inventario inve : listaProducto) {
         Ajuste_Unidad.jComboBoxProductoUnidad.addItem(inve);
    }
    
    // Removemos ActionListeners anteriores para evitar duplicados
    for (ActionListener al :   Ajuste_Unidad.jComboBoxProductoUnidad.getActionListeners()) {
          Ajuste_Unidad.jComboBoxProductoUnidad.removeActionListener(al);
    }
    
    // Nuevo ActionListener
    Ajuste_Unidad.jComboBoxProductoUnidad.addActionListener(e -> {
        Inventario seleccionado = (Inventario)   Ajuste_Unidad.jComboBoxProductoUnidad.getSelectedItem();
        
        if (seleccionado != null) {
            try {
            /*    // Actualizar precio de venta
                Vista_Salida_Unidad.txtPrecioVentaUnitario.setText(
                    String.valueOf(seleccionado.getPrecio_Venta())
                );
                */
                // Cargar información completa del producto Unidad
                cargarInformacionCompletaProductoUnidadAjuste(seleccionado.getIdinventario());
                
            } catch (SQLException | ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(null,
                    "Error al cargar información del producto:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    });
}

    
    
}


 
