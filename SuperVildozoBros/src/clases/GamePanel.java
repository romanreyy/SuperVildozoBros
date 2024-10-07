package clases;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;

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
    private Boss boss;
    private Coin specialCoin;
    private boolean gameWon = false;
    private boolean bossDefeated = false;
    private Timer timer;
    private int cameraX = 0;
    private int playerLives = 3;
    private boolean gameOver = false;
    private Flag flag;
    private int levelWidth = 2500;
    private int groundLevel = 400;
    private int platformHeight = 20;
    private int currentLevel = 1;

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
            platforms.add(new MovingPlatform(200, groundLevel - 100, 150, platformHeight, 200, 450)); // Límite entre 200 y 400
            platforms.add(new MovingPlatform(650, groundLevel - 150, 150, platformHeight, 550, 900)); // Límite entre 600 y 800
            platforms.add(new MovingPlatform(1000, groundLevel - 180, 150, platformHeight, 1000, 1300)); // Límite entre 1000 y 1200
            platforms.add(new MovingPlatform(1450, groundLevel - 170, 150, platformHeight, 1450, 1800)); // Límite entre 1400 y 1600
            platforms.add(new MovingPlatform(2000, groundLevel - 170, 150, platformHeight, 1450, 1800)); // Límite entre 1400 y 1600

                break;
               
            case 5:
            levelWidth = 2000;
                flag = new Flag(2000, groundLevel - 180);
                // Plataformas para que el jugador pueda saltar y esquivar disparos
                platforms.add(new Platform(200, groundLevel - 100, 150, platformHeight));
                platforms.add(new Platform(550, groundLevel - 150, 150, platformHeight));
                platforms.add(new Platform(800, groundLevel - 100, 150, platformHeight));
                platforms.add(new Platform(300, groundLevel - 260, 150, platformHeight));
                platforms.add(new Platform(550, groundLevel - 300, 150, platformHeight));
                platforms.add(new Platform(880, groundLevel - 240, 150, platformHeight));
                platforms.add(new Platform(1200, groundLevel - 200, 300, platformHeight)); // Plataforma alta
                platforms.add(new Platform(1500, groundLevel - 250, 200, platformHeight)); // Otra plataforma alta para saltar

                boss = new Boss(1200, groundLevel - 200); // Ajustado la posición vertical del jefe
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
       
        if(currentLevel == 5) {
        boxes.add(new Box(300, groundLevel - 50, 50, 50)); // Ajusté la posición Y
            boxes.add(new Box(600, groundLevel - 50, 50, 50)); // Ajusté la posición Y
            boxes.add(new Box(900, groundLevel - 50, 50, 50)); // Ajusté la posición Y
            boxes.add(new Box(1200, groundLevel - 50, 50, 50)); // Ajusté la posición Y+
            boxes.add(new Box(1500, groundLevel - 50, 50, 50)); // Ajusté la posición Y
            boxes.add(new Box(1900, groundLevel - 50, 50, 50)); // Ajusté la posición Y
            boxes.add(new Box(1250, groundLevel - 250, 50, 50)); // Ajusté la posición Y+
            boxes.add(new Box(1560, groundLevel - 300, 50, 50)); // Ajusté la posición Y+
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
                enemies.add(new Enemy(1450, groundLevel - 200, true));  // En la segunda plataforma
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
                enemies.add(new Enemy(1400, groundLevel - 30, true));  // En la segunda plataforma
                enemies.add(new Enemy(1200, groundLevel - 30, true));  // En la segunda plataforma
                enemies.add(new Enemy(1100, groundLevel - 30, true));  // En la segunda plataforma
                enemies.add(new Enemy(950, groundLevel - 30, true));  // En la segunda plataforma
                enemies.add(new Enemy(800, groundLevel - 30, true));  // En la segunda plataforma
                enemies.add(new Enemy(500, groundLevel - 30, true));  // En la segunda plataforma

                break;
               
            case 5:
            enemies.add(new Enemy(210, groundLevel - 135, true));  // En la primera plataforma
                enemies.add(new Enemy(550, groundLevel - 185, true));  // En la segunda plataforma
                enemies.add(new Enemy(400, groundLevel - 30, true));
                enemies.add(new Enemy(700, groundLevel - 30, true));
                enemies.add(new Enemy(600, groundLevel - 330, true));
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
       
        if (currentLevel == 5 && !bossDefeated) {
            boss.draw(g);
        }

        for (Rectangle hole : holes) {
            g.setColor(Color.BLACK);
            g.fillRect(hole.x, hole.y, hole.width, hole.height);
        }
       
        if (currentLevel == 5 && !specialCoin.isCollected()) {
            specialCoin.draw(g);
        }
        player.drawBullets(g);
        flag.draw(g);
       
        if (gameWon) {
            g.setColor(Color.GREEN);
            g.setFont(new Font("Arial", Font.BOLD, 50));
            g.drawString("You Win!", getWidth() / 2 - 100, getHeight() / 2);
        } else if (gameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 50));
            g.drawString("Game Over", getWidth() / 2 - 100, getHeight() / 2);
        }

    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
        // Actualizar la posición del jugador y los disparos
            player.update(boss, boxes, specialCoin);
            player.updateBullets();

            // Actualizar el jefe si estamos en el nivel 5 y no ha sido derrotado
            if (currentLevel == 5 && !bossDefeated) {
                boss.update(player, boxes);  // Añadido: actualizar el jefe
            }

            // Manejo de colisiones con el jefe
            if (currentLevel == 5 && !bossDefeated) {
                handleBossCollisions();
            }

            // Manejo de colisiones con enemigos
            handleEnemyCollisions();

            // Manejo de colisiones con plataformas
            handlePlatformCollisions();

            // Manejo de colisiones con la bandera
            handleFlagCollision();

            // Actualizar plataformas móviles
            updateMovingPlatforms();

            // Manejo de agujeros (holes)
            handleHoles();

            // Actualizar posición de la cámara
            updateCameraPosition();
        }
        repaint();
    }

    // Método separado para manejar colisiones con el jefe
    private void handleBossCollisions() {
        for (Projectile p : boss.getProjectiles()) {
            if (player.intersects(p.getBounds())) {
                playerHit();
            }
        }
        for (SpecialAttack sa : boss.getSpecialAttacks()) {
            if (player.intersects(sa.getBounds())) {
                playerHit();
            }
        }
        if (player.intersects(boss.getBounds())) {
            playerHit();
        }
        for (Bullet bullet : player.getBullets()) {
            if (boss.intersects(bullet.getBounds())) {
                boss.damageBoss();
                player.getBullets().remove(bullet);
                if (boss.isDead()) {
                    bossDefeated = true;
                    System.out.println("¡Jefe derrotado!"); // Para depuración
                }
                break; // Salir después de golpear al jefe
            }
        }
    }

    // Método para manejar colisiones con enemigos
    private void handleEnemyCollisions() {
        boolean playerHit = false;
        for (Enemy enemy : enemies) {
            enemy.update(platforms, boxes, levelWidth);
            if (player.intersects(enemy.getBounds())) {
                playerHit = true;
                break; // Salir si el jugador fue golpeado
            }
        }

        if (playerHit) {
            playerLives--;
            player.respawn();
            if (playerLives <= 0) {
                gameOver = true; // Fin del juego si no hay vidas
            }
        }
    }

    // Método para manejar colisiones con plataformas
    private void handlePlatformCollisions() {
        for (Platform platform : platforms) {
            if (player.intersects(platform.getBounds())) {
                player.handleCollision(platform.getBounds(), "platform");
            }
        }
    }

 // Método para manejar colisiones con la bandera
    private void handleFlagCollision() {
        if (player.intersects(flag.getBounds())) {
            // Verifica si el jefe ha sido derrotado en el nivel 5
            if (currentLevel == 5 && bossDefeated) {
                currentLevel++;
                gameWon = true; // El jugador ganó el juego
                System.out.println("¡Juego ganado!"); // Mensaje de victoria
            } else if (currentLevel < 5) {
                // Si no es el nivel 5, simplemente avanza al siguiente nivel
                currentLevel++;
                resetLevel(); // Reinicia el nivel actual
            }
        }
    }


    // Método para actualizar plataformas móviles
    private void updateMovingPlatforms() {
        for (Platform platform : platforms) {
            if (platform instanceof MovingPlatform) {
                ((MovingPlatform) platform).update(); // Llamar el método update de la plataforma móvil
            }
        }
    }

    // Método para manejar agujeros
    private void handleHoles() {
        boolean inHole = false;
        for (Rectangle hole : holes) {
            if (player.intersects(hole) && player.getVelY() > 0) {
                inHole = true; // No pierde vidas al caer en el agujero
            }
        }
    }

    // Método para actualizar la posición de la cámara
    private void updateCameraPosition() {
        cameraX = player.getX() - getWidth() / 2 + player.getWidth() / 2;
        if (cameraX < 0) cameraX = 0;
        if (cameraX > levelWidth - getWidth()) cameraX = levelWidth - getWidth();
    }


   
    private void playerHit() {
        playerLives--;
        player.respawn();
        if (playerLives <= 0) {
            gameOver = true;
        }
    }

    private void resetLevel() {
        player.respawn();
        createPlatforms(currentLevel);
        createEnemies(currentLevel);
        createBoxes(currentLevel); // Asegúrate de que se llamen al reiniciar el nivel
        if (currentLevel == 5) {
            boss = new Boss(levelWidth - 300, groundLevel - 200); // Ajustado la posición vertical
            bossDefeated = false;
        }
        if (currentLevel == 5) {
            specialCoin = new Coin(levelWidth / 4, groundLevel - 100);
        }
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
            if (e.getKeyCode() == KeyEvent.VK_X) {
                player.shoot();
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
    public static final boolean flag = false;
    private int x, y;
    private int velX = 0;
    private int velY = 0;
    private int jumpsLeft = 0;
    private int width = 30;
    private int height = 50;
    private final int GRAVITY = 1;
    private final int JUMP_STRENGTH = -15;
    private int initialX, initialY;
    private boolean canShoot = false;
    private ArrayList<Bullet> bullets = new ArrayList<>();

    public Player(int x, int y) {
        this.x = x;
        this.y = y;
        this.initialX = x;
        this.initialY = y;
    }

    public void update(Boss boss, ArrayList<Box> boxes, Coin coin) {
        x += velX;
        y += velY;

        velY += GRAVITY;

        // Limitar el jugador al suelo
        if (y > 350) {
            y = 350;
            velY = 0;
            jumpsLeft = 0;
        }

        // Verificar colisiones con las balas
        for (int i = bullets.size() - 1; i >= 0; i--) {
            Bullet bullet = bullets.get(i);
           
            // Verificar colisión con el jefe
            if (boss.intersects(bullet.getBounds())) {
                boss.damageBoss();
                bullets.remove(i);
                continue;
            }

            // Verificar colisión con las cajas
            for (Box box : boxes) {
                if (bullet.getBounds().intersects(box.getBounds())) {
                    bullets.remove(i);
                    break;
                }
            }
        }

        // Verificar colisiones con las cajas
        for (Box box : boxes) {
            if (this.intersects(box.getBounds())) {
                handleCollision(box.getBounds(), "box");
            }
        }

        // Verificar colisión con la moneda
        if (coin != null && !coin.isCollected() && this.intersects(coin.getBounds())) {
            coin.collect();         // Recoger la moneda
            enableShooting();       // Habilitar disparo
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
   
    public void enableShooting() {
        canShoot = true;
    }

    public void shoot() {
        if (canShoot) {
            bullets.add(new Bullet(x + width, y + height / 2, 10));
        }
    }

    public void updateBullets() {
        for (int i = bullets.size() - 1; i >= 0; i--) {
            Bullet bullet = bullets.get(i);
            bullet.update();
            if (bullet.isOutOfBounds()) {
                bullets.remove(i);
            }
        }
    }

    public void drawBullets(Graphics g) {
        for (Bullet bullet : bullets) {
            bullet.draw(g);
        }
    }

    public ArrayList<Bullet> getBullets() {
        return bullets;
    }

    public boolean intersects(Rectangle r) {
        return new Rectangle(x, y, width, height).intersects(r);
    }
   
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
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
                jumpsLeft = 0; // Reiniciar saltos
            }
            // Colisión por la izquierda o derecha de la caja
            else if (x + width > bounds.x && x < bounds.x + bounds.width) {
                // Colisión por la izquierda
                if (x + width - velX <= bounds.x) {
                    x = bounds.x - width; // Ajustar posición
                }
                // Colisión por la derecha
                else if (x - velX >= bounds.x + bounds.width) {
                    x = bounds.x + bounds.width; // Ajustar posición
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


class Bullet {
    private int x, y;
    private int speed = 10;
    private int size = 10;

    public Bullet(int x, int y, int size) {
        this.x = x;
        this.y = y;
        this.size = size;
    }

    public void update() {
        x += speed;
    }

    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillOval(x, y, size, size);
    }

    public boolean isOutOfBounds() {
        return x > 3000;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, size, size);
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

class Boss {
    private int x, y;
    private int width = 150;
    private int height = 150;
    int health = 30; // Cambiado a 25 para representar los 25 disparos necesarios
    private int shootCooldown = 0;
    private int specialAttackCooldown = 0;
    private ArrayList<Projectile> projectiles;
    private ArrayList<SpecialAttack> specialAttacks;
    private Random random;
    private boolean playerVisible = false;
    private boolean isAlive = true; // Estado de vida del jefe


    // Nuevas variables para el movimiento vertical
    private int initialY;
    private int moveDirection = 1; // 1 para abajo, -1 para arriba
    private int moveSpeed = 2;
    private int moveRange = 100; // Rango de movimiento vertical

    public Boss(int x, int y) {
        this.x = x;
        this.y = y;
        this.initialY = y;
        this.projectiles = new ArrayList<>();
        this.specialAttacks = new ArrayList<>();
        this.random = new Random();
    }

    public void update(Player player, ArrayList<Box> boxes) {
        if (!isAlive) {
            return;
        }

        playerVisible = isPlayerVisible(player);

        // Movimiento vertical del jefe
        y += moveDirection * moveSpeed;
        if (y > initialY + moveRange || y < initialY - moveRange) {
            moveDirection *= -1;
        }

        if (playerVisible) {
            if (shootCooldown == 0) {
                shoot(player);
                shootCooldown = 45;
            } else {
                shootCooldown--;
            }

            if (specialAttackCooldown == 0) {
                launchSpecialAttack(player);
                specialAttackCooldown = 180;
            } else {
                specialAttackCooldown--;
            }
        }

        // Actualizar proyectiles y verificar colisiones con las cajas
        for (int i = projectiles.size() - 1; i >= 0; i--) {
            Projectile p = projectiles.get(i);
            p.update();

            // Verificar colisión con las cajas
            for (Box box : boxes) {
                if (p.getBounds().intersects(box.getBounds())) {
                    projectiles.remove(i);
                    break;
                }
            }

            if (p.isOutOfBounds()) {
                projectiles.remove(i);
            }
        }

        // Actualizar ataques especiales
        for (int i = specialAttacks.size() - 1; i >= 0; i--) {
            SpecialAttack sa = specialAttacks.get(i);
            sa.update();
            if (sa.isFinished()) {
                specialAttacks.remove(i);
            }
        }
    }



    private boolean isPlayerVisible(Player player) {
        int screenWidth = 900;
        return player.getX() > x - screenWidth && player.getX() < x + width;
    }

    private void shoot(Player player) {
        // Generar un ángulo aleatorio para el disparo, limitando a que siempre vaya hacia la izquierda
        double randomAngleOffset = Math.toRadians(random.nextInt(30) - 15); // Un rango de -15 a +15 grados
        double dx = -(random.nextInt(100) + 50);  // Aseguramos que siempre vaya hacia la izquierda
        double dy = random.nextInt(20) - 10; // Movimiento vertical aleatorio entre -10 y +10

        double angle = Math.atan2(dy, dx);  // Calcular el ángulo con el desplazamiento aleatorio
        projectiles.add(new Projectile(x + width / 2, y + height / 2, angle));  // Crear la bala con ese ángulo
    }


    private void launchSpecialAttack(Player player) {
        // Calcular la dirección hacia el jugador
        double dx = player.getX() - (x + width / 2);
        double dy = player.getY() - (y + height / 2);
       
        // Ajustar el ángulo para una trayectoria más alta
        double angle = Math.atan2(dy, dx) - Math.PI / 3;  // Cambiamos de PI/4 a PI/3 para una trayectoria más alta
       
        // Aumentar significativamente la velocidad inicial para que la bomba llegue más lejos
        double initialSpeed = 15;  // Aumentado de 15 a 25
       
        specialAttacks.add(new Bomb(x + width / 2, y + height / 2, 30, angle, initialSpeed, player));
    }

    public void draw(Graphics g) {
   
    if (isAlive) { // Solo dibujar si el jefe está vivo
            g.setColor(Color.MAGENTA);
            g.fillRect(x, y, width, height);
    }

        // Dibuja el marcador de salud encima del jefe
        g.setColor(Color.BLACK);
        g.drawString("Vida: " + health, x + width / 2 - 15, y - 10); // Ajusta la posición según sea necesario

        g.setColor(Color.RED);
        g.fillRect(x, y - 20, width, 10);
        g.setColor(Color.GREEN);
        g.fillRect(x, y - 20, width * health / 100, 10);

        for (Projectile p : projectiles) {
            p.draw(g, playerVisible, health, health, health);
        }
       
        for (SpecialAttack sa : specialAttacks) {
            sa.draw(g);
        }
    }


    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
    public boolean intersects(Rectangle r) {
        return getBounds().intersects(r);
    }


 // Supongamos que este es tu método para manejar el daño al jefe
    public void damageBoss() {
        health--; // Reducir la vida del jefe
        if (health <= 0) {
            // Mostrar mensaje de victoria
            showWinMessage();
        }
    }

    private void showWinMessage() {
        // Usamos JOptionPane para mostrar un mensaje
        int response = JOptionPane.showConfirmDialog(null, "¡Ganaste!", "Victoria", JOptionPane.OK_CANCEL_OPTION);
        if (response == JOptionPane.OK_OPTION) {
            System.exit(0); // Cierra el programa
        }
    }

    public boolean isDead() {
        return health <= 0;
    }

    public ArrayList<Projectile> getProjectiles() {
        return projectiles;
    }

    public ArrayList<SpecialAttack> getSpecialAttacks() {
        return specialAttacks;
    }

    public int getWidth() {
        return width;
    }
}

class Projectile {
    private double x, y;
    private double speed = 4; // Velocidad constante
    private double angle;  // Ángulo calculado al inicio
    private int size = 20; // Tamaño de las balas

    public Projectile(int x, int y, double angle) {
        this.x = x;
        this.y = y;
        this.angle = angle;  // Ángulo calculado al inicio
    }

    public void update() {
        // Movimiento en línea recta basado en el ángulo
        x += speed * Math.cos(angle);  // Incrementa en el eje X
        y += speed * Math.sin(angle);  // Incrementa en el eje Y
    }

    public void draw(Graphics g, boolean firingLaser, int width, double laserAngle, int height) {
        g.setColor(Color.ORANGE);
        g.fillOval((int)x - size/2, (int)y - size/2, size, size);
    }

    public boolean isOutOfBounds() {
        // Condición para que la bala salga de los límites de la pantalla
        return x < 0 || x > 3000 || y < 0 || y > 800;
    }

    public Rectangle getBounds() {
        return new Rectangle((int)x - size/2, (int)y - size/2, size, size);
    }
}

abstract class SpecialAttack {
    protected int x, y;
    protected boolean finished;

    public SpecialAttack(int x, int y) {
        this.x = x;
        this.y = y;
        this.finished = false;
    }

    public abstract void update();
    public abstract void draw(Graphics g);
    public abstract Rectangle getBounds();

    public boolean isFinished() {
        return finished;
    }
}

class Bomb extends SpecialAttack {
    private int size;
    private double speed = 7;
    private double angle;
    private double vx, vy;
    private double x, y;
    private double gravity = 0.3;  // Reducida la gravedad para permitir una trayectoria más alta y larga
    private Player targetPlayer;
    private double initialVy;  // Nueva variable para recordar la velocidad vertical inicial

    public Bomb(int startX, int startY, int size, double angle, double speed, Player player) {
        super(startX, startY);
        this.x = startX;
        this.y = startY;
        this.size = size;
        this.angle = angle;
        this.speed = speed;
        this.vx = Math.cos(angle) * speed;
        this.vy = Math.sin(angle) * speed;
        this.initialVy = this.vy;  // Guardamos la velocidad vertical inicial
        this.targetPlayer = player;
    }

    @Override
    public void update() {
        x += vx;
        y += vy;
        vy += gravity;
       
        // Ajustar la trayectoria hacia el jugador solo en la fase descendente
        if (vy > 0) {  // Si la bomba está cayendo
            double dx = targetPlayer.getX() - x;
            double dy = targetPlayer.getY() - y;
            double distanceToPlayer = Math.sqrt(dx * dx + dy * dy);
           
            // Ajustar la velocidad horizontal para seguir al jugador
            if (distanceToPlayer > 100) {  // Solo ajustar si está lejos del jugador
                vx += dx / distanceToPlayer * 0.2;  // Aumentado el factor de ajuste
            }
        }

        // La bomba explota al tocar el suelo o al jugador
        if (y > 800 || (Math.abs(x - targetPlayer.getX()) < 20 && Math.abs(y - targetPlayer.getY()) < 20)) {
            finished = true;
        }
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillOval((int)x - size/2, (int)y - size/2, size, size);
       
        // Dibujar una "cola" para la bomba
        g.setColor(Color.ORANGE);
        for (int i = 1; i <= 5; i++) {
            int tailSize = size - i * 4;
            g.fillOval((int)(x - vx * i) - tailSize/2,
                       (int)(y - vy * i) - tailSize/2,
                       tailSize, tailSize);
        }
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int)x - size/2, (int)y - size/2, size, size);
    }
}

class Coin {
    private int x, y;
    private int size = 20;
    private boolean collected = false;  // Para verificar si ya fue recogida

    public Coin(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void draw(Graphics g) {
        if (!collected) {
            g.setColor(Color.YELLOW);
            g.fillOval(x, y, size, size);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, size, size);
    }

    public boolean isCollected() {
        return collected;
    }

    public void collect() {
        collected = true;
    }
}