/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.Producto;
import ModeloDAO.CategoriaDao;
import ModeloDAO.MarcaDao;
import ModeloDAO.ProductoDao;
import Validacion.Validar_Producto;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Vista_Gestion_Productos.Vista_Registrar_Producto_Unidad;
import Vista_Usuari_Empleado.Menu_Sistema;
import dictionary.AutoCorrectorGlobal;
import java.sql.SQLException;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
/**
 *
 * @author avila
 */
public class Controlador_Producto_Unidad implements ActionListener{
    private final Menu_Sistema menu;
    
    private  Vista_Registrar_Producto_Unidad Producto_Unidad;
    
    public  Controlador_Producto_Unidad(Menu_Sistema menu){
        this.menu = menu;
    }

    
    public  void MostrarVista (){
        if (Producto_Unidad == null) {
            
            Producto_Unidad = new Vista_Registrar_Producto_Unidad();
            
            // Agregar Listeners
            AgregarListenrs();
        }
        
        Producto_Unidad.setVisible(true);
         Validacion.Validar_Producto validar_Producto = new Validar_Producto();
         AutoCorrectorGlobal.aplicar(Producto_Unidad);
        CargarVista();
        
       
        
        menu.setVisible(false);
    }
    
    
    private  void AgregarListenrs (){
        Producto_Unidad.btbVolverMenu.addActionListener(this);
        Producto_Unidad.btbEliminarProducto.addActionListener(this);
        Producto_Unidad.btbConfirmarActualizacion.addActionListener(this);
        Producto_Unidad.btbCancelarActualizacion.addActionListener(this);
        Producto_Unidad.btbActualizarProducto.addActionListener(this);
        Producto_Unidad.btbRegistrarProdutoUnidad.addActionListener(this);
    }
    
    
    @Override
    public void actionPerformed(ActionEvent e) {
        
        if (e.getSource() == Producto_Unidad.btbVolverMenu){
            VolverAlMenu();
        }
        else if (e.getSource() == Producto_Unidad.btbRegistrarProdutoUnidad){
            RegistrarProducto();
                 
        }
        
        else if (e.getSource() == Producto_Unidad.btbEliminarProducto){
            EliminarProducto();
        }
        
        else if (e.getSource() == Producto_Unidad.btbCancelarActualizacion){
            CancelarActualizacion();
        }
        
        else if (e.getSource() == Producto_Unidad.btbActualizarProducto){
            SeleccionarProductoParaActualizar();
        }
        
        else if (e.getSource() == Producto_Unidad.btbConfirmarActualizacion){
            ConfirmarActualizacion();
        }
        
    }
    
     
    
    private  void ConfirmarActualizacion (){
          Validacion.Validar_Producto validar_Producto = new Validar_Producto();
        try {
            validar_Producto. actualizarProductoUnidadConValidacion();
           
        } catch (ClassNotFoundException ex) {
            System.getLogger(Controlador_Producto_Unidad.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
    private  void RegistrarProducto (){
        Validacion.Validar_Producto validar_Producto = new Validar_Producto();
        
        try {
            validar_Producto.registrarProductoUnidadValidado();
        } catch (ClassNotFoundException ex) {
            System.getLogger(Controlador_Producto_Unidad.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
    
    private  void CancelarActualizacion (){
        
       Producto_Unidad.btbCancelarActualizacion.setVisible(false);
       Producto_Unidad.btbConfirmarActualizacion.setVisible(false);
       
       Producto_Unidad.btbEliminarProducto.setVisible(true);
       Producto_Unidad.btbActualizarProducto.setVisible(true);
       Producto_Unidad.btbRegistrarProdutoUnidad.setVisible(true);
       
       Producto_Unidad.txtNombreUnidad.setText("");
       Producto_Unidad.txtNumeroSerial.setText("");
       Producto_Unidad.txtCompartibilidadUnidad.setText("");
       Producto_Unidad.txtEspecificacionTecnica.setText("");
        
        
        
    }
    
    private  void SeleccionarProductoParaActualizar (){
        
        int filla = Vista_Registrar_Producto_Unidad.TablaUnidad.getSelectedRow();
        
        if (filla == -1) {
            
            JOptionPane.showMessageDialog(null, "Debe seleccionar un producto");
            return;
        } else {
            
           Producto_Unidad.btbConfirmarActualizacion.setVisible(true);
           Producto_Unidad.btbCancelarActualizacion.setVisible(true);
           
           Producto_Unidad.btbEliminarProducto.setVisible(false);
           Producto_Unidad.btbActualizarProducto.setVisible(false);
           Producto_Unidad.btbRegistrarProdutoUnidad.setVisible(false);
           
           
           Producto_Unidad.txtNombreUnidad.setText(Producto_Unidad.TablaUnidad.getValueAt(filla,1).toString());
           Producto_Unidad.txtNumeroSerial.setText(Producto_Unidad.TablaUnidad.getValueAt(filla, 2).toString());
           Producto_Unidad.txtCompartibilidadUnidad.setText(Producto_Unidad.TablaUnidad.getValueAt(filla, 3).toString());
            seleccionarItemCombo(Producto_Unidad.JcomoBoxUnidadDeMedida, Producto_Unidad.TablaUnidad.getValueAt(filla, 4).toString());
            Producto_Unidad.txtEspecificacionTecnica.setText(Producto_Unidad.TablaUnidad.getValueAt(filla, 5).toString());
            seleccionarItemCombo(Producto_Unidad.jComboxCategoria, Producto_Unidad.TablaUnidad.getValueAt(filla, 6).toString());
            seleccionarItemCombo(Producto_Unidad.JcomoBoxMarca, Producto_Unidad.TablaUnidad.getValueAt(filla, 7).toString());
            
            
        }
        
        
    }
    
    
    private  void EliminarProducto (){
        ModeloDAO.ProductoDao productoDao = new ProductoDao();
        try {
            productoDao.EliminarProductoUnidad();
        } catch (ClassNotFoundException ex) {
            System.getLogger(Controlador_Producto_Unidad.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (SQLException ex) {
            System.getLogger(Controlador_Producto_Unidad.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
    
    
    
    private  void VolverAlMenu (){
        menu.setVisible(true);
        
        Producto_Unidad.setVisible(false);
    }
   
    
    
    
    
    
    private  void CargarVista (){
        
        Validacion.Validar_Producto validar_Producto = new Validar_Producto();
        validar_Producto.inicializarValidacionesProductoUnidad();
        ModeloDAO.CategoriaDao categoriaDao = new CategoriaDao();
        try {
            categoriaDao.CargarComboxBoxCategoriUnidad();
        } catch (ClassNotFoundException ex) {
            System.getLogger(Controlador_Producto_Unidad.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (SQLException ex) {
            System.getLogger(Controlador_Producto_Unidad.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        
        categoriaDao.agregarBuscadorCategoriaUnida();
        
        ModeloDAO.MarcaDao marcaDao = new MarcaDao();
        
        try {
            marcaDao.CargarComboxBoxMacarUnidad();
        } catch (ClassNotFoundException ex) {
            System.getLogger(Controlador_Producto_Unidad.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (SQLException ex) {
            System.getLogger(Controlador_Producto_Unidad.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
         marcaDao.agregarBuscadorMarcaUnidad() ;
         
         
         ModeloDAO.ProductoDao productoDao = new ProductoDao();
        try {
            productoDao. MostrarProductosUnidad();
        } catch (ClassNotFoundException ex) {
            System.getLogger(Controlador_Producto_Unidad.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (SQLException ex) {
            System.getLogger(Controlador_Producto_Unidad.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        
        productoDao.agregarFiltroBusquedaProductoUnidad();
        
        CancelarActualizacion();
        
        
       
    }
    
    
    
      private void seleccionarItemCombo(JComboBox combo, Object valor) {
    if (valor == null) return;
    String valorStr = valor.toString().trim().toLowerCase();

    for (int i = 0; i < combo.getItemCount(); i++) {
        String itemStr = combo.getItemAt(i).toString().trim().toLowerCase();

        // 1️⃣ Coincidencia exacta
        if (itemStr.equals(valorStr)) {
            combo.setSelectedIndex(i);
            return;
        }

        // 2️⃣ Dividir por "|" y comparar cada parte
        String[] partes = itemStr.split("\\|");
        for (String parte : partes) {
            if (parte.trim().equalsIgnoreCase(valorStr)) {
                combo.setSelectedIndex(i);
                return;
            }
        }

        // 3️⃣ Último recurso: el valor está contenido en todo el ítem
        if (itemStr.contains(valorStr)) {
            combo.setSelectedIndex(i);
            return;
        }
    }
}
    
    
    
}
