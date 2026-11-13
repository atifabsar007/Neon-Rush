package com.neonrush.service

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.utils.Disposable

/** Creates each audio resource once and obeys platform lifecycle callbacks. */
class AudioManager(private val saves: SaveRepository) : Disposable {
    private val pickup: Sound = Gdx.audio.newSound(Gdx.files.internal("pickup.wav"))
    private val move: Sound = Gdx.audio.newSound(Gdx.files.internal("move.wav"))
    private val crash: Sound = Gdx.audio.newSound(Gdx.files.internal("crash.wav"))
    private val music: Music = Gdx.audio.newMusic(Gdx.files.internal("ambient.wav")).apply {
        isLooping = true
        volume = 0.20f
    }
    private var appActive = true

    fun pickup() = play(pickup, 0.65f)
    fun move() = play(move, 0.28f)
    fun crash() = play(crash, 0.65f)

    private fun play(sound: Sound, volume: Float) {
        if (saves.soundEnabled && appActive) sound.play(volume)
    }

    fun syncMusic() {
        if (saves.musicEnabled && appActive) {
            if (!music.isPlaying) music.play()
        } else {
            if (music.isPlaying) music.pause()
        }
    }

    fun onPause() { appActive = false; music.pause() }
    fun onResume() { appActive = true; syncMusic() }

    override fun dispose() {
        music.dispose()
        pickup.dispose()
        move.dispose()
        crash.dispose()
    }
}
