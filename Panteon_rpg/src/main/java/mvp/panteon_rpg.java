package mvp;

import controlador.navegacion.NavegacionController;
import vista.VistaMenuInicial;

/**
 * Clase principal (Entry Point) que arranca la aplicación del videojuego
 * "Panteón RPG". Inicia la interfaz gráfica de usuario en el hilo de despacho
 * de eventos de AWT (EDT) y muestra el menú inicial del juego.
 */
public class panteon_rpg {

    /**
     * Método de entrada principal de la aplicación.
     *
     * * @param args Argumentos de la línea de comandos (no se utilizan).
     */
    public static void main(String[] args) {
        // Ejecuta de forma asíncrona en el Event Dispatch Thread (EDT) para garantizar la seguridad de hilos en Swing/AWT
        java.awt.EventQueue.invokeLater(() -> {
            // Inicializa la navegación cargando la vista del menú principal del juego
            NavegacionController.getInstancia().cambiarVista(new VistaMenuInicial());
        });
    }
}
