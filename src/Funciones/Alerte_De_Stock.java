package Funciones;

import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;
import  Vista_GestionInventario.InventarioLiquido;
import  Vista_GestionInventario.InventarioSolido;

import Vista_Gestionar_Proveedor.Vista_Cotizaciónn;
import Vista_GestionInventario.InventarioUnidad;

/**
 * Renderizador personalizado para alertas de stock y cotización
 * @author Adrian
 */
public class Alerte_De_Stock extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable tabla, Object valor,
            boolean estaSeleccionado, boolean tieneFoco, int fila, int columna) {

        Component componente = super.getTableCellRendererComponent(tabla, valor, estaSeleccionado, tieneFoco, fila, columna);

        // Centrar el texto en todas las celdas
        setHorizontalAlignment(SwingConstants.CENTER);

        // ====================== TABLAS DE INVENTARIO (columna 2) ======================
        if (tabla.equals(InventarioLiquido.TablaLiquido) ||
            tabla.equals(InventarioUnidad.TablaUnidad) ||
            tabla.equals(InventarioSolido.TablaSolido)) {

            if (columna == 2) {
                aplicarLogicaInventario(componente, tabla, fila);
            } else {
                restablecerColor(componente);
            }
        } 
        // ====================== TABLA DE COTIZACIÓN (columna 3) ======================
        else if (tabla.equals(Vista_Cotizaciónn.tablaCotizacion)) {

            if (columna == 3) {
                aplicarLogicaCotizacion(componente, tabla, fila);
            } else {
                restablecerColor(componente);
            }
        } 
        // Otras tablas → color por defecto
        else {
            restablecerColor(componente);
        }

        return componente;
    }

    // ====================== LÓGICA PARA TABLAS DE INVENTARIO ======================
    private void aplicarLogicaInventario(Component componente, JTable tabla, int fila) {
        try {
            String cantidadStr = getValorSeguro(tabla, fila, 2);
            String stockMinStr = getValorSeguro(tabla, fila, 3);
            String stockMaxStr = getValorSeguro(tabla, fila, 4);

            int cantidad = Integer.parseInt(cantidadStr);
            int stockMin = Integer.parseInt(stockMinStr);
            int stockMax = Integer.parseInt(stockMaxStr);

            if (cantidad <= stockMin) {
                componente.setBackground(Color.RED);
                componente.setForeground(Color.WHITE);
            } else if (cantidad > stockMin && cantidad < stockMax - 10) {
                componente.setBackground(Color.YELLOW);
                componente.setForeground(Color.BLACK);
            } else {
                componente.setBackground(Color.GREEN);
                componente.setForeground(Color.BLACK);
            }
        } catch (NumberFormatException e) {
            restablecerColor(componente);
            mostrarErrorInventario(fila);
        }
    }

    // ====================== LÓGICA PARA TABLA DE COTIZACIÓN ======================
    private void aplicarLogicaCotizacion(Component componente, JTable tabla, int fila) {
        try {
            String cantidadStr = getValorSeguro(tabla, fila, 3); // columna 3 en cotización

            int cantidad = Integer.parseInt(cantidadStr);

            if (cantidad >= 0 && cantidad <= 10) {
                componente.setBackground(Color.RED);
                componente.setForeground(Color.WHITE);   // Mejor contraste
            } else {
                componente.setBackground(Color.GREEN);   // Mayor a 10 → Verde
                componente.setForeground(Color.BLACK);
            }
        } catch (NumberFormatException e) {
            restablecerColor(componente);
        }
    }

    // ====================== MÉTODOS AUXILIARES ======================

    private String getValorSeguro(JTable tabla, int fila, int columna) {
        Object valor = tabla.getValueAt(fila, columna);
        return valor != null ? valor.toString().trim() : "0";
    }

    private void restablecerColor(Component componente) {
        componente.setBackground(Color.WHITE);
        componente.setForeground(Color.BLACK);
    }

    private void mostrarErrorInventario(int fila) {
        JOptionPane.showMessageDialog(null, 
            "Error: Valor no numérico detectado en la fila " + fila + 
            "\nVerifique los datos de Cantidad, Stock Min y Stock Max.", 
            "Error en Alerta de Stock", JOptionPane.ERROR_MESSAGE);
    }

    // ====================== MÉTODOS PARA APLICAR EL RENDERER ======================

    public void AlertaLiquido() {
        aplicarRenderer(InventarioLiquido.TablaLiquido, 2);
    }

    public void AlertaUnida() {
        aplicarRenderer(InventarioUnidad.TablaUnidad, 2);
    }

    public void AlertaSolido() {
        aplicarRenderer(InventarioSolido.TablaSolido, 2);
    }

    public void AlertaCotizacion() {
        aplicarRenderer(Vista_Cotizaciónn.tablaCotizacion, 3);  // columna 3 en cotización
    }

    /**
     * Método auxiliar para aplicar el renderer
     */
    private void aplicarRenderer(JTable tabla, int columnaCantidad) {
        if (tabla != null && tabla.getColumnCount() > columnaCantidad) {
            
            tabla.getColumnModel().getColumn(columnaCantidad).setCellRenderer(this);
            
            tabla.revalidate();
            tabla.repaint();

            // Forzar repintado seguro en el Event Dispatch Thread
            SwingUtilities.invokeLater(() -> {
                tabla.repaint();
                if (tabla.getParent() instanceof javax.swing.JScrollPane scrollPane) {
                    scrollPane.repaint();
                }
            });
        }
    }
}