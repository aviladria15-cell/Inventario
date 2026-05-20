/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

/**
 *
 * @author avila
 */
import Funciones.BCV;
import ModeloDAO.CuentaDao;
import ModeloDAO.InventarioUnidadDAO;
import ModeloDAO.MovientosDao;
import Reportes.Orden_Salida_Unidad;


import Vista_Almacen.Vista_Salida_Unidad;
import Vista_Usuari_Empleado.Menu_Sistema;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


public class Controlador_Salida_Unidad  implements  ActionListener{

    private  final Menu_Sistema menu;
    
    private  Vista_Salida_Unidad Salida_Unidad;
    
    public Controlador_Salida_Unidad  (Menu_Sistema menu){
        this.menu = menu;
    }
    
    
    public  void MostrarVistaSalidaUnidad (){
        
        if (Salida_Unidad == null) {
            
            Salida_Unidad = new Vista_Salida_Unidad();
            //AgragarListeners
            AgregarListeners();
        }
        
        Salida_Unidad.setVisible(true);
        
        TasaBcv();
        CalcularTotalUnida() ;
        CargarCuentas();
        CargarProducto();
        menu.setVisible(false);
        
    }
    
    
    
    private  void AgregarListeners (){
        Salida_Unidad.btbVolverMenu.addActionListener(this);
        Salida_Unidad.btbRealizarMovimiento.addActionListener(this);
    }
    
    
    
    
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == Salida_Unidad.btbVolverMenu) {
            VolverMenu();
            
        }
        
        else if (e.getSource() == Salida_Unidad.btbRealizarMovimiento){
            RealizarMovimiento();
        }
        
        
    }
    
    
    
    private  void RealizarMovimiento (){
        ModeloDAO.MovientosDao movientosDao = new MovientosDao();
        
        
         Reportes.Orden_Salida_Unidad orden_Salida_Unidad = new Orden_Salida_Unidad();
       

BigDecimal tasaDolar = new BigDecimal(Vista_Salida_Unidad.txtTasaDolar.getText().replace(",", "."));
String detalle = Vista_Salida_Unidad.txtDetalle.getText().trim();

orden_Salida_Unidad.generarPDFOrdenSalidaUnidad(

 Integer.parseInt(Vista_Salida_Unidad.txtCantidadUnida.getText()),
    new BigDecimal(Vista_Salida_Unidad.txtPrecioVentaUnitario.getText()),
    tasaDolar,
    detalle);
        
         
        try {
            movientosDao.Realizar_SALIDA_Unidad();
        } catch (ClassNotFoundException ex) {
            System.getLogger(Controlador_Salida_Unidad.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (SQLException ex) {
            System.getLogger(Controlador_Salida_Unidad.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        
        CargarCuentas();
        CargarProducto();
    }
    
    
    private  void VolverMenu (){
        menu.setVisible(true);
        
        Salida_Unidad.setVisible(false);
    }
    
    
    
    
    private  void CargarProducto (){
        
        ModeloDAO.InventarioUnidadDAO unidadDAO = new InventarioUnidadDAO();
        try {
            unidadDAO.CargarComboxBoxUnidad();
        } catch (ClassNotFoundException ex) {
            System.getLogger(Controlador_Salida_Unidad.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (SQLException ex) {
            System.getLogger(Controlador_Salida_Unidad.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
    
    private  void TasaBcv (){
        
        
        Funciones.BCV bcv = new BCV();
        
        bcv.mostrarTasaEnCampo(Salida_Unidad.txtTasaDolar);
        
        
    }
    
    
    
    private void CargarCuentas (){
        
        
        try {

            
            ModeloDAO.CuentaDao cuentaDao = new CuentaDao();
            
            try {
                cuentaDao.cargarComboCuentasCaja(Salida_Unidad.jComboBoxCuenta);
            } catch (ClassNotFoundException ex) {
                System.getLogger(Controlador_Salida_Unidad.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            } catch (SQLException ex) {
                System.getLogger(Controlador_Salida_Unidad.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
            
            cuentaDao.cargarComboCuentascVentas(Salida_Unidad.jComboBoxIngreson);
            
            
        } catch (ClassNotFoundException ex) {
            System.getLogger(Controlador_Salida_Unidad.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (SQLException ex) {
            System.getLogger(Controlador_Salida_Unidad.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        
        
    }
    
       
   
     private boolean listenersConfiguradosSolido = false;
public void CalcularTotalUnida() {
    // ===============================================================
    // 1. CONFIGURAR LISTENERS (SOLO LA PRIMERA VEZ)
    // ===============================================================
    if (!listenersConfiguradosSolido) {
        DocumentListener listener = new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { CalcularTotalUnida(); }
            @Override public void removeUpdate(DocumentEvent e) { CalcularTotalUnida(); }
            @Override public void changedUpdate(DocumentEvent e) { CalcularTotalUnida(); }
        };

        Salida_Unidad.txtCantidadUnida.getDocument().addDocumentListener(listener);
        Salida_Unidad.txtPrecioVentaUnitario.getDocument().addDocumentListener(listener);
      Salida_Unidad.txtTasaDolar.getDocument().addDocumentListener(listener);

       Salida_Unidad.txtTotalEnBolivares.setEditable(false);
        Salida_Unidad.txtTotalEnDolares.setEditable(false);

        // INICIA LA CARGA DE LA TASA
      //  BCV.mostrarTasaEnCampo(Gestionar_Almacen.txtDolarHoy);

        listenersConfiguradosSolido = true;
    }

    // ===============================================================
    // 2. LEER CANTIDAD
    // ===============================================================
    double cantidad = 0;
    try {
        String texto = Salida_Unidad.txtCantidadUnida.getText().trim().replace(",", ".");
        if (!texto.isEmpty()) cantidad = Double.parseDouble(texto);
    } catch (Exception e) {}

    // ===============================================================
    // 3. LEER PRECIO EN DÓLARES
    // ===============================================================
    double precioUSD = 0;
    try {
        String texto = Salida_Unidad.txtPrecioVentaUnitario.getText().trim().replace(",", ".");
        if (!texto.isEmpty()) precioUSD = Double.parseDouble(texto);
    } catch (Exception e) {}

    // ===============================================================
    // 4. LEER TASA DEL DÓLAR (IGNORA "Cargando...")
    // ===============================================================
    double tasaDolar = 1;
    try {
        String texto = Salida_Unidad.txtTasaDolar.getText()
                .replaceAll("<[^>]+>", "")  // Quita HTML
                .trim();

        if (texto.equalsIgnoreCase("Cargando...") || texto.isEmpty()) {
            tasaDolar = BCV.leerDeCache(); // Usa caché si está cargando
        } else {
            texto = texto.replace(",", ".");
            tasaDolar = Double.parseDouble(texto);
            if (tasaDolar <= 0) tasaDolar = BCV.leerDeCache();
        }
    } catch (Exception e) {
        tasaDolar = BCV.leerDeCache(); // En error, usa caché
    }

    // ===============================================================
    // 5. CALCULAR TOTALES
    // ===============================================================
    double totalDolares = cantidad * precioUSD;
    double totalBolivares = totalDolares * tasaDolar;

    // ===============================================================
    // 6. FORMATO VENEZOLANO
    // ===============================================================
    DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("es", "VE"));
    symbols.setGroupingSeparator('.');
    symbols.setDecimalSeparator(',');
    DecimalFormat df = new DecimalFormat("#,##0.00", symbols);

    // ===============================================================
    // 7. MOSTRAR RESULTADOS
    // ===============================================================
    Salida_Unidad.txtTotalEnBolivares.setText("Bs. " + df.format(totalBolivares));
    Salida_Unidad.txtTotalEnDolares.setText("$ " + String.format("%.2f", totalDolares));
}
    
    
    
    
    
}
