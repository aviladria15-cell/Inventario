/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;


public class Movimiento_inventario {
    private int idMovimiento;
    private String idProducto;
    private String tipoMovimiento;
    private int cantidad;
    private String fechaMovimiento;
    private String usuario;
    private String tipoProducto;
    private  String Detalle;
    private  double Precio_Venta;
    private  int  StockFinal;
    private int idCuenta;

    public Movimiento_inventario(int idMovimiento, String idProducto, String tipoMovimiento, int cantidad, String fechaMovimiento, String usuario, String tipoProducto, String Detalle, double Precio_Venta, int StockFinal, int idCuenta) {
        this.idMovimiento = idMovimiento;
        this.idProducto = idProducto;
        this.tipoMovimiento = tipoMovimiento;
        this.cantidad = cantidad;
        this.fechaMovimiento = fechaMovimiento;
        this.usuario = usuario;
        this.tipoProducto = tipoProducto;
        this.Detalle = Detalle;
        this.Precio_Venta = Precio_Venta;
        this.StockFinal = StockFinal;
        this.idCuenta = idCuenta;
    }

    



    public  Movimiento_inventario (){
        
    }

    public int getIdCuenta() {
        return idCuenta;
    }

    public void setIdCuenta(int idCuenta) {
        this.idCuenta = idCuenta;
    }

    
    
    
    public int getStockFinal() {
        return StockFinal;
    }

    public void setStockFinal(int StockFinal) {
        this.StockFinal = StockFinal;
    }
    
    
    
    
    
    public int getIdMovimiento() {
        return idMovimiento;
    }

    public void setIdMovimiento(int idMovimiento) {
        this.idMovimiento = idMovimiento;
    }

    public String getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }

    public String getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(String tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getFechaMovimiento() {
        return fechaMovimiento;
    }

    public void setFechaMovimiento(String fechaMovimiento) {
        this.fechaMovimiento = fechaMovimiento;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getTipoProducto() {
        return tipoProducto;
    }

    public void setTipoProducto(String tipoProducto) {
        this.tipoProducto = tipoProducto;
    }

    public String getDetalle() {
        return Detalle;
    }

    public void setDetalle(String Detalle) {
        this.Detalle = Detalle;
    }

    public double getPrecio_Venta() {
        return Precio_Venta;
    }

    public void setPrecio_Venta(double Precio_Venta) {
        this.Precio_Venta = Precio_Venta;
    }
    
    
}
    
 
