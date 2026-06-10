package negocio;

import java.util.List;

public class ResultadoSolver {
    private String nombreAlgoritmo;
    private List<Persona> equipoIdeal;
    private double tiempoEjecucionMs;
    private long casoBaseEjecutado;
    private long ramasPodadas;

    public ResultadoSolver(String nombreAlgoritmo, List<Persona> equipoIdeal, double tiempoEjecucionMs, 
                           long casoBaseEjecutado, long ramasPodadas) {
        this.nombreAlgoritmo = nombreAlgoritmo;
        this.equipoIdeal = equipoIdeal;
        this.tiempoEjecucionMs = tiempoEjecucionMs;
        this.casoBaseEjecutado = casoBaseEjecutado;
        this.ramasPodadas = ramasPodadas;
    }

    public String getNombreAlgoritmo() { return nombreAlgoritmo; }
    public List<Persona> getEquipoIdeal() { return equipoIdeal; }
    public double getTiempoEjecucionMs() { return tiempoEjecucionMs; }
    public long getCasoBaseEjecutado() { return casoBaseEjecutado; }
    public long getRamasPodadas() { return ramasPodadas; }
    
    public int getPuntajeTotal() {
        int suma = 0;
        if (equipoIdeal != null) {
            for (Persona p : equipoIdeal) {
                suma += p.getCalificacion();
            }
        }
        return suma;
    }
}