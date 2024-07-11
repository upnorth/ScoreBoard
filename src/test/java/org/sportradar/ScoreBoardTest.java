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
    public static final String TEAM_4_NAME = "Team 4";
    public static final String TEAM_5_NAME = "Team 5";
    public static final String TEAM_6_NAME = "Team 6";

    @Test
    void teams_must_have_valid_non_null_names() {
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
    void a_new_match_get_correct_initial_scores() {
        var scoreBoard = new ScoreBoard();

        var matchId = scoreBoard.newMatch(TEAM_1_NAME, TEAM_2_NAME);

        var match = scoreBoard.getMatch(matchId);

        assertThat(match.getHomeTeam().getName()).isEqualTo(TEAM_1_NAME);
        assertThat(match.getHomeTeam().getScore()).isEqualTo(0);
        assertThat(match.getAwayTeam().getName()).isEqualTo(TEAM_2_NAME);
        assertThat(match.getAwayTeam().getScore()).isEqualTo(0);
    }

    @Test
    void a_match_can_be_updated_with_valid_scores() {
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
    void a_match_can_not_be_updated_with_new_scores_for_both_teams_at_the_same_time() {
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
    void a_match_can_not_be_updated_with_the_current_scores() {
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
    void a_team_can_not_get_lower_score_than_before() {
        var scoreBoard = new ScoreBoard();
        var matchId = scoreBoard.newMatch(TEAM_1_NAME, TEAM_2_NAME);
        var match = scoreBoard.getMatch(matchId);

        assertThatThrownBy(() -> scoreBoard.updateMatch(
                matchId,
                match.getHomeTeam().getScore() - 1,
                match.getAwayTeam().getScore())
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(UPDATED_SCORE_LOWER);
    }

    @Test
    void a_team_can_not_score_more_than_one_goal_at_a_time() {
        var scoreBoard = new ScoreBoard();
        var matchId = scoreBoard.newMatch(TEAM_1_NAME, TEAM_2_NAME);
        var match = scoreBoard.getMatch(matchId);

        assertThatThrownBy(() -> scoreBoard.updateMatch(
                matchId,
                match.getHomeTeam().getScore() + 2,
                match.getAwayTeam().getScore())
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(MAX_ONE_GOAL_INCREASE);
    }

    @Test
    void a_team_can_not_be_in_multiple_concurrent_matches() {
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
    void finishing_an_existing_match_should_remove_it_from_scoreboard() {
        var scoreBoard = new ScoreBoard();
        var matchId = scoreBoard.newMatch(TEAM_1_NAME, TEAM_2_NAME);

        scoreBoard.finishMatch(matchId);

        assertThatThrownBy(() -> scoreBoard.getMatch(matchId)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(MATCH_DOES_NOT_EXIST);
    }

    @Test
    void a_match_that_does_not_exist_in_scoreboard_can_not_be_removed() {
        var scoreBoard = new ScoreBoard();
        var matchId = scoreBoard.newMatch(TEAM_1_NAME, TEAM_2_NAME);
        scoreBoard.finishMatch(matchId);

        assertThatThrownBy(() -> scoreBoard.updateMatch(matchId, 0, 1)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(MATCH_DOES_NOT_EXIST);
    }

    @Test
    void a_finished_match_can_not_get_updated_scores() {
        var scoreBoard = new ScoreBoard();
        var matchId = scoreBoard.newMatch(TEAM_1_NAME, TEAM_2_NAME);
        scoreBoard.finishMatch(matchId);

        assertThatThrownBy(() -> scoreBoard.finishMatch(matchId )
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(MATCH_DOES_NOT_EXIST);
    }

    @Test
    void summary_of_current_matches_should_have_correct_sorting_team_names_and_scores() {
        // Create scoreboard and add matches
        var scoreBoard = new ScoreBoard();
        var match1Id = scoreBoard.newMatch(TEAM_1_NAME, TEAM_2_NAME);
        var match2Id = scoreBoard.newMatch(TEAM_3_NAME, TEAM_4_NAME);
        scoreBoard.finishMatch(match1Id); // First match finished, enables rematch in #4
        var match3Id = scoreBoard.newMatch(TEAM_5_NAME, TEAM_6_NAME);
        var match4Id = scoreBoard.newMatch(TEAM_2_NAME, TEAM_1_NAME);

        // Update match scores
        scoreBoard.updateMatch(match2Id, 0, 1);

        scoreBoard.updateMatch(match3Id, 0, 1);
        scoreBoard.updateMatch(match3Id, 0, 2);
        scoreBoard.updateMatch(match3Id, 1, 2);

        scoreBoard.updateMatch(match4Id, 1, 0);

        // Verify that summary is ordered by bigger total score first, and lower match id second
        var summary = scoreBoard.getSummary();
        var nr1ThirdStarted3total = scoreBoard.getMatch(match3Id);
        var nr2secondStarted1total = scoreBoard.getMatch(match2Id);
        var nr3fourthStarted1total = scoreBoard.getMatch(match4Id);

        assertThat(summary.size()).isEqualTo(3);

        assertThat(summary.getFirst().homeTeamName()).isEqualTo(nr1ThirdStarted3total.getHomeTeam().getName());
        assertThat(summary.getFirst().homeTeamScore()).isEqualTo(nr1ThirdStarted3total.getHomeTeam().getScore());
        assertThat(summary.getFirst().awayTeamName()).isEqualTo(nr1ThirdStarted3total.getAwayTeam().getName());
        assertThat(summary.getFirst().awayTeamScore()).isEqualTo(nr1ThirdStarted3total.getAwayTeam().getScore());

        assertThat(summary.get(1).homeTeamName()).isEqualTo(nr2secondStarted1total.getHomeTeam().getName());
        assertThat(summary.get(1).homeTeamScore()).isEqualTo(nr2secondStarted1total.getHomeTeam().getScore());
        assertThat(summary.get(1).awayTeamName()).isEqualTo(nr2secondStarted1total.getAwayTeam().getName());
        assertThat(summary.get(1).awayTeamScore()).isEqualTo(nr2secondStarted1total.getAwayTeam().getScore());

        assertThat(summary.get(2).homeTeamName()).isEqualTo(nr3fourthStarted1total.getHomeTeam().getName());
        assertThat(summary.get(2).homeTeamScore()).isEqualTo(nr3fourthStarted1total.getHomeTeam().getScore());
        assertThat(summary.get(2).awayTeamName()).isEqualTo(nr3fourthStarted1total.getAwayTeam().getName());
        assertThat(summary.get(2).awayTeamScore()).isEqualTo(nr3fourthStarted1total.getAwayTeam().getScore());
    }
}