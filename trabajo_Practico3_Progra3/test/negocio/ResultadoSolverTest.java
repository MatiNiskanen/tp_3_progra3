package negocio;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

public class ResultadoSolverTest {

    @Test
    public void testAsignacionYGettersBasicos() {
        List<Persona> equipoFicticio = new ArrayList<>();
        equipoFicticio.add(new Persona("Juan", "Tester", 4));
        
        ResultadoSolver res = new ResultadoSolver(
            "Algoritmo de Prueba", 
            equipoFicticio, 
            2.54, 
            150, 
            35
        );

        assertEquals("Algoritmo de Prueba", res.getNombreAlgoritmo());
        assertEquals(equipoFicticio, res.getEquipoIdeal());
        assertEquals(2.54, res.getTiempoEjecucionMs(), 0.0001);
        assertEquals(150, res.getCasoBaseEjecutado());
        assertEquals(35, res.getRamasPodadas());
    }

    @Test
    public void testGetPuntajeTotalConEquipoValido() {
        List<Persona> equipo = new ArrayList<>();
        equipo.add(new Persona("Ana", "Líder de proyecto", 5));
        equipo.add(new Persona("Luis", "Programador", 3));
        equipo.add(new Persona("Sofía", "Tester", 4));

        ResultadoSolver res = new ResultadoSolver("Test", equipo, 0.0, 0, 0);

        assertEquals("El puntaje total debe ser la suma exacta de las calificaciones individuales (5+3+4)", 12, res.getPuntajeTotal());
    }

    @Test
    public void testGetPuntajeTotalConEquipoVacioONulo() {
        ResultadoSolver resVacio = new ResultadoSolver("Test Vacío", new ArrayList<>(), 0.0, 0, 0);
        assertEquals("Un equipo sin integrantes debe retornar 0 puntos", 0, resVacio.getPuntajeTotal());

        ResultadoSolver resNulo = new ResultadoSolver("Test Nulo", null, 0.0, 0, 0);
        assertEquals("Un equipo nulo debe ser atajado y retornar 0 puntos", 0, resNulo.getPuntajeTotal());
    }
}