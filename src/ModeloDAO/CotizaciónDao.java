
package ModeloDAO;

/**
 *
 * @author avila
 */

import Vista_Gestionar_Proveedor.Vista_Cotizaciónn;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.ArrayList;
import Modelo.Cotizacion;
import Modelo.ConexiónBD;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
public class CotizaciónDao extends ConexiónBD{
    
    
    
     DefaultTableModel modeloCotizacion = new DefaultTableModel();
     
         PreparedStatement ps;
ResultSet rs;
    
    public void TituloCotizacion (){
   
    String Titulo [] = { "Nombre","  Marca", " Categoria","Cantidad Actual","Lote" , "Proveedor","email"};
    modeloCotizacion.setColumnIdentifiers(Titulo);
  Vista_Cotizaciónn.tablaCotizacion.setModel(modeloCotizacion);
 
    
}
    
     private void limpiarTabla(DefaultTableModel modelo) {
    modelo.setRowCount(0);  // elimina todas las filas
}
    
     public void centrarTextoTabla(JTable tabla) {
    DefaultTableCellRenderer centrado = new DefaultTableCellRenderer();
    centrado.setHorizontalAlignment(SwingConstants.CENTER); // mejor usar SwingConstants

    // Recorremos todas las columnas visibles
    for (int i = 0; i < tabla.getColumnModel().getColumnCount(); i++) {
        tabla.getColumnModel().getColumn(i).setCellRenderer(centrado);
    }
}

    
   private  ArrayList<Cotizacion> ListaContizacion ()throws ClassNotFoundException, SQLException{
       ArrayList<Cotizacion> Lista = new ArrayList<>();
       
       try {
           String sql = """
        SELECT 
            p.nombre AS nombre_producto,
            m.nombre AS marca,
            c.nombre AS categoria,
            i.Cantidad_Disponible,
            i.lote,                    -- ← Nuevo: Número del lote
            pr.idProveedor AS nombre,
            pro.email AS email_proveedor
        FROM producto p
        INNER JOIN marca m ON p.idMarca = m.idMarca
        INNER JOIN categoria c ON p.idCategoria = c.idCategoria
        INNER JOIN inventario pr ON pr.idProveedor = pr.idProveedor
        INNER JOIN proveedor pro ON pro.idProveedor = pro.idProveedor                
        INNER JOIN inventario i ON p.idProducto = i.idProducto
        WHERE i.Cantidad_Disponible > 0 
        ORDER BY p.nombre, i.lote;
        """;
           
                 this.conectar();
        ps = this.con.prepareStatement(sql);
        rs = ps.executeQuery();

           while (rs.next()) {
            Cotizacion cot = new Cotizacion();

            cot.setNombreProducto(rs.getString("nombre_producto"));
            cot.setMarca(rs.getString("marca"));
            cot.setCategoria(rs.getString("categoria"));
            cot.setCantidadDisponible(rs.getInt("Cantidad_Disponible"));
            cot.setLote(rs.getString("lote"));
            cot.setProveedor(rs.getString("nombre"));
            cot.setEmailProveedor(rs.getString("email_proveedor"));

            Lista.add(cot);
        }
           
       } catch (SQLException e) {
           JOptionPane.showMessageDialog(null, 
            "Error al obtener lista de cotización:\n" + e.getMessage(), 
            "Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
       }
      return  Lista;
       
   }
    
    
    public  void MostrarCotizacion () throws ClassNotFoundException, SQLException{
        TituloCotizacion();
        limpiarTabla(modeloCotizacion);
        
        ArrayList<Cotizacion> ListaTerminda = ListaContizacion();
        
        modeloCotizacion = (DefaultTableModel) Vista_Cotizaciónn.tablaCotizacion.getModel();
        
            Object[] obj = new Object[7];
            
            for (int i = 0; i <ListaTerminda.size(); i++) {
                Cotizacion cot = ListaTerminda.get(i);
                
                obj [0] = cot.getNombreProducto();
                obj [1] = cot.getMarca();
                obj [2] = cot.getCategoria();
                obj [3] = cot.getCantidadDisponible();
                obj [4] = cot.getLote();
                obj [5] = cot.getProveedor();
                obj [6] = cot.getEmailProveedor();
                
                modeloCotizacion.addRow(obj);
                
            
        }
        Vista_Cotizaciónn.tablaCotizacion.setModel(modeloCotizacion);
        
        centrarTextoTabla(Vista_Cotizaciónn.tablaCotizacion);
    }
    
    
    
    
}
