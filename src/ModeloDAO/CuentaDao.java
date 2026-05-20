/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ModeloDAO;

import Modelo.cuenta;
import Modelo.ConexiónBD;
import java.sql.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import vista_Libro_Contable.Frm_Cuenta;


import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JList;

public class CuentaDao extends ConexiónBD {

    private ResultSet rs;
    private PreparedStatement ps;
    private cuenta Cu = new cuenta();
    private DefaultTableModel modeloCuenta = new DefaultTableModel();

    // ======================
    // 1. TABLA DE CUENTAS
    // ======================
    public void TituloCuenta() {
        String[] Titulo = {"ID", "Código", "Nombre", "Tipo", "Descripción", "Saldo Actual $ "};
        modeloCuenta.setColumnIdentifiers(Titulo);
        if (Frm_Cuenta.TablaCuentas != null) {
            Frm_Cuenta.TablaCuentas.setModel(modeloCuenta);
            centrarTextoTabla(Frm_Cuenta.TablaCuentas);
        }
    }

    private void limpiarCuenta() {
        int fila = modeloCuenta.getRowCount();
        for (int i = 0; i < fila; i++) {
            modeloCuenta.removeRow(0);
        }
    }

    public void centrarTextoTabla(JTable tabla) {
        DefaultTableCellRenderer centrado = new DefaultTableCellRenderer();
        centrado.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < tabla.getColumnModel().getColumnCount(); i++) {
            tabla.getColumnModel().getColumn(i).setCellRenderer(centrado);
        }
    }

    // ======================
    // 2. REGISTRAR CUENTA
    // ======================
    public void RegistrarCuenta() throws ClassNotFoundException, SQLException {
        Cu.setCodigo(Frm_Cuenta.txtCodigoCuenta.getText());
        Cu.setNombre(Frm_Cuenta.txtNombreCuenta.getText());
        Cu.setDescripcion(Frm_Cuenta.txtDescripcion.getText());
        Cu.setSaldo_inicial(Double.parseDouble(Frm_Cuenta.txtSaldo_Inicial.getText()));
        Cu.setTipo(Frm_Cuenta.jComboBoxTipoCuenta.getSelectedItem().toString());

        String sql = "INSERT INTO cuenta (codigo, nombre, tipo, descripcion, saldo_inicial) VALUES (?,?,?,?,?)";
        try {
            this.conectar();
            ps = this.con.prepareStatement(sql);
            ps.setString(1, Cu.getCodigo());
            ps.setString(2, Cu.getNombre());
            ps.setString(3, Cu.getTipo());
            ps.setString(4, Cu.getDescripcion());
            ps.setDouble(5, Cu.getSaldo_inicial());

            if (ps.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(null, "Cuenta registrada correctamente");
                MostrarListaDeCuenta();
                limpiarCamposCuenta();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al registrar: " + e.getMessage());
        } finally {
            this.cerrarCn();
        }
    }

    private void limpiarCamposCuenta() {
        Frm_Cuenta.txtCodigoCuenta.setText("");
        Frm_Cuenta.txtNombreCuenta.setText("");
        Frm_Cuenta.txtDescripcion.setText("");
        Frm_Cuenta.txtSaldo_Inicial.setText("");
    }

    // ======================
    // 3. LISTA DE CUENTAS
    // ======================
    private ArrayList<cuenta> ListaDeCuenta() throws ClassNotFoundException, SQLException {
        ArrayList<cuenta> Lista = new ArrayList<>();
     String sql = "SELECT c.id_cuenta, " +
             "c.codigo, " +
             "c.nombre, " +
             "c.tipo, " +
             "c.descripcion, " +
             "c.saldo_inicial " +
             "FROM cuenta c " +
             "GROUP BY c.id_cuenta, c.codigo, c.nombre, c.tipo, c.descripcion";

        try {
            this.conectar();
            ps = this.con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                cuenta c = new cuenta();
                c.setIdCuenta(rs.getInt("id_cuenta"));
                c.setCodigo(rs.getString("codigo"));
                c.setNombre(rs.getString("nombre"));
                c.setTipo(rs.getString("tipo"));
                c.setDescripcion(rs.getString("descripcion"));
                c.setSaldo_inicial(rs.getDouble("saldo_inicial"));
                Lista.add(c);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar cuentas: " + e.getMessage());
        } finally {
            this.cerrarCn();
        }
        return Lista;
    }

    public void MostrarListaDeCuenta() throws ClassNotFoundException, SQLException {
        TituloCuenta();
        limpiarCuenta();
        ArrayList<cuenta> lista = ListaDeCuenta();
        Object[] obj = new Object[6];
        for (cuenta c : lista) {
            obj[0] = c.getIdCuenta();
            obj[1] = c.getCodigo();
            obj[2] = c.getNombre();
            obj[3] = c.getTipo();
            obj[4] = c.getDescripcion();
            obj[5] = c.getSaldo_inicial();
            modeloCuenta.addRow(obj);
        }
        Frm_Cuenta.TablaCuentas.setModel(modeloCuenta);
        centrarTextoTabla(Frm_Cuenta.TablaCuentas);
    }

    // ======================
    // 4. MÉTODO ÚNICO: CARGAR COMBO EN CUALQUIER FORMULARIO
    // ======================
    
     private ArrayList<cuenta> ListaDeCuentaParaComboxPasivo() throws ClassNotFoundException, SQLException {
        ArrayList<cuenta> Lista = new ArrayList<>();
  String sql = "SELECT c.id_cuenta, " +
             "c.codigo, " +
             "c.nombre, " +
             "c.tipo, " +
             "c.descripcion, " +
             "SUM(L.saldo_final) AS saldo_final " +
             "FROM cuenta c " +
             "INNER JOIN libro_mayor L ON L.id_cuenta = c.id_cuenta " +
             "WHERE c.tipo IN ('ACTIVO', 'PATRIMONIO', 'PASIVO') " +
             "GROUP BY c.id_cuenta, c.codigo, c.nombre, c.tipo, c.descripcion";
        try {
            this.conectar();
            ps = this.con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                cuenta c = new cuenta();
                c.setIdCuenta(rs.getInt("id_cuenta"));
                c.setCodigo(rs.getString("codigo"));
                c.setNombre(rs.getString("nombre"));
                c.setTipo(rs.getString("tipo"));
                c.setDescripcion(rs.getString("descripcion"));
                c.setSaldo_inicial(rs.getDouble("saldo_final"));
                Lista.add(c);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar cuentas: " + e.getMessage());
        } finally {
            this.cerrarCn();
        }
        return Lista;
    }
    
    private ArrayList<cuenta> ListaDeCuentaParaComboxInventario() throws ClassNotFoundException, SQLException {
        ArrayList<cuenta> Lista = new ArrayList<>();
  String sql = "SELECT c.id_cuenta, " +
             "c.codigo, " +
             "c.nombre, " +
             "c.tipo, " +
             "c.descripcion, " +
             "SUM(L.saldo_final) AS saldo_final " +
             "FROM cuenta c " +
             "INNER JOIN libro_mayor L ON L.id_cuenta = c.id_cuenta " +
             "WHERE c.nombre = 'Inventario' " +
             "GROUP BY c.id_cuenta, c.codigo, c.nombre, c.tipo, c.descripcion";
        try {
            this.conectar();
            ps = this.con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                cuenta c = new cuenta();
                c.setIdCuenta(rs.getInt("id_cuenta"));
                c.setCodigo(rs.getString("codigo"));
                c.setNombre(rs.getString("nombre"));
                c.setTipo(rs.getString("tipo"));
                c.setDescripcion(rs.getString("descripcion"));
                c.setSaldo_inicial(rs.getDouble("saldo_final"));
                Lista.add(c);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar cuentas: " + e.getMessage());
        } finally {
            this.cerrarCn();
        }
        return Lista;
    }
    
    private ArrayList<cuenta> ListaDeCuentaParaComboxCaja() throws ClassNotFoundException, SQLException {
        ArrayList<cuenta> Lista = new ArrayList<>();
    String sql = "SELECT c.id_cuenta, " +
             "c.codigo, " +
             "c.nombre, " +
             "c.tipo, " +
             "c.descripcion, " +
             "SUM(L.saldo_final) AS saldo_final " +
             "FROM cuenta c " +
             "INNER JOIN libro_mayor L ON L.id_cuenta = c.id_cuenta " +
             "WHERE c.nombre = 'Caja' " +
             "GROUP BY c.id_cuenta, c.codigo, c.nombre, c.tipo, c.descripcion";
        try {
            this.conectar();
            ps = this.con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                cuenta c = new cuenta();
                c.setIdCuenta(rs.getInt("id_cuenta"));
                c.setCodigo(rs.getString("codigo"));
                c.setNombre(rs.getString("nombre"));
                c.setTipo(rs.getString("tipo"));
                c.setDescripcion(rs.getString("descripcion"));
                c.setSaldo_inicial(rs.getDouble("saldo_final"));
                Lista.add(c);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar cuentas: " + e.getMessage());
        } finally {
            this.cerrarCn();
        }
        return Lista;
    }
    
       
    private ArrayList<cuenta> ListaDeCuentaParaComboxVentas() throws ClassNotFoundException, SQLException {
        ArrayList<cuenta> Lista = new ArrayList<>();
    String sql = "SELECT c.id_cuenta, " +
             "c.codigo, " +
             "c.nombre, " +
             "c.tipo, " +
             "c.descripcion, " +
             "SUM(L.saldo_final) AS saldo_final " +
             "FROM cuenta c " +
             "INNER JOIN libro_mayor L ON L.id_cuenta = c.id_cuenta " +
             "WHERE c.nombre = 'Ventas' " +
             "GROUP BY c.id_cuenta, c.codigo, c.nombre, c.tipo, c.descripcion";
        try {
            this.conectar();
            ps = this.con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                cuenta c = new cuenta();
                c.setIdCuenta(rs.getInt("id_cuenta"));
                c.setCodigo(rs.getString("codigo"));
                c.setNombre(rs.getString("nombre"));
                c.setTipo(rs.getString("tipo"));
                c.setDescripcion(rs.getString("descripcion"));
                c.setSaldo_inicial(rs.getDouble("saldo_final"));
                Lista.add(c);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar cuentas: " + e.getMessage());
        } finally {
            this.cerrarCn();
        }
        return Lista;
    }
    
       private ArrayList<cuenta> ListaDeCuentaParaComboxAjuste() throws ClassNotFoundException, SQLException {
        ArrayList<cuenta> Lista = new ArrayList<>();
   String sql = "SELECT c.id_cuenta, " +
             "c.codigo, " +
             "c.nombre, " +
             "c.tipo, " +
             "c.descripcion, " +
             "COALESCE(SUM(L.saldo_final), 0) AS saldo_final " +
             "FROM cuenta c " +
             "LEFT JOIN libro_mayor L ON L.id_cuenta = c.id_cuenta " +
             "WHERE c.nombre = 'Ajustes de Inventario' " +
             "GROUP BY c.id_cuenta, c.codigo, c.nombre, c.tipo, c.descripcion";
        try {
            this.conectar();
            ps = this.con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                cuenta c = new cuenta();
                c.setIdCuenta(rs.getInt("id_cuenta"));
                c.setCodigo(rs.getString("codigo"));
                c.setNombre(rs.getString("nombre"));
                c.setTipo(rs.getString("tipo"));
                c.setDescripcion(rs.getString("descripcion"));
                c.setSaldo_inicial(rs.getDouble("saldo_final"));
                Lista.add(c);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar cuentas: " + e.getMessage());
        } finally {
            this.cerrarCn();
        }
        return Lista;
    }
    
     
     
     
     
    public void cargarComboCuentasPasivo(JComboBox<cuenta> combo) throws ClassNotFoundException, SQLException {
        ArrayList<cuenta> lista = ListaDeCuentaParaComboxPasivo();
        combo.removeAllItems();
        for (cuenta c : lista) {
            combo.addItem(c);
        }
        combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof cuenta) {
                    cuenta c = (cuenta) value;
                    setText(c.getNombre() + " --> " + c.getTipo() + "  --> $ " + String.format("%.2f", c.getSaldo_inicial()));
                }
                return this;
            }
        });
    }
      public void cargarComboCuentasInventarioo(JComboBox<cuenta> combo) throws ClassNotFoundException, SQLException {
        ArrayList<cuenta> lista =  ListaDeCuenta();
        combo.removeAllItems();
        for (cuenta c : lista) {
            combo.addItem(c);
        }
        combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof cuenta) {
                    cuenta c = (cuenta) value;
                    setText(c.getNombre() + " --> " + c.getTipo() + "  --> $ "   + String.format("%.2f", c.getSaldo_inicial()) + "  -->  "+ c.getDescripcion());
                }
                return this;
            }
        });
    }
    
    
    
    
    
    
    public void cargarComboCuentasCaja(JComboBox<cuenta> combo) throws ClassNotFoundException, SQLException {
        ArrayList<cuenta> lista = ListaDeCuentaParaComboxCaja();
        combo.removeAllItems();
        for (cuenta c : lista) {
            combo.addItem(c);
        }
        combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof cuenta) {
                    cuenta c = (cuenta) value;
                    setText(c.getNombre() + " --> " + c.getTipo() + " Saldo --> $ " + String.format("%.2f", c.getSaldo_inicial()));
                }
                return this;
            }
        });
    }
    
    
    
    
       public void cargarComboCuentascVentas(JComboBox<cuenta> combo) throws ClassNotFoundException, SQLException {
        ArrayList<cuenta> lista = ListaDeCuentaParaComboxVentas();
        combo.removeAllItems();
        for (cuenta c : lista) {
            combo.addItem(c);
        }
        combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof cuenta) {
                    cuenta c = (cuenta) value;
                    setText(c.getNombre() + " --> " + c.getTipo() + " Saldo --> $ " + String.format("%.2f", c.getSaldo_inicial()));
                }
                return this;
            }
        });
    }
    
    
    
    
    
    
    
       public void cargarComboCuentascInventario(JComboBox<cuenta> combo) throws ClassNotFoundException, SQLException {
        ArrayList<cuenta> lista = ListaDeCuentaParaComboxAjuste();
        combo.removeAllItems();
        for (cuenta c : lista) {
            combo.addItem(c);
        }
        combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof cuenta) {
                    cuenta c = (cuenta) value;
                    setText(c.getNombre() + " --> " + c.getTipo() + "  --> $ " + String.format("%.2f", c.getSaldo_inicial()));
                }
                return this;
            }
        });
    }
    
    
    
    
    
    

    // ======================
    // 5. CARGAR COMBO EN CADA FORMULARIO (REUTILIZANDO EL MÉTODO)
    // ======================

    
    
    
    


    
    
}