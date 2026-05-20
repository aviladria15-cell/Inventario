/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Validacion;

import Modelo.Categoria;
import ModeloDAO.CategoriaDao;
import Vista_Gestion_Categoria.frm_Categoria;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.DocumentFilter.FilterBypass;

public class Validar_Categoria extends CategoriaDao {

    CategoriaDao dao = new CategoriaDao();

    // ------------------- INICIALIZAR VALIDACIONES -------------------
 // ------------------- INICIALIZAR VALIDACIONES -------------------
public void inicializarValidacionCategoria() {
    // Ocultar mensaje de error al inicio
    ocultarError();

    // === APLICAR FILTRO: SOLO LETRAS, ESPACIOS Y ACENTOS ===
    aplicarFiltroSoloLetras(frm_Categoria.txtNombreCategoria);

    // === VALIDACIÓN VISUAL EN TIEMPO REAL ===
    frm_Categoria.txtNombreCategoria.getDocument()
        .addDocumentListener(new SimpleDocumentListener(this::validarNombreCategoria));
}


private void aplicarFiltroSoloLetras(JTextField campo) {
    ((AbstractDocument) campo.getDocument()).setDocumentFilter(new DocumentFilter() {
        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                throws BadLocationException {
            if (string != null && string.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]*")) {
                super.insertString(fb, offset, string, attr);
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                throws BadLocationException {
            if (text != null && text.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]*")) {
                super.replace(fb, offset, length, text, attrs);
            }
        }
    });
}




    // ------------------- VALIDAR NOMBRE DE CATEGORÍA -------------------
 private void validarNombreCategoria() {
    String nombre = frm_Categoria.txtNombreCategoria.getText().trim();

    if (nombre.isEmpty()) {
        mostrarError("El nombre de la categoría no puede estar vacío");
        return;
    }

    // Ya no necesitas verificar letras: el filtro lo bloquea
    // Pero mantenemos longitud y repeticiones

    if (nombre.length() < 3) {
        mostrarError("El nombre debe tener al menos 3 letras");
        return;
    }

    // Detecta 3 o más letras iguales seguidas: aaa, eeee, lllll
    if (nombre.matches(".*(.)\\1{2}.*")) {
        mostrarError("Evita repeticiones inusuales (ej: aaa)");
        return;
    }

    // Opcional: evita patrones de teclado
    String minusculas = nombre.toLowerCase();
    String[] patronesInvalidos = {"qwerty", "asdfgh", "zxcvbn", "123", "abc"};
    for (String patron : patronesInvalidos) {
        if (minusculas.contains(patron)) {
            mostrarError("Nombre no válido");
            return;
        }
    }

    ocultarError();
}
 
 
 private void mostrarError(String mensaje) {
    frm_Categoria.lblCategoriaValidar.setText(mensaje);
    frm_Categoria.lblCategoriaValidar.setVisible(true);
}

private void ocultarError() {
    frm_Categoria.lblCategoriaValidar.setText("");
    frm_Categoria.lblCategoriaValidar.setVisible(false);
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
    // ------------------- VALIDAR FORMULARIO COMPLETO -------------------
 public boolean validarFormularioCategoria() {
    validarNombreCategoria();
    return !frm_Categoria.lblCategoriaValidar.isVisible();
}

    // ------------------- REGISTRAR CATEGORÍA -------------------
    public void registrarCategoriaValidada() throws ClassNotFoundException, SQLException {
        if (!validarFormularioCategoria()) {
            JOptionPane.showMessageDialog(null, "Corrige los errores antes de registrar la categoría.");
            return;
        }

        Categoria cat = new Categoria();
        cat.setNombre(frm_Categoria.txtNombreCategoria.getText().trim());

        dao.RegistrarCategoria();
        
    }

    // ------------------- ACTUALIZAR CATEGORÍA -------------------
    public void actualizarCategoriaValidada() throws ClassNotFoundException, SQLException {
        if (!validarFormularioCategoria()) {
            JOptionPane.showMessageDialog(null, "Corrige los errores antes de actualizar la categoría.");
            return;
        }

        Categoria cat = new Categoria();
        cat.setNombre(frm_Categoria.txtNombreCategoria.getText().trim());
        // Si tienes un campo ID:
        // cat.setId(Integer.parseInt(frm_Categoria.txtIdCategoria.getText().trim()));

        dao.ActualizarCategoria(cat);
        
    }

  
  }


