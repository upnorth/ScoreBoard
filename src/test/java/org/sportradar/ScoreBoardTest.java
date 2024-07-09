package org.sportradar;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.sportradar.Match.NO_CHANGES_IN_SCORE_UPDATE;
import static org.sportradar.Match.SCORE_UPDATE_FOR_BOTH_TEAMS;
import static org.sportradar.ScoreBoard.MATCH_DOES_NOT_EXIST;
import static org.sportradar.ScoreBoard.ONE_MATCH_PER_TEAM;
import static org.sportradar.Team.*;

class ScoreBoardTest {

    public static final String TEAM_1_NAME = "Team 1";
    public static final String TEAM_2_NAME = "Team 2";
    public static final String TEAM_3_NAME = "Team 3";

    @Test
    void team_must_have_valid_non_null_name() {
        var scoreBoard = new ScoreBoard();

        // Felt redundant to have 4 distinct test cases for each combination
        assertThatThrownBy(() -> scoreBoard.newMatch(null, TEAM_1_NAME)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(INVALID_TEAM_NAME);

        assertThatThrownBy(() -> scoreBoard.newMatch("", TEAM_1_NAME)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(INVALID_TEAM_NAME);

        assertThatThrownBy(() -> scoreBoard.newMatch(TEAM_1_NAME, null)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(INVALID_TEAM_NAME);

        assertThatThrownBy(() -> scoreBoard.newMatch(TEAM_1_NAME, "")
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(INVALID_TEAM_NAME);
    }

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

        assertThatThrownBy(() -> scoreBoard.updateMatch(
                matchId,
                1,
                1)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(SCORE_UPDATE_FOR_BOTH_TEAMS);
    }

    @Test
    void match_can_not_be_updated_with_the_current_scores() {
        var scoreBoard = new ScoreBoard();
        var matchId = scoreBoard.newMatch(TEAM_1_NAME, TEAM_2_NAME);
        var match = scoreBoard.getMatch(matchId);

        assertThatThrownBy(() -> scoreBoard.updateMatch(
                matchId,
                match.getHomeTeam().getScore(),
                match.getAwayTeam().getScore())
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(NO_CHANGES_IN_SCORE_UPDATE);
    }

    @Test
    void team_can_not_get_lower_score_than_before() {
        var scoreBoard = new ScoreBoard();
        var matchId = scoreBoard.newMatch(TEAM_1_NAME, TEAM_2_NAME);
        var match = scoreBoard.getMatch(matchId);

        assertThatThrownBy(() -> scoreBoard.updateMatch(
                matchId,
                match.getHomeTeam().getScore() -1,
                match.getAwayTeam().getScore())
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(UPDATED_SCORE_LOWER);
    }

    @Test
    void team_can_not_score_more_than_one_goal_at_a_time() {
        var scoreBoard = new ScoreBoard();
        var matchId = scoreBoard.newMatch(TEAM_1_NAME, TEAM_2_NAME);
        var match = scoreBoard.getMatch(matchId);

        assertThatThrownBy(() -> scoreBoard.updateMatch(
                matchId,
                match.getHomeTeam().getScore() +2,
                match.getAwayTeam().getScore())
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(MAX_ONE_GOAL_INCREASE);
    }

    @Test
    void team_can_not_be_in_multiple_concurrent_matches() {
        var scoreBoard = new ScoreBoard();
        scoreBoard.newMatch(TEAM_1_NAME, TEAM_2_NAME);

        // Checks both home and away teams against being added as either in new matches
        // Felt redundant to have 4 distinct test cases for each combination
        assertThatThrownBy(() -> scoreBoard.newMatch(TEAM_1_NAME, TEAM_3_NAME)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ONE_MATCH_PER_TEAM);

        assertThatThrownBy(() -> scoreBoard.newMatch(TEAM_3_NAME, TEAM_1_NAME)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ONE_MATCH_PER_TEAM);

        assertThatThrownBy(() -> scoreBoard.newMatch(TEAM_2_NAME, TEAM_3_NAME)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ONE_MATCH_PER_TEAM);

        assertThatThrownBy(() -> scoreBoard.newMatch(TEAM_3_NAME, TEAM_2_NAME)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ONE_MATCH_PER_TEAM);
    }

    @Test
    void finishing_existing_match_should_remove_it_from_scoreboard() {
        var scoreBoard = new ScoreBoard();
        var matchId = scoreBoard.newMatch(TEAM_1_NAME, TEAM_2_NAME);

        scoreBoard.finishMatch(matchId);

        assertThat(scoreBoard.getMatch(matchId)).isNull();
    }

    @Test
    void match_that_does_not_exist_in_scoreboard_can_not_be_removed() {
        var scoreBoard = new ScoreBoard();
        var matchId = scoreBoard.newMatch(TEAM_1_NAME, TEAM_2_NAME);

        assertThatThrownBy(() -> scoreBoard.finishMatch(matchId +1)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(MATCH_DOES_NOT_EXIST);
    }
}