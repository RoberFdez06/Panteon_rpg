package controlador.eventos;

import java.util.Set;
import java.util.Map;
import vista.VistaBestiario;
import java.text.Normalizer;

public class BestiarioController {

    private VistaBestiario vista;
    private AccionUsuarioController ctrlPrincipal;

    public BestiarioController(VistaBestiario vista) {
        this.vista = vista;
        this.ctrlPrincipal = AccionUsuarioController.getInstancia();
        Set<String> monstruosDescubiertos = this.ctrlPrincipal.obtenerMonstruosDescubiertosDB();
        procesarYActualizarIndice(monstruosDescubiertos);
        inicializarEventos();
    }

    private String normalizar(String texto) {
        if (texto == null) {
            return "";
        }
        String nfdNormalizedString = Normalizer.normalize(texto, Normalizer.Form.NFD);
        return nfdNormalizedString.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "").toLowerCase().trim();
    }

    private void inicializarEventos() {
        vista.getBasiliscoMenor().addActionListener(e -> procesarClickMonstruo("Basilisco Menor"));
        vista.getCiclope().addActionListener(e -> procesarClickMonstruo("Cíclope"));
        vista.getFenixDeFuego().addActionListener(e -> procesarClickMonstruo("Fénix de Fuego"));
        vista.getGolemDePiedra().addActionListener(e -> procesarClickMonstruo("Golem de Piedra"));
        vista.getHarpia().addActionListener(e -> procesarClickMonstruo("Harpía"));
        vista.getHidraDeLerna().addActionListener(e -> procesarClickMonstruo("Hidra de Lerna"));
        vista.getMinotauro().addActionListener(e -> procesarClickMonstruo("Minotauro"));
        vista.getQuimera().addActionListener(e -> procesarClickMonstruo("Quimera"));
        vista.getSombraEspectral().addActionListener(e -> procesarClickMonstruo("Sombra Espectral"));
        vista.getDragonAncestral().addActionListener(e -> procesarClickMonstruo("Dragón Ancestral"));

        vista.getVolverInicio().addActionListener(e -> {
            ctrlPrincipal.clickVolverAlInicio();
            vista.dispose();
        });
    }

    public void procesarYActualizarIndice(Set<String> monstruosDescubiertos) {
        configurarBoton(vista.getBasiliscoMenor(), "Basilisco Menor", "basilisco menor", monstruosDescubiertos);
        configurarBoton(vista.getCiclope(), "Cíclope", "ciclope", monstruosDescubiertos);
        configurarBoton(vista.getFenixDeFuego(), "Fénix de Fuego", "fenix de fuego", monstruosDescubiertos);
        configurarBoton(vista.getGolemDePiedra(), "Golem de Piedra", "golem de piedra", monstruosDescubiertos);
        configurarBoton(vista.getHarpia(), "Harpía", "harpia", monstruosDescubiertos);
        configurarBoton(vista.getHidraDeLerna(), "Hidra de Lerna", "hidra de lerna", monstruosDescubiertos);
        configurarBoton(vista.getMinotauro(), "Minotauro", "minotauro", monstruosDescubiertos);
        configurarBoton(vista.getQuimera(), "Quimera", "quimera", monstruosDescubiertos);
        configurarBoton(vista.getSombraEspectral(), "Sombra Espectral", "sombra espectral", monstruosDescubiertos);
        configurarBoton(vista.getDragonAncestral(), "Dragón Ancestral", "dragon ancestral", monstruosDescubiertos);
    }

    private void configurarBoton(javax.swing.JButton boton, String nombreReal, String claveBusqueda, Set<String> descubiertos) {
        boolean encontrado = false;
        String claveNorm = normalizar(claveBusqueda);

        for (String s : descubiertos) {
            if (normalizar(s).contains(claveNorm)) {
                encontrado = true;
                break;
            }
        }

        boton.setText(encontrado ? nombreReal : "??? (No Encontrado)");
        boton.setEnabled(encontrado);
    }

    private void procesarClickMonstruo(String nombreMonstruo) {
        Map<String, Object> datos = ctrlPrincipal.consultarDatosMonstruo(nombreMonstruo);

        if (datos.isEmpty()) {
            System.err.println("DEBUG: No se encontraron datos en BD para: " + nombreMonstruo);
            return;
        }

        String nombreReal = (String) datos.get("nombreReal");
        int hp = (int) datos.get("hp");
        int ataque = (int) datos.get("ataque");
        int defensa = (int) datos.get("defensa");
        int velocidad = (int) datos.get("velocidad");

        String nombreArchivo;
        String nombreNorm = normalizar(nombreReal);

        switch (nombreNorm) {
            case "basilisco menor":
                nombreArchivo = "BasiliscoMenorSinFondoV1Escalado.png";
                break;
            case "ciclope":
                nombreArchivo = "CíclopeSinFondoV1Escalado.png";
                break;
            case "dragon ancestral":
                nombreArchivo = "DragonAncestralSinFondoV1Escalado.png";
                break;
            case "fenix de fuego":
                nombreArchivo = "FenixdeFuegoSinFondoV1Escalado.png";
                break;
            case "golem de piedra":
                nombreArchivo = "GolemPiedraSinFondoV1Escalado.png";
                break;
            case "harpia":
                nombreArchivo = "HarpiaSinFondoV1Escalado.png";
                break;
            case "hidra de lerna":
                nombreArchivo = "HidradeLernaSinFondoV1Escalado.png";
                break;
            case "minotauro":
                nombreArchivo = "MinotauroSinFondoV1Escalado.png";
                break;
            case "quimera":
                nombreArchivo = "QuimeraSinFondoV1Escalado.png";
                break;
            case "sombra espectral":
                nombreArchivo = "SombraEspectralSinFondoV1Escalado.png";
                break;
            default:
                nombreArchivo = "SombraEspectralSinFondoV1Escalado.png";
                break;
        }

        String descripcionHtml = "<html><body>Esta criatura ha sido avistada en tu travesía.</body></html>";
        vista.mostrarDatosMonstruo(nombreReal, "Información de Avistamiento", descripcionHtml,
                "HP: " + hp, "ATQ: " + ataque, "DEF: " + defensa,
                "VEL: " + velocidad, "/imagenes/Monstruos/" + nombreArchivo);
    }
}
