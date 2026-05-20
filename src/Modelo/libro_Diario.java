/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author Adrian
 */
public class libro_Diario {
    
 private int id_libro_diario;
 private  String id_Asiento;
 private  String  idCuenta;
 private  double  deber;
 private  double haber;
 private  String Fecha;

    public libro_Diario(int id_libro_diario, String id_Asiento, String idCuenta, double deber, double haber, String Fecha) {
        this.id_libro_diario = id_libro_diario;
        this.id_Asiento = id_Asiento;
        this.idCuenta = idCuenta;
        this.deber = deber;
        this.haber = haber;
        this.Fecha = Fecha;
    }

   
    
    
    public  libro_Diario (){
        
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String Fecha) {
        this.Fecha = Fecha;
    }
    
    

    public int getId_libro_diario() {
        return id_libro_diario;
    }

    public void setId_libro_diario(int id_libro_diario) {
        this.id_libro_diario = id_libro_diario;
    }

    public double getDeber() {
        return deber;
    }

    public void setDeber(double deber) {
        this.deber = deber;
    }

    public double getHaber() {
        return haber;
    }

    public void setHaber(double haber) {
        this.haber = haber;
    }

    public String getId_Asiento() {
        return id_Asiento;
    }

    public void setId_Asiento(String id_Asiento) {
        this.id_Asiento = id_Asiento;
    }

    public String getIdCuenta() {
        return idCuenta;
    }

    public void setIdCuenta(String idCuenta) {
        this.idCuenta = idCuenta;
    }
    
    
    
    
}
