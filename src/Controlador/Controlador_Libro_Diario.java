package Controlador;

import Modelo.Asiento_contable;
import ModeloDAO.AsientosDao;
import ModeloDAO.LibroDiarioDao;
import Vista_Usuari_Empleado.Menu_Sistema;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import vista_Libro_Contable.frm_AsientoContable;
import vista_Libro_Contable.frm_libroDiario;

/**
 * Controlador para el módulo de Libro Diario Contable
 */
public class Controlador_Libro_Diario implements ActionListener {

    private final Menu_Sistema menu;
    private frm_libroDiario vistaLibroDiario;
    
    // Cambiamos a lazy initialization también para el Asiento Contable
    private frm_AsientoContable vistaAsientoContable;

    public Controlador_Libro_Diario(Menu_Sistema menu) {
        this.menu = menu;
        // No recibimos la vista aquí para evitar NullPointer
    }

    /**
     * Muestra la ventana del Libro Diario
     */
    public void mostrarVista() {
        if (vistaLibroDiario == null) {
          
                vistaLibroDiario = new frm_libroDiario();
                agregarListeners();
                CargarCuentas();
           
        }

        vistaLibroDiario.setVisible(true);
        
       CargarCuentas();
        menu.setVisible(false);
    }

    private void agregarListeners() {
        vistaLibroDiario.btbVolveMenu.addActionListener(this);
        vistaLibroDiario.btbFiltrar.addActionListener(this);
        vistaLibroDiario.btbVerAsientoContable.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == vistaLibroDiario.btbVolveMenu) {
            volverAlMenu();
        }
        else if (e.getSource() == vistaLibroDiario.btbFiltrar) {
            filtrarPorFecha();
        }
        else if (e.getSource() == vistaLibroDiario.btbVerAsientoContable) {
            mostrarAsientoContable();
        }
        
        else if (e.getSource() == vistaAsientoContable.btbAtras){
            
           MenoAsiento();
            
        }
        
        
        else if (e.getSource() == vistaAsientoContable.btbFiltrarAsientos) {
            
            FiltarAsientos();
            
        }
    }

    // ====================== MÉTODOS PRIVADOS ======================

    private  void CargarCuentas (){
        ModeloDAO.LibroDiarioDao libroDiarioDao = new LibroDiarioDao();
        
        try {
            libroDiarioDao.MostrarListaLibroContable();
        } catch (ClassNotFoundException ex) {
            System.getLogger(Controlador_Libro_Diario.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (SQLException ex) {
            System.getLogger(Controlador_Libro_Diario.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
    
    
    private void volverAlMenu() {
        vistaLibroDiario.setVisible(false);
        menu.setVisible(true);
    }

    private void filtrarPorFecha() {
        LibroDiarioDao dao = new LibroDiarioDao();
        try {
            dao.FiltrarLibroDiarioPorFecha(
                frm_libroDiario.TablaLibroDiario, 
                frm_libroDiario.jDateDesde, 
                frm_libroDiario.jDateHasta
            );
        } catch (SQLException ex) {
            Logger.getLogger(Controlador_Libro_Diario.class.getName())
                  .log(Level.SEVERE, "Error al filtrar Libro Diario por fecha", ex);
        }
    }

    /**
     * Muestra la ventana de Asiento Contable (creándola si no existe)
     */
    private void mostrarAsientoContable() {
        if (vistaAsientoContable == null) {
            
                vistaAsientoContable = new frm_AsientoContable();
                // Aquí puedes agregar listeners si el AsientoContable también tiene botones controlados
           AgregarListenrs();
        }

        vistaAsientoContable.setVisible(true);
        CargarAsuientosContables();
        
        vistaLibroDiario.setVisible(false);
    }
    
    
    
    
    private  void FiltarAsientos (){
        ModeloDAO.AsientosDao asientosDao = new AsientosDao();
        
        try {
            asientosDao.FiltrarAsientoContablePorFecha(vistaAsientoContable.TablaAsiento, vistaAsientoContable.jDateDesde, vistaAsientoContable.jDateHasta);
        } catch (SQLException ex) {
            System.getLogger(Controlador_Libro_Diario.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
    
    private  void MenoAsiento (){
        vistaLibroDiario.setVisible(true);
        vistaAsientoContable.setVisible(false);
    }
    
    
    private  void AgregarListenrs (){
        
        vistaAsientoContable.btbAtras.addActionListener(this);
        vistaAsientoContable.btbFiltrarAsientos.addActionListener(this);
 
    }
    
    
    
    
    private  void CargarAsuientosContables (){
        ModeloDAO.AsientosDao asientosDao = new AsientosDao();
        
        try {
            asientosDao.MostrarAsientosContables();
        } catch (ClassNotFoundException ex) {
            System.getLogger(Controlador_Libro_Diario.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (SQLException ex) {
            System.getLogger(Controlador_Libro_Diario.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
    
    
    
}