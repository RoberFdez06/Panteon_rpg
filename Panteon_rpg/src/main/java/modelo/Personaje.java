package modelo;

/**
 * Clase que representa al héroe controlado por el jugador en la partida.
 * Contiene todas sus estadísticas de combate, nivel, experiencia y estado
 * actual.
 */
public class Personaje {

    private int id;
    private int partida_id;
    private String nombre;
    private String clase;
    private int nivel;
    private int experiencia;
    private int hp_max;
    private int hp_actual;
    private int ataque;
    private int defensa;
    private int velocidad;
    private int suerte;
    private int estamina_max;
    private int estamina_actual;

    /**
     * Constructor por defecto que inicializa un personaje con estadísticas base
     * genéricas.
     */
    public Personaje() {
        this.id = 0;
        this.partida_id = 0;
        this.nombre = "Desconocido";
        this.clase = "Sin clase";
        this.nivel = 1;
        this.experiencia = 0;
        this.hp_max = 100;
        this.hp_actual = 100;
        this.ataque = 10;
        this.defensa = 10;
        this.velocidad = 5;
        this.suerte = 5;
        this.estamina_max = 50;
        this.estamina_actual = 50;
    }

    /**
     * Constructor parametrizado para inicializar un personaje con todas sus
     * estadísticas completas (utilizado habitualmente al cargar datos desde la
     * base de datos).
     *
     * @param id Identificador único del personaje en la base de datos.
     * @param partida_id ID de la partida a la que pertenece el personaje.
     * @param nombre Nombre asignado al héroe.
     * @param clase Clase o profesión elegida (ej. Guerrero, Mago).
     * @param nivel Nivel de progreso actual del héroe.
     * @param experiencia Puntos de experiencia acumulados en el nivel actual.
     * @param hp_max Puntos de salud máximos permitidos.
     * @param hp_actual Puntos de salud que posee actualmente el personaje.
     * @param ataque Poder ofensivo base para calcular el daño físico o mágico.
     * @param defensa Atributo defensivo que mitiga el daño recibido de los
     * enemigos.
     * @param velocidad Determina el orden de los turnos en el sistema de
     * combate.
     * @param suerte Influye en la probabilidad de golpes críticos y eventos
     * fortuitos.
     * @param estamina_max Puntos de estamina máximos para realizar acciones
     * especiales.
     * @param estamina_actual Cantidad de estamina disponible en el momento
     * actual.
     */
    public Personaje(int id, int partida_id, String nombre, String clase, int nivel, int experiencia,
            int hp_max, int hp_actual, int ataque, int defensa, int velocidad, int suerte,
            int estamina_max, int estamina_actual) {
        this.id = id;
        this.partida_id = partida_id;
        this.nombre = nombre;
        this.clase = clase;
        this.nivel = nivel;
        this.experiencia = experiencia;
        this.hp_max = hp_max;
        this.hp_actual = hp_actual;
        this.ataque = ataque;
        this.defensa = defensa;
        this.velocidad = velocidad;
        this.suerte = suerte;
        this.estamina_max = estamina_max;
        this.estamina_actual = estamina_actual;
    }

    // --- Getters y Setters ---
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPartida_id() {
        return partida_id;
    }

    public void setPartida_id(int partida_id) {
        this.partida_id = partida_id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getClase() {
        return clase;
    }

    public void setClase(String clase) {
        this.clase = clase;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public int getExperiencia() {
        return experiencia;
    }

    public void setExperiencia(int experiencia) {
        this.experiencia = experiencia;
    }

    public int getHp_max() {
        return hp_max;
    }

    public void setHp_max(int hp_max) {
        this.hp_max = hp_max;
    }

    public int getHp_actual() {
        return hp_actual;
    }

    public void setHp_actual(int hp_actual) {
        this.hp_actual = hp_actual;
    }

    public int getAtaque() {
        return ataque;
    }

    public void setAtaque(int ataque) {
        this.ataque = ataque;
    }

    public int getDefensa() {
        return defensa;
    }

    public void setDefensa(int defensa) {
        this.defensa = defensa;
    }

    public int getVelocidad() {
        return velocidad;
    }

    public void setVelocidad(int velocidad) {
        this.velocidad = velocidad;
    }

    public int getSuerte() {
        return suerte;
    }

    public void setSuerte(int suerte) {
        this.suerte = suerte;
    }

    public int getEstamina_max() {
        return estamina_max;
    }

    public void setEstamina_max(int estamina_max) {
        this.estamina_max = estamina_max;
    }

    public int getEstamina_actual() {
        return estamina_actual;
    }

    public void setEstamina_actual(int estamina_actual) {
        this.estamina_actual = estamina_actual;
    }
}
