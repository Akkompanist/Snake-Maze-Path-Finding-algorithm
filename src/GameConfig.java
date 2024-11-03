import java.awt.Color;
import java.util.Random;

public class GameConfig {
    public static Random rand = new Random();

    public static final int POINTS_TO_ESCAPE = 20;

    public static final int WINDOW_WIDTH = 900; // Window width in px
    public static final int WINDOW_HEIGHT = 600; // Window height in px
    public static final int BLOCK_SIZE = 30; // Size of each block in px
    public static final int MAP_WIDTH = 1800; // Total map width in px
    public static final int MAP_HEIGHT = 1200; // Total map height in px

    public static int DELAY = 150; // Timer delay for game loop

    public static final Color BACKGROUND_COLOR = new Color(4,2,4);
    public static final Color SNAKE_COLOR = new Color(0, 200, 0);
    public static final Color APPLE_COLOR = new Color(255, 50, 50);
    public static final Color MAZE_COLOR = new Color(250, 250, 250);
    public static final Color EXIT_COLOR = new Color(0, 0, 139);
    public static final Color TREE_CROWN_COLOR = new Color(1, 50, 32);
    public static final Color TREE_COLOR = new Color(1, 81, 41);

    public static final int MAZE_LENGTH_OF_PATH = 4;
    public static final int APPLE_SIZE = 1;
    public static final int APPLE_COUNT = 4;
    public static final int INVINSIBILITY_COUNT = 0;
    public static final int MAZE_BLOCK_SIZE = BLOCK_SIZE * MAZE_LENGTH_OF_PATH;
    public static final double TREE_CROWN_SIZE = 3;
    public static final boolean SNAKE_COLLISION = true;
    public static final boolean WINDOWWALLS_COLLISION = true;
    public static final boolean MAZEWALLS_COLLISION = true;
    public static boolean INVINCIBLE = false;
    public static boolean PAUSE = true;
    public static boolean HARDMODE = false;
    public static boolean AUTOMATIC_SNAKE = false;
    public static double FORESTS_TREE_FREQUENCY = 2;

    public static final int MAZE_WIDTH = GameConfig.MAP_WIDTH/GameConfig.MAZE_BLOCK_SIZE;
    public static final int MAZE_HEIGHT = GameConfig.MAP_HEIGHT/GameConfig.MAZE_BLOCK_SIZE;


}
