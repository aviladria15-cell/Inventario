/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Vista_Gestion_Categoria;

import Modelo.Categoria;
import ModeloDAO.CategoriaDao;
import Validacion.Validar_Categoria;
import Vista_Usuari_Empleado.Menu_Sistema;
import controladorLogin.ControladorLogin;
import dictionary.AutoCorrectorGlobal;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import dictionary.AutoCorrectorGlobal;

/**
 *
 * @author Adrian
 */
public class frm_Categoria extends javax.swing.JFrame {

    /**
     * Creates new form frm_Categoria
     */
    public frm_Categoria() throws ClassNotFoundException, SQLException {
       
        initComponents();
         AutoCorrectorGlobal.aplicar(this);
        setTitle("Gestionar Categoria");
        setLocationRelativeTo(null);
        Cate.MostrarCategorias();
        Cate.centrarTextoTabla(tablaCategoria);
        txtBuscar.putClientProperty("autocorrect", false);
        AutoCorrectorGlobal.aplicar(this);
        setExtendedState(MAXIMIZED_BOTH);
     Cate.agregarFiltroBusqueda();
     Cate.EscondertxtEnCAtegoria();
     vali.inicializarValidacionCategoria();
     
     Cate.TituloCategoriaProducto();
     Cate.cargarComboCategorias();
   
     
        
    }

  
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        btbVolverCategoria = new javax.swing.JButton();
        txtNombreCategoria = new javax.swing.JTextField();
        btbRegistrar = new javax.swing.JButton();
        txtBuscar = new javax.swing.JTextField();
        btbEliminar = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        btbConfirmarActualizacion = new javax.swing.JButton();
        btbActualizarVerdadero = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaCategoria = new javax.swing.JTable();
        txtId = new javax.swing.JTextField();
        lblId = new javax.swing.JLabel();
        btbCancelar = new javax.swing.JButton();
        lblCategoriaValidar = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        TablaProductoEnCategoria = new javax.swing.JTable();
        ComboxProductoEnCategoria = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        btbFiltrarCategoria = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel1.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 43, 360, 10));

        jLabel2.setFont(new java.awt.Font("Georgia", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Gestionar Categoria");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 10, -1, 50));

        jLabel3.setFont(new java.awt.Font("Georgia", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Sistema de Inventario");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 40, -1, 30));

        jLabel4.setFont(new java.awt.Font("Georgia", 1, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Nombre de Categoria :");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 150, -1, 30));

        btbVolverCategoria.setFont(new java.awt.Font("Georgia", 1, 14)); // NOI18N
        btbVolverCategoria.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/casita.png"))); // NOI18N
        btbVolverCategoria.setText("Volver");
        btbVolverCategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btbVolverCategoriaActionPerformed(evt);
            }
        });
        jPanel1.add(btbVolverCategoria, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 690, -1, 30));

        txtNombreCategoria.setFont(new java.awt.Font("Georgia", 0, 18)); // NOI18N
        jPanel1.add(txtNombreCategoria, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 150, 330, 30));

        btbRegistrar.setFont(new java.awt.Font("Georgia", 1, 14)); // NOI18N
        btbRegistrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/Acepatar.png"))); // NOI18N
        btbRegistrar.setText("Registrar ");
        btbRegistrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btbRegistrarActionPerformed(evt);
            }
        });
        jPanel1.add(btbRegistrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 150, 140, 30));

        txtBuscar.setFont(new java.awt.Font("Georgia", 0, 14)); // NOI18N
        jPanel1.add(txtBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 240, 110, 30));

        btbEliminar.setFont(new java.awt.Font("Georgia", 1, 14)); // NOI18N
        btbEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/Eliminar.png"))); // NOI18N
        btbEliminar.setText("Eliminar");
        btbEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btbEliminarActionPerformed(evt);
            }
        });
        jPanel1.add(btbEliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 690, -1, 30));

        jLabel5.setFont(new java.awt.Font("Georgia", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Buscar :");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 240, -1, 30));

        btbConfirmarActualizacion.setFont(new java.awt.Font("Georgia", 1, 14)); // NOI18N
        btbConfirmarActualizacion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/Acepatar.png"))); // NOI18N
        btbConfirmarActualizacion.setText("  Confirmar Actualización");
        btbConfirmarActualizacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btbConfirmarActualizacionActionPerformed(evt);
            }
        });
        jPanel1.add(btbConfirmarActualizacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 150, 260, 30));

        btbActualizarVerdadero.setFont(new java.awt.Font("Georgia", 1, 14)); // NOI18N
        btbActualizarVerdadero.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/actualizar.png"))); // NOI18N
        btbActualizarVerdadero.setText("Actualizar");
        btbActualizarVerdadero.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btbActualizarVerdaderoActionPerformed(evt);
            }
        });
        jPanel1.add(btbActualizarVerdadero, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 690, -1, 30));

        tablaCategoria.setFont(new java.awt.Font("Georgia", 0, 14)); // NOI18N
        tablaCategoria.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tablaCategoria);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 280, 750, 350));

        txtId.setEditable(false);
        txtId.setFont(new java.awt.Font("Georgia", 1, 18)); // NOI18N
        txtId.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtId.setBorder(null);
        jPanel1.add(txtId, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 30, 40, 30));

        lblId.setFont(new java.awt.Font("Georgia", 0, 24)); // NOI18N
        lblId.setForeground(new java.awt.Color(255, 255, 255));
        lblId.setText("ID :");
        jPanel1.add(lblId, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 30, 50, -1));

        btbCancelar.setFont(new java.awt.Font("Georgia", 1, 14)); // NOI18N
        btbCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/boton-x.png"))); // NOI18N
        btbCancelar.setText("Cancelar");
        btbCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btbCancelarActionPerformed(evt);
            }
        });
        jPanel1.add(btbCancelar, new org.netbeans.lib.awtextra.AbsoluteConstraints(1050, 150, -1, 30));

        lblCategoriaValidar.setFont(new java.awt.Font("Georgia", 1, 12)); // NOI18N
        lblCategoriaValidar.setForeground(new java.awt.Color(255, 0, 0));
        jPanel1.add(lblCategoriaValidar, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 180, 330, 20));

        TablaProductoEnCategoria.setFont(new java.awt.Font("Georgia", 0, 14)); // NOI18N
        TablaProductoEnCategoria.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(TablaProductoEnCategoria);

        jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1050, 270, 300, 360));

        ComboxProductoEnCategoria.setFont(new java.awt.Font("Georgia", 1, 14)); // NOI18N
        jPanel1.add(ComboxProductoEnCategoria, new org.netbeans.lib.awtextra.AbsoluteConstraints(1110, 220, 250, 30));

        jLabel6.setFont(new java.awt.Font("Georgia", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Filtrar Producto En Categoria :");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 220, -1, 30));

        btbFiltrarCategoria.setFont(new java.awt.Font("Georgia", 1, 14)); // NOI18N
        btbFiltrarCategoria.setText("Filtrar");
        btbFiltrarCategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btbFiltrarCategoriaActionPerformed(evt);
            }
        });
        jPanel1.add(btbFiltrarCategoria, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 260, 90, 30));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/Componentes automotrices.png"))); // NOI18N
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1590, 760));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1408, 760));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btbVolverCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btbVolverCategoriaActionPerformed

    }//GEN-LAST:event_btbVolverCategoriaActionPerformed

    private void btbRegistrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btbRegistrarActionPerformed
    
    }//GEN-LAST:event_btbRegistrarActionPerformed

    private void btbEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btbEliminarActionPerformed

    }//GEN-LAST:event_btbEliminarActionPerformed

    private void btbActualizarVerdaderoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btbActualizarVerdaderoActionPerformed

    }//GEN-LAST:event_btbActualizarVerdaderoActionPerformed

    private void btbConfirmarActualizacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btbConfirmarActualizacionActionPerformed
      
    }//GEN-LAST:event_btbConfirmarActualizacionActionPerformed

    private void btbCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btbCancelarActionPerformed

    }//GEN-LAST:event_btbCancelarActionPerformed

    private void btbFiltrarCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btbFiltrarCategoriaActionPerformed
       
    }//GEN-LAST:event_btbFiltrarCategoriaActionPerformed

   
    
 
   Validacion.Validar_Categoria vali = new Validar_Categoria();
ModeloDAO.CategoriaDao Cate = new CategoriaDao();
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JComboBox<String> ComboxProductoEnCategoria;
    public static javax.swing.JTable TablaProductoEnCategoria;
    public static javax.swing.JButton btbActualizarVerdadero;
    public static javax.swing.JButton btbCancelar;
    public static javax.swing.JButton btbConfirmarActualizacion;
    public static javax.swing.JButton btbEliminar;
    public static javax.swing.JButton btbFiltrarCategoria;
    public static javax.swing.JButton btbRegistrar;
    public static javax.swing.JButton btbVolverCategoria;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    public static javax.swing.JLabel lblCategoriaValidar;
    public static javax.swing.JLabel lblId;
    public static javax.swing.JTable tablaCategoria;
    public static javax.swing.JTextField txtBuscar;
    public static javax.swing.JTextField txtId;
    public static javax.swing.JTextField txtNombreCategoria;
    // End of variables declaration//GEN-END:variables
}
