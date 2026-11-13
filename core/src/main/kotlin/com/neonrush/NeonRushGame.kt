package com.neonrush

import com.badlogic.gdx.Game
import com.badlogic.gdx.Screen
import com.neonrush.render.NeonPainter
import com.neonrush.screen.MenuScreen
import com.neonrush.service.AudioManager
import com.neonrush.service.MissionManager
import com.neonrush.service.SaveRepository
import com.neonrush.service.AuthService
import com.neonrush.service.GuestAuthService
import com.neonrush.screen.AuthScreen

class NeonRushGame(private val authProvider: AuthService = GuestAuthService()) : Game() {
    val auth: AuthService get() = authProvider
    lateinit var painter: NeonPainter
        private set
    lateinit var saves: SaveRepository
        private set
    lateinit var audio: AudioManager
        private set
    lateinit var missions: MissionManager
        private set

    override fun create() {
        painter = NeonPainter()
        saves = SaveRepository()
        audio = AudioManager(saves)
        missions = MissionManager(saves)
        navigate(if (auth.currentUser() == null) AuthScreen(this) else MenuScreen(this))
        audio.syncMusic()
    }

    fun navigate(next: Screen) {
        val previous = screen
        setScreen(next)
        previous?.dispose()
    }

    override fun pause() {
        super.pause()
        audio.onPause()
    }

    override fun resume() {
        super.resume()
        audio.onResume()
    }

    override fun dispose() {
        screen?.dispose()
        super.dispose()
        audio.dispose()
        painter.dispose()
    }
}
