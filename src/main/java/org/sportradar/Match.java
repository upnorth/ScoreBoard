package org.sportradar;

public class Match {

    private final String homeTeam;
    private int homeTeamScore;

    private final String awayTeam;
    private int awayTeamScore;

    public Match(String homeTeam, String awayTeam) {
        this.homeTeam = homeTeam;
        this.homeTeamScore = 0;
        this.awayTeam = awayTeam;
        this.awayTeamScore = 0;
    }

    public int getHomeTeamScore() {
        return homeTeamScore;
    }

    public int getAwayTeamScore() {
        return awayTeamScore;
    }

    public String getHomeTeamName() {
        return homeTeam;
    }

    public String getAwayTeamName() {
        return awayTeam;
    }
}
