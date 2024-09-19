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
    private ArrayList<Rectangle> holes; // Cambiar a ArrayList<Rectangle>
    private Timer timer;
    private int cameraX = 0;
    private int playerLives = 3;
    private boolean gameOver = false;
    private Flag flag;
    private int levelWidth = 12000; // Ancho del nivel extendido
    private int groundLevel = 500; // Nivel del suelo
    private int skyLevel = 150; // Nivel del "cielo" para las plataformas
    private int boxSize = 50; // Tamaño de las cajas
    private int platformWidth = 100; // Ancho de las plataformas
    private int platformHeight = 20; // Alto de las plataformas

    public GamePanel() {
        setFocusable(true);
        addKeyListener(this);
        player = new Player(50, groundLevel - 50);
        boxes = new ArrayList<>();
        platforms = new ArrayList<>();
        holes = new ArrayList<>(); // Inicializar como ArrayList<Rectangle>

        // Definir muchas cajas en posiciones fijas a lo largo del nivel
        boxes.add(new Box(200, groundLevel - 150));  
        boxes.add(new Box(400, groundLevel - 150));  
        boxes.add(new Box(700, groundLevel - 150));  
        boxes.add(new Box(1200, groundLevel - 150));  
        boxes.add(new Box(1500, groundLevel - 150));  
        boxes.add(new Box(2000, groundLevel - 150));  
        boxes.add(new Box(2300, groundLevel - 150));  
        boxes.add(new Box(2900, groundLevel - 150));  
        boxes.add(new Box(3400, groundLevel - 150));  
        boxes.add(new Box(3900, groundLevel - 150));  
        boxes.add(new Box(4300, groundLevel - 150));  
        boxes.add(new Box(4700, groundLevel - 150));  
        boxes.add(new Box(5100, groundLevel - 150));  
        boxes.add(new Box(5500, groundLevel - 150));  
        boxes.add(new Box(5900, groundLevel - 150));  
        boxes.add(new Box(6300, groundLevel - 150));  
        boxes.add(new Box(6700, groundLevel - 150));  
        boxes.add(new Box(7100, groundLevel - 150));  
        boxes.add(new Box(7500, groundLevel - 150));  

        // Definir muchas plataformas en posiciones fijas a lo largo del nivel
        platforms.add(new Platform(300, groundLevel - 250, platformWidth, platformHeight));  
        platforms.add(new Platform(400, groundLevel - 300, platformWidth, platformHeight));  
        platforms.add(new Platform(600, groundLevel - 300, platformWidth, platformHeight));  
        platforms.add(new Platform(900, groundLevel - 250, platformWidth, platformHeight));  
        platforms.add(new Platform(1100, groundLevel - 270, platformWidth, platformHeight));  
        platforms.add(new Platform(1500, groundLevel - 250, platformWidth, platformHeight));  
        platforms.add(new Platform(1800, groundLevel - 270, platformWidth, platformHeight));  
        platforms.add(new Platform(2100, groundLevel - 265, platformWidth, platformHeight));  
        platforms.add(new Platform(2500, groundLevel - 250, platformWidth, platformHeight));  
        platforms.add(new Platform(3000, groundLevel - 300, platformWidth, platformHeight));  
        platforms.add(new Platform(3200, groundLevel - 250, platformWidth, platformHeight));  
        platforms.add(new Platform(3500, groundLevel - 280, platformWidth, platformHeight));  
        platforms.add(new Platform(3700, groundLevel - 250, platformWidth, platformHeight));  
        platforms.add(new Platform(4300, groundLevel - 250, platformWidth, platformHeight));  
        platforms.add(new Platform(5000, groundLevel - 280, platformWidth, platformHeight));  
        platforms.add(new Platform(5200, groundLevel - 300, platformWidth, platformHeight));  
        platforms.add(new Platform(5500, groundLevel - 260, platformWidth, platformHeight));  
        platforms.add(new Platform(5900, groundLevel - 240, platformWidth, platformHeight));  
        platforms.add(new Platform(6100, groundLevel - 250, platformWidth, platformHeight));  
        platforms.add(new Platform(6600, groundLevel - 270, platformWidth, platformHeight));  
        platforms.add(new Platform(6900, groundLevel - 250, platformWidth, platformHeight));  
        platforms.add(new Platform(7100, groundLevel - 200, platformWidth, platformHeight));  
        platforms.add(new Platform(7500, groundLevel - 240, platformWidth, platformHeight));  
        platforms.add(new Platform(8000, groundLevel - 280, platformWidth, platformHeight));  

        // Crear agujeros en el suelo (espacios vacíos), más altos y en posiciones más arriba
        holes.add(new Rectangle(900, groundLevel - 200, 100, 200));   // Un agujero en x=900
        holes.add(new Rectangle(2200, groundLevel - 200, 150, 200));  // Otro agujero más adelante
        holes.add(new Rectangle(4500, groundLevel - 200, 200, 200));  // Otro agujero en x=4500
        holes.add(new Rectangle(6500, groundLevel - 200, 100, 200));  // Último agujero en x=6500
        
        // Ampliar el nivel para que sea más largo
        levelWidth = 8000;

        // Agregar bandera al final del nivel
        flag = new Flag(levelWidth - 180, groundLevel - 180);

        timer = new Timer(20, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Dibujar el fondo
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, getWidth(), getHeight());

        // Dibujar el suelo con agujeros
        g.setColor(Color.DARK_GRAY);
        for (int i = 0; i < levelWidth; i += 800) {
            boolean isHole = false;
            for (Rectangle hole : holes) {
                if (i >= hole.x && i < hole.x + hole.width) {
                    isHole = true;
                    break;
                }
            }
            if (!isHole) {
                g.fillRect(i, groundLevel, 800, getHeight() - groundLevel);
            }
        }

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

        // Dibujar agujeros
        g.setColor(Color.BLACK);
        for (Rectangle hole : holes) {
            g.fillRect(900, 400, 200, 300);
        }

        // Dibujar bandera
        flag.draw(g);
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

            // Verificar si el jugador ha caído en un agujero
            boolean inHole = false;
            for (Rectangle hole : holes) {
                if (player.intersects(hole) && player.getVelY() > 0) {
                    if (player.getY() + player.getHeight() > hole.getY() &&
                        player.getY() < hole.getY() + hole.getHeight()) {
                        inHole = true;
                        break;
                    }
                }
            }

            if (inHole) {
                // Iniciar animación de caída
                player.startFallingAnimation();
                playerLives--;
                if (playerLives <= 0) {
                    gameOver = true;
                } else {
                    player.respawn();
                }
            }

            // Movimiento de la cámara
            cameraX = player.getX() - getWidth() / 2 + player.getWidth() / 2;
            if (cameraX < 0) cameraX = 0;
            if (cameraX > levelWidth - getWidth()) cameraX = levelWidth - getWidth();
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
    private int jumpsLeft = 2;
    private int width = 30;
    private int height = 50;
    private final int GRAVITY = 1;
    private final int JUMP_STRENGTH = -15;
    private int initialX, initialY;
    private boolean falling = false; // Añadido para la animación de caída

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
            jumpsLeft = 2;
        }
    }

    public void jump() {
        if (jumpsLeft > 0) {
            velY = JUMP_STRENGTH;
            jumpsLeft--;
        }
    }

    public void setVelX(int velX) {
        this.velX = velX;
    }

    public void startFallingAnimation() {
        falling = true;
    }

    public void stopFallingAnimation() {
        falling = false;
    }

    public void draw(Graphics g) {
        if (falling) {
            g.setColor(Color.RED); // Cambiar color para la animación de caída
        } else {
            g.setColor(Color.BLUE);
        }
        g.fillRect(x, y, width, height);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height; // Método agregado para obtener la altura del jugador
    }

    public int getVelY() {
        return velY;
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
            jumpsLeft = 2;
            stopFallingAnimation();
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
    }

    public void respawn() {
        x = initialX;
        y = initialY;
        velX = 0;
        velY = 0;
        jumpsLeft = 2;
        stopFallingAnimation(); // Detener animación de caída al respawn
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



class Flag {
    private int x, y;
    private int width = 20;
    private int height = 100;

    public Flag(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void draw(Graphics g) {
        g.setColor(Color.GREEN);
        g.fillRect(x, y, width, height);
        g.setColor(Color.WHITE);
        g.fillRect(x, y, 40, 20);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}