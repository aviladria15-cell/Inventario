/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

public class Usuario {
    private int IdUsuario;
    private String NombreUsuario;
    private String Clave;
    private String Nivel_Acceso;
    private  String estado;
    private String Empleado;
  
     public static String usuarioActual;
     
     
public static String nivelActual;

    public Usuario(int IdUsuario, String NombreUsuario, String Clave, String Nivel_Acceso, String estado, String Empleado) {
        this.IdUsuario = IdUsuario;
        this.NombreUsuario = NombreUsuario;
        this.Clave = Clave;
        this.Nivel_Acceso = Nivel_Acceso;
        this.estado = estado;
        this.Empleado = Empleado;
    }

   
    
    

  public  Usuario (){
      
  }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getEmpleado() {
        return Empleado;
    }

    public void setEmpleado(String Empleado) {
        this.Empleado = Empleado;
    }

    public static String getUsuarioActual() {
        return usuarioActual;
    }

    public static void setUsuarioActual(String usuarioActual) {
        Usuario.usuarioActual = usuarioActual;
    }

    public static String getNivelActual() {
        return nivelActual;
    }

    public static void setNivelActual(String nivelActual) {
        Usuario.nivelActual = nivelActual;
    }

  
  
    public int getIdUsuario() {
        return IdUsuario;
    }

    public void setIdUsuario(int IdUsuario) {
        this.IdUsuario = IdUsuario;
    }

    public String getNombreUsuario() {
        return NombreUsuario;
    }

    public void setNombreUsuario(String NombreUsuario) {
        this.NombreUsuario = NombreUsuario;
    }

    public String getClave() {
        return Clave;
    }

    public void setClave(String Clave) {
        this.Clave = Clave;
    }

    public String getNivel_Acceso() {
        return Nivel_Acceso;
    }

    public void setNivel_Acceso(String Nivel_Acceso) {
        this.Nivel_Acceso = Nivel_Acceso;
    }

  
  
  
    
    
}
