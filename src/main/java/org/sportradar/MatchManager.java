package org.sportradar;

import java.util.HashMap;
import java.util.Map;

public class MatchManager {

    private int nextMatchId;
    private final Map<Integer, Match> matches;

    public MatchManager() {
        nextMatchId = 0;
        matches = new HashMap<>();
    }

    public int newMatch(String homeTeam, String awayTeam) {
        var newMatchId = nextMatchId;
        nextMatchId++;

        matches.put(newMatchId, new Match(homeTeam, awayTeam));

        return newMatchId;
    }

    public Match getMatch(int matchId) {
        return matches.get(matchId);
    }

}
