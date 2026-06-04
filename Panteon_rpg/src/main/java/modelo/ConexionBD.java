package modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase encargada de gestionar la conexión con la base de datos MySQL local.
 */
public class ConexionBD {

    private final String bd = "panteon_rpg";
    private final String user = "root";
    private final String password = "";
    private final String url = "jdbc:mysql://localhost/" + bd;

    /**
     * Establece y devuelve una conexión a la base de datos.
     *
     * @return Objeto Connection activo, o null si la conexión falla.
     */
    public Connection getConexion() {
        Connection con = null;
        try {
            // Se recomienda cargar el driver si es necesario, 
            // aunque en versiones modernas de JDBC no suele ser obligatorio.
            con = DriverManager.getConnection(this.url, this.user, this.password);
        } catch (SQLException e) {
            System.err.println("Error al establecer la conexión a la base de datos: " + e.getMessage());
        }
        return con;
    }
}
