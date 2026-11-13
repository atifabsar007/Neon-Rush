package com.neonrush.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.math.Vector3
import com.neonrush.NeonRushGame
import com.neonrush.logic.RunSession
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.sin
import kotlin.math.exp

class PlayScreen(game: NeonRushGame) : BaseScreen(game) {
    private val session = RunSession()
    private val input = PlayInput()
    private var paused = false
    private var laneX = 240f
    private var orbFlashRemaining = 0f
    private var tutorialTime = 9f
    private var actionFlash = ActionHint.NONE

    private enum class ActionHint { NONE, JUMP, SLIDE }

    override fun show() {
        super.show()
        Gdx.input.inputProcessor = input
    }

    override fun hide() {
        if (Gdx.input.inputProcessor === input) Gdx.input.inputProcessor = null
    }

    override fun pause() { paused = true }

    override fun render(delta: Float) {
        prepare(delta)
        val frameDt = delta.coerceIn(0f, 0.05f)
        tutorialTime = (tutorialTime - frameDt).coerceAtLeast(0f)
        readKeyboard()

        if (!paused) {
            val update = session.step(frameDt)
            if (update.collected > 0) {
                game.audio.pickup()
                orbFlashRemaining = 0.40f
            }
            if (update.crashed) {
                game.audio.crash()
                val summary = session.summary()
                val missionOutcome = game.missions.record(summary)
                val coinReward = game.saves.recordRun(summary)
                val isNewRecord = game.saves.recordScore(summary.score)
                game.auth.syncProgress(game.saves.playerName, game.saves.bestScore, game.saves.playerLevel, game.saves.xp, game.saves.coins, game.saves.totalRuns, game.saves.totalDistance, game.saves.totalOrbs)
                game.navigate(GameOverScreen(game, summary, isNewRecord, missionOutcome, coinReward))
                return
            }
        }

        val laneTarget = (120 + session.lane * 120).toFloat()
        val laneEase = (1f - exp((-18f * frameDt).toDouble()).toFloat()).coerceIn(0f, 1f)
        laneX += (laneTarget - laneX) * laneEase
        orbFlashRemaining = (orbFlashRemaining - frameDt).coerceAtLeast(0f)
        val mission = game.missions.activeMission()
        val missionProgress = game.missions.progressFor(session.summary(), mission)

        painter.beginShapes()
        painter.backdrop(session.distanceMeters * 1.8f)
        painter.road()
        session.objects.forEach { item ->
            painter.objectShape(item.kind, item.lane, item.y)
            if (item.kind == RunSession.Kind.LOW_BARRIER && item.y in 170f..405f) actionFlash = ActionHint.JUMP
            if (item.kind == RunSession.Kind.HIGH_BARRIER && item.y in 170f..405f) actionFlash = ActionHint.SLIDE
        }
        val jumpProgress = 1f - session.jumpRemaining / 0.78f
        val jumpHeight = if (session.jumpRemaining > 0f) sin(jumpProgress * PI).toFloat() * 72f else 0f
        val runPhase = session.distanceMeters * 0.10f
        painter.runnerAura(laneX, RunSession.PLAYER_Y, jumpHeight)
        painter.playerRunner(laneX, RunSession.PLAYER_Y, jumpHeight, session.slideRemaining > 0f, runPhase)
        if (orbFlashRemaining > 0f) painter.orbParticles(laneX, RunSession.PLAYER_Y + 32f, orbFlashRemaining)
        painter.hudBar(speedProgress())
        painter.controlPad(actionFlash, tutorialTime > 0f)
        painter.missionChip()
        if (tutorialTime > 0f) painter.tutorialCard()
        if (paused) {
            painter.overlay()
            painter.bigPanel(44f, 252f, 392f, 326f)
            painter.button(78f, 403f, 324f, 67f)
            painter.button(78f, 310f, 324f, 67f, painter.pink())
        }
        painter.endShapes()

        painter.beginText()
        painter.text("${session.score}", 24f, 776f, 2.15f, painter.white())
        painter.text("ORB ${session.orbsCollected}", 24f, 736f, 1.20f, painter.cyan())
        painter.text("DIST ${session.distanceMeters.toInt()}m", 178f, 736f, 1.0f, painter.muted())
        painter.text("SPD ${session.speed.toInt()}", 325f, 736f, 1.0f, painter.muted())
        painter.text(mission.title.uppercase(), 240f, 703f, 0.90f, painter.white(), true)
        painter.text("${mission.description}  •  ${missionProgress.label()}", 240f, 684f, 0.72f, painter.muted(), true)
        if (orbFlashRemaining > 0f) painter.text("+75", laneX, 325f, 1.6f, painter.cyan(), true)
        painter.text("PAUSE", 432f, 759f, 0.68f, painter.muted(), true)
        painter.controlLabels()
        if (tutorialTime > 0f) painter.tutorialText()
        when (actionFlash) {
            ActionHint.JUMP -> painter.text("JUMP!", 240f, 160f, 1.9f, painter.lime(), true)
            ActionHint.SLIDE -> painter.text("SLIDE!", 240f, 160f, 1.9f, painter.pink(), true)
            ActionHint.NONE -> Unit
        }
        if (paused) {
            painter.text("PAUSED", 240f, 552f, 3.0f, painter.white(), true)
            painter.text("Take a breath. Resume when ready.", 240f, 512f, 1.08f, painter.muted(), true)
            painter.labelButton("RESUME", 78f, 403f, 324f, 67f)
            painter.labelButton("MAIN MENU", 78f, 310f, 324f, 67f)
        }
        painter.endText()
        actionFlash = ActionHint.NONE
    }

    private fun speedProgress(): Float {
        val min = 250f
        val max = 590f
        return ((session.speed - min) / (max - min)).coerceIn(0f, 1f)
    }

    private fun readKeyboard() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.P) || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) paused = !paused
        if (paused) return
        when {
            Gdx.input.isKeyJustPressed(Input.Keys.LEFT) || Gdx.input.isKeyJustPressed(Input.Keys.A) -> moveLeft()
            Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) || Gdx.input.isKeyJustPressed(Input.Keys.D) -> moveRight()
            Gdx.input.isKeyJustPressed(Input.Keys.UP) || Gdx.input.isKeyJustPressed(Input.Keys.W) || Gdx.input.isKeyJustPressed(Input.Keys.SPACE) -> jump()
            Gdx.input.isKeyJustPressed(Input.Keys.DOWN) || Gdx.input.isKeyJustPressed(Input.Keys.S) -> slide()
        }
    }

    private fun moveLeft() {
        val before = session.lane
        session.moveLeft()
        if (session.lane != before) game.audio.move()
    }
    private fun moveRight() {
        val before = session.lane
        session.moveRight()
        if (session.lane != before) game.audio.move()
    }
    private fun jump() { if (session.jump()) game.audio.move() }
    private fun slide() { if (session.slide()) game.audio.move() }

    inner class PlayInput : InputAdapter() {
        private var downX = 0f
        private var downY = 0f
        private var activePointer = -1

        override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
            if (activePointer != -1) return false
            val world = viewport.unproject(Vector3(screenX.toFloat(), screenY.toFloat(), 0f))
            downX = world.x
            downY = world.y
            activePointer = pointer

            if (paused) return true
            when {
                inRect(world, 408f, 733f, 48f, 44f) -> { paused = true; return true }
                inRect(world, 28f, 25f, 92f, 92f) -> { moveLeft(); return true }
                inRect(world, 134f, 25f, 92f, 92f) -> { moveRight(); return true }
                inRect(world, 254f, 25f, 92f, 92f) -> { jump(); return true }
                inRect(world, 360f, 25f, 92f, 92f) -> { slide(); return true }
            }
            return true
        }

        override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
            if (pointer != activePointer) return false
            activePointer = -1
            val world = viewport.unproject(Vector3(screenX.toFloat(), screenY.toFloat(), 0f))
            if (paused) {
                if (inRect(world, 78f, 403f, 324f, 67f)) paused = false
                else if (inRect(world, 78f, 310f, 324f, 67f)) game.navigate(MenuScreen(game))
                return true
            }
            val dx = world.x - downX
            val dy = world.y - downY
            if (abs(dx) > abs(dy) && abs(dx) > 38f) {
                if (dx > 0) moveRight() else moveLeft()
            } else if (abs(dy) > 38f) {
                if (dy > 0) jump() else slide()
            }
            return true
        }
    }
}
