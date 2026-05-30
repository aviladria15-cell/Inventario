/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import ModeloDAO.Libro_MayorDao;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import vista_Libro_Contable.frm_LibroMayor;
import Vista_Usuari_Empleado.Menu_Sistema;
import java.sql.SQLException;
import static vista_Libro_Contable.frm_LibroMayor.TablaLibroMayor;
import static vista_Libro_Contable.frm_LibroMayor.jDateDesde;
import static vista_Libro_Contable.frm_LibroMayor.jDateHasta;
/**
 *
 * @author avila
 */
public class Controlador_Libro_Mayor implements ActionListener{

    private  final  Menu_Sistema menu;
    private  frm_LibroMayor LibroMayor;
    
    public  Controlador_Libro_Mayor (Menu_Sistema menu ){
        this.menu = menu;
    }
    
    
    public  void MostrarVista (){
        if (LibroMayor == null) {
          
                LibroMayor = new frm_LibroMayor();
                AgregarListeners();
                // lISTENERS Aqui
           
        }
        
        LibroMayor.setVisible(true);
        MostrarLibroMayor();
        menu.setVisible(false);
        
    }
    
    
    private  void AgregarListeners (){
        
       LibroMayor.btbVolveMenu.addActionListener(this);
       LibroMayor.btbFiltarCuenta.addActionListener(this);
        
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == LibroMayor.btbVolveMenu){
            VolverMenu();
        }
        
        else if (e.getSource() == LibroMayor.btbFiltarCuenta){
            FiltrarFecha();
            
        }
        
    }
    
    
    private void VolverMenu (){
        menu.setVisible(true);
        LibroMayor.setVisible(false);
    }
    
    
    private   void MostrarLibroMayor (){
        ModeloDAO.Libro_MayorDao libro_MayorDao = new Libro_MayorDao();
        try {
            libro_MayorDao.MostrarLibro_Mayor();
        } catch (ClassNotFoundException ex) {
            System.getLogger(Controlador_Libro_Mayor.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (SQLException ex) {
            System.getLogger(Controlador_Libro_Mayor.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
    private  void FiltrarFecha (){
        ModeloDAO.Libro_MayorDao lM = new Libro_MayorDao();
       
    lM.FiltrarLibroMayorPorFecha(TablaLibroMayor, jDateDesde, jDateHasta);
}
    
}
