package negocio;

import java.util.Objects;

public class Persona {
    private String nombre;
    private String rol; 
    private int calificacion; 

    public Persona(String nombre, String rol, int calificacion) {
        this.nombre = nombre;
        this.rol = rol;
        this.calificacion = calificacion;
    }

    public String getNombre() { return nombre; }
    public String getRol() { return rol; }
    public int getCalificacion() { return calificacion; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Persona persona = (Persona) o;
        return Objects.equals(nombre, persona.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre);
    }
}