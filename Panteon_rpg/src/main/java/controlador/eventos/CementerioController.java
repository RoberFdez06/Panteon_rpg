package controlador.eventos;

import vista.VistaCementerio;
import java.awt.Color;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class CementerioController {

    private VistaCementerio vista;
    private AccionUsuarioController ctrlPrincipal;
    private int paginaActual = 0;

    public CementerioController(VistaCementerio vista) {
        this.vista = vista;
        this.ctrlPrincipal = AccionUsuarioController.getInstancia();

        inicializarEstilosOpaques();
        inicializarEventos();

        // ¡IMPORTANTE! Forzamos la carga de la primera página al abrir
        cargarDatosPagina();
    }

    private void inicializarEstilosOpaques() {
        JScrollPane[] scrolls = {vista.getScrollPanel1(), vista.getScrollPanel2()};
        JTable[] tablas = {vista.getTabla1(), vista.getTabla2()};

        // 1. Nombres reales para las columnas en vez de "Title 1"
        String[] columnas = {"Héroe", "Clase", "Nivel", "Piso"};

        for (int i = 0; i < scrolls.length; i++) {
            // Desactivamos opacidad en el JScrollPane
            scrolls[i].setOpaque(false);
            scrolls[i].getViewport().setOpaque(false);
            scrolls[i].setBorder(null);

            // Quitamos opacidad de la tabla
            tablas[i].setOpaque(false);
            tablas[i].setBackground(new Color(0, 0, 0, 0));
            tablas[i].setShowGrid(false);

            // 2. CREAMOS EL MODELO LIMPIO Y SE LO APLICAMOS A LA TABLA
            DefaultTableModel modeloLimpio = new DefaultTableModel(null, columnas) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; // Evita que el usuario escriba en el libro
                }
            };
            tablas[i].setModel(modeloLimpio);

            // 3. ¡EL SECRETO! HACEMOS TRANSPARENTE EL ENCABEZADO (Los "Titles")
            tablas[i].getTableHeader().setOpaque(false);
            tablas[i].getTableHeader().setBackground(new Color(0, 0, 0, 0));
            tablas[i].getTableHeader().setForeground(new Color(62, 39, 35)); // Color sepia/café
            tablas[i].getTableHeader().setFont(new java.awt.Font("Cinzel", java.awt.Font.BOLD, 14));
            // Forzamos al renderizador del encabezado a no pintar el fondo blanco
            ((DefaultTableCellRenderer) tablas[i].getTableHeader().getDefaultRenderer()).setOpaque(false);

            // 4. Modificamos las celdas internas
            DefaultTableCellRenderer renderizadorTransparente = new DefaultTableCellRenderer();
            renderizadorTransparente.setOpaque(false);
            renderizadorTransparente.setForeground(new Color(62, 39, 35));
            renderizadorTransparente.setFont(new java.awt.Font("Cinzel", java.awt.Font.BOLD, 12));

            for (int col = 0; col < tablas[i].getColumnCount(); col++) {
                tablas[i].getColumnModel().getColumn(col).setCellRenderer(renderizadorTransparente);
            }
        }
    }

    private void inicializarEventos() {
        vista.getAnterior().addActionListener(e -> clickAnteriorPagina());
        vista.getSiguiente().addActionListener(e -> clickSiguientePagina());

        vista.getVolverInicio().addActionListener(e -> {
            ctrlPrincipal.clickVolverAlInicio();
            vista.dispose();
        });
    }

    private void clickAnteriorPagina() {
        if (paginaActual > 0) {
            paginaActual--;
            cargarDatosPagina();
        }
    }

    private void clickSiguientePagina() {
        paginaActual++;
        cargarDatosPagina();
    }

    private void cargarDatosPagina() {
        System.out.println("DEBUG: Consultando a la BBDD página " + paginaActual + " del cementerio.");

        DefaultTableModel modelo1 = (DefaultTableModel) vista.getTabla1().getModel();
        DefaultTableModel modelo2 = (DefaultTableModel) vista.getTabla2().getModel();

        // Vaciamos las tablas antes de escribir la nueva página
        modelo1.setRowCount(0);
        modelo2.setRowCount(0);

        // Límite de 8 registros por página doble (4 izquierda + 4 derecha)
        int limite = 8;
        int offset = paginaActual * limite;

        // Pedimos los datos a la base de datos
        java.util.List<Object[]> caidos = ctrlPrincipal.obtenerHeroesCaidosDB(limite, offset);

        // Repartimos los caídos en el libro
        for (int i = 0; i < caidos.size(); i++) {
            if (i < 4) {
                modelo1.addRow(caidos.get(i)); // Mitad izquierda
            } else {
                modelo2.addRow(caidos.get(i)); // Mitad derecha
            }
        }

        // Bloqueamos/Desbloqueamos los botones según si hay más páginas
        vista.getAnterior().setEnabled(paginaActual > 0);
        // Si nos devolvió exactamente 8, asumimos que puede haber una página siguiente
        vista.getSiguiente().setEnabled(caidos.size() == limite);
    }
}
