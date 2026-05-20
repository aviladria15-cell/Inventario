package Validacion;
/**
 *
 * @author Adrian
 */
import Vista_Almacen.Gestionar_Almacenn;
import ModeloDAO.MovientosDao;
import java.sql.SQLException;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.DocumentFilter.FilterBypass;
public class Validar_Salida_Liquido {

    
    
    
    
    MovientosDao mv = new MovientosDao();
    
    
    public void inicializarValidacionesSalidaLiquido() {
    // Ocultar errores al inicio
    ocultarTodosErroresSalidaLiquido();
    // === FILTROS DE ENTRADA ===
    aplicarFiltroSoloNumerosEnteros(Gestionar_Almacenn.txtCantidadLiquido);
    aplicarFiltroTextoConSentido(Gestionar_Almacenn.txtDatelleLiquido);
    aplicarFiltroPrecio(Gestionar_Almacenn.txtPrecioVentaLiquido);
    // === VALIDACIÓN EN TIEMPO REAL ===
    Gestionar_Almacenn.txtCantidadLiquido.getDocument()
        .addDocumentListener(new SimpleDocumentListener(this::validarCantidadLiquido));
    Gestionar_Almacenn.txtDatelleLiquido.getDocument()
        .addDocumentListener(new SimpleDocumentListener(this::validarDetalleLiquido));
    Gestionar_Almacenn.txtPrecioVentaLiquido.getDocument()
        .addDocumentListener(new SimpleDocumentListener(this::validarPrecioVentaLiquido));
    Gestionar_Almacenn.JComboxLiquido.addActionListener(e -> validarComboLiquido());
}
private void aplicarFiltroSoloNumerosEnteros(JTextField campo) {
    ((AbstractDocument) campo.getDocument()).setDocumentFilter(new DocumentFilter() {
        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                throws BadLocationException {
            if (string != null && string.matches("\\d*")) {
                super.insertString(fb, offset, string, attr);
            }
        }
        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                throws BadLocationException {
            if (text != null && text.matches("\\d*")) {
                super.replace(fb, offset, length, text, attrs);
            }
        }
    });
}
private void aplicarFiltroTextoConSentido(JTextField campo) {
    ((AbstractDocument) campo.getDocument()).setDocumentFilter(new DocumentFilter() {
        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                throws BadLocationException {
            if (string != null && string.matches("[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ., ]*")) {
                String nuevo = construirTexto(fb, offset, 0, string);
                if (esDetalleValido(nuevo)) {
                    super.insertString(fb, offset, string, attr);
                }
            }
        }
        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                throws BadLocationException {
            if (text != null && text.matches("[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ., ]*")) {
                String nuevo = construirTexto(fb, offset, length, text);
                if (esDetalleValido(nuevo)) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        }
        private String construirTexto(FilterBypass fb, int offset, int length, String text)
                throws BadLocationException {
            String actual = fb.getDocument().getText(0, fb.getDocument().getLength());
            return actual.substring(0, offset) + text + actual.substring(offset + length);
        }
    });
}
private boolean esDetalleValido(String texto) {
    if (texto == null || texto.trim().isEmpty()) return true;
    texto = texto.trim();
    // Mínimo 3 caracteres (validado en listener, no bloquea aquí para permitir escribir)
    // Máximo 3 caracteres iguales seguidos
    if (texto.matches(".*(.)\\1{3}.*")) return false;
    // Al menos una letra o número
    if (!texto.matches(".*[a-zA-Z0-9].*")) return false;
    return true;
}
private void aplicarFiltroPrecio(JTextField campo) {
    ((AbstractDocument) campo.getDocument()).setDocumentFilter(new DocumentFilter() {
        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                throws BadLocationException {
            if (string != null && string.matches("\\d*\\.?\\d*")) {
                super.insertString(fb, offset, string, attr);
            }
        }
        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                throws BadLocationException {
            if (text != null && text.matches("\\d*\\.?\\d*")) {
                super.replace(fb, offset, length, text, attrs);
            }
        }
    });
}
private void validarCantidadLiquido() {
    String cantidad = Gestionar_Almacenn.txtCantidadLiquido.getText().trim();
    if (cantidad.isEmpty()) {
        mostrarError(Gestionar_Almacenn.lblValidarCantidadLiquido, "La cantidad no puede estar vacía");
    } else {
        try {
            int cant = Integer.parseInt(cantidad);
            if (cant <= 0) {
                mostrarError(Gestionar_Almacenn.lblValidarCantidadLiquido, "Debe ser mayor a 0");
            } else {
                ocultarError(Gestionar_Almacenn.lblValidarCantidadLiquido);
            }
        } catch (NumberFormatException e) {
            mostrarError(Gestionar_Almacenn.lblValidarCantidadLiquido, "Número inválido");
        }
    }
}
private void validarDetalleLiquido() {
    String detalle = Gestionar_Almacenn.txtDatelleLiquido.getText().trim();
    if (detalle.isEmpty()) {
        mostrarError(Gestionar_Almacenn.lblValidarDetalleLiquyido, "El detalle no puede estar vacío");
    } else if (detalle.length() < 3) {
        mostrarError(Gestionar_Almacenn.lblValidarDetalleLiquyido, "Mínimo 3 caracteres");
    } else if (!esDetalleValido(detalle)) {
        mostrarError(Gestionar_Almacenn.lblValidarDetalleLiquyido, "Evita repeticiones o texto sin sentido");
    } else {
        ocultarError(Gestionar_Almacenn.lblValidarDetalleLiquyido);
    }
}
private void validarPrecioVentaLiquido() {
    String precio = Gestionar_Almacenn.txtPrecioVentaLiquido.getText().trim();
    if (precio.isEmpty()) {
        mostrarError(Gestionar_Almacenn.lblValidacionPrecioVentaLiquido, "El precio no puede estar vacío");
    } else {
        try {
            double prec = Double.parseDouble(precio);
            if (prec <= 0) {
                mostrarError(Gestionar_Almacenn.lblValidacionPrecioVentaLiquido, "Debe ser mayor a 0");
            } else {
                ocultarError(Gestionar_Almacenn.lblValidacionPrecioVentaLiquido);
            }
        } catch (NumberFormatException e) {
            mostrarError(Gestionar_Almacenn.lblValidacionPrecioVentaLiquido, "Precio inválido");
        }
    }
}
private void validarComboLiquido() {
    Object seleccionado = Gestionar_Almacenn.JComboxLiquido.getSelectedItem();
    if (seleccionado == null || seleccionado.toString().trim().isEmpty()
        || seleccionado.toString().equalsIgnoreCase("Seleccione")) {
        mostrarError(Gestionar_Almacenn.lblValidadarComboxLiquido, "Debe seleccionar un líquido");
    } else {
        ocultarError(Gestionar_Almacenn.lblValidadarComboxLiquido);
    }
}
private void ocultarTodosErroresSalidaLiquido() {
    ocultarError(Gestionar_Almacenn.lblValidadarComboxLiquido);
    ocultarError(Gestionar_Almacenn.lblValidarCantidadLiquido);
    ocultarError(Gestionar_Almacenn.lblValidarDetalleLiquyido);
    ocultarError(Gestionar_Almacenn.lblValidacionPrecioVentaLiquido);
}
public void RealizarSalidaLiquidoValidada() throws ClassNotFoundException, SQLException {
    // Validar todo
    validarComboLiquido();
    validarCantidadLiquido();
    validarDetalleLiquido();
    validarPrecioVentaLiquido();
    // Si hay errores, no continuar
    if (Gestionar_Almacenn.lblValidadarComboxLiquido.isVisible() ||
        Gestionar_Almacenn.lblValidarCantidadLiquido.isVisible() ||
        Gestionar_Almacenn.lblValidarDetalleLiquyido.isVisible() ||
        Gestionar_Almacenn.lblValidacionPrecioVentaLiquido.isVisible()) {
       
        JOptionPane.showMessageDialog(null, "Corrige los errores antes de registrar la salida.");
        return;
    }
    // Si todo está bien, proceder
    mv.Realizar_SALIDA_Liquido();
}
private void mostrarError(JLabel label, String mensaje) {
    label.setText(mensaje);
    label.setVisible(true);
}
private void ocultarError(JLabel label) {
    label.setText("");
    label.setVisible(false);
}
@FunctionalInterface
interface RunnableTask { void run(); }
static class SimpleDocumentListener implements DocumentListener {
    private final RunnableTask task;
    public SimpleDocumentListener(RunnableTask task) { this.task = task; }
    public void insertUpdate(DocumentEvent e) { task.run(); }
    public void removeUpdate(DocumentEvent e) { task.run(); }
    public void changedUpdate(DocumentEvent e) { task.run(); }
}
    
    // ------------------- INICIALIZAR VALIDACIONES SALIDA UNIDAD -------------------

}
