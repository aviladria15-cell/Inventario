/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.Usuario;
import ModeloDAO.UsurioDAO;
import Vista_Usuari_Empleado.Menu_Sistema;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Vista_Usuari_Empleado.Consultar_Usuario;
import java.sql.SQLException;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

/**
 *
 * @author avila
 */
public class ControladorConsultarUsuarios implements ActionListener{

    private  final Menu_Sistema menu;
    private  Consultar_Usuario  consulUsuaio;
    
    
    public  ControladorConsultarUsuarios (Menu_Sistema menu){

       this.menu = menu; 
    }
    
    
    public  void MostrarVistaConsultarUsuario (){
        if (consulUsuaio == null) {
            
            try {
                consulUsuaio = new Consultar_Usuario();
                AgregarListener();
            } catch (SQLException ex) {
                System.getLogger(ControladorConsultarUsuarios.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            } catch (ClassNotFoundException ex) {
                System.getLogger(ControladorConsultarUsuarios.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
            
          
        }
          consulUsuaio.setVisible(true);
          
            ModeloDAO.UsurioDAO udao = new UsurioDAO();
        try {
            udao.MostrarListaUsuario();
        } catch (SQLException ex) {
            System.getLogger(ControladorConsultarUsuarios.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (ClassNotFoundException ex) {
            System.getLogger(ControladorConsultarUsuarios.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
            menu.setVisible(false);
        
    }
    
   private  void AgregarListener (){
    consulUsuaio.btbVolverCerraConsultarUsuario.addActionListener(this);
    consulUsuaio.btbConfirmarActulizacion.addActionListener(this);
    consulUsuaio.btbActualizar.addActionListener(this);
    consulUsuaio.btbCancelarActualizacion.addActionListener(this);
    
       
       
   }
    
    
    
    
    
    @Override
    public void actionPerformed(ActionEvent e) {
        
        if (e.getSource() == consulUsuaio.btbVolverCerraConsultarUsuario) {
            VolveMenu();
            
        }
        
        else if (e.getSource() == consulUsuaio.btbConfirmarActulizacion){
            ConfirmarActualizacion();
            
        }
        
        else if (e.getSource() == consulUsuaio.btbCancelarActualizacion){
            CancelarActualizacion();
        }
        
        else if (e.getSource() == consulUsuaio.btbActualizar){
            ActualizarSeleccionar();
        }
        
    }
    
    
    private  void VolveMenu (){
        consulUsuaio.setVisible(false);
        menu.setVisible(true);
    }
    
    
    private  void ConfirmarActualizacion (){
          UsurioDAO dAO = new UsurioDAO();
          Usuario u = new Usuario();
          
        try {
            dAO.ActulizarUsuario(u);
        } catch (ClassNotFoundException ex) {
            System.getLogger(ControladorConsultarUsuarios.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (SQLException ex) {
            System.getLogger(ControladorConsultarUsuarios.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
         ModeloDAO.UsurioDAO udao = new UsurioDAO();
        try {
            udao.MostrarListaUsuario();
        } catch (SQLException ex) {
            System.getLogger(ControladorConsultarUsuarios.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (ClassNotFoundException ex) {
            System.getLogger(ControladorConsultarUsuarios.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
    
    
    private  void CancelarActualizacion (){
        Consultar_Usuario.lblNivelDeAcceso.setVisible(false);
    Consultar_Usuario.jComboxNivelAcceso.setVisible(false);
     Consultar_Usuario.lblEstado.setVisible(false);
      Consultar_Usuario.jComboEstadoUsuario.setVisible(false);
      Consultar_Usuario.txtIdUsuarioEstado.setVisible(false);
      Consultar_Usuario.btbCancelarActualizacion.setVisible(false);
      Consultar_Usuario.btbConfirmarActulizacion.setVisible(false);
     Consultar_Usuario.btbActualizar.setVisible(true);
    }
    
    
    private  void ActualizarSeleccionar (){
         
 int fila = Consultar_Usuario.TablaUsuario.getSelectedRow();
 
    if (fila == -1) {
        
        JOptionPane.showMessageDialog(null, "Debe selecionar un Usuario para actulizar el estado");
    } else {
 
      Consultar_Usuario.lblEstado.setVisible(true);
      Consultar_Usuario.jComboEstadoUsuario.setVisible(true);
      Consultar_Usuario.txtIdUsuarioEstado.setVisible(true);
      Consultar_Usuario.btbCancelarActualizacion.setVisible(true);
      Consultar_Usuario.btbConfirmarActulizacion.setVisible(true);
      Consultar_Usuario.lblNivelDeAcceso.setVisible(true);
      Consultar_Usuario.jComboxNivelAcceso.setVisible(true);
     Consultar_Usuario.btbActualizar.setVisible(false);
      
        seleccionarItemCombo(Consultar_Usuario.jComboEstadoUsuario,Consultar_Usuario.TablaUsuario.getValueAt(fila, 3).toString()
        );
        seleccionarItemCombo(Consultar_Usuario.jComboxNivelAcceso,Consultar_Usuario.TablaUsuario.getValueAt(fila, 2).toString()
        );
      Consultar_Usuario.txtIdUsuarioEstado.setText(Consultar_Usuario.TablaUsuario.getValueAt(fila,0).toString());
    
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
    
}
