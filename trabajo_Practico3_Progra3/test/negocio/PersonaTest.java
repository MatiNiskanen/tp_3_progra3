package negocio;

import static org.junit.Assert.*;
import org.junit.Test;

public class PersonaTest {

    @Test
    public void testCreacionYGetters() {
        Persona p = new Persona("Juan", "Programador", 4);
        assertEquals("Juan", p.getNombre());
        assertEquals("Programador", p.getRol());
        assertEquals(4, p.getCalificacion());
    }

    @Test
    public void testIgualdadPersonas() {
        Persona p1 = new Persona("Ana", "Líder de proyecto", 5);
        Persona p2 = new Persona("Ana", "Arquitecto", 3);
        Persona p3 = new Persona("Carlos", "Líder de proyecto", 5);

        assertTrue("Dos personas con el mismo nombre deben ser consideradas iguales", p1.equals(p2));
        assertFalse("Personas con distinto nombre no son iguales", p1.equals(p3));
    }
}