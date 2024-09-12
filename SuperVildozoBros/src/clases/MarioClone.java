package clases;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MarioClone extends JPanel implements KeyListener, ActionListener {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int GROUND_Y = 500;
    private static final int GRAVITY = 1;
    private static final int SCROLL_SPEED = 5;

    private Player player;
    private List<Enemy> enemies;
    private List<Platform> platforms;
    private List<Coin> coins;
    private Flag flag; // Bandera para pasar de nivel
    private int score;
    private int cameraX;
    private Timer timer;
    private boolean[] keys;
    private Image backgroundImage;

    public MarioClone() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        addKeyListener(this);
        setFocusable(true);

        keys = new boolean[256];
        
        loadResources();
        initializeGame();
        
        timer = new Timer(1000 / 60, this);
        timer.start();
    }

    private void loadResources() {
        // En un juego real, cargaríamos imágenes para los sprites aquí
        backgroundImage = new ImageIcon("background.png").getImage();
    }

    private void initializeGame() {
        player = new Player(50, GROUND_Y - 50, GRAVITY);
        enemies = new ArrayList<>();
        platforms = new ArrayList<>();
        coins = new ArrayList<>();
        score = 0;
        cameraX = 0;

        // Add platforms, enemies, and coins
        for (int i = 0; i < 10; i++) {
            platforms.add(new Platform(i * 200, GROUND_Y - 100 - (i % 3) * 50, 150, 20));
            enemies.add(new Enemy(i * 250 + 100, GROUND_Y - 30));
            coins.add(new Coin(i * 180 + 50, GROUND_Y - 150 - (i % 3) * 50));
        }

        // Add the flag
        flag = new Flag(3000, GROUND_Y - 100); // Adjust position as needed
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Dibujar fondo con scroll
        g.drawImage(backgroundImage, -cameraX % WIDTH, 0, null);
        g.drawImage(backgroundImage, (-cameraX % WIDTH) + WIDTH, 0, null);

        // Dibujar elementos del juego
        for (Platform platform : platforms) {
            platform.draw(g, cameraX);
        }
        for (Enemy enemy : enemies) {
            enemy.draw(g, cameraX);
        }
        for (Coin coin : coins) {
            coin.draw(g, cameraX);
        }
        flag.draw(g, cameraX); // Dibujar la bandera
        player.draw(g, cameraX);

        // Dibujar suelo
        g.setColor(Color.GREEN);
        g.fillRect(0, GROUND_Y, WIDTH, HEIGHT - GROUND_Y);

        // Dibujar puntuación
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + score, 20, 30);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        updateGame();
        repaint();
    }

    private void updateGame() {
        player.update(keys);
        
        // Scroll de cámara
        if (player.x - cameraX > WIDTH * 0.7) {
            cameraX += SCROLL_SPEED;
        }

        // Actualizar enemigos
        for (Enemy enemy : enemies) {
            enemy.update();
        }

        // Colisiones con plataformas
        player.isOnGround = false;
        for (Platform platform : platforms) {
            if (player.collidesWith(platform)) {
                player.handleCollision(platform);
            }
        }

        // Colisiones con enemigos
        for (Enemy enemy : enemies) {
            if (player.collidesWith(enemy)) {
                player.hit();
            }
        }

        // Recolección de monedas
        Iterator<Coin> coinIterator = coins.iterator();
        while (coinIterator.hasNext()) {
            Coin coin = coinIterator.next();
            if (player.collidesWith(coin)) {
                coinIterator.remove();
                score += 100;
            }
        }

        // Colisión con la bandera
        if (player.collidesWith(flag)) {
            System.out.println("Level Completed!");
            // Aquí puedes añadir la lógica para pasar al siguiente nivel o finalizar el juego
        }

        // Gravedad si no está en el suelo
        if (!player.isOnGround) {
            player.velocityY += GRAVITY;
        }

        // Mantener al jugador dentro de los límites
        player.y = Math.min(player.y, GROUND_Y - player.height);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Mario Clone");
        MarioClone game = new MarioClone();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

// Clase base para objetos del juego
class GameObject {
    int x, y, width, height;

    GameObject(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    boolean collidesWith(GameObject other) {
        return x < other.x + other.width &&
               x + width > other.x &&
               y < other.y + other.height &&
               y + height > other.y;
    }

    void draw(Graphics g, int cameraX) {
        g.fillRect(x - cameraX, y, width, height);
    }
}

// Clase para el jugador
class Player extends GameObject {
    int velocityX = 0;
    int velocityY = 0;
    boolean isOnGround = false;
    static final int MOVE_SPEED = 5;
    static final int JUMP_VELOCITY = -15;
    private int gravity;

    Player(int x, int y, int gravity) {
        super(x, y, 30, 50);
        this.gravity = gravity;
    }

    void update(boolean[] keys) {
        if (keys[KeyEvent.VK_LEFT]) {
            velocityX = -MOVE_SPEED;
        } else if (keys[KeyEvent.VK_RIGHT]) {
            velocityX = MOVE_SPEED;
        } else {
            velocityX = 0;
        }

        if (keys[KeyEvent.VK_SPACE] && isOnGround) {
            velocityY = JUMP_VELOCITY;
            isOnGround = false;
        }

        x += velocityX;
        y += velocityY;

        // Apply gravity
        if (!isOnGround) {
            velocityY += gravity;
        }
    }

    void handleCollision(Platform platform) {
        if (velocityY > 0 && y + height > platform.y && y < platform.y) {
            y = platform.y - height;
            velocityY = 0;
            isOnGround = true;
        }
    }

    void hit() {
        // Logic when the player is hit
        System.out.println("Player hit!");
    }

    @Override
    void draw(Graphics g, int cameraX) {
        g.setColor(Color.RED);
        super.draw(g, cameraX);
    }
}


// Clase para los enemigos
class Enemy extends GameObject {
    int direction = 1;
    static final int MOVE_SPEED = 2;

    Enemy(int x, int y) {
        super(x, y, 30, 30);
    }

    void update() {
        x += MOVE_SPEED * direction;
        if (x % 100 == 0) direction *= -1;
    }

    @Override
    void draw(Graphics g, int cameraX) {
        g.setColor(Color.BLUE);
        g.fillOval(x - cameraX, y, width, height);
    }
}

// Clase para las plataformas
class Platform extends GameObject {
    Platform(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    void draw(Graphics g, int cameraX) {
        g.setColor(Color.GRAY);
        super.draw(g, cameraX);
    }
}

// Clase para las monedas
class Coin extends GameObject {
    Coin(int x, int y) {
        super(x, y, 20, 20);
    }

    @Override
    void draw(Graphics g, int cameraX) {
        g.setColor(Color.YELLOW);
        g.fillOval(x - cameraX, y, width, height);
    }
}

// Clase para la bandera
class Flag extends GameObject {
    Flag(int x, int y) {
        super(x, y, 30, 60); // Ajusta el tamaño según sea necesario
    }

    @Override
    void draw(Graphics g, int cameraX) {
        g.setColor(Color.BLACK);
        g.fillRect(x - cameraX, y, width, height); // Bandera negra para la visibilidad
        g.setColor(Color.RED);
        g.fillRect(x - cameraX + width / 4, y - height / 2, width / 2, height / 2); // La parte roja de la bandera
    }
}
