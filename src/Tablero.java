import java.awt.Color;

public class Tablero {

    public static final int ROWS = 20;
    public static final int COLS = 10;

    private final Color[][] board = new Color[ROWS][COLS];

    public boolean isCellEmpty(int row, int col) {
        return board[row][col] == null;
    }

    public Color getCell(int row, int col) {
        return board[row][col];
    }

    public void placeBlock(int row, int col, Color color) {
        board[row][col] = color;
    }

    public void clearBoard() {
        for (int r = 0; r < ROWS; r++)
            for (int c = 0; c < COLS; c++)
                board[r][c] = null;
    }

    public boolean isLineFull(int row) {
        for (int c = 0; c < COLS; c++)
            if (board[row][c] == null) return false;
        return true;
    }

    public void removeLine(int row) {
        for (int r = row; r > 0; r--) {
            System.arraycopy(board[r - 1], 0, board[r], 0, COLS);
        }
        // llenar fila superior en blanco
        for (int c = 0; c < COLS; c++)
            board[0][c] = null;
    }
}
