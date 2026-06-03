package controlador.eventos;

import vista.VistaHasGanado;

public class HasGanadoController {

    private VistaHasGanado vista;
    private AccionUsuarioController ctrlPrincipal;

    public HasGanadoController(VistaHasGanado vista) {
        this.vista = vista;
        this.ctrlPrincipal = AccionUsuarioController.getInstancia();
        inicializarEventos();
    }

    private void inicializarEventos() {
        // Vinculamos el botón de continuar usando el getter de la vista
        vista.getBtnContinuar().addActionListener(e -> {
            ctrlPrincipal.clickContinuarVictoria();
            // No hace falta hacer dispose aquí, ya que el NavegacionController se encarga de cerrar la vistaActual
        });
    }
}