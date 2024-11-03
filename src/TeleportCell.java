import javax.swing.ImageIcon;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;

public class TeleportCell extends Cell {
    private static final Image teleport_gif = new ImageIcon("asc/teleport.gif").getImage();
    private double rotationAngle = 0; 

    public TeleportCell(Cell cell) {
        super(cell);

        if (!walls.get("top")) {rotationAngle = 90;}
        else if (!walls.get("right")) {rotationAngle = 180;}
        else if (!walls.get("down")) {rotationAngle = 270;}
    }

    public void render_teleport(Graphics g, int cameraX, int cameraY) {
        Graphics2D g2d = (Graphics2D) g;
        
        // Calculate position
        int xPos = x * GameConfig.MAZE_BLOCK_SIZE - cameraX;
        int yPos = y * GameConfig.MAZE_BLOCK_SIZE - cameraY;
        
        // Calculate the center of the image
        int centerX = xPos + GameConfig.MAZE_BLOCK_SIZE / 2;
        int centerY = yPos + GameConfig.MAZE_BLOCK_SIZE / 2;

        // Apply rotation
        AffineTransform originalTransform = g2d.getTransform();
        g2d.rotate(Math.toRadians(rotationAngle), centerX, centerY);

        // Draw the image at its top-left corner adjusted for the rotation
        g2d.drawImage(teleport_gif, xPos, yPos, GameConfig.MAZE_BLOCK_SIZE, GameConfig.MAZE_BLOCK_SIZE, null);

        // Reset transformation to avoid affecting other drawings
        g2d.setTransform(originalTransform);
    }

    @Override
    public void check_collision(Snake snake) {
        if ((snake.getBodyX(0) > x * GameConfig.MAZE_BLOCK_SIZE && snake.getBodyX(0) < (x+1) * GameConfig.MAZE_BLOCK_SIZE)
        && (snake.getBodyY(0) > y * GameConfig.MAZE_BLOCK_SIZE && snake.getBodyY(0) < (y+1) * GameConfig.MAZE_BLOCK_SIZE)) {
            snake.changeBodyPos(0, GameConfig.rand.nextInt(2, GameConfig.MAP_WIDTH/GameConfig.MAZE_BLOCK_SIZE-2) * GameConfig.MAZE_BLOCK_SIZE, 
                                     GameConfig.rand.nextInt(2, GameConfig.MAP_HEIGHT/GameConfig.MAZE_BLOCK_SIZE-2) * GameConfig.MAZE_BLOCK_SIZE);
        }
    }

}