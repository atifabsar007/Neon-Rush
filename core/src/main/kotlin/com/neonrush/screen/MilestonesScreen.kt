package com.neonrush.screen

import com.neonrush.NeonRushGame

/** Lifetime milestones are derived from saved player statistics. */
class MilestonesScreen(game: NeonRushGame) : BaseScreen(game) {
    override fun render(delta: Float) {
        prepare(delta)
        val s = game.saves
        val items = listOf(
            Triple("FIRST FLIGHT", "Complete your first run", s.totalRuns >= 1),
            Triple("TEN RUN CLUB", "Complete 10 runs", s.totalRuns >= 10),
            Triple("ORB APPRENTICE", "Collect 25 energy orbs", s.totalOrbs >= 25),
            Triple("ENERGY MASTER", "Collect 100 energy orbs", s.totalOrbs >= 100),
            Triple("CITY EXPLORER", "Travel 5,000 metres", s.totalDistance >= 5000),
            Triple("ELITE RUNNER", "Reach player level 5", s.playerLevel >= 5)
        )
        painter.beginShapes()
        painter.backdrop()
        painter.bigPanel(34f, 112f, 412f, 590f)
        painter.button(78f, 142f, 324f, 66f)
        painter.endShapes()
        painter.beginText()
        painter.text("MILESTONES", 240f, 658f, 2.5f, painter.cyan(), true)
        painter.text("YOUR JOURNEY SO FAR", 240f, 624f, 0.95f, painter.muted(), true)
        items.forEachIndexed { index, item ->
            val y = 552f - index * 62f
            painter.text(if (item.third) "★" else "◇", 86f, y, 1.6f, if (item.third) painter.lime() else painter.muted(), true)
            painter.text(item.first, 122f, y + 4f, 1.0f, if (item.third) painter.white() else painter.muted())
            painter.text(item.second, 122f, y - 20f, 0.78f, painter.muted())
            painter.text(if (item.third) "UNLOCKED" else "LOCKED", 374f, y - 2f, 0.68f, if (item.third) painter.lime() else painter.muted(), true)
        }
        painter.labelButton("BACK TO PROFILE", 78f, 142f, 324f, 66f)
        painter.endText()
        tapped()?.let { pos ->
            if (inRect(pos, 78f, 142f, 324f, 66f)) game.navigate(ProfileScreen(game))
        }
    }
}
