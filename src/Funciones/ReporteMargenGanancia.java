package Funciones;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import javax.swing.*;
import Modelo.ConexiónBD;
import java.util.Date;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.awt.Dimension;           // ← ESTE ES EL QUE FALTABA
import javax.swing.JEditorPane;     // ← ESTE TAMBIÉN
import javax.swing.JScrollPane;     // ← Y ESTE
import javax.swing.JOptionPane;

public class ReporteMargenGanancia {

    // ===================================================================
    // MÉTODO ESTÁTICO PRINCIPAL - ¡¡SÓLO ESTO LLAMAS EN EL BOTÓN!!
    // ===================================================================
    public static void mostrar() {
        new ReporteMargenGanancia().mostrarReporte();
    }

    // Constructor privado: crea su propia conexión
    private ReporteMargenGanancia() {}
private void mostrarReporte() {
    ConexiónBD conexion = new ConexiónBD();
    try {
        conexion.conectar();
        if (!conexion.estaConectado()) {
            JOptionPane.showMessageDialog(null, "No hay conexión a la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 1. OBTENER EL COSTO REAL DEL INVENTARIO DESDE LA CUENTA CONTABLE "INVENTARIO"
        String sqlCostoContable = """
            SELECT COALESCE(saldo_final, 0) AS costo_inventario_contable
            FROM libro_mayor lm
            JOIN cuenta c ON lm.id_cuenta = c.id_cuenta
            WHERE c.nombre = 'Inventario'
            ORDER BY lm.fecha DESC, lm.id_libro_mayor DESC
            LIMIT 1
            """;

        BigDecimal costoTotal = BigDecimal.ZERO;
        try (PreparedStatement ps1 = conexion.getConnection().prepareStatement(sqlCostoContable);
             ResultSet rs1 = ps1.executeQuery()) {
            if (rs1.next()) {
                costoTotal = rs1.getBigDecimal("costo_inventario_contable");
            }
        }

        // 2. SI NO HAY REGISTRO EN CONTABILIDAD (PRIMERA VEZ), USAR SUMA DE LA TABLA
        if (costoTotal.compareTo(BigDecimal.ZERO) == 0) {
            String sqlTabla = "SELECT COALESCE(SUM(costo_total), 0) FROM inventario WHERE Cantidad_Disponible > 0";
            try (PreparedStatement ps2 = conexion.getConnection().prepareStatement(sqlTabla);
                 ResultSet rs2 = ps2.executeQuery()) {
                if (rs2.next()) {
                    costoTotal = rs2.getBigDecimal(1);
                }
            }
        }

        // 3. OBTENER VALOR DE VENTA, UNIDADES Y LOTES
        String sqlDatos = """
            SELECT 
                COALESCE(SUM(CASE WHEN precio_venta > 0 THEN Cantidad_Disponible * precio_venta ELSE 0 END), 0) AS valor_venta,
                COALESCE(SUM(Cantidad_Disponible), 0) AS unidades,
                COUNT(*) AS lotes
            FROM inventario 
            WHERE Cantidad_Disponible > 0
            """;

        BigDecimal valorVentaTotal = BigDecimal.ZERO;
        long totalUnidades = 0;
        int totalLotes = 0;

        try (PreparedStatement ps = conexion.getConnection().prepareStatement(sqlDatos);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                valorVentaTotal = rs.getBigDecimal("valor_venta");
                totalUnidades = rs.getLong("unidades");
                totalLotes = rs.getInt("lotes");
            }
        }

        // 4. CÁLCULOS FINALES
        BigDecimal gananciaBruta = valorVentaTotal.subtract(costoTotal);
        BigDecimal margenPorcentaje = BigDecimal.ZERO;
        if (costoTotal.compareTo(BigDecimal.ZERO) > 0) {
            margenPorcentaje = gananciaBruta.divide(costoTotal, 6, RoundingMode.HALF_UP)
                                           .multiply(BigDecimal.valueOf(100));
        }

        String colorMargen = margenPorcentaje.compareTo(new BigDecimal("50")) >= 0 ? "#27AE60" :
                            margenPorcentaje.compareTo(new BigDecimal("30")) >= 0 ? "#F39C12" : "#E74C3C";

        String fecha = java.time.LocalDateTime.now()
                       .format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy - hh:mm a"));

        // 5. HTML EXACTO COMO TÚ LO QUERÍAS (TU ESTILO ORIGINAL)
        String mensajeHTML = String.format("""
            <html>
            <body style="font-family: Segoe UI, Arial; text-align: center; padding: 20px; line-height: 1.6; background: #f8fafc;">
                <h2 style="color: #2E86C1; margin: 0; font-size: 28px;">MARGEN DE GANANCIA GLOBAL</h2>
                <p style="color: #7F8C8D; font-size: 13px; margin: 8px 0 15px;">
                    Si vendieras <b>TODO</b> el inventario al precio actual
                </p>
                <hr style="border: 1px solid #BDC3C7; margin: 15px 0;">
               
                <div style="font-size: 16px; color: #2C3E50;">
                    <div><b>Unidades en stock:</b> <span style="color:#2C3E50; font-weight: bold;">%,d</span> (en %d lotes)</div>
                    
                    <div style="margin: 18px 0;">
                        <b>Costo total inventario:</b><br>
                        <span style="color: #E74C3C; font-size: 22px; font-weight: bold;">$ %,15.2f</span>
                    </div>
                    
                    <div style="margin: 18px 0;">
                        <b>Valor de venta total:</b><br>
                        <span style="color: #27AE60; font-size: 22px; font-weight: bold;">$ %,15.2f</span>
                    </div>
                    
                    <hr style="border: 1px dashed #BDC3C7; margin: 25px 0;">
                    
                    <div style="margin: 20px 0;">
                        <b style="font-size: 18px;">Ganancia bruta esperada:</b><br>
                        <span style="color: #27AE60; font-size: 28px; font-weight: bold;">$ %,15.2f</span>
                    </div>
                    
                    <div style="margin: 30px 0; padding: 20px; background: #f8f9fa; border-radius: 16px; box-shadow: 0 4px 15px rgba(0,0,0,0.1);">
                        <b style="font-size: 20px; color: #34495e;">Margen de ganancia:</b><br>
                        <span style="font-size: 52px; color: %s; font-weight: bold;">%.2f%%</span>
                    </div>
                </div>
               
                <br><br>
                <small style="color: #95A5A6; font-size: 12px;">Reporte generado el %s</small>
            </body>
            </html>
            """,
            totalUnidades, totalLotes,
            costoTotal,
            valorVentaTotal,
            gananciaBruta,
            colorMargen, margenPorcentaje,
            fecha
        );

        // 6. MOSTRAR EN VENTANA MEDIANA Y BONITA
        JEditorPane editor = new JEditorPane("text/html", mensajeHTML);
        editor.setEditable(false);
        editor.setOpaque(false);

        JScrollPane scroll = new JScrollPane(editor);
        scroll.setPreferredSize(new Dimension(680, 620));

        JOptionPane.showMessageDialog(
            null,
            scroll,
            "Margen de Ganancia Global",
            JOptionPane.INFORMATION_MESSAGE
        );

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error al generar reporte:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    } finally {
        try { if (conexion != null) conexion.cerrarCn(); } catch (Exception ignored) {}
    }
}
}



