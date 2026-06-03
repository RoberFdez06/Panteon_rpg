package controlador.eventos;

import vista.VistaCombates;
import controlador.logica.MonstruoController;
import modelo.Personaje;

public class CombatesController {

    private VistaCombates vista;
    private AccionUsuarioController ctrlPrincipal;

    public CombatesController(VistaCombates vista) {
        this.vista = vista;
        this.ctrlPrincipal = AccionUsuarioController.getInstancia();

        actualizarPantallaCombate("¡Un enemigo corta tu avance!");
        inicializarEventos();
    }

    private void inicializarEventos() {
        vista.getBtnAtaque().addActionListener(e -> {
            // Deshabilitamos el botón para evitar spam y clics dobles mientras dura el turno
            vista.getBtnAtaque().setEnabled(false);

            MonstruoController m = ctrlPrincipal.getMonstruoActual();
            Personaje heroe = ctrlPrincipal.getHeroeActual();

            if (m == null || heroe == null) {
                vista.getBtnAtaque().setEnabled(true);
                return;
            }

            // ==========================================
            // PASO 1: TU ACCIÓN (ATAQUE DEL HÉROE)
            // ==========================================
            int vidaAntesMonstruo = m.getHpActual();
            int danoAlMonstruo = heroe.getAtaque();
            m.recibirDano(danoAlMonstruo);

            int danoRealizado = vidaAntesMonstruo - m.getHpActual();
            String logHeroe = "¡Atacas al enemigo y le infliges " + danoRealizado + " puntos de daño!";

            // Actualizamos la pantalla al instante para ver tu golpe
            actualizarPantallaCombate(logHeroe);

            // ==========================================
            // PASO 2: TURNO ENEMIGO (1 SEGUNDO DESPUÉS)
            // ==========================================
            javax.swing.Timer timerEnemigo = new javax.swing.Timer(1000, evt -> {

                if (m.estaMuerto()) {
                    // Si con tu golpe muere, esperamos 1 segundo antes de cambiar de ventana
                    manejarFinDelCombate(true);
                } else {
                    // Si sigue vivo, contraataca
                    int danoAlHeroe = m.getAtaque();
                    int nuevaVidaHeroe = heroe.getHp_actual() - danoAlHeroe;
                    heroe.setHp_actual(Math.max(0, nuevaVidaHeroe));

                    String logMonstruo = "¡El enemigo contraataca y te inflige " + danoAlHeroe + " puntos de daño!";
                    actualizarPantallaCombate(logMonstruo);

                    // ==========================================
                    // PASO 3: REVISAR DERROTA (1 SEGUNDO DESPUÉS)
                    // ==========================================
                    javax.swing.Timer timerFinal = new javax.swing.Timer(1000, evtFinal -> {
                        if (heroe.getHp_actual() <= 0) {
                            manejarFinDelCombate(false);
                        } else {
                            // Si ambos siguen vivos, reactivamos el botón para el siguiente turno
                            vista.getBtnAtaque().setEnabled(true);
                        }
                    });
                    timerFinal.setRepeats(false);
                    timerFinal.start();
                }
            });
            timerEnemigo.setRepeats(false);
            timerEnemigo.start();
        });
    }

    /**
     * Espera 1 segundo mostrando el estado final en pantalla antes de redirigir
     * a la vista de Victoria o Derrota delegando en AccionUsuarioController.
     */
    private void manejarFinDelCombate(boolean esVictoria) {
        javax.swing.Timer timerTransicion = new javax.swing.Timer(1000, e -> {
            // clickAtacar se encarga de evaluar la vida del monstruo/héroe y cambiar la pantalla
            ctrlPrincipal.clickAtacar();
        });
        timerTransicion.setRepeats(false);
        timerTransicion.start();
    }

    private void actualizarPantallaCombate(String mensajeLog) {
        Personaje heroe = ctrlPrincipal.getHeroeActual();
        MonstruoController monstruo = ctrlPrincipal.getMonstruoActual();

        String txtVidaHeroe = "Vida Héroe: --/--";
        if (heroe != null) {
            txtVidaHeroe = heroe.getNombre() + " (HP: " + heroe.getHp_actual() + "/" + heroe.getHp_max() + ")";
        }

        String txtVidaMonstruo = "Vida Monstruo: --/--";
        String rutaImagen = "";

        if (monstruo != null) {
            txtVidaMonstruo = monstruo.getNombre() + " (HP: " + monstruo.getHpActual() + "/" + monstruo.getHpMax() + ")";

            String nombreRaw = monstruo.getNombre();
            String parseado = java.text.Normalizer.normalize(nombreRaw, java.text.Normalizer.Form.NFD)
                    .replaceAll("[\\p{InCombiningDiacriticalMarks}]", "")
                    .toLowerCase().trim();

            String carpeta = "/imagenes/Monstruos/";

            switch (parseado) {
                case "basilisco menor":
                    rutaImagen = carpeta + "BasiliscoMenorSinFondoV1Escalado.png";
                    break;
                case "ciclope":
                    rutaImagen = carpeta + "CíclopeSinFondoV1Escalado.png";
                    break;
                case "fenix de fuego":
                    rutaImagen = carpeta + "FenixdeFuegoSinFondoV1Escalado.png";
                    break;
                case "golem de piedra":
                    rutaImagen = carpeta + "GolemPiedraSinFondoV1Escalado.png";
                    break;
                case "harpia":
                    rutaImagen = carpeta + "HarpiaSinFondoV1Escalado.png";
                    break;
                case "hidra de lerna":
                    rutaImagen = carpeta + "HidradeLernaSinFondoV1Escalado.png";
                    break;
                case "minotauro":
                    rutaImagen = carpeta + "MinotauroSinFondoV1Escalado.png";
                    break;
                case "quimera":
                    rutaImagen = carpeta + "QuimeraSinFondoV1Escalado.png";
                    break;
                case "sombra espectral":
                    rutaImagen = carpeta + "SombraEspectralSinFondoV1Escalado.png";
                    break;
                case "dragon ancestral":
                    rutaImagen = carpeta + "DragonAncestralSinFondoV1Escalado.png";
                    break;
                default:
                    System.out.println("DEBUG: Imagen no encontrada para: " + parseado);
                    rutaImagen = carpeta + "SombraEspectralSinFondoV1Escalado.png";
                    break;
            }
        }

        vista.refrescarDatosInterfaz(txtVidaHeroe, txtVidaMonstruo, mensajeLog, rutaImagen);
    }
}
