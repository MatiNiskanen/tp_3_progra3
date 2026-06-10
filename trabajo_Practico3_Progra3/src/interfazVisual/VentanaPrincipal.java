package interfazVisual;

import javax.swing.*;
import controlador.EquipoControlador;
import negocio.Persona;
import negocio.ResultadoSolver;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Map;

public class VentanaPrincipal extends JFrame {
    private EquipoControlador controlador;
    
    private DefaultListModel<String> modeloVisualRequerimientos;
    private DefaultListModel<String> modeloVisualPersonas;
    private DefaultListModel<String> modeloVisualIncompatibilidades;
    
    private JList<String> listaUI_Personas;
    private JList<String> listaUI_Incomp;
    
    private JComboBox<String> comboIncomp1;
    private JComboBox<String> comboIncomp2;

    private JSpinner spinLider;
    private JSpinner spinArq;
    private JSpinner spinProg;
    private JSpinner spinTest;

    private JTabbedPane panelTabsResultados; 
    private JButton btnResolver;
    private JLabel lblEstado;

    public VentanaPrincipal() {
        super("El equipo ideal");
        configurarVentana();
        inicializarComponentes();
        this.controlador = new EquipoControlador(this); 
    }

    private void configurarVentana() {
        setSize(1000, 650);
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
        btnCargarDemo.addActionListener(e -> controlador.cargarDemo());
        panelFormularios.add(btnCargarDemo);

        add(panelFormularios, BorderLayout.WEST);

        JPanel panelListas = new JPanel(new GridLayout(3, 1, 5, 5));
        panelListas.setBorder(BorderFactory.createTitledBorder("Datos Ingresados (Clic derecho para opciones)"));
        
        modeloVisualRequerimientos = new DefaultListModel<>();
        JList<String> listaUI_Req = new JList<>(modeloVisualRequerimientos);
        panelListas.add(new JScrollPane(listaUI_Req));

        modeloVisualPersonas = new DefaultListModel<>();
        listaUI_Personas = new JList<>(modeloVisualPersonas);
        configurarMenuContextualPersonas(); 
        panelListas.add(new JScrollPane(listaUI_Personas));

        modeloVisualIncompatibilidades = new DefaultListModel<>();
        listaUI_Incomp = new JList<>(modeloVisualIncompatibilidades);
        configurarMenuContextualIncompatibilidades(); 
        panelListas.add(new JScrollPane(listaUI_Incomp));

        add(panelListas, BorderLayout.CENTER);

        JPanel panelResultados = new JPanel(new BorderLayout());
        panelResultados.setBorder(BorderFactory.createTitledBorder("Comparativa de Rendimiento"));
        panelResultados.setPreferredSize(new Dimension(380, 0));

        panelTabsResultados = new JTabbedPane();
        panelResultados.add(panelTabsResultados, BorderLayout.CENTER);

        JPanel panelAccion = new JPanel(new BorderLayout());
        btnResolver = new JButton("Calcular y Comparar Soluciones");
        btnResolver.setFont(new Font("Arial", Font.BOLD, 14));
        btnResolver.addActionListener(e -> controlador.resolverComparativa());
        
        lblEstado = new JLabel("Estado: Esperando datos...");
        panelAccion.add(btnResolver, BorderLayout.CENTER);
        panelAccion.add(lblEstado, BorderLayout.SOUTH);
        
        panelResultados.add(panelAccion, BorderLayout.SOUTH);
        add(panelResultados, BorderLayout.EAST);
    }

    private void configurarMenuContextualPersonas() {
        JPopupMenu popup = new JPopupMenu();
        JMenuItem itemModificar = new JMenuItem("Modificar Rol/Calificación");
        JMenuItem itemEliminar = new JMenuItem("Eliminar Persona");
        
        popup.add(itemModificar);
        popup.addSeparator();
        popup.add(itemEliminar);

        itemEliminar.addActionListener(e -> {
            int index = listaUI_Personas.getSelectedIndex();
            if (index != -1) controlador.eliminarPersona(index);
        });

        itemModificar.addActionListener(e -> {
            int index = listaUI_Personas.getSelectedIndex();
            if (index != -1) {
                String[] roles = {"Líder de proyecto", "Arquitecto", "Programador", "Tester"};
                JComboBox<String> comboRol = new JComboBox<>(roles);
                String[] notas = {"1", "2", "3", "4", "5"};
                JComboBox<String> comboNota = new JComboBox<>(notas);
                
                JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
                panel.add(new JLabel("Nuevo Rol:")); panel.add(comboRol);
                panel.add(new JLabel("Nueva Calificación:")); panel.add(comboNota);
                
                int result = JOptionPane.showConfirmDialog(this, panel, "Modificar Empleado", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    controlador.modificarPersona(index, (String)comboRol.getSelectedItem(), Integer.parseInt((String)comboNota.getSelectedItem()));
                }
            }
        });

        listaUI_Personas.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    int row = listaUI_Personas.locationToIndex(e.getPoint());
                    if (row != -1) {
                        listaUI_Personas.setSelectedIndex(row);
                        popup.show(listaUI_Personas, e.getX(), e.getY());
                    }
                }
            }
        });
    }

    private void configurarMenuContextualIncompatibilidades() {
        JPopupMenu popup = new JPopupMenu();
        JMenuItem itemEliminar = new JMenuItem("Eliminar Incompatibilidad");
        popup.add(itemEliminar);

        itemEliminar.addActionListener(e -> {
            int index = listaUI_Incomp.getSelectedIndex();
            if (index != -1) controlador.eliminarIncompatibilidad(index);
        });

        listaUI_Incomp.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    int row = listaUI_Incomp.locationToIndex(e.getPoint());
                    
                    if (row != -1) {
                        listaUI_Incomp.setSelectedIndex(row);
                        popup.show(listaUI_Incomp, e.getX(), e.getY());
                    }
                }
            }
        });
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
                controlador.agregarPersona(nombre, rol, nota);
                txtNombre.setText("");
            }
        });

        return panel;
    }

    private JPanel crearPanelRequerimientos() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("2. Requerimientos del Equipo"));

        spinLider = new JSpinner(new SpinnerNumberModel(0, 0, 999, 1));
        spinArq = new JSpinner(new SpinnerNumberModel(0, 0, 999, 1));
        spinProg = new JSpinner(new SpinnerNumberModel(0, 0, 999, 1));
        spinTest = new JSpinner(new SpinnerNumberModel(0, 0, 999, 1));
        JButton btnFijarReq = new JButton("Fijar Cupos");

        panel.add(new JLabel("Líder de proyecto:")); panel.add(spinLider);
        panel.add(new JLabel("Arquitecto:")); panel.add(spinArq);
        panel.add(new JLabel("Programador:")); panel.add(spinProg);
        panel.add(new JLabel("Tester:")); panel.add(spinTest);
        panel.add(new JLabel("")); panel.add(btnFijarReq);

        btnFijarReq.addActionListener(e -> {
            controlador.fijarRequerimientos(
                (Integer) spinLider.getValue(),
                (Integer) spinArq.getValue(),
                (Integer) spinProg.getValue(),
                (Integer) spinTest.getValue()
            );
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
            controlador.registrarIncompatibilidad((String)comboIncomp1.getSelectedItem(), (String)comboIncomp2.getSelectedItem());
        });

        return panel;
    }

    public void actualizarListasVisuales(List<Persona> personas, Map<String, Integer> reqs, List<String[]> incomp) {
        modeloVisualPersonas.clear();
        comboIncomp1.removeAllItems();
        comboIncomp2.removeAllItems();
        for (Persona p : personas) {
            modeloVisualPersonas.addElement(p.getNombre() + " (" + p.getRol() + " - " + p.getCalificacion() + " pts)");
            comboIncomp1.addItem(p.getNombre());
            comboIncomp2.addItem(p.getNombre());
        }

        modeloVisualRequerimientos.clear();
        if(reqs.containsKey("Líder de proyecto")) {
            modeloVisualRequerimientos.addElement("Cupos - Líderes: " + reqs.get("Líder de proyecto"));
            modeloVisualRequerimientos.addElement("Cupos - Arquitectos: " + reqs.get("Arquitecto"));
            modeloVisualRequerimientos.addElement("Cupos - Programadores: " + reqs.get("Programador"));
            modeloVisualRequerimientos.addElement("Cupos - Testers: " + reqs.get("Tester"));
        }

        modeloVisualIncompatibilidades.clear();
        for (String[] par : incomp) {
            modeloVisualIncompatibilidades.addElement("Choque: " + par[0] + " <-> " + par[1]);
        }
    }

    public void setValoresSpinners(Map<String, Integer> reqs) {
        spinLider.setValue(reqs.get("Líder de proyecto"));
        spinArq.setValue(reqs.get("Arquitecto"));
        spinProg.setValue(reqs.get("Programador"));
        spinTest.setValue(reqs.get("Tester"));
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    public void actualizarEstado(String mensaje, boolean activarBoton) {
        lblEstado.setText("Estado: " + mensaje);
        btnResolver.setEnabled(activarBoton);
    }

    public void mostrarResultadosComparativos(List<ResultadoSolver> comparativa, int totalPersonasSistema) {
        panelTabsResultados.removeAll();

        for (ResultadoSolver res : comparativa) {
            JTextArea txtResultadoTab = new JTextArea();
            txtResultadoTab.setEditable(false);
            txtResultadoTab.setFont(new Font("Monospaced", Font.PLAIN, 13));
            txtResultadoTab.setLineWrap(true);
            txtResultadoTab.setWrapStyleWord(true);
            txtResultadoTab.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

            StringBuilder sb = new StringBuilder();
            boolean esBacktracking = res.getNombreAlgoritmo().contains("Backtracking");

            sb.append("Enfoque: ").append(esBacktracking ? "Algoritmo Exacto (Backtracking)\n" : "Algoritmo Heurístico (Goloso)\n");
            
            if (res.getEquipoIdeal() == null || res.getEquipoIdeal().isEmpty()) {
                sb.append("\nNo se encontró una solución factible.\n");
            } else {
                sb.append("Puntos: ").append(res.getPuntajeTotal()).append("\n");
                
                if (esBacktracking) {
                    sb.append("Complejidad teórica: O(2^N)\n");
                } else {
                    sb.append("Complejidad teórica: O(N log N)\n");
                }
                
                sb.append(" Tiempo de ejecución: ").append(String.format("%.4f", res.getTiempoEjecucionMs())).append(" ms\n\n");
                
                sb.append("Integrantes seleccionados\n");
                for (Persona p : res.getEquipoIdeal()) {
                    sb.append("  ").append(p.getNombre())
                      .append(" | ").append(p.getRol())
                      .append(" | ").append(p.getCalificacion())
                      .append(" pts\n");
                }
            }

            txtResultadoTab.setText(sb.toString());
            panelTabsResultados.addTab(res.getNombreAlgoritmo().split(" ")[0], new JScrollPane(txtResultadoTab));
        }
    }
}