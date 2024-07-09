package org.sportradar;

class Match {

    static final String NO_CHANGES_IN_SCORE_UPDATE = "Updated scores are identical to current scores";
    static final String SCORE_UPDATE_FOR_BOTH_TEAMS = "Both teams can't score at the same time";

    private final Team homeTeam;
    private final Team awayTeam;

    Match(String homeTeamName, String awayTeamName) {
        this.homeTeam = new Team(homeTeamName);
        this.awayTeam = new Team(awayTeamName);
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

        this.homeTeam.updateScore(newHomeTeamScore);
        this.awayTeam.updateScore(newAwayTeamScore);
    }
}
