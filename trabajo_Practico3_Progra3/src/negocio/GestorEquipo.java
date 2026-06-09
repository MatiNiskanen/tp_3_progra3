package negocio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GestorEquipo {
    private List<Persona> listaPersonas;
    private Map<String, Integer> requerimientos;
    private List<String[]> listaIncompatibilidades;

    public GestorEquipo() {
        this.listaPersonas = new ArrayList<>();
        this.requerimientos = new HashMap<>();
        this.listaIncompatibilidades = new ArrayList<>();
        
        requerimientos.put("Líder de proyecto", 0);
        requerimientos.put("Arquitecto", 0);
        requerimientos.put("Programador", 0);
        requerimientos.put("Tester", 0);
    }

    public List<Persona> getListaPersonas() { return listaPersonas; }
    public Map<String, Integer> getRequerimientos() { return requerimientos; }
    public List<String[]> getListaIncompatibilidades() { return listaIncompatibilidades; }

    public boolean agregarPersona(Persona p) {
        if (!listaPersonas.contains(p)) {
            listaPersonas.add(p);
            return true;
        }
        return false;
    }

    public void eliminarPersona(int index) {
        if (index >= 0 && index < listaPersonas.size()) {
            Persona p = listaPersonas.remove(index);
            listaIncompatibilidades.removeIf(par -> par[0].equals(p.getNombre()) || par[1].equals(p.getNombre()));
        }
    }

    public void modificarPersona(int index, String nuevoRol, int nuevaCalificacion) {
        if (index >= 0 && index < listaPersonas.size()) {
            Persona actual = listaPersonas.get(index);
            listaPersonas.set(index, new Persona(actual.getNombre(), nuevoRol, nuevaCalificacion));
        }
    }

    public void fijarRequerimientos(int lider, int arq, int prog, int test) {
        requerimientos.put("Líder de proyecto", lider);
        requerimientos.put("Arquitecto", arq);
        requerimientos.put("Programador", prog);
        requerimientos.put("Tester", test);
    }

    public boolean registrarIncompatibilidad(String p1, String p2) {
        if (p1 != null && p2 != null && !p1.equals(p2)) {
            for (String[] par : listaIncompatibilidades) {
                if ((par[0].equals(p1) && par[1].equals(p2)) || (par[0].equals(p2) && par[1].equals(p1))) {
                    return false; // Ya existe
                }
            }
            listaIncompatibilidades.add(new String[]{p1, p2});
            return true;
        }
        return false;
    }

    public void eliminarIncompatibilidad(int index) {
        if (index >= 0 && index < listaIncompatibilidades.size()) {
            listaIncompatibilidades.remove(index);
        }
    }

    public void cargarEscenarioDemo() {
        listaPersonas.clear();
        listaIncompatibilidades.clear();

        agregarPersona(new Persona("Ana", "Líder de proyecto", 5));
        agregarPersona(new Persona("Pedro", "Líder de proyecto", 4));
        agregarPersona(new Persona("Carlos", "Arquitecto", 4));
        agregarPersona(new Persona("Beatriz", "Arquitecto", 5));
        agregarPersona(new Persona("Juan", "Programador", 3));
        agregarPersona(new Persona("María", "Programador", 5));
        agregarPersona(new Persona("Luis", "Programador", 4));
        agregarPersona(new Persona("Sofía", "Tester", 4));
        agregarPersona(new Persona("Diego", "Tester", 2));

        fijarRequerimientos(1, 1, 2, 1);

        registrarIncompatibilidad("Ana", "María");
        registrarIncompatibilidad("Carlos", "Juan");
    }
}