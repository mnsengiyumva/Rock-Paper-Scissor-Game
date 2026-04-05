package Main;

public class BracketMatch {
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