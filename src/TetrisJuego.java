import javax.swing.JFrame;

public class TetrisJuego extends JFrame {
    TetrisJuego() {
        setTitle("Tetris clásico");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        add(new GamePanel());
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}