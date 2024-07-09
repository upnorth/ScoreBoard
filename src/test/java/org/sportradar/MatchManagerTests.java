package org.sportradar;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MatchManagerTests {

    public static final String TEAM_1_NAME = "Team 1";
    public static final String TEAM_2_NAME = "Team 2";

    @Test
    void new_match_has_correct_initial_scores() {
        var matchManager = new MatchManager();

        var matchId = matchManager.newMatch(TEAM_1_NAME, TEAM_2_NAME);

        var match = matchManager.getMatch(matchId);

        assertThat(match.getHomeTeamName()).isEqualTo(TEAM_1_NAME);
        assertThat(match.getHomeTeamScore()).isEqualTo(0);
        assertThat(match.getAwayTeamName()).isEqualTo(TEAM_2_NAME);
        assertThat(match.getAwayTeamScore()).isEqualTo(0);
    }
}