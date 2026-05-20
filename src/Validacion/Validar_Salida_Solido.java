/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Validacion;

import ModeloDAO.MovientosDao;

import Vista_Almacen.Gestionar_Almacenn;
import ModeloDAO.MovientosDao;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import javax.swing.JComboBox;
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
/**
 *
 * @author Adrian
 */
public class Validar_Salida_Solido {
    
    MovientosDao mv = new MovientosDao();
    private void limpiarActionListenersCombo(JComboBox<?> combo) {
    ActionListener[] listeners = combo.getActionListeners();
    for (ActionListener al : listeners) {
        combo.removeActionListener(al);
    }
}
    // ------------------- INICIALIZAR VALIDACIONES SALIDA SÓLIDO -------------------
public void inicializarValidacionesSalidaSolido() {
    // LIMPIAR listeners anteriores (evita choque con ajuste)
    limpiarActionListenersCombo(Gestionar_Almacenn.JComboxSOLIDO);

    ocultarTodosErroresSalidaSolido();

    // Filtros
    aplicarFiltroSoloNumerosEnteros(Gestionar_Almacenn.txtCantidadSolido);
    aplicarFiltroTextoConSentido(Gestionar_Almacenn.txtDatelleSolido);
    aplicarFiltroPrecio(Gestionar_Almacenn.txtPrecioVentaSOLIDONuevo);

    // Validación en tiempo real
    Gestionar_Almacenn.txtCantidadSolido.getDocument()
        .addDocumentListener(new SimpleDocumentListener(this::validarCantidadSolido));
    Gestionar_Almacenn.txtDatelleSolido.getDocument()
        .addDocumentListener(new SimpleDocumentListener(this::validarDetalleSolido));
    Gestionar_Almacenn.txtPrecioVentaSOLIDONuevo.getDocument()
        .addDocumentListener(new SimpleDocumentListener(this::validarPrecioVentaSolido));

    // Listener del combo (solo para salida)
    Gestionar_Almacenn.JComboxSOLIDO.addActionListener(e -> validarComboSolido());
}

// ------------------- FILTROS (REUTILIZABLES) -------------------
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
                super.insertString(fb, offset, string, attr); // Permitir siempre
            }
        }
        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                throws BadLocationException {
            if (text != null && text.matches("[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ., ]*")) {
                super.replace(fb, offset, length, text, attrs); // Permitir siempre
            }
        }
    });
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

// ------------------- VALIDACIONES EN TIEMPO REAL -------------------
private void validarCantidadSolido() {
    String cantidad = Gestionar_Almacenn.txtCantidadSolido.getText().trim();
    if (cantidad.isEmpty()) {
        mostrarError(Gestionar_Almacenn.lblValidarCantidadSolido, "La cantidad no puede estar vacía");
    } else {
        try {
            int cant = Integer.parseInt(cantidad);
            if (cant <= 0) {
                mostrarError(Gestionar_Almacenn.lblValidarCantidadSolido, "Debe ser mayor a 0");
            } else {
                ocultarError(Gestionar_Almacenn.lblValidarCantidadSolido);
            }
        } catch (NumberFormatException e) {
            mostrarError(Gestionar_Almacenn.lblValidarCantidadSolido, "Número inválido");
        }
    }
}

private void validarDetalleSolido() {
    String detalle = Gestionar_Almacenn.txtDatelleSolido.getText().trim();
    if (detalle.isEmpty()) {
        mostrarError(Gestionar_Almacenn.lblValidarDetalleSOLIDO, "El detalle no puede estar vacío");
        return;
    }
    if (detalle.length() < 3) {
        mostrarError(Gestionar_Almacenn.lblValidarDetalleSOLIDO, "Mínimo 3 caracteres");
        return;
    }
    if (detalle.matches(".*(.)\\1{3}.*")) {
        mostrarError(Gestionar_Almacenn.lblValidarDetalleSOLIDO, "Evita repeticiones (ej: aaaa, 1111, ....)");
        return;
    }
    if (detalle.matches("[., ]+")) {
        mostrarError(Gestionar_Almacenn.lblValidarDetalleSOLIDO, "Escribe un detalle con sentido");
        return;
    }
    if (!detalle.matches(".*[a-zA-Z0-9].*")) {
        mostrarError(Gestionar_Almacenn.lblValidarDetalleSOLIDO, "Debe contener al menos una letra o número");
        return;
    }
    ocultarError(Gestionar_Almacenn.lblValidarDetalleSOLIDO);
}

private void validarPrecioVentaSolido() {
    String precio = Gestionar_Almacenn.txtPrecioVentaSOLIDONuevo.getText().trim();
    if (precio.isEmpty()) {
        mostrarError(Gestionar_Almacenn.lblValidacionPrecioVentaSolido, "El precio no puede estar vacío");
    } else {
        try {
            double prec = Double.parseDouble(precio);
            if (prec <= 0) {
                mostrarError(Gestionar_Almacenn.lblValidacionPrecioVentaSolido, "Debe ser mayor a 0");
            } else {
                ocultarError(Gestionar_Almacenn.lblValidacionPrecioVentaSolido);
            }
        } catch (NumberFormatException e) {
            mostrarError(Gestionar_Almacenn.lblValidacionPrecioVentaSolido, "Precio inválido");
        }
    }
}

private void validarComboSolido() {
    Object seleccionado = Gestionar_Almacenn.JComboxSOLIDO.getSelectedItem();
    String valor = (seleccionado != null) ? seleccionado.toString().trim() : "";
    if (valor.isEmpty() || valor.equalsIgnoreCase("Seleccione")) {
        mostrarError(Gestionar_Almacenn.lblValidadarComboxSolido, "Debe seleccionar un producto sólido");
    } else {
        ocultarError(Gestionar_Almacenn.lblValidadarComboxSolido);
    }
}

// ------------------- OCULTAR ERRORES -------------------
private void ocultarTodosErroresSalidaSolido() {
    ocultarError(Gestionar_Almacenn.lblValidadarComboxSolido);
    ocultarError(Gestionar_Almacenn.lblValidarCantidadSolido);
    ocultarError(Gestionar_Almacenn.lblValidarDetalleSOLIDO);
    ocultarError(Gestionar_Almacenn.lblValidacionPrecioVentaSolido);
}

// ------------------- MÉTODO FINAL: REGISTRAR SALIDA -------------------
public void ValidarSalidaSolido() throws ClassNotFoundException, SQLException {
    // Forzar validación
    validarComboSolido();
    validarCantidadSolido();
    validarDetalleSolido();
    validarPrecioVentaSolido();

    // Si hay errores, no continuar
    if (Gestionar_Almacenn.lblValidadarComboxSolido.isVisible() ||
        Gestionar_Almacenn.lblValidarCantidadSolido.isVisible() ||
        Gestionar_Almacenn.lblValidarDetalleSOLIDO.isVisible() ||
        Gestionar_Almacenn.lblValidacionPrecioVentaSolido.isVisible()) {

        JOptionPane.showMessageDialog(null, "Corrige los errores antes de registrar la salida.");
        return;
    }

    // Si todo bien → proceder
    mv.Realizar_SALIDA_SOLIDO();
}

// ------------------- MÉTODOS AUXILIARES -------------------
private void mostrarError(JLabel label, String mensaje) {
    label.setText(mensaje);
    label.setVisible(true);
}

private void ocultarError(JLabel label) {
    label.setText("");
    label.setVisible(false);
}

// ------------------- SimpleDocumentListener -------------------
@FunctionalInterface
interface RunnableTask { void run(); }

static class SimpleDocumentListener implements DocumentListener {
    private final RunnableTask task;
    public SimpleDocumentListener(RunnableTask task) { this.task = task; }
    public void insertUpdate(DocumentEvent e) { task.run(); }
    public void removeUpdate(DocumentEvent e) { task.run(); }
    public void changedUpdate(DocumentEvent e) { task.run(); }
}
    
    
}
