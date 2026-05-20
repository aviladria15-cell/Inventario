package ModeloDAO;


import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import Vista_GestionInventario.InventarioUnidad;
import java.sql.*;
import java.util.*;
import Vista_Almacen.Ajuste_Unidad;
import Modelo.*;

import Vista_Almacen.Ajuste_Liquido;
import Vista_Almacen.Ajuste_Solido;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;

import Modelo.Movimiento_inventario;
import java.util.regex.Pattern;
import  Vista_GestionInventario.InventarioLiquido;
import Vista_GestionInventario.InventarioSolido;
import Vista_Almacen.Vista_Salida_Liquido;
import Vista_Almacen.Vista_Salida_Solido;
import Vista_Almacen.Vista_Salida_Unidad;
public class MovientosDao extends ConexiónBD {

    PreparedStatement ps;
    ResultSet rs;
    DefaultTableModel modeloMovientoLiquido = new DefaultTableModel();
    
    
    DefaultTableModel modeloMovientoSolido = new DefaultTableModel();
    
      DefaultTableModel modeloMovientoUnida = new DefaultTableModel();
    
    InventarioUnidadDAO iv = new InventarioUnidadDAO();
  
    Movimiento_inventario mv = new Movimiento_inventario();

    
    
    private void limpiarTabla(DefaultTableModel modelo) {
    modelo.setRowCount(0);  // elimina todas las filas
}
    
    public void TituloInventarioMovimientoUNIDAD() {
    
    
    String Titulo[] = {
        "ID", 
        "Producto", 
        "Tipo Producto", 
        "Movimiento",
        "Cantidad", 
        "Stock Final",
        "Usuario",
        "Detalle",
        "Fecha Movimiento"
        
    };

    modeloMovientoUnida.setColumnIdentifiers(Titulo);

    if (InventarioUnidad.TablaHistorialMovimienrto != null) {
        InventarioUnidad.TablaHistorialMovimienrto.setModel(modeloMovientoUnida);

        // 👇 aplicar centrado de datos después de setear el modelo
        centrarTextoTabla(InventarioUnidad.TablaHistorialMovimienrto);
    }
} 
    
   public void agregarFiltroBusquedaUNIDADMovimiento(){
        if (InventarioUnidad.TablaHistorialMovimienrto!= null && InventarioUnidad.txtBuscarHistorial != null) {
            TableRowSorter<DefaultTableModel> filtro = new TableRowSorter<>(modeloMovientoUnida);
           InventarioUnidad.TablaHistorialMovimienrto.setRowSorter(filtro);

            InventarioUnidad.txtBuscarHistorial.getDocument().addDocumentListener(new DocumentListener() {
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
                    String texto = InventarioUnidad.txtBuscarHistorial.getText();
                    if (texto.trim().length() == 0) {
                        filtro.setRowFilter(null);
                    } else {
                       filtro.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(texto)));
                    }
                }
            });
        }
    }
 
    
    
    public  void MostrarMovimientoUNIDAD() throws  ClassNotFoundException,SQLException {


      TituloInventarioMovimientoUNIDAD();
        
        
       
        
        
        ArrayList<Movimiento_inventario> ListaMovimiento_inventarios = ListaDeMovimientoUNIDA();
        
        modeloMovientoUnida = (DefaultTableModel) InventarioUnidad.TablaHistorialMovimienrto.getModel();
        
        TableRowSorter<?> sorterOriginal = null;
    if (InventarioUnidad.TablaHistorialMovimienrto.getRowSorter() != null) {
        sorterOriginal = (TableRowSorter<?>)InventarioUnidad.TablaHistorialMovimienrto.getRowSorter();
     InventarioUnidad.TablaHistorialMovimienrto.setRowSorter(null);
    }
         limpiarTabla(modeloMovientoUnida);
        Object  [] obj = new  Object[9];
        
        for ( Movimiento_inventario mvt  :ListaMovimiento_inventarios){
            
            
            
            obj [0] = mvt.getIdMovimiento();
            obj [1] = mvt.getIdProducto();
            obj [2] = mvt.getTipoProducto();
            obj [3] = mvt.getTipoMovimiento();
            obj [4] = mvt.getCantidad();
            obj [5] = mvt.getStockFinal();
            obj [6] = mvt.getUsuario();
            obj [7] = mvt.getDetalle();
            obj [8] = mvt.getFechaMovimiento();
            modeloMovientoUnida.addRow(obj);
            
        }
        if (sorterOriginal != null) {
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modeloMovientoUnida);
        InventarioUnidad.TablaHistorialMovimienrto.setRowSorter(sorter);
    }
        
        InventarioUnidad.TablaHistorialMovimienrto.setModel(modeloMovientoUnida);
        
        centrarTextoTabla(   InventarioUnidad.TablaHistorialMovimienrto);
        
        
        
        
        
        
        
        
        
    }
    
     private  ArrayList<Movimiento_inventario> ListaDeMovimientoUNIDA() throws  ClassNotFoundException,SQLException {
        
     ArrayList<Movimiento_inventario> ListaMovimientoLiquido =  new  ArrayList<>();
        
        
        try {
            
            String sql = "SELECT "
           + "m.ID, "
           + "p.nombre AS nombreProducto, "
           + "m.Tipo_Producto, "
           + "m.Movimiento, "
           + "m.Cantidad, "
           + "m.StockFinal, "
           + "m.Usuario, "
           + "m.Detalle, "
           + "m.FechaMovimiento "         
           + "FROM movimiento m "
           + "INNER JOIN producto p ON p.idProducto = m.idProducto "
           + "WHERE m.Tipo_Producto = 'UNIDAD'"
           + "ORDER BY m.ID";
;
            
                   this.conectar();
                   ps = this.con.prepareStatement(sql);
                   rs = ps.executeQuery();
                   
                   while (rs.next()) {  
                       
                       Movimiento_inventario mvt = new Movimiento_inventario();
                       
                       mvt.setIdMovimiento(rs.getInt("ID"));
                       mvt.setIdProducto(rs.getString("nombreProducto"));
                       mvt.setTipoProducto(rs.getString("Tipo_Producto"));
                       mvt.setTipoMovimiento(rs.getString("Movimiento"));
                       mvt.setCantidad(rs.getInt("Cantidad"));
                       mvt.setStockFinal(rs.getInt("StockFinal"));
                       mvt.setUsuario(rs.getString("Usuario"));
                       mvt.setDetalle(rs.getString("Detalle"));
                       mvt.setFechaMovimiento(rs.getString("FechaMovimiento"));
                       
                      ListaMovimientoLiquido.add(mvt);
                     
                
            }
            
            
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error en cargar la lista de movimiento de Liquido" + e.getMessage());
        } finally {
            this.cerrarCn();
        }
          
        return  ListaMovimientoLiquido;
        
 
        
    }
    
    
    
    
    public void TituloInventarioMovimientoLiquido() {
    
    
    String Titulo[] = {
        "ID", 
        "Producto", 
        "Tipo Producto", 
        "Movimiento",
        "Cantidad", 
        "Stock Final",
        "Usuario",
        "Detalle",
        "Fecha Movimiento"
        
    };

    modeloMovientoLiquido.setColumnIdentifiers(Titulo);

    if (InventarioLiquido.TablaHistorial != null) {
        InventarioLiquido.TablaHistorial.setModel(modeloMovientoLiquido);

        // 👇 aplicar centrado de datos después de setear el modelo
        centrarTextoTabla(InventarioLiquido.TablaHistorial);
    }
} 
     
    
    public void TituloInventarioMovimientoSOLIDO() {
    
    
    String Titulo[] = {
        "ID", 
        "Producto", 
        "Tipo Producto", 
        "Movimiento",
        "Cantidad", 
        "Stock Final",
        "Usuario",
        "Detalle",
        "Fecha Movimiento"
        
    };

    modeloMovientoSolido.setColumnIdentifiers(Titulo);

    if (InventarioSolido.TablaHistorialSolido != null) {
        InventarioSolido.TablaHistorialSolido.setModel(modeloMovientoSolido);

        // 👇 aplicar centrado de datos después de setear el modelo
        centrarTextoTabla(InventarioSolido.TablaHistorialSolido);
    }
} 
    
    
    public void agregarFiltroBusquedaSOLIDOMovimiento(){
        if (InventarioSolido.TablaHistorialSolido != null && InventarioSolido.txtBuscarHistorial != null) {
            TableRowSorter<DefaultTableModel> filtro = new TableRowSorter<>(modeloMovientoSolido);
          InventarioSolido.TablaHistorialSolido.setRowSorter(filtro);

            InventarioSolido.txtBuscarHistorial.getDocument().addDocumentListener(new DocumentListener() {
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
                    String texto = InventarioSolido.txtBuscarHistorial.getText();
                    if (texto.trim().length() == 0) {
                        filtro.setRowFilter(null);
                    } else {
                       filtro.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(texto)));
                    }
                }
            });
        }
    }

    
    public  void MostrarMovimientoSOLIDO() throws  ClassNotFoundException,SQLException {


      TituloInventarioMovimientoSOLIDO();

        
        
      
        
        ArrayList<Movimiento_inventario> ListaMovimiento_inventarios = ListaDeMovimientoSOLIDO();
        
        modeloMovientoSolido = (DefaultTableModel) InventarioSolido.TablaHistorialSolido.getModel();
        
         
    // === DESACTIVAR SORTER TEMPORALMENTE ===
    TableRowSorter<?> sorterOriginal = null;
    if (InventarioSolido.TablaHistorialSolido.getRowSorter() != null) {
        sorterOriginal = (TableRowSorter<?>)InventarioSolido.TablaHistorialSolido.getRowSorter();
      InventarioSolido.TablaHistorialSolido.setRowSorter(null);
    }
    
      limpiarTabla(modeloMovientoSolido);
        
        
        Object  [] obj = new  Object[9];
        
        for (Movimiento_inventario mvt : ListaMovimiento_inventarios) {
            
         
            
            obj [0] = mvt.getIdMovimiento();
            obj [1] = mvt.getIdProducto();
            obj [2] = mvt.getTipoProducto();
            obj [3] = mvt.getTipoMovimiento();
            obj [4] = mvt.getCantidad();
            obj [5] = mvt.getStockFinal();
            obj [6] = mvt.getUsuario();
            obj [7] = mvt.getDetalle();
            obj [8] = mvt.getFechaMovimiento();
            modeloMovientoSolido.addRow(obj);
            
        } if (sorterOriginal != null) {
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modeloMovientoSolido);
        InventarioSolido.TablaHistorialSolido.setRowSorter(sorter);
    }
        
        
         InventarioSolido.TablaHistorialSolido.setModel(modeloMovientoSolido);
        
        centrarTextoTabla( InventarioSolido.TablaHistorialSolido);
        
        
        
        
        
        
        
        
        
    }
    
    
    
public void RealizarEntradaLiquido(int idInventario, int idProducto, String tipoProducto, 
                                       int cantidad, String lote, double precioUnitario) {
        
        String sql = """
            INSERT INTO movimiento 
            (idProducto, id_inventario, Tipo_Producto, Movimiento, Cantidad, 
             StockFinal, Usuario, Detalle)
            VALUES (?, ?, ?, 'ENTRADA', ?, ?, ?, ?)
            """;

        try {
            this.conectar();
            
            PreparedStatement ps = con.prepareStatement(sql);
            
            String detalle = "Compra a proveedor - Lote: " + lote;
            
            ps.setInt(1, idProducto);
            ps.setInt(2, idInventario);
            ps.setString(3, tipoProducto);
            ps.setInt(4, cantidad);
            ps.setInt(5, cantidad);                    // StockFinal = Cantidad al ser ENTRADA
            ps.setString(6, Usuario.usuarioActual);    // Usuario logueado
            ps.setString(7, detalle);

            int resultado = ps.executeUpdate();
            
            if (resultado > 0) {
                // Opcional: Mensaje de éxito (puedes quitarlo si prefieres)
                // JOptionPane.showMessageDialog(null, "Movimiento de ENTRADA registrado correctamente");
            } else {
                JOptionPane.showMessageDialog(null, "No se pudo registrar el movimiento", 
                                            "Error", JOptionPane.WARNING_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                "Error al registrar movimiento de entrada:\n" + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
 
        private  ArrayList<Movimiento_inventario> ListaDeMovimientoSOLIDO () throws  ClassNotFoundException,SQLException {
        
     ArrayList<Movimiento_inventario> ListaMovimientoLiquido =  new  ArrayList<>();
        
        
        try {
            
            String sql = "SELECT "
           + "m.ID, "
           + "p.nombre AS nombreProducto, "
           + "m.Tipo_Producto, "
           + "m.Movimiento, "
           + "m.Cantidad, "
           + "m.StockFinal, "
           + "m.Usuario, "
           + "m.Detalle, "
           + "m.FechaMovimiento "         
           + "FROM movimiento m "
           + "INNER JOIN producto p ON p.idProducto = m.idProducto "
           + "WHERE m.Tipo_Producto = 'SOLIDO'"
           + "ORDER BY m.ID";

            
                   this.conectar();
                   ps = this.con.prepareStatement(sql);
                   rs = ps.executeQuery();
                   
                   while (rs.next()) {  
                       
                       Movimiento_inventario mvt = new Movimiento_inventario();
                       
                       mvt.setIdMovimiento(rs.getInt("ID"));
                       mvt.setIdProducto(rs.getString("nombreProducto"));
                       mvt.setTipoProducto(rs.getString("Tipo_Producto"));
                       mvt.setTipoMovimiento(rs.getString("Movimiento"));
                       mvt.setCantidad(rs.getInt("Cantidad"));
                       mvt.setStockFinal(rs.getInt("StockFinal"));
                       mvt.setUsuario(rs.getString("Usuario"));
                       mvt.setDetalle(rs.getString("Detalle"));
                       mvt.setFechaMovimiento(rs.getString("FechaMovimiento"));
                       
                      ListaMovimientoLiquido.add(mvt);
                     
                
            }
            
            
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error en cargar la lista de movimiento de Liquido" + e.getMessage());
        } finally {
            this.cerrarCn();
        }
          
        return  ListaMovimientoLiquido;
        
 
        
    }
    
    
    
    
    
    
    
    public  void MostrarMovimientoLiquido() throws  ClassNotFoundException,SQLException {


      TituloInventarioMovimientoLiquido();
        
        limpiarTablaMovimiento();
        
        
        limpiarTabla(modeloMovientoLiquido);
        
        
        ArrayList<Movimiento_inventario> ListaMovimiento_inventarios = ListaDeMovimientoLIQUIDO();
        
        modeloMovientoLiquido = (DefaultTableModel) InventarioLiquido.TablaHistorial.getModel();
        
        Object  [] obj = new  Object[9];
        
        for (int i = 0; i < ListaMovimiento_inventarios.size(); i++) {
            
            Movimiento_inventario mvt = ListaMovimiento_inventarios.get(i);
            
            obj [0] = mvt.getIdMovimiento();
            obj [1] = mvt.getIdProducto();
            obj [2] = mvt.getTipoProducto();
            obj [3] = mvt.getTipoMovimiento();
            obj [4] = mvt.getCantidad();
            obj [5] = mvt.getStockFinal();
            obj [6] = mvt.getUsuario();
            obj [7] = mvt.getDetalle();
            obj [8] = mvt.getFechaMovimiento();
            modeloMovientoLiquido.addRow(obj);
            
        }
        
        
       InventarioLiquido.TablaHistorial.setModel(modeloMovientoLiquido);
        
        centrarTextoTabla(InventarioLiquido.TablaHistorial);
        
        
        
        
        
        
        
        
        
    }
    
    
    
  public void agregarFiltroBusquedaLiquidoHistario(){
        if (InventarioLiquido.TablaHistorial != null && InventarioLiquido.txtBuscarHistorial != null) {
            TableRowSorter<DefaultTableModel> filtro = new TableRowSorter<>(modeloMovientoLiquido);
            InventarioLiquido.TablaHistorial.setRowSorter(filtro);

            InventarioLiquido.txtBuscarHistorial.getDocument().addDocumentListener(new DocumentListener() {
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
                    String texto = InventarioLiquido.txtBuscarHistorial.getText();
                    if (texto.trim().length() == 0) {
                        filtro.setRowFilter(null);
                    } else {
                       filtro.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(texto)));
                    }
                }
            });
        }
    }
    
    
    
    private  ArrayList<Movimiento_inventario> ListaDeMovimientoLIQUIDO () throws  ClassNotFoundException,SQLException {
        
     ArrayList<Movimiento_inventario> ListaMovimientoLiquido =  new  ArrayList<>();
        
        
        try {
            
            String sql = "SELECT "
           + "m.ID, "
           + "p.nombre AS nombreProducto, "
           + "m.Tipo_Producto, "
           + "m.Movimiento, "
           + "m.Cantidad, "
           + "m.StockFinal, "
           + "m.Usuario, "
           + "m.Detalle, "
           + "m.FechaMovimiento "         
           + "FROM movimiento m "
           + "INNER JOIN producto p ON p.idProducto = m.idProducto "
           + "WHERE m.Tipo_Producto = 'LIQUIDO'"
             + "ORDER BY m.ID";

            
                   this.conectar();
                   ps = this.con.prepareStatement(sql);
                   rs = ps.executeQuery();
                   
                   while (rs.next()) {  
                       
                       Movimiento_inventario mvt = new Movimiento_inventario();
                       
                       mvt.setIdMovimiento(rs.getInt("ID"));
                       mvt.setIdProducto(rs.getString("nombreProducto"));
                       mvt.setTipoProducto(rs.getString("Tipo_Producto"));
                       mvt.setTipoMovimiento(rs.getString("Movimiento"));
                       mvt.setCantidad(rs.getInt("Cantidad"));
                       mvt.setStockFinal(rs.getInt("StockFinal"));
                       mvt.setUsuario(rs.getString("Usuario"));
                       mvt.setDetalle(rs.getString("Detalle"));
                       mvt.setFechaMovimiento(rs.getString("FechaMovimiento"));
                       
                      ListaMovimientoLiquido.add(mvt);
                     
                
            }
            
            
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error en cargar la lista de movimiento de Liquido" + e.getMessage());
        } finally {
            this.cerrarCn();
        }
          
        return  ListaMovimientoLiquido;
        
 
        
    }
    
    
    
    
    // 1. Definir títulos de la tabla
  
    // 2. Centrar texto de tabla
    public void centrarTextoTabla(JTable tabla) {
        DefaultTableCellRenderer centrado = new DefaultTableCellRenderer();
        centrado.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < tabla.getColumnCount(); i++) {
            tabla.getColumnModel().getColumn(i).setCellRenderer(centrado);
        }
    }

    // 3. Limpiar tabla
    private void limpiarTablaMovimiento() {
        modeloMovientoLiquido.setRowCount(0);
    }
  
  
    
 
    public  void Realizar_SALIDA_SOLIDO() throws  ClassNotFoundException,SQLException {
        
        mv.setTipoProducto(Vista_Salida_Solido.txtProductoSolido.getText());
        
        mv.setTipoMovimiento(Vista_Salida_Solido.txtSalidaSolido.getText());
        
        mv.setCantidad(Integer.parseInt(Vista_Salida_Solido.txtCantidad.getText()));
        
        mv.setPrecio_Venta(Double.parseDouble((Vista_Salida_Solido.txtPrecioVentaUnitario.getText())));
        
        
        mv.setDetalle(Vista_Salida_Solido.txtDetalle.getText());
   
        
        Inventario productoSeleccionado = 
       (Inventario) Vista_Salida_Solido.jComboBoxProductoSolido.getSelectedItem();

int idProducto = productoSeleccionado.getIdProducto(); // 👈 Este es el que necesitas

       cuenta SeleccionCuenta = (cuenta) Vista_Salida_Solido.jComboBoxCuantaPasivo.getSelectedItem();
       int idCuenta = SeleccionCuenta.getIdCuenta();
        
        Inventario productoSeleccionado0 = 
       (Inventario) Vista_Salida_Solido.jComboBoxProductoSolido.getSelectedItem();

int idProduct = productoSeleccionado0.getIdinventario();


cuenta SeleccionCuentaIngreso = (cuenta)Vista_Salida_Solido.jComboBoxIngreso.getSelectedItem();
int IDCuentaIngreso = SeleccionCuentaIngreso.getIdCuenta();

        String sql = "insert into movimiento (idProducto,id_inventario,Tipo_Producto,Movimiento,Cantidad,Usuario,Detalle,Precio_Venta,id_cuenta,id_cuenta_ingreso) values (?,?,?,?,?,?,?,?,?,?)";
        
        try {
            this.conectar();
            ps = this.con.prepareStatement(sql);
            
            ps.setInt(1, idProducto);
            ps.setInt(2, idProduct);
            ps.setString(3, mv.getTipoProducto());
            ps.setString(4, mv.getTipoMovimiento());
            ps.setInt(5, mv.getCantidad());
            ps.setString(6,Usuario.usuarioActual);
            ps.setString(7,mv.getDetalle());
            ps.setDouble(8 , mv.getPrecio_Venta());
            ps.setInt(9, idCuenta);
            ps.setInt(10, IDCuentaIngreso);
            
            if (ps.executeUpdate() > 0) {
                
                JOptionPane.showMessageDialog(null, "Movimiento realizado"); 
            
                
              Vista_Salida_Solido.txtCantidad.setText("");
                Vista_Salida_Solido.txtPrecioVentaUnitario.setText("");
                Vista_Salida_Solido.txtDetalle.setText("");
                
                
            } 
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error " + e.getMessage());
        }
        
        
        
        
        
        
        
    }
    
    
    
    
    public  void Realizar_SALIDA_Unidad() throws  ClassNotFoundException,SQLException {
        
        mv.setTipoProducto(Vista_Salida_Unidad.txtTipoProductoUnidad.getText());
        
        mv.setTipoMovimiento(Vista_Salida_Unidad.txtAccionSalida.getText());
        
        mv.setCantidad(Integer.parseInt(Vista_Salida_Unidad.txtCantidadUnida.getText()));
        
        mv.setPrecio_Venta(Double.parseDouble((Vista_Salida_Unidad.txtPrecioVentaUnitario.getText())));
        
        
        mv.setDetalle(Vista_Salida_Unidad.txtDetalle.getText());
   
        
        Inventario productoSeleccionado = 
       (Inventario) Vista_Salida_Unidad.jComboBoxProductoUnida.getSelectedItem();

int idProducto = productoSeleccionado.getIdProducto(); // 👈 Este es el que necesitas

       cuenta SeleccionCuenta = (cuenta) Vista_Salida_Unidad.jComboBoxCuenta.getSelectedItem();
       int idCuenta = SeleccionCuenta.getIdCuenta();
        
        Inventario productoSeleccionado0 = 
       (Inventario) Vista_Salida_Unidad.jComboBoxProductoUnida.getSelectedItem();
        
     
int idProduct = productoSeleccionado0.getIdinventario();


cuenta SeleccionCuentaIngreso = (cuenta) Vista_Salida_Unidad.jComboBoxIngreson.getSelectedItem();

int idCuentaIngreso = SeleccionCuentaIngreso.getIdCuenta();


        String sql = "insert into movimiento (idProducto,id_inventario,Tipo_Producto,Movimiento,Cantidad,Usuario,Detalle,Precio_Venta,id_cuenta,id_cuenta_ingreso) values (?,?,?,?,?,?,?,?,?,?)";
        
        try {
            this.conectar();
            ps = this.con.prepareStatement(sql);
            
            ps.setInt(1, idProducto);
            ps.setInt(2, idProduct);
            ps.setString(3, mv.getTipoProducto());
            ps.setString(4, mv.getTipoMovimiento());
            ps.setInt(5, mv.getCantidad());
            ps.setString(6,Usuario.usuarioActual);
            ps.setString(7,mv.getDetalle());
            ps.setDouble(8, mv.getPrecio_Venta());
            ps.setInt(9, idCuenta);
            ps.setInt(10, idCuentaIngreso);
            if (ps.executeUpdate() > 0) {
                
                JOptionPane.showMessageDialog(null, "Movimiento realizado"); 
               
                
             Vista_Salida_Unidad.txtCantidadUnida.setText("");
                Vista_Salida_Unidad.txtPrecioVentaUnitario.setText("");
                Vista_Salida_Unidad.txtDetalle.setText("");
                
                
                
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error " + e.getMessage());
        }
        
        
        
        
        
        
        
    }
    
    
  
     public  void Realizar_SALIDA_Liquido() throws  ClassNotFoundException,SQLException {
        
        mv.setTipoProducto(Vista_Salida_Liquido.txtTipoProducto.getText());
        
        mv.setTipoMovimiento(Vista_Salida_Liquido.Txttiposalidaliquido.getText());
        
        mv.setCantidad(Integer.parseInt(Vista_Salida_Liquido.txtCantidadSalida.getText()));
        
        mv.setPrecio_Venta(Double.parseDouble((Vista_Salida_Liquido.txtPrecioVenta.getText())));
        
        
        mv.setDetalle(Vista_Salida_Liquido.txtDetalle.getText());
   
        
        Inventario productoSeleccionado = 
       (Inventario) Vista_Salida_Liquido.jComboBoxProductoLiquido.getSelectedItem();

int idProducto = productoSeleccionado.getIdProducto(); // 👈 Este es el que necesitas

       cuenta SeleccionCuenta = (cuenta) Vista_Salida_Liquido.jComboBoxCuentaPasivo.getSelectedItem();
       int idCuenta = SeleccionCuenta.getIdCuenta();
        
        cuenta seleCCIONcUENTAiNGRESO = (cuenta) Vista_Salida_Liquido.jComboBoxCuentaIngreso.getSelectedItem();
        int idCuentaIngreso = seleCCIONcUENTAiNGRESO.getIdCuenta();

        Inventario productoSeleccionado0 = 
       (Inventario) Vista_Salida_Liquido.jComboBoxProductoLiquido.getSelectedItem();
        
int idProduct = productoSeleccionado0.getIdinventario();
        String sql = "insert into movimiento (idProducto,id_inventario,Tipo_Producto,Movimiento,Cantidad,Usuario,Detalle,Precio_Venta,id_cuenta,id_cuenta_ingreso) values (?,?,?,?,?,?,?,?,?,?)";
        
        try {
            this.conectar();
            ps = this.con.prepareStatement(sql);
            
            ps.setInt(1, idProducto);
            ps.setInt(2, idProduct);
            ps.setString(3, mv.getTipoProducto());
            ps.setString(4, mv.getTipoMovimiento());
            ps.setInt(5, mv.getCantidad());
            ps.setString(6,Usuario.usuarioActual);
            ps.setString(7,mv.getDetalle());
            ps.setDouble(8, mv.getPrecio_Venta());
            ps.setInt(9, idCuenta);
            ps.setInt(10, idCuentaIngreso);
            
            if (ps.executeUpdate() > 0) {
                
                JOptionPane.showMessageDialog(null, "Movimiento realizado"); 
              
                
                Vista_Salida_Liquido.txtCantidadSalida.setText("");
                Vista_Salida_Liquido.txtPrecioVenta.setText("");
                Vista_Salida_Liquido.txtDetalle.setText("");
                Vista_Salida_Liquido.TextAreaInformacionProducto.setText("");
                
                
            } 
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error " + e.getMessage());
        }
        
        
        
        
        
        
        
    }
    
    
    public void Realizar_AJUSTE_Solido() throws ClassNotFoundException, SQLException {

    mv.setTipoProducto(Ajuste_Solido.txtTipoProductoSolido.getText());

    mv.setTipoMovimiento(Ajuste_Solido.txtAccionAjuste.getText());

    // ⚡ Leer cantidad con signos (+40, -70) y sin espacios
    String cantidadStr = Ajuste_Solido.txtCantidad.getText().trim().replace(" ", "");

    try {
        int cantidadAjuste = Integer.parseInt(cantidadStr); // acepta +40, -70, 40
        mv.setCantidad(cantidadAjuste);
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "Ingrese un número válido. Ejemplo: 40, -40, +40");
        return; // salir si no es válido
    }

    mv.setDetalle(Ajuste_Solido.txtDetalleUnidad.getText());

    // Obtener idProducto
    Inventario productoSeleccionado = (Inventario) Ajuste_Solido.jComboBoxProducto.getSelectedItem();
    int idProducto = productoSeleccionado.getIdProducto();

    // Obtener idInventario
    int idInventario = productoSeleccionado.getIdinventario();
    
    cuenta SeleccionCuenta = (cuenta) Ajuste_Solido.jComboBoxCuentaPasivo.getSelectedItem();
  int idCuenta = SeleccionCuenta.getIdCuenta();
    String sql = "INSERT INTO movimiento (idProducto, id_inventario, Tipo_Producto, Movimiento, Cantidad, Usuario, Detalle,id_cuenta) VALUES (?,?,?,?,?,?,?,?)";

    try {
        this.conectar();
        ps = this.con.prepareStatement(sql);

        ps.setInt(1, idProducto);
        ps.setInt(2, idInventario);
        ps.setString(3, mv.getTipoProducto());
        ps.setString(4, mv.getTipoMovimiento());
        ps.setInt(5, mv.getCantidad());
        ps.setString(6, Usuario.usuarioActual);
        ps.setString(7, mv.getDetalle());
        ps.setInt(8,idCuenta );

        if (ps.executeUpdate() > 0) {
            
            JOptionPane.showMessageDialog(null, "Movimiento realizado con exito");
   
           Ajuste_Solido.txtCantidad.setText("");
          Ajuste_Solido.txtDetalleUnidad.setText("");

        } else {
            JOptionPane.showMessageDialog(null, "No se pudo realizar el movimiento");
        }

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
    }
}

         
 public void RealizarAjusteSolido() {
    
     int prododucto = Ajuste_Solido.jComboBoxProducto.getSelectedIndex();
     
    if (prododucto == -1) {
        JOptionPane.showMessageDialog(null,
            "Debe seleccionar un producto para realizar el ajuste.",
            "Error",
            JOptionPane.ERROR_MESSAGE);
        return;
    } 
    
    // Mensaje de confirmación
  String mensaje = "<html><b>¿Estás seguro de realizar este ajuste Solido?</b><br><br>" +
                 "Este movimiento:<br><br>" +
                 "• Afectará el Stock del producto<br>" +
                 "• Afectará la cuenta de Ajuste de Inventario<br><br>" +
                 "• Cantidad: " + Ajuste_Solido.txtCantidad.getText() + "<br>" +
                 "• Detalle: " + Ajuste_Solido.txtDetalleUnidad.getText() + "</html>";
   
    int respuesta = JOptionPane.showConfirmDialog(null,
        mensaje,
        "Confirmar Ajuste Solido",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.QUESTION_MESSAGE);
   
    if (respuesta == JOptionPane.YES_OPTION) {
        
        try {
            Realizar_AJUSTE_Solido();   // No se toca este método
        } 
        catch (ClassNotFoundException | SQLException ex) {
            JOptionPane.showMessageDialog(null,
                "Error al realizar el ajuste:\n" + ex.getMessage(),
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            
            // Opcional: registrar en consola
            ex.printStackTrace();
        }
    } 
    // else: el usuario canceló → no se hace nada
}
  
    
         
    public void Realizar_AJUSTE_Unidad() throws ClassNotFoundException, SQLException {

    mv.setTipoProducto(Ajuste_Unidad.txtTipoProductuUnidad.getText());

    mv.setTipoMovimiento(Ajuste_Unidad.txtAccionAjuste.getText());

    // ⚡ Leer cantidad con signos (+40, -70) y sin espacios
    String cantidadStr = Ajuste_Unidad.txtCantidad.getText().trim().replace(" ", "");

    try {
        int cantidadAjuste = Integer.parseInt(cantidadStr); // acepta +40, -70, 40
        mv.setCantidad(cantidadAjuste);
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "Ingrese un número válido. Ejemplo: 40, -40, +40");
        return; // salir si no es válido
    }

    mv.setDetalle(Ajuste_Unidad.txtDetalle.getText());

    // Obtener idProducto
    Inventario productoSeleccionado = (Inventario) Ajuste_Unidad.jComboBoxProductoUnidad.getSelectedItem();
    int idProducto = productoSeleccionado.getIdProducto();

    // Obtener idInventario
    int idInventario = productoSeleccionado.getIdinventario();

    cuenta SaleccionCuenta = (cuenta) Ajuste_Unidad.jComboBoxCuentas.getSelectedItem();
    int idCuenta = SaleccionCuenta.getIdCuenta();
    String sql = "INSERT INTO movimiento (idProducto, id_inventario, Tipo_Producto, Movimiento, Cantidad, Usuario, Detalle,id_cuenta) VALUES (?,?,?,?,?,?,?,?)";

    try {
        this.conectar();
        ps = this.con.prepareStatement(sql);

        ps.setInt(1, idProducto);
        ps.setInt(2, idInventario);
        ps.setString(3, mv.getTipoProducto());
        ps.setString(4, mv.getTipoMovimiento());
        ps.setInt(5, mv.getCantidad());
        ps.setString(6, Usuario.usuarioActual);
        ps.setString(7, mv.getDetalle());
        ps.setInt(8, idCuenta);

        if (ps.executeUpdate() > 0) {
            JOptionPane.showMessageDialog(null, "Movimiento realizado");
         

            // Limpiar campos
           Ajuste_Unidad.txtCantidad.setText("");
            Ajuste_Unidad.txtDetalle.setText("");

        } else {
            JOptionPane.showMessageDialog(null, "No se pudo realizar el movimiento");
        }

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
    }
}
       
 public void RealizarAjusteUnidad() {
    
     int prododucto = Ajuste_Unidad.jComboBoxProductoUnidad.getSelectedIndex();
     
    if (prododucto == -1) {
        JOptionPane.showMessageDialog(null,
            "Debe seleccionar un producto para realizar el ajuste.",
            "Error",
            JOptionPane.ERROR_MESSAGE);
        return;
    } 
    
    // Mensaje de confirmación
  String mensaje = "<html><b>¿Estás seguro de realizar este ajuste Unidad?</b><br><br>" +
                 "Este movimiento:<br><br>" +
                 "• Afectará el Stock del producto<br>" +
                 "• Afectará la cuenta de Ajuste de Inventario<br><br>" +
                 "• Cantidad: " + Ajuste_Unidad.txtCantidad.getText() + "<br>" +
                 "• Detalle: " + Ajuste_Unidad.txtDetalle.getText() + "</html>";
   
    int respuesta = JOptionPane.showConfirmDialog(null,
        mensaje,
        "Confirmar Ajuste Unidad",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.QUESTION_MESSAGE);
   
    if (respuesta == JOptionPane.YES_OPTION) {
        
        try {
            Realizar_AJUSTE_Unidad();   // No se toca este método
        } 
        catch (ClassNotFoundException | SQLException ex) {
            JOptionPane.showMessageDialog(null,
                "Error al realizar el ajuste:\n" + ex.getMessage(),
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            
            // Opcional: registrar en consola
            ex.printStackTrace();
        }
    } 
    // else: el usuario canceló → no se hace nada
}
   
    
     public void Realizar_AJUSTE_Liquido() throws ClassNotFoundException, SQLException {

    mv.setTipoProducto(Ajuste_Liquido.txtTipoProducto.getText());

    mv.setTipoMovimiento(Ajuste_Liquido.txtAccionAjuste.getText());

    // ⚡ Leer cantidad con signos (+40, -70) y sin espacios
    String cantidadStr = Ajuste_Liquido.txtCantidad.getText().trim().replace(" ", "");

    try {
        int cantidadAjuste = Integer.parseInt(cantidadStr); // acepta +40, -70, 40
        mv.setCantidad(cantidadAjuste);
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "Ingrese un número válido. Ejemplo: 40, -40, +40");
        return; // salir si no es válido
    }

    mv.setDetalle(Ajuste_Liquido.txtDetalle.getText());

    // Obtener idProducto
    Inventario productoSeleccionado = (Inventario) Ajuste_Liquido.jComboBoxProductoLiquidoAjuste.getSelectedItem();
    int idProducto = productoSeleccionado.getIdProducto();

    // Obtener idInventario
    int idInventario = productoSeleccionado.getIdinventario();
    
    cuenta SeleccionCuenta = (cuenta) Ajuste_Liquido.jComboBoxCuentaInventario.getSelectedItem();
    int idCuenta = SeleccionCuenta.getIdCuenta();

    String sql = "INSERT INTO movimiento (idProducto, id_inventario, Tipo_Producto, Movimiento, Cantidad, Usuario, Detalle,id_cuenta) VALUES (?,?,?,?,?,?,?,?)";

    try {
        this.conectar();
        ps = this.con.prepareStatement(sql);

        ps.setInt(1, idProducto);
        ps.setInt(2, idInventario);
        ps.setString(3, mv.getTipoProducto());
        ps.setString(4, mv.getTipoMovimiento());
        ps.setInt(5, mv.getCantidad());
        ps.setString(6, Usuario.usuarioActual);
        ps.setString(7, mv.getDetalle());
        ps.setInt(8, idCuenta);

        if (ps.executeUpdate() > 0) {
            JOptionPane.showMessageDialog(null, "Movimiento realizado");
       //   
            Ajuste_Liquido.txtCantidad.setText("");
           Ajuste_Liquido.txtDetalle.setText("");

        } else {
            JOptionPane.showMessageDialog(null, "No se pudo realizar el movimiento");
        }

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
    }
}

     ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
     
  
     
 public void RealizarAjusteLiquido() {
    
     int prododucto = Ajuste_Liquido.jComboBoxProductoLiquidoAjuste.getSelectedIndex();
     
    if (prododucto == -1) {
        JOptionPane.showMessageDialog(null,
            "Debe seleccionar un producto para realizar el ajuste.",
            "Error",
            JOptionPane.ERROR_MESSAGE);
        return;
    } 
    
    // Mensaje de confirmación
  String mensaje = "<html><b>¿Estás seguro de realizar este ajuste líquido?</b><br><br>" +
                 "Este movimiento:<br><br>" +
                 "• Afectará el Stock del producto<br>" +
                 "• Afectará la cuenta de Ajuste de Inventario<br><br>" +
                 "• Cantidad: " + Ajuste_Liquido.txtCantidad.getText() + "<br>" +
                 "• Detalle: " + Ajuste_Liquido.txtDetalle.getText() + "</html>";
   
    int respuesta = JOptionPane.showConfirmDialog(null,
        mensaje,
        "Confirmar Ajuste Líquido",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.QUESTION_MESSAGE);
   
    if (respuesta == JOptionPane.YES_OPTION) {
        
        try {
            Realizar_AJUSTE_Liquido();   // No se toca este método
        } 
        catch (ClassNotFoundException | SQLException ex) {
            JOptionPane.showMessageDialog(null,
                "Error al realizar el ajuste:\n" + ex.getMessage(),
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            
            // Opcional: registrar en consola
            ex.printStackTrace();
        }
    } 
    // else: el usuario canceló → no se hace nada
}
   
}