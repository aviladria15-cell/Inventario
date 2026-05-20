/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;
import java.math.BigDecimal;
public class Inventario {

    private int StockMinimo;
    private int StockMaximo;
    
    private String productos;
    private  int Idinventario;
    private  int Cantidad;
    private  double Precio_Unitario;
    private  String Fecha_Ingreso;
    private  String Lote ;
    private  String Ubicacion;
    private  BigDecimal Costo_Total;
    private  String Cantidad_Disponible;
    private String Fecha_Vencimiento;
    private  String tipo_Producto;
    private  String Usuario;// no es de esta clase es para mostrar en la tabla histotial Movimiento
    private  double Precio_Venta;
    private int  porcentaje;
    private  int idProducto;
    private  int idCuenta;
    private  String idProveedor ;
    private  int Iva;
    private  String Factura;

    public Inventario(int StockMinimo, int StockMaximo, String productos, int Idinventario, int Cantidad, double Precio_Unitario, String Fecha_Ingreso, String Lote, String Ubicacion, BigDecimal Costo_Total, String Cantidad_Disponible, String Fecha_Vencimiento, String tipo_Producto, String Usuario, double Precio_Venta, int porcentaje, int idProducto, int idCuenta, String idProveedor, int Iva, String Factura) {
        this.StockMinimo = StockMinimo;
        this.StockMaximo = StockMaximo;
        this.productos = productos;
        this.Idinventario = Idinventario;
        this.Cantidad = Cantidad;
        this.Precio_Unitario = Precio_Unitario;
        this.Fecha_Ingreso = Fecha_Ingreso;
        this.Lote = Lote;
        this.Ubicacion = Ubicacion;
        this.Costo_Total = Costo_Total;
        this.Cantidad_Disponible = Cantidad_Disponible;
        this.Fecha_Vencimiento = Fecha_Vencimiento;
        this.tipo_Producto = tipo_Producto;
        this.Usuario = Usuario;
        this.Precio_Venta = Precio_Venta;
        this.porcentaje = porcentaje;
        this.idProducto = idProducto;
        this.idCuenta = idCuenta;
        this.idProveedor = idProveedor;
        this.Iva = Iva;
        this.Factura = Factura;
    }

 

   

 
  
    



    public Inventario() {
        
        
        
    }

    public int getIva() {
        return Iva;
    }

    public void setIva(int Iva) {
        this.Iva = Iva;
    }

    public String getFactura() {
        return Factura;
    }

    public void setFactura(String Factura) {
        this.Factura = Factura;
    }
    
    

    public  String getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(String idProveedor) {
        this.idProveedor = idProveedor;
    }
    
    

    public int getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(int porcentaje) {
        this.porcentaje = porcentaje;
    }

    
    
    
    public int getIdCuenta() {
        return idCuenta;
    }

    public void setIdCuenta(int idCuenta) {
        this.idCuenta = idCuenta;
    }
    
    
    
    

    public double getPrecio_Venta() {
        return Precio_Venta;
    }

    public void setPrecio_Venta(double Precio_Venta) {
        this.Precio_Venta = Precio_Venta;
    }
    
    
    
    

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    
    
    
    public String getTipo_Producto() {
        return tipo_Producto;
    }

    public void setTipo_Producto(String tipo_Producto) {
        this.tipo_Producto = tipo_Producto;
    }

    public String getUsuario() {
        return Usuario;
    }

    public void setUsuario(String Usuario) {
        this.Usuario = Usuario;
    }





    public double getPrecio_Unitario() {
        return Precio_Unitario;
    }

    public void setPrecio_Unitario(double Precio_Unitario) {
        this.Precio_Unitario = Precio_Unitario;
    }

    public String getFecha_Ingreso() {
        return Fecha_Ingreso;
    }

    public void setFecha_Ingreso(String Fecha_Ingreso) {
        this.Fecha_Ingreso = Fecha_Ingreso;
    }

    public String getLote() {
        return Lote;
    }

    public void setLote(String Lote) {
        this.Lote = Lote;
    }

    public String getUbicacion() {
        return Ubicacion;
    }

    public void setUbicacion(String Ubicacion) {
        this.Ubicacion = Ubicacion;
    }

    public BigDecimal getCosto_Total() {
        return Costo_Total;
    }

    public void setCosto_Total(BigDecimal Costo_Total) {
        this.Costo_Total = Costo_Total;
    }

    public String getCantidad_Disponible() {
        return Cantidad_Disponible;
    }

    public void setCantidad_Disponible(String Cantidad_Disponible) {
        this.Cantidad_Disponible = Cantidad_Disponible;
    }

   

    
    
    public int getCantidad() {
        return Cantidad;
    }

    public void setCantidad(int Cantidad) {
        this.Cantidad = Cantidad;
    }

    public String getFecha_Vencimiento() {
        return Fecha_Vencimiento;
    }

    public void setFecha_Vencimiento(String Fecha_Vencimiento) {
        this.Fecha_Vencimiento = Fecha_Vencimiento;
    }

 
   

    
    public int getStockMinimo() {
        return StockMinimo;
    }

    public void setStockMinimo(int StockMinimo) {
        this.StockMinimo = StockMinimo;
    }

   
    public int getStockMaximo() {
        return StockMaximo;
    }

    public void setStockMaximo(int StockMaximo) {
        this.StockMaximo = StockMaximo;
    }



    public String getProductos() {
        return productos;
    }

    public void setProductos(String productos) {
        this.productos = productos;
    }

    
    
    @Override
    public String toString() {
        return "Producto -> " + productos + " | StockActual -> " + Cantidad_Disponible + "| Precio Venta -> " + Precio_Venta;
    }

  @Override
public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Inventario)) return false;

    Inventario that = (Inventario) o;
    return Idinventario == that.Idinventario;
}

@Override
public int hashCode() {
    return Integer.hashCode(Idinventario);
}

    public int getIdinventario() {
        return Idinventario;
    }

    public void setIdinventario(int ididProducto) {
        this.Idinventario= ididProducto;
    }
    
    
    
}
