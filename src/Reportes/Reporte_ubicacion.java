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

public class Reporte_ubicacion {

    public void generarReporteUbicacionPDF() {
        ConexiónBD conexion = new ConexiónBD();
        PDDocument document = null;
        try {
            conexion.conectar();
            if (!conexion.estaConectado()) {
                JOptionPane.showMessageDialog(null, "No hay conexión.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // CONSULTA EXACTAMENTE IGUAL A TU PANTALLA
            String sql = """
                SELECT 
                    a.id_ubicacion AS id_ubicacion,
                    a.pasillo,
                    a.ala,
                    a.estante,
                    a.nivel,
                    COALESCE(a.capacidad, 0) AS capacidad,
                    p.nombre AS producto,
                    i.Cantidad_Disponible AS stock_actual
                FROM almacen a
                LEFT JOIN inventario i ON a.id_ubicacion = i.id_Ubicacion
                LEFT JOIN producto p ON i.idProducto = p.idProducto
                ORDER BY a.pasillo, a.ala, a.estante, a.nivel
                """;

            PreparedStatement ps = conexion.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            java.util.List<Object[]> filas = new java.util.ArrayList<>();
            int totalUbicaciones = 0;
            int ubicacionesConProducto = 0;

            while (rs.next()) {
                totalUbicaciones++;
                if (rs.getObject("producto") != null) ubicacionesConProducto++;

                filas.add(new Object[]{
                    rs.getInt("id_ubicacion"),
                    rs.getString("pasillo") != null ? rs.getString("pasillo") : "",
                    rs.getString("ala") != null ? rs.getString("ala") : "",
                    rs.getString("estante") != null ? rs.getString("estante") : "",
                    rs.getString("nivel") != null ? rs.getString("nivel") : "",
                    rs.getInt("capacidad"),
                    rs.getString("producto") != null ? rs.getString("producto") : "VACÍA",
                    rs.getInt("stock_actual")
                });
            }
            rs.close();
            ps.close();

            if (filas.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No hay ubicaciones registradas.", "Sin datos", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // === GRÁFICA ===
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            java.util.Map<String, Integer> porPasillo = new java.util.HashMap<>();
            for (Object[] f : filas) {
                String pasillo = "P" + f[1];
                porPasillo.put(pasillo, porPasillo.getOrDefault(pasillo, 0) + 1);
            }
            porPasillo.forEach((pasillo, cant) -> dataset.addValue(cant, "Ubicaciones", pasillo));

            JFreeChart chart = ChartFactory.createBarChart(
                "Ubicaciones por Pasillo", "Pasillo", "Cantidad", dataset
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
            guardar.setSelectedFile(new File("Reporte_Ubicacion_Completo_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm")) + ".pdf"));
            if (guardar.showSaveDialog(null) != JFileChooser.APPROVE_OPTION) return;

            String ruta = guardar.getSelectedFile().getAbsolutePath();
            if (!ruta.toLowerCase().endsWith(".pdf")) ruta += ".pdf";

            document = new PDDocument();
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);
            PDPageContentStream content = new PDPageContentStream(document, page);

            float margin = 30;
            float y = 780;

            // TÍTULO
            content.beginText();
            content.setFont(PDType1Font.HELVETICA_BOLD, 22);
            content.newLineAtOffset(margin, y);
            content.showText("REPORTE COMPLETO DE ALMACÉN POR UBICACIÓN");
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

            // TABLA EXACTAMENTE IGUAL A TU PANTALLA
            float tableWidth = 540;
            float rowHeight = 22;

            // Encabezado
            content.setNonStrokingColor(new Color(41, 128, 185));
            content.addRect(margin, y, tableWidth, rowHeight);
            content.fill();
            content.setNonStrokingColor(Color.WHITE);
            content.beginText();
            content.setFont(PDType1Font.HELVETICA_BOLD, 10);
            content.newLineAtOffset(margin + 5, y + 5);
            content.showText("ID_UBICACIÓN  PASILLO     ALA       ESTANTE   NIVEL   CAPACIDAD   PRODUCTO                  STOCK ACTUAL");
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
                content.newLineAtOffset(margin + 5, y + 5);
                content.showText(String.format("%-13d %-11s %-9s %-9s %-7s %-11d %-30s %-13d",
                    f[0], f[1], f[2], f[3], f[4], f[5],
                    truncar((String)f[6], 30), f[7]));
                content.endText();

                y -= rowHeight;
                alternar = !alternar;
            }

            // TOTALES
            content.beginText();
            content.setFont(PDType1Font.HELVETICA_BOLD, 16);
            content.newLineAtOffset(margin, y - 40);
            content.showText("TOTAL UBICACIONES: " + totalUbicaciones);
            content.newLineAtOffset(0, -25);
            content.showText("UBICACIONES CON PRODUCTO: " + ubicacionesConProducto);
            content.newLineAtOffset(0, -25);
            content.showText("UBICACIONES VACÍAS: " + (totalUbicaciones - ubicacionesConProducto));
            content.endText();

            content.close();
            document.save(ruta);
            document.close();

            JOptionPane.showMessageDialog(null,
                "Reporte de Almacén por Ubicación generado!\n" + ruta,
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
        generarReporteUbicacionPDF();
    }
}