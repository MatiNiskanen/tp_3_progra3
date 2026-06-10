package controlador;

import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingWorker;
import interfazVisual.VentanaPrincipal;
import negocio.GestorEquipo;
import negocio.Persona;
import negocio.ResultadoSolver;
import negocio.SolverEquipo;
import negocio.SolverHeuristico;

public class EquipoControlador {
    private VentanaPrincipal vista;
    private GestorEquipo gestor;

    public EquipoControlador(VentanaPrincipal vista) {
        this.vista = vista;
        this.gestor = new GestorEquipo();
        actualizarVista();
    }

    
    public void agregarPersona(String nombre, String rol, int nota) {
        if (gestor.agregarPersona(new Persona(nombre, rol, nota))) {
            actualizarVista();
        } else {
        
            vista.mostrarMensaje("La persona ya existe en el sisttema.");
        }
    }

    public void eliminarPersona(int index) {
        gestor.eliminarPersona(index);
        actualizarVista();
    }

    public void modificarPersona(int index, String nuevoRol, int nuevaCalificacion) {
        gestor.modificarPersona(index, nuevoRol, nuevaCalificacion);
        actualizarVista();
        
    }

    public void fijarRequerimientos(int lider, int arq, int prog, int test) {
        gestor.fijarRequerimientos(lider, arq, prog, test);
        actualizarVista();
        vista.mostrarMensaje("Requerimientos actualizados.");
    }

    public void registrarIncompatibilidad(String p1, String p2) {
        if (gestor.registrarIncompatibilidad(p1, p2)) {
            actualizarVista();
        }
        
        else {
            vista.mostrarMensaje("Incompatibilidad inválida o ya registrada.");
        }
    }

    public void eliminarIncompatibilidad(int index) {
        gestor.eliminarIncompatibilidad(index);
        actualizarVista();
    }

    public void cargarDemo() {
        gestor.cargarEscenarioDemo();
        actualizarVista();
        vista.setValoresSpinners(gestor.getRequerimientos());
        vista.mostrarMensaje("Datos de prueba cargados.");
    }

    private void actualizarVista() {
        vista.actualizarListasVisuales(gestor.getListaPersonas(), gestor.getRequerimientos(), gestor.getListaIncompatibilidades());
    }

    public void resolverComparativa() {
        vista.actualizarEstado("Ejecutando resolvedores en segundo plano...", false);

        SwingWorker<List<ResultadoSolver>, Void> trabajador = new SwingWorker<>() {
            @Override
            protected List<ResultadoSolver> doInBackground() throws Exception {
                List<ResultadoSolver> resultados = new ArrayList<>();
                
                SolverEquipo solverExacto = new SolverEquipo(gestor.getListaPersonas(), gestor.getRequerimientos());
                for (String[] par : gestor.getListaIncompatibilidades()) {
                    Persona p1 = buscarPersonaPorNombre(gestor.getListaPersonas(), par[0]);
                    Persona p2 = buscarPersonaPorNombre(gestor.getListaPersonas(), par[1]);
                    if (p1 != null && p2 != null) solverExacto.registrarIncompatibilidad(p1, p2);
                }
                resultados.add(solverExacto.resolver());
            
                SolverHeuristico solverAproximado = new SolverHeuristico(gestor.getListaPersonas(), gestor.getRequerimientos()); // despues lo termino de implementar
                for (String[] par : gestor.getListaIncompatibilidades()) {
                    Persona p1 = buscarPersonaPorNombre(gestor.getListaPersonas(), par[0]);
                    Persona p2 = buscarPersonaPorNombre(gestor.getListaPersonas(), par[1]);
                    if (p1 != null && p2 != null) solverAproximado.registrarIncompatibilidad(p1, p2);
                }
                resultados.add(solverAproximado.resolver());

                return resultados;
            }

            @Override
            protected void done() {
                try {
                    List<ResultadoSolver> comparativa = get();
                    vista.mostrarResultadosComparativos(comparativa, gestor.getListaPersonas().size());
                } catch (Exception e) {
                    vista.mostrarMensaje("Ocurrió un error durante el cálculo: " + e.getMessage());
                } finally {
                    vista.actualizarEstado("Listo", true);
                }
            }
        };

        trabajador.execute();
    }

    private Persona buscarPersonaPorNombre(List<Persona> personas, String nombre) {
        for (Persona p : personas) {
            if (p.getNombre().equals(nombre)) return p;
        }
        return null;
    }
}