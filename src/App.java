import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;


import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TetrisJuego());
    }
}



class GamePanel extends JPanel implements ActionListener, KeyListener {
    private static final int COLS = 10;
    private static final int ROWS = 20;
    private static final int BLOCK_SIZE = 30;
    private static final int PANEL_WIDTH = COLS * BLOCK_SIZE;
    private static final int PANEL_HEIGHT = ROWS * BLOCK_SIZE;
    private static final int INITIAL_DELAY = 500;
    private static final Color[] COLORS = {
            new Color(0x00BCD4), new Color(0xFF5722), new Color(0x4CAF50),
            new Color(0xFFEB3B), new Color(0x673AB7), new Color(0x9E9E9E),
            new Color(0xE91E63)
    };

    private final Color[][] board = new Color[ROWS][COLS];
    private final Random random = new Random();
    private final Timer timer;

    private int[][] currentShape;
    private Color currentColor;
    private Point currentPos;
    private boolean gameOver;

    GamePanel() {
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);
        spawnNewPiece();
        timer = new Timer(INITIAL_DELAY, this);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameOver) {
            return;
        }
        moveDown();
    }

    private void spawnNewPiece() {
        int shapeIndex = random.nextInt(SHAPES.length);
        currentShape = copyShape(SHAPES[shapeIndex]);
        currentColor = COLORS[shapeIndex];
        currentPos = new Point(COLS / 2 - currentShape[0].length / 2, 0);
        if (!canMove(currentShape, currentPos.x, currentPos.y)) {
            gameOver = true;
            timer.stop();
        }
    }

    private void moveDown() {
        if (canMove(currentShape, currentPos.x, currentPos.y + 1)) {
            currentPos.y++;
        } else {
            mergePiece();
            clearLines();
            spawnNewPiece();
        }
        repaint();
    }

    private void moveHorizontal(int delta) {
        if (gameOver) {
            return;
        }
        if (canMove(currentShape, currentPos.x + delta, currentPos.y)) {
            currentPos.x += delta;
            repaint();
        }
    }

    private void hardDrop() {
        if (gameOver) {
            return;
        }
        while (canMove(currentShape, currentPos.x, currentPos.y + 1)) {
            currentPos.y++;
        }
        mergePiece();
        clearLines();
        spawnNewPiece();
        repaint();
    }

    private void rotatePieceClockwise() {
        if (gameOver) {
            return;
        }
        int[][] rotated = rotateMatrixCW(currentShape);
        if (tryApplyRotation(rotated)) {
            repaint();
        }
    }

    private void rotatePieceCounterClockwise() {
        if (gameOver) {
            return;
        }
        int[][] rotated = rotateMatrixCCW(currentShape);
        if (tryApplyRotation(rotated)) {
            repaint();
        }
    }

    private boolean tryApplyRotation(int[][] rotated) {
        if (canMove(rotated, currentPos.x, currentPos.y)) {
            currentShape = rotated;
            return true;
        }
        if (canMove(rotated, currentPos.x - 1, currentPos.y)) {
            currentPos.x -= 1;
            currentShape = rotated;
            return true;
        }
        if (canMove(rotated, currentPos.x + 1, currentPos.y)) {
            currentPos.x += 1;
            currentShape = rotated;
            return true;
        }
        return false;
    }

    private void mergePiece() {
        for (int row = 0; row < currentShape.length; row++) {
            for (int col = 0; col < currentShape[row].length; col++) {
                if (currentShape[row][col] == 1) {
                    int boardRow = currentPos.y + row;
                    int boardCol = currentPos.x + col;
                    if (boardRow >= 0 && boardRow < ROWS && boardCol >= 0 && boardCol < COLS) {
                        board[boardRow][boardCol] = currentColor;
                    }
                }
            }
        }
    }

    private void clearLines() {
        for (int row = ROWS - 1; row >= 0; row--) {
            boolean full = true;
            for (int col = 0; col < COLS; col++) {
                if (board[row][col] == null) {
                    full = false;
                    break;
                }
            }
            if (full) {
                removeRow(row);
                row++; // Revisamos la misma fila porque bajaron las de arriba
            }
        }
    }

    private void removeRow(int rowToRemove) {
        for (int row = rowToRemove; row > 0; row--) {
            System.arraycopy(board[row - 1], 0, board[row], 0, COLS);
        }
        for (int col = 0; col < COLS; col++) {
            board[0][col] = null;
        }
    }

    private boolean canMove(int[][] shape, int destX, int destY) {
        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[row].length; col++) {
                if (shape[row][col] == 0) {
                    continue;
                }
                int boardX = destX + col;
                int boardY = destY + row;
                if (boardX < 0 || boardX >= COLS || boardY >= ROWS) {
                    return false;
                }
                if (boardY >= 0 && board[boardY][boardX] != null) {
                    return false;
                }
            }
        }
        return true;
    }

    private void drawBoard(Graphics g) {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                Color color = board[row][col];
                if (color != null) {
                    drawBlock(g, col, row, color);
                }
            }
        }
    }

    private void drawCurrentPiece(Graphics g) {
        for (int row = 0; row < currentShape.length; row++) {
            for (int col = 0; col < currentShape[row].length; col++) {
                if (currentShape[row][col] == 1) {
                    int drawX = currentPos.x + col;
                    int drawY = currentPos.y + row;
                    drawBlock(g, drawX, drawY, currentColor);
                }
            }
        }
    }

    private void drawBlock(Graphics g, int x, int y, Color color) {
        int pixelX = x * BLOCK_SIZE;
        int pixelY = y * BLOCK_SIZE;
        g.setColor(color);
        g.fillRect(pixelX, pixelY, BLOCK_SIZE, BLOCK_SIZE);
        g.setColor(color.darker());
        g.drawRect(pixelX, pixelY, BLOCK_SIZE, BLOCK_SIZE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBoard(g);
        if (!gameOver) {
            drawCurrentPiece(g);
        } else {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 24));
            g.drawString("Juego terminado", PANEL_WIDTH / 2 - 100, PANEL_HEIGHT / 2);
            g.setFont(new Font("Arial", Font.PLAIN, 16));
            g.drawString("Presiona R para reiniciar", PANEL_WIDTH / 2 - 100, PANEL_HEIGHT / 2 + 30);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                moveHorizontal(-1);
                break;
            case KeyEvent.VK_RIGHT:
                moveHorizontal(1);
                break;
            case KeyEvent.VK_DOWN:
                moveDown();
                break;
            case KeyEvent.VK_UP:
            case KeyEvent.VK_X:
                rotatePieceClockwise();
                break;
            case KeyEvent.VK_Z:
                rotatePieceCounterClockwise();
                break;
            case KeyEvent.VK_SPACE:
                hardDrop();
                break;
            case KeyEvent.VK_R:
                if (gameOver) {
                    resetGame();
                }
                break;
            default:
                break;
        }
    }

    private void resetGame() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                board[row][col] = null;
            }
        }
        gameOver = false;
        spawnNewPiece();
        timer.start();
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // No usamos este evento pero la interfaz lo exige
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // No usamos este evento pero la interfaz lo exige
    }

    private int[][] rotateMatrixCW(int[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        int[][] rotated = new int[cols][rows];
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                rotated[col][rows - 1 - row] = matrix[row][col];
            }
        }
        return rotated;
    }

    private int[][] rotateMatrixCCW(int[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        int[][] rotated = new int[cols][rows];
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                rotated[cols - 1 - col][row] = matrix[row][col];
            }
        }
        return rotated;
    }

    private int[][] copyShape(int[][] shape) {
        int[][] clone = new int[shape.length][];
        for (int i = 0; i < shape.length; i++) {
            clone[i] = shape[i].clone();
        }
        return clone;
    }

    private static final int[][][] SHAPES = {
            { {1, 1, 1, 1} }, // I
            {
                    {1, 0, 0},
                    {1, 1, 1}
            }, // J
            {
                    {0, 0, 1},
                    {1, 1, 1}
            }, // L
            {
                    {1, 1},
                    {1, 1}
            }, // O
            {
                    {0, 1, 1},
                    {1, 1, 0}
            }, // S
            {
                    {0, 1, 0},
                    {1, 1, 1}
            }, // T
            {
                    {1, 1, 0},
                    {0, 1, 1}
            } // Z
    };
}
