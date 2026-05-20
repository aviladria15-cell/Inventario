/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author Adrian
 */
public class libro_mayor {
    
private  int id_Libro_mayor;
private String id_Cuenta;
private  String Fecha;
private double Saldo_anterior;
private  double debe ;
private  double haber;
private  double saldo_Final;

    public libro_mayor(int id_Libro_mayor, String id_Cuenta, String Fecha, double Saldo_anterior, double debe, double haber, double saldo_Final) {
        this.id_Libro_mayor = id_Libro_mayor;
        this.id_Cuenta = id_Cuenta;
        this.Fecha = Fecha;
        this.Saldo_anterior = Saldo_anterior;
        this.debe = debe;
        this.haber = haber;
        this.saldo_Final = saldo_Final;
    }


    public  libro_mayor (){
        
    }

    public int getId_Libro_mayor() {
        return id_Libro_mayor;
    }

    public void setId_Libro_mayor(int id_Libro_mayor) {
        this.id_Libro_mayor = id_Libro_mayor;
    }

    public  String getId_Cuenta() {
        return id_Cuenta;
    }

    public void setId_Cuenta(   String id_Cuenta) {
        this.id_Cuenta = id_Cuenta;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String Fecha) {
        this.Fecha = Fecha;
    }

    public double  getSaldo_anterior() {
        return Saldo_anterior;
    }

    public void setSaldo_anterior(double Saldo_anterior) {
        this.Saldo_anterior = Saldo_anterior;
    }

    public double getDebe() {
        return debe;
    }

    public void setDebe(double debe) {
        this.debe = debe;
    }

    public double getHaber() {
        return haber;
    }

    public void setHaber(double haber) {
        this.haber = haber;
    }

    public double getSaldo_Final() {
        return saldo_Final;
    }

    public void setSaldo_Final(double saldo_Final) {
        this.saldo_Final = saldo_Final;
    }
    
    
    
    
    
    
    
}
