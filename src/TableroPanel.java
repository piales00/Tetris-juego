import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
public class TableroPanel extends JPanel implements ActionListener, KeyListener {

    private static final int COLUMNAS = 15;
    private static final int FILAS = 20;
    private static final int TAM_BLOQUE = 30;
    private static final int ANCHO_SCORE = 200;

    private static final int ANCHO_PANEL = COLUMNAS * TAM_BLOQUE + ANCHO_SCORE;
    private static final int ALTO_PANEL = FILAS * TAM_BLOQUE;

    private static final int RETRASO_INICIAL = 500;

    private final Color[][] tablero = new Color[FILAS][COLUMNAS];

    private Pieza piezaActual;
    private Color colorActual;
    private Point posicionActual;
    
    private Pieza piezaSiguiente;
    private Color colorSiguiente;

    private final Timer temporizador;
    private boolean juegoTerminado = false;
    private int score = 0;
    private int lineasEliminadas = 0;

    private final Random random = new Random();

    public TableroPanel() {
        setPreferredSize(new Dimension(ANCHO_PANEL, ALTO_PANEL));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        // Generar primera pieza siguiente
        generarPiezaSiguiente();
        // Generar pieza actual
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

    private void generarPiezaSiguiente() {
        int indice = random.nextInt(PiezaFactory.FIGURAS.length);
        piezaSiguiente = PiezaFactory.copiar(PiezaFactory.FIGURAS[indice]);
        colorSiguiente = PiezaFactory.COLORES[indice];
    }
    
    private void generarNuevaPieza() {
        // si ya hay una pieza siguiente, usarla como actual
        if (piezaSiguiente != null) {
            piezaActual = piezaSiguiente;
            colorActual = colorSiguiente;
        } else {
            // primera vez: generar pieza actual
            int indice = random.nextInt(PiezaFactory.FIGURAS.length);
            piezaActual = PiezaFactory.copiar(PiezaFactory.FIGURAS[indice]);
            colorActual = PiezaFactory.COLORES[indice];
        }
        
        // nueva pieza siguiente
        generarPiezaSiguiente();

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
        int lineasEliminadasEnTurno = 0;
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
                lineasEliminadasEnTurno++;
            }
        }
        
        // calcular puntuación
        if (lineasEliminadasEnTurno > 0) {
            lineasEliminadas += lineasEliminadasEnTurno;
            // sistema de puntuación
            int puntosPorLinea = 100;
            switch (lineasEliminadasEnTurno) {
                case 1 -> score += puntosPorLinea;
                case 2 -> score += puntosPorLinea * 3;
                case 3 -> score += puntosPorLinea * 5;
                case 4 -> score += puntosPorLinea * 8; // Tetris!
                default -> score += puntosPorLinea * lineasEliminadasEnTurno * 2;
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
    
    private Point calcularPiezaFantasma(int[][] forma, int x, int y) {
        int yFantasma = y;
        // bajar fila por fila hasta detectar colisión
        while (puedeMover(forma, x, yFantasma + 1)) {
            yFantasma++;
        }
        return new Point(x, yFantasma);
    }

    private void dibujarBloque(Graphics g, int x, int y, Color color) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int px = x * TAM_BLOQUE;
        int py = y * TAM_BLOQUE;
        
        // Sombra 3D 
        g2d.setColor(new Color(0, 0, 0, 80));
        g2d.fillRect(px + 3, py + 3, TAM_BLOQUE, TAM_BLOQUE);
        
        // efecto gradiente para efecto 3D
        GradientPaint gradient = new GradientPaint(
            px, py, color.brighter(),
            px + TAM_BLOQUE, py + TAM_BLOQUE, color.darker()
        );
        g2d.setPaint(gradient);
        g2d.fillRect(px, py, TAM_BLOQUE - 2, TAM_BLOQUE - 2);
        
        // Borde superior e izquierdo 
        g2d.setColor(color.brighter().brighter());
        g2d.setStroke(new BasicStroke(2.0f));
        g2d.drawLine(px, py, px + TAM_BLOQUE - 2, py);
        g2d.drawLine(px, py, px, py + TAM_BLOQUE - 2);
        
        // Borde inferior y derecho 
        g2d.setColor(color.darker().darker());
        g2d.drawLine(px + TAM_BLOQUE - 2, py, px + TAM_BLOQUE - 2, py + TAM_BLOQUE - 2);
        g2d.drawLine(px, py + TAM_BLOQUE - 2, px + TAM_BLOQUE - 2, py + TAM_BLOQUE - 2);
    }
    
    private void dibujarBloqueConGlow(Graphics g, int x, int y, Color color) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        
        int px = x * TAM_BLOQUE;
        int py = y * TAM_BLOQUE;
        
        // Efecto glow 
        for (int i = 3; i >= 1; i--) {
            float alpha = 0.15f / i;
            Color glowColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)(alpha * 255));
            g2d.setColor(glowColor);
            g2d.setStroke(new BasicStroke(i * 2.0f));
            g2d.drawRect(px - i, py - i, TAM_BLOQUE + i * 2, TAM_BLOQUE + i * 2);
        }
        
        // dibujar el bloque normal con sombra 3D
        dibujarBloque(g, x, y, color);
    }
    
    private void dibujarBloqueFantasma(Graphics g, int x, int y, Color color) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int px = x * TAM_BLOQUE;
        int py = y * TAM_BLOQUE;
        
        // color semi-transparente
        Color colorFantasma = new Color(color.getRed(), color.getGreen(), color.getBlue(), 80);
        
        // Relleno semi-transparente
        g2d.setColor(colorFantasma);
        g2d.fillRect(px + 1, py + 1, TAM_BLOQUE - 2, TAM_BLOQUE - 2);
        
        // Borde punteado
        float[] dashPattern = {5.0f, 5.0f};
        BasicStroke dashedStroke = new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10.0f, dashPattern, 0.0f);
        g2d.setStroke(dashedStroke);
        g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 150));
        g2d.drawRect(px + 1, py + 1, TAM_BLOQUE - 2, TAM_BLOQUE - 2);
    }
    
    private void dibujarCuadricula(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        Color colorCuadricula = new Color(0x1a1a1a);
        g2d.setColor(colorCuadricula);
        g2d.setStroke(new BasicStroke(1.0f));
        
        // Líneas verticales
        for (int x = 0; x <= COLUMNAS; x++) {
            int px = x * TAM_BLOQUE;
            g2d.drawLine(px, 0, px, FILAS * TAM_BLOQUE);
        }
        
        // Líneas horizontales
        for (int y = 0; y <= FILAS; y++) {
            int py = y * TAM_BLOQUE;
            g2d.drawLine(0, py, COLUMNAS * TAM_BLOQUE, py);
        }
    }

    //borde neón
    
    private void dibujarBordeNeon(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        
        Color colorNeon = new Color(0x00f0ff);
        int grosorBorde = 3;
        int anchoTablero = COLUMNAS * TAM_BLOQUE;
        int altoTablero = FILAS * TAM_BLOQUE;
        
        // Efecto glow del borde 
        for (int i = 2; i >= 0; i--) {
            float alpha = 0.3f / (i + 1);
            Color glowColor = new Color(
                colorNeon.getRed(), 
                colorNeon.getGreen(), 
                colorNeon.getBlue(), 
                (int)(alpha * 255)
            );
            g2d.setColor(glowColor);
            g2d.setStroke(new BasicStroke(grosorBorde + i * 2));
            g2d.drawRect(-i - grosorBorde/2, -i - grosorBorde/2, 
                        anchoTablero + (i + grosorBorde/2) * 2, 
                        altoTablero + (i + grosorBorde/2) * 2);
        }
        
        // Borde principal
        g2d.setColor(colorNeon);
        g2d.setStroke(new BasicStroke(grosorBorde));
        g2d.drawRect(0, 0, anchoTablero, altoTablero);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        // dibujar cuadrícula sutil en el fondo
        dibujarCuadricula(g);
        
        // dibujar borde neón alrededor del área de juego
        dibujarBordeNeon(g);

        // tablero (bloques fijos)
        for (int y = 0; y < FILAS; y++) {
            for (int x = 0; x < COLUMNAS; x++) {
                if (tablero[y][x] != null) {
                    dibujarBloque(g, x, y, tablero[y][x]);
                }
            }
        }

        // pieza fantasma (solo si el juego no ha terminado)
        if (!juegoTerminado) {
            Point posicionFantasma = calcularPiezaFantasma(piezaActual.forma, posicionActual.x, posicionActual.y);
            // Solo dibujar si la posición fantasma es diferente a la actual
            if (posicionFantasma.y != posicionActual.y) {
                for (int y = 0; y < piezaActual.forma.length; y++) {
                    for (int x = 0; x < piezaActual.forma[y].length; x++) {
                        if (piezaActual.forma[y][x] == 1) {
                            dibujarBloqueFantasma(g, posicionFantasma.x + x, posicionFantasma.y + y, colorActual);
                        }
                    }
                }
            }
        }

        // pieza actual con efecto glow
        if (!juegoTerminado) {
            for (int y = 0; y < piezaActual.forma.length; y++) {
                for (int x = 0; x < piezaActual.forma[y].length; x++) {
                    if (piezaActual.forma[y][x] == 1) {
                        dibujarBloqueConGlow(g, posicionActual.x + x, posicionActual.y + y, colorActual);
                    }
                }
            }
        } else {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 24));
            g.drawString("Juego terminado", COLUMNAS * TAM_BLOQUE / 2 - 100, ALTO_PANEL / 2);
            g.setFont(new Font("Arial", Font.PLAIN, 16));
            g.drawString("Presiona R para reiniciar", COLUMNAS * TAM_BLOQUE / 2 - 100, ALTO_PANEL / 2 + 30);
        }
        
        // Panel de información (score)
        int panelX = COLUMNAS * TAM_BLOQUE;
        g.setColor(new Color(30, 30, 30));
        g.fillRect(panelX, 0, ANCHO_SCORE, ALTO_PANEL);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("SCORE", panelX + 20, 40);
        g.setFont(new Font("Arial", Font.BOLD, 32));
        g.drawString(String.valueOf(score), panelX + 20, 80);
        
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("Líneas:", panelX + 20, 120);
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.drawString(String.valueOf(lineasEliminadas), panelX + 20, 150);
        
        // Siguiente pieza
        if (!juegoTerminado && piezaSiguiente != null) {
            g.setFont(new Font("Arial", Font.BOLD, 16));
            g.setColor(Color.WHITE);
            g.drawString("Siguiente:", panelX + 20, 200);
            
            // siguiente pieza en miniatura
            int tamMiniBloque = 15;
            int offsetX = panelX + 30;
            int offsetY = 220;
            int centroX = offsetX + (4 * tamMiniBloque) / 2;
            
            for (int y = 0; y < piezaSiguiente.forma.length; y++) {
                for (int x = 0; x < piezaSiguiente.forma[y].length; x++) {
                    if (piezaSiguiente.forma[y][x] == 1) {
                        int px = centroX - (piezaSiguiente.forma[y].length * tamMiniBloque) / 2 + x * tamMiniBloque;
                        int py = offsetY + y * tamMiniBloque;
                        
                        g.setColor(colorSiguiente);
                        g.fillRect(px, py, tamMiniBloque, tamMiniBloque);
                        g.setColor(colorSiguiente.darker());
                        g.drawRect(px, py, tamMiniBloque, tamMiniBloque);
                    }
                }
            }
        }
        
        // instrucciones
        g.setFont(new Font("Arial", Font.PLAIN, 12));
        g.setColor(new Color(200, 200, 200));
        int instruccionesY = juegoTerminado ? 200 : 320;
        g.drawString("Controles:", panelX + 20, instruccionesY);
        g.drawString("← → : Mover", panelX + 20, instruccionesY + 20);
        g.drawString("↓ : Bajar", panelX + 20, instruccionesY + 40);
        g.drawString("Espacio: Caída", panelX + 20, instruccionesY + 60);
        g.drawString("rápida", panelX + 20, instruccionesY + 75);
        g.drawString("X : Rotar der.", panelX + 20, instruccionesY + 95);
        g.drawString("Z : Rotar izq.", panelX + 20, instruccionesY + 115);
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
        score = 0;
        lineasEliminadas = 0;
        piezaSiguiente = null;
        // Generar primera pieza siguiente
        generarPiezaSiguiente();
        // Generar pieza actual
        generarNuevaPieza();
        temporizador.start();
        repaint();
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}
}
