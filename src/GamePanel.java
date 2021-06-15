import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    static int DELAY = 75;
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int bodyParts = 6;
    int applesEaten;

    int appleX;
    int appleY;
    int rabbitX;
    int rabbitY;
    int turtleX;
    int turtleY;

    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;
    boolean gameStarted = false;

    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter(this));
        timer = new Timer(DELAY, this);
        timer.stop();
        startGame();
    }

    public void startGame() {
        newApple();
        newRabbit();
        newTurtle();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        try {
            draw(g);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics g) throws IOException {
        if (!gameStarted) {
            preGame(g);

        } else if (running) {

            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i * UNIT_SIZE, i * SCREEN_WIDTH, i * UNIT_SIZE);
            }
            g.setColor(Color.red);

            BufferedImage applePng = ImageIO.read(GamePanel.class.getResourceAsStream("apple.png"));
            g.drawImage(applePng, appleX, appleY, UNIT_SIZE, UNIT_SIZE, null);

            g.setColor(Color.pink);
            BufferedImage bunnyPng = ImageIO.read(GamePanel.class.getResourceAsStream("bunny.png"));
            g.drawImage(bunnyPng, rabbitX, rabbitY, UNIT_SIZE, UNIT_SIZE, null);

            g.setColor(Color.GREEN.darker().darker().darker());
            BufferedImage turtPng = ImageIO.read(GamePanel.class.getResourceAsStream("turt.png"));
            g.drawImage(turtPng, turtleX, turtleY, UNIT_SIZE, UNIT_SIZE, null);

            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.yellow);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    g.setColor(new Color(45, 180, 0));
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            g.setColor(Color.cyan);
            g.setFont(new Font("Ink Free", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score:" + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score:" + applesEaten)) / 2, g.getFont().getSize());

        } else {
            gameOver(g);
        }
    }

    public void newApple() {
        appleX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        appleY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
    }

    public void newRabbit() {
        rabbitX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        rabbitY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
    }

    public void newTurtle() {
        turtleX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        turtleY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
    }

    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch (direction) {
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }
    }

    public void checkApple() {
        if ((x[0] == appleX) && (y[0] == appleY)) {
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }

    public void checkRabbit() {
        if ((x[0] == rabbitX) && (y[0] == rabbitY)) {
            bodyParts = bodyParts + 2;
            applesEaten = applesEaten + 2;

            if (DELAY > 20) {
                timer.stop();
                DELAY = DELAY - 5;
                timer = new Timer(DELAY, this);
                timer.start();
            }

            newRabbit();
        }
    }

    public void checkTurtle() {
        if ((x[0] == turtleX) && (y[0] == turtleY)) {

            if (DELAY < 250) {
                timer.stop();
                DELAY = DELAY + 10;
                timer = new Timer(DELAY, this);
                timer.start();
            }
            newTurtle();
        }
    }

    public void checkCollisions() {
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0]) == x[i] && (y[0] == y[i])) {
                running = false;
            }
        }

        if (x[0] < 0) {
            running = false;
        }

        if (x[0] > SCREEN_WIDTH) {
            running = false;
        }

        if (y[0] < 0) {
            running = false;
        }

        if (y[0] > SCREEN_HEIGHT) {
            running = false;
        }

        if (!running) {
            timer.stop();
        }
    }

    public void gameOver(Graphics g) {
        g.setColor(Color.cyan);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Score:" + applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score:" + applesEaten)) / 2, g.getFont().getSize());

        g.setColor(Color.cyan);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);
    }

    public void preGame(Graphics g) {

        g.setColor(Color.cyan);
        g.setFont(new Font("Ink Free", Font.BOLD, 50));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("~ SNAKE ~", (SCREEN_WIDTH - metrics2.stringWidth("~ SNAKE ~")) / 2, SCREEN_HEIGHT - SCREEN_HEIGHT + 100);
        g.setFont(new Font("Ink Free", Font.BOLD, 30));
        g.drawString("apple -> 1 point", (SCREEN_WIDTH - metrics2.stringWidth("-----------------")) / 2, SCREEN_HEIGHT - SCREEN_HEIGHT + 200);
        g.drawString("bunny -> faster + 2 points", (SCREEN_WIDTH - metrics2.stringWidth("-----------------")) / 2, SCREEN_HEIGHT - SCREEN_HEIGHT + 270);
        g.drawString("turtle -> slower", (SCREEN_WIDTH - metrics2.stringWidth("-----------------")) / 2, SCREEN_HEIGHT - SCREEN_HEIGHT + 340);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        g.drawString("Press any key to start.", (SCREEN_WIDTH - metrics2.stringWidth("---------------------")) / 2, SCREEN_HEIGHT - SCREEN_HEIGHT + 450);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkApple();
            checkRabbit();
            checkTurtle();
            checkCollisions();

        }
        repaint();
    }

}

