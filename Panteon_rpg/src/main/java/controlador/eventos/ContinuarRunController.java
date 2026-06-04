package controlador.eventos;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import vista.VistaContinuarRun;

/**
 * Controlador encargado de gestionar la pantalla de transición entre niveles,
 * permitiendo al jugador avanzar en la mazmorra, guardar su progreso o
 * abandonar la run.
 */
public class ContinuarRunController implements ActionListener {

    /**
     * La vista de la pantalla intermedia controlada por esta clase.
     */
    private VistaContinuarRun vista;

    /**
     * Instancia del controlador principal encargado de las acciones globales
     * del usuario.
     */
    private AccionUsuarioController ctrlPrincipal;

    /**
     * Inicializa el controlador con la vista asociada y configura los eventos
     * de interacción.
     *
     * @param vista Instancia de la vista de continuación de run.
     */
    public ContinuarRunController(VistaContinuarRun vista) {
        this.vista = vista;
        this.ctrlPrincipal = AccionUsuarioController.getInstancia();
        inicializarEventos();
    }

    /**
     * Define y registra las acciones de los botones de la interfaz de usuario.
     */
    private void inicializarEventos() {
        vista.getBtnContinuarRun().addActionListener(e -> {
            ctrlPrincipal.clickAvanzarSiguientePiso();
        });

        // Se vincula con 'this' para que use el método actionPerformed heredado de ActionListener
        vista.getBtnGuardarPartida().addActionListener(this);

        vista.getBtnSalir().addActionListener(e -> {
            int opcion = JOptionPane.showConfirmDialog(
                    vista,
                    "¿Estás seguro de que quieres salir? Cualquier progreso no guardado se perderá.",
                    "Advertencia de Guardado",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (opcion == JOptionPane.YES_OPTION) {
                ctrlPrincipal.clickVolverAlInicio();
                vista.dispose();
            }
        });
    }

    /**
     * Gestiona los eventos de acción tradicionales. Captura el clic en el botón
     * de guardar partida, delega la persistencia en el controlador principal y
     * actualiza la etiqueta informativa con un mensaje de éxito.
     *
     * @param e Evento de acción capturado desde la interfaz gráfica.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.getBtnGuardarPartida()) {
            ctrlPrincipal.clickGuardarPartida();
            vista.getTxtInfo().setText("Se ha guardado la partida correctamente.");
            vista.getTxtInfo().setForeground(new java.awt.Color(46, 204, 113));
        }
    }
}
