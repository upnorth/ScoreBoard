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

    @Test
    void test_of_example_summary_from_task_description() {
        /*
         * a. Mexico 0 - Canada 5
         * b. Spain 10 - Brazil 2
         * c. Germany 2 - France 2
         * d. Uruguay 6 - Italy 6
         * e. Argentina 3 - Australia 1
         * -----------------------------
         * The summary should be as follows:
         * 1. Uruguay 6 - Italy 6 // Same total as Spain - Brazil but started after that?
         * 2. Spain 10 - Brazil 2
         * 3. Mexico 0 - Canada 5
         * 4. Argentina 3 - Australia 1 // Same total as Germany - France but started after that?
         * 5. Germany 2 - France 2
         * ------------------------------
         * Test summary (as asserted below):
         * 1. Spain 10 - Brazil 2
         * 2. Uruguay 6 - Italy 6
         * 3. Mexico 0 - Canada 5
         * 4. Germany 2 - France 2
         * 5. Argentina 3 - Australia 1
        */
        var scoreBoard = new ScoreBoard();
        String mexico = "Mexico";
        String canada = "Canada";
        var meCaMatchId = scoreBoard.newMatch(mexico, canada);
        String spain = "Spain";
        String brazil = "Brazil";
        var spBrMatchId = scoreBoard.newMatch(spain, brazil);
        String germany = "Germany";
        String france = "France";
        var geFrMatchId = scoreBoard.newMatch(germany, france);
        String uruguay = "Uruguay";
        String italy = "Italy";
        var urItMatchId = scoreBoard.newMatch(uruguay, italy);
        String argentina = "Argentina";
        String australia = "Australia";
        var arAuMatchId = scoreBoard.newMatch(argentina, australia);

        int mexicoEndScore = 0;
        int canadaEndScore = 5;
        simulateEndResult(scoreBoard, meCaMatchId, mexicoEndScore, canadaEndScore);
        int spainEndScore = 10;
        int brazilEndScore = 2;
        simulateEndResult(scoreBoard, spBrMatchId, spainEndScore, brazilEndScore);
        int germanyEndScore = 2;
        int franceEndScore = 2;
        simulateEndResult(scoreBoard, geFrMatchId, germanyEndScore, franceEndScore);
        int uruguayEndScore = 6;
        int italyEndScore = 6;
        simulateEndResult(scoreBoard, urItMatchId, uruguayEndScore, italyEndScore);
        int argentinaEndScore = 3;
        int australiaEndScore = 1;
        simulateEndResult(scoreBoard, arAuMatchId, argentinaEndScore, australiaEndScore);

        var summary = scoreBoard.getSummary();
        assertThat(summary.size()).isEqualTo(5);

        assertThat(summary.get(0).homeTeamName()).isEqualTo(spain);
        assertThat(summary.get(0).homeTeamScore()).isEqualTo(spainEndScore);
        assertThat(summary.get(0).awayTeamName()).isEqualTo(brazil);
        assertThat(summary.get(0).awayTeamScore()).isEqualTo(brazilEndScore);

        assertThat(summary.get(1).homeTeamName()).isEqualTo(uruguay);
        assertThat(summary.get(1).homeTeamScore()).isEqualTo(uruguayEndScore);
        assertThat(summary.get(1).awayTeamName()).isEqualTo(italy);
        assertThat(summary.get(1).awayTeamScore()).isEqualTo(italyEndScore);

        assertThat(summary.get(2).homeTeamName()).isEqualTo(mexico);
        assertThat(summary.get(2).homeTeamScore()).isEqualTo(mexicoEndScore);
        assertThat(summary.get(2).awayTeamName()).isEqualTo(canada);
        assertThat(summary.get(2).awayTeamScore()).isEqualTo(canadaEndScore);

        assertThat(summary.get(3).homeTeamName()).isEqualTo(germany);
        assertThat(summary.get(3).homeTeamScore()).isEqualTo(germanyEndScore);
        assertThat(summary.get(3).awayTeamName()).isEqualTo(france);
        assertThat(summary.get(3).awayTeamScore()).isEqualTo(franceEndScore);

        assertThat(summary.get(4).homeTeamName()).isEqualTo(argentina);
        assertThat(summary.get(4).homeTeamScore()).isEqualTo(argentinaEndScore);
        assertThat(summary.get(4).awayTeamName()).isEqualTo(australia);
        assertThat(summary.get(4).awayTeamScore()).isEqualTo(australiaEndScore);
    }

    private void simulateEndResult(ScoreBoard scoreBoard, int matchId, int homeEndScore, int awayEndScore) {
        for (int nextHomeScore = 1; nextHomeScore <= homeEndScore; nextHomeScore++) {
            scoreBoard.updateMatch(matchId, nextHomeScore, 0);
        }

        for (int nextAwayScore = 1; nextAwayScore <= awayEndScore; nextAwayScore++) {
            scoreBoard.updateMatch(matchId, homeEndScore, nextAwayScore);
        }
    }
}