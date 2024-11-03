import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class Cell {
    
    public int x, y;
    public Map<String, Boolean> walls;
    public boolean visited = false;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        // Initialize walls
        walls = new HashMap<>();
        walls.put("top", true);
        walls.put("right", true);
        walls.put("down", true);
        walls.put("left", true);
    }

    public Cell(Cell cell) {
        this.x = cell.x;
        this.y = cell.y;
        this.walls = cell.walls; // Cloning to avoid reference issues
    }

    // Check neighboring cells
    public Cell check_neighbours_cells(Cell[][] maze) {
        List<Cell> neighbours = new ArrayList<>();
        if (y - 1 >= 0 && !maze[x][y - 1].visited) {
            neighbours.add(maze[x][y - 1]);
        }
        if (x - 1 >= 0 && !maze[x - 1][y].visited) {
            neighbours.add(maze[x - 1][y]);
        }
        if (x + 1 < maze.length && !maze[x + 1][y].visited) {
            neighbours.add(maze[x + 1][y]);
        }
        if (y + 1 < maze[0].length && !maze[x][y + 1].visited) {
            neighbours.add(maze[x][y + 1]);
        }

        if (!neighbours.isEmpty()) {
            return neighbours.get(GameConfig.rand.nextInt(neighbours.size()));
        }
        return null;
    }

    // Print cell data for debugging
    public void print_cell() {
        int i = 0;
        if (walls.get("top")) i++;
        if (walls.get("right")) i += 10;
        if (walls.get("down")) i += 100;
        if (walls.get("left")) i += 1000;

        System.out.printf("(%d, %d : %04d) \n", x, y, i);
    }
    public void check_collision(Snake snake) {}

    public int get_walls_count() {
        int count = 0;
        for (Boolean value : walls.values()) {
            if (Boolean.TRUE.equals(value)) {
                count++;
            }
        }
        return count;
    }
}