/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import ModeloDAO.ProductoDao;
import ModeloDAO.ProveedorDao;
import Validacion.Validar_Proveedor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Vista_Usuari_Empleado.Menu_Sistema;
import Vista_Gestionar_Proveedor.Gestionar_Proveedor;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;


/**
 *
 * @author avila
 */
public class Controlador_Proveedor implements ActionListener{

   
    private  final Menu_Sistema menu;
    private Gestionar_Proveedor proveedor;
    
    public  Controlador_Proveedor (Menu_Sistema menu){
        this.menu= menu;
    }
    
    
    public  void MostrarVistaProveedor (){
        
        if (proveedor == null) {
            
            try {
                proveedor = new Gestionar_Proveedor();
                //  Agregar Listener Aqui
                AgregarlISTENERS();
            } catch (ClassNotFoundException ex) {
                System.getLogger(Controlador_Proveedor.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            } catch (SQLException ex) {
                System.getLogger(Controlador_Proveedor.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
            
        }
        
        proveedor.setVisible(true);
        menu.setVisible(false);
    }
    
    
    
    
    private void AgregarlISTENERS (){
        
        proveedor.btbVolverProveedor.addActionListener(this);
        proveedor.btbConfirmarActualizacion.addActionListener(this);
        proveedor.btbEliminarProveedor.addActionListener(this);
        proveedor.btbActualizarProveedor.addActionListener(this);
        proveedor.btbCancelarProveedor.addActionListener(this);
        proveedor.btbCancelarProductoSuministrado.addActionListener(this);
        proveedor.btbProductoDEProveedor.addActionListener(this);
        proveedor.btbRegistrarProveedor.addActionListener(this);
        
    }
    
    
    
    @Override
    public void actionPerformed(ActionEvent e) {
       
       if (e.getSource() == proveedor.btbVolverProveedor){
           VolveralMenu();
       } 
       
       else if (e.getSource() == proveedor.btbConfirmarActualizacion){
           ConfirmarActualizacion();
       }
        
       else if (e.getSource() == proveedor.btbEliminarProveedor){
           EliminarProveedor();
       }
       
       else if (e.getSource() == proveedor.btbActualizarProveedor){
           ActualizarProveedor();
       }
       
       else if (e.getSource() == proveedor.btbCancelarProveedor){
           CancelarProveedor();
       }
       
       else if (e.getSource() == proveedor.btbCancelarProductoSuministrado){
           CancelarProductoSuministrado();
       }
       
       else if (e.getSource() == proveedor.btbProductoDEProveedor){
           ProductoProveedor();
       }
       
       else if (e.getSource() == proveedor.btbRegistrarProveedor){
           RegistrarProveedor();
       }
        
    }
    
    
    private  void VolveralMenu (){
        
        menu.setVisible(true);
        proveedor.setVisible(false);
 
    }
    
   
    private  void ConfirmarActualizacion (){
         Validacion.Validar_Proveedor validar_Proveedor = new Validar_Proveedor();
        try {
            validar_Proveedor.actualizarProveedorValidado();
        } catch (ClassNotFoundException ex) {
            System.getLogger(Controlador_Proveedor.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
    
    
    private  void EliminarProveedor (){
         ProveedorDao DAO = new ProveedorDao();
        try {
            DAO.EliminarProveedorBajoSeguridad();
        } catch (ClassNotFoundException ex) {
            System.getLogger(Controlador_Proveedor.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (SQLException ex) {
            System.getLogger(Controlador_Proveedor.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
    
    
    
    private  void ActualizarProveedor (){
      
 int fila = Gestionar_Proveedor.TablaProveedor.getSelectedRow();
 
    if (fila == -1) {
        
        JOptionPane.showMessageDialog(null, " Debe seleccionar un proveedor si lo desea actualizar ");
        
    } else {
        
     
      Gestionar_Proveedor.txtNombreProveedor.setText(Gestionar_Proveedor.TablaProveedor.getValueAt(fila, 1).toString());
       Gestionar_Proveedor.txtTelefonoProveedor.setText(Gestionar_Proveedor.TablaProveedor.getValueAt(fila, 2).toString());
       Gestionar_Proveedor.txtDireccionProveedor.setText(Gestionar_Proveedor.TablaProveedor.getValueAt(fila, 3).toString());
       Gestionar_Proveedor.txtEmailProveedor.setText(Gestionar_Proveedor.TablaProveedor.getValueAt(fila, 4).toString());
       Gestionar_Proveedor.txtRFCProveedor.setText(Gestionar_Proveedor.TablaProveedor.getValueAt(fila, 5).toString());
       Gestionar_Proveedor.txtID.setText(Gestionar_Proveedor.TablaProveedor.getValueAt(fila, 0).toString());
        
       
        Gestionar_Proveedor.btbEliminarProveedor.setVisible(false);
        Gestionar_Proveedor.btbActualizarProveedor.setVisible(false);
        Gestionar_Proveedor.btbRegistrarProveedor.setVisible(false);
        Gestionar_Proveedor.btbProductoDEProveedor.setVisible(false);
        
        Gestionar_Proveedor.btbCancelarProveedor.setVisible(true);
        Gestionar_Proveedor.btbConfirmarActualizacion.setVisible(true);
      Gestionar_Proveedor.btbCancelarProductoSuministrado.setVisible(false);
    }
    }
    
    
    private  void CancelarProveedor (){
        Gestionar_Proveedor.btbCancelarProveedor.setVisible(false);
  Gestionar_Proveedor.btbConfirmarActualizacion.setVisible(false);
  Gestionar_Proveedor.txtID.setVisible(false);
 
    Gestionar_Proveedor.btbCancelarProductoSuministrado.setVisible(false);
  Gestionar_Proveedor.btbActualizarProveedor.setVisible(true);
  Gestionar_Proveedor.btbEliminarProveedor.setVisible(true);
  Gestionar_Proveedor.btbRegistrarProveedor.setVisible(true);
  Gestionar_Proveedor.btbProductoDEProveedor.setVisible(true);
  Gestionar_Proveedor.btbProductoDEProveedor.setVisible(true);
  Gestionar_Proveedor.txtDireccionProveedor.setText("");
  Gestionar_Proveedor.txtNombreProveedor.setText("");
  Gestionar_Proveedor.txtEmailProveedor.setText("");
  Gestionar_Proveedor.txtRFCProveedor.setText("");
  Gestionar_Proveedor.txtTelefonoProveedor.setText("");
    
    }
    
    
    private  void CancelarProductoSuministrado (){
       
     Gestionar_Proveedor.btbActualizarProveedor.setVisible(true);
  Gestionar_Proveedor.btbEliminarProveedor.setVisible(true);
  Gestionar_Proveedor.btbRegistrarProveedor.setVisible(true);
  Gestionar_Proveedor.btbProductoDEProveedor.setVisible(true);
  Gestionar_Proveedor.btbProductoDEProveedor.setVisible(true);
    Gestionar_Proveedor.btbCancelarProductoSuministrado.setVisible(false);
    
    
  ModeloDAO.ProveedorDao pr = new ProveedorDao();
   
       try {
           pr.MostrarListaDeProveedor();
       } catch (ClassNotFoundException ex) {
           Logger.getLogger(controladorVisual.class.getName()).log(Level.SEVERE, null, ex);
       } catch (SQLException ex) {
           Logger.getLogger(controladorVisual.class.getName()).log(Level.SEVERE, null, ex);
       }
  
    
    }
    
    private  void ProductoProveedor (){
          ModeloDAO.ProductoDao producto = new ProductoDao();
        try {
            producto.MostrarListaProductoParaProveedor();
        } catch (ClassNotFoundException ex) {
            System.getLogger(Controlador_Proveedor.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (SQLException ex) {
            System.getLogger(Controlador_Proveedor.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
    
    
    private  void RegistrarProveedor (){
        Validar_Proveedor validar_Proveedor = new Validar_Proveedor();
        
        try {
            validar_Proveedor.registrarProveedorValidado();
        } catch (ClassNotFoundException ex) {
            System.getLogger(Controlador_Proveedor.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
    
}
