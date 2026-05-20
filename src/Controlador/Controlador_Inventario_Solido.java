/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Funciones.Alerte_De_Stock;
import ModeloDAO.AlmacenDao;
import ModeloDAO.CuentaDao;
import ModeloDAO.InventarioSolidoDAO;
import ModeloDAO.MovientosDao;
import ModeloDAO.ProductoDao;
import ModeloDAO.ProveedorDao;
import Validacion.validar_inventario_solido;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import  Vista_GestionInventario.InventarioSolido;
import Vista_Usuari_Empleado.Menu_Sistema;
import java.sql.SQLException;
import java.util.Locale;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
/**
 *
 * @author avila
 */
public class Controlador_Inventario_Solido implements  ActionListener{

    
    private  final  Menu_Sistema menu;
    
    private InventarioSolido Inventario_Solido;
    
    
    public  Controlador_Inventario_Solido (Menu_Sistema menu ){
        this.menu = menu;
    }
    
    
    
    public  void MostrarVistaInventarioSOLIDO (){
        
        if (Inventario_Solido == null) {
            
            Inventario_Solido = new InventarioSolido();
            
            // AgregarListeners
            AgregarListeners();
            
        }
        
        Inventario_Solido.setVisible(true);
        
        CargaVista();
        CargarCuenta();
        IniciarPrecioVenta();
        CalcularPrecioVentaSolido();
        
        Funciones.Alerte_De_Stock alerte_De_Stock = new Alerte_De_Stock();
        
        alerte_De_Stock.AlertaSolido();
        
        Validacion.validar_inventario_solido inventarioSOlido = new validar_inventario_solido();
        
        inventarioSOlido.inicializarValidacionesSolido();
        
        
        IniciarVista();
       
        menu.setVisible(false);
        
        
    }
    
    
    
   private  void AgregarListeners (){
       Inventario_Solido.btbVolverMenu.addActionListener(this);
       Inventario_Solido.btbModificar.addActionListener(this);
       Inventario_Solido.btbHistorialDeMovimientoSolido.addActionListener(this);
       Inventario_Solido.btbInformacionDeLote.addActionListener(this);
       Inventario_Solido.btbRegistrar.addActionListener(this);
       Inventario_Solido.btbOcultarHistorial.addActionListener(this);
       Inventario_Solido.btbCancelarActualizacion.addActionListener(this);
       Inventario_Solido.btbConfirmarActualizacion.addActionListener(this);
       Inventario_Solido.btbOcultarLote.addActionListener(this);
       Inventario_Solido.btbConfirmarActualizacionLote.addActionListener(this);
       Inventario_Solido.btbCancelarActualizcionLote.addActionListener(this);
       Inventario_Solido.btbModificarLote.addActionListener(this);
   }
    
    
    
    
    
    @Override
    public void actionPerformed(ActionEvent e) {
        
        if (e.getSource() == Inventario_Solido.btbVolverMenu) {
            Volvermenu();
            
        }
        
        else if (e.getSource() == Inventario_Solido.btbRegistrar){
            RegistrarProductoSolido();
        }
        
        else if (e.getSource() == Inventario_Solido.btbHistorialDeMovimientoSolido){
            HistorialMovientoSolido();
        }
        
        else if (e.getSource() == Inventario_Solido.btbOcultarHistorial){
            OcultarHistorial();
        }
        
        else if (e.getSource() == Inventario_Solido.btbModificar){
            SeleccioanrParaActualizarDatos();
        }
        
        else if (e.getSource() == Inventario_Solido.btbCancelarActualizacion){
           CancelarActualizacionDatos();
        }
        
        else if (e.getSource() == Inventario_Solido.btbConfirmarActualizacion){
            ConfirmaActualizacion();
            
        }
        
        else if (e.getSource() == Inventario_Solido.btbInformacionDeLote){
           
            MostrarInformacionLote();
        }
        
        else if (e.getSource() == Inventario_Solido.btbOcultarLote){
            OcultarInformacionLote();
        }
        else if (e.getSource() == Inventario_Solido.btbModificarLote){
            SeleccionarLote();
        }
        
        else if (e.getSource() == Inventario_Solido.btbCancelarActualizcionLote){
            CancelarActuliconLote();
        }
        
        else if (e.getSource() == Inventario_Solido.btbConfirmarActualizacionLote){
            ConfirmarActualizacionLote();
        }
        
        
       
    }
    
    
    
    private  void  Volvermenu (){
        menu.setVisible(true);
        
        Inventario_Solido.setVisible(false);
    }
    
    
    private  void ConfirmarActualizacionLote(){
      Validacion.validar_inventario_solido inventarioSolido = new validar_inventario_solido();
        try {
            inventarioSolido. ValidarDatosLoteSolido() ;
        } catch (ClassNotFoundException ex) {
            System.getLogger(Controlador_Inventario_Solido.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (SQLException ex) {
            System.getLogger(Controlador_Inventario_Solido.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        
        CargarCuenta();
    }
    
    private  void SeleccionarLote (){
        
        int filla = Inventario_Solido.TablaLote.getSelectedRow();
        
        if (filla == -1) {
    JOptionPane.showMessageDialog(null, 
        "Debe seleccionar un producto para modificarlo.", 
        "Error", 
        JOptionPane.ERROR_MESSAGE);
    return;
} else {
    String mensaje = "<html><b>¿Estás seguro de actualizar los siguientes campos?</b><br><br>" +
                 "• Cantidad<br>" +
                 "• Costo Unitario<br>" +
                 "• Precio Venta <br>" +
                 "• Proveedor</html>"  ;
               

int respuesta = JOptionPane.showConfirmDialog(null, 
    mensaje, 
    "Confirmar modificación", 
    JOptionPane.YES_NO_OPTION, 
    JOptionPane.QUESTION_MESSAGE);

if (respuesta == JOptionPane.YES_OPTION) {
    
    Inventario_Solido.btbOcultarLote.setVisible(false);
    Inventario_Solido.btbModificarLote.setVisible(false);
    Inventario_Solido.btbConfirmarActualizacionLote.setVisible(true);
    Inventario_Solido.btbCancelarActualizcionLote.setVisible(true);
   
  Inventario_Solido.txtCantidadSolido.setText(Inventario_Solido.TablaLote.getValueAt(filla, 2).toString());
  Inventario_Solido.txtCostoUUnitario.setText(Inventario_Solido.TablaLote.getValueAt(filla,5).toString());
  Inventario_Solido.txtPrecioVentaSolido.setText(Inventario_Solido.TablaLote.getValueAt(filla, 7).toString());
    seleccionarItemCombo(Inventario_Solido.jComboBoxProveedor, Inventario_Solido.TablaLote.getValueAt(filla, 9));
    
    
} else {
    return; // el usuario canceló
}
}
        
        
        
    }
        
    
    private  void CancelarActuliconLote (){
        
    Inventario_Solido.btbOcultarLote.setVisible(true);
    Inventario_Solido.btbModificarLote.setVisible(true);
    Inventario_Solido.btbConfirmarActualizacionLote.setVisible(false);
    Inventario_Solido.btbCancelarActualizcionLote.setVisible(false);
    
    Inventario_Solido.txtCantidadSolido.setText("");
    Inventario_Solido.txtCostoUUnitario.setText("");
    Inventario_Solido.txtPrecioVentaSolido.setText("");
    Inventario_Solido.jComboBoxProveedor.setSelectedIndex(0);
    
   
        
    }
    
    private  void MostrarInformacionLote (){
        
        Inventario_Solido.jScrollPaneSolidoPrincipal.setVisible(false);
        Inventario_Solido.jScrollPaneSolidoInformacionLote.setVisible(true);
        Inventario_Solido.btbOcultarLote.setVisible(true);
           Inventario_Solido.txtBuscarLote.setText("");
            Inventario_Solido.btbModificar.setVisible(false);
    Inventario_Solido.btbHistorialDeMovimientoSolido.setVisible(false);
    Inventario_Solido.btbInformacionDeLote.setVisible(false);
    Inventario_Solido.btbRegistrar.setVisible(false);
    
     Inventario_Solido.txtBuscarLote.setVisible(true);
        Inventario_Solido.txtBuscarProductoEntabla.setVisible(false);
        
        Inventario_Solido.btbModificarLote.setVisible(true);
    
    ModeloDAO.InventarioSolidoDAO inventarioSolidoDAO = new InventarioSolidoDAO();
        try {
            inventarioSolidoDAO. MostrarVerInventarioSolidoLote();
        } catch (ClassNotFoundException ex) {
            System.getLogger(Controlador_Inventario_Solido.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (SQLException ex) {
            System.getLogger(Controlador_Inventario_Solido.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        
       inventarioSolidoDAO.agregarFiltroBusquedalOTE();
        
    }
    
    private  void OcultarInformacionLote (){
        
            Inventario_Solido.btbModificar.setVisible(true);
    Inventario_Solido.btbHistorialDeMovimientoSolido.setVisible(true);
    Inventario_Solido.btbInformacionDeLote.setVisible(true);
    Inventario_Solido.btbRegistrar.setVisible(true);
    
       Inventario_Solido.btbModificarLote.setVisible(false);
    
        Inventario_Solido.jScrollPaneSolidoPrincipal.setVisible(true);
        Inventario_Solido.jScrollPaneSolidoInformacionLote.setVisible(false);
        Inventario_Solido.btbOcultarLote.setVisible(false);    
        Inventario_Solido.txtBuscarLote.setVisible(false);
        Inventario_Solido.txtBuscarProductoEntabla.setVisible(true);
        Inventario_Solido.txtBuscarProductoEntabla.setText("");
     
        
    }
    
    private  void ConfirmaActualizacion (){
      Validacion.validar_inventario_solido InventarioSolido = new validar_inventario_solido();
      
        try {
            InventarioSolido.ValidarActualizacionDatosSolido();
        } catch (ClassNotFoundException ex) {
            System.getLogger(Controlador_Inventario_Solido.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (SQLException ex) {
            System.getLogger(Controlador_Inventario_Solido.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
         Funciones.Alerte_De_Stock alerte_De_Stock = new Alerte_De_Stock();
        
        alerte_De_Stock.AlertaSolido();
        
    }
    
    
    
private void SeleccioanrParaActualizarDatos (){

       int fila = Inventario_Solido.TablaSolido.getSelectedRow();
           
       if (fila == -1) {
    JOptionPane.showMessageDialog(null,
        "Debe seleccionar un producto para modificarlo.",
        "Error",
        JOptionPane.ERROR_MESSAGE);
    return;
} else {
    String mensaje = "<html><b>¿Estás seguro de actualizar los siguientes campos?</b><br><br>" +
                 "• Stock Mínimo<br>" +
                 "• Stock Máximo<br>" +
                 "• Ubicación<br>" +
                 "• Producto</html>";
int respuesta = JOptionPane.showConfirmDialog(null,
    mensaje,
    "Confirmar modificación",
    JOptionPane.YES_NO_OPTION,
    JOptionPane.QUESTION_MESSAGE);
if (respuesta == JOptionPane.YES_OPTION) {
   
    Inventario_Solido.btbModificar.setVisible(false);
    Inventario_Solido.btbHistorialDeMovimientoSolido.setVisible(false);
    Inventario_Solido.btbInformacionDeLote.setVisible(false);
    Inventario_Solido.btbRegistrar.setVisible(false);
    
    Inventario_Solido.btbConfirmarActualizacion.setVisible(true);
    Inventario_Solido.btbCancelarActualizacion.setVisible(true);
  // Dentro del if (respuesta == JOptionPane.YES_OPTION):

seleccionarItemCombo(Inventario_Solido.jComboProductoSolido, 
                     Inventario_Solido.TablaSolido.getValueAt(fila, 1));

Inventario_Solido.txtStockMinimo.setText(
    Inventario_Solido.TablaSolido.getValueAt(fila,3).toString());

Inventario_Solido.txtStoxkMaximo.setText(
    Inventario_Solido.TablaSolido.getValueAt(fila,4).toString());

// Ubicación - Usamos el método mejorado
Object valorUbicacion = Inventario_Solido.TablaSolido.getValueAt(fila, 5);
seleccionarItemCombo(Inventario_Solido.jComboUbicacionSolido, valorUbicacion);
 
   
   
} else {
    return; // el usuario canceló
}
}
       
       
       
    }
       
        
        
        private  void  CancelarActualizacionDatos (){
            
           Inventario_Solido.btbConfirmarActualizacion.setVisible(false);
    Inventario_Solido.btbCancelarActualizacion.setVisible(false);
   
    Inventario_Solido.btbModificar.setVisible(true);
    Inventario_Solido.btbHistorialDeMovimientoSolido.setVisible(true);
    Inventario_Solido.btbInformacionDeLote.setVisible(true);
    Inventario_Solido.btbRegistrar.setVisible(true); 
    
    
    
    Inventario_Solido.txtStockMinimo.setText("");
    Inventario_Solido.txtStoxkMaximo.setText("");
    
    
            
            
        }
        
        
 
    
    private  void HistorialMovientoSolido (){
 
        Inventario_Solido.jScrollPaneSolidoPrincipal.setVisible(false);
        Inventario_Solido.txtBuscarProductoEntabla.setVisible(false);
        
        Inventario_Solido.jScrollPaneSolidoHistorial.setVisible(true);
        Inventario_Solido.txtBuscarHistorial.setVisible(true);
        Inventario_Solido.btbOcultarHistorial.setVisible(true);
        
        Inventario_Solido.btbModificar.setVisible(false);
        Inventario_Solido.btbHistorialDeMovimientoSolido.setVisible(false);
        Inventario_Solido.btbInformacionDeLote.setVisible(false);
        Inventario_Solido.btbRegistrar.setVisible(false);
        Inventario_Solido.txtBuscarHistorial.setText("");
        ModeloDAO.MovientosDao movientosDao = new MovientosDao();
        
        try {
            movientosDao. MostrarMovimientoSOLIDO();
        } catch (ClassNotFoundException ex) {
            System.getLogger(Controlador_Inventario_Solido.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (SQLException ex) {
            System.getLogger(Controlador_Inventario_Solido.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        
        movientosDao.agregarFiltroBusquedaSOLIDOMovimiento();
  
    }
    
    
    private  void OcultarHistorial (){
          Inventario_Solido.jScrollPaneSolidoPrincipal.setVisible(true);
        Inventario_Solido.txtBuscarProductoEntabla.setVisible(true);
        
        Inventario_Solido.jScrollPaneSolidoHistorial.setVisible(false);
        Inventario_Solido.txtBuscarHistorial.setVisible(false);
        Inventario_Solido.btbOcultarHistorial.setVisible(false);
        
        Inventario_Solido.btbModificar.setVisible(true);
        Inventario_Solido.btbHistorialDeMovimientoSolido.setVisible(true);
        Inventario_Solido.btbInformacionDeLote.setVisible(true);
        Inventario_Solido.btbRegistrar.setVisible(true);
        
        Inventario_Solido.txtBuscarProductoEntabla.setText("");
        
    }
    
    
    private  void IniciarVista (){
        Inventario_Solido.jScrollPaneSolidoPrincipal.setVisible(true);
        Inventario_Solido.jScrollPaneSolidoHistorial.setVisible(false);
        Inventario_Solido.btbOcultarHistorial.setVisible(false);
        Inventario_Solido.txtBuscarHistorial.setVisible(false);
        Inventario_Solido.btbConfirmarActualizacion.setVisible(false);
        Inventario_Solido.btbCancelarActualizacion.setVisible(false);
        
          Inventario_Solido.btbModificar.setVisible(true);
    Inventario_Solido.btbHistorialDeMovimientoSolido.setVisible(true);
    Inventario_Solido.btbInformacionDeLote.setVisible(true);
    Inventario_Solido.btbRegistrar.setVisible(true); 
    
    
    Inventario_Solido.txtBuscarProductoEntabla.setVisible(true);
    Inventario_Solido.txtStockMinimo.setText("");
    Inventario_Solido.txtStoxkMaximo.setText("");
    Inventario_Solido.txtCostoUUnitario.setText("");
  Inventario_Solido.btbOcultarLote.setVisible(false);
   Inventario_Solido.txtBuscarLote.setVisible(false);
    Inventario_Solido.btbConfirmarActualizacionLote.setVisible(false);
       Inventario_Solido.btbCancelarActualizcionLote.setVisible(false);
       Inventario_Solido.btbModificarLote.setVisible(false);
      Inventario_Solido.txtBuscarLote.setText("");
      Inventario_Solido.txtBuscarProductoEntabla.setText("");
      Inventario_Solido.txtBuscarHistorial.setText("");
      
      Inventario_Solido.txtBuscarProducroSolido.setText("");
      Inventario_Solido.txtBuscarUbicacion.setText("");
      Inventario_Solido.txtCantidadSolido.setText("");
      Inventario_Solido.txtStoxkMaximo.setText("");
      Inventario_Solido.txtStockMinimo.setText("");
      Inventario_Solido.txtCostoUUnitario.setText("");
      Inventario_Solido.txtPrecioVentaSolido.setText("");
       
    }
    
    
    
    private  void RegistrarProductoSolido (){
    Validacion.validar_inventario_solido inventarioSolido = new validar_inventario_solido();
    
        try {
            inventarioSolido.ValidarRegistroSolido();
        } catch (ClassNotFoundException ex) {
            System.getLogger(Controlador_Inventario_Solido.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (SQLException ex) {
            System.getLogger(Controlador_Inventario_Solido.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
         
        
        Funciones.Alerte_De_Stock alerte_De_Stock = new Alerte_De_Stock();
        
        alerte_De_Stock.AlertaSolido();
        
        CargarCuenta();
    }
    
    
    
    private  void CargaVista (){
        
           ModeloDAO.AlmacenDao almacenDao = new AlmacenDao();
        try {
            almacenDao.CargarComboxBoxAlmecenSolido();
        } catch (ClassNotFoundException ex) {
            System.getLogger(Controlador_Inventario_Solido.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (SQLException ex) {
            System.getLogger(Controlador_Inventario_Solido.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        
          ModeloDAO.InventarioSolidoDAO inventarioSolidoDAO = new InventarioSolidoDAO();
        try {
            inventarioSolidoDAO.MostrarVerInventarioSOLIDO();
        } catch (ClassNotFoundException ex) {
            System.getLogger(Controlador_Inventario_Solido.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (SQLException ex) {
            System.getLogger(Controlador_Inventario_Solido.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        inventarioSolidoDAO.agregarFiltroBusquedaSOLID();
        
        
        
        ModeloDAO.ProductoDao productoDao = new ProductoDao ();
        try {
            productoDao.cargarProductosEnComboSolido();
        } catch (ClassNotFoundException ex) {
            System.getLogger(Controlador_Inventario_Solido.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (SQLException ex) {
            System.getLogger(Controlador_Inventario_Solido.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        
        productoDao. agregarBuscadorProductoSolidoInvenatario() ;
        
        
     
        
      
        almacenDao.agregarBuscadorAlmacenSolido();
        
        
        ModeloDAO.ProveedorDao proveedorDao = new ProveedorDao();
        
        try {
            proveedorDao.cargarProveedoresInventarioSolido();
        } catch (ClassNotFoundException ex) {
            System.getLogger(Controlador_Inventario_Solido.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (SQLException ex) {
            System.getLogger(Controlador_Inventario_Solido.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
         proveedorDao.agregarBuscadorProveedorInventarioSOLIDO();
         
        
         
        
    }
    
    private  void CargarCuenta (){
         ModeloDAO.CuentaDao cuentaDao = new CuentaDao();
         
        try {
            cuentaDao.cargarComboCuentasInventarioo(Inventario_Solido.jComboBoxCuentaDebitar);
        } catch (ClassNotFoundException ex) {
            System.getLogger(Controlador_Inventario_Solido.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (SQLException ex) {
            System.getLogger(Controlador_Inventario_Solido.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        
        try {
            cuentaDao.cargarComboCuentasPasivo(Inventario_Solido.jComboBoxCuentaAcreditar);
        } catch (ClassNotFoundException ex) {
            System.getLogger(Controlador_Inventario_Solido.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (SQLException ex) {
            System.getLogger(Controlador_Inventario_Solido.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
    
    
    
    
    
      
  private boolean actualizandoPrecio = false;   // ← Bandera para evitar recursión

private void CalcularPrecioVentaSolido() {
    // Si ya estamos actualizando, salimos para evitar bucle infinito
    if (actualizandoPrecio) {
        return;
    }

    try {
        actualizandoPrecio = true;   // Activamos la bandera

        double precioUnitario = Double.parseDouble(
           Inventario_Solido.txtCostoUUnitario.getText().trim());

        double porcentaje = Double.parseDouble(
        Inventario_Solido.txtPorcentajeSolido.getText().trim());

          double precioVentaFinal = precioUnitario  / (1 - (porcentaje / 100.0));

        // Formatear con 2 decimales
        String textoFormateado = String.format(Locale.US, "%.2f", precioVentaFinal);

        // Actualizamos el campo de precio de venta
     Inventario_Solido.txtPrecioVentaSolido.setText(textoFormateado);

    } catch (NumberFormatException e) {
        // Si hay error (campos vacíos o texto inválido), limpiamos el precio
        Inventario_Solido.txtPrecioVentaSolido.setText("");
    } finally {
        actualizandoPrecio = false;   // Siempre liberamos la bandera
    }
}
    
private void IniciarPrecioVenta() {

    // Listener para el campo de PORCENTAJE
   Inventario_Solido.txtPorcentajeSolido.getDocument().addDocumentListener(new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            CalcularPrecioVentaSolido();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            CalcularPrecioVentaSolido();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            CalcularPrecioVentaSolido();
        }
    });

    // Listener para el campo de COSTO UNITARIO
    Inventario_Solido.txtCostoUUnitario.getDocument().addDocumentListener(new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            CalcularPrecioVentaSolido();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            CalcularPrecioVentaSolido();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            CalcularPrecioVentaSolido();
        }
    });

}
    

private void seleccionarItemCombo(JComboBox combo, Object valor) {
    if (valor == null) {
        if (combo != null) combo.setSelectedIndex(0);
        return;
    }

    // Si es el combo de Ubicación, usamos el método especializado
    if (combo == Inventario_Solido.jComboUbicacionSolido) {
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
    
    JComboBox combo = Inventario_Solido.jComboUbicacionSolido;
    
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
