/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;
import Funciones.Alerte_De_Stock;
import ModeloDAO.AlmacenDao;
import ModeloDAO.CuentaDao;
import ModeloDAO.InventarioUnidadDAO;
import ModeloDAO.MovientosDao;
import ModeloDAO.ProductoDao;
import ModeloDAO.ProveedorDao;
import Validacion.Validar_Inventario_Unidad;
import Vista_GestionInventario.InventarioUnidad;
import Vista_Usuari_Empleado.Menu_Sistema;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Locale;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


public class Controlador_Inventario_Unidad  implements  ActionListener{

 private  final Menu_Sistema menu;
 
 private  InventarioUnidad Inventario_Unidad;

public  Controlador_Inventario_Unidad (Menu_Sistema menu){
    this.menu=menu;
}
    
 public  void mostrarVistaUnidad(){
     
     if (Inventario_Unidad == null) {
         Inventario_Unidad = new InventarioUnidad();
         
        // AgregarListenres
         AgregarListenrs();
     }
     
     Inventario_Unidad.setVisible(true);
     
     CargarVista();
     CargarCuentas();
     IniciarPrecioVenta();
     CalcularPrecioVentaUnidad();
     IniciarVista();
      Validar_Inventario_Unidad validar_Inventario_Unidad = new Validar_Inventario_Unidad();
      validar_Inventario_Unidad.inicializarValidacionesUnidad();
     menu.setVisible(false);
     
     
 }
 
 
 
 private  void AgregarListenrs (){
     
     
     Inventario_Unidad.btbVolverMenu.addActionListener(this);
     Inventario_Unidad.btbModificar.addActionListener(this);
     Inventario_Unidad.btbHistorialMovimiento.addActionListener(this);
     Inventario_Unidad.btbInofrmacionLote.addActionListener(this);
     Inventario_Unidad.btbRegistrar.addActionListener(this);
     Inventario_Unidad.btbOcultarHistorial.addActionListener(this);
 
     Inventario_Unidad.btbCancelarModificaion.addActionListener(this);
     Inventario_Unidad.btbConfirmarActualizacion.addActionListener(this);
     Inventario_Unidad.btbOcultarLote.addActionListener(this);
     Inventario_Unidad.btbModificarLote.addActionListener(this);
     Inventario_Unidad.btbConfirmaModificacionLote.addActionListener(this);
     Inventario_Unidad.btbCancelarModificacionLote.addActionListener(this);
     
 }
 



    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == Inventario_Unidad.btbVolverMenu) {
            VolverAlmenu();
            
        }
        else if (e.getSource() == Inventario_Unidad.btbModificar){
            Modificar();
        }
        
        else if (e.getSource() == Inventario_Unidad.btbRegistrar){
            RegistrarProductoUnidad();
        }
        
        else if (e.getSource() == Inventario_Unidad.btbHistorialMovimiento){
            MostrarHistorialMovimoiento();
        }
        
        
        else if (e.getSource() == Inventario_Unidad.btbOcultarHistorial){
            OcultarHistorialDeMovimoiento();
        }
        
     
        
        else if (e.getSource() == Inventario_Unidad.btbCancelarModificaion){
          CAncelarModificacion();
        }
        
        else if (e.getSource() == Inventario_Unidad.btbConfirmarActualizacion){
            ConfirmarActualizacion();
        }
        
        else if (e.getSource() == Inventario_Unidad.btbInofrmacionLote){
            MostrarInformacionDeLote();
        }
        
        else if (e.getSource() == Inventario_Unidad.btbOcultarLote){
            OcultarLote();
        }
        
        else if (e.getSource() == Inventario_Unidad.btbModificarLote){
         SelecionarLoteparaMoficarlo ();
        }
        
        else if (e.getSource() == Inventario_Unidad.btbCancelarModificacionLote){
            CancelarMofidicicionLote();
        }
        else if (e.getSource() == Inventario_Unidad.btbConfirmaModificacionLote){
            ConfirmarModificacionLote();
        }
        
    }
    
    
    
    
    private  void ConfirmarModificacionLote (){
        
        Validar_Inventario_Unidad validar_Inventario_Unidad = new Validar_Inventario_Unidad();
        
     try {
         validar_Inventario_Unidad.ValidarActualizacionLoteUnida();
     } catch (ClassNotFoundException ex) {
         System.getLogger(Controlador_Inventario_Unidad.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
     } catch (SQLException ex) {
         System.getLogger(Controlador_Inventario_Unidad.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
     }
     
     CargarCuentas();
    }
    
    
    
    private  void SelecionarLoteparaMoficarlo (){
        int filla = Inventario_Unidad.TablaLote.getSelectedRow();
        if (filla == -1) {
    JOptionPane.showMessageDialog(null, 
        "Debe seleccionar un lote para modificarlo.", 
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
    
    Inventario_Unidad.btbModificarLote.setVisible(false);
    Inventario_Unidad.btbOcultarLote.setVisible(false);
    
    Inventario_Unidad.btbConfirmaModificacionLote.setVisible(true);
    Inventario_Unidad.btbCancelarModificacionLote.setVisible(true);
    
    Inventario_Unidad.txtCantidadProducto.setText(Inventario_Unidad.TablaLote.getValueAt(filla, 2).toString());
  Inventario_Unidad.txtCostoUnitario.setText(Inventario_Unidad.TablaLote.getValueAt(filla,5).toString());
  Inventario_Unidad.txtPrecioVenta.setText(Inventario_Unidad.TablaLote.getValueAt(filla, 7).toString());
    seleccionarItemCombo(Inventario_Unidad.jComboBoxProveedor, Inventario_Unidad.TablaLote.getValueAt(filla, 9));
   
  
  
    
    
} else {
    return; // el usuario canceló
}
}
        
        
        
    }
        
        
        
        
    
    
    
    
    
    private  void CancelarMofidicicionLote (){
        
        Inventario_Unidad.btbModificarLote.setVisible(true);
    Inventario_Unidad.btbOcultarLote.setVisible(true);
    
    Inventario_Unidad.btbConfirmaModificacionLote.setVisible(false);
    Inventario_Unidad.btbCancelarModificacionLote.setVisible(false);
    
    Inventario_Unidad.txtCantidadProducto.setText("");
    Inventario_Unidad.txtCostoUnitario.setText("");
    Inventario_Unidad.txtPrecioVenta.setText("");
    
    }
    
    
    
    
    
    
    
    
    private  void MostrarInformacionDeLote (){
        
        
        Inventario_Unidad.jScrollPanePrincipal.setVisible(false);
        Inventario_Unidad.jScrollPaneInformacionLote.setVisible(true);
        
        Inventario_Unidad.btbModificarLote.setVisible(true);
        Inventario_Unidad.btbOcultarLote.setVisible(true);
        
        Inventario_Unidad.btbModificar.setVisible(false);
        Inventario_Unidad.btbHistorialMovimiento.setVisible(false);
        Inventario_Unidad.btbInofrmacionLote.setVisible(false);
        Inventario_Unidad.btbRegistrar.setVisible(false);
        Inventario_Unidad.txtBuscarLote.setText("");
        Inventario_Unidad.txtBuscarLote.setVisible(true);
        
        Inventario_Unidad.txtBuscarProductoEnTABLA.setVisible(false);
        
        
        ModeloDAO.InventarioUnidadDAO unidadDAO = new InventarioUnidadDAO();
        
     try {
         unidadDAO.MostrarLoteUNIDAD();
     } catch (ClassNotFoundException ex) {
         System.getLogger(Controlador_Inventario_Unidad.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
     } catch (SQLException ex) {
         System.getLogger(Controlador_Inventario_Unidad.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
     }
          
          
        unidadDAO.agregarFiltroBusquedaUnidadLote();
        
        
        
        
        
    }
    
    
    private void  OcultarLote (){
        
        Inventario_Unidad.jScrollPanePrincipal.setVisible(true);
        Inventario_Unidad.jScrollPaneInformacionLote.setVisible(false);
        
        Inventario_Unidad.btbModificarLote.setVisible(false);
        Inventario_Unidad.btbOcultarLote.setVisible(false);
        
        Inventario_Unidad.btbModificar.setVisible(true);
        Inventario_Unidad.btbHistorialMovimiento.setVisible(true);
        Inventario_Unidad.btbInofrmacionLote.setVisible(true);
        Inventario_Unidad.btbRegistrar.setVisible(true);
        Inventario_Unidad.txtBuscarLote.setText("");
        Inventario_Unidad.txtBuscarLote.setVisible(false);
        Inventario_Unidad.txtBuscarProductoEnTABLA.setVisible(true);
        
        
    } 
    
    
    
    
    private  void ConfirmarActualizacion (){
    Validar_Inventario_Unidad validar_Inventario_Unidad = new Validar_Inventario_Unidad();
     try {
         validar_Inventario_Unidad.ActualizarDatosUnidadInventario();
     } catch (ClassNotFoundException ex) {
         System.getLogger(Controlador_Inventario_Unidad.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
     } catch (SQLException ex) {
         System.getLogger(Controlador_Inventario_Unidad.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
     }
     
      Funciones.Alerte_De_Stock alerte_De_Stock = new Alerte_De_Stock();
     
     alerte_De_Stock.AlertaUnida();
    }
    
    
  private void Modificar() {
    
    int fila = InventarioUnidad.TablaUnidad.getSelectedRow();
       
    if (fila == -1) {
        JOptionPane.showMessageDialog(null,
            "Debe seleccionar un producto para modificarlo.",
            "Error",
            JOptionPane.ERROR_MESSAGE);
        return;
    }

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
  
        Inventario_Unidad.btbConfirmarActualizacion.setVisible(true);
        Inventario_Unidad.btbCancelarModificaion.setVisible(true);
 
        Inventario_Unidad.btbModificar.setVisible(false);
        Inventario_Unidad.btbHistorialMovimiento.setVisible(false);
        Inventario_Unidad.btbInofrmacionLote.setVisible(false);
        Inventario_Unidad.btbRegistrar.setVisible(false);
 
        // Cargar datos
        seleccionarItemCombo(Inventario_Unidad.jComboProductoUnidad,
                             Inventario_Unidad.TablaUnidad.getValueAt(fila, 1));

        InventarioUnidad.txtStockMinimo.setText(
            InventarioUnidad.TablaUnidad.getValueAt(fila,3).toString());

        Inventario_Unidad.txtStockMaximo.setText(
            InventarioUnidad.TablaUnidad.getValueAt(fila,4).toString());

        Object valorUbicacion = InventarioUnidad.TablaUnidad.getValueAt(fila, 5);
        seleccionarItemCombo(InventarioUnidad.jComboBoxUbicacion, valorUbicacion);
        
    } 
    // else: el usuario canceló → no hacemos nada
}
    
    
    
    private  void CAncelarModificacion (){
         Inventario_Unidad.btbConfirmarActualizacion.setVisible(false);
  Inventario_Unidad.btbCancelarModificaion.setVisible(false);
  
  Inventario_Unidad.btbModificar.setVisible(true);
  Inventario_Unidad.btbHistorialMovimiento.setVisible(true);
  Inventario_Unidad.btbInofrmacionLote.setVisible(true);
  Inventario_Unidad.btbRegistrar.setVisible(true);
  Inventario_Unidad.txtStockMaximo.setText("");
  Inventario_Unidad.txtStockMinimo.setText("");

        
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    private  void MostrarHistorialMovimoiento (){
        Inventario_Unidad.btbOcultarHistorial.setVisible(true);
        Inventario_Unidad.jScrollPanePrincipal.setVisible(false);
        Inventario_Unidad.txtBuscarProductoEnTABLA.setVisible(false);
        
        Inventario_Unidad.jScrollPaneHistorialMovimiento.setVisible(true);
        Inventario_Unidad.txtBuscarHistorial.setVisible(true);
        Inventario_Unidad.txtBuscarHistorial.setText("");
        Inventario_Unidad.btbModificar.setVisible(false);
        Inventario_Unidad.btbHistorialMovimiento.setVisible(false);
        Inventario_Unidad.btbInofrmacionLote.setVisible(false);
        Inventario_Unidad.btbRegistrar.setVisible(false);
       
        
        
        ModeloDAO.MovientosDao movientosDao = new MovientosDao();
     try {
         movientosDao.MostrarMovimientoUNIDAD();
     } catch (ClassNotFoundException ex) {
         System.getLogger(Controlador_Inventario_Unidad.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
     } catch (SQLException ex) {
         System.getLogger(Controlador_Inventario_Unidad.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
     }
        movientosDao. agregarFiltroBusquedaUNIDADMovimiento();
        
    }
    
    
    private void OcultarHistorialDeMovimoiento (){
           Inventario_Unidad.jScrollPanePrincipal.setVisible(true);
        Inventario_Unidad.txtBuscarProductoEnTABLA.setVisible(true);
        Inventario_Unidad.txtBuscarProductoEnTABLA.setText("");
        
        Inventario_Unidad.jScrollPaneHistorialMovimiento.setVisible(false);
        Inventario_Unidad.txtBuscarHistorial.setVisible(false);
  
        Inventario_Unidad.btbModificar.setVisible(true);
        Inventario_Unidad.btbHistorialMovimiento.setVisible(true);
        Inventario_Unidad.btbInofrmacionLote.setVisible(true);
        Inventario_Unidad.btbRegistrar.setVisible(true);
        Inventario_Unidad.btbOcultarHistorial.setVisible(false);
        
    }
    
    
   
    
    
    
    private  void RegistrarProductoUnidad (){
        
     Validacion.Validar_Inventario_Unidad validar_Inventario_Unidad = new Validar_Inventario_Unidad();
     
     try {
         validar_Inventario_Unidad. ValidarRegistroUnidadInventario();
     } catch (ClassNotFoundException ex) {
         System.getLogger(Controlador_Inventario_Unidad.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
     } catch (SQLException ex) {
         System.getLogger(Controlador_Inventario_Unidad.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
     }
      
     Funciones.Alerte_De_Stock alerte_De_Stock = new Alerte_De_Stock();
     
     alerte_De_Stock.AlertaUnida();
        
        CargarCuentas();
    }
    
    private  void VolverAlmenu (){
        
        Inventario_Unidad.setVisible(false);

       menu.setVisible(true);
    }
    
    
    private  void IniciarVista (){
        Inventario_Unidad.btbConfirmaModificacionLote.setVisible(false);
        Inventario_Unidad.btbCancelarModificacionLote.setVisible(false);
        Inventario_Unidad.txtBuscarLote.setVisible(false);
        Inventario_Unidad.btbModificarLote.setVisible(false);
        Inventario_Unidad.btbOcultarLote.setVisible(false);
        Inventario_Unidad.jScrollPaneInformacionLote.setVisible(false);
        
        Inventario_Unidad.btbCancelarModificaion.setVisible(false);
        Inventario_Unidad.btbConfirmarActualizacion.setVisible(false);
        
       Inventario_Unidad.jScrollPanePrincipal.setVisible(true);
       Inventario_Unidad.btbModificar.setVisible(true);
       Inventario_Unidad.btbHistorialMovimiento.setVisible(true);
       Inventario_Unidad.btbInofrmacionLote.setVisible(true);
       Inventario_Unidad.btbRegistrar.setVisible(true);
       Inventario_Unidad.txtBuscarProductoEnTABLA.setVisible(true);
       Inventario_Unidad.txtBuscarProductoEnTABLA.setText("");
       Inventario_Unidad.txtBuscarProveedor.setText("");
       Inventario_Unidad.txtBuscarUbicacion.setText("");
       Inventario_Unidad.txtBuscarProductoCombox.setText("");
       Inventario_Unidad.txtBuscarHistorial.setText("");
       Inventario_Unidad.txtCantidadProducto.setText("");
       Inventario_Unidad.txtStockMaximo.setText("");
       Inventario_Unidad.txtStockMinimo.setText("");
       Inventario_Unidad.txtCostoUnitario.setText("");
       Inventario_Unidad.txtPrecioVenta.setText("");
           Inventario_Unidad.txtBuscarLote.setText("");
       
       Inventario_Unidad.txtBuscarHistorial.setVisible(false);
       Inventario_Unidad.jScrollPaneHistorialMovimiento.setVisible(false);
       Inventario_Unidad.btbOcultarHistorial.setVisible(false);
       
       
       
        
        
        
    }
    
    private  void CargarVista (){
        
      ModeloDAO.ProductoDao productoDao = new ProductoDao();
     try {
         productoDao.cargarProductosEnComboUNIDAD();
     } catch (ClassNotFoundException ex) {
         System.getLogger(Controlador_Inventario_Unidad.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
     } catch (SQLException ex) {
         System.getLogger(Controlador_Inventario_Unidad.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
     }
     
     productoDao.agregarBuscadorProductoUNIDADINVENTARIO();
     
     
     ModeloDAO.ProveedorDao proveedorDao = new ProveedorDao();
     
     try {
         proveedorDao.cargarProveedoresInventarioUnidad();
     } catch (ClassNotFoundException ex) {
         System.getLogger(Controlador_Inventario_Unidad.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
     } catch (SQLException ex) {
         System.getLogger(Controlador_Inventario_Unidad.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
     }
        
     proveedorDao.agregarBuscadorProveedorInventarioUnidad();
     
     ModeloDAO.AlmacenDao almacenDao = new AlmacenDao();
     
     try {
         almacenDao. CargarComboxBoxAlmecenuUNIDAD();
     } catch (ClassNotFoundException ex) {
         System.getLogger(Controlador_Inventario_Unidad.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
     } catch (SQLException ex) {
         System.getLogger(Controlador_Inventario_Unidad.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
     }
     
     almacenDao.agregarBuscadorAlmacenUNIDAD();
     
     
     
     ModeloDAO.InventarioUnidadDAO unidadDAO = new InventarioUnidadDAO ();
     try {
         unidadDAO. MostrarUNIDADINVENTARIO();
     } catch (ClassNotFoundException ex) {
         System.getLogger(Controlador_Inventario_Unidad.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
     } catch (SQLException ex) {
         System.getLogger(Controlador_Inventario_Unidad.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
     }
     
     unidadDAO.agregarFiltroBusquedaUnidad();
     
     Funciones.Alerte_De_Stock alerte_De_Stock = new Alerte_De_Stock();
     
     alerte_De_Stock.AlertaUnida();
        
    }
    
    
    private  void  CargarCuentas (){
        
     try {
         ModeloDAO.CuentaDao cuantasDao = new CuentaDao();
         
         cuantasDao.cargarComboCuentasInventario(Inventario_Unidad.jComboBoxCuantDebitar);
         
         cuantasDao.cargarComboCuentasPasivo(Inventario_Unidad.jComboBoxCuentaAcreditar);
     } catch (ClassNotFoundException ex) {
         System.getLogger(Controlador_Inventario_Unidad.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
     } catch (SQLException ex) {
         System.getLogger(Controlador_Inventario_Unidad.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
     }
    }
    
    
        
  private boolean actualizandoPrecio = false;   // ← Bandera para evitar recursión

private void CalcularPrecioVentaUnidad() {
    // Si ya estamos actualizando, salimos para evitar bucle infinito
    if (actualizandoPrecio) {
        return;
    }

    try {
        actualizandoPrecio = true;   // Activamos la bandera

        double precioUnitario = Double.parseDouble(
           Inventario_Unidad.txtCostoUnitario.getText().trim());

        double porcentaje = Double.parseDouble(
        Inventario_Unidad.txtPorcentaje.getText().trim());

       double precioVentaFinal = precioUnitario  / (1 - (porcentaje / 100.0));

        // Formatear con 2 decimales
        String textoFormateado = String.format(Locale.US, "%.2f", precioVentaFinal);

        // Actualizamos el campo de precio de venta
     Inventario_Unidad.txtPrecioVenta.setText(textoFormateado);

    } catch (NumberFormatException e) {
        // Si hay error (campos vacíos o texto inválido), limpiamos el precio
        Inventario_Unidad.txtPrecioVenta.setText("");
    } finally {
        actualizandoPrecio = false;   // Siempre liberamos la bandera
    }
}
    
private void IniciarPrecioVenta() {

    // Listener para el campo de PORCENTAJE
   Inventario_Unidad.txtPorcentaje.getDocument().addDocumentListener(new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            CalcularPrecioVentaUnidad();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            CalcularPrecioVentaUnidad();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            CalcularPrecioVentaUnidad();
        }
    });

    // Listener para el campo de COSTO UNITARIO
    Inventario_Unidad.txtCostoUnitario.getDocument().addDocumentListener(new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            CalcularPrecioVentaUnidad();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            CalcularPrecioVentaUnidad();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            CalcularPrecioVentaUnidad();
        }
    });

}
     
    
private void seleccionarItemCombo(JComboBox combo, Object valor) {
    if (valor == null) {
        if (combo != null) combo.setSelectedIndex(0);
        return;
    }

    // Si es el combo de Ubicación, usamos el método especializado
    if (combo == Inventario_Unidad.jComboBoxUbicacion) {
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
    
    JComboBox combo = Inventario_Unidad.jComboBoxUbicacion;
    
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
