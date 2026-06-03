package controlador.eventos;

import vista.VistaHasPerdido;

public class HasPerdidoController {

    private VistaHasPerdido vista;
    private AccionUsuarioController ctrlPrincipal;

    public HasPerdidoController(VistaHasPerdido vista) {
        this.vista = vista;
        this.ctrlPrincipal = AccionUsuarioController.getInstancia();
        inicializarEventos();
    }

    private void inicializarEventos() {
        vista.getBtnSalir().addActionListener(e -> ctrlPrincipal.clickSalirDerrota());
    }
}
