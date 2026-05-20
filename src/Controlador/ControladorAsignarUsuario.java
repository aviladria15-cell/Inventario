package Controlador;

import Validacion.Validar_Formulario_Empleado;
import Vista_Usuari_Empleado.AsignarUsuario;
import Vista_Usuari_Empleado.Menu_Sistema;
import Vista_Usuari_Empleado.frm_GestionarEmpleado;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controlador específico para la ventana de Asignar Usuario a un Empleado
 */
public class ControladorAsignarUsuario implements ActionListener {

    private final Menu_Sistema menu;
    private final frm_GestionarEmpleado vistaEmpleado;   // Referencia al padre (Empleado)

    private AsignarUsuario vistaAsignarUsuario;

    /**
     * Constructor - Recibe el menú y la vista de Empleado para poder volver
     */
    public ControladorAsignarUsuario(Menu_Sistema menu, frm_GestionarEmpleado vistaEmpleado) {
        this.menu = menu;
        this.vistaEmpleado = vistaEmpleado;
    }

    /**
     * Muestra la ventana de Asignar Usuario
     */
    public void mostrarVista() {
        if (vistaAsignarUsuario == null) {
            vistaAsignarUsuario = new AsignarUsuario();
            agregarListeners();
        }

        vistaAsignarUsuario.setVisible(true);
        // Ocultamos la vista de empleado mientras asignamos el usuario
        if (vistaEmpleado != null) {
            vistaEmpleado.setVisible(false);
        }
    }

    private void agregarListeners() {
        vistaAsignarUsuario.btbRegistrarUsuario.addActionListener(this);
        // Si tienes botón de cancelar, agrégalo aquí también
        // vistaAsignarUsuario.btbCancelar.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vistaAsignarUsuario.btbRegistrarUsuario) {
            registrarUsuario();
        }
        // Puedes agregar más botones aquí en el futuro (Cancelar, etc.)
    }

    /**
     * Registra el usuario y vuelve a la ventana de Empleados
     */
    private void registrarUsuario() {
        Validar_Formulario_Empleado validador = new Validar_Formulario_Empleado();

        try {
            boolean guardadoCorrecto = validador.registrarUsuarioValidado(vistaAsignarUsuario);

            if (guardadoCorrecto) {
                volverAVistaEmpleado();
                
            }

        } catch (Exception ex) {
            Logger.getLogger(ControladorAsignarUsuario.class.getName())
                  .log(Level.SEVERE, "Error al registrar usuario", ex);
        }
    }

    /**
     * Vuelve a la ventana de gestionar empleado después de registrar el usuario
     */
    private void volverAVistaEmpleado() {
        if (vistaAsignarUsuario != null) {
            vistaAsignarUsuario.setVisible(false);
        }

        if (vistaEmpleado != null) {
            vistaEmpleado.setVisible(true);
        }
    }

    /**
     * Método público por si quieres cancelar desde afuera
     */
    public void cancelarAsignacion() {
        volverAVistaEmpleado();
    }
}