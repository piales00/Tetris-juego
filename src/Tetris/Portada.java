package Tetris;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class Portada extends JFrame {
    public Portada() {
        setTitle("Tetris Clásico - UTP");
        setSize(700, 800); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panelFondo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Fondo 
                GradientPaint gp = new GradientPaint(
                    0, 0, new Color(15, 32, 39),
                    0, getHeight(), new Color(32, 58, 67)
                );
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                // Patrón de puntos 
                g2d.setColor(new Color(255, 255, 255, 15));
                for (int i = 0; i < getWidth(); i += 30) {
                    for (int j = 0; j < getHeight(); j += 30) {
                        g2d.fillOval(i, j, 3, 3);
                    }
                }

                // piezas de Tetris decorativas con sombra
                drawTetrisPiece(g2d, 80, 150, new Color(0, 240, 255), "I"); 
                drawTetrisPiece(g2d, 520, 200, new Color(255, 160, 0), "L"); 
                drawTetrisPiece(g2d, 100, 600, new Color(255, 0, 255), "T"); 
                drawTetrisPiece(g2d, 550, 650, new Color(0, 240, 0), "S"); 
            }

            private void drawTetrisPiece(Graphics2D g2d, int x, int y, Color color, String type) {
                int blockSize = 25;
                
                // Sombra
                g2d.setColor(new Color(0, 0, 0, 50));
                if (type.equals("I")) {
                    for (int i = 0; i < 4; i++) {
                        g2d.fillRoundRect(x + i * blockSize + 3, y + 3, blockSize - 2, blockSize - 2, 8, 8);
                    }
                } else if (type.equals("L")) {
                    g2d.fillRoundRect(x + 3, y + 3, blockSize - 2, blockSize - 2, 8, 8);
                    g2d.fillRoundRect(x + 3, y + blockSize + 3, blockSize - 2, blockSize - 2, 8, 8);
                    g2d.fillRoundRect(x + 3, y + blockSize * 2 + 3, blockSize - 2, blockSize - 2, 8, 8);
                    g2d.fillRoundRect(x + blockSize + 3, y + blockSize * 2 + 3, blockSize - 2, blockSize - 2, 8, 8);
                } else if (type.equals("T")) {
                    for (int i = 0; i < 3; i++) {
                        g2d.fillRoundRect(x + i * blockSize + 3, y + 3, blockSize - 2, blockSize - 2, 8, 8);
                    }
                    g2d.fillRoundRect(x + blockSize + 3, y + blockSize + 3, blockSize - 2, blockSize - 2, 8, 8);
                } else if (type.equals("S")) {
                    g2d.fillRoundRect(x + blockSize + 3, y + 3, blockSize - 2, blockSize - 2, 8, 8);
                    g2d.fillRoundRect(x + blockSize * 2 + 3, y + 3, blockSize - 2, blockSize - 2, 8, 8);
                    g2d.fillRoundRect(x + 3, y + blockSize + 3, blockSize - 2, blockSize - 2, 8, 8);
                    g2d.fillRoundRect(x + blockSize + 3, y + blockSize + 3, blockSize - 2, blockSize - 2, 8, 8);
                }

                // Pieza con brillo
                GradientPaint gp = new GradientPaint(x, y, color, x, y + blockSize * 3, color.darker());
                g2d.setPaint(gp);
                
                if (type.equals("I")) {
                    for (int i = 0; i < 4; i++) {
                        g2d.fillRoundRect(x + i * blockSize, y, blockSize - 2, blockSize - 2, 8, 8);
                    }
                } else if (type.equals("L")) {
                    g2d.fillRoundRect(x, y, blockSize - 2, blockSize - 2, 8, 8);
                    g2d.fillRoundRect(x, y + blockSize, blockSize - 2, blockSize - 2, 8, 8);
                    g2d.fillRoundRect(x, y + blockSize * 2, blockSize - 2, blockSize - 2, 8, 8);
                    g2d.fillRoundRect(x + blockSize, y + blockSize * 2, blockSize - 2, blockSize - 2, 8, 8);
                } else if (type.equals("T")) {
                    for (int i = 0; i < 3; i++) {
                        g2d.fillRoundRect(x + i * blockSize, y, blockSize - 2, blockSize - 2, 8, 8);
                    }
                    g2d.fillRoundRect(x + blockSize, y + blockSize, blockSize - 2, blockSize - 2, 8, 8);
                } else if (type.equals("S")) {
                    g2d.fillRoundRect(x + blockSize, y, blockSize - 2, blockSize - 2, 8, 8);
                    g2d.fillRoundRect(x + blockSize * 2, y, blockSize - 2, blockSize - 2, 8, 8);
                    g2d.fillRoundRect(x, y + blockSize, blockSize - 2, blockSize - 2, 8, 8);
                    g2d.fillRoundRect(x + blockSize, y + blockSize, blockSize - 2, blockSize - 2, 8, 8);
                }
            }
        };
        panelFondo.setLayout(null);

        // Subtítulo
        JLabel subtitulo = new JLabel("PROYECTO FINAL - TALLER DE PROGRAMACIÓN", JLabel.CENTER);
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitulo.setForeground(new Color(180, 200, 210));
        subtitulo.setBounds(50, 100, 600, 25);
        panelFondo.add(subtitulo);
        
        // Universidad
        JLabel tituloUTP = new JLabel("UNIVERSIDAD TECNOLÓGICA DEL PERÚ", JLabel.CENTER);
        tituloUTP.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tituloUTP.setForeground(new Color(255, 255, 255));
        tituloUTP.setBounds(50, 130, 600, 30);
        panelFondo.add(tituloUTP);
        
        // Título 
        JLabel tituloSombra = new JLabel("TETRIS", JLabel.CENTER);
        tituloSombra.setFont(new Font("Arial Black", Font.BOLD, 72));
        tituloSombra.setForeground(new Color(0, 0, 0, 80));
        tituloSombra.setBounds(52, 192, 600, 80);
        panelFondo.add(tituloSombra);
        
        JLabel titulo = new JLabel("TETRIS", JLabel.CENTER);
        titulo.setFont(new Font("Arial Black", Font.BOLD, 72));
        titulo.setForeground(new Color(0, 240, 255));
        titulo.setBounds(50, 190, 600, 80);
        panelFondo.add(titulo);

        // Subtítulo 
        JLabel subtituloJuego = new JLabel("CLÁSICO", JLabel.CENTER);
        subtituloJuego.setFont(new Font("Segoe UI", Font.BOLD, 20));
        subtituloJuego.setForeground(new Color(255, 160, 0));
        subtituloJuego.setBounds(50, 270, 600, 30);
        panelFondo.add(subtituloJuego);

        // Botón de jugar con hover effect
        JButton btnJugar = createStyledButton("JUGAR", new Color(0, 230, 118), 225, 350);
        panelFondo.add(btnJugar);

        // Botón de salir
        JButton btnSalir = createStyledButton("SALIR", new Color(239, 83, 80), 225, 430);
        panelFondo.add(btnSalir);

        // Panel de créditos
        JPanel panelCreditos = new JPanel();
        panelCreditos.setLayout(new BoxLayout(panelCreditos, BoxLayout.Y_AXIS));
        panelCreditos.setOpaque(false);
        panelCreditos.setBounds(75, 530, 550, 200);

        JLabel creditoTitulo = new JLabel("DESARROLLADO POR:");
        creditoTitulo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        creditoTitulo.setForeground(new Color(180, 200, 210));
        creditoTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelCreditos.add(creditoTitulo);
        panelCreditos.add(Box.createVerticalStrut(10));

        String[] autores = {
            "Jhair Eros Rivera Salvador",
            "Piero Alessandro Perez Izquierdo",
            "Huanuco Jimenez Luis Alberto",
            "Contreras Mayorga Marcelo Fabian",
            "Mathias Gianpaul Cuba Barbaran"
        };

        for (String autor : autores) {
            JLabel labelAutor = new JLabel(autor);
            labelAutor.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            labelAutor.setForeground(new Color(200, 220, 230));
            labelAutor.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelCreditos.add(labelAutor);
            panelCreditos.add(Box.createVerticalStrut(5));
        }

        panelFondo.add(panelCreditos);

        // Año
        JLabel anio = new JLabel("10 de diciembre de 2025", JLabel.CENTER);
        anio.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        anio.setForeground(new Color(150, 170, 180));
        anio.setBounds(250, 740, 200, 20);
        panelFondo.add(anio);

        // Acciones de los botones
        btnJugar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                SwingUtilities.invokeLater(() -> new JuegoTetris());
            }
        });

        btnSalir.addActionListener(e -> System.exit(0));

        setContentPane(panelFondo);
    }

    private JButton createStyledButton(String text, Color bgColor, int x, int y) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 20));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setBounds(x, y, 250, 55);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        // Efecto hover
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.brighter());
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Portada portada = new Portada();
            portada.setVisible(true);
        });
    }
}