package org.sportradar;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.sportradar.Team.*;

class TeamTest {

    @Test
    void team_with_valid_name_can_be_created() {
        assertThat(new Team("Valid name").getName())
                .isEqualTo("Valid name");
    }

    @Test
    void team_name_can_not_be_null() {
        assertThatThrownBy(() -> new Team(null)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(INVALID_TEAM_NAME);
    }

    @Test
    void team_name_can_not_be_empty_string() {
        assertThatThrownBy(() -> new Team("")
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(INVALID_TEAM_NAME);
    }

    @Test
    void team_can_get_a_new_goal() {
        var team = new Team("Team 1");

        team.updateScore(team.getScore() +1);

        assertThat(team.getScore()).isEqualTo(1);
    }

    @Test
    void team_can_not_get_lower_score_than_before() {
        var team = new Team("Team 1");

        assertThatThrownBy(() -> team.updateScore(team.getScore() -1)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(UPDATED_SCORE_LOWER);
    }

    @Test
    void team_can_not_score_more_than_one_goal_at_a_time() {
        var team = new Team("Team 1");

        assertThatThrownBy(() -> team.updateScore(team.getScore() +2)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(MAX_ONE_GOAL_INCREASE);
    }
}