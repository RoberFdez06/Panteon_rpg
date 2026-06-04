package controlador.eventos;

import vista.VistaMenuCreacionPersonaje;
import modelo.ConsultasRPG;
import javax.swing.JOptionPane;

/**
 * Controlador encargado de gestionar el proceso de creación de nuevos
 * personajes, incluyendo la selección de clases y la validación de nombres
 * únicos en la base de datos.
 */
public class MenuCreacionPersonajeController {

    private VistaMenuCreacionPersonaje vista;
    private AccionUsuarioController ctrlPrincipal;
    private ConsultasRPG consultas;

    /**
     * Inicializa el controlador con la vista asociada y configura los
     * componentes necesarios.
     *
     * @param vista Instancia de la vista de creación de personaje.
     */
    public MenuCreacionPersonajeController(VistaMenuCreacionPersonaje vista) {
        this.vista = vista;
        this.ctrlPrincipal = AccionUsuarioController.getInstancia();
        this.consultas = new ConsultasRPG();
        inicializarEventos();
    }

    /**
     * Registra los eventos de selección de clase, validación de nombre y
     * navegación.
     */
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
                JOptionPane.showMessageDialog(vista,
                        "¡El nombre del héroe no puede estar vacío!",
                        "Error de creación",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (consultas.existeNombrePartida(nombreIngresado)) {
                JOptionPane.showMessageDialog(vista,
                        "Ya existe un héroe con el nombre '" + nombreIngresado + "'. Elige otro.",
                        "Nombre duplicado",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            ctrlPrincipal.clickIniciarRun(nombreIngresado);
        });

        vista.getVolverInicio().addActionListener(e -> ctrlPrincipal.clickVolverAlInicio());
    }
}
