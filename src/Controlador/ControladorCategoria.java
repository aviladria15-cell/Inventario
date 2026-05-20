/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import ModeloDAO.CategoriaDao;
import Validacion.Validar_Categoria;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Vista_Usuari_Empleado.Menu_Sistema;
import Vista_Gestion_Categoria.frm_Categoria;
import java.sql.SQLException;
import javax.swing.JOptionPane;
/**
 *
 * @author avila
 */
public class ControladorCategoria implements ActionListener {
    
    
    private final Menu_Sistema menu;
    private frm_Categoria categoria;
    
    
    public ControladorCategoria (Menu_Sistema menu){
        this.menu=menu;
        
    }
   
    
    public  void MostrarVista(){
        if (categoria == null) {
            
            try {
                categoria = new frm_Categoria();
                AgregarAccionesBotones();
            } catch (ClassNotFoundException ex) {
                System.getLogger(ControladorCategoria.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            } catch (SQLException ex) {
                System.getLogger(ControladorCategoria.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
            
        }
 
        categoria.setVisible(true);
        menu.setVisible(false);
        
    }
    
    
    private void  AgregarAccionesBotones (){
        categoria.btbActualizarVerdadero.addActionListener(this);
        categoria.btbVolverCategoria.addActionListener(this);
        categoria.btbEliminar.addActionListener(this);
        categoria.btbActualizarVerdadero.addActionListener(this);
        categoria.btbFiltrarCategoria.addActionListener(this);
        categoria.btbRegistrar.addActionListener(this);
        categoria.btbCancelar.addActionListener(this);
        categoria.btbConfirmarActualizacion.addActionListener(this);
        
        
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        
       if (e.getSource() == categoria.btbVolverCategoria){
                VolverMenuNormal();
                }
        
       else if (e.getSource() == categoria.btbEliminar){
           ElimanrCategoria();
       }
       
       else if (e.getSource() == categoria.btbActualizarVerdadero){
           SeleccionarParaActualizar();
       }
       
       else if (e.getSource() == categoria.btbRegistrar){
           RegistrarCategoria();
       }
       else if (e.getSource() == categoria.btbConfirmarActualizacion){
           ConfirmarActualizacion();
       } 
       
       else if (e.getSource() == categoria.btbCancelar){
           cancelarActualizacion();
       }
       
       else if (e.getSource() == categoria.btbFiltrarCategoria){
           FiltrarCategoria();
       }
        
       
    }
    
    
    
    
    
    private void VolverMenuNormal (){
        categoria.setVisible(false);
        menu.setVisible(true);
    }
    
    private  void ElimanrCategoria (){
           CategoriaDao categoriaDao = new CategoriaDao();
        try {
            categoriaDao.ElimanarConseguridad();
        } catch (ClassNotFoundException ex) {
            System.getLogger(ControladorCategoria.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (SQLException ex) {
            System.getLogger(ControladorCategoria.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
    
    
    
    private void SeleccionarParaActualizar (){
      int filla =   frm_Categoria.tablaCategoria.getSelectedRow();
 
 
    if (filla ==-1) {
        
        JOptionPane.showMessageDialog(null, "Debe seleccionar una categoria si la deseas actualizar");
        
   
    }  else {
        frm_Categoria.txtId.setVisible(true);
        frm_Categoria.lblId.setVisible(true);
        frm_Categoria.btbRegistrar.setVisible(false);
        frm_Categoria.btbConfirmarActualizacion.setVisible(true);
        frm_Categoria.btbCancelar.setVisible(true);
        frm_Categoria.btbActualizarVerdadero.setVisible(false);
        frm_Categoria.btbEliminar.setVisible(false);
       frm_Categoria.txtNombreCategoria.setText(frm_Categoria.tablaCategoria.getValueAt(filla, 1).toString());
       frm_Categoria.txtId.setText(frm_Categoria.tablaCategoria.getValueAt(filla, 0).toString());
       
        
        
    }
    }
    
    
    private  void RegistrarCategoria (){
         Validacion.Validar_Categoria vali = new Validar_Categoria();
        try {
            vali.registrarCategoriaValidada();
        } catch (ClassNotFoundException ex) {
            System.getLogger(ControladorCategoria.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (SQLException ex) {
            System.getLogger(ControladorCategoria.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        
        CategoriaDao CATE = new CategoriaDao();
        
        try {
            CATE.cargarComboCategorias();
        } catch (ClassNotFoundException ex) {
            System.getLogger(ControladorCategoria.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (SQLException ex) {
            System.getLogger(ControladorCategoria.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        
    }
    
    private void ConfirmarActualizacion (){
          Validacion.Validar_Categoria vali = new Validar_Categoria();
        try {
            vali.actualizarCategoriaValidada();
        } catch (ClassNotFoundException ex) {
            System.getLogger(ControladorCategoria.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (SQLException ex) {
            System.getLogger(ControladorCategoria.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
    
    
    private  void cancelarActualizacion (){
            
 frm_Categoria.lblId.setVisible(false);
 frm_Categoria.txtId.setVisible(false);
    frm_Categoria.btbConfirmarActualizacion.setVisible(false);
    frm_Categoria.btbCancelar.setVisible(false);
    frm_Categoria.btbActualizarVerdadero.setVisible(true);
    frm_Categoria.btbEliminar.setVisible(true);
    frm_Categoria.btbRegistrar.setVisible(true);
    frm_Categoria.txtNombreCategoria.setText("");
      
    }
    
    
    private void FiltrarCategoria (){
        CategoriaDao categoriaDao = new CategoriaDao();
        try {
            categoriaDao.filtrarProductosPorCategoria();
        } catch (ClassNotFoundException ex) {
            System.getLogger(ControladorCategoria.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (SQLException ex) {
            System.getLogger(ControladorCategoria.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }    
    
    
}
