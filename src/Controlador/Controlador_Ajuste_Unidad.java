/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import ModeloDAO.CuentaDao;
import ModeloDAO.InventarioUnidadDAO;
import ModeloDAO.MovientosDao;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Vista_Almacen.Ajuste_Unidad;
import Vista_Usuari_Empleado.Menu_Sistema;
import java.sql.SQLException;

/**
 *
 * @author avila
 */
public class Controlador_Ajuste_Unidad  implements ActionListener{

    private  final Menu_Sistema menu;
    
    private Ajuste_Unidad Unida_Ajuste ;
    
    public  Controlador_Ajuste_Unidad (Menu_Sistema menu){
        this.menu=menu;
    }
    
    
    public  void MostrarVistaAjusteUnidad (){
        
        if (Unida_Ajuste == null) {
            
            Unida_Ajuste = new Ajuste_Unidad();
            
            //AGREGAR lISTERNERS
            AgregarListenrs();
        }
   
        
        Unida_Ajuste.setVisible(true);
        
        CargarProducto();
        CargarCuentas();
        
        menu.setVisible(false);
        
        
    }
    
    
    private  void AgregarListenrs (){
        Unida_Ajuste.btbVolverMenu.addActionListener(this);
        Unida_Ajuste.btbRealizarMovimiento.addActionListener(this);
    }
    
    
    
    @Override
    public void actionPerformed(ActionEvent e) {
        
        if (e.getSource() == Unida_Ajuste.btbVolverMenu) {
            VolverMenu();
            
        }
        
        else if (e.getSource() == Unida_Ajuste.btbRealizarMovimiento){
            RealizarMovimiento();
        }
  
        
    }
    
    
    
    private  void RealizarMovimiento (){
        
      ModeloDAO.MovientosDao movientosDao = new MovientosDao();
      
      movientosDao.RealizarAjusteUnidad();
        CargarCuentas();
        CargarProducto();
        
    }
    
    private  void VolverMenu (){
        
        menu.setVisible(true);
        
        Unida_Ajuste.setVisible(false);
    }
    
    public  void CargarCuentas (){
        
      ModeloDAO.CuentaDao cuentaDao = new CuentaDao();
      
        try {
            cuentaDao.cargarComboCuentascInventario(Unida_Ajuste.jComboBoxCuentas);
        } catch (ClassNotFoundException ex) {
            System.getLogger(Controlador_Ajuste_Unidad.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (SQLException ex) {
            System.getLogger(Controlador_Ajuste_Unidad.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        
        
    }
    
    
    
    public  void CargarProducto (){
        ModeloDAO.InventarioUnidadDAO inventarioUnidadDAO = new InventarioUnidadDAO();
        
        try {
            inventarioUnidadDAO.CargarComboxBoxUnidadaJUSTE() ;
        } catch (ClassNotFoundException ex) {
            System.getLogger(Controlador_Ajuste_Unidad.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (SQLException ex) {
            System.getLogger(Controlador_Ajuste_Unidad.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
    
    
    
}
