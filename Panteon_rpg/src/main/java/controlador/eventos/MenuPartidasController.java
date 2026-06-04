package controlador.eventos;

import vista.VistaMenuPartidas;
import java.util.List;
import java.util.Map;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.border.Border;

/**
 * Controlador para la gestión de las ranuras de guardado de partidas. Permite
 * al jugador seleccionar, cargar o eliminar partidas existentes.
 */
public class MenuPartidasController {

    private VistaMenuPartidas vista;
    private AccionUsuarioController ctrlPrincipal;

    // IDs de la base de datos para cada ranura (-1 indica ranura vacía)
    private int idPartidaRanura1 = -1;
    private int idPartidaRanura2 = -1;
    private int idPartidaRanura3 = -1;

    // Almacena el número de ranura seleccionada actualmente (1, 2, 3 o -1)
    private int ranuraSeleccionada = -1;

    /**
     * Inicializa el controlador con la vista de selección de partidas.
     *
     * @param vista La vista del menú de partidas.
     */
    public MenuPartidasController(VistaMenuPartidas vista) {
        this.vista = vista;
        this.ctrlPrincipal = AccionUsuarioController.getInstancia();
        inicializarEventos();
        cargarYMostrarPartidas();
    }

    /**
     * Configura los listeners para los botones de acción y la selección de
     * ranuras.
     */
    private void inicializarEventos() {
        // --- Selección de ranuras ---
        configurarClicRanura(vista.getPanelGuardados1(), 1);
        configurarClicRanura(vista.getPanelGuardados2(), 2);
        configurarClicRanura(vista.getPanelGuardados3(), 3);

        // --- Cargar Partida ---
        vista.getBtnCargarPartida().addActionListener(e -> {
            if (ranuraSeleccionada == -1) {
                JOptionPane.showMessageDialog(vista,
                        "Por favor, selecciona una ranura haciendo clic sobre su recuadro para continuar.",
                        "Ninguna Selección", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int idACargar = obtenerIdPorRanura(ranuraSeleccionada);

            if (idACargar == -1) {
                JOptionPane.showMessageDialog(vista,
                        "Esa ranura está vacía. Ve al menú principal para iniciar una nueva aventura.",
                        "Ranura Vacía", JOptionPane.ERROR_MESSAGE);
                return;
            }

            ctrlPrincipal.clickCargarPartida(idACargar);
        });

        // --- Borrar Partida ---
        vista.getBtnBorrarPartida().addActionListener(e -> {
            if (ranuraSeleccionada == -1) {
                JOptionPane.showMessageDialog(vista, "Selecciona una partida antes de borrar.", "Ninguna Selección", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int idABorrar = obtenerIdPorRanura(ranuraSeleccionada);

            if (idABorrar == -1) {
                JOptionPane.showMessageDialog(vista, "No puedes borrar una ranura vacía.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int respuesta = JOptionPane.showConfirmDialog(vista, "¿Deseas eliminar de forma permanente esta partida?", "Confirmar", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if (respuesta == JOptionPane.YES_OPTION) {
                ctrlPrincipal.clickBorrarPartida(idABorrar);
                JOptionPane.showMessageDialog(vista, "Partida eliminada.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                deseleccionarTodas();
                cargarYMostrarPartidas();
            }
        });

        vista.getVolverInicio().addActionListener(e -> ctrlPrincipal.clickVolverAlInicio());
    }

    /**
     * Recupera la información de las partidas desde la base de datos y
     * actualiza la vista.
     */
    public void cargarYMostrarPartidas() {
        modelo.ConsultasRPG consultas = new modelo.ConsultasRPG();
        List<Map<String, Object>> partidas = consultas.obtenerTodasLasPartidas();

        // Actualización de ranura 1
        if (partidas.size() > 0) {
            Map<String, Object> p = partidas.get(0);
            idPartidaRanura1 = (int) p.get("partida_id");
            actualizarRanuraVisual(vista.getTxtPartida(), vista.getTxtHeroe(), vista.getTxtClase(), vista.getTxtPiso(), vista.getTxtNivel(), p);
        } else {
            idPartidaRanura1 = -1;
            vaciarRanuraVisual(vista.getTxtPartida(), vista.getTxtHeroe(), vista.getTxtClase(), vista.getTxtPiso(), vista.getTxtNivel());
        }

        // Actualización de ranura 2
        if (partidas.size() > 1) {
            Map<String, Object> p = partidas.get(1);
            idPartidaRanura2 = (int) p.get("partida_id");
            actualizarRanuraVisual(vista.getTxtPartida1(), vista.getTxtHeroe1(), vista.getTxtClase1(), vista.getTxtPiso1(), vista.getTxtNivel1(), p);
        } else {
            idPartidaRanura2 = -1;
            vaciarRanuraVisual(vista.getTxtPartida1(), vista.getTxtHeroe1(), vista.getTxtClase1(), vista.getTxtPiso1(), vista.getTxtNivel1());
        }

        // Actualización de ranura 3
        if (partidas.size() > 2) {
            Map<String, Object> p = partidas.get(2);
            idPartidaRanura3 = (int) p.get("partida_id");
            actualizarRanuraVisual(vista.getTxtPartida2(), vista.getTxtHeroe2(), vista.getTxtClase2(), vista.getTxtPiso2(), vista.getTxtNivel2(), p);
        } else {
            idPartidaRanura3 = -1;
            vaciarRanuraVisual(vista.getTxtPartida2(), vista.getTxtHeroe2(), vista.getTxtClase2(), vista.getTxtPiso2(), vista.getTxtNivel2());
        }
    }

    private void configurarClicRanura(javax.swing.JPanel panel, int numeroRanura) {
        java.awt.event.MouseAdapter mouseHandler = new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                marcarRanuraComoSeleccionada(numeroRanura);
            }
        };
        panel.addMouseListener(mouseHandler);
        for (java.awt.Component comp : panel.getComponents()) {
            comp.addMouseListener(mouseHandler);
        }
    }

    private int obtenerIdPorRanura(int numeroRanura) {
        switch (numeroRanura) {
            case 1:
                return idPartidaRanura1;
            case 2:
                return idPartidaRanura2;
            case 3:
                return idPartidaRanura3;
            default:
                return -1;
        }
    }

    private void marcarRanuraComoSeleccionada(int numeroRanura) {
        this.ranuraSeleccionada = numeroRanura;
        vista.getPanelGuardados1().setBorder(null);
        vista.getPanelGuardados2().setBorder(null);
        vista.getPanelGuardados3().setBorder(null);

        Border bordeResaltado = BorderFactory.createLineBorder(new Color(212, 175, 55), 2);
        if (numeroRanura == 1) {
            vista.getPanelGuardados1().setBorder(bordeResaltado);
        }
        if (numeroRanura == 2) {
            vista.getPanelGuardados2().setBorder(bordeResaltado);
        }
        if (numeroRanura == 3) {
            vista.getPanelGuardados3().setBorder(bordeResaltado);
        }
    }

    private void deseleccionarTodas() {
        this.ranuraSeleccionada = -1;
        vista.getPanelGuardados1().setBorder(null);
        vista.getPanelGuardados2().setBorder(null);
        vista.getPanelGuardados3().setBorder(null);
    }

    private void actualizarRanuraVisual(javax.swing.JLabel lblP, javax.swing.JLabel lblH, javax.swing.JLabel lblC, javax.swing.JLabel lblPi, javax.swing.JLabel lblN, Map<String, Object> p) {
        lblP.setText(String.valueOf(p.get("nombre_partida")));
        lblH.setText("Héroe: " + (p.get("heroe_nombre") != null ? p.get("heroe_nombre") : "Sin Nombre"));
        lblC.setText("Clase: " + (p.get("clase") != null ? p.get("clase") : "-"));
        lblPi.setText("Piso: " + p.get("piso_actual"));
        lblN.setText("Nivel: " + (p.get("nivel") != null ? p.get("nivel") : "-"));
    }

    private void vaciarRanuraVisual(javax.swing.JLabel lblP, javax.swing.JLabel lblH, javax.swing.JLabel lblC, javax.swing.JLabel lblPi, javax.swing.JLabel lblN) {
        lblP.setText("Ranura Vacía");
        lblH.setText("-");
        lblC.setText("-");
        lblPi.setText("-");
        lblN.setText("-");
    }
}
