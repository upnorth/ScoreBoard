package org.sportradar;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MatchManagerTest {

    @Test
    void Test() {
        var matchManager = new MatchManager();

        assertNotNull(matchManager);
    }
}