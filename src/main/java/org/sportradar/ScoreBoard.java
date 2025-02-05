package org.sportradar;

import java.util.*;
import java.util.stream.Stream;

public class ScoreBoard {

    public static final String TEAM_IS_NOT_IN_AN_ACTIVE_MATCH = "Team is not in an active match";
    static final String ONE_MATCH_PER_TEAM = "One of the teams is already in a match";
    static final String MATCH_DOES_NOT_EXIST = "Match does not exist";
    private final List<Match> matches;
    private int nextMatchId;

    public ScoreBoard() {
        nextMatchId = 0;
        matches = new ArrayList<>();
    }

    public int newMatch(String homeTeam, String awayTeam) {
        if (anyTeamIsAlreadyInAnOngoingMatch(homeTeam, awayTeam)) {
            throw new IllegalArgumentException(ONE_MATCH_PER_TEAM);
        }

        var newMatchId = nextMatchId;
        nextMatchId++;

        matches.add(new Match(newMatchId, homeTeam, awayTeam));

        return newMatchId;
    }

    private boolean anyTeamIsAlreadyInAnOngoingMatch(String homeTeam, String awayTeam) {
        // This check feels pretty heavy and there are probably better ways to implement it.
        // Although new matches doesn't start too often, so it might be ok depending on total # of matches in the scoreboard.
        return matches.stream()
                .flatMap(match -> Stream.of(
                        match.getHomeTeam().getName(),
                        match.getAwayTeam().getName())
                )
                .anyMatch(teamName -> teamName.equals(homeTeam) || teamName.equals(awayTeam));
    }

    Match getMatch(int matchId) {
        return matches.stream()
                .filter(match -> match.getId() == matchId)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(MATCH_DOES_NOT_EXIST));
    }

    public void updateMatch(int matchId, int newHomeTeamScore, int newAwayTeamScore) {
        Match match = getMatch(matchId);
        match.updateScores(newHomeTeamScore, newAwayTeamScore);
    }

    public void finishMatch(int matchId) {
        Match matchToRemove = matches.stream()
                .filter(match -> match.getId() == matchId)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(MATCH_DOES_NOT_EXIST));

        matches.remove(matchToRemove);
    }

    public List<MatchSummary> getSummary() {
        return matches.stream()
                .sorted(Comparator.comparingInt(Match::getTotalScore).reversed()
                        .thenComparing(Comparator.comparingInt(Match::getId).reversed()))
                .map(match -> new MatchSummary(
                        match.getHomeTeam().getName(),
                        match.getHomeTeam().getScore(),
                        match.getAwayTeam().getName(),
                        match.getAwayTeam().getScore()))
                .toList();
    }

    public int getCurrentGoalsForTeam(String teamName) {
        return matches.stream()
                .flatMap(match -> Stream.of(match.getHomeTeam(), match.getAwayTeam()))
                .filter(team -> team.getName().equals(teamName))
                .map(Team::getScore)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(TEAM_IS_NOT_IN_AN_ACTIVE_MATCH));
    }
}
