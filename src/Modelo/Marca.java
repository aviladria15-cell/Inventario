/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author Adrian
 */
public class Marca {
    
    private  int idMarca;
    public  String Nombre;
    private String PaisOrigen;
    
    // metodo para mostrar en la tabla
    private  String Producto;
    
    // metodo para mostrarla en la tabla
    private String categoria;

    public Marca(int idMarca, String Nombre, String PaisOrigen, String Producto, String categoria) {
        this.idMarca = idMarca;
        this.Nombre = Nombre;
        this.PaisOrigen = PaisOrigen;
        this.Producto = Producto;
        this.categoria = categoria;
    }

    
    
    
    public  Marca (){
        
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    
    

    public int getIdMarca() {
        return idMarca;
    }

    public void setIdMarca(int idMarca) {
        this.idMarca = idMarca;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public String getPaisOrigen() {
        return PaisOrigen;
    }

    public void setPaisOrigen(String PaisOrigen) {
        this.PaisOrigen = PaisOrigen;
    }

    public String getProducto() {
        return Producto;
    }

    public void setProducto(String Producto) {
        this.Producto = Producto;
    }
    
    
    
    
@Override
public String toString() {
    return this.Nombre; // Se mostrará en el ComboBox
}

@Override
public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Marca)) return false;
    Marca marca = (Marca) o;
    return Nombre != null && Nombre.equalsIgnoreCase(marca.Nombre);
}

@Override
public int hashCode() {
    return Nombre != null ? Nombre.toLowerCase().hashCode() : 0;
}
}
