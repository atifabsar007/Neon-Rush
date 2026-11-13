package com.neonrush.logic

/** Pure, platform-independent rules: safe to test on the JVM. */
object ScoreRules {
    const val POINTS_PER_ORB = 75

    fun score(distanceMeters: Float, collectedOrbs: Int): Int =
        distanceMeters.toInt().coerceAtLeast(0) + collectedOrbs.coerceAtLeast(0) * POINTS_PER_ORB

    fun speed(elapsedSeconds: Float): Float =
        (250f + elapsedSeconds.coerceAtLeast(0f) * 7.2f).coerceAtMost(590f)

    fun obstacleInterval(elapsedSeconds: Float): Float =
        (1.55f - elapsedSeconds.coerceAtLeast(0f) * 0.007f).coerceAtLeast(0.72f)
}
