package modelo;

import java.sql.*;

public class ConsultasRPG extends ConexionBD {

    public int crearNuevaPartida(String nombrePartida) {
        Connection con = getConexion();
        String sql = "INSERT INTO partidas (nombre_partida, piso_actual) VALUES (?, 1)";
        int idGenerado = -1;
        try {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, nombrePartida);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                idGenerado = rs.getInt(1);
            }
            rs.close();
            ps.close();
            con.close();
        } catch (SQLException e) {
            System.err.println(e);
        }
        return idGenerado;
    }

    public void crearPersonaje(Personaje p) {
        Connection con = getConexion();
        // Cambiamos la consulta para que absorba los atributos buffados directamente de clases_personaje según la clase elegida
        String sql = "INSERT INTO personajes (partida_id, nombre, clase, hp_max, hp_actual, ataque, defensa, velocidad, suerte, estamina_max, estamina_actual) "
                + "SELECT ?, ?, clase, hp_max, hp_max, ataque, defensa, velocidad, suerte, estamina_max, estamina_max "
                + "FROM clases_personaje WHERE clase = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);

            // 1. Vinculamos el ID de la partida
            ps.setInt(1, p.getPartida_id());

            // 2. Vinculamos el nombre que introdujo el jugador
            ps.setString(2, p.getNombre());

            // 3. Vinculamos la clase seleccionada (Mago, Guerrero, etc.) para que filtre en el WHERE de SQL
            ps.setString(3, p.getClase());

            ps.executeUpdate();
            ps.close();
            con.close();
            System.out.println("¡Personaje de clase '" + p.getClase() + "' creado exitosamente con estadísticas de la BD!");
        } catch (SQLException e) {
            System.err.println("Error al crear personaje con estadísticas base: " + e);
        }
    }

    public void guardarPisoActual(int partidaId, int nuevoPiso) {
        Connection con = getConexion();
        String sql = "UPDATE partidas SET piso_actual = ? WHERE id = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, nuevoPiso);
            ps.setInt(2, partidaId);
            ps.executeUpdate();
            ps.close();
            con.close();
        } catch (SQLException e) {
            System.err.println(e);
        }
    }

    public void actualizarEstadoPersonaje(Personaje p) {
        Connection con = getConexion();
        String sql = "UPDATE personajes SET hp_actual = ?, estamina_actual = ?, nivel = ?, experiencia = ? WHERE id = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, p.getHp_actual());
            ps.setInt(2, p.getEstamina_actual());
            ps.setInt(3, p.getNivel());
            ps.setInt(4, p.getExperiencia());
            ps.setInt(5, p.getId());
            ps.executeUpdate();
            ps.close();
            con.close();
        } catch (SQLException e) {
            System.err.println(e);
        }
    }

    public Personaje obtenerPersonajePorPartida(int partidaId) {
        Connection con = getConexion();
        String sql = "SELECT * FROM personajes WHERE partida_id = ?";
        Personaje p = null;
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, partidaId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                p = new Personaje(rs.getInt("id"), rs.getInt("partida_id"), rs.getString("nombre"), rs.getString("clase"),
                        rs.getInt("nivel"), rs.getInt("experiencia"), rs.getInt("hp_max"), rs.getInt("hp_actual"),
                        rs.getInt("ataque"), rs.getInt("defensa"), rs.getInt("velocidad"), rs.getInt("suerte"),
                        rs.getInt("estamina_max"), rs.getInt("estamina_actual"));
            }
            rs.close();
            ps.close();
            con.close();
        } catch (SQLException e) {
            System.err.println(e);
        }
        return p;
    }

    public void borrarPartida(int partidaId) {
        Connection con = getConexion();
        String sql = "DELETE FROM partidas WHERE id = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, partidaId);
            ps.executeUpdate();
            ps.close();
            con.close();
        } catch (SQLException e) {
            System.err.println(e);
        }
    }

    public ResultSet obtenerMonstruoAleatorio() {
        String sql = "SELECT * FROM monstruos ORDER BY RAND() LIMIT 1";
        Connection con = getConexion();
        try {
            // Usamos TYPE_SCROLL_INSENSITIVE para poder maniobrar el ResultSet de forma segura al pasarlo
            PreparedStatement ps = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            return ps.executeQuery();
            // Nota: El objeto que reciba este ResultSet deberá encargarse de cerrar el ResultSet, su Statement y su Conexión.
        } catch (SQLException e) {
            System.err.println("Error al obtener monstruo aleatorio: " + e.getMessage());
            return null;
        }
    }

    public void registrarAvistamientoBestiario(int partidaId, int monstruoId) {
        Connection con = getConexion();
        // INSERT IGNORE evita errores si el personaje se vuelve a encontrar al mismo monstruo
        String sql = "INSERT IGNORE INTO bestiario_partida (partida_id, monstruo_id) VALUES (?, ?)";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, partidaId);
            ps.setInt(2, monstruoId);
            ps.executeUpdate();
            ps.close();
            con.close();
        } catch (SQLException e) {
            System.err.println("Error al registrar avistamiento en ConsultasRPG: " + e.getMessage());
        }
    }

    public java.util.Set<String> obtenerMonstruosEncontrados(int partidaId) {
        java.util.Set<String> encontrados = new java.util.HashSet<>();
        Connection con = getConexion();
        String sql = "SELECT LOWER(m.nombre) AS nombre_monstruo FROM bestiario_partida b "
                + "JOIN monstruos m ON b.monstruo_id = m.id WHERE b.partida_id = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, partidaId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                encontrados.add(rs.getString("nombre_monstruo").trim());
            }
            rs.close();
            ps.close();
            con.close();
        } catch (SQLException e) {
            System.err.println("Error al obtener avistamientos en ConsultasRPG: " + e.getMessage());
        }
        return encontrados;
    }

    public java.util.List<java.util.Map<String, Object>> obtenerTodasLasPartidas() {
        java.util.List<java.util.Map<String, Object>> lista = new java.util.ArrayList<>();
        Connection con = getConexion();

        // Hacemos un LEFT JOIN por si se creó una partida pero el personaje aún no se insertó
        String sql = "SELECT p.id AS partida_id, p.nombre_partida, p.piso_actual, "
                + "per.nombre AS heroe_nombre, per.clase, per.nivel "
                + "FROM partidas p "
                + "LEFT JOIN personajes per ON p.id = per.partida_id "
                + "ORDER BY p.id ASC LIMIT 3";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                java.util.Map<String, Object> partida = new java.util.HashMap<>();
                partida.put("partida_id", rs.getInt("partida_id"));
                partida.put("nombre_partida", rs.getString("nombre_partida"));
                partida.put("piso_actual", rs.getInt("piso_actual"));
                partida.put("heroe_nombre", rs.getString("heroe_nombre"));
                partida.put("clase", rs.getString("clase"));
                partida.put("nivel", rs.getInt("nivel"));
                lista.add(partida);
            }

            rs.close();
            ps.close();
            con.close();
        } catch (SQLException e) {
            System.err.println("Error al obtener el listado de partidas: " + e.getMessage());
        }
        return lista;
    }
}
