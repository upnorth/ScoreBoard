package org.sportradar;

import java.util.HashMap;
import java.util.Map;

public class ScoreBoard {

    static final String ONE_MATCH_PER_TEAM = "One of the teams is already in a match";

    private int nextMatchId;
    private final Map<Integer, Match> matches;

    public ScoreBoard() {
        nextMatchId = 0;
        matches = new HashMap<>();
    }

    public int newMatch(String homeTeam, String awayTeam) {
        if (anyTeamIsAlreadyInAnOngoingMatch(homeTeam, awayTeam)) {
            throw new IllegalArgumentException(ONE_MATCH_PER_TEAM);
        }

        var newMatchId = nextMatchId;
        nextMatchId++;

        matches.put(newMatchId, new Match(homeTeam, awayTeam));

        return newMatchId;
    }

    private boolean anyTeamIsAlreadyInAnOngoingMatch(String homeTeam, String awayTeam) {
        // This check feels pretty heavy and there are probably better ways to implement it.
        // Although new matches doesn't start too often, so it might be ok depending on total # of matches in the scoreboard.
        return matches.values().stream()
                .anyMatch(match -> match.getHomeTeam().getName().equals(homeTeam)
                        || match.getAwayTeam().getName().equals(awayTeam)
                        || match.getHomeTeam().getName().equals(awayTeam)
                        || match.getAwayTeam().getName().equals(homeTeam));
    }

    Match getMatch(int matchId) {
        return matches.get(matchId);
    }

    public void updateMatch(int matchId, int newHomeTeamScore, int newAwayTeamScore) {
        Match match = matches.get(matchId);
        match.updateScores(newHomeTeamScore, newAwayTeamScore);
    }
}
