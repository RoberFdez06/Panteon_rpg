package controlador.eventos;

import vista.VistaMenuCreacionPersonaje;

public class MenuCreacionPersonajeController {

    private VistaMenuCreacionPersonaje vista;
    private AccionUsuarioController ctrlPrincipal;

    public MenuCreacionPersonajeController(VistaMenuCreacionPersonaje vista) {
        this.vista = vista;
        this.ctrlPrincipal = AccionUsuarioController.getInstancia();
        inicializarEventos();
    }

    private void inicializarEventos() {
        vista.getBtnGuerrero().addActionListener(e -> {
            ctrlPrincipal.seleccionarClase("Guerrero");
            vista.setLabelClaseSeleccionada("Clase: Guerrero");
        });

        vista.getBtnTanque().addActionListener(e -> {
            ctrlPrincipal.seleccionarClase("Tanque");
            vista.setLabelClaseSeleccionada("Clase: Tanque");
        });

        vista.getBtnMago().addActionListener(e -> {
            ctrlPrincipal.seleccionarClase("Mago");
            vista.setLabelClaseSeleccionada("Clase: Mago");
        });

        vista.getBtnPicaro().addActionListener(e -> {
            ctrlPrincipal.seleccionarClase("Picaro");
            vista.setLabelClaseSeleccionada("Clase: Pícaro");
        });

        vista.getBtnArquero().addActionListener(e -> {
            ctrlPrincipal.seleccionarClase("Arquero");
            vista.setLabelClaseSeleccionada("Clase: Arquero");
        });

        vista.getBtnRun().addActionListener(e -> {
            String nombreIngresado = vista.getTxtInputField().getText().trim();
            if (nombreIngresado.isEmpty()) {
                nombreIngresado = "Héroe Legendario";
            }
            ctrlPrincipal.clickIniciarRun(nombreIngresado);
        });

        vista.getVolverInicio().addActionListener(e -> ctrlPrincipal.clickVolverAlInicio());
    }
}
