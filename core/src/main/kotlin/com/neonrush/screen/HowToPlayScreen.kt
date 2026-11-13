package com.neonrush.screen

import com.neonrush.NeonRushGame

class HowToPlayScreen(game: NeonRushGame) : BaseScreen(game) {
    override fun render(delta: Float) {
        prepare(delta)
        painter.beginShapes()
        painter.backdrop()
        painter.bigPanel(38f, 110f, 404f, 590f)
        painter.button(80f, 130f, 320f, 66f)
        painter.endShapes()
        painter.beginText()
        painter.text("HOW TO PLAY", 240f, 656f, 2.5f, painter.cyan(), true)
        painter.text("MOVE", 240f, 587f, 1.55f, painter.white(), true)
        painter.wrappedText("Tap LEFT / RIGHT buttons or swipe sideways", 240f, 559f, 340f, 0.96f, painter.muted(), 22f, 2)
        painter.text("JUMP", 240f, 502f, 1.55f, painter.lime(), true)
        painter.wrappedText("Use the green button or swipe up", 240f, 474f, 340f, 0.96f, painter.muted(), 22f, 2)
        painter.text("SLIDE", 240f, 418f, 1.55f, painter.pink(), true)
        painter.wrappedText("Use the pink button or swipe down", 240f, 390f, 340f, 0.96f, painter.muted(), 22f, 2)
        painter.text("OBJECTS", 240f, 334f, 1.38f, painter.white(), true)
        painter.wrappedText("Crates crash you • Jump road barriers • Slide under beams", 240f, 306f, 340f, 0.84f, painter.muted(), 21f, 2)
        painter.text("MISSIONS", 240f, 254f, 1.38f, painter.cyan(), true)
        painter.text("Each run tracks one active mission. Finish it to unlock the next one.", 240f, 225f, 0.84f, painter.muted(), true)
        painter.text("Desktop: A/D or arrows, Space/W, S, P", 240f, 176f, 0.88f, painter.muted(), true)
        painter.labelButton("GOT IT", 80f, 130f, 320f, 66f)
        painter.endText()
        tapped()?.let { pos -> if (inRect(pos, 80f, 130f, 320f, 66f)) game.navigate(MenuScreen(game)) }
    }
}
