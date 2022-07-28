import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;
import java.util.List;

public class Panel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    Dimension dimension = new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT);
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    final int[] x = new int[GAME_UNITS];
    final int[] y = new int[GAME_UNITS];
    int bodyParts = 6;
    int applesEaten = 0;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    final int DELAY = 75;
    Random random;


    LOOOOOOOOOOOOOOOOOOOL
    LOOOOOOOOOOOOOOOL
            

    // enemy
    final int[] enemyX = new int[GAME_UNITS];
    final int[] enemyY = new int[GAME_UNITS];
    int enemyBody = 3;
    char enemyDirection = 'D';

    char[] enemyVect = {'U','D','L','R'};
    long buffMoves = 0;  // used to change enemy's direction

    // button
    JButton retryButton;
    JButton exitButton;
    int timeAlive = 0;


    // sprite boxes

    Rectangle playerBox;
    Rectangle enemyBox;

    // menu
    Rectangle startButton = new Rectangle(150, 100, 100, 25);
    Rectangle quitButton = new Rectangle(150, 150, 100, 25);

    Panel() {
        super();

        exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Monaco", Font.PLAIN, 20));
        exitButton.setBounds((SCREEN_WIDTH / 2 ) + 50, (SCREEN_HEIGHT / 2) + 200, 200, 50);
        exitButton.setFocusable(false);
        exitButton.setVisible(true);
        exitButton.setEnabled(true);

        random = new Random();
        this.setPreferredSize(dimension);
        this.setBackground(Color.BLACK);
        this.setOpaque(true);
       // this.setBounds(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT); // same size as window  -> white bg but grid displayed
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        this.addKeyListener(new KeyClicked());

        startGame();

        this.setVisible(true);
    }
    public void menu(Graphics g) {
        if (!running) {
            g.setFont(new Font("Monaco", Font.BOLD, 26));
            g.setColor(Color.WHITE);
            g.drawString("Snake REVAMP", 125, 75);
            g.setColor(Color.CYAN);
            g.fillRect(startButton.x, startButton.y, startButton.width, startButton.height);
            g.setFont(new Font("Monaco", Font.BOLD, 12));
            g.setColor(Color.GRAY);
            g.drawString("Start Game", startButton.x+20, startButton.y+17);
            g.setColor(Color.CYAN);
            g.setColor(Color.PINK);
            g.fillRect(quitButton.x, quitButton.y, quitButton.x+20, quitButton.y+17);
            g.setColor(Color.GRAY);
            g.drawString("Quit Game", quitButton.x+20, quitButton.y+17);
        }
    }

    public void startGame() {
        newApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g) {
        menu(g);
        if (running) {
            for (int i = 0; i < SCREEN_WIDTH / UNIT_SIZE; i++) {
                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);

                for (int j = 0; j < SCREEN_HEIGHT / UNIT_SIZE; j++) {
                    g.drawLine(j * UNIT_SIZE, 0, j * UNIT_SIZE, SCREEN_HEIGHT);
                }
            }
            g.setColor(Color.RED);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            // drawing snake
            for (int i = 0; i <= bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.BLUE);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
                playerBox = new Rectangle(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            }


            // drawing enemy
            for (int i = 0; i < enemyBody; i++) {
                if (i == 0) {
                    g.setColor(new Color(47, 152, 47));
                    //g.fillOval(enemyX[i], enemyY[i], UNIT_SIZE, UNIT_SIZE);
                    g.fillOval(SCREEN_WIDTH / 2 , enemyY[i], UNIT_SIZE, UNIT_SIZE);


/*                    g.translate(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2);*/
                } else {
                    g.setColor(new Color(47, 152, 47));
               //     g.fillOval(enemyX[i], enemyY[i], UNIT_SIZE, UNIT_SIZE);
                    g.fillOval(SCREEN_WIDTH / 2 , enemyY[i], UNIT_SIZE, UNIT_SIZE);
/*                    g.translate(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2);*/
                }
                enemyBox = new Rectangle(SCREEN_WIDTH / 2, enemyY[i], UNIT_SIZE, UNIT_SIZE);
            }
        }
        else {
            gameOver(g);
        }
    }
    public void newApple() {
        appleX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        appleY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
    }
    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i-1];
            y[i] = y[i-1];
            System.out.println("{Snake} " +  "     " + "x: " + x[i] + " " + "y: " + y[i]);
        }
        switch (direction) {
            case 'U': y[0] = y[0] - UNIT_SIZE; break;
            case 'D': y[0] = y[0] + UNIT_SIZE; break;
            case 'L': x[0] = x[0] - UNIT_SIZE; break;
            case 'R': x[0] = x[0] + UNIT_SIZE;
        }
        // enemy movement
        for (int j = enemyBody; j > 0; j--) {
            enemyY[j] = enemyY[j-1];
            System.out.println("{Enemy} " +  "     " +  "x: " + enemyX[j] + " " + "y: " + enemyY[j]);
            if (enemyY[j] == SCREEN_HEIGHT) {
                enemyY[j] = enemyY[j+1];
            }
        }


        // enemy direction
        switch (enemyDirection) {
            case 'U': enemyY[0] = enemyY[0] - UNIT_SIZE;
            if (enemyY[0] == 0)
                enemyDirection = 'D';
            break;
            case 'D': enemyY[0] = enemyY[0] + UNIT_SIZE;
            if (enemyY[0] == SCREEN_HEIGHT)
                enemyDirection = 'U';
            break;
            case 'L': enemyX[0] = enemyX[0] - UNIT_SIZE; break;
            case 'R': enemyX[0] = enemyX[0] + UNIT_SIZE;
        }
    }
    public void checkApple() {
        if ((x[0] == appleX) && (y[0] == appleY)) {
            applesEaten++;
            bodyParts++;
            newApple();
        }
    }

    public void checkCollisions() {
        // check if head touches body
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
                break;
            }
        }
        // check if head touches borders
        if (x[0] < 0) {
            running = false; // if touches left border
        }
        if (x[0] > SCREEN_WIDTH) { // if touches right border
            running = false;
        }
        if (y[0] < 0) { // if touches top border
            running = false;
        }
        if (y[0] > SCREEN_HEIGHT) { // if touches bottom border
            running = false;
        }

/*        if ((x[0] == enemyX[0]) && (y[0] == enemyY[0])) {
            running = false;
        }*/
/*        for (int i = 0; i <= bodyParts; i++) {
            for (int j = 0; j <= enemyBody; j++) {
                if (x[i] == enemyX[j] && y[i] == enemyY[j]) {
                    running = false;
                }
            }
        }*/

        if (playerBox.intersects(enemyBox)) {
            System.out.println("BOXES INTERSECTS");
            running = false;
        }
        for (int i = 0; i <= bodyParts; i++) {
            for (int j = 0; j <= enemyBody; j++) {
                if (x[i] == enemyY[j] && y[i] == enemyY[j]) {
                    System.out.println("COLLIDES");
                    running = false;
                }
            }
            break;
        }
    }
    public void gameOver(Graphics g) {
        // game over text
        g.setColor(Color.RED);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics fontMetrics0 = getFontMetrics(g.getFont());
        g.drawString("Game Over!", (SCREEN_WIDTH - fontMetrics0.stringWidth("Game Over")) / 2 , 200); // center text

        g.setColor(Color.RED);
        g.setFont(new Font("Ink Free", Font.BOLD, 30));
        FontMetrics fontMetrics1 = getFontMetrics(g.getFont()); // getFont() uses font specified
        g.drawString("Apples Eaten: " + applesEaten, + (SCREEN_WIDTH - fontMetrics1.stringWidth("Apples Eaten: " + applesEaten)) / 2, 300);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Interstate", Font.BOLD, 30));
        g.drawString("Press SPACE to restart", 150 , SCREEN_HEIGHT / 2 + 100);

        // button
/*        retryButton.addActionListener(this);
        this.add(retryButton);*/

        exitButton.addActionListener(this);
        this.add(exitButton);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
        if (e.getSource() == retryButton) {
            //   System.out.println("you clicked try again...");
            restart();
        } else if (e.getSource() == exitButton) {
            System.exit(0);
        }
    }


    public void restart() {
        this.remove(new JFrame());
        this.remove(new Panel());
        SwingUtilities.updateComponentTreeUI(this);
        Panel game = new Panel();
        this.add(game);
        game.grabFocus();         // trigger refocus on new panel*/
        exitButton.setVisible(false);
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch(e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') {
                        direction = 'R'; }
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') {
                        direction = 'D';
                    }
                    break;
/*                case KeyEvent.VK_SPACE: //  try to separate out into another keyAdapter class
                    restart();
                    repaint();*/
            }
        }
    }
    public class KeyClicked extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {

            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                restart();
                repaint();
            }
        }
    }
}

