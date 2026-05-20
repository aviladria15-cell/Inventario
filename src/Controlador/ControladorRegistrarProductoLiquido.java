/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

/**
 *
 * @author avila
 */
import ModeloDAO.CategoriaDao;
import ModeloDAO.MarcaDao;
import ModeloDAO.ProductoDao;
import Validacion.Validar_Producto;
import Vista_Gestion_Productos.Vista_Registro_Producto_Liquido;
import Vista_Usuari_Empleado.Menu_Sistema;
import dictionary.AutoCorrectorGlobal;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;


public class ControladorRegistrarProductoLiquido implements ActionListener{

    private final Menu_Sistema menu;
private Vista_Registro_Producto_Liquido ProductoLiquido;
    

public  ControladorRegistrarProductoLiquido (Menu_Sistema menu){
    this.menu = menu;   
}


public  void MostrarVista (){
    if (ProductoLiquido == null) {
        
        try {
            ProductoLiquido = new Vista_Registro_Producto_Liquido();
             // Agregar Listener
        AgregarListener();
        
        } catch (ClassNotFoundException ex) {
            System.getLogger(ControladorRegistrarProductoLiquido.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (SQLException ex) {
            System.getLogger(ControladorRegistrarProductoLiquido.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        
       
        
    }
    
    ProductoLiquido.setVisible(true);
    ActualizarVista();
    CancelarActualizacion();
      AutoCorrectorGlobal.aplicar(ProductoLiquido);
     Validacion.Validar_Producto validar_Producto = new Validar_Producto();
     validar_Producto.inicializarValidacionesProductoLiquido();
      ModeloDAO.MarcaDao marcaDao = new MarcaDao();
     ModeloDAO.CategoriaDao Cate = new CategoriaDao();
ModeloDAO.ProductoDao productoDao = new ProductoDao();

        try {
            productoDao.MostrarProductosLiquidos();
        } catch (ClassNotFoundException ex) {
            System.getLogger(ControladorRegistrarProductoLiquido.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (SQLException ex) {
            System.getLogger(ControladorRegistrarProductoLiquido.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
      
       
        try {
            Cate.CargarComboxBoxCategorialIQUIDO();
        } catch (ClassNotFoundException ex) {
            System.getLogger(ControladorRegistrarProductoLiquido.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (SQLException ex) {
            System.getLogger(ControladorRegistrarProductoLiquido.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        try {
            marcaDao.CargarComboxBoxMarcaLiquido();
        } catch (ClassNotFoundException ex) {
            System.getLogger(ControladorRegistrarProductoLiquido.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (SQLException ex) {
            System.getLogger(ControladorRegistrarProductoLiquido.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        Cate. agregarBuscadorCategoriaLiquido();
        marcaDao.agregarBuscadorMarcaLiquido();
      productoDao.agregarFiltroBusquedaLiquido();
        
        
    menu.setVisible(false);
    
}
    
private  void AgregarListener (){
    ProductoLiquido.btbVolverMenu.addActionListener(this);
    ProductoLiquido.btbEliminar.addActionListener(this);
    ProductoLiquido.btbConfirmarActualizacion.addActionListener(this);
    ProductoLiquido.btbCancelarActualizacion.addActionListener(this);
    ProductoLiquido.btbActualizar.addActionListener(this);
    ProductoLiquido.btbRegistrar.addActionListener(this);
}
    
    @Override
    public void actionPerformed(ActionEvent e) {
        
        if (e.getSource() == ProductoLiquido.btbVolverMenu){
            VolverAlMenu();
        }
        
        else if (e.getSource()== ProductoLiquido.btbEliminar){
            EliminarProductoLiquido();
        }
        
        else if (e.getSource() == ProductoLiquido.btbRegistrar){
            RegistrarProductoLiQUIDO();
        }
        else if (e.getSource() == ProductoLiquido.btbActualizar){
            SeleccionarProductoParaActualizarlo();
        }
        else if (e.getSource() == ProductoLiquido.btbCancelarActualizacion){
            CancelarActualizacion();
        }
        else if (e.getSource() == ProductoLiquido.btbConfirmarActualizacion){
            ConfirmarActulizacion();
        }
       
    }


private  void VolverAlMenu (){
    menu.setVisible(true);
    ProductoLiquido.setVisible(false);
}
    
private void EliminarProductoLiquido (){
    ModeloDAO.ProductoDao Dao = new ProductoDao();
        try {
            Dao.EliminarProductoLiquido();
        } catch (ClassNotFoundException ex) {
            System.getLogger(ControladorRegistrarProductoLiquido.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (SQLException ex) {
            System.getLogger(ControladorRegistrarProductoLiquido.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
}
    
    private void RegistrarProductoLiQUIDO (){

        
        Validacion.Validar_Producto validar_Producto = new Validar_Producto();
        try {
            validar_Producto.registrarProductoLiquidoValidado();
        } catch (ClassNotFoundException ex) {
            System.getLogger(ControladorRegistrarProductoLiquido.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
    
    private  void ActualizarVista (){
        if (ProductoLiquido == null) return; {
        
        ModeloDAO.MarcaDao marcaDao = new MarcaDao();
     ModeloDAO.CategoriaDao Cate = new CategoriaDao();
            try {
                Cate.CargarComboxBoxCategorialIQUIDO();
            } catch (ClassNotFoundException ex) {
                System.getLogger(ControladorRegistrarProductoLiquido.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            } catch (SQLException ex) {
                System.getLogger(ControladorRegistrarProductoLiquido.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
            try {
                marcaDao.CargarComboxBoxMarcaLiquido();
            } catch (ClassNotFoundException ex) {
                System.getLogger(ControladorRegistrarProductoLiquido.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            } catch (SQLException ex) {
                System.getLogger(ControladorRegistrarProductoLiquido.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
            
        }
    }
    
    
    private  void SeleccionarProductoParaActualizarlo (){
        
      int filla = Vista_Registro_Producto_Liquido.TablaRegistroLiquido.getSelectedRow();
      
        if (filla == -1) {
            
            JOptionPane.showMessageDialog(null, "Debe seleccionar un producto para actulizarlo");
            
        } else {
            
            Vista_Registro_Producto_Liquido.btbEliminar.setVisible(false);
            Vista_Registro_Producto_Liquido.btbActualizar.setVisible(false);
            Vista_Registro_Producto_Liquido.btbRegistrar.setVisible(false);
            Vista_Registro_Producto_Liquido.btbConfirmarActualizacion.setVisible(true);
            Vista_Registro_Producto_Liquido.btbCancelarActualizacion.setVisible(true);
            
            Vista_Registro_Producto_Liquido.txtNombreProductoLiquido.setText(Vista_Registro_Producto_Liquido.TablaRegistroLiquido.getValueAt(filla, 1).toString());
           Vista_Registro_Producto_Liquido.txtTipodeLiquido.setText(Vista_Registro_Producto_Liquido.TablaRegistroLiquido.getValueAt(filla, 2).toString());
            Vista_Registro_Producto_Liquido.txtViscosidad.setText(Vista_Registro_Producto_Liquido.TablaRegistroLiquido.getValueAt(filla, 3).toString());
            seleccionarItemCombo(Vista_Registro_Producto_Liquido.jComboDensidad, Vista_Registro_Producto_Liquido.TablaRegistroLiquido.getValueAt(filla, 4).toString());
            Vista_Registro_Producto_Liquido.txtPresentacion.setText(Vista_Registro_Producto_Liquido.TablaRegistroLiquido.getValueAt(filla, 5).toString());
            seleccionarItemCombo(Vista_Registro_Producto_Liquido.jComboCategoria, Vista_Registro_Producto_Liquido.TablaRegistroLiquido.getValueAt(filla,6).toString());
            seleccionarItemCombo(Vista_Registro_Producto_Liquido.jComboMarca, Vista_Registro_Producto_Liquido.TablaRegistroLiquido.getValueAt(filla, 7).toString());
        }
      
    }
    
    
    
    private  void CancelarActualizacion (){
        Vista_Registro_Producto_Liquido.txtNombreProductoLiquido.setText("");
        Vista_Registro_Producto_Liquido.txtTipodeLiquido.setText("");
        Vista_Registro_Producto_Liquido.txtViscosidad.setText("");
        Vista_Registro_Producto_Liquido.txtPresentacion.setText("");
        
        
         Vista_Registro_Producto_Liquido.btbEliminar.setVisible(true);
            Vista_Registro_Producto_Liquido.btbActualizar.setVisible(true);
            Vista_Registro_Producto_Liquido.btbRegistrar.setVisible(true);
            Vista_Registro_Producto_Liquido.btbConfirmarActualizacion.setVisible(false);
            Vista_Registro_Producto_Liquido.btbCancelarActualizacion.setVisible(false);
        
    }
    
    private  void ConfirmarActulizacion (){
        Validacion.Validar_Producto validar_Producto = new Validar_Producto();
        
        try {
            validar_Producto.actualizarProductoLiquidoConValidacion();
        } catch (ClassNotFoundException ex) {
            System.getLogger(ControladorRegistrarProductoLiquido.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
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
