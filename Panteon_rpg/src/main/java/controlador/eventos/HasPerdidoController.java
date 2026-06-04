package controlador.eventos;

import vista.VistaHasPerdido;

/**
 * Controlador encargado de gestionar la pantalla de derrota del jugador. Maneja
 * la lógica de salida tras finalizar la partida.
 */
public class HasPerdidoController {

    private VistaHasPerdido vista;
    private AccionUsuarioController ctrlPrincipal;

    /**
     * Inicializa el controlador con la vista de derrota asociada.
     *
     * @param vista Instancia de la vista que se muestra al perder.
     */
    public HasPerdidoController(VistaHasPerdido vista) {
        this.vista = vista;
        this.ctrlPrincipal = AccionUsuarioController.getInstancia();
        inicializarEventos();
    }

    /**
     * Configura los eventos de interacción de la interfaz de usuario.
     */
    private void inicializarEventos() {
        vista.getBtnSalir().addActionListener(e -> ctrlPrincipal.clickSalirDerrota());
    }
}
