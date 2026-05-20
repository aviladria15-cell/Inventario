/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Vista_Gestionar_Proveedor;

import Controlador.controladorVisual;
import Funciones.Alerte_De_Stock;
import ModeloDAO.CotizaciónDao;
import java.sql.SQLException;

/**
 *
 * @author avila
 */
public class Vista_Cotizaciónn extends javax.swing.JFrame {
    
 
    public Vista_Cotizaciónn() throws ClassNotFoundException, SQLException {
        initComponents();
        setTitle("Cotización");
        setLocationRelativeTo(this);
        setExtendedState(MAXIMIZED_BOTH);
        Dao.MostrarCotizacion();
        AL.AlertaCotizacion();
        lblCorreo.setVisible(false);
        txtCorreo.setVisible(false);
        lblNombreProveedor.setVisible(false);
        txtNombreProveedor.setVisible(false);
        txtNombreProducto.setVisible(false);
        lblNombreProducto.setVisible(false);
        btbCancelarCotizacion.setVisible(false);
        lblCategoriaProducto.setVisible(false);
        txtCategoriaProudcto.setVisible(false);
        lblMarca.setVisible(false);
        txtMarca.setVisible(false);
        lblCantidadActual.setVisible(false);
        txtCantidadActual.setVisible(false);
        lblCantidadRequeridad.setVisible(false);
        txtCantidadRequeridad.setVisible(false);
        jScrollDescri.setVisible(false);
        lblDescripcion.setVisible(false);
        txtDescripcion.setVisible(false);
        btbEnviarSolicitud.setVisible(false);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        btbVolverProvvedor = new javax.swing.JButton();
        btbGenerarCotizacion = new javax.swing.JButton();
        lblCorreo = new javax.swing.JLabel();
        txtCorreo = new javax.swing.JTextField();
        lblNombreProveedor = new javax.swing.JLabel();
        txtNombreProveedor = new javax.swing.JTextField();
        lblNombreProducto = new javax.swing.JLabel();
        txtNombreProducto = new javax.swing.JTextField();
        btbCancelarCotizacion = new javax.swing.JButton();
        lblCategoriaProducto = new javax.swing.JLabel();
        txtCategoriaProudcto = new javax.swing.JTextField();
        lblMarca = new javax.swing.JLabel();
        txtMarca = new javax.swing.JTextField();
        lblCantidadActual = new javax.swing.JLabel();
        txtCantidadActual = new javax.swing.JTextField();
        lblCantidadRequeridad = new javax.swing.JLabel();
        txtCantidadRequeridad = new javax.swing.JTextField();
        lblDescripcion = new javax.swing.JLabel();
        jScrollDescri = new javax.swing.JScrollPane();
        txtDescripcion = new javax.swing.JTextArea();
        jScrollPanelCotiza = new javax.swing.JScrollPane();
        tablaCotizacion = new javax.swing.JTable();
        btbEnviarSolicitud = new javax.swing.JButton();
        FondoPantalla = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(102, 102, 102));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Georgia", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Sistema de inventario");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 10, -1, -1));

        jLabel3.setFont(new java.awt.Font("Georgia", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Cotización");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 30, 110, -1));

        btbVolverProvvedor.setFont(new java.awt.Font("Georgia", 1, 14)); // NOI18N
        btbVolverProvvedor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/casita.png"))); // NOI18N
        btbVolverProvvedor.setText("Volver");
        btbVolverProvvedor.addActionListener(this::btbVolverProvvedorActionPerformed);
        jPanel1.add(btbVolverProvvedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 670, 140, 30));

        btbGenerarCotizacion.setFont(new java.awt.Font("Georgia", 1, 14)); // NOI18N
        btbGenerarCotizacion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/Acepatar.png"))); // NOI18N
        btbGenerarCotizacion.setText("Generar Cotización");
        btbGenerarCotizacion.addActionListener(this::btbGenerarCotizacionActionPerformed);
        jPanel1.add(btbGenerarCotizacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(1050, 660, 210, 30));

        lblCorreo.setFont(new java.awt.Font("Georgia", 1, 18)); // NOI18N
        lblCorreo.setForeground(new java.awt.Color(255, 255, 255));
        lblCorreo.setText("Dirección del proveedor:");
        jPanel1.add(lblCorreo, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 120, -1, 30));

        txtCorreo.setEditable(false);
        txtCorreo.setFont(new java.awt.Font("Georgia", 0, 14)); // NOI18N
        txtCorreo.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(txtCorreo, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 120, 280, 30));

        lblNombreProveedor.setFont(new java.awt.Font("Georgia", 1, 18)); // NOI18N
        lblNombreProveedor.setForeground(new java.awt.Color(255, 255, 255));
        lblNombreProveedor.setText("Nombre Proveedor:");
        jPanel1.add(lblNombreProveedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 190, -1, 30));

        txtNombreProveedor.setEditable(false);
        txtNombreProveedor.setFont(new java.awt.Font("Georgia", 1, 14)); // NOI18N
        txtNombreProveedor.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(txtNombreProveedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 190, 270, 30));

        lblNombreProducto.setFont(new java.awt.Font("Georgia", 1, 18)); // NOI18N
        lblNombreProducto.setForeground(new java.awt.Color(255, 255, 255));
        lblNombreProducto.setText("Nombre del Proudcto:");
        jPanel1.add(lblNombreProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 120, -1, -1));

        txtNombreProducto.setEditable(false);
        txtNombreProducto.setFont(new java.awt.Font("Georgia", 1, 14)); // NOI18N
        txtNombreProducto.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(txtNombreProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 120, 220, -1));

        btbCancelarCotizacion.setFont(new java.awt.Font("Georgia", 1, 14)); // NOI18N
        btbCancelarCotizacion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/boton-x.png"))); // NOI18N
        btbCancelarCotizacion.setText("Cancelar Cotización");
        jPanel1.add(btbCancelarCotizacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(1050, 660, -1, 30));

        lblCategoriaProducto.setFont(new java.awt.Font("Georgia", 1, 18)); // NOI18N
        lblCategoriaProducto.setForeground(new java.awt.Color(255, 255, 255));
        lblCategoriaProducto.setText("Categoria:");
        jPanel1.add(lblCategoriaProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 190, -1, -1));

        txtCategoriaProudcto.setEditable(false);
        txtCategoriaProudcto.setFont(new java.awt.Font("Georgia", 1, 14)); // NOI18N
        txtCategoriaProudcto.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(txtCategoriaProudcto, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 190, 220, -1));

        lblMarca.setFont(new java.awt.Font("Georgia", 1, 18)); // NOI18N
        lblMarca.setForeground(new java.awt.Color(255, 255, 255));
        lblMarca.setText("Marca:");
        jPanel1.add(lblMarca, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 240, -1, -1));

        txtMarca.setEditable(false);
        txtMarca.setFont(new java.awt.Font("Georgia", 1, 14)); // NOI18N
        txtMarca.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(txtMarca, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 240, 220, -1));

        lblCantidadActual.setFont(new java.awt.Font("Georgia", 1, 18)); // NOI18N
        lblCantidadActual.setForeground(new java.awt.Color(255, 255, 255));
        lblCantidadActual.setText("Cantidad Actual:");
        jPanel1.add(lblCantidadActual, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 290, -1, -1));

        txtCantidadActual.setEditable(false);
        txtCantidadActual.setFont(new java.awt.Font("Georgia", 1, 14)); // NOI18N
        txtCantidadActual.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtCantidadActual.setToolTipText("");
        jPanel1.add(txtCantidadActual, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 290, 220, -1));

        lblCantidadRequeridad.setFont(new java.awt.Font("Georgia", 1, 18)); // NOI18N
        lblCantidadRequeridad.setForeground(new java.awt.Color(255, 255, 255));
        lblCantidadRequeridad.setText("Cantidad Requerida:");
        jPanel1.add(lblCantidadRequeridad, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 250, -1, 40));

        txtCantidadRequeridad.setFont(new java.awt.Font("Georgia", 1, 14)); // NOI18N
        txtCantidadRequeridad.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(txtCantidadRequeridad, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 260, 250, -1));

        lblDescripcion.setFont(new java.awt.Font("Georgia", 1, 18)); // NOI18N
        lblDescripcion.setForeground(new java.awt.Color(255, 255, 255));
        lblDescripcion.setText("Descripción:");
        lblDescripcion.setToolTipText("");
        jPanel1.add(lblDescripcion, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 320, -1, 20));

        txtDescripcion.setColumns(20);
        txtDescripcion.setFont(new java.awt.Font("Georgia", 1, 14)); // NOI18N
        txtDescripcion.setRows(5);
        jScrollDescri.setViewportView(txtDescripcion);

        jPanel1.add(jScrollDescri, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 320, 270, 160));

        tablaCotizacion.setFont(new java.awt.Font("Georgia", 0, 14)); // NOI18N
        tablaCotizacion.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPanelCotiza.setViewportView(tablaCotizacion);

        jPanel1.add(jScrollPanelCotiza, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 170, 1260, 330));

        btbEnviarSolicitud.setFont(new java.awt.Font("Georgia", 1, 14)); // NOI18N
        btbEnviarSolicitud.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/Acepatar.png"))); // NOI18N
        btbEnviarSolicitud.setText("  Enviar Solicitud");
        btbEnviarSolicitud.addActionListener(this::btbEnviarSolicitudActionPerformed);
        jPanel1.add(btbEnviarSolicitud, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 530, 230, 30));

        FondoPantalla.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/fondoMordeno.jpg"))); // NOI18N
        jPanel1.add(FondoPantalla, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1380, 740));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1383, 740));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btbGenerarCotizacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btbGenerarCotizacionActionPerformed

    }//GEN-LAST:event_btbGenerarCotizacionActionPerformed

    private void btbVolverProvvedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btbVolverProvvedorActionPerformed
   
    }//GEN-LAST:event_btbVolverProvvedorActionPerformed

    private void btbEnviarSolicitudActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btbEnviarSolicitudActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btbEnviarSolicitudActionPerformed

        
ModeloDAO.CotizaciónDao Dao = new CotizaciónDao();
Funciones.Alerte_De_Stock AL = new Alerte_De_Stock();
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel FondoPantalla;
    public static javax.swing.JButton btbCancelarCotizacion;
    public static javax.swing.JButton btbEnviarSolicitud;
    public static javax.swing.JButton btbGenerarCotizacion;
    public static javax.swing.JButton btbVolverProvvedor;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    public static javax.swing.JScrollPane jScrollDescri;
    public static javax.swing.JScrollPane jScrollPanelCotiza;
    public static javax.swing.JLabel lblCantidadActual;
    public static javax.swing.JLabel lblCantidadRequeridad;
    public static javax.swing.JLabel lblCategoriaProducto;
    public static javax.swing.JLabel lblCorreo;
    public static javax.swing.JLabel lblDescripcion;
    public static javax.swing.JLabel lblMarca;
    public static javax.swing.JLabel lblNombreProducto;
    public static javax.swing.JLabel lblNombreProveedor;
    public static javax.swing.JTable tablaCotizacion;
    public static javax.swing.JTextField txtCantidadActual;
    public static javax.swing.JTextField txtCantidadRequeridad;
    public static javax.swing.JTextField txtCategoriaProudcto;
    public static javax.swing.JTextField txtCorreo;
    public static javax.swing.JTextArea txtDescripcion;
    public static javax.swing.JTextField txtMarca;
    public static javax.swing.JTextField txtNombreProducto;
    public static javax.swing.JTextField txtNombreProveedor;
    // End of variables declaration//GEN-END:variables
}
