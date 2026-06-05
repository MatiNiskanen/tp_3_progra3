package interfazVisual;

import javax.swing.*;

import controlador.EquipoControlador;
import negocio.Persona;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VentanaPrincipal extends JFrame {
    private EquipoControlador controlador;
    
    // Componentes de la interfaz
    private JTextArea txtDatosCargados;
    private JTextArea txtResultado;
    private JButton btnResolver;
    private JLabel lblEstado;

    // Listas locales que simulan la persistencia de datos cargados por la UI
    private List<Persona> listaPersonas;
    private Map<String, Integer> requerimientos;
    private List<String[]> listaIncompatibilidades;

    public VentanaPrincipal() {
        super("Programación III - El Equipo Ideal");
        controlador = new EquipoControlador(this);
        
        inicializarDatosDemo();
        configurarVentana();
        inicializarComponentes();
    }

    private void configurarVentana() {
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
    }

    private void inicializarComponentes() {
        // --- PANEL SUPERIOR: Título y Datos Iniciales ---
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        
        JLabel lblTitulo = new JLabel("Simulador de Selección de Personal - Backtracking", JLabel.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        panelSuperior.add(lblTitulo, BorderLayout.NORTH);
        
        txtDatosCargados = new JTextArea(6, 40);
        txtDatosCargados.setEditable(false);
        txtDatosCargados.setText(obtenerResumenDatosCargados());
        panelSuperior.add(new JScrollPane(txtDatosCargados), BorderLayout.CENTER);
        
        add(panelSuperior, BorderLayout.NORTH);

        // --- PANEL CENTRAL: Resultados ---
        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.setBorder(BorderFactory.createTitledBorder("Equipo Resultante"));
        
        txtResultado = new JTextArea();
        txtResultado.setEditable(false);
        txtResultado.setFont(new Font("Monospaced", Font.PLAIN, 13));
        panelCentral.add(new JScrollPane(txtResultado), BorderLayout.CENTER);
        
        add(panelCentral, BorderLayout.CENTER);

        // --- PANEL INFERIOR: Botón de acción y Estado ---
        JPanel panelInferior = new JPanel(new BorderLayout(5, 5));
        panelInferior.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));

        btnResolver = new JButton("Calcular Equipo Ideal");
        btnResolver.setFont(new Font("Arial", Font.BOLD, 14));
        
        // Evento que dispara la acción a través del controlador
        btnResolver.addActionListener(e -> {
            controlador.resolverEquipo(listaPersonas, requerimientos, listaIncompatibilidades);
        });
        
        lblEstado = new JLabel("Estado: Listo");
        lblEstado.setBorder(BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        panelInferior.add(btnResolver, BorderLayout.CENTER);
        panelInferior.add(lblEstado, BorderLayout.SOUTH);

        add(panelInferior, BorderLayout.SOUTH);
    }

    // Métodos de callback que llamará el Controlador para modificar la Vista
    public void actualizarEstado(String mensaje, boolean activarBoton) {
        lblEstado.setText("Estado: " + mensaje);
        btnResolver.setEnabled(activarBoton);
    }

    public void mostrarResultado(List<Persona> equipo, String mensajeFinal) {
        StringBuilder sb = new StringBuilder();
        sb.append(mensajeFinal).append("\n");
        sb.append("=========================================================\n");
        
        if (equipo != null && !equipo.isEmpty()) {
            int calificacionTotal = 0;
            for (Persona p : equipo) {
                sb.append(String.format("- %-20s | Rol: %-20s | Calificación: %d\n", 
                        p.getNombre(), p.getRol(), p.getCalificacion()));
                calificacionTotal += p.getCalificacion();
            }
            sb.append("=========================================================\n");
            sb.append("Calificación Histórica Total del Equipo: ").append(calificacionTotal).append(" pts.\n");
        }
        
        txtResultado.setText(sb.toString());
    }

    // Inicialización de datos de ejemplo basados fielmente en la consigna
    private void inicializarDatosDemo() {
        listaPersonas = new ArrayList<>();
        listaPersonas.add(new Persona("Ana", "Líder de proyecto", 5));
        listaPersonas.add(new Persona("Pedro", "Líder de proyecto", 4));
        listaPersonas.add(new Persona("Carlos", "Arquitecto", 4));
        listaPersonas.add(new Persona("Beatriz", "Arquitecto", 5));
        listaPersonas.add(new Persona("Juan", "Programador", 3));
        listaPersonas.add(new Persona("María", "Programador", 5));
        listaPersonas.add(new Persona("Luis", "Programador", 4));
        listaPersonas.add(new Persona("Sofía", "Tester", 4));
        listaPersonas.add(new Persona("Diego", "Tester", 2));

        requerimientos = new HashMap<>();
        requerimientos.put("Líder de proyecto", 1);
        requerimientos.put("Arquitecto", 1);
        requerimientos.put("Programador", 2);
        requerimientos.put("Tester", 1);

        listaIncompatibilidades = new ArrayList<>();
        // Ejemplo: Ana (Líder, 5 pts) se lleva mal con María (Programadora, 5 pts). 
        // El algoritmo va a tener que elegir dejar a una afuera para balancear.
        listaIncompatibilidades.add(new String[]{"Ana", "María"});
        listaIncompatibilidades.add(new String[]{"Carlos", "Juan"});
    }

    private String obtenerResumenDatosCargados() {
        return "Personas Disponibles: " + listaPersonas.size() + " cargadas en el sistema.\n" +
               "Requerimientos buscados: " + requerimientos.toString() + "\n" +
               "Incompatibilidades activas: " + listaIncompatibilidades.size() + " pares registrados.";
    }
}