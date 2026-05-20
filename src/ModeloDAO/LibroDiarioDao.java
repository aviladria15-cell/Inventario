/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ModeloDAO;

import  Modelo.libro_Diario;
import Modelo.ConexiónBD;
import com.toedter.calendar.JDateChooser;
import java.sql.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import  vista_Libro_Contable.frm_libroDiario;


public class LibroDiarioDao extends  ConexiónBD{

    private ResultSet rs;
    private PreparedStatement ps;
    
   libro_Diario LD = new  libro_Diario();
    
    DefaultTableModel modeLibroDiario = new DefaultTableModel();
      
      
      
      public void TituloLibroContable() {
    String Titulo[] = {
        "ID", 
        "Fecha", 
        "Cuenta",
        "Debe", 
        "Haber",
        "Descripcion"
       
       
    };

    modeLibroDiario.setColumnIdentifiers(Titulo);

    if (frm_libroDiario.TablaLibroDiario != null) {
        frm_libroDiario.TablaLibroDiario.setModel(modeLibroDiario);

        // 👇 aplicar centrado de datos después de setear el modelo
        centrarTextoTabla(frm_libroDiario.TablaLibroDiario);
    }
}

      
      
    
    
    private void limpiarLibroDiario (){
 int fila = modeLibroDiario.getRowCount();
    if (fila > 0) {
        for (int i = 0; i < fila; i++) {
            modeLibroDiario.removeRow(0);
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

public ArrayList<libro_Diario> ListaLibroDiario() throws ClassNotFoundException, SQLException {
    ArrayList<libro_Diario> Lista = new ArrayList<>();

    String sql = "SELECT " +
                 "l.id_libro_diario, " +
                 "a.Fecha, " +
                 "c.nombre, " +
                 "l.debe, " +
                 "l.haber, " +
                 "a.descripcion " +
                 "FROM libro_diario l " +
                 "INNER JOIN asiento_contable a ON a.id_asiento = l.id_asiento " +
                 "INNER JOIN cuenta c ON c.id_cuenta = l.id_cuenta " +
                 "ORDER BY a.Fecha, l.id_libro_diario";  // ORDEN CORRECTO

    try {
        this.conectar();
        ps = this.con.prepareCall(sql);
        rs = ps.executeQuery();

        while (rs.next()) {
            libro_Diario Ll = new libro_Diario();
            Ll.setId_libro_diario(rs.getInt("id_libro_diario"));
            Ll.setId_Asiento(rs.getString("Fecha"));           // Fecha como String
            Ll.setIdCuenta(rs.getString("nombre"));
            Ll.setDeber(rs.getDouble("debe"));
            Ll.setHaber(rs.getDouble("haber"));
            Ll.setFecha(rs.getString("descripcion"));

            Lista.add(Ll);
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, 
            "Error al obtener el libro diario: " + e.getMessage(), 
            "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    } finally {
        this.cerrarCn();
    }

    return Lista;
}
    
    
    
   
   
    
    public  void MostrarListaLibroContable () throws ClassNotFoundException, SQLException{
        
        TituloLibroContable();
        limpiarLibroDiario();
        
        ArrayList<libro_Diario> ListaTerminada = ListaLibroDiario();
        
        modeLibroDiario = (DefaultTableModel) frm_libroDiario.TablaLibroDiario.getModel();
        
        Object [] obj = new  Object[6];
        
        for (int i = 0; i < ListaTerminada.size(); i++) {
            
         obj [0] = ListaTerminada.get(i).getId_libro_diario();
         obj [1] = ListaTerminada.get(i).getId_Asiento();
         obj [2] = ListaTerminada.get(i).getIdCuenta();
         obj [3] = ListaTerminada.get(i).getDeber();
         obj [4] = ListaTerminada.get(i).getHaber();
         obj [5] = ListaTerminada.get(i).getFecha();
         
         modeLibroDiario.addRow(obj);
            
            
        }
        
        frm_libroDiario.TablaLibroDiario.setModel(modeLibroDiario);
        centrarTextoTabla(frm_libroDiario.TablaLibroDiario);
        
        
        
        
        
        
        
    }
    
    
    
    
    
 public void FiltrarLibroDiarioPorFecha(JTable tabla, JDateChooser desde, JDateChooser hasta) throws SQLException {
    java.util.Date fechaDesdeUtil = desde.getDate();
    java.util.Date fechaHastaUtil = hasta.getDate();

    DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
    modelo.setRowCount(0); // Siempre limpiar

    try {
        this.conectar();

        // CASO 1: Ambas fechas están vacías → CARGAR TODO
        if (fechaDesdeUtil == null && fechaHastaUtil == null) {
            String sqlTodo = "SELECT l.id_libro_diario, a.Fecha, c.nombre, l.debe, l.haber, a.descripcion " +
                             "FROM libro_diario l " +
                             "INNER JOIN asiento_contable a ON a.id_asiento = l.id_asiento " +
                             "INNER JOIN cuenta c ON c.id_cuenta = l.id_cuenta " +
                             "ORDER BY a.Fecha, l.id_libro_diario";

            ps = this.con.prepareStatement(sqlTodo);
            rs = ps.executeQuery();

            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getInt("id_libro_diario"),
                    rs.getDate("Fecha"),
                    rs.getString("nombre"),
                    rs.getDouble("debe"),
                    rs.getDouble("haber"),
                    rs.getString("descripcion")
                });
            }
            return; // Salir: ya se cargó todo
        }

        // CASO 2: Al menos una fecha tiene valor → VALIDAR Y FILTRAR
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

        // FILTRAR POR RANGO
        String sqlFiltro = "SELECT l.id_libro_diario, a.Fecha, c.nombre, l.debe, l.haber, a.descripcion " +
                           "FROM libro_diario l " +
                           "INNER JOIN asiento_contable a ON a.id_asiento = l.id_asiento " +
                           "INNER JOIN cuenta c ON c.id_cuenta = l.id_cuenta " +
                           "WHERE a.Fecha BETWEEN ? AND ? " +
                           "ORDER BY a.Fecha, l.id_libro_diario";

        ps = this.con.prepareStatement(sqlFiltro);
        ps.setDate(1, fechaDesde);
        ps.setDate(2, fechaHasta);
        rs = ps.executeQuery();

        while (rs.next()) {
            modelo.addRow(new Object[]{
                rs.getInt("id_libro_diario"),
                rs.getDate("Fecha"),
                rs.getString("nombre"),
                rs.getDouble("debe"),
                rs.getDouble("haber"),
                rs.getString("descripcion")
            });
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, 
            "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    } finally {
        this.cerrarCn();
    }
}
    
}
