import java.awt.Graphics;
import java.awt.FontMetrics;
import java.awt.Font;

public class Exit {
    private Cell exit;
    private int till_escape = GameConfig.POINTS_TO_ESCAPE;

    public Exit(Cell cell) {
        exit = cell;
    }
    public int get_exit_x() {
        return exit.x;
    }
    public int get_exit_y() {
        return exit.y;
    }
    public void render(Graphics g, int cameraX, int cameraY) {
        g.setColor(GameConfig.EXIT_COLOR);
        g.fillRect((exit.x*GameConfig.MAZE_BLOCK_SIZE - cameraX)+1, (exit.y*GameConfig.MAZE_BLOCK_SIZE - cameraY)+1, GameConfig.MAZE_BLOCK_SIZE-2, GameConfig.MAZE_BLOCK_SIZE-2);
        
        g.setFont(new Font("Courier", Font.BOLD, GameConfig.MAZE_BLOCK_SIZE * 2 / 3));
        FontMetrics metrics = g.getFontMetrics(g.getFont());

        // Aprēķinam teksta platumu un augstumu
        int textWidth = metrics.stringWidth(String.valueOf(till_escape));
        int textHeight = metrics.getHeight();

        // Aprēķinam centra pozīciju blokā
        int xText = (exit.x * GameConfig.MAZE_BLOCK_SIZE - cameraX) + (GameConfig.MAZE_BLOCK_SIZE - textWidth) / 2;
        int yText = (exit.y * GameConfig.MAZE_BLOCK_SIZE - cameraY) + ((GameConfig.MAZE_BLOCK_SIZE - textHeight) / 2) + metrics.getAscent();

        // Zīmējam tekstu centrā
        g.setColor(GameConfig.MAZE_COLOR);
        g.drawString(String.valueOf(till_escape), xText, yText);
    }

    public void checkExitCollision(Snake snake) {
        // System.out.printf("x: %d y: %d\n", snake.getBodyX(0) - exit.x * MAZE_BLOCK_SIZE, snake.getBodyY(0) - exit.y * MAZE_BLOCK_SIZE );
        if ((snake.getBodyX(0) >= exit.x * GameConfig.MAZE_BLOCK_SIZE && snake.getBodyX(0) < (exit.x + 1) * GameConfig.MAZE_BLOCK_SIZE) 
        && (snake.getBodyY(0) >= exit.y * GameConfig.MAZE_BLOCK_SIZE && snake.getBodyY(0) < (exit.y + 1) * GameConfig.MAZE_BLOCK_SIZE)) {
            if (till_escape == 1) {
                System.out.println("Snake escaped!");
                System.exit(0);
            }
            if (snake.getBodySize() == 1) return;

            snake.decrease(); till_escape--;
            System.out.printf("Till escape %d people have to be saved", till_escape);
        }
    }
}
