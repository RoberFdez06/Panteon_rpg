package modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    private final String bd = "panteon_rpg";
    private final String user = "root";
    private final String password = "";
    private final String url = "jdbc:mysql://localhost/" + bd;
    private Connection con = null;

    public Connection getConexion() {
        try {
            con = DriverManager.getConnection(this.url, this.user, this.password);
        } catch (SQLException e) {
            System.err.println(e);
        }
        return con;
    }
}
