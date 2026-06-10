package negocio;

import java.util.*;

public class SolverHeuristico {
    private List<Persona> personasDisponibles;
    private Set<String> incompatibilidades; 
    private Map<String, Integer> requerimientos; 

    public SolverHeuristico(List<Persona> personas, Map<String, Integer> requerimientos) {
        this.personasDisponibles = new ArrayList<>(personas);
        this.requerimientos = requerimientos;
        this.incompatibilidades = new HashSet<>();
    }

    public void registrarIncompatibilidad(Persona p1, Persona p2) {
        incompatibilidades.add(p1.getNombre() + "-" + p2.getNombre());
        incompatibilidades.add(p2.getNombre() + "-" + p1.getNombre());
    }

    public ResultadoSolver resolver() {
        long tiempoInicio = System.nanoTime();
        
        // Criterio Goloso (Semana 10/11): Ordenar candidatos de mayor a menor calificación
        personasDisponibles.sort((p1, p2) -> Integer.compare(p2.getCalificacion(), p1.getCalificacion()));
        
        List<Persona> equipoResultante = new ArrayList<>();
        Map<String, Integer> rolesActuales = new HashMap<>();
        for (String rol : requerimientos.keySet()) {
            rolesActuales.put(rol, 0);
        }

        for (Persona candidato : personasDisponibles) {
            if (esViable(candidato, equipoResultante, rolesActuales)) {
                equipoResultante.add(candidato);
                rolesActuales.put(candidato.getRol(), rolesActuales.get(candidato.getRol()) + 1);
            }
        }

        long tiempoFin = System.nanoTime();
        double tiempoTotalMs = (tiempoFin - tiempoInicio) / 1_000_000.0;
        
        // Si la aproximación golosa no satisface la demanda exacta, la solución no es factible
        if (!cumpleRequerimientos(rolesActuales)) {
            equipoResultante.clear();
        }

        return new ResultadoSolver("Aproximado (Heurística Golosa)", equipoResultante, tiempoTotalMs, 0, 0);
    }

    private boolean esViable(Persona p, List<Persona> equipoActual, Map<String, Integer> rolesActuales) {
        int cupoMaximo = requerimientos.getOrDefault(p.getRol(), 0);
        int cupoActual = rolesActuales.getOrDefault(p.getRol(), 0);
        if (cupoActual >= cupoMaximo) return false;

        for (Persona miembro : equipoActual) {
            if (incompatibilidades.contains(p.getNombre() + "-" + miembro.getNombre())) return false; 
        }
        return true;
    }

    private boolean cumpleRequerimientos(Map<String, Integer> rolesActuales) {
        for (String rol : requerimientos.keySet()) {
            if (!rolesActuales.get(rol).equals(requerimientos.get(rol))) return false;
        }
        return true;
    }
}