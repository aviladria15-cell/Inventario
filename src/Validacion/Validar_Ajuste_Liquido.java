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
import java.awt.Color;
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

public class Validar_Ajuste_Liquido {
      MovientosDao mv = new MovientosDao();
      
      
     public void inicializarValidacionesAjusteLiquido() {
        ocultarErrores();

        // Validación en tiempo real
        addDocumentListener(Gestionar_Almacenn.txtCantidadAjusteLIQUIDO, this::validarCantidad);
        addDocumentListener(Gestionar_Almacenn.txtDatelleAjsuteLIQUIDO, this::validarDetalle);
        Gestionar_Almacenn.JComboxLiquido.addActionListener(e -> validarCombo());
    }

    // ======================================================================
    // VALIDAR CANTIDAD EN TIEMPO REAL (PERMITE DECIMALES)
    // ======================================================================
    private void validarCantidad() {
        String texto = Gestionar_Almacenn.txtCantidadAjusteLIQUIDO.getText().trim();

        if (texto.isEmpty()) {
            mostrarError(Gestionar_Almacenn.lblValidarCantidadAJUSTELIQUIDO, "Ingrese cantidad");
            return;
        }

        if (!texto.matches("^[+-](\\d+\\.?\\d*|\\d*\\.?\\d+)$")) {
            mostrarError(Gestionar_Almacenn.lblValidarCantidadAJUSTELIQUIDO, "Formato: +5.5 o -3.2");
            return;
        }

        try {
            double num = Double.parseDouble(texto.substring(1));
            if (num == 0) {
                mostrarError(Gestionar_Almacenn.lblValidarCantidadAJUSTELIQUIDO, "No puede ser 0");
            } else {
                ocultarError(Gestionar_Almacenn.lblValidarCantidadAJUSTELIQUIDO);
            }
        } catch (NumberFormatException e) {
            mostrarError(Gestionar_Almacenn.lblValidarCantidadAJUSTELIQUIDO, "Número inválido");
        }
    }

    // ======================================================================
    // VALIDAR DETALLE EN TIEMPO REAL
    // ======================================================================
    private void validarDetalle() {
        String texto = Gestionar_Almacenn.txtDatelleAjsuteLIQUIDO.getText().trim();

        if (texto.isEmpty()) {
            mostrarError(Gestionar_Almacenn.lblValidarDetalleAjusteLIQUIDO, "Detalle requerido");
        } else if (texto.length() < 3) {
            mostrarError(Gestionar_Almacenn.lblValidarDetalleAjusteLIQUIDO, "Mínimo 3 caracteres");
        } else {
            ocultarError(Gestionar_Almacenn.lblValidarDetalleAjusteLIQUIDO);
        }
    }

    // ======================================================================
    // VALIDAR COMBO EN TIEMPO REAL
    // ======================================================================
    private void validarCombo() {
        String valor = obtenerCombo();
        if (valor.isEmpty() || valor.equalsIgnoreCase("Seleccione")) {
            mostrarError(Gestionar_Almacenn.lblValidadarComboxLIQUIDOAJUSTE1, "Seleccione producto");
        } else {
            ocultarError(Gestionar_Almacenn.lblValidadarComboxLIQUIDOAJUSTE1);
        }
    }

    private String obtenerCombo() {
        Object sel = Gestionar_Almacenn.JComboxLiquido.getSelectedItem();
        return (sel != null) ? sel.toString().trim() : "";
    }

    // ======================================================================
    // VALIDAR AJUSTE LÍQUIDO (BOTÓN) - VALIDACIÓN FINAL
    // ======================================================================
    public void validarAjusteLiquido() throws ClassNotFoundException, SQLException {
        ocultarErrores();
        boolean error = false;

        // COMBO
        if (obtenerCombo().isEmpty() || obtenerCombo().equalsIgnoreCase("Seleccione")) {
            mostrarError(Gestionar_Almacenn.lblValidadarComboxLIQUIDOAJUSTE1, "Seleccione producto");
            error = true;
        }

        // CANTIDAD
        String cant = Gestionar_Almacenn.txtCantidadAjusteLIQUIDO.getText().trim();
        if (cant.isEmpty()) {
            mostrarError(Gestionar_Almacenn.lblValidarCantidadAJUSTELIQUIDO, "Ingrese cantidad");
            error = true;
        } else if (!cant.matches("^[+-](\\d+\\.?\\d*|\\d*\\.?\\d+)$")) {
            mostrarError(Gestionar_Almacenn.lblValidarCantidadAJUSTELIQUIDO, "Formato: +5.5 o -3.2");
            error = true;
        } else {
            try {
                double num = Double.parseDouble(cant.substring(1));
                if (num == 0) {
                    mostrarError(Gestionar_Almacenn.lblValidarCantidadAJUSTELIQUIDO, "No puede ser 0");
                    error = true;
                }
            } catch (NumberFormatException e) {
                mostrarError(Gestionar_Almacenn.lblValidarCantidadAJUSTELIQUIDO, "Número inválido");
                error = true;
            }
        }

        // DETALLE
        String detalle = Gestionar_Almacenn.txtDatelleAjsuteLIQUIDO.getText().trim();
        if (detalle.isEmpty()) {
            mostrarError(Gestionar_Almacenn.lblValidarDetalleAjusteLIQUIDO, "Detalle requerido");
            error = true;
        } else if (detalle.length() < 3) {
            mostrarError(Gestionar_Almacenn.lblValidarDetalleAjusteLIQUIDO, "Mínimo 3 caracteres");
            error = true;
        }

        if (error) {
            JOptionPane.showMessageDialog(null,
                "Corrige los errores en rojo.\n\nEjemplo:\n+5.5 → Sumar 5.5 litros\n-3.2 → Restar 3.2 litros",
                "Ajuste de Líquido", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // GUARDAR
        mv.Realizar_AJUSTE_Liquido();

        // LIMPIAR CAMPOS
        Gestionar_Almacenn.JComboxLiquido.setSelectedIndex(0);
        Gestionar_Almacenn.txtCantidadAjusteLIQUIDO.setText("");
        Gestionar_Almacenn.txtDatelleAjsuteLIQUIDO.setText("");

    }

    // ======================================================================
    // MOSTRAR / OCULTAR ERRORES
    // ======================================================================
    private void mostrarError(JLabel lbl, String msg) {
        lbl.setText(msg);
        lbl.setForeground(new Color(220, 20, 20));
        lbl.setVisible(true);
    }

    private void ocultarError(JLabel lbl) {
        lbl.setText("");
        lbl.setVisible(false);
    }

    private void ocultarErrores() {
        ocultarError(Gestionar_Almacenn.lblValidadarComboxLIQUIDOAJUSTE1);
        ocultarError(Gestionar_Almacenn.lblValidarCantidadAJUSTELIQUIDO);
        ocultarError(Gestionar_Almacenn.lblValidarDetalleAjusteLIQUIDO);
    }

    // ======================================================================
    // DOCUMENT LISTENER SEGURO (SIN TRABAS)
    // ======================================================================
    private void addDocumentListener(JTextField campo, Runnable accion) {
        campo.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { accion.run(); }
            public void removeUpdate(DocumentEvent e) { accion.run(); }
            public void changedUpdate(DocumentEvent e) { accion.run(); }
        });
    }
}
      
      

