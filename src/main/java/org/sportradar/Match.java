package org.sportradar;

public class Match {

    static final String NO_CHANGES_IN_SCORE_UPDATE = "Updated scores are identical to current scores";
    static final String SCORE_UPDATE_FOR_BOTH_TEAMS = "Both teams can't score at the same time";

    private final int id;
    private final Team homeTeam;
    private final Team awayTeam;

    Match(int id, String homeTeamName, String awayTeamName) {
        this.id = id;
        homeTeam = new Team(homeTeamName);
        awayTeam = new Team(awayTeamName);
    }

    Team getHomeTeam() {
        return homeTeam;
    }

    Team getAwayTeam() {
        return awayTeam;
    }

    public void updateScores(int newHomeTeamScore, int newAwayTeamScore) {
        if (newHomeTeamScore == homeTeam.getScore() && newAwayTeamScore == awayTeam.getScore()) {
            throw new IllegalArgumentException(NO_CHANGES_IN_SCORE_UPDATE);
        }

        if (newHomeTeamScore != homeTeam.getScore() && newAwayTeamScore != awayTeam.getScore()) {
            throw new IllegalArgumentException(SCORE_UPDATE_FOR_BOTH_TEAMS);
        }

        homeTeam.updateScore(newHomeTeamScore);
        awayTeam.updateScore(newAwayTeamScore);
    }

    public int getTotalScore() {
        return homeTeam.getScore() + awayTeam.getScore();
    }

    public int getId() {
        return id;
    }
}
