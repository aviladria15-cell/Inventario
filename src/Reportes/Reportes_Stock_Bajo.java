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

public class Reportes_Stock_Bajo {

    // LIMPIAR CARACTERES PROBLEMÁTICOS (NUNCA MÁS ERROR DE FUENTE)
    private String limpiarTexto(String texto) {
        if (texto == null) return "";
        return texto.replace("≤", "<=")
                    .replace("≥", ">=")
                    .replace("ñ", "n")
                    .replace("Ñ", "N")
                    .replace("á", "a").replace("é", "e").replace("í", "i")
                    .replace("ó", "o").replace("ú", "u")
                    .replace("Á", "A").replace("É", "E").replace("Í", "I")
                    .replace("Ó", "O").replace("Ú", "U")
                    .replaceAll("[^\\x00-\\x7F]", "?");
    }

    private String truncar(String s, int len) {
        if (s == null) return "";
        s = limpiarTexto(s);
        return s.length() > len ? s.substring(0, len - 3) + "..." : s;
    }

    public void generarReporteStockBajoPDF(String fechaDesde, String fechaHasta) {
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
                    p.nombre AS producto,
                    c.nombre AS categoria,
                    m.nombre AS marca,
                    i.Cantidad_Disponible AS stock_actual,
                    COALESCE(i.stockMinimo, 0) AS stock_minimo
                FROM inventario i
                JOIN producto p ON i.idProducto = p.idProducto
                JOIN categoria c ON p.idCategoria = c.idCategoria
                JOIN marca m ON p.idMarca = m.idMarca
                WHERE i.Cantidad_Disponible <= COALESCE(i.stockMinimo, 0)
                  AND DATE(i.fecha_ingreso) BETWEEN ? AND ?
                ORDER BY i.Cantidad_Disponible ASC
                """;

            PreparedStatement ps = conexion.getConnection().prepareStatement(sql);
            ps.setString(1, fechaDesde);
            ps.setString(2, fechaHasta);
            ResultSet rs = ps.executeQuery();

            java.util.List<Object[]> filas = new java.util.ArrayList<>();
            int sinStock = 0;
            int bajoStock = 0;

            while (rs.next()) {
                int stock = rs.getInt("stock_actual");
                filas.add(new Object[]{
                    rs.getString("producto"),
                    rs.getString("categoria"),
                    rs.getString("marca"),
                    stock,
                    rs.getInt("stock_minimo")
                });
                if (stock == 0) sinStock++;
                else bajoStock++;
            }
            rs.close();
            ps.close();

            if (filas.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No hay productos con stock bajo en este período.", "Todo bien", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // === GRÁFICA ===
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            for (Object[] f : filas) {
                String nombre = truncar((String) f[0], 20);
                dataset.addValue((Integer) f[3], "Stock Actual", nombre);
                dataset.addValue((Integer) f[4], "Stock Mínimo", nombre);
            }

            JFreeChart chart = ChartFactory.createBarChart(
                "Productos con Stock Bajo", "Producto", "Cantidad", dataset
            );
            CategoryPlot plot = chart.getCategoryPlot();
            BarRenderer renderer = (BarRenderer) plot.getRenderer();
            renderer.setSeriesPaint(0, new Color(231, 76, 60));
            renderer.setSeriesPaint(1, new Color(241, 196, 15));

            BufferedImage chartImage = chart.createBufferedImage(1000, 400);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(chartImage, "png", baos);
            byte[] imageBytes = baos.toByteArray();
            baos.close();

            // === PDF ===
            JFileChooser guardar = new JFileChooser();
            guardar.setSelectedFile(new File("Reporte_Stock_Bajo_" + fechaDesde + "_al_" + fechaHasta + ".pdf"));
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
            content.showText("REPORTE DE STOCK BAJO");
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

            // TABLA (5 columnas - SIN UBICACIÓN)
            float tableWidth = 520;
            float rowHeight = 22;

            // Encabezado
            content.setNonStrokingColor(new Color(192, 57, 43));
            content.addRect(margin, y, tableWidth, rowHeight);
            content.fill();
            content.setNonStrokingColor(Color.WHITE);
            content.beginText();
            content.setFont(PDType1Font.HELVETICA_BOLD, 11);
            content.newLineAtOffset(margin + 10, y + 5);
            content.showText("PRODUCTO                        CATEGORÍA        MARCA        STOCK   MÍNIMO");
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

                int stock = (Integer) f[3];
                content.setNonStrokingColor(stock == 0 ? Color.RED : Color.BLACK);

                content.beginText();
                content.setFont(PDType1Font.HELVETICA, 10);
                content.newLineAtOffset(margin + 10, y + 5);
                content.showText(String.format("%-40s %-20s %-15s %-10d %-10d",
                    truncar((String) f[0], 40),
                    truncar((String) f[1], 20),
                    truncar((String) f[2], 15),
                    stock,
                    f[4]));
                content.endText();

                y -= rowHeight;
                alternar = !alternar;
            }

            // RESUMEN
            content.beginText();
            content.setFont(PDType1Font.HELVETICA_BOLD, 16);
            content.newLineAtOffset(margin, y - 40);
            content.showText("RESUMEN:");
            content.newLineAtOffset(0, -30);
            content.showText("• Sin stock (0 unidades): " + sinStock);
            content.newLineAtOffset(0, -20);
            content.showText("• Con stock bajo: " + bajoStock);
            content.endText();

            content.close();
            document.save(ruta);
            document.close();

            JOptionPane.showMessageDialog(null, 
                "Reporte Stock Bajo generado correctamente!\n" + ruta,
                "Éxito", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } finally {
            try { if (document != null) document.close(); } catch (Exception ignored) {}
            try { conexion.cerrarCn(); } catch (Exception ignored) {}
        }
    }

    public void mostrar() {
        String desde = JOptionPane.showInputDialog(null, "Fecha desde (yyyy-MM-dd):", "2025-01-01");
        String hasta = JOptionPane.showInputDialog(null, "Fecha hasta (yyyy-MM-dd):", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        if (desde != null && hasta != null && !desde.trim().isEmpty() && !hasta.trim().isEmpty()) {
            generarReporteStockBajoPDF(desde.trim(), hasta.trim());
        }
    }
}