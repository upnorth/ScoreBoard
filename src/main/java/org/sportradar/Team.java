package org.sportradar;

class Team {

    static final String UPDATED_SCORE_LOWER = "A new score can not be lower than the previous";
    static final String MAX_ONE_GOAL_INCREASE = "A new score can not increase with more than 1 goal";
    static final String INVALID_TEAM_NAME = "Team name cannot be null or empty";

    private final String name;
    private int score;

    Team(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException(INVALID_TEAM_NAME);
        }

        this.name = name;
        this.score = 0;
    }

    String getName() {
        return name;
    }

    int getScore() {
        return score;
    }

    void updateScore(int newScore) {
        if (newScore < score) {
            throw new IllegalArgumentException(UPDATED_SCORE_LOWER);
        }

        if (newScore - score > 1) {
            throw new IllegalArgumentException(MAX_ONE_GOAL_INCREASE);
        }

        score = newScore;
    }
}
