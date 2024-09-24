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
    private int playerLives = 3;
    private boolean gameOver = false;
    private Flag flag;
    private int levelWidth = 2500;
    private int groundLevel = 400;
    private int platformHeight = 20;
    private int currentLevel = 3;

    public GamePanel() {
        setFocusable(true);
        addKeyListener(this);
        player = new Player(50, groundLevel - 50);
        enemies = new ArrayList<>();
        platforms = new ArrayList<>();
        holes = new ArrayList<>();
        boxes = new ArrayList<>();
        createBoxes(currentLevel);

        // Crear plataformas con una mejor distribución
        createPlatforms(currentLevel);
        
        // Crear enemigos en posiciones específicas
        createEnemies(currentLevel);

        // Crear agujeros
        holes.add(new Rectangle(900, groundLevel - 200, 100, 200));

        flag = new Flag(levelWidth - 100, groundLevel - 180); // Mover la bandera al final del nivel

        timer = new Timer(20, this);
        timer.start();
    }
    
    private void createBoxes(int currentLevel) {
        boxes.clear(); // Limpiar cajas antes de añadir nuevas
        if (currentLevel == 3) {
            boxes.add(new Box(150, groundLevel - 50, 50, 50)); // Cajas en diferentes posiciones del suelo
            boxes.add(new Box(300, groundLevel - 50, 50, 50));
            boxes.add(new Box(600, groundLevel - 50, 50, 50));
            boxes.add(new Box(900, groundLevel - 50, 50, 50));
            boxes.add(new Box(1200, groundLevel - 50, 50, 50));
            boxes.add(new Box(1500, groundLevel - 50, 50, 50));
            boxes.add(new Box(1900, groundLevel - 50, 50, 50));
        }
    }

    private void createPlatforms(int currentLevel) {
        platforms.clear(); // Limpiar plataformas antes de añadir nuevas

        switch (currentLevel) {
            case 1:
                platforms.add(new Platform(200, groundLevel - 100, 150, platformHeight));
                platforms.add(new Platform(400, groundLevel - 150, 150, platformHeight));
                platforms.add(new Platform(600, groundLevel - 200, 150, platformHeight));
                platforms.add(new Platform(800, groundLevel - 250, 150, platformHeight));
                platforms.add(new Platform(1000, groundLevel - 150, 150, platformHeight));
                platforms.add(new Platform(1200, groundLevel - 100, 150, platformHeight));
                break;
            case 2:
            	platforms.add(new Platform(150, groundLevel - 100, 200, platformHeight)); 
                platforms.add(new Platform(400, groundLevel - 150, 150, platformHeight)); // Distancia más corta
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
                platforms.add(new Platform(100, groundLevel - 120, 150, platformHeight));
                platforms.add(new Platform(300, groundLevel - 150, 150, platformHeight));
                platforms.add(new Platform(1100, groundLevel - 120, 150, platformHeight));
                platforms.add(new Platform(1300, groundLevel - 160, 150, platformHeight));
                platforms.add(new Platform(1500, groundLevel - 180, 150, platformHeight));
                platforms.add(new Platform(1750, groundLevel - 100, 150, platformHeight));
                break;

            case 4:
                platforms.add(new Platform(300, groundLevel - 150, 150, platformHeight));
                platforms.add(new Platform(500, groundLevel - 200, 150, platformHeight));
                platforms.add(new Platform(700, groundLevel - 250, 150, platformHeight));
                platforms.add(new Platform(900, groundLevel - 300, 150, platformHeight));
                break;
            case 5:
                platforms.add(new Platform(100, groundLevel - 100, 150, platformHeight));
                platforms.add(new Platform(300, groundLevel - 150, 150, platformHeight));
                platforms.add(new Platform(500, groundLevel - 200, 150, platformHeight));
                platforms.add(new Platform(700, groundLevel - 250, 150, platformHeight));
                platforms.add(new Platform(900, groundLevel - 300, 150, platformHeight));
                break;
        }
    }

    private void createEnemies(int currentLevel) {
        enemies.clear(); 

        switch (currentLevel) {
            case 1:
                enemies.add(new Enemy(200, groundLevel - 150));
                enemies.add(new Enemy(500, groundLevel - 200));
                enemies.add(new Enemy(850, groundLevel - 300));
                enemies.add(new Enemy(1050, groundLevel - 300));
                break;
            case 2:
            	enemies.add(new Enemy(180, groundLevel - 150));  // Enemigo en la primera plataforma
                enemies.add(new Enemy(600, groundLevel - 200));  // Enemigo en plataforma alta
                enemies.add(new Enemy(400, groundLevel - 50));  
                enemies.add(new Enemy(1100, groundLevel - 50)); 
                enemies.add(new Enemy(1300, groundLevel - 50));// Enemigo en el suelo
                enemies.add(new Enemy(1200, groundLevel - 220)); // Enemigo en una plataforma elevada
                enemies.add(new Enemy(1700, groundLevel - 100)); // Enemigo al final del nivel
                enemies.add(new Enemy(2000, groundLevel - 50)); // Enemigo en una plataforma elevada
                enemies.add(new Enemy(1990, groundLevel - 200));
                enemies.add(new Enemy(2300, groundLevel - 50));
                enemies.add(new Enemy(2300, groundLevel - 50));
                break;
            case 3:
                enemies.add(new Enemy(200, groundLevel - 50));  // Enemigos en el suelo
                enemies.add(new Enemy(400, groundLevel - 50));
                enemies.add(new Enemy(700, groundLevel - 50));
                enemies.add(new Enemy(1300, groundLevel - 50));
                enemies.add(new Enemy(1600, groundLevel - 50));
                enemies.add(new Enemy(2000, groundLevel - 50));
                enemies.add(new Enemy(2300, groundLevel - 50));
                enemies.add(new Enemy(2400, groundLevel - 50));
                break;

            case 4:
                enemies.add(new Enemy(150, groundLevel - 170));
                enemies.add(new Enemy(450, groundLevel - 180));
                enemies.add(new Enemy(750, groundLevel - 200));
                break;
            case 5:
                enemies.add(new Enemy(100, groundLevel - 150));
                enemies.add(new Enemy(300, groundLevel - 160));
                enemies.add(new Enemy(500, groundLevel - 180));
                enemies.add(new Enemy(700, groundLevel - 150));
                break;
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
        g.drawString("Vidas: " + playerLives, 20, 20);

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

            boolean collisionDetected = false;

            // Actualizar enemigos y verificar colisiones con cajas
            for (Enemy enemy : enemies) {
                enemy.update(platforms, boxes); // Actualizar con cajas también

                if (player.intersects(enemy.getBounds())) {
                    playerLives--;
                    player.respawn();
                    resetEnemies(); // Reiniciar la posición de los enemigos
                    if (playerLives <= 0) {
                        gameOver = true;
                    }
                    collisionDetected = true;
                    break;
                }
            }
            
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

            boolean inHole = false;
            for (Rectangle hole : holes) {
                if (player.intersects(hole) && player.getVelY() > 0) {
                    // Permitir pasar el agujero negro
                    inHole = false; // No resta vidas por caer en el agujero
                }
            }

            if (player.intersects(flag.getBounds())) {
            	 System.out.println("avanzo");
                currentLevel++;
                if (currentLevel > 5) {
                    System.out.println("Has completado todos los niveles!");
                    gameOver = true;
                } else {
                    resetLevel();
                }
            }

            cameraX = player.getX() - getWidth() / 2 + player.getWidth() / 2;
            if (cameraX < 0) cameraX = 0;
            if (cameraX > levelWidth - getWidth()) cameraX = levelWidth - getWidth();
        }
        repaint();
    }
    
 // Método para reiniciar la posición de los enemigos
    private void resetEnemies() {
        createEnemies(currentLevel);  // Reiniciar los enemigos a su posición original
    }

    private void resetLevel() {
        player.respawn();
        levelWidth = 2500;
        createPlatforms(currentLevel);

        System.out.println("Avanzaste al nivel " + currentLevel);
    }

 // En la clase GamePanel

    @Override
    public void keyPressed(KeyEvent e) {
        if (gameOver && e.getKeyCode() == KeyEvent.VK_ENTER) {
            resetGame(); // Llama al método para reiniciar el juego
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

    // Método para reiniciar el juego
    private void resetGame() {
        playerLives = 3; 
        currentLevel = 1;
        gameOver = false; 
        player.respawn(); 
        createPlatforms(currentLevel); 
        createEnemies(currentLevel); 
        holes.clear(); 
        holes.add(new Rectangle(900, groundLevel - 200, 100, 200)); 
        flag = new Flag(levelWidth - 800, groundLevel - 180);
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

        if (y > 350) {
            y = 350;
            velY = 0;
            jumpsLeft = 0; // Restablecer saltos cuando el jugador toca el suelo
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

    public void handleCollision(Rectangle r, String type) {
        Rectangle playerBounds = new Rectangle(x, y, width, height);

        // Colisión desde arriba
        if (velY > 0 && playerBounds.getMaxY() > r.getY() && playerBounds.getMaxY() - r.getY() <= velY + 5) {
            y = (int) r.getY() - height;
            velY = 0;
            jumpsLeft = 0; // Solo un salto permitido
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

    public void respawn() {
        x = initialX;
        y = initialY;
        velY = 0;
    }
}

class Enemy {
    private int x, y;
    private int size = 50;
    private int velX = 2; // Velocidad horizontal
    private boolean movingRight = true;

    public Enemy(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void update(ArrayList<Platform> platforms, ArrayList<Box> boxes) {
        // Movimiento horizontal
        if (movingRight) {
            x += velX;
        } else {
            x -= velX;
        }

        // Colisiones con plataformas
        boolean onPlatform = false;
        for (Platform platform : platforms) {
            Rectangle platformBounds = platform.getBounds();
            if (x + size > platformBounds.x && x < platformBounds.x + platformBounds.width &&
                y + size == platformBounds.y) {
                onPlatform = true;
                if (x <= platformBounds.x || x + size >= platformBounds.x + platformBounds.width) {
                    movingRight = !movingRight;
                }
                break;
            }
        }

        // Colisiones con cajas
        for (Box box : boxes) {
            if (new Rectangle(x, y, size, size).intersects(box.getBounds())) {
                movingRight = !movingRight; // Cambiar de dirección al colisionar con una caja
            }
        }

        // Si no está en plataforma, moverse por el suelo
        if (!onPlatform) {
            if (x <= 0 || x + size >= 2500) { // Limites del nivel
                movingRight = !movingRight;
            }
        }
    }


    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(x, y, size, size);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, size, size);
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

    public int getWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getX() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void draw(Graphics g) {
        g.setColor(Color.GREEN);
        g.fillRect(x, y, width, height);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
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
        g.setColor(Color.ORANGE);
        g.fillRect(x, y, width, height);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}


class Flag {
    private int x, y;

    public Flag(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void draw(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillRect(x, y, 10, 30);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, 10, 30);
    }
}