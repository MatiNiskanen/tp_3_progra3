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

        SwingWorker<List<Persona>, Void> trabajador = new SwingWorker<>() {
            
            @Override
            protected List<Persona> doInBackground() throws Exception {
                SolverEquipo solucionador = new SolverEquipo(personas, requerimientos);
                
                for (String[] par : incompatibilidades) {
                    Persona p1 = buscarPersonaPorNombre(personas, par[0]);
                    Persona p2 = buscarPersonaPorNombre(personas, par[1]);
                    if (p1 != null && p2 != null) {
                        solucionador.registrarIncompatibilidad(p1, p2);
                    }
                }
                
                return solucionador.resolver();
            }

            @Override
            protected void done() {
                try {
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

        trabajador.execute();
    }

    private Persona buscarPersonaPorNombre(List<Persona> personas, String nombre) {
        for (Persona p : personas) {
            if (p.getNombre().equals(nombre)) {
                return p;
            }
        }
        return null;
    }
}