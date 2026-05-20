 
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

public class Almacen {
    
    private int id_Ubicacion;
    private String Pasillo;
    private String Estante;
    private int Capacidad;
    private int Nivel;
    private String Ala;
    // no es de esta clase pero es para la tabla 
    private String Producto;
    private String Stock;

    // Constructor completo
    public Almacen(int id_Ubicacion, String Pasillo, String Estante, int Capacidad, int Nivel, String Ala, String Producto, String Stock) {
        this.id_Ubicacion = id_Ubicacion;
        this.Pasillo = Pasillo;
        this.Estante = Estante;
        this.Capacidad = Capacidad;
        this.Nivel = Nivel;
        this.Ala = Ala;
        this.Producto = Producto;
        this.Stock = Stock;
    }

    // Constructor vacío
    public Almacen() {
    }

    // Getters y Setters
    public int getId_Ubicacion() {
        return id_Ubicacion;
    }

    public void setId_Ubicacion(int id_Ubicacion) {
        this.id_Ubicacion = id_Ubicacion;
    }

    public String getPasillo() {
        return Pasillo;
    }

    public void setPasillo(String Pasillo) {
        this.Pasillo = Pasillo;
    }

    public String getEstante() {
        return Estante;
    }

    public void setEstante(String Estante) {
        this.Estante = Estante;
    }

    public int getCapacidad() {
        return Capacidad;
    }

    public void setCapacidad(int Capacidad) {
        this.Capacidad = Capacidad;
    }

    public int getNivel() {
        return Nivel;
    }

    public void setNivel(int Nivel) {
        this.Nivel = Nivel;
    }

    public String getAla() {
        return Ala;
    }

    public void setAla(String Ala) {
        this.Ala = Ala;
    }

    public String getProducto() {
        return Producto;
    }

    public void setProducto(String Producto) {
        this.Producto = Producto;
    }

    public String getStock() {
        return Stock;
    }

    public void setStock(String Stock) {
        this.Stock = Stock;
    }

   @Override
public String toString() {
    StringBuilder sb = new StringBuilder();
    
    if (Ala != null && !Ala.trim().isEmpty())
        sb.append("Ala: ").append(Ala).append(" | ");
    
    if (Pasillo != null && !Pasillo.trim().isEmpty())
        sb.append("").append(Pasillo).append(" | ");
    
    if (Estante != null && !Estante.trim().isEmpty())
        sb.append("Estante ").append(Estante).append(" | ");
    
    if (Nivel != 0)  // o según cómo guardes el nivel
        sb.append("Nivel ").append(Nivel);
    
    if (Capacidad != 0)
        sb.append(" | Cap: ").append(Capacidad);
    
    String resultado = sb.toString().replaceAll(" \\| $", "");
    return resultado.isEmpty() ? "Sin ubicación" : resultado;
}
}
