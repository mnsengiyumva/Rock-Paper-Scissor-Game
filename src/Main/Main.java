package Main;

import javax.swing.*;

public class Main{

    public static void main(String[] args){

        SwingUtilities.invokeLater(() -> new RockPaperScissorGame());

    }
}

//    private JPanel createGamePanel(){
//
//        JLayeredPane layeredPane = new JLayeredPane();
//        layeredPane.setPreferredSize(new Dimension(700, 800));
//
//
//        JPanel panel = new ImagePanel("/images/background.jpg");
//        panel.setLayout(new BorderLayout(10, 10));
//        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
//        panel.setBounds(0, 0, 700, 800);
//
//        //Top panel player information
//
//        JPanel topPanel = new JPanel();
//        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
//        topPanel.setOpaque(false);
//        topPanel.setBackground(new Color(255,255,255,180));
//        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
//
//        avatarLabel = new JLabel();
//        avatarLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
//
//        playerLabel = new JLabel("Player's Turn");
//        playerLabel.setFont(new Font("Arial", Font.BOLD, 28));
//        playerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
//        playerLabel.setForeground(Color.white);
//
//
//        triesLabel = new JLabel("Tries remaining: 0");
//        triesLabel.setFont(new Font("Arial", Font.PLAIN, 18));
//        triesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
//        triesLabel.setForeground(Color.white);
//
//        countdownLabel = new JLabel("");
//        countdownLabel.setFont(new Font("Arial", Font.BOLD, 72));
//        countdownLabel.setForeground(new Color(255, 0, 0, 200));
//        countdownLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
//
//        topPanel.add(avatarLabel);
//        topPanel.add(Box.createVerticalStrut(10));
//        topPanel.add(playerLabel);
//        topPanel.add(Box.createVerticalStrut(10));
//        topPanel.add(triesLabel);
//        topPanel.add(countdownLabel);
//
//        centerPanel = new JPanel();
//        centerPanel.setLayout(new GridLayout(1, 3, 15, 0)); // Will change to 1,5 in extended mode
//        centerPanel.setOpaque(false);
//        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
//
//        rockButton = createChoiceButton("Rock", "rock.png");
//        paperButton = createChoiceButton("Paper", "paper.png");
//        scissorsButton = createChoiceButton("Scissors", "scissors.png");
//        lizardButton = createChoiceButton("Lizard", "lizard.png");
//        spockButton = createChoiceButton("Spock", "spock.png");
//
//        rockButton.addActionListener(e -> { playSound("click.wav"); playRound("Rock"); });
//        paperButton.addActionListener(e -> { playSound("click.wav"); playRound("Paper"); });
//        scissorsButton.addActionListener(e -> { playSound("click.wav"); playRound("Scissors"); });
//        lizardButton.addActionListener(e -> { playSound("click.wav"); playRound("Lizard"); });
//        spockButton.addActionListener(e -> { playSound("click.wav"); playRound("Spock"); });
//
//        centerPanel.add(rockButton);
//        centerPanel.add(paperButton);
//        centerPanel.add(scissorsButton);
//        // Lizard and Spock added dynamically based on mode
//
//
//        rockButton = createChoiceButton("Rock", "rock.png");
//        paperButton = createChoiceButton("Paper", "paper.png");
//        scissorsButton = createChoiceButton("Scissors", "scissors.png");
//
//
//
//        rockButton.setMaximumSize(new Dimension(10, 0));
//        rockButton.setFont(new Font("Arial", Font.BOLD, 20));
//        rockButton.setForeground(Color.WHITE);
//        rockButton.setFocusPainted(false);
//        //rockButton.addActionListener(e -> createChoiceButton("Rock", "rock.png"));
//        rockButton.setBorder(new RoundedBorder(80, Color.WHITE));
//
//        paperButton.setMaximumSize(new Dimension(10, 0));
//        paperButton.setFont(new Font("Arial", Font.BOLD, 20));
//        paperButton.setForeground(Color.WHITE);
//        paperButton.setFocusPainted(false);
//        //paperButton.addActionListener(e -> createChoiceButton("Rock", "paper.png"));
//        paperButton.setBorder(new RoundedBorder(80, Color.WHITE));
//
//        scissorsButton.setMaximumSize(new Dimension(10, 0));
//        scissorsButton.setFont(new Font("Arial", Font.BOLD, 20));
//        scissorsButton.setForeground(Color.WHITE);
//        //scissorsButton.setFocusPainted(false);scissorsButton.addActionListener(e -> createChoiceButton("Scissors", "scissors.png"));
//        scissorsButton.setBorder(new RoundedBorder(80, Color.WHITE));
//
//        rockButton.setForeground(Color.WHITE);
//        paperButton.setForeground(Color.WHITE);
//        scissorsButton.setForeground(Color.WHITE);
//
//        rockButton.addActionListener(e -> playRound("Rock"));
//        paperButton.addActionListener(e -> playRound("Paper"));
//        scissorsButton.addActionListener(e -> playRound("Scissors"));
//
//        centerPanel.add(rockButton);
//        centerPanel.add(paperButton);
//        centerPanel.add(scissorsButton);
//
//        resultLabel = new JLabel(" ");
//        resultLabel.setFont(new Font("Arial", Font.BOLD, 24));
//        resultLabel.setHorizontalAlignment(SwingConstants.CENTER);
//        resultLabel.setOpaque(true);
//        resultLabel.setBackground(new Color(255, 255, 255, 220));
//        resultLabel.setPreferredSize(new Dimension(0, 80));
//
//
//        resultLabel.setBackground(new Color(255, 255, 255, 100));
//
//        resultLabel.setPreferredSize(new Dimension(0, 100));
//
//
//        JPanel bottomPanel = new JPanel(new BorderLayout());
//        bottomPanel.setOpaque(false);
//        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
//
//        JLabel scoreLabel = new JLabel("ScoreBoard");
//        scoreLabel.setFont(new Font("Arial", Font.BOLD, 14));
//
//
//
//        scoreboardArea = new JTextArea(8, 40);
//        scoreboardArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
//        scoreboardArea.setEditable(false);
//        JScrollPane scrollPane = new JScrollPane(scoreboardArea);
//
//        // ADD THIS after scoreboardArea and scrollPane:
//        bracketDisplay = new JTextArea(6, 40);
//        bracketDisplay.setFont(new Font("Monospaced", Font.PLAIN, 12));
//        bracketDisplay.setEditable(false);
//        JScrollPane bracketScroll = new JScrollPane(bracketDisplay);
//
//        // Update bottomPanel to show bracket in tournament mode
//        JTabbedPane tabbedPane = new JTabbedPane();
//        tabbedPane.addTab("Scoreboard", scrollPane);
//        tabbedPane.addTab("Bracket", bracketScroll);
//
//        bottomPanel.add(scoreLabel, BorderLayout.NORTH);
//        bottomPanel.add(tabbedPane, BorderLayout.CENTER); // Replace: bottomPanel.add(scrollPane, ...)
//
//        bottomPanel.add(scoreLabel, BorderLayout.NORTH);
//        bottomPanel.add(scrollPane, BorderLayout.CENTER);
//
//        panel.add(topPanel, BorderLayout.NORTH);
//        panel.add(centerPanel, BorderLayout.CENTER);
//        panel.add(resultLabel, BorderLayout.SOUTH);
//
//
//        JPanel mainGamePanel = new JPanel(new BorderLayout());
//
//        mainGamePanel.add(panel, BorderLayout.CENTER);
//        mainGamePanel.add(bottomPanel, BorderLayout.SOUTH);
//        mainGamePanel.setOpaque(true);
//        mainGamePanel.setBounds(0,0,700,800);
//
//        particlePanel = new ParticlePanel();
//        particlePanel.setBounds(0, 0, 700, 800);
//
//        layeredPane.add(mainGamePanel, Integer.valueOf(0));
//        layeredPane.add(particlePanel, Integer.valueOf(1));
//
//        JPanel wrapper = new JPanel(new BorderLayout());
//        wrapper.add(layeredPane);
//        return wrapper;
//
//
//    }