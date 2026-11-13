package com.neonrush.logic

import org.junit.Assert.*
import org.junit.Test

class ScoreRulesTest {
    @Test fun `orbs and distance contribute to score`() {
        assertEquals(260, ScoreRules.score(110.9f, 2))
        assertEquals(0, ScoreRules.score(-5f, -1))
    }

    @Test fun `speed increases but remains capped`() {
        assertEquals(250f, ScoreRules.speed(0f), 0.0001f)
        assertTrue(ScoreRules.speed(25f) > ScoreRules.speed(2f))
        assertEquals(590f, ScoreRules.speed(9999f), 0.0001f)
    }

    @Test fun `obstacle interval decreases but stays playable`() {
        assertTrue(ScoreRules.obstacleInterval(100f) < ScoreRules.obstacleInterval(0f))
        assertEquals(0.72f, ScoreRules.obstacleInterval(999f), 0.0001f)
    }
}
