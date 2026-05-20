/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ModeloDAO;

import Modelo.Asiento_contable;
import Modelo.ConexiónBD;
import com.toedter.calendar.JDateChooser;
import vista_Libro_Contable.frm_AsientoContable;
import java.sql.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;



public class AsientosDao  extends ConexiónBD {
    
     private ResultSet rs;
    private PreparedStatement ps;
    
    
    Asiento_contable As = new  Asiento_contable();
    
    
     DefaultTableModel modeloAsientos = new DefaultTableModel();
    
    
    
      
      public void TituloLibroContable() {
    String Titulo[] = {
        "ID", 
        "Fecha", 
        "Descripcion",
        "Referencia",
        "Total Debe", 
        "Total Haber"
    };

    modeloAsientos.setColumnIdentifiers(Titulo);

    if (frm_AsientoContable.TablaAsiento != null) {
      frm_AsientoContable.TablaAsiento.setModel(modeloAsientos);

        // 👇 aplicar centrado de datos después de setear el modelo
        centrarTextoTabla(frm_AsientoContable.TablaAsiento);
    }
}

      
      
    
    
    private void limpiarTablAsiento (){
 int fila = modeloAsientos.getRowCount();
    if (fila > 0) {
        for (int i = 0; i < fila; i++) {
            modeloAsientos.removeRow(0);
        }
   
    }
    
}
    
    
    public void centrarTextoTabla(JTable tabla) {
    DefaultTableCellRenderer centrado = new DefaultTableCellRenderer();
    centrado.setHorizontalAlignment(SwingConstants.CENTER); // mejor usar SwingConstants

    // Recorremos todas las columnas visibles
    for (int i = 0; i < tabla.getColumnModel().getColumnCount(); i++) {
        tabla.getColumnModel().getColumn(i).setCellRenderer(centrado);
    }
}

    
    public ArrayList<Asiento_contable> ListaASIENToContable() throws ClassNotFoundException, SQLException {
    ArrayList<Asiento_contable> Lista = new ArrayList<>();

    String sql = "SELECT id_asiento, fecha, descripcion, referencia, total_debe, total_haber " +
                 "FROM asiento_contable " +
                 "ORDER BY fecha, id_asiento";  // ORDENADO

    try {
        this.conectar();
        ps = this.con.prepareStatement(sql);
        rs = ps.executeQuery();

        while (rs.next()) {
            Asiento_contable At = new Asiento_contable();
            At.setId_Asiento(rs.getInt("id_asiento"));
            At.setFecha(rs.getString("fecha"));           // Mejor: Date, no String
            At.setDescripcion(rs.getString("descripcion"));
            At.setReferencia(rs.getString("referencia"));
            At.setTotal_Debe(rs.getDouble("total_debe"));
            At.setTotal_haber(rs.getDouble("total_haber"));
            Lista.add(At);
        }

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, 
            "Error al obtener asientos: " + e.getMessage());
        this.cerrarCn();
    }

    return Lista;
}
    
    
    
    public  void MostrarAsientosContables() throws ClassNotFoundException, SQLException{
        
       TituloLibroContable();
       limpiarTablAsiento();
       
       ArrayList<Asiento_contable> ListaTerminada = ListaASIENToContable();
       
       modeloAsientos = (DefaultTableModel) frm_AsientoContable.TablaAsiento.getModel();
       
       Object [] obj =  new  Object [6];
        
        for (int i = 0; i < ListaTerminada.size(); i++) {
            
            obj [0] = ListaTerminada.get(i).getId_Asiento();
            obj [1] = ListaTerminada.get(i).getFecha();
            obj [2] = ListaTerminada.get(i).getDescripcion();
            obj [3] = ListaTerminada.get(i).getReferencia();
            obj [4] = ListaTerminada.get(i).getTotal_Debe();
            obj [5] = ListaTerminada.get(i).getTotal_haber();
            
            modeloAsientos.addRow(obj);
            
        }
        
        
        frm_AsientoContable.TablaAsiento.setModel(modeloAsientos);
        centrarTextoTabla(frm_AsientoContable.TablaAsiento);
        
        
        
        
        
    }
    
    
    
    
  public void FiltrarAsientoContablePorFecha(JTable tabla, JDateChooser desde, JDateChooser hasta) throws SQLException {
    java.util.Date fechaDesdeUtil = desde.getDate();
    java.util.Date fechaHastaUtil = hasta.getDate();

    DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
    modelo.setRowCount(0); // Limpiar tabla

    try {
        this.conectar();

        // CASO 1: Ambas fechas vacías → CARGAR TODO
        if (fechaDesdeUtil == null && fechaHastaUtil == null) {
            String sqlTodo = "SELECT id_asiento, fecha, descripcion, referencia, total_debe, total_haber " +
                             "FROM asiento_contable " +
                             "ORDER BY fecha, id_asiento";

            ps = this.con.prepareStatement(sqlTodo);
            rs = ps.executeQuery();

            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getInt("id_asiento"),
                    rs.getDate("fecha"),
                    rs.getString("descripcion"),
                    rs.getString("referencia"),
                    rs.getDouble("total_debe"),
                    rs.getDouble("total_haber")
                });
            }
            return;
        }

        // CASO 2: Validar ambas fechas
        if (fechaDesdeUtil == null || fechaHastaUtil == null) {
            JOptionPane.showMessageDialog(null, 
                "Por favor selecciona ambas fechas para filtrar.", 
                "Fechas incompletas", JOptionPane.WARNING_MESSAGE);
            return;
        }

        java.sql.Date fechaDesde = new java.sql.Date(fechaDesdeUtil.getTime());
        java.sql.Date fechaHasta = new java.sql.Date(fechaHastaUtil.getTime());

        if (fechaDesde.after(fechaHasta)) {
            JOptionPane.showMessageDialog(null, 
                "La fecha 'Desde' no puede ser mayor que 'Hasta'.", 
                "Rango inválido", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // CASO 3: FILTRAR POR RANGO
        String sqlFiltro = "SELECT id_asiento, fecha, descripcion, referencia, total_debe, total_haber " +
                           "FROM asiento_contable " +
                           "WHERE fecha BETWEEN ? AND ? " +
                           "ORDER BY fecha, id_asiento";

        ps = this.con.prepareStatement(sqlFiltro);
        ps.setDate(1, fechaDesde);
        ps.setDate(2, fechaHasta);
        rs = ps.executeQuery();

        while (rs.next()) {
            modelo.addRow(new Object[]{
                rs.getInt("id_asiento"),
                rs.getDate("fecha"),
                rs.getString("descripcion"),
                rs.getString("referencia"),
                rs.getDouble("total_debe"),
                rs.getDouble("total_haber")
            });
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, 
            "Error al filtrar asientos: " + e.getMessage(), 
            "Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    } finally {
        this.cerrarCn();
    }
}
    
    
    
    
    
    
}
