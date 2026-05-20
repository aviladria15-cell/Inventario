
    
    package Validacion;

import ModeloDAO.ProductoDao;
import Modelo.Producto;

import Vista_Gestion_Productos.Vista_Registro_Producto_Liquido;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.sql.SQLException;
import Vista_Gestion_Productos.Vista_Registrar_Producto_Solido;
import  Vista_Gestion_Productos.Vista_Registrar_Producto_Unidad;

public class Validar_Producto {

    private ProductoDao productoDao = new ProductoDao();

    // ------------------- MÉTODOS AUXILIARES -------------------
    private void mostrarError(JLabel label, String mensaje) {
        label.setText(mensaje);
        label.setVisible(true);
    }

    private void ocultarError(JLabel label) {
        label.setText("");
        label.setVisible(false);
    }

    // ======================================================================================
    // VALIDACIONES Y REGISTRO DE PRODUCTOS LÍQUIDOS
    // ======================================================================================

    public void inicializarValidacionesProductoLiquido() {
        // Ocultar etiquetas de error
        Vista_Registro_Producto_Liquido.lblNombreLiquidoValidacion.setVisible(false);
        Vista_Registro_Producto_Liquido.lblNombreViscocidadValidacion.setVisible(false);
        Vista_Registro_Producto_Liquido.lblPresentacionValidacion.setVisible(false);
        Vista_Registro_Producto_Liquido.lblCategoriaValidacion.setVisible(false);
        Vista_Registro_Producto_Liquido.lblMarcaValidacion.setVisible(false);
        Vista_Registro_Producto_Liquido.lblTipoLiquidoVALIDACION.setVisible(false);
       
        // Listeners para campos
       Vista_Registro_Producto_Liquido.txtNombreProductoLiquido.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { validarNombreLiquido(); }
            public void removeUpdate(DocumentEvent e) { validarNombreLiquido(); }
            public void changedUpdate(DocumentEvent e) { validarNombreLiquido(); }
        });

        Vista_Registro_Producto_Liquido.txtViscosidad.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { validarViscosidad(); }
            public void removeUpdate(DocumentEvent e) { validarViscosidad(); }
            public void changedUpdate(DocumentEvent e) { validarViscosidad(); }
        });
        
       Vista_Registro_Producto_Liquido.txtTipodeLiquido.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { validarTipoLiquido(); }
            public void removeUpdate(DocumentEvent e) { validarTipoLiquido(); }
            public void changedUpdate(DocumentEvent e) { validarTipoLiquido(); }
        });

       Vista_Registro_Producto_Liquido.txtPresentacion.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { validarPresentacion(); }
            public void removeUpdate(DocumentEvent e) { validarPresentacion(); }
            public void changedUpdate(DocumentEvent e) { validarPresentacion(); }
        });

       Vista_Registro_Producto_Liquido.jComboCategoria.addActionListener(e -> validarCategoriaLiquido());
        
      Vista_Registro_Producto_Liquido.jComboMarca.addActionListener(e -> validarMarcaLiquido());
    }

    
    
  private void validarTipoLiquido() {
    String tipo = Vista_Registro_Producto_Liquido.txtTipodeLiquido.getText().trim();
    
    if (tipo.isEmpty()) {
        mostrarError(Vista_Registro_Producto_Liquido.lblTipoLiquidoVALIDACION, "El tipo de líquido no puede estar vacío");
    } 
    else if (tipo.length() < 2 || tipo.length() > 25) {
        mostrarError(Vista_Registro_Producto_Liquido.lblTipoLiquidoVALIDACION, "Debe tener entre 2 y 35 caracteres");
    } 
    else if (!tipo.matches("^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\- ]+$")) {
        mostrarError(Vista_Registro_Producto_Liquido.lblTipoLiquidoVALIDACION, "Solo letras, números, guiones y espacios");
    } 
    else if (esPalabraSinSentido(tipo)) {
        mostrarError(Vista_Registro_Producto_Liquido.lblTipoLiquidoVALIDACION, "Tipo de líquido no válido");
    } 
    else {
        ocultarError(Vista_Registro_Producto_Liquido.lblTipoLiquidoVALIDACION);
    }
}
  
    private void validarNombreLiquido() {
    String nombre = Vista_Registro_Producto_Liquido.txtNombreProductoLiquido.getText().trim();
    
    if (nombre.isEmpty()) {
        mostrarError(Vista_Registro_Producto_Liquido.lblNombreLiquidoValidacion, "El nombre no puede estar vacío");
    } 
    else if (nombre.length() < 3 || nombre.length() > 25) {
        mostrarError(Vista_Registro_Producto_Liquido.lblNombreLiquidoValidacion, "El nombre debe tener entre 3 y 25 caracteres");
    } 
    else if (!nombre.matches("^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\- ]+$")) {
        mostrarError(Vista_Registro_Producto_Liquido.lblNombreLiquidoValidacion, "Solo letras, números, guiones y espacios");
    } 
    else if (esPalabraSinSentido(nombre)) {
        mostrarError(Vista_Registro_Producto_Liquido.lblNombreLiquidoValidacion, "Nombre no válido o sin sentido");
    } 
    else {
        ocultarError(Vista_Registro_Producto_Liquido.lblNombreLiquidoValidacion);
    }
}
   private void validarViscosidad() {
    String visco = Vista_Registro_Producto_Liquido.txtViscosidad.getText().trim();
    
    if (visco.isEmpty()) {
        mostrarError(Vista_Registro_Producto_Liquido.lblNombreViscocidadValidacion, "La viscosidad no puede estar vacía");
    } 
    else if (visco.length() > 25) {
        mostrarError(Vista_Registro_Producto_Liquido.lblNombreViscocidadValidacion, "Máximo 25 caracteres");
    } 
    else if (!visco.matches("^[a-zA-Z0-9()\\-\\. ]+$")) {   // agregué punto
        mostrarError(Vista_Registro_Producto_Liquido.lblNombreViscocidadValidacion, "Solo letras, números, guiones, paréntesis y punto");
    } else if  (esPalabraSinSentido(visco)) {
        mostrarError(Vista_Registro_Producto_Liquido.lblNombreLiquidoValidacion, "Nombre no válido o sin sentido");
    } 
            
            
    else {
        ocultarError(Vista_Registro_Producto_Liquido.lblNombreViscocidadValidacion);
    }
} 
private void validarPresentacion() {
    String present = Vista_Registro_Producto_Liquido.txtPresentacion.getText().trim();
    
    if (present.isEmpty()) {
        mostrarError(Vista_Registro_Producto_Liquido.lblPresentacionValidacion, "La presentación no puede estar vacía");
    } 
    else if (present.length() < 3 || present.length() > 25) {
        mostrarError(Vista_Registro_Producto_Liquido.lblPresentacionValidacion, "Debe tener entre 3 y 25 caracteres");
    } 
    else if (!present.matches("^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\.()\\- ]+$")) {
        mostrarError(Vista_Registro_Producto_Liquido.lblPresentacionValidacion, "Solo letras, números, punto, guiones y paréntesis");
    }  
    else if  (esPalabraSinSentido(present)) {
        mostrarError(Vista_Registro_Producto_Liquido.lblNombreLiquidoValidacion, "Nombre no válido o sin sentido");
    } 
    else {
        ocultarError(Vista_Registro_Producto_Liquido.lblPresentacionValidacion);
    }
}

    private void validarCategoriaLiquido() {
        Object sel = Vista_Registro_Producto_Liquido.jComboCategoria.getSelectedItem();
        String cat = (sel != null) ? sel.toString().trim() : "";
        if (cat.isEmpty() || cat.equalsIgnoreCase("Seleccione")) {
            mostrarError(Vista_Registro_Producto_Liquido.lblCategoriaValidacion, "Seleccione una categoría");
        } else {
            ocultarError(Vista_Registro_Producto_Liquido.lblCategoriaValidacion);
        }
    }
    
    private boolean esPalabraSinSentido(String texto) {
    String t = texto.toLowerCase().trim();
    
    // Lista de palabras o patrones sin sentido comunes
    String[] palabrasProhibidas = {
        "asdf", "qwerty", "zxcv", "aaaa", "bbbb", "cccc", "dddd", "eeee",
        "12345", "00000", "11111", "abcde", "prueba", "test", "xxxx", "yyyy"
    };
    
    for (String palabra : palabrasProhibidas) {
        if (t.contains(palabra)) return true;
    }
    
    // Si solo tiene 2 o menos caracteres diferentes (ej: "aaaaa", "ababab")
    if (t.chars().distinct().count() <= 2 && t.length() >= 5) {
        return true;
    }
    
    return false;
}
/*
    private void validarProveedorLiquido() {
        Object sel = Registrar_Productoss.ComboxBoxProveedor.getSelectedItem();
        String prov = (sel != null) ? sel.toString().trim() : "";
        if (prov.isEmpty() || prov.equalsIgnoreCase("Seleccione")) {
            mostrarError(Registrar_Productoss.lblProveedorValidacion, "Seleccione un proveedor");
        } else {
            ocultarError(Registrar_Productoss.lblProveedorValidacion);
        }
    }
*/
    private void validarMarcaLiquido() {
        Object sel =Vista_Registro_Producto_Liquido.jComboMarca.getSelectedItem();
        String marca = (sel != null) ? sel.toString().trim() : "";
        if (marca.isEmpty() || marca.equalsIgnoreCase("Seleccione")) {
            mostrarError(Vista_Registro_Producto_Liquido.lblMarcaValidacion, "Seleccione una marca");
        } else {
            ocultarError(Vista_Registro_Producto_Liquido.lblMarcaValidacion);
        }
    }

    public boolean validarFormularioProductoLiquido() {
        validarNombreLiquido();
        validarViscosidad();
        validarPresentacion();
        validarCategoriaLiquido();
       
        validarMarcaLiquido();
  validarTipoLiquido();
        return !Vista_Registro_Producto_Liquido.lblNombreLiquidoValidacion.isVisible()
                && !Vista_Registro_Producto_Liquido.lblNombreViscocidadValidacion.isVisible()
                && !Vista_Registro_Producto_Liquido.lblPresentacionValidacion.isVisible()
                && !Vista_Registro_Producto_Liquido.lblCategoriaValidacion.isVisible()
                && !Vista_Registro_Producto_Liquido.lblMarcaValidacion.isVisible()
                && !Vista_Registro_Producto_Liquido.lblTipoLiquidoVALIDACION.isVisible();
    }

    public void registrarProductoLiquidoValidado() throws ClassNotFoundException {
        if (!validarFormularioProductoLiquido()) {
            JOptionPane.showMessageDialog(null, "Corrige los errores antes de registrar el producto líquido.");
            return;
        }

        try {
            Producto p = new Producto();
            
            
            
            p.setNombre(Vista_Registro_Producto_Liquido.txtNombreProductoLiquido.getText().trim());
            p.setViscosidad(Vista_Registro_Producto_Liquido.txtViscosidad.getText().trim());
            p.setPresentacion(Vista_Registro_Producto_Liquido.txtPresentacion.getText().trim());
            
            p.setTipo_Liquido(Vista_Registro_Producto_Liquido.txtTipodeLiquido.getText().trim());

            Object catSel = Vista_Registro_Producto_Liquido.jComboCategoria.getSelectedItem();
            p.setIdCategoria(catSel != null ? catSel.toString() : "");
            Object marcaSel = Vista_Registro_Producto_Liquido.jComboMarca.getSelectedItem();
            p.setIdMarca(marcaSel != null ? marcaSel.toString() : "");

            
            productoDao.registrarProducto(p);
           
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al registrar producto: " + ex.getMessage());
        }
    }

    // ------------------- ACTUALIZAR PRODUCTO LÍQUIDO -------------------
    public void actualizarProductoLiquidoConValidacion() throws ClassNotFoundException {
        if (!validarFormularioProductoLiquido()) {
            JOptionPane.showMessageDialog(null, "Corrige los errores antes de actualizar el producto líquido.");
            return;
        }

        try {
            Producto p = new Producto();
            // Si tienes un campo de ID en el formulario (por ejemplo txtIdProductoLiquido), asignalo aquí:
            // p.setId(Integer.parseInt(Registrar_Productos.txtIdProductoLiquido.getText().trim()));
 
            p.setNombre(Vista_Registro_Producto_Liquido.txtNombreProductoLiquido.getText().trim());
            p.setViscosidad(Vista_Registro_Producto_Liquido.txtViscosidad.getText().trim());
            p.setPresentacion(Vista_Registro_Producto_Liquido.txtPresentacion.getText().trim());
            p.setTipo_Liquido(Vista_Registro_Producto_Liquido.txtTipodeLiquido.getText().trim());

            Object catSel = Vista_Registro_Producto_Liquido.jComboCategoria.getSelectedItem();
            p.setIdCategoria(catSel != null ? catSel.toString() : "");

          
            Object marcaSel = Vista_Registro_Producto_Liquido.jComboMarca.getSelectedItem();
            p.setIdMarca(marcaSel != null ? marcaSel.toString() : "");

            try {
                // Llamada al DAO para actualizar (ajusta el nombre del método según tu DAO real)
                productoDao.ActulizarProductoLiquido();
            } catch (SQLException ex) {
                System.getLogger(Validar_Producto.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
            
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(null, "ID de producto inválido: " + nfe.getMessage());
        }
    }

    // ======================================================================================
    // VALIDACIONES Y REGISTRO DE PRODUCTOS SÓLIDOS
    // ======================================================================================

    public void inicializarValidacionesProductoSolido() {
       Vista_Registrar_Producto_Solido.lblNombreSolidoValidavion.setVisible(false);
        Vista_Registrar_Producto_Solido.lblCompartibilidadValidacionSolido.setVisible(false);
       Vista_Registrar_Producto_Solido.lblUnidadDeMedidaValidacion.setVisible(false);
        Vista_Registrar_Producto_Solido.lblCandicionValidacion.setVisible(false);
        Vista_Registrar_Producto_Solido.lblCategoriaValidacion.setVisible(false);
       Vista_Registrar_Producto_Solido.lblMarcaValidacion.setVisible(false);

        Vista_Registrar_Producto_Solido.txtNombreProductoSolido.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { validarNombreProductoSolido(); }
            public void removeUpdate(DocumentEvent e) { validarNombreProductoSolido(); }
            public void changedUpdate(DocumentEvent e) { validarNombreProductoSolido(); }
        });

        Vista_Registrar_Producto_Solido.txtSolidoCompartibilidad.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { validarCompatibilidadSolido(); }
            public void removeUpdate(DocumentEvent e) { validarCompatibilidadSolido(); }
            public void changedUpdate(DocumentEvent e) { validarCompatibilidadSolido(); }
        });

      Vista_Registrar_Producto_Solido.txtCondicionSolido.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { validarCondicionSolido(); }
            public void removeUpdate(DocumentEvent e) { validarCondicionSolido(); }
            public void changedUpdate(DocumentEvent e) { validarCondicionSolido(); }
        });

       Vista_Registrar_Producto_Solido.jComboUnidaSolido.addActionListener(e -> validarUnidadMedidaSolido());
      Vista_Registrar_Producto_Solido.jComboCategoria.addActionListener(e -> validarCategoriaSolido());
      
        Vista_Registrar_Producto_Solido.jComboMarca.addActionListener(e -> validarMarcaSolido());
    }

   // ------------------- MÉTODOS DE VALIDACIÓN -------------------

private void validarNombreProductoSolido() {
    String nombre = Vista_Registrar_Producto_Solido.txtNombreProductoSolido.getText().trim();

    if (nombre.isEmpty()) {
        mostrarError(Vista_Registrar_Producto_Solido.lblNombreSolidoValidavion, "El nombre no puede estar vacío");
    } else if (nombre.length() < 3 || nombre.length() > 25) {
        mostrarError(Vista_Registrar_Producto_Solido.lblNombreSolidoValidavion, "El nombre debe tener entre 3 y 25 caracteres");
    } else if (!nombre.matches("^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\- ]+$")) {
        mostrarError(Vista_Registrar_Producto_Solido.lblNombreSolidoValidavion, "Solo letras, números, guiones y espacios");
    } else if (esPalabraSinSentido(nombre)) {
        mostrarError(Vista_Registrar_Producto_Solido.lblNombreSolidoValidavion, "Nombre no válido o sin sentido");
    } else {
        ocultarError(Vista_Registrar_Producto_Solido.lblNombreSolidoValidavion);
    }
}

private void validarCompatibilidadSolido() {
    String compat = Vista_Registrar_Producto_Solido.txtSolidoCompartibilidad.getText().trim();

    if (compat.isEmpty()) {
        mostrarError(Vista_Registrar_Producto_Solido.lblCompartibilidadValidacionSolido, "La compatibilidad no puede estar vacía");
    } else if (compat.length() > 30) {
        mostrarError(Vista_Registrar_Producto_Solido.lblCompartibilidadValidacionSolido, "Máximo 300 caracteres");
    } else if (!compat.matches("^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\.(),\\- ]+$")) {
        mostrarError(Vista_Registrar_Producto_Solido.lblCompartibilidadValidacionSolido, "Solo letras, números, punto, guiones, paréntesis y comas");
    } else if (esPalabraSinSentido(compat)) {
        mostrarError(Vista_Registrar_Producto_Solido.lblCompartibilidadValidacionSolido, "Compatibilidad no válida o sin sentido");
    } else {
        ocultarError(Vista_Registrar_Producto_Solido.lblCompartibilidadValidacionSolido);
    }
}

private void validarCondicionSolido() {
    String condicion = Vista_Registrar_Producto_Solido.txtCondicionSolido.getText().trim();

    if (condicion.isEmpty()) {
        mostrarError(Vista_Registrar_Producto_Solido.lblCandicionValidacion, "La condición no puede estar vacía");
    } else if (condicion.length() < 2 || condicion.length() > 15) {
        mostrarError(Vista_Registrar_Producto_Solido.lblCandicionValidacion, "La condición debe tener entre 2 y  caracteres");
    } else if (!condicion.matches("^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\.\\-() ]+$")) {
        mostrarError(Vista_Registrar_Producto_Solido.lblCandicionValidacion, "Solo letras, números, punto, guiones y paréntesis");
    } else if (esPalabraSinSentido(condicion)) {
        mostrarError(Vista_Registrar_Producto_Solido.lblCandicionValidacion, "Condición no válida o sin sentido");
    } else {
        ocultarError(Vista_Registrar_Producto_Solido.lblCandicionValidacion);
    }
}

private void validarUnidadMedidaSolido() {
    Object sel = Vista_Registrar_Producto_Solido.jComboUnidaSolido.getSelectedItem();
    String unidad = (sel != null) ? sel.toString().trim() : "";
    
    if (unidad.isEmpty() || unidad.equalsIgnoreCase("Seleccione")) {
        mostrarError(Vista_Registrar_Producto_Solido.lblUnidadDeMedidaValidacion, "Debe seleccionar una unidad de medida");
    } else {
        ocultarError(Vista_Registrar_Producto_Solido.lblUnidadDeMedidaValidacion);
    }
}

private void validarCategoriaSolido() {
    Object sel = Vista_Registrar_Producto_Solido.jComboCategoria.getSelectedItem();
    String cat = (sel != null) ? sel.toString().trim() : "";
    
    if (cat.isEmpty() || cat.equalsIgnoreCase("Seleccione")) {
        mostrarError(Vista_Registrar_Producto_Solido.lblCategoriaValidacion, "Debe seleccionar una categoría");
    } else {
        ocultarError(Vista_Registrar_Producto_Solido.lblCategoriaValidacion);
    }
}

private void validarMarcaSolido() {
    Object sel = Vista_Registrar_Producto_Solido.jComboMarca.getSelectedItem();
    String marca = (sel != null) ? sel.toString().trim() : "";
    
    if (marca.isEmpty() || marca.equalsIgnoreCase("Seleccione")) {
        mostrarError(Vista_Registrar_Producto_Solido.lblMarcaValidacion, "Debe seleccionar una marca");
    } else {
        ocultarError(Vista_Registrar_Producto_Solido.lblMarcaValidacion);
    }
}
  /*  private void validarProveedorSolido() {
        Object sel = Registrar_Productoss.ComboxBoxProveedor.getSelectedItem();
        String prov = (sel != null) ? sel.toString().trim() : "";
        if (prov.isEmpty() || prov.equalsIgnoreCase("Seleccione")) {
            mostrarError(Registrar_Productoss.lblProveedorValidacion, "Debe seleccionar un proveedor");
        } else {
            ocultarError(Registrar_Productoss.lblProveedorValidacion);
        }
    }*/

 
    public boolean validarFormularioProductoSolido() {
        validarNombreProductoSolido();
        validarCompatibilidadSolido();
       validarUnidadMedidaSolido();
        validarCondicionSolido();
        validarCategoriaSolido();
       
        validarMarcaSolido();

        return !Vista_Registrar_Producto_Solido.lblNombreSolidoValidavion.isVisible()
                && !Vista_Registrar_Producto_Solido.lblCompartibilidadValidacionSolido.isVisible()
                && !Vista_Registrar_Producto_Solido.lblUnidadDeMedidaValidacion.isVisible()
                && !Vista_Registrar_Producto_Solido.lblCandicionValidacion.isVisible()
                && !Vista_Registrar_Producto_Solido.lblCategoriaValidacion.isVisible()
                && !Vista_Registrar_Producto_Solido.lblMarcaValidacion.isVisible();
    }

    public void Registrar_ProductosSolidoConValidacion() throws ClassNotFoundException {
        if (!validarFormularioProductoSolido()) {
            JOptionPane.showMessageDialog(null, "Corrige los campos marcados antes de registrar.");
            return;
        }

        try {
            Producto p = new Producto();
            p.setNombre(Vista_Registrar_Producto_Solido.txtNombreProductoSolido.getText().trim());
            p.setCompartibilidad(Vista_Registrar_Producto_Solido.txtSolidoCompartibilidad.getText().trim());

           Object unidadSel = Vista_Registrar_Producto_Solido.jComboUnidaSolido.getSelectedItem();
            p.setUnida_De_Medida(unidadSel != null ? unidadSel.toString() : "");

            p.setCondicion(Vista_Registrar_Producto_Solido.txtCondicionSolido.getText().trim());

            Object catSel = Vista_Registrar_Producto_Solido.jComboCategoria.getSelectedItem();
            p.setIdCategoria(catSel != null ? catSel.toString() : "");

          /*  Object provSel = Registrar_Productoss.ComboxBoxProveedor.getSelectedItem();
            p.setIdProveedor(provSel != null ? provSel.toString() : ""); */

            Object marcaSel = Vista_Registrar_Producto_Solido.jComboMarca.getSelectedItem();
            p.setIdMarca(marcaSel != null ? marcaSel.toString() : "");

            productoDao.RegistrarProductoSolido(p);
          
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al registrar producto: " + ex.getMessage());
        }
    }

    // ------------------- ACTUALIZAR PRODUCTO SÓLIDO -------------------
    public void actualizarProductoSolidoConValidacion() throws ClassNotFoundException {
        if (!validarFormularioProductoSolido()) {
            JOptionPane.showMessageDialog(null, "Corrige los errores antes de actualizar el producto sólido.");
            return;
        }

        try {
            Producto p = new Producto();
            // Si tienes un campo de ID en el formulario (por ejemplo txtIdProductoSolido), asignalo aquí:
          ;

            p.setNombre(Vista_Registrar_Producto_Solido.txtNombreProductoSolido.getText().trim());
            p.setCompartibilidad(Vista_Registrar_Producto_Solido.txtSolidoCompartibilidad.getText().trim());

            Object unidadSel =Vista_Registrar_Producto_Solido.jComboUnidaSolido.getSelectedItem();
            p.setUnida_De_Medida(unidadSel != null ? unidadSel.toString() : "");

            p.setCondicion(Vista_Registrar_Producto_Solido.txtCondicionSolido.getText().trim());

            Object catSel = Vista_Registrar_Producto_Solido.jComboCategoria.getSelectedItem();
            p.setIdCategoria(catSel != null ? catSel.toString() : "");

           /* Object provSel = Registrar_Productoss.ComboxBoxProveedor.getSelectedItem();
            p.setIdProveedor(provSel != null ? provSel.toString() : "");*/

            Object marcaSel = Vista_Registrar_Producto_Solido.jComboMarca.getSelectedItem();
            p.setIdMarca(marcaSel != null ? marcaSel.toString() : "");

            try {
                productoDao. Actulizar_ProductoSolido();
            } catch (SQLException ex) {
                System.getLogger(Validar_Producto.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
         
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(null, "ID de producto inválido: " + nfe.getMessage());
        }
    }
    
    
    
    
   public void inicializarValidacionesProductoUnidad() {
        // Ocultar etiquetas de validación al iniciar
      Vista_Registrar_Producto_Unidad.lblNombreUnidadValidacion.setVisible(false);
        Vista_Registrar_Producto_Unidad.lblCompartibilidaUnidadValidacion.setVisible(false);
        Vista_Registrar_Producto_Unidad.lblUnidadMedidaValidacion.setVisible(false);
        Vista_Registrar_Producto_Unidad.lblEspecificaionesValidacion.setVisible(false);
        Vista_Registrar_Producto_Unidad.lblNumeroSerialValidacion.setVisible(false);
       Vista_Registrar_Producto_Unidad.lblCategoriaUnidadValidacion.setVisible(false);
        
        Vista_Registrar_Producto_Unidad.lblMarcaValidscion.setVisible(false);

        // Listeners de campos de texto
     Vista_Registrar_Producto_Unidad.txtNombreUnidad.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { validarNombreUnidad(); }
            public void removeUpdate(DocumentEvent e) { validarNombreUnidad(); }
            public void changedUpdate(DocumentEvent e) { validarNombreUnidad(); }
        });

        Vista_Registrar_Producto_Unidad.txtCompartibilidadUnidad.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { validarCompatibilidadUnidad(); }
            public void removeUpdate(DocumentEvent e) { validarCompatibilidadUnidad(); }
            public void changedUpdate(DocumentEvent e) { validarCompatibilidadUnidad(); }
        });

        Vista_Registrar_Producto_Unidad.txtEspecificacionTecnica.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { validarEspecificacionesUnidad(); }
            public void removeUpdate(DocumentEvent e) { validarEspecificacionesUnidad(); }
            public void changedUpdate(DocumentEvent e) { validarEspecificacionesUnidad(); }
        });

       Vista_Registrar_Producto_Unidad.txtNumeroSerial.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { validarNumeroSerial(); }
            public void removeUpdate(DocumentEvent e) { validarNumeroSerial(); }
            public void changedUpdate(DocumentEvent e) { validarNumeroSerial(); }
        });

        // ComboBoxes
     Vista_Registrar_Producto_Unidad.JcomoBoxUnidadDeMedida.addActionListener(e -> validarUnidadMedidaUnidad());
        Vista_Registrar_Producto_Unidad.jComboxCategoria.addActionListener(e -> validarCategoriaUnidad());
        
        Vista_Registrar_Producto_Unidad.JcomoBoxMarca.addActionListener(e -> validarMarcaUnidad());
    }

    private void validarNombreUnidad() {
    String nombre = Vista_Registrar_Producto_Unidad.txtNombreUnidad.getText().trim();
    if (nombre.isEmpty()) {
        mostrarError(Vista_Registrar_Producto_Unidad.lblNombreUnidadValidacion, "El nombre no puede estar vacío");
    } else if (nombre.length() < 3 || nombre.length() > 25) {
        mostrarError(Vista_Registrar_Producto_Unidad.lblNombreUnidadValidacion, "Entre 3 y 25 caracteres");
    } else if (!nombre.matches("^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ().\\- ]+$")) {
        mostrarError(Vista_Registrar_Producto_Unidad.lblNombreUnidadValidacion, "Solo letras, números y guiones");
    } else if (esPalabraSinSentido(nombre)) {
        mostrarError(Vista_Registrar_Producto_Unidad.lblNombreUnidadValidacion, "Nombre no válido");
    } else {
        ocultarError(Vista_Registrar_Producto_Unidad.lblNombreUnidadValidacion);
    }
}

private void validarCompatibilidadUnidad() {
    String compat = Vista_Registrar_Producto_Unidad.txtCompartibilidadUnidad.getText().trim();
    if (compat.isEmpty()) {
        mostrarError(Vista_Registrar_Producto_Unidad.lblCompartibilidaUnidadValidacion, "Campo obligatorio");
    } else if (compat.length() < 3 || compat.length() > 30) {
        mostrarError(Vista_Registrar_Producto_Unidad.lblCompartibilidaUnidadValidacion, "Entre 3 y 30 caracteres");
    } else if (!compat.matches("^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ().\\- ]+$")) {
        mostrarError(Vista_Registrar_Producto_Unidad.lblCompartibilidaUnidadValidacion, "Solo letras, números y guiones");
    } else if (esPalabraSinSentido(compat)) {
        mostrarError(Vista_Registrar_Producto_Unidad.lblCompartibilidaUnidadValidacion, "Valor no válido");
    } else {
        ocultarError(Vista_Registrar_Producto_Unidad.lblCompartibilidaUnidadValidacion);
    }
}

private void validarUnidadMedidaUnidad() {
    Object sel = Vista_Registrar_Producto_Unidad.JcomoBoxUnidadDeMedida.getSelectedItem();
    String unidad = (sel != null) ? sel.toString().trim() : "";
    if (unidad.isEmpty() || unidad.equalsIgnoreCase("Seleccione")) {
        mostrarError(Vista_Registrar_Producto_Unidad.lblUnidadMedidaValidacion, "Seleccione una unidad");
    } else {
        ocultarError(Vista_Registrar_Producto_Unidad.lblUnidadMedidaValidacion);
    }
}

private void validarEspecificacionesUnidad() {
    String esp = Vista_Registrar_Producto_Unidad.txtEspecificacionTecnica.getText().trim();
    if (esp.isEmpty()) {
        mostrarError(Vista_Registrar_Producto_Unidad.lblEspecificaionesValidacion, "Campo obligatorio");
    } else if (esp.length() < 3 || esp.length() > 50) {
        mostrarError(Vista_Registrar_Producto_Unidad.lblEspecificaionesValidacion, "Entre 3 y 50 caracteres");
    } else if (!esp.matches("^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ().,\\- ]+$")) {
        mostrarError(Vista_Registrar_Producto_Unidad.lblEspecificaionesValidacion, "Caracteres no permitidos");
    } else if (esPalabraSinSentido(esp)) {
        mostrarError(Vista_Registrar_Producto_Unidad.lblEspecificaionesValidacion, "Valor no válido");
    } else {
        ocultarError(Vista_Registrar_Producto_Unidad.lblEspecificaionesValidacion);
    }
}

private void validarNumeroSerial() {
    String serial = Vista_Registrar_Producto_Unidad.txtNumeroSerial.getText().trim();
    if (serial.isEmpty()) {
        mostrarError(Vista_Registrar_Producto_Unidad.lblNumeroSerialValidacion, "Campo obligatorio");
    } else if (serial.length() < 3 || serial.length() > 20) {
        mostrarError(Vista_Registrar_Producto_Unidad.lblNumeroSerialValidacion, "Entre 3 y 20 caracteres");
    } else if (!serial.matches("^[a-zA-Z0-9().\\- ]+$")) {
        mostrarError(Vista_Registrar_Producto_Unidad.lblNumeroSerialValidacion, "Solo letras, números y guiones");
    } else {
        ocultarError(Vista_Registrar_Producto_Unidad.lblNumeroSerialValidacion);
    }
}

private void validarCategoriaUnidad() {
    Object sel = Vista_Registrar_Producto_Unidad.jComboxCategoria.getSelectedItem();
    String cat = (sel != null) ? sel.toString().trim() : "";
    if (cat.isEmpty() || cat.equalsIgnoreCase("Seleccione")) {
        mostrarError(Vista_Registrar_Producto_Unidad.lblCategoriaUnidadValidacion, "Seleccione una categoría");
    } else {
        ocultarError(Vista_Registrar_Producto_Unidad.lblCategoriaUnidadValidacion);
    }
}

private void validarMarcaUnidad() {
    Object sel = Vista_Registrar_Producto_Unidad.JcomoBoxMarca.getSelectedItem();
    String marca = (sel != null) ? sel.toString().trim() : "";
    if (marca.isEmpty() || marca.equalsIgnoreCase("Seleccione")) {
        mostrarError(Vista_Registrar_Producto_Unidad.lblMarcaValidscion, "Seleccione una marca");
    } else {
        ocultarError(Vista_Registrar_Producto_Unidad.lblMarcaValidscion);
    }
}

    // ------------------- VALIDAR FORMULARIO -------------------
    public boolean validarFormularioProductoUnidad() {
        validarNombreUnidad();
        validarCompatibilidadUnidad();
        validarUnidadMedidaUnidad();
        validarEspecificacionesUnidad();
        validarNumeroSerial();
        validarCategoriaUnidad();
        
        validarMarcaUnidad();

        return !Vista_Registrar_Producto_Unidad.lblNombreUnidadValidacion.isVisible()
                && !Vista_Registrar_Producto_Unidad.lblCompartibilidaUnidadValidacion.isVisible()
                && !Vista_Registrar_Producto_Unidad.lblUnidadMedidaValidacion.isVisible()
                && !Vista_Registrar_Producto_Unidad.lblEspecificaionesValidacion.isVisible()
                && !Vista_Registrar_Producto_Unidad.lblNumeroSerialValidacion.isVisible()
                && ! Vista_Registrar_Producto_Unidad.lblCategoriaUnidadValidacion.isVisible()
                && !Vista_Registrar_Producto_Unidad.lblMarcaValidscion.isVisible();
    }

    // ------------------- REGISTRAR PRODUCTO UNIDAD -------------------
    public void registrarProductoUnidadValidado() throws ClassNotFoundException {
        if (!validarFormularioProductoUnidad()) {
            JOptionPane.showMessageDialog(null, "Corrige los errores antes de registrar el producto de unidad.");
            return;
        }

        try {
            Producto p = new Producto();
            p.setNombre(Vista_Registrar_Producto_Unidad.txtNombreUnidad.getText().trim());
            p.setCompartibilidad(Vista_Registrar_Producto_Unidad.txtCompartibilidadUnidad.getText().trim());

            Object unidadSel = Vista_Registrar_Producto_Unidad.JcomoBoxUnidadDeMedida.getSelectedItem();
            p.setUnida_De_Medida(unidadSel != null ? unidadSel.toString() : "");

            p.setEspecificaciones(Vista_Registrar_Producto_Unidad.txtEspecificacionTecnica.getText().trim());
            p.setNumeroSerial(Vista_Registrar_Producto_Unidad.txtNumeroSerial.getText().trim());

            Object catSel = Vista_Registrar_Producto_Unidad.jComboxCategoria.getSelectedItem();
            p.setIdCategoria(catSel != null ? catSel.toString() : "");
/*
            Object provSel = Registrar_Productoss.ComboxBoxProveedor.getSelectedItem();
            p.setIdProveedor(provSel != null ? provSel.toString() : "");
*/
            Object marcaSel = Vista_Registrar_Producto_Unidad.JcomoBoxMarca.getSelectedItem();
            p.setIdMarca(marcaSel != null ? marcaSel.toString() : "");

            productoDao.RegistarUnidad(p);
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al registrar producto: " + ex.getMessage());
        }
    }

    // ------------------- ACTUALIZAR PRODUCTO UNIDAD -------------------
    public void actualizarProductoUnidadConValidacion() throws ClassNotFoundException {
        if (!validarFormularioProductoUnidad()) {
            JOptionPane.showMessageDialog(null, "Corrige los errores antes de actualizar el producto de unidad.");
            return;
        }

        try {
            Producto p = new Producto();
            // Si tienes un campo de ID en el formulario (ej: txtIdProductoUnidad), asignarlo aquí:
            // p.setId(Integer.parseInt(Registrar_Productos.txtIdProductoUnidad.getText().trim()));

            p.setNombre(Vista_Registrar_Producto_Unidad.txtNombreUnidad.getText().trim());
            p.setCompartibilidad(Vista_Registrar_Producto_Unidad.txtCompartibilidadUnidad.getText().trim());

            Object unidadSel = Vista_Registrar_Producto_Unidad.JcomoBoxUnidadDeMedida.getSelectedItem();
            p.setUnida_De_Medida(unidadSel != null ? unidadSel.toString() : "");

            p.setEspecificaciones(Vista_Registrar_Producto_Unidad.txtEspecificacionTecnica.getText().trim());
            p.setNumeroSerial(Vista_Registrar_Producto_Unidad.txtNumeroSerial.getText().trim());

            Object catSel = Vista_Registrar_Producto_Unidad.jComboxCategoria.getSelectedItem();
            p.setIdCategoria(catSel != null ? catSel.toString() : "");

           /* Object provSel = Registrar_Productoss.ComboxBoxProveedor.getSelectedItem();
            p.setIdProveedor(provSel != null ? provSel.toString() : "");*/

            Object marcaSel = Vista_Registrar_Producto_Unidad.JcomoBoxMarca.getSelectedItem();
            p.setIdMarca(marcaSel != null ? marcaSel.toString() : "");

            try {
                productoDao.ActualizarProducdoUnidad();
                Vista_Registrar_Producto_Unidad.btbCancelarActualizacion.setVisible(false);
       Vista_Registrar_Producto_Unidad.btbConfirmarActualizacion.setVisible(false);
       
     Vista_Registrar_Producto_Unidad.btbEliminarProducto.setVisible(true);
     Vista_Registrar_Producto_Unidad.btbActualizarProducto.setVisible(true);
    Vista_Registrar_Producto_Unidad.btbRegistrarProdutoUnidad.setVisible(true);
       
     Vista_Registrar_Producto_Unidad.txtNombreUnidad.setText("");
     Vista_Registrar_Producto_Unidad.txtNumeroSerial.setText("");
  Vista_Registrar_Producto_Unidad.txtCompartibilidadUnidad.setText("");
    Vista_Registrar_Producto_Unidad.txtEspecificacionTecnica.setText("");
            } catch (SQLException ex) {
                System.getLogger(Validar_Producto.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
          
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(null, "ID de producto inválido: " + nfe.getMessage());
        } 
    
    
    
    
    
    }
    
    
}

    
    
    
    
    
    
 
