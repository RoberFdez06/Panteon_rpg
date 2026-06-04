package controlador.eventos;

import java.awt.Dimension;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import vista.VistaMenuInicial;

/**
 * Controlador encargado de gestionar la interfaz del menú principal. Maneja la
 * navegación hacia las distintas secciones del juego y la visualización de la
 * historia (Lore).
 */
public class MenuInicialController {

    private VistaMenuInicial vista;
    private AccionUsuarioController ctrlPrincipal;

    // Ruta relativa apuntando a la carpeta de recursos del proyecto
    private final String rutaLore = "src/main/java/txt/LorePanteon_rpg.txt";

    /**
     * Inicializa el controlador con la vista asociada y establece la conexión
     * con el controlador principal.
     *
     * @param vista Instancia de la vista del menú inicial.
     */
    public MenuInicialController(VistaMenuInicial vista) {
        this.vista = vista;
        this.ctrlPrincipal = AccionUsuarioController.getInstancia();
        inicializarEventos();
    }

    /**
     * Configura los listeners para todos los botones del menú inicial.
     */
    private void inicializarEventos() {
        vista.getBtnNuevaPartida().addActionListener(e -> ctrlPrincipal.clickNuevaPartida());
        vista.getBtnReanudarPartida().addActionListener(e -> ctrlPrincipal.clickReanudarPartida());
        vista.getBtnBestiario().addActionListener(e -> ctrlPrincipal.clickAbrirBestiario());
        vista.getBtnCementerio().addActionListener(e -> ctrlPrincipal.clickAbrirCementerio());
        vista.getBtnSalir().addActionListener(e -> ctrlPrincipal.clickSalirJuego());

        // Listener para el botón de Lore
        vista.getBtnVerLore().addActionListener(e -> mostrarLoreDesdeArchivo());
    }

    /**
     * Lee el archivo de texto que contiene el lore del juego y lo muestra en
     * una ventana emergente. Utiliza UTF-8 para asegurar la correcta
     * visualización de caracteres especiales.
     */
    private void mostrarLoreDesdeArchivo() {
        StringBuilder contenidoLore = new StringBuilder();

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(rutaLore), StandardCharsets.UTF_8))) {

            String linea;
            while ((linea = br.readLine()) != null) {
                contenidoLore.append(linea).append("\n");
            }

            // Configuración del área de texto para el despliegue del Lore
            JTextArea areaTexto = new JTextArea(contenidoLore.toString());
            areaTexto.setFont(new Font("Georgia", Font.PLAIN, 14));
            areaTexto.setEditable(false);
            areaTexto.setLineWrap(true);
            areaTexto.setWrapStyleWord(true);

            // Panel con scroll para manejar textos largos
            JScrollPane scrollPane = new JScrollPane(areaTexto);
            scrollPane.setPreferredSize(new Dimension(550, 400));

            // Desplegar diálogo informativo
            JOptionPane.showMessageDialog(vista, scrollPane, "📖 Crónicas del Panteón RPG", JOptionPane.PLAIN_MESSAGE);

        } catch (Exception ex) {
            System.err.println("Error al leer el archivo de Lore: " + ex.getMessage());
            JOptionPane.showMessageDialog(vista,
                    "No se ha podido abrir el libro de Lore.\nVerifica que el archivo exista en la ruta:\n" + rutaLore,
                    "⚠️ Error de Archivo",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
