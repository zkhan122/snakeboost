import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

public class Panel extends JPanel implements ActionListener, ChangeListener {

    final int SCREEN_WIDTH = 600;
    final int SCREEN_HEIGHT = 600;
    Dimension dimension = new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT);
    final int UNIT_SIZE = 25;
    final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
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
    JLabel sliderLabel;
    JSlider slider;
    JButton confirmButton;


    // sprite boxes

    Rectangle playerBox;
    Rectangle enemyBox;

    // menu
    Rectangle startButton = new Rectangle(150, 100, 100, 25);
    Rectangle quitButton = new Rectangle(150, 150, 100, 25);

    // poison apples
    int pXPos;
    int pYPos;

    // hold x, y coordinates for pA in buffer
    ArrayList<Integer> poisonAppleX = new ArrayList<Integer>();
    ArrayList<Integer> poisonAppleY = new ArrayList<Integer>();

    // rand bounds
    int xRB = 0;
    int yRB = 0;

    // initializing wall image var
    BufferedImage wallImgBuff;
    BufferedImage vWallImgBuff;
    JLabel wallImgLabel_horizontal;
    JLabel wallImgLabel_horizontal2;
    JLabel wallImgLabel_vertical;
    JLabel wallImgLabel_vertical2;
    ArrayList<JLabel> walls = new ArrayList<JLabel>();
    ImageIcon wallIcon;
    Image wallImage;
    Image vWallImage; // vertical wall image

    static boolean showMenu = false;




    Panel() {
        super();

        exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Monaco", Font.PLAIN, 20));
        exitButton.setBounds((SCREEN_WIDTH / 2 ) + 50, (SCREEN_HEIGHT / 2) + 200, 200, 50);
        exitButton.setFocusable(false);
/*        exitButton.setVisible(true);*/
        exitButton.setEnabled(true);
        this.add(exitButton);

        // slider
        slider = new JSlider(JSlider.CENTER, 0, 10, 1);
        slider.setPreferredSize(new Dimension(400, 200));
        slider.setPaintTicks(true);
        slider.setMinorTickSpacing(1);
        slider.setPaintTrack(true);
        slider.setMajorTickSpacing(1);
        slider.setPaintLabels(true);
        slider.setFont(new Font("Monaco", Font.PLAIN, 15));
        // slider.setOrientation(SwingConstants.VERTICAL); // sets slider vertically
        slider.addChangeListener(this);

        // lobels and buttons on slider
        sliderLabel = new JLabel();
        sliderLabel.setPreferredSize(new Dimension(450, 50));
        sliderLabel.setForeground(Color.BLACK);
        sliderLabel.setBackground(Color.WHITE);
        sliderLabel.setOpaque(true);
        sliderLabel.setHorizontalAlignment(JLabel.CENTER);
        sliderLabel.setVerticalAlignment((JLabel.CENTER));
        sliderLabel.setVisible(true);


        confirmButton = new JButton();
        confirmButton.setFont(new Font("Monaco", Font.PLAIN, 20));
        confirmButton.setBounds(100, 200, 100, 100);
        confirmButton.setText("Generate");
        confirmButton.setFocusable(false);
        confirmButton.setHorizontalAlignment(JLabel.CENTER);
        confirmButton.setVerticalAlignment(JLabel.CENTER);
        confirmButton.setForeground(Color.BLACK);
        confirmButton.setBackground(Color.LIGHT_GRAY);
        confirmButton.setBorder(BorderFactory.createEtchedBorder());

        confirmButton.setVisible(true);
        confirmButton.setEnabled(true);


        random = new Random();
        this.setPreferredSize(dimension);
        this.setBackground(Color.BLACK);
        this.setOpaque(true);

        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        this.addKeyListener(new KeyClicked());

        this.add(slider);
        confirmButton.addActionListener(this);
        this.add(confirmButton);
        this.add(sliderLabel);
        sliderLabel.setFont(new Font("Monaco", Font.PLAIN, 25));
        sliderLabel.setText("Generate " + slider.getValue() + " apples");

        this.setVisible(true);
    }

    public void mainMenu() {
        showMenu = true;
        Menu menu = new Menu();

    }

    public void startGame() {
        running = true;
        slider.setVisible(false);
        sliderLabel.setVisible(false);

        confirmButton.setVisible(false);
        exitButton.setVisible(false);
        newApple();
        //    drawWalls();

        grabFocus();

        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);

    }

    public void draw(Graphics g) {

        if (running) {
            for (int i = 0; i < SCREEN_WIDTH / UNIT_SIZE; i++) {
                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
                if (i == 9) {
                    xRB = i;
                }                                                 // 24 x 24 grid
                for (int j = 0; j < SCREEN_HEIGHT / UNIT_SIZE; j++) {
                    g.drawLine(j * UNIT_SIZE, 0, j * UNIT_SIZE, SCREEN_HEIGHT);
                    if (j == 5) {
                        yRB = j;
                    }
                }
            }


            g.setColor(Color.RED);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            g.setColor(new Color(187, 29, 219));
            /*            g.fillOval(pXPos, pYPos, UNIT_SIZE, UNIT_SIZE);*/

            for (int x = 0; x <= slider.getValue(); x++) {
                pXPos = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
                if (x == xRB) {
                    pXPos = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
                }
                poisonAppleX.add(pXPos);
            }

            // test
            System.out.println();
            System.out.println("X coordinate buffer");
            System.out.println(poisonAppleX);


            for (int y = 0; y <= slider.getValue(); y++) {
                pYPos = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
                if (y == yRB) {
                    pYPos = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
                }
                poisonAppleY.add(pYPos);
            }


            System.out.println();
            System.out.println("Y Coordinate buffer");
            System.out.println(poisonAppleY);

            int tempX = 0;
            int tempY = 0;

            for (int coord = 0; coord < slider.getValue(); coord++) {
                g.fillOval(poisonAppleX.get(coord), poisonAppleY.get(coord), UNIT_SIZE, UNIT_SIZE);
                tempX = poisonAppleX.get(coord);
                tempY = poisonAppleY.get(coord);

                // checking poison apple collisions
                if (x[0] == tempX && y[0] == tempY) {
                    running = false;
                    System.out.println("PURPLE RAIN");
                }
            }


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

    public BufferedImage loadImage(String filename) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(filename));
        }
        catch(IOException e) {

        }
        return image;
    }



    public void drawWalls(boolean show) {
        // wall image
        wallImgBuff = null;
        try {
            wallImgBuff = ImageIO.read(new File("assets\\wallHorizontal.jpg")); // loading image
        }
        catch (IOException e) {
            e.printStackTrace();
            System.out.println("Image not processed");
        }

        vWallImgBuff = null;
        try {
            vWallImgBuff = ImageIO.read(new File("\\assets\\wallVertical.jpg"));
        }
        catch (IOException e) {
            e.printStackTrace();
            System.out.println("Image not processed");

        }

        if (show) {

            wallImgLabel_horizontal = new JLabel(new ImageIcon(wallImgBuff.getScaledInstance(wallImgBuff.getWidth() - 140, wallImgBuff.getHeight() - 30, Image.SCALE_FAST)));
            wallImgLabel_horizontal.setBounds(0, 0, wallImgBuff.getWidth() - 140, wallImgBuff.getHeight() - 30);
            this.add(wallImgLabel_horizontal);

            wallImgLabel_horizontal2 = new JLabel(new ImageIcon(wallImgBuff.getScaledInstance(wallImgBuff.getWidth() - 140, wallImgBuff.getHeight() + 30, Image.SCALE_FAST)));
            wallImgLabel_horizontal2.setBounds(0, SCREEN_HEIGHT - 5, wallImgBuff.getWidth() - 140, wallImgBuff.getHeight() + 30);
            this.add(wallImgLabel_horizontal2);

            // vertical walls
            wallImgLabel_vertical = new JLabel(new ImageIcon(vWallImgBuff.getScaledInstance(vWallImgBuff.getWidth() - 20, vWallImgBuff.getHeight(), Image.SCALE_FAST))); // bug at - 30 (at 0)
            wallImgLabel_vertical.setBounds(1, 0, vWallImgBuff.getWidth() - 20, vWallImgBuff.getHeight());
            this.add(wallImgLabel_vertical);

            wallImgLabel_vertical2 = new JLabel(new ImageIcon(vWallImgBuff.getScaledInstance(vWallImgBuff.getWidth() + 250, vWallImgBuff.getHeight(), Image.SCALE_FAST)));
            wallImgLabel_vertical2.setBounds(SCREEN_WIDTH - 25, 0, vWallImgBuff.getWidth() + 250, vWallImgBuff.getHeight());
            this.add(wallImgLabel_vertical2);
        }
    }

    public void newApple() {
        appleX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        if (appleX == pXPos) {
            appleX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        }
        if (appleY == pYPos) {
            appleY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
        }
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
    public void checkPoisonApple() {
        for (int i = 0; i < poisonAppleX.size(); i++) {
            if (x[0] == pXPos && y[0] == pYPos) {
                System.out.println("PURPLE RAIN");
                running = false;
            }
            break;
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

        if (playerBox != null && playerBox.intersects(enemyBox)) {
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
        g.drawString("collect the  ", 150, SCREEN_HEIGHT / 2 + 50);
        g.setColor(Color.RED);
        g.drawString("RED", (SCREEN_WIDTH / 2) + 10 , SCREEN_HEIGHT / 2 + 50);
        g.setColor(Color.WHITE);
        g.drawString("apples!", (SCREEN_WIDTH / 2) + 80, SCREEN_HEIGHT / 2 + 50);

        g.setColor(new Color(195, 20, 230));
        g.setFont(new Font("Interstate", Font.BOLD, 30));
        g.drawString("Don't hit the walls!", 130 , SCREEN_HEIGHT / 2 + 100);
        g.drawString("or the PURPLE APPLES!", 130 , SCREEN_HEIGHT / 2 + 150);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Interstate", Font.BOLD, 30));
        g.drawString("Press SPACE to restart", 130 , SCREEN_HEIGHT / 2 + 220);

        // button

        exitButton.addActionListener(this);
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
        } else if (e.getSource() == confirmButton) {
            if (confirmButton.getModel().isArmed()) {
                startGame();

            } else {
                running = false;
            }
        }
    }


    public void restart() {
        this.remove(new JFrame());
        this.remove(new Panel());
        SwingUtilities.updateComponentTreeUI(this);
        Panel game = new Panel();
        this.add(game);
        game.grabFocus();         // trigger refocus on new panel*/
        repaint();
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        sliderLabel.setText("Generate " + slider.getValue() + " apples");

    }

    // keyboard event input
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

