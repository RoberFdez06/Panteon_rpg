package controlador.logica;

import modelo.ConsultasRPG;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Controlador de lógica para los monstruos. Gestiona la carga de datos desde la
 * base de datos y el estado vital del enemigo durante el combate.
 */
public class MonstruoController {

    private int id;
    private String nombreActual;
    private int hpMax;
    private int hpActual;
    private int ataque;

    /**
     * Carga un monstruo aleatorio desde la base de datos y establece sus
     * valores iniciales.
     *
     * @return true si la carga fue exitosa, false en caso contrario.
     */
    public boolean cargarMonstruoAleatorio() {
        ConsultasRPG consultas = new ConsultasRPG();
        ResultSet rs = consultas.obtenerMonstruoAleatorio();

        try {
            if (rs != null && rs.next()) {
                this.id = rs.getInt("id");
                this.nombreActual = rs.getString("nombre");
                this.hpMax = rs.getInt("hp_max");
                this.hpActual = this.hpMax;
                this.ataque = rs.getInt("ataque");

                rs.getStatement().getConnection().close();
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar monstruo en controlador: " + e.getMessage());
        }
        return false;
    }

    /**
     * Carga un monstruo específico de la base de datos filtrando por su nombre.
     *
     * @param nombreBuscado El nombre del monstruo a buscar.
     * @return true si se encontró y cargó correctamente, false en caso
     * contrario.
     */
    public boolean cargarMonstruoPorNombre(String nombreBuscado) {
        try {
            modelo.ConexionBD conexionBD = new modelo.ConexionBD() {
            };
            java.sql.Connection con = conexionBD.getConexion();
            String sql = "SELECT * FROM monstruos WHERE LOWER(nombre) = LOWER(?)";

            java.sql.PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, nombreBuscado);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                this.id = rs.getInt("id");
                this.nombreActual = rs.getString("nombre");
                this.hpMax = rs.getInt("hp_max");
                this.hpActual = this.hpMax;
                this.ataque = rs.getInt("ataque");

                rs.close();
                ps.close();
                con.close();
                return true;
            }
        } catch (Exception e) {
            System.err.println("Error al cargar monstruo por nombre (" + nombreBuscado + "): " + e.getMessage());
        }
        return false;
    }

    /**
     * Aplica daño al monstruo, reduciendo sus puntos de vida.
     *
     * @param cantidad Cantidad de daño recibido.
     */
    public void recibirDano(int cantidad) {
        this.hpActual = Math.max(0, this.hpActual - cantidad);
    }

    /**
     * Determina si el monstruo ha sido derrotado.
     *
     * @return true si la vida actual es menor o igual a cero.
     */
    public boolean estaMuerto() {
        return this.hpActual <= 0;
    }

    // --- GETTERS ---
    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombreActual;
    }

    public int getHpActual() {
        return hpActual;
    }

    public int getHpMax() {
        return hpMax;
    }

    public int getAtaque() {
        return ataque;
    }

    // --- SETTERS ---
    public void setHpMax(int hpMax) {
        this.hpMax = hpMax;
    }

    public void setHpActual(int hpActual) {
        this.hpActual = hpActual;
    }

    public void setAtaque(int ataque) {
        this.ataque = ataque;
    }
}
