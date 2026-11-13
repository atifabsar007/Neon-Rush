package com.neonrush.logic

import org.junit.Assert.*
import org.junit.Test
import kotlin.random.Random

class RunSessionTest {
    @Test fun `lane is clamped`() {
        val session = RunSession(Random(7))
        repeat(10) { session.moveLeft() }
        assertEquals(0, session.lane)
        repeat(10) { session.moveRight() }
        assertEquals(2, session.lane)
    }

    @Test fun `jump and slide cannot overlap`() {
        val session = RunSession(Random(7))
        assertTrue(session.jump())
        assertFalse(session.slide())
        repeat(20) { session.step(0.05f) }
        assertTrue(session.slide())
        assertFalse(session.jump())
    }

    @Test fun `collecting an orb increases score`() {
        val session = RunSession(Random(2))
        session.objects += RunSession.TrackObject(99, 1, RunSession.Kind.ORB, RunSession.PLAYER_Y + 1f)
        val previous = session.score
        val result = session.step(0.05f)
        assertEquals(1, result.collected)
        assertEquals(1, session.orbsCollected)
        assertTrue(session.score >= previous + 75)
    }

    @Test fun `collision ends the run and freezes subsequent updates`() {
        val session = RunSession(Random(7))
        session.objects += RunSession.TrackObject(99, 1, RunSession.Kind.BLOCK, RunSession.PLAYER_Y + 1f)
        assertTrue(session.step(0.05f).crashed)
        val score = session.score
        session.step(0.05f)
        assertEquals(score, session.score)
    }

    @Test fun `jump clears low barrier`() {
        val session = RunSession(Random(7))
        session.objects += RunSession.TrackObject(99, 1, RunSession.Kind.LOW_BARRIER, RunSession.PLAYER_Y + 1f)
        assertTrue(session.jump())
        assertFalse(session.step(0.05f).crashed)
    }
}
