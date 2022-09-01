import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Menu extends JFrame implements ActionListener {

    private Panel panelBuffer;

    int menuWidth;
    int menuHeight;

    JButton play = new JButton("play");
    JButton settings = new JButton("settings");
    JButton exit = new JButton("exit");
    JButton mainMenu = new JButton("main menu");

    CardLayout layout = new CardLayout();

    JPanel panel = new JPanel();
    JPanel game = new JPanel();
    JPanel menu = new JPanel();

    public Menu(int menuWidth, int menuHeight) {
        this.menuWidth = menuWidth;
        this.menuHeight = menuHeight;


        panel.setLayout(layout);
        addButtons();

        setSize(menuWidth, menuHeight);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
        setTitle("BUILD YOUR EMPIRE");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        requestFocus();
    }
    private void addButtons() {


        //menu buttons
        menu.add(play);
        menu.add(settings);
        menu.add(exit);

        //game buttons
        game.add(mainMenu);

        play.addActionListener(this);
        this.add(play);
        settings.addActionListener(this);
        this.add(settings);
        exit.addActionListener(this);
        this.add(exit);
        mainMenu.addActionListener(this);
        this.add(mainMenu);

        play.setFocusable(false);
        settings.setFocusable(false);
        exit.setFocusable(false);
        mainMenu.setFocusable(false);

        //background colors
        game.setBackground(Color.MAGENTA);
        menu.setBackground(Color.GREEN);

        //adding children to parent Panel
        panel.add(menu,"Menu");
        panel.add(game,"Game");

        add(panel);
        layout.show(panel,"Menu");


    }

    public void actionPerformed1(ActionEvent e) {


        if (e.getSource() == exit) {
            System.exit(0);
        } else if (e.getSource() == play) {
            layout.show(panel, "Game");
            panelBuffer.startGame();
            this.setVisible(false);

        } else if (e.getSource() == settings){
            layout.show(panel, "Settings");
        } else if (e.getSource() == mainMenu){
            layout.show(panel, "Menu");
        }
        else {
            System.out.println("kerboodle");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub

    }
}
