package Main;

import org.w3c.dom.events.MouseEvent;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.util.HashMap;
import java.awt.*;
import java.util.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import java.util.List;
import java.io.*;
import javax.sound.sampled.*;


class RoundedBorder extends AbstractBorder {
    private int radius;
    private Color color;

    RoundedBorder(int radius, Color color) {
        this.color = color;
        this.radius = radius;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(color);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
    }
}

class ImagePanel extends JPanel{

    private Image backgroundImage;

    ImagePanel(String imagePath){

        try{
            ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource(imagePath)));
            backgroundImage = icon.getImage();
        } catch (Exception e){
            System.out.println("Background image not found: "+imagePath);
        }
        setOpaque(false);
    }

    public boolean isOpaque(){
        return false;
    }

    protected void paintComponent(Graphics g){
        super.paintComponent(g);

        if(backgroundImage != null){
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
        else{
            g.setColor(new Color(59, 130, 246));
            g.fillRect(0,0,getWidth(),getHeight());
        }

        super.paintComponent(g);
    }
}


class PlayerStats {

    String name;
    int totalWins = 0;
    int totalLosses = 0;
    int totalTies = 0;
    int rockWins = 0;
    int paperWins = 0;
    int scissorsWins = 0;
    int longestWinStreak = 0;
    int currentWinStreak = 0;
    String favoriteChoice = "Rock";
    HashMap<String, Integer> choiceCount = new HashMap<>();
    ImageIcon avatar;
    int lizardWins = 0;
    int spockWins = 0;

    PlayerStats(String name) {
        this.name = name;
        choiceCount.put("Rock", 0);
        choiceCount.put("Paper", 0);
        choiceCount.put("Scissors", 0);
        choiceCount.put("Lizard", 0);   // ADD
        choiceCount.put("Spock", 0);

    }

    void recordWin(String choice) {
        totalWins++;
        currentWinStreak++;
        if (currentWinStreak > longestWinStreak) {
            longestWinStreak = currentWinStreak;
        }

        if (choice.equals("Rock")) rockWins++;
        else if (choice.equals("Paper")) paperWins++;
        else if (choice.equals("Scissors")) scissorsWins++;
        else if (choice.equals("Lizard")) lizardWins++;  // ADD
        else if (choice.equals("Spock")) spockWins++;

        recordChoice(choice);
    }

    void recordLoss(String choice) {
        totalLosses++;
        currentWinStreak = 0;
        recordChoice(choice);
    }

    void recordTie(String choice) {
        totalTies++;
        recordChoice(choice);
    }

    void recordChoice(String choice) {
        choiceCount.put(choice, choiceCount.get(choice) + 1);
        updateFavoriteChoice();
    }

    void updateFavoriteChoice() {
        int maxCount = 0;
        for (Map.Entry<String, Integer> entry : choiceCount.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                favoriteChoice = entry.getKey();
            }
        }
    }

    int getTotalGames() {
        return totalWins + totalLosses + totalTies;
    }

    double getWinRate() {
        int total = getTotalGames();
        return total > 0 ? (totalWins * 100.0 / total) : 0;
    }
}

// ============================================================================
// PARTICLE EFFECT CLASS (for celebrations)
// ============================================================================
class Particle {
    double x, y;
    double vx, vy;
    Color color;
    int size;
    int life;

    Particle(double x, double y) {
        this.x = x;
        this.y = y;
        this.vx = (Math.random() - 0.5) * 10;
        this.vy = (Math.random() - 0.5) * 10 - 5;
        this.color = new Color(
                (int)(Math.random() * 255),
                (int)(Math.random() * 255),
                (int)(Math.random() * 255)
        );
        this.size = (int)(Math.random() * 8) + 3;
        this.life = 100;
    }

    void update() {
        x += vx;
        y += vy;
        vy += 0.3; // gravity
        life--;
    }

    boolean isAlive() {
        return life > 0;
    }

    void draw(Graphics g) {
        g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(),
                Math.min(255, life * 2)));
        g.fillOval((int)x, (int)y, size, size);
    }
}

class ParticlePanel extends JPanel {
    private List<Particle> particles = new ArrayList<>();
    private Timer animationTimer;

    ParticlePanel() {
        setOpaque(false);
        animationTimer = new Timer(30, e -> {
            particles.removeIf(p -> !p.isAlive());
            particles.forEach(Particle::update);
            repaint();
            if (particles.isEmpty()) {
                ((Timer)e.getSource()).stop();
            }
        });
    }

    void explode(int x, int y) {
        for (int i = 0; i < 50; i++) {
            particles.add(new Particle(x, y));
        }
        animationTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        particles.forEach(p -> p.draw(g));
    }
}

// ============================================================================
// TOURNAMENT BRACKET CLASSES
// ============================================================================
class BracketMatch {
    String player1;
    String player2;
    String winner;
    int round;
    boolean completed;

    BracketMatch(String p1, String p2, int round) {
        this.player1 = p1;
        this.player2 = p2;
        this.round = round;
        this.completed = false;
    }
}

class TournamentBracket {
    ArrayList<ArrayList<BracketMatch>> rounds;
    ArrayList<String> participants;
    int currentRound;
    int currentMatchInRound;

    TournamentBracket(ArrayList<String> players) {
        this.participants = new ArrayList<>(players);
        this.rounds = new ArrayList<>();
        this.currentRound = 0;
        this.currentMatchInRound = 0;
        generateBracket();
    }

    void generateBracket() {
        // Pad participants to next power of 2
        int targetSize = 1;
        while (targetSize < participants.size()) {
            targetSize *= 2;
        }

        while (participants.size() < targetSize) {
            participants.add("BYE");
        }

        // Generate first round matches
        ArrayList<BracketMatch> firstRound = new ArrayList<>();
        for (int i = 0; i < participants.size(); i += 2) {
            firstRound.add(new BracketMatch(participants.get(i), participants.get(i + 1), 1));
        }
        rounds.add(firstRound);

        // Generate subsequent rounds
        int roundNum = 2;
        int matchCount = firstRound.size() / 2;

        while (matchCount >= 1) {
            ArrayList<BracketMatch> round = new ArrayList<>();
            for (int i = 0; i < matchCount; i++) {
                round.add(new BracketMatch("TBD", "TBD", roundNum));
            }
            rounds.add(round);
            matchCount /= 2;
            roundNum++;
        }
    }

    BracketMatch getCurrentMatch() {
        if (currentRound >= rounds.size()) return null;
        ArrayList<BracketMatch> round = rounds.get(currentRound);
        if (currentMatchInRound >= round.size()) return null;
        return round.get(currentMatchInRound);
    }

    void recordMatchWinner(String winner) {
        BracketMatch match = getCurrentMatch();
        if (match == null) return;

        match.winner = winner;
        match.completed = true;

        // Advance to next match or round
        currentMatchInRound++;
        if (currentMatchInRound >= rounds.get(currentRound).size()) {
            advanceWinnersToNextRound();
            currentRound++;
            currentMatchInRound = 0;
        }
    }

    void advanceWinnersToNextRound() {
        if (currentRound + 1 >= rounds.size()) return;

        ArrayList<BracketMatch> currentRoundMatches = rounds.get(currentRound);
        ArrayList<BracketMatch> nextRoundMatches = rounds.get(currentRound + 1);

        for (int i = 0; i < nextRoundMatches.size(); i++) {
            BracketMatch nextMatch = nextRoundMatches.get(i);
            nextMatch.player1 = currentRoundMatches.get(i * 2).winner;
            nextMatch.player2 = currentRoundMatches.get(i * 2 + 1).winner;
        }
    }

    boolean isComplete() {
        return currentRound >= rounds.size();
    }

    String getTournamentWinner() {
        if (!isComplete()) return null;
        return rounds.get(rounds.size() - 1).get(0).winner;
    }
}


// ============================================================================
// ACHIEVEMENT SYSTEM
// ============================================================================
class Achievement {
    String id;
    String name;
    String description;
    String emoji;
    boolean unlocked;

    Achievement(String id, String name, String description, String emoji) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.emoji = emoji;
        this.unlocked = false;
    }
}

class AchievementManager {
    HashMap<String, Achievement> achievements;
    HashMap<String, HashSet<String>> playerAchievements; // player -> achievement IDs

    AchievementManager() {
        achievements = new HashMap<>();
        playerAchievements = new HashMap<>();
        initializeAchievements();
    }

    void initializeAchievements() {
        achievements.put("first_win", new Achievement("first_win", "First Blood", "Win your first round", "🎯"));
        achievements.put("win_streak_3", new Achievement("win_streak_3", "Hot Streak", "Win 3 rounds in a row", "🔥"));
        achievements.put("win_streak_5", new Achievement("win_streak_5", "Unstoppable", "Win 5 rounds in a row", "⚡"));
        achievements.put("perfect_game", new Achievement("perfect_game", "Flawless Victory", "Win all rounds without losing", "💎"));
        achievements.put("rock_master", new Achievement("rock_master", "Rock Solid", "Win 10 rounds with Rock", "🪨"));
        achievements.put("paper_master", new Achievement("paper_master", "Paper Trail", "Win 10 rounds with Paper", "📄"));
        achievements.put("scissors_master", new Achievement("scissors_master", "Sharp Shooter", "Win 10 rounds with Scissors", "✂️"));
        achievements.put("comeback_kid", new Achievement("comeback_kid", "Comeback Kid", "Win after being down 3 rounds", "💪"));
        achievements.put("veteran", new Achievement("veteran", "Veteran Player", "Play 50 total rounds", "🎖️"));
        achievements.put("tournament_winner", new Achievement("tournament_winner", "Champion", "Win a tournament", "🏆"));
    }

    void checkAchievements(String player, PlayerStats stats) {
        HashSet<String> playerAchs = playerAchievements.computeIfAbsent(player, k -> new HashSet<>());

        // First win
        if (stats.totalWins >= 1 && !playerAchs.contains("first_win")) {
            unlockAchievement(player, "first_win");
        }

        // Win streaks
        if (stats.longestWinStreak >= 3 && !playerAchs.contains("win_streak_3")) {
            unlockAchievement(player, "win_streak_3");
        }
        if (stats.longestWinStreak >= 5 && !playerAchs.contains("win_streak_5")) {
            unlockAchievement(player, "win_streak_5");
        }

        // Choice masters
        if (stats.rockWins >= 10 && !playerAchs.contains("rock_master")) {
            unlockAchievement(player, "rock_master");
        }
        if (stats.paperWins >= 10 && !playerAchs.contains("paper_master")) {
            unlockAchievement(player, "paper_master");
        }
        if (stats.scissorsWins >= 10 && !playerAchs.contains("scissors_master")) {
            unlockAchievement(player, "scissors_master");
        }

        // Veteran
        if (stats.getTotalGames() >= 50 && !playerAchs.contains("veteran")) {
            unlockAchievement(player, "veteran");
        }
    }

    void unlockAchievement(String player, String achievementId) {
        HashSet<String> playerAchs = playerAchievements.computeIfAbsent(player, k -> new HashSet<>());
        playerAchs.add(achievementId);
        Achievement ach = achievements.get(achievementId);
        ach.unlocked = true;
    }

    ArrayList<Achievement> getPlayerAchievements(String player) {
        HashSet<String> achIds = playerAchievements.get(player);
        ArrayList<Achievement> result = new ArrayList<>();
        if (achIds != null) {
            for (String id : achIds) {
                result.add(achievements.get(id));
            }
        }
        return result;
    }

    String getAchievementSummary(String player) {
        ArrayList<Achievement> achs = getPlayerAchievements(player);
        if (achs.isEmpty()) return "";

        StringBuilder sb = new StringBuilder();
        for (Achievement ach : achs) {
            sb.append(ach.emoji).append(" ");
        }
        return sb.toString();
    }
}







public class RockPaperScissorGame extends JFrame {

    private int numPlayers;

    private int triesPerPlayer;
    private ArrayList<String> players;
    private HashMap<String, Integer> scores;
    private HashMap<String, PlayerStats> playerStats;


    // Game mode
    private boolean extendedMode = false; // RPSLS mode
    private JButton lizardButton, spockButton;

    private JPanel centerPanel;

    // Achievements
    private AchievementManager achievementManager;
    private HashMap<String, String> playerAvatars; // player -> avatar emoji

    // Tournament mode
    private boolean tournamentMode = false;
    private TournamentBracket bracket;
    private JTextArea bracketDisplay;

    private JCheckBox tournamentCheckbox;
    private JComboBox<String> gameModeBox;



    private int currentPlayerIndex;

    private int currentTries;

    private JPanel mainPanel;
    private CardLayout cardLayout;

    //Panel components
    private JTextField playersField;
    private JTextField triesField;
    private JComboBox<String> difficultyBox;

    //Game panel component;

    private JLabel playerLabel;
    private JLabel triesLabel;
    private JLabel resultLabel;
    private JTextArea scoreboardArea;
    private JLabel avatarLabel;
    private JLabel countdownLabel;
    private JLabel wish;


    private JButton rockButton, paperButton, scissorsButton;

    //Winner panel components

    private JLabel winnerLabel;
    private JTextArea finalScoresArea;
    private JTextArea statsArea;


    private ParticlePanel particlePanel;

    // Game modes
    private enum Difficulty { EASY, MEDIUM, HARD }
    private Difficulty currentDifficulty = Difficulty.MEDIUM;

    // Player history for AI
    private ArrayList<String> playerChoiceHistory = new ArrayList<>();

    // Sound enabled flag
    private boolean soundEnabled = true;

    /**
     * Constructor
     */

    public RockPaperScissorGame(){
        setTitle("Rock Paper Scissor Game");
        setSize(600, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        loadPlayerProfiles();

        // Initialize achievement system
        achievementManager = new AchievementManager();
        playerAvatars = new HashMap<>();

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setOpaque(false);

        mainPanel.add(createSplashPanel(), "splash");
        mainPanel.add(createSetupPanel(), "setup");
        mainPanel.add(createGamePanel(), "game");
        mainPanel.add(createWinnerPanel(), "winner");
        add(mainPanel);

        setupKeyboardShortcuts();
        setVisible(true);

        showSplashScreen();

        add(mainPanel);
        setVisible(true);

//        // In constructor:
//        getRootPane().registerKeyboardAction(
//                e -> playRound("Rock"),
//                KeyStroke.getKeyStroke(KeyEvent.VK_R, 0),
//                JComponent.WHEN_IN_FOCUSED_WINDOW
//        );
// P for Paper, S for Scissors


    }

    private JPanel createSplashPanel() {
        JPanel panel = new ImagePanel("/images/background.jpg");
        panel.setLayout(new BorderLayout());

        JLabel logo = new JLabel("Rock Paper Scissors Game", SwingConstants.CENTER);
        logo.setFont(new Font("Arial", Font.BOLD, 40));
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        logo.setForeground(Color.WHITE);
        playSound("/sounds/start.wav");

        JLabel subtitle = new JLabel("Pro Edition", SwingConstants.CENTER);
        subtitle.setFont(new Font("Arial", Font.ITALIC, 20));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        subtitle.setForeground(new Color(255, 255, 255, 200));

        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(logo);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(subtitle);
        centerPanel.add(Box.createVerticalGlue());

        panel.add(centerPanel, BorderLayout.CENTER);

        return panel;
    }

    private void showSplashScreen() {
        cardLayout.show(mainPanel, "splash");
        Timer splashTimer = new Timer(2000, e -> cardLayout.show(mainPanel, "setup"));
        splashTimer.setRepeats(false);
        splashTimer.start();
    }

    private JPanel createSetupPanel(){

        JPanel panel = new ImagePanel("/images/background.jpg");
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));


        JLabel titleLabel = new JLabel("Rock Paper Scissor Game");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(Color.white);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Game Mode");
        subtitleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        subtitleLabel.setForeground(Color.WHITE);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(Box.createVerticalStrut(30));
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(subtitleLabel);
        panel.add(Box.createVerticalStrut(50));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);


        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(new Color(255,255,255,50));
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);


        JLabel playersLabel = new JLabel("Number of Players");
        playersLabel.setFont(new Font("Arial", Font.BOLD, 20));
        playersLabel.setForeground(Color.white);
        playersLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        playersField = new JTextField(10);
        playersField.setFont(new Font("Arial", Font.PLAIN, 16));
        playersField.setMaximumSize(new Dimension(200, 40));
        playersField.setAlignmentX(Component.RIGHT_ALIGNMENT);


        JLabel triesLabel = new JLabel("Tries per Player");
        triesLabel.setFont(new Font("Arial", Font.BOLD, 20));

        triesLabel.setForeground(Color.white);
        triesLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        triesField = new JTextField(10);
        triesField.setFont(new Font("Arial", Font.PLAIN, 18));
        triesField.setMaximumSize(new Dimension(200, 40));
        triesField.setAlignmentX(Component.RIGHT_ALIGNMENT);

        JLabel difficultyLabel = new JLabel("Choose Difficulty Level");
        difficultyLabel.setForeground(Color.WHITE);
        difficultyLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        difficultyLabel.setFont(new Font("Arial", Font.BOLD, 20));

        difficultyBox = new JComboBox<>(new String[]{"Easy", "Medium", "Hard"});

        // ADD AFTER difficultyBox:
        JLabel gameModeLabel = new JLabel("Game Mode");
        gameModeLabel.setFont(new Font("Arial", Font.BOLD, 16));

        //JComboBox<String> gameModeBox = new JComboBox<>(new String[]{"Classic (3 choices)",
        // "Extended (5 choices - RPSLS)"});

        gameModeBox = new JComboBox<>(new String[]{"Classic (3 choices)", "Extended (5 choices - RPSLS)"});

        gameModeBox.setFont(new Font("Arial", Font.PLAIN, 16));
        gameModeBox.setMaximumSize(new Dimension(400, 40));

        formPanel.add(gameModeLabel);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(gameModeBox);
        formPanel.add(Box.createVerticalStrut(20));

        // ADD THIS BEFORE startButton code:
//        JCheckBox tournamentCheckbox = new JCheckBox("Tournament Bracket Mode (4+ players)");
        tournamentCheckbox = new JCheckBox("Tournament Bracket Mode (4+ players)");
        tournamentCheckbox.setForeground(Color.WHITE);
        tournamentCheckbox.setFont(new Font("Arial", Font.PLAIN, 16));
        tournamentCheckbox.setOpaque(false);
        tournamentCheckbox.setAlignmentX(Component.CENTER_ALIGNMENT);

        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(tournamentCheckbox);
        formPanel.add(Box.createVerticalStrut(20));



        JButton startButton = new JButton("Start Game");
        startButton.setFont(new Font("Arial", Font.BOLD, 20));
        startButton.setForeground(Color.WHITE);
        startButton.setFocusPainted(false);
        startButton.setMaximumSize(new Dimension(400, 50));
        startButton.addActionListener(e -> startGame());
        startButton.setBorder(new RoundedBorder(20, Color.WHITE));
        startButton.setAlignmentX(Component.RIGHT_ALIGNMENT);

        //addButtonHoverEffect(startButton, new Color(99, 102, 241), new Color(79, 82, 221));

        JLabel wish = new JLabel("Good Luck");
        wish.setFont(new Font("Arial", Font.BOLD, 16));
        playSound("/sounds/start.wav");


        formPanel.add(playersLabel);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(playersField);
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(triesLabel);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(triesField);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(difficultyLabel);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(difficultyBox);
        formPanel.add(Box.createVerticalStrut(30));
        formPanel.add(startButton);

        panel.add(formPanel);
        return panel;

    }

    private JPanel createGamePanel(){

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(700, 800));


        JPanel panel = new ImagePanel("/images/background.jpg");
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBounds(0, 0, 700, 800);

        //Top panel player information

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setOpaque(false);
        topPanel.setBackground(new Color(255,255,255,180));
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        avatarLabel = new JLabel();
        avatarLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        playerLabel = new JLabel("Player's Turn");
        playerLabel.setFont(new Font("Arial", Font.BOLD, 28));
        playerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        playerLabel.setForeground(Color.white);


        triesLabel = new JLabel("Tries remaining: 0");
        triesLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        triesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        triesLabel.setForeground(Color.white);

        countdownLabel = new JLabel("");
        countdownLabel.setFont(new Font("Arial", Font.BOLD, 72));
        countdownLabel.setForeground(new Color(255, 0, 0, 200));
        countdownLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        topPanel.add(avatarLabel);
        topPanel.add(Box.createVerticalStrut(10));
        topPanel.add(playerLabel);
        topPanel.add(Box.createVerticalStrut(10));
        topPanel.add(triesLabel);
        topPanel.add(countdownLabel);


//        JPanel centerPanel = new JPanel(new GridLayout(1, 3, 15, 0));
//        centerPanel.setOpaque(false);
//        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // REPLACE: JPanel centerPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        // WITH:
        centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(1, 3, 15, 0)); // Will change to 1,5 in extended mode
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        rockButton = createChoiceButton("Rock", "rock.png");
        paperButton = createChoiceButton("Paper", "paper.png");
        scissorsButton = createChoiceButton("Scissors", "scissors.png");
        lizardButton = createChoiceButton("Lizard", "lizard.png");
        spockButton = createChoiceButton("Spock", "spock.png");

        rockButton.addActionListener(e -> { playSound("click.wav"); playRound("Rock"); });
        paperButton.addActionListener(e -> { playSound("click.wav"); playRound("Paper"); });
        scissorsButton.addActionListener(e -> { playSound("click.wav"); playRound("Scissors"); });
        lizardButton.addActionListener(e -> { playSound("click.wav"); playRound("Lizard"); });
        spockButton.addActionListener(e -> { playSound("click.wav"); playRound("Spock"); });

        centerPanel.add(rockButton);
        centerPanel.add(paperButton);
        centerPanel.add(scissorsButton);
        // Lizard and Spock added dynamically based on mode


        rockButton = createChoiceButton("Rock", "rock.png");
        paperButton = createChoiceButton("Paper", "paper.png");
        scissorsButton = createChoiceButton("Scissors", "scissors.png");



        rockButton.setMaximumSize(new Dimension(10, 0));
        rockButton.setFont(new Font("Arial", Font.BOLD, 20));
        rockButton.setForeground(Color.WHITE);
        rockButton.setFocusPainted(false);
        //rockButton.addActionListener(e -> createChoiceButton("Rock", "rock.png"));
        rockButton.setBorder(new RoundedBorder(80, Color.WHITE));

        paperButton.setMaximumSize(new Dimension(10, 0));
        paperButton.setFont(new Font("Arial", Font.BOLD, 20));
        paperButton.setForeground(Color.WHITE);
        paperButton.setFocusPainted(false);
        //paperButton.addActionListener(e -> createChoiceButton("Rock", "paper.png"));
        paperButton.setBorder(new RoundedBorder(80, Color.WHITE));

        scissorsButton.setMaximumSize(new Dimension(10, 0));
        scissorsButton.setFont(new Font("Arial", Font.BOLD, 20));
        scissorsButton.setForeground(Color.WHITE);
        //scissorsButton.setFocusPainted(false);scissorsButton.addActionListener(e -> createChoiceButton("Scissors", "scissors.png"));
        scissorsButton.setBorder(new RoundedBorder(80, Color.WHITE));

        rockButton.setForeground(Color.WHITE);
        paperButton.setForeground(Color.WHITE);
        scissorsButton.setForeground(Color.WHITE);

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
        resultLabel.setBackground(new Color(255, 255, 255, 220));
        resultLabel.setPreferredSize(new Dimension(0, 80));


        resultLabel.setBackground(new Color(255, 255, 255, 100));

        resultLabel.setPreferredSize(new Dimension(0, 100));


        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        JLabel scoreLabel = new JLabel("ScoreBoard");
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 14));



        scoreboardArea = new JTextArea(8, 40);
        scoreboardArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        scoreboardArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(scoreboardArea);

        // ADD THIS after scoreboardArea and scrollPane:
        bracketDisplay = new JTextArea(6, 40);
        bracketDisplay.setFont(new Font("Monospaced", Font.PLAIN, 12));
        bracketDisplay.setEditable(false);
        JScrollPane bracketScroll = new JScrollPane(bracketDisplay);

        // Update bottomPanel to show bracket in tournament mode
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Scoreboard", scrollPane);
        tabbedPane.addTab("Bracket", bracketScroll);

        bottomPanel.add(scoreLabel, BorderLayout.NORTH);
        bottomPanel.add(tabbedPane, BorderLayout.CENTER); // Replace: bottomPanel.add(scrollPane, ...)

        bottomPanel.add(scoreLabel, BorderLayout.NORTH);
        bottomPanel.add(scrollPane, BorderLayout.CENTER);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(resultLabel, BorderLayout.SOUTH);


        JPanel mainGamePanel = new JPanel(new BorderLayout());

        mainGamePanel.add(panel, BorderLayout.CENTER);
        mainGamePanel.add(bottomPanel, BorderLayout.SOUTH);
        mainGamePanel.setOpaque(true);
        mainGamePanel.setBounds(0,0,700,800);

        particlePanel = new ParticlePanel();
        particlePanel.setBounds(0, 0, 700, 800);

        layeredPane.add(mainGamePanel, Integer.valueOf(0));
        layeredPane.add(particlePanel, Integer.valueOf(1));

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(layeredPane);
        return wrapper;





    }

    private JButton createChoiceButton(String text, String imagePath){


        JButton button = new JButton("<html><center>"+ text+ "</center></html>");

        button.setLayout(new BorderLayout());

        ImageIcon icon = loadScaledImage(imagePath, 100, 100);
        if (icon != null) {
            JLabel imageLabel = new JLabel(icon, SwingConstants.CENTER);
            button.add(imageLabel, BorderLayout.CENTER);
        }

        JLabel textLabel = new JLabel(text, SwingConstants.CENTER);
        textLabel.setFont(new Font("Arial", Font.BOLD, 18));
        button.add(textLabel, BorderLayout.SOUTH);

        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(new RoundedBorder(15, new Color(99, 102, 241)));

        // Add hover effect with animation
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(240, 240, 255));
                button.setBorder(new RoundedBorder(15, new Color(79, 82, 221)));
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(Color.WHITE);
                button.setBorder(new RoundedBorder(15, new Color(99, 102, 241)));
                button.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });


//        button.setFont(new Font("Arial", Font.BOLD, 18));
//        button.setBackground(new Color(99, 102, 241));
//        button.setForeground(Color.black);
//        button.setFocusPainted(false);
//        button.setBorder(BorderFactory.createEmptyBorder(30, 10, 30, 10));


        return button;

    }



    private JPanel createWinnerPanel(){

        JPanel panel = new ImagePanel("/images/background.jpg");
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        panel.setOpaque(false);


        JLabel titleLabel = new JLabel("Game Over");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        winnerLabel = new JLabel("🏆Winner🏆");
        winnerLabel.setFont(new Font("Arial", Font.BOLD, 30));
        winnerLabel.setForeground(Color.WHITE);
        winnerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(Box.createVerticalStrut(30));
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(30));
        panel.add(winnerLabel);
        panel.add(Box.createVerticalStrut(40));

        JPanel scoresPanel = new JPanel(new BorderLayout());
        scoresPanel.setBackground(new Color(255,255,255,100));
        scoresPanel.setOpaque(true);
        scoresPanel.setBorder(new RoundedBorder(20, Color.BLACK));
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
        playAgainButton.setBackground(new Color(185, 51, 234, 57));
        playAgainButton.setForeground(Color.BLACK);
        playAgainButton.setFocusPainted(false);
        playAgainButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        playAgainButton.setMaximumSize(new Dimension(300, 50));
        playAgainButton.addActionListener(e -> resetGame());

        // Stats panel
        JPanel statsPanel = new JPanel(new BorderLayout());
        statsPanel.setBackground(new Color(255, 255, 255, 220));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        statsPanel.setMaximumSize(new Dimension(600, 200));

        JLabel statsLabel = new JLabel("Detailed Results Statistics");
        statsLabel.setFont(new Font("Arial", Font.BOLD, 20));

        statsArea = new JTextArea(6, 40);
        statsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        statsArea.setEditable(false);
        JScrollPane statsScroll = new JScrollPane(statsArea);

        statsPanel.add(statsLabel, BorderLayout.NORTH);
        statsPanel.add(statsScroll, BorderLayout.CENTER);

        //Button

        JButton quitButton = new JButton("End Game");
        quitButton.setFont(new Font("Arial", Font.BOLD, 18));
        quitButton.setBackground(new Color(132, 131, 34, 100));
        quitButton.setForeground(Color.BLACK);
        quitButton.setFocusPainted(false);
        quitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        quitButton.setMaximumSize(new Dimension(300, 50));
        quitButton.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to quit?",
                    "Confirm Exit",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );
            if (choice == JOptionPane.YES_OPTION) {
                savePlayerProfiles();
                System.exit(0);
            }
        });

        addButtonHoverEffect(quitButton, new Color(239, 68, 68), new Color(220, 38, 38));


        panel.add(scoresPanel);
        panel.add(Box.createVerticalStrut(20));
        panel.add(statsPanel);
        panel.add(Box.createVerticalStrut(30));
        panel.add(playAgainButton);
        panel.add(Box.createVerticalStrut(10));
        panel.add(quitButton);
        panel.add(Box.createHorizontalStrut(5));

        return panel;

    }



//    private void startGame(){
//        try{
//            numPlayers = Integer.parseInt(playersField.getText());
//            triesPerPlayer = Integer.parseInt(triesField.getText());
//
//            if(numPlayers <= 0 || triesPerPlayer <= 0){
//                JOptionPane.showMessageDialog(this, "Please enter positive numbers!",
//                        "Invalid Input",
//                        JOptionPane.ERROR_MESSAGE);
//                return;
//            }
//
//            String diff = (String) difficultyBox.getSelectedItem();
//            assert diff != null;
//            currentDifficulty = Difficulty.valueOf(diff.toUpperCase());
//
//            players = new ArrayList<>();
//            scores = new HashMap<>();
//            playerStats = new HashMap<>();
//
//            // Collect player names
//            for(int i = 1; i <= numPlayers; i++){
//                String playerName = JOptionPane.showInputDialog(
//                        this,
//                        "Enter name for Player " + i + ":",
//                        "Player Name",
//                        JOptionPane.QUESTION_MESSAGE
//                );
//
//                // Handle cancel or empty input
//                if(playerName == null || playerName.trim().isEmpty()){
//                    playerName = "Player " + i; // fallback to default name
//                }
//
//                players.add(playerName.trim());
//                scores.put(playerName.trim(), 0);
//                playerStats.put(playerName.trim(), new PlayerStats(playerName.trim()));
//            }
//
//
//            currentPlayerIndex = 0;
//            currentTries = triesPerPlayer;
//            playerChoiceHistory.clear();
//
//            updateGamePanel();
//            cardLayout.show(mainPanel, "game");
//
//            playSound("/sounds/start.wav");
//            updateGamePanel();
//            cardLayout.show(mainPanel, "game");
//
//        } catch (NumberFormatException e){
//            JOptionPane.showMessageDialog(this,
//                    "Please enter valid numbers!",
//                    "Invalid Input",
//                    JOptionPane.ERROR_MESSAGE);
//        }
//    }


    private void startGame() {
        try {
            numPlayers = Integer.parseInt(playersField.getText());
            triesPerPlayer = Integer.parseInt(triesField.getText());

            if (numPlayers <= 0 || triesPerPlayer <= 0) {
                JOptionPane.showMessageDialog(this,
                        "Please enter positive numbers!",
                        "Invalid Input",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Check tournament mode
//            Component[] components = ((JPanel)((JPanel)mainPanel.getComponent(1)).getComponent(1)).getComponents();
//            for (Component c : components) {
//                if (c instanceof JCheckBox) {
//                    tournamentMode = ((JCheckBox)c).isSelected();
//                    break;
//                }
//            }
            // Check tournament mode (simplified)
            tournamentMode = tournamentCheckbox.isSelected();

            if (tournamentMode && numPlayers < 4) {
                JOptionPane.showMessageDialog(this,
                        "Tournament mode requires at least 4 players!",
                        "Invalid Setup",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Set difficulty
            String diff = (String) difficultyBox.getSelectedItem();
            currentDifficulty = Difficulty.valueOf(diff.toUpperCase());

            // ADD AFTER: currentDifficulty = Difficulty.valueOf(diff.toUpperCase());

            // Get game mode
//            for (Component c : components) {
//                if (c instanceof JComboBox && c != difficultyBox) {
//                    JComboBox<?> modeBox = (JComboBox<?>)c;
//                    extendedMode = modeBox.getSelectedIndex() == 1;
//                    break;
//                }
//            }

            // Get game mode (simplified)
            extendedMode = gameModeBox.getSelectedIndex() == 1;

            players = new ArrayList<>();
            scores = new HashMap<>();
            playerStats = new HashMap<>();

            // Collect player names
            for (int i = 1; i <= numPlayers; i++) {
                String playerName = JOptionPane.showInputDialog(
                        this,
                        "Enter name for Player " + i + ":",
                        "Player Name",
                        JOptionPane.QUESTION_MESSAGE
                );

                if (playerName == null || playerName.trim().isEmpty()) {
                    playerName = "Player " + i;
                }

                players.add(playerName.trim());

                // ADD AFTER: players.add(playerName.trim());

                // Avatar selection
                String[] avatarOptions = {"👤", "😀", "😎", "🤓", "🥳", "🤠", "👑", "🎮", "🐱", "🐶", "🦁", "🐼"};
                String avatar = (String) JOptionPane.showInputDialog(
                        this,
                        "Choose an avatar for " + playerName.trim(),
                        "Avatar Selection",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        avatarOptions,
                        avatarOptions[0]
                );

                if (avatar == null) avatar = "👤";
                playerAvatars.put(playerName.trim(), avatar);
                scores.put(playerName.trim(), 0);
                playerStats.put(playerName.trim(), new PlayerStats(playerName.trim()));
            }

            currentPlayerIndex = 0;
            currentTries = triesPerPlayer;
            playerChoiceHistory.clear();

            // Initialize tournament bracket if needed
            if (tournamentMode) {
                bracket = new TournamentBracket(players);
                updateBracketDisplay();
            }

            playSound("start.wav");
            updateGamePanel();
            cardLayout.show(mainPanel, "game");
            // Update UI for extended mode
            updateGameModeUI();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Please enter valid numbers!",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

//    private void updateGameModeUI() {
//        // Find the center panel in game panel
//        Component[] mainComps = ((JPanel)mainPanel.getComponent(2)).getComponents();
//        JLayeredPane layeredPane = (JLayeredPane)mainComps[0];
//        JPanel mainGamePanel = (JPanel)layeredPane.getComponent(0);
//        JPanel panel = (JPanel)mainGamePanel.getComponent(0);
//        JPanel centerPanel = (JPanel)panel.getComponent(1);
//        // Add this near other JPanel/JButton declarations
//
//
//        centerPanel.removeAll();
//
//        if (extendedMode) {
//            centerPanel.setLayout(new GridLayout(1, 5, 10, 0));
//            centerPanel.add(rockButton);
//            centerPanel.add(paperButton);
//            centerPanel.add(scissorsButton);
//            centerPanel.add(lizardButton);
//            centerPanel.add(spockButton);
//        } else {
//            centerPanel.setLayout(new GridLayout(1, 3, 15, 0));
//            centerPanel.add(rockButton);
//            centerPanel.add(paperButton);
//            centerPanel.add(scissorsButton);
//        }
//
//        centerPanel.revalidate();
//        centerPanel.repaint();
//    }


    private void updateGameModeUI() {
        if (centerPanel == null) return; // Safety check

        centerPanel.removeAll();

        if (extendedMode) {
            centerPanel.setLayout(new GridLayout(1, 5, 10, 0));
            centerPanel.add(rockButton);
            centerPanel.add(paperButton);
            centerPanel.add(scissorsButton);
            centerPanel.add(lizardButton);
            centerPanel.add(spockButton);
        } else {
            centerPanel.setLayout(new GridLayout(1, 3, 15, 0));
            centerPanel.add(rockButton);
            centerPanel.add(paperButton);
            centerPanel.add(scissorsButton);
        }

        centerPanel.revalidate();
        centerPanel.repaint();
    }


    private void updateBracketDisplay() {
        if (!tournamentMode || bracket == null) return;

        StringBuilder display = new StringBuilder();
        display.append("═══════ TOURNAMENT BRACKET ═══════\n\n");

        for (int r = 0; r < bracket.rounds.size(); r++) {
            String roundName;
            int remaining = bracket.rounds.size() - r;
            if (remaining == 1) roundName = "🏆 FINAL";
            else if (remaining == 2) roundName = "SEMI-FINALS";
            else if (remaining == 3) roundName = "QUARTER-FINALS";
            else roundName = "ROUND " + (r + 1);

            display.append(roundName).append(":\n");

            ArrayList<BracketMatch> round = bracket.rounds.get(r);
            for (int m = 0; m < round.size(); m++) {
                BracketMatch match = round.get(m);
                String status = match.completed ? "✓" :
                        (r == bracket.currentRound && m == bracket.currentMatchInRound) ? "▶" : "○";

                display.append("  ").append(status).append(" ");
                display.append(match.player1).append(" vs ").append(match.player2);

                if (match.completed) {
                    display.append(" → Winner: ").append(match.winner);
                }
                display.append("\n");
            }
            display.append("\n");
        }

        if (bracketDisplay != null) {
            bracketDisplay.setText(display.toString());
        }
    }



    public void playRound(String playerChoice){
        String[] choices = {"Rock", "Paper", "Scissors"};

        String computerChoice = choices[new Random().nextInt(3)];
        String result = determineWinner(playerChoice, computerChoice);
        String currentPlayer = players.get(currentPlayerIndex);
        PlayerStats stats = playerStats.get(currentPlayer);


        if(result.equals("player")){

            scores.put(currentPlayer, scores.get(currentPlayer)+1);
            resultLabel.setText("You chose "+playerChoice+ " | Computer chose "+ computerChoice+" | You win!🙌");
            stats.recordWin(playerChoice);
            //showResultWithAnimation("You chose "+playerChoice+ " | Computer chose "+ computerChoice+" | You win!🙌", new Color(187, 247, 208));
            resultLabel.setBackground(new Color(59, 202, 12));
            resultLabel.setForeground(Color.WHITE);

            playSound("/sounds/win.wav");
            particlePanel.explode(350, 300);

        } else if(result.equals("computer")){
            stats.recordLoss(playerChoice);
            resultLabel.setText("You chose "+playerChoice+ " | Computer chose "+ computerChoice+" | Computer won😔!");
            resultLabel.setBackground(new Color(244, 6, 6));
            playSound("/sounds/lose.wav");


        } else{
            stats.recordTie(playerChoice);

            resultLabel.setText("You chose "+playerChoice+ " and the Computer chose "+ computerChoice+". It is a tie😰!");
            resultLabel.setBackground(new Color(229, 232, 188));
            playSound("/sounds/tie.wav");

        }

        // ADD AFTER: stats.recordWin(playerChoice); (or recordLoss, recordTie)

        // Check for new achievements
        achievementManager.checkAchievements(currentPlayer, stats);

        playerChoiceHistory.add(playerChoice);

        currentTries--;

        if (currentTries == 0) {
            disableButton();

            Timer timer = new Timer(2500, e -> {
                if (tournamentMode) {
                    // Tournament mode: record winner and advance bracket
                    //String currentPlayer = players.get(currentPlayerIndex);
                    bracket.recordMatchWinner(currentPlayer);
                    updateBracketDisplay();

                    if (bracket.isComplete()) {
                        showTournamentWinner();
                    } else {
                        // Setup next match
                        BracketMatch nextMatch = bracket.getCurrentMatch();
                        if (nextMatch != null && !nextMatch.player1.equals("BYE") && !nextMatch.player2.equals("BYE")) {
                            // Find player indices
                            currentPlayerIndex = players.indexOf(nextMatch.player1);
                            currentTries = triesPerPlayer;
                            scores.put(nextMatch.player1, 0);
                            scores.put(nextMatch.player2, 0);
                            resultLabel.setText(" ");
                            resultLabel.setBackground(new Color(255, 255, 255, 220));
                            countdownLabel.setText("");
                            enableButtons();
                            updateGamePanel();
                            startCountdown();
                        }
                    }
                } else {
                    // Normal mode (existing code)
                    currentPlayerIndex++;
                    if (currentPlayerIndex >= players.size()) {
                        showWinner();
                    } else {
                        currentTries = triesPerPlayer;
                        resultLabel.setText(" ");
                        resultLabel.setBackground(new Color(255, 255, 255, 220));
                        countdownLabel.setText("");
                        enableButtons();
                        updateGamePanel();
                        startCountdown();
                    }
                }
            });

            timer.setRepeats(false);
            timer.start();
        } else {
            updateGamePanel();
        }

//


    }

//    private String determineWinner(String player, String computer){
//        if(player.equals(computer)) return "tie";
//
//        if((player.equals("Rock") && computer.equals("Scissors")) ||
//                (player.equals("Paper") && computer.equals("Rock")) ||
//                (player.equals("Scissors") && computer.equals("Paper"))) {
//
//            return "player";
//        }
//        return "computer";
//    }


    private String determineWinner(String player, String computer) {
        if (player.equals(computer)) return "tie";

        if (extendedMode) {
            // Rock-Paper-Scissors-Lizard-Spock rules
            HashMap<String, HashSet<String>> wins = new HashMap<>();
            wins.put("Rock", new HashSet<>(Arrays.asList("Scissors", "Lizard")));
            wins.put("Paper", new HashSet<>(Arrays.asList("Rock", "Spock")));
            wins.put("Scissors", new HashSet<>(Arrays.asList("Paper", "Lizard")));
            wins.put("Lizard", new HashSet<>(Arrays.asList("Spock", "Paper")));
            wins.put("Spock", new HashSet<>(Arrays.asList("Scissors", "Rock")));

            if (wins.get(player).contains(computer)) {
                return "player";
            }
            return "computer";
        } else {
            // Classic rules
            if ((player.equals("Rock") && computer.equals("Scissors")) ||
                    (player.equals("Paper") && computer.equals("Rock")) ||
                    (player.equals("Scissors") && computer.equals("Paper"))) {
                return "player";
            }
            return "computer";
        }
    }

    private String getComputerChoice() {
        String[] choices = extendedMode?
                new String[]{"Rock", "Paper", "Scissors", "Lizard", "Spock"} :
                new String[]{"Rock", "Paper", "Scissors"};


        switch (currentDifficulty) {
            case EASY:
                return choices[new Random().nextInt(3)];

            case MEDIUM:
                // 50% random, 50% pattern based
                if (Math.random() < 0.5) {
                    return choices[new Random().nextInt(3)];
                }
                // Fall through to HARD logic

            case HARD:
                // Counter player's most frequent choice
                if (playerChoiceHistory.size() > 0) {
                    HashMap<String, Integer> choiceCounts = new HashMap<>();
                    choiceCounts.put("Rock", 0);
                    choiceCounts.put("Paper", 0);
                    choiceCounts.put("Scissors", 0);

                    for (String choice : playerChoiceHistory) {
                        choiceCounts.put(choice, choiceCounts.get(choice) + 1);
                    }

                    String mostFrequent = "Rock";
                    int maxCount = 0;
                    for (Map.Entry<String, Integer> entry : choiceCounts.entrySet()) {
                        if (entry.getValue() > maxCount) {
                            maxCount = entry.getValue();
                            mostFrequent = entry.getKey();
                        }
                    }

                    // Counter the most frequent choice
                    if (mostFrequent.equals("Rock")) return "Paper";
                    if (mostFrequent.equals("Paper")) return "Scissors";
                    return "Rock";
                }
                return choices[new Random().nextInt(3)];

            default:
                return choices[new Random().nextInt(3)];
        }
    }

    private void updateGamePanel(){
        String currentPlayer = players.get(currentPlayerIndex);
        PlayerStats stats = playerStats.get(currentPlayer);

        playerLabel.setText(currentPlayer + "'s Turn");
        triesLabel.setText("Tries remaining: "+currentTries);

//        StringBuilder sb = new StringBuilder();
//
//        for(int i = 0; i < players.size(); i++){
//            String player = players.get(i);
//
//            sb.append(player);
//            if(i == currentPlayerIndex){
//                sb.append(" ");
//            }
//            sb.append(": ").append(scores.get(player)).append(" points\n");
//
//            PlayerStats pStats = playerStats.get(player);
//
//            if(pStats.currentWinStreak>1){
//                sb.append(" ").append(pStats.currentWinStreak);
//            }
//            sb.append("\n");
//
//        }

        // REPLACE the existing StringBuilder section with:
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < players.size(); i++) {
            String player = players.get(i);

            // Add avatar
            String avatar = playerAvatars.getOrDefault(player, "👤");
            sb.append(avatar).append(" ");

            sb.append(player);
            if (i == currentPlayerIndex) {
                sb.append(" ← 👈");
            }
            sb.append(": ").append(scores.get(player)).append(" pts");

            PlayerStats pStats = playerStats.get(player);
            if (pStats.currentWinStreak > 1) {
                sb.append(" 🔥").append(pStats.currentWinStreak);
            }

            // Add achievements
            String achs = achievementManager.getAchievementSummary(player);
            if (!achs.isEmpty()) {
                sb.append(" ").append(achs);
            }

            sb.append("\n");
        }



        scoreboardArea.setText(sb.toString());

    }

    private void showWinner(){

        playSound("/sounds/victory.wav");
        int maxScore = Collections.max(scores.values());

        ArrayList<String> winners = new ArrayList<>();

        for(Map.Entry<String, Integer> entry : scores.entrySet()){
            if(entry.getValue() == maxScore){
                winners.add(entry.getKey());
            }
        }

        if(winners.size() == 1){
            winnerLabel.setText(winners.getFirst() + " Wins!");

        } else{
            winnerLabel.setText(String.join(" &", winners) + " Tie!");

        }

        //Sort the scores in descending order;

        ArrayList<Map.Entry<String, Integer>> sortedScores = new ArrayList<>(scores.entrySet());
        sortedScores.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        StringBuilder sb = new StringBuilder();
        String[] medals = {"🥇", "🥈", "🥉"};

        for(int i = 0; i< sortedScores.size(); i++){
            Map.Entry<String, Integer> entry = sortedScores.get(i);

            if(i<3){
                sb.append(medals[i]).append(" ");

            }

            sb.append(entry.getKey()).append(": ").append(entry.getValue()).append(" points\n");
        }

        finalScoresArea.setText(sb.toString());

        StringBuilder statsSb = new StringBuilder();
        for (String player : players) {
            PlayerStats stats = playerStats.get(player);
            statsSb.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
            statsSb.append(player).append(":\n");
            statsSb.append(String.format("  Win Rate: %.1f%%\n", stats.getWinRate()));
            statsSb.append(String.format("  Record: %dW-%dL-%dT\n",
                    stats.totalWins, stats.totalLosses, stats.totalTies));
            statsSb.append(String.format("  Best Streak: %d wins\n", stats.longestWinStreak));
            statsSb.append(String.format("  Favorite: %s\n", stats.favoriteChoice));
            statsSb.append(String.format("  Rock Wins: %d | Paper: %d | Scissors: %d\n",
                    stats.rockWins, stats.paperWins, stats.scissorsWins));

            // FIND the statsSb section and ADD this before the closing:

            // ADD BEFORE the closing of the for loop:
            ArrayList<Achievement> playerAchs = achievementManager.getPlayerAchievements(player);
            if (!playerAchs.isEmpty()) {
                statsSb.append("  Achievements: ");
                for (Achievement ach : playerAchs) {
                    statsSb.append(ach.emoji).append(" ");
                }
                statsSb.append("\n");
            }
        }

        statsArea.setText(statsSb.toString());

        // Save player profiles
        savePlayerProfiles();

        // Celebration animation
        Timer celebrationTimer = new Timer(200, null);
        final int[] explosions = {0};
        celebrationTimer.addActionListener(e -> {
            int x = (int)(Math.random() * 600) + 50;
            int y = (int)(Math.random() * 400) + 100;
            particlePanel.explode(x, y);
            explosions[0]++;
            if (explosions[0] >= 10) {
                ((Timer)e.getSource()).stop();
            }
        });
        celebrationTimer.start();

        cardLayout.show(mainPanel, "winner");


    }

    private void showTournamentWinner() {
        playSound("victory.wav");

        String champion = bracket.getTournamentWinner();
        winnerLabel.setText("🏆 TOURNAMENT CHAMPION: " + champion + " 🏆");

        // Show bracket final state
        StringBuilder sb = new StringBuilder();
        sb.append("FINAL BRACKET:\n\n");
        updateBracketDisplay();

        finalScoresArea.setText(bracketDisplay.getText());

        // Show detailed stats
        StringBuilder statsSb = new StringBuilder();
        for (String player : players) {
            if (player.equals("BYE")) continue;
            PlayerStats stats = playerStats.get(player);
            statsSb.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
            statsSb.append(player).append(":\n");
            statsSb.append(String.format("  Tournament Wins: %d\n", scores.getOrDefault(player, 0)));
            statsSb.append(String.format("  Win Rate: %.1f%%\n", stats.getWinRate()));
            statsSb.append(String.format("  Best Streak: %d\n", stats.longestWinStreak));
        }

        statsArea.setText(statsSb.toString());

        // Celebration
        Timer celebrationTimer = new Timer(200, null);
        final int[] explosions = {0};
        celebrationTimer.addActionListener(e -> {
            int x = (int)(Math.random() * 600) + 50;
            int y = (int)(Math.random() * 400) + 100;
            particlePanel.explode(x, y);
            explosions[0]++;
            if (explosions[0] >= 15) {
                ((Timer)e.getSource()).stop();
            }
        });
        celebrationTimer.start();

        cardLayout.show(mainPanel, "winner");
    }

    private void showResultWithAnimation(String message, Color color) {
        resultLabel.setBackground(color);
        resultLabel.setText("");

        Timer fadeIn = new Timer(30, null);
        final int[] charIndex = {0};

        fadeIn.addActionListener(e -> {
            if (charIndex[0] < message.length()) {
                resultLabel.setText(message.substring(0, charIndex[0] + 1));
                charIndex[0]++;
            } else {
                ((Timer) e.getSource()).stop();
            }
        });
        fadeIn.start();
    }

    private void showAchievementUnlock(String player, String achievementId) {
        Achievement ach = achievementManager.achievements.get(achievementId);
        if (ach == null) return;

        JOptionPane.showMessageDialog(
                this,
                ach.emoji + " ACHIEVEMENT UNLOCKED!\n\n" +
                        ach.name + "\n" + ach.description,
                "Achievement for " + player,
                JOptionPane.INFORMATION_MESSAGE
        );
        playSound("achievement.wav"); // Add this sound file if you have one
    }

    private void startCountdown() {
        final int[] count = {3};
        disableButton();

        Timer countdownTimer = new Timer(1000, null);
        countdownTimer.addActionListener(e -> {
            if (count[0] > 0) {
                countdownLabel.setText(String.valueOf(count[0]));
                playSound("/sounds/beep.wav");
                count[0]--;
            } else {
                countdownLabel.setText("GO!");
                playSound("/sounds/go.wav");
                Timer clearTimer = new Timer(500, evt -> {
                    countdownLabel.setText("");
                    enableButtons();
                    ((Timer) evt.getSource()).stop();
                });
                clearTimer.setRepeats(false);
                clearTimer.start();
                ((Timer) e.getSource()).stop();
            }
        });
        countdownTimer.start();
    }

    private void disableButton(){
        rockButton.setEnabled(false);
        paperButton.setEnabled(false);
        scissorsButton.setEnabled(false);
    }

    private void enableButtons(){

        rockButton.setEnabled(true);
        paperButton.setEnabled(true);
        scissorsButton.setEnabled(true);


    }

    private void resetGame(){
        playersField.setText("");
        triesField.setText("");
        resultLabel.setText(" ");
        resultLabel.setBackground(Color.WHITE);
        enableButtons();

        cardLayout.show(mainPanel, "setup");
    }

    private void playSound(String soundFile) {
        if (!soundEnabled) return;

        try {
            java.net.URL soundURL = getClass().getResource(soundFile);
            if (soundURL == null) {
                // Try direct file path
                File soundPath = new File(soundFile);
                if (soundPath.exists()) {
                    soundURL = soundPath.toURI().toURL();
                } else {
                    return;
                }
            }

            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundURL);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();

            // Clean up after playing
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    clip.close();
                }
            });

        } catch (Exception e) {
            // Fail silently if sound not available
        }
    }

    // ========================================================================
    // IMAGE LOADING
    // ========================================================================
    private ImageIcon loadScaledImage(String path, int width, int height) {
        try {
            java.net.URL imgURL = getClass().getResource("/" + path);
            ImageIcon icon;

            if (imgURL != null) {
                icon = new ImageIcon(imgURL);
            } else {
                icon = new ImageIcon(path);
            }

            Image scaled = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        } catch (Exception e) {
            System.out.println("Image not found: " + path);
            return null;
        }
    }

    // ========================================================================
    // KEYBOARD SHORTCUTS
    // ========================================================================
    private void setupKeyboardShortcuts() {
        getRootPane().registerKeyboardAction(
                e -> {
                    if (rockButton.isEnabled()) {
                        playSound("/sounds/click.wav");
                        playRound("Rock");
                    }
                },
                KeyStroke.getKeyStroke(KeyEvent.VK_R, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW
        );

        getRootPane().registerKeyboardAction(
                e -> {
                    if (paperButton.isEnabled()) {
                        playSound("/sounds/click.wav");
                        playRound("Paper");
                    }
                },
                KeyStroke.getKeyStroke(KeyEvent.VK_P, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW
        );

        getRootPane().registerKeyboardAction(
                e -> {
                    if (scissorsButton.isEnabled()) {
                        playSound("/sounds/click.wav");
                        playRound("Scissors");
                    }
                },
                KeyStroke.getKeyStroke(KeyEvent.VK_S, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW
        );

        // ADD AFTER the existing three shortcuts:

        getRootPane().registerKeyboardAction(
                e -> {
                    if (lizardButton.isEnabled() && extendedMode) {
                        playSound("click.wav");
                        playRound("Lizard");
                    }
                },
                KeyStroke.getKeyStroke(KeyEvent.VK_L, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW
        );

        getRootPane().registerKeyboardAction(
                e -> {
                    if (spockButton.isEnabled() && extendedMode) {
                        playSound("click.wav");
                        playRound("Spock");
                    }
                },
                KeyStroke.getKeyStroke(KeyEvent.VK_K, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW
        );


    }

    // ========================================================================
    // BUTTON HOVER EFFECT
    // ========================================================================
    private void addButtonHoverEffect(JButton button, Color normalColor, Color hoverColor) {
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(normalColor);
                button.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
    }

    // ========================================================================
    // SAVE/LOAD PLAYER PROFILES
    // ========================================================================
    private void savePlayerProfiles() {
        if (playerStats == null || playerStats.isEmpty()) return;

        try {
            File profilesFile = new File("player_profiles.txt");
            BufferedWriter writer = new BufferedWriter(new FileWriter(profilesFile));

            for (Map.Entry<String, PlayerStats> entry : playerStats.entrySet()) {
                PlayerStats stats = entry.getValue();
                writer.write(String.format("%s|%d|%d|%d|%d\n",
                        stats.name,
                        stats.totalWins,
                        stats.totalLosses,
                        stats.totalTies,
                        stats.longestWinStreak
                ));
            }

            writer.close();
        } catch (IOException e) {
            System.out.println("Could not save profiles: " + e.getMessage());
        }
    }

    private void loadPlayerProfiles() {
        try {
            File profilesFile = new File("player_profiles.txt");
            if (!profilesFile.exists()) return;

            BufferedReader reader = new BufferedReader(new FileReader(profilesFile));
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 5) {
                    // Future use: can display returning player stats
                    System.out.println("Loaded profile: " + parts[0]);
                }
            }

            reader.close();
        } catch (IOException e) {
            System.out.println("Could not load profiles: " + e.getMessage());
        }
    }

    // ========================================================================
    // MAIN METHOD
    // ========================================================================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RockPaperScissorGame());
    }


}