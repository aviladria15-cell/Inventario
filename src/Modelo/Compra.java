/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author avila
 */
public class Compra {
    
    private  int id_Compras;
    private  int id_inventario;
    private String numero_factura;
    private   String Proveedor;
    private  String idProducto;
    private  double total_pagar;
    private  double abono;
    private  double debe;
    private  String estado;
    private  int id_cuenta;
    private  String forma_pago;
    private String NombreCuenta;
    private  String CuentaDestino;

    public Compra(int id_Compras, int id_inventario, String numero_factura, String Proveedor, String idProducto, double total_pagar, double abono, double debe, String estado, int id_cuenta, String forma_pago, String NombreCuenta, String CuentaDestino) {
        this.id_Compras = id_Compras;
        this.id_inventario = id_inventario;
        this.numero_factura = numero_factura;
        this.Proveedor = Proveedor;
        this.idProducto = idProducto;
        this.total_pagar = total_pagar;
        this.abono = abono;
        this.debe = debe;
        this.estado = estado;
        this.id_cuenta = id_cuenta;
        this.forma_pago = forma_pago;
        this.NombreCuenta = NombreCuenta;
        this.CuentaDestino = CuentaDestino;
    }

 
   
   
    
    public  Compra (){
        
    }

    public String getCuentaDestino() {
        return CuentaDestino;
    }

    public void setCuentaDestino(String CuentaDestino) {
        this.CuentaDestino = CuentaDestino;
    }

    
    
    public String getNombreCuenta() {
        return NombreCuenta;
    }

    public void setNombreCuenta(String NombreCuenta) {
        this.NombreCuenta = NombreCuenta;
    }

    
    
    
    public String getProveedor() {
        return Proveedor;
    }

    public void setProveedor(String Proveedor) {
        this.Proveedor = Proveedor;
    }

    public String getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }


    
    
    public int getId_Compras() {
        return id_Compras;
    }

    public void setId_Compras(int id_Compras) {
        this.id_Compras = id_Compras;
    }

    public int getId_inventario() {
        return id_inventario;
    }

    public void setId_inventario(int id_inventario) {
        this.id_inventario = id_inventario;
    }

    public String getNumero_factura() {
        return numero_factura;
    }

    public void setNumero_factura(String numero_factura) {
        this.numero_factura = numero_factura;
    }

  
    public double getTotal_pagar() {
        return total_pagar;
    }

    public void setTotal_pagar(double total_pagar) {
        this.total_pagar = total_pagar;
    }

    public double getAbono() {
        return abono;
    }

    public void setAbono(double abono) {
        this.abono = abono;
    }

    public double getDebe() {
        return debe;
    }

    public void setDebe(double debe) {
        this.debe = debe;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getId_cuenta() {
        return id_cuenta;
    }

    public void setId_cuenta(int id_cuenta) {
        this.id_cuenta = id_cuenta;
    }

    public String getForma_pago() {
        return forma_pago;
    }

    public void setForma_pago(String forma_pago) {
        this.forma_pago = forma_pago;
    }
    
    
    
}
