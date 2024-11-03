import javax.swing.JFrame;

public class GameWindow extends JFrame{
    public GameWindow() {
        setTitle("Lost in Maze");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setSize(GameConfig.WINDOW_WIDTH+20, GameConfig.WINDOW_HEIGHT+40);
        setLocationRelativeTo(null);

        add(new GamePanel());

        setVisible(true);

    }
}