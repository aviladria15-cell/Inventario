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

public class Reportes_de_proveedores {

    public void generarReporteProveedoresPDF(String fechaDesde, String fechaHasta) {
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
                    prov.nombre AS proveedor,
                    p.nombre AS producto,
                    c.nombre AS categoria,
                    i.precio_unitario AS precio_promedio,
                    SUM(mov.Cantidad) AS cantidad_total,
                    SUM(mov.Cantidad * i.precio_unitario) AS total_pagado
                FROM movimiento mov
                JOIN inventario i ON mov.id_inventario = i.id_inventario
                JOIN producto p ON i.idProducto = p.idProducto
                JOIN categoria c ON p.idCategoria = c.idCategoria
                JOIN proveedor prov ON p.idProveedor = prov.idProveedor
                WHERE mov.Movimiento = 'ENTRADA'
                  AND DATE(mov.FechaMovimiento) BETWEEN ? AND ?
                GROUP BY prov.idProveedor, p.idProducto
                ORDER BY total_pagado DESC
                """;

            PreparedStatement ps = conexion.getConnection().prepareStatement(sql);
            ps.setString(1, fechaDesde);
            ps.setString(2, fechaHasta);
            ResultSet rs = ps.executeQuery();

            java.util.List<Object[]> filas = new java.util.ArrayList<>();
            double granTotalPagado = 0;

            while (rs.next()) {
                double pagado = rs.getDouble("total_pagado");
                granTotalPagado += pagado;

                filas.add(new Object[]{
                    rs.getString("proveedor"),
                    rs.getString("producto"),
                    rs.getString("categoria"),
                    rs.getDouble("precio_promedio"),
                    rs.getLong("cantidad_total"),
                    pagado
                });
            }
            rs.close();
            ps.close();

            if (filas.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No hay compras en este período.", "Sin datos", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // === GRÁFICA ===
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            for (Object[] f : filas) {
                String prov = ((String) f[0]).length() > 18 ? ((String) f[0]).substring(0, 18) + "..." : (String) f[0];
                dataset.addValue((Double) f[5], "Total Pagado ($)", prov);
            }

            JFreeChart chart = ChartFactory.createBarChart(
                "Compras por Proveedor", "Proveedor", "Total Pagado ($)", dataset
            );
            CategoryPlot plot = chart.getCategoryPlot();
            BarRenderer renderer = (BarRenderer) plot.getRenderer();
            renderer.setSeriesPaint(0, new Color(52, 152, 219));

            BufferedImage chartImage = chart.createBufferedImage(1000, 400);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(chartImage, "png", baos);
            byte[] imageBytes = baos.toByteArray();
            baos.close();

            // === PDF ===
            JFileChooser guardar = new JFileChooser();
            guardar.setSelectedFile(new File("Reporte_Proveedores_" + fechaDesde + "_al_" + fechaHasta + ".pdf"));
            if (guardar.showSaveDialog(null) != JFileChooser.APPROVE_OPTION) return;

            String ruta = guardar.getSelectedFile().getAbsolutePath();
            if (!ruta.toLowerCase().endsWith(".pdf")) ruta += ".pdf";

            document = new PDDocument();
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);
            PDPageContentStream content = new PDPageContentStream(document, page);

            float margin = 35;
            float y = 780;

            // TÍTULO
            content.beginText();
            content.setFont(PDType1Font.HELVETICA_BOLD, 22);
            content.newLineAtOffset(margin, y);
            content.showText("REPORTE DE PROVEEDORES");
            content.endText();
            y -= 35;

            content.beginText();
            content.setFont(PDType1Font.HELVETICA, 12);
            content.newLineAtOffset(margin, y);
            content.showText("Período: " + fechaDesde + " al " + fechaHasta);
            content.newLineAtOffset(0, -18);
            content.showText("Generado: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a")));
            content.endText();
            y -= 60;

            // GRÁFICA
            PDImageXObject pdImage = PDImageXObject.createFromByteArray(document, imageBytes, "grafica");
            content.drawImage(pdImage, margin, y - 320, 515, 320);
            y -= 350;

            // TABLA PERFECTA (6 columnas bien alineadas)
            float tableWidth = 530;
            float rowHeight = 20;

            // Encabezado
            content.setNonStrokingColor(new Color(41, 128, 185));
            content.addRect(margin, y, tableWidth, rowHeight);
            content.fill();
            content.setNonStrokingColor(Color.WHITE);
            content.beginText();
            content.setFont(PDType1Font.HELVETICA_BOLD, 10);
            content.newLineAtOffset(margin + 8, y + 5);
            content.showText("PROVEEDOR               PRODUCTO                  CATEGORÍA        PRECIO   CANTIDAD   TOTAL PAGADO");
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

                if (alternar) content.setNonStrokingColor(new Color(245, 245, 245));
                else content.setNonStrokingColor(Color.WHITE);
                content.addRect(margin, y, tableWidth, rowHeight);
                content.fill();

                content.setNonStrokingColor(Color.BLACK);
                content.beginText();
                content.setFont(PDType1Font.HELVETICA, 9);
                content.newLineAtOffset(margin + 8, y + 5);
                content.showText(String.format("%-23s %-30s %-18s %-9.2f %-10d $%,.2f",
                    truncar((String)f[0], 23),
                    truncar((String)f[1], 30),
                    truncar((String)f[2], 18),
                    f[3], f[4], f[5]));
                content.endText();

                y -= rowHeight;
                alternar = !alternar;
            }

            // TOTAL
            content.beginText();
            content.setFont(PDType1Font.HELVETICA_BOLD, 16);
            content.newLineAtOffset(margin, y - 40);
            content.showText("TOTAL PAGADO EN EL PERÍODO: $" + String.format("%,.2f", granTotalPagado));
            content.endText();

            content.close();
            document.save(ruta);
            document.close();

            JOptionPane.showMessageDialog(null,
                "Reporte de Proveedores generado!\n" + ruta,
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
            generarReporteProveedoresPDF(desde.trim(), hasta.trim());
        }
    }
}