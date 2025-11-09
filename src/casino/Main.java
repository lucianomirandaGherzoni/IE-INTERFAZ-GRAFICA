/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package casino;

import casino.controlador.ControladorJuego;
import casino.vista.VentanaConfiguracion;
/**
 *
 * @author BANGHO
 */
public class Main {
    public static void main(String[] args) {
        //Look and feel para mejor visual
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            // Si falla, usa el Look and Feel por defecto
        }
        
        //Abrir ventana de configuración
        java.awt.EventQueue.invokeLater(() -> {
            VentanaConfiguracion ventana = new VentanaConfiguracion();
            ventana.setVisible(true);
        });
        
    }
}
