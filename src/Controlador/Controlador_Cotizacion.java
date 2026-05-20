/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Reportes.Generar_Documento_Cotizacion;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Vista_Gestionar_Proveedor.Vista_Cotizaciónn;
import Vista_Usuari_Empleado.Menu_Sistema;
import java.sql.SQLException;
import javax.swing.JOptionPane;
/**
 *
 * @author avila
 */
public class Controlador_Cotizacion  implements  ActionListener{
    
    
    private  final Menu_Sistema menu;
    private  Vista_Cotizaciónn cotizacion;
    
    
    
    public  Controlador_Cotizacion(Menu_Sistema menu){
        this.menu = menu;
    }
    
    
    public  void MostrarVistaCotizacion (){
        if (cotizacion == null) {
            
            try {
                cotizacion = new Vista_Cotizaciónn();
                AgregarListners();
                // Listners
            } catch (ClassNotFoundException ex) {
                System.getLogger(Controlador_Cotizacion.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            } catch (SQLException ex) {
                System.getLogger(Controlador_Cotizacion.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }

        }
        
        cotizacion.setVisible(true);
        menu.setVisible(false);
    }
    
    
    private  void AgregarListners (){
        cotizacion.btbGenerarCotizacion.addActionListener(this);
        cotizacion.btbVolverProvvedor.addActionListener(this);
        cotizacion.btbEnviarSolicitud.addActionListener(this);
        cotizacion.btbCancelarCotizacion.addActionListener(this);
        
    }
    
    

    @Override
    public void actionPerformed(ActionEvent e) {
       
        if (e.getSource() == cotizacion.btbVolverProvvedor) {
        VolverMenu();
            
        }
        
        else if (e.getSource() == cotizacion.btbGenerarCotizacion){
            GenerarCotizacion();
        }
        
        else if (e.getSource() == cotizacion.btbEnviarSolicitud){
            EnviarSolicitud();
        }
        
        else if (e.getSource() == cotizacion.btbCancelarCotizacion){
            CancelarCotizacion();
        }
        
        
    }
    
    
    
    private  void VolverMenu (){
        menu.setVisible(true);
        cotizacion.setVisible(false);
    }
    
    
    private  void GenerarCotizacion (){
        int filla = Vista_Cotizaciónn.tablaCotizacion.getSelectedRow();
    
    if (filla == -1){
        JOptionPane.showMessageDialog(null, "Debe Selecionar una fila para Generar la cotización");
    } else {
        
        
        Vista_Cotizaciónn.jScrollPanelCotiza.setVisible(false);
        Vista_Cotizaciónn.lblCorreo.setVisible(true);
        Vista_Cotizaciónn.txtCorreo.setVisible(true);
        Vista_Cotizaciónn.lblNombreProveedor.setVisible(true);
        Vista_Cotizaciónn.txtNombreProveedor.setVisible(true);
        Vista_Cotizaciónn.txtNombreProducto.setVisible(true);
        Vista_Cotizaciónn.lblNombreProducto.setVisible(true);
        Vista_Cotizaciónn.btbGenerarCotizacion.setVisible(false);
        Vista_Cotizaciónn.btbCancelarCotizacion.setVisible(true);
        Vista_Cotizaciónn.lblCategoriaProducto.setVisible(true);
        Vista_Cotizaciónn.txtCategoriaProudcto.setVisible(true);
         Vista_Cotizaciónn.lblMarca.setVisible(true);
      Vista_Cotizaciónn.txtMarca.setVisible(true);
      Vista_Cotizaciónn.lblCantidadActual.setVisible(true);
      Vista_Cotizaciónn.txtCantidadActual.setVisible(true);
      Vista_Cotizaciónn.lblCantidadRequeridad.setVisible(true);
        Vista_Cotizaciónn.txtCantidadRequeridad.setVisible(true);
        Vista_Cotizaciónn.jScrollDescri.setVisible(true);
       Vista_Cotizaciónn.lblDescripcion.setVisible(true);
        Vista_Cotizaciónn.txtDescripcion.setVisible(true);
        Vista_Cotizaciónn.btbEnviarSolicitud.setVisible(true);
      
        Vista_Cotizaciónn.txtCorreo.setText(Vista_Cotizaciónn.tablaCotizacion.getValueAt(filla, 6).toString());
        Vista_Cotizaciónn.txtNombreProveedor.setText(Vista_Cotizaciónn.tablaCotizacion.getValueAt(filla, 5).toString());
        Vista_Cotizaciónn.txtNombreProducto.setText(Vista_Cotizaciónn.tablaCotizacion.getValueAt(filla, 0).toString());
        Vista_Cotizaciónn.txtCategoriaProudcto.setText(Vista_Cotizaciónn.tablaCotizacion.getValueAt(filla, 2).toString());
        Vista_Cotizaciónn.txtMarca.setText(Vista_Cotizaciónn.tablaCotizacion.getValueAt(filla, 1).toString());
        Vista_Cotizaciónn.txtCantidadActual.setText(Vista_Cotizaciónn.tablaCotizacion.getValueAt(filla, 3).toString());
    }
    }
    
    private  void EnviarSolicitud (){
         Reportes.Generar_Documento_Cotizacion repote = new Generar_Documento_Cotizacion();
          
          repote.GenerarPFDCotizacion(cotizacion);
    }
    
    private  void CancelarCotizacion (){
      Vista_Cotizaciónn.btbEnviarSolicitud.setVisible(false);
        Vista_Cotizaciónn.jScrollPanelCotiza.setVisible(true);
        Vista_Cotizaciónn.lblCorreo.setVisible(false);
        Vista_Cotizaciónn.txtCorreo.setVisible(false);
        Vista_Cotizaciónn.lblNombreProveedor.setVisible(false);
        Vista_Cotizaciónn.txtNombreProveedor.setVisible(false);
        Vista_Cotizaciónn.txtNombreProducto.setVisible(false);
        Vista_Cotizaciónn.lblNombreProducto.setVisible(false);
         Vista_Cotizaciónn.btbGenerarCotizacion.setVisible(true);
        Vista_Cotizaciónn.btbCancelarCotizacion.setVisible(false);
       Vista_Cotizaciónn.lblCategoriaProducto.setVisible(false);
        Vista_Cotizaciónn.txtCategoriaProudcto.setVisible(false);
      Vista_Cotizaciónn.lblMarca.setVisible(false);
      Vista_Cotizaciónn.txtMarca.setVisible(false);
     Vista_Cotizaciónn.lblCantidadActual.setVisible(false);
      Vista_Cotizaciónn.txtCantidadActual.setVisible(false);
      Vista_Cotizaciónn.lblCantidadRequeridad.setVisible(false);
        Vista_Cotizaciónn.txtCantidadRequeridad.setVisible(false);
      Vista_Cotizaciónn.jScrollDescri.setVisible(false);
       Vista_Cotizaciónn.lblDescripcion.setVisible(false);
        Vista_Cotizaciónn.txtDescripcion.setVisible(false);
        
    
}
}