package Vista_Usuari_Empleado;


import javax.swing.WindowConstants;
import Validacion.Validar_Formulario_Empleado;
import dictionary.AutoCorrectorGlobal;
import java.sql.SQLException;

public class frm_GestionarEmpleado extends javax.swing.JFrame {


    public frm_GestionarEmpleado() throws SQLException, ClassNotFoundException {
        initComponents();
        setTitle("Gestionar Empleado");
        setLocationRelativeTo(null);
      
        this.setExtendedState(frm_GestionarEmpleado.MAXIMIZED_BOTH);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        De.TituloEmpleado();
        De.MostrarListaEmpleado();
        De.centrarTextoTabla(TablaEmpleado);
        De.configurarCampoTelefono();

        btbConfirmarActualizacion.setVisible(false);
        btbCancelar.setVisible(false);
       txtID.setVisible(false);
    AutoCorrectorGlobal.aplicar(this);
    
    vali.inicializarValidaciones();

    De.agregarFiltroBusqueda();
    }

  
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        txtApellido = new javax.swing.JTextField();
        txtTelefono = new javax.swing.JTextField();
        txtEmail = new javax.swing.JTextField();
        txtCedula = new javax.swing.JTextField();
        btbRegistrar = new javax.swing.JButton();
        btbCerrar = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        ComboxCargo = new javax.swing.JComboBox<>();
        jDateNacimiento = new com.toedter.calendar.JDateChooser();
        jLabel11 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        TablaEmpleado = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();
        txtBuscar = new javax.swing.JTextField();
        btnEliminarEmpleado = new javax.swing.JButton();
        btbActualizarEmpleado = new javax.swing.JButton();
        txtID = new javax.swing.JTextField();
        btbConfirmarActualizacion = new javax.swing.JButton();
        btbCancelar = new javax.swing.JButton();
        lblValidarNombre = new javax.swing.JLabel();
        lblValidarApellido = new javax.swing.JLabel();
        lblValidarFecha = new javax.swing.JLabel();
        lblValidarCorreo = new javax.swing.JLabel();
        lblValidarTelefono = new javax.swing.JLabel();
        lblValidarCedula = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        ComboBoxOperadora = new javax.swing.JComboBox<>();
        lblMensajeOperadora = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Georgia", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Nombre :");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 70, 120, 50));

        jLabel3.setFont(new java.awt.Font("Georgia", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Sistema de Inventario");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 30, -1, 20));

        jLabel4.setFont(new java.awt.Font("Georgia", 1, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Apellido :");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 170, 130, -1));

        jLabel5.setFont(new java.awt.Font("Georgia", 1, 24)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Teléfono :");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 250, 130, -1));

        jLabel6.setFont(new java.awt.Font("Georgia", 1, 24)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Email :");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 70, 90, 40));

        jLabel7.setFont(new java.awt.Font("Georgia", 1, 24)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Cédula :");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 80, 110, -1));

        jLabel8.setFont(new java.awt.Font("Georgia", 1, 24)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("F.N :");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 170, 70, -1));

        txtNombre.setFont(new java.awt.Font("Georgia", 0, 18)); // NOI18N
        txtNombre.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(txtNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 80, 230, 30));

        txtApellido.setFont(new java.awt.Font("Georgia", 0, 18)); // NOI18N
        txtApellido.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtApellido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtApellidoActionPerformed(evt);
            }
        });
        jPanel1.add(txtApellido, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 170, 230, 30));

        txtTelefono.setFont(new java.awt.Font("Georgia", 0, 18)); // NOI18N
        txtTelefono.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtTelefono.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTelefonoActionPerformed(evt);
            }
        });
        jPanel1.add(txtTelefono, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 250, 220, 30));

        txtEmail.setFont(new java.awt.Font("Georgia", 0, 18)); // NOI18N
        jPanel1.add(txtEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 80, 230, 30));

        txtCedula.setFont(new java.awt.Font("Georgia", 0, 18)); // NOI18N
        txtCedula.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtCedula.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCedulaActionPerformed(evt);
            }
        });
        jPanel1.add(txtCedula, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 80, 220, 30));

        btbRegistrar.setFont(new java.awt.Font("Georgia", 1, 14)); // NOI18N
        btbRegistrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/Acepatar.png"))); // NOI18N
        btbRegistrar.setText("Registrar");
        btbRegistrar.setBorder(null);
        btbRegistrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btbRegistrarActionPerformed(evt);
            }
        });
        jPanel1.add(btbRegistrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(1070, 680, 120, 30));

        btbCerrar.setFont(new java.awt.Font("Georgia", 1, 14)); // NOI18N
        btbCerrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/casita.png"))); // NOI18N
        btbCerrar.setText("Volver");
        btbCerrar.setBorder(null);
        btbCerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btbCerrarActionPerformed(evt);
            }
        });
        jPanel1.add(btbCerrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 680, 110, 30));
        jPanel1.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 30, 480, 40));

        ComboxCargo.setFont(new java.awt.Font("Georgia", 0, 18)); // NOI18N
        ComboxCargo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Empleado", "Dueño" }));
        jPanel1.add(ComboxCargo, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 250, -1, -1));
        jPanel1.add(jDateNacimiento, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 170, 220, 30));

        jLabel11.setFont(new java.awt.Font("Georgia", 1, 24)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Cargo :");
        jPanel1.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 250, -1, -1));

        jLabel10.setFont(new java.awt.Font("Georgia", 1, 24)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Gestionar Empleado");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 10, -1, 20));

        TablaEmpleado.setAutoCreateRowSorter(true);
        TablaEmpleado.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TablaEmpleado.setGridColor(new java.awt.Color(153, 153, 153));
        jScrollPane2.setViewportView(TablaEmpleado);
        if (TablaEmpleado.getColumnModel().getColumnCount() > 0) {
            TablaEmpleado.getColumnModel().getColumn(0).setResizable(false);
            TablaEmpleado.getColumnModel().getColumn(1).setResizable(false);
            TablaEmpleado.getColumnModel().getColumn(2).setResizable(false);
            TablaEmpleado.getColumnModel().getColumn(3).setResizable(false);
        }

        jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 330, 1200, 300));

        jLabel9.setFont(new java.awt.Font("Georgia", 1, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Buscar :");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 280, 90, 30));

        txtBuscar.setFont(new java.awt.Font("Georgia", 0, 14)); // NOI18N
        jPanel1.add(txtBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(1030, 280, 120, 30));

        btnEliminarEmpleado.setFont(new java.awt.Font("Georgia", 1, 14)); // NOI18N
        btnEliminarEmpleado.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/Eliminar.png"))); // NOI18N
        btnEliminarEmpleado.setText("Eliminar");
        btnEliminarEmpleado.setEnabled(false);
        btnEliminarEmpleado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarEmpleadoActionPerformed(evt);
            }
        });
        jPanel1.add(btnEliminarEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 680, 140, 30));

        btbActualizarEmpleado.setFont(new java.awt.Font("Georgia", 1, 14)); // NOI18N
        btbActualizarEmpleado.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/actualizar.png"))); // NOI18N
        btbActualizarEmpleado.setText("Actualizar");
        btbActualizarEmpleado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btbActualizarEmpleadoActionPerformed(evt);
            }
        });
        jPanel1.add(btbActualizarEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 680, -1, 30));

        txtID.setEditable(false);
        txtID.setFont(new java.awt.Font("Georgia", 1, 14)); // NOI18N
        txtID.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtID.setToolTipText("");
        jPanel1.add(txtID, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 10, 50, -1));

        btbConfirmarActualizacion.setFont(new java.awt.Font("Georgia", 1, 14)); // NOI18N
        btbConfirmarActualizacion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/Acepatar.png"))); // NOI18N
        btbConfirmarActualizacion.setText("Confirmar Actualización");
        btbConfirmarActualizacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btbConfirmarActualizacionActionPerformed(evt);
            }
        });
        jPanel1.add(btbConfirmarActualizacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 680, -1, -1));

        btbCancelar.setFont(new java.awt.Font("Georgia", 1, 14)); // NOI18N
        btbCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/boton-x.png"))); // NOI18N
        btbCancelar.setText("Cancelar");
        btbCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btbCancelarActionPerformed(evt);
            }
        });
        jPanel1.add(btbCancelar, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 680, 170, 30));

        lblValidarNombre.setFont(new java.awt.Font("Georgia", 1, 12)); // NOI18N
        lblValidarNombre.setForeground(new java.awt.Color(255, 0, 0));
        jPanel1.add(lblValidarNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 110, 210, 20));

        lblValidarApellido.setFont(new java.awt.Font("Georgia", 1, 12)); // NOI18N
        lblValidarApellido.setForeground(new java.awt.Color(255, 0, 0));
        jPanel1.add(lblValidarApellido, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 200, 210, 20));

        lblValidarFecha.setFont(new java.awt.Font("Georgia", 1, 12)); // NOI18N
        lblValidarFecha.setForeground(new java.awt.Color(255, 0, 0));
        jPanel1.add(lblValidarFecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 200, 350, 20));

        lblValidarCorreo.setFont(new java.awt.Font("Georgia", 1, 12)); // NOI18N
        lblValidarCorreo.setForeground(new java.awt.Color(255, 0, 0));
        jPanel1.add(lblValidarCorreo, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 110, 300, 20));

        lblValidarTelefono.setFont(new java.awt.Font("Georgia", 1, 12)); // NOI18N
        lblValidarTelefono.setForeground(new java.awt.Color(255, 0, 0));
        jPanel1.add(lblValidarTelefono, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 280, 210, 20));

        lblValidarCedula.setFont(new java.awt.Font("Georgia", 1, 12)); // NOI18N
        lblValidarCedula.setForeground(new java.awt.Color(255, 0, 0));
        jPanel1.add(lblValidarCedula, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 110, 230, 20));

        jLabel12.setFont(new java.awt.Font("Georgia", 1, 24)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Operadora :");
        jPanel1.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 170, 160, -1));

        ComboBoxOperadora.setFont(new java.awt.Font("Georgia", 1, 14)); // NOI18N
        ComboBoxOperadora.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Elegir Operadora", "Movistar (0414) ", "Movistar (0424)", "Digitel (0412)", "Digitel (0422)", "Movilnet (0416) ", "Movilnet (0426)" }));
        jPanel1.add(ComboBoxOperadora, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 173, 200, 30));

        lblMensajeOperadora.setFont(new java.awt.Font("Georgia", 1, 12)); // NOI18N
        lblMensajeOperadora.setForeground(new java.awt.Color(255, 0, 0));
        lblMensajeOperadora.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1.add(lblMensajeOperadora, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 200, 230, 20));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/Componentes automotrices.png"))); // NOI18N
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1540, 760));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtApellidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtApellidoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtApellidoActionPerformed

    private void txtCedulaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCedulaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCedulaActionPerformed

    private void btbRegistrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btbRegistrarActionPerformed
  
        
    }//GEN-LAST:event_btbRegistrarActionPerformed

    private void btbCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btbCerrarActionPerformed
   
// TODO add your handling code here:
    }//GEN-LAST:event_btbCerrarActionPerformed

    private void txtTelefonoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTelefonoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTelefonoActionPerformed

    private void btbActualizarEmpleadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btbActualizarEmpleadoActionPerformed
     
    }//GEN-LAST:event_btbActualizarEmpleadoActionPerformed

    private void btbCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btbCancelarActionPerformed
       
    }//GEN-LAST:event_btbCancelarActionPerformed

    private void btbConfirmarActualizacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btbConfirmarActualizacionActionPerformed

     
        
        
    }//GEN-LAST:event_btbConfirmarActualizacionActionPerformed

    private void btnEliminarEmpleadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarEmpleadoActionPerformed



       
    }//GEN-LAST:event_btnEliminarEmpleadoActionPerformed

    
 
   
   Validacion.Validar_Formulario_Empleado vali = new Validar_Formulario_Empleado();
        
ModeloDAO.EmpleadoDAO De = new ModeloDAO.EmpleadoDAO();
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JComboBox<String> ComboBoxOperadora;
    public static javax.swing.JComboBox<String> ComboxCargo;
    public static javax.swing.JTable TablaEmpleado;
    public static javax.swing.JButton btbActualizarEmpleado;
    public static javax.swing.JButton btbCancelar;
    public static javax.swing.JButton btbCerrar;
    public static javax.swing.JButton btbConfirmarActualizacion;
    public static javax.swing.JButton btbRegistrar;
    public static javax.swing.JButton btnEliminarEmpleado;
    public static com.toedter.calendar.JDateChooser jDateNacimiento;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    public static javax.swing.JLabel lblMensajeOperadora;
    public static javax.swing.JLabel lblValidarApellido;
    public static javax.swing.JLabel lblValidarCedula;
    public static javax.swing.JLabel lblValidarCorreo;
    public static javax.swing.JLabel lblValidarFecha;
    public static javax.swing.JLabel lblValidarNombre;
    public static javax.swing.JLabel lblValidarTelefono;
    public static javax.swing.JTextField txtApellido;
    public static javax.swing.JTextField txtBuscar;
    public static javax.swing.JTextField txtCedula;
    public static javax.swing.JTextField txtEmail;
    public static javax.swing.JTextField txtID;
    public static javax.swing.JTextField txtNombre;
    public static javax.swing.JTextField txtTelefono;
    // End of variables declaration//GEN-END:variables
}
