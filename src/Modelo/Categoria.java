/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;


public class Categoria {
    
 private  int idCategoria;
 private  String Nombre;
 // para mostrar en la tabla 
 private String Producto;

    public Categoria(int idCategoria, String Nombre, String Producto) {
        this.idCategoria = idCategoria;
        this.Nombre = Nombre;
        this.Producto = Producto;
    }

  
 
 public  Categoria (){
     
 }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public String getProducto() {
        return Producto;
    }

    public void setProducto(String Producto) {
        this.Producto = Producto;
    }

     @Override
    public String toString() {
        return Nombre;
    }
    
    
    
      @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Categoria)) return false;
        Categoria that = (Categoria) o;
        return Nombre != null && Nombre.equalsIgnoreCase(that.Nombre);
    }

    @Override
    public int hashCode() {
        return Nombre != null ? Nombre.toLowerCase().hashCode() : 0;
    }
    
}
