package controlador;



import java.util.List;
import java.util.Map;

import javax.swing.SwingWorker;

import interfazVisual.VentanaPrincipal;
import negocio.Persona;
import negocio.SolverEquipo;

public class EquipoControlador {
    private VentanaPrincipal vista;

    public EquipoControlador(VentanaPrincipal vista) {
        this.vista = vista;
    }

    public void resolverEquipo(List<Persona> personas, Map<String, Integer> requerimientos, List<String[]> incompatibilidades) {
        vista.actualizarEstado("Calculando con SwingWorker...", false);

        // Definimos el SwingWorker: <Tipo de Resultado Final, Tipo de Datos Intermedios>
        SwingWorker<List<Persona>, Void> trabajador = new SwingWorker<>() {
            
            // 1. Esto corre en el hilo secundario (Segundo Plano)
            @Override
            protected List<Persona> doInBackground() throws Exception {
                SolverEquipo solucionador = new SolverEquipo(personas, requerimientos);
                
                for (String[] par : incompatibilidades) {
                    // ... (registro de incompatibilidades igual que antes) ...
                }
                
                // Ejecuta el backtracking pesado
                return solucionador.resolver();
            }

            // 2. Esto corre automáticamente en el hilo de la UI al terminar (UI Thread)
            @Override
            protected void done() {
                try {
                    // .get() recupera lo que devolvió doInBackground()
                    List<Persona> resultado = get(); 
                    
                    if (resultado == null || resultado.isEmpty()) {
                        vista.mostrarResultado(resultado, "No se encontró un equipo que cumpla los requisitos.");
                    } else {
                        vista.mostrarResultado(resultado, "¡Equipo calculado exitosamente!");
                    }
                } catch (Exception e) {
                    vista.mostrarResultado(null, "Ocurrió un error en el cálculo: " + e.getMessage());
                } finally {
                    vista.actualizarEstado("Listo", true);
                }
            }
        };

        // Al igual que con un Thread común, se le da arranque
        trabajador.execute();
    }
}