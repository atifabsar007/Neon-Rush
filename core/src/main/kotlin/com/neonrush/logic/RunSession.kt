package com.neonrush.logic

import kotlin.random.Random

/** Gameplay rules are independent of rendering, audio, Android, and input APIs. */
class RunSession(private val random: Random = Random.Default) {
    enum class Kind { BLOCK, LOW_BARRIER, HIGH_BARRIER, ORB }
    data class TrackObject(val id: Long, val lane: Int, val kind: Kind, var y: Float)
    data class StepResult(val collected: Int = 0, val crashed: Boolean = false)
    data class Summary(
        val score: Int,
        val orbsCollected: Int,
        val distanceMeters: Int,
        val jumpsPerformed: Int,
        val slidesPerformed: Int,
        val laneSwitches: Int
    )

    companion object {
        const val PLAYER_Y = 192f
        const val SPAWN_Y = 870f
        const val LANE_COUNT = 3
    }

    val objects = mutableListOf<TrackObject>()
    var lane = 1
        private set
    var elapsedSeconds = 0f
        private set
    var distanceMeters = 0f
        private set
    var orbsCollected = 0
        private set
    var isDead = false
        private set
    var jumpRemaining = 0f
        private set
    var slideRemaining = 0f
        private set
    var jumpsPerformed = 0
        private set
    var slidesPerformed = 0
        private set
    var laneSwitches = 0
        private set

    val speed: Float get() = ScoreRules.speed(elapsedSeconds)
    val score: Int get() = ScoreRules.score(distanceMeters, orbsCollected)
    private var obstacleCountdown = 1.55f
    private var orbCountdown = 0.68f
    private var nextId = 0L

    fun moveLeft() {
        if (isDead) return
        val next = (lane - 1).coerceAtLeast(0)
        if (next != lane) laneSwitches++
        lane = next
    }

    fun moveRight() {
        if (isDead) return
        val next = (lane + 1).coerceAtMost(2)
        if (next != lane) laneSwitches++
        lane = next
    }

    fun jump(): Boolean {
        if (isDead || jumpRemaining > 0f || slideRemaining > 0f) return false
        jumpRemaining = 0.78f
        jumpsPerformed++
        return true
    }

    fun slide(): Boolean {
        if (isDead || slideRemaining > 0f || jumpRemaining > 0f) return false
        slideRemaining = 0.72f
        slidesPerformed++
        return true
    }

    /** Caller supplies a bounded frame delta; large stalls are clamped for fairness. */
    fun step(deltaSeconds: Float): StepResult {
        if (isDead || deltaSeconds <= 0f) return StepResult()
        val dt = deltaSeconds.coerceAtMost(0.05f)
        elapsedSeconds += dt
        distanceMeters += speed * dt * 0.060f
        jumpRemaining = (jumpRemaining - dt).coerceAtLeast(0f)
        slideRemaining = (slideRemaining - dt).coerceAtLeast(0f)
        obstacleCountdown -= dt
        orbCountdown -= dt
        if (obstacleCountdown <= 0f) {
            spawnObstacle()
            obstacleCountdown += ScoreRules.obstacleInterval(elapsedSeconds)
        }
        if (orbCountdown <= 0f) {
            spawnOrb()
            orbCountdown += 0.92f + random.nextFloat() * 0.34f
        }
        var newlyCollected = 0
        val iterator = objects.iterator()
        while (iterator.hasNext()) {
            val item = iterator.next()
            item.y -= speed * dt
            if (item.y <= PLAYER_Y) {
                iterator.remove()
                if (item.lane != lane) continue
                when (item.kind) {
                    Kind.ORB -> { orbsCollected++; newlyCollected++ }
                    Kind.LOW_BARRIER -> if (jumpRemaining <= 0f) isDead = true
                    Kind.HIGH_BARRIER -> if (slideRemaining <= 0f) isDead = true
                    Kind.BLOCK -> isDead = true
                }
                if (isDead) return StepResult(newlyCollected, crashed = true)
            }
        }
        return StepResult(newlyCollected)
    }

    fun summary(): Summary = Summary(
        score = score,
        orbsCollected = orbsCollected,
        distanceMeters = distanceMeters.toInt(),
        jumpsPerformed = jumpsPerformed,
        slidesPerformed = slidesPerformed,
        laneSwitches = laneSwitches
    )

    private fun spawnObstacle() {
        val roll = random.nextInt(100)
        val kind = when {
            roll < 36 -> Kind.BLOCK
            roll < 68 -> Kind.LOW_BARRIER
            else -> Kind.HIGH_BARRIER
        }
        objects += TrackObject(nextId++, random.nextInt(LANE_COUNT), kind, SPAWN_Y)
    }

    private fun spawnOrb() {
        objects += TrackObject(nextId++, random.nextInt(LANE_COUNT), Kind.ORB, SPAWN_Y + 35f)
    }
}
