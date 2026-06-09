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
    
    private List<Persona> listaPersonas;
    private Map<String, Integer> requerimientos;
    private List<String[]> listaIncompatibilidades;

    private DefaultListModel<String> modeloVisualPersonas;
    private DefaultListModel<String> modeloVisualIncompatibilidades;
    private JComboBox<String> comboIncomp1;
    private JComboBox<String> comboIncomp2;

    private JTextArea txtResultado;
    private JButton btnResolver;
    private JLabel lblEstado;

    public VentanaPrincipal() {
        super("Simulador de Selección de Personal - Backtracking");
        this.controlador = new EquipoControlador(this);
        
        this.listaPersonas = new ArrayList<>();
        this.requerimientos = new HashMap<>();
        this.listaIncompatibilidades = new ArrayList<>();
        
        requerimientos.put("Líder de proyecto", 0);
        requerimientos.put("Arquitecto", 0);
        requerimientos.put("Programador", 0);
        requerimientos.put("Tester", 0);

        configurarVentana();
        inicializarComponentes();
    }

    private void configurarVentana() {
        setSize(950, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
    }

    private void inicializarComponentes() {
        JPanel panelFormularios = new JPanel();
        panelFormularios.setLayout(new BoxLayout(panelFormularios, BoxLayout.Y_AXIS));
        panelFormularios.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelFormularios.setPreferredSize(new Dimension(350, 0));

        panelFormularios.add(crearPanelCargaPersona());
        panelFormularios.add(Box.createRigidArea(new Dimension(0, 15)));
        panelFormularios.add(crearPanelRequerimientos());
        panelFormularios.add(Box.createRigidArea(new Dimension(0, 15)));
        panelFormularios.add(crearPanelIncompatibilidades());
        panelFormularios.add(Box.createVerticalGlue());

        JButton btnCargarDemo = new JButton("Cargar Datos de Prueba (Demo)");
        btnCargarDemo.addActionListener(e -> cargarEscenarioPorDefecto());
        panelFormularios.add(btnCargarDemo);

        add(panelFormularios, BorderLayout.WEST);

        JPanel panelListas = new JPanel(new GridLayout(2, 1, 5, 5));
        panelListas.setBorder(BorderFactory.createTitledBorder("Datos Ingresados en el Sistema"));
        
        modeloVisualPersonas = new DefaultListModel<>();
        JList<String> listaUI_Personas = new JList<>(modeloVisualPersonas);
        panelListas.add(new JScrollPane(listaUI_Personas));

        modeloVisualIncompatibilidades = new DefaultListModel<>();
        JList<String> listaUI_Incomp = new JList<>(modeloVisualIncompatibilidades);
        panelListas.add(new JScrollPane(listaUI_Incomp));

        add(panelListas, BorderLayout.CENTER);

        JPanel panelResultados = new JPanel(new BorderLayout());
        panelResultados.setBorder(BorderFactory.createTitledBorder("Salida del Algoritmo"));
        panelResultados.setPreferredSize(new Dimension(350, 0));

        txtResultado = new JTextArea();
        txtResultado.setEditable(false);
        txtResultado.setFont(new Font("Monospaced", Font.PLAIN, 12));
        panelResultados.add(new JScrollPane(txtResultado), BorderLayout.CENTER);

        JPanel panelAccion = new JPanel(new BorderLayout());
        btnResolver = new JButton("Calcular Equipo Ideal");
        btnResolver.setFont(new Font("Arial", Font.BOLD, 14));
        btnResolver.addActionListener(e -> controlador.resolverEquipo(listaPersonas, requerimientos, listaIncompatibilidades));
        
        lblEstado = new JLabel("Estado: Esperando datos...");
        panelAccion.add(btnResolver, BorderLayout.CENTER);
        panelAccion.add(lblEstado, BorderLayout.SOUTH);
        
        panelResultados.add(panelAccion, BorderLayout.SOUTH);
        add(panelResultados, BorderLayout.EAST);
    }

    private JPanel crearPanelCargaPersona() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("1. Nueva Persona"));

        JTextField txtNombre = new JTextField();
        String[] roles = {"Líder de proyecto", "Arquitecto", "Programador", "Tester"};
        JComboBox<String> comboRol = new JComboBox<>(roles);
        String[] notas = {"1", "2", "3", "4", "5"};
        JComboBox<String> comboNota = new JComboBox<>(notas);
        JButton btnAgregar = new JButton("Agregar");

        panel.add(new JLabel("Nombre:")); panel.add(txtNombre);
        panel.add(new JLabel("Rol:")); panel.add(comboRol);
        panel.add(new JLabel("Calificación:")); panel.add(comboNota);
        panel.add(new JLabel("")); panel.add(btnAgregar);

        btnAgregar.addActionListener(e -> {
            String nombre = txtNombre.getText().trim();
            if (!nombre.isEmpty()) {
                String rol = (String) comboRol.getSelectedItem();
                int nota = Integer.parseInt((String) comboNota.getSelectedItem());
                
                Persona nueva = new Persona(nombre, rol, nota);
                if (!listaPersonas.contains(nueva)) {
                    listaPersonas.add(nueva);
                    modeloVisualPersonas.addElement(nombre + " (" + rol + " - " + nota + " pts)");
                    comboIncomp1.addItem(nombre);
                    comboIncomp2.addItem(nombre);
                    txtNombre.setText("");
                } else {
                    JOptionPane.showMessageDialog(VentanaPrincipal.this, "La persona ya existe.");
                }
            }
        });

        return panel;
    }

    private JPanel crearPanelRequerimientos() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("2. Requerimientos del Equipo"));

        JSpinner spinLider = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));
        JSpinner spinArq = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));
        JSpinner spinProg = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));
        JSpinner spinTest = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));
        JButton btnFijarReq = new JButton("Fijar Cupos");

        panel.add(new JLabel("Líder de proyecto:")); panel.add(spinLider);
        panel.add(new JLabel("Arquitecto:")); panel.add(spinArq);
        panel.add(new JLabel("Programador:")); panel.add(spinProg);
        panel.add(new JLabel("Tester:")); panel.add(spinTest);
        panel.add(new JLabel("")); panel.add(btnFijarReq);

        btnFijarReq.addActionListener(e -> {
            requerimientos.put("Líder de proyecto", (Integer) spinLider.getValue());
            requerimientos.put("Arquitecto", (Integer) spinArq.getValue());
            requerimientos.put("Programador", (Integer) spinProg.getValue());
            requerimientos.put("Tester", (Integer) spinTest.getValue());
            JOptionPane.showMessageDialog(VentanaPrincipal.this, "Requerimientos guardados exitosamente.");
        });

        return panel;
    }

    private JPanel crearPanelIncompatibilidades() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("3. Registrar Incompatibilidad"));

        comboIncomp1 = new JComboBox<>();
        comboIncomp2 = new JComboBox<>();
        JButton btnRegistrarInc = new JButton("Cruzar");

        panel.add(new JLabel("Sujeto A:")); panel.add(comboIncomp1);
        panel.add(new JLabel("Sujeto B:")); panel.add(comboIncomp2);
        panel.add(new JLabel("")); panel.add(btnRegistrarInc);

        btnRegistrarInc.addActionListener(e -> {
            String p1 = (String) comboIncomp1.getSelectedItem();
            String p2 = (String) comboIncomp2.getSelectedItem();
            
            if (p1 != null && p2 != null && !p1.equals(p2)) {
                listaIncompatibilidades.add(new String[]{p1, p2});
                modeloVisualIncompatibilidades.addElement("Choque: " + p1 + " <-> " + p2);
            } else {
                JOptionPane.showMessageDialog(VentanaPrincipal.this, "Selección inválida.");
            }
        });

        return panel;
    }

    private void cargarEscenarioPorDefecto() {
        listaPersonas.clear();
        modeloVisualPersonas.clear();
        listaIncompatibilidades.clear();
        modeloVisualIncompatibilidades.clear();
        comboIncomp1.removeAllItems();
        comboIncomp2.removeAllItems();

        agregarPersonaDesdeDemo("Ana", "Líder de proyecto", 5);
        agregarPersonaDesdeDemo("Pedro", "Líder de proyecto", 4);
        agregarPersonaDesdeDemo("Carlos", "Arquitecto", 4);
        agregarPersonaDesdeDemo("Beatriz", "Arquitecto", 5);
        agregarPersonaDesdeDemo("Juan", "Programador", 3);
        agregarPersonaDesdeDemo("María", "Programador", 5);
        agregarPersonaDesdeDemo("Luis", "Programador", 4);
        agregarPersonaDesdeDemo("Sofía", "Tester", 4);
        agregarPersonaDesdeDemo("Diego", "Tester", 2);

        requerimientos.put("Líder de proyecto", 1);
        requerimientos.put("Arquitecto", 1);
        requerimientos.put("Programador", 2);
        requerimientos.put("Tester", 1);

        registrarIncompatibilidadDemo("Ana", "María");
        registrarIncompatibilidadDemo("Carlos", "Juan");
        
        JOptionPane.showMessageDialog(this, "Datos de prueba cargados. Requerimientos fijados internamente.");
    }

    private void agregarPersonaDesdeDemo(String nombre, String rol, int calificacion) {
        Persona p = new Persona(nombre, rol, calificacion);
        listaPersonas.add(p);
        modeloVisualPersonas.addElement(nombre + " (" + rol + " - " + calificacion + " pts)");
        comboIncomp1.addItem(nombre);
        comboIncomp2.addItem(nombre);
    }
    
    private void registrarIncompatibilidadDemo(String p1, String p2) {
        listaIncompatibilidades.add(new String[]{p1, p2});
        modeloVisualIncompatibilidades.addElement("Choque: " + p1 + " <-> " + p2);
    }

    public void actualizarEstado(String mensaje, boolean activarBoton) {
        lblEstado.setText("Estado: " + mensaje);
        btnResolver.setEnabled(activarBoton);
    }

    public void mostrarResultado(List<Persona> equipo, String mensajeFinal) {
        StringBuilder sb = new StringBuilder();
        sb.append(mensajeFinal).append("\n");
        sb.append("=========================================\n");
        
        if (equipo != null && !equipo.isEmpty()) {
            int calificacionTotal = 0;
            for (Persona p : equipo) {
                sb.append(String.format("- %-10s | %-15s | %d pts\n", 
                        p.getNombre(), p.getRol(), p.getCalificacion()));
                calificacionTotal += p.getCalificacion();
            }
            sb.append("=========================================\n");
            sb.append("Puntaje Histórico Total: ").append(calificacionTotal).append("\n");
        }
        txtResultado.setText(sb.toString());
    }
}