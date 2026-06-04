package controlador.eventos;

import controlador.navegacion.NavegacionController;
import controlador.persistencia.DataController;
import controlador.logica.MonstruoController;
import modelo.Personaje;
import java.util.Set;
import java.util.HashSet;

/**
 * Controlador central que gestiona las acciones del usuario, el flujo de la
 * partida y la persistencia de datos del sistema.
 */
public class AccionUsuarioController {

    private static AccionUsuarioController instancia;
    private NavegacionController navCtrl;
    private DataController dataCtrl = new DataController();
    private MonstruoController monstruoActual;

    private Personaje heroeActual;
    private int pisoActual = 1;
    private String claseSeleccionada = "Guerrero";

    /**
     * Constructor privado para implementar el patrón Singleton.
     */
    private AccionUsuarioController() {
        this.navCtrl = NavegacionController.getInstancia();
    }

    /**
     * Obtiene la única instancia del controlador de acciones.
     *
     * @return Instancia única de AccionUsuarioController.
     */
    public static AccionUsuarioController getInstancia() {
        if (instancia == null) {
            instancia = new AccionUsuarioController();
        }
        return instancia;
    }

    /**
     * Obtiene el personaje actual en juego.
     *
     * @return El objeto Personaje activo.
     */
    public Personaje getHeroeActual() {
        return heroeActual;
    }

    /**
     * Define el personaje activo en la sesión.
     *
     * @param heroeActual Personaje a establecer.
     */
    public void setHeroeActual(Personaje heroeActual) {
        this.heroeActual = heroeActual;
    }

    /**
     * Obtiene el monstruo al que se enfrenta el jugador.
     *
     * @return Controlador del monstruo actual.
     */
    public MonstruoController getMonstruoActual() {
        return monstruoActual;
    }

    /**
     * Obtiene el número del piso actual.
     *
     * @return El entero representando el piso.
     */
    public int getPisoActual() {
        return pisoActual;
    }

    /**
     * Genera un nuevo monstruo adaptado al piso actual. Escala estadísticas
     * según sea un jefe, minijefe o enemigo común.
     */
    public void generarNuevoMonstruo() {
        this.monstruoActual = new MonstruoController();

        boolean esJefe = (this.pisoActual % 10 == 0);
        boolean esMiniJefe = (this.pisoActual % 5 == 0 && !esJefe);

        if (esJefe) {
            String[] jefes = {"Dragón Ancestral", "Hidra de Lerna"};
            String jefeElegido = jefes[(int) (Math.random() * jefes.length)];

            this.monstruoActual.cargarMonstruoPorNombre(jefeElegido);
            ajustarEstadisticasMonstruo(2.5, 1.4);
        } else if (esMiniJefe) {
            String[] miniJefes = {"Fénix de Fuego", "Cíclope", "Golem de Piedra"};
            String miniJefeElegido = miniJefes[(int) (Math.random() * miniJefes.length)];

            this.monstruoActual.cargarMonstruoPorNombre(miniJefeElegido);
            ajustarEstadisticasMonstruo(1.6, 1.2);
        } else {
            this.monstruoActual.cargarMonstruoAleatorio();
        }

        if (this.heroeActual != null && this.monstruoActual != null) {
            modelo.ConsultasRPG consultas = new modelo.ConsultasRPG();
            consultas.registrarAvistamientoBestiario(this.heroeActual.getPartida_id(), this.monstruoActual.getId());
        }
    }

    /**
     * Ajusta las estadísticas base del monstruo mediante multiplicadores.
     *
     * @param factorHp Multiplicador para la salud máxima.
     * @param factorAtq Multiplicador para el poder de ataque.
     */
    private void ajustarEstadisticasMonstruo(double factorHp, double factorAtq) {
        if (this.monstruoActual == null) {
            return;
        }

        int nuevoHpMax = (int) (this.monstruoActual.getHpMax() * factorHp);
        int nuevoAtaque = (int) (this.monstruoActual.getAtaque() * factorAtq);

        this.monstruoActual.setHpMax(nuevoHpMax);
        this.monstruoActual.setHpActual(nuevoHpMax);
        this.monstruoActual.setAtaque(nuevoAtaque);
    }

    /**
     * Gestiona la lógica al intentar crear una nueva partida.
     */
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

    /**
     * Dirige al usuario al menú de selección de partidas.
     */
    public void clickReanudarPartida() {
        navCtrl.irAMenuPartidas();
    }

    /**
     * Dirige al usuario al bestiario.
     */
    public void clickAbrirBestiario() {
        navCtrl.irABestiario();
    }

    /**
     * Finaliza la ejecución del juego.
     */
    public void clickSalirJuego() {
        System.exit(0);
    }

    /**
     * Establece la clase seleccionada para el personaje.
     *
     * @param clase String representando la clase.
     */
    public void seleccionarClase(String clase) {
        this.claseSeleccionada = clase;
    }

    /**
     * Inicia una nueva sesión de juego con un personaje dado.
     *
     * @param nombrePersonaje Nombre del nuevo héroe.
     */
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

    /**
     * Guarda el estado actual del personaje en la persistencia.
     */
    public void clickGuardarPartida() {
        if (heroeActual != null) {
            dataCtrl.guardarProgreso(heroeActual, pisoActual);
        }
    }

    /**
     * Elimina una partida específica de la base de datos.
     *
     * @param idPartida ID de la partida a eliminar.
     */
    public void clickBorrarPartida(int idPartida) {
        dataCtrl.borrarPartida(idPartida);
    }

    /**
     * Carga una partida guardada y redirige al combate.
     *
     * @param idPartida ID de la partida a retomar.
     */
    public void clickCargarPartida(int idPartida) {
        this.heroeActual = dataCtrl.cargarPartida(idPartida);
        if (this.heroeActual != null) {
            this.pisoActual = obtenerPisoPartidaDB(idPartida);
            generarNuevoMonstruo();
            navCtrl.irACombate();
        }
    }

    /**
     * Ejecuta la lógica de ataque entre el héroe y el monstruo.
     */
    public void clickAtacar() {
        if (monstruoActual != null && heroeActual != null) {
            int danoAlMonstruo = heroeActual.getAtaque();
            monstruoActual.recibirDano(danoAlMonstruo);

            if (monstruoActual.estaMuerto()) {
                int monstruosDerrotados = heroeActual.getExperiencia() + 1;
                heroeActual.setExperiencia(monstruosDerrotados);

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
     * Incrementa estadísticas y nivel del héroe tras cumplir objetivos.
     */
    public void subirNivelHeroe() {
        if (this.heroeActual == null) {
            return;
        }

        int nivelNuevo = this.heroeActual.getNivel() + 1;
        this.heroeActual.setNivel(nivelNuevo);
        this.heroeActual.setExperiencia(0);

        int nuevaHpMax = (int) (this.heroeActual.getHp_max() * 1.15);
        this.heroeActual.setHp_max(nuevaHpMax);
        this.heroeActual.setAtaque(this.heroeActual.getAtaque() + 10);
        this.heroeActual.setDefensa(this.heroeActual.getDefensa() + 5);
        this.heroeActual.setVelocidad(this.heroeActual.getVelocidad() + 4);

        int puntosACurar = (int) (nuevaHpMax * 0.30);
        int vidaCalculada = this.heroeActual.getHp_actual() + puntosACurar;

        this.heroeActual.setHp_actual(Math.min(nuevaHpMax, vidaCalculada));
        this.heroeActual.setEstamina_actual(this.heroeActual.getEstamina_max());

        dataCtrl.guardarProgreso(this.heroeActual, this.pisoActual);
    }

    /**
     * Acción para continuar tras una victoria.
     */
    public void clickContinuarVictoria() {
        navCtrl.irAContinuarRun();
    }

    /**
     * Acción para salir tras una derrota.
     */
    public void clickSalirDerrota() {
        navCtrl.irAMenuPrincipal();
    }

    /**
     * Acción para regresar al menú principal.
     */
    public void clickVolverAlInicio() {
        navCtrl.irAMenuPrincipal();
    }

    /**
     * Incrementa el piso actual y genera el siguiente encuentro.
     */
    public void clickAvanzarSiguientePiso() {
        this.pisoActual++;
        generarNuevoMonstruo();
        navCtrl.irACombate();
    }

    /**
     * Redirige a la vista del cementerio.
     */
    public void clickAbrirCementerio() {
        navCtrl.irACementerio();
    }

    /**
     * Consulta el nivel de piso actual almacenado para una partida en BD.
     *
     * @param idPartida ID de la partida a consultar.
     * @return El número de piso actual.
     */
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

    /**
     * Obtiene el conjunto de nombres de monstruos descubiertos por el héroe.
     *
     * @return Set de nombres de monstruos.
     */
    public Set<String> obtenerMonstruosDescubiertosDB() {
        if (this.heroeActual != null) {
            modelo.ConsultasRPG consultas = new modelo.ConsultasRPG();
            return consultas.obtenerMonstruosEncontrados(this.heroeActual.getPartida_id());
        }
        return new HashSet<>();
    }

    /**
     * Consulta los atributos base de un monstruo en la base de datos.
     *
     * @param nombreMonstruo Nombre del monstruo a consultar.
     * @return Mapa con los datos del monstruo.
     */
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

    /**
     * Cuenta cuántas partidas están guardadas actualmente en el sistema.
     *
     * @return Total de partidas.
     */
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

    /**
     * Obtiene una lista de héroes caídos para mostrar en el cementerio.
     *
     * @param limite Cantidad máxima de registros.
     * @param offset Desplazamiento de registros.
     * @return Lista de objetos conteniendo datos de los héroes caídos.
     */
    public java.util.List<Object[]> obtenerHeroesCaidosDB(int limite, int offset) {
        java.util.List<Object[]> listaCaidos = new java.util.ArrayList<>();
        try {
            modelo.ConexionBD conexionBD = new modelo.ConexionBD() {
            };
            java.sql.Connection con = conexionBD.getConexion();

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
