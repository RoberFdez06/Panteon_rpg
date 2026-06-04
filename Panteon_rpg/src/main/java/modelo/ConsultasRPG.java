package modelo;

import java.sql.*;
import java.util.*;

/**
 * Clase de acceso a datos (DAO) encargada de ejecutar las consultas SQL
 * relacionadas con la gestión de partidas, personajes, monstruos y el
 * bestiario.
 */
public class ConsultasRPG extends ConexionBD {

    /**
     * Crea una nueva partida en la base de datos.
     *
     * @param nombrePartida Nombre asignado por el jugador a la nueva partida.
     * @return El ID de la partida generada, o -1 si hubo un error.
     */
    public int crearNuevaPartida(String nombrePartida) {
        String sql = "INSERT INTO partidas (nombre_partida, piso_actual) VALUES (?, 1)";
        int idGenerado = -1;
        Connection con = getConexion();
        if (con == null) {
            return -1;
        }

        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, nombrePartida);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    idGenerado = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al crear partida: " + e.getMessage());
        } finally {
            cerrarConexion(con);
        }
        return idGenerado;
    }

    /**
     * Registra un nuevo personaje asociado a una partida específica.
     *
     * @param p Objeto Personaje con los datos iniciales cargados.
     */
    public void crearPersonaje(Personaje p) {
        String sql = "INSERT INTO personajes (partida_id, nombre, clase, hp_max, hp_actual, ataque, defensa, velocidad, suerte, estamina_max, estamina_actual) "
                + "SELECT ?, ?, clase, hp_max, hp_max, ataque, defensa, velocidad, suerte, estamina_max, estamina_max "
                + "FROM clases_personaje WHERE clase = ?";
        Connection con = getConexion();
        if (con == null) {
            return;
        }

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, p.getPartida_id());
            ps.setString(2, p.getNombre());
            ps.setString(3, p.getClase());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al crear personaje: " + e.getMessage());
        } finally {
            cerrarConexion(con);
        }
    }

    /**
     * Actualiza el progreso de piso actual de una partida.
     *
     * @param partidaId El identificador de la partida.
     * @param nuevoPiso El número de piso donde se encuentra el jugador.
     */
    public void guardarPisoActual(int partidaId, int nuevoPiso) {
        String sql = "UPDATE partidas SET piso_actual = ? WHERE id = ?";
        Connection con = getConexion();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, nuevoPiso);
            ps.setInt(2, partidaId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al guardar piso: " + e.getMessage());
        } finally {
            cerrarConexion(con);
        }
    }

    /**
     * Sincroniza el estado vital y de nivel del personaje en la base de datos.
     *
     * @param p Objeto Personaje con las estadísticas actuales.
     */
    public void actualizarEstadoPersonaje(Personaje p) {
        String sql = "UPDATE personajes SET hp_max = ?, hp_actual = ?, ataque = ?, defensa = ?, "
                + "velocidad = ?, suerte = ?, estamina_max = ?, estamina_actual = ?, "
                + "nivel = ?, experiencia = ? WHERE id = ?";
        Connection con = getConexion();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, p.getHp_max());
            ps.setInt(2, p.getHp_actual());
            ps.setInt(3, p.getAtaque());
            ps.setInt(4, p.getDefensa());
            ps.setInt(5, p.getVelocidad());
            ps.setInt(6, p.getSuerte());
            ps.setInt(7, p.getEstamina_max());
            ps.setInt(8, p.getEstamina_actual());
            ps.setInt(9, p.getNivel());
            ps.setInt(10, p.getExperiencia());
            ps.setInt(11, p.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al actualizar personaje: " + e.getMessage());
        } finally {
            cerrarConexion(con);
        }
    }

    /**
     * Recupera el personaje asociado a un ID de partida.
     *
     * @param partidaId ID de la partida.
     * @return El objeto Personaje encontrado, o null si no existe.
     */
    public Personaje obtenerPersonajePorPartida(int partidaId) {
        String sql = "SELECT * FROM personajes WHERE partida_id = ?";
        Connection con = getConexion();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, partidaId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Personaje(rs.getInt("id"), rs.getInt("partida_id"), rs.getString("nombre"), rs.getString("clase"),
                            rs.getInt("nivel"), rs.getInt("experiencia"), rs.getInt("hp_max"), rs.getInt("hp_actual"),
                            rs.getInt("ataque"), rs.getInt("defensa"), rs.getInt("velocidad"), rs.getInt("suerte"),
                            rs.getInt("estamina_max"), rs.getInt("estamina_actual"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener personaje: " + e.getMessage());
        } finally {
            cerrarConexion(con);
        }
        return null;
    }

    /**
     * Elimina una partida y toda su información vinculada.
     *
     * @param partidaId ID de la partida a eliminar.
     */
    public void borrarPartida(int partidaId) {
        String sql = "DELETE FROM partidas WHERE id = ?";
        Connection con = getConexion();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, partidaId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al borrar partida: " + e.getMessage());
        } finally {
            cerrarConexion(con);
        }
    }

    /**
     * Obtiene un monstruo aleatorio de la base de datos. NOTA: El llamador es
     * responsable de cerrar el ResultSet y su conexión.
     *
     * @return Un ResultSet con los datos del monstruo, o null si ocurre un
     * error.
     */
    public ResultSet obtenerMonstruoAleatorio() {
        String sql = "SELECT * FROM monstruos ORDER BY RAND() LIMIT 1";
        try {
            Connection con = getConexion();
            PreparedStatement ps = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            return ps.executeQuery();
        } catch (SQLException e) {
            System.err.println("Error al obtener monstruo: " + e.getMessage());
            return null;
        }
    }

    /**
     * Lista todas las partidas existentes para la selección del jugador.
     *
     * @return Una lista de mapas, cada uno representando una partida y sus
     * datos de personaje.
     */
    public List<Map<String, Object>> obtenerTodasLasPartidas() {
        List<Map<String, Object>> lista = new ArrayList<>();
        String sql = "SELECT p.id AS partida_id, p.nombre_partida, p.piso_actual, per.nombre AS heroe_nombre, per.clase, per.nivel "
                + "FROM partidas p LEFT JOIN personajes per ON p.id = per.partida_id ORDER BY p.id ASC LIMIT 3";
        Connection con = getConexion();
        try (PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> p = new HashMap<>();
                p.put("partida_id", rs.getInt("partida_id"));
                p.put("nombre_partida", rs.getString("nombre_partida"));
                p.put("piso_actual", rs.getInt("piso_actual"));
                p.put("heroe_nombre", rs.getString("heroe_nombre"));
                p.put("clase", rs.getString("clase"));
                p.put("nivel", rs.getInt("nivel"));
                lista.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener partidas: " + e.getMessage());
        } finally {
            cerrarConexion(con);
        }
        return lista;
    }

    /**
     * Obtiene los nombres de los monstruos descubiertos por el jugador en una
     * partida.
     *
     * @param partidaId ID de la partida.
     * @return Un set con los nombres de los monstruos encontrados.
     */
    public Set<String> obtenerMonstruosEncontrados(int partidaId) {
        Set<String> encontrados = new HashSet<>();
        String sql = "SELECT LOWER(m.nombre) AS nombre_monstruo FROM bestiario_partida b "
                + "JOIN monstruos m ON b.monstruo_id = m.id WHERE b.partida_id = ?";

        Connection con = getConexion();
        if (con == null) {
            return encontrados;
        }

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, partidaId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    encontrados.add(rs.getString("nombre_monstruo").trim());
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener avistamientos: " + e.getMessage());
        } finally {
            cerrarConexion(con);
        }
        return encontrados;
    }

    /**
     * Cierra de forma segura una conexión abierta a la base de datos.
     *
     * @param con La conexión a cerrar.
     */
    private void cerrarConexion(Connection con) {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Registra un nuevo avistamiento de monstruo en el bestiario de una partida
     * específica.
     *
     * @param partidaId El identificador de la partida.
     * @param monstruoId El identificador del monstruo avistado.
     */
    public void registrarAvistamientoBestiario(int partidaId, int monstruoId) {
        // INSERT IGNORE evita errores si el personaje se vuelve a encontrar al mismo monstruo
        String sql = "INSERT IGNORE INTO bestiario_partida (partida_id, monstruo_id) VALUES (?, ?)";
        Connection con = getConexion();
        if (con == null) {
            return;
        }

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, partidaId);
            ps.setInt(2, monstruoId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al registrar avistamiento en ConsultasRPG: " + e.getMessage());
        } finally {
            cerrarConexion(con);
        }
    }

    /**
     * Comprueba si ya existe una partida registrada con el mismo nombre para
     * evitar duplicados en la base de datos.
     *
     * @param nombrePartida El nombre que se desea verificar.
     * @return true si el nombre ya está registrado, false si está libre.
     */
    public boolean existeNombrePartida(String nombrePartida) {
        String sql = "SELECT COUNT(*) FROM partidas WHERE nombre_partida = ?";
        Connection con = getConexion();
        if (con == null) {
            return false;
        }

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombrePartida);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar la existencia del nombre de la partida: " + e.getMessage());
        } finally {
            cerrarConexion(con);
        }
        return false;
    }
}
