package controlador.eventos;

import controlador.navegacion.NavegacionController;
import controlador.persistencia.DataController;
import controlador.logica.MonstruoController;
import modelo.Personaje;
import java.util.Set;
import java.util.HashSet;

public class AccionUsuarioController {

    private static AccionUsuarioController instancia;
    private NavegacionController navCtrl;
    private DataController dataCtrl = new DataController();
    private MonstruoController monstruoActual;

    // Estado actual de la partida
    private Personaje heroeActual;
    private int pisoActual = 1; // Se actualizará dinámicamente
    private String claseSeleccionada = "Guerrero";

    private AccionUsuarioController() {
        this.navCtrl = NavegacionController.getInstancia();
    }

    public static AccionUsuarioController getInstancia() {
        if (instancia == null) {
            instancia = new AccionUsuarioController();
        }
        return instancia;
    }

    // --- GETTERS Y SETTERS ---
    public Personaje getHeroeActual() {
        return heroeActual;
    }

    public void setHeroeActual(Personaje heroeActual) {
        this.heroeActual = heroeActual;
    }

    public MonstruoController getMonstruoActual() {
        return monstruoActual;
    }

    public int getPisoActual() {
        return pisoActual;
    }

    /**
     * Genera un nuevo monstruo adaptado al piso actual para permitir runs
     * infinitas. - Pisos múltiplos de 10: JEFES (Dragón Ancestral, Hidra de
     * Lerna) -> x2.5 HP, x1.4 ATQ - Pisos múltiplos de 5 (pero no de 10):
     * MINIJEFES (Fénix de Fuego, Cíclope, Golem de Piedra) -> x1.6 HP, x1.2 ATQ
     * - Resto de pisos: Monstruos comunes cargados de forma aleatoria.
     */
    public void generarNuevoMonstruo() {
        this.monstruoActual = new MonstruoController();

        // Determinar el tipo de encuentro según el piso
        boolean esJefe = (this.pisoActual % 10 == 0);
        boolean esMiniJefe = (this.pisoActual % 5 == 0 && !esJefe);

        if (esJefe) {
            // Pool de JEFES SUPREMOS
            String[] jefes = {"Dragón Ancestral", "Hidra de Lerna"};
            String jefeElegido = jefes[(int) (Math.random() * jefes.length)];

            this.monstruoActual.cargarMonstruoPorNombre(jefeElegido);
            ajustarEstadisticasMonstruo(2.5, 1.4); // Multiplicadores de Jefe

            System.out.println("¡¡ALERTA DE ALTO PELIGRO!! ¡UN JEFE HA DESPERTADO EN EL PISO " + this.pisoActual + ": " + monstruoActual.getNombre() + "!!");
        } else if (esMiniJefe) {
            // Pool de MINIJEFES
            String[] miniJefes = {"Fénix de Fuego", "Cíclope", "Golem de Piedra"};
            String miniJefeElegido = miniJefes[(int) (Math.random() * miniJefes.length)];

            this.monstruoActual.cargarMonstruoPorNombre(miniJefeElegido);
            ajustarEstadisticasMonstruo(1.6, 1.2); // Multiplicadores de MiniJefe

            System.out.println("¡CUIDADO! ¡Un Mini-Jefe bloquea el paso en el piso " + this.pisoActual + ": " + monstruoActual.getNombre() + "!");
        } else {
            // Enemy común aleatorio
            this.monstruoActual.cargarMonstruoAleatorio();
        }

        // Registrar avistamiento en el Bestiario
        if (this.heroeActual != null && this.monstruoActual != null) {
            modelo.ConsultasRPG consultas = new modelo.ConsultasRPG();
            consultas.registrarAvistamientoBestiario(this.heroeActual.getPartida_id(), this.monstruoActual.getId());
            System.out.println("¡Monstruo '" + monstruoActual.getNombre() + "' registrado en el Bestiario!");
        }
    }

    /**
     * Método auxiliar para escalar los atributos base de los jefes y minijefes
     * dinámicamente en base al piso, curándolos por completo tras la mutación.
     */
    private void ajustarEstadisticasMonstruo(double factorHp, double factorAtq) {
        if (this.monstruoActual == null) {
            return;
        }

        int nuevoHpMax = (int) (this.monstruoActual.getHpMax() * factorHp);
        int nuevoAtaque = (int) (this.monstruoActual.getAtaque() * factorAtq);

        this.monstruoActual.setHpMax(nuevoHpMax);
        this.monstruoActual.setHpActual(nuevoHpMax); // Rellenamos su vida al nuevo límite
        this.monstruoActual.setAtaque(nuevoAtaque);
    }

    // --- MÉTODOS DE NAVEGACIÓN ---
    public void clickNuevaPartida() {
        if (contarPartidasGuardadasDB() >= 3) {
            javax.swing.JOptionPane.showMessageDialog(
                    null,
                    "¡Límite alcanzado! Ya tienes 3 partidas guardadas.\nDebes borrar alguna desde el menú de partidas para empezar una nueva.",
                    "Espacio Insuficiente",
                    javax.swing.JOptionPane.WARNING_MESSAGE
            );
            navCtrl.irAMenuPartidas();
        } else {
            navCtrl.irACreacionPersonaje();
        }
    }

    public void clickReanudarPartida() {
        navCtrl.irAMenuPartidas();
    }

    public void clickAbrirBestiario() {
        navCtrl.irABestiario();
    }

    public void clickSalirJuego() {
        System.exit(0);
    }

    // --- MÉTODOS DE CREACIÓN ---
    public void seleccionarClase(String clase) {
        this.claseSeleccionada = clase;
        System.out.println("Clase seleccionada: " + clase);
    }

    public void clickIniciarRun(String nombrePersonaje) {
        Personaje heroeTemporal = new Personaje(0, 0, nombrePersonaje, claseSeleccionada, 1, 0, 100, 100, 10, 10, 10, 5, 50, 50);
        int idPartida = dataCtrl.iniciarNuevaRun(nombrePersonaje, heroeTemporal);

        if (idPartida != -1) {
            this.pisoActual = 1;
            this.heroeActual = dataCtrl.cargarPartida(idPartida);
            generarNuevoMonstruo();
            navCtrl.irACombate();
        }
    }

    // --- MÉTICODS DE PARTIDAS ---
    public void clickGuardarPartida() {
        if (heroeActual != null) {
            dataCtrl.guardarProgreso(heroeActual, pisoActual);
            System.out.println("Partida guardada en el piso " + pisoActual);
        }
    }

    public void clickBorrarPartida(int idPartida) {
        dataCtrl.borrarPartida(idPartida);
        System.out.println("Partida " + idPartida + " borrada.");
    }

    public void clickCargarPartida(int idPartida) {
        this.heroeActual = dataCtrl.cargarPartida(idPartida);
        if (this.heroeActual != null) {
            this.pisoActual = obtenerPisoPartidaDB(idPartida);
            generarNuevoMonstruo();
            navCtrl.irACombate();
        }
    }

    // --- MÉTODOS DE COMBATE ---
    public void clickAtacar() {
        if (monstruoActual != null && heroeActual != null) {
            int danoAlMonstruo = heroeActual.getAtaque();
            monstruoActual.recibirDano(danoAlMonstruo);

            if (monstruoActual.estaMuerto()) {
                int monstruosDerrotados = heroeActual.getExperiencia() + 1;
                heroeActual.setExperiencia(monstruosDerrotados);

                System.out.println("Monstruos derrotados para el siguiente nivel: " + monstruosDerrotados + "/3");

                if (monstruosDerrotados >= 3) {
                    subirNivelHeroe();
                } else {
                    dataCtrl.guardarProgreso(this.heroeActual, this.pisoActual);
                }

                navCtrl.irAVictoria();
            } else {
                int danoAlHeroe = monstruoActual.getAtaque();
                int nuevaVidaHeroe = heroeActual.getHp_actual() - danoAlHeroe;
                heroeActual.setHp_actual(Math.max(0, nuevaVidaHeroe));

                if (heroeActual.getHp_actual() <= 0) {
                    navCtrl.irADerrota();
                }
            }
        }
    }

    /**
     * Sube el nivel numérico, escala las estadísticas un 15% en salud y añade
     * puntos planos, sana un 30% de la nueva vida máxima total calculada y
     * limpia el contador de bajas.
     */
    public void subirNivelHeroe() {
        if (this.heroeActual == null) {
            return;
        }

        // 1. Incrementar el nivel
        int nivelNuevo = this.heroeActual.getNivel() + 1;
        this.heroeActual.setNivel(nivelNuevo);

        // 2. Reiniciar contador de experiencia interna (bajas)
        this.heroeActual.setExperiencia(0);

        // 3. Escalar y actualizar las estadísticas base del Personaje
        int nuevaHpMax = (int) (this.heroeActual.getHp_max() * 1.15); // +15% HP Max
        this.heroeActual.setHp_max(nuevaHpMax);
        this.heroeActual.setAtaque(this.heroeActual.getAtaque() + 10);      // +10 ATQ
        this.heroeActual.setDefensa(this.heroeActual.getDefensa() + 5);     // +5 DEF
        this.heroeActual.setVelocidad(this.heroeActual.getVelocidad() + 4); // +4 VEL

        // 4. CURACIÓN PARCIAL (30% de la nueva vida máxima)
        int puntosACurar = (int) (nuevaHpMax * 0.30);
        int vidaCalculada = this.heroeActual.getHp_actual() + puntosACurar;

        // Tope de seguridad
        this.heroeActual.setHp_actual(Math.min(nuevaHpMax, vidaCalculada));

        // Restaurar estamina por completo
        this.heroeActual.setEstamina_actual(this.heroeActual.getEstamina_max());

        System.out.println("¡SUBIDA DE NIVEL LOGRADA! " + this.heroeActual.getNombre() + " avanzó al Nivel " + nivelNuevo);
        System.out.println("Curación recibida por Level Up: +" + puntosACurar + " HP");
        System.out.println("Nuevas Stats -> HP Actual: " + this.heroeActual.getHp_actual() + "/" + nuevaHpMax + " | ATQ: " + this.heroeActual.getAtaque());

        // 5. Forzar el guardado automático del progreso escalado en la Base de Datos
        dataCtrl.guardarProgreso(this.heroeActual, this.pisoActual);
    }

    // --- MÉTODOS DE RESULTADOS ---
    public void clickContinuarVictoria() {
        navCtrl.irAContinuarRun();
    }

    public void clickSalirDerrota() {
        navCtrl.irAMenuPrincipal();
    }

    public void clickVolverAlInicio() {
        navCtrl.irAMenuPrincipal();
    }

    public void clickAvanzarSiguientePiso() {
        this.pisoActual++;
        generarNuevoMonstruo();
        navCtrl.irACombate();
    }

    public void clickAbrirCementerio() {
        System.out.println("Abriendo el Libro de los Caídos...");
        navCtrl.irACementerio();
    }

    // =========================================================================
    //  [MVC PURO]: MÉTODOS DE CONSULTA DIRECTA A BASE DE DATOS
    // =========================================================================
    private int obtenerPisoPartidaDB(int idPartida) {
        int piso = 1;
        try {
            modelo.ConexionBD conexionBD = new modelo.ConexionBD() {
            };
            java.sql.Connection con = conexionBD.getConexion();
            String sql = "SELECT piso_actual FROM partidas WHERE id = ?";

            java.sql.PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idPartida);
            java.sql.ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                piso = rs.getInt("piso_actual");
            }
            rs.close();
            ps.close();
            con.close();
        } catch (Exception e) {
            System.err.println("Error al recuperar el piso de la BD: " + e.getMessage());
        }
        return piso;
    }

    public Set<String> obtenerMonstruosDescubiertosDB() {
        if (this.heroeActual != null) {
            modelo.ConsultasRPG consultas = new modelo.ConsultasRPG();
            return consultas.obtenerMonstruosEncontrados(this.heroeActual.getPartida_id());
        }
        return new HashSet<>();
    }

    public java.util.Map<String, Object> consultarDatosMonstruo(String nombreMonstruo) {
        java.util.Map<String, Object> datos = new java.util.HashMap<>();
        try {
            modelo.ConexionBD conexionBD = new modelo.ConexionBD() {
            };
            java.sql.Connection con = conexionBD.getConexion();
            String sql = "SELECT * FROM monstruos WHERE LOWER(nombre) = LOWER(?)";

            java.sql.PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, nombreMonstruo);
            java.sql.ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                datos.put("nombreReal", rs.getString("nombre"));
                datos.put("hp", rs.getInt("hp_max"));
                datos.put("ataque", rs.getInt("ataque"));
                datos.put("defensa", rs.getInt("defensa"));
                datos.put("velocidad", rs.getInt("velocidad"));
            }
            rs.close();
            ps.close();
            con.close();
        } catch (Exception e) {
            System.err.println("Error en consulta BD de monstruo: " + e.getMessage());
        }
        return datos;
    }

    private int contarPartidasGuardadasDB() {
        int totalPartidas = 0;
        try {
            modelo.ConexionBD conexionBD = new modelo.ConexionBD() {
            };
            java.sql.Connection con = conexionBD.getConexion();
            String sql = "SELECT COUNT(*) AS total FROM partidas";

            java.sql.PreparedStatement ps = con.prepareStatement(sql);
            java.sql.ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                totalPartidas = rs.getInt("total");
            }
            rs.close();
            ps.close();
            con.close();
        } catch (Exception e) {
            System.err.println("Error al contar las partidas de la BD: " + e.getMessage());
        }
        return totalPartidas;
    }

    public java.util.List<Object[]> obtenerHeroesCaidosDB(int limite, int offset) {
        java.util.List<Object[]> listaCaidos = new java.util.ArrayList<>();
        try {
            modelo.ConexionBD conexionBD = new modelo.ConexionBD() {
            };
            java.sql.Connection con = conexionBD.getConexion();

            // Buscamos a los héroes muertos (hp_actual <= 0), ordenados del más reciente al más antiguo
            String sql = "SELECT nombre, clase, nivel, piso_actual FROM partidas WHERE hp_actual <= 0 ORDER BY id DESC LIMIT ? OFFSET ?";

            java.sql.PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, limite);
            ps.setInt(2, offset);
            java.sql.ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                listaCaidos.add(new Object[]{
                    rs.getString("nombre"),
                    rs.getString("clase"),
                    rs.getInt("nivel"),
                    rs.getInt("piso_actual")
                });
            }
            rs.close();
            ps.close();
            con.close();
        } catch (Exception e) {
            System.err.println("Error al recuperar el cementerio de la BD: " + e.getMessage());
        }
        return listaCaidos;
    }
}
