package org.sportradar;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ScoreBoardTests {

    public static final String TEAM_1_NAME = "Team 1";
    public static final String TEAM_2_NAME = "Team 2";

    @Test
    void new_match_has_correct_initial_scores() {
        var scoreBoard = new ScoreBoard();

        var matchId = scoreBoard.newMatch(TEAM_1_NAME, TEAM_2_NAME);

        var match = scoreBoard.getMatch(matchId);

        assertThat(match.getHomeTeam().getName()).isEqualTo(TEAM_1_NAME);
        assertThat(match.getHomeTeam().getScore()).isEqualTo(0);
        assertThat(match.getAwayTeam().getName()).isEqualTo(TEAM_2_NAME);
        assertThat(match.getAwayTeam().getScore()).isEqualTo(0);
    }

    @Test
    void match_can_be_updated_with_valid_scores() {
        var scoreBoard = new ScoreBoard();

        var matchId = scoreBoard.newMatch(TEAM_1_NAME, TEAM_2_NAME);
        scoreBoard.updateMatch(matchId, 1, 0);

        var match = scoreBoard.getMatch(matchId);

        assertThat(match.getHomeTeam().getName()).isEqualTo(TEAM_1_NAME);
        assertThat(match.getHomeTeam().getScore()).isEqualTo(1);
        assertThat(match.getAwayTeam().getName()).isEqualTo(TEAM_2_NAME);
        assertThat(match.getAwayTeam().getScore()).isEqualTo(0);
    }
}