/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import ModeloDAO.CuentaDao;
import ModeloDAO.InventarioSolidoDAO;
import ModeloDAO.MovientosDao;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Vista_Usuari_Empleado.Menu_Sistema;
import Vista_Almacen.Ajuste_Solido;
import java.sql.SQLException;
/**
 *
 * @author avila
 */
public class Controlador_Ajuste_Solido  implements  ActionListener{
    
    private  final Menu_Sistema menu;
    
    private  Ajuste_Solido  Solido_Ajustel;
    
    
    public  Controlador_Ajuste_Solido (Menu_Sistema menu){
        this.menu = menu;
    }
    
    public  void MOstrarVista (){
        
        if (Solido_Ajustel == null) {
            
            Solido_Ajustel = new Ajuste_Solido();
            
            // Agregar Lsitenrs
            AgregarListeners();
        }
        
        Solido_Ajustel.setVisible(true); 
        
       CargarProducto();
         CargarCuentasInventario();
menu .setVisible(false);
        
    }
    
    
    private  void AgregarListeners (){
        
      Solido_Ajustel.btbVolverMenu.addActionListener(this);
      Solido_Ajustel.btbRealizarMovimiento.addActionListener(this);
    }
    
    

    @Override
    public void actionPerformed(ActionEvent e) {
         if (e.getSource() == Solido_Ajustel.btbVolverMenu) {
             VolverAlMenu();
            
        }
        
         else if (e.getSource() == Solido_Ajustel.btbRealizarMovimiento){
             RealizarAjuste();
         }
         
         
         
    }
    
    
    
    private  void VolverAlMenu (){
        menu.setVisible(true);
        
        Solido_Ajustel.setVisible(false);
    }
    
    
    private  void RealizarAjuste (){
        
       ModeloDAO.MovientosDao movientosDao = new MovientosDao();
       
       movientosDao.RealizarAjusteSolido();
       
       CargarProducto();
       CargarCuentasInventario();
    }
    
    
    
    
    private  void CargarCuentasInventario (){
        
        
      ModeloDAO.CuentaDao cuentaDao = new CuentaDao();
      
        try {
            cuentaDao.cargarComboCuentascInventario(Ajuste_Solido.jComboBoxCuentaPasivo);
        } catch (ClassNotFoundException ex) {
            System.getLogger(Controlador_Ajuste_Solido.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (SQLException ex) {
            System.getLogger(Controlador_Ajuste_Solido.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
 
    }
    
    
    
    
    
    
    private  void CargarProducto (){
        
        ModeloDAO.InventarioSolidoDAO inventarioSolidoDAO = new InventarioSolidoDAO();
        
        try {
            inventarioSolidoDAO.CargarComboxBoxSOLIDOAjuste();
        } catch (ClassNotFoundException ex) {
            System.getLogger(Controlador_Ajuste_Solido.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (SQLException ex) {
            System.getLogger(Controlador_Ajuste_Solido.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        
        
        
    }
    
    
    
    
}
