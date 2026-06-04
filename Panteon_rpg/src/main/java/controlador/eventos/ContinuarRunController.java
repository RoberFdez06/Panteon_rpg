package controlador.eventos;

import javax.swing.JOptionPane;
import vista.VistaContinuarRun;

/**
 * Controlador encargado de gestionar la pantalla de transición entre niveles,
 * permitiendo al jugador avanzar en la mazmorra, guardar su progreso o
 * abandonar la run.
 */
public class ContinuarRunController {

    private VistaContinuarRun vista;
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

        vista.getBtnGuardarPartida().addActionListener(e -> {
            ctrlPrincipal.clickGuardarPartida();
        });

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
}
