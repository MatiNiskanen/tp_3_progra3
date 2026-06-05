package negocio;

import java.util.*;

public class SolverEquipo {
    private List<Persona> personasDisponibles;
    private Set<String> incompatibilidades; // Formato "Nombre1-Nombre2" para búsqueda rápida
    private Map<String, Integer> requerimientos; // Rol -> Cantidad necesaria

    // Variables para guardar la mejor solución encontrada
    private List<Persona> mejorEquipo;
    private int mejorCalificacionTotal;

    public SolverEquipo(List<Persona> personas, Map<String, Integer> requerimientos) {
        this.personasDisponibles = personas;
        this.requerimientos = requerimientos;
        this.incompatibilidades = new HashSet<>();
        this.mejorEquipo = new ArrayList<>();
        this.mejorCalificacionTotal = -1;
    }

    public void registrarIncompatibilidad(Persona p1, Persona p2) {
        // Guardamos en ambos sentidos para facilitar la consulta rápida
        incompatibilidades.add(p1.getNombre() + "-" + p2.getNombre());
        incompatibilidades.add(p2.getNombre() + "-" + p1.getNombre());
    }

    // Método que inicia la búsqueda (Se debería llamar desde el Thread secundario)
    public List<Persona> resolver() {
        mejorEquipo = new ArrayList<>();
        mejorCalificacionTotal = -1;
        
        List<Persona> equipoActual = new ArrayList<>();
        Map<String, Integer> rolesActuales = new HashMap<>();
        for (String rol : requerimientos.keySet()) {
            rolesActuales.put(rol, 0);
        }

        backtracking(0, equipoActual, rolesActuales, 0);
        
        return mejorEquipo;
    }

    private void backtracking(int indice, List<Persona> equipoActual, Map<String, Integer> rolesActuales, int calificacionActual) {
        // CASO BASE: Si ya evaluamos a todas las personas
        if (indice == personasDisponibles.size()) {
            // Validamos si el equipo cumple exactamente con todos los requerimientos
            if (cumpleRequerimientos(rolesActuales)) {
                if (calificacionActual > mejorCalificacionTotal) {
                    mejorCalificacionTotal = calificacionActual;
                    mejorEquipo = new ArrayList<>(equipoActual);
                }
            }
            return;
        }

        Persona personaEvaluar = personasDisponibles.get(indice);

        // OPCIÓN 1: Intentar INCLUIR a la persona (si es viable)
        if (esViable(personaEvaluar, equipoActual, rolesActuales)) {
            // Registrar cambio
            equipoActual.add(personaEvaluar);
            rolesActuales.put(personaEvaluar.getRol(), rolesActuales.get(personaEvaluar.getRol()) + 1);
            
            // Llamada recursiva
            backtracking(indice + 1, equipoActual, rolesActuales, calificacionActual + personaEvaluar.getCalificacion());
            
            // Deshacer cambio (Backtrack)
            equipoActual.remove(equipoActual.size() - 1);
            rolesActuales.put(personaEvaluar.getRol(), rolesActuales.get(personaEvaluar.getRol()) - 1);
        }

        // OPCIÓN 2: Dejar afuera a la persona y seguir explorando
        backtracking(indice + 1, equipoActual, rolesActuales, calificacionActual);
    }

    private boolean esViable(Persona p, List<Persona> equipoActual, Map<String, Integer> rolesActuales) {
        // 1. Validar que no nos pasemos del cupo para ese rol
        int cupoMaximo = requerimientos.getOrDefault(p.getRol(), 0);
        int cupoActual = rolesActuales.getOrDefault(p.getRol(), 0);
        if (cupoActual >= cupoMaximo) {
            return false;
        }

        // 2. Validar incompatibilidades con los miembros actuales del equipo
        for (Persona miembro : equipoActual) {
            if (incompatibilidades.contains(p.getNombre() + "-" + miembro.getNombre())) {
                return false; // Incompatibles, no se puede agregar
            }
        }

        return true;
    }

    private boolean cumpleRequerimientos(Map<String, Integer> rolesActuales) {
        for (String rol : requerimientos.keySet()) {
            if (!rolesActuales.get(rol).equals(requerimientos.get(rol))) {
                return false;
            }
        }
        return true;
    }
}
