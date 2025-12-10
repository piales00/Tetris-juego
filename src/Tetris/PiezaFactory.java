package Tetris;
import java.awt.Color;

public class PiezaFactory {

    public static final int[][][] FIGURAS = {
            { {1, 1, 1, 1} },
            { {1, 0, 0}, {1, 1, 1} },
            { {0, 0, 1}, {1, 1, 1} },
            { {1, 1}, {1, 1} },
            { {0, 1, 1}, {1, 1, 0} },
            { {0, 1, 0}, {1, 1, 1} },
            { {1, 1, 0}, {0, 1, 1} }
    };

    public static final Color[] COLORES = {
            new Color(0x00BCD4),
            new Color(0xFF5722),
            new Color(0x4CAF50),
            new Color(0xFFEB3B),
            new Color(0x673AB7),
            new Color(0x9E9E9E),
            new Color(0xE91E63)
    };

    public static Pieza copiar(int[][] forma) {
        int[][] copia = new int[forma.length][];
        for (int i = 0; i < forma.length; i++) {
            copia[i] = forma[i].clone();
        }
        return new Pieza(copia);
    }

    public static int[][] rotarDerecha(int[][] m) {
        int filas = m.length;
        int columnas = m[0].length;
        int[][] r = new int[columnas][filas];

        for (int y = 0; y < filas; y++)
            for (int x = 0; x < columnas; x++)
                r[x][filas - 1 - y] = m[y][x];

        return r;
    }

    public static int[][] rotarIzquierda(int[][] m) {
        int filas = m.length;
        int columnas = m[0].length;
        int[][] r = new int[columnas][filas];

        for (int y = 0; y < filas; y++)
            for (int x = 0; x < columnas; x++)
                r[columnas - 1 - x][y] = m[y][x];

        return r;
    }
}

