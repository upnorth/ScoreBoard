package org.sportradar;

class Match {

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
            throw new IllegalArgumentException("Updated scores are identical to current scores");
        }

        if (newHomeTeamScore != homeTeam.getScore() && newAwayTeamScore != awayTeam.getScore()) {
            throw new IllegalArgumentException("Both teams can't score at the same time");
        }

        this.homeTeam.updateScore(newHomeTeamScore);
        this.awayTeam.updateScore(newAwayTeamScore);
    }
}
