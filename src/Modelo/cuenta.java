
 package Modelo;

public class cuenta {
   
    private int idCuenta;
    private String codigo;
    private String nombre;
    private String tipo;
    private String Descripcion;
    private double saldo_inicial;
    private int nivel;          // Nivel jerárquico (1 al 6) según el PCP 2024
    private Integer parent_id;  // ID de la cuenta padre (permite nulos para niveles raíz)
    private boolean activo;
   
    // Constructor completo actualizado
    public cuenta(int idCuenta, String codigo, String nombre, String tipo, String Descripcion, double saldo_inicial, int nivel, Integer parent_id, boolean activo) {
        this.idCuenta = idCuenta;
        this.codigo = codigo;
        this.nombre = nombre;
        this.tipo = tipo;
        this.Descripcion = Descripcion;
        this.saldo_inicial = saldo_inicial;
        this.nivel = nivel;
        this.parent_id = parent_id;
        this.activo = activo;
    }

    // Constructor vacío
    public cuenta() {
     this.activo = true; // Por defecto toda cuenta nueva nace activa
    }

    // Getters y Setters
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

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }
    
   
    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public Integer getParent_id() {
        return parent_id;
    }

    public void setParent_id(Integer parent_id) {
        this.parent_id = parent_id;
    }

    /**
     * Sobrescribimos el método toString() para que al meter los objetos 'cuenta'
     * dentro de los JComboBox de la interfaz visual, se despliegue únicamente el 
     * código junto con el nombre de la cuenta contable de forma limpia.
     */
    @Override
    public String toString() {
        return this.codigo + " - " + this.nombre;
    }
}