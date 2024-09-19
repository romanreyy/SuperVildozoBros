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
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private Player player;
    private ArrayList<Box> boxes;
    private ArrayList<Platform> platforms;
    private Timer timer;
    private int cameraX = 0;
    private int playerLives = 3;
    private boolean gameOver = false;

    public GamePanel() {
        setFocusable(true);
        addKeyListener(this);
        player = new Player(50, 300);
        boxes = new ArrayList<>();
        platforms = new ArrayList<>();

        // Crear cajas en el suelo
        boxes.add(new Box(100, 350));
        boxes.add(new Box(250, 350));
        boxes.add(new Box(400, 350));
        boxes.add(new Box(550, 350));
        boxes.add(new Box(700, 350));

        // Crear plataformas en el aire (ahora más bajas)
        platforms.add(new Platform(150, 280, 100, 20));
        platforms.add(new Platform(350, 250, 100, 20));
        platforms.add(new Platform(550, 220, 100, 20));
        platforms.add(new Platform(750, 250, 100, 20));
        platforms.add(new Platform(950, 280, 100, 20));

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
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            player.update();

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
            }

            // Movimiento de la cámara
            cameraX = player.getX() - getWidth() / 2 + player.getWidth() / 2;
            if (cameraX < 0) cameraX = 0;
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
            player.jump();
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
    private boolean canJump = true;
    private int width = 30;
    private int height = 50;
    private final int GRAVITY = 1;
    private final int JUMP_STRENGTH = -15;
    private int initialX, initialY;

    public Player(int x, int y) {
        this.x = x;
        this.y = y;
        this.initialX = x;
        this.initialY = y;
    }

    public void update() {
        x += velX;
        y += velY;

        velY += GRAVITY;

        if (y > 350) {
            y = 350;
            velY = 0;
            canJump = true;
        }
    }

    public void jump() {
        if (canJump) {
            velY = JUMP_STRENGTH;
            canJump = false;
        }
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
            canJump = true;
        }
        // Colisión desde abajo
        else if (velY < 0 && playerBounds.getY() < r.getMaxY() && r.getMaxY() - playerBounds.getY() <= Math.abs(velY) + 5) {
            y = (int) r.getMaxY();
            velY = 0;
        }
        // Colisión desde la izquierda
        else if (velX > 0 && playerBounds.getMaxX() > r.getX() && playerBounds.getMaxX() - r.getX() <= velX + 5) {
            x = (int) r.getX() - width;
        }
        // Colisión desde la derecha
        else if (velX < 0 && playerBounds.getX() < r.getMaxX() && r.getMaxX() - playerBounds.getX() <= Math.abs(velX) + 5) {
            x = (int) r.getMaxX();
        }
    }

    public void applyGravity() {
        velY += GRAVITY;
        canJump = true;
    }
    

    public void respawn() {
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