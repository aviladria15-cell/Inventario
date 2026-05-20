/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Validacion;

import Modelo.Almacen;
import ModeloDAO.AlmacenDao;
import Vista_Almacen.Vista_Registrar_Almacen;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class Validar_Almacen extends AlmacenDao {

    AlmacenDao dao = new AlmacenDao();

    // ------------------- INICIALIZAR VALIDACIONES -------------------
   public void inicializarValidacionesAlmacen() {
    // Ocultar errores
    ocultarTodosErroresAlmacen();

    // === APLICAR FILTROS ===
    aplicarFiltroAlfanumericoConGuion(Vista_Registrar_Almacen.txtPasillo);
    aplicarFiltroSoloNumeros(Vista_Registrar_Almacen.txtNivel);
    aplicarFiltroSoloNumeros(Vista_Registrar_Almacen.txtEstante);
    aplicarFiltroSoloNumeros(Vista_Registrar_Almacen.txtCapacidad);

    // === VALIDACIÓN EN TIEMPO REAL ===
    Vista_Registrar_Almacen.txtPasillo.getDocument()
        .addDocumentListener(new SimpleDocumentListener(this::validarPasillo));
    
Vista_Registrar_Almacen.txtNivel.getDocument()
        .addDocumentListener(new SimpleDocumentListener(this::validarNivel));
    
    Vista_Registrar_Almacen.txtEstante.getDocument()
        .addDocumentListener(new SimpleDocumentListener(this::validarEstante));
    
    Vista_Registrar_Almacen.txtCapacidad.getDocument()
        .addDocumentListener(new SimpleDocumentListener(this::validarCapacidad));
    
    Vista_Registrar_Almacen.jComboBoxAla.addActionListener(e -> validarAla());
}

   
   
   private void aplicarFiltroAlfanumericoConGuion(JTextField campo) {
    ((AbstractDocument) campo.getDocument()).setDocumentFilter(new DocumentFilter() {
        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                throws BadLocationException {
            if (string != null && string.matches("[a-zA-Z0-9\\- ]*")) {
                super.insertString(fb, offset, string, attr);
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                throws BadLocationException {
            if (text != null && text.matches("[a-zA-Z0-9\\- ]*")) {
                super.replace(fb, offset, length, text, attrs);
            }
        }
    });
}
   
   
   private void aplicarFiltroSoloNumeros(JTextField campo) {
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
    // ------------------- VALIDACIONES -------------------

   private void validarPasillo() {
    String pasillo = Vista_Registrar_Almacen.txtPasillo.getText().trim();
    if (pasillo.isEmpty()) {
        mostrarError(Vista_Registrar_Almacen.lblValidarPasillo, "El pasillo no puede estar vacío");
    } else if (pasillo.length() < 2) {
        mostrarError(Vista_Registrar_Almacen.lblValidarPasillo, "Mínimo 2 caracteres");
    } else if (tieneRepeticionesExtrañas(pasillo)) {
        mostrarError(Vista_Registrar_Almacen.lblValidarPasillo, "Evita repeticiones (ej: aaa, 111)");
    } else {
        ocultarError(Vista_Registrar_Almacen.lblValidarPasillo);
    }
}

   private void validarNivel() {
    String nivel = Vista_Registrar_Almacen.txtNivel.getText().trim();
    if (nivel.isEmpty()) {
        mostrarError(Vista_Registrar_Almacen.LblValidarnivel, "El nivel no puede estar vacío");
    } else if (nivel.length() > 3) {
        mostrarError(Vista_Registrar_Almacen.LblValidarnivel, "Máximo 3 dígitos");
    } else if (tieneRepeticionesExtrañas(nivel)) {
        mostrarError(Vista_Registrar_Almacen.LblValidarnivel, "Evita repeticiones (ej: 111)");
    } else {
        ocultarError(Vista_Registrar_Almacen.LblValidarnivel);
    }
}
   private void validarEstante() {
    String estante = Vista_Registrar_Almacen.txtEstante.getText().trim();
    if (estante.isEmpty()) {
        mostrarError(Vista_Registrar_Almacen.LblValidarestante, "El estante no puede estar vacío");
    } else if (estante.length() > 3) {
        mostrarError(Vista_Registrar_Almacen.LblValidarestante, "Máximo 3 dígitos");
    } else if (tieneRepeticionesExtrañas(estante)) {
        mostrarError(Vista_Registrar_Almacen.LblValidarestante, "Evita repeticiones (ej: 999)");
    } else {
        ocultarError(Vista_Registrar_Almacen.LblValidarestante);
    }
}

   private void validarCapacidad() {
    String capacidad = Vista_Registrar_Almacen.txtCapacidad.getText().trim();
    if (capacidad.isEmpty()) {
        mostrarError(Vista_Registrar_Almacen.LblValidarcapacidad, "La capacidad no puede estar vacía");
    } else {
        try {
            int cap = Integer.parseInt(capacidad);
            if (cap <= 0) {
                mostrarError(Vista_Registrar_Almacen.LblValidarcapacidad, "Debe ser mayor a 0");
            } else if (cap > 99) {
                mostrarError(Vista_Registrar_Almacen.LblValidarcapacidad, "Máximo 99");
            } else if (tieneRepeticionesExtrañas(capacidad)) {
                mostrarError(Vista_Registrar_Almacen.LblValidarcapacidad, "Evita repeticiones (ej: 1111)");
            } else {
                ocultarError(Vista_Registrar_Almacen.LblValidarcapacidad);
            }
        } catch (NumberFormatException e) {
            mostrarError(Vista_Registrar_Almacen.LblValidarcapacidad, "Número inválido");
        }
    }
}
   private void validarAla() {
    Object alaSel = Vista_Registrar_Almacen.jComboBoxAla.getSelectedItem();
    String ala = (alaSel != null) ? alaSel.toString().trim() : "";
    if (ala.isEmpty() || ala.equalsIgnoreCase("Seleccione")) {
        mostrarError(Vista_Registrar_Almacen.LblValidarAla, "Debe seleccionar un ala");
    } else {
        ocultarError(Vista_Registrar_Almacen.LblValidarAla);
    }
}
   private boolean tieneRepeticionesExtrañas(String texto) {
    return texto.matches(".*(.)\\1{2}.*"); // 3 o más caracteres iguales
}
   
   private void ocultarTodosErroresAlmacen() {
    ocultarError(Vista_Registrar_Almacen.lblValidarPasillo);
    ocultarError(Vista_Registrar_Almacen.LblValidarnivel);
    ocultarError(Vista_Registrar_Almacen.LblValidarestante);
    ocultarError(Vista_Registrar_Almacen.LblValidarcapacidad);
    ocultarError(Vista_Registrar_Almacen.LblValidarAla);
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
    public boolean validarFormularioAlmacen() {
        validarPasillo();
        validarNivel();
        validarEstante();
        validarCapacidad();
        validarAla();

        return !Vista_Registrar_Almacen.lblValidarPasillo.isVisible()
                && !Vista_Registrar_Almacen.LblValidarnivel.isVisible()
                && !Vista_Registrar_Almacen.LblValidarestante.isVisible()
                && !Vista_Registrar_Almacen.LblValidarcapacidad.isVisible()
                && !Vista_Registrar_Almacen.LblValidarAla.isVisible();
    }

    // ------------------- REGISTRAR -------------------
    public void registrarAlmacenValidado() throws ClassNotFoundException, SQLException {
        if (!validarFormularioAlmacen()) {
          JOptionPane.showMessageDialog(null, "Corrige los campos marcados antes de registrar el almacén.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
                    
                    
                 
          
        }

        Almacen a = new Almacen();
        a.setPasillo(Vista_Registrar_Almacen.txtPasillo.getText().trim());
        a.setNivel(Integer.parseInt(Vista_Registrar_Almacen.txtNivel.getText().trim()));
        a.setEstante(Vista_Registrar_Almacen.txtEstante.getText().trim());
        a.setCapacidad(Integer.parseInt(Vista_Registrar_Almacen.txtCapacidad.getText().trim()));
        a.setAla(Vista_Registrar_Almacen.jComboBoxAla.getSelectedItem().toString().trim());

        dao.RegistrarAlmacen();
      
    }

    // ------------------- ACTUALIZAR -------------------
    public void actualizarAlmacenValidado() throws ClassNotFoundException, SQLException {
        if (!validarFormularioAlmacen()) {
            JOptionPane.showMessageDialog(null, "Corrige los campos marcados antes de ACTUALIZAR el almacén.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Almacen a = new Almacen();
    //    a.setId_Ubicacion(Integer.parseInt(Vista_Registrar_Almacen.txtIDAlmacen.getText().trim()));
        a.setPasillo(Vista_Registrar_Almacen.txtPasillo.getText().trim());
        a.setNivel(Integer.parseInt(Vista_Registrar_Almacen.txtNivel.getText().trim()));
        a.setEstante(Vista_Registrar_Almacen.txtEstante.getText().trim());
        a.setCapacidad(Integer.parseInt(Vista_Registrar_Almacen.txtCapacidad.getText().trim()));
        a.setAla(Vista_Registrar_Almacen.jComboBoxAla.getSelectedItem().toString().trim());

        dao.ActualizarConseguridad();
       
    }

 

    private boolean esTextoSinSentido(String texto) {
        // Detecta combinaciones ilógicas tipo "uu22" o "aa11" o exceso de mezcla
        return texto.matches(".*([a-zA-Z]{2,}[0-9]{2,}|[0-9]{2,}[a-zA-Z]{2,}).*");
    }

    // ------------------- MÉTODOS DE ERROR -------------------
    private void mostrarError(javax.swing.JLabel lbl, String mensaje) {
        lbl.setText(mensaje);
        lbl.setVisible(true);
    }

    private void ocultarError(javax.swing.JLabel lbl) {
        lbl.setVisible(false);
    }
}
