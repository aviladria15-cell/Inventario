/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;


public class Proveedor {
    
  private  int idProveedor;
  private  String Nombre;
  private   String Telefono;
  private  String Direccion;
  private  String email;
  private  String RFC;

    public Proveedor(int idProveedor, String Nombre, String Telefono, String Direccion, String email, String RFC) {
        this.idProveedor = idProveedor;
        this.Nombre = Nombre;
        this.Telefono = Telefono;
        this.Direccion = Direccion;
        this.email = email;
        this.RFC = RFC;
    }
  
    
  
  
  public  Proveedor (){
      
  }

    public int getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(int idProveedor) {
        this.idProveedor = idProveedor;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String Telefono) {
        this.Telefono = Telefono;
    }

    public String getDireccion() {
        return Direccion;
    }

    public void setDireccion(String Direccion) {
        this.Direccion = Direccion;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRFC() {
        return RFC;
    }

    public void setRFC(String RFC) {
        this.RFC = RFC;
    }
    
    
    @Override
    
    public  String toString () {
        
     return  Nombre;
        
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Proveedor)) return false;
        Proveedor p = (Proveedor) o;
        return Nombre != null && Nombre.equalsIgnoreCase(p.Nombre);
    }

    @Override
    public int hashCode() {
        return Nombre != null ? Nombre.toLowerCase().hashCode() : 0;
    }
}
