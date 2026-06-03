package modelo;

public class Partida {

    private int id;
    private String nombre_partida;
    private int piso_actual;
    private String fecha_creacion;

    public Partida(int id, String nombre_partida, int piso_actual, String fecha_creacion) {
        this.id = id;
        this.nombre_partida = nombre_partida;
        this.piso_actual = piso_actual;
        this.fecha_creacion = fecha_creacion;
    }

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
