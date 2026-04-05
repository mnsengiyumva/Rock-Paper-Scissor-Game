
package Main;

import Main.BracketMatch;
import java.util.ArrayList;

public class TournamentBracket {
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
        return rounds.getLast().getFirst().winner;
    }
}