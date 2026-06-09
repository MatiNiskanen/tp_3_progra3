package negocio;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class GestorEquipoTest {

    private GestorEquipo gestor;

    @Before
    public void setUp() {
        gestor = new GestorEquipo();
    }

    @Test
    public void testAgregarPersonaEvitaDuplicados() {
        Persona p1 = new Persona("Juan", "Programador", 4);
        Persona p2 = new Persona("Juan", "Tester", 2);

        assertTrue("Debe permitir agregar a una persona nueva", gestor.agregarPersona(p1));
        assertFalse("No debe permitir agregar a alguien con el mismo nombre", gestor.agregarPersona(p2));
        
        assertEquals(1, gestor.getListaPersonas().size());
    }

    @Test
    public void testBorradoEnCascadaDeIncompatibilidades() {
        gestor.agregarPersona(new Persona("Ana", "Líder de proyecto", 5));
        gestor.agregarPersona(new Persona("María", "Programador", 4));
        gestor.agregarPersona(new Persona("Carlos", "Arquitecto", 3));

        gestor.registrarIncompatibilidad("Ana", "María");
        gestor.registrarIncompatibilidad("Ana", "Carlos");
        
        assertEquals("Debe haber 2 incompatibilidades registradas", 2, gestor.getListaIncompatibilidades().size());

        gestor.eliminarPersona(0);

        assertEquals("El borrado en cascada debe eliminar las incompatibilidades de Ana", 0, gestor.getListaIncompatibilidades().size());
        assertEquals("Solo deben quedar 2 personas en el sistema", 2, gestor.getListaPersonas().size());
    }

    @Test
    public void testRegistrarIncompatibilidadEvitaDuplicadosEInvalidos() {
        gestor.agregarPersona(new Persona("Pedro", "Líder de proyecto", 4));
        gestor.agregarPersona(new Persona("Luis", "Programador", 4));

        assertTrue("Debe registrar la incompatibilidad correctamente", gestor.registrarIncompatibilidad("Pedro", "Luis"));
        
        assertFalse("No debe permitir registrar la misma incompatibilidad exacta", gestor.registrarIncompatibilidad("Pedro", "Luis"));
        assertFalse("No debe permitir registrar la incompatibilidad invertida", gestor.registrarIncompatibilidad("Luis", "Pedro"));
        assertFalse("No debe permitir que una persona sea incompatible consigo misma", gestor.registrarIncompatibilidad("Pedro", "Pedro"));
        
        assertEquals(1, gestor.getListaIncompatibilidades().size());
    }
}