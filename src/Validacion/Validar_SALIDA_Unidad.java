/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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

public class Validar_SALIDA_Unidad {
    
    
     
    MovientosDao mv = new MovientosDao();
    
     // ------------------- INICIALIZAR VALIDACIONES SALIDA UNIDAD -------------------
public void inicializarValidacionesSalidaUnidad() {
    // Ocultar todos los errores al inicio
    ocultarTodosErroresSalidaUnidad();

    // === FILTROS DE ENTRADA ===
    aplicarFiltroSoloNumerosEnteros(Gestionar_Almacenn.txtCantidadUnidad);
    aplicarFiltroPrecio(Gestionar_Almacenn.txtPrecioVentaUnidad);
    aplicarFiltroTextoConSentido(Gestionar_Almacenn.txtDatelleUnidad);

    // === VALIDACIÓN EN TIEMPO REAL ===
    Gestionar_Almacenn.txtCantidadUnidad.getDocument()
        .addDocumentListener(new SimpleDocumentListener(this::validarCantidadUnidad));

    Gestionar_Almacenn.txtPrecioVentaUnidad.getDocument()
        .addDocumentListener(new SimpleDocumentListener(this::validarPrecioVentaUnidad));

    Gestionar_Almacenn.txtDatelleUnidad.getDocument()
        .addDocumentListener(new SimpleDocumentListener(this::validarDetalleUnidad));

    Gestionar_Almacenn.JComboxSUnidad.addActionListener(e -> validarComboUnidad());
}

// ------------------- FILTROS DE ENTRADA -------------------
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

// ------------------- VALIDACIONES EN TIEMPO REAL -------------------
private void validarCantidadUnidad() {
    String cantidad = Gestionar_Almacenn.txtCantidadUnidad.getText().trim();
    if (cantidad.isEmpty()) {
        mostrarError(Gestionar_Almacenn.lblValidarCantidadAjusteSolido, "La cantidad no puede estar vacía");
    } else {
        try {
            int cant = Integer.parseInt(cantidad);
            if (cant <= 0) {
                mostrarError(Gestionar_Almacenn.lblValidarCantidadAjusteSolido, "Debe ser mayor a 0");
            } else {
                ocultarError(Gestionar_Almacenn.lblValidarCantidadAjusteSolido);
            }
        } catch (NumberFormatException e) {
            mostrarError(Gestionar_Almacenn.lblValidarCantidadAjusteSolido, "Número inválido");
        }
    }
}

private void validarPrecioVentaUnidad() {
    String precio = Gestionar_Almacenn.txtPrecioVentaUnidad.getText().trim();
    if (precio.isEmpty()) {
        mostrarError(Gestionar_Almacenn.lblValidacionPrecioVentaUnidad, "El precio no puede estar vacío");
    } else {
        try {
            double prec = Double.parseDouble(precio);
            if (prec <= 0) {
                mostrarError(Gestionar_Almacenn.lblValidacionPrecioVentaUnidad, "Debe ser mayor a 0");
            } else {
                ocultarError(Gestionar_Almacenn.lblValidacionPrecioVentaUnidad);
            }
        } catch (NumberFormatException e) {
            mostrarError(Gestionar_Almacenn.lblValidacionPrecioVentaUnidad, "Precio inválido");
        }
    }
}

private void validarDetalleUnidad() {
    String detalle = Gestionar_Almacenn.txtDatelleUnidad.getText().trim();

    if (detalle.isEmpty()) {
        mostrarError(Gestionar_Almacenn.lblValidarDetalleUnidad, "El detalle no puede estar vacío");
        return;
    }

    if (detalle.length() < 3) {
        mostrarError(Gestionar_Almacenn.lblValidarDetalleUnidad, "Mínimo 3 caracteres");
        return;
    }

    // Detectar repeticiones inusuales: 4 o más caracteres iguales
    if (detalle.matches(".*(.)\\1{3}.*")) {
        mostrarError(Gestionar_Almacenn.lblValidarDetalleUnidad, "Evita repeticiones (ej: aaaa, 1111, ....)");
        return;
    }

    // Solo símbolos sin sentido
    if (detalle.matches("[., ]+")) {
        mostrarError(Gestionar_Almacenn.lblValidarDetalleUnidad, "Escribe un detalle con sentido");
        return;
    }

    // Al menos una letra o número
    if (!detalle.matches(".*[a-zA-Z0-9].*")) {
        mostrarError(Gestionar_Almacenn.lblValidarDetalleUnidad, "Debe contener al menos una letra o número");
        return;
    }

    ocultarError(Gestionar_Almacenn.lblValidarDetalleUnidad);
}

private void validarComboUnidad() {
    Object seleccionado = Gestionar_Almacenn.JComboxSUnidad.getSelectedItem();
    String valor = (seleccionado != null) ? seleccionado.toString().trim() : "";
    if (valor.isEmpty() || valor.equalsIgnoreCase("Seleccione")) {
        mostrarError(Gestionar_Almacenn.lblValidadarComboxUnidad, "Debe seleccionar un producto");
    } else {
        ocultarError(Gestionar_Almacenn.lblValidadarComboxUnidad);
    }
}

// ------------------- OCULTAR TODOS LOS ERRORES -------------------
private void ocultarTodosErroresSalidaUnidad() {
    ocultarError(Gestionar_Almacenn.lblValidadarComboxUnidad);
    ocultarError(Gestionar_Almacenn.lblValidarCantidadAjusteSolido);
    ocultarError(Gestionar_Almacenn.lblValidarDetalleUnidad);
    ocultarError(Gestionar_Almacenn.lblValidacionPrecioVentaUnidad);
}

// ------------------- MÉTODO PRINCIPAL: REALIZAR SALIDA -------------------
public void ValidarSalidaUNIdad() throws ClassNotFoundException, SQLException {
    // Forzar validación completa
    validarComboUnidad();
    validarCantidadUnidad();
    validarPrecioVentaUnidad();
    validarDetalleUnidad();

    // Verificar si hay errores visibles
    if (Gestionar_Almacenn.lblValidadarComboxUnidad.isVisible() ||
        Gestionar_Almacenn.lblValidarCantidadAjusteSolido.isVisible() ||
        Gestionar_Almacenn.lblValidarDetalleUnidad.isVisible() ||
        Gestionar_Almacenn.lblValidacionPrecioVentaUnidad.isVisible()) {

        JOptionPane.showMessageDialog(null, "Corrige los errores antes de registrar la salida.");
        return;
    }

    // Si todo está bien → proceder
    mv.Realizar_SALIDA_Unidad();
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
