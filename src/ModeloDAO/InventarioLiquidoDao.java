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
import Vista_Almacen.Vista_Salida_Liquido;
import Vista_GestionInventario.InventarioLiquido;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
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
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import Vista_Almacen.Ajuste_Liquido;
/**
 *
 * @author avila
 */
public class InventarioLiquidoDao  extends  ConexiónBD{
    
       
    private ResultSet rs;
    
    private PreparedStatement ps;
    
    Modelo.Inventario iv = new Inventario();
    
     DefaultTableModel modeloInventarioLiquido = new DefaultTableModel();
        DefaultTableModel modeloLiquidoLote = new DefaultTableModel();
     
     private void limpiarTablaInventario (){
 int fila = modeloInventarioLiquido.getRowCount();
    if (fila > 0) {
        for (int i = 0; i < fila; i++) {
            modeloInventarioLiquido.removeRow(0);
        }
   
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
     
     

     public void TituloInventarioLiquido() {
    
    
    String Titulo[] = {
        "ID", 
        "Nombre", 
        "Cantidad Disponible", 
        "Stock Minimo",
        "Stock Máximo", 
        "Ubicación"
        
    };

  modeloInventarioLiquido.setColumnIdentifiers(Titulo);

    if (InventarioLiquido.TablaLiquido != null) {
       InventarioLiquido.TablaLiquido.setModel(modeloInventarioLiquido);

        // 👇 aplicar centrado de datos después de setear el modelo
        centrarTextoTabla(InventarioLiquido.TablaLiquido);
    }
}
     

public void TituloInventarioLiquidoLote() {
    String Titulo[] = {
        "ID", 
        "Nombre", 
        "Cantidad",
        "Lote",
        "Fecha ingreso", 
        "Fecha V",
        "Costo Unitario", 
        "Costo total",
        "Precio venta",
         " % Ganancia ",
         " Proveedor",
         "# Factura",
         " IVA"
            
        
    };

    modeloLiquidoLote.setColumnIdentifiers(Titulo);

    if (InventarioLiquido.TablaLote != null) {
        InventarioLiquido.TablaLote.setModel(modeloLiquidoLote);

        // 👇 aplicar centrado de datos después de setear el modelo
        centrarTextoTabla(InventarioLiquido.TablaLote);
    }
}

     

    public void RegistrarStockProductoLiquido() throws ClassNotFoundException, SQLException {
    int idInventarioGenerado = 0;
    try {
        this.conectar();

        // === Asignación de valores ===
        iv.setCantidad((int) Double.parseDouble(InventarioLiquido.txtCantidadActual.getText()));
        iv.setStockMaximo((int) Double.parseDouble(InventarioLiquido.txtStockMaximo.getText()));
        iv.setStockMinimo((int) Double.parseDouble(InventarioLiquido.txtStockMinimo.getText()));
        iv.setPrecio_Unitario(Double.parseDouble(InventarioLiquido.txtCostoUnitario.getText()));
        iv.setPrecio_Venta(Double.parseDouble(InventarioLiquido.txtPrecioVenta.getText()));
        iv.setTipo_Producto(InventarioLiquido.txtTipoProductoLiquido.getText());
        iv.setPorcentaje(Integer.parseInt(InventarioLiquido.txtPorcentaje.getText()));
        iv.setFactura(InventarioLiquido.txtNumeroFACTURA.getText());
       
int ivaFinal = 0;

if (InventarioLiquido.jCheckBoxiIVA.isSelected() && 
    InventarioLiquido.txtPorcentajeIVA.isVisible()) {
    
    String textoIVA = InventarioLiquido.txtPorcentajeIVA.getText().trim();
    
    if (!textoIVA.isEmpty()) {
        try {
            ivaFinal = (int) Double.parseDouble(textoIVA);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "El valor de IVA no es válido", 
                                        "Error", JOptionPane.WARNING_MESSAGE);
        }
    }
}

iv.setIva(ivaFinal);
        // Ubicación
        Almacen AlmacenSeleccionado = (Almacen) InventarioLiquido.jComboUbicacion.getSelectedItem();
        int idUbicacion = AlmacenSeleccionado.getId_Ubicacion();

        // Producto
        Producto productoSeleccionado = (Producto) InventarioLiquido.jComboProductoLiquido.getSelectedItem();
        int idProducto = productoSeleccionado.getIdProducto();

        // Proveedor
        Proveedor proveedorSeleccionado = (Proveedor) InventarioLiquido.jComboProveedor.getSelectedItem();
        int idProveedor = proveedorSeleccionado.getIdProveedor();

        // Fecha de vencimiento
        SimpleDateFormat fecha = new SimpleDateFormat("yyyy-MM-dd");
        String fechaVencimiento = fecha.format(InventarioLiquido.jDateFechaVencimiento.getDate());

        // === Cálculos automáticos ===
        double costoTotal = iv.getCantidad() * iv.getPrecio_Unitario();
        int cantidadDisponible = iv.getCantidad();   // Al registrar, disponible = cantidad inicial

        // === Generación automática del Lote ===
        String loteGenerado = generarLoteAutomatico();

        // === SQL Actualizado ===
        String sql = """
            INSERT INTO inventario 
            (idProducto, stockMinimo, Cantidad, precio_unitario, stockMaximo, lote, 
             Fecha_Vencimiento, id_Ubicacion, costo_total, Cantidad_Disponible, 
             tipo_producto, Usuario, precio_venta, porcentaje, idProveedor, numero_Factura, iva)
            VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)
            """;

        ps = this.con.prepareStatement(sql,ps.RETURN_GENERATED_KEYS);

        ps.setInt(1, idProducto);
        ps.setInt(2, iv.getStockMinimo());
        ps.setInt(3, iv.getCantidad());
        ps.setDouble(4, iv.getPrecio_Unitario());
        ps.setInt(5, iv.getStockMaximo());
        ps.setString(6, loteGenerado);           // Lote generado
        ps.setString(7, fechaVencimiento);
        ps.setInt(8, idUbicacion);
        ps.setDouble(9, costoTotal);             // Costo total calculado
        ps.setInt(10, cantidadDisponible);
        ps.setString(11, iv.getTipo_Producto());
        ps.setString(12, Usuario.usuarioActual);
        ps.setDouble(13, iv.getPrecio_Venta());
        ps.setInt(14, iv.getPorcentaje());
        ps.setInt(15, idProveedor);
        ps.setString(16, iv.getFactura());
        ps.setInt(17, iv.getIva());
if (ps.executeUpdate() > 0) {
            
            // Obtener el ID generado del inventario
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                idInventarioGenerado = rs.getInt(1);
            }

            // ====================== REGISTRAR MOVIMIENTO ======================
           Mvt_Liquido_Dao mvtLiquido = new Mvt_Liquido_Dao();
            
            mvtLiquido.RealizarEntradaLiquido(
                idInventarioGenerado,
                idProducto,
                iv.getTipo_Producto(),
                iv.getCantidad(),
                loteGenerado,
                iv.getPrecio_Unitario()
            );

            JOptionPane.showMessageDialog(null, 
                "✅ Producto registrado exitosamente\nLote: " + loteGenerado, 
                "Éxito", JOptionPane.INFORMATION_MESSAGE);

            limpiarCamposLiquido();
            MostrarVerInventarioLiquido();
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error al registrar: " + e.getMessage());
        e.printStackTrace();
    }
}
    
    private String generarLoteAutomatico() throws SQLException {
    String lote = "";
    String sql = """
        SELECT IFNULL(MAX(CAST(SUBSTRING_INDEX(lote, 'prov', -1) AS UNSIGNED)), 0) + 1 
        FROM inventario 
        WHERE lote LIKE ?
        """;
    
    try (PreparedStatement pst = con.prepareStatement(sql)) {
        String fechaHoy = new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
        pst.setString(1, "%" + fechaHoy + "%prov%");
        
        ResultSet rs = pst.executeQuery();
        int numero = 1;
        if (rs.next()) {
            numero = rs.getInt(1);
        }
        
        lote = "Lot " + fechaHoy + " prov" + String.format("%03d", numero);
    }
    
    return lote;
}
    
    private void limpiarCamposLiquido() {
    InventarioLiquido.txtCantidadActual.setText("");
    InventarioLiquido.txtStockMaximo.setText("");
    InventarioLiquido.txtStockMinimo.setText("");
    InventarioLiquido.txtCostoUnitario.setText("");
    InventarioLiquido.txtPrecioVenta.setText("");
   
    InventarioLiquido.txtNumeroFACTURA.setText("");
    InventarioLiquido.txtPorcentajeIVA.setText("");
    InventarioLiquido.jDateFechaVencimiento.setDate(null);
    InventarioLiquido.jCheckBoxiIVA.setSelected(false);
    InventarioLiquido.txtPorcentajeIVA.setVisible(false);
}
    
    
    private void limpiarTabla(DefaultTableModel modelo) {
    modelo.setRowCount(0);  // elimina todas las filas
}
    
    
public void MostrarVerInventarioLiquido() throws ClassNotFoundException, SQLException {
    
    TituloInventarioLiquido();
    
    // 1. Obtener los datos frescos de la BD
    ArrayList<Inventario> ListaTerminada = MostrarInventarioLiquido();

    // 2. Obtener el modelo actual
    modeloInventarioLiquido = (DefaultTableModel) InventarioLiquido.TablaLiquido.getModel();

    // === DESACTIVAR SORTER TEMPORALMENTE ===
    TableRowSorter<?> sorterOriginal = null;
    if (InventarioLiquido.TablaLiquido.getRowSorter() != null) {
        sorterOriginal = (TableRowSorter<?>) InventarioLiquido.TablaLiquido.getRowSorter();
        InventarioLiquido.TablaLiquido.setRowSorter(null);
    }
    

    // 3. Limpiar y llenar la tabla
    limpiarTabla(modeloInventarioLiquido);

    Object[] fila = new Object[6];
    for (Inventario inv : ListaTerminada) {
        fila[0] = inv.getIdinventario();
        fila[1] = inv.getProductos();
        fila[2] = inv.getCantidad_Disponible();
        fila[3] = inv.getStockMinimo();
        fila[4] = inv.getStockMaximo();
        fila[5] = inv.getUbicacion();
        
        modeloInventarioLiquido.addRow(fila);
    }

    // 4. Restaurar el sorter
    if (sorterOriginal != null) {
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modeloInventarioLiquido);
        InventarioLiquido.TablaLiquido.setRowSorter(sorter);
    }

    // 5. Asignar el modelo a la tabla
    InventarioLiquido.TablaLiquido.setModel(modeloInventarioLiquido);

    // 6. VOLVER A APLICAR LOS RENDERERS Y FORMATO DE LA TABLA
       // ← Este será el método clave

    // 7. Centrar texto
    centrarTextoTabla(InventarioLiquido.TablaLiquido);
}
   
// Agrega este método nuevo

  public void MostrarVerInventarioLiquidoLote() throws ClassNotFoundException, SQLException {
    // Cargar títulos y limpiar tabla
    
   
    TituloInventarioLiquidoLote();  // Asegúrate que los títulos incluyan Cantidad
  
      
    // Obtener lista de inventario desde la BD (solo líquidos)
    ArrayList<Inventario> ListaTerminada = MostrarInventarioLiquidoLote(); // Método que trae todos los campos necesarios

    // Modelo de la tabla
    modeloLiquidoLote = (DefaultTableModel) InventarioLiquido.TablaLote.getModel();
    
     TableRowSorter<?> sorterOriginal = null;
    if (InventarioLiquido.TablaLote.getRowSorter() != null) {
        sorterOriginal = (TableRowSorter<?>) InventarioLiquido.TablaLote.getRowSorter();
        InventarioLiquido.TablaLote.setRowSorter(null);
    }
limpiarTabla(modeloLiquidoLote);
    // Arreglo para llenar cada fila (8 columnas)
    Object[] obj = new Object[13];

    for (int i = 0; i < ListaTerminada.size(); i++) {
        Inventario inv = ListaTerminada.get(i);

        obj[0] = inv.getIdinventario();           // ID
        obj[1] = inv.getProductos();              // Nombre
        obj[2] = inv.getCantidad_Disponible();    // Cantidad
        obj[3] = inv.getLote();                   // Lote
        obj[4] = inv.getFecha_Ingreso();          // Fecha ingreso
        obj[5] = inv.getFecha_Vencimiento();      // Fecha Vencimiento
        obj[6] = inv.getPrecio_Unitario();        // Precio Unitario
        obj[7] = inv.getCosto_Total();            // Costo total
        obj [8] = inv.getPrecio_Venta();
        obj [9] = inv.getPorcentaje();
        obj [10] = inv.getIdProveedor();
        obj [11] = inv.getFactura();
        obj [12] = inv.getIva();
        // Agregar la fila al modelo
        modeloLiquidoLote.addRow(obj);
    }
 
    // Asignar el modelo actualizado a la tabla
  InventarioLiquido.TablaLote.setModel(modeloLiquidoLote);
  
TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modeloLiquidoLote);
    InventarioLiquido.TablaLote.setRowSorter(sorter);

    // Centrar texto en la tabla
    centrarTextoTabla(InventarioLiquido.TablaLote);
}
  
private ArrayList<Inventario> MostrarInventarioLiquido() throws ClassNotFoundException, SQLException {
    ArrayList<Inventario> ListaInventario = new ArrayList<>();

    String sql = "SELECT " +
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
                 "WHERE i.tipo_producto = 'LIQUIDO' " +
                 "ORDER BY i.id_inventario";   // opcional: ordena por nombre de producto

    try {
        this.conectar();
        ps = this.con.prepareStatement(sql);
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

            ListaInventario.add(inve);
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null,
            "Error al obtener la lista del inventario:\n" + e.getMessage(),
            "Error de Base de Datos",
            JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    } finally {
        this.cerrarCn();
    }
    return ListaInventario;
}
  
public void agregarFiltroBusquedaLiquido() {
    // Verifica que el campo de búsqueda y la tabla existan
    if (InventarioLiquido.txtBuscarProductoLiquidoTabla != null && InventarioLiquido.TablaLiquido != null) {

        // Agregamos un listener persistente
       InventarioLiquido.txtBuscarProductoLiquidoTabla.getDocument().addDocumentListener(new DocumentListener() {

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
                String texto = InventarioLiquido.txtBuscarProductoLiquidoTabla.getText().trim();

                // Siempre obtener el modelo actual
                DefaultTableModel modeloActual = (DefaultTableModel) InventarioLiquido.TablaLiquido.getModel();

                // Verificar o crear sorter
                TableRowSorter<?> sorter = (TableRowSorter<?>) InventarioLiquido.TablaLiquido.getRowSorter();
                if (sorter == null || sorter.getModel() != modeloActual) {
                    sorter = new TableRowSorter<>(modeloActual);
                    InventarioLiquido.TablaLiquido.setRowSorter(sorter);
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
    


public void agregarFiltroBusquedalOTE() {
    // Verifica que el campo de búsqueda y la tabla existan
    if (InventarioLiquido.txtBuscarLote != null && InventarioLiquido.TablaLote != null) {

        // Agregamos un listener persistente
       InventarioLiquido.txtBuscarLote.getDocument().addDocumentListener(new DocumentListener() {

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
                String texto = InventarioLiquido.txtBuscarLote.getText().trim();

                // Siempre obtener el modelo actual
                DefaultTableModel modeloActual = (DefaultTableModel) InventarioLiquido.TablaLote.getModel();

                // Verificar o crear sorter
                TableRowSorter<?> sorter = (TableRowSorter<?>) InventarioLiquido.TablaLote.getRowSorter();
                if (sorter == null || sorter.getModel() != modeloActual) {
                    sorter = new TableRowSorter<>(modeloActual);
                    InventarioLiquido.TablaLote.setRowSorter(sorter);
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




public  void ActualizarLoteLiquidoInventario (int IdInventario) throws  ClassNotFoundException,SQLException{
    
  // iv.setIdinventario(Integer.parseInt((Gestion_Inventarioo.txtIDLiquido.getText())));
    iv.setCantidad((int) Double.parseDouble(InventarioLiquido.txtCantidadActual.getText()));
    iv.setPrecio_Unitario(Double.parseDouble((InventarioLiquido.txtCostoUnitario.getText())));
   
    //iv.setPorcentaje(Integer.parseInt(Gestion_Inventarioo.txtPorcentaje_Liquido.getText()));
    
    
   SimpleDateFormat fecha = new SimpleDateFormat("yyyy-MM-dd");
      String fechaVencimiento = fecha.format(InventarioLiquido.jDateFechaVencimiento.getDate());
       
    iv.setPrecio_Venta(Double.parseDouble((InventarioLiquido.txtPrecioVenta.getText())));
    
      Proveedor proveedorSeleccionado = (Proveedor)  InventarioLiquido.jComboProveedor.getSelectedItem();
        int idProveedor = proveedorSeleccionado.getIdProveedor();
        
       iv.setFactura(InventarioLiquido.txtNumeroFACTURA.getText());
       
     int ivaFinal = 0;

if (InventarioLiquido.jCheckBoxiIVA.isSelected() && 
    InventarioLiquido.txtPorcentajeIVA.isVisible()) {
    
    String textoIVA = InventarioLiquido.txtPorcentajeIVA.getText().trim();
    
    if (!textoIVA.isEmpty()) {
        try {
            ivaFinal = (int) Double.parseDouble(textoIVA);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "El valor de IVA no es válido", 
                                        "Error", JOptionPane.WARNING_MESSAGE);
        }
    }
}
       iv.setIva(ivaFinal);
         double costoTotal = iv.getCantidad() * iv.getPrecio_Unitario();
       
    
    String sql = "UPDATE inventario SET Cantidad=?,Fecha_Vencimiento =?,precio_Unitario=?,precio_venta=?,idProveedor=?,numero_Factura=?,iva=?,costo_total=? where id_inventario=? ";
    
    try {
        this.conectar();
        ps = this.con.prepareStatement(sql);
        
        ps.setInt(1, iv.getCantidad());
        ps.setString(2, fechaVencimiento);
        ps.setDouble(3, iv.getPrecio_Unitario());
        ps.setDouble(4, iv.getPrecio_Venta());
        ps.setInt(5, idProveedor);
        ps.setString(6, iv.getFactura());
        ps.setInt(7, iv.getIva());
        ps.setDouble(8, costoTotal);
        ps.setInt(9, IdInventario);
       
        if (ps.executeUpdate() > 0) {
           JOptionPane.showMessageDialog(null, "Modificacion correcta de lote");
          
         MostrarVerInventarioLiquidoLote();
            
          
            
        }
        
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error " + e.getMessage());
    }
    
    
    
    
}


public  void ActualizarLoteConseguridad (){
    
   int filla = InventarioLiquido.TablaLote.getSelectedRow();
   
  if (filla == -1) {
    JOptionPane.showMessageDialog(null, 
        "Actualizacion Correcta.", 
        "Error", 
        JOptionPane.ERROR_MESSAGE);
    return;
       
}
  int idInventario = Integer.parseInt(InventarioLiquido.TablaLote.getValueAt(filla, 0).toString());

  int confirmar = JOptionPane.showConfirmDialog(
        null,
        "¿Estás seguro de actualizar este Lote?",
        "Confirmar actualización",
        JOptionPane.YES_NO_OPTION
    );
   if (confirmar == JOptionPane.YES_OPTION) {
   
       try {
           ActualizarLoteLiquidoInventario(idInventario);
       } catch (ClassNotFoundException ex) {
           System.getLogger(InventarioLiquidoDao.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
       } catch (SQLException ex) {
           System.getLogger(InventarioLiquidoDao.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
       }
   
   
       try {
           MostrarVerInventarioLiquidoLote();
       } catch (ClassNotFoundException ex) {
           System.getLogger(InventarioLiquidoDao.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
       } catch (SQLException ex) {
           System.getLogger(InventarioLiquidoDao.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
       }
       
       InventarioLiquido.btbOcultarLote.setVisible(true);
      InventarioLiquido.btbModificarLote.setVisible(true);
    
    InventarioLiquido.btbConfirmarModificacionLote.setVisible(false);
      InventarioLiquido.btbCancelarModificacionLote.setVisible(false);
    
     InventarioLiquido.txtCantidadActual.setText("");
     InventarioLiquido.txtCostoUnitario.setText("");
     InventarioLiquido.txtPrecioVenta.setText("");
     InventarioLiquido.jDateFechaVencimiento.setDate(null);
       
       
   
   
   }
  
    
    
}

public  void ActualizarDatosLiquidoInventario (int idInventario) throws ClassNotFoundException,SQLException{
    
    iv.setStockMaximo((int) Double.parseDouble(InventarioLiquido.txtStockMaximo.getText()));
     iv.setStockMinimo((int) Double.parseDouble(InventarioLiquido.txtStockMinimo.getText()));
     //iv.setIdinventario(Integer.parseInt(Gestion_Inventarioo.txtIDLiquido.getText()));
     
     
     Almacen AlmacenSeleccionado = (Almacen) InventarioLiquido.jComboUbicacion.getSelectedItem();
     
     int idUbicacion = AlmacenSeleccionado.getId_Ubicacion();
     
    Producto productoSeleccionado = (Producto) InventarioLiquido.jComboProductoLiquido.getSelectedItem();
    
    int idProducdo = productoSeleccionado.getIdProducto();
    
    

String sql = "Update inventario set idProducto=?, stockMinimo =?,stockMaximo=?,id_Ubicacion=? where id_inventario =?";

    try {
        this.conectar();
        ps = this.con.prepareStatement(sql);
        
        ps.setInt(1, idProducdo);
        ps.setInt(2, iv.getStockMinimo());
        ps.setInt(3, iv.getStockMaximo());
        ps.setInt(4, idUbicacion);
        ps.setInt(5, idInventario);
         
        if (ps.executeUpdate() >0) {
           JOptionPane.showMessageDialog(null, "Actualizacion de datos correcta");
            MostrarVerInventarioLiquido();
            
         
            
        }

        
        
     
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error " + e.getMessage());
    }

    
    
}


public void ActualizarConseguridadLiquido (){
    
    int filla = InventarioLiquido.TablaLiquido.getSelectedRow();
    
    
if (filla == -1) {
    JOptionPane.showMessageDialog(null, 
        "Actualizacion Correcta.", 
        "Error", 
        JOptionPane.ERROR_MESSAGE);
    return;
       
}

int idInventario = Integer.parseInt(InventarioLiquido.TablaLiquido.getValueAt(filla, 0).toString());

  int confirmar = JOptionPane.showConfirmDialog(
        null,
        "¿Estás seguro de actualizar este producto?",
        "Confirmar actualización",
        JOptionPane.YES_NO_OPTION
    );
  
  if (confirmar == JOptionPane.YES_OPTION) {
        try {
            ActualizarDatosLiquidoInventario(idInventario);
        } catch (ClassNotFoundException ex) {
            System.getLogger(InventarioLiquidoDao.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (SQLException ex) {
            System.getLogger(InventarioLiquidoDao.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        
        try {
            MostrarInventarioLiquido();
        } catch (ClassNotFoundException ex) {
            System.getLogger(InventarioLiquidoDao.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (SQLException ex) {
            System.getLogger(InventarioLiquidoDao.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        
        
        InventarioLiquido.txtStockMaximo.setText("");
        InventarioLiquido.txtStockMinimo.setText("");
            InventarioLiquido.btbConfirmarActualizacion.setVisible(false);
   InventarioLiquido.btbCancelarActualizacion.setVisible(false);
   
   
   InventarioLiquido.btbModificar.setVisible(true);
   InventarioLiquido.btbHistorialMovimiento.setVisible(true);
   InventarioLiquido.btbInformacionLote.setVisible(true);
   InventarioLiquido.btbRegistrar.setVisible(true);
   InventarioLiquido.txtStockMaximo.setText("");
   InventarioLiquido.txtStockMinimo.setText("");
  }
  
  }
    
    



    private ArrayList<Inventario> MostrarInventarioLiquidoLote() throws ClassNotFoundException, SQLException {
    ArrayList<Inventario> ListaInventario = new ArrayList<>();

    try {
      String Sql = "SELECT " +
             "i.id_inventario, " +
             "p.nombre AS nombreProducto, " +
             "pr.nombre AS nombreProveedor, " +
             "i.Cantidad, " +
             "i.lote, " +
             "i.fecha_ingreso, " +
             "i.fecha_vencimiento, " +
             "i.precio_Unitario, " +
             "i.costo_Total, " +
             "i.precio_venta, " +
             "i.porcentaje, " +
              "i.numero_Factura, " +
              "i.iva " +
               
             "FROM inventario i " +
             "INNER JOIN producto p ON p.idProducto = i.idProducto " +
             "INNER JOIN proveedor pr ON pr.idProveedor = i.idProveedor " +
             "WHERE i.tipo_producto = 'LIQUIDO'" +
              "ORDER BY i.id_inventario";
      
        this.conectar();
        ps = this.con.prepareStatement(Sql);
        rs = ps.executeQuery();

        while (rs.next()) {
            Inventario inve = new Inventario();

            inve.setIdinventario(rs.getInt("id_inventario"));             // ID
            inve.setProductos(rs.getString("nombreProducto"));            // Nombre
            inve.setCantidad_Disponible(rs.getString("Cantidad")); // Cantidad
            inve.setLote(rs.getString("lote"));                           // Lote
            inve.setFecha_Ingreso(rs.getString("fecha_ingreso"));         // Fecha ingreso
            inve.setFecha_Vencimiento(rs.getString("fecha_vencimiento")); // Fecha Vencimiento
            inve.setPrecio_Unitario(rs.getDouble("precio_Unitario"));        // Precio unitario
         inve.setCosto_Total(rs.getBigDecimal("costo_Total"));
                                                                // Costo total
            inve.setPrecio_Venta(rs.getDouble("precio_venta"));
            inve.setPorcentaje(rs.getInt("porcentaje"));
            
            inve.setIdProveedor(rs.getString("nombreProveedor"));
            inve.setFactura(rs.getString("numero_Factura"));
            inve.setIva(rs.getInt("iva"));
            
            ListaInventario.add(inve);
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al obtener la lista del inventario: " + e.getMessage());
    } finally {
        this.cerrarCn();
    }

    return ListaInventario;
}
 
    

    
    
   private ArrayList<Inventario> MostrarInventarioLiquidoCom() throws ClassNotFoundException, SQLException {
    ArrayList<Inventario> ListaInventario = new ArrayList<>();

    try {
        String Sql = "SELECT " +
                     "i.id_inventario, " +
                     "p.idProducto," +
                     "p.nombre AS nombreProducto, " +
                     "i.Cantidad_Disponible, " +
                     "i.precio_venta," +
                     "i.stockMinimo, " +
                     "i.stockMaximo, " +
                     "CONCAT('Ala-', a.ala, ' -Pasillo ', a.pasillo, ' -Estante ', a.estante, ' -Nivel ', a.nivel) AS nombreUbicacion " +
                     "FROM inventario i " +
                     "INNER JOIN producto p ON p.idProducto = i.idProducto " +
                     "LEFT JOIN almacen a ON a.id_Ubicacion = i.id_Ubicacion " +
                     "WHERE i.tipo_producto = 'LIQUIDO'";

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

            ListaInventario.add(inve);
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al obtener la lista del inventario: " + e.getMessage());
    } finally {
        this.cerrarCn();
    }

    return ListaInventario;
} 
     
public void CargarComboxBoxLiquido() throws ClassNotFoundException, SQLException {
    ArrayList<Inventario> listaProducto = this.MostrarInventarioLiquidoCom();
   
    Vista_Salida_Liquido.jComboBoxProductoLiquido.removeAllItems();
   
    for (Inventario inve : listaProducto) {
        Vista_Salida_Liquido.jComboBoxProductoLiquido.addItem(inve);
    }
    
    // Removemos ActionListeners anteriores para evitar duplicados
    for (ActionListener al : Vista_Salida_Liquido.jComboBoxProductoLiquido.getActionListeners()) {
        Vista_Salida_Liquido.jComboBoxProductoLiquido.removeActionListener(al);
    }
    
    // Nuevo ActionListener
    Vista_Salida_Liquido.jComboBoxProductoLiquido.addActionListener(e -> {
        Inventario seleccionado = (Inventario) Vista_Salida_Liquido.jComboBoxProductoLiquido.getSelectedItem();
       
        if (seleccionado != null) {
            try {
                // Actualizamos el precio de venta desde el objeto del ComboBox
                Vista_Salida_Liquido.txtPrecioVenta.setText(
                    String.valueOf(seleccionado.getPrecio_Venta())
                );
                
                // Cargamos la información completa en el TextArea
                cargarInformacionCompletaProducto(seleccionado.getIdinventario());
                
            } catch (SQLException | ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(null,
                    "Error al cargar información del producto:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    });
}

/**
 * Carga toda la información completa del producto seleccionado en el TextArea
 * (Sin incluir precios)
 */
private void cargarInformacionCompletaProducto(int idInventario) throws SQLException, ClassNotFoundException {
   
    String sql = """
        SELECT
            p.nombre,
            p.tipo_Liquido,
            p.viscosidad,
            p.densidad,
            p.presentacion,
            m.nombre AS marca,
            i.Fecha_Vencimiento,
            i.lote,
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
                    info.append("=== INFORMACIÓN COMPLETA ===\n\n");
                    info.append("Producto: ").append(rs.getString("nombre")).append("\n");
                    info.append("Marca: ").append(rs.getString("marca")).append("\n");
                    info.append("Tipo de Líquido: ").append(rs.getString("tipo_Liquido")).append("\n");
                    info.append("Viscosidad: ").append(rs.getString("viscosidad")).append("\n");
                    info.append("Densidad: ").append(rs.getString("densidad")).append("\n");
                    info.append("Presentación: ").append(rs.getString("presentacion")).append("\n\n");
                    
                    info.append("=== INVENTARIO === \n");
                    info.append("Lote: ").append(rs.getString("lote") != null ? rs.getString("lote") : "N/A").append("\n");
                    info.append("Fecha de Vencimiento: ").append(rs.getString("Fecha_Vencimiento") != null ? rs.getString("Fecha_Vencimiento") : "N/A").append("\n\n");
                    
                    
                    info.append("=== UBICACIÓN ===\n");
                    if (rs.getString("pasillo") != null) {
                        info.append("Pasillo: ").append(rs.getString("pasillo")).append("\n");
                        info.append("Ala: ").append(rs.getString("ala")).append("\n");
                        info.append("Estante: ").append(rs.getInt("estante")).append("\n");
                        info.append("Nivel: ").append(rs.getInt("nivel")).append("\n");
                    } else {
                        info.append("Sin ubicación asignada\n");
                    }
                    
                    Vista_Salida_Liquido.TextAreaInformacionProducto.setText(info.toString());
                    
                } else {
                    Vista_Salida_Liquido.TextAreaInformacionProducto.setText("No se encontró información del producto.");
                }
            }
        }
        
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null,
            "Error al cargar información del producto:\n" + e.getMessage(),
            "Error de Base de Datos",
            JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    } 
    // finally { this.cerrarCn(); }  ← Descomenta si quieres cerrar la conexión aquí
}



    
public void CargarComboxBoxLiquidoAjuste() throws ClassNotFoundException, SQLException {
    ArrayList<Inventario> listaProducto = this.MostrarInventarioLiquidoCom();
   
    Ajuste_Liquido.jComboBoxProductoLiquidoAjuste.removeAllItems();
   
    for (Inventario inve : listaProducto) {
       Ajuste_Liquido.jComboBoxProductoLiquidoAjuste.addItem(inve);
    }
    
    // Removemos ActionListeners anteriores para evitar duplicados
    for (ActionListener al :  Ajuste_Liquido.jComboBoxProductoLiquidoAjuste.getActionListeners()) {
     Ajuste_Liquido.jComboBoxProductoLiquidoAjuste.removeActionListener(al);
    }
    
    // Nuevo ActionListener
    Ajuste_Liquido.jComboBoxProductoLiquidoAjuste.addActionListener(e -> {
        Inventario seleccionado = (Inventario)  Ajuste_Liquido.jComboBoxProductoLiquidoAjuste.getSelectedItem();
       
        if (seleccionado != null) {
            try {
              /*  // Actualizamos el precio de venta desde el objeto del ComboBox
                     
                Vista_Salida_Liquido.txtPrecioVenta.setText(
                    String.valueOf(seleccionado.getPrecio_Venta())
                );
                
                */
                // Cargamos la información completa en el TextArea
                cargarInformacionCompletaProductoAjute(seleccionado.getIdinventario());
                
            } catch (SQLException | ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(null,
                    "Error al cargar información del producto:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    });
}

/**
 * Carga toda la información completa del producto seleccionado en el TextArea
 * (Sin incluir precios)
 */
private void cargarInformacionCompletaProductoAjute(int idInventario) throws SQLException, ClassNotFoundException {
   
    String sql = """
        SELECT
            p.nombre,
            p.tipo_Liquido,
            p.viscosidad,
            p.densidad,
            p.presentacion,
            m.nombre AS marca,
            i.Fecha_Vencimiento,
            i.lote,
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
                    info.append("===        INFORMACIÓN COMPLETA      ===\n\n");
                    info.append("Producto: ").append(rs.getString("nombre")).append("\n");
                    info.append("Marca: ").append(rs.getString("marca")).append("\n");
                    info.append("Tipo de Líquido: ").append(rs.getString("tipo_Liquido")).append("\n");
                    info.append("Viscosidad: ").append(rs.getString("viscosidad")).append("\n");
                    info.append("Densidad: ").append(rs.getString("densidad")).append("\n");
                    info.append("Presentación: ").append(rs.getString("presentacion")).append("\n\n");
                    
                    info.append("=== INVENTARIO === \n");
                    info.append("Lote: ").append(rs.getString("lote") != null ? rs.getString("lote") : "N/A").append("\n");
                    info.append("Fecha de Vencimiento: ").append(rs.getString("Fecha_Vencimiento") != null ? rs.getString("Fecha_Vencimiento") : "N/A").append("\n\n");
                    
                    
                    info.append("=== UBICACIÓN ===\n");
                    if (rs.getString("pasillo") != null) {
                        info.append("Pasillo: ").append(rs.getString("pasillo")).append("\n");
                        info.append("Ala: ").append(rs.getString("ala")).append("\n");
                        info.append("Estante: ").append(rs.getInt("estante")).append("\n");
                        info.append("Nivel: ").append(rs.getInt("nivel")).append("\n");
                    } else {
                        info.append("Sin ubicación asignada\n");
                    }
                    
                    Ajuste_Liquido.jTextAreaInformacionLiquidoAjuste.setText(info.toString());
                    
                } else {
                   Ajuste_Liquido.jTextAreaInformacionLiquidoAjuste.setText("No se encontró información del producto.");
                }
            }
        }
        
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null,
            "Error al cargar información del producto:\n" + e.getMessage(),
            "Error de Base de Datos",
            JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    } 
    // finally { this.cerrarCn(); }  ← Descomenta si quieres cerrar la conexión aquí
}








}
