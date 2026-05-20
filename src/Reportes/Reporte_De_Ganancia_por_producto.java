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
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Reporte_De_Ganancia_por_producto {

    public void generarReporteGananciasPDF() {
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
                    i.precio_unitario AS costo_unitario,
                    COALESCE(i.precio_venta, i.precio_unitario) AS precio_venta,
                    i.Cantidad_Disponible AS stock_actual,
                    (COALESCE(i.precio_venta, i.precio_unitario) - i.precio_unitario) AS margen_unitario,
                    ((COALESCE(i.precio_venta, i.precio_unitario) - i.precio_unitario) / i.precio_unitario * 100) AS margen_porcentaje,
                    (i.Cantidad_Disponible * (COALESCE(i.precio_venta, i.precio_unitario) - i.precio_unitario)) AS ganancia_total
                FROM inventario i
                JOIN producto p ON i.idProducto = p.idProducto
                JOIN categoria c ON p.idCategoria = c.idCategoria
                JOIN marca m ON p.idMarca = m.idMarca
                WHERE i.Cantidad_Disponible > 0
                  AND i.precio_unitario > 0
                ORDER BY ganancia_total DESC
                """;

            PreparedStatement ps = conexion.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            java.util.List<Object[]> filas = new java.util.ArrayList<>();
            double totalGanancia = 0;
            int productosRentables = 0;

            while (rs.next()) {
                double ganancia = rs.getDouble("ganancia_total");
                totalGanancia += ganancia;
                if (ganancia > 0) productosRentables++;

                filas.add(new Object[]{
                    rs.getString("producto"),
                    rs.getString("categoria"),
                    rs.getString("marca"),
                    rs.getDouble("costo_unitario"),
                    rs.getDouble("precio_venta"),
                    rs.getInt("stock_actual"),
                    rs.getDouble("margen_unitario"),
                    rs.getDouble("margen_porcentaje"),
                    ganancia
                });
            }
            rs.close();
            ps.close();

            if (filas.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No hay productos con stock.", "Sin datos", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // === GRÁFICA ===
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            for (Object[] f : filas) {
                String nombre = ((String) f[0]).length() > 20 ? ((String) f[0]).substring(0, 20) + "..." : (String) f[0];
                dataset.addValue((Double) f[6], "Margen Unitario ($)", nombre);
                dataset.addValue((Double) f[7], "Margen %", nombre);
            }

            JFreeChart chart = ChartFactory.createBarChart(
                "Ganancias por Producto", "Producto", "Valor", dataset
            );
            CategoryPlot plot = chart.getCategoryPlot();
            BarRenderer renderer = (BarRenderer) plot.getRenderer();
            renderer.setSeriesPaint(0, new Color(34, 197, 94));   // Verde = Margen $
            renderer.setSeriesPaint(1, new Color(52, 152, 219));  // Azul = Margen %

            BufferedImage chartImage = chart.createBufferedImage(1000, 400);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(chartImage, "png", baos);
            byte[] imageBytes = baos.toByteArray();
            baos.close();

            // === PDF ===
            JFileChooser guardar = new JFileChooser();
            guardar.setSelectedFile(new File("Reporte_Ganancias_por_Producto_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + ".pdf"));
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
            content.showText("REPORTE DE GANANCIAS POR PRODUCTO");
            content.endText();
            y -= 40;

            content.beginText();
            content.setFont(PDType1Font.HELVETICA, 12);
            content.newLineAtOffset(margin, y);
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
            content.showText("PRODUCTO                       CATEGORÍA        MARCA        COSTO    PRECIO   STOCK   MARGEN $   MARGEN %   GANANCIA");
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

                double margenPorc = (Double) f[7];
                content.setNonStrokingColor(margenPorc < 20 ? Color.RED : margenPorc > 50 ? Color.GREEN : Color.BLACK);

                content.beginText();
                content.setFont(PDType1Font.HELVETICA, 9);
                content.newLineAtOffset(margin + 5, y + 5);
                content.showText(String.format("%-35s %-20s %-15s %-9.2f %-9.2f %-7d %-10.2f %-10.1f%% %-10.2f",
                    truncar((String)f[0], 35),
                    truncar((String)f[1], 20),
                    truncar((String)f[2], 15),
                    f[3], f[4], f[5], f[6], margenPorc, f[8]));
                content.endText();

                y -= rowHeight;
                alternar = !alternar;
            }

            // TOTALES
            content.beginText();
            content.setFont(PDType1Font.HELVETICA_BOLD, 16);
            content.newLineAtOffset(margin, y - 40);
            content.showText("GANANCIA BRUTA TOTAL: $" + String.format("%,.2f", totalGanancia));
            content.newLineAtOffset(0, -25);
            content.showText("PRODUCTOS RENTABLES: " + productosRentables + " de " + filas.size());
            content.endText();

            content.close();
            document.save(ruta);
            document.close();

            JOptionPane.showMessageDialog(null,
                "Reporte de Ganancias generado!\n" + ruta + "\n\nGanancia total: $" + String.format("%,.2f", totalGanancia),
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
        generarReporteGananciasPDF();
    }
}