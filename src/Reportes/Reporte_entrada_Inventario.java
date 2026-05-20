/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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

public class Reporte_entrada_Inventario {

    public void generarReporteEntradasPDF(String fechaDesde, String fechaHasta) {
        ConexiónBD conexion = new ConexiónBD();
        PDDocument document = null;
        try {
            conexion.conectar();
            if (!conexion.estaConectado()) {
                JOptionPane.showMessageDialog(null, "No hay conexión.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String sql = """
                SELECT 
                    DATE(m.FechaMovimiento) AS fecha,
                    p.nombre AS proveedor,
                    m.Usuario AS usuario,
                    prod.nombre AS producto,
                    i.precio_unitario AS costo_unitario,
                    m.Cantidad AS cantidad,
                    (m.Cantidad * i.precio_unitario) AS costo_total,
                    m.Movimiento AS tipo_movimiento,
                    m.Detalle AS detalle
                FROM movimiento m
                JOIN inventario i ON m.id_inventario = i.id_inventario
                JOIN producto prod ON i.idProducto = prod.idProducto
                JOIN proveedor p ON prod.idProveedor = p.idProveedor
                WHERE m.Movimiento IN ('ENTRADA', 'AJUSTE')
                  AND m.Cantidad > 0
                  AND DATE(m.FechaMovimiento) BETWEEN ? AND ?
                ORDER BY m.FechaMovimiento DESC, m.ID DESC
                """;

            PreparedStatement ps = conexion.getConnection().prepareStatement(sql);
            ps.setString(1, fechaDesde);
            ps.setString(2, fechaHasta);
            ResultSet rs = ps.executeQuery();

            java.util.List<Object[]> filas = new java.util.ArrayList<>();
            double totalCosto = 0;
            int totalEntradas = 0;
            int totalAjustes = 0;

            while (rs.next()) {
                totalCosto += rs.getDouble("costo_total");
                if ("ENTRADA".equals(rs.getString("tipo_movimiento"))) totalEntradas++;
                else totalAjustes++;

                filas.add(new Object[]{
                    rs.getString("fecha"),
                    rs.getString("proveedor"),
                    rs.getString("usuario"),
                    rs.getString("producto"),
                    rs.getDouble("costo_unitario"),
                    rs.getInt("cantidad"),
                    rs.getDouble("costo_total"),
                    rs.getString("tipo_movimiento"),
                    rs.getString("detalle")
                });
            }
            rs.close();
            ps.close();

            if (filas.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No hay entradas en este período.", "Sin datos", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // === GRÁFICA: CANTIDAD POR PROVEEDOR ===
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            java.util.Map<String, Integer> porProveedor = new java.util.HashMap<>();
            for (Object[] f : filas) {
                String prov = (String) f[1];
                int cant = (Integer) f[5];
                porProveedor.put(prov, porProveedor.getOrDefault(prov, 0) + cant);
            }
            porProveedor.entrySet().stream()
                .sorted(java.util.Map.Entry.<String, Integer>comparingByValue().reversed())
                .forEach(e -> dataset.addValue(e.getValue(), "Cantidad", e.getKey()));

            JFreeChart chart = ChartFactory.createBarChart(
                "Entradas por Proveedor", "Proveedor", "Cantidad", dataset
            );
            CategoryPlot plot = chart.getCategoryPlot();
            BarRenderer renderer = (BarRenderer) plot.getRenderer();
            renderer.setSeriesPaint(0, new Color(34, 153, 84));

            BufferedImage chartImage = chart.createBufferedImage(1000, 400);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(chartImage, "png", baos);
            byte[] imageBytes = baos.toByteArray();
            baos.close();

            // === PDF ===
            JFileChooser guardar = new JFileChooser();
            guardar.setSelectedFile(new File("Reporte_Entradas_" + fechaDesde + "_al_" + fechaHasta + ".pdf"));
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
            content.showText("REPORTE DE ENTRADAS AL INVENTARIO");
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
            content.setNonStrokingColor(new Color(41, 128, 185));
            content.addRect(margin, y, tableWidth, rowHeight);
            content.fill();
            content.setNonStrokingColor(Color.WHITE);
            content.beginText();
            content.setFont(PDType1Font.HELVETICA_BOLD, 10);
            content.newLineAtOffset(margin + 5, y + 5);
            content.showText("FECHA       PROVEEDOR           USUARIO     PRODUCTO                 CANTIDAD   COSTO UNIT.   TOTAL");
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

                if (alternar) content.setNonStrokingColor(new Color(240, 248, 255));
                else content.setNonStrokingColor(Color.WHITE);
                content.addRect(margin, y, tableWidth, rowHeight);
                content.fill();

                content.setNonStrokingColor("AJUSTE".equals(f[7]) ? Color.BLUE : Color.BLACK);

                content.beginText();
                content.setFont(PDType1Font.HELVETICA, 9);
                content.newLineAtOffset(margin + 5, y + 5);
                content.showText(String.format("%-12s %-20s %-12s %-30s %-10d %-12.2f %-12.2f",
                    f[0], truncar((String)f[1], 20),
                    truncar((String)f[2], 12),
                    truncar((String)f[3], 30),
                    f[5], f[4], f[6]));
                content.endText();

                y -= rowHeight;
                alternar = !alternar;
            }

            // TOTALES
            content.beginText();
            content.setFont(PDType1Font.HELVETICA_BOLD, 16);
            content.newLineAtOffset(margin, y - 40);
            content.showText("TOTAL COSTO DE ENTRADAS: $" + String.format("%,.2f", totalCosto));
            content.newLineAtOffset(0, -25);
            content.showText("Entradas por compra: " + totalEntradas + " | Ajustes positivos: " + totalAjustes);
            content.endText();

            content.close();
            document.save(ruta);
            document.close();

            JOptionPane.showMessageDialog(null, 
                "Reporte de Entradas generado!\n" + ruta + "\n\nTotal costo: $" + String.format("%,.2f", totalCosto),
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
            generarReporteEntradasPDF(desde.trim(), hasta.trim());
        }
    }
}