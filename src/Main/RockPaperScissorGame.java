package Main;
import javax.naming.ldap.StartTlsRequest;
import javax.swing.*;
import javax.swing.plaf.ComponentInputMapUIResource;
import javax.swing.plaf.FontUIResource;
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

    private JPanel createGamePanel(){

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(59, 130, 246));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        //Top panel player information

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        playerLabel = new JLabel("Player's Turn");
        playerLabel.setFont(new Font("Arial", Font.BOLD, 28));
        playerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        triesLabel = new JLabel("Tries remaining: 0");
        triesLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        triesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        topPanel.add(playerLabel);
        topPanel.add(Box.createVerticalStrut(10));
        topPanel.add(triesLabel);


        JPanel centerPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));


        rockButton = createChoiceButtoon("Rock");
        paperButton = createChoiceButton("Paper");
        scissorsButton = createChoiceButton("Scissors");

        rockButton.addActionListener(e -> playRound("Rock"));
        paperButton.addActionListener(e -> playRound("Paper"));
        scissorsButton.addActionListener(e -> playRound("Scissors"));

        centerPanel.add(rockButton);
        centerPanel.add(paperButton);
        centerPanel.add(scissorsButton);

        resultLabel = new JLabel(" ");
        resultLabel.setFont(new Font("Arial", Font.BOLD, 24));
        resultLabel.setHorizontalAlignment(SwingConstants.CENTER);
        resultLabel.setOpaque(true);

        resultLabel.setBackground(Color.WHITE);
        resultLabel.setPreferredSize(new Dimension(0, 100));


        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        JLabel scoreLabel = new JLabel("ScoreBoard");
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 14));

        scoreboardArea = new JTextArea(8, 40);
        scoreboardArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        scoreboardArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(scoreboardArea);

        bottomPanel.add(scoreLabel, BorderLayout.NORTH);
        bottomPanel.add(scrollPane, BorderLayout.CENTER);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(resultLabel, BorderLayout.SOUTH);


        JPanel mainGamePanel = new JPanel(new BorderLayout());
        mainGamePanel.add(panel, BorderLayout.CENTER);
        mainGamePanel.add(bottomPanel, BorderLayout.SOUTH);

        return mainGamePanel;



    }

    private JButton createChoiceButton(String text){
        JButton button = new JButton("<html><center>"+ text+ "</center></html>");
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setBackground(new Color(99, 102, 241));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(30, 10, 30, 10));
        return button;

    }

    private JPanel createWinnerPanel(){

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(251, 146, 60));
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));


        JLabel titleLabel = new JLabel("Tournament complete");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        winnerLabel = new JLabel("Winner");
        winnerLabel.setFont(new Font("Arial", Font.BOLD, 30));
        winnerLabel.setForeground(Color.WHITE);
        winnerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(Box.createVerticalStrut(30));
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(30));
        panel.add(winnerLabel);
        panel.add(Box.createVerticalStrut(40));

        JPanel scoresPanel = new JPanel(new BorderLayout());
        scoresPanel.setBackground(Color.WHITE);
        scoresPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel finalScoresLabel = new JLabel("Final Scores");
        finalScoresLabel.setFont(new Font("Arial", Font.BOLD, 20));

        finalScoresArea = new JTextArea(10, 40);
        finalScoresArea.setFont(new Font("Monospaced", Font.PLAIN, 16));
        finalScoresArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(finalScoresArea);

        scoresPanel.add(finalScoresLabel, BorderLayout.NORTH);
        scoresPanel.add(scrollPane, BorderLayout.CENTER);

        JButton playAgainButton = new JButton("Play Again");
        playAgainButton.setFont(new Font("Arial", Font.BOLD, 18));
        playAgainButton.setBackground(new Color(147, 51, 234));
        playAgainButton.setForeground(Color.WHITE);
        playAgainButton.setFocusPainted(false);
        playAgainButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        playAgainButton.setMaximumSize(new Dimension(300, 50));
        playAgainButton.addActionListener(e -> resetGame());

        panel.add(scoresPanel);
        panel.add(Box.createVerticalStrut(20));
        panel.add(playAgainButton);

        return panel;

    }

    private void startGame(){

        try{
            numPlayers = Integer.parseInt(playersField.getText());
            triesPerPlayer = Integer.parseInt(triesField.getText());

            if(numPlayers <= 0 || triesPerPlayer <= 0){
                JOptionPane.showMessageDialog(this, "Please enter positive numbers!",
                        "Invalid Input",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            players = new ArrayList<>();
            scores = new HashMap<>();

            for(int i = 1; i <= numPlayers; i++){
                String playerName = "Player "+ i;
                players.add(playerName);
                scores.put(playerName, 0);
            }

            currentPlayerIndex = 0;
            currentTries = triesPerPlayer;

            updateGamePanel();
            cardLayout.show(mainPanel, "game");

        } catch (NumberFormatException e){
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid numbers!",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE);

        }
    }






}