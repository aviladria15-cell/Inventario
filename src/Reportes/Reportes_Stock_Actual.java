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

public class Reportes_Stock_Actual {

    public void generarReporteStockActualPDF(String fechaDesde, String fechaHasta) {
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
                    COALESCE(i.stockMinimo, 0) AS stock_minimo,
                    COALESCE(i.stockMaximo, 9999) AS stock_maximo
                FROM inventario i
                JOIN producto p ON i.idProducto = p.idProducto
                JOIN categoria c ON p.idCategoria = c.idCategoria
                JOIN marca m ON p.idMarca = m.idMarca
                WHERE i.Cantidad_Disponible > 0
                  AND DATE(i.fecha_ingreso) BETWEEN ? AND ?
                ORDER BY i.Cantidad_Disponible DESC
                """;

            PreparedStatement ps = conexion.getConnection().prepareStatement(sql);
            ps.setString(1, fechaDesde);
            ps.setString(2, fechaHasta);
            ResultSet rs = ps.executeQuery();

            // === RECOLECTAR DATOS Y GRÁFICA ===
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            java.util.List<Object[]> filas = new java.util.ArrayList<>();
            int totalUnidades = 0;
            int bajoStock = 0;

            while (rs.next()) {
                String producto = rs.getString("producto");
                int stock = rs.getInt("stock_actual");
                int minimo = rs.getInt("stock_minimo");
                totalUnidades += stock;
                if (stock <= minimo) bajoStock++;

                String nombreCorto = producto.length() > 18 ? producto.substring(0, 18) + "..." : producto;
                dataset.addValue(stock, "Stock Actual", nombreCorto);
                dataset.addValue(minimo, "Stock Mínimo", nombreCorto);

                filas.add(new Object[]{
                    producto,
                    rs.getString("categoria"),
                    rs.getString("marca"),
                    stock,
                    minimo,
                    rs.getInt("stock_maximo")
                });
            }
            rs.close();
            ps.close();

            if (filas.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No hay datos.", "Sin datos", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // === GRÁFICA ===
            JFreeChart chart = ChartFactory.createBarChart(
                "Stock Actual vs Stock Mínimo", "Producto", "Cantidad", dataset
            );
            CategoryPlot plot = chart.getCategoryPlot();
            BarRenderer renderer = (BarRenderer) plot.getRenderer();
            renderer.setSeriesPaint(0, new Color(34, 197, 94));
            renderer.setSeriesPaint(1, new Color(255, 99, 132));

            BufferedImage chartImage = chart.createBufferedImage(1000, 400);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(chartImage, "png", baos);
            byte[] imageBytes = baos.toByteArray();
            baos.close();

            // === GUARDAR PDF ===
            JFileChooser guardar = new JFileChooser();
            guardar.setSelectedFile(new File("Reporte_Stock_Actual_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm")) + ".pdf"));
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
            content.showText("REPORTE DE EXISTENCIAS - STOCK ACTUAL");
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

            // === TABLA PERFECTA CON PDFBox (6 columnas alineadas) ===
            float rowHeight = 20;
            float tableWidth = page.getMediaBox().getWidth() - 2 * margin;
            float[] colWidths = {180, 100, 90, 60, 60, 70}; // 6 columnas

            // Encabezado
            content.setNonStrokingColor(new Color(41, 128, 185));
            content.addRect(margin, y, tableWidth, rowHeight);
            content.fill();
            content.setNonStrokingColor(Color.WHITE);
            content.beginText();
            content.setFont(PDType1Font.HELVETICA_BOLD, 11);
            content.newLineAtOffset(margin + 5, y + 5);
            content.showText("PRODUCTO                     CATEGORÍA       MARCA       STOCK   MÍNIMO   MÁXIMO");
            content.endText();
            y -= rowHeight;

            boolean alternar = false;
            for (Object[] fila : filas) {
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

                int stock = (int) fila[3];
                int minimo = (int) fila[4];
                if (stock <= minimo) {
                    content.setNonStrokingColor(Color.RED);
                } else {
                    content.setNonStrokingColor(Color.BLACK);
                }

                content.beginText();
                content.setFont(PDType1Font.HELVETICA, 10);
                content.newLineAtOffset(margin + 5, y + 5);
                content.showText(String.format("%-35s %-20s %-15s %-10d %-10d %-10d",
                    truncar((String) fila[0], 35),
                    truncar((String) fila[1], 20),
                    truncar((String) fila[2], 15),
                    stock, minimo, fila[5]));
                content.endText();

                y -= rowHeight;
                alternar = !alternar;
            }

            // TOTALES
            content.beginText();
            content.setFont(PDType1Font.HELVETICA_BOLD, 16);
            content.newLineAtOffset(margin, y - 40);
            content.showText("TOTAL UNIDADES EN STOCK: " + totalUnidades);
            if (bajoStock > 0) {
                content.newLineAtOffset(0, -30);
                content.setFont(PDType1Font.HELVETICA_BOLD, 18);
                content.showText(bajoStock + " LOTE(S) CON STOCK CRÍTICO - ¡REPONER URGENTE!");
            }
            content.endText();

            content.close();
            document.save(ruta);
            document.close();

            JOptionPane.showMessageDialog(null, "PDF generado:\n" + ruta, "Éxito", JOptionPane.INFORMATION_MESSAGE);

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
            generarReporteStockActualPDF(desde.trim(), hasta.trim());
        }
    }
}