package Main;
import javax.naming.ldap.StartTlsRequest;
import javax.swing.*;
import java.lang.foreign.PaddingLayout;
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
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));


        JLabel titleLabel = new JLabel("Rock Paper Scissor");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Tournament Mode");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        subtitleLabel.setForeground(Color.WHITE);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(Box.createVerticalStrut(30));
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(subtitleLabel);
        panel.add(Box.createVerticalStrut(50));

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel playersLabel = new JLabel("Number of Players");
        playersLabel.setFont(new Font("Arial", Font.BOLD, 16));

        playersField = new JTextField(10);
        playersField.setFont(new Font("Arial", Font.PLAIN, 16));
        playersField.setMaximumSize(new Dimension(400, 40));

        JLabel triesLabel = new JLabel("Tries per Player");
        triesLabel.setFont(new Font("Arial", Font.BOLD, 16));
        triesField = new JTextField(10);
        triesField.setFont(new Font("Arial", Font.PLAIN, 18));
        triesField.setMaximumSize(new Dimension(400, 40));

        JButton startButton = new JButton("Start Tournament");
        startButton.setFont(new Font("Arial", Font.BOLD, 18));
        startButton.setBackground(new Color(147, 51, 234));
        startButton.setForeground(Color.WHITE);
        startButton.setFocusPainted(false);
        startButton.setMaximumSize(new Dimension(400, 50));
        startButton.addActionListener(e -> startGame());


        formPanel.add(playersLabel);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(playersField);
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(triesLabel);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(triesField);
        formPanel.add(Box.createVerticalStrut(30));
        formPanel.add(startButton);

        panel.add(formPanel);
        return panel;



    }





}