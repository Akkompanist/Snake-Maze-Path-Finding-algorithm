# Snake
The project was made to try and implement snake, maze algorithm and path-finding algorithm.

 - [x] snake
 - [x] maze algorithm
 - [ ] path-finding algorithm (was implemented half of it)

Project is divided on classes (files):
- `Main.java` (contains `main()` function, starts the program)
- `GameWindow.java` (contains constructor for window creation)
- `GameConfig.java` (contains different parameters to setup the program)
- `GamePanel.java` (contains the logic how the program works)

- Game Elements:
	1. `Snake.java` (contains all related to snake, rendering, collision, movement + checks user pressed keys)
	2. `Apple.java` (contains all related to apple, rendering, collision, spawning)
	3. `Maze.java` (contains all related to apple, rendering, collision, generation)
		1. Elements of maze:
			1. `Cell.java` (simple cell, no obstacles)
			2. `TeleportCell.java` (teleport, when collision happens, snake moves to random point)
			3. `ForestCell.java` (cell with obstacles, where is a chance of collision not only with walls, but also with trees)
			4. `Exit.java` (contains all related to exit, rendering, collision, generation)

- `Node.java` (was created to implement the path-finding algorithm)
