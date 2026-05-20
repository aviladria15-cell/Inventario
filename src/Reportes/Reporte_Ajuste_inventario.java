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

public class Reporte_Ajuste_inventario {

    public void generarReporteAjustesPDF(String fechaDesde, String fechaHasta) {
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
                    m.Usuario AS usuario,
                    p.nombre AS producto,
                    m.Cantidad AS cantidad_ajustada,
                    i.Cantidad_Disponible AS stock_actual,
                    (i.Cantidad_Disponible - m.Cantidad) AS stock_previo,
                    m.Detalle AS motivo
                FROM movimiento m
                JOIN inventario i ON m.id_inventario = i.id_inventario
                JOIN producto p ON i.idProducto = p.idProducto
                WHERE m.Movimiento = 'AJUSTE'
                  AND DATE(m.FechaMovimiento) BETWEEN ? AND ?
                ORDER BY m.FechaMovimiento DESC, m.ID DESC
                """;

            PreparedStatement ps = conexion.getConnection().prepareStatement(sql);
            ps.setString(1, fechaDesde);
            ps.setString(2, fechaHasta);
            ResultSet rs = ps.executeQuery();

            java.util.List<Object[]> filas = new java.util.ArrayList<>();
            int ajustesPositivos = 0;
            int ajustesNegativos = 0;
            int totalAjustes = 0;

            while (rs.next()) {
                int cantidad = rs.getInt("cantidad_ajustada");
                totalAjustes++;
                if (cantidad > 0) ajustesPositivos++;
                else ajustesNegativos++;

                filas.add(new Object[]{
                    rs.getString("fecha"),
                    rs.getString("usuario"),
                    rs.getString("producto"),
                    cantidad,
                    rs.getInt("stock_previo"),
                    rs.getInt("stock_actual"),
                    rs.getString("motivo")
                });
            }
            rs.close();
            ps.close();

            if (filas.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No hay ajustes en este período.", "Sin datos", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // === GRÁFICA: AJUSTES POR USUARIO ===
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            java.util.Map<String, Integer> porUsuario = new java.util.HashMap<>();
            for (Object[] f : filas) {
                String usuario = (String) f[1];
                int cant = Math.abs((Integer) f[3]);
                porUsuario.put(usuario, porUsuario.getOrDefault(usuario, 0) + cant);
            }
            porUsuario.entrySet().stream()
                .sorted(java.util.Map.Entry.<String, Integer>comparingByValue().reversed())
                .forEach(e -> dataset.addValue(e.getValue(), "Ajustes", e.getKey()));

            JFreeChart chart = ChartFactory.createBarChart(
                "Ajustes por Usuario", "Usuario", "Cantidad Ajustada", dataset
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
            guardar.setSelectedFile(new File("Reporte_Ajustes_" + fechaDesde + "_al_" + fechaHasta + ".pdf"));
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
            content.showText("REPORTE DE AJUSTES DE INVENTARIO");
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
            content.showText("FECHA       USUARIO       PRODUCTO                 AJUSTE   STOCK PREVIO   STOCK ACTUAL   MOTIVO");
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

                int ajuste = (Integer) f[3];
                Color colorTexto = ajuste < 0 ? Color.RED : new Color(34, 153, 84);
                content.setNonStrokingColor(colorTexto);

                content.beginText();
                content.setFont(PDType1Font.HELVETICA, 10);
                content.newLineAtOffset(margin + 5, y + 5);
                content.showText(String.format("%-12s %-14s %-30s %-9d %-14d %-14d %-30s",
                    f[0], f[1],
                    truncar((String)f[2], 30),
                    ajuste,
                    f[4], f[5],
                    truncar((String)f[6], 30)));
                content.endText();

                y -= rowHeight;
                alternar = !alternar;
            }

            // RESUMEN
            content.beginText();
            content.setFont(PDType1Font.HELVETICA_BOLD, 16);
            content.newLineAtOffset(margin, y - 40);
            content.showText("RESUMEN DE AJUSTES:");
            content.newLineAtOffset(0, -30);
            content.showText("• Ajustes positivos: " + ajustesPositivos);
            content.newLineAtOffset(0, -20);
            content.showText("• Ajustes negativos: " + ajustesNegativos);
            content.newLineAtOffset(0, -20);
            content.showText("• Total ajustes: " + totalAjustes);
            content.endText();

            content.close();
            document.save(ruta);
            document.close();

            JOptionPane.showMessageDialog(null,
                "Reporte de Ajustes generado!\n" + ruta + "\n\nTotal ajustes: " + totalAjustes,
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
            generarReporteAjustesPDF(desde.trim(), hasta.trim());
        }
    }
}