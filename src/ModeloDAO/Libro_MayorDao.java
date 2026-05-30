/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ModeloDAO;

import Modelo.ConexiónBD;
import Modelo.libro_mayor;
import com.toedter.calendar.JDateChooser;
import vista_Libro_Contable.frm_LibroMayor;
import java.sql.*;
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
public class Libro_MayorDao extends ConexiónBD {

    private ResultSet rs;
    private PreparedStatement ps;

    DefaultTableModel modeloLibroMayor = new DefaultTableModel();

    public void TituloLibroMayor() {
        String Titulo[] = {
            "ID", "Cuenta", "Fecha",  "Debe", "Haber", "Saldo Final $"
        };
        modeloLibroMayor.setColumnIdentifiers(Titulo);

        if (frm_LibroMayor.TablaLibroMayor != null) {
            frm_LibroMayor.TablaLibroMayor.setModel(modeloLibroMayor);
            centrarTextoTabla(frm_LibroMayor.TablaLibroMayor);
        }
    }

    private void limpiarTablaLibroMayor() {
        modeloLibroMayor.setRowCount(0);
    }

    public void centrarTextoTabla(JTable tabla) {
        DefaultTableCellRenderer centrado = new DefaultTableCellRenderer();
        centrado.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < tabla.getColumnModel().getColumnCount(); i++) {
            tabla.getColumnModel().getColumn(i).setCellRenderer(centrado);
        }
    }

  private ArrayList<libro_mayor> ListaLibroMayor() throws ClassNotFoundException, SQLException {
        ArrayList<libro_mayor> Lista = new ArrayList<>();

        String sql = """
            SELECT 
                c.id_cuenta,
                c.nombre AS nombreCuenta,
                c.saldo_inicial AS saldoActual,
                COALESCE(SUM(l.debe), 0) AS total_debe,
                COALESCE(SUM(l.haber), 0) AS total_haber,
                MAX(l.fecha) AS ultima_fecha
            FROM cuenta c
            LEFT JOIN libro_mayor l ON l.id_cuenta = c.id_cuenta
            GROUP BY c.id_cuenta, c.nombre, c.saldo_inicial
            HAVING COALESCE(SUM(l.debe), 0) > 0 OR COALESCE(SUM(l.haber), 0) > 0 
                OR c.saldo_inicial != 0
            ORDER BY c.nombre ASC
            """;

        try {
            this.conectar();
            ps = this.con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                libro_mayor LM = new libro_mayor();

                LM.setId_Libro_mayor(rs.getInt("id_cuenta"));
                LM.setId_Cuenta(rs.getString("nombreCuenta"));
                LM.setFecha(rs.getString("ultima_fecha"));
                LM.setDebe(rs.getDouble("total_debe"));
                LM.setHaber(rs.getDouble("total_haber"));
                LM.setSaldo_Final(rs.getDouble("saldoActual"));   // ← Saldo REAL de la tabla cuenta

                Lista.add(LM);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, 
                "Error al obtener Libro Mayor:\n" + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            cerrarRecursos();
        }
        return Lista;
    }

    // =========================================================================
    // MOSTRAR EN TABLA
    // =========================================================================
    public void MostrarLibro_Mayor() throws ClassNotFoundException, SQLException {
        TituloLibroMayor();
        limpiarTablaLibroMayor();

        ArrayList<libro_mayor> Lista = ListaLibroMayor();

        Object[] obj = new Object[7];

        for (libro_mayor lm : Lista) {
            obj[0] = lm.getId_Libro_mayor();
            obj[1] = lm.getId_Cuenta();
            obj[2] = lm.getFecha() != null ? lm.getFecha() : "—";
                            // Saldo Anterior (puedes mejorarlo)
            obj[3] = lm.getDebe();
            obj[4] = lm.getHaber();
            obj[5] = lm.getSaldo_Final();     // ← Este es el saldo correcto

            modeloLibroMayor.addRow(obj);
        }

        frm_LibroMayor.TablaLibroMayor.setModel(modeloLibroMayor);
        centrarTextoTabla(frm_LibroMayor.TablaLibroMayor);
    }

    // =========================================================================
    // FILTRO POR FECHA
    // =========================================================================
    public void FiltrarLibroMayorPorFecha(JTable tabla, JDateChooser desde, JDateChooser hasta) {
        limpiarTablaLibroMayor();

        java.util.Date fDesde = desde.getDate();
        java.util.Date fHasta = hasta.getDate();

        if (fDesde == null && fHasta == null) {
            try {
                MostrarLibro_Mayor();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }

        if (fDesde == null || fHasta == null) {
            JOptionPane.showMessageDialog(null, "Seleccione ambas fechas", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String sql = """
            SELECT 
                MIN(l.id_libro_mayor) as id_libro_mayor,
                c.nombre AS nombreCuenta,
                l.fecha,
                SUM(l.debe) AS total_debe,
                SUM(l.haber) AS total_haber,
                MAX(l.saldo_final) AS saldo_final
            FROM libro_mayor l
            INNER JOIN cuenta c ON c.id_cuenta = l.id_cuenta
            WHERE l.fecha BETWEEN ? AND ?
            GROUP BY c.nombre, l.fecha
            ORDER BY c.nombre ASC, l.fecha ASC
            """;

        try {
            this.conectar();
            ps = con.prepareStatement(sql);
            ps.setDate(1, new java.sql.Date(fDesde.getTime()));
            ps.setDate(2, new java.sql.Date(fHasta.getTime()));
            rs = ps.executeQuery();

            Object[] obj = new Object[7];
            while (rs.next()) {
                obj[0] = rs.getInt("id_libro_mayor");
                obj[1] = rs.getString("nombreCuenta");
                obj[2] = rs.getDate("fecha");
                obj[3] = 0.0;
                obj[4] = rs.getDouble("total_debe");
                obj[5] = rs.getDouble("total_haber");
                obj[6] = rs.getDouble("saldo_final");
                modeloLibroMayor.addRow(obj);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al filtrar:\n" + e.getMessage());
        } finally {
            cerrarRecursos();
        }
    }

    private void cerrarRecursos() {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}