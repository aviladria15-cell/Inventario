/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;
import ModeloDAO.AlmacenDao;
import Validacion.Validar_Almacen;
import Vista_Almacen.Vista_Registrar_Almacen;
import Vista_Usuari_Empleado.Menu_Sistema;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
/**
 *
 * @author avila
 */
public class Controlador_Almacen implements  ActionListener{

    private  final Menu_Sistema menu;
    
    private Vista_Registrar_Almacen Registrar_Almacen;
    
    
    public Controlador_Almacen (Menu_Sistema menu){
        this.menu = menu;
    }
    
    
    public  void MostraVista (){
        
        if (Registrar_Almacen == null) {
            
            Registrar_Almacen = new Vista_Registrar_Almacen();
            
            // AgregarListeners
            AgregarListeners();
        }
        
        Registrar_Almacen.setVisible(true);
        
        
        IniciarVista();
        CargarVista();
         Validacion.Validar_Almacen validar_Almacen = new Validar_Almacen();
         validar_Almacen.inicializarValidacionesAlmacen();
        menu.setVisible(false);
        
    }
    
    
    
    
    private  void AgregarListeners (){
      
        this.Registrar_Almacen.btbVolverMenu.addActionListener(this);
        this.Registrar_Almacen.btbEliminar.addActionListener(this);
        this.Registrar_Almacen.btbConfirmarActualizacion.addActionListener(this);
        this.Registrar_Almacen.btbCancelarActualizacion.addActionListener(this);
        this.Registrar_Almacen.btbActualizar.addActionListener(this);
        this.Registrar_Almacen.btbRegistrar.addActionListener(this);
        
        
        
    }
    
    
    
    
    
    
    
    
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == Registrar_Almacen.btbVolverMenu) {
            VolverMenu();
            
        }
       
        else if (e.getSource() == Registrar_Almacen.btbEliminar){
            EliminarUbicacion();
        }
        
        else if (e.getSource() == Registrar_Almacen.btbActualizar){
      SeleccionarParaActualizar();
    }
        
        else if (e.getSource() == Registrar_Almacen.btbCancelarActualizacion){
            Cancelar();
        }
         
        else if (e.getSource() == Registrar_Almacen.btbRegistrar){
            RegistrarUbicacion();
        }
        
        else if (e.getSource() == Registrar_Almacen.btbConfirmarActualizacion){
            ConfirmarActualizacion();
        }
        
    
    } 
    
    
    private  void ConfirmarActualizacion () {
        Validacion.Validar_Almacen almacen = new Validar_Almacen();
        
        try {
            almacen. actualizarAlmacenValidado();
        } catch (ClassNotFoundException ex) {
            System.getLogger(Controlador_Almacen.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (SQLException ex) {
            System.getLogger(Controlador_Almacen.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        
     
    }   
    
    
    private  void RegistrarUbicacion (){
        
        
      Validacion.Validar_Almacen validar_Almacen = new Validar_Almacen();
      
        try {
            validar_Almacen.registrarAlmacenValidado();
        } catch (ClassNotFoundException ex) {
            System.getLogger(Controlador_Almacen.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (SQLException ex) {
            System.getLogger(Controlador_Almacen.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        
    }
    
    
   
    private  void SeleccionarParaActualizar (){
        
        int filla = Registrar_Almacen.TablaAlmacen.getSelectedRow();
        
        if (filla == -1) {
    JOptionPane.showMessageDialog(null, 
        "Debe seleccionar una ubicación para actualizarla.", 
        "Error", 
        JOptionPane.ERROR_MESSAGE);
    return;
} else {
    String mensaje = "<html><b>¿Estás seguro de actualizar los siguientes campos?</b><br><br>" +
                 "• Pasillo <br>" +
                 "• Ala <br>" +
                 "• Nivel <br>" +
                 "• Capacidad <br>" +
                 "• Estante </html>";

int respuesta = JOptionPane.showConfirmDialog(null, 
    mensaje, 
    "Confirmar modificación", 
    JOptionPane.YES_NO_OPTION, 
    JOptionPane.QUESTION_MESSAGE);

if (respuesta == JOptionPane.YES_OPTION) {
   
  Registrar_Almacen.btbEliminar.setVisible(false);
  Registrar_Almacen.btbActualizar.setVisible(false);
  Registrar_Almacen.btbRegistrar.setVisible(false);
  
  Registrar_Almacen.btbCancelarActualizacion.setVisible(true);
  Registrar_Almacen.btbConfirmarActualizacion.setVisible(true);
  
  Registrar_Almacen.txtPasillo.setText(Registrar_Almacen.TablaAlmacen.getValueAt(filla, 1).toString());
  
    seleccionarItemCombo(Registrar_Almacen.jComboBoxAla, Registrar_Almacen.TablaAlmacen.getValueAt(filla, 2).toString());
    
    Registrar_Almacen.txtEstante.setText(Vista_Registrar_Almacen.TablaAlmacen.getValueAt(filla, 3).toString());
   
    Registrar_Almacen.txtNivel.setText(Vista_Registrar_Almacen.TablaAlmacen.getValueAt(filla, 4).toString());
    
    Registrar_Almacen.txtCapacidad.setText(Vista_Registrar_Almacen.TablaAlmacen.getValueAt(filla, 5).toString());
    
    
} else {
    return; // el usuario canceló
}
}
        
        
        
    }
        
        
        
    
    private  void Cancelar (){
        
        
          
  Registrar_Almacen.btbEliminar.setVisible(true);
  Registrar_Almacen.btbActualizar.setVisible(true);
  Registrar_Almacen.btbRegistrar.setVisible(true);
  
  Registrar_Almacen.btbCancelarActualizacion.setVisible(false);
  Registrar_Almacen.btbConfirmarActualizacion.setVisible(false);
  
  Registrar_Almacen.txtPasillo.setText("");
  Registrar_Almacen.txtEstante.setText("");
  Registrar_Almacen.txtCapacidad.setText("");
  Registrar_Almacen.txtNivel.setText("");
  
  
        
        
        
        
    }
    
    
    
    
    
    
    private  void IniciarVista (){
        
        Registrar_Almacen.btbConfirmarActualizacion.setVisible(false);
        Registrar_Almacen.btbCancelarActualizacion.setVisible(false);
        
                  
  Registrar_Almacen.btbEliminar.setVisible(true);
  Registrar_Almacen.btbActualizar.setVisible(true);
  Registrar_Almacen.btbRegistrar.setVisible(true);
  
 Registrar_Almacen.txtBuscarProductoEnTabla.setText("");
  
  Registrar_Almacen.txtPasillo.setText("");
  Registrar_Almacen.txtEstante.setText("");
  Registrar_Almacen.txtCapacidad.setText("");
  Registrar_Almacen.txtNivel.setText("");
  
  
        
        
        
    }
    
    
    private  void CargarVista (){
        
      ModeloDAO.AlmacenDao almacenDao = new AlmacenDao();
      
        try {
            almacenDao.MostrarAlmacen();
        } catch (ClassNotFoundException ex) {
            System.getLogger(Controlador_Almacen.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (SQLException ex) {
            System.getLogger(Controlador_Almacen.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        
        almacenDao.agregarFiltroBusqueda();
    }
    
    private  void EliminarUbicacion (){
          ModeloDAO.AlmacenDao almacenDao = new AlmacenDao();
          
          almacenDao.EliminarConSeguridad();
    }
    
     
    private  void VolverMenu (){
       menu.setVisible(true);
        Registrar_Almacen.setVisible(false);
        
    }
    
    
private void seleccionarItemCombo(JComboBox combo, Object valor) {
    if (valor == null) {
        if (combo != null) combo.setSelectedIndex(0);
        return;
    }

    // Si es el combo de Ubicación, usamos el método especializado
    if (combo == Registrar_Almacen.jComboBoxAla) {
        seleccionarUbicacionEnCombo(valor.toString().trim());
        return;
    }

    // Lógica normal para otros combos (Producto, etc.)
    String valorStr = valor.toString().trim().toLowerCase();

    for (int i = 0; i < combo.getItemCount(); i++) {
        Object item = combo.getItemAt(i);
        if (item == null) continue;

        String itemStr = item.toString().trim().toLowerCase();

        if (itemStr.equals(valorStr) || itemStr.contains(valorStr) || valorStr.contains(itemStr)) {
            combo.setSelectedIndex(i);
            return;
        }
    }
    combo.setSelectedIndex(0);
}
 
 
private void seleccionarUbicacionEnCombo(String textoTabla) {
    
    JComboBox combo = Registrar_Almacen.jComboBoxAla;
    
    if (textoTabla == null || textoTabla.trim().isEmpty()) {
        combo.setSelectedIndex(0);
        return;
    }

    String buscado = textoTabla.toLowerCase().trim();
    int mejorIndice = -1;
    int mejorPuntaje = -1;

    for (int i = 0; i < combo.getItemCount(); i++) {
        Object item = combo.getItemAt(i);
        if (item == null) continue;

        String itemStr = item.toString().toLowerCase().trim();

        // Contamos cuántas partes coinciden (Ala, Pasillo, Estante, Nivel, Cap)
        int puntaje = 0;
        String[] partes = buscado.split("\\|");
        
        for (String parte : partes) {
            parte = parte.trim().toLowerCase();
            if (parte.length() > 2 && itemStr.contains(parte)) {
                puntaje++;
            }
        }

        // Bonus si coincide casi todo el texto
        if (itemStr.contains(buscado) || buscado.contains(itemStr)) {
            puntaje += 5;
        }

        if (puntaje > mejorPuntaje) {
            mejorPuntaje = puntaje;
            mejorIndice = i;
        }
    }

    if (mejorIndice != -1) {
        combo.setSelectedIndex(mejorIndice);
       
    } else {
     
        combo.setSelectedIndex(0);
    }
}
}
