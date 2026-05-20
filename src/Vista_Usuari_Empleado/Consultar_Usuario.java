
package Vista_Usuari_Empleado;
import ModeloDAO.UsurioDAO;
import java.sql.SQLException;
import javax.swing.WindowConstants;

public class Consultar_Usuario extends javax.swing.JFrame {

    public Consultar_Usuario() throws SQLException, ClassNotFoundException {
     
            initComponents();
            setTitle("Consultar Usuario");
            setLocationRelativeTo(null);
            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            u.TituloUsuario(); 
           u.MostrarListaUsuario();
       u.centrarTextoTabla(TablaUsuario); 
       u.agregarFiltroBusqueda();
this.setExtendedState(Consultar_Usuario.MAXIMIZED_BOTH);
lblEstado.setVisible(false);
jComboEstadoUsuario.setVisible(false);
txtIdUsuarioEstado.setVisible(false);
btbConfirmarActulizacion.setVisible(false);
btbCancelarActualizacion.setVisible(false);
lblNivelDeAcceso.setVisible(false);
jComboxNivelAcceso.setVisible(false);
      
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        btbActualizar = new javax.swing.JButton();
        btbVolverCerraConsultarUsuario = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        txtBuscar = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        TablaUsuario = new javax.swing.JTable();
        lblEstado = new javax.swing.JLabel();
        jComboEstadoUsuario = new javax.swing.JComboBox<>();
        txtIdUsuarioEstado = new javax.swing.JTextField();
        btbConfirmarActulizacion = new javax.swing.JButton();
        btbCancelarActualizacion = new javax.swing.JButton();
        lblNivelDeAcceso = new javax.swing.JLabel();
        jComboxNivelAcceso = new javax.swing.JComboBox<>();
        fondo = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setFont(new java.awt.Font("Georgia", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Sistema de Inventario");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 20, 210, 50));

        btbActualizar.setFont(new java.awt.Font("Georgia", 1, 14)); // NOI18N
        btbActualizar.setText("Actualizar");
        btbActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btbActualizarActionPerformed(evt);
            }
        });
        jPanel1.add(btbActualizar, new org.netbeans.lib.awtextra.AbsoluteConstraints(1080, 670, 120, 30));

        btbVolverCerraConsultarUsuario.setFont(new java.awt.Font("Georgia", 1, 14)); // NOI18N
        btbVolverCerraConsultarUsuario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/Atras.png"))); // NOI18N
        btbVolverCerraConsultarUsuario.setText("Atras");
        btbVolverCerraConsultarUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btbVolverCerraConsultarUsuarioActionPerformed(evt);
            }
        });
        jPanel1.add(btbVolverCerraConsultarUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 670, 120, 30));
        jPanel1.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 30, 430, 10));

        txtBuscar.setFont(new java.awt.Font("Georgia", 0, 18)); // NOI18N
        jPanel1.add(txtBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(1080, 200, 110, -1));

        jLabel1.setFont(new java.awt.Font("Georgia", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Consultar Usuario");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 10, -1, -1));

        jLabel2.setFont(new java.awt.Font("Georgia", 0, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Buscar:");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1010, 190, -1, 40));

        TablaUsuario.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(TablaUsuario);
        if (TablaUsuario.getColumnModel().getColumnCount() > 0) {
            TablaUsuario.getColumnModel().getColumn(0).setResizable(false);
            TablaUsuario.getColumnModel().getColumn(1).setResizable(false);
            TablaUsuario.getColumnModel().getColumn(2).setResizable(false);
            TablaUsuario.getColumnModel().getColumn(3).setResizable(false);
        }

        jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 250, 1260, 320));

        lblEstado.setFont(new java.awt.Font("Georgia", 1, 24)); // NOI18N
        lblEstado.setForeground(new java.awt.Color(255, 255, 255));
        lblEstado.setText("Estado:");
        jPanel1.add(lblEstado, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 120, -1, -1));

        jComboEstadoUsuario.setFont(new java.awt.Font("Georgia", 1, 14)); // NOI18N
        jComboEstadoUsuario.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Activo", "Inactivo" }));
        jPanel1.add(jComboEstadoUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 120, 140, 30));

        txtIdUsuarioEstado.setEditable(false);
        txtIdUsuarioEstado.setFont(new java.awt.Font("Georgia", 1, 14)); // NOI18N
        txtIdUsuarioEstado.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(txtIdUsuarioEstado, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, 50, 30));

        btbConfirmarActulizacion.setFont(new java.awt.Font("Georgia", 1, 14)); // NOI18N
        btbConfirmarActulizacion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/Acepatar.png"))); // NOI18N
        btbConfirmarActulizacion.setText("Confirmar Actualización");
        btbConfirmarActulizacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btbConfirmarActulizacionActionPerformed(evt);
            }
        });
        jPanel1.add(btbConfirmarActulizacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 670, -1, 30));

        btbCancelarActualizacion.setFont(new java.awt.Font("Georgia", 1, 14)); // NOI18N
        btbCancelarActualizacion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/boton-x.png"))); // NOI18N
        btbCancelarActualizacion.setText("Cancelar");
        btbCancelarActualizacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btbCancelarActualizacionActionPerformed(evt);
            }
        });
        jPanel1.add(btbCancelarActualizacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 670, -1, -1));

        lblNivelDeAcceso.setFont(new java.awt.Font("Georgia", 1, 24)); // NOI18N
        lblNivelDeAcceso.setForeground(new java.awt.Color(255, 255, 255));
        lblNivelDeAcceso.setText("Nivel de Acceso :");
        jPanel1.add(lblNivelDeAcceso, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 120, -1, -1));

        jComboxNivelAcceso.setFont(new java.awt.Font("Georgia", 1, 14)); // NOI18N
        jComboxNivelAcceso.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Alto", "Bajo" }));
        jPanel1.add(jComboxNivelAcceso, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 120, 170, 30));

        fondo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/FondoCompleto.png"))); // NOI18N
        jPanel1.add(fondo, new org.netbeans.lib.awtextra.AbsoluteConstraints(-3, -4, 1380, 760));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, -10, 1380, 760));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btbVolverCerraConsultarUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btbVolverCerraConsultarUsuarioActionPerformed
   
    }//GEN-LAST:event_btbVolverCerraConsultarUsuarioActionPerformed

    private void btbActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btbActualizarActionPerformed
          
    }//GEN-LAST:event_btbActualizarActionPerformed

    private void btbCancelarActualizacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btbCancelarActualizacionActionPerformed

    }//GEN-LAST:event_btbCancelarActualizacionActionPerformed

    private void btbConfirmarActulizacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btbConfirmarActulizacionActionPerformed
        
    }//GEN-LAST:event_btbConfirmarActulizacionActionPerformed

    ModeloDAO.UsurioDAO u = new  UsurioDAO();
 
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JTable TablaUsuario;
    public static javax.swing.JButton btbActualizar;
    public static javax.swing.JButton btbCancelarActualizacion;
    public static javax.swing.JButton btbConfirmarActulizacion;
    public static javax.swing.JButton btbVolverCerraConsultarUsuario;
    public static javax.swing.JLabel fondo;
    public static javax.swing.JComboBox<String> jComboEstadoUsuario;
    public static javax.swing.JComboBox<String> jComboxNivelAcceso;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    public static javax.swing.JLabel lblEstado;
    public static javax.swing.JLabel lblNivelDeAcceso;
    public static javax.swing.JTextField txtBuscar;
    public static javax.swing.JTextField txtIdUsuarioEstado;
    // End of variables declaration//GEN-END:variables
}
