/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Validacion;

import Modelo.Marca;
import ModeloDAO.MarcaDao;
import Vista_Gestion_Marca.Gestionar_Marca;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class Validar_Marca extends Marca {

    MarcaDao marcaDao = new MarcaDao();

    // ------------------- INICIALIZAR VALIDACIONES -------------------
public void inicializarValidacionMarca() {
    // Ocultar etiquetas al inicio
    ocultarError(Gestionar_Marca.lblNombreMarcaValidacion);
    ocultarError(Gestionar_Marca.lblNombrePaisValidacion);

    // === APLICAR FILTROS: SOLO LETRAS, ESPACIOS Y ACENTOS ===
    aplicarFiltroSoloLetras(Gestionar_Marca.txtNombre);
    aplicarFiltroSoloLetras(Gestionar_Marca.txtPais);

    // === VALIDACIÓN VISUAL EN TIEMPO REAL ===
    Gestionar_Marca.txtNombre.getDocument()
        .addDocumentListener(new SimpleDocumentListener(this::validarNombreMarca));

    Gestionar_Marca.txtPais.getDocument()
        .addDocumentListener(new SimpleDocumentListener(this::validarPaisMarca));
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


private void validarNombreMarca() {
    String nombre = Gestionar_Marca.txtNombre.getText().trim();

    if (nombre.isEmpty()) {
        mostrarError(Gestionar_Marca.lblNombreMarcaValidacion, "El nombre de la marca no puede estar vacío");
        return;
    }

    if (nombre.length() < 2) {
        mostrarError(Gestionar_Marca.lblNombreMarcaValidacion, "El nombre es demasiado corto");
        return;
    }

    if (nombre.matches(".*(.)\\1{2}.*")) { // 3 o más letras iguales seguidas
        mostrarError(Gestionar_Marca.lblNombreMarcaValidacion, "Evita repeticiones (ej: aaa)");
        return;
    }

    // Opcional: bloquear patrones absurdos
    String minus = nombre.toLowerCase();
    if (minus.matches(".*(qwerty|asdf|zxcv|123|abc).*")) {
        mostrarError(Gestionar_Marca.lblNombreMarcaValidacion, "Nombre no válido");
        return;
    }

    ocultarError(Gestionar_Marca.lblNombreMarcaValidacion);
}




private void validarPaisMarca() {
    String pais = Gestionar_Marca.txtPais.getText().trim();

    if (pais.isEmpty()) {
        mostrarError(Gestionar_Marca.lblNombrePaisValidacion, "El país no puede estar vacío");
        return;
    }

    if (pais.length() < 3) {
        mostrarError(Gestionar_Marca.lblNombrePaisValidacion, "El país debe tener al menos 3 letras");
        return;
    }

    if (pais.matches(".*(.)\\1{2}.*")) {
        mostrarError(Gestionar_Marca.lblNombrePaisValidacion, "Evita repeticiones (ej: eee)");
        return;
    }

    ocultarError(Gestionar_Marca.lblNombrePaisValidacion);
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
    public boolean validarFormularioMarca() {
        validarNombreMarca();
        validarPaisMarca();

        return !Gestionar_Marca.lblNombreMarcaValidacion.isVisible()
                && !Gestionar_Marca.lblNombrePaisValidacion.isVisible();
    }

    // ------------------- REGISTRAR MARCA -------------------
    public void registrarMarcaValidada() throws ClassNotFoundException, SQLException {
        if (!validarFormularioMarca()) {
            JOptionPane.showMessageDialog(null, "Corrige los errores antes de registrar la marca.");
            return;
        }

        Marca marca = new Marca();
        marca.setNombre(Gestionar_Marca.txtNombre.getText().trim());
        marca.setPaisOrigen(Gestionar_Marca.txtPais.getText().trim());

        marcaDao.RegistrarMarca();
     //   JOptionPane.showMessageDialog(null, "Marca registrada correctamente.");
    }

    // ------------------- ACTUALIZAR MARCA -------------------
    public void actualizarMarcaValidada() throws ClassNotFoundException, SQLException {
        if (!validarFormularioMarca()) {
            JOptionPane.showMessageDialog(null, "Corrige los errores antes de actualizar la marca.");
            return;
        }

        Marca marca = new Marca();
        marca.setNombre(Gestionar_Marca.txtNombre.getText().trim());
        marca.setPaisOrigen(Gestionar_Marca.txtPais.getText().trim());

        // Si existe campo ID:
        // marca.setIdMarca(Integer.parseInt(Gestionar_Marca.txtIdMarca.getText().trim()));

        marcaDao.ActualizarMarca(marca);
    //    JOptionPane.showMessageDialog(null, "Marca actualizada correctamente.");
    }

  private void mostrarError(javax.swing.JLabel label, String mensaje) {
    label.setText(mensaje);
    label.setVisible(true);
}

private void ocultarError(javax.swing.JLabel label) {
    label.setText("");
    label.setVisible(false);
}
}
