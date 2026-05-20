package Validacion;


import ModeloDAO.InventarioUnidadDAO;
import java.sql.SQLException;
import java.util.Date;
import java.util.Locale;
import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import java.beans.PropertyChangeListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import Vista_GestionInventario.InventarioLiquido;
import ModeloDAO.InventarioLiquidoDao;
public class Validar_Inventario_Liquido {

  private  final  InventarioLiquidoDao inventarioLiquidoDao = new InventarioLiquidoDao();
    private static final double PRECIO_MAXIMO = 9999.99; // decimal(18,2)

    // ======================================================================
    // INICIALIZAR VALIDACIONES
    // ======================================================================
    public void inicializarValidacionesLiquido() {
        ocultarTodosErrores();

        soloNumerosEnterosConLimite(InventarioLiquido.txtCantidadActual, 99);
        soloNumerosEnterosConLimite(InventarioLiquido.txtStockMaximo, 99);
        soloNumerosEnterosConLimite(InventarioLiquido.txtStockMinimo, 99);
        soloNumerosEnterosConLimite(InventarioLiquido.txtPorcentaje, 30);

        // FORZAMOS SIEMPRE PUNTO (.) → 10.00 NUNCA 10,00
        formatoDineroConPuntoFijo(InventarioLiquido.txtCostoUnitario);
        formatoDineroConPuntoFijo(InventarioLiquido.txtPrecioVenta);

        addKeyListener(InventarioLiquido.txtCantidadActual, this::validarStockActual);
        addKeyListener(InventarioLiquido.txtStockMaximo, this::validarStockMaximo);
        addKeyListener(InventarioLiquido.txtStockMinimo, this::validarStockMinimo);
        addKeyListener(InventarioLiquido.txtPorcentaje, this::validarPorcentaje);
        addKeyListener(InventarioLiquido.txtCostoUnitario, this::validarCostoUnitario);
        addKeyListener(InventarioLiquido.txtPrecioVenta, this::validarPrecioVenta);

      InventarioLiquido.jComboProductoLiquido.addActionListener(e -> validarProducto());
        InventarioLiquido.jComboUbicacion.addActionListener(e -> validarAlmacen());
        InventarioLiquido.jComboProveedor.addActionListener(e -> validarProveedorLiquido());
        // Reemplaza las líneas actuales de fecha por estas:
InventarioLiquido.jDateFechaVencimiento.getDateEditor().getUiComponent()
        .addPropertyChangeListener("text", evt -> validarFechaVencimiento());

InventarioLiquido.jDateFechaVencimiento.addPropertyChangeListener("date", evt -> validarFechaVencimiento());

// Listener al perder foco (útil)
InventarioLiquido.jDateFechaVencimiento.getDateEditor().getUiComponent()
        .addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                validarFechaVencimiento();
            }
        });
        
        // ======================================================================
// VALIDACIÓN EN TIEMPO REAL PARA CAMPOS CALCULADOS Y FECHA
// ======================================================================

// 1. VALIDAR PRECIO VENTA aunque no sea editable (se calcula automáticamente)
    InventarioLiquido.txtPrecioVenta.getDocument().addDocumentListener(new DocumentListener() {
    @Override public void insertUpdate(DocumentEvent e) { validarPrecioVenta(); }
    @Override public void removeUpdate(DocumentEvent e) { validarPrecioVenta(); }
    @Override public void changedUpdate(DocumentEvent e) { validarPrecioVenta(); }
});

// 2. VALIDAR FECHA DE VENCIMIENTO EN TIEMPO REAL (mejorado)
InventarioLiquido.jDateFechaVencimiento.getDateEditor().getUiComponent()
    .addFocusListener(new FocusAdapter() {
        @Override
        public void focusLost(FocusEvent e) {
            validarFechaVencimiento();
        }
    });

InventarioLiquido.jDateFechaVencimiento.addPropertyChangeListener("date", evt -> validarFechaVencimiento());

// 3. También validar cuando cambie el % o costo (por si afecta el precio venta)
addKeyListener(InventarioLiquido.txtPorcentaje, this::validarPrecioVenta);
addKeyListener(InventarioLiquido.txtCostoUnitario, this::validarPrecioVenta);
    }

    // ======================================================================
    // REGISTRO / ACTUALIZAR
    // ======================================================================
  public void ValidarRegistroDeEntradaLiquido() throws ClassNotFoundException, SQLException {
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
       if (!validarFechaVencimiento()) hayError = true;
        if (!validarProveedorLiquido()) hayError = true;
         {
           
        }
        if (hayError) {
            JOptionPane.showMessageDialog(null, "Corrige los campos en rojo", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
      inventarioLiquidoDao.RegistrarStockProductoLiquido();
      
    
       // JOptionPane.showMessageDialog(null, "Registrado con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }
 
    public void ActualizarEndradaLiquido() throws ClassNotFoundException, SQLException {
        ocultarErroresBasicos();
        if (!validarProducto() || !validarStockMaximo() || !validarStockMinimo() || !validarAlmacen()) {
            JOptionPane.showMessageDialog(null, "Corrige los campos en rojo", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
       inventarioLiquidoDao.ActualizarConseguridadLiquido();
       // 
    }

    public void ActualizarDatosLoteLiquido() throws ClassNotFoundException, SQLException {
        ocultarErroresLote();
        if (!validarStockActual() || !validarCostoUnitario() || !validarPrecioVenta() || !validarPorcentaje() || !validarFechaVencimiento() || !validarProveedorLiquido()  ) {
            JOptionPane.showMessageDialog(null, "Corrige los campos en rojo", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        inventarioLiquidoDao.ActualizarLoteConseguridad();
        InventarioLiquido.txtNumeroFACTURA.setText("");
    InventarioLiquido.jCheckBoxiIVA.setSelected(false);
     InventarioLiquido.txtPorcentajeIVA.setVisible(false);
        //JOptionPane.showMessageDialog(null, "Lote actualizado", "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    // ======================================================================
    // VALIDACIONES EN TIEMPO REAL
    // ======================================================================
    private boolean validarProducto() {
        String sel = obtenerCombo(InventarioLiquido.jComboProductoLiquido);
        if (sel.isEmpty() || sel.equalsIgnoreCase("Seleccione")) {
            mostrarError(InventarioLiquido.lblValidarProducto, "Seleccione");
            return false;
        }
        ocultarError(InventarioLiquido.lblValidarProducto);
        return true;
    }
    
    
    
    private boolean validarProveedorLiquido() {
        Object sel = InventarioLiquido.jComboProveedor.getSelectedItem();
        String prov = (sel != null) ? sel.toString().trim() : "";
        if (prov.isEmpty() || prov.equalsIgnoreCase("Seleccione")) {
            mostrarError(InventarioLiquido.lblValidarProveedor, "Seleccione un proveedor");
        } else {
            ocultarError(InventarioLiquido.lblValidarProveedor);
        }
        return true;
    }


    private boolean validarAlmacen() {
        String sel = obtenerCombo(InventarioLiquido.jComboUbicacion);
        if (sel.isEmpty() || sel.equalsIgnoreCase("Seleccione")) {
            mostrarError(InventarioLiquido.lblValidarUbicacion, "Seleccione");
            return false;
        }
        ocultarError(InventarioLiquido.lblValidarUbicacion);
        return true;
    }

    private boolean validarStockActual() {
        String texto = InventarioLiquido.txtCantidadActual.getText().trim();
        if (!esNumeroValido(texto, 1, 99)) {
            mostrarError(InventarioLiquido.lblValidarCantidad, "1 - 99");
            return false;
        }
        ocultarError(InventarioLiquido.lblValidarCantidad);
        return true;
    }

    private boolean validarStockMaximo() {
        String maxTexto = InventarioLiquido.txtStockMaximo.getText().trim();
        if (!esNumeroValido(maxTexto, 1, 99)) {
            mostrarError(InventarioLiquido.lblValidadStockMaximo, "1 - 99");
            return false;
        }
        String actTexto = InventarioLiquido.txtCantidadActual.getText().trim();
        if (!actTexto.isEmpty() && actTexto.matches("\\d+")) {
            int actual = Integer.parseInt(actTexto);
            int maximo = Integer.parseInt(maxTexto);
            if (maximo < actual) {
                mostrarError(InventarioLiquido.lblValidadStockMaximo, "≥ Stock Actual");
                return false;
            }
        }
        ocultarError(InventarioLiquido.lblValidadStockMaximo);
        return true;
    }

    private boolean validarStockMinimo() {
        String minTexto = InventarioLiquido.txtStockMinimo.getText().trim();
        if (!esNumeroValido(minTexto, 0, 99)) {
            mostrarError(InventarioLiquido.lblValidarStockMinimo, "0 - 99");
            return false;
        }
        String actTexto = InventarioLiquido.txtCantidadActual.getText().trim();
        if (!actTexto.isEmpty() && actTexto.matches("\\d+")) {
            int actual = Integer.parseInt(actTexto);
            int minimo = Integer.parseInt(minTexto);
            if (minimo >= actual) {
                mostrarError(InventarioLiquido.lblValidarStockMinimo, "< Stock Actual");
                return false;
            }
        }
        ocultarError(InventarioLiquido.lblValidarStockMinimo);
        return true;
    }

    private boolean validarPorcentaje() {
        String texto = InventarioLiquido.txtPorcentaje.getText().trim();
        if (!esNumeroValido(texto, 0, 30)) {
            mostrarError(InventarioLiquido.lblValidarPorcentaje, "0 - 30");
            return false;
        }
        ocultarError(InventarioLiquido.lblValidarPorcentaje);
        return true;
    }

    private boolean validarCostoUnitario() {
        String texto = InventarioLiquido.txtCostoUnitario.getText().trim();
        if (!esPrecioDecimal18_2Valido(texto)) {
            mostrarError(InventarioLiquido.lblvValidarCostoUnitario, "Máx 4 dígitos");
            return false;
        }
        ocultarError(InventarioLiquido.lblvValidarCostoUnitario);
        return true;
    }

    private boolean validarPrecioVenta() {
        String texto = InventarioLiquido.txtPrecioVenta.getText().trim();
        if (!esPrecioDecimal18_2Valido(texto)) {
            mostrarError(InventarioLiquido.lblValidarPrecioVenta, "Máx 4 dígitos");
            return false;
        }
        ocultarError(InventarioLiquido.lblValidarPrecioVenta);
        return true;
    }

    private boolean validarFechaVencimiento() {
        Date fecha = InventarioLiquido.jDateFechaVencimiento.getDate();
        Date hoy = new Date();
        if (fecha == null || !fecha.after(hoy)) {
            mostrarError(InventarioLiquido.lblValidarFechaVencimiento, "Fecha futura");
            return false;
        }
        ocultarError(InventarioLiquido.lblValidarFechaVencimiento);
        return true;
    }


    // ======================================================================
    // FORMATO DINERO: SIEMPRE CON PUNTO (.) → 10.00
    // ======================================================================
    private void formatoDineroConPuntoFijo(JTextField campo) {
        campo.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                String texto = campo.getText()
                        .replace(",", ".")                     // Acepta coma del teclado
                        .replaceAll("[^0-9.]", "")
                        .trim();

                if (texto.isEmpty() || texto.equals(".")) texto = "0";

                try {
                    double valor = Double.parseDouble(texto);
                    if (valor > PRECIO_MAXIMO) {
                        campo.setText("9999.99");
                    } else if (valor <= 0) {
                        campo.setText("0.00");
                    } else {
                        campo.setText(String.format(Locale.US, "%.2f", valor)); // FORZAMOS PUNTO
                    }
                } catch (Exception e) {
                    campo.setText("0.00");
                }
            }

            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
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

    private boolean esPrecioDecimal18_2Valido(String texto) {
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
        ocultarError(InventarioLiquido.lblValidarProducto);
        ocultarError(InventarioLiquido.lblValidarCantidad);
        ocultarError(InventarioLiquido.lblValidadStockMaximo);
        ocultarError(InventarioLiquido.lblValidarStockMinimo);
        ocultarError(InventarioLiquido.lblValidarUbicacion);
        ocultarError(InventarioLiquido.lblvValidarCostoUnitario);
        ocultarError(InventarioLiquido.lblValidarPrecioVenta);
        ocultarError(InventarioLiquido.lblValidarPorcentaje);
        ocultarError(InventarioLiquido.lblValidarFechaVencimiento);
        ocultarError(InventarioLiquido.lblValidarProveedor);
    }

    private void ocultarErroresBasicos() {
        ocultarError(InventarioLiquido.lblValidarProducto);
        ocultarError(InventarioLiquido.lblValidadStockMaximo);
        ocultarError(InventarioLiquido.lblValidarStockMinimo);
        ocultarError(InventarioLiquido.lblValidarUbicacion);
    }

    private void ocultarErroresLote() {
        ocultarError(InventarioLiquido.lblValidarCantidad);
        ocultarError(InventarioLiquido.lblvValidarCostoUnitario);
        ocultarError(InventarioLiquido.lblValidarPrecioVenta);
        ocultarError(InventarioLiquido.lblValidarPorcentaje);
        ocultarError(InventarioLiquido.lblValidarFechaVencimiento);
    }

    private void addKeyListener(JTextField campo, Runnable accion) {
        campo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) { accion.run(); }
        });
    }

    private void limpiarCamposRegistro() {
        InventarioLiquido.jComboProductoLiquido.setSelectedIndex(0);
        InventarioLiquido.jComboUbicacion.setSelectedIndex(0);
        InventarioLiquido.txtCantidadActual.setText("");
        InventarioLiquido.txtStockMaximo.setText("");
        InventarioLiquido.txtStockMinimo.setText("");
        InventarioLiquido.txtCostoUnitario.setText("0.00");
       InventarioLiquido.txtPrecioVenta.setText("0.00");
       // InventarioLiquido.txtPorcentaje_Liquido.setText("");
        InventarioLiquido.jDateFechaVencimiento.setDate(null);
        InventarioLiquido.jComboProveedor.setSelectedIndex(0);
    }
}