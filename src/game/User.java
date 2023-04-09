package game;

public class User {
    private int score;
    private String name;

    public User(String name, int score) {
        this.name = name;
        this.score = score;
    }
    public String getName() {return name;}
    public int getScore() {return score;}
}
