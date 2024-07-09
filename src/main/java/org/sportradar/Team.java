package org.sportradar;

public class Team {

    static final String UPDATED_SCORE_LOWER = "A new score can not be lower than the previous";
    static final String MAX_ONE_GOAL_INCREASE = "A new score can not increase with more than 1 goal";

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
        if (newScore < this.score) {
            throw new IllegalArgumentException(UPDATED_SCORE_LOWER);
        }

        if (newScore - this.score > 1) {
            throw new IllegalArgumentException(MAX_ONE_GOAL_INCREASE);
        }

        this.score = newScore;
    }
}
