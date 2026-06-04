package controlador.eventos;

import vista.VistaCementerio;
import java.awt.Color;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 * Controlador que gestiona la lógica de presentación y navegación de los héroes
 * caídos en el libro del cementerio, aplicando estilos personalizados para una
 * estética de época.
 */
public class CementerioController {

    private VistaCementerio vista;
    private AccionUsuarioController ctrlPrincipal;
    private int paginaActual = 0;

    /**
     * Inicializa el controlador del cementerio.
     *
     * @param vista La vista correspondiente al libro del cementerio.
     */
    public CementerioController(VistaCementerio vista) {
        this.vista = vista;
        this.ctrlPrincipal = AccionUsuarioController.getInstancia();

        inicializarEstilosOpaques();
        inicializarEventos();

        cargarDatosPagina();
    }

    /**
     * Configura los estilos visuales de las tablas y scrolls para que se
     * integren transparentemente en la interfaz del cementerio.
     */
    private void inicializarEstilosOpaques() {
        JScrollPane[] scrolls = {vista.getScrollPanel1(), vista.getScrollPanel2()};
        JTable[] tablas = {vista.getTabla1(), vista.getTabla2()};

        String[] columnas = {"Héroe", "Clase", "Nivel", "Piso"};

        for (int i = 0; i < scrolls.length; i++) {
            scrolls[i].setOpaque(false);
            scrolls[i].getViewport().setOpaque(false);
            scrolls[i].setBorder(null);

            tablas[i].setOpaque(false);
            tablas[i].setBackground(new Color(0, 0, 0, 0));
            tablas[i].setShowGrid(false);

            DefaultTableModel modeloLimpio = new DefaultTableModel(null, columnas) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            tablas[i].setModel(modeloLimpio);

            tablas[i].getTableHeader().setOpaque(false);
            tablas[i].getTableHeader().setBackground(new Color(0, 0, 0, 0));
            tablas[i].getTableHeader().setForeground(new Color(62, 39, 35));
            tablas[i].getTableHeader().setFont(new java.awt.Font("Cinzel", java.awt.Font.BOLD, 14));
            ((DefaultTableCellRenderer) tablas[i].getTableHeader().getDefaultRenderer()).setOpaque(false);

            DefaultTableCellRenderer renderizadorTransparente = new DefaultTableCellRenderer();
            renderizadorTransparente.setOpaque(false);
            renderizadorTransparente.setForeground(new Color(62, 39, 35));
            renderizadorTransparente.setFont(new java.awt.Font("Cinzel", java.awt.Font.BOLD, 12));

            for (int col = 0; col < tablas[i].getColumnCount(); col++) {
                tablas[i].getColumnModel().getColumn(col).setCellRenderer(renderizadorTransparente);
            }
        }
    }

    /**
     * Registra los eventos de navegación para los botones del libro.
     */
    private void inicializarEventos() {
        vista.getAnterior().addActionListener(e -> clickAnteriorPagina());
        vista.getSiguiente().addActionListener(e -> clickSiguientePagina());

        vista.getVolverInicio().addActionListener(e -> {
            ctrlPrincipal.clickVolverAlInicio();
            vista.dispose();
        });
    }

    /**
     * Retrocede a la página anterior del cementerio si es posible.
     */
    private void clickAnteriorPagina() {
        if (paginaActual > 0) {
            paginaActual--;
            cargarDatosPagina();
        }
    }

    /**
     * Avanza a la siguiente página del cementerio.
     */
    private void clickSiguientePagina() {
        paginaActual++;
        cargarDatosPagina();
    }

    /**
     * Solicita a la capa de persistencia los datos correspondientes a la página
     * actual y actualiza las tablas del libro.
     */
    private void cargarDatosPagina() {
        DefaultTableModel modelo1 = (DefaultTableModel) vista.getTabla1().getModel();
        DefaultTableModel modelo2 = (DefaultTableModel) vista.getTabla2().getModel();

        modelo1.setRowCount(0);
        modelo2.setRowCount(0);

        int limite = 8;
        int offset = paginaActual * limite;

        java.util.List<Object[]> caidos = ctrlPrincipal.obtenerHeroesCaidosDB(limite, offset);

        for (int i = 0; i < caidos.size(); i++) {
            if (i < 4) {
                modelo1.addRow(caidos.get(i));
            } else {
                modelo2.addRow(caidos.get(i));
            }
        }

        vista.getAnterior().setEnabled(paginaActual > 0);
        vista.getSiguiente().setEnabled(caidos.size() == limite);
    }
}
