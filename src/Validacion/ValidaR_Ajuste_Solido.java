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
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.text.DocumentFilter.FilterBypass;
public class ValidaR_Ajuste_Solido {
    
    
    MovientosDao mv = new MovientosDao();
    
 
    // ======================================================================
    // INICIALIZAR VALIDACIONES
    // ======================================================================



public void inicializarValidacionesAjusteSolido() {
        ocultarErrores();

        // VALIDACIÓN EN TIEMPO REAL: CANTIDAD
        addDocumentListener(Gestionar_Almacenn.txtCantidadAjusteSolido, this::validarCantidad);

        // VALIDACIÓN EN TIEMPO REAL: DETALLE
        addDocumentListener(Gestionar_Almacenn.txtDatelleAjsuteSolido, this::validarDetalle);

        // COMBO
        Gestionar_Almacenn.JComboxSOLIDO.addActionListener(e -> validarCombo());
    }

    // ======================================================================
    // VALIDAR CANTIDAD EN TIEMPO REAL
    // ======================================================================
    private void validarCantidad() {
        String texto = Gestionar_Almacenn.txtCantidadAjusteSolido.getText().trim();

        if (texto.isEmpty()) {
            mostrarError(Gestionar_Almacenn.lblValidarCantidadAjusteSolido, "Ingrese cantidad");
            return;
        }

        if (!texto.matches("^[+-]\\d+$")) {
            mostrarError(Gestionar_Almacenn.lblValidarCantidadAjusteSolido, "Formato: +50 o -20");
        } else {
            int num = Integer.parseInt(texto.substring(1));
            if (num == 0) {
                mostrarError(Gestionar_Almacenn.lblValidarCantidadAjusteSolido, "No puede ser 0");
            } else {
                ocultarError(Gestionar_Almacenn.lblValidarCantidadAjusteSolido);
            }
        }
    }

    // ======================================================================
    // VALIDAR DETALLE EN TIEMPO REAL
    // ======================================================================
    private void validarDetalle() {
        String texto = Gestionar_Almacenn.txtDatelleAjsuteSolido.getText().trim();

        if (texto.isEmpty()) {
            mostrarError(Gestionar_Almacenn.lblValidarDetalleAjusteSolido, "Detalle requerido");
        } else if (texto.length() < 3) {
            mostrarError(Gestionar_Almacenn.lblValidarDetalleAjusteSolido, "Mínimo 3 caracteres");
        } else {
            ocultarError(Gestionar_Almacenn.lblValidarDetalleAjusteSolido);
        }
    }

    // ======================================================================
    // VALIDAR COMBO
    // ======================================================================
    private void validarCombo() {
        String valor = obtenerCombo();
        if (valor.isEmpty() || valor.equalsIgnoreCase("Seleccione")) {
            mostrarError(Gestionar_Almacenn.lblValidadarComboxSolidoAjuste, "Seleccione producto");
        } else {
            ocultarError(Gestionar_Almacenn.lblValidadarComboxSolidoAjuste);
        }
    }

    private String obtenerCombo() {
        Object sel = Gestionar_Almacenn.JComboxSOLIDO.getSelectedItem();
        return (sel != null) ? sel.toString().trim() : "";
    }

    // ======================================================================
    // REALIZAR AJUSTE (BOTÓN)
    // ======================================================================
    public void RealizarAjusteSolidoValidado() throws ClassNotFoundException, SQLException {
        ocultarErrores();

        boolean error = false;

        // COMBO
        if (obtenerCombo().isEmpty() || obtenerCombo().equalsIgnoreCase("Seleccione")) {
            mostrarError(Gestionar_Almacenn.lblValidadarComboxSolidoAjuste, "Seleccione producto");
            error = true;
        }

        // CANTIDAD
        String cant = Gestionar_Almacenn.txtCantidadAjusteSolido.getText().trim();
        if (cant.isEmpty()) {
            mostrarError(Gestionar_Almacenn.lblValidarCantidadAjusteSolido, "Ingrese cantidad");
            error = true;
        } else if (!cant.matches("^[+-]\\d+$")) {
            mostrarError(Gestionar_Almacenn.lblValidarCantidadAjusteSolido, "Formato: +50 o -20");
            error = true;
        } else {
            int num = Integer.parseInt(cant.substring(1));
            if (num == 0) {
                mostrarError(Gestionar_Almacenn.lblValidarCantidadAjusteSolido, "No puede ser 0");
                error = true;
            }
        }

        // DETALLE
        String detalle = Gestionar_Almacenn.txtDatelleAjsuteSolido.getText().trim();
        if (detalle.isEmpty()) {
            mostrarError(Gestionar_Almacenn.lblValidarDetalleAjusteSolido, "Detalle requerido");
            error = true;
        } else if (detalle.length() < 3) {
            mostrarError(Gestionar_Almacenn.lblValidarDetalleAjusteSolido, "Mínimo 3 caracteres");
            error = true;
        }

        if (error) {
            JOptionPane.showMessageDialog(null, "Corrige los errores en rojo", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        mv.Realizar_AJUSTE_Solido();

        // LIMPIAR
        Gestionar_Almacenn.JComboxSOLIDO.setSelectedIndex(0);
        Gestionar_Almacenn.txtCantidadAjusteSolido.setText("");
        Gestionar_Almacenn.txtDatelleAjsuteSolido.setText("");

        //JOptionPane.showMessageDialog(null, "Ajuste realizado", "Éxito", JOptionPane.INFORMATION_MESSAGE);
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
        ocultarError(Gestionar_Almacenn.lblValidadarComboxSolidoAjuste);
        ocultarError(Gestionar_Almacenn.lblValidarCantidadAjusteSolido);
        ocultarError(Gestionar_Almacenn.lblValidarDetalleAjusteSolido);
    }

    // ======================================================================
    // DOCUMENT LISTENER SIMPLE Y SEGURO
    // ======================================================================
    private void addDocumentListener(JTextField campo, Runnable accion) {
        campo.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { accion.run(); }
            public void removeUpdate(DocumentEvent e) { accion.run(); }
            public void changedUpdate(DocumentEvent e) { accion.run(); }
        });
    } }
    
    

