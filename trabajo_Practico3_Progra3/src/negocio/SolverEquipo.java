package negocio;

import java.util.*;

public class SolverEquipo {
    private List<Persona> personasDisponibles;
    private Set<String> incompatibilidades; 
    private Map<String, Integer> requerimientos; 

    private List<Persona> mejorEquipo;
    private int mejorCalificacionTotal;
    
    private int[] calificacionesRestantes;

    public SolverEquipo(List<Persona> personas, Map<String, Integer> requerimientos) {
        this.personasDisponibles = personas;
        this.requerimientos = requerimientos;
        this.incompatibilidades = new HashSet<>();
        this.mejorEquipo = new ArrayList<>();
        this.mejorCalificacionTotal = -1;
    }

    public void registrarIncompatibilidad(Persona p1, Persona p2) {
        incompatibilidades.add(p1.getNombre() + "-" + p2.getNombre());
        incompatibilidades.add(p2.getNombre() + "-" + p1.getNombre());
    }

    public List<Persona> resolver() {
        mejorEquipo = new ArrayList<>();
        mejorCalificacionTotal = -1;
        
        calificacionesRestantes = new int[personasDisponibles.size() + 1];
        calificacionesRestantes[personasDisponibles.size()] = 0;
        for (int i = personasDisponibles.size() - 1; i >= 0; i--) {
            calificacionesRestantes[i] = calificacionesRestantes[i + 1] + personasDisponibles.get(i).getCalificacion();
        }
        
        List<Persona> equipoActual = new ArrayList<>();
        Map<String, Integer> rolesActuales = new HashMap<>();
        for (String rol : requerimientos.keySet()) {
            rolesActuales.put(rol, 0);
        }

        backtracking(0, equipoActual, rolesActuales, 0);
        
        return mejorEquipo;
    }

    private void backtracking(int indice, List<Persona> equipoActual, Map<String, Integer> rolesActuales, int calificacionActual) {
        if (calificacionActual + calificacionesRestantes[indice] <= mejorCalificacionTotal) {
            return;
        }

        if (indice == personasDisponibles.size()) {
            if (cumpleRequerimientos(rolesActuales)) {
                if (calificacionActual > mejorCalificacionTotal) {
                    mejorCalificacionTotal = calificacionActual;
                    mejorEquipo = new ArrayList<>(equipoActual);
                }
            }
            return;
        }

        Persona personaEvaluar = personasDisponibles.get(indice);

        if (esViable(personaEvaluar, equipoActual, rolesActuales)) {
            equipoActual.add(personaEvaluar);
            rolesActuales.put(personaEvaluar.getRol(), rolesActuales.get(personaEvaluar.getRol()) + 1);
            
            backtracking(indice + 1, equipoActual, rolesActuales, calificacionActual + personaEvaluar.getCalificacion());
            
            equipoActual.remove(equipoActual.size() - 1);
            rolesActuales.put(personaEvaluar.getRol(), rolesActuales.get(personaEvaluar.getRol()) - 1);
        }

        backtracking(indice + 1, equipoActual, rolesActuales, calificacionActual);
    }

    private boolean esViable(Persona p, List<Persona> equipoActual, Map<String, Integer> rolesActuales) {
        int cupoMaximo = requerimientos.getOrDefault(p.getRol(), 0);
        int cupoActual = rolesActuales.getOrDefault(p.getRol(), 0);
        if (cupoActual >= cupoMaximo) {
            return false;
        }

        for (Persona miembro : equipoActual) {
            if (incompatibilidades.contains(p.getNombre() + "-" + miembro.getNombre())) {
                return false; 
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