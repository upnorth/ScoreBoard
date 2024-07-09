package org.sportradar;

public class Team {
    private final String name;
    private int score;

    Team(String name) {
        this.name = name;
        this.score = 0;
    }

    public String getName() {
        return this.name;
    }

    public int getScore() {
        return this.score;
    }

    public void updateScore(int newScore) {
        if (newScore < this.score || newScore - this.score > 1) {
            throw new IllegalArgumentException("A new score can not be lower than the previous or increase with more than 1");
        }

        this.score = newScore;
    }
}
