package controlador.eventos;

import vista.VistaMenuInicial;

public class MenuInicialController {

    private VistaMenuInicial vista;
    private AccionUsuarioController ctrlPrincipal;

    public MenuInicialController(VistaMenuInicial vista) {
        this.vista = vista;
        this.ctrlPrincipal = AccionUsuarioController.getInstancia();
        inicializarEventos();
    }

    private void inicializarEventos() {
        vista.getBtnNuevaPartida().addActionListener(e -> ctrlPrincipal.clickNuevaPartida());
        vista.getBtnReanudarPartida().addActionListener(e -> ctrlPrincipal.clickReanudarPartida());
        vista.getBtnBestiario().addActionListener(e -> ctrlPrincipal.clickAbrirBestiario());
        vista.getBtnCementerio().addActionListener(e -> ctrlPrincipal.clickAbrirCementerio());
        vista.getBtnSalir().addActionListener(e -> ctrlPrincipal.clickSalirJuego());
    }
}
