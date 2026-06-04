package modelo;

/**
 * Clase que representa una partida guardada en el juego. Almacena los datos
 * básicos del progreso y metadatos de la sesión de juego.
 */
public class Partida {

    private int id;
    private String nombre_partida;
    private int piso_actual;
    private String fecha_creacion;

    /**
     * Constructor completo para inicializar todos los atributos de una partida.
     *
     * @param id Identificador único de la partida en la base de datos.
     * @param nombre_partida Nombre descriptivo asignado a la partida.
     * @param piso_actual El piso actual en el que se encuentra el jugador
     * dentro de la run.
     * @param fecha_creacion Fecha y hora en la que se creó el registro del
     * juego.
     */
    public Partida(int id, String nombre_partida, int piso_actual, String fecha_creacion) {
        this.id = id;
        this.nombre_partida = nombre_partida;
        this.piso_actual = piso_actual;
        this.fecha_creacion = fecha_creacion;
    }

    // --- Getters y Setters ---
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre_partida() {
        return nombre_partida;
    }

    public void setNombre_partida(String nombre_partida) {
        this.nombre_partida = nombre_partida;
    }

    public int getPiso_actual() {
        return piso_actual;
    }

    public void setPiso_actual(int piso_actual) {
        this.piso_actual = piso_actual;
    }

    public String getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(String fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }
}
