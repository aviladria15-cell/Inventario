/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author Adrian
 */
public class Producto {
    
 private  int idProducto;
 private  String Nombre;
 private  String Viscosidad;
 private  String Tipo_Liquido;
 private  String presentacion;

 private  String Condicion;
 private  String Compartibilidad;
 private  String Unida_De_Medida;
 
 private  String NumeroSerial;
 private  String Especificaciones;
 private  String idProveedor;
 private  String idCategoria;
 private  String idMarca;
  private  String Densidad;
  
  // metodo para mostralo no es de la clase 
 private  int Cantidad;
 private  int precio_Unitario;
 private  String Fecha_Ingreso;


    public Producto(int idProducto, String Nombre, String Viscosidad, String Tipo_Liquido, String presentacion, String Condicion, String Compartibilidad, String Unida_De_Medida, String NumeroSerial, String Especificaciones, String idProveedor, String idCategoria, String idMarca, int Cantidad, int precio_Unitario, String Fecha_Ingreso, String Densidad) {
        this.idProducto = idProducto;
        this.Nombre = Nombre;
        this.Viscosidad = Viscosidad;
        this.Tipo_Liquido = Tipo_Liquido;
        this.presentacion = presentacion;
        this.Condicion = Condicion;
        this.Compartibilidad = Compartibilidad;
        this.Unida_De_Medida = Unida_De_Medida;
        this.NumeroSerial = NumeroSerial;
        this.Especificaciones = Especificaciones;
        this.idProveedor = idProveedor;
        this.idCategoria = idCategoria;
        this.idMarca = idMarca;
        this.Cantidad = Cantidad;
        this.precio_Unitario = precio_Unitario;
        this.Fecha_Ingreso = Fecha_Ingreso;
        this.Densidad = Densidad;
    }

 
   

   
  
 
 
  public  Producto (){
      
  }

    public String getDensidad() {
        return Densidad;
    }

    public void setDensidad(String Densidad) {
        this.Densidad = Densidad;
    }
  
  

    public int getCantidad() {
        return Cantidad;
    }

    public void setCantidad(int Cantidad) {
        this.Cantidad = Cantidad;
    }

    public int getPrecio_Unitario() {
        return precio_Unitario;
    }

    public void setPrecio_Unitario(int precio_Unitario) {
        this.precio_Unitario = precio_Unitario;
    }

    public String getFecha_Ingreso() {
        return Fecha_Ingreso;
    }

    public void setFecha_Ingreso(String Fecha_Ingreso) {
        this.Fecha_Ingreso = Fecha_Ingreso;
    }

   

    public String getNumeroSerial() {
        return NumeroSerial;
    }

    public void setNumeroSerial(String NumeroSerial) {
        this.NumeroSerial = NumeroSerial;
    }

    public String getEspecificaciones() {
        return Especificaciones;
    }

    public void setEspecificaciones(String Especificaciones) {
        this.Especificaciones = Especificaciones;
    }

  
  
    public String getCondicion() {
        return Condicion;
    }

    public void setCondicion(String Condicion) {
        this.Condicion = Condicion;
    }

    public String getCompartibilidad() {
        return Compartibilidad;
    }

    public void setCompartibilidad(String Compartibilidad) {
        this.Compartibilidad = Compartibilidad;
    }

    public String getUnida_De_Medida() {
        return Unida_De_Medida;
    }

    public void setUnida_De_Medida(String Unida_De_Medida) {
        this.Unida_De_Medida = Unida_De_Medida;
    }

  
  
    public String getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(String idProveedor) {
        this.idProveedor = idProveedor;
    }

    public String getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(String idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getIdMarca() {
        return idMarca;
    }

    public void setIdMarca(String idMarca) {
        this.idMarca = idMarca;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public String getViscosidad() {
        return Viscosidad;
    }

    public void setViscosidad(String Viscosidad) {
        this.Viscosidad = Viscosidad;
    }

    public String getTipo_Liquido() {
        return Tipo_Liquido;
    }

    public void setTipo_Liquido(String Tipo_Liquido) {
        this.Tipo_Liquido = Tipo_Liquido;
    }

    public String getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(String presentacion) {
        this.presentacion = presentacion;
    }


// En tu clase Producto
@Override
public String toString() {
    StringBuilder sb = new StringBuilder();
    
    if (Nombre != null) {
        sb.append(Nombre);
    }

    // Mostrar TODOS los campos disponibles (sin depender de Tipo_Producto)
    if (Compartibilidad != null && !Compartibilidad.isEmpty()) {
        sb.append(" | ").append(Compartibilidad);
    }
    if (Unida_De_Medida != null && !Unida_De_Medida.isEmpty()) {
        sb.append(" | ").append(Unida_De_Medida);
    }
    if (Especificaciones != null && !Especificaciones.isEmpty()) {
        sb.append(" | ").append(Especificaciones);
    }
    if (Condicion != null && !Condicion.isEmpty()) {
        sb.append(" | ").append(Condicion);
    }
    if (Tipo_Liquido != null && !Tipo_Liquido.isEmpty()) {
        sb.append(" | ").append(Tipo_Liquido);
    }
    if (Viscosidad != null && !Viscosidad.isEmpty()) {
        sb.append(" | " ).append(Viscosidad);
    }
    if (Densidad != null && !Densidad.isEmpty()) {
        sb.append("  | " + "Densidad --> ").append(Densidad);
        
    }
    if (presentacion != null && !presentacion.isEmpty()) {
        sb.append(" | ").append(presentacion);
    }
    
    return sb.toString();
}
    
}
