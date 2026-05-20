/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Validacion;

import Vista_GestionInventario.InventarioSolido;

import java.sql.SQLException;
import java.util.Date;
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
import ModeloDAO.InventarioSolidoDAO;
public class validar_inventario_solido {

    private final InventarioSolidoDAO iv = new InventarioSolidoDAO();
    private static final double PRECIO_MAXIMO = 9999.99; // decimal(18,2)

    // ======================================================================
    // INICIALIZAR VALIDACIONES EN TIEMPO REAL
    // ======================================================================
    public void inicializarValidacionesSolido() {
        ocultarTodosErrores();

        // Solo números enteros (máx 900)
        soloNumerosEnterosConLimite(InventarioSolido.txtCantidadSolido, 99);
        soloNumerosEnterosConLimite(InventarioSolido.txtStoxkMaximo, 99);
        soloNumerosEnterosConLimite(InventarioSolido.txtStockMinimo, 99);
        soloNumerosEnterosConLimite(InventarioSolido.txtPorcentajeSolido, 30);

        // Formato dinero con punto fijo
        formatoDineroConPuntoFijo(InventarioSolido.txtCostoUUnitario);
        formatoDineroConPuntoFijo(InventarioSolido.txtPrecioVentaSolido);

        // Validaciones normales
        addKeyListener(InventarioSolido.txtCantidadSolido, this::validarStockActual);
        addKeyListener(InventarioSolido.txtStoxkMaximo, this::validarStockMaximo);
        addKeyListener(InventarioSolido.txtStockMinimo, this::validarStockMinimo);
        addKeyListener(InventarioSolido.txtPorcentajeSolido, this::validarPorcentaje);
        addKeyListener(InventarioSolido.txtCostoUUnitario, this::validarCostoUnitario);

        // Precio Venta (aunque no editable) → valida cuando cambie
       InventarioSolido.txtPrecioVentaSolido.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { validarPrecioVenta(); }
            public void removeUpdate(DocumentEvent e) { validarPrecioVenta(); }
            public void changedUpdate(DocumentEvent e) { validarPrecioVenta(); }
        });

        // Validar Precio Venta cuando cambie costo o porcentaje
        addKeyListener(InventarioSolido.txtCostoUUnitario, this::validarPrecioVenta);
        addKeyListener(InventarioSolido.txtPorcentajeSolido, this::validarPrecioVenta);

        // Combos
      InventarioSolido.jComboProductoSolido.addActionListener(e -> validarProducto());
        InventarioSolido.jComboUbicacionSolido.addActionListener(e -> validarAlmacen());
        InventarioSolido.jComboBoxProveedor.addActionListener(e -> validarProveedorSolido());
    }

    // ======================================================================
    // REGISTRAR
    // ======================================================================
    public void ValidarRegistroSolido() throws ClassNotFoundException, SQLException {
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
        if (!validarProveedorSolido()) hayError = true; {
            
        }

        if (hayError) {
            JOptionPane.showMessageDialog(null, "Corrige los campos en rojo", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        iv.RegistrarStockProductoSolido();
        limpiarCamposRegistro();
      //  JOptionPane.showMessageDialog(null, "Registrado con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    // ======================================================================
    // ACTUALIZAR DATOS BÁSICOS
    // ======================================================================
    public void ValidarActualizacionDatosSolido() throws ClassNotFoundException, SQLException {
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

        iv. ActualizarProductoSolidoSeguridad();
       // JOptionPane.showMessageDialog(null, "Actualizado", "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    // ======================================================================
    // ACTUALIZAR LOTE
    // ======================================================================
    public void ValidarDatosLoteSolido() throws ClassNotFoundException, SQLException {
        ocultarErroresLote();
        boolean hayError = false;

        if (!validarStockActual()) hayError = true;
        if (!validarCostoUnitario()) hayError = true;
        if (!validarPrecioVenta()) hayError = true;
        if (!validarPorcentaje()) hayError = true;
        if (!validarProveedorSolido()) hayError = true; {
            
        }

        if (hayError) {
            JOptionPane.showMessageDialog(null, "Corrige los campos en rojo", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        iv.ActualizarLoteSolidoSeguridad();
       // JOptionPane.showMessageDialog(null, "Lote actualizado", "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    // ======================================================================
    // VALIDACIONES EN TIEMPO REAL
    // ======================================================================
    private boolean validarProducto() {
        String sel = obtenerCombo(InventarioSolido.jComboProductoSolido);
        if (sel.isEmpty() || sel.equalsIgnoreCase("Seleccione")) {
            mostrarError(InventarioSolido.lblValidarProductoSolido, "Seleccione");
            return false;
        }
        ocultarError(InventarioSolido.lblValidarProductoSolido);
        return true;
    }

    private boolean validarAlmacen() {
        String sel = obtenerCombo(InventarioSolido.jComboUbicacionSolido);
        if (sel.isEmpty() || sel.equalsIgnoreCase("Seleccione")) {
            mostrarError(InventarioSolido.lblValidarAlmacenSolido, "Seleccione");
            return false;
        }
        ocultarError(InventarioSolido.lblValidarAlmacenSolido);
        return true;
    }
   private boolean validarProveedorSolido() {
        Object sel = InventarioSolido.jComboBoxProveedor.getSelectedItem();
        String prov = (sel != null) ? sel.toString().trim() : "";
        if (prov.isEmpty() || prov.equalsIgnoreCase("Seleccione")) {
            mostrarError(InventarioSolido.lblValidarProveedor, "Seleccione un proveedor");
        } else {
            ocultarError(InventarioSolido.lblValidarProveedor);
        }
        return true;
    }

    private boolean validarStockActual() {
        String texto = InventarioSolido.txtCantidadSolido.getText().trim();
        if (!esNumeroValido(texto, 1, 99)) {
            mostrarError(InventarioSolido.lblValidarCantidadActual, "1 - 99");
            return false;
        }
        ocultarError(InventarioSolido.lblValidarCantidadActual);
        return true;
    }

    private boolean validarStockMaximo() {
        String maxTexto = InventarioSolido.txtStoxkMaximo.getText().trim();
        if (!esNumeroValido(maxTexto, 1,99)) {
            mostrarError(InventarioSolido.lblValidarStocMaximo, "1 - 99");
            return false;
        }
        String actTexto = InventarioSolido.txtCantidadSolido.getText().trim();
        if (!actTexto.isEmpty() && actTexto.matches("\\d+")) {
            int actual = Integer.parseInt(actTexto);
            int maximo = Integer.parseInt(maxTexto);
            if (maximo < actual) {
                mostrarError(InventarioSolido.lblValidarStocMaximo, "≥ Stock Actual");
                return false;
            }
        }
        ocultarError(InventarioSolido.lblValidarStocMaximo);
        return true;
    }

    private boolean validarStockMinimo() {
        String minTexto = InventarioSolido.txtStockMinimo.getText().trim();
        if (!esNumeroValido(minTexto, 0, 99)) {
            mostrarError(InventarioSolido.lblValidarStockMinimo, "0 - 99");
            return false;
        }
        String actTexto = InventarioSolido.txtCantidadSolido.getText().trim();
        if (!actTexto.isEmpty() && actTexto.matches("\\d+")) {
            int actual = Integer.parseInt(actTexto);
            int minimo = Integer.parseInt(minTexto);
            if (minimo >= actual) {
                mostrarError(InventarioSolido.lblValidarStockMinimo, "< Stock Actual");
                return false;
            }
        }
        ocultarError(InventarioSolido.lblValidarStockMinimo);
        return true;
    }

    private boolean validarPorcentaje() {
        String texto = InventarioSolido.txtPorcentajeSolido.getText().trim();
        if (!esNumeroValido(texto, 0, 30)) {
            mostrarError(InventarioSolido.lblValidarPorcentaje, "0 - 30");
            return false;
        }
        ocultarError(InventarioSolido.lblValidarPorcentaje);
        return true;
    }

    private boolean validarCostoUnitario() {
        String texto = InventarioSolido.txtCostoUUnitario.getText().trim();
        if (!esPrecioValido(texto)) {
            mostrarError(InventarioSolido.lblValidarCostoUnitario, "Máx 4 dígitos");
            return false;
        }
        ocultarError(InventarioSolido.lblValidarCostoUnitario);
        return true;
    }

    private boolean validarPrecioVenta() {
        String texto = InventarioSolido.txtPrecioVentaSolido.getText().trim();
        if (!esPrecioValido(texto)) {
            mostrarError(InventarioSolido.lblValidarPrecioVenta, "Máx 4 dígitos");
            return false;
        }
        ocultarError(InventarioSolido.lblValidarPrecioVenta);
        return true;
    }

    // ======================================================================
    // FORMATO DINERO: SIEMPRE PUNTO
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
        ocultarError(InventarioSolido.lblValidarProductoSolido);
        ocultarError(InventarioSolido.lblValidarCantidadActual);
        ocultarError(InventarioSolido.lblValidarStocMaximo);
        ocultarError(InventarioSolido.lblValidarStockMinimo);
        ocultarError(InventarioSolido.lblValidarAlmacenSolido);
        ocultarError(InventarioSolido.lblValidarCostoUnitario);
        ocultarError(InventarioSolido.lblValidarPrecioVenta);
        ocultarError(InventarioSolido.lblValidarPorcentaje);
        ocultarError(InventarioSolido.lblValidarProveedor);
    }

    private void ocultarErroresBasicos() {
        ocultarError(InventarioSolido.lblValidarProductoSolido);
        ocultarError(InventarioSolido.lblValidarStocMaximo);
        ocultarError(InventarioSolido.lblValidarStockMinimo);
        ocultarError(InventarioSolido.lblValidarAlmacenSolido);
    }

    private void ocultarErroresLote() {
        ocultarError(InventarioSolido.lblValidarCantidadActual);
        ocultarError(InventarioSolido.lblValidarCostoUnitario);
        ocultarError(InventarioSolido.lblValidarPrecioVenta);
      ocultarError(InventarioSolido.lblValidarProveedor);
        ocultarError(InventarioSolido.lblValidarPorcentaje);
    }

    private void addKeyListener(JTextField campo, Runnable accion) {
        campo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) { accion.run(); }
        });
    }

    private void limpiarCamposRegistro() {
     InventarioSolido.jComboProductoSolido.setSelectedIndex(0);
   InventarioSolido.jComboUbicacionSolido.setSelectedIndex(0);
        InventarioSolido.txtCantidadSolido.setText("");
      InventarioSolido.txtStoxkMaximo.setText("");
       InventarioSolido.txtStockMinimo.setText("");
        InventarioSolido.txtCostoUUnitario.setText("0.00");
        InventarioSolido.txtPrecioVentaSolido.setText("0.00");
       
        InventarioSolido.jComboBoxProveedor.setSelectedIndex(0);
    }
}