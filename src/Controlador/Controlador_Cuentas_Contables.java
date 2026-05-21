package Controlador;

import Modelo.cuenta;
import ModeloDAO.CuentaDao;
import Vista_Usuari_Empleado.Menu_Sistema;
import vista_Libro_Contable.Frm_Cuenta;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;

public class Controlador_Cuentas_Contables implements ActionListener {

    private final Menu_Sistema menu;
    private Frm_Cuenta Cuenta_Contables;
    private CuentaDao cuentaDao = new CuentaDao();
    
    // Variables de control de estado para el CRUD (Modificación y Desactivación)
    private int idCuentaSeleccionada = -1;
    private boolean modoEdicion = false;
    private boolean modificandoCombos = false; // Bandera protectora crítica contra bucles de eventos

    public Controlador_Cuentas_Contables(Menu_Sistema menu) {
        this.menu = menu;
    }

    /**
     * Inicializa y despliega la ventana configurando todos sus componentes
     */
    public void MostrarVista() {
        if (Cuenta_Contables == null) {
            Cuenta_Contables = new Frm_Cuenta();
            AgregarListeners();
        }
        
        // Limpieza total y reactivación jerárquica limpia al entrar
        LimpiarFormularioLimpiamente();
        Cuenta_Contables.setVisible(true);
        
        try {
            cuentaDao.TituloCuenta();
            cuentaDao.MostrarListaDeCuenta();
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
        menu.setVisible(false);
    }

    private void AgregarListeners() {
        Cuenta_Contables.btbVolverMenu.addActionListener(this);
        Cuenta_Contables.btbAgregarCuenta.addActionListener(this);

        Cuenta_Contables.jComboBoxGrupo.addActionListener(this);
        Cuenta_Contables.jComboBoxSubgrupo.addActionListener(this);
        Cuenta_Contables.jComboBoxRubro.addActionListener(this);
        Cuenta_Contables.jComboBoxCuentaN4.addActionListener(this);
        Cuenta_Contables.jComboBoxSubcuentaN5.addActionListener(this);
        Cuenta_Contables.jComboBoxSubcuentaN6.addActionListener(this);

        // Escuchador del doble clic para edición
        Cuenta_Contables.TablaCuentas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    CargarDatosParaEditar();
                }
            }
        });

        // =====================================================================
        // MENÚ POPUP FLOTANTE (CLIC DERECHO ORIGINAL CONSERVADO)
        // =====================================================================
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem itemCambiarEstado = new JMenuItem("Cambiar Estado (Activar / Desactivar)");
        popupMenu.add(itemCambiarEstado);
        Cuenta_Contables.TablaCuentas.setComponentPopupMenu(popupMenu);
        itemCambiarEstado.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EjecutarCambioEstadoLgico();
            }
        });
    }

    private void inicializarCombosJerarquicos() {
        try {
            // Carga el Nivel 1 (Grupos) usando tu método nativo de CuentaDao
            cuentaDao.cargarComboPorPadre(Cuenta_Contables.jComboBoxGrupo, null);
            
            // Forzar apagado y limpieza absoluta de los niveles inferiores en cascada
            BloquearNivelesHijos(Cuenta_Contables.jComboBoxSubgrupo);
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Bloqueo de seguridad: si el sistema está operando los combos internamente, ignora el evento
        if (modificandoCombos) return;

        Object origen = e.getSource();

        if (origen == Cuenta_Contables.btbVolverMenu) {
            VolverMenu();
        } else if (origen == Cuenta_Contables.btbAgregarCuenta) {
            ProcesarGuardadoCuenta();
        } 
        else if (origen == Cuenta_Contables.jComboBoxGrupo) {
            try {
                procesarCambioDeFiltro(Cuenta_Contables.jComboBoxGrupo, Cuenta_Contables.jComboBoxSubgrupo);
            } catch (SQLException ex) {
                System.getLogger(Controlador_Cuentas_Contables.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        } 
        else if (origen == Cuenta_Contables.jComboBoxSubgrupo) {
            try {
                procesarCambioDeFiltro(Cuenta_Contables.jComboBoxSubgrupo, Cuenta_Contables.jComboBoxRubro);
            } catch (SQLException ex) {
                System.getLogger(Controlador_Cuentas_Contables.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        } 
        else if (origen == Cuenta_Contables.jComboBoxRubro) {
            try {
                procesarCambioDeFiltro(Cuenta_Contables.jComboBoxRubro, Cuenta_Contables.jComboBoxCuentaN4);
            } catch (SQLException ex) {
                System.getLogger(Controlador_Cuentas_Contables.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        } 
        else if (origen == Cuenta_Contables.jComboBoxCuentaN4) {
            try {
                procesarCambioDeFiltro(Cuenta_Contables.jComboBoxCuentaN4, Cuenta_Contables.jComboBoxSubcuentaN5);
            } catch (SQLException ex) {
                System.getLogger(Controlador_Cuentas_Contables.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        } 
        else if (origen == Cuenta_Contables.jComboBoxSubcuentaN5) {
            try {
                procesarCambioDeFiltro(Cuenta_Contables.jComboBoxSubcuentaN5, Cuenta_Contables.jComboBoxSubcuentaN6);
            } catch (SQLException ex) {
                System.getLogger(Controlador_Cuentas_Contables.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        } 
        else if (origen == Cuenta_Contables.jComboBoxSubcuentaN6) {
            try {
                actualizarTipoYCodigoPorSeleccion(Cuenta_Contables.jComboBoxSubcuentaN6);
            } catch (SQLException ex) {
                System.getLogger(Controlador_Cuentas_Contables.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        }
    }

    /**
     * Maneja el flujo de selección jerárquica conectándose a tu CuentaDao
     */
    private void procesarCambioDeFiltro(JComboBox comboOrigen, JComboBox comboDestino) throws SQLException {
        Object seleccionado = comboOrigen.getSelectedItem();
        
        if (seleccionado instanceof cuenta) {
            cuenta c = (cuenta) seleccionado;
            
            if (c.getIdCuenta() == -1) {
                EjecutarLimpiezaPorSeleccionInvalida(comboDestino);
                recalcularCodigoSugerido();
                return;
            }
            
            // Seleccionar saldo por coincidencia estricta de texto
            seleccionarTipoSaldoEnCombo(c.getTipo());
            
            try {
                modificandoCombos = true; 
                cuentaDao.cargarComboPorPadre(comboDestino, c.getIdCuenta());
                modificandoCombos = false;
                
                comboDestino.setEnabled(comboDestino.getItemCount() > 1);
            } catch (ClassNotFoundException | SQLException ex) {
                modificandoCombos = false;
                ex.printStackTrace();
            }
        } else {
            EjecutarLimpiezaPorSeleccionInvalida(comboDestino);
        }
        
        // Control estricto en cascada hacia abajo para limpiar los siguientes niveles profundos
        if (comboDestino == Cuenta_Contables.jComboBoxSubgrupo) BloquearNivelesHijos(Cuenta_Contables.jComboBoxRubro);
        else if (comboDestino == Cuenta_Contables.jComboBoxRubro) BloquearNivelesHijos(Cuenta_Contables.jComboBoxCuentaN4);
        else if (comboDestino == Cuenta_Contables.jComboBoxCuentaN4) BloquearNivelesHijos(Cuenta_Contables.jComboBoxSubcuentaN5);
        else if (comboDestino == Cuenta_Contables.jComboBoxSubcuentaN5) BloquearNivelesHijos(Cuenta_Contables.jComboBoxSubcuentaN6);
        
        recalcularCodigoSugerido();
    }

    private void EjecutarLimpiezaPorSeleccionInvalida(JComboBox comboDestino) {
        modificandoCombos = true;
        comboDestino.removeAllItems();
        comboDestino.setEnabled(false);
        modificandoCombos = false;
    }

    private void actualizarTipoYCodigoPorSeleccion(JComboBox comboFinal) throws SQLException {
        Object selected = comboFinal.getSelectedItem();
        if (selected instanceof cuenta && ((cuenta) selected).getIdCuenta() != -1) {
            seleccionarTipoSaldoEnCombo(((cuenta) selected).getTipo());
        }
        recalcularCodigoSugerido();
    }

    /**
     * Busca y selecciona el tipo de saldo de forma inteligente mapeando el objeto cuenta
     */
    private void seleccionarTipoSaldoEnCombo(String tipo) {
        if (tipo == null || tipo.trim().isEmpty()) return;
        
        String limpioBD = tipo.trim().toLowerCase();
        int items = Cuenta_Contables.jComboBoxTipoCuenta.getItemCount();
        
        for (int i = 0; i < items; i++) {
            Object itemObj = Cuenta_Contables.jComboBoxTipoCuenta.getItemAt(i);
            if (itemObj instanceof cuenta) {
                String itemStr = ((cuenta) itemObj).getNombre().trim().toLowerCase();
                if (itemStr.equals(limpioBD)) {
                    Cuenta_Contables.jComboBoxTipoCuenta.setSelectedIndex(i);
                    return;
                }
            }
        }
    }

    /**
     * Limpia y apaga de forma segura los componentes hijos bloqueando hilos cruzados
     */
    private void BloquearNivelesHijos(JComboBox comboDesde) {
        modificandoCombos = true; 
        
        if (comboDesde == Cuenta_Contables.jComboBoxSubgrupo) {
            Cuenta_Contables.jComboBoxSubgrupo.removeAllItems();
            Cuenta_Contables.jComboBoxSubgrupo.setEnabled(false);
        }
        if (comboDesde == Cuenta_Contables.jComboBoxSubgrupo || comboDesde == Cuenta_Contables.jComboBoxRubro) {
            Cuenta_Contables.jComboBoxRubro.removeAllItems();
            Cuenta_Contables.jComboBoxRubro.setEnabled(false);
        }
        if (comboDesde == Cuenta_Contables.jComboBoxSubgrupo || comboDesde == Cuenta_Contables.jComboBoxRubro || comboDesde == Cuenta_Contables.jComboBoxCuentaN4) {
            Cuenta_Contables.jComboBoxCuentaN4.removeAllItems();
            Cuenta_Contables.jComboBoxCuentaN4.setEnabled(false); 
        }
        if (comboDesde != Cuenta_Contables.jComboBoxSubcuentaN6) {
            Cuenta_Contables.jComboBoxSubcuentaN5.removeAllItems();
            Cuenta_Contables.jComboBoxSubcuentaN5.setEnabled(false);
            Cuenta_Contables.jComboBoxSubcuentaN6.removeAllItems();
            Cuenta_Contables.jComboBoxSubcuentaN6.setEnabled(false);
        }
        
        modificandoCombos = false; 
    }

   private void recalcularCodigoSugerido() throws SQLException {
        if (modoEdicion) return;
        
        String codigoCalculado = "";
        JComboBox[] ordenCombos = {
            Cuenta_Contables.jComboBoxGrupo,
            Cuenta_Contables.jComboBoxSubgrupo,
            Cuenta_Contables.jComboBoxRubro,
            Cuenta_Contables.jComboBoxCuentaN4,
            Cuenta_Contables.jComboBoxSubcuentaN5,
            Cuenta_Contables.jComboBoxSubcuentaN6
        };

        // Recorremos los combos de arriba a abajo para encontrar la última cuenta seleccionada
        for (JComboBox cb : ordenCombos) {
            Object item = cb.getSelectedItem();
            if (item instanceof cuenta && ((cuenta) item).getIdCuenta() != -1) {
                codigoCalculado = ((cuenta) item).getCodigo();
            }
        }
        
        // Si encontramos una cuenta seleccionada, calculamos el código de su nuevo hijo de forma automática
        if (!codigoCalculado.isEmpty()) {
            String codigoSiguienteHijo = cuentaDao.generarSiguienteCodigoHijo(codigoCalculado);
            Cuenta_Contables.txtCodigoCuenta.setText(codigoSiguienteHijo);
        } else {
            Cuenta_Contables.txtCodigoCuenta.setText("");
        }
    }

    private void CargarDatosParaEditar() {
        int fila = Cuenta_Contables.TablaCuentas.getSelectedRow();
        if (fila >= 0) {
            modoEdicion = true;
            
            idCuentaSeleccionada = Integer.parseInt(Cuenta_Contables.TablaCuentas.getValueAt(fila, 0).toString());
            String codigo = Cuenta_Contables.TablaCuentas.getValueAt(fila, 1).toString();
            String nombre = Cuenta_Contables.TablaCuentas.getValueAt(fila, 2).toString();
            String tipo = Cuenta_Contables.TablaCuentas.getValueAt(fila, 3).toString();
            String descripcion = Cuenta_Contables.TablaCuentas.getValueAt(fila, 4).toString();
            
            Cuenta_Contables.txtCodigoCuenta.setText(codigo);
            Cuenta_Contables.txtNombreCuenta.setText(nombre);
            
            // Asegura rellenar las opciones antes de intentar seleccionar
            rellenarOpcionesTipoSaldo();
            seleccionarTipoSaldoEnCombo(tipo); 
            
            Cuenta_Contables.txtDescripcion.setText(descripcion);
            
            if (Cuenta_Contables.txtSaldo_inicial != null) {
                Cuenta_Contables.txtSaldo_inicial.setEnabled(false); 
            }
            
            HabilitarArbolJerarquico(false);
            Cuenta_Contables.btbAgregarCuenta.setText("Guardar Cambios");
        }
    }

    private void ProcesarGuardadoCuenta() {
        if (Cuenta_Contables.txtNombreCuenta.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(Cuenta_Contables, "⚠️ El nombre de la cuenta es obligatorio.");
            return;
        }
        if (Cuenta_Contables.jComboBoxTipoCuenta.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(Cuenta_Contables, "⚠️ Debe seleccionar un Tipo de Saldo.");
            return;
        }

        try {
            cuenta c = new cuenta();
            c.setNombre(Cuenta_Contables.txtNombreCuenta.getText().trim());
            
            // CORREGIDO: Al guardar, extrae el texto del objeto cuenta del combo
            Object selectedTipo = Cuenta_Contables.jComboBoxTipoCuenta.getSelectedItem();
            if (selectedTipo instanceof cuenta) {
                c.setTipo(((cuenta) selectedTipo).getNombre());
            } else {
                c.setTipo(selectedTipo.toString());
            }
            
            c.setDescripcion(Cuenta_Contables.txtDescripcion.getText().trim());

            if (modoEdicion) {
                c.setIdCuenta(idCuentaSeleccionada);
                cuentaDao.ModificarCuenta(c);
            } else {
                c.setCodigo(Cuenta_Contables.txtCodigoCuenta.getText().trim());
                double saldo = 0.0;
                if (Cuenta_Contables.txtSaldo_inicial != null && !Cuenta_Contables.txtSaldo_inicial.getText().trim().isEmpty()) {
                    try {
                        saldo = Double.parseDouble(Cuenta_Contables.txtSaldo_inicial.getText().trim());
                    } catch (NumberFormatException nfe) {
                        saldo = 0.0;
                    }
                }
                c.setSaldo_inicial(saldo);
                c.setNivel(obtenerNivelCalculado());
                c.setParent_id(obtenerParentIdSeleccionado());
                c.setActivo(true); 
                
                cuentaDao.RegistrarCuenta(c);
            }
            
            cuentaDao.MostrarListaDeCuenta();
            LimpiarFormularioLimpiamente();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(Cuenta_Contables, "Error operacional: " + ex.getMessage());
        }
    }

    private void EjecutarCambioEstadoLgico() {
        int fila = Cuenta_Contables.TablaCuentas.getSelectedRow();
        if (fila >= 0) {
            int id = Integer.parseInt(Cuenta_Contables.TablaCuentas.getValueAt(fila, 0).toString());
            int columnaEstado = Cuenta_Contables.TablaCuentas.getColumnCount() - 1; 
            String estadoVisual = Cuenta_Contables.TablaCuentas.getValueAt(fila, columnaEstado).toString();
            boolean nuevoEstadoBoolean = !estadoVisual.equalsIgnoreCase("Activo");
            
            int confirmacion = JOptionPane.showConfirmDialog(Cuenta_Contables, 
                    "¿Seguro que desea cambiar el estado operacional de esta cuenta?", 
                    "Confirmación", JOptionPane.YES_NO_OPTION);
            if (confirmacion == JOptionPane.YES_OPTION) {
                try {
                    cuentaDao.CambiarEstadoCuenta(id, nuevoEstadoBoolean);
                    cuentaDao.MostrarListaDeCuenta();
                    LimpiarFormularioLimpiamente();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(Cuenta_Contables, "Error al alterar estado: " + ex.getMessage());
                }
            }
        }
    }

    private Integer obtenerParentIdSeleccionado() {
        JComboBox[] combos = {
            Cuenta_Contables.jComboBoxSubcuentaN6,
            Cuenta_Contables.jComboBoxSubcuentaN5,
            Cuenta_Contables.jComboBoxCuentaN4,
            Cuenta_Contables.jComboBoxRubro,
            Cuenta_Contables.jComboBoxSubgrupo,
            Cuenta_Contables.jComboBoxGrupo
        };

        for (JComboBox combo : combos) {
            Object selected = combo.getSelectedItem();
            if (selected instanceof cuenta && ((cuenta) selected).getIdCuenta() != -1) {
                return ((cuenta) selected).getIdCuenta();
            }
        }
        return null;
    }

    private int obtenerNivelCalculado() {
        int nivel = 1;
        if (validarSeleccionItem(Cuenta_Contables.jComboBoxGrupo)) nivel = 2;
        if (validarSeleccionItem(Cuenta_Contables.jComboBoxSubgrupo)) nivel = 3;
        if (validarSeleccionItem(Cuenta_Contables.jComboBoxRubro)) nivel = 4;
        if (validarSeleccionItem(Cuenta_Contables.jComboBoxCuentaN4)) nivel = 5;
        if (validarSeleccionItem(Cuenta_Contables.jComboBoxSubcuentaN5)) nivel = 6;
        if (validarSeleccionItem(Cuenta_Contables.jComboBoxSubcuentaN6)) nivel = 7;
        return nivel;
    }

    private boolean validarSeleccionItem(JComboBox combo) {
        Object item = combo.getSelectedItem();
        return (item instanceof cuenta && ((cuenta) item).getIdCuenta() != -1);
    }

    private void HabilitarArbolJerarquico(boolean switchEstado) {
        Cuenta_Contables.jComboBoxGrupo.setEnabled(switchEstado);
        Cuenta_Contables.jComboBoxSubgrupo.setEnabled(switchEstado);
        Cuenta_Contables.jComboBoxRubro.setEnabled(switchEstado);
        Cuenta_Contables.jComboBoxCuentaN4.setEnabled(switchEstado);
        Cuenta_Contables.jComboBoxSubcuentaN5.setEnabled(switchEstado);
        Cuenta_Contables.jComboBoxSubcuentaN6.setEnabled(switchEstado);
    }

    /**
     * CORREGIDO: Envuelve los strings en objetos de tipo 'cuenta' para que compile perfectamente con tu modelo
     */
    private void rellenarOpcionesTipoSaldo() {
        Cuenta_Contables.jComboBoxTipoCuenta.removeAllItems();
        
        cuenta cDeudora = new cuenta();
        cDeudora.setIdCuenta(-1);
        cDeudora.setNombre("Deudora");
        
        cuenta cAcreedora = new cuenta();
        cAcreedora.setIdCuenta(-1);
        cAcreedora.setNombre("Acreedora");
        
        Cuenta_Contables.jComboBoxTipoCuenta.addItem(cDeudora);
        Cuenta_Contables.jComboBoxTipoCuenta.addItem(cAcreedora);
    }

    private void LimpiarFormularioLimpiamente() {
        modificandoCombos = true;
        
        Cuenta_Contables.txtCodigoCuenta.setText("");
        Cuenta_Contables.txtNombreCuenta.setText("");
        Cuenta_Contables.txtDescripcion.setText("");
        if (Cuenta_Contables.txtSaldo_inicial != null) {
            Cuenta_Contables.txtSaldo_inicial.setText("0.0");
            Cuenta_Contables.txtSaldo_inicial.setEnabled(true);
        }
        
        // Forzamos la inyección limpia de los objetos cuenta en el combo
        rellenarOpcionesTipoSaldo();
        Cuenta_Contables.jComboBoxTipoCuenta.setSelectedIndex(-1); 
        
        modoEdicion = false;
        idCuentaSeleccionada = -1;
        Cuenta_Contables.btbAgregarCuenta.setText("Agregar Cuenta");
        
        modificandoCombos = false;
        
        HabilitarArbolJerarquico(true);
        inicializarCombosJerarquicos();
    }

    private void VolverMenu() {
        LimpiarFormularioLimpiamente(); 
        menu.setVisible(true);
        Cuenta_Contables.setVisible(false);
    }
}