/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ModeloDAO;

import Modelo.Almacen;

import Modelo.ConexiónBD;
import Modelo.Inventario;
import Modelo.Producto;
import Modelo.Proveedor;
import Modelo.Usuario;
import Modelo.cuenta;
import Vista_Almacen.Ajuste_Solido;

import Vista_GestionInventario.InventarioSolido;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableRowSorter;
import Vista_Almacen.Vista_Salida_Solido ;
import java.awt.event.ActionListener;


public class InventarioSolidoDAO  extends  ConexiónBD{
    
    
      private ResultSet rs;
    
    private PreparedStatement ps;
    
      DefaultTableModel modeloLotoSolido = new DefaultTableModel();
    
      DefaultTableModel modeloSolido = new DefaultTableModel();
    
      public void TituloInventarioSolido() {
    
    
    String Titulo[] = {
        "ID", 
        "Nombre", 
        "Cantidad Disponible", 
        "Stock Minimo",
        "Stock Máximo", 
        "Ubicación"
        
    };

    modeloSolido.setColumnIdentifiers(Titulo);

    if (InventarioSolido.TablaSolido != null) {
       InventarioSolido.TablaSolido.setModel(modeloSolido);

        // 👇 aplicar centrado de datos después de setear el modelo
        centrarTextoTabla(InventarioSolido.TablaSolido);
    }
} 
    
      
      public void TituloInventarioSolidoLote() {
    String Titulo[] = {
        "ID", 
        "Nombre", 
        "Cantidad",
        "Lote",
        "Fecha ingreso", 
        "Costo Unitario", 
        "Costo total",
        "Precio venta",
        "% Ganancia",
        "Proveedor"
            
        
    };

    modeloLotoSolido.setColumnIdentifiers(Titulo);

  if (InventarioSolido.TablaLote != null) {
        InventarioSolido.TablaLote.setModel(modeloLotoSolido);
        // 👇 aplicar centrado de datos después de setear el modelo
        centrarTextoTabla(InventarioSolido.TablaLote);
    }
}

      
      public void centrarTextoTabla(JTable tabla) {
    DefaultTableCellRenderer centrado = new DefaultTableCellRenderer();
    centrado.setHorizontalAlignment(SwingConstants.CENTER); // mejor usar SwingConstants

    // Recorremos todas las columnas visibles
    for (int i = 0; i < tabla.getColumnModel().getColumnCount(); i++) {
        tabla.getColumnModel().getColumn(i).setCellRenderer(centrado);
    }
}


 
   public void RegistrarStockProductoSolido() throws ClassNotFoundException, SQLException {

       Inventario iv = new Inventario();
       
    // Validar que los campos no estén vacíos
    if (InventarioSolido.txtCantidadSolido.getText().trim().isEmpty() ||
       InventarioSolido.txtStoxkMaximo.getText().trim().isEmpty() ||
        InventarioSolido.txtStockMinimo.getText().trim().isEmpty() ||
        InventarioSolido.txtCostoUUnitario.getText().trim().isEmpty()) {
        
        JOptionPane.showMessageDialog(null, "Debes llenar todos los campos numéricos antes de registrar.");
        return; // salir del método
    }
      iv.setPrecio_Venta(Double.parseDouble((InventarioSolido.txtPrecioVentaSolido.getText())));
         
      iv.setPorcentaje(Integer.parseInt(InventarioSolido.txtPorcentajeSolido.getText()));
    // Ahora sí convertimos porque ya sabemos que no están vacíos
    iv.setCantidad((int) Double.parseDouble(InventarioSolido.txtCantidadSolido.getText()));
    iv.setStockMaximo((int) Double.parseDouble(InventarioSolido.txtStoxkMaximo.getText()));
    iv.setStockMinimo((int) Double.parseDouble(InventarioSolido.txtStockMinimo.getText()));
    iv.setPrecio_Unitario(Double.parseDouble((InventarioSolido.txtPrecioVentaSolido.getText())));

    iv.setTipo_Producto(InventarioSolido.txtTipoProducto.getText());

    Almacen AlmacenSeleccionado = (Almacen) InventarioSolido.jComboUbicacionSolido.getSelectedItem();
    int idUbicacion = AlmacenSeleccionado.getId_Ubicacion();

    cuenta SeleccionadaCUENTA = (cuenta)InventarioSolido.jComboBoxCuentaDebitar.getSelectedItem();

 int idCuenta = SeleccionadaCUENTA.getIdCuenta();
     
 
 cuenta SaleccionCuentaPasivo = (cuenta) InventarioSolido.jComboBoxCuentaAcreditar.getSelectedItem();
 
 int idCuentaPasivo = SaleccionCuentaPasivo.getIdCuenta();
 
 
    
    Producto productoSeleccionado = (Producto) InventarioSolido.jComboProductoSolido.getSelectedItem();
    int idProducdo = productoSeleccionado.getIdProducto();
    
    
      Proveedor proveedorSeleccionado = (Proveedor)  InventarioSolido.jComboBoxProveedor.getSelectedItem();
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
             JOptionPane.showMessageDialog(null, "Registro exitoso del producto en el inventari", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    
                  
            MostrarVerInventarioSOLIDO();

           
            InventarioSolido.txtCostoUUnitario.setText("");
            InventarioSolido.txtCantidadSolido.setText("");
            InventarioSolido.txtStoxkMaximo.setText("");
            InventarioSolido.txtStoxkMaximo.setText("");
            InventarioSolido.txtPrecioVentaSolido.setText("");
           
        }

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al registrar  " + e.getMessage());
    }
}

             private void limpiarTabla(DefaultTableModel modelo) {
    modelo.setRowCount(0);  // elimina todas las filas
}
    

 public void MostrarVerInventarioSOLIDO() throws ClassNotFoundException, SQLException {
    // Cargar títulos y limpiar tabla
  TituloInventarioSolido();  // Asegúrate de que solo tenga las columnas que vamos a mostrar
   
  
    // Obtener lista de inventario desde la BD (solo líquidos)
    ArrayList<Inventario> ListaTerminada = MostrarInventarioSolido();

    // Modelo de la tabla
    modeloSolido = (DefaultTableModel) InventarioSolido.TablaSolido.getModel();

    
    // === DESACTIVAR SORTER TEMPORALMENTE ===
    TableRowSorter<?> sorterOriginal = null;
    if (InventarioSolido.TablaSolido.getRowSorter() != null) {
        sorterOriginal = (TableRowSorter<?>)InventarioSolido.TablaSolido.getRowSorter();
      InventarioSolido.TablaSolido.setRowSorter(null);
    }
    
     limpiarTabla(modeloSolido);
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
        modeloSolido.addRow(obj);
    }
// 4. Restaurar el sorter
    if (sorterOriginal != null) {
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modeloSolido);
        InventarioSolido.TablaSolido.setRowSorter(sorter);
    }

    // Asignar el modelo actualizado a la tabla
    InventarioSolido.TablaSolido.setModel(modeloSolido);

    // Centrar texto en la tabla
    centrarTextoTabla( InventarioSolido.TablaSolido);
}

 
 
 
 private ArrayList<Inventario> MostrarInventarioSolido() throws ClassNotFoundException, SQLException {
    ArrayList<Inventario> ListaInventarioo = new ArrayList<>();

    try {
        String Sql = "SELECT " +
                     "i.id_inventario, " +
                     "p.nombre AS nombreProducto, " +
                     "i.Cantidad_Disponible, " +
                      "i.precio_Unitario,"+
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
                     "WHERE i.tipo_producto = 'SOLIDO'" +
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
                ubicacion.append("").append(pasillo.trim()).append(" | ");
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

            ListaInventarioo.add(inve);
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al obtener la lista del inventario: " + e.getMessage());
    } finally {
        this.cerrarCn();
    }

    return ListaInventarioo;
}
 
 
 
 
 
 
    
 
public void agregarFiltroBusquedaSOLID() {
    // Verifica que el campo de búsqueda y la tabla existan
    if (InventarioSolido.txtBuscarProductoEntabla != null && InventarioSolido.TablaSolido != null) {

        // Agregamos un listener persistente
      InventarioSolido.txtBuscarProductoEntabla.getDocument().addDocumentListener(new DocumentListener() {

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
                String texto = InventarioSolido.txtBuscarProductoEntabla.getText().trim();

                // Siempre obtener el modelo actual
                DefaultTableModel modeloActual = (DefaultTableModel) InventarioSolido.TablaSolido.getModel();

                // Verificar o crear sorter
                TableRowSorter<?> sorter = (TableRowSorter<?>) InventarioSolido.TablaSolido.getRowSorter();
                if (sorter == null || sorter.getModel() != modeloActual) {
                    sorter = new TableRowSorter<>(modeloActual);
                    InventarioSolido.TablaSolido.setRowSorter(sorter);
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


public void MostrarVerInventarioSolidoLote() throws ClassNotFoundException, SQLException {
    // Cargar títulos y limpiar tabla
    TituloInventarioSolidoLote();  // asegúrate que los títulos incluyan Cantidad

    // Obtener lista de inventario desde la BD (solo sólidos)
    ArrayList<Inventario> ListaTerminada = ListaDeLoteSolido(); // Método que trae todos los campos necesarios

    // Modelo de la tabla: asignarlo antes de limpiarlo (evita NPE)
    modeloLotoSolido = (DefaultTableModel) InventarioSolido.TablaLote.getModel();
   
    
     TableRowSorter<?> sorterOriginal = null;
    if (InventarioSolido.TablaLote.getRowSorter() != null) {
        sorterOriginal = (TableRowSorter<?>)InventarioSolido.TablaLote.getRowSorter();
      InventarioSolido.TablaLote.setRowSorter(null);
    }

    // Arreglo para llenar cada fila (8 columnas)
    Object[] obj = new Object[10];

    for (Inventario inv: ListaTerminada) {
      

        obj[0] = inv.getIdinventario();           // ID
        obj[1] = inv.getProductos();               // Nombre
        obj[2] = inv.getCantidad();                // Cantidad
        obj[3] = inv.getLote();                    // Lote
        obj[4] = inv.getFecha_Ingreso();           // Fecha ingreso
        obj[5] = inv.getPrecio_Unitario();         // Precio Unitario
        obj[6] = inv.getCosto_Total();             // Costo total
        obj[7] = inv.getPrecio_Venta();            // Precio Venta
        obj [8] = inv.getPorcentaje();
        obj [9] = inv.getIdProveedor();

        // Agregar la fila al modelo
        modeloLotoSolido.addRow(obj);
    }
 if (sorterOriginal != null) {
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modeloLotoSolido);
        InventarioSolido.TablaLote.setRowSorter(sorter);
    }

    // Asignar el modelo actualizado a la tabla (no imprescindible si ya usas el mismo modelo)
 InventarioSolido.TablaLote.setModel(modeloLotoSolido);

    // Centrar texto en la tabla
    centrarTextoTabla(InventarioSolido.TablaLote);
}


private ArrayList<Inventario> ListaDeLoteSolido() throws ClassNotFoundException, SQLException {
    ArrayList<Inventario> ListaInventario = new ArrayList<>();

    try {
        String Sql = "SELECT " +
                     "i.id_inventario, " +
                     "p.nombre AS nombreProducto, " +
                     "pr.nombre AS nombreProveedor," +
                     "i.Cantidad, " +
                     "i.lote, " +
                     "i.fecha_ingreso, " +
                     "i.precio_Unitario, " +
                     "i.costo_Total, " +   // <<< asegurarse de NO dejar coma final antes de FROM
                     "i.precio_venta, " +   // última columna sin coma
                     "i.porcentaje " +
                     "FROM inventario i " +
                     "INNER JOIN producto p ON p.idProducto = i.idProducto " +
                     "INNER JOIN proveedor pr ON pr.idProveedor = i.idProveedor " +
                     "WHERE i.tipo_producto = 'SOLIDO'" +
                    
                "ORDER BY i.id_inventario";

        this.conectar();
        ps = this.con.prepareStatement(Sql);
        rs = ps.executeQuery();

        while (rs.next()) {
            Inventario inve = new Inventario();

            inve.setIdinventario(rs.getInt("id_inventario"));
            inve.setProductos(rs.getString("nombreProducto"));
            inve.setCantidad(rs.getInt("Cantidad"));
            inve.setLote(rs.getString("lote"));
            inve.setFecha_Ingreso(rs.getString("fecha_ingreso"));
            // Ajusta los siguientes getters según el tipo real en tu clase Inventario:
            inve.setPrecio_Unitario(rs.getDouble("precio_Unitario")); // si tu setter recibe double
            inve.setCosto_Total(rs.getBigDecimal("costo_Total"));         // si es double; si es String, usa getString
            inve.setPrecio_Venta(rs.getDouble("precio_venta"));       // idem
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


public void agregarFiltroBusquedalOTE() {
    // Verifica que el campo de búsqueda y la tabla existan
    if (InventarioSolido.txtBuscarLote != null && InventarioSolido.TablaLote != null) {

        // Agregamos un listener persente
       InventarioSolido.txtBuscarLote.getDocument().addDocumentListener(new DocumentListener() {

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
                String texto = InventarioSolido.txtBuscarLote.getText().trim();

                // Siempre obtener el modelo actual
                DefaultTableModel modeloActual = (DefaultTableModel) InventarioSolido.TablaLote.getModel();

                // Verificar o crear sorter
                TableRowSorter<?> sorter = (TableRowSorter<?>) InventarioSolido.TablaLote.getRowSorter();
                if (sorter == null || sorter.getModel() != modeloActual) {
                    sorter = new TableRowSorter<>(modeloActual);
                    InventarioSolido.TablaLote.setRowSorter(sorter);
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




public  void ActualizarLoteSOLIDOInventario (int  idInventario) throws  ClassNotFoundException,SQLException{
    
   Inventario iv = new Inventario();
   
    iv.setCantidad((int) Double.parseDouble(InventarioSolido.txtCantidadSolido.getText()));
    iv.setPrecio_Unitario(Double.parseDouble((InventarioSolido.txtCostoUUnitario.getText())));
   
  //  iv.setPorcentaje(Integer.parseInt(Gestion_Inventarioo.txtPorcentaje_Solido.getText()));

  //  iv.setIdinventario(Integer.parseInt((Gestion_Inventarioo.txtIDSolido.getText())));
      
   iv.setPrecio_Venta(Double.parseDouble((InventarioSolido.txtPrecioVentaSolido.getText())));
   
    Proveedor proveedorSeleccionado = (Proveedor)  InventarioSolido.jComboBoxProveedor.getSelectedItem();
        int idProveedor = proveedorSeleccionado.getIdProveedor();
    
    String sql = "UPDATE inventario SET Cantidad=?,precio_Unitario=?,precio_venta=?, idProveedor=? where id_inventario=? ";
    
    try {
        this.conectar();
        ps = this.con.prepareStatement(sql);
        
        ps.setInt(1, iv.getCantidad());
        ps.setDouble(2, iv.getPrecio_Unitario());
          ps.setDouble(3, iv.getPrecio_Venta());
        ps.setInt(4, idProveedor);
        ps.setInt(5, idInventario);
     
        
        if (ps.executeUpdate() > 0) {
           JOptionPane.showMessageDialog(null, "Modificacion correcta de lote");
          
         MostrarVerInventarioSolidoLote();
            
      InventarioSolido.txtCantidadSolido.setText("");
      InventarioSolido.txtCostoUUnitario.setText("");
      InventarioSolido.txtPrecioVentaSolido.setText("");
      InventarioSolido.jComboBoxProveedor.setSelectedIndex(0);
      InventarioSolido.btbConfirmarActualizacionLote.setVisible(false);
      InventarioSolido.btbCancelarActualizcionLote.setVisible(false);
      InventarioSolido.btbModificarLote.setVisible(true);
      InventarioSolido.btbOcultarLote.setVisible(true);
            
        }
        
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error " + e.getMessage());
    }
    
    
    
    
}
public void ActualizarLoteSolidoSeguridad() throws ClassNotFoundException, SQLException {
    int fila = InventarioSolido.TablaLote.getSelectedRow();

    if (fila == -1) {
        JOptionPane.showMessageDialog(null, "Debe seleccionar un producto para Modificarlo");
        return;
    }

    int idInventario = Integer.parseInt(InventarioSolido.TablaLote.getValueAt(fila, 0).toString()
    );

    int confirmar = JOptionPane.showConfirmDialog(
        null,
        "¿Estás seguro de modificar este Lote?",
        "Confirmar modificación",
        JOptionPane.YES_NO_OPTION
    );

    if (confirmar == JOptionPane.YES_OPTION) {
        ActualizarLoteSOLIDOInventario(idInventario);

// refrescar tabla
    }
}

public  void ActualizarDatosSOLIDOInventario (int idInventario) throws ClassNotFoundException,SQLException{
    Inventario iv = new Inventario();
    iv.setStockMaximo((int) Double.parseDouble(InventarioSolido.txtStoxkMaximo.getText()));
     iv.setStockMinimo((int) Double.parseDouble(InventarioSolido.txtStockMinimo.getText()));
    // iv.setIdinventario(Integer.parseInt(Gestion_Inventarioo.txtIDSolido.getText()));
     
    
     Almacen AlmacenSeleccionado = (Almacen) InventarioSolido.jComboUbicacionSolido.getSelectedItem();
     
     int idUbicacion = AlmacenSeleccionado.getId_Ubicacion();
     
    Producto productoSeleccionado = (Producto) InventarioSolido.jComboProductoSolido.getSelectedItem();
    
    int idProducdo = productoSeleccionado.getIdProducto();

String sql = "Update inventario set idProducto=?, stockMinimo =?,stockMaximo=?,id_Ubicacion=? where id_inventario =?";

    try {
        this.conectar();
        ps = this.con.prepareStatement(sql);
        
        ps.setInt(1, idProducdo);
        ps.setInt(2, iv.getStockMinimo());
        ps.setInt(3, iv.getStockMaximo());
        ps.setInt(4, idUbicacion);
        ps.setInt(5,idInventario);
         
        if (ps.executeUpdate() >0) {
           JOptionPane.showMessageDialog(null, "Actualizacion de datos correcta");
           MostrarVerInventarioSOLIDO();
            
         
                   InventarioSolido.btbConfirmarActualizacion.setVisible(false);
    InventarioSolido.btbCancelarActualizacion.setVisible(false);
   
    InventarioSolido.btbModificar.setVisible(true);
    InventarioSolido.btbHistorialDeMovimientoSolido.setVisible(true);
    InventarioSolido.btbInformacionDeLote.setVisible(true);
    InventarioSolido.btbRegistrar.setVisible(true); 
    
    
    
    InventarioSolido.txtStockMinimo.setText("");
    InventarioSolido.txtStoxkMaximo.setText("");
    
        }
                
        
     
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error " + e.getMessage());
    }    
    

    
    
}


public void ActualizarProductoSolidoSeguridad() throws ClassNotFoundException, SQLException {
    int fila = InventarioSolido.TablaSolido.getSelectedRow();

    if (fila == -1) {
        JOptionPane.showMessageDialog(null, "Debe seleccionar un producto para Modificarlo");
        return;
    }

    int idInventario = Integer.parseInt(InventarioSolido.TablaSolido.getValueAt(fila, 0).toString()
    );

    int confirmar = JOptionPane.showConfirmDialog(
        null,
        "¿Estás seguro de modificar este producto?",
        "Confirmar modificación",
        JOptionPane.YES_NO_OPTION
    );

    if (confirmar == JOptionPane.YES_OPTION) {
        ActualizarDatosSOLIDOInventario(idInventario);

// refrescar tabla
    }
}



 private ArrayList<Inventario> MostrarInventarioSolidoCom() throws ClassNotFoundException, SQLException {
    ArrayList<Inventario> ListaInventarioo = new ArrayList<>();

    try {
        String Sql = "SELECT " +
                     "i.id_inventario, " +
                     "p.idProducto,"+
                     "p.nombre AS nombreProducto, " +
                     "i.Cantidad_Disponible, " +
                      "i.precio_venta,"+
                     "i.stockMinimo, " +
                     "i.stockMaximo, " +
                     "CONCAT('Ala-', a.ala, ' -Pasillo ', a.pasillo, ' -Estante ', a.estante, ' -Nivel ', a.nivel) AS nombreUbicacion " +
                     "FROM inventario i " +
                     "INNER JOIN producto p ON p.idProducto = i.idProducto " +
                     "LEFT JOIN almacen a ON a.id_Ubicacion = i.id_Ubicacion " +
                     "WHERE i.tipo_producto = 'SOLIDO'";

        this.conectar();
        ps = this.con.prepareStatement(Sql);
        rs = ps.executeQuery();

        while (rs.next()) {
            Inventario inve = new Inventario();
    inve.setIdinventario(rs.getInt("id_inventario"));
    inve.setIdProducto(rs.getInt("idProducto"));   // 👈 Agregar esto
    inve.setProductos(rs.getString("nombreProducto"));
    inve.setCantidad_Disponible(rs.getString("Cantidad_Disponible"));
    inve.setPrecio_Venta(rs.getDouble("precio_venta"));
    inve.setStockMinimo(rs.getInt("stockMinimo"));
    inve.setStockMaximo(rs.getInt("stockMaximo"));
    inve.setUbicacion(rs.getString("nombreUbicacion"));

    ListaInventarioo.add(inve);
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al obtener la lista del inventario: " + e.getMessage());
    } finally {
        this.cerrarCn();
    }

    return ListaInventarioo;
}

 
private void cargarInformacionCompletaProductoSolido(int idInventario) throws SQLException, ClassNotFoundException {
    
    String sql = """
        SELECT 
            p.nombre,
            p.condicion,
            p.compatibilidad,
            p.unidad_De_Medidad,
            m.nombre AS marca,
            i.Fecha_Vencimiento,
            i.lote,
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
                    info.append("===   INFORMACIÓN COMPLETA   ===\n\n");
                    info.append("Producto : ").append(rs.getString("nombre")).append("\n");
                    info.append("Marca : ").append(rs.getString("marca")).append("\n");
                    info.append("Condición : ").append(rs.getString("condicion") != null ? rs.getString("condicion") : "N/A").append("\n");
                    info.append("Compatibilidad: ").append(rs.getString("compatibilidad") != null ? rs.getString("compatibilidad") : "N/A").append("\n");
                    info.append("Unidad Medida : ").append(rs.getString("unidad_De_Medidad") != null ? rs.getString("unidad_De_Medidad") : "N/A").append("\n\n");
                    
                    info.append("=== INVENTARIO ===\n");
                    info.append("Lote : ").append(rs.getString("lote") != null ? rs.getString("lote") : "N/A").append("\n\n");
                    //info.append("Fecha Vencimiento  : ").append(rs.getString("Fecha_Vencimiento") != null ? rs.getString("Fecha_Vencimiento") : "N/A").append("\n");
                  //  info.append("Cantidad Disponible: ").append(rs.getInt("Cantidad_Disponible")).append("\n\n");
                    
                    info.append("=== UBICACIÓN ===\n");
                    if (rs.getString("pasillo") != null) {
                        info.append("Pasillo : ").append(rs.getString("pasillo")).append("\n");
                        info.append("Ala : ").append(rs.getString("ala")).append("\n");
                        info.append("Estante : ").append(rs.getInt("estante")).append("\n");
                        info.append("Nivel : ").append(rs.getInt("nivel")).append("\n");
                    } else {
                        info.append("Sin ubicación asignada\n");
                    }

                    Vista_Salida_Solido.txtInformacionProducto.setText(info.toString());
                    
                } else {
                    Vista_Salida_Solido.txtInformacionProducto.setText("No se encontró información del producto.");
                }
            }
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, 
            "Error al cargar información del producto sólido:\n" + e.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
}


public void CargarComboxBoxSOLIDO() throws ClassNotFoundException, SQLException {
    ArrayList<Inventario> listaProducto = this.MostrarInventarioSolidoCom();
    
    // Limpiamos el ComboBox
    Vista_Salida_Solido.jComboBoxProductoSolido.removeAllItems();
    
    // Agregamos los productos
    for (Inventario inve : listaProducto) {
        Vista_Salida_Solido.jComboBoxProductoSolido.addItem(inve);  // ← Corregido
    }
    
    // Removemos listeners anteriores para evitar duplicados
    for (ActionListener al : Vista_Salida_Solido.jComboBoxProductoSolido.getActionListeners()) {
        Vista_Salida_Solido.jComboBoxProductoSolido.removeActionListener(al);
    }
    
    // ActionListener mejorado
    Vista_Salida_Solido.jComboBoxProductoSolido.addActionListener(e -> {
        Inventario seleccionado = (Inventario) Vista_Salida_Solido.jComboBoxProductoSolido.getSelectedItem();
        
        if (seleccionado != null) {
            try {
                // Actualizar precio
                Vista_Salida_Solido.txtPrecioVentaUnitario.setText(
                    String.valueOf(seleccionado.getPrecio_Venta())
                );
                
                // Cargar información completa
                cargarInformacionCompletaProductoSolido(seleccionado.getIdinventario());
                
            } catch (SQLException | ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(null,
                    "Error al cargar información del producto:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    });
}
 
    
private void cargarInformacionCompletaProductoSolidoAjuste(int idInventario) throws SQLException, ClassNotFoundException {
    
    String sql = """
        SELECT 
            p.nombre,
            p.condicion,
            p.compatibilidad,
            p.unidad_De_Medidad,
            m.nombre AS marca,
            i.Fecha_Vencimiento,
            i.lote,
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
                    info.append("===   INFORMACIÓN COMPLETA   ===\n\n");
                    info.append("Producto : ").append(rs.getString("nombre")).append("\n");
                    info.append("Marca : ").append(rs.getString("marca")).append("\n");
                    info.append("Condición : ").append(rs.getString("condicion") != null ? rs.getString("condicion") : "N/A").append("\n");
                    info.append("Compatibilidad: ").append(rs.getString("compatibilidad") != null ? rs.getString("compatibilidad") : "N/A").append("\n");
                    info.append("Unidad Medida : ").append(rs.getString("unidad_De_Medidad") != null ? rs.getString("unidad_De_Medidad") : "N/A").append("\n\n");
                    
                    info.append("=== INVENTARIO ===\n");
                    info.append("Lote : ").append(rs.getString("lote") != null ? rs.getString("lote") : "N/A").append("\n\n");
                    //info.append("Fecha Vencimiento  : ").append(rs.getString("Fecha_Vencimiento") != null ? rs.getString("Fecha_Vencimiento") : "N/A").append("\n");
                  //  info.append("Cantidad Disponible: ").append(rs.getInt("Cantidad_Disponible")).append("\n\n");
                    
                    info.append("=== UBICACIÓN ===\n");
                    if (rs.getString("pasillo") != null) {
                        info.append("Pasillo : ").append(rs.getString("pasillo")).append("\n");
                        info.append("Ala : ").append(rs.getString("ala")).append("\n");
                        info.append("Estante : ").append(rs.getInt("estante")).append("\n");
                        info.append("Nivel : ").append(rs.getInt("nivel")).append("\n");
                    } else {
                        info.append("Sin ubicación asignada\n");
                    }

                   Ajuste_Solido.TxtAreaInformacionProducto.setText(info.toString());
                    
                } else {
                     Ajuste_Solido.TxtAreaInformacionProducto.setText("No se encontró información del producto.");
                }
            }
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, 
            "Error al cargar información del producto sólido:\n" + e.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
}


public void CargarComboxBoxSOLIDOAjuste() throws ClassNotFoundException, SQLException {
    ArrayList<Inventario> listaProducto = this.MostrarInventarioSolidoCom();
    
    // Limpiamos el ComboBox
  Ajuste_Solido.jComboBoxProducto.removeAllItems();
    
    // Agregamos los productos
    for (Inventario inve : listaProducto) {
        Ajuste_Solido.jComboBoxProducto.addItem(inve);  // ← Corregido
    }
    
    // Removemos listeners anteriores para evitar duplicados
    for (ActionListener al : Ajuste_Solido.jComboBoxProducto.getActionListeners()) {
      Ajuste_Solido.jComboBoxProducto.removeActionListener(al);
    }
    
    // ActionListener mejorado
   Ajuste_Solido.jComboBoxProducto.addActionListener(e -> {
        Inventario seleccionado = (Inventario)Ajuste_Solido.jComboBoxProducto.getSelectedItem();
        
        if (seleccionado != null) {
            try {
             /*   // Actualizar precio
                Vista_Salida_Solido.txtPrecioVentaUnitario.setText(
                    String.valueOf(seleccionado.getPrecio_Venta())
                );
                */
                // Cargar información completa
                cargarInformacionCompletaProductoSolidoAjuste(seleccionado.getIdinventario());
                
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
