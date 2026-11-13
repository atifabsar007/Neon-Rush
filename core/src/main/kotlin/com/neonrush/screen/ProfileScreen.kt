package com.neonrush.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.neonrush.NeonRushGame

/** Local player identity and lifetime progression. Online authentication is intentionally not implied. */
class ProfileScreen(game: NeonRushGame) : BaseScreen(game) {
    private var notice = "YOUR PROGRESS IS SAVED ON THIS DEVICE"

    override fun render(delta: Float) {
        prepare(delta)
        val saves = game.saves
        painter.beginShapes()
        painter.backdrop()
        painter.bigPanel(34f, 112f, 412f, 590f)
        painter.bigPanel(58f, 478f, 364f, 154f)
        painter.progressBar(92f, 492f, 296f, 8f, saves.xpIntoLevel / 500f, painter.lime())
        painter.avatarBadge(110f, 590f, saves.avatarIndex)
        painter.button(78f, 270f, 324f, 62f, painter.purple())
        painter.button(78f, 190f, 324f, 62f, painter.pink())
        painter.button(78f, 115f, 324f, 60f)
        painter.button(78f, 42f, 158f, 54f, painter.purple())
        painter.button(244f, 42f, 158f, 54f, painter.pink())
        painter.endShapes()

        painter.beginText()
        painter.text("PLAYER PROFILE", 240f, 660f, 2.35f, painter.cyan(), true)
        painter.fitText(saves.playerName, 240f, 586f, 300f, 2.0f, painter.white())
        painter.text("EDIT AVATAR", 110f, 555f, 0.62f, painter.cyan(), true)
        painter.text("LEVEL ${saves.playerLevel}", 240f, 548f, 1.35f, painter.lime(), true)
        painter.text("${saves.xpIntoLevel} / 500 XP TO NEXT LEVEL", 240f, 516f, 0.86f, painter.muted(), true)
        painter.text("RUNS", 116f, 452f, 0.95f, painter.muted(), true)
        painter.text("${saves.totalRuns}", 116f, 423f, 1.55f, painter.white(), true)
        painter.text("BEST SCORE", 240f, 452f, 0.95f, painter.muted(), true)
        painter.text("${saves.bestScore}", 240f, 423f, 1.55f, painter.white(), true)
        painter.text("NEON COINS", 364f, 452f, 0.95f, painter.muted(), true)
        painter.text("${saves.coins}", 364f, 423f, 1.55f, painter.cyan(), true)
        painter.text("DISTANCE  ${saves.totalDistance} m", 240f, 390f, 1.0f, painter.white(), true)
        painter.text("ENERGY ORBS  ${saves.totalOrbs}", 240f, 354f, 1.0f, painter.white(), true)
        painter.labelButton("EDIT PLAYER NAME", 78f, 270f, 324f, 62f)
        painter.labelButton("VIEW MILESTONES", 78f, 190f, 324f, 62f)
        painter.labelButton("BACK TO MENU", 78f, 115f, 324f, 60f)
        painter.labelButton(if(game.auth.currentUser()?.isGuest == true) "LINK ACCOUNT" else "ACCOUNT", 78f, 42f, 158f, 54f, 0.95f)
        painter.labelButton("LOG OUT", 244f, 42f, 158f, 54f, 1.0f)
        painter.text(if(game.auth.currentUser()?.isGuest == true) "GUEST PROFILE • LOCAL SAVE" else "SIGNED IN • CLOUD SAVE ENABLED", 240f, 88f, 0.66f, painter.muted(), true)
        painter.text(notice, 240f, 22f, 0.65f, painter.muted(), true)
        painter.endText()

        tapped()?.let { pos ->
            when {
                inRect(pos, 78f, 568f, 64f, 52f) -> { saves.avatarIndex = (saves.avatarIndex + 1) % 6; notice = "AVATAR UPDATED" }
                inRect(pos, 78f, 270f, 324f, 62f) -> Gdx.input.getTextInput(object : Input.TextInputListener {
                    override fun input(text: String) {
                        if (text.isNotBlank()) {
                            saves.playerName = text
                            game.auth.updateProfile(text) { result -> Gdx.app.postRunnable { notice = if(result.isSuccess) "PROFILE UPDATED SUCCESSFULLY" else "LOCAL NAME SAVED • CLOUD UPDATE FAILED" } }
                        } else notice = "NAME WAS NOT CHANGED"
                    }
                    override fun canceled() { notice = "EDIT CANCELLED" }
                }, "Player name", saves.playerName, "Enter a nickname")
                inRect(pos, 78f, 190f, 324f, 62f) -> game.navigate(MilestonesScreen(game))
                inRect(pos, 78f, 115f, 324f, 60f) -> game.navigate(MenuScreen(game))
                inRect(pos, 78f, 42f, 158f, 54f) -> {
                    if(game.auth.currentUser()?.isGuest == true) game.navigate(AuthScreen(game))
                    else notice = "YOUR ACCOUNT IS LINKED"
                }
                inRect(pos, 244f, 42f, 158f, 54f) -> game.auth.logout { result -> Gdx.app.postRunnable { result.onSuccess { game.navigate(AuthScreen(game)) }.onFailure { notice=it.message ?: "LOGOUT FAILED" } } }
            }
        }
    }
}
