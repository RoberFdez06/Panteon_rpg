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

public class MenuInicialController {

    private VistaMenuInicial vista;
    private AccionUsuarioController ctrlPrincipal;
    // Ruta relativa apuntando a la carpeta de tu proyecto
    private final String rutaLore = "src/main/java/txt/LorePanteon_rpg.txt";

    public MenuInicialController(VistaMenuInicial vista) {
        this.vista = vista;
        this.ctrlPrincipal = AccionUsuarioController.getInstancia();
        inicializarEventos();
    }

    private void inicializarEventos() {
        vista.getBtnNuevaPartida().addActionListener(e -> ctrlPrincipal.clickNuevaPartida());
        vista.getBtnReanudarPartida().addActionListener(e -> ctrlPrincipal.clickReanudarPartida());
        vista.getBtnBestiario().addActionListener(e -> ctrlPrincipal.clickAbrirBestiario());
        vista.getBtnCementerio().addActionListener(e -> ctrlPrincipal.clickAbrirCementerio());
        vista.getBtnSalir().addActionListener(e -> ctrlPrincipal.clickSalirJuego());

        // [NUEVO] Listener para el botón de Lore con función lambda
        vista.getBtnVerLore().addActionListener(e -> mostrarLoreDesdeArchivo());
    }

    /**
     * [NUEVO] Lee el archivo .txt usando BufferedReader e InputStreamReader en
     * UTF-8 para que los emojis y acentos salgan perfectos en el cuadro de
     * diálogo.
     */
    private void mostrarLoreDesdeArchivo() {
        StringBuilder contenidoLore = new StringBuilder();

        // Try-with-resources: asegura que el buffer se cierre pase lo que pase
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(rutaLore), StandardCharsets.UTF_8))) {

            String linea;
            while ((linea = br.readLine()) != null) {
                contenidoLore.append(linea).append("\n");
            }

            // --- DISEÑO DE LA VENTANA DE LORE ---
            JTextArea areaTexto = new JTextArea(contenidoLore.toString());
            areaTexto.setFont(new Font("Georgia", Font.PLAIN, 14));
            areaTexto.setEditable(false);
            areaTexto.setLineWrap(true);       // Salto de línea automático al llegar al borde
            areaTexto.setWrapStyleWord(true);   // Corta por palabras completas en lugar de romper sílabas

            // Un panel con scroll por si el texto es largo
            JScrollPane scrollPane = new JScrollPane(areaTexto);
            scrollPane.setPreferredSize(new Dimension(550, 400));

            // Desplegamos la pop-up
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
