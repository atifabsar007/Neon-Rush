package com.neonrush.desktop

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.neonrush.NeonRushGame

fun main() {
    val configuration = Lwjgl3ApplicationConfiguration().apply {
        setTitle("NEON RUSH")
        setWindowedMode(480, 800)
        useVsync(true)
        setForegroundFPS(60)
    }
    Lwjgl3Application(NeonRushGame(), configuration)
}
