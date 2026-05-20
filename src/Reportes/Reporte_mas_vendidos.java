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

public class Reporte_mas_vendidos {

    public void generarReporteMasVendidosPDF(String fechaDesde, String fechaHasta) {
        ConexiónBD conexion = new ConexiónBD();
        PDDocument document = null;
        try {
            conexion.conectar();
            if (!conexion.estaConectado()) {
                JOptionPane.showMessageDialog(null, "No hay conexión.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // CONSULTA CORREGIDA: alias 'mov' para evitar conflicto
            String sql = """
                SELECT 
                    p.nombre AS producto,
                    c.nombre AS categoria,
                    m.nombre AS marca,
                    SUM(mov.Cantidad) AS total_vendido,
                    COUNT(*) AS veces_vendido
                FROM movimiento mov
                JOIN inventario i ON mov.id_inventario = i.id_inventario
                JOIN producto p ON i.idProducto = p.idProducto
                JOIN categoria c ON p.idCategoria = c.idCategoria
                JOIN marca m ON p.idMarca = m.idMarca
                WHERE mov.Movimiento = 'SALIDA'
                  AND DATE(mov.FechaMovimiento) BETWEEN ? AND ?
                GROUP BY p.idProducto
                ORDER BY total_vendido DESC
                LIMIT 20
                """;

            PreparedStatement ps = conexion.getConnection().prepareStatement(sql);
            ps.setString(1, fechaDesde);   // Primer parámetro
            ps.setString(2, fechaHasta);   // Segundo parámetro (ESTE FALTABA)
            ResultSet rs = ps.executeQuery();

            java.util.List<Object[]> filas = new java.util.ArrayList<>();
            int totalUnidadesVendidas = 0;

            while (rs.next()) {
                int vendido = rs.getInt("total_vendido");
                totalUnidadesVendidas += vendido;

                filas.add(new Object[]{
                    rs.getString("producto"),
                    rs.getString("categoria"),
                    rs.getString("marca"),
                    vendido,
                    rs.getInt("veces_vendido")
                });
            }
            rs.close();
            ps.close();

            if (filas.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No hay ventas en este período.", "Sin datos", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // === GRÁFICA ===
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            for (Object[] f : filas) {
                String nombre = ((String) f[0]).length() > 25 ? ((String) f[0]).substring(0, 25) + "..." : (String) f[0];
                dataset.addValue((Integer) f[3], "Unidades Vendidas", nombre);
            }

            JFreeChart chart = ChartFactory.createBarChart(
                "Top 20 Productos Más Vendidos", "Producto", "Unidades Vendidas", dataset
            );
            CategoryPlot plot = chart.getCategoryPlot();
            BarRenderer renderer = (BarRenderer) plot.getRenderer();
            renderer.setSeriesPaint(0, new Color(34, 153, 84));

            BufferedImage chartImage = chart.createBufferedImage(1100, 450);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(chartImage, "png", baos);
            byte[] imageBytes = baos.toByteArray();
            baos.close();

            // === PDF ===
            JFileChooser guardar = new JFileChooser();
            guardar.setSelectedFile(new File("Top_Vendidos_" + fechaDesde + "_al_" + fechaHasta + ".pdf"));
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
            content.showText("TOP 20 PRODUCTOS MÁS VENDIDOS");
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
            content.drawImage(pdImage, margin, y - 350, 515, 350);
            y -= 380;

            // TABLA
            float tableWidth = 520;
            float rowHeight = 22;

            // Encabezado
            content.setNonStrokingColor(new Color(41, 128, 185));
            content.addRect(margin, y, tableWidth, rowHeight);
            content.fill();
            content.setNonStrokingColor(Color.WHITE);
            content.beginText();
            content.setFont(PDType1Font.HELVETICA_BOLD, 11);
            content.newLineAtOffset(margin + 5, y + 5);
            content.showText("PRODUCTO                       CATEGORÍA        MARCA        UNIDADES   VECES");
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

                content.setNonStrokingColor(Color.BLACK);
                content.beginText();
                content.setFont(PDType1Font.HELVETICA, 10);
                content.newLineAtOffset(margin + 5, y + 5);
                content.showText(String.format("%-35s %-20s %-15s %-10d %-10d",
                    truncar((String)f[0], 35),
                    truncar((String)f[1], 20),
                    truncar((String)f[2], 15),
                    f[3], f[4]));
                content.endText();

                y -= rowHeight;
                alternar = !alternar;
            }

            // TOTALES
            content.beginText();
            content.setFont(PDType1Font.HELVETICA_BOLD, 16);
            content.newLineAtOffset(margin, y - 40);
            content.showText("TOTAL UNIDADES VENDIDAS EN EL PERÍODO: " + totalUnidadesVendidas);
            content.endText();

            content.close();
            document.save(ruta);
            document.close();

            JOptionPane.showMessageDialog(null,
                "Reporte Top 20 Más Vendidos generado!\n" + ruta,
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
            generarReporteMasVendidosPDF(desde.trim(), hasta.trim());
        }
    }
}