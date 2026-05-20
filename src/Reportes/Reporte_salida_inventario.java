package Reportes;

import Modelo.ConexiónBD;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Reporte_salida_inventario {

    public void generarReporteSalidasPDF(String fechaDesde, String fechaHasta) {
        ConexiónBD conexion = new ConexiónBD();
        PDDocument document = null;
        try {
            conexion.conectar();
            if (!conexion.estaConectado()) {
                JOptionPane.showMessageDialog(null, "No hay conexión.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // CONSULTA 100% COMPATIBLE CON TU BASE DE DATOS ACTUAL
            String sql = """
                SELECT 
                    DATE(m.FechaMovimiento) AS fecha,
                    m.Movimiento AS tipo_salida,
                    COALESCE(m.Detalle, 'Sin descripción') AS destino,
                    p.nombre AS producto,
                    m.Cantidad AS cantidad,
                    i.precio_unitario AS costo_unitario,
                    COALESCE(i.precio_venta, i.precio_unitario) AS precio_venta,
                    (m.Cantidad * i.precio_unitario) AS costo_total,
                    (m.Cantidad * COALESCE(i.precio_venta, i.precio_unitario)) AS valor_venta,
                    m.Usuario AS usuario
                FROM movimiento m
                JOIN inventario i ON m.id_inventario = i.id_inventario
                JOIN producto p ON i.idProducto = p.idProducto
                WHERE m.Movimiento = 'SALIDA'
                  AND DATE(m.FechaMovimiento) BETWEEN ? AND ?
                ORDER BY m.FechaMovimiento DESC, m.ID DESC
                """;

            PreparedStatement ps = conexion.getConnection().prepareStatement(sql);
            ps.setString(1, fechaDesde);
            ps.setString(2, fechaHasta);
            ResultSet rs = ps.executeQuery();

            java.util.List<Object[]> filas = new java.util.ArrayList<>();
            double totalCosto = 0;
            double totalVenta = 0;
            int totalSalidas = 0;

            while (rs.next()) {
                double costo = rs.getDouble("costo_total");
                double venta = rs.getDouble("valor_venta");
                totalCosto += costo;
                totalVenta += venta;
                totalSalidas++;

                filas.add(new Object[]{
                    rs.getString("fecha"),
                    rs.getString("destino"),
                    rs.getString("producto"),
                    rs.getInt("cantidad"),
                    rs.getDouble("costo_unitario"),
                    rs.getDouble("precio_venta"),
                    rs.getString("usuario")
                });
            }
            rs.close();
            ps.close();

            if (filas.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No hay salidas en este período.", "Sin datos", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // === GRÁFICA: SALIDAS POR MOTIVO (usando el campo Detalle) ===
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            java.util.Map<String, Integer> porMotivo = new java.util.HashMap<>();
            for (Object[] f : filas) {
                String motivo = ((String) f[1]).length() > 20 ? ((String) f[1]).substring(0, 20) + "..." : (String) f[1];
                int cant = (Integer) f[3];
                porMotivo.put(motivo, porMotivo.getOrDefault(motivo, 0) + cant);
            }
            porMotivo.entrySet().stream()
                .sorted(java.util.Map.Entry.<String, Integer>comparingByValue().reversed())
                .forEach(e -> dataset.addValue(e.getValue(), "Cantidad", e.getKey()));

            JFreeChart chart = ChartFactory.createBarChart(
                "Salidas por Motivo", "Motivo", "Cantidad", dataset
            );
            CategoryPlot plot = chart.getCategoryPlot();
            BarRenderer renderer = (BarRenderer) plot.getRenderer();
            renderer.setSeriesPaint(0, new Color(231, 76, 60));

            BufferedImage chartImage = chart.createBufferedImage(1000, 400);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(chartImage, "png", baos);
            byte[] imageBytes = baos.toByteArray();
            baos.close();

            // === PDF ===
            JFileChooser guardar = new JFileChooser();
            guardar.setSelectedFile(new File("Reporte_Salidas_" + fechaDesde + "_al_" + fechaHasta + ".pdf"));
            if (guardar.showSaveDialog(null) != JFileChooser.APPROVE_OPTION) return;

            String ruta = guardar.getSelectedFile().getAbsolutePath();
            if (!ruta.toLowerCase().endsWith(".pdf")) ruta += ".pdf";

            document = new PDDocument();
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);
            PDPageContentStream content = new PDPageContentStream(document, page);

            float margin = 40;
            float y = 780;

            // TÍTULO
            content.beginText();
            content.setFont(PDType1Font.HELVETICA_BOLD, 22);
            content.newLineAtOffset(margin, y);
            content.showText("REPORTE DE SALIDAS DEL INVENTARIO");
            content.endText();
            y -= 40;

            content.beginText();
            content.setFont(PDType1Font.HELVETICA, 12);
            content.newLineAtOffset(margin, y);
            content.showText("Período: " + fechaDesde + " al " + fechaHasta);
            content.newLineAtOffset(0, -20);
            content.showText("Generado: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a")));
            content.endText();
            y -= 60;

            // GRÁFICA
            PDImageXObject pdImage = PDImageXObject.createFromByteArray(document, imageBytes, "grafica");
            content.drawImage(pdImage, margin, y - 320, 515, 320);
            y -= 350;

            // TABLA
            float tableWidth = 520;
            float rowHeight = 22;

            // Encabezado
            content.setNonStrokingColor(new Color(192, 57, 43));
            content.addRect(margin, y, tableWidth, rowHeight);
            content.fill();
            content.setNonStrokingColor(Color.WHITE);
            content.beginText();
            content.setFont(PDType1Font.HELVETICA_BOLD, 10);
            content.newLineAtOffset(margin + 5, y + 5);
            content.showText("FECHA       DESTINO/DOCUMENTO      PRODUCTO                 CANT.   COSTO U.   PRECIO V.   USUARIO");
            content.endText();
            y -= rowHeight;

            boolean alternar = false;
            for (Object[] f : filas) {
                if (y < 100) {
                    content.close();
                    page = new PDPage(PDRectangle.A4);
                    document.addPage(page);
                    content = new PDPageContentStream(document, page);
                    y = 750;
                }

                if (alternar) content.setNonStrokingColor(new Color(250, 230, 230));
                else content.setNonStrokingColor(Color.WHITE);
                content.addRect(margin, y, tableWidth, rowHeight);
                content.fill();

                content.setNonStrokingColor(Color.BLACK);
                content.beginText();
                content.setFont(PDType1Font.HELVETICA, 9);
                content.newLineAtOffset(margin + 5, y + 5);
                content.showText(String.format("%-12s %-25s %-30s %-8d %-11.2f %-11.2f %-15s",
                    f[0],
                    truncar((String)f[1], 25),
                    truncar((String)f[2], 30),
                    f[3], f[4], f[5],
                    truncar((String)f[6], 15)));
                content.endText();

                y -= rowHeight;
                alternar = !alternar;
            }

            // TOTALES
            content.beginText();
            content.setFont(PDType1Font.HELVETICA_BOLD, 16);
            content.newLineAtOffset(margin, y - 40);
            content.showText("TOTAL SALIDAS: " + totalSalidas);
            content.newLineAtOffset(0, -25);
            content.showText("COSTO TOTAL: $" + String.format("%,.2f", totalCosto));
            content.newLineAtOffset(0, -20);
            content.showText("VALOR VENTA: $" + String.format("%,.2f", totalVenta));
            content.newLineAtOffset(0, -20);
            content.showText("GANANCIA: $" + String.format("%,.2f", totalVenta - totalCosto));
            content.endText();

            content.close();
            document.save(ruta);
            document.close();

            JOptionPane.showMessageDialog(null, 
                "Reporte de Salidas generado correctamente!\n" + ruta,
                "Éxito", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } finally {
            try { if (document != null) document.close(); } catch (Exception ignored) {}
            try { conexion.cerrarCn(); } catch (Exception ignored) {}
        }
    }

    private String truncar(String s, int len) {
        if (s == null) return "";
        return s.length() > len ? s.substring(0, len - 3) + "..." : s;
    }

    public void mostrar() {
        String desde = JOptionPane.showInputDialog(null, "Fecha desde (yyyy-MM-dd):", "2025-01-01");
        String hasta = JOptionPane.showInputDialog(null, "Fecha hasta (yyyy-MM-dd):", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        if (desde != null && hasta != null && !desde.trim().isEmpty() && !hasta.trim().isEmpty()) {
            generarReporteSalidasPDF(desde.trim(), hasta.trim());
        }
    }
}