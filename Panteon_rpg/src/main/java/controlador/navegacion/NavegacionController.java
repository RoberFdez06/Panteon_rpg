package controlador.navegacion;

import vista.*;
import javax.swing.JFrame;

public class NavegacionController {

    // 1. Añadimos la variable estática privada para guardar la única instancia
    private static NavegacionController instancia;
    private JFrame vistaActual;

    // 2. Volvemos el constructor privado para que nadie pueda hacer un "new NavegacionController()" fuera de aquí
    private NavegacionController() {
    }

    // 3. Creamos el método público getInstancia() que te estaba dando el error
    public static NavegacionController getInstancia() {
        if (instancia == null) {
            instancia = new NavegacionController();
        }
        return instancia;
    }

    // Método para cambiar de pantalla
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

    // --- Métodos específicos de navegación ---
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
