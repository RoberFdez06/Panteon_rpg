package controlador.eventos;

import vista.VistaHasGanado;

/**
 * Controlador encargado de gestionar la pantalla de victoria del jugador tras
 * derrotar a un enemigo. Facilita la transición hacia la siguiente fase de la
 * aventura.
 */
public class HasGanadoController {

    private VistaHasGanado vista;
    private AccionUsuarioController ctrlPrincipal;

    /**
     * Inicializa el controlador con la vista de victoria asociada.
     *
     * @param vista Instancia de la vista que se muestra al ganar.
     */
    public HasGanadoController(VistaHasGanado vista) {
        this.vista = vista;
        this.ctrlPrincipal = AccionUsuarioController.getInstancia();
        inicializarEventos();
    }

    /**
     * Configura los eventos de interacción de la interfaz de usuario.
     */
    private void inicializarEventos() {
        vista.getBtnContinuar().addActionListener(e -> {
            ctrlPrincipal.clickContinuarVictoria();
        });
    }
}
