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
import Vista_Gestion_Productos.Vista_Registrar_Producto_Solido;
import Vista_Usuari_Empleado.Menu_Sistema;
import dictionary.AutoCorrectorGlobal;
import java.sql.SQLException;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
/**
 *
 * @author avila
 */
public class Controlador_Registro_Producto_Solido implements ActionListener{
    
   private final Menu_Sistema menu;
   
   private  Vista_Registrar_Producto_Solido Producto_Solido;
   
   
   public Controlador_Registro_Producto_Solido (Menu_Sistema menu){
       this.menu=menu;
       
   }
    
    
   
   
   public  void MostrarVistaRegistroSolido (){
       
       if (Producto_Solido == null) {

     Producto_Solido = new Vista_Registrar_Producto_Solido();
     AgregarListeners();
           
       }
  
       Producto_Solido.setVisible(true);
       AutoCorrectorGlobal.aplicar(Producto_Solido);
       
       CargarDatosEnLaVista();
       
       menu.setVisible(false);
       
       
   }
    
   
   private  void AgregarListeners (){
       Producto_Solido.btbVolverMenu.addActionListener(this);
       Producto_Solido.btbEliminarSolido.addActionListener(this);
       Producto_Solido.btbConfirmarActulizacion.addActionListener(this);
       Producto_Solido.btbCancelarActuailizacion.addActionListener(this);
       Producto_Solido.btbActualizarSolido.addActionListener(this);
       Producto_Solido.btbRegistrarProductoSolido.addActionListener(this);
   }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        if (e.getSource() == Producto_Solido.btbVolverMenu){
            VolverMenu();
        }
        
        
        else if (e.getSource() == Producto_Solido.btbRegistrarProductoSolido){
            
            RegistrarProductoSolido();
        }
        
        else if (e.getSource() == Producto_Solido.btbEliminarSolido){
            EliminarProductoSolido();
        }
        
        else if (e.getSource() == Producto_Solido.btbActualizarSolido){
            SeleccionarProductoParaActualizarlo();
        }
        
        else if (e.getSource() == Producto_Solido.btbCancelarActuailizacion){
            CancelarActualizacion();
        } 
        
        else if (e.getSource() == Producto_Solido.btbConfirmarActulizacion){
            ActualizarProducto();
        }
        
    }
    
    
    
    
    private  void VolverMenu (){
        menu.setVisible(true);
        
        Producto_Solido.setVisible(false);
    }
    
    

    private  void ActualizarProducto (){
        Validacion.Validar_Producto validar_Producto = new Validar_Producto();
        
       try {
           validar_Producto.actualizarProductoSolidoConValidacion();
       } catch (ClassNotFoundException ex) {
           System.getLogger(Controlador_Registro_Producto_Solido.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
       }
    }
    
    private  void RegistrarProductoSolido (){
       Validacion.Validar_Producto validar_Producto = new Validar_Producto();
       try {
           validar_Producto. Registrar_ProductosSolidoConValidacion();
       } catch (ClassNotFoundException ex) {
           System.getLogger(Controlador_Registro_Producto_Solido.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
       }
    }
    
    
    
    private void EliminarProductoSolido (){
        
        
      ModeloDAO.ProductoDao productoDao = new ProductoDao();
      
       try {
           productoDao.EliminarProductoSolido();
       } catch (ClassNotFoundException ex) {
           System.getLogger(Controlador_Registro_Producto_Solido.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
       } catch (SQLException ex) {
           System.getLogger(Controlador_Registro_Producto_Solido.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
       }
        
        
    }
    
    
    private  void CancelarActualizacion (){
        Producto_Solido.btbConfirmarActulizacion.setVisible(false);
        Producto_Solido.btbCancelarActuailizacion.setVisible(false);
        Producto_Solido.btbEliminarSolido.setVisible(true);
        Producto_Solido.btbActualizarSolido.setVisible(true);
        Producto_Solido.btbRegistrarProductoSolido.setVisible(true);
        Producto_Solido.txtNombreProductoSolido.setText("");
        Producto_Solido.txtSolidoCompartibilidad.setText("");
        Producto_Solido.txtCondicionSolido.setText("");
        
    }
        
    private  void SeleccionarProductoParaActualizarlo (){
        
        int filla = Vista_Registrar_Producto_Solido.TablaSolido.getSelectedRow();
        
        if (filla ==-1) {
            
            JOptionPane.showMessageDialog(null, "Debe seleccionar un producto para actualizarlo");
            return;
            
        } else {
             Producto_Solido.btbConfirmarActulizacion.setVisible(true);
        Producto_Solido.btbCancelarActuailizacion.setVisible(true);
        Producto_Solido.btbEliminarSolido.setVisible(false);
        Producto_Solido.btbActualizarSolido.setVisible(false);
        Producto_Solido.btbRegistrarProductoSolido.setVisible(false);
        
        Producto_Solido.txtNombreProductoSolido.setText(Producto_Solido.TablaSolido.getValueAt(filla, 1).toString());
        Producto_Solido.txtCondicionSolido.setText(Producto_Solido.TablaSolido.getValueAt(filla, 2).toString());
        Producto_Solido.txtSolidoCompartibilidad.setText(Producto_Solido.TablaSolido.getValueAt(filla, 3).toString());
        seleccionarItemCombo(Producto_Solido.jComboUnidaSolido, Producto_Solido.TablaSolido.getValueAt(filla, 4).toString());
            seleccionarItemCombo(Producto_Solido.jComboCategoria, Producto_Solido.TablaSolido.getValueAt(filla, 5).toString());
            seleccionarItemCombo(Producto_Solido.jComboMarca, Producto_Solido.TablaSolido.getValueAt(filla,6).toString());
        }
        
        
        
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
    
    
    
    
    
    
    
    
    private  void CargarDatosEnLaVista (){
    
       ModeloDAO.CategoriaDao categoriaDao = new CategoriaDao();
       try {
           categoriaDao.CargarComboxBoxCategoriSolido();
       } catch (ClassNotFoundException ex) {
           System.getLogger(Controlador_Registro_Producto_Solido.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
       } catch (SQLException ex) {
           System.getLogger(Controlador_Registro_Producto_Solido.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
       }
       
       
       categoriaDao.agregarBuscadorCategoriaSolido();
       
       
       
       
       ModeloDAO.MarcaDao marcaDao = new MarcaDao();
       try {
           marcaDao.CargarComboxBoxMacarSolido();
       } catch (ClassNotFoundException ex) {
           System.getLogger(Controlador_Registro_Producto_Solido.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
       } catch (SQLException ex) {
           System.getLogger(Controlador_Registro_Producto_Solido.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
       }
       marcaDao.agregarBuscadorMarcaSolido();
       
       ModeloDAO.ProductoDao productoDao = new ProductoDao();
       try {
           productoDao. MostrarProductosSolidos();
       } catch (ClassNotFoundException ex) {
           System.getLogger(Controlador_Registro_Producto_Solido.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
       } catch (SQLException ex) {
           System.getLogger(Controlador_Registro_Producto_Solido.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
       }
       
       
       
       productoDao.agregarFiltroBusquedaProductoSolido();
       
       CancelarActualizacion();
       
       Validacion.Validar_Producto validar_Producto = new Validar_Producto();
       
       validar_Producto.inicializarValidacionesProductoSolido();
       
       
       
    }
    
    
    
   
    
    
    
    
    
    
    
}
