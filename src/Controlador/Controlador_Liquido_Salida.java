/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Funciones.BCV;
import ModeloDAO.CuentaDao;
import ModeloDAO.InventarioLiquidoDao;
import ModeloDAO.MovientosDao;
import Reportes.Ordenes_de_Salidad;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Vista_Usuari_Empleado.Menu_Sistema;
import Vista_Almacen.Vista_Salida_Liquido;
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
public class Controlador_Liquido_Salida implements  ActionListener{
    
    private  final Menu_Sistema menu;
    
    private  Vista_Salida_Liquido Liquido_Salida;
    
    public  Controlador_Liquido_Salida (Menu_Sistema menu ){
        this.menu= menu;
    }
    
    
    
    public  void MostrarVista (){
        if (Liquido_Salida == null) {
            
            Liquido_Salida = new  Vista_Salida_Liquido();
            AgregarListeners();
        }
        
        Liquido_Salida.setVisible(true);
        CargaVista();
        LimpiarVista();
        CalcularTotalLiquido();
        cargarCuentas();
        
        menu.setVisible(false);
    }
    
    private   void  AgregarListeners(){
Liquido_Salida.btbVolverAlMenu.addActionListener(this);
Liquido_Salida.btbRealizarMovimiento.addActionListener(this);
    }
    

    @Override
    public void actionPerformed(ActionEvent e) {
      
        if (e.getSource() == Liquido_Salida.btbVolverAlMenu){
            VolverMenu();
        }
        
        else if (e.getSource() == Liquido_Salida.btbRealizarMovimiento){
            RealizarMovimiento();
        }
    }
    
    private  void VolverMenu (){
        Liquido_Salida.setVisible(false);
        
        menu.setVisible(true);
    }
    
    
    
    
    
    
    
    private  void RealizarMovimiento (){
        
        
        Ordenes_de_Salidad reportes = new Ordenes_de_Salidad();

BigDecimal tasaDolar = new BigDecimal(Vista_Salida_Liquido.txtTasaDelDolar.getText().replace(",", "."));
String detalle = Vista_Salida_Liquido.txtDetalle.getText().trim();

reportes.generarPDFOrdenSalida(
    Integer.parseInt(Vista_Salida_Liquido.txtCantidadSalida.getText()),
    new BigDecimal(Vista_Salida_Liquido.txtPrecioVenta.getText()),
    tasaDolar,
    detalle
);
        ModeloDAO.MovientosDao movientosDao = new MovientosDao();
        try {
            movientosDao. Realizar_SALIDA_Liquido();
        } catch (ClassNotFoundException ex) {
            System.getLogger(Controlador_Liquido_Salida.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (SQLException ex) {
            System.getLogger(Controlador_Liquido_Salida.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        
         ModeloDAO.InventarioLiquidoDao inventarioLiquidoDao = new InventarioLiquidoDao();
        try {
            inventarioLiquidoDao. CargarComboxBoxLiquido();
        } catch (ClassNotFoundException ex) {
            System.getLogger(Controlador_Liquido_Salida.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (SQLException ex) {
            System.getLogger(Controlador_Liquido_Salida.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        // Dentro del ActionListener del botón Realizar Movimiento
cargarCuentas();
CargaComb();

    }
    
   private  void CargaVista (){
       Funciones.BCV bcv = new BCV();
       
       bcv.mostrarTasaEnCampo(Liquido_Salida.txtTasaDelDolar);
       
       ModeloDAO.InventarioLiquidoDao inventarioLiquidoDao = new InventarioLiquidoDao();
        try {
            inventarioLiquidoDao. CargarComboxBoxLiquido();
        } catch (ClassNotFoundException ex) {
            System.getLogger(Controlador_Liquido_Salida.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (SQLException ex) {
            System.getLogger(Controlador_Liquido_Salida.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
   }
   
   
   private  void CargaComb (){
       
       ModeloDAO.InventarioLiquidoDao inventarioLiquidoDao = new InventarioLiquidoDao();
        try {
            inventarioLiquidoDao. CargarComboxBoxLiquido();
        } catch (ClassNotFoundException ex) {
            System.getLogger(Controlador_Liquido_Salida.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (SQLException ex) {
            System.getLogger(Controlador_Liquido_Salida.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
   }
   
   private  void LimpiarVista (){
       Liquido_Salida.txtPrecioVenta.setText("");
       Liquido_Salida.txtCantidadSalida.setText("");
       Liquido_Salida.txtTotalEnBolivares.setText("");
       Liquido_Salida.txtTotalEnDolares.setText("");
       Liquido_Salida.txtDetalle.setText("");
       Liquido_Salida.TextAreaInformacionProducto.setText("");
   }
   
   
   private  void cargarCuentas (){
       
        try {

            ModeloDAO.CuentaDao cuentaDao = new CuentaDao();
            try {
                cuentaDao.cargarComboCuentasCaja(Liquido_Salida.jComboBoxCuentaPasivo);
            } catch (ClassNotFoundException ex) {
                System.getLogger(Controlador_Liquido_Salida.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            } catch (SQLException ex) {
                System.getLogger(Controlador_Liquido_Salida.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
            cuentaDao.cargarComboCuentascVentas(Liquido_Salida.jComboBoxCuentaIngreso);
        } catch (ClassNotFoundException ex) {
            System.getLogger(Controlador_Liquido_Salida.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (SQLException ex) {
            System.getLogger(Controlador_Liquido_Salida.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
   }
   private boolean listenersConfiguradosSolido = false;
public void CalcularTotalLiquido() {
    // ===============================================================
    // 1. CONFIGURAR LISTENERS (SOLO LA PRIMERA VEZ)
    // ===============================================================
    if (!listenersConfiguradosSolido) {
        DocumentListener listener = new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { CalcularTotalLiquido(); }
            @Override public void removeUpdate(DocumentEvent e) { CalcularTotalLiquido(); }
            @Override public void changedUpdate(DocumentEvent e) { CalcularTotalLiquido(); }
        };

        Liquido_Salida.txtCantidadSalida.getDocument().addDocumentListener(listener);
        Liquido_Salida.txtPrecioVenta.getDocument().addDocumentListener(listener);
      Liquido_Salida.txtTasaDelDolar.getDocument().addDocumentListener(listener);

       Liquido_Salida.txtTotalEnBolivares.setEditable(false);
        Liquido_Salida.txtTotalEnDolares.setEditable(false);

        // INICIA LA CARGA DE LA TASA
      //  BCV.mostrarTasaEnCampo(Gestionar_Almacen.txtDolarHoy);

        listenersConfiguradosSolido = true;
    }

    // ===============================================================
    // 2. LEER CANTIDAD
    // ===============================================================
    double cantidad = 0;
    try {
        String texto = Liquido_Salida.txtCantidadSalida.getText().trim().replace(",", ".");
        if (!texto.isEmpty()) cantidad = Double.parseDouble(texto);
    } catch (Exception e) {}

    // ===============================================================
    // 3. LEER PRECIO EN DÓLARES
    // ===============================================================
    double precioUSD = 0;
    try {
        String texto = Liquido_Salida.txtPrecioVenta.getText().trim().replace(",", ".");
        if (!texto.isEmpty()) precioUSD = Double.parseDouble(texto);
    } catch (Exception e) {}

    // ===============================================================
    // 4. LEER TASA DEL DÓLAR (IGNORA "Cargando...")
    // ===============================================================
    double tasaDolar = 1;
    try {
        String texto = Liquido_Salida.txtTasaDelDolar.getText()
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
    Liquido_Salida.txtTotalEnBolivares.setText("Bs. " + df.format(totalBolivares));
    Liquido_Salida.txtTotalEnDolares.setText("$ " + String.format("%.2f", totalDolares));
}

   
}
