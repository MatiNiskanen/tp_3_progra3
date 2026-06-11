package negocio;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;

public class SolverHeuristicoTest {

    private List<Persona> personasPrueba;
    private Map<String, Integer> reqPrueba;

    @Before
    public void setUp() {
        personasPrueba = new ArrayList<>();
        reqPrueba = new HashMap<>();
    }

    @Test
    public void testComportamientoGolosoPriorizaPuntaje() {
        personasPrueba.add(new Persona("Junior", "Programador", 2));
        personasPrueba.add(new Persona("Senior", "Programador", 5));
        personasPrueba.add(new Persona("SemiSenior", "Programador", 4));
        
        reqPrueba.put("Programador", 2);
        
        SolverHeuristico solver = new SolverHeuristico(personasPrueba, reqPrueba);
        ResultadoSolver res = solver.resolver();
        List<Persona> resultado = res.getEquipoIdeal();
        
        assertEquals("Debe seleccionar exactamente 2 programadores", 2, resultado.size());
        assertEquals("El algoritmo debe sumar 9 puntos (5 + 4)", 9, res.getPuntajeTotal());
        
        assertTrue(resultado.contains(new Persona("Senior", "Programador", 5)));
        assertTrue(resultado.contains(new Persona("SemiSenior", "Programador", 4)));
    }

    @Test
    public void testHeuristicaFallaAlLlenarCupoPorIncompatibilidad() {
        Persona p1 = new Persona("Lider Top", "Líder de proyecto", 5);
        Persona p2 = new Persona("Lider Bueno", "Líder de proyecto", 4);
        Persona p3 = new Persona("Prog Único", "Programador", 5);
        
        personasPrueba.add(p1);
        personasPrueba.add(p2);
        personasPrueba.add(p3);
        
        reqPrueba.put("Líder de proyecto", 1);
        reqPrueba.put("Programador", 1);
        
        SolverHeuristico solver = new SolverHeuristico(personasPrueba, reqPrueba);
        
        solver.registrarIncompatibilidad(p1, p3);
        
        ResultadoSolver res = solver.resolver();
        List<Persona> resultado = res.getEquipoIdeal();
        
        assertTrue("El equipo debe estar vacío porque el goloso se trancó y no pudo cumplir el cupo", resultado.isEmpty());
    }
    
    @Test
    public void testHeuristicaConRequerimientosImposibles() {
        personasPrueba.add(new Persona("Juan", "Líder de proyecto", 5));
        
        reqPrueba.put("Arquitecto", 1);
        
        SolverHeuristico solver = new SolverHeuristico(personasPrueba, reqPrueba);
        ResultadoSolver res = solver.resolver();
        
        assertTrue("La heurística debe abortar y retornar vacío si no puede cumplir la firma de requerimientos", res.getEquipoIdeal().isEmpty());
    }
}