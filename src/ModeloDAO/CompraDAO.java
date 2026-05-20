/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ModeloDAO;

import Modelo.ConexiónBD;
import Modelo.Compra;
import Modelo.cuenta; // Asegúrate de que coincida exactamente con el nombre de tu archivo cuenta.java
import Vista_Gestionar_Proveedor.Vista_Pagar_Compra;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author avila
 */
public class CompraDAO extends ConexiónBD {

    DefaultTableModel modeloPagarCuenta = new DefaultTableModel();

    PreparedStatement ps;
    ResultSet rs;

    Compra c = new Compra();

    public void TituloCompra() {
        String Titulo[] = {" # Inventario ", " # Factura ", "Proveedor", "Producto", "Tot. a Pagar", "Abono", "Debe", "Estado", "Forma de Pago", "Cuenta Ori", "Cuenta Des"};
        modeloPagarCuenta.setColumnIdentifiers(Titulo);
        Vista_Pagar_Compra.TablaPagarDeudas.setModel(modeloPagarCuenta);
        centrarTextoTabla(Vista_Pagar_Compra.TablaPagarDeudas);
    }

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

    public void registrarPago() {
        int fila = Vista_Pagar_Compra.TablaPagarDeudas.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "Debe seleccionar una deuda", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (Vista_Pagar_Compra.jComboBoxCuenta.getSelectedItem() == null ||
            Vista_Pagar_Compra.jComboBoxCuentaDestino.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(null, "Debe seleccionar Cuenta Origen y Cuenta Destino", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (Vista_Pagar_Compra.txtABono.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar un monto a abonar", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            this.conectar();

            int idInventario = Integer.parseInt(Vista_Pagar_Compra.TablaPagarDeudas.getValueAt(fila, 0).toString());
            String numFactura = Vista_Pagar_Compra.TablaPagarDeudas.getValueAt(fila, 1).toString();

            // Evitar duplicados si ya está totalmente liquidada
            if (yaTienePagoRegistrado(idInventario, numFactura)) {
                JOptionPane.showMessageDialog(null, "Esta factura ya se encuentra cancelada por completo.", "Información", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // CORRECCIÓN: El monto del pago debe ser lo que el usuario digita en la vista para abonar.
            double montoPago = Double.parseDouble(Vista_Pagar_Compra.txtABono.getText().trim());
            double totalPagar = Double.parseDouble(Vista_Pagar_Compra.TablaPagarDeudas.getValueAt(fila, 4).toString());
            double abonoAnterior = Double.parseDouble(Vista_Pagar_Compra.TablaPagarDeudas.getValueAt(fila, 5).toString());
            double saldoPendiente = totalPagar - abonoAnterior;

            if (montoPago <= 0) {
                JOptionPane.showMessageDialog(null, "El monto a abonar debe ser mayor a 0", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (montoPago > saldoPendiente) {
                JOptionPane.showMessageDialog(null, "El abono no puede ser mayor al saldo pendiente ($" + saldoPendiente + ")", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // === Cuentas ===
            cuenta cuentaOrigen = (cuenta) Vista_Pagar_Compra.jComboBoxCuenta.getSelectedItem();
            cuenta cuentaDestino = (cuenta) Vista_Pagar_Compra.jComboBoxCuentaDestino.getSelectedItem();

            int idCuentaOrigen = cuentaOrigen.getIdCuenta();
            int idCuentaDestino = cuentaDestino.getIdCuenta();

            // === Registrar Asiento Contable ===
            int idAsiento = crearAsientoContable(numFactura, montoPago, cuentaOrigen.getNombre(), cuentaDestino.getNombre());

            // === Libro Diario (Partida Doble) ===
            registrarLibroDiario(idAsiento, idCuentaDestino, montoPago, 0.0); // Debe → Destino
            registrarLibroDiario(idAsiento, idCuentaOrigen, 0.0, montoPago);   // Haber → Origen

            // === Actualizar saldos en Cuentas ===
            actualizarSaldoCuenta(idCuentaDestino, montoPago, true);   // Debe
            actualizarSaldoCuenta(idCuentaOrigen, montoPago, false);   // Haber

            // === Actualizar tabla compra y obtener estado (CORREGIDO: Llamado único) ===
            String estadoFinal = actualizarDeudaCompra(idInventario, numFactura, montoPago, idCuentaOrigen, idCuentaDestino);

            JOptionPane.showMessageDialog(null,
                "¡Pago registrado correctamente!\n\n" +
                "Debe: " + cuentaDestino.getNombre() + " → $" + montoPago + "\n" +
                "Haber: " + cuentaOrigen.getNombre() + " → $" + montoPago + "\n\n" +
                "Estado de Factura: " + estadoFinal,
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
            
            // Recargar la tabla en la interfaz para ver cambios reflejados
            MostrarDeudas();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al registrar pago:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ====================== MÉTODOS AUXILIARES ======================

    private int crearAsientoContable(String numFactura, double monto, String origen, String destino) throws SQLException {
        String descripcion = "Pago factura " + numFactura + " | " + origen + " → " + destino;

        String sql = """
            INSERT INTO asiento_contable (fecha, descripcion, referencia, total_debe, total_haber)
            VALUES (CURDATE(), ?, ?, ?, ?)
            """;

        ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, descripcion);
        ps.setString(2, "FAC-" + numFactura);
        ps.setDouble(3, monto);
        ps.setDouble(4, monto);

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
        String sql = "UPDATE cuenta SET saldo_inicial = saldo_inicial + ? WHERE id_cuenta = ?";
        ps = con.prepareStatement(sql);

        double valor = esDebe ? monto : -monto;
        ps.setDouble(1, valor);
        ps.setInt(2, idCuenta);
        ps.executeUpdate();

        registrarEnLibroMayor(idCuenta, monto, esDebe);
    }

    private void registrarEnLibroMayor(int idCuenta, double monto, boolean esDebe) throws SQLException {
        String sql = """
            INSERT INTO libro_mayor (id_cuenta, fecha, saldo_anterior, debe, haber, saldo_final)
            SELECT id_cuenta, CURDATE(), saldo_inicial, ?, ?, saldo_inicial
            FROM cuenta WHERE id_cuenta = ?
            """;

        ps = con.prepareStatement(sql);
        if (esDebe) {
            ps.setDouble(1, monto);
            ps.setDouble(2, 0.0);
        } else {
            ps.setDouble(1, 0.0);
            ps.setDouble(2, monto);
        }
        ps.setInt(3, idCuenta);
        ps.executeUpdate();
    }

    private String actualizarDeudaCompra(int idInventario, String numFactura, double montoPago, 
                                         int idCuentaOrigen, int idCuentaDestino) throws SQLException {
        
        String formaPago = (String) Vista_Pagar_Compra.jComboBoxFormaPago.getSelectedItem();
        
        int idProveedor = obtenerIdProveedorDesdeInventario(idInventario);
        int idProducto = obtenerIdProductoDesdeInventario(idInventario);
        double totalPagar = obtenerTotalPagarDesdeInventario(idInventario);

        // CORRECCIÓN: El nuevo abono es la suma de lo que ya había en la base de datos + el nuevo pago realizado
        double abonoAnterior = obtenerAbonoActual(idInventario, numFactura);
        double nuevoAbonoAcumulado = abonoAnterior + montoPago;
        double nuevoDebe = totalPagar - nuevoAbonoAcumulado;

        // Determinar estado de forma precisa basándose en el acumulado total
        String estadoAutomatico;
        if (nuevoAbonoAcumulado >= totalPagar) {
            estadoAutomatico = "Pagada";
            nuevoDebe = 0; // Evitar residuo negativo por decimales
        } else if (nuevoAbonoAcumulado > 0) {
            estadoAutomatico = "Parcial";
        } else {
            estadoAutomatico = "Pendiente";
        }

        // Verificar si la compra ya existe en base de datos
        String sqlCheck = "SELECT id_compras FROM compra WHERE id_inventario = ? AND numero_factura = ?";
        ps = con.prepareStatement(sqlCheck);
        ps.setInt(1, idInventario);
        ps.setString(2, numFactura);
        rs = ps.executeQuery();

        if (rs.next()) {
            // ACTUALIZAR REGISTRO EXISTENTE
            String sql = """
                UPDATE compra
                SET abono = ?,
                    debe = ?,
                    estado = ?,
                    forma_pago = ?,
                    id_cuenta = ?,
                    id_cuenta_destino = ?,
                    fecha_pago = CURDATE()
                WHERE id_inventario = ? AND numero_factura = ?
                """;
            
            ps = con.prepareStatement(sql);
            ps.setDouble(1, nuevoAbonoAcumulado);
            ps.setDouble(2, nuevoDebe);
            ps.setString(3, estadoAutomatico);
            ps.setString(4, formaPago);
            ps.setInt(5, idCuentaOrigen);
            ps.setInt(6, idCuentaDestino);
            ps.setInt(7, idInventario);
            ps.setString(8, numFactura);
            
        } else {
            // INSERTAR REGISTRO NUEVO
            String sql = """
                INSERT INTO compra
                (id_inventario, numero_factura, idProveedor, idProducto, total_pagar,
                 abono, debe, estado, id_cuenta, id_cuenta_destino, forma_pago, fecha_pago)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURDATE())
                """;
            
            ps = con.prepareStatement(sql);
            ps.setInt(1, idInventario);
            ps.setString(2, numFactura);
            ps.setInt(3, idProveedor);
            ps.setInt(4, idProducto);
            ps.setDouble(5, totalPagar);
            ps.setDouble(6, nuevoAbonoAcumulado);
            ps.setDouble(7, nuevoDebe);
            ps.setString(8, estadoAutomatico);
            ps.setInt(9, idCuentaOrigen);
            ps.setInt(10, idCuentaDestino);
            ps.setString(11, formaPago);
        }
        
        ps.executeUpdate();
        return estadoAutomatico; 
    }
      
    private double obtenerAbonoActual(int idInventario, String numFactura) throws SQLException {
        String sql = """
            SELECT COALESCE(abono, 0) AS abono_actual 
            FROM compra 
            WHERE id_inventario = ? AND numero_factura = ?
            """;
        ps = con.prepareStatement(sql);
        ps.setInt(1, idInventario);
        ps.setString(2, numFactura);
        rs = ps.executeQuery();
        
        return rs.next() ? rs.getDouble("abono_actual") : 0.0;
    }

    private int obtenerIdProveedorDesdeInventario(int idInventario) throws SQLException {
        String sql = "SELECT idProveedor FROM inventario WHERE id_inventario = ?";
        ps = con.prepareStatement(sql);
        ps.setInt(1, idInventario);
        rs = ps.executeQuery();
        return rs.next() ? rs.getInt("idProveedor") : 0;
    }

    private int obtenerIdProductoDesdeInventario(int idInventario) throws SQLException {
        String sql = "SELECT idProducto FROM inventario WHERE id_inventario = ?";
        ps = con.prepareStatement(sql);
        ps.setInt(1, idInventario);
        rs = ps.executeQuery();
        return rs.next() ? rs.getInt("idProducto") : 0;
    }

    private double obtenerTotalPagarDesdeInventario(int idInventario) throws SQLException {
        String sql = "SELECT costo_total FROM inventario WHERE id_inventario = ?";
        ps = con.prepareStatement(sql);
        ps.setInt(1, idInventario);
        rs = ps.executeQuery();
        return rs.next() ? rs.getDouble("costo_total") : 0.0;
    }

    private boolean yaTienePagoRegistrado(int idInventario, String numFactura) throws SQLException {
        // CORRECCIÓN: Se considera pagado si el abono cubre o supera el costo total del inventario
        String sql = """
            SELECT c.id_compras FROM compra c
            INNER JOIN inventario i ON c.id_inventario = i.id_inventario
            WHERE c.id_inventario = ? AND c.numero_factura = ? AND c.abono >= i.costo_total
            """;
        ps = con.prepareStatement(sql);
        ps.setInt(1, idInventario);
        ps.setString(2, numFactura);
        rs = ps.executeQuery();
        return rs.next();
    }

    private ArrayList<Compra> ListaDeudada() throws ClassNotFoundException, SQLException {
        
        ArrayList<Compra> ListaCompras = new ArrayList<>();
        String sql = """
            SELECT
                COALESCE(c.id_compras, 0) AS id_compras,
                COALESCE(cu.nombre, '') AS nombreCuentaOrigen,
                COALESCE(cu2.nombre, '') AS nombreCuentaDestino,
                COALESCE(c.id_cuenta, 0) AS id_cuenta,
                COALESCE(c.id_cuenta_destino, 0) AS id_cuenta_destino,
                i.id_inventario,
                i.numero_Factura,
                prov.nombre AS nombreProveedor,
                prod.nombre AS nombreProducto,
                i.costo_total AS total_pagar,
                COALESCE(c.abono, 0) AS abono,
                (i.costo_total - COALESCE(c.abono, 0)) AS debe,
                CASE
                    WHEN COALESCE(c.abono, 0) = 0 THEN 'Pendiente'
                    WHEN COALESCE(c.abono, 0) > 0 AND COALESCE(c.abono, 0) < i.costo_total THEN 'Parcial'
                    WHEN COALESCE(c.abono, 0) >= i.costo_total THEN 'Pagado'
                END AS estado,
                COALESCE(c.forma_pago, '') AS forma_pago
            FROM inventario i
            LEFT JOIN compra c ON i.id_inventario = c.id_inventario
            LEFT JOIN cuenta cu ON c.id_cuenta = cu.id_cuenta
            LEFT JOIN cuenta cu2 ON c.id_cuenta_destino = cu2.id_cuenta
            INNER JOIN proveedor prov ON i.idProveedor = prov.idProveedor
            INNER JOIN producto prod ON i.idProducto = prod.idProducto
            WHERE i.costo_total > 0 AND (c.abono IS NULL OR c.abono < i.costo_total)
            ORDER BY i.fecha_ingreso DESC;
            """;
        try {
            this.conectar();
            ps = this.con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Compra compra = new Compra();

                compra.setId_Compras(rs.getInt("id_compras"));
                compra.setId_inventario(rs.getInt("id_inventario"));
                compra.setNumero_factura(rs.getString("numero_Factura"));
                compra.setProveedor(rs.getString("nombreProveedor"));
                compra.setIdProducto(rs.getString("nombreProducto"));
                compra.setTotal_pagar(rs.getDouble("total_pagar"));
                compra.setAbono(rs.getDouble("abono"));
                compra.setDebe(rs.getDouble("debe"));
                compra.setEstado(rs.getString("estado"));
                compra.setForma_pago(rs.getString("forma_pago"));
                
                compra.setNombreCuenta(rs.getString("nombreCuentaOrigen"));
                compra.setCuentaDestino(rs.getString("nombreCuentaDestino"));

                ListaCompras.add(compra);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, 
                "Error al obtener lista de deudas:\n" + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        
        return ListaCompras;
    }

    public void MostrarDeudas() throws ClassNotFoundException, SQLException {
        TituloCompra();
        limpiarTabla(modeloPagarCuenta);
        
        ArrayList<Compra> ListaTerminda = ListaDeudada();
        Object[] obj = new Object[11];
        
        for (Compra c : ListaTerminda) {
            obj[0] = c.getId_inventario();
            obj[1] = c.getNumero_factura();
            obj[2] = c.getProveedor();
            obj[3] = c.getIdProducto();
            obj[4] = c.getTotal_pagar();
            obj[5] = c.getAbono();
            obj[6] = c.getDebe();
            obj[7] = c.getEstado();
            obj[8] = c.getForma_pago();
            obj[9] = c.getNombreCuenta();     // Cuenta Origen
            obj[10] = c.getCuentaDestino();   // Cuenta Destino
            
            modeloPagarCuenta.addRow(obj);
        }
        
        Vista_Pagar_Compra.TablaPagarDeudas.setModel(modeloPagarCuenta);
        centrarTextoTabla(Vista_Pagar_Compra.TablaPagarDeudas);
    }
    
    
// =========================================================================
    // MÉTODO PARA EL BOTÓN "ABONAR": Permite elegir libremente la cantidad
    // =========================================================================
    // =========================================================================
    // MÉTODO OPTIMIZADO PARA EL BOTÓN "ABONAR": Cuentas automáticas desde la BD
    // =========================================================================
    public void AhacerAbono() {
        int fila = Vista_Pagar_Compra.TablaPagarDeudas.getSelectedRow();

        // 1. Validar que se haya seleccionado una fila de la tabla
        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "Debe seleccionar una deuda de la tabla para abonar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            this.conectar();

            int idInventario = Integer.parseInt(Vista_Pagar_Compra.TablaPagarDeudas.getValueAt(fila, 0).toString());
            String numFactura = Vista_Pagar_Compra.TablaPagarDeudas.getValueAt(fila, 1).toString();

            // 2. Verificar si la factura ya fue liquidada en su totalidad
            if (yaTienePagoRegistrado(idInventario, numFactura)) {
                JOptionPane.showMessageDialog(null, "Esta factura ya se encuentra cancelada por completo.", "Información", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // 3. CONSULTA AUTOMÁTICA DE CUENTAS: Recuperar las cuentas asociadas previamente a esta compra
            int idCuentaOrigen = 0;
            int idCuentaDestino = 0;
            String nombreCuentaOrigen = "";
            String nombreCuentaDestino = "";

            String sqlCuentas = """
                SELECT c.id_cuenta, c.id_cuenta_destino, cu1.nombre AS nombre_origen, cu2.nombre AS nombre_destino 
                FROM compra c
                LEFT JOIN cuenta cu1 ON c.id_cuenta = cu1.id_cuenta
                LEFT JOIN cuenta cu2 ON c.id_cuenta_destino = cu2.id_cuenta
                WHERE c.id_inventario = ? AND c.numero_factura = ?
                """;
            
            ps = con.prepareStatement(sqlCuentas);
            ps.setInt(1, idInventario);
            ps.setString(2, numFactura);
            rs = ps.executeQuery();

            if (rs.next()) {
                idCuentaOrigen = rs.getInt("id_cuenta");
                idCuentaDestino = rs.getInt("id_cuenta_destino");
                nombreCuentaOrigen = rs.getString("nombre_origen");
                nombreCuentaDestino = rs.getString("nombre_destino");
            }

            // Si es el primer abono y aún no hay cuentas en la tabla 'compra', tomamos de forma segura las del combo por única vez
            if (idCuentaOrigen == 0 || idCuentaDestino == 0) {
                if (Vista_Pagar_Compra.jComboBoxCuenta.getSelectedItem() == null ||
                    Vista_Pagar_Compra.jComboBoxCuentaDestino.getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(null, "Esta deuda no tiene un abono previo registrado.\nPor favor, asigne Cuenta Origen y Destino en los paneles de arriba para iniciar el historial.", "Configuración Requerida", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                cuenta co = (cuenta) Vista_Pagar_Compra.jComboBoxCuenta.getSelectedItem();
                cuenta cd = (cuenta) Vista_Pagar_Compra.jComboBoxCuentaDestino.getSelectedItem();
                idCuentaOrigen = co.getIdCuenta();
                idCuentaDestino = cd.getIdCuenta();
                nombreCuentaOrigen = co.getNombre();
                nombreCuentaDestino = cd.getNombre();
            }

            // 4. Calcular el saldo actual pendiente basado en la tabla
            double totalPagar = Double.parseDouble(Vista_Pagar_Compra.TablaPagarDeudas.getValueAt(fila, 4).toString());
            double abonoAnterior = Double.parseDouble(Vista_Pagar_Compra.TablaPagarDeudas.getValueAt(fila, 5).toString());
            double saldoPendiente = totalPagar - abonoAnterior;

            // 5. Ventana emergente para capturar el dinero a abonar
            String inputMonto = JOptionPane.showInputDialog(null, 
                    "Factura N°: " + numFactura + "\n" +
                    "Cuenta Origen: " + nombreCuentaOrigen + "\n" +
                    "Cuenta Destino: " + nombreCuentaDestino + "\n" +
                    "Saldo Pendiente Actual: $" + saldoPendiente + "\n\n" +
                    "Ingrese la cantidad que desea abonar:", 
                    "Registrar Abono Parcial", JOptionPane.QUESTION_MESSAGE);

            if (inputMonto == null) {
                return; 
            }

            // 6. Validar que la entrada de texto sea un número válido
            double montoAbono;
            try {
                montoAbono = Double.parseDouble(inputMonto.trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Por favor, introduzca una cantidad numérica válida.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 7. Validaciones del negocio
            if (montoAbono <= 0) {
                JOptionPane.showMessageDialog(null, "El monto a abonar debe ser una cantidad mayor a $0.00", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (montoAbono > saldoPendiente) {
                JOptionPane.showMessageDialog(null, "No puede abonar $" + montoAbono + " porque supera el saldo pendiente de esta factura ($" + saldoPendiente + ")", "Límite Excedido", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 8. Procesamiento del Asiento Contable y Libros
            int idAsiento = crearAsientoContable(numFactura, montoAbono, nombreCuentaOrigen, nombreCuentaDestino);
            registrarLibroDiario(idAsiento, idCuentaDestino, montoAbono, 0.0); // Debe
            registrarLibroDiario(idAsiento, idCuentaOrigen, 0.0, montoAbono);   // Haber

            actualizarSaldoCuenta(idCuentaDestino, montoAbono, true);  
            actualizarSaldoCuenta(idCuentaOrigen, montoAbono, false);  

            // 9. Actualizar la base de datos de compras
            String estadoFinal = actualizarDeudaCompra(idInventario, numFactura, montoAbono, idCuentaOrigen, idCuentaDestino);

            // 10. Notificación de éxito
            JOptionPane.showMessageDialog(null,
                "¡Abono registrado con éxito!\n\n" +
                "Detalle de Cuentas Utilizadas automáticamente:\n" +
                "• Cuenta Origen (Haber): " + nombreCuentaOrigen + " → -$" + montoAbono + "\n" +
                "• Cuenta Destino (Debe): " + nombreCuentaDestino + " → +$" + montoAbono + "\n\n" +
                "El nuevo estado de la factura es: [" + estadoFinal + "]",
                "Operación Completada", JOptionPane.INFORMATION_MESSAGE);
            
            // 11. Refrescar la JTable
            MostrarDeudas();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error crítico al procesar el abono:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}