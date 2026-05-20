/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ModeloDAO;


import  Modelo.libro_mayor;
import Modelo.ConexiónBD;
import com.toedter.calendar.JDateChooser;
import vista_Libro_Contable.frm_LibroMayor;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;



/**
 *
 * @author Adrian
 */
public class Libro_MayorDao  extends  ConexiónBD{
    
    
     private ResultSet rs;
    private PreparedStatement ps;
    
    
    libro_mayor Lm = new libro_mayor();
    
    
     DefaultTableModel modeloLibroMayor = new DefaultTableModel();
    
    
    
      
      public void TituloLibroMayor() {
    String Titulo[] = {
        "ID", 
        "Cuenta", 
        "Fecha",
        "Saldo Anterior",
        "Debe", 
        "Haber",
        "Saldo Final $"
    };

    modeloLibroMayor.setColumnIdentifiers(Titulo);

    if (frm_LibroMayor.TablaLibroMayor != null) {
      frm_LibroMayor.TablaLibroMayor.setModel(modeloLibroMayor);

        // 👇 aplicar centrado de datos después de setear el modelo
        centrarTextoTabla(frm_LibroMayor.TablaLibroMayor);
    }
}

      
      
    
    
    private void limpiarTablaLibroMayor (){
 int fila = modeloLibroMayor.getRowCount();
    if (fila > 0) {
        for (int i = 0; i < fila; i++) {
            modeloLibroMayor.removeRow(0);
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
    // =========================================================================
// 1. MÉTODO PARA OBTENER LA LISTA DE TRANSACCIONES DEL LIBRO MAYOR (AGRUPADO)
// =========================================================================
private ArrayList<libro_mayor> ListaLibroMayor() throws ClassNotFoundException, SQLException {
    ArrayList<libro_mayor> Lista = new ArrayList<>();
    
    // CONSULTA CORREGIDA: Agrupa los movimientos por el nombre de la cuenta (c.nombre)
    // y los ordena secuencialmente por el ID del movimiento (l.id_libro_mayor)
    String sql = """
        SELECT 
            l.id_libro_mayor, 
            c.nombre, 
            l.fecha, 
            l.saldo_anterior, 
            l.debe, 
            l.haber, 
            l.saldo_final 
        FROM libro_mayor l 
        INNER JOIN cuenta c ON c.id_cuenta = l.id_cuenta 
        ORDER BY c.nombre ASC, l.id_libro_mayor ASC
        """;
        
    try {
        this.conectar();
        ps = this.con.prepareStatement(sql);
        rs = ps.executeQuery();
        
        while (rs.next()) {
            libro_mayor LM = new libro_mayor();
            
            LM.setId_Libro_mayor(rs.getInt("id_libro_mayor"));
            LM.setId_Cuenta(rs.getString("nombre")); // Aquí guardamos el nombre de la cuenta agrupada
            LM.setFecha(rs.getString("fecha"));
            LM.setSaldo_anterior(rs.getDouble("saldo_anterior"));
            LM.setDebe(rs.getDouble("debe"));
            LM.setHaber(rs.getDouble("haber"));
            LM.setSaldo_Final(rs.getDouble("saldo_final"));
            
            Lista.add(LM);
        }
        
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al obtener la lista del Libro Mayor:\n" + e.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
    } finally {
        // Cierre preventivo de los canales para liberar recursos en MariaDB
        try { 
            if (rs != null) rs.close(); 
            if (ps != null) ps.close(); 
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    return Lista;
}
    
    
// =========================================================================
// 2. MÉTODO PARA PINTAR E IMPRIMIR LOS DATOS EN LA TABLA DE LA INTERFAZ
// =========================================================================
public void MostrarLibro_Mayor() throws ClassNotFoundException, SQLException {
    
    // Inicializa los títulos y limpia filas residuales de la JTable
    TituloLibroMayor();
    limpiarTablaLibroMayor();
    
    // Trae la lista perfectamente ordenada y agrupada por cuentas
    ArrayList<libro_mayor> ListaTerminada = ListaLibroMayor();
    
    // Captura el modelo de tu tabla desde el formulario
    modeloLibroMayor = (DefaultTableModel) frm_LibroMayor.TablaLibroMayor.getModel();
    
    Object[] obj = new Object[7];
    
    // Recorremos la lista usando un bucle For-Each moderno y optimizado
    for (libro_mayor lm : ListaTerminada) {
        
        obj[0] = lm.getId_Libro_mayor();
        obj[1] = lm.getId_Cuenta();       // Nombre de la cuenta (Banesco, Inventario, etc.)
        obj[2] = lm.getFecha();
        obj[3] = lm.getSaldo_anterior();
        obj[4] = lm.getDebe();
        obj[5] = lm.getHaber();
        obj[6] = lm.getSaldo_Final();
        
        // Inserta la fila en el modelo visual
        modeloLibroMayor.addRow(obj);
    }

    // Actualiza los cambios en la JTable de la interfaz gráfica
    frm_LibroMayor.TablaLibroMayor.setModel(modeloLibroMayor);
    
    // Centra el texto de las columnas para mejorar la estética visual
    centrarTextoTabla(frm_LibroMayor.TablaLibroMayor);
}
    
    
    
    
    
 public void FiltrarLibroMayorPorFecha(JTable tabla, JDateChooser desde, JDateChooser hasta) throws SQLException {
    java.util.Date fechaDesdeUtil = desde.getDate();
    java.util.Date fechaHastaUtil = hasta.getDate();

    DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
    modelo.setRowCount(0); // Limpiar siempre

    try {
        this.conectar();

        // CASO 1: Ambas fechas vacías → CARGAR TODO
        if (fechaDesdeUtil == null && fechaHastaUtil == null) {
            String sqlTodo = "SELECT " +
                             "l.id_libro_mayor, " +
                             "c.nombre, " +
                             "l.fecha, " +
                             "l.saldo_anterior, " +
                             "l.debe, " +
                             "l.haber, " +
                             "l.saldo_final " +
                             "FROM libro_mayor l " +
                             "INNER JOIN cuenta c ON c.id_cuenta = l.id_cuenta " +
                             "ORDER BY l.fecha, l.id_libro_mayor";

            ps = this.con.prepareStatement(sqlTodo);
            rs = ps.executeQuery();

            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getInt("id_libro_mayor"),
                    rs.getString("nombre"),
                    rs.getDate("fecha"),
                    rs.getDouble("saldo_anterior"),
                    rs.getDouble("debe"),
                    rs.getDouble("haber"),
                    rs.getDouble("saldo_final")
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
        String sqlFiltro = "SELECT " +
                           "l.id_libro_mayor, " +
                           "c.nombre, " +
                           "l.fecha, " +
                           "l.saldo_anterior, " +
                           "l.debe, " +
                           "l.haber, " +
                           "l.saldo_final " +
                           "FROM libro_mayor l " +
                           "INNER JOIN cuenta c ON c.id_cuenta = l.id_cuenta " +
                           "WHERE l.fecha BETWEEN ? AND ? " +
                           "ORDER BY l.fecha, l.id_libro_mayor";

        ps = this.con.prepareStatement(sqlFiltro);
        ps.setDate(1, fechaDesde);
        ps.setDate(2, fechaHasta);
        rs = ps.executeQuery();

        while (rs.next()) {
            modelo.addRow(new Object[]{
                rs.getInt("id_libro_mayor"),
                rs.getString("nombre"),
                rs.getDate("fecha"),
                rs.getDouble("saldo_anterior"),
                rs.getDouble("debe"),
                rs.getDouble("haber"),
                rs.getDouble("saldo_final")
            });
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, 
            "Error al filtrar Libro Mayor: " + e.getMessage(), 
            "Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    } finally {
        this.cerrarCn();
    }
}
    

    
    
    
}
