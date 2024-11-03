import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Dimension;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GamePanel extends JPanel implements ActionListener {
    private Timer timer;

    private Snake snake;
    private Apple apple;
    private Maze maze;
    
    private Exit exit;

    private int cameraX = 0; // Top-left X coordinate of the viewport
    private int cameraY = 0; // Top-left Y coordinate of the viewport

    public GamePanel() {
        setPreferredSize(new Dimension(GameConfig.WINDOW_WIDTH, GameConfig.WINDOW_HEIGHT));
        setBackground(GameConfig.BACKGROUND_COLOR);

        int exitx = GameConfig.rand.nextInt(GameConfig.MAZE_BLOCK_SIZE, GameConfig.MAP_WIDTH-GameConfig.MAZE_BLOCK_SIZE)/GameConfig.MAZE_BLOCK_SIZE;
        int exity = GameConfig.rand.nextInt(GameConfig.MAZE_BLOCK_SIZE, GameConfig.MAP_HEIGHT-GameConfig.MAZE_BLOCK_SIZE)/GameConfig.MAZE_BLOCK_SIZE;

        snake = new Snake();
        apple = new Apple();
        maze = new Maze(snake, apple, exitx, exity);

        exit = new Exit(maze.getCell(exitx, exity));
        setFocusable(true);
        addKeyListener(snake.getKeyAdapter());

        timer = new Timer(GameConfig.DELAY, this);
        timer.start();

        updateCamera();
        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!GameConfig.PAUSE) { 
            
            if (GameConfig.AUTOMATIC_SNAKE) snake.path_finding_algorithm(maze, apple, exit);
            else snake.move();  
            
            updateCamera();
            repaint();

            if (apple.checkAppleCollision(snake)) {
                // maze = new Maze(snake, apple);
            }  

            snake.checkSnakeCollisions();  
            maze.checkMazeCollision(snake);
            exit.checkExitCollision(snake);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawDirectionLine(g);
        
        maze.render_teleports(g, cameraX, cameraY);

        exit.render(g, cameraX, cameraY);
        
        snake.render(g, cameraX, cameraY, GameConfig.INVINCIBLE); // Pass camera position and block size
        maze.render(g, cameraX, cameraY); // Pass camera position and block size
        apple.render(g, cameraX, cameraY); // Pass camera position and block size
    }

    private void drawDirectionLine(Graphics g) {
        // Get the coordinates of the snake's head
        int startX = GameConfig.BLOCK_SIZE / 2 + snake.getBodyX(0) - cameraX; // Center of the snake head
        int startY = GameConfig.BLOCK_SIZE / 2 + snake.getBodyY(0) - cameraY; // Center of the snake head
    
        int windowWidth = GameConfig.WINDOW_WIDTH;
        int windowHeight = GameConfig.WINDOW_HEIGHT;
    
        for (int i = 0; i < GameConfig.APPLE_COUNT; i++) {
            int appleX = apple.getX(i) - cameraX;
            int appleY = apple.getY(i) - cameraY;
    
            // Check if apple is within the visible window bounds
            boolean isAppleInWindow = (appleX >= 0 && appleX <= windowWidth && appleY >= 0 && appleY <= windowHeight);
    
            if (!isAppleInWindow) {
                // Calculate direction to apple
                double dx = appleX - startX;
                double dy = appleY - startY;
    
                // Calculate edge intersection point
                int edgeX = startX;
                int edgeY = startY;
    
                if (appleX < 0) {
                    edgeX = 0;
                    edgeY = startY + (int) ((0 - startX) * dy / dx); // calculate Y based on X=0
                } else if (appleX > windowWidth) {
                    edgeX = windowWidth;
                    edgeY = startY + (int) ((windowWidth - startX) * dy / dx); // calculate Y based on X=windowWidth
                }
    
                if (appleY < 0) {
                    edgeY = 0;
                    edgeX = startX + (int) ((0 - startY) * dx / dy); // calculate X based on Y=0
                } else if (appleY > windowHeight) {
                    edgeY = windowHeight;
                    edgeX = startX + (int) ((windowHeight - startY) * dx / dy); // calculate X based on Y=windowHeight
                }
    
                // Draw a small circle or arrow at the edge of the window
                g.setColor(GameConfig.APPLE_COLOR);
                g.fillOval(edgeX - 5, edgeY - 5, 10, 10); // Draw a small circle as an indicator
            }
        }
    }
    
    private void updateCamera() {
        cameraX =  ((snake.getBodyX(0) <= GameConfig.WINDOW_WIDTH / 2) ? 0 : ((snake.getBodyX(0) >= GameConfig.MAP_WIDTH - GameConfig.WINDOW_WIDTH / 2) ? GameConfig.MAP_WIDTH - GameConfig.WINDOW_WIDTH : snake.getBodyX(0)-GameConfig.WINDOW_WIDTH/2));
        cameraY =  ((snake.getBodyY(0) <= GameConfig.WINDOW_HEIGHT / 2) ? 0 : ((snake.getBodyY(0) >= GameConfig.MAP_HEIGHT - GameConfig.WINDOW_HEIGHT / 2) ? GameConfig.MAP_HEIGHT - GameConfig.WINDOW_HEIGHT : snake.getBodyY(0)-GameConfig.WINDOW_HEIGHT/2));
    }
}
