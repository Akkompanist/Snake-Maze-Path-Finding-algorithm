import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;

public class Apple {
    private int[] x = new int[GameConfig.APPLE_COUNT];
    private int[] y = new int[GameConfig.APPLE_COUNT];

    public Apple() {
        for (int i = 0; i < GameConfig.APPLE_COUNT; i++) {
            spawnNewApple(i);  // Spawn apple at a random location initially
        }
    }

    public void spawnNewApple(int i) {

        x[i] = GameConfig.rand.nextInt(0, GameConfig.MAP_WIDTH / GameConfig.BLOCK_SIZE) * GameConfig.BLOCK_SIZE; //Math.max(0, (snakeX - 2 * GameConfig.WINDOW_WIDTH)/GameConfig.BLOCK_SIZE), Math.min(GameConfig.MAP_WIDTH/GameConfig.BLOCK_SIZE, (snakeX + 2 * GameConfig.WINDOW_WIDTH)/GameConfig.BLOCK_SIZE)) * GameConfig.BLOCK_SIZE; 
        y[i] = GameConfig.rand.nextInt(0, GameConfig.MAP_HEIGHT / GameConfig.BLOCK_SIZE) * GameConfig.BLOCK_SIZE; //Math.max(0, (snakeY - 2 * GameConfig.WINDOW_HEIGHT)/GameConfig.BLOCK_SIZE), Math.min(GameConfig.MAP_HEIGHT/GameConfig.BLOCK_SIZE, (snakeY + 2 * GameConfig.WINDOW_HEIGHT)/GameConfig.BLOCK_SIZE)) * GameConfig.BLOCK_SIZE; 
    }

    public ArrayList<int[]> in_cell_crd(int x, int y) {
        ArrayList<int[]> apples_in_cell = new ArrayList<int[]>();

        for (int i = 0; i < GameConfig.APPLE_COUNT; i++) {
            if ((x * GameConfig.MAZE_BLOCK_SIZE <= getX(i) && (x+1)* GameConfig.MAZE_BLOCK_SIZE > getX(i) + GameConfig.BLOCK_SIZE * GameConfig.APPLE_SIZE) 
            && (y * GameConfig.MAZE_BLOCK_SIZE <= getY(i) && (y+1)* GameConfig.MAZE_BLOCK_SIZE > getY(i) + GameConfig.BLOCK_SIZE * GameConfig.APPLE_SIZE)) {
                apples_in_cell.add(new int[] {getX(i)/GameConfig.BLOCK_SIZE, getY(i)/GameConfig.BLOCK_SIZE});
            }
        }
        return apples_in_cell;
    }
    public boolean checkAppleCollision(Snake snake) {
        for (int i = 0; i < GameConfig.APPLE_COUNT; i++) {
            if ((snake.getBodyX(0) >= getX(i) && snake.getBodyX(0) <getX(i) + GameConfig.BLOCK_SIZE * GameConfig.APPLE_SIZE) 
            && (snake.getBodyY(0) >= getY(i) && snake.getBodyY(0) < getY(i) + GameConfig.BLOCK_SIZE * GameConfig.APPLE_SIZE)) {
                snake.grow();  // Make the snake longer
                spawnNewApple(i);  // Spawn a new apple in a random location
                return true;
            }
        }
        return false;
    }

    public void render(Graphics g, int cameraX, int cameraY) {
        int apple_size = GameConfig.BLOCK_SIZE * GameConfig.APPLE_SIZE;
        for (int i = 0; i < GameConfig.APPLE_COUNT; i++) {
            g.setColor(GameConfig.APPLE_COLOR);
            g.fillRect((x[i] - cameraX), (y[i] - cameraY), apple_size, apple_size);
        }
    }    
    
    public int getX(int i) { return x[i]; }
    public int getY(int i) { return y[i]; }
}
