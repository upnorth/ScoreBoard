package org.sportradar;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

    @Test
    void match_can_not_be_updated_with_new_scores_for_both_teams_at_the_same_time() {
        var scoreBoard = new ScoreBoard();
        var matchId = scoreBoard.newMatch(TEAM_1_NAME, TEAM_2_NAME);

        assertThatThrownBy(() -> scoreBoard.updateMatch(matchId, 1, 1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void match_can_not_be_updated_with_the_current_scores() {
        // To be tested
    }

    @Test
    void team_can_not_get_lower_score_than_before() {
        // To be tested
    }

    @Test
    void team_can_not_score_more_than_one_goal_at_a_time() {
        // To be tested
    }
}