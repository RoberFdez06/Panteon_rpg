package controlador.eventos;

import vista.VistaMenuPartidas;
import java.util.List;
import java.util.Map;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.border.Border;

public class MenuPartidasController {

    private VistaMenuPartidas vista;
    private AccionUsuarioController ctrlPrincipal;

    // IDs reales de la BD para cada ranura (-1 significa vacía)
    private int idPartidaRanura1 = -1;
    private int idPartidaRanura2 = -1;
    private int idPartidaRanura3 = -1;

    // Guarda el número de ranura clicado (1, 2, 3 o -1 si ninguna)
    private int ranuraSeleccionada = -1;

    public MenuPartidasController(VistaMenuPartidas vista) {
        this.vista = vista;
        this.ctrlPrincipal = AccionUsuarioController.getInstancia();
        inicializarEventos();
        cargarYMostrarPartidas();
    }

    private void inicializarEventos() {
        // --- SELECCIÓN DE RANURAS ---
        configurarClicRanura(vista.getPanelGuardados1(), 1);
        configurarClicRanura(vista.getPanelGuardados2(), 2);
        configurarClicRanura(vista.getPanelGuardados3(), 3);

        // --- BOTÓN REANUDAR / CARGAR PARTIDA (CORREGIDO) ---
        vista.getBtnCargarPartida().addActionListener(e -> {
            if (ranuraSeleccionada == -1) {
                JOptionPane.showMessageDialog(vista,
                        "Por favor, selecciona una ranura haciendo clic sobre su recuadro para continuar.",
                        "Ninguna Selección", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Averiguamos la ID de la partida seleccionada
            int idACargar = -1;
            if (ranuraSeleccionada == 1) {
                idACargar = idPartidaRanura1;
            }
            if (ranuraSeleccionada == 2) {
                idACargar = idPartidaRanura2;
            }
            if (ranuraSeleccionada == 3) {
                idACargar = idPartidaRanura3;
            }

            // Validamos si la ranura está vacía
            if (idACargar == -1) {
                JOptionPane.showMessageDialog(vista,
                        "Esa ranura está vacía. Ve al menú principal para iniciar una nueva aventura.",
                        "Ranura Vacía", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // ¡A jugar! Sincronizamos los datos, cargamos el piso real de la BD y abrimos el combate
            ctrlPrincipal.clickCargarPartida(idACargar);
        });

        // --- BOTÓN BORRAR PARTIDA ---
        vista.getBtnBorrarPartida().addActionListener(e -> {
            if (ranuraSeleccionada == -1) {
                JOptionPane.showMessageDialog(vista, "Selecciona una partida antes de borrar.", "Ninguna Selección", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int idABorrar = -1;
            if (ranuraSeleccionada == 1) {
                idABorrar = idPartidaRanura1;
            }
            if (ranuraSeleccionada == 2) {
                idABorrar = idPartidaRanura2;
            }
            if (ranuraSeleccionada == 3) {
                idABorrar = idPartidaRanura3;
            }

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
     * Muestra la información limpia directamente extraída de la base de datos
     */
    public void cargarYMostrarPartidas() {
        modelo.ConsultasRPG consultas = new modelo.ConsultasRPG();
        List<Map<String, Object>> partidas = consultas.obtenerTodasLasPartidas();

        // --- RANURA 1 ---
        if (partidas.size() > 0) {
            Map<String, Object> p = partidas.get(0);
            idPartidaRanura1 = (int) p.get("partida_id");

            vista.getTxtPartida().setText(String.valueOf(p.get("nombre_partida")));
            vista.getTxtHeroe().setText("Héroe: " + (p.get("heroe_nombre") != null ? p.get("heroe_nombre") : "Sin Nombre"));
            vista.getTxtClase().setText("Clase: " + (p.get("clase") != null ? p.get("clase") : "-"));
            vista.getTxtPiso().setText("Piso: " + p.get("piso_actual"));
            vista.getTxtNivel().setText("Nivel: " + (p.get("nivel") != null ? p.get("nivel") : "-"));
        } else {
            idPartidaRanura1 = -1;
            vaciarRanuraVisual(vista.getTxtPartida(), vista.getTxtHeroe(), vista.getTxtClase(), vista.getTxtPiso(), vista.getTxtNivel());
        }

        // --- RANURA 2 ---
        if (partidas.size() > 1) {
            Map<String, Object> p = partidas.get(1);
            idPartidaRanura2 = (int) p.get("partida_id");

            vista.getTxtPartida1().setText(String.valueOf(p.get("nombre_partida")));
            vista.getTxtHeroe1().setText("Héroe: " + (p.get("heroe_nombre") != null ? p.get("heroe_nombre") : "Sin Nombre"));
            vista.getTxtClase1().setText("Clase: " + (p.get("clase") != null ? p.get("clase") : "-"));
            vista.getTxtPiso1().setText("Piso: " + p.get("piso_actual"));
            vista.getTxtNivel1().setText("Nivel: " + (p.get("nivel") != null ? p.get("nivel") : "-"));
        } else {
            idPartidaRanura2 = -1;
            vaciarRanuraVisual(vista.getTxtPartida1(), vista.getTxtHeroe1(), vista.getTxtClase1(), vista.getTxtPiso1(), vista.getTxtNivel1());
        }

        // --- RANURA 3 ---
        if (partidas.size() > 2) {
            Map<String, Object> p = partidas.get(2);
            idPartidaRanura3 = (int) p.get("partida_id");

            vista.getTxtPartida2().setText(String.valueOf(p.get("nombre_partida")));
            vista.getTxtHeroe2().setText("Héroe: " + (p.get("heroe_nombre") != null ? p.get("heroe_nombre") : "Sin Nombre"));
            vista.getTxtClase2().setText("Clase: " + (p.get("clase") != null ? p.get("clase") : "-"));
            vista.getTxtPiso2().setText("Piso: " + p.get("piso_actual"));
            vista.getTxtNivel2().setText("Nivel: " + (p.get("nivel") != null ? p.get("nivel") : "-"));
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

    private void vaciarRanuraVisual(javax.swing.JLabel lblP, javax.swing.JLabel lblH, javax.swing.JLabel lblC, javax.swing.JLabel lblPi, javax.swing.JLabel lblN) {
        lblP.setText("Ranura Vacía");
        lblH.setText("-");
        lblC.setText("-");
        lblPi.setText("-");
        lblN.setText("-");
    }
}
