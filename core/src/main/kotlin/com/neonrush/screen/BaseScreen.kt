package com.neonrush.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.viewport.FitViewport
import com.neonrush.NeonRushGame

abstract class BaseScreen(protected val game: NeonRushGame) : ScreenAdapter() {
    protected val viewport = FitViewport(480f, 800f)
    protected val painter get() = game.painter
    protected fun prepare(delta: Float) { painter.prepare(viewport, delta) }
    protected fun tapped(): Vector3? = if (Gdx.input.justTouched()) {
        viewport.unproject(Vector3(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f))
    } else null
    protected fun inRect(pos: Vector3, x: Float, y: Float, w: Float, h: Float): Boolean =
        pos.x >= x && pos.x <= x + w && pos.y >= y && pos.y <= y + h
    override fun resize(width: Int, height: Int) { viewport.update(width, height, true) }
    override fun show() { viewport.update(Gdx.graphics.width, Gdx.graphics.height, true) }
}
