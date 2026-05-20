 /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.util.Date;

import Modelo.Usuario;
public class Empleado {
    
    private int IdEmpleado;
    private String Nombre;
    private String Apellido;
    private String Telefono;
    private String email;
    private String Cedula;
    private Date FechaNacimiento;
    private String Cargo;
    private  String usuario;

    public Empleado(int IdEmpleado, String Nombre, String Apellido, String Telefono, String email, String Cedula, Date FechaNacimiento, String Cargo, String usuario) {
        this.IdEmpleado = IdEmpleado;
        this.Nombre = Nombre;
        this.Apellido = Apellido;
        this.Telefono = Telefono;
        this.email = email;
        this.Cedula = Cedula;
        this.FechaNacimiento = FechaNacimiento;
        this.Cargo = Cargo;
        this.usuario = usuario;
    }

   

    
    
  

   

   


public Empleado (){
    
}

    public int getIdEmpleado() {
        return IdEmpleado;
    }

    public void setIdEmpleado(int IdEmpleado) {
        this.IdEmpleado = IdEmpleado;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public String getApellido() {
        return Apellido;
    }

    public void setApellido(String Apellido) {
        this.Apellido = Apellido;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String Telefono) {
        this.Telefono = Telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCedula() {
        return Cedula;
    }

    public void setCedula(String Cedula) {
        this.Cedula = Cedula;
    }

   

    public String getCargo() {
        return Cargo;
    }

    public void setCargo(String Cargo) {
        this.Cargo = Cargo;
    }

    public Date getFechaNacimiento() {
        return FechaNacimiento;
    }

    public void setFechaNacimiento(Date FechaNacimiento) {
        this.FechaNacimiento = FechaNacimiento;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

  

 
 

  

  
    
    
    
}
