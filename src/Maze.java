import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Maze {
    private int width = GameConfig.MAZE_WIDTH;
    private int height = GameConfig.MAZE_HEIGHT;

    private Cell[][] maze = new Cell[width][height];
    private Apple apple;

    public Maze(Snake snake, Apple apple, int exitx, int exity) {
        boolean last_cell_has_forest = false;

        this.apple = apple;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                maze[i][j] = new Cell(i, j);
                if (i == exitx &&  j == exity) {
                    last_cell_has_forest = true;
                    continue;
                }

                if ((i * GameConfig.MAZE_BLOCK_SIZE <= snake.getBodyX(0) && (i+1) * GameConfig.MAZE_BLOCK_SIZE >= snake.getBodyX(0))
                && (j * GameConfig.MAZE_BLOCK_SIZE <= snake.getBodyY(0) && (j+1) * GameConfig.MAZE_BLOCK_SIZE >= snake.getBodyY(0))) {
                    last_cell_has_forest = false;
                    continue;
                }
        
                if (apple.in_cell_crd(i, j) != null) {
                    last_cell_has_forest = false; continue;
                }

                if (last_cell_has_forest) {
                    if (GameConfig.rand.nextInt(10) < 7) {
                        maze[i][j] = new ForestCell(i, j);
                        last_cell_has_forest = true;
                        continue;
                    }
                } else {
                    if (GameConfig.rand.nextInt(10) == 5) {
                        maze[i][j] = new ForestCell(i, j);
                        last_cell_has_forest = true;
                        continue;
                    }
                }
            }
        }
        generateMaze(exitx, exity);
    }
    public boolean check_wall(int x, int y, int dx, int dy) {
        if (dx == 1) {
            return maze[x][y].walls.get("right");
        } else if (dx == -1) {
            return maze[x][y].walls.get("left");
        }

        if (dy == 1) {
            return maze[x][y].walls.get("down");
        } else if (dy == -1) {
            return maze[x][y].walls.get("top");
        }

        return false;
    }
    
    private void remove_walls(Cell current, Cell next) {
        int dx = current.x - next.x;
        int dy = current.y - next.y;
        if (dx == 1) {
            current.walls.put("left", false);
            next.walls.put("right", false);
        } else if (dx == -1) {
            current.walls.put("right", false);
            next.walls.put("left", false);
        }

        if (dy == 1) {
            current.walls.put("top", false);
            next.walls.put("down", false);
        } else if (dy == -1) {
            current.walls.put("down", false);
            next.walls.put("top", false);
        }
    }

    private void generateMaze(int x, int y) {
        Cell currentCell = maze[x][y];
        List<Cell> stack = new ArrayList<>();
        int counter = 1;
        
        while (counter < width * height) {
            currentCell.visited = true;
            Cell nextCell = currentCell.check_neighbours_cells(maze);
            if (nextCell != null) {
                nextCell.visited = true;
                counter++;
                stack.add(currentCell);
                remove_walls(currentCell, nextCell);
                currentCell = nextCell;
            } else if (stack.size() > 0) {
                currentCell = stack.remove(stack.size() - 1);
            }
        }

        create_teleports();
    }

    private void create_teleport(int start_x, int start_y, int end_x, int end_y) {
        for (int i = start_x; i < end_x; i++) {
            for (int j = start_y; j < end_y; j++) {
                if (maze[i][j].get_walls_count() == 3 && !(maze[i][j] instanceof ForestCell) && apple.in_cell_crd(i, j) == null) {
                    maze[i][j] = new TeleportCell(maze[i][j]);
                    return;
                }
            }
        }
    }

    private void create_teleports() {
        create_teleport(1, 1, width/2-1, height/2-1);
        create_teleport(width/2, 1, width-1, height/2-1);
        create_teleport(1, height/2, width/2-1, height-1);
        create_teleport(width/2, height/2, width-1, height-1);
    }

    private void drawCell(Graphics g, Cell cell, int cameraX, int cameraY) {
        int x = cell.x * GameConfig.MAZE_BLOCK_SIZE;
        int y = cell.y * GameConfig.MAZE_BLOCK_SIZE;
        
        

        g.setColor(GameConfig.MAZE_COLOR);
        // Draw walls based on the cell's wall information
        if (cell.walls.get("top")) {
            g.drawLine(x - cameraX , y - cameraY, x + GameConfig.MAZE_BLOCK_SIZE - cameraX, y - cameraY); // Top wall
        }
        if (cell.walls.get("right")) {
            g.drawLine(x + GameConfig.MAZE_BLOCK_SIZE - cameraX, y - cameraY, x + GameConfig.MAZE_BLOCK_SIZE - cameraX, y + GameConfig.MAZE_BLOCK_SIZE - cameraY); // Right wall
        }
        if (cell.walls.get("down")) {
            g.drawLine(x - cameraX, y + GameConfig.MAZE_BLOCK_SIZE - cameraY, x + GameConfig.MAZE_BLOCK_SIZE - cameraX, y + GameConfig.MAZE_BLOCK_SIZE - cameraY); // Bottom wall
        }
        if (cell.walls.get("left")) {
            g.drawLine(x - cameraX, y - cameraY, x - cameraX, y + GameConfig.MAZE_BLOCK_SIZE - cameraY); // Left wall
        }

    }
    public void render_teleports(Graphics g, int cameraX, int cameraY) {
        int start_x = Math.max(0, (int)(cameraX / GameConfig.MAZE_BLOCK_SIZE)-1);
        int start_y = Math.max(0, (int)(cameraY / GameConfig.MAZE_BLOCK_SIZE)-1);

        int end_x = Math.min(maze.length-1, start_x + (int)(GameConfig.WINDOW_WIDTH / GameConfig.MAZE_BLOCK_SIZE) + 2);
        int end_y = Math.min(maze[0].length-1, start_y + (int)(GameConfig.WINDOW_HEIGHT / GameConfig.MAZE_BLOCK_SIZE) + 2);
        
        for (int i = start_x; i <= end_x; i++) {
            for (int j = start_y; j <= end_y; j++) {
                if (maze[i][j] instanceof TeleportCell) {
                    ((TeleportCell) maze[i][j]).render_teleport(g, cameraX, cameraY);
                }
            }
        }
    }
    public void render(Graphics g, int cameraX, int cameraY) {
        int start_x = Math.max(0, (int)(cameraX / GameConfig.MAZE_BLOCK_SIZE)-1);
        int start_y = Math.max(0, (int)(cameraY / GameConfig.MAZE_BLOCK_SIZE)-1);

        int end_x = Math.min(maze.length-1, start_x + (int)(GameConfig.WINDOW_WIDTH / GameConfig.MAZE_BLOCK_SIZE) + 2);
        int end_y = Math.min(maze[0].length-1, start_y + (int)(GameConfig.WINDOW_HEIGHT / GameConfig.MAZE_BLOCK_SIZE) + 2);
        
        for (int i = start_x; i <= end_x; i++) {
            for (int j = start_y; j <= end_y; j++) {
                if (maze[i][j] instanceof ForestCell) {
                        ((ForestCell) maze[i][j]).render_tree_crown(g, cameraX, cameraY);
                }
            }
        }
        for (int i = start_x; i <= end_x; i++) {
            for (int j = start_y; j <= end_y; j++) {
                if (maze[i][j] instanceof ForestCell) {
                        ((ForestCell) maze[i][j]).render_tree(g, cameraX, cameraY);
                }
            }
        }
        for (int i = start_x; i <= end_x; i++) {
            for (int j = start_y; j <= end_y; j++) {
                drawCell(g, maze[i][j], cameraX, cameraY);  
            }
        }
    }

    public Cell getCell(int x, int y) {
        // Convert the x and y coordinates to indices in the maze array
        int cellX = x / GameConfig.MAZE_BLOCK_SIZE;
        int cellY = y / GameConfig.MAZE_BLOCK_SIZE;

        // Make sure the coordinates are within the bounds of the maze
        if (cellX >= 0 && cellX < width && cellY >= 0 && cellY < height) {
            return maze[cellX][cellY]; // Return the cell at the specified position
        } else {
            return null; // If the coordinates are out of bounds, return null
        }
    }

    private Cell lastCell;

    public void checkMazeCollision(Snake snake) {
        Cell currentCell = getCell(snake.getBodyX(0), snake.getBodyY(0)); // Get the cell at the snake's head position

        if (!GameConfig.MAZEWALLS_COLLISION || GameConfig.INVINCIBLE) {
            lastCell = currentCell;
            return;
        }
        if (currentCell instanceof TeleportCell) { currentCell.check_collision(snake); return;}

        currentCell.check_collision(snake);

        if (currentCell == lastCell || lastCell == null) {
            lastCell = currentCell;
            return;
        }
        // Check for collisions based on the snake's direction
        if (snake.getDirectionX() == 1) { // Moving right
            if (lastCell.walls.get("right")) {
                if (snake.getBodySize() == 1 || GameConfig.HARDMODE) {
                    System.out.println("Snake collided with the maze wall!");
                    System.exit(0);
                }
                else {
                    snake.decrease(); 
                    lastCell.walls.put("right", false);
                    currentCell.walls.put("left", false);
                }
            }
        } else if (snake.getDirectionX() == -1) { // Moving left
            if (lastCell.walls.get("left")) {
                if (snake.getBodySize() == 1 || GameConfig.HARDMODE) {
                    System.out.println("Snake collided with the maze wall!");
                    System.exit(0);
                }
                else {
                    snake.decrease(); 
                    lastCell.walls.put("left", false);
                    currentCell.walls.put("right", false);
                }
            }
        } else if (snake.getDirectionY() == 1) { // Moving down
            if (lastCell.walls.get("down")) {
                if (snake.getBodySize() == 1 || GameConfig.HARDMODE) {
                    System.out.println("Snake collided with the maze wall!");
                    System.exit(0);
                }
                else {
                    snake.decrease(); 
                    lastCell.walls.put("down", false);
                    currentCell.walls.put("top", false);
                }
            }
        } else if (snake.getDirectionY() == -1) { // Moving up
            if (lastCell.walls.get("top")) {
                if (snake.getBodySize() == 1 || GameConfig.HARDMODE) {
                    System.out.println("Snake collided with the maze wall!");
                    System.exit(0);
                }
                else {
                    snake.decrease(); 
                    lastCell.walls.put("top", false);
                    currentCell.walls.put("down", false);
                }
            }
        }
        lastCell = currentCell;
    }
    
    public void print_maze() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Cell cell = maze[i][j];
                if (cell != null) {
                    System.out.print("Cell (" + i + ", " + j + "): ");
                    System.out.print("Visited = " + cell.visited + ", ");
                    System.out.print("Walls = {");
                    System.out.print("top: " + cell.walls.get("top") + ", ");
                    System.out.print("right: " + cell.walls.get("right") + ", ");
                    System.out.print("bottom: " + cell.walls.get("down") + ", ");
                    System.out.print("left: " + cell.walls.get("left"));
                    System.out.print("}, ");
                    System.out.print("Has Forest = " + (cell instanceof ForestCell));
                    System.out.println();
                } else {
                    System.out.println("Cell (" + i + ", " + j + "): null");
                }
            }
            System.out.println();
        }
    }
    public int[][] get_cell(int x, int y) {
        int[][] cell_obstacles = new int[GameConfig.MAZE_LENGTH_OF_PATH][GameConfig.MAZE_LENGTH_OF_PATH];
        if (maze[x][y] instanceof TeleportCell) {
            Arrays.setAll(cell_obstacles, row -> new int[GameConfig.MAZE_LENGTH_OF_PATH]);
            for (int[] row : cell_obstacles) Arrays.fill(row, -1);
        } else if (maze[x][y] instanceof ForestCell) {
            cell_obstacles = ((ForestCell)maze[x][y]).get_forest();
        } else {
            Arrays.setAll(cell_obstacles, row -> new int[GameConfig.MAZE_LENGTH_OF_PATH]);
            for (int[] row : cell_obstacles) Arrays.fill(row, 0);
        }
        return cell_obstacles;
         
    }
}
