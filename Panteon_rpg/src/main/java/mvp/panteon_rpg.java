package mvp;

import controlador.navegacion.NavegacionController;
import vista.VistaMenuInicial;

public class panteon_rpg {

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            NavegacionController.getInstancia().cambiarVista(new VistaMenuInicial());
        });
    }
}