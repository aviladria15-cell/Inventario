/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import ModeloDAO.MarcaDao;
import Validacion.Validar_Marca;
import Vista_Gestion_Marca.Gestionar_Marca;
import Vista_Usuari_Empleado.Menu_Sistema;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * Controlador específico para el módulo de Gestión de Marcas
 * @author avila
 */
public class ControladorMarca implements ActionListener {

    private final Menu_Sistema menu;
    private Gestionar_Marca vista;

    // Constructor - Recibe el menú principal para poder volver
    public ControladorMarca(Menu_Sistema menu) {
        this.menu = menu;
    }
    
    

    /**
     * Muestra la vista de Marca (lazy initialization)
     */
    public void mostrarVista() {
        if (vista == null) {
            try {
                vista = new Gestionar_Marca();
                agregarListeners();
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(ControladorMarca.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        vista.setVisible(true);
        menu.setVisible(false);
    }

    /**
     * Agrega todos los listeners a los botones de la vista
     */
    private void agregarListeners() {
        vista.BotonVolverMarca.addActionListener(this);
        vista.btbEliminar.addActionListener(this);
        vista.btbActualizarAntes.addActionListener(this);
        vista.btbRegistrar.addActionListener(this);
        vista.btbConfirmarActualizacion.addActionListener(this);
        vista.btbfitralMarca.addActionListener(this);
        vista.btbCancelar.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        // ====================== VOLVER AL MENÚ ======================
        if (e.getSource() == vista.BotonVolverMarca) {
            volverAlMenu();
        }

        // ====================== REGISTRAR MARCA ======================
        else if (e.getSource() == vista.btbRegistrar) {
            registrarMarca();
        }

        // ====================== ACTUALIZAR MARCA ======================
        else if (e.getSource() == vista.btbActualizarAntes) {
            SeleccionarMarcaParaActualizar();
        }

        else if (e.getSource() == vista.btbConfirmarActualizacion) {
            actualizarMarca();
        }

        else if (e.getSource() == vista.btbCancelar) {
            cancelarActualizacion();
        }

        // ====================== ELIMINAR MARCA ======================
        else if (e.getSource() == vista.btbEliminar) {
            eliminarMarca();
        }

        // ====================== FILTRAR MARCA ======================
        else if (e.getSource() == vista.btbfitralMarca) {
            filtrarMarcas();
        }
    }

    // ====================== MÉTODOS PRIVADOS ======================

    private void volverAlMenu() {
        vista.setVisible(false);
        menu.setVisible(true);
    }

    private void registrarMarca() {
        Validar_Marca validador = new Validar_Marca();
        try {
            validador.registrarMarcaValidada();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ControladorMarca.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void actualizarMarca() {
        Validar_Marca validador = new Validar_Marca();
        try {
            validador.actualizarMarcaValidada();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ControladorMarca.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void cancelarActualizacion() {
         Gestionar_Marca.txtNombre.setText("");
   Gestionar_Marca.txtPais.setText(" ");
       Gestionar_Marca.lblId.setVisible(false);
    Gestionar_Marca.txtId.setVisible(false);
    Gestionar_Marca.btbConfirmarActualizacion.setVisible(false);
    Gestionar_Marca.btbRegistrar.setVisible(true);
    Gestionar_Marca.btbActualizarAntes.setVisible(true);
    Gestionar_Marca.btbEliminar.setVisible(true);
    Gestionar_Marca.btbCancelar.setVisible(false);
   
   
    }

    private void eliminarMarca() {
        MarcaDao dao = new MarcaDao();
        try {
            dao.EliminarMarcaConseguridad();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ControladorMarca.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void filtrarMarcas() {
        MarcaDao dao = new MarcaDao();
        try {
            dao.filtrarProductosPorMarca();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ControladorMarca.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    private   void SeleccionarMarcaParaActualizar (){
        int fila = Gestionar_Marca.TablaMarca.getSelectedRow();
  
    if (fila == -1) {
        JOptionPane.showMessageDialog(null, " Debes seleccionar una marca si deseas actualizarla");
       
        
    } else {
        Gestionar_Marca.btbRegistrar.setVisible(false);
        Gestionar_Marca.btbActualizarAntes.setVisible(false);
        Gestionar_Marca.btbEliminar.setVisible(false);
        Gestionar_Marca.btbConfirmarActualizacion.setVisible(true);
        Gestionar_Marca.btbCancelar.setVisible(true);
        Gestionar_Marca.lblId.setVisible(true);
        Gestionar_Marca.txtId.setVisible(true);
      Gestionar_Marca.txtNombre.setText(Gestionar_Marca.TablaMarca.getValueAt(fila, 1).toString());
      Gestionar_Marca.txtPais.setText(Gestionar_Marca.TablaMarca.getValueAt(fila, 2).toString());
      Gestionar_Marca.txtId.setText(Gestionar_Marca.TablaMarca.getValueAt(fila, 0).toString());
        
   
    }
    }
    
    
  


}