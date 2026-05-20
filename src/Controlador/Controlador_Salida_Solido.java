/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Funciones.BCV;
import ModeloDAO.CuentaDao;
import ModeloDAO.InventarioSolidoDAO;
import Reportes.Orden_Salida_Solido;



import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Vista_Usuari_Empleado.Menu_Sistema;
import Vista_Almacen.Vista_Salida_Solido;
import java.math.BigDecimal;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
/**
 *
 * @author avila
 */
public class Controlador_Salida_Solido  implements  ActionListener{
    
    private  final Menu_Sistema menu;
    private Vista_Salida_Solido Salida_Solido;
    
    public  Controlador_Salida_Solido (Menu_Sistema menu){
        this.menu = menu;
    }
    
    
    public  void MostrarVista(){
        if (Salida_Solido == null) {
            
            Salida_Solido = new Vista_Salida_Solido();
            
            //AgregarListener 
            CargarListeners();
        }
        Salida_Solido.setVisible(true);
        TasaBCV();
        CargarProductoSolido();
        CargarCuentas();
        CalcularTotalSolido();
        
        menu.setVisible(false);
    }
    
    
    private  void CargarListeners (){
        Salida_Solido.btbVolver.addActionListener(this);
        Salida_Solido.btbRealizarMovimiento.addActionListener(this);
    }
    
    
    
    @Override
    public void actionPerformed(ActionEvent e) {
         if (e.getSource() == Salida_Solido.btbVolver) {
             VolverMenu();
            
        }
         
         else if (e.getSource() == Salida_Solido.btbRealizarMovimiento){
             RealizarMovimiento();
         }
         
         
        
        
    }
    
    
    private  void VolverMenu (){
        menu.setVisible(true);
        
        Salida_Solido.setVisible(false);
    }
    
    
    
    private  void  RealizarMovimiento (){
        
       Reportes.Orden_Salida_Solido orden_Salida_Solido = new Orden_Salida_Solido();
       

BigDecimal tasaDolar = new BigDecimal(Vista_Salida_Solido.txtTasaBCV.getText().replace(",", "."));
String detalle = Vista_Salida_Solido.txtDetalle.getText().trim();

orden_Salida_Solido.generarPDFOrdenSalidaSolido(


 Integer.parseInt(Vista_Salida_Solido.txtCantidad.getText()),
    new BigDecimal(Vista_Salida_Solido.txtPrecioVentaUnitario.getText()),
    tasaDolar,
    detalle);
        
         
        ModeloDAO.MovientosDao movientosDao = new ModeloDAO.MovientosDao();
        
        try {
            movientosDao. Realizar_SALIDA_SOLIDO();
        } catch (ClassNotFoundException ex) {
            System.getLogger(Controlador_Salida_Solido.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (SQLException ex) {
            System.getLogger(Controlador_Salida_Solido.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        
        CargarProductoSolido();
        
        CargarCuentas();
    }
    
    
    private  void  TasaBCV (){
        
        Funciones.BCV bcv = new BCV();
        
        bcv.mostrarTasaEnCampo(Salida_Solido.txtTasaBCV);
    }
    
    
    
    private  void CargarProductoSolido (){
        ModeloDAO.InventarioSolidoDAO inventarioSolidoDAO = new InventarioSolidoDAO();
        
        try {
            inventarioSolidoDAO. CargarComboxBoxSOLIDO();
        } catch (ClassNotFoundException ex) {
            System.getLogger(Controlador_Salida_Solido.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (SQLException ex) {
            System.getLogger(Controlador_Salida_Solido.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
    
    
    
   private  void CargarCuentas (){
       try {

            ModeloDAO.CuentaDao cuentaDao = new CuentaDao();
            try {
                cuentaDao.cargarComboCuentasCaja(Salida_Solido.jComboBoxCuantaPasivo);
            } catch (ClassNotFoundException ex) {
                System.getLogger(Controlador_Liquido_Salida.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            } catch (SQLException ex) {
                System.getLogger(Controlador_Liquido_Salida.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
            cuentaDao.cargarComboCuentascVentas(Salida_Solido.jComboBoxIngreso);
        } catch (ClassNotFoundException ex) {
            System.getLogger(Controlador_Liquido_Salida.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (SQLException ex) {
            System.getLogger(Controlador_Liquido_Salida.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
   }
   
   
   
   
   
   
   
   
     private boolean listenersConfiguradosSolido = false;
public void CalcularTotalSolido() {
    // ===============================================================
    // 1. CONFIGURAR LISTENERS (SOLO LA PRIMERA VEZ)
    // ===============================================================
    if (!listenersConfiguradosSolido) {
        DocumentListener listener = new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { CalcularTotalSolido(); }
            @Override public void removeUpdate(DocumentEvent e) { CalcularTotalSolido(); }
            @Override public void changedUpdate(DocumentEvent e) { CalcularTotalSolido(); }
        };

        Salida_Solido.txtCantidad.getDocument().addDocumentListener(listener);
        Salida_Solido.txtPrecioVentaUnitario.getDocument().addDocumentListener(listener);
      Salida_Solido.txtTasaBCV.getDocument().addDocumentListener(listener);

       Salida_Solido.txtTotalEnBolivares.setEditable(false);
        Salida_Solido.txtTotalEnDolares.setEditable(false);

        // INICIA LA CARGA DE LA TASA
      //  BCV.mostrarTasaEnCampo(Gestionar_Almacen.txtDolarHoy);

        listenersConfiguradosSolido = true;
    }

    // ===============================================================
    // 2. LEER CANTIDAD
    // ===============================================================
    double cantidad = 0;
    try {
        String texto = Salida_Solido.txtCantidad.getText().trim().replace(",", ".");
        if (!texto.isEmpty()) cantidad = Double.parseDouble(texto);
    } catch (Exception e) {}

    // ===============================================================
    // 3. LEER PRECIO EN DÓLARES
    // ===============================================================
    double precioUSD = 0;
    try {
        String texto = Salida_Solido.txtPrecioVentaUnitario.getText().trim().replace(",", ".");
        if (!texto.isEmpty()) precioUSD = Double.parseDouble(texto);
    } catch (Exception e) {}

    // ===============================================================
    // 4. LEER TASA DEL DÓLAR (IGNORA "Cargando...")
    // ===============================================================
    double tasaDolar = 1;
    try {
        String texto = Salida_Solido.txtTasaBCV.getText()
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
    Salida_Solido.txtTotalEnBolivares.setText("Bs. " + df.format(totalBolivares));
    Salida_Solido.txtTotalEnDolares.setText("$ " + String.format("%.2f", totalDolares));
}
    
}
