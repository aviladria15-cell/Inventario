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
    private DefaultTableModel modeloCuenta = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column) {
            // Tabla de solo lectura para evitar edición de celdas tipo Excel
            return false;
        }
    };

    // ======================
    // 1. TABLA DE CUENTAS
    // ======================
    public void TituloCuenta() {
        String[] Titulo = {"ID", "Código", "Nombre", "Tipo", "Descripción", "Saldo Actual $ ", "Estado"};
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

    private void limpiarCamposCuenta() {
        Frm_Cuenta.txtCodigoCuenta.setText("");
        Frm_Cuenta.txtNombreCuenta.setText("");
        Frm_Cuenta.txtDescripcion.setText("");
        if(Frm_Cuenta.txtSaldo_inicial != null) {
            Frm_Cuenta.txtSaldo_inicial.setText("");
        }
    }

    // =========================================================================
    // 4. MÉTODOS DE COMBOXES REPARADOS PARA NATURALEZA DEUDORA/ACREEDORA Y MAYÚSCULAS
    // =========================================================================
    
    private ArrayList<cuenta> ListaDeCuentaParaComboxPasivo() throws ClassNotFoundException, SQLException {
        ArrayList<cuenta> Lista = new ArrayList<>();
        // CORREGIDO: Como el tipo ahora es DEUDORA/ACREEDORA, filtramos por tipos válidos y activos
        String sql = "SELECT c.id_cuenta, c.codigo, c.nombre, c.tipo, c.descripcion, COALESCE(SUM(L.saldo_final), c.saldo_inicial) AS saldo_final " +
                     "FROM cuenta c LEFT JOIN libro_mayor L ON L.id_cuenta = c.id_cuenta " +
                     "WHERE c.tipo IN ('DEUDORA', 'ACREEDORA') AND c.activo = true " +
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
            JOptionPane.showMessageDialog(null, "Error al cargar cuentas de balance: " + e.getMessage());
        } finally {
            this.cerrarCn();
        }
        return Lista;
    }
    
    private ArrayList<cuenta> ListaDeCuentaParaComboxInventario() throws ClassNotFoundException, SQLException {
        ArrayList<cuenta> Lista = new ArrayList<>();
        // CORREGIDO: Busqueda exacta en MAYÚSCULAS porque así se guardan en tu base de datos
        String sql = "SELECT c.id_cuenta, c.codigo, c.nombre, c.tipo, c.descripcion, COALESCE(SUM(L.saldo_final), c.saldo_inicial) AS saldo_final " +
                     "FROM cuenta c LEFT JOIN libro_mayor L ON L.id_cuenta = c.id_cuenta " +
                     "WHERE c.nombre = 'INVENTARIO' AND c.activo = true " +
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
            JOptionPane.showMessageDialog(null, "Error al cargar cuenta Inventario: " + e.getMessage());
        } finally {
            this.cerrarCn();
        }
        return Lista;
    }
    
    private ArrayList<cuenta> ListaDeCuentaParaComboxCaja() throws ClassNotFoundException, SQLException {
        ArrayList<cuenta> Lista = new ArrayList<>();
        // CORREGIDO: Búsqueda adaptada a nombres en MAYÚSCULAS
        String sql = "SELECT c.id_cuenta, c.codigo, c.nombre, c.tipo, c.descripcion, COALESCE(SUM(L.saldo_final), c.saldo_inicial) AS saldo_final " +
                     "FROM cuenta c LEFT JOIN libro_mayor L ON L.id_cuenta = c.id_cuenta " +
                     "WHERE c.nombre = 'CAJA' AND c.activo = true " +
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
            JOptionPane.showMessageDialog(null, "Error al cargar cuenta Caja: " + e.getMessage());
        } finally {
            this.cerrarCn();
        }
        return Lista;
    }
    
    
    
    private ArrayList<cuenta> ListaDeCuentaParaComboxVentas() throws ClassNotFoundException, SQLException {
        ArrayList<cuenta> Lista = new ArrayList<>();
        // CORREGIDO: Búsqueda adaptada a nombres en MAYÚSCULAS
        String sql = "SELECT c.id_cuenta, c.codigo, c.nombre, c.tipo, c.descripcion, COALESCE(SUM(L.saldo_final), c.saldo_inicial) AS saldo_final " +
                     "FROM cuenta c LEFT JOIN libro_mayor L ON L.id_cuenta = c.id_cuenta " +
                     "WHERE c.nombre = 'VENTAS' AND c.activo = true " +
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
            JOptionPane.showMessageDialog(null, "Error al cargar cuenta Ventas: " + e.getMessage());
        } finally {
            this.cerrarCn();
        }
        return Lista;
    }
    
    private ArrayList<cuenta> ListaDeCuentaParaComboxAjuste() throws ClassNotFoundException, SQLException {
        ArrayList<cuenta> Lista = new ArrayList<>();
        // CORREGIDO: Búsqueda adaptada a nombres en MAYÚSCULAS
        String sql = "SELECT c.id_cuenta, c.codigo, c.nombre, c.tipo, c.descripcion, COALESCE(SUM(L.saldo_final), c.saldo_inicial) AS saldo_final " +
                     "FROM cuenta c LEFT JOIN libro_mayor L ON L.id_cuenta = c.id_cuenta " +
                     "WHERE c.nombre = 'AJUSTES DE INVENTARIO' AND c.activo = true " +
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
            JOptionPane.showMessageDialog(null, "Error al cargar cuenta Ajustes: " + e.getMessage());
        } finally {
            this.cerrarCn();
        }
        return Lista;
    }
    
    public void cargarComboCuentasPasivo(JComboBox<cuenta> combo) throws ClassNotFoundException, SQLException {
        ArrayList<cuenta> lista =  ListaDeCuentaJerarquicaCompletaPasivo() ;
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
                    setText(c.getNombre() + " --> " + c.getTipo() + "  --> $ " + String.format("%.2f", c.getSaldo_inicial()) + "  -->  "+ c.getDescripcion());
                }
                return this;
            }
        });
    }

    public void cargarComboCuentasInventario(JComboBox<cuenta> combo) throws ClassNotFoundException, SQLException {
        ArrayList<cuenta> lista = ListaDeCuentaJerarquicaCompletaDestino();
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
                    setText(c.getNombre() + " --> " + c.getTipo() + "  --> $ " + String.format("%.2f", c.getSaldo_inicial()) + "  -->  "+ c.getDescripcion());
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

    // =========================================================================
    // 5. SISTEMA EN CASCADA JERÁRQUICO, CRUD Y DESACTIVACIÓN LOGICA
    // =========================================================================

    public void cargarComboPorPadre(JComboBox<cuenta> combo, Integer parentId) throws ClassNotFoundException, SQLException {
        combo.removeAllItems();
        cuenta inicial = new cuenta();
        inicial.setIdCuenta(-1);
        inicial.setNombre("[Seleccione una opción]");
        combo.addItem(inicial);

        String sql;
        if (parentId == null) {
            sql = "SELECT * FROM cuenta WHERE parent_id IS NULL AND activo = true";
        } else {
            sql = "SELECT * FROM cuenta WHERE parent_id = ? AND activo = true";
        }

        try {
            this.conectar();
            ps = this.con.prepareStatement(sql);
            if (parentId != null) {
                ps.setInt(1, parentId);
            }
            rs = ps.executeQuery();
            while (rs.next()) {
                cuenta c = new cuenta();
                c.setIdCuenta(rs.getInt("id_cuenta"));
                c.setCodigo(rs.getString("codigo"));
                c.setNombre(rs.getString("nombre"));
                c.setTipo(rs.getString("tipo"));
                c.setDescripcion(rs.getString("descripcion"));
                c.setSaldo_inicial(rs.getDouble("saldo_inicial"));
                combo.addItem(c);
            }
        } finally {
            this.cerrarCn();
        }
    }

    public void RegistrarCuenta(cuenta nuevaCuenta) throws ClassNotFoundException, SQLException {
        String sql = "INSERT INTO cuenta (codigo, nombre, tipo, descripcion, saldo_inicial, nivel, parent_id, activo) VALUES (?,?,?,?,?,?,?,?)";
        try {
            this.conectar();
            ps = this.con.prepareStatement(sql);
            ps.setString(1, nuevaCuenta.getCodigo());
            ps.setString(2, nuevaCuenta.getNombre().toUpperCase());
            ps.setString(3, nuevaCuenta.getTipo());
            ps.setString(4, nuevaCuenta.getDescripcion());
            ps.setDouble(5, nuevaCuenta.getSaldo_inicial());
            ps.setInt(6, nuevaCuenta.getNivel());
            if (nuevaCuenta.getParent_id() == null || nuevaCuenta.getParent_id() == -1) {
                ps.setNull(7, java.sql.Types.INTEGER);
            } else {
                ps.setInt(7, nuevaCuenta.getParent_id());
            }
            ps.setBoolean(8, nuevaCuenta.isActivo());
            if (ps.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(null, "✅ Cuenta registrada exitosamente.");
                MostrarListaDeCuenta();
                limpiarCamposCuenta();
            }
        } finally {
            this.cerrarCn();
        }
    }

    private ArrayList<cuenta> ListaDeCuentaJerarquicaCompleta() throws ClassNotFoundException, SQLException {
        ArrayList<cuenta> Lista = new ArrayList<>();
        String sql = "SELECT id_cuenta, codigo, nombre, tipo, descripcion, saldo_inicial, activo FROM cuenta ORDER BY codigo ASC";
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
                c.setActivo(rs.getBoolean("activo"));
                Lista.add(c);
            }
        } finally {
            this.cerrarCn();
        }
        return Lista;
    }

    
    
    
    
    private ArrayList<cuenta> ListaDeCuentaJerarquicaCompletaDestino() throws ClassNotFoundException, SQLException {
        ArrayList<cuenta> Lista = new ArrayList<>();
        String sql = "SELECT id_cuenta, codigo, nombre, tipo, descripcion, saldo_inicial  FROM cuenta  where tipo = 'Deudora' ";
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
        } finally {
            this.cerrarCn();
        }
        return Lista;
    }
      private ArrayList<cuenta> ListaDeCuentaJerarquicaCompletaPasivo() throws ClassNotFoundException, SQLException {
        ArrayList<cuenta> Lista = new ArrayList<>();
        String sql = "SELECT id_cuenta, codigo, nombre, tipo, descripcion, saldo_inicial  FROM cuenta  where tipo = 'Acreedora' ";
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
        } finally {
            this.cerrarCn();
        }
        return Lista;
    }

    public void MostrarListaDeCuenta() throws ClassNotFoundException, SQLException {
        TituloCuenta();
        limpiarCuenta();
        ArrayList<cuenta> lista = ListaDeCuentaJerarquicaCompleta();
        Object[] obj = new Object[7];
        for (cuenta c : lista) {
            obj[0] = c.getIdCuenta();
            obj[1] = c.getCodigo();
            obj[2] = c.getNombre();
            obj[3] = c.getTipo();
            obj[4] = c.getDescripcion();
            obj[5] = c.getSaldo_inicial();
            obj[6] = c.isActivo() ? "Activo" : "Inactivo";
            modeloCuenta.addRow(obj);
        }
        Frm_Cuenta.TablaCuentas.setModel(modeloCuenta);
        centrarTextoTabla(Frm_Cuenta.TablaCuentas);
    }

    public void ModificarCuenta(cuenta c) throws ClassNotFoundException, SQLException {
        String sql = "UPDATE cuenta SET nombre = ?, tipo = ?, descripcion = ? WHERE id_cuenta = ?";
        try {
            this.conectar();
            ps = this.con.prepareStatement(sql);
            ps.setString(1, c.getNombre().toUpperCase());
            ps.setString(2, c.getTipo());
            ps.setString(3, c.getDescripcion());
            ps.setInt(4, c.getIdCuenta());
            
            if (ps.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(null, "✅ Cuenta modificada correctamente.");
                MostrarListaDeCuenta();
                limpiarCamposCuenta();
            }
        } finally {
            this.cerrarCn();
        }
    }

    public void CambiarEstadoCuenta(int idCuenta, boolean nuevoEstado) throws ClassNotFoundException, SQLException {
        String sql = "UPDATE cuenta SET activo = ? WHERE id_cuenta = ?";
        try {
            this.conectar();
            ps = this.con.prepareStatement(sql);
            ps.setBoolean(1, nuevoEstado);
            ps.setInt(2, idCuenta);
            
            if (ps.executeUpdate() > 0) {
                String msg = nuevoEstado ? "✅ Cuenta activada exitosamente." : "⚠️ Cuenta desactivada correctamente.";
                JOptionPane.showMessageDialog(null, msg);
            }
        } finally {
            this.cerrarCn();
        }
    }

    /**
     * NUEVO: Consulta la base de datos para contar cuántos hijos tiene el padre
     * y genera el siguiente código correlativo formateado a dos dígitos (ej: .01, .02, .03)
     */
    public String generarSiguienteCodigoHijo(String codigoPadre) throws SQLException {
        if (codigoPadre == null || codigoPadre.trim().isEmpty()) return "";
        
        String sql = "SELECT COUNT(*) FROM cuenta WHERE codigo LIKE ? AND nivel = (SELECT nivel + 1 FROM cuenta WHERE codigo = ? LIMIT 1)";
        try {
            this.conectar();
            ps = this.con.prepareStatement(sql);
            ps.setString(1, codigoPadre + ".%");
            ps.setString(2, codigoPadre);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                int cantidadHijos = rs.getInt(1);
                int siguienteCorrelativo = cantidadHijos + 1;
                String sufijo = String.format("%02d", siguienteCorrelativo);
                return codigoPadre + "." + sufijo;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.cerrarCn();
        }
        return codigoPadre + ".01";
    }
}