package controlador.logica;

import modelo.Personaje;

public class PersonajeController {

    private Personaje personajeActual;

    // Método para inicializar el personaje cuando lo cargas de la BD
    public void cargarPersonaje(int id, int partida_id, String nombre, String clase, int nivel,
            int experiencia, int hp_max, int hp_actual, int ataque,
            int defensa, int velocidad, int suerte, int estamina_max,
            int estamina_actual) {

        this.personajeActual = new Personaje(id, partida_id, nombre, clase, nivel, experiencia,
                hp_max, hp_actual, ataque, defensa, velocidad,
                suerte, estamina_max, estamina_actual);
    }

    // Método para aplicar daño (lógica de combate)
    public void recibirDano(int cantidad) {
        if (personajeActual != null) {
            int nuevaVida = personajeActual.getHp_actual() - cantidad;
            personajeActual.setHp_actual(Math.max(0, nuevaVida));
        }
    }

    public Personaje getPersonaje() {
        return personajeActual;
    }
}
