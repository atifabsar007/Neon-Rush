package com.neonrush.screen

import com.neonrush.NeonRushGame

class MenuScreen(game: NeonRushGame) : BaseScreen(game) {
    override fun render(delta: Float) {
        val mission = game.missions.activeMission()
        prepare(delta)
        painter.beginShapes()
        painter.backdrop()
        painter.logo()
        painter.bigPanel(42f, 430f, 396f, 104f)
        painter.button(72f, 330f, 336f, 76f)
        painter.button(72f, 244f, 160f, 62f, painter.purple())
        painter.button(248f, 244f, 160f, 62f, painter.pink())
        painter.button(72f, 164f, 160f, 58f)
        painter.button(248f, 164f, 160f, 58f, painter.purple())
        painter.endShapes()

        painter.beginText()
        painter.text("NEON", 276f, 639f, 3.7f, painter.cyan(), true)
        painter.text("RUSH", 246f, 585f, 3.8f, painter.white(), true)
        painter.text("WELCOME BACK", 240f, 508f, 0.75f, painter.cyan(), true)
        painter.fitText(game.saves.playerName, 240f, 478f, 340f, 1.55f, painter.white())
        painter.text("LV ${game.saves.playerLevel}     ${game.saves.coins} NEON COINS", 240f, 450f, 0.88f, painter.lime(), true)
        painter.labelButton("START RUN", 72f, 330f, 336f, 76f, 2.0f)
        painter.labelButton("PROFILE", 72f, 244f, 160f, 62f, 1.12f)
        painter.labelButton("SETTINGS", 248f, 244f, 160f, 62f, 1.12f)
        painter.labelButton("HOW TO PLAY", 72f, 164f, 160f, 58f, 1.0f)
        painter.labelButton("LEADERBOARD", 248f, 164f, 160f, 58f, 0.92f)
        painter.text("ACTIVE MISSION  •  ${mission.title.uppercase()}", 240f, 128f, 0.88f, painter.cyan(), true)
        painter.text(mission.description, 240f, 103f, 0.78f, painter.muted(), true)
        painter.text("BEST ${game.saves.bestScore}     RUNS ${game.saves.totalRuns}", 240f, 65f, 0.92f, painter.white(), true)
        painter.text(if(game.auth.currentUser()?.isGuest == true) "GUEST MODE" else "ONLINE PLAYER", 240f, 35f, 0.72f, painter.muted(), true)
        painter.endText()

        tapped()?.let { pos ->
            when {
                inRect(pos, 72f, 330f, 336f, 76f) -> game.navigate(PlayScreen(game))
                inRect(pos, 72f, 244f, 160f, 62f) -> game.navigate(ProfileScreen(game))
                inRect(pos, 248f, 244f, 160f, 62f) -> game.navigate(SettingsScreen(game))
                inRect(pos, 72f, 164f, 160f, 58f) -> game.navigate(HowToPlayScreen(game))
                inRect(pos, 248f, 164f, 160f, 58f) -> game.navigate(LeaderboardScreen(game))
            }
        }
    }
}
