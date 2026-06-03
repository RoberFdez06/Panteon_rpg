package controlador.persistencia;

import modelo.ConsultasRPG;
import modelo.Personaje;

public class DataController {

    private ConsultasRPG consultasBD;

    public DataController() {
        this.consultasBD = new ConsultasRPG();
    }

    // --- MÉTODOS DE PARTIDA Y PERSONAJE ---
    public int iniciarNuevaRun(String nombrePartida, Personaje heroe) {
        int idPartida = consultasBD.crearNuevaPartida(nombrePartida);
        if (idPartida != -1) {
            heroe.setPartida_id(idPartida); // Vincula el héroe a la partida generada
            consultasBD.crearPersonaje(heroe);
        }
        return idPartida;
    }

    public void guardarProgreso(Personaje p, int pisoActual) {
        if (p != null) {
            consultasBD.guardarPisoActual(p.getPartida_id(), pisoActual);
            consultasBD.actualizarEstadoPersonaje(p);
        }
    }

    public Personaje cargarPartida(int idPartida) {
        return consultasBD.obtenerPersonajePorPartida(idPartida);
    }

    public void borrarPartida(int idPartida) {
        consultasBD.borrarPartida(idPartida);
    }

    // --- MÉTODOS DE HISTORIAL (CEMENTERIO / BESTIARIO) ---
    public void registrarMuerteHeroe(String nombre, String clase, int nivel, int piso, String asesino) {
        // Asumiendo que añadas este método a ConsultasRPG
        // consultasBD.registrarMuerte(nombre, clase, nivel, piso, asesino, "Murió en combate.");
    }
}
