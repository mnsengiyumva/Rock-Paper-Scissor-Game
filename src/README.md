# Rock Paper Scissors — Ultimate Edition

A feature-rich desktop Rock Paper Scissors game built in Java Swing, complete with multiple difficulty levels, extended gameplay (Lizard & Spock), a tournament bracket system, player profiles, achievements, and particle effects.

---

## Features

### Core Gameplay
- Classic **Rock, Paper, Scissors** — plus the **Lizard & Spock** extension (as popularized by *The Big Bang Theory*)
- Three AI difficulty levels: **Easy**, **Medium**, and **Hard**
- Animated UI with custom hover effects and rounded borders

### Player Profiles
- Multiple named player profiles stored in `player_profiles.txt`
- Per-player statistics tracked across sessions:
    - Total wins, losses, and ties
    - Win rate and longest win streak
    - Per-choice win counts (Rock, Paper, Scissors, Lizard, Spock)
    - Favourite choice (auto-detected from history)
    - Largest comeback deficit recovered

### Tournament Mode
- Multi-player **tournament bracket** supporting 2–N participants
- Automatic bracket generation and round advancement
- Match-by-match play with a live bracket display

### Achievement System
- Unlockable achievements tracked per player
- Achievements checked automatically after each round
- Visual summary of earned badges with emoji icons

### Visual Polish
- **Particle explosion effects** on win/loss events
- Custom **image background panel**
- Smooth component resize handling
- Consistent themed color palette with hover animations

---

## Project Structure

```
Main.java                  — Entry point
RockPaperScissorGame.java  — Main game window (JFrame) and round logic
  └── Difficulty            — Enum: EASY | MEDIUM | HARD
PlayerStats.java           — Per-player statistics model
Achievement.java           — Achievement data model
AchievementManager.java    — Achievement unlock logic and registry
TournamentBracket.java     — Bracket generation and advancement
BracketMatch.java          — Individual match model
ParticlePanel.java         — Particle effect overlay panel
Particle.java              — Individual particle physics & rendering
ImagePanel.java            — Background image rendering panel
RoundedBorder.java         — Custom rounded UI border component
player_profiles.txt        — Persistent player profile data
```

---

## Player Profile Format

Profiles are saved to `player_profiles.txt`, one player per line:

```
PlayerName|wins|losses|ties|streak
```

Example:
```
Player 1|2|1|2|2
Player 2|2|1|2|2
Player 3|4|0|1|4
```

---

## Getting Started

### Requirements
- Java 11 or higher (Java 21 recommended)
- No external dependencies — pure Java SE + Swing

### Running the Game

```bash
# Compile all source files
javac -d out src/Main/*.java

# Run
java -cp out Main.Main
```

Or if running from pre-compiled `.class` files:

```bash
java -cp . Main.Main
```

---

## Game Rules

| Choice   | Beats              | Loses to            |
|----------|--------------------|---------------------|
| Rock     | Scissors, Lizard   | Paper, Spock        |
| Paper    | Rock, Spock        | Scissors, Lizard    |
| Scissors | Paper, Lizard      | Rock, Spock         |
| Lizard   | Paper, Spock       | Rock, Scissors      |
| Spock    | Rock, Scissors     | Paper, Lizard       |

---

## Difficulty Levels

| Level  |   | AI Behaviour                                      |   |
|--------|---|---------------------------------------------------|---|
| Easy   |   | AI picks randomly                                 |   |
| Medium |   | AI uses mild counter-strategy based on history    |   |
| Hard   |   | AI actively predicts and counters your patterns   |   |

---

## License

This project is for personal/educational use. Feel free to fork and extend it.