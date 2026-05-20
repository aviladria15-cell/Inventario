/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author Adrian
 */
public class Asiento_contable {
    
  private  int id_Asiento;
  private  String Fecha;
  private  String Descripcion;
  private  String Referencia;
  private  double total_Debe ;
  private  double total_haber;

    public Asiento_contable(int id_Asiento, String Fecha, String Descripcion, String Referencia, double total_Debe, double total_haber) {
        this.id_Asiento = id_Asiento;
        this.Fecha = Fecha;
        this.Descripcion = Descripcion;
        this.Referencia = Referencia;
        this.total_Debe = total_Debe;
        this.total_haber = total_haber;
    }
  
  
    
    public  Asiento_contable (){
        
    }

    public int getId_Asiento() {
        return id_Asiento;
    }

    public void setId_Asiento(int id_Asiento) {
        this.id_Asiento = id_Asiento;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String Fecha) {
        this.Fecha = Fecha;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String Descripcion) {
        this.Descripcion = Descripcion;
    }

    public String getReferencia() {
        return Referencia;
    }

    public void setReferencia(String Referencia) {
        this.Referencia = Referencia;
    }

    public double getTotal_Debe() {
        return total_Debe;
    }

    public void setTotal_Debe(double total_Debe) {
        this.total_Debe = total_Debe;
    }

    public double getTotal_haber() {
        return total_haber;
    }

    public void setTotal_haber(double total_haber) {
        this.total_haber = total_haber;
    }
    
    
    
    
    
}
