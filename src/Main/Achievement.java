package Main;

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