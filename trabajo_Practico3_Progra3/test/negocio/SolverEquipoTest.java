package negocio;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;

public class SolverEquipoTest {

    private List<Persona> personasPrueba;
    private Map<String, Integer> reqPrueba;

    @Before
    public void setUp() {
        personasPrueba = new ArrayList<>();
        reqPrueba = new HashMap<>();
    }

    @Test
    public void testPodaPorIncompatibilidad() {
        Persona p1 = new Persona("Lider A", "Líder de proyecto", 5);
        Persona p2 = new Persona("Lider B", "Líder de proyecto", 5);
        personasPrueba.add(p1);
        personasPrueba.add(p2);
        
        reqPrueba.put("Líder de proyecto", 2);
        
        SolverEquipo solver = new SolverEquipo(personasPrueba, reqPrueba);
        solver.registrarIncompatibilidad(p1, p2);
        
        ResultadoSolver res = solver.resolver();
        List<Persona> resultado = res.getEquipoIdeal();
        
        assertTrue("El equipo debería estar vacío porque los únicos dos candidatos se odian", resultado.isEmpty());
    }

    @Test
    public void testPodaPorCupoExcedido() {
        Persona p1 = new Persona("Arq Malo", "Arquitecto", 1);
        Persona p2 = new Persona("Arq Excelente", "Arquitecto", 5);
        Persona p3 = new Persona("Arq Normal", "Arquitecto", 3);
        
        personasPrueba.add(p1);
        personasPrueba.add(p2);
        personasPrueba.add(p3);
        
        reqPrueba.put("Arquitecto", 1);
        
        SolverEquipo solver = new SolverEquipo(personasPrueba, reqPrueba);
        ResultadoSolver res = solver.resolver();
        List<Persona> resultado = res.getEquipoIdeal();
        
        assertEquals("Debe haber exactamente 1 persona en el equipo para no exceder el cupo", 1, resultado.size());
        assertEquals("El algoritmo debe seleccionar al Arquitecto de mayor puntaje", "Arq Excelente", resultado.get(0).getNombre());
    }

    @Test
    public void testPodaPorOptimidad() {
        personasPrueba.add(new Persona("A", "Tester", 5));
        personasPrueba.add(new Persona("B", "Tester", 4));
        personasPrueba.add(new Persona("C", "Tester", 1));
        
        reqPrueba.put("Tester", 2);
        
        SolverEquipo solver = new SolverEquipo(personasPrueba, reqPrueba);
        ResultadoSolver res = solver.resolver();
        List<Persona> resultado = res.getEquipoIdeal();
        
        assertEquals("El equipo óptimo debe tener 2 miembros", 2, resultado.size());
        assertEquals("La suma óptima de calificaciones debe ser 9", 9, res.getPuntajeTotal());
    }
    
    @Test
    public void testRequerimientosImposibles() {
        personasPrueba.add(new Persona("Solo", "Tester", 5));
        
        reqPrueba.put("Tester", 2);
        
        SolverEquipo solver = new SolverEquipo(personasPrueba, reqPrueba);
        ResultadoSolver res = solver.resolver();
        
        assertTrue("Si no hay suficiente personal para cubrir el cupo, debe retornar una lista vacía", res.getEquipoIdeal().isEmpty());
    }

    @Test
    public void testGrafoCompletoDeIncompatibilidades() {
        Persona p1 = new Persona("A", "Programador", 5);
        Persona p2 = new Persona("B", "Programador", 4);
        Persona p3 = new Persona("C", "Programador", 3);
        
        personasPrueba.add(p1);
        personasPrueba.add(p2);
        personasPrueba.add(p3);
        
        reqPrueba.put("Programador", 2);
        
        SolverEquipo solver = new SolverEquipo(personasPrueba, reqPrueba);
        
        solver.registrarIncompatibilidad(p1, p2);
        solver.registrarIncompatibilidad(p2, p3);
        solver.registrarIncompatibilidad(p1, p3);
        
        ResultadoSolver res = solver.resolver();
        
        assertTrue("Es imposible formar un equipo de 2 si todos son incompatibles mutuamente", res.getEquipoIdeal().isEmpty());
    }

    @Test
    public void testCuposEnCero() {
        personasPrueba.add(new Persona("A", "Programador", 5));
        
        reqPrueba.put("Programador", 0);
        reqPrueba.put("Tester", 0);
        reqPrueba.put("Arquitecto", 0);
        reqPrueba.put("Líder de proyecto", 0);
        
        SolverEquipo solver = new SolverEquipo(personasPrueba, reqPrueba);
        ResultadoSolver res = solver.resolver();
        
        assertTrue("Si los cupos son cero, el equipo ideal no debe tener integrantes", res.getEquipoIdeal().isEmpty());
        assertEquals("El puntaje total debe ser 0", 0, res.getPuntajeTotal());
    }
}