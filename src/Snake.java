import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.lang.reflect.Array;

public class Snake {
    private List<int[]> body;  // Snake body
    private int directionX = 0; 
    private int directionY = 0;
    private boolean justAteApple = false;

    private ArrayList<int[]> path = new ArrayList<>();
    int[][] directions = {
        {0, -1}, // augšā
        {0, 1},  // apakšā
        {-1, 0}, // pa kreisi
        {1, 0}   // pa labi
    };

    public Snake() {
        body = new ArrayList<>();
        body.add(new int[]{
            GameConfig.rand.nextInt(0, (GameConfig.MAP_WIDTH-GameConfig.MAZE_BLOCK_SIZE)/GameConfig.BLOCK_SIZE) * GameConfig.BLOCK_SIZE,
            GameConfig.rand.nextInt(0, (GameConfig.MAP_HEIGHT-GameConfig.MAZE_BLOCK_SIZE)/GameConfig.BLOCK_SIZE) * GameConfig.BLOCK_SIZE,
            }
        );  // Starting point
    }

    public void move() {

        int[] head = body.get(0);
        int newX = head[0] + directionX * GameConfig.BLOCK_SIZE;
        int newY = head[1] + directionY * GameConfig.BLOCK_SIZE;
        body.add(0, new int[]{newX, newY});  // Add new head
        if (!justAteApple) {
            body.remove(body.size() - 1);  // Remove tail only if snake hasn't eaten
        } else {
            justAteApple = false;  // Reset after growth
        }
    }
    
    public int[] get_cell() {
        return new int[]{
            body.get(0)[0] / GameConfig.MAZE_BLOCK_SIZE, 
            body.get(0)[1] / GameConfig.MAZE_BLOCK_SIZE
        };
    }

    public void path_finding_algorithm(Maze maze, Apple apples, Exit exit) {
        if (!path.isEmpty()) {follow_path(); return;}

        int[] startXY = get_cell();
        Node start = new Node(startXY[0], startXY[1], null, 0, 0);

        Queue<Node> queue = new LinkedList<>();
        queue.add(start);

        boolean[][] visited = new boolean[GameConfig.MAZE_WIDTH][GameConfig.MAZE_HEIGHT]; // Saglabā apmeklētās šūnas
        visited[start.x][start.y] = true;

        while (!queue.isEmpty()) {
            Node current = queue.poll();

            if (apples.in_cell_crd(current.x, current.y) != null) {
                create_block_path(current, apples.in_cell_crd(current.x, current.y), maze);
                return;
            }

            for (int[] direction : directions) {
                int newX = current.x + direction[0];
                int newY = current.y + direction[1];

                if (maze.check_wall(current.x, current.y, direction[0], direction[1])) continue;
                if (newX >= 0 && newX < GameConfig.MAZE_WIDTH && newY >= 0 && newY < GameConfig.MAZE_HEIGHT && !visited[newX][newY]) {
                    visited[newX][newY] = true;
                    queue.add(new Node(newX, newY, current, direction[0], direction[1]));
                }
            }
        }
    }

    private void create_block_path(Node t, ArrayList<int[]> apple_cord, Maze maze) {
        int[][] cell_path = new int[GameConfig.MAP_HEIGHT/GameConfig.BLOCK_SIZE][GameConfig.MAP_WIDTH/GameConfig.BLOCK_SIZE];
        
        Arrays.setAll(cell_path, row -> new int[GameConfig.MAP_WIDTH/GameConfig.BLOCK_SIZE]);
        for (int[] row : cell_path) Arrays.fill(row, -1);

        for (; t != null; t = t.parent) {
            int[][] tcell = maze.get_cell(t.x, t.y);
            System.out.printf("t.x %d t.y %d \n", t.x, t.y);
            for (int i = t.y * GameConfig.MAZE_LENGTH_OF_PATH; i < t.y * GameConfig.MAZE_LENGTH_OF_PATH + tcell.length; i++) {
                for (int j = t.x * GameConfig.MAZE_LENGTH_OF_PATH; j < t.x * GameConfig.MAZE_LENGTH_OF_PATH + tcell[i-t.y* GameConfig.MAZE_LENGTH_OF_PATH].length; j++) {
                    System.out.printf("%d ", tcell[i-t.y * GameConfig.MAZE_LENGTH_OF_PATH][j-t.x* GameConfig.MAZE_LENGTH_OF_PATH]);
                    cell_path[i][j] = tcell[i-t.y * GameConfig.MAZE_LENGTH_OF_PATH][j-t.x* GameConfig.MAZE_LENGTH_OF_PATH];
                } System.out.println();
            }
        } System.out.println();

        
       
        int sy = body.get(0)[0]/GameConfig.BLOCK_SIZE;
        int sx = body.get(0)[1]/GameConfig.BLOCK_SIZE;
        
        for (int[] i : apple_cord) {
            cell_path[i[0]][i[1]] = -2;
        }
        
        Node start = new Node(sx, sy, null, 0, 0);
        
        cell_path[sx][sy] = 1;

        Queue<Node> queue = new LinkedList<>();
        queue.add(start);
        // System.out.printf("\nsx %d sy %d : fx %d fy %d\n", sx, sy, fx, fy);

        for (int[] i : cell_path) {
            System.out.println(Arrays.toString(i));
        }

        while (!queue.isEmpty()) {
            // Node current = queue.poll();

        }
        
    }

    private void create_direction_path(Node t) {
        for (; t != null; t = t.parent) {
                System.out.printf("x%d y%d dx%d dy%d\n", t.y, t.x, t.dy, t.dx);
                path.add(0, new int[]{t.dy, t.dx});
        }
    }
    private void follow_path() {
        directionX = path.get(0)[0];
        directionY = path.get(0)[1];
        path.remove(0);

        move();
    }

    public void grow() {
        justAteApple = true;  // Set flag so next move doesn't remove tail
    }
    public void decrease() {
        body.remove(body.size() - 1);
    }
    public int getBodyX(int i) {
        return body.get(i)[0];
    }
    public int getBodyY(int i) {
        return body.get(i)[1];
    }

    public int getDirectionX() {
        return directionX;
    }
    public int getDirectionY() {
        return directionY;
    }

    public void changeBodyX(int i, int x) {
        body.add(0, new int[]{x, body.get(0)[1]});
        body.remove(body.size() - 1);
    }
    public void changeBodyY(int i, int y) {
        body.add(0, new int[]{body.get(0)[0], y});
        body.remove(body.size() - 1);
    }
    public void changeBodyPos(int i, int x, int y) {
        body.add(0, new int[]{x, y});
        body.remove(body.size() - 1);
    }

    public void render(Graphics g, int cameraX, int cameraY, boolean invinsible) {
        if (invinsible) {g.setColor(GameConfig.MAZE_COLOR);}
        else {g.setColor(GameConfig.SNAKE_COLOR);}

        for (int[] part : body) {
            g.fillRect((part[0] - cameraX), (part[1] - cameraY), GameConfig.BLOCK_SIZE, GameConfig.BLOCK_SIZE);
        }
    }

    public void checkSnakeCollisions() {
        if (GameConfig.SNAKE_COLLISION && !GameConfig.INVINCIBLE) {
            for (int i = 1; i <  getBodySize(); i++) {
                if ( getBodyX(0) ==  getBodyX(i) &&  getBodyY(0) ==  getBodyY(i)) {
                    // Game over logic can go here (e.g., reset game or display game over screen)
                    System.out.println("Game Over! Snake collided with itself.");
                    System.exit(0); // Exiting the game for simplicity
                }
            }
        }
        // Check wall collisions (assuming walls are at the edges of the maze)
        if (getBodyX(0) < 0 ||  getBodyX(0) >= GameConfig.MAP_WIDTH ||  getBodyY(0) < 0 ||  getBodyY(0) >= GameConfig.MAP_HEIGHT) {
            // Game over logic can go here (e.g., reset game or display game over screen)
            if (GameConfig.WINDOWWALLS_COLLISION && !GameConfig.INVINCIBLE) {
                System.out.println("Game Over! Snake collided with the wall.");
                System.exit(0); // Exiting the game for simplicity
            } else {
                if ( getBodyX(0) < 0) { changeBodyX(0, GameConfig.MAP_WIDTH);}
                else if ( getBodyX(0) >= GameConfig.MAP_WIDTH) { changeBodyX(0, 0);}

                else if ( getBodyY(0) < 0) { changeBodyY(0, GameConfig.MAP_HEIGHT);}
                else if ( getBodyY(0) >= GameConfig.MAP_HEIGHT) { changeBodyY(0, 0);}
            }
        }
    }

    public KeyAdapter getKeyAdapter() {
        return new SnakeKeyAdapter();
    }

    private class SnakeKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            if ((key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) && directionX != 1) {
                directionX = -1;
                directionY = 0;
            }
            if ((key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) && directionX != -1) {
                directionX = 1;
                directionY = 0;
            }
            if ((key == KeyEvent.VK_W || key == KeyEvent.VK_UP) && directionY != 1) {
                directionX = 0;
                directionY = -1;
            }
            if ((key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN) && directionY != -1) {
                directionX = 0;
                directionY = 1;
            }

            if (key == KeyEvent.VK_SPACE) {
                if (GameConfig.PAUSE) GameConfig.PAUSE = false;
                else GameConfig.PAUSE = true;  
            }
            if (e.getKeyChar() == 'i') {
                if (GameConfig.INVINCIBLE) GameConfig.INVINCIBLE = false;
                else GameConfig.INVINCIBLE = true;
            }
            if (e.getKeyChar() == '+') {
                grow();
            }
            if (e.getKeyChar() == '-') {
                grow();
            }
        }
    }
    public int getBodySize() {
        return body.size();
    }
}
