/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ModeloDAO;

import Modelo.ConexiónBD;
import Modelo.Inventario;
import Modelo.Movimiento_inventario;
import Modelo.Usuario;
import Modelo.cuenta;
import Vista_Almacen.Ajuste_Liquido;
import Vista_Almacen.Vista_Salida_Liquido;
import Vista_GestionInventario.InventarioLiquido;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class Mvt_Liquido_Dao extends ConexiónBD {

    PreparedStatement ps;
    ResultSet rs;
    Movimiento_inventario mv = new Movimiento_inventario();
    DefaultTableModel modeloMovientoLiquido = new DefaultTableModel();

    // ====================== MÉTODOS GENERALES ======================
    private void limpiarTabla(DefaultTableModel modelo) {
        modelo.setRowCount(0);
    }

    public void centrarTextoTabla(JTable tabla) {
        DefaultTableCellRenderer centrado = new DefaultTableCellRenderer();
        centrado.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < tabla.getColumnModel().getColumnCount(); i++) {
            tabla.getColumnModel().getColumn(i).setCellRenderer(centrado);
        }
    }

    public void TituloInventarioMovimientoLiquido() {
        String Titulo[] = {
            "ID", "Producto", "Tipo Producto", "Movimiento",
            "Cantidad", "Stock Final", "Usuario", "Detalle", "Fecha Movimiento"
        };
        modeloMovientoLiquido.setColumnIdentifiers(Titulo);

        if (InventarioLiquido.TablaHistorial != null) {
            InventarioLiquido.TablaHistorial.setModel(modeloMovientoLiquido);
            centrarTextoTabla(InventarioLiquido.TablaHistorial);
        }
    }

public void Realizar_SALIDA_Liquido() throws ClassNotFoundException, SQLException {
        try {
            this.conectar();

            Inventario producto = (Inventario) Vista_Salida_Liquido.jComboBoxProductoLiquido.getSelectedItem();
            if (producto == null) {
                JOptionPane.showMessageDialog(null, "Debe seleccionar un producto", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int idInventario = producto.getIdinventario();
            int idProducto = producto.getIdProducto();
            String tipoProducto = Vista_Salida_Liquido.txtTipoProducto.getText();
            String tipoMovimiento = Vista_Salida_Liquido.Txttiposalidaliquido.getText();
            int cantidad = Integer.parseInt(Vista_Salida_Liquido.txtCantidadSalida.getText().trim());
            double precioVenta = Double.parseDouble(Vista_Salida_Liquido.txtPrecioVenta.getText().trim());
            String detalle = Vista_Salida_Liquido.txtDetalle.getText();

            cuenta cuentaCaja = (cuenta) Vista_Salida_Liquido.jComboBoxCuentaIngreso.getSelectedItem();   // Caja / Banco
            cuenta cuentaIngreso = (cuenta) Vista_Salida_Liquido.jComboBoxCuentaPasivo.getSelectedItem(); // Ventas

            if (cuentaCaja == null || cuentaIngreso == null) {
                JOptionPane.showMessageDialog(null, "Debe seleccionar las cuentas contables", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double totalVenta = cantidad * precioVenta;

            // === Contabilidad ===
            int idAsiento = crearAsientoSalida(tipoMovimiento, totalVenta, producto.getProductos());

            registrarLibroDiario(idAsiento, cuentaCaja.getIdCuenta(), totalVenta, 0.0);      // Debe: Caja
            registrarLibroDiario(idAsiento, cuentaIngreso.getIdCuenta(), 0.0, totalVenta);   // Haber: Ingresos

            // Actualizar saldos + Libro Mayor
            actualizarSaldoCuenta(cuentaCaja.getIdCuenta(), totalVenta, true);    // Debe → +
            actualizarSaldoCuenta(cuentaIngreso.getIdCuenta(), totalVenta, false); // Haber → +

            // === Movimiento e Inventario ===
            registrarMovimiento(idProducto, idInventario, tipoProducto, tipoMovimiento, cantidad, detalle);
            actualizarStockInventario(idInventario, cantidad);

            JOptionPane.showMessageDialog(null,
                "¡Salida registrada correctamente!\n\n" +
                "Producto: " + producto.getProductos()+ "\n" +
                "Cantidad: " + cantidad + "\n" +
                "Total: $" + totalVenta + "\n\n" +
                "Debe: " + cuentaCaja.getNombre() + " (+$" + totalVenta + ")\n" +
                "Haber: " + cuentaIngreso.getNombre() + " (+$" + totalVenta + ")",
                "Éxito", JOptionPane.INFORMATION_MESSAGE);

            limpiarCamposSalida();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al realizar salida:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ====================== MÉTODOS AUXILIARES ======================

    private int crearAsientoSalida(String tipoMov, double total, String producto) throws SQLException {
        String descripcion = "Salida - " + tipoMov + " | " + producto + " | Total: $" + total;

        String sql = """
            INSERT INTO asiento_contable (fecha, descripcion, referencia, total_debe, total_haber)
            VALUES (CURDATE(), ?, ?, ?, ?)
            """;

        ps = con.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, descripcion);
        ps.setString(2, "SAL-" + System.currentTimeMillis());
        ps.setDouble(3, total);
        ps.setDouble(4, total);
        ps.executeUpdate();

        rs = ps.getGeneratedKeys();
        return rs.next() ? rs.getInt(1) : 0;
    }

    private void registrarLibroDiario(int idAsiento, int idCuenta, double debe, double haber) throws SQLException {
        String sql = """
            INSERT INTO libro_diario (id_asiento, id_cuenta, debe, haber)
            VALUES (?, ?, ?, ?)
            """;
        ps = con.prepareStatement(sql);
        ps.setInt(1, idAsiento);
        ps.setInt(2, idCuenta);
        ps.setDouble(3, debe);
        ps.setDouble(4, haber);
        ps.executeUpdate();
    }

   private void actualizarSaldoCuenta(int idCuenta, double monto, boolean esDebe) throws SQLException {
    // Actualizar saldo en tabla cuenta
    String sqlCuenta = "UPDATE cuenta SET saldo_inicial = saldo_inicial + ? WHERE id_cuenta = ?";
    ps = con.prepareStatement(sqlCuenta);
    ps.setDouble(1, esDebe ? monto : -monto);
    ps.setInt(2, idCuenta);
    ps.executeUpdate();

    // Registrar movimiento en Libro Mayor
    registrarEnLibroMayor(idCuenta, monto, esDebe);
}

   private void registrarEnLibroMayor(int idCuenta, double monto, boolean esDebe) throws SQLException {
    
    // 1. Obtener el último saldo de esta cuenta
    String sqlSaldo = """
        SELECT saldo_final 
        FROM libro_mayor 
        WHERE id_cuenta = ? 
        ORDER BY fecha DESC, id_libro_mayor DESC 
        LIMIT 1
        """;
    
    ps = con.prepareStatement(sqlSaldo);
    ps.setInt(1, idCuenta);
    rs = ps.executeQuery();
    
    double saldoAnterior = 0.0;
    if (rs.next()) {
        saldoAnterior = rs.getDouble("saldo_final");
    }

    // 2. Calcular nuevo saldo
    double debe = esDebe ? monto : 0.0;
    double haber = esDebe ? 0.0 : monto;
    double saldoFinal = saldoAnterior + (esDebe ? monto : -monto);

    // 3. Insertar en Libro Mayor
    String sql = """
        INSERT INTO libro_mayor 
        (id_cuenta, fecha, saldo_anterior, debe, haber, saldo_final)
        VALUES (?, CURDATE(), ?, ?, ?, ?)
        """;
    
    ps = con.prepareStatement(sql);
    ps.setInt(1, idCuenta);
    ps.setDouble(2, saldoAnterior);
    ps.setDouble(3, debe);
    ps.setDouble(4, haber);
    ps.setDouble(5, saldoFinal);
    ps.executeUpdate();
}
    private void registrarMovimiento(int idProducto, int idInventario, String tipoProducto,
                                    String tipoMovimiento, int cantidad, String detalle) throws SQLException {
        
        int stockActual = obtenerStockActual(idInventario);
        int stockFinal = stockActual - cantidad;

        String sql = """
            INSERT INTO movimiento
            (idProducto, id_inventario, Tipo_Producto, Movimiento, Cantidad,
             StockFinal, Usuario, Detalle)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;
        
        ps = con.prepareStatement(sql);
        ps.setInt(1, idProducto);
        ps.setInt(2, idInventario);
        ps.setString(3, tipoProducto);
        ps.setString(4, tipoMovimiento);
        ps.setInt(5, cantidad);
        ps.setInt(6, stockFinal);
        ps.setString(7, Usuario.usuarioActual);
        ps.setString(8, detalle);
        ps.executeUpdate();
    }

    private int obtenerStockActual(int idInventario) throws SQLException {
        String sql = "SELECT Cantidad_Disponible FROM inventario WHERE id_inventario = ?";
        ps = con.prepareStatement(sql);
        ps.setInt(1, idInventario);
        rs = ps.executeQuery();
        return rs.next() ? rs.getInt("Cantidad_Disponible") : 0;
    }

    private void actualizarStockInventario(int idInventario, int cantidadSalida) throws SQLException {
        String sql = "UPDATE inventario SET Cantidad_Disponible = Cantidad_Disponible - ? WHERE id_inventario = ?";
        ps = con.prepareStatement(sql);
        ps.setInt(1, cantidadSalida);
        ps.setInt(2, idInventario);
        ps.executeUpdate();
    }

    private void limpiarCamposSalida() {
        Vista_Salida_Liquido.txtCantidadSalida.setText("");
        Vista_Salida_Liquido.txtPrecioVenta.setText("");
        Vista_Salida_Liquido.txtDetalle.setText("");
        Vista_Salida_Liquido.TextAreaInformacionProducto.setText("");
    }

    // ====================== ENTRADA ======================
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
            ps = con.prepareStatement(sql);
            String detalle = "Compra a proveedor - Lote: " + lote;

            ps.setInt(1, idProducto);
            ps.setInt(2, idInventario);
            ps.setString(3, tipoProducto);
            ps.setInt(4, cantidad);
            ps.setInt(5, cantidad);
            ps.setString(6, Usuario.usuarioActual);
            ps.setString(7, detalle);
            ps.executeUpdate();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al registrar entrada:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // ====================== AJUSTE ======================
    public void RealizarAjusteLiquido() {
        int seleccionado = Ajuste_Liquido.jComboBoxProductoLiquidoAjuste.getSelectedIndex();
        if (seleccionado == -1) {
            JOptionPane.showMessageDialog(null, "Debe seleccionar un producto", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String mensaje = "<html><b>¿Confirmar ajuste de inventario?</b><br><br>" +
                        "Cantidad: " + Ajuste_Liquido.txtCantidad.getText() + "<br>" +
                        "Detalle: " + Ajuste_Liquido.txtDetalle.getText() + "</html>";

        int respuesta = JOptionPane.showConfirmDialog(null, mensaje, "Confirmar Ajuste", 
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (respuesta == JOptionPane.YES_OPTION) {
            try {
                Realizar_AJUSTE_Liquido();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error al realizar ajuste:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    public void Realizar_AJUSTE_Liquido() throws ClassNotFoundException, SQLException {
        mv.setTipoProducto(Ajuste_Liquido.txtTipoProducto.getText());
        mv.setTipoMovimiento(Ajuste_Liquido.txtAccionAjuste.getText());

        String cantidadStr = Ajuste_Liquido.txtCantidad.getText().trim().replace(" ", "");
        int cantidadAjuste = Integer.parseInt(cantidadStr);

        mv.setCantidad(cantidadAjuste);
        mv.setDetalle(Ajuste_Liquido.txtDetalle.getText());

        Inventario producto = (Inventario) Ajuste_Liquido.jComboBoxProductoLiquidoAjuste.getSelectedItem();
        int idProducto = producto.getIdProducto();
        int idInventario = producto.getIdinventario();

        cuenta cuentaAjuste = (cuenta) Ajuste_Liquido.jComboBoxCuentaInventario.getSelectedItem();

        String sql = """
            INSERT INTO movimiento
            (idProducto, id_inventario, Tipo_Producto, Movimiento, Cantidad, Usuario, Detalle, id_cuenta)
            VALUES (?,?,?,?,?,?,?,?)
            """;

        this.conectar();
        ps = con.prepareStatement(sql);
        ps.setInt(1, idProducto);
        ps.setInt(2, idInventario);
        ps.setString(3, mv.getTipoProducto());
        ps.setString(4, mv.getTipoMovimiento());
        ps.setInt(5, mv.getCantidad());
        ps.setString(6, Usuario.usuarioActual);
        ps.setString(7, mv.getDetalle());
        ps.setInt(8, cuentaAjuste.getIdCuenta());

        ps.executeUpdate();

        JOptionPane.showMessageDialog(null, "Ajuste realizado correctamente");
        Ajuste_Liquido.txtCantidad.setText("");
        Ajuste_Liquido.txtDetalle.setText("");
    }

    // ====================== HISTORIAL ======================
    public void MostrarMovimientoLiquido() throws ClassNotFoundException, SQLException {
        TituloInventarioMovimientoLiquido();
        limpiarTabla(modeloMovientoLiquido);

        ArrayList<Movimiento_inventario> lista = ListaDeMovimientoLIQUIDO();

        Object[] obj = new Object[9];
        for (Movimiento_inventario mvt : lista) {
            obj[0] = mvt.getIdMovimiento();
            obj[1] = mvt.getIdProducto();
            obj[2] = mvt.getTipoProducto();
            obj[3] = mvt.getTipoMovimiento();
            obj[4] = mvt.getCantidad();
            obj[5] = mvt.getStockFinal();
            obj[6] = mvt.getUsuario();
            obj[7] = mvt.getDetalle();
            obj[8] = mvt.getFechaMovimiento();
            modeloMovientoLiquido.addRow(obj);
        }

        InventarioLiquido.TablaHistorial.setModel(modeloMovientoLiquido);
        centrarTextoTabla(InventarioLiquido.TablaHistorial);
    }

    private ArrayList<Movimiento_inventario> ListaDeMovimientoLIQUIDO() throws ClassNotFoundException, SQLException {
        ArrayList<Movimiento_inventario> lista = new ArrayList<>();

        String sql = """
            SELECT m.ID, p.nombre AS nombreProducto, m.Tipo_Producto, m.Movimiento,
                   m.Cantidad, m.StockFinal, m.Usuario, m.Detalle, m.FechaMovimiento
            FROM movimiento m
            INNER JOIN producto p ON p.idProducto = m.idProducto
            WHERE m.Tipo_Producto = 'LIQUIDO'
            ORDER BY m.ID DESC
            """;

        try {
            this.conectar();
            ps = con.prepareStatement(sql);
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
                lista.add(mvt);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar historial: " + e.getMessage());
        } finally {
            this.cerrarCn();
        }
        return lista;
    }

    public void agregarFiltroBusquedaLiquidoHistario() {
        if (InventarioLiquido.TablaHistorial != null && InventarioLiquido.txtBuscarHistorial != null) {
            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modeloMovientoLiquido);
            InventarioLiquido.TablaHistorial.setRowSorter(sorter);

            InventarioLiquido.txtBuscarHistorial.getDocument().addDocumentListener(new DocumentListener() {
                public void insertUpdate(DocumentEvent e) { filtrar(sorter); }
                public void removeUpdate(DocumentEvent e) { filtrar(sorter); }
                public void changedUpdate(DocumentEvent e) { filtrar(sorter); }

                private void filtrar(TableRowSorter<DefaultTableModel> sorter) {
                    String texto = InventarioLiquido.txtBuscarHistorial.getText().trim();
                    if (texto.isEmpty()) {
                        sorter.setRowFilter(null);
                    } else {
                        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(texto)));
                    }
                }
            });
        }
    }
}