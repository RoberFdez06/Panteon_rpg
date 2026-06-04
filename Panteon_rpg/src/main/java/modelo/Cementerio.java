package modelo;

/**
 * Clase que representa un registro en el cementerio del juego. Almacena la
 * información de los héroes caídos tras finalizar una partida.
 */
public class Cementerio {

    private int id;
    private String nombre_heroe;
    private String clase;
    private int nivel_alcanzado;
    private int piso_final;
    private String estado;
    private String asesino;
    private String epitafio;

    /**
     * Constructor para inicializar una nueva instancia de registro de
     * cementerio.
     *
     * * @param id Identificador único del registro.
     * @param nombre_heroe Nombre del héroe.
     * @param clase Clase del héroe.
     * @param nivel_alcanzado Nivel alcanzado por el héroe antes de morir.
     * @param piso_final Piso en el que ocurrió la derrota.
     * @param estado Estado de la partida (ej. "Derrota").
     * @param asesino Nombre del monstruo que causó la derrota.
     * @param epitafio Mensaje conmemorativo final.
     */
    public Cementerio(int id, String nombre_heroe, String clase, int nivel_alcanzado,
            int piso_final, String estado, String asesino, String epitafio) {
        this.id = id;
        this.nombre_heroe = nombre_heroe;
        this.clase = clase;
        this.nivel_alcanzado = nivel_alcanzado;
        this.piso_final = piso_final;
        this.estado = estado;
        this.asesino = asesino;
        this.epitafio = epitafio;
    }

    // --- Getters y Setters ---
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre_heroe() {
        return nombre_heroe;
    }

    public void setNombre_heroe(String nombre_heroe) {
        this.nombre_heroe = nombre_heroe;
    }

    public String getClase() {
        return clase;
    }

    public void setClase(String clase) {
        this.clase = clase;
    }

    public int getNivel_alcanzado() {
        return nivel_alcanzado;
    }

    public void setNivel_alcanzado(int nivel_alcanzado) {
        this.nivel_alcanzado = nivel_alcanzado;
    }

    public int getPiso_final() {
        return piso_final;
    }

    public void setPiso_final(int piso_final) {
        this.piso_final = piso_final;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getAsesino() {
        return asesino;
    }

    public void setAsesino(String asesino) {
        this.asesino = asesino;
    }

    public String getEpitafio() {
        return epitafio;
    }

    public void setEpitafio(String epitafio) {
        this.epitafio = epitafio;
    }
}
