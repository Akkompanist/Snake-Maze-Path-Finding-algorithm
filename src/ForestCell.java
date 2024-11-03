import java.awt.Graphics;

public class ForestCell extends Cell {
    public int[][] forest;

    public ForestCell(int x, int y) {
        super(x, y);
        create_forest();
    }

    public int[][] get_forest() {
        return forest;
    }
    // Method to create forest
    private void create_forest() {
        forest = new int[GameConfig.MAZE_LENGTH_OF_PATH][GameConfig.MAZE_LENGTH_OF_PATH];
        for (int i = 0; i < forest.length - 1; i += GameConfig.FORESTS_TREE_FREQUENCY) {
            for (int j = 0; j < forest[i].length - 1; j += GameConfig.FORESTS_TREE_FREQUENCY) {
                forest[i + GameConfig.rand.nextInt(2)][j + GameConfig.rand.nextInt(2)] = -1;
            }
        }
    }

    public void render_tree(Graphics g, int cameraX, int cameraY) {
        int tx = x * GameConfig.MAZE_BLOCK_SIZE;
        int ty = y * GameConfig.MAZE_BLOCK_SIZE;

        for (int i = 0; i < forest.length; i++) {
            for (int j = 0; j < forest[i].length; j++) {
                if (forest[i][j] == -1) {

                    // Draw tree trunk
                    g.setColor(GameConfig.TREE_COLOR);
                    g.fillRect(
                        tx + GameConfig.BLOCK_SIZE * j - cameraX,
                        ty + GameConfig.BLOCK_SIZE * i - cameraY,
                        GameConfig.BLOCK_SIZE,
                        GameConfig.BLOCK_SIZE
                    );
                }
            }
        }
    }

    public void render_tree_crown(Graphics g, int cameraX, int cameraY) {
        int tx = x * GameConfig.MAZE_BLOCK_SIZE;
        int ty = y * GameConfig.MAZE_BLOCK_SIZE;

        // Draw tree crowns and trunks
        for (int i = 0; i < forest.length; i++) {
            for (int j = 0; j < forest[i].length; j++) {
                if (forest[i][j] == -1) {
                    // Draw tree crown
                    g.setColor(GameConfig.TREE_CROWN_COLOR);
                    g.fillOval(
                        (int) (tx + (0.5 - GameConfig.TREE_CROWN_SIZE / 2 + j) * GameConfig.BLOCK_SIZE - cameraX),
                        (int) (ty + (0.5 - GameConfig.TREE_CROWN_SIZE / 2 + i) * GameConfig.BLOCK_SIZE - cameraY),
                        (int) (GameConfig.BLOCK_SIZE * GameConfig.TREE_CROWN_SIZE),
                        (int) (GameConfig.BLOCK_SIZE * GameConfig.TREE_CROWN_SIZE)
                    );
                }
            }
        }
    }
    // Check for collisions with forest elements
    @Override
    public void check_collision(Snake snake) {
        int tx = x * GameConfig.MAZE_BLOCK_SIZE;
        int ty = y * GameConfig.MAZE_BLOCK_SIZE;

        for (int i = 0; i < forest.length; i++) {
            for (int j = 0; j < forest[i].length; j++) {
                if (forest[i][j] == -1) {
                    if (tx + GameConfig.BLOCK_SIZE * j == snake.getBodyX(0) && 
                        ty + GameConfig.BLOCK_SIZE * i == snake.getBodyY(0)) {

                        if (snake.getBodySize() == 1 || GameConfig.HARDMODE) {
                            System.out.println("Snake collided with the tree!");
                            System.exit(0);
                        } else {
                            snake.decrease();
                            forest[i][j] = 0; // Remove tree on collision
                        }
                    }
                }
            }
        }
    }
}
