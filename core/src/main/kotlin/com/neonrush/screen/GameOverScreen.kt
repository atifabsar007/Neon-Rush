package com.neonrush.screen

import com.neonrush.NeonRushGame
import com.neonrush.logic.RunSession
import com.neonrush.service.MissionManager

class GameOverScreen(
    game: NeonRushGame,
    private val summary: RunSession.Summary,
    private val newRecord: Boolean,
    private val missionOutcome: MissionManager.Outcome,
    private val coinReward: Int
) : BaseScreen(game) {
    override fun render(delta: Float) {
        prepare(delta)
        painter.beginShapes()
        painter.backdrop()
        painter.overlay()
        painter.bigPanel(28f, 104f, 424f, 536f)
        painter.button(78f, 184f, 324f, 68f)
        painter.button(78f, 108f, 324f, 58f, painter.purple())
        painter.endShapes()
        painter.beginText()
        painter.text("RUN COMPLETE", 240f, 602f, 2.45f, painter.pink(), true)
        painter.text("FINAL SCORE", 240f, 556f, 1.1f, painter.muted(), true)
        painter.text("${summary.score}", 240f, 514f, 3.25f, painter.white(), true)
        painter.text("Best ${game.saves.bestScore}  •  Distance ${summary.distanceMeters}m", 240f, 474f, 1.0f, painter.cyan(), true)
        painter.text("Orbs ${summary.orbsCollected}  •  Jumps ${summary.jumpsPerformed}  •  Slides ${summary.slidesPerformed}", 240f, 438f, 0.85f, painter.white(), true)
        painter.text("+${coinReward} NEON COINS     •     +${summary.score} XP", 240f, 400f, 1.05f, painter.lime(), true)
        painter.text("Mission: ${missionOutcome.mission.title}", 240f, 366f, 1.0f, painter.lime(), true)
        painter.wrappedText(missionOutcome.mission.description, 240f, 340f, 360f, 0.78f, painter.muted(), 20f, 2)
        val status = if (missionOutcome.completedNow) "MISSION COMPLETE!" else "Mission progress: ${missionOutcome.progress.label()}"
        painter.text(status, 240f, 310f, 0.95f, if (missionOutcome.completedNow) painter.lime() else painter.white(), true)
        if (newRecord) painter.text("NEW PERSONAL RECORD!", 240f, 282f, 1.05f, painter.cyan(), true)
        painter.labelButton("PLAY AGAIN", 78f, 184f, 324f, 68f, 1.8f)
        painter.labelButton("MAIN MENU", 78f, 108f, 324f, 58f)
        painter.endText()
        tapped()?.let { pos ->
            when {
                inRect(pos, 78f, 184f, 324f, 68f) -> game.navigate(PlayScreen(game))
                inRect(pos, 78f, 108f, 324f, 58f) -> game.navigate(MenuScreen(game))
            }
        }
    }
}
