package controlador.eventos;

import javax.swing.JOptionPane;
import vista.VistaContinuarRun;

public class ContinuarRunController {

    private VistaContinuarRun vista;
    private AccionUsuarioController ctrlPrincipal;

    public ContinuarRunController(VistaContinuarRun vista) {
        this.vista = vista;
        this.ctrlPrincipal = AccionUsuarioController.getInstancia();
        inicializarEventos();
    }

    private void inicializarEventos() {
        vista.getBtnContinuarRun().addActionListener(e -> {
            ctrlPrincipal.clickAvanzarSiguientePiso();
        });

        vista.getBtnGuardarPartida().addActionListener(e -> {
            ctrlPrincipal.clickGuardarPartida();
            System.out.println("Partida guardada desde su propio controlador.");
        });

        vista.getBtnSalir().addActionListener(e -> {
            // Lanza un cuadro de diálogo de confirmación con un icono de advertencia
            int opcion = JOptionPane.showConfirmDialog(
                    vista,
                    "¿Estás seguro de que quieres salir? Cualquier progreso no guardado se perderá.",
                    "Advertencia de Guardado",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            // Si el usuario confirma que quiere salir
            if (opcion == JOptionPane.YES_OPTION) {
                ctrlPrincipal.clickVolverAlInicio();
                vista.dispose();
            }
        });
    }
}
