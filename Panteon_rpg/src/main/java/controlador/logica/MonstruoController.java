package controlador.logica;

import modelo.ConsultasRPG;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MonstruoController {

    private int id; // [NUEVO] Guardamos la ID única de la base de datos
    private String nombreActual;
    private int hpMax;
    private int hpActual;
    private int ataque;

    // Llama a la BD, elige un monstruo al azar y guarda sus datos temporalmente
    public boolean cargarMonstruoAleatorio() {
        ConsultasRPG consultas = new ConsultasRPG();
        ResultSet rs = consultas.obtenerMonstruoAleatorio();

        try {
            if (rs != null && rs.next()) {
                this.id = rs.getInt("id"); // [NUEVO] Sacamos la ID de la fila de la BD
                this.nombreActual = rs.getString("nombre");
                this.hpMax = rs.getInt("hp_max");
                this.hpActual = this.hpMax; // Empieza con la vida al máximo
                this.ataque = rs.getInt("ataque");

                // IMPORTANTE: Cerrar conexión y ResultSet tras sacar los datos
                rs.getStatement().getConnection().close();
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar monstruo en controlador: " + e.getMessage());
        }
        return false;
    }

    // Método para cuando el héroe le pega al monstruo
    public void recibirDano(int cantidad) {
        this.hpActual = Math.max(0, this.hpActual - cantidad);
    }

    // Comprueba si el monstruo ha muerto
    public boolean estaMuerto() {
        return this.hpActual <= 0;
    }

    // --- GETTERS ---
    // [NUEVO] Getter para que AccionUsuarioController pueda registrar los avistamientos
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
}
