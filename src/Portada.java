
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Portada extends JFrame {
    public Portada() {
        setTitle("Portada - Tetris clásico");
        setSize(420, 700); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

     
        JPanel panelFondo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Fondo degradado
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(36, 123, 160);
                Color color2 = new Color(255, 216, 90);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                // Dibujar un bloque de Tetris como adorno
                g2d.setColor(new Color(0x00BCD4));
                g2d.fillRoundRect(150, 70, 100, 30, 12, 12);
                g2d.setColor(new Color(0xFFEB3B));
                g2d.fillRoundRect(120, 110, 30, 30, 12, 12);
                g2d.setColor(new Color(0xE91E63));
                g2d.fillRoundRect(180, 110, 30, 30, 12, 12);
                g2d.setColor(new Color(0x4CAF50));
                g2d.fillRoundRect(240, 110, 30, 30, 12, 12);
            }
        };
        panelFondo.setLayout(null);

        // Titulo
        JLabel titulo = new JLabel("TETRIS CLÁSICO", JLabel.CENTER);
        titulo.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 36));
        titulo.setForeground(Color.WHITE);
        titulo.setBounds(50, 20, 300, 60);
        panelFondo.add(titulo);

        // Botón de jugar
        JButton btnJugar = new JButton("Jugar");
        btnJugar.setFont(new Font("Arial", Font.BOLD, 22));
        btnJugar.setBackground(new Color(46, 204, 113));
        btnJugar.setForeground(Color.WHITE);
        btnJugar.setFocusPainted(false);
        btnJugar.setBounds(120, 250, 160, 50);
        btnJugar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        panelFondo.add(btnJugar);

        // Botón de salir
        JButton btnSalir = new JButton("Salir");
        btnSalir.setFont(new Font("Arial", Font.BOLD, 18));
        btnSalir.setBackground(new Color(231, 76, 60));
        btnSalir.setForeground(Color.WHITE);
        btnSalir.setFocusPainted(false);
        btnSalir.setBounds(120, 320, 160, 45);
        btnSalir.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        panelFondo.add(btnSalir);

        // Pie de página/crédito
        JLabel credit = new JLabel("© 2024 Mi Tetris", JLabel.CENTER);
        credit.setFont(new Font("Arial", Font.PLAIN, 14));
        credit.setForeground(new Color(0, 0, 0, 128));
        credit.setBounds(100, 440, 200, 20);
        panelFondo.add(credit);

        // Acciones de los botones
        btnJugar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                // Ejecutar la App principal
                App.main(new String[]{});
            }
        });

        btnSalir.addActionListener(e -> System.exit(0));

        setContentPane(panelFondo);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Portada portada = new Portada();
            portada.setVisible(true);
        });
    }
}

