package Main;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class PlayerStats {

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
    int maxDeficit = 0;

    void updateDeficit(int opponentScore, int myScore) {
        int deficit = opponentScore - myScore;
        if (deficit > maxDeficit) {
            maxDeficit = deficit;
        }
    }

    /**
     * PlayerStarts Constructor
     * @param name
     */
    PlayerStats(String name) {
        this.name = name;
        choiceCount.put("Rock", 0);
        choiceCount.put("Paper", 0);
        choiceCount.put("Scissors", 0);
        choiceCount.put("Lizard", 0);   // ADD
        choiceCount.put("Spock", 0);

    }

    /**
     *This methods records wins of each player
     * It uses switch statement to  increment a correct choice
     * @param choice
     */

    void recordWin(String choice) {
        totalWins++;
        currentWinStreak++;
        if (currentWinStreak > longestWinStreak) {
            longestWinStreak = currentWinStreak;
        }

        switch (choice) {
            case "Rock" -> rockWins++;
            case "Paper" -> paperWins++;
            case "Scissors" -> scissorsWins++;
            case "Lizard" -> lizardWins++;  // ADD
            case "Spock" -> spockWins++;
        }

        recordChoice(choice);
    }

    /**
     * This method records loses for a player
     * @param choice
     */

    void recordLoss(String choice) {
        totalLosses++;
        currentWinStreak = 0;
        recordChoice(choice);
    }

    /**
     * This methods handles the tie situation
     * @param choice
     */

    void recordTie(String choice) {
        totalTies++;
        recordChoice(choice);
    }

    /**
     * This Method records player choices in HashMap, key: choice, value: choiceCount
     */

    void recordChoice(String choice) {
        choiceCount.put(choice, choiceCount.get(choice) + 1);
        updateFavoriteChoice();
    }

    /**
     * This tracks the favorite choice of a player,
     *
     */

    void updateFavoriteChoice() {
        int maxCount = 0;
        for (Map.Entry<String, Integer> entry : choiceCount.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                favoriteChoice = entry.getKey();
            }
        }
    }

    /**
     *
     * @return This method returns player's wins losses, and ties combined
     */

    int getTotalGames() {
        return totalWins + totalLosses + totalTies;
    }

    /**
     * This method calculates the win percentage of each player
     * after considering the tota wins, losses, and ties
     *
     */

    double getWinRate() {
        int total = getTotalGames();
        return total > 0 ? (totalWins * 100.0 / total) : 0;
    }
}