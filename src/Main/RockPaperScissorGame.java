package Main;
import javax.swing.*;
import java.util.HashMap;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class RockPaperScissorGame extends JFrame {

    private int numPlayers;
    private int triesPerPlayer;
    private ArrayList<String> players;
    private HashMap<String, Integer> scores;

    private int currentPlayerIndex;

    private int currentTries;

    private JPanel mainPanel;
    private CardLayout cardLayout;

    //Panel components
    private JTextField playersField;
    private JTextField triesField;

    //Game panel component;

    private JLabel playerLabel;
    private JLabel triesLabel;
    private JLabel resultLabel;
    private JTextArea scoreboardArea;

    //Winner panel components

    private JLabel winnerLabel;
    private JTextArea finalScoresArea;

    /**
     * Constructor
     */

    public RockPaperScissorGame(){
        setTitle("Rock Paper Scissor Tournament");
        setSize(600, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(createSetupPanel(), "setup");
        mainPanel.add(createGamePanel(), "game");
        mainPanel.add(createWinnerPanel(), "winner");

        add(mainPanel);
        setVisible(true);

    }

    private JPanel createSetupPanel(){

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(147, 51, 234));


        JLabel titleLabel = new JLabel("Rock Paper Scissor");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);




    }





}