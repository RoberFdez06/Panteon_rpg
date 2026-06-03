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

    public void generarNuevoMonstruo() {
        this.monstruoActual = new MonstruoController();
        this.monstruoActual.cargarMonstruoAleatorio();

        if (this.heroeActual != null && this.monstruoActual != null) {
            modelo.ConsultasRPG consultas = new modelo.ConsultasRPG();
            consultas.registrarAvistamientoBestiario(this.heroeActual.getPartida_id(), this.monstruoActual.getId());
            System.out.println("¡Monstruo '" + monstruoActual.getNombre() + "' registrado en el Bestiario!");
        }
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

    public void clickAbrirCementerio() {
        System.out.println("Abriendo cementerio...");
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
        // CORRECCIÓN NOMBRE: Guardamos solo el nombre limpio del personaje en la run
        Personaje heroeTemporal = new Personaje(0, 0, nombrePersonaje, claseSeleccionada, 1, 0, 100, 100, 10, 10, 10, 5, 50, 50);
        int idPartida = dataCtrl.iniciarNuevaRun(nombrePersonaje, heroeTemporal);

        if (idPartida != -1) {
            this.pisoActual = 1; // CORRECCIÓN PISO: Forzamos a que empiece en el piso 1 obligatoriamente
            this.heroeActual = dataCtrl.cargarPartida(idPartida);
            generarNuevoMonstruo();
            navCtrl.irACombate();
        }
    }

    // --- MÉTODOS DE PARTIDAS ---
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

    /**
     * CORRECCIÓN CRÍTICA: Ahora recupera el piso guardado en la base de datos
     * para que no se quede siempre en 1 al reanudar.
     */
    public void clickCargarPartida(int idPartida) {
        this.heroeActual = dataCtrl.cargarPartida(idPartida);
        if (this.heroeActual != null) {
            // Sincronizamos el piso actual del controlador con el valor real de la BD
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
                // ================================================================
                // ¡MONSTRUO DERROTADO! Llevamos la cuenta usando la experiencia (0 a 3)
                // ================================================================
                int monstruosDerrotados = heroeActual.getExperiencia() + 1;
                heroeActual.setExperiencia(monstruosDerrotados);

                System.out.println("Monstruos derrotados para el siguiente nivel: " + monstruosDerrotados + "/3");

                if (monstruosDerrotados >= 3) {
                    // Si llega a 3 bajas, sube estadísticas, se cura un 30% y se reinicia el contador a 0
                    subirNivelHeroe();
                } else {
                    // Si no llega a 3, simplemente guardamos el avance del contador en la BBDD
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
     * Incrementa el nivel del héroe actual tras acumular 3 bajas, escala
     * progresivamente todas sus estadísticas de combate, cura un 30% de la
     * nueva vida máxima, restaura la estamina, y guarda el estado directamente
     * en la persistencia del sistema.
     */
    public void subirNivelHeroe() {
        if (this.heroeActual == null) {
            return;
        }

        // 1. Incrementar el nivel
        int nivelNuevo = this.heroeActual.getNivel() + 1;
        this.heroeActual.setNivel(nivelNuevo);

        // 2. REINICIAR EL CONTADOR: Volvemos la experiencia a 0 para el próximo ciclo
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

        // Nos aseguramos de no pasarnos de la nueva vida máxima por si acaso
        this.heroeActual.setHp_actual(Math.min(nuevaHpMax, vidaCalculada));

        // La estamina sí la restauramos completa para que pueda seguir peleando bien
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

    // =========================================================================
    //  [MVC PURO]: MÉTODOS DE CONSULTA DIRECTA A BASE DE DATOS
    // =========================================================================
    private int obtenerPisoPartidaDB(int idPartida) {
        int piso = 1;
        try {
            modelo.ConexionBD conexionBD = new modelo.ConexionBD() {
            };
            java.sql.Connection con = conexionBD.getConexion();
            String sql = "SELECT piso_actual FROM partidas WHERE id = ?"; // CORRECCIÓN CAMPO: de 'partida_id' a 'id' según la DBDD

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
}
