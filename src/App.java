import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Portada portada = new Portada();
            portada.setVisible(true);
        });
    }
}
