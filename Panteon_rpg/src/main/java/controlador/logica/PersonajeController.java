package controlador.logica;

import modelo.Personaje;

/**
 * Controlador encargado de gestionar el estado lógico y los atributos del
 * personaje durante la partida.
 */
public class PersonajeController {

    private Personaje personajeActual;

    /**
     * Inicializa o actualiza los datos del personaje con la información
     * proveniente de la base de datos.
     */
    public void cargarPersonaje(int id, int partida_id, String nombre, String clase, int nivel,
            int experiencia, int hp_max, int hp_actual, int ataque,
            int defensa, int velocidad, int suerte, int estamina_max,
            int estamina_actual) {

        this.personajeActual = new Personaje(id, partida_id, nombre, clase, nivel, experiencia,
                hp_max, hp_actual, ataque, defensa, velocidad,
                suerte, estamina_max, estamina_actual);
    }

    /**
     * Aplica daño al personaje reduciendo su vida actual, asegurando que esta
     * no sea menor a cero.
     *
     * @param cantidad Cantidad de puntos de daño a restar.
     */
    public void recibirDano(int cantidad) {
        if (personajeActual != null) {
            int nuevaVida = personajeActual.getHp_actual() - cantidad;
            personajeActual.setHp_actual(Math.max(0, nuevaVida));
        }
    }

    /**
     * Obtiene la instancia del modelo de personaje actual.
     *
     * @return El objeto Personaje cargado en el controlador.
     */
    public Personaje getPersonaje() {
        return personajeActual;
    }
}
