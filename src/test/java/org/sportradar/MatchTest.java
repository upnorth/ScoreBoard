package org.sportradar;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.sportradar.Match.NO_CHANGES_IN_SCORE_UPDATE;
import static org.sportradar.Match.SCORE_UPDATE_FOR_BOTH_TEAMS;

class MatchTest {

    public static final String TEAM_1_NAME = "Team 1";
    public static final String TEAM_2_NAME = "Team 2";

    @Test
    void a_match_can_be_updated_with_one_more_goal_for_home_team() {
        var match = new Match(1, TEAM_1_NAME, TEAM_2_NAME);

        match.updateScores(
                match.getHomeTeam().getScore() + 1,
                match.getAwayTeam().getScore()
        );

        assertThat(match.getHomeTeam().getScore()).isEqualTo(1);
        assertThat(match.getAwayTeam().getScore()).isEqualTo(0);
    }

    @Test
    void a_match_can_be_updated_with_one_more_goal_for_away_team() {
        var match = new Match(1, TEAM_1_NAME, TEAM_2_NAME);

        match.updateScores(
                match.getHomeTeam().getScore(),
                match.getAwayTeam().getScore() + 1);

        assertThat(match.getHomeTeam().getScore()).isEqualTo(0);
        assertThat(match.getAwayTeam().getScore()).isEqualTo(1);
    }

    @Test
    void a_match_can_not_be_updated_with_new_scores_for_both_teams_at_the_same_time() {
        var match = new Match(1, TEAM_1_NAME, TEAM_2_NAME);

        assertThatThrownBy(() -> match.updateScores(
                match.getHomeTeam().getScore() + 1,
                match.getAwayTeam().getScore() + 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(SCORE_UPDATE_FOR_BOTH_TEAMS);
    }

    @Test
    void a_match_can_not_be_updated_with_the_current_scores() {
        var match = new Match(1, TEAM_1_NAME, TEAM_2_NAME);

        assertThatThrownBy(() -> match.updateScores(
                match.getHomeTeam().getScore(),
                match.getAwayTeam().getScore())
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(NO_CHANGES_IN_SCORE_UPDATE);
    }

}