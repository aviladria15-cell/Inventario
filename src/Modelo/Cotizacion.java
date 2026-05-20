
package Modelo;

/**
 *
 * @author avila
 */
public class Cotizacion {
 
    private String nombreProducto;
    private String marca;
    private String categoria;
    private int cantidadDisponible;
    private String proveedor;
    private String emailProveedor;
    private String Lote;

    public Cotizacion(String nombreProducto, String marca, String categoria, int cantidadDisponible, String proveedor, String emailProveedor, String Lote) {
        this.nombreProducto = nombreProducto;
        this.marca = marca;
        this.categoria = categoria;
        this.cantidadDisponible = cantidadDisponible;
        this.proveedor = proveedor;
        this.emailProveedor = emailProveedor;
        this.Lote = Lote;
    }

    
    
    
   public  Cotizacion (){
       
   }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public int getCantidadDisponible() {
        return cantidadDisponible;
    }

    public void setCantidadDisponible(int cantidadDisponible) {
        this.cantidadDisponible = cantidadDisponible;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public String getEmailProveedor() {
        return emailProveedor;
    }

    public void setEmailProveedor(String emailProveedor) {
        this.emailProveedor = emailProveedor;
    }

    public String getLote() {
        return Lote;
    }

    public void setLote(String Lote) {
        this.Lote = Lote;
    }


    
}
