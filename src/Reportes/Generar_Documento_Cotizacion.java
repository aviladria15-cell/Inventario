package Reportes;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import javax.swing.JOptionPane;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import Vista_Gestionar_Proveedor.Vista_Cotizaciónn;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.awt.Color;

public class Generar_Documento_Cotizacion {

    public void GenerarPFDCotizacion(Vista_Cotizaciónn vista) {

        // ==================== OBTENER DATOS ====================
        String nombreProveedor = vista.txtNombreProveedor.getText().trim();
        String nombreProducto = vista.txtNombreProducto.getText().trim();
        String categoria = vista.txtCategoriaProudcto.getText().trim();
        String marca = vista.txtMarca.getText().trim();
        String cantidadActual = vista.txtCantidadActual.getText().trim();
        String cantidadRequerida = vista.txtCantidadRequeridad.getText().trim();
        String detalleUsuario = vista.txtDescripcion.getText().trim();   // ← Lo que escribe el usuario

        if (nombreProducto.isEmpty() || cantidadRequerida.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                "Debe seleccionar un producto y especificar la cantidad requerida",
                "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nombreSugerido = "Cotizacion_" + nombreProducto.replace(" ", "_") + "_"
                + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + ".pdf";

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar Cotización PDF");
        fileChooser.setSelectedFile(new File(nombreSugerido));
        fileChooser.setFileFilter(new FileNameExtensionFilter("Archivo PDF (*.pdf)", "pdf"));

        int userSelection = fileChooser.showSaveDialog(null);
        if (userSelection != JFileChooser.APPROVE_OPTION) return;

        File archivoSeleccionado = fileChooser.getSelectedFile();
        String rutaCompleta = archivoSeleccionado.getAbsolutePath();
        if (!rutaCompleta.toLowerCase().endsWith(".pdf")) {
            rutaCompleta += ".pdf";
        }

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            try (PDPageContentStream cs = new PDPageContentStream(document, page)) {

                float y = 800;

                // ==================== ENCABEZADO ====================
                cs.setNonStrokingColor(new Color(0, 51, 102));
                cs.setFont(PDType1Font.HELVETICA_BOLD, 24);
                cs.beginText();
                cs.newLineAtOffset(50, y);
                cs.showText("REPUESTOS Y ACCESORIOS RAC 2012 C.A.");
                cs.endText();

                y -= 45;
                cs.setNonStrokingColor(Color.BLACK);
                cs.setFont(PDType1Font.HELVETICA_BOLD, 18);
                cs.beginText();
                cs.newLineAtOffset(50, y);
                cs.showText("COTIZACIÓN DE PRODUCTO");
                cs.endText();

                cs.setFont(PDType1Font.HELVETICA, 12);
                cs.beginText();
                cs.newLineAtOffset(390, y + 10);
                cs.showText("Fecha: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                cs.endText();

                y -= 80;

                // ==================== PROVEEDOR ====================
                cs.setFont(PDType1Font.HELVETICA_BOLD, 13);
                cs.setNonStrokingColor(new Color(0, 51, 102));
                cs.beginText();
                cs.newLineAtOffset(50, y);
                cs.showText("PROVEEDOR");
                cs.endText();

                y -= 28;
                cs.setFont(PDType1Font.HELVETICA, 11);
                cs.setNonStrokingColor(Color.BLACK);
                dibujarLineaSimple(cs, 50, y, "Nombre:", nombreProveedor.isEmpty() ? "N/A" : nombreProveedor);

                y -= 55;

                // ==================== PRODUCTO ====================
                cs.setFont(PDType1Font.HELVETICA_BOLD, 13);
                cs.setNonStrokingColor(new Color(0, 51, 102));
                cs.beginText();
                cs.newLineAtOffset(50, y);
                cs.showText("PRODUCTO");
                cs.endText();

                y -= 28;
                cs.setFont(PDType1Font.HELVETICA, 11);
                cs.setNonStrokingColor(Color.BLACK);

                dibujarLineaSimple(cs, 50, y, "Nombre:", nombreProducto);
                y -= 22;
                dibujarLineaSimple(cs, 50, y, "Marca:", marca.isEmpty() ? "N/A" : marca);
                y -= 22;
                dibujarLineaSimple(cs, 50, y, "Categoría:", categoria.isEmpty() ? "N/A" : categoria);
                y -= 22;
                dibujarLineaSimple(cs, 50, y, "Stock Actual:", cantidadActual.isEmpty() ? "0" : cantidadActual);
                y -= 22;
                dibujarLineaSimple(cs, 50, y, "Cantidad Requerida:", cantidadRequerida);

                y -= 70;

                // ==================== DETALLE DE LA COTIZACIÓN ====================
                // Aquí va exactamente lo que el usuario escribe en el txtArea
                cs.setFont(PDType1Font.HELVETICA_BOLD, 13);
                cs.setNonStrokingColor(new Color(0, 51, 102));
                cs.beginText();
                cs.newLineAtOffset(50, y);
                cs.showText("DETALLE DE LA COTIZACIÓN");
                cs.endText();

                y -= 32;
                cs.setFont(PDType1Font.HELVETICA, 11);
                cs.setNonStrokingColor(Color.BLACK);
                dibujarTextoMultilinea(cs, 50, y, 
                    detalleUsuario.isEmpty() ? "Se necesita con urgencia." : detalleUsuario, 490, 15);

                y -= 110;

                // ==================== PIE ====================
                cs.setFont(PDType1Font.HELVETICA_OBLIQUE, 10);
                cs.setNonStrokingColor(Color.GRAY);
                cs.beginText();
                cs.newLineAtOffset(50, y);
                cs.showText("         Esta cotización tiene una validez de 7 días hábiles. Gracias por su preferencia.");
                cs.endText();

            }

            document.save(rutaCompleta);
            JOptionPane.showMessageDialog(null,
                "¡Cotización generada exitosamente!\nGuardado en:\n" + rutaCompleta,
                "Éxito", JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al generar el PDF:\n" + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "La cantidad requerida debe ser un número válido",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ==================== MÉTODOS AUXILIARES ====================

    private void dibujarLineaSimple(PDPageContentStream cs, float x, float y, String label, String value) throws IOException {
        cs.beginText();
        cs.newLineAtOffset(x, y);
        cs.showText(label);
        cs.newLineAtOffset(145, 0);
        cs.showText(value);
        cs.endText();
    }

    private void dibujarTextoMultilinea(PDPageContentStream cs, float x, float y, String texto, float anchoMax, float leading) throws IOException {
        if (texto == null || texto.trim().isEmpty()) return;

        cs.setLeading(leading);
        cs.beginText();
        cs.newLineAtOffset(x, y);

        String[] lineas = texto.split("\n");
        for (String linea : lineas) {
            if (!linea.trim().isEmpty()) {
                cs.showText(linea.trim());
                cs.newLine();
            }
        }
        cs.endText();
    }
}