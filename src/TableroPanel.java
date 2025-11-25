import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
public class TableroPanel extends JPanel implements ActionListener, KeyListener {

    private static final int COLUMNAS = 10;
    private static final int FILAS = 20;
    private static final int TAM_BLOQUE = 30;

    private static final int ANCHO_PANEL = COLUMNAS * TAM_BLOQUE;
    private static final int ALTO_PANEL = FILAS * TAM_BLOQUE;

    private static final int RETRASO_INICIAL = 500;

    private final Color[][] tablero = new Color[FILAS][COLUMNAS];

    private Pieza piezaActual;
    private Color colorActual;
    private Point posicionActual;

    private final Timer temporizador;
    private boolean juegoTerminado = false;

    private final Random random = new Random();

    public TableroPanel() {
        setPreferredSize(new Dimension(ANCHO_PANEL, ALTO_PANEL));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        generarNuevaPieza();
        temporizador = new Timer(RETRASO_INICIAL, this);
        temporizador.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!juegoTerminado) {
            moverAbajo();
        }
    }

    private void generarNuevaPieza() {
        int indice = random.nextInt(PiezaFactory.FIGURAS.length);
        piezaActual = PiezaFactory.copiar(PiezaFactory.FIGURAS[indice]);
        colorActual = PiezaFactory.COLORES[indice];

        posicionActual = new Point(COLUMNAS / 2 - piezaActual.forma[0].length / 2, 0);

        if (!puedeMover(piezaActual.forma, posicionActual.x, posicionActual.y)) {
            juegoTerminado = true;
            temporizador.stop();
        }
    }

    private void moverAbajo() {
        if (puedeMover(piezaActual.forma, posicionActual.x, posicionActual.y + 1)) {
            posicionActual.y++;
        } else {
            fusionarPieza();
            limpiarFilas();
            generarNuevaPieza();
        }
        repaint();
    }

    private void moverHorizontal(int dx) {
        if (puedeMover(piezaActual.forma, posicionActual.x + dx, posicionActual.y)) {
            posicionActual.x += dx;
            repaint();
        }
    }

    private void caidaRapida() {
        while (puedeMover(piezaActual.forma, posicionActual.x, posicionActual.y + 1)) {
            posicionActual.y++;
        }
        fusionarPieza();
        limpiarFilas();
        generarNuevaPieza();
        repaint();
    }

    private void rotarDerecha() {
        int[][] rotada = PiezaFactory.rotarDerecha(piezaActual.forma);
        intentarRotacion(rotada);
    }

    private void rotarIzquierda() {
        int[][] rotada = PiezaFactory.rotarIzquierda(piezaActual.forma);
        intentarRotacion(rotada);
    }

    private void intentarRotacion(int[][] rotada) {
        if (puedeMover(rotada, posicionActual.x, posicionActual.y)) {
            piezaActual.forma = rotada;
        } else if (puedeMover(rotada, posicionActual.x - 1, posicionActual.y)) {
            posicionActual.x--;
            piezaActual.forma = rotada;
        } else if (puedeMover(rotada, posicionActual.x + 1, posicionActual.y)) {
            posicionActual.x++;
            piezaActual.forma = rotada;
        }
        repaint();
    }

    private void fusionarPieza() {
        for (int y = 0; y < piezaActual.forma.length; y++) {
            for (int x = 0; x < piezaActual.forma[y].length; x++) {
                if (piezaActual.forma[y][x] == 1) {
                    tablero[posicionActual.y + y][posicionActual.x + x] = colorActual;
                }
            }
        }
    }

    private void limpiarFilas() {
        for (int fila = FILAS - 1; fila >= 0; fila--) {
            boolean llena = true;
            for (int col = 0; col < COLUMNAS; col++) {
                if (tablero[fila][col] == null) {
                    llena = false;
                    break;
                }
            }
            if (llena) {
                eliminarFila(fila);
                fila++;
            }
        }
    }

    private void eliminarFila(int fila) {
        for (int y = fila; y > 0; y--) {
            System.arraycopy(tablero[y - 1], 0, tablero[y], 0, COLUMNAS);
        }
        for (int x = 0; x < COLUMNAS; x++) {
            tablero[0][x] = null;
        }
    }

    private boolean puedeMover(int[][] forma, int nuevoX, int nuevoY) {
        for (int y = 0; y < forma.length; y++) {
            for (int x = 0; x < forma[y].length; x++) {
                if (forma[y][x] == 1) {
                    int tableroX = nuevoX + x;
                    int tableroY = nuevoY + y;

                    if (tableroX < 0 || tableroX >= COLUMNAS || tableroY >= FILAS) {
                        return false;
                    }
                    if (tableroY >= 0 && tablero[tableroY][tableroX] != null) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void dibujarBloque(Graphics g, int x, int y, Color color) {
        int px = x * TAM_BLOQUE;
        int py = y * TAM_BLOQUE;

        g.setColor(color);
        g.fillRect(px, py, TAM_BLOQUE, TAM_BLOQUE);
        g.setColor(color.darker());
        g.drawRect(px, py, TAM_BLOQUE, TAM_BLOQUE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // tablero
        for (int y = 0; y < FILAS; y++) {
            for (int x = 0; x < COLUMNAS; x++) {
                if (tablero[y][x] != null) {
                    dibujarBloque(g, x, y, tablero[y][x]);
                }
            }
        }

        // pieza actual
        if (!juegoTerminado) {
            for (int y = 0; y < piezaActual.forma.length; y++) {
                for (int x = 0; x < piezaActual.forma[y].length; x++) {
                    if (piezaActual.forma[y][x] == 1) {
                        dibujarBloque(g, posicionActual.x + x, posicionActual.y + y, colorActual);
                    }
                }
            }
        } else {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 24));
            g.drawString("Juego terminado", ANCHO_PANEL / 2 - 100, ALTO_PANEL / 2);
            g.setFont(new Font("Arial", Font.PLAIN, 16));
            g.drawString("Presiona R para reiniciar", ANCHO_PANEL / 2 - 100, ALTO_PANEL / 2 + 30);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (juegoTerminado) {
            if (e.getKeyCode() == KeyEvent.VK_R) reiniciarJuego();
            return;
        }

        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT -> moverHorizontal(-1);
            case KeyEvent.VK_RIGHT -> moverHorizontal(1);
            case KeyEvent.VK_DOWN -> moverAbajo();
            case KeyEvent.VK_SPACE -> caidaRapida();
            case KeyEvent.VK_X -> rotarDerecha();
            case KeyEvent.VK_Z -> rotarIzquierda();
        }
    }

    private void reiniciarJuego() {
        for (int y = 0; y < FILAS; y++)
            for (int x = 0; x < COLUMNAS; x++)
                tablero[y][x] = null;

        juegoTerminado = false;
        generarNuevaPieza();
        temporizador.start();
        repaint();
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}
}
