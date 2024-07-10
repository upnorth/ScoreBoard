package org.sportradar;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class ScoreBoard {

    static final String ONE_MATCH_PER_TEAM = "One of the teams is already in a match";
    static final String MATCH_DOES_NOT_EXIST = "Match does not exist";

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
                .flatMap(match -> Stream.of(
                        match.getHomeTeam().getName(),
                        match.getAwayTeam().getName())
                )
                .anyMatch(teamName -> teamName.equals(homeTeam) || teamName.equals(awayTeam));
    }

    Match getMatch(int matchId) {
        return matches.get(matchId);
    }

    public void updateMatch(int matchId, int newHomeTeamScore, int newAwayTeamScore) {
        Match match = matches.get(matchId);
        match.updateScores(newHomeTeamScore, newAwayTeamScore);
    }

    public void finishMatch(int matchId) {
        if (!matches.containsKey(matchId)) {
            throw new IllegalArgumentException(MATCH_DOES_NOT_EXIST);
        }

        matches.remove(matchId);
    }

    public List<Match> getSummary() {
        return matches.entrySet().stream()
                .sorted(Comparator.comparingInt((Map.Entry<Integer, Match> entry) -> entry.getValue().getTotalScore())
                        .reversed()
                        .thenComparing(Map.Entry::getKey))
                .map(Map.Entry::getValue)
                .toList();
    }
}
