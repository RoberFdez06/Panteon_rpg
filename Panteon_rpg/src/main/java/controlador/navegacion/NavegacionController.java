package controlador.navegacion;

import vista.*;
import javax.swing.JFrame;

/**
 * Controlador de navegación centralizado implementado como Singleton. Gestiona
 * el ciclo de vida de las vistas, encargándose de cerrar la ventana actual y
 * mostrar la nueva ventana de manera ordenada.
 */
public class NavegacionController {

    private static NavegacionController instancia;
    private JFrame vistaActual;

    /**
     * Constructor privado para garantizar el patrón Singleton.
     */
    private NavegacionController() {
    }

    /**
     * Obtiene la instancia única del controlador de navegación.
     *
     * @return La instancia única de NavegacionController.
     */
    public static NavegacionController getInstancia() {
        if (instancia == null) {
            instancia = new NavegacionController();
        }
        return instancia;
    }

    /**
     * Realiza la transición entre la vista actual y una nueva vista. Cierra la
     * ventana existente, centra la nueva y la pone en primer plano.
     *
     * @param nuevaVista La vista que se desea mostrar.
     */
    public void cambiarVista(JFrame nuevaVista) {
        if (vistaActual != null) {
            vistaActual.dispose();
        }
        this.vistaActual = nuevaVista;
        this.vistaActual.setLocationRelativeTo(null);
        this.vistaActual.setVisible(true);
        this.vistaActual.toFront();
        this.vistaActual.requestFocus();
    }

    // --- Métodos de navegación a vistas específicas ---
    public void irACreacionPersonaje() {
        cambiarVista(new VistaMenuCreacionPersonaje());
    }

    public void irACombate() {
        cambiarVista(new VistaCombates());
    }

    public void irABestiario() {
        cambiarVista(new VistaBestiario());
    }

    public void irAVictoria() {
        cambiarVista(new VistaHasGanado());
    }

    public void irADerrota() {
        cambiarVista(new VistaHasPerdido());
    }

    public void irAMenuPrincipal() {
        cambiarVista(new VistaMenuInicial());
    }

    public void irAMenuPartidas() {
        cambiarVista(new VistaMenuPartidas());
    }

    public void irAContinuarRun() {
        cambiarVista(new VistaContinuarRun());
    }

    public void irACementerio() {
        cambiarVista(new VistaCementerio());
    }
}
