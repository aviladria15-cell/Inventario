package Funciones;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;
import javax.swing.*;
import java.awt.*;
import Vista_Usuari_Empleado.RecuperarContrasena;
import Vista_Gestionar_Proveedor.Vista_Cotizaciónn;

public class Enviar_Codigo_Correo {

    private static final String REMITENTE = "sistemadeautopartesrac2012c.a@gmail.com";
    private static final String CLAVE = "Krxd avyz mdjt lsdx";

    /**
     * Envía un correo de verificación personalizado con el nombre del usuario.
     * ¡NO MODIFICAR ESTE MÉTODO!
     */
    public static void enviarCodigoRapido(String destinatario, String nombreUsuario, String codigo) {
        // Configuración SMTP de Gmail
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(REMITENTE, CLAVE);
            }
        });

        // Contenido del mensaje HTML
        String contenidoHTML = """
            <html>
                <body style='font-family: Arial, sans-serif; color:#333; margin: 20px;'>
                    <h2 style='color: #2c3e50;'>Recuperación de contraseña</h2>
                    <p>Hola <strong>%s</strong>,</p>
                    <p>Has solicitado recuperar tu contraseña. Tu código de verificación es:</p>
                    <div style='text-align:center; margin:30px 0;'>
                        <h1 style='background:#007bff; color:white; display:inline-block; padding:15px 30px; border-radius:10px; letter-spacing:5px;'>
                            %s
                        </h1>
                    </div>
                    <p><strong>Este código expira en 5 minutos.</strong></p>
                    <p>Si no solicitaste este cambio, ignora este mensaje.</p>
                    <hr style='border: 1px solid #eee; margin:30px 0;'>
                    <p style='color:#7f8c8d; font-size:12px;'>
                        <em>Sistema de Autopartes RAC 2012 C.A.</em>
                    </p>
                </body>
            </html>
            """.formatted(nombreUsuario, codigo);

        // Crear diálogo de carga
        JDialog dialogoCarga = crearDialogoCarga("Enviando código de verificación...");

        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                try {
                    Message message = new MimeMessage(session);
                    message.setFrom(new InternetAddress(REMITENTE, "Sistema de Autopartes RAC 2012 C.A."));
                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
                    message.setSubject("Código de verificación para " + nombreUsuario);
                    message.setContent(contenidoHTML, "text/html; charset=utf-8");
                    Transport.send(message);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return null;
            }

            @Override
            public void done() {
                dialogoCarga.dispose();
                try {
                    get();
                    JOptionPane.showMessageDialog(null, "✅ Código enviado correctamente");

                    // Ocultar campos iniciales y mostrar código
                    RecuperarContrasena.lblNombreUsuario.setVisible(false);
                    RecuperarContrasena.lblCorreo.setVisible(false);
                    RecuperarContrasena.txtNombreUsuarioRecuperar.setVisible(false);
                    RecuperarContrasena.txtCorreoRecuperar.setVisible(false);
                    RecuperarContrasena.btbVerificar.setVisible(false);

                    RecuperarContrasena.lblCodigoDeVerificacion.setVisible(true);
                    RecuperarContrasena.txtCodigoDeVerificacion.setVisible(true);
                    RecuperarContrasena.btbConfirmar.setVisible(true);

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Sin conexión a internet");
                }
            }
        };

        worker.execute();
        dialogoCarga.setVisible(true);
    }

    /**
     * NUEVO MÉTODO: Envía la solicitud de cotización al proveedor por correo
     */
    public  void enviarSolicitudCotizacion() {

        // Obtener datos de la vista
        String correoProveedor = Vista_Cotizaciónn.txtCorreo.getText().trim();
        String nombreProveedor = Vista_Cotizaciónn.txtNombreProveedor.getText().trim();
        String nombreProducto = Vista_Cotizaciónn.txtNombreProducto.getText().trim();
        String categoria = Vista_Cotizaciónn.txtCategoriaProudcto.getText().trim();
        String marca = Vista_Cotizaciónn.txtMarca.getText().trim();
        String cantidadRequerida = Vista_Cotizaciónn.txtCantidadRequeridad.getText().trim();
        String cantidadActual = Vista_Cotizaciónn.txtCantidadActual.getText().trim();
        String descripcion = Vista_Cotizaciónn.txtDescripcion.getText().trim();

        // Validaciones básicas
        if (correoProveedor.isEmpty() || !correoProveedor.contains("@")) {
            JOptionPane.showMessageDialog(null,
                "❌ Por favor ingrese un correo electrónico válido del proveedor.",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (nombreProducto.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                "❌ El nombre del producto es obligatorio.",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Diálogo de carga
        JDialog dialogoCarga = crearDialogoCarga("Enviando solicitud de cotización...");

        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                try {
                    Properties props = new Properties();
                    props.put("mail.smtp.auth", "true");
                    props.put("mail.smtp.starttls.enable", "true");
                    props.put("mail.smtp.host", "smtp.gmail.com");
                    props.put("mail.smtp.port", "587");

                    Session session = Session.getInstance(props, new Authenticator() {
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(REMITENTE, CLAVE);
                        }
                    });

                    // Contenido HTML del correo
                    String contenidoHTML = """
                        <html>
                            <body style='font-family: Arial, sans-serif; color:#333; margin: 20px; background:#f9f9f9;'>
                                <div style='max-width: 650px; margin: 0 auto; background: white; padding: 30px; border-radius: 10px; box-shadow: 0 0 15px rgba(0,0,0,0.1);'>
                                    <h2 style='color: #1e3a8a; text-align: center;'>Solicitud de Cotización</h2>
                                    <hr style='border: 2px solid #1e3a8a;'>
                                    
                                    <p><strong>Proveedor:</strong> %s</p>
                                    <p><strong>Producto:</strong> %s</p>
                                    <p><strong>Categoría:</strong> %s</p>
                                    <p><strong>Marca:</strong> %s</p>
                                    <p><strong>Cantidad Requerida:</strong> %s unidades</p>
                                    <p><strong>Cantidad Actual en Inventario:</strong> %s unidades</p>
                                    
                                    %s
                                    
                                    <hr>
                                    <p style='color:#1e40af;'><strong>Por favor cotizar a la brevedad posible.</strong></p>
                                    <p style='color:#555; font-size:13px;'>
                                        Responder a este correo con precio unitario, condiciones y tiempo de entrega.
                                    </p>
                                </div>
                            </body>
                        </html>
                        """.formatted(
                            nombreProveedor.isEmpty() ? "No especificado" : nombreProveedor,
                            nombreProducto,
                            categoria.isEmpty() ? "No especificada" : categoria,
                            marca.isEmpty() ? "No especificada" : marca,
                            cantidadRequerida.isEmpty() ? "No especificada" : cantidadRequerida,
                            cantidadActual.isEmpty() ? "0" : cantidadActual,
                            descripcion.isEmpty() ? "" : 
                                "<p><strong>Descripción:</strong><br>" + descripcion.replace("\n", "<br>") + "</p>"
                        );

                    Message message = new MimeMessage(session);
                    message.setFrom(new InternetAddress(REMITENTE, "Sistema de Autopartes RAC 2012 C.A."));
                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(correoProveedor));
                    message.setSubject("Solicitud de Cotización - " + nombreProducto);
                    message.setContent(contenidoHTML, "text/html; charset=utf-8");

                    Transport.send(message);

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return null;
            }

            @Override
            public void done() {
                dialogoCarga.dispose();
                try {
                    get();
                    JOptionPane.showMessageDialog(null,
                        "✅ Solicitud de cotización enviada correctamente a " + correoProveedor,
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);

                    // Limpiar los campos
                    limpiarCamposCotizacion();

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null,
                        "❌ Error al enviar la solicitud.\nVerifique su conexión a internet.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };

        worker.execute();
        dialogoCarga.setVisible(true);
    }

    /**
     * Crea el diálogo de carga (usado por ambos métodos)
     */
    private static JDialog crearDialogoCarga(String mensaje) {
        JDialog dialog = new JDialog((Frame) null, "Por favor espere...", true);
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dialog.setSize(380, 130);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setLocationRelativeTo(null);
        dialog.setResizable(false);

        JLabel lblMensaje = new JLabel(mensaje, SwingConstants.CENTER);
        lblMensaje.setFont(new Font("Arial", Font.PLAIN, 14));

        JProgressBar barra = new JProgressBar();
        barra.setIndeterminate(true);

        dialog.add(lblMensaje, BorderLayout.CENTER);
        dialog.add(barra, BorderLayout.SOUTH);

        return dialog;
    }

    /**
     * Limpia todos los campos de la ventana de cotización
     */
    private static void limpiarCamposCotizacion() {
        Vista_Cotizaciónn.txtCantidadActual.setText("");
        Vista_Cotizaciónn.txtCantidadRequeridad.setText("");
        Vista_Cotizaciónn.txtCategoriaProudcto.setText("");
        Vista_Cotizaciónn.txtCorreo.setText("");
        Vista_Cotizaciónn.txtDescripcion.setText("");
        Vista_Cotizaciónn.txtMarca.setText("");
        Vista_Cotizaciónn.txtNombreProducto.setText("");
        Vista_Cotizaciónn.txtNombreProveedor.setText("");
    }
}