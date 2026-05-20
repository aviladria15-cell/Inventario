/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Validacion;

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

public class Validar_Ajuste_Unidad {
     
    MovientosDao mv = new MovientosDao();
    
    
    
    
 public void inicializarValidacionesAjusteUnidad() {
        ocultarErrores();

        // Validación en tiempo real
        addDocumentListener(Gestionar_Almacenn.txtCantidadAjusteUNIDAD, this::validarCantidad);
        addDocumentListener(Gestionar_Almacenn.txtDatelleAjsuteUNIDAD, this::validarDetalle);
        Gestionar_Almacenn.JComboxSUnidad.addActionListener(e -> validarCombo());
    }

    // ======================================================================
    // VALIDAR CANTIDAD EN TIEMPO REAL
    // ======================================================================
    private void validarCantidad() {
        String texto = Gestionar_Almacenn.txtCantidadAjusteUNIDAD.getText().trim();

        if (texto.isEmpty()) {
            mostrarError(Gestionar_Almacenn.lblValidarCantidadAJUSTEUNIDAD, "Ingrese cantidad");
            return;
        }

        if (!texto.matches("^[+-]\\d+$")) {
            mostrarError(Gestionar_Almacenn.lblValidarCantidadAJUSTEUNIDAD, "Formato: +50 o -20");
            return;
        }

        int num = Integer.parseInt(texto.substring(1));
        if (num == 0) {
            mostrarError(Gestionar_Almacenn.lblValidarCantidadAJUSTEUNIDAD, "No puede ser 0");
        } else {
            ocultarError(Gestionar_Almacenn.lblValidarCantidadAJUSTEUNIDAD);
        }
    }

    // ======================================================================
    // VALIDAR DETALLE EN TIEMPO REAL
    // ======================================================================
    private void validarDetalle() {
        String texto = Gestionar_Almacenn.txtDatelleAjsuteUNIDAD.getText().trim();

        if (texto.isEmpty()) {
            mostrarError(Gestionar_Almacenn.lblValidarDetalleAjusteUNIDAD, "Detalle requerido");
        } else if (texto.length() < 3) {
            mostrarError(Gestionar_Almacenn.lblValidarDetalleAjusteUNIDAD, "Mínimo 3 caracteres");
        } else {
            ocultarError(Gestionar_Almacenn.lblValidarDetalleAjusteUNIDAD);
        }
    }

    // ======================================================================
    // VALIDAR COMBO EN TIEMPO REAL
    // ======================================================================
    private void validarCombo() {
        String valor = obtenerCombo();
        if (valor.isEmpty() || valor.equalsIgnoreCase("Seleccione")) {
            mostrarError(Gestionar_Almacenn.lblValidadarComboxUNIDADAJUSTE, "Seleccione producto");
        } else {
            ocultarError(Gestionar_Almacenn.lblValidadarComboxUNIDADAJUSTE);
        }
    }

    private String obtenerCombo() {
        Object sel = Gestionar_Almacenn.JComboxSUnidad.getSelectedItem();
        return (sel != null) ? sel.toString().trim() : "";
    }

    // ======================================================================
    // VALIDAR AJUSTE UNIDAD (BOTÓN) - VALIDACIÓN FINAL
    // ======================================================================
    public void ValidarAjusteUnidad() throws ClassNotFoundException, SQLException {
        ocultarErrores();
        boolean error = false;

        // COMBO
        if (obtenerCombo().isEmpty() || obtenerCombo().equalsIgnoreCase("Seleccione")) {
            mostrarError(Gestionar_Almacenn.lblValidadarComboxUNIDADAJUSTE, "Seleccione producto");
            error = true;
        }

        // CANTIDAD
        String cant = Gestionar_Almacenn.txtCantidadAjusteUNIDAD.getText().trim();
        if (cant.isEmpty()) {
            mostrarError(Gestionar_Almacenn.lblValidarCantidadAJUSTEUNIDAD, "Ingrese cantidad");
            error = true;
        } else if (!cant.matches("^[+-]\\d+$")) {
            mostrarError(Gestionar_Almacenn.lblValidarCantidadAJUSTEUNIDAD, "Formato: +50 o -20");
            error = true;
        } else {
            int num = Integer.parseInt(cant.substring(1));
            if (num == 0) {
                mostrarError(Gestionar_Almacenn.lblValidarCantidadAJUSTEUNIDAD, "No puede ser 0");
                error = true;
            }
        }

        // DETALLE
        String detalle = Gestionar_Almacenn.txtDatelleAjsuteUNIDAD.getText().trim();
        if (detalle.isEmpty()) {
            mostrarError(Gestionar_Almacenn.lblValidarDetalleAjusteUNIDAD, "Detalle requerido");
            error = true;
        } else if (detalle.length() < 3) {
            mostrarError(Gestionar_Almacenn.lblValidarDetalleAjusteUNIDAD, "Mínimo 3 caracteres");
            error = true;
        }

        if (error) {
            JOptionPane.showMessageDialog(null,
                "Corrige los errores en rojo.\n\nEjemplo:\n+50 → Sumar 50 unidades\n-20 → Restar 20 unidades",
                "Ajuste por Unidad", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // GUARDAR
        mv.Realizar_AJUSTE_Unidad();

        // LIMPIAR CAMPOS
        Gestionar_Almacenn.JComboxSUnidad.setSelectedIndex(0);
        Gestionar_Almacenn.txtCantidadAjusteUNIDAD.setText("");
        Gestionar_Almacenn.txtDatelleAjsuteUNIDAD.setText("");

       
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
        ocultarError(Gestionar_Almacenn.lblValidadarComboxUNIDADAJUSTE);
        ocultarError(Gestionar_Almacenn.lblValidarCantidadAJUSTEUNIDAD);
        ocultarError(Gestionar_Almacenn.lblValidarDetalleAjusteUNIDAD);
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

