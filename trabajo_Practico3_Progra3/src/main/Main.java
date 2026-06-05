package main;

import java.awt.EventQueue;

import javax.swing.UIManager;

import interfazVisual.VentanaPrincipal;

public class Main {
    public static void main(String[] args) {
        // Aseguramos que la interfaz gráfica se dibuje en el hilo de Swing correspondiente
        EventQueue.invokeLater(() -> {
            try {
                // Forzar el estilo nativo del Sistema Operativo para que se vea moderno
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                // Si falla, usa el look por defecto de Java
            }
            
            VentanaPrincipal ventana = new VentanaPrincipal();
            ventana.setVisible(true);
        });
    }
}
