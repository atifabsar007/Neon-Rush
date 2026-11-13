package com.neonrush.screen

import com.neonrush.NeonRushGame

class SettingsScreen(game: NeonRushGame) : BaseScreen(game) {
    override fun render(delta: Float) {
        prepare(delta)
        painter.beginShapes()
        painter.backdrop()
        painter.bigPanel(44f, 148f, 392f, 486f)
        painter.button(78f, 442f, 324f, 70f, painter.purple())
        painter.button(78f, 342f, 324f, 70f, painter.lime())
        painter.button(78f, 208f, 324f, 64f)
        painter.endShapes()

        painter.beginText()
        painter.text("SETTINGS", 240f, 592f, 2.8f, painter.cyan(), true)
        painter.text("Customize your run experience", 240f, 559f, 1.0f, painter.muted(), true)
        painter.text("SOUND EFFECTS", 240f, 490f, 1.25f, painter.white(), true)
        painter.text(if (game.saves.soundEnabled) "ON" else "OFF", 240f, 462f, 1.55f, painter.white(), true)
        painter.text("BACKGROUND MUSIC", 240f, 390f, 1.25f, painter.white(), true)
        painter.text(if (game.saves.musicEnabled) "ON" else "OFF", 240f, 362f, 1.55f, painter.white(), true)
        painter.labelButton("BACK TO MENU", 78f, 208f, 324f, 64f)
        painter.fitText("Tap each card to toggle. Settings save instantly.", 240f, 170f, 340f, 0.82f, painter.muted())
        painter.endText()

        tapped()?.let { pos ->
            when {
                inRect(pos, 78f, 442f, 324f, 70f) -> game.saves.soundEnabled = !game.saves.soundEnabled
                inRect(pos, 78f, 342f, 324f, 70f) -> {
                    game.saves.musicEnabled = !game.saves.musicEnabled
                    game.audio.syncMusic()
                }
                inRect(pos, 78f, 208f, 324f, 64f) -> game.navigate(MenuScreen(game))
            }
        }
    }
}
