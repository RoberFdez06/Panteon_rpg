package controlador.eventos;

import modelo.Personaje;
import vista.VistaSubidaNivel;

/**
 * Controlador encargado de gestionar la pantalla de subida de nivel. Calcula la
 * diferencia entre las estadísticas anteriores y las nuevas, inyecta los textos
 * formateados en la vista y gestiona la confirmación del usuario.
 *
 * * @author rober
 */
public class SubidaNivelController {

    /**
     * La vista de subida de nivel controlada por esta clase.
     */
    private final VistaSubidaNivel vista;

    /**
     * Instancia del controlador principal para gestionar el flujo global del
     * juego.
     */
    private final AccionUsuarioController ctrlPrincipal;

    /**
     * Inicializa el controlador, calcula la transición de atributos del héroe y
     * registra los eventos de interacción de la interfaz gráfica.
     *
     * @param vista Instancia de la vista de subida de nivel.
     * @param antiguo Instancia del personaje con sus estadísticas previas al
     * ascenso.
     * @param nuevo Instancia del personaje con sus estadísticas ya
     * actualizadas.
     */
    public SubidaNivelController(VistaSubidaNivel vista, Personaje antiguo, Personaje nuevo) {
        this.vista = vista;
        this.ctrlPrincipal = AccionUsuarioController.getInstancia();

        cargarDatosEnPantalla(antiguo, nuevo);
        inicializarEventos();
    }

    /**
     * Compara los atributos anteriores y nuevos para generar texto formateado
     * en HTML que resalte en verde los incrementos obtenidos.
     *
     * @param antiguo Estadísticas antes del nivel superior.
     * @param nuevo Estadísticas después del nivel superior.
     */
    private void cargarDatosEnPantalla(Personaje antiguo, Personaje nuevo) {
        // Título del nivel alcanzado
        vista.getTxtNivel().setText("¡NIVEL " + nuevo.getNivel() + " ALCANZADO!");

        // Cálculo de incrementos individuales basados estrictamente en el modelo Personaje
        int incHp = nuevo.getHp_max() - antiguo.getHp_max();
        int incEstamina = nuevo.getEstamina_max() - antiguo.getEstamina_max();
        int incAtaque = nuevo.getAtaque() - antiguo.getAtaque();
        int incDefensa = nuevo.getDefensa() - antiguo.getDefensa();
        int incVelocidad = nuevo.getVelocidad() - antiguo.getVelocidad();
        int incSuerte = nuevo.getSuerte() - antiguo.getSuerte();

        // Inyección de textos combinando la base blanca y la bonificación en verde medieval
        vista.getTxtHp().setText("<html><font color='white'>Salud Máxima: " + antiguo.getHp_max() + "</font> <font color='#2ecc71'>+ " + incHp + "</font></html>");
        vista.getTxtEstamina().setText("<html><font color='white'>Estamina Máxima: " + antiguo.getEstamina_max() + "</font> <font color='#2ecc71'>+ " + incEstamina + "</font></html>");
        vista.getTxtAtaque().setText("<html><font color='white'>Ataque: " + antiguo.getAtaque() + "</font> <font color='#2ecc71'>+ " + incAtaque + "</font></html>");
        vista.getTxtDefensa().setText("<html><font color='white'>Defensa: " + antiguo.getDefensa() + "</font> <font color='#2ecc71'>+ " + incDefensa + "</font></html>");
        vista.getTxtVelocidad().setText("<html><font color='white'>Velocidad: " + antiguo.getVelocidad() + "</font> <font color='#2ecc71'>+ " + incVelocidad + "</font></html>");
        vista.getTxtSuerte().setText("<html><font color='white'>Suerte: " + antiguo.getSuerte() + "</font> <font color='#2ecc71'>+ " + incSuerte + "</font></html>");
    }

    /**
     * Define y registra las acciones del botón de confirmación de la interfaz.
     */
    private void inicializarEventos() {
        vista.getBtnContinuar().addActionListener(e -> {
            // Cierra la interfaz de nivel y delega la reanudación al controlador principal
            vista.dispose();
            ctrlPrincipal.clickContinuarTrasSubidaNivel();
        });
    }
}
