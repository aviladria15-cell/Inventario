/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

/**
 *
 * @author avila
 */
import ModeloDAO.CuentaDao;
import ModeloDAO.InventarioLiquidoDao;
import ModeloDAO.MovientosDao;
import Vista_Almacen.Ajuste_Liquido;
import Vista_Usuari_Empleado.Menu_Sistema;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class ControladorAjuste_Liquido implements  ActionListener{

    private  final Menu_Sistema menu;
    
    private Ajuste_Liquido Liquido_Ajuste;
    
    public  ControladorAjuste_Liquido (Menu_Sistema menu){
        this.menu=menu;
    }
    
    
    public  void MostrarVistaAjusteLiquido (){
        
        if (Liquido_Ajuste == null) {
            Liquido_Ajuste = new Ajuste_Liquido();
            
            // AgregarListeners
            AgregarListener();
        }
        
        Liquido_Ajuste.setVisible(true);
IniciarVista();
  CargarCuentasInventario();
  
  CargarProducto();
menu.setVisible(false);
        
        
    }
    
    private  void AgregarListener (){
        
        Liquido_Ajuste.btbVolveAlMenu.addActionListener(this);
        Liquido_Ajuste.btbRealizarMovimiento.addActionListener(this);
        
        
    }
    
    
    @Override
    public void actionPerformed(ActionEvent e) {
        
        if (e.getSource() == Liquido_Ajuste.btbVolveAlMenu) {
            VolverMenu();
            
        }
        
        else if (e.getSource() == Liquido_Ajuste.btbRealizarMovimiento){
            RealizarAjuste();
        }
        
    }
    
    
    
    
    private  void VolverMenu (){
        Liquido_Ajuste.setVisible(false);
        
        menu.setVisible(true);
    }
    
    
    
    private  void RealizarAjuste (){
        ModeloDAO.MovientosDao movientosDao = new MovientosDao();
        
       movientosDao.RealizarAjusteLiquido();
        
        CargarCuentasInventario();
        CargarProducto();
    }
    
    private  void IniciarVista (){
        Liquido_Ajuste.jTextAreaInformacionLiquidoAjuste.setText("");
        
        
        
    
    }
    
    
    
    
    private  void CargarCuentasInventario (){
        try {
            ModeloDAO.CuentaDao cuentaDao = new CuentaDao();
            
            cuentaDao.cargarComboCuentascInventario(Liquido_Ajuste.jComboBoxCuentaInventario);
        } catch (ClassNotFoundException ex) {
            System.getLogger(ControladorAjuste_Liquido.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (SQLException ex) {
            System.getLogger(ControladorAjuste_Liquido.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
    
    
    private  void CargarProducto (){
        ModeloDAO.InventarioLiquidoDao inventarioLiquidoDao = new InventarioLiquidoDao();
        try {
            inventarioLiquidoDao. CargarComboxBoxLiquidoAjuste();
        } catch (ClassNotFoundException ex) {
            System.getLogger(ControladorAjuste_Liquido.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (SQLException ex) {
            System.getLogger(ControladorAjuste_Liquido.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
    
}
