import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MyKeyAdapter extends KeyAdapter {
    private final GamePanel gamePanel;

    public MyKeyAdapter(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (!gamePanel.gameStarted) {
            gamePanel.gameStarted = true;
        }

        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                if (gamePanel.direction != 'R') {
                    gamePanel.direction = 'L';
                }
                break;
            case KeyEvent.VK_RIGHT:
                if (gamePanel.direction != 'L') {
                    gamePanel.direction = 'R';
                }
                break;
            case KeyEvent.VK_DOWN:
                if (gamePanel.direction != 'U') {
                    gamePanel.direction = 'D';
                }
                break;
            case KeyEvent.VK_UP:
                if (gamePanel.direction != 'D') {
                    gamePanel.direction = 'U';
                }
                break;
        }
    }

}
