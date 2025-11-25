import javax.swing.JFrame;

public class JuegoTetris extends JFrame {

    public JuegoTetris() {
        setTitle("Tetris Clásico");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        TableroPanel panel = new TableroPanel();
        add(panel);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
