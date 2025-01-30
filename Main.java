import javax.swing.*;
import java.awt.*;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        
        Panel panel = new Panel();
        JFrame game = new JFrame();
        game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        game.setTitle("SnakeBoost");
        Dimension dimension = new Dimension(620, 660);
        game.setPreferredSize(dimension);
        game.setResizable(false);
        game.getContentPane().setBackground(Color.BLACK);
        game.setLayout(null); // no layout manager being used
        game.pack();

        game.add(panel);
        game.setContentPane(panel);
        game.setVisible(true);
        game.setLocationRelativeTo(null);
    }
}
