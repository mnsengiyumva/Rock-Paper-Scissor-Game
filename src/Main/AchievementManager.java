package Main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

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
