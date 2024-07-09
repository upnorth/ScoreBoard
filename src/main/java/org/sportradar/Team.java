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
}
