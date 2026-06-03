package controlador.eventos;

import vista.VistaMenuCreacionPersonaje;
import modelo.ConsultasRPG;
import javax.swing.JOptionPane;

public class MenuCreacionPersonajeController {

    private VistaMenuCreacionPersonaje vista;
    private AccionUsuarioController ctrlPrincipal;
    private ConsultasRPG consultas; // Añadimos acceso a las consultas

    public MenuCreacionPersonajeController(VistaMenuCreacionPersonaje vista) {
        this.vista = vista;
        this.ctrlPrincipal = AccionUsuarioController.getInstancia();
        this.consultas = new ConsultasRPG();
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

        // --- VALIDACIÓN DE NOMBRE Y BBDD ---
        vista.getBtnRun().addActionListener(e -> {
            String nombreIngresado = vista.getTxtInputField().getText().trim();

            // 1. Validar que no esté vacío
            if (nombreIngresado.isEmpty()) {
                JOptionPane.showMessageDialog(vista,
                        "¡El nombre del héroe no puede estar vacío!",
                        "Error de creación",
                        JOptionPane.WARNING_MESSAGE);
                return; // Cortamos aquí la ejecución
            }

            // 2. Validar duplicidad en la BBDD
            if (consultas.existeNombrePartida(nombreIngresado)) {
                JOptionPane.showMessageDialog(vista,
                        "Ya existe un héroe con el nombre '" + nombreIngresado + "'. Elige otro.",
                        "Nombre duplicado",
                        JOptionPane.ERROR_MESSAGE);
                return; // Cortamos aquí la ejecución
            }

            // 3. Si pasa los filtros, iniciamos la run
            ctrlPrincipal.clickIniciarRun(nombreIngresado);
        });

        vista.getVolverInicio().addActionListener(e -> ctrlPrincipal.clickVolverAlInicio());
    }
}
