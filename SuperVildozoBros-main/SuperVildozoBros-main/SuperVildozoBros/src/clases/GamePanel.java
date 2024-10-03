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
        frame.setSize(1000, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    private Player player;
    private ArrayList<Enemy> enemies;
    private ArrayList<Platform> platforms;
    private ArrayList<Rectangle> holes;
    private ArrayList<Box> boxes;
    private Timer timer;
    private int cameraX = 0;
    private int playerLives = 10;
    private boolean gameOver = false;
    private Flag flag;
    private int levelWidth = 2500;
    private int groundLevel = 400;
    private int platformHeight = 20;
    private int currentLevel = 4;

     public GamePanel() {
        setFocusable(true);
        addKeyListener(this);
        player = new Player(50, groundLevel - 50);
        enemies = new ArrayList<>();
        platforms = new ArrayList<>();
        holes = new ArrayList<>();
        boxes = new ArrayList<>();

        // Create holes
        holes.add(new Rectangle(900, groundLevel - 200, 100, 200));

        flag = new Flag(levelWidth - 100, groundLevel - 180); // Move flag to the end of the level

        timer = new Timer(20, this);
        timer.start();
        resetLevel(); // Initialize the first level
    }

    private void createPlatforms(int currentLevel) {
        platforms.clear();
        switch (currentLevel) {
            case 1:
                levelWidth = 2000;
                flag = new Flag(levelWidth - 100, groundLevel - 180);
                platforms.add(new Platform(200, groundLevel - 100, 150, platformHeight));
                platforms.add(new Platform(400, groundLevel - 150, 150, platformHeight));
                platforms.add(new Platform(600, groundLevel - 200, 150, platformHeight));
                platforms.add(new Platform(800, groundLevel - 250, 150, platformHeight));
                platforms.add(new Platform(1000, groundLevel - 150, 150, platformHeight));
                platforms.add(new Platform(1200, groundLevel - 100, 150, platformHeight));
                platforms.add(new Platform(1450, groundLevel - 160, 150, platformHeight));
                platforms.add(new Platform(1650, groundLevel - 150, 220, platformHeight));
                break;

            case 2:
                platforms.add(new Platform(150, groundLevel - 100, 200, platformHeight));
                platforms.add(new Platform(400, groundLevel - 150, 150, platformHeight));
                platforms.add(new Platform(650, groundLevel - 180, 150, platformHeight));
                platforms.add(new Platform(850, groundLevel - 250, 150, platformHeight));
                platforms.add(new Platform(1150, groundLevel - 200, 200, platformHeight));
                platforms.add(new Platform(1450, groundLevel - 250, 180, platformHeight));
                platforms.add(new Platform(1750, groundLevel - 100, 150, platformHeight));
                platforms.add(new Platform(1950, groundLevel - 150, 150, platformHeight));
                platforms.add(new Platform(2150, groundLevel - 200, 150, platformHeight));
                platforms.add(new Platform(2350, groundLevel - 100, 150, platformHeight));
                break;

            case 3:
                platforms.add(new Platform(200, groundLevel - 100, 150, platformHeight));
                platforms.add(new Platform(550, groundLevel - 150, 150, platformHeight));
                platforms.add(new Platform(800, groundLevel - 100, 150, platformHeight));
                platforms.add(new Platform(300, groundLevel - 260, 150, platformHeight));
                platforms.add(new Platform(550, groundLevel - 300, 150, platformHeight));
                platforms.add(new Platform(850, groundLevel - 240, 150, platformHeight));
                platforms.add(new Platform(1300, groundLevel - 160, 150, platformHeight));
                platforms.add(new Platform(1750, groundLevel - 100, 150, platformHeight));
                platforms.add(new Platform(1400, groundLevel - 250, 150, platformHeight));
                platforms.add(new Platform(1600, groundLevel - 250, 150, platformHeight));

                break;

            case 4:
                platforms.add(new MovingPlatform(200, groundLevel - 100, 150, platformHeight, 200, 500)); // Límite entre 200 y 400
                platforms.add(new MovingPlatform(650, groundLevel - 150, 150, platformHeight, 650, 900)); // Límite entre 600 y 800
                platforms.add(new MovingPlatform(1000, groundLevel - 180, 150, platformHeight, 1000, 1300)); // Límite entre 1000 y 1200
                platforms.add(new MovingPlatform(1450, groundLevel - 170, 150, platformHeight, 1450, 1800)); // Límite entre 1400 y 1600
                platforms.add(new MovingPlatform(2000, groundLevel - 170, 150, platformHeight, 1450, 1800)); // Límite entre 1400 y 1600

                break;
        }
    }

    private void createBoxes(int currentLevel) {
        boxes.clear();
        if (currentLevel == 3) {
            // Asegúrate de que la posición Y sea correcta y visible
            boxes.add(new Box(300, groundLevel - 50, 50, 50)); // Ajusté la posición Y
            boxes.add(new Box(600, groundLevel - 50, 50, 50)); // Ajusté la posición Y
            boxes.add(new Box(900, groundLevel - 50, 50, 50)); // Ajusté la posición Y
            boxes.add(new Box(1200, groundLevel - 50, 50, 50)); // Ajusté la posición Y+
            boxes.add(new Box(1500, groundLevel - 50, 50, 50)); // Ajusté la posición Y
            boxes.add(new Box(1900, groundLevel - 50, 50, 50)); // Ajusté la posición Y
        }

        if(currentLevel == 4) {
            boxes.add(new Box(500, groundLevel - 50, 50, 50));
            boxes.add(new Box(925, groundLevel - 250, 50, 50));
            boxes.add(new Box(1350, groundLevel - 50, 50, 50));

        }
    }

    private void createEnemies(int currentLevel) {
        enemies.clear();
        switch(currentLevel) {
            case 1:
                enemies.add(new Enemy(250, groundLevel - 130, false));
                enemies.add(new Enemy(490, groundLevel - 180, false));
                enemies.add(new Enemy(860, groundLevel - 280, false));
                enemies.add(new Enemy(1010, groundLevel - 200, false));
                enemies.add(new Enemy(1110, groundLevel - 200, false));
                enemies.add(new Enemy(900, groundLevel - 130, false));
                enemies.add(new Enemy(900, groundLevel - 70, false));
                enemies.add(new Enemy(900, groundLevel - 30, false));
                enemies.add(new Enemy(1200, groundLevel - 70, false));
                enemies.add(new Enemy(1200, groundLevel - 30, false));
                enemies.add(new Enemy(1450, groundLevel - 110, false));
                enemies.add(new Enemy(1450, groundLevel - 70, false));
                enemies.add(new Enemy(1450, groundLevel - 30, false));
                enemies.add(new Enemy(1550, groundLevel - 190, false));
                break;
            case 2:
                enemies.add(new Enemy(200, groundLevel - 135, true));  // En la primera plataforma
                enemies.add(new Enemy(500, groundLevel - 185, true));  // En la segunda plataforma
                enemies.add(new Enemy(700, groundLevel - 215, true));  // En la tercera plataforma
                enemies.add(new Enemy(900, groundLevel - 285, true));
                enemies.add(new Enemy(900, groundLevel - 130, false));
                enemies.add(new Enemy(900, groundLevel - 70, false));
                enemies.add(new Enemy(900, groundLevel - 30, false));  // En la cuarta plataforma
                enemies.add(new Enemy(1200, groundLevel - 235, true)); // En la quinta plataforma
                enemies.add(new Enemy(1500, groundLevel - 285, true)); // En la sexta plataforma
                enemies.add(new Enemy(1800, groundLevel - 135, true)); // En la séptima plataforma
                enemies.add(new Enemy(2000, groundLevel - 185, true)); // En la octava plataforma
                enemies.add(new Enemy(2200, groundLevel - 235, true)); // En la novena plataforma
                enemies.add(new Enemy(2400, groundLevel - 135, true));
                break;

            case 3:
                enemies.add(new Enemy(210, groundLevel - 135, true));  // En la primera plataforma
                enemies.add(new Enemy(550, groundLevel - 185, true));  // En la segunda plataforma
                enemies.add(new Enemy(900, groundLevel - 285, true));
                enemies.add(new Enemy(400, groundLevel - 30, true));
                enemies.add(new Enemy(700, groundLevel - 30, true));
                enemies.add(new Enemy(600, groundLevel - 330, true));
                enemies.add(new Enemy(900, groundLevel - 130, false));
                enemies.add(new Enemy(900, groundLevel - 70, false));
                enemies.add(new Enemy(900, groundLevel - 30, false));
                enemies.add(new Enemy(1100, groundLevel - 30, true));
                enemies.add(new Enemy(1300, groundLevel - 30, true));
                enemies.add(new Enemy(1600, groundLevel - 30, true));
                enemies.add(new Enemy(1700, groundLevel - 130, true));
                enemies.add(new Enemy(2000, groundLevel - 30, true));
                enemies.add(new Enemy(2300, groundLevel - 30, true));
                enemies.add(new Enemy(2400, groundLevel - 30, true));
                break;
                //Los que la X vale 900 son los que estan adentro del rectangulo
            case 4:

                //En plataformas
                enemies.add(new Enemy(200, groundLevel - 135, true));
                enemies.add(new Enemy(650, groundLevel - 185, true));  // En la segunda plataforma
                enemies.add(new Enemy(1000, groundLevel - 210, true));  // En la segunda plataforma
                enemies.add(new Enemy(1450, groundLevel - 200, true));  // En la segunda plataforma
                enemies.add(new Enemy(1455, groundLevel - 200, true));  // En la segunda plataforma
                enemies.add(new Enemy(2000, groundLevel - 150, true));  // En la segunda plataforma



                //Piso
                enemies.add(new Enemy(2200, groundLevel - 30, true));  // En la segunda plataforma
                enemies.add(new Enemy(2150, groundLevel - 30, true));  // En la segunda plataforma
                enemies.add(new Enemy(2100, groundLevel - 30, true));  // En la segunda plataforma
                enemies.add(new Enemy(2050, groundLevel - 30, true));  // En la segunda plataforma
                enemies.add(new Enemy(2000, groundLevel - 30, true));  // En la segunda plataforma
                enemies.add(new Enemy(1950, groundLevel - 30, true));  // En la segunda plataforma
                enemies.add(new Enemy(1900, groundLevel - 30, true));  // En la segunda plataforma
                enemies.add(new Enemy(1850, groundLevel - 30, true));  // En la segunda plataforma
                enemies.add(new Enemy(1800, groundLevel - 30, true));  // En la segunda plataforma
                enemies.add(new Enemy(1750, groundLevel - 30, true));  // En la segunda plataforma
                enemies.add(new Enemy(1700, groundLevel - 30, true));  // En la segunda plataforma
                enemies.add(new Enemy(1650, groundLevel - 30, true));  // En la segunda plataforma
                enemies.add(new Enemy(1600, groundLevel - 30, true));  // En la segunda plataforma
                enemies.add(new Enemy(1550, groundLevel - 30, true));  // En la segunda plataforma
                enemies.add(new Enemy(1500, groundLevel - 30, true));  // En la segunda plataforma
                enemies.add(new Enemy(1450, groundLevel - 30, true));  // En la segunda plataforma
                enemies.add(new Enemy(1400, groundLevel - 30, true));  // En la segunda plataforma
                enemies.add(new Enemy(1350, groundLevel - 30, true));  // En la segunda plataforma
                enemies.add(new Enemy(1300, groundLevel - 30, true));  // En la segunda plataforma
                enemies.add(new Enemy(1250, groundLevel - 30, true));  // En la segunda plataforma
                enemies.add(new Enemy(1200, groundLevel - 30, true));  // En la segunda plataforma
                enemies.add(new Enemy(1150, groundLevel - 30, true));  // En la segunda plataforma
                enemies.add(new Enemy(1100, groundLevel - 30, true));  // En la segunda plataforma
                enemies.add(new Enemy(1050, groundLevel - 30, true));  // En la segunda plataforma
                enemies.add(new Enemy(1000, groundLevel - 30, true));  // En la segunda plataforma
                enemies.add(new Enemy(950, groundLevel - 30, true));  // En la segunda plataforma
                enemies.add(new Enemy(900, groundLevel - 30, true));  // En la segunda plataforma
                enemies.add(new Enemy(850, groundLevel - 30, true));  // En la segunda plataforma
                enemies.add(new Enemy(800, groundLevel - 30, true));  // En la segunda plataforma
                enemies.add(new Enemy(500, groundLevel - 30, true));  // En la segunda plataforma
              










        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, getWidth(), getHeight());

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

        g.setColor(Color.BLACK);
        g.drawString("Lives: " + playerLives, 20, 20);

        if (gameOver) {
            g.drawString("Game Over", getWidth() / 2 - 40, getHeight() / 2);
            return;
        }

        g.translate(-cameraX, 0);
        player.draw(g);
        for (Enemy enemy : enemies) {
            enemy.draw(g);
        }
        for (Platform platform : platforms) {
            platform.draw(g);
        }
        for (Box box : boxes) {
            box.draw(g);
        }


        for (Rectangle hole : holes) {
            g.setColor(Color.BLACK);
            g.fillRect(hole.x, hole.y, hole.width, hole.height);
        }
        flag.draw(g);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            player.update();

            boolean playerHit = false;

            for (Enemy enemy : enemies) {
                enemy.update(platforms, boxes, levelWidth);
                if (player.intersects(enemy.getBounds())) {
                    playerHit = true;
                    break;
                }
            }

            if (playerHit) {
                playerLives--;
                player.respawn();
                if (playerLives <= 0) {
                    gameOver = true;
                }
            }

            for (Platform platform : platforms) {
                if (player.intersects(platform.getBounds())) {
                    player.handleCollision(platform.getBounds(), "platform");
                }
            }

            boolean inHole = false;
            for (Rectangle hole : holes) {
                if (player.intersects(hole) && player.getVelY() > 0) {
                    inHole = false; // No lose lives for falling into the hole
                }
            }

            // Actualizar plataformas
            for (Platform platform : platforms) {
                if (platform instanceof MovingPlatform) {
                    ((MovingPlatform) platform).update(); // Llamar el método update de la plataforma móvil
                }
            }


            if (player.intersects(flag.getBounds())) {
                currentLevel++;
                if (currentLevel > 5) {
                    gameOver = true; // Completed all levels
                } else {
                    resetLevel();
                }
            }

            // Añadir colisión con cajas
            for (Box box : boxes) {
                if (player.intersects(box.getBounds())) {
                    player.handleCollision(box.getBounds(), "box");
                }
            }

            cameraX = player.getX() - getWidth() / 2 + player.getWidth() / 2;
            if (cameraX < 0) cameraX = 0;
            if (cameraX > levelWidth - getWidth()) cameraX = levelWidth - getWidth();
        }
        repaint();
    }

    private void resetLevel() {
        player.respawn();
        createPlatforms(currentLevel);
        createEnemies(currentLevel);
        createBoxes(currentLevel); // Asegúrate de que se llamen al reiniciar el nivel
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (gameOver && e.getKeyCode() == KeyEvent.VK_ENTER) {
            resetGame();
        } else {
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
    }

    private void resetGame() {
        playerLives = 3;
        currentLevel = 1;
        gameOver = false;
        player.respawn();
        resetLevel();
        cameraX = 0;
        repaint();
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
    private int jumpsLeft = 0;
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

        // Limitar el jugador al suelo
        if (y > 350) {
            y = 350;
            velY = 0;
            jumpsLeft = 0; // Reiniciar saltos cuando el jugador toca el suelo
        }
    }

    public void jump() {
        if (jumpsLeft == 0) { // Permitir solo un salto
            velY = JUMP_STRENGTH;
            jumpsLeft++;
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

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getVelY() {
        return velY;
    }

    public boolean intersects(Rectangle r) {
        return new Rectangle(x, y, width, height).intersects(r);
    }

    // Método para manejar colisiones con plataformas y cajas
    public void handleCollision(Rectangle bounds, String type) {
        // Colisiones con plataformas
        if (type.equals("platform")) {
            if (y + height - velY <= bounds.y) {
                // Colisión por arriba de la plataforma
                y = bounds.y - height;
                velY = 0;
                jumpsLeft = 0;
            } else if (y + height > bounds.y && y < bounds.y + bounds.height && velY < 0) {
                // Colisión por debajo de la plataforma
                y = bounds.y + bounds.height;
                velY = 0;
            }
        }

        // Colisiones con cajas
        if (type.equals("box")) {
            // Colisión por arriba de la caja
            if (y + height - velY <= bounds.y) {
                y = bounds.y - height;
                velY = 0;
                jumpsLeft = 0;
            }
            // Colisión por la izquierda o derecha de la caja
            else if (x + width > bounds.x && x < bounds.x + bounds.width) {
                // Colisión por la izquierda
                if (x + width - velX <= bounds.x) {
                    x = bounds.x - width;
                }
                // Colisión por la derecha
                else if (x - velX >= bounds.x + bounds.width) {
                    x = bounds.x + bounds.width;
                }
            }
        }
    }

    // Método para reubicar al jugador en su posición inicial
    public void respawn() {
        x = initialX;
        y = initialY;
    }
}


class Enemy {
    private int x, y;
    private int width = 30;
    private int height = 30;
    private boolean isMoving;
    private int moveDirection = 1; // 1 para derecha, -1 para izquierda
    private int moveSpeed = 2;
    private Platform currentPlatform;

    public Enemy(int x, int y, boolean isMoving) {
        this.x = x;
        this.y = y;
        this.isMoving = isMoving;
    }

    public void update(ArrayList<Platform> platforms, ArrayList<Box> boxes, int levelWidth) {
        if (isMoving) {
            if (currentPlatform == null) {
                findPlatform(platforms);
            }

            if (currentPlatform != null) {
                // Mover solo dentro de los límites de la plataforma
                x += moveDirection * moveSpeed;

                // Verificar si el enemigo llegó al borde de la plataforma
                if (x <= currentPlatform.getBounds().x ||
                        x + width >= currentPlatform.getBounds().x + currentPlatform.getBounds().width) {
                    moveDirection *= -1;
                    x = Math.max(currentPlatform.getBounds().x,
                            Math.min(x, currentPlatform.getBounds().x + currentPlatform.getBounds().width - width));
                }

                // Mantener el enemigo sobre la plataforma
                y = currentPlatform.getBounds().y - height;
            } else {
                // Si no está en una plataforma, mover libremente y verificar colisiones con los bordes del nivel
                x += moveDirection * moveSpeed;
                if (x <= 0 || x + width >= levelWidth) {
                    moveDirection *= -1;
                }
            }

            // Verificar colisiones con las cajas
            for (Box box : boxes) {
                if (getBounds().intersects(box.getBounds())) {
                    moveDirection *= -1; // Cambiar dirección
                    break; // Salir del bucle al colisionar con una caja
                }
            }
        }
    }

    private void findPlatform(ArrayList<Platform> platforms) {
        for (Platform platform : platforms) {
            if (x >= platform.getBounds().x &&
                    x + width <= platform.getBounds().x + platform.getBounds().width &&
                    y + height <= platform.getBounds().y + 5 &&
                    y + height >= platform.getBounds().y - 5) {
                currentPlatform = platform;
                y = platform.getBounds().y - height;
                break;
            }
        }
    }

    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(x, y, width, height);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}


class Flag {
    private int x, y;
    private int width = 30;
    private int height = 60;

    public Flag(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void draw(Graphics g) {
        g.setColor(Color.GREEN);
        g.fillRect(x, y, width, height);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}

class Platform {
    protected int x, y, width, height;

    public Platform(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void update() {
        // Método vacío, puede ser sobrescrito en subclases
    }

    public void draw(Graphics g) {
        g.setColor(Color.GRAY);
        g.fillRect(x, y, width, height);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}

class MovingPlatform extends Platform {
    private int moveDirection = 1; // 1 para mover a la derecha, -1 para izquierda
    private int speed = 1;
    private int minX; // Límite izquierdo
    private int maxX; // Límite derecho

    // Modificar el constructor para incluir los límites
    public MovingPlatform(int x, int y, int width, int height, int minX, int maxX) {
        super(x, y, width, height);
        this.minX = minX;
        this.maxX = maxX;
    }

    @Override
    public void update() {
        // Mueve la plataforma
        x += moveDirection * speed;

        // Cambiar dirección si toca los bordes del rango
        if (x < minX || x + width > maxX) {
            moveDirection *= -1; // Cambiar dirección
        }
    }
}





class Box {
    private int x, y, width, height;

    public Box(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void draw(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillRect(x, y, width, height);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}