package Controlador;

import Validacion.Validar_Formulario_Empleado;
import Vista_Usuari_Empleado.Menu_Sistema;
import Vista_Usuari_Empleado.frm_GestionarEmpleado;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * Controlador para el módulo de Gestión de Empleados
 */
public class Controlador_Empleado implements ActionListener {

    private final Menu_Sistema menu;
    private frm_GestionarEmpleado vistaEmpleado;

    // Controlador hijo para la asignación de usuario
    private ControladorAsignarUsuario controladorAsignarUsuario;

    public Controlador_Empleado(Menu_Sistema menu) {
        this.menu = menu;
    }

    /**
     * Muestra la ventana de Gestionar Empleado (Lazy Initialization)
     */
    public void mostrarVista() {
        if (vistaEmpleado == null) {
            try {
                vistaEmpleado = new frm_GestionarEmpleado();
                agregarListeners();
            } catch (SQLException | ClassNotFoundException ex) {
                Logger.getLogger(Controlador_Empleado.class.getName())
                      .log(Level.SEVERE, "Error al crear frm_GestionarEmpleado", ex);
            }
        }

        vistaEmpleado.setVisible(true);
        menu.setVisible(false);
    }

    /**
     * Agrega los listeners a todos los botones
     */
    private void agregarListeners() {
        vistaEmpleado.btbCerrar.addActionListener(this);
        vistaEmpleado.btbRegistrar.addActionListener(this);
        vistaEmpleado.btbActualizarEmpleado.addActionListener(this);
        vistaEmpleado.btbCancelar.addActionListener(this);
        vistaEmpleado.btbConfirmarActualizacion.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == vistaEmpleado.btbCerrar) {
            volverAlMenu();
        }
        else if (e.getSource() == vistaEmpleado.btbRegistrar) {
            registrarEmpleado();
        }
        else if (e.getSource() == vistaEmpleado.btbActualizarEmpleado) {
            prepararActualizacion();
        }
        else if (e.getSource() == vistaEmpleado.btbCancelar) {
            cancelarActualizacion();
        }
        else if (e.getSource() == vistaEmpleado.btbConfirmarActualizacion) {
            confirmarActualizacion();
        }
    }

    // ====================== MÉTODOS PRIVADOS ======================

    private void volverAlMenu() {
        vistaEmpleado.setVisible(false);
        menu.setVisible(true);
    }

    private void registrarEmpleado() {
        Validar_Formulario_Empleado validador = new Validar_Formulario_Empleado();

        try {
            boolean guardadoCorrecto = validador.RegistrarEmpleadoValidado();

            if (guardadoCorrecto) {
                abrirAsignacionDeUsuario();
            }

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Controlador_Empleado.class.getName())
                  .log(Level.SEVERE, "Error al registrar empleado", ex);
        }
    }

    /**
     * Abre la ventana para asignar usuario después de registrar un empleado
     */
    private void abrirAsignacionDeUsuario() {
        if (controladorAsignarUsuario == null) {
            controladorAsignarUsuario = new ControladorAsignarUsuario(menu, vistaEmpleado);
        }
        controladorAsignarUsuario.mostrarVista();
    }

    private void prepararActualizacion() {
         int fila = frm_GestionarEmpleado.TablaEmpleado.getSelectedRow();
    
    if (fila == -1) {
        JOptionPane.showMessageDialog(null, " Debe selecionar un empleado para Actulizarlo");
        
    } else {

       if (fila != -1) {
      
        
     frm_GestionarEmpleado.txtNombre.setText(frm_GestionarEmpleado.TablaEmpleado.getValueAt(fila,1).toString());
     
     frm_GestionarEmpleado.txtApellido.setText(frm_GestionarEmpleado. TablaEmpleado.getValueAt(fila, 2).toString());
     
     frm_GestionarEmpleado.txtCedula.setText(frm_GestionarEmpleado.TablaEmpleado.getValueAt(fila,5).toString());
     
     frm_GestionarEmpleado.txtEmail.setText(frm_GestionarEmpleado.TablaEmpleado.getValueAt(fila, 4).toString());
     
     frm_GestionarEmpleado.txtTelefono.setText(frm_GestionarEmpleado.TablaEmpleado.getValueAt(fila,3).toString());
     
     frm_GestionarEmpleado.jDateNacimiento.setDate((Date) frm_GestionarEmpleado.TablaEmpleado.getValueAt(fila, 6));
     
     frm_GestionarEmpleado.ComboxCargo.setSelectedItem(frm_GestionarEmpleado.TablaEmpleado.getValueAt(fila,7).toString());
      
     frm_GestionarEmpleado.txtID.setVisible(true);
     
     frm_GestionarEmpleado.txtID.setText(frm_GestionarEmpleado.TablaEmpleado.getValueAt(fila, 0).toString());

    frm_GestionarEmpleado.btbRegistrar.setVisible(false);
    frm_GestionarEmpleado.btnEliminarEmpleado.setVisible(false);
    frm_GestionarEmpleado.btbActualizarEmpleado.setVisible(false);
    
    frm_GestionarEmpleado.btbCancelar.setVisible(true);
    frm_GestionarEmpleado.btbConfirmarActualizacion.setVisible(true);
    
    }

    }
   
    }

    private void cancelarActualizacion() {
        frm_GestionarEmpleado.txtNombre.setText("");
  frm_GestionarEmpleado.txtApellido.setText("");
  frm_GestionarEmpleado.txtCedula.setText("");
  frm_GestionarEmpleado.txtEmail.setText("");
  frm_GestionarEmpleado.txtTelefono.setText("");
  frm_GestionarEmpleado.txtID.setText("");
  frm_GestionarEmpleado.ComboxCargo.addItem("");
  frm_GestionarEmpleado.txtID.setVisible(false);
 frm_GestionarEmpleado.jDateNacimiento.setDate(null);

   frm_GestionarEmpleado.btbConfirmarActualizacion.setVisible(false);
   frm_GestionarEmpleado.btbCancelar.setVisible(false);
 
   frm_GestionarEmpleado.btbCerrar.setVisible(true);
   frm_GestionarEmpleado.btbActualizarEmpleado.setVisible(true);
   frm_GestionarEmpleado.btbRegistrar.setVisible(true);
   frm_GestionarEmpleado.btnEliminarEmpleado.setVisible(true);
    }

    private void confirmarActualizacion() {
        Validar_Formulario_Empleado validador = new Validar_Formulario_Empleado();
        try {
            validador.ActualizarEmpleadoValidadoActualizar(vistaEmpleado);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Controlador_Empleado.class.getName())
                  .log(Level.SEVERE, "Error al confirmar actualización", ex);
        }
    }
}
