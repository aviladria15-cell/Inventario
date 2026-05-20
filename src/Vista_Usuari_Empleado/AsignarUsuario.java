
package Vista_Usuari_Empleado;
import javax.swing.WindowConstants;

import Validacion.Validar_Formulario_Empleado;
import dictionary.AutoCorrectorGlobal;


public class AsignarUsuario extends javax.swing.JFrame {

    
    public AsignarUsuario() {
        initComponents();
        setTitle("Registrar Usuario");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        this.setExtendedState(MAXIMIZED_BOTH);
         AutoCorrectorGlobal.aplicar(this);
         vali.inicializarValidacionesAsignarUsuario();
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
        txtNombreUsuario = new javax.swing.JTextField();
        txtClave = new javax.swing.JTextField();
        btbRegistrarUsuario = new javax.swing.JButton();
        ComboxAsignarUsuario = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        lblNombreNivelAcceso = new javax.swing.JLabel();
        lblNombreClave = new javax.swing.JLabel();
        lblNombreUsuario = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        ComboxEstadoUsuario = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Georgia", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Asignar Usuario");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 10, -1, 40));

        jLabel3.setFont(new java.awt.Font("Georgia", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Sistema de Inventario");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 50, 220, 20));

        jLabel4.setFont(new java.awt.Font("Georgia", 1, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Nombre Usuario :");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 280, 230, 40));

        jLabel5.setFont(new java.awt.Font("Georgia", 1, 24)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Contraseña :");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 400, 170, -1));

        jLabel6.setFont(new java.awt.Font("Georgia", 1, 24)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Nivel de Acceso:");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 510, 200, 30));

        txtNombreUsuario.setFont(new java.awt.Font("Georgia", 0, 18)); // NOI18N
        txtNombreUsuario.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(txtNombreUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 290, 350, 30));

        txtClave.setFont(new java.awt.Font("Georgia", 0, 18)); // NOI18N
        jPanel1.add(txtClave, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 400, 180, 30));

        btbRegistrarUsuario.setFont(new java.awt.Font("Georgia", 1, 14)); // NOI18N
        btbRegistrarUsuario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/Acepatar.png"))); // NOI18N
        btbRegistrarUsuario.setText("Asignar");
        btbRegistrarUsuario.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btbRegistrarUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btbRegistrarUsuarioActionPerformed(evt);
            }
        });
        jPanel1.add(btbRegistrarUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(1090, 650, 120, 40));

        ComboxAsignarUsuario.setFont(new java.awt.Font("Georgia", 1, 18)); // NOI18N
        ComboxAsignarUsuario.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Alto", "Bajo" }));
        ComboxAsignarUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ComboxAsignarUsuarioActionPerformed(evt);
            }
        });
        jPanel1.add(ComboxAsignarUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 510, 350, 30));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/17826088.png"))); // NOI18N
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 110, -1, -1));

        lblNombreNivelAcceso.setFont(new java.awt.Font("Georgia", 1, 12)); // NOI18N
        lblNombreNivelAcceso.setForeground(new java.awt.Color(255, 0, 0));
        jPanel1.add(lblNombreNivelAcceso, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 430, 310, 20));

        lblNombreClave.setFont(new java.awt.Font("Georgia", 1, 12)); // NOI18N
        lblNombreClave.setForeground(new java.awt.Color(255, 0, 0));
        jPanel1.add(lblNombreClave, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 450, 310, 20));

        lblNombreUsuario.setFont(new java.awt.Font("Georgia", 1, 12)); // NOI18N
        lblNombreUsuario.setForeground(new java.awt.Color(255, 0, 0));
        jPanel1.add(lblNombreUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 324, 350, 20));

        jLabel8.setFont(new java.awt.Font("Georgia", 1, 24)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Estado :");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 610, -1, -1));

        ComboxEstadoUsuario.setFont(new java.awt.Font("Georgia", 1, 18)); // NOI18N
        ComboxEstadoUsuario.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Activo", "Inactivo" }));
        jPanel1.add(ComboxEstadoUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 610, 330, 30));

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/FondoCompleto.png"))); // NOI18N
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1370, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btbRegistrarUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btbRegistrarUsuarioActionPerformed
     
    }//GEN-LAST:event_btbRegistrarUsuarioActionPerformed

    private void ComboxAsignarUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ComboxAsignarUsuarioActionPerformed
       
    }//GEN-LAST:event_ComboxAsignarUsuarioActionPerformed

  
 Validacion.Validar_Formulario_Empleado vali = new Validar_Formulario_Empleado();
ModeloDAO.UsurioDAO Mu = new ModeloDAO.UsurioDAO();
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JComboBox<String> ComboxAsignarUsuario;
    public static javax.swing.JComboBox<String> ComboxEstadoUsuario;
    public static javax.swing.JButton btbRegistrarUsuario;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    public static javax.swing.JLabel lblNombreClave;
    public static javax.swing.JLabel lblNombreNivelAcceso;
    public static javax.swing.JLabel lblNombreUsuario;
    public static javax.swing.JTextField txtClave;
    public static javax.swing.JTextField txtNombreUsuario;
    // End of variables declaration//GEN-END:variables
}
