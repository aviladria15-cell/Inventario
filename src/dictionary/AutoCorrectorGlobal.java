/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package dictionary;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.util.Set;

public class AutoCorrectorGlobal {

 public static void aplicar(Container contenedor) {
    for (Component comp : contenedor.getComponents()) {

        if (comp instanceof JTextComponent) {
            JTextComponent textComp = (JTextComponent) comp;

            // Revisar si debe aplicar autocorrector
            Object prop = textComp.getClientProperty("autocorrect");
            boolean debeAplicar = textComp.isEditable()
                    && !Boolean.FALSE.equals(prop)
                    && textComp.getClientProperty("autocorrect_aplicado") == null;

            if (debeAplicar) {
                try {
                    AutoCorrector.aplicar(textComp);
                    textComp.putClientProperty("autocorrect_aplicado", true);
                } catch (Exception ex) {
                    System.err.println("⚠️ Error aplicando autocorrector a " 
                                       + textComp.getName() + ": " + ex.getMessage());
                }
            }

        } else if (comp instanceof Container) {
            aplicar((Container) comp); // recursión para paneles internos
        }
    }
}

public static void aplicar(JFrame frame) {
    aplicar(frame.getContentPane());
}

}