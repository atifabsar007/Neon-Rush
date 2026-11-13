package com.neonrush.service

import com.neonrush.logic.RunSession

class MissionManager(private val saves: SaveRepository) {
    enum class Metric { DISTANCE, ORBS, SCORE, JUMPS, SLIDES, LANE_SWITCHES }

    data class Mission(
        val id: Int,
        val title: String,
        val description: String,
        val metric: Metric,
        val target: Int
    )

    data class Progress(val current: Int, val target: Int) {
        val isComplete: Boolean get() = current >= target
        fun label(): String = "$current / $target"
    }

    data class Outcome(
        val mission: Mission,
        val progress: Progress,
        val completedNow: Boolean,
        val totalCompleted: Int
    )

    private val missions = listOf(
        Mission(0, "Warm-Up Run", "Reach 400 meters in a single run", Metric.DISTANCE, 400),
        Mission(1, "Energy Hunter", "Collect 10 energy orbs in one run", Metric.ORBS, 10),
        Mission(2, "Style Jumper", "Perform 8 jumps in one run", Metric.JUMPS, 8),
        Mission(3, "Smooth Slider", "Perform 6 slides in one run", Metric.SLIDES, 6),
        Mission(4, "Lane Master", "Switch lanes 20 times in one run", Metric.LANE_SWITCHES, 20),
        Mission(5, "Neon Pro", "Score 1200 points in one run", Metric.SCORE, 1200)
    )

    fun activeMission(): Mission = missions[saves.activeMissionIndex.coerceIn(0, missions.lastIndex)]

    fun progressFor(summary: RunSession.Summary, mission: Mission = activeMission()): Progress {
        val current = when (mission.metric) {
            Metric.DISTANCE -> summary.distanceMeters
            Metric.ORBS -> summary.orbsCollected
            Metric.SCORE -> summary.score
            Metric.JUMPS -> summary.jumpsPerformed
            Metric.SLIDES -> summary.slidesPerformed
            Metric.LANE_SWITCHES -> summary.laneSwitches
        }
        return Progress(current.coerceAtLeast(0), mission.target)
    }

    fun record(summary: RunSession.Summary): Outcome {
        val mission = activeMission()
        val progress = progressFor(summary, mission)
        var completedNow = false
        if (progress.isComplete && saves.missionsCompleted < missions.size) {
            completedNow = true
            saves.missionsCompleted += 1
            saves.activeMissionIndex = (saves.activeMissionIndex + 1).coerceAtMost(missions.lastIndex)
        }
        return Outcome(mission, progress, completedNow, saves.missionsCompleted)
    }
}
