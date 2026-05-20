/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Validacion;

import Vista_GestionInventario.InventarioUnidad;
import ModeloDAO.InventarioUnidadDAO;
import java.sql.SQLException;
import java.util.Locale;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class Validar_Inventario_Unidad {

    private final InventarioUnidadDAO iv = new InventarioUnidadDAO();
    private static final double PRECIO_MAXIMO = 9999.99; // decimal(18,2)

    // ======================================================================
    // INICIALIZAR VALIDACIONES EN TIEMPO REAL
    // ======================================================================
    public void inicializarValidacionesUnidad() {
        ocultarTodosErrores();

        // Solo números enteros (máx 900)
        soloNumerosEnterosConLimite(InventarioUnidad.txtCantidadProducto, 99);
        soloNumerosEnterosConLimite(InventarioUnidad.txtStockMaximo, 99);
        soloNumerosEnterosConLimite(InventarioUnidad.txtStockMinimo, 99);
        soloNumerosEnterosConLimite(InventarioUnidad.txtPorcentaje, 30);

        // Formato dinero con punto fijo
        formatoDineroConPuntoFijo(InventarioUnidad.txtCostoUnitario);
        formatoDineroConPuntoFijo(InventarioUnidad.txtPrecioVenta);

        // Validaciones normales
        addKeyListener(InventarioUnidad.txtCantidadProducto, this::validarStockActual);
        addKeyListener(InventarioUnidad.txtStockMaximo, this::validarStockMaximo);
        addKeyListener(InventarioUnidad.txtStockMinimo, this::validarStockMinimo);
        addKeyListener(InventarioUnidad.txtPorcentaje, this::validarPorcentaje);
        addKeyListener(InventarioUnidad.txtCostoUnitario, this::validarCostoUnitario);

        // Precio Venta (aunque no editable) → valida cuando cambie
       InventarioUnidad.txtPrecioVenta.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { validarPrecioVenta(); }
            public void removeUpdate(DocumentEvent e) { validarPrecioVenta(); }
            public void changedUpdate(DocumentEvent e) { validarPrecioVenta(); }
        });

        // Validar Precio Venta cuando cambie costo o porcentaje
        addKeyListener(InventarioUnidad.txtCostoUnitario, this::validarPrecioVenta);
        addKeyListener(InventarioUnidad.txtPorcentaje, this::validarPrecioVenta);

        // Combos
        InventarioUnidad.jComboProductoUnidad.addActionListener(e -> validarProducto());
      InventarioUnidad.jComboBoxUbicacion.addActionListener(e -> validarAlmacen());
      
      InventarioUnidad.jComboBoxProveedor.addActionListener(e -> validarProveedorUnidad());
    }

    // ======================================================================
    // REGISTRAR PRODUCTO UNIDAD
    // ======================================================================
    public void ValidarRegistroUnidadInventario() throws ClassNotFoundException, SQLException {
        ocultarTodosErrores();
        boolean hayError = false;

        if (!validarProducto()) hayError = true;
        if (!validarStockActual()) hayError = true;
        if (!validarStockMaximo()) hayError = true;
        if (!validarStockMinimo()) hayError = true;
        if (!validarAlmacen()) hayError = true;
        if (!validarCostoUnitario()) hayError = true;
        if (!validarPrecioVenta()) hayError = true;
        if (!validarPorcentaje()) hayError = true;
        if (!validarProveedorUnidad()) hayError = true; {
            
        }

        if (hayError) {
            JOptionPane.showMessageDialog(null, "Corrige los campos en rojo", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        iv.RegistrarStockProductoUNIDAD();
        limpiarCamposRegistro();
       // JOptionPane.showMessageDialog(null, "Registrado con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    // ======================================================================
    // ACTUALIZAR DATOS BÁSICOS
    // ======================================================================
    public void ActualizarDatosUnidadInventario() throws ClassNotFoundException, SQLException {
        ocultarErroresBasicos();
        boolean hayError = false;

        if (!validarProducto()) hayError = true;
        if (!validarStockMaximo()) hayError = true;
        if (!validarStockMinimo()) hayError = true;
        if (!validarAlmacen()) hayError = true;

        if (hayError) {
            JOptionPane.showMessageDialog(null, "Corrige los campos en rojo", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        iv.ActualizarUNIDADSeguridad();
        //JOptionPane.showMessageDialog(null, "Datos actualizados", "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    // ======================================================================
    // ACTUALIZAR LOTE
    // ======================================================================
    public void ValidarActualizacionLoteUnida() throws ClassNotFoundException, SQLException {
        ocultarErroresLote();
        boolean hayError = false;

        if (!validarStockActual()) hayError = true;
        if (!validarCostoUnitario()) hayError = true;
        if (!validarPrecioVenta()) hayError = true;
        if (!validarPorcentaje()) hayError = true;
        if (!validarProveedorUnidad()) hayError = true; {
            
        }

        if (hayError) {
            JOptionPane.showMessageDialog(null, "Corrige los campos en rojo", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        iv.ActualizarLoteConseguridad();
     //   JOptionPane.showMessageDialog(null, "Lote actualizado", "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    // ======================================================================
    // VALIDACIONES EN TIEMPO REAL
    // ======================================================================
    private boolean validarProducto() {
        String sel = obtenerCombo(InventarioUnidad.jComboProductoUnidad);
        if (sel.isEmpty() || sel.equalsIgnoreCase("Seleccione")) {
            mostrarError(InventarioUnidad.lblValidaProductoUnidad, "Seleccione");
            return false;
        }
        ocultarError(InventarioUnidad.lblValidaProductoUnidad);
        return true;
    }

      
    private boolean validarProveedorUnidad() {
        Object sel = InventarioUnidad.jComboBoxProveedor.getSelectedItem();
        String prov = (sel != null) ? sel.toString().trim() : "";
        if (prov.isEmpty() || prov.equalsIgnoreCase("Seleccione")) {
            mostrarError(InventarioUnidad.lblValidarProveedorUnida, "Seleccione un proveedor");
        } else {
            ocultarError(InventarioUnidad.lblValidarProveedorUnida);
        }
        return true;
    }

    private boolean validarAlmacen() {
        String sel = obtenerCombo(InventarioUnidad.jComboBoxUbicacion);
        if (sel.isEmpty() || sel.equalsIgnoreCase("Seleccione")) {
            mostrarError(InventarioUnidad.lblValidarAlamcenUnidad, "Seleccione");
            return false;
        }
        ocultarError(InventarioUnidad.lblValidarAlamcenUnidad);
        return true;
    }

    private boolean validarStockActual() {
        String texto = InventarioUnidad.txtCantidadProducto.getText().trim();
        if (!esNumeroValido(texto, 1, 99)) {
            mostrarError(InventarioUnidad.lblValidarCantidaUnidad, "1 - 99");
            return false;
        }
        ocultarError(InventarioUnidad.lblValidarCantidaUnidad);
        return true;
    }

    private boolean validarStockMaximo() {
        String maxTexto = InventarioUnidad.txtStockMaximo.getText().trim();
        if (!esNumeroValido(maxTexto, 1, 99)) {
            mostrarError(InventarioUnidad.lblValidarStockMaximoUnida, "1 - 99");
            return false;
        }
        String actTexto = InventarioUnidad.txtCantidadProducto.getText().trim();
        if (!actTexto.isEmpty() && actTexto.matches("\\d+")) {
            int actual = Integer.parseInt(actTexto);
            int maximo = Integer.parseInt(maxTexto);
            if (maximo < actual) {
                mostrarError(InventarioUnidad.lblValidarStockMaximoUnida, "≥ Stock Actual");
                return false;
            }
        }
        ocultarError(InventarioUnidad.lblValidarStockMaximoUnida);
        return true;
    }

    private boolean validarStockMinimo() {
        String minTexto = InventarioUnidad.txtStockMinimo.getText().trim();
        if (!esNumeroValido(minTexto, 0, 99)) {
            mostrarError(InventarioUnidad.lblValidarStockMinimo, "0 - 99");
            return false;
        }
        String actTexto = InventarioUnidad.txtCantidadProducto.getText().trim();
        if (!actTexto.isEmpty() && actTexto.matches("\\d+")) {
            int actual = Integer.parseInt(actTexto);
            int minimo = Integer.parseInt(minTexto);
            if (minimo >= actual) {
                mostrarError(InventarioUnidad.lblValidarStockMinimo, "< Stock Actual");
                return false;
            }
        }
        ocultarError(InventarioUnidad.lblValidarStockMinimo);
        return true;
    }

    private boolean validarPorcentaje() {
        String texto = InventarioUnidad.txtPorcentaje.getText().trim();
        if (!esNumeroValido(texto, 0, 99)) {
            mostrarError(InventarioUnidad.lblValidarPorcentaje, "0 - 30");
            return false;
        }
        ocultarError(InventarioUnidad.lblValidarPorcentaje);
        return true;
    }

    private boolean validarCostoUnitario() {
        String texto = InventarioUnidad.txtCostoUnitario.getText().trim();
        if (!esPrecioValido(texto)) {
            mostrarError(InventarioUnidad.lblValidarCostoUnitario, "Máx 4 dígitos");
            return false;
        }
        ocultarError(InventarioUnidad.lblValidarCostoUnitario);
        return true;
    }

    private boolean validarPrecioVenta() {
        String texto = InventarioUnidad.txtPrecioVenta.getText().trim();
        if (!esPrecioValido(texto)) {
            mostrarError(InventarioUnidad.lblValidarPrecioVenta, "Máx 4 dígitos");
            return false;
        }
        ocultarError(InventarioUnidad.lblValidarPrecioVenta);
        return true;
    }

    // ======================================================================
    // FORMATO DINERO: SIEMPRE CON PUNTO
    // ======================================================================
    private void formatoDineroConPuntoFijo(JTextField campo) {
        campo.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent evt) {
                String texto = campo.getText().replace(",", ".").replaceAll("[^0-9.]", "").trim();
                if (texto.isEmpty() || texto.equals(".")) texto = "0";
                try {
                    double valor = Double.parseDouble(texto);
                    if (valor > PRECIO_MAXIMO) {
                        campo.setText("999999999999999999.99");
                    } else if (valor <= 0) {
                        campo.setText("0.00");
                    } else {
                        campo.setText(String.format(Locale.US, "%.2f", valor));
                    }
                } catch (Exception e) {
                    campo.setText("0.00");
                }
            }

            @Override
            public void focusGained(FocusEvent evt) {
                if (campo.getText().equals("0.00")) {
                    campo.setText("");
                }
            }
        });
    }

    // ======================================================================
    // FILTROS Y UTILIDADES
    // ======================================================================
    private void soloNumerosEnterosConLimite(JTextField campo, int limite) {
        ((AbstractDocument) campo.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                String nuevo = (fb.getDocument().getText(0, fb.getDocument().getLength()) + text).replaceAll("\\D", "");
                if (nuevo.isEmpty() || Integer.parseInt(nuevo) <= limite) {
                    super.replace(fb, offset, length, text.replaceAll("\\D", ""), attrs);

                }
            }

            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                String nuevo = (fb.getDocument().getText(0, fb.getDocument().getLength()) + string).replaceAll("\\D", "");
                if (nuevo.isEmpty() || Integer.parseInt(nuevo) <= limite) {
                    super.insertString(fb, offset, string.replaceAll("\\D", ""), attr);
                }
            }
        });
    }

    private boolean esNumeroValido(String texto, int min, int max) {
        if (texto == null || texto.trim().isEmpty()) return false;
        if (!texto.matches("\\d+")) return false;
        try {
            int valor = Integer.parseInt(texto);
            return valor >= min && valor <= max;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean esPrecioValido(String texto) {
        if (texto == null || texto.trim().isEmpty()) return false;
        texto = texto.replace(",", ".");
        if (!texto.matches("\\d+\\.?\\d{0,2}")) return false;
        try {
            double valor = Double.parseDouble(texto);
            return valor > 0 && valor <= PRECIO_MAXIMO;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private String obtenerCombo(JComboBox<?> combo) {
        Object sel = combo.getSelectedItem();
        return (sel != null) ? sel.toString().trim() : "";
    }

    private void mostrarError(JLabel lbl, String msg) {
        lbl.setText(msg);
        lbl.setForeground(new java.awt.Color(220, 0, 0));
        lbl.setVisible(true);
    }

    private void ocultarError(JLabel lbl) {
        lbl.setText("");
        lbl.setVisible(false);
    }

    private void ocultarTodosErrores() {
        ocultarError(InventarioUnidad.lblValidaProductoUnidad);
        ocultarError(InventarioUnidad.lblValidarCantidaUnidad);
        ocultarError(InventarioUnidad.lblValidarStockMaximoUnida);
        ocultarError(InventarioUnidad.lblValidarStockMinimo);
        ocultarError(InventarioUnidad.lblValidarAlamcenUnidad);
        ocultarError(InventarioUnidad.lblValidarCostoUnitario);
        ocultarError(InventarioUnidad.lblValidarPrecioVenta);
        ocultarError(InventarioUnidad.lblValidarPorcentaje);
        ocultarError(InventarioUnidad.lblValidarProveedorUnida);
    }

    private void ocultarErroresBasicos() {
       ocultarError(InventarioUnidad.lblValidaProductoUnidad);
      ocultarError(InventarioUnidad.lblValidarStockMaximoUnida);
       ocultarError(InventarioUnidad.lblValidarStockMinimo);
       ocultarError(InventarioUnidad.lblValidarAlamcenUnidad);
    }

    private void ocultarErroresLote() {
        ocultarError(InventarioUnidad.lblValidarCantidaUnidad);
        ocultarError(InventarioUnidad.lblValidarCostoUnitario);
       ocultarError(InventarioUnidad.lblValidarPrecioVenta);
          ocultarError(InventarioUnidad.lblValidarPorcentaje);
    }

    private void addKeyListener(JTextField campo, Runnable accion) {
        campo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) { accion.run(); }
        });
    }

    private void limpiarCamposRegistro() {
       InventarioUnidad.jComboProductoUnidad.setSelectedIndex(0);
        InventarioUnidad.jComboBoxUbicacion.setSelectedIndex(0);
        InventarioUnidad.txtCantidadProducto.setText("");
        InventarioUnidad.txtStockMaximo.setText("");
        InventarioUnidad.txtStockMinimo.setText("");
        InventarioUnidad.txtCostoUnitario.setText("0.00");
        InventarioUnidad.txtPrecioVenta.setText("0.00");
        //Gestion_Inventario.txtPorcentaje_UNIDAD.setText("");
    }
}