package controlador.persistencia;

import modelo.ConsultasRPG;
import modelo.Personaje;

/**
 * Controlador de persistencia encargado de coordinar la comunicación entre la
 * lógica del juego y la base de datos a través de ConsultasRPG.
 */
public class DataController {

    private ConsultasRPG consultasBD;

    public DataController() {
        this.consultasBD = new ConsultasRPG();
    }

    /**
     * Inicia una nueva partida y vincula un personaje recién creado a la misma.
     *
     * @param nombrePartida El nombre dado por el usuario a la partida.
     * @param heroe El objeto Personaje instanciado.
     * @return El ID de la partida generada, o -1 si hubo un error.
     */
    public int iniciarNuevaRun(String nombrePartida, Personaje heroe) {
        int idPartida = consultasBD.crearNuevaPartida(nombrePartida);
        if (idPartida != -1) {
            heroe.setPartida_id(idPartida); // Vincula el héroe a la partida generada
            consultasBD.crearPersonaje(heroe);
        }
        return idPartida;
    }

    /**
     * Guarda el estado actual de la partida y el progreso del personaje en la
     * base de datos.
     *
     * @param p El personaje con sus estadísticas actuales.
     * @param pisoActual El número de piso donde se encuentra el jugador.
     */
    public void guardarProgreso(Personaje p, int pisoActual) {
        if (p != null) {
            consultasBD.guardarPisoActual(p.getPartida_id(), pisoActual);
            consultasBD.actualizarEstadoPersonaje(p);
        }
    }

    /**
     * Carga un personaje asociado a una partida específica desde la base de
     * datos.
     *
     * @param idPartida El ID de la partida a recuperar.
     * @return El objeto Personaje cargado o null si no se encuentra.
     */
    public Personaje cargarPartida(int idPartida) {
        return consultasBD.obtenerPersonajePorPartida(idPartida);
    }

    /**
     * Elimina permanentemente una partida y sus datos asociados.
     *
     * @param idPartida El ID de la partida a borrar.
     */
    public void borrarPartida(int idPartida) {
        consultasBD.borrarPartida(idPartida);
    }

    /**
     * Registra en el cementerio los datos del héroe tras su caída en combate.
     *
     * @param nombre Nombre del héroe.
     * @param clase Clase del héroe.
     * @param nivel Nivel alcanzado.
     * @param piso Piso donde murió.
     * @param asesino Nombre del monstruo que acabó con el héroe.
     */
    public void registrarMuerteHeroe(String nombre, String clase, int nivel, int piso, String asesino) {
        // Método destinado a interactuar con el registro histórico de caídos en el cementerio.
        // consultasBD.registrarMuerte(nombre, clase, nivel, piso, asesino, "Murió en combate.");
    }
}
