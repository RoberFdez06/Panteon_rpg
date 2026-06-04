package controlador.eventos;

import vista.VistaCombates;
import controlador.logica.MonstruoController;
import modelo.Personaje;

/**
 * Controlador encargado de gestionar la lógica y eventos de la pantalla de
 * combate. Coordina las interacciones entre el héroe y el monstruo, así como la
 * actualización de la interfaz gráfica.
 */
public class CombatesController {

    private VistaCombates vista;
    private AccionUsuarioController ctrlPrincipal;

    /**
     * Inicializa el controlador de combates, actualiza la vista inicial y
     * configura los eventos.
     *
     * @param vista La vista de combate asociada.
     */
    public CombatesController(VistaCombates vista) {
        this.vista = vista;
        this.ctrlPrincipal = AccionUsuarioController.getInstancia();

        actualizarPantallaCombate("¡Un enemigo corta tu avance!");
        inicializarEventos();
    }

    /**
     * Configura los listeners para los componentes interactivos de la vista.
     */
    private void inicializarEventos() {
        vista.getBtnAtaque().addActionListener(e -> {
            vista.getBtnAtaque().setEnabled(false);

            MonstruoController m = ctrlPrincipal.getMonstruoActual();
            Personaje heroe = ctrlPrincipal.getHeroeActual();

            if (m == null || heroe == null) {
                vista.getBtnAtaque().setEnabled(true);
                return;
            }

            int vidaAntesMonstruo = m.getHpActual();
            m.recibirDano(heroe.getAtaque());
            int danoRealizado = vidaAntesMonstruo - m.getHpActual();
            actualizarPantallaCombate("¡Atacas al enemigo y le infliges " + danoRealizado + " puntos de daño!");

            javax.swing.Timer timerEnemigo = new javax.swing.Timer(1000, evt -> {
                if (m.estaMuerto()) {
                    manejarFinDelCombate(true);
                } else {
                    int danoAlHeroe = m.getAtaque();
                    heroe.setHp_actual(Math.max(0, heroe.getHp_actual() - danoAlHeroe));
                    actualizarPantallaCombate("¡El enemigo contraataca y te inflige " + danoAlHeroe + " puntos de daño!");

                    javax.swing.Timer timerFinal = new javax.swing.Timer(1000, evtFinal -> {
                        if (heroe.getHp_actual() <= 0) {
                            manejarFinDelCombate(false);
                        } else {
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

        if (vista.getTxtPiso() != null) {
            vista.getTxtPiso().addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    javax.swing.JOptionPane.showMessageDialog(vista,
                            "Te encuentras descendiendo por el piso actual.",
                            "Información del Panteón",
                            javax.swing.JOptionPane.INFORMATION_MESSAGE);
                }

                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    vista.getTxtPiso().setForeground(java.awt.Color.YELLOW);
                    vista.getTxtPiso().setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
                    vista.getTxtPiso().setForeground(java.awt.Color.WHITE);
                    vista.getTxtPiso().setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
                }
            });
        }
    }

    /**
     * Maneja el desenlace del combate tras una espera controlada.
     *
     * @param esVictoria Indica si el jugador ganó el encuentro.
     */
    private void manejarFinDelCombate(boolean esVictoria) {
        javax.swing.Timer timerTransicion = new javax.swing.Timer(1000, e -> {
            ctrlPrincipal.clickAtacar();
        });
        timerTransicion.setRepeats(false);
        timerTransicion.start();
    }

    /**
     * Sincroniza el estado lógico de los personajes con la interfaz gráfica.
     *
     * @param mensajeLog Mensaje que se mostrará en el log de combate.
     */
    private void actualizarPantallaCombate(String mensajeLog) {
        Personaje heroe = ctrlPrincipal.getHeroeActual();
        MonstruoController monstruo = ctrlPrincipal.getMonstruoActual();

        int numeroPiso = ctrlPrincipal.getPisoActual();
        vista.setPiso("Piso: " + numeroPiso);

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
                    rutaImagen = carpeta + "SombraEspectralSinFondoV1Escalado.png";
                    break;
            }
        }

        vista.refrescarDatosInterfaz(txtVidaHeroe, txtVidaMonstruo, mensajeLog, rutaImagen);
    }
}
