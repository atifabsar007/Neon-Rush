package com.neonrush.service

import com.badlogic.gdx.Gdx
import com.neonrush.logic.RunSession

/** Local-first profile and progression storage. No account data leaves the device. */
class SaveRepository {
    private val prefs = Gdx.app.getPreferences("neon_rush_preferences")

    var soundEnabled: Boolean
        get() = prefs.getBoolean("soundEnabled", true)
        set(value) { prefs.putBoolean("soundEnabled", value).flush() }

    var musicEnabled: Boolean
        get() = prefs.getBoolean("musicEnabled", true)
        set(value) { prefs.putBoolean("musicEnabled", value).flush() }

    var bestScore: Int
        get() = prefs.getInteger("bestScore", 0)
        private set(value) { prefs.putInteger("bestScore", value.coerceAtLeast(0)).flush() }

    var activeMissionIndex: Int
        get() = prefs.getInteger("activeMissionIndex", 0)
        set(value) { prefs.putInteger("activeMissionIndex", value.coerceAtLeast(0)).flush() }

    var missionsCompleted: Int
        get() = prefs.getInteger("missionsCompleted", 0)
        set(value) { prefs.putInteger("missionsCompleted", value.coerceAtLeast(0)).flush() }

    var playerName: String
        get() = prefs.getString("playerName", "NEON RUNNER").ifBlank { "NEON RUNNER" }
        set(value) { prefs.putString("playerName", value.trim().take(18).ifBlank { "NEON RUNNER" }).flush() }

    val totalRuns: Int get() = prefs.getInteger("totalRuns", 0)
    val totalDistance: Int get() = prefs.getInteger("totalDistance", 0)
    val totalOrbs: Int get() = prefs.getInteger("totalOrbs", 0)
    val coins: Int get() = prefs.getInteger("coins", 0)
    val xp: Int get() = prefs.getInteger("xp", 0)
    var avatarIndex: Int
        get() = prefs.getInteger("avatarIndex", 0).coerceIn(0, 5)
        set(value) { prefs.putInteger("avatarIndex", value.coerceIn(0, 5)).flush() }
    val playerLevel: Int get() = (xp / 500) + 1
    val xpIntoLevel: Int get() = xp % 500

    fun applyCloudProgress(progress: AuthService.Progress) {
        prefs.putInteger("bestScore", maxOf(bestScore, progress.score))
        prefs.putInteger("xp", maxOf(xp, progress.xp))
        prefs.putInteger("coins", maxOf(coins, progress.coins))
        prefs.putInteger("totalRuns", maxOf(totalRuns, progress.runs))
        prefs.putInteger("totalDistance", maxOf(totalDistance, progress.distance))
        prefs.putInteger("totalOrbs", maxOf(totalOrbs, progress.orbs))
        prefs.flush()
    }

    fun recordScore(score: Int): Boolean {
        if (score <= bestScore) return false
        bestScore = score
        return true
    }

    /** Called exactly once when a run ends. Returns the earned coin reward. */
    fun recordRun(summary: RunSession.Summary): Int {
        val reward = (summary.distanceMeters / 50) + (summary.orbsCollected * 3)
        prefs.putInteger("totalRuns", totalRuns + 1)
        prefs.putInteger("totalDistance", totalDistance + summary.distanceMeters)
        prefs.putInteger("totalOrbs", totalOrbs + summary.orbsCollected)
        prefs.putInteger("coins", coins + reward)
        prefs.putInteger("xp", xp + summary.score.coerceAtLeast(0))
        prefs.flush()
        return reward
    }
}
