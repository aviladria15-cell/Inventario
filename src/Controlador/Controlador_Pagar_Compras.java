/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import  Vista_Usuari_Empleado.Menu_Sistema;
import Vista_Gestionar_Proveedor.Vista_Pagar_Compra;
import ModeloDAO.CompraDAO;
import ModeloDAO.CuentaDao;
import java.sql.SQLException;
import javax.swing.JOptionPane;
/**
 *
 * @author avila
 */
public class Controlador_Pagar_Compras  implements ActionListener{

    private  final Menu_Sistema menu;
    private Vista_Pagar_Compra Compra_Pagar;
    
    public  Controlador_Pagar_Compras (Menu_Sistema menu ){
        this.menu = menu;
    }
    
    public  void MostrarVista (){
        if (Compra_Pagar == null) {
            Compra_Pagar = new Vista_Pagar_Compra();
            
            // AGREGARlISTNERS
            AgregarListenrs();
            
        }
        Compra_Pagar.setVisible(true);
        MostraTabla();
        CargarCuentas();
        Compra_Pagar.btbPagar.setVisible(false);
        Compra_Pagar.btbAsumirPago.setVisible(true);
        menu.setVisible(false);
    }
    
    private  void AgregarListenrs (){
        Compra_Pagar.btbVolveMenu.addActionListener(this);
        Compra_Pagar.btbAsumirPago.addActionListener(this);
        Compra_Pagar.btbPagar.addActionListener(this);
        Compra_Pagar.btbAbonar.addActionListener(this);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        
        if (e.getSource() == Compra_Pagar.btbVolveMenu){
           VolverMenu();
        }
        
        else if (e.getSource() == Compra_Pagar.btbAsumirPago){
            AsumirPago();
        }
        
        else  if (e.getSource() == Compra_Pagar.btbPagar){
            Pagar();
        }
        else if (e.getSource() == Compra_Pagar.btbAbonar){
            HACERaBONO();
        }
        
    }
    
    
    private  void VolverMenu (){
      menu.setVisible(true);
      Compra_Pagar.setVisible(false);
        
        
    }
    
    private  void HACERaBONO (){
        CompraDAO compraDAO = new CompraDAO();
        compraDAO.AhacerAbono();
    }    
    private  void Pagar(){
         CompraDAO compraDAO = new CompraDAO();
        
            compraDAO.registrarPago();
            MostraTabla();
    }
    
    private  void AsumirPago (){
     int fila = Compra_Pagar.TablaPagarDeudas.getSelectedRow();
    
    if (fila == -1) {
        JOptionPane.showMessageDialog(null, "Debe seleccionar una fila", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
 
    
    Compra_Pagar.txtNumeroFactura.setText(Compra_Pagar.TablaPagarDeudas.getValueAt(fila, 1).toString());
    Compra_Pagar.btbAsumirPago.setVisible(false);
    Compra_Pagar.btbPagar.setVisible(true);
       
    }
    
    
    private void  MostraTabla (){
       CompraDAO compraDAO = new CompraDAO();
        try {
            compraDAO.MostrarDeudas();
        } catch (ClassNotFoundException ex) {
            System.getLogger(Controlador_Pagar_Compras.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (SQLException ex) {
            System.getLogger(Controlador_Pagar_Compras.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
    
    private  void CargarCuentas (){
        ModeloDAO.CuentaDao cuentaDao = new CuentaDao();
        
        try {
            cuentaDao.cargarComboCuentasInventarioo(Compra_Pagar.jComboBoxCuenta);
        } catch (ClassNotFoundException ex) {
            System.getLogger(Controlador_Pagar_Compras.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (SQLException ex) {
            System.getLogger(Controlador_Pagar_Compras.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        try {
            cuentaDao.cargarComboCuentasInventarioo(Compra_Pagar.jComboBoxCuentaDestino);
        } catch (ClassNotFoundException ex) {
            System.getLogger(Controlador_Pagar_Compras.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (SQLException ex) {
            System.getLogger(Controlador_Pagar_Compras.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
}
