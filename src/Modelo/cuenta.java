/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

public class cuenta {
   
    private int idCuenta;
    private String codigo;
    private String nombre;
    private String tipo;
    private String Descripcion;
    private double saldo_inicial;
   
    // Constructor completo
    public cuenta(int idCuenta, String codigo, String nombre, String tipo, String Descripcion, double saldo_inicial) {
        this.idCuenta = idCuenta;
        this.codigo = codigo;
        this.nombre = nombre;
        this.tipo = tipo;
        this.Descripcion = Descripcion;
        this.saldo_inicial = saldo_inicial;
    }

    public cuenta() {}

    public int getIdCuenta() {
        return idCuenta;
    }

    public void setIdCuenta(int idCuenta) {
        this.idCuenta = idCuenta;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String Descripcion) {
        this.Descripcion = Descripcion;
    }

    public double getSaldo_inicial() {
        return saldo_inicial;
    }

    public void setSaldo_inicial(double saldo_inicial) {
        this.saldo_inicial = saldo_inicial;
    }


    // toString() CLARO, CON SALDO Y CÓDIGO
    @Override
    public String toString() {
        return String.format("%s (%s) - : $%.2f", nombre, codigo,saldo_inicial);
    }
}