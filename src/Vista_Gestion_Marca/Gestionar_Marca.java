
package Vista_Gestion_Marca;

import Modelo.Marca;
import ModeloDAO.MarcaDao;
import Validacion.Validar_Marca;
import Vista_Usuari_Empleado.Menu_Sistema;
import controladorLogin.ControladorLogin;
import dictionary.AutoCorrectorGlobal;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Adrian
 */
public class Gestionar_Marca extends javax.swing.JFrame {

   
    public Gestionar_Marca() throws ClassNotFoundException, SQLException  {
        initComponents();
        setTitle("Gestionar Marca");
        setLocationRelativeTo(null);
        setExtendedState(MAXIMIZED_BOTH);
        Mk.MostrarMARCA();
        Mk.agregarFiltroBusqueda();
      Mk.EsconderTxtLblEnMarca();
      txtBuscar.putClientProperty("autocorrect", false);
       AutoCorrectorGlobal.aplicar(this);
       Mk.cargarComboMarcasFitaral();
     vali.inicializarValidacionMarca();
     Mk.TituloMarcaFitarl();
    
    }

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        txtNombre = new javax.swing.JTextField();
        txtPais = new javax.swing.JTextField();
        btbRegistrar = new javax.swing.JButton();
        BotonVolverMarca = new javax.swing.JButton();
        btbEliminar = new javax.swing.JButton();
        btbActualizarAntes = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        TablaMarca = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        txtBuscar = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        lblId = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        btbConfirmarActualizacion = new javax.swing.JButton();
        btbCancelar = new javax.swing.JButton();
        lblNombreMarcaValidacion = new javax.swing.JLabel();
        lblNombrePaisValidacion = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        comboxMarca = new javax.swing.JComboBox<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        TablaProductoPorMarca = new javax.swing.JTable();
        btbfitralMarca = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel1.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 283, 300, 10));

        txtNombre.setFont(new java.awt.Font("Georgia", 0, 18)); // NOI18N
        jPanel1.add(txtNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 380, 190, 30));

        txtPais.setFont(new java.awt.Font("Georgia", 0, 18)); // NOI18N
        jPanel1.add(txtPais, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 380, 170, 30));

        btbRegistrar.setFont(new java.awt.Font("Georgia", 1, 14)); // NOI18N
        btbRegistrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/Acepatar.png"))); // NOI18N
        btbRegistrar.setText("Registrar");
        btbRegistrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btbRegistrarActionPerformed(evt);
            }
        });
        jPanel1.add(btbRegistrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 380, 160, -1));

        BotonVolverMarca.setFont(new java.awt.Font("Georgia", 1, 14)); // NOI18N
        BotonVolverMarca.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/casita.png"))); // NOI18N
        BotonVolverMarca.setText("Volver");
        BotonVolverMarca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotonVolverMarcaActionPerformed(evt);
            }
        });
        jPanel1.add(BotonVolverMarca, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 930, 130, 30));

        btbEliminar.setFont(new java.awt.Font("Georgia", 1, 14)); // NOI18N
        btbEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/Eliminar.png"))); // NOI18N
        btbEliminar.setText("Eliminar");
        btbEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btbEliminarActionPerformed(evt);
            }
        });
        jPanel1.add(btbEliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 930, 130, 30));

        btbActualizarAntes.setFont(new java.awt.Font("Georgia", 1, 14)); // NOI18N
        btbActualizarAntes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/actualizar.png"))); // NOI18N
        btbActualizarAntes.setText("Actualizar");
        btbActualizarAntes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btbActualizarAntesActionPerformed(evt);
            }
        });
        jPanel1.add(btbActualizarAntes, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 930, -1, 30));

        TablaMarca.setFont(new java.awt.Font("Georgia", 0, 14)); // NOI18N
        TablaMarca.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(TablaMarca);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 570, 830, 340));

        jLabel2.setFont(new java.awt.Font("Georgia", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Gestionar Marca");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 260, -1, -1));

        txtBuscar.setFont(new java.awt.Font("Georgia", 0, 18)); // NOI18N
        jPanel1.add(txtBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 470, 120, -1));

        jLabel3.setFont(new java.awt.Font("Georgia", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Sistema de Inventario");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 271, -1, 50));

        jLabel6.setFont(new java.awt.Font("Georgia", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Buscar :");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 460, -1, 40));

        jLabel4.setFont(new java.awt.Font("Georgia", 1, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Nombre :");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 380, -1, -1));

        jLabel5.setFont(new java.awt.Font("Georgia", 1, 24)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Pais :");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 380, -1, -1));

        lblId.setFont(new java.awt.Font("Georgia", 1, 18)); // NOI18N
        lblId.setForeground(new java.awt.Color(255, 255, 255));
        lblId.setText("ID :");
        jPanel1.add(lblId, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 280, -1, -1));

        txtId.setEditable(false);
        txtId.setFont(new java.awt.Font("Georgia", 1, 18)); // NOI18N
        txtId.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtId.setBorder(null);
        jPanel1.add(txtId, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 280, 40, 30));

        btbConfirmarActualizacion.setFont(new java.awt.Font("Georgia", 1, 14)); // NOI18N
        btbConfirmarActualizacion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/Acepatar.png"))); // NOI18N
        btbConfirmarActualizacion.setText("Confirmar Actualización");
        btbConfirmarActualizacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btbConfirmarActualizacionActionPerformed(evt);
            }
        });
        jPanel1.add(btbConfirmarActualizacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 380, 260, 30));

        btbCancelar.setFont(new java.awt.Font("Georgia", 1, 14)); // NOI18N
        btbCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/boton-x.png"))); // NOI18N
        btbCancelar.setText("Cancelar");
        btbCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btbCancelarActionPerformed(evt);
            }
        });
        jPanel1.add(btbCancelar, new org.netbeans.lib.awtextra.AbsoluteConstraints(1050, 380, 140, 30));

        lblNombreMarcaValidacion.setFont(new java.awt.Font("Georgia", 1, 12)); // NOI18N
        lblNombreMarcaValidacion.setForeground(new java.awt.Color(255, 0, 0));
        lblNombreMarcaValidacion.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1.add(lblNombreMarcaValidacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 410, 230, 20));

        lblNombrePaisValidacion.setFont(new java.awt.Font("Georgia", 1, 12)); // NOI18N
        lblNombrePaisValidacion.setForeground(new java.awt.Color(255, 0, 0));
        lblNombrePaisValidacion.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblNombrePaisValidacion.setToolTipText("");
        jPanel1.add(lblNombrePaisValidacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 410, 280, 20));

        jLabel7.setFont(new java.awt.Font("Georgia", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Fitral Marca Por Producto:");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(901, 470, 260, 30));

        comboxMarca.setFont(new java.awt.Font("Georgia", 1, 12)); // NOI18N
        jPanel1.add(comboxMarca, new org.netbeans.lib.awtextra.AbsoluteConstraints(1160, 470, 190, 30));

        TablaProductoPorMarca.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(TablaProductoPorMarca);

        jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1070, 520, 270, 320));

        btbfitralMarca.setFont(new java.awt.Font("Georgia", 1, 14)); // NOI18N
        btbfitralMarca.setText("Filtrar");
        btbfitralMarca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btbfitralMarcaActionPerformed(evt);
            }
        });
        jPanel1.add(btbfitralMarca, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 520, 110, 30));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/Componentes automotrices.png"))); // NOI18N
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 250, -1, 770));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, -250, 1520, 1000));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btbRegistrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btbRegistrarActionPerformed
        
    }//GEN-LAST:event_btbRegistrarActionPerformed

    private void BotonVolverMarcaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonVolverMarcaActionPerformed

    }//GEN-LAST:event_BotonVolverMarcaActionPerformed

    private void btbEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btbEliminarActionPerformed
        
    }//GEN-LAST:event_btbEliminarActionPerformed

    private void btbActualizarAntesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btbActualizarAntesActionPerformed

    }//GEN-LAST:event_btbActualizarAntesActionPerformed

    private void btbCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btbCancelarActionPerformed

    }//GEN-LAST:event_btbCancelarActionPerformed

    private void btbConfirmarActualizacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btbConfirmarActualizacionActionPerformed

    }//GEN-LAST:event_btbConfirmarActualizacionActionPerformed

    private void btbfitralMarcaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btbfitralMarcaActionPerformed

    }//GEN-LAST:event_btbfitralMarcaActionPerformed

    Validacion.Validar_Marca vali = new Validar_Marca();
  
 ModeloDAO.MarcaDao Mk = new  MarcaDao();
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JButton BotonVolverMarca;
    public static javax.swing.JTable TablaMarca;
    public static javax.swing.JTable TablaProductoPorMarca;
    public static javax.swing.JButton btbActualizarAntes;
    public static javax.swing.JButton btbCancelar;
    public static javax.swing.JButton btbConfirmarActualizacion;
    public static javax.swing.JButton btbEliminar;
    public static javax.swing.JButton btbRegistrar;
    public static javax.swing.JButton btbfitralMarca;
    public static javax.swing.JComboBox<String> comboxMarca;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    public static javax.swing.JLabel lblId;
    public static javax.swing.JLabel lblNombreMarcaValidacion;
    public static javax.swing.JLabel lblNombrePaisValidacion;
    public static javax.swing.JTextField txtBuscar;
    public static javax.swing.JTextField txtId;
    public static javax.swing.JTextField txtNombre;
    public static javax.swing.JTextField txtPais;
    // End of variables declaration//GEN-END:variables
}
