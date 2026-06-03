package modelo;

public class Monstruo {

    private int id;
    private String nombre;
    private String tipo;
    private int hp_max;
    private int ataque;
    private int defensa;
    private int velocidad;
    private int suerte;
    private boolean descubierto;

    public Monstruo() {
        this.id = 0;
        this.nombre = "Desconocido";
        this.tipo = "Normal";
        this.hp_max = 10;
        this.ataque = 1;
        this.defensa = 1;
        this.velocidad = 1;
        this.suerte = 1;
        this.descubierto = false;
    }

    public Monstruo(int id, String nombre, String tipo, int hp_max, int ataque, int defensa, int velocidad, int suerte) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.hp_max = hp_max;
        this.ataque = ataque;
        this.defensa = defensa;
        this.velocidad = velocidad;
        this.suerte = suerte;
    }

    public Monstruo(int id, String nombre, String tipo, int hp_max, int ataque, int defensa, int velocidad, int suerte, boolean descubierto) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.hp_max = hp_max;
        this.ataque = ataque;
        this.defensa = defensa;
        this.velocidad = velocidad;
        this.suerte = suerte;
        this.descubierto = descubierto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getHp_max() {
        return hp_max;
    }

    public void setHp_max(int hp_max) {
        this.hp_max = hp_max;
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

    public boolean isDescubierto() {
        return descubierto;
    }

    public void setDescubierto(boolean descubierto) {
        this.descubierto = descubierto;
    }
}
