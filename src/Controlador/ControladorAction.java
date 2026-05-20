package Controlador;

import Modelo.ConexiónBD;

import Vista_Usuari_Empleado.Login_Ingreso;
import Vista_Usuari_Empleado.Menu_Sistema;
import controladorLogin.ControladorLogin;
import dictionary.AutoCorrectorGlobal;
import dictionary.Diccionario;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import javax.swing.SwingUtilities;

import java.awt.event.ItemEvent;




public class ControladorAction implements ActionListener {
    
    public static void main(String[] args) throws ClassNotFoundException {
        controladorVisual ct = new controladorVisual();
        ct.Estilo();
        ConexiónBD cone = new ConexiónBD();
        cone.conectar();
        Diccionario.cargarDiccionario("src/dictionary/autopartes.dic");
        Login_Ingreso login = new Login_Ingreso();
        AutoCorrectorGlobal.aplicar(login);
        new ControladorAction(login);
        SwingUtilities.invokeLater(() -> login.setVisible(true));
    }
   
    private final Login_Ingreso vistaLogin;
    private Menu_Sistema menu;
 

  
  
  
   
  private Controlador_Proveedor controlador_Proveedor;


 

   
   private ControladorMarca controladorMarca;
   private  ControladorCategoria controladorCategoria;
   private  ControladorConsultarUsuarios controladorConsultarUsuarios;
   private  Controlador_Empleado controlador_Empleado;
   private ControladorAsignarUsuario controladorAsignarUsuario;
   private Controlador_Cotizacion controlador_Cotizacion;
   private  Controlador_Libro_Mayor controlador_Libro_Mayor;
   private  Controlador_Libro_Diario controlador_Libro_Diario;
   private ControladorRegistrarProductoLiquido controladorRegistrarProductoLiquido;
   private  Controlador_Registro_Producto_Solido controlador_Registro_Producto_Solido;
   private  Controlador_Producto_Unidad controlador_Producto_Unidad;
   private  Controlador_Inventario_Liquido controlador_Inventario_Liquido;
   private  Controlador_Inventario_Solido controlador_Inventario_Solido;
   private Controlador_Liquido_Salida controlador_Liquido_Salida;
   private Controlador_Inventario_Unidad controlador_Inventario_Unidad;
   private  Controlador_Salida_Solido controlador_Salida_Solido;
   private  Controlador_Salida_Unidad controlador_Salida_Unidad;
   private  ControladorAjuste_Liquido controladorAjuste_Liquido;
  private  Controlador_Ajuste_Solido controlador_Ajuste_Solido;
  private Controlador_Ajuste_Unidad controlador_Ajuste_Unidad;
  private Controlador_Almacen controlador_Almacen;
  private Controlador_Cuentas_Contables  controlador_Cuentas_Contables;
  private  Controlador_Pagar_Compras controlador_Pagar_Compras;
   // esta variable se ultiliza en este caso para mostrar mostrar la contraseña si el usuario lo desea
   private char echoCharOriginal;
   
    public ControladorAction(Login_Ingreso login) {
        this.vistaLogin = login;
        this.vistaLogin.BtbIniciar.addActionListener(this);
       
   
    this.echoCharOriginal = vistaLogin.txtPasContraseña.getEchoChar();
    
    
    
     // Este es un pequeño metodos para mostrar la contraseña si el usuario lo deseas
    this.vistaLogin.MostrarContraseña.addItemListener(e -> {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            vistaLogin.txtPasContraseña.setEchoChar((char) 0);
        } else {
            vistaLogin.txtPasContraseña.setEchoChar(echoCharOriginal);
        }
    });
    
       
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        
       /*
        Metodos para controlar el Login y el Menu en el modelos MVC con los Metodos ActionPerfomen
        
        |
        |
        |
        |
        |
        |
        |
        |
       \|/
        V
        */
       
       
       
       
        if (e.getSource() == vistaLogin.BtbIniciar) {
            ControladorLogin crt = new ControladorLogin();
            try {
                crt.BtbIniciarActionPerformed(vistaLogin);
                
                this.menu = Menu_Sistema.getInstancia();
                
      
                
               
                this.menu.MenuCategoria.addActionListener(this);
                this.menu.MenuMarca.addActionListener(this);
                this.menu.Menu_Reportes_Sistema.addActionListener(this);
                     
                     this.menu.Jmenu_Abri_Empleado.addActionListener(this);
                     this.menu.Jmenu_abrir_Proveedor.addActionListener(this);
                     this.menu.Menu_cotizacion.addActionListener(this);
                   this.menu.BotonCerraSeccion.addActionListener(this);
                   this.menu.jMenuIuSUARIOS.addActionListener(this);
             
                
                 
                   
                   // inventario
             
                   
                   //Libro contables
                   this.menu.JmenuLibroMayor.addActionListener(this);
                   this.menu.jMenuLibroDIARIO.addActionListener(this);
                   this.menu.jMenuItemCuentasContables.addActionListener(this);
                   
                   // Productos
                   this.menu.jMenuProductoLiquido.addActionListener(this);
                   this.menu.jMenuProductoSolido.addActionListener(this);
                   this.menu.jMenuRegistrarUnidad.addActionListener(this);
                
                   //Inventario 
                   this.menu.jMenuInventarioLiquido.addActionListener(this);
                   this.menu.jMenuItemInventarioSolido.addActionListener(this);
                   this.menu.jMenuIInventarioUnidad.addActionListener(this);
                   // Almecen
                   this.menu.jMenuItemLiquidoSalida.addActionListener(this);
                   this.menu.jMenuISALIDA_SOLIDO.addActionListener(this);
                   this.menu.jMenuItemUnidaSalida.addActionListener(this);
                   this.menu.jMenuItemAjusteLiquiod.addActionListener(this);
                   this.menu.jMenuItemAjusteSolido.addActionListener(this);
                   this.menu.jMenuItemAjusteUnidad.addActionListener(this);
                      this.menu.jMenuAbrirAlmacen.addActionListener(this);
                      
                      // Pagar Compras
                      this.menu.jMenuItemPagarProveedor.addActionListener(this);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    
        else if (e.getSource() == menu.BotonCerraSeccion) {
            controladorVisual cts = new controladorVisual();
            cts.CerrarSeccion(menu);
            vistaLogin.setVisible(true);
        }
       
        
        /*
        
        Metodos de Action Listener para MVC de la Gestion de Empleados y Usuarios
        
                 
        |
        |
        |
        |
        |
        |
        |
       \|/
        V
        
        */
        else if (e.getSource() == menu.jMenuItemPagarProveedor){
            if (controlador_Pagar_Compras == null) {
                
                controlador_Pagar_Compras = new Controlador_Pagar_Compras(menu);
                
            }
            controlador_Pagar_Compras.MostrarVista();
        }
        
        else if (e.getSource() == menu.jMenuItemCuentasContables){
            if (controlador_Cuentas_Contables == null) {
                controlador_Cuentas_Contables = new Controlador_Cuentas_Contables(menu);
                
            }
            
            controlador_Cuentas_Contables.MostrarVista();
        }
        
        
        else if (e.getSource() == menu.jMenuAbrirAlmacen){
            if (controlador_Almacen == null) {
                
                controlador_Almacen = new Controlador_Almacen(menu);
                
            }
            
            controlador_Almacen.MostraVista();
        }
        
        else if (e.getSource() == menu.jMenuItemAjusteUnidad){
            if (controlador_Ajuste_Unidad == null) {

            controlador_Ajuste_Unidad = new Controlador_Ajuste_Unidad(menu);
                
            }
            
            controlador_Ajuste_Unidad.MostrarVistaAjusteUnidad();
        }
        
        else if (e.getSource() == menu.jMenuItemAjusteSolido){
            
            if (controlador_Ajuste_Solido == null) {
                
                controlador_Ajuste_Solido = new Controlador_Ajuste_Solido(menu);
                
            }
            
            controlador_Ajuste_Solido.MOstrarVista();
            
        }
        
        else if (e.getSource() == menu.jMenuItemAjusteLiquiod){
            if (controladorAjuste_Liquido == null) {
                
                controladorAjuste_Liquido = new ControladorAjuste_Liquido(menu);
                
            }
            
            controladorAjuste_Liquido.MostrarVistaAjusteLiquido();
        }
        
        else if (e.getSource() == menu.jMenuItemLiquidoSalida){
            if (controlador_Liquido_Salida == null) {
                
                controlador_Liquido_Salida = new Controlador_Liquido_Salida(menu);
                
            }
  
            controlador_Liquido_Salida.MostrarVista();
            
        }
        
        
        else if (e.getSource() == menu.jMenuISALIDA_SOLIDO) {
            if (controlador_Salida_Solido == null) {
                
                controlador_Salida_Solido = new Controlador_Salida_Solido(menu);
                
            }
            
            controlador_Salida_Solido.MostrarVista();
            
        }
        
        
        else if (e.getSource() == menu.jMenuItemUnidaSalida) {
            if (controlador_Salida_Unidad == null) {
                
                controlador_Salida_Unidad = new Controlador_Salida_Unidad(menu);
            }
            
            
            controlador_Salida_Unidad.MostrarVistaSalidaUnidad();
        }
        
        
        
        
        
     
     else if (e.getSource() == menu.Jmenu_Abri_Empleado) {
    if (controlador_Empleado == null) {
        controlador_Empleado = new Controlador_Empleado(menu);
    }
    controlador_Empleado.mostrarVista();
}
              /*
        
        Metodos Inventario Liquido
        
                 
        |
        |
        |
        |
        |
        |
        |
       \|/
        V
        
        */
     else if (e.getSource() == menu.jMenuInventarioLiquido){
            if (controlador_Inventario_Liquido == null) {
                controlador_Inventario_Liquido = new Controlador_Inventario_Liquido(menu);
                
            }
            controlador_Inventario_Liquido.MostrarVistaInventarioLiquido();
     }  
        
     
     
     else if (e.getSource() == menu.jMenuItemInventarioSolido) {
         
            if (controlador_Inventario_Solido == null) {
                
                controlador_Inventario_Solido = new Controlador_Inventario_Solido(menu);
                
            }
            
            controlador_Inventario_Solido.MostrarVistaInventarioSOLIDO();
            
        }
     
     
     
     else if (e.getSource() == menu.jMenuIInventarioUnidad){
            if (controlador_Inventario_Unidad == null) {
                
                
                controlador_Inventario_Unidad = new Controlador_Inventario_Unidad(menu);
            }
            
            controlador_Inventario_Unidad.mostrarVistaUnidad();
     }
     
     
     
     
        /*
        Metodos para la gestion de Consultar el Usuarios y cambiarle los previlegios ect
        |
        |
        |
        |
        |
        |
        |
        |
       \|/
        V	
        
        
        */
      
        else if (e.getSource() == menu.jMenuIuSUARIOS){
            
            if (controladorConsultarUsuarios == null) {
                controladorConsultarUsuarios = new ControladorConsultarUsuarios(menu);
                
            }
            controladorConsultarUsuarios.MostrarVistaConsultarUsuario();
            
        }
        
      
        
        /*
        
        Metodos de Gestionar Marca  estos metodos de aqui en adelante son metodos de acticon Perfomen MVC
        
        |
        |
        |
        |
        |
        |
        |
        |
       \|/
        V
        
        
        */
        
 


  else if (e.getSource() == menu.MenuMarca) {
    if (controladorMarca == null) {
        controladorMarca = new ControladorMarca(menu);
    }
    controladorMarca.mostrarVista();
}
    
        
        /*
        
        
        Metodos de Acciones de mvc de Categoria
        |
        |
        |
        |
        |
        |
        |
        |
       \|/
        V
        
        
        */
  else if (e.getSource() == menu.MenuCategoria){
            if (controladorCategoria == null) {
                controladorCategoria = new ControladorCategoria(menu);
      
            }
            controladorCategoria.MostrarVista();
  }
        
        
        /*
        Metodos para Gestionar Proveedor 
        |
        |
        |
        |
        |
        |
        |
        |
       \|/
        V	
        
        */
        else if (e.getSource() == menu.Jmenu_abrir_Proveedor){
            
            if (controlador_Proveedor == null) {
                controlador_Proveedor = new Controlador_Proveedor(menu);
                
            }
           
          controlador_Proveedor.MostrarVistaProveedor();
            
           
            
        }

     
        
        
        /*
        Producto lIQUIDOS 
        |
        |
        |
        |
        |
        |
        |
        |
       \|/
        V
        */
        
        
        else if (e.getSource() == menu.jMenuProductoLiquido){
            
            if (controladorRegistrarProductoLiquido == null) {
                
                controladorRegistrarProductoLiquido = new ControladorRegistrarProductoLiquido(menu);
                
            }
            
            controladorRegistrarProductoLiquido.MostrarVista();
        }
        
        
        /*
        
         Registro de Producto SOlido
        |
        |
        |
        |
        |
        |
        |
        |
       \|/
        V
        */
        
        else if (e.getSource() == menu.jMenuProductoSolido){
            
            if (controlador_Registro_Producto_Solido == null) {

controlador_Registro_Producto_Solido = new Controlador_Registro_Producto_Solido(menu);
                
            }
            
            controlador_Registro_Producto_Solido.MostrarVistaRegistroSolido();
            
        }
        
        
        else if (e.getSource() == menu.jMenuRegistrarUnidad){
            
            if (controlador_Producto_Unidad == null) {
                
                controlador_Producto_Unidad = new Controlador_Producto_Unidad(menu);
                
            }
            
            controlador_Producto_Unidad.MostrarVista();
        }
        
        /*
            Metodos para cotizacion 
        |
        |
        |
        |
        |
        |
        |
        |
       \|/
        V		
        */
        
        else if (e.getSource() == menu.Menu_cotizacion){
            
            if (controlador_Cotizacion == null) {
                
                controlador_Cotizacion = new Controlador_Cotizacion(menu);
                
            }
            
            controlador_Cotizacion.MostrarVistaCotizacion();
            
        }
        
        
        
        /*
        Metodos para libro mayor
       
        |
        |
        |
        |
        |
        |
        |
        |
       \|/
        V
        */
        
        else if (e.getSource() == menu.JmenuLibroMayor){
            if (controlador_Libro_Mayor == null) {
                
                controlador_Libro_Mayor = new Controlador_Libro_Mayor(menu);
                
            }
            
            controlador_Libro_Mayor.MostrarVista();
        }
        
        
        /*
        
        Metodos Libro Diario
        |
        |
        |
        |
        |
        |
        |
        |
       \|/
        V
        */
        else if (e.getSource() == menu.jMenuLibroDIARIO){
            if (controlador_Libro_Diario == null) {
                
                controlador_Libro_Diario = new Controlador_Libro_Diario(menu);
                    
                
            }
            controlador_Libro_Diario.mostrarVista();
        }
        
        
        /*
        Metodos de Action Perfomen para el Modulos de Gestionar Productos
        |
        |
        |
        |
        |
        |
        |
        |
       \|/
        V	
        */
        
       
        
       /*
     Metodos para la salida del alamcen
        |
        |
        |
        |
        |
        |
        |
       \|/
        V
    */
   
        
        
     
        
        
    
    
    }
    
    


}