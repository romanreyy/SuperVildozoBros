package clases;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Mario Clone");
        GamePanel gamePanel = new GamePanel();
        frame.add(gamePanel);
        frame.setSize(800, 600);  // Ajustar el tamaño de la ventana
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private Player player;
    private ArrayList<Box> boxes;
    private ArrayList<Platform> platforms;
    private ArrayList<Enemy> enemies;
    private Timer timer;
    private int cameraX = 0;  // Posición de la cámara
    private int playerLives = 3; // Vidas del jugador
    private boolean gameOver = false;

    public GamePanel() {
        setFocusable(true);
        addKeyListener(this);
        player = new Player(50, 300);  // Posición inicial del jugador
        boxes = new ArrayList<>();
        platforms = new ArrayList<>();
        enemies = new ArrayList<>();

        // Crear algunas cajas en el suelo
        boxes.add(new Box(100, 350));
        boxes.add(new Box(250, 350));
        boxes.add(new Box(400, 350));

        // Crear plataformas en el aire
        platforms.add(new Platform(150, 250, 100, 20));
        platforms.add(new Platform(350, 200, 100, 20));

        // Crear enemigos
        enemies.add(new Enemy(200, 350, boxes));
        enemies.add(new Enemy(500, 350, boxes));

        timer = new Timer(20, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Dibujar el fondo
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, getWidth(), getHeight());

        // Dibujar vidas del jugador
        g.setColor(Color.BLACK);
        g.drawString("Vidas: " + playerLives, 20, 20);

        if (gameOver) {
            g.drawString("Game Over", getWidth() / 2 - 40, getHeight() / 2);
            return;
        }

        // Mover la cámara en función de la posición del jugador
        g.translate(-cameraX, 0);

        player.draw(g);

        // Dibujar cajas
        for (Box box : boxes) {
            box.draw(g);
        }

        // Dibujar plataformas
        for (Platform platform : platforms) {
            platform.draw(g);
        }

        // Dibujar enemigos
        for (Enemy enemy : enemies) {
            enemy.draw(g);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            player.update();

            // Detectar colisión con enemigos
            for (Enemy enemy : enemies) {
                enemy.update();
                if (player.intersects(enemy.getBounds())) {
                    playerLives--;
                    player.respawn();  // Reaparecer después de colisionar
                    if (playerLives == 0) {
                        gameOver = true;
                    }
                }
            }

            // Detectar colisión con cajas y plataformas
            boolean collisionDetected = false;
            for (Box box : boxes) {
                if (player.intersects(box.getBounds())) {
                    player.handleCollision(box.getBounds(), "box");
                    collisionDetected = true;
                }
            }

            for (Platform platform : platforms) {
                if (player.intersects(platform.getBounds())) {
                    player.handleCollision(platform.getBounds(), "platform");
                    collisionDetected = true;
                }
            }

            // Si no hay colisiones, el jugador está cayendo (gravedad)
            if (!collisionDetected) {
                player.applyGravity();
            } else {
                player.stopFalling();  // Si hay colisión, detener la caída
            }

            // Movimiento de la cámara
            cameraX = player.getX() - getWidth() / 2 + player.getWidth() / 2;
            if (cameraX < 0) cameraX = 0;  // Evitar que la cámara se mueva más allá del inicio
        }

        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            player.setVelX(-5);
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            player.setVelX(5);
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            player.jump();  // Permitir saltar en cualquier momento
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT) {
            player.setVelX(0);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

}

class Player {
    private int x, y;
    private int velX = 0;
    private int velY = 0;
    private boolean onGround = false;
    private int width = 30;
    private int height = 50;
    private final int GRAVITY = 1;  // Velocidad de caída
    private final int JUMP_STRENGTH = -15;  // Fuerza del salto
    private int initialX, initialY;  // Para reaparecer al morir

    public Player(int x, int y) {
        this.x = x;
        this.y = y;
        this.initialX = x;  // Guardar posición inicial para reaparecer
        this.initialY = y;
    }

    public void update() {
        x += velX;
        y += velY;

        // Aplicar gravedad si el jugador no está en el suelo
        if (!onGround) {
            velY += GRAVITY;  // Aumentar la velocidad de caída
        }

        // Limitar la posición del jugador para no caer más allá del piso (350)
        if (y > 350) {
            y = 350; // Asegúrate de que el jugador no atraviese el suelo
            velY = 0;
            onGround = true; // Ahora está en el suelo
        }
    }

    public void jump() {
        if (onGround) { // Solo saltar si está en el suelo
            velY = JUMP_STRENGTH; // Aplicar fuerza de salto
            onGround = false; // El jugador ya no está en el suelo
        }
    }

    public boolean isOnGround() {
        return onGround;
    }

    public void setVelX(int velX) {
        this.velX = velX;
    }

    public void draw(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillRect(x, y, width, height);
    }

    public int getX() {
        return x;
    }

    public int getWidth() {
        return width;
    }

    public boolean intersects(Rectangle r) {
        return new Rectangle(x, y, width, height).intersects(r);
    }

    public void handleCollision(Rectangle r, String type) {
        Rectangle playerBounds = new Rectangle(x, y, width, height);
        
        // Colisión desde arriba
        if (velY > 0 && playerBounds.getMaxY() > r.getY() && playerBounds.getMaxY() - r.getY() <= velY + 5) {
            y = (int) r.getY() - height;
            velY = 0;
            onGround = true; // Ahora está en el suelo
        }
        // Colisión desde abajo
        else if (velY < 0 && playerBounds.getY() < r.getMaxY() && r.getMaxY() - playerBounds.getY() <= Math.abs(velY) + 5) {
            y = (int) r.getMaxY();
            velY = 0;
        }
        // Colisión desde la izquierda
        else if (velX > 0 && playerBounds.getMaxX() > r.getX() && playerBounds.getMaxX() - r.getX() <= velX + 5) {
            x = (int) r.getX() - width;
            velX = 0;
        }
        // Colisión desde la derecha
        else if (velX < 0 && playerBounds.getX() < r.getMaxX() && r.getMaxX() - playerBounds.getX() <= Math.abs(velX) + 5) {
            x = (int) r.getMaxX();
            velX = 0;
        }
    }

    public void applyGravity() {
        if (!onGround) {
            velY += GRAVITY;  // Simular la gravedad
        }
    }

    public void stopFalling() {
        velY = 0;  // Detener la caída
    }

    public void respawn() {
        // Reaparecer en la posición inicial después de perder una vida
        x = initialX;
        y = initialY;
        velX = 0;
        velY = 0;
    }
}

class Box {
    private int x, y;

    public Box(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getY() {
        return y;
    }

    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(x, y, 50, 50);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, 50, 50);
    }
}

class Platform {
    private int x, y, width, height;

    public Platform(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void draw(Graphics g) {
        g.setColor(Color.GRAY);
        g.fillRect(x, y, width, height);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}

class Enemy {
    private int x, y;
    private int velX = 2;
    private ArrayList<Box> boxes;

    public Enemy(int x, int y, ArrayList<Box> boxes) {
        this.x = x;
        this.y = y;
        this.boxes = boxes;
    }

    public void update() {
        x += velX;

        // Cambiar de dirección al chocar con los bordes o cajas
        if (x < 0 || x > 550) {
            velX = -velX;
        }

        for (Box box : boxes) {
            if (new Rectangle(x, y, 30, 50).intersects(box.getBounds())) {
                velX = -velX;
            }
        }
    }

    public void draw(Graphics g) {
        g.setColor(Color.GREEN);
        g.fillRect(x, y, 30, 50);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, 30, 50);
    }
}
