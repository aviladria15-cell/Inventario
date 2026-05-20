package Reportes;

import Modelo.Inventario;
import Vista_Almacen.Vista_Salida_Unidad;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import java.awt.Color;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Generador de Reportes Estilo SAP Business One - Productos Unidad
 * @author avila
 */
public class Orden_Salida_Unidad {

    private final Color SAP_BLUE = new Color(0, 93, 169);
    private final Color LIGHT_GRAY = new Color(240, 240, 240);
    private final Color TEXT_DARK = new Color(40, 40, 40);

    // Formato venezolano: 19.376,37
    private final DecimalFormat dfBolivares = createBolivaresFormat();

    private DecimalFormat createBolivaresFormat() {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.GERMANY);
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');
        DecimalFormat df = new DecimalFormat("#,##0.00", symbols);
        df.setRoundingMode(RoundingMode.HALF_UP);
        return df;
    }

    public void generarPDFOrdenSalidaUnidad(int cantidad, BigDecimal precioUnitario,
                                            BigDecimal tasaDolar, String detalle) {

        Inventario seleccionado = (Inventario) Vista_Salida_Unidad.jComboBoxProductoUnida.getSelectedItem();

        if (seleccionado == null) {
            JOptionPane.showMessageDialog(null, "Debe seleccionar un producto unidad", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                float margin = 50;
                float width = page.getMediaBox().getWidth() - (2 * margin);
                float y = 780;

                // ====================== ENCABEZADO ======================
                contentStream.setNonStrokingColor(SAP_BLUE);
                contentStream.addRect(margin, y - 5, width, 3);
                contentStream.fill();
                y -= 30;

                contentStream.setNonStrokingColor(SAP_BLUE);
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 24);
                drawText(contentStream, margin, y, "Orden de Salida de Inventario");

                contentStream.setNonStrokingColor(TEXT_DARK);
                contentStream.setFont(PDType1Font.HELVETICA, 10);
                drawText(contentStream, margin, y - 15, "RAC 2012 C.A. - Gestión de Almacén");

                float rightInfoX = 400;
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
                drawText(contentStream, rightInfoX, y, "N° Documento:");
                drawText(contentStream, rightInfoX, y - 15, "Fecha Emisión:");

                contentStream.setFont(PDType1Font.HELVETICA, 10);
                drawText(contentStream, rightInfoX + 80, y, "OSU-" + System.currentTimeMillis() / 100000);
                drawText(contentStream, rightInfoX + 80, y - 15, LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

                y -= 60;

                // ====================== INFORMACIÓN DEL ARTÍCULO ======================
                y = drawSectionHeader(contentStream, margin, y, width, "INFORMACIÓN DEL ARTÍCULO");

                float col1 = margin + 10;
                float col2 = margin + 180;
                float rowH = 15;

                String[][] productData = {
                    {"Descripción:", seleccionado.getProductos()},
                    {"Marca:", getCampo("Marca")},
                    {"Número Serial:", getCampo("Número Serial")},
                    {"Compatibilidad:", getCampo("Compatibilidad")},
                    {"Unidad de Medida:", getCampo("Unidad Medida")},
                    {"Especificaciones:", getCampo("Especificaciones")},
                    {"Lote:", getCampo("Lote")}
                };

                for (String[] row : productData) {
                    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
                    drawText(contentStream, col1, y, row[0]);
                    contentStream.setFont(PDType1Font.HELVETICA, 10);
                    drawText(contentStream, col2, y, row[1]);
                    y -= rowH;
                }
                y -= 25;

                // ====================== LOGÍSTICA ======================
                y = drawSectionHeader(contentStream, margin, y, width, "LOGÍSTICA Y UBICACIÓN");

                drawText(contentStream, col1, y, "Ubicación en Almacén:");
                contentStream.setFont(PDType1Font.HELVETICA, 10);
                String ubicacion = String.format("Pasillo: %s | Ala: %s | Estante: %s | Nivel: %s",
                        getCampo("Pasillo"), getCampo("Ala"), getCampo("Estante"), getCampo("Nivel"));
                drawText(contentStream, col2, y, ubicacion);

                y -= 35;

                // ====================== RESUMEN DE SALIDA ======================
                y = drawSectionHeader(contentStream, margin, y, width, "RESUMEN DE SALIDA");

                contentStream.setNonStrokingColor(LIGHT_GRAY);
                contentStream.addRect(margin, y - 5, width, 15);
                contentStream.fill();

                contentStream.setNonStrokingColor(TEXT_DARK);
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 9);
                drawText(contentStream, margin + 5, y, "CANTIDAD");
                drawText(contentStream, margin + 80, y, "DESCRIPCIÓN");
                drawText(contentStream, margin + 280, y, "PRECIO UNIT. (USD)");
                drawText(contentStream, margin + 400, y, "TOTAL (USD)");
                y -= 20;

                BigDecimal totalDolares = precioUnitario.multiply(new BigDecimal(cantidad));
                BigDecimal totalBolivares = totalDolares.multiply(tasaDolar);

                contentStream.setFont(PDType1Font.HELVETICA, 10);
                drawText(contentStream, margin + 5, y, String.valueOf(cantidad));
                drawText(contentStream, margin + 80, y, seleccionado.getProductos());
                drawText(contentStream, margin + 280, y, "$ " + precioUnitario.setScale(2, RoundingMode.HALF_UP));
                drawText(contentStream, margin + 400, y, "$ " + totalDolares.setScale(2, RoundingMode.HALF_UP));

                y -= 40;

                // ====================== BLOQUE TOTALES ======================
                float totalBoxX = 350;
                contentStream.setNonStrokingColor(LIGHT_GRAY);
                contentStream.addRect(totalBoxX, y - 40, 195, 55);
                contentStream.fill();

                contentStream.setNonStrokingColor(TEXT_DARK);
                drawText(contentStream, totalBoxX + 10, y, "Tasa BCV:");
                drawText(contentStream, totalBoxX + 110, y, "Bs. " + tasaDolar.setScale(2, RoundingMode.HALF_UP));

                y -= 15;
                drawText(contentStream, totalBoxX + 10, y, "Total USD:");
                drawText(contentStream, totalBoxX + 110, y, "$ " + totalDolares.setScale(2, RoundingMode.HALF_UP));

                y -= 15;
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 11);
                contentStream.setNonStrokingColor(SAP_BLUE);
                drawText(contentStream, totalBoxX + 10, y, "TOTAL BS:");
                drawText(contentStream, totalBoxX + 110, y, "Bs. " + dfBolivares.format(totalBolivares));

                // ====================== PIE DE PÁGINA CON FIRMAS ======================
                y = 100;
                contentStream.setNonStrokingColor(Color.GRAY);
                contentStream.setFont(PDType1Font.HELVETICA_OBLIQUE, 8);
                drawText(contentStream, margin, y, "Motivo de salida: " + (detalle.isEmpty() ? "Despacho de inventario estándar" : detalle));

                // Firmas
                drawLine(contentStream, margin, 70, margin + 150, 70);
                drawText(contentStream, margin + 35, 60, "Firma Almacén");

                drawLine(contentStream, width - 100, 70, width + margin, 70);
                drawText(contentStream, width - 80, 60, "Firma Receptor");
            }

            guardarArchivo(document);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error SAP Report: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // ==================== MÉTODOS AUXILIARES ====================

    private float drawSectionHeader(PDPageContentStream cs, float x, float y, float w, String title) throws IOException {
        cs.setNonStrokingColor(SAP_BLUE);
        cs.addRect(x, y - 5, w, 15);
        cs.fill();

        cs.setNonStrokingColor(Color.WHITE);
        cs.setFont(PDType1Font.HELVETICA_BOLD, 10);
        cs.beginText();
        cs.newLineAtOffset(x + 5, y - 1);
        cs.showText(title);
        cs.endText();

        cs.setNonStrokingColor(TEXT_DARK);
        return y - 25;
    }

    private void drawText(PDPageContentStream cs, float x, float y, String text) throws IOException {
        cs.beginText();
        cs.newLineAtOffset(x, y);
        cs.showText(text != null ? text : "N/A");
        cs.endText();
    }

    private void drawLine(PDPageContentStream cs, float x1, float y1, float x2, float y2) throws IOException {
        cs.setStrokingColor(Color.LIGHT_GRAY);
        cs.setLineWidth(0.5f);
        cs.moveTo(x1, y1);
        cs.lineTo(x2, y2);
        cs.stroke();
    }

    private void guardarArchivo(PDDocument document) throws IOException {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File("SAP_Orden_Salida_Unidad_" + System.currentTimeMillis() / 1000 + ".pdf"));
        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            document.save(fileChooser.getSelectedFile());
            JOptionPane.showMessageDialog(null, "Reporte generado con éxito.");
        }
    }

    private String getCampo(String campo) {
        String texto = Vista_Salida_Unidad.TxtInformacionProducto.getText();
        if (texto == null || texto.isEmpty()) return "N/A";

        for (String linea : texto.split("\n")) {
            linea = linea.trim();
            if (linea.isEmpty()) continue;

            if (linea.toLowerCase().contains(campo.toLowerCase())) {
                int index = linea.indexOf(":");
                if (index != -1) {
                    String valor = linea.substring(index + 1).trim();
                    return valor.isEmpty() ? "N/A" : valor;
                }
            }
        }
        return "N/A";
    }
}