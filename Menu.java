import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// has not yet been added into panel container

public class Menu {

    Panel panelObj = new Panel();

    public Rectangle playButton = new Rectangle(panelObj.SCREEN_WIDTH / 2 + 120, 150, 100, 50);
    public Rectangle helpButton = new Rectangle(panelObj.SCREEN_WIDTH / 2 + 240, 150, 100, 50);
    public Rectangle quitButton = new Rectangle(panelObj.SCREEN_WIDTH / 2 + 360, 150, 100, 50);


    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        Font font0 = new Font("Monaco", Font.BOLD, 50);
        g.setFont(font0);
        g.setColor(Color.WHITE);
        g.drawString("Snake Boost!", panelObj.SCREEN_WIDTH / 2, 100);

        Font font1 = new Font("Merryweather", Font.BOLD, 30);
        g2d.setFont(font1);
        g2d.drawString("Play", playButton.x, playButton.y);
        g2d.draw(playButton);
        g2d.drawString("Help",  helpButton.x, helpButton.y);
        g2d.draw(helpButton);
        g2d.drawString("Quit", quitButton.x, quitButton.y);
        g2d.draw(quitButton);

    }
}
