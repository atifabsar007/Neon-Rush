package com.neonrush.render

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.viewport.Viewport
import com.neonrush.logic.RunSession
import kotlin.math.cos
import kotlin.math.sin

/** Procedural neon art style with a photo-inspired cartoon runner. */
class NeonPainter : Disposable {
    private val shapes = ShapeRenderer()
    private val batch = SpriteBatch()
    private val font = BitmapFont()
    private val glyph = GlyphLayout()
    private val cyan = Color(0.15f, 0.96f, 1f, 1f)
    private val pink = Color(1f, 0.23f, 0.66f, 1f)
    private val purple = Color(0.60f, 0.45f, 1f, 1f)
    private val white = Color(0.95f, 0.98f, 1f, 1f)
    private val muted = Color(0.63f, 0.71f, 0.82f, 1f)
    private val lime = Color(0.55f, 1f, 0.62f, 1f)
    private val skin = Color(0.78f, 0.58f, 0.43f, 1f)
    private val hair = Color(0.08f, 0.08f, 0.10f, 1f)
    private val suit = Color(0.12f, 0.14f, 0.20f, 1f)
    private val shirt = Color(0.70f, 0.86f, 1f, 1f)
    private val avatarAccents = arrayOf(cyan, pink, purple, lime, Color(1f, 0.62f, 0.22f, 1f), Color(0.48f, 0.78f, 1f, 1f))
    private var worldTime = 0f

    fun prepare(viewport: Viewport, delta: Float) {
        worldTime += delta.coerceIn(0f, 0.05f)
        viewport.apply()
        shapes.projectionMatrix = viewport.camera.combined
        batch.projectionMatrix = viewport.camera.combined
        Gdx.gl.glClearColor(0.015f, 0.02f, 0.06f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    }

    fun beginShapes() {
        Gdx.gl.glEnable(GL20.GL_BLEND)
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
        shapes.begin(ShapeRenderer.ShapeType.Filled)
    }

    fun endShapes() { shapes.end(); Gdx.gl.glDisable(GL20.GL_BLEND) }
    fun beginText() { batch.begin() }
    fun endText() { batch.end() }

    /** Draws text without allowing it to spill beyond the logical game canvas. */
    fun text(value: String, x: Float, y: Float, scale: Float = 1.6f, color: Color = white, centered: Boolean = false) {
        font.color = color
        if (centered) {
            val edgeSafeWidth = minOf(392f, (x * 2f).coerceAtLeast(24f), ((480f - x) * 2f).coerceAtLeast(24f))
            val fitted = fittedScale(value, scale, edgeSafeWidth)
            font.data.setScale(fitted)
            glyph.setText(font, value)
            font.draw(batch, value, x - glyph.width / 2f, y)
        } else {
            val fitted = fittedScale(value, scale, (472f - x).coerceAtLeast(24f))
            font.data.setScale(fitted)
            font.draw(batch, value, x, y)
        }
    }

    /** Centered single-line text constrained to a specific card or control width. */
    fun fitText(value: String, centerX: Float, y: Float, maxWidth: Float,
                scale: Float = 1f, color: Color = white) {
        font.color = color
        font.data.setScale(fittedScale(value, scale, maxWidth))
        glyph.setText(font, value)
        font.draw(batch, value, centerX - glyph.width / 2f, y)
    }

    /** Wrap longer descriptions into readable centered lines inside their cards. */
    fun wrappedText(value: String, centerX: Float, topY: Float, maxWidth: Float,
                    scale: Float = 1f, color: Color = muted, lineGap: Float = 23f,
                    maxLines: Int = 3) {
        font.color = color
        font.data.setScale(scale)
        val words = value.trim().split(Regex("\\s+"))
        val lines = mutableListOf<String>()
        var line = ""
        for (word in words) {
            val candidate = if (line.isEmpty()) word else "$line $word"
            glyph.setText(font, candidate)
            if (glyph.width <= maxWidth || line.isEmpty()) line = candidate
            else { lines += line; line = word }
        }
        if (line.isNotEmpty()) lines += line
        lines.take(maxLines).forEachIndexed { index, part ->
            glyph.setText(font, part)
            font.draw(batch, part, centerX - glyph.width / 2f, topY - index * lineGap)
        }
    }

    private fun fittedScale(value: String, requested: Float, maxWidth: Float): Float {
        font.data.setScale(requested)
        glyph.setText(font, value)
        return if (glyph.width > maxWidth) requested * (maxWidth / glyph.width) else requested
    }

    fun backdrop(scroll: Float = worldTime * 45f) {
        shapes.setColor(0.03f, 0.045f, 0.11f, 1f)
        shapes.rect(0f, 0f, 480f, 800f)

        shapes.setColor(0.03f, 0.08f, 0.18f, 0.72f)
        shapes.rect(0f, 430f, 480f, 370f)
        shapes.setColor(0.10f, 0.16f, 0.28f, 0.25f)
        shapes.rect(0f, 410f, 480f, 80f)

        for (i in 0..21) {
            val x = 6f + i * 22f
            val height = 78f + ((i * 39) % 165)
            shapes.setColor(0.04f, 0.06f, 0.16f, 1f)
            shapes.rect(x, 420f, 18f, height)
            for (j in 0 until height.toInt() / 22) {
                val alpha = if ((i + j) % 4 == 0) 0.28f else 0.12f
                shapes.setColor(0.20f, 0.84f, 1f, alpha)
                shapes.rect(x + 4f, 430f + j * 18f, 3f, 5f)
            }
        }

        shapes.setColor(pink.r, pink.g, pink.b, 0.24f)
        shapes.circle(380f, 628f, 78f, 40)
        shapes.setColor(0.98f, 0.58f, 0.88f, 0.52f)
        shapes.circle(380f, 628f, 54f, 40)
        shapes.setColor(0.17f, 0.17f, 0.34f, 1f)
        for (i in 0..4) shapes.rect(328f, 610f + i * 11f, 104f, 5f)

        for (i in -1..16) {
            val y = 425f + ((i * 46f - (scroll * 0.35f % 46f)) + 46f) % 720f
            shapes.setColor(0.2f, 0.65f, 1f, 0.07f)
            shapes.rect(68f, y, 344f, 1.2f)
        }

        for (i in 0..26) {
            val x = ((i * 131) % 480).toFloat()
            val y = 505f + ((i * 199 + 77) % 275).toFloat()
            val a = 0.08f + 0.10f * (1f + sin(worldTime * 1.7f + i))
            shapes.setColor(0.78f, 0.9f, 1f, a)
            shapes.circle(x, y, 1.2f, 6)
        }
    }

    fun road() {
        // Layered midnight asphalt with a luminous perspective grid.
        shapes.setColor(0.012f, 0.018f, 0.055f, 1f)
        shapes.rect(54f, 0f, 372f, 438f)
        shapes.setColor(0.035f, 0.055f, 0.12f, 0.72f)
        shapes.rect(66f, 0f, 348f, 430f)
        shapes.setColor(cyan.r, cyan.g, cyan.b, 0.34f)
        shapes.rect(64f, 0f, 3f, 438f)
        shapes.rect(413f, 0f, 3f, 438f)
        shapes.setColor(purple.r, purple.g, purple.b, 0.30f)
        shapes.rect(181f, 0f, 2f, 430f)
        shapes.rect(297f, 0f, 2f, 430f)
        // Moving lane dashes and cross-road reflections create speed/depth.
        for (i in 0..13) {
            val y = (i * 39f + worldTime * 285f % 39f)
            val alpha = 0.07f + (i % 3) * 0.025f
            shapes.setColor(0.24f, 0.76f, 1f, alpha)
            shapes.rect(70f, y, 340f, 1.2f)
            shapes.setColor(0.54f, 0.72f, 1f, 0.20f)
            shapes.rect(118f, y + 7f, 3f, 13f)
            shapes.rect(238f, y + 7f, 3f, 13f)
            shapes.rect(358f, y + 7f, 3f, 13f)
        }
        // Side energy rails and animated light strips.
        val pulse = 0.25f + 0.18f * (1f + sin(worldTime * 3.2f))
        shapes.setColor(cyan.r, cyan.g, cyan.b, pulse)
        shapes.rect(55f, 0f, 2f, 438f)
        shapes.rect(423f, 0f, 2f, 438f)
        for (i in 0..7) {
            val y = (i * 66f + worldTime * 170f % 66f)
            shapes.setColor(pink.r, pink.g, pink.b, 0.18f)
            shapes.rect(48f, y, 7f, 18f)
            shapes.rect(425f, y, 7f, 18f)
        }
        // Subtle center-lane glow; gameplay objects remain high contrast.
        shapes.setColor(0.10f, 0.38f, 0.62f, 0.055f)
        shapes.rect(184f, 0f, 112f, 430f)
    }

    fun runnerAura(x: Float, y: Float, jumpHeight: Float) {
        val pulse = 0.10f + 0.07f * (1f + sin(worldTime * 5.2f))
        shapes.setColor(cyan.r, cyan.g, cyan.b, pulse)
        shapes.ellipse(x - 31f, y - 30f + jumpHeight * 0.12f, 62f, 14f)
        shapes.setColor(purple.r, purple.g, purple.b, 0.10f)
        shapes.circle(x, y + 24f + jumpHeight, 34f + 2f * sin(worldTime * 4f).toFloat(), 28)
        for (i in 0..5) {
            val angle = worldTime * 1.6f + i * 1.047f
            val px = x + cos(angle) * 27f
            val py = y + 25f + jumpHeight + sin(angle) * 31f
            shapes.setColor(if (i % 2 == 0) cyan else pink)
            shapes.circle(px, py, 1.8f, 8)
        }
    }

    fun playerRunner(laneX: Float, y: Float, jumpHeight: Float, isSliding: Boolean, runPhase: Float) {
        val bob = if (jumpHeight > 0f) jumpHeight else sin(worldTime * 10f).toFloat() * 2f
        val phase = sin(runPhase).toFloat()
        val phaseOpp = sin(runPhase + Math.PI).toFloat()
        val baseY = y - 3f + bob

        shapes.setColor(cyan.r, cyan.g, cyan.b, 0.12f)
        shapes.ellipse(laneX - 34f, y - 31f, 68f, 15f)

        if (isSliding) {
            shapes.setColor(suit)
            shapes.rect(laneX - 22f, baseY - 1f, 44f, 11f)
            shapes.setColor(shirt)
            shapes.rect(laneX - 3f, baseY + 2f, 14f, 4f)
            shapes.setColor(skin)
            shapes.circle(laneX + 20f, baseY + 9f, 7f, 18)
            shapes.setColor(hair)
            shapes.circle(laneX + 22f, baseY + 14f, 7.5f, 18)
            shapes.setColor(white)
            shapes.rectLine(laneX + 15f, baseY + 10f, laneX + 25f, baseY + 10f, 1.8f)
            shapes.setColor(suit)
            shapes.rectLine(laneX - 3f, baseY + 2f, laneX - 22f, baseY - 5f, 3f)
            shapes.rectLine(laneX + 4f, baseY + 2f, laneX - 16f, baseY - 9f, 3f)
            shapes.rectLine(laneX + 10f, baseY + 2f, laneX + 26f, baseY - 2f, 3f)
            shapes.rectLine(laneX + 15f, baseY + 2f, laneX + 29f, baseY + 7f, 3f)
            return
        }

        shapes.setColor(suit)
        shapes.rect(laneX - 14f, baseY + 8f, 28f, 31f)
        shapes.setColor(shirt)
        shapes.rect(laneX - 6f, baseY + 10f, 12f, 18f)
        shapes.setColor(pink)
        shapes.rect(laneX - 1f, baseY + 11f, 2f, 13f)

        shapes.setColor(skin)
        shapes.circle(laneX, baseY + 49f, 9.8f, 20)
        shapes.setColor(hair)
        shapes.circle(laneX - 5f, baseY + 56f, 5.4f, 16)
        shapes.circle(laneX, baseY + 58f, 5.6f, 16)
        shapes.circle(laneX + 5f, baseY + 56f, 5.2f, 16)
        shapes.circle(laneX - 8f, baseY + 52f, 4.4f, 16)
        shapes.circle(laneX + 8f, baseY + 52f, 4.2f, 16)
        shapes.setColor(white)
        shapes.rect(laneX - 7f, baseY + 49f, 6f, 2f)
        shapes.rect(laneX + 1f, baseY + 49f, 6f, 2f)
        shapes.rect(laneX - 1f, baseY + 49f, 2f, 1.2f)
        shapes.setColor(hair)
        shapes.rect(laneX - 3f, baseY + 44f, 6f, 1f)

        shapes.setColor(suit)
        shapes.rectLine(laneX - 12f, baseY + 33f, laneX - 22f, baseY + 20f + phase * 7f, 3.2f)
        shapes.rectLine(laneX + 12f, baseY + 33f, laneX + 21f, baseY + 19f + phaseOpp * 7f, 3.2f)
        shapes.rectLine(laneX - 5f, baseY + 8f, laneX - 14f, baseY - 18f + phaseOpp * 10f, 4f)
        shapes.rectLine(laneX + 5f, baseY + 8f, laneX + 14f, baseY - 18f + phase * 10f, 4f)
        shapes.rectLine(laneX - 14f, baseY - 18f + phaseOpp * 10f, laneX - 20f, baseY - 27f + phaseOpp * 12f, 4f)
        shapes.rectLine(laneX + 14f, baseY - 18f + phase * 10f, laneX + 20f, baseY - 27f + phase * 12f, 4f)
    }

    fun objectShape(kind: RunSession.Kind, lane: Int, y: Float) {
        val x = (120 + lane * 120).toFloat()
        when (kind) {
            RunSession.Kind.ORB -> {
                shapes.setColor(0.16f, 1f, 0.93f, 0.18f)
                shapes.circle(x, y, 26f, 20)
                shapes.setColor(cyan)
                shapes.circle(x, y, 14f, 20)
                shapes.setColor(0.05f, 0.22f, 0.33f, 1f)
                shapes.circle(x, y, 8.5f, 20)
                shapes.setColor(white)
                shapes.circle(x, y, 3f, 10)
            }
            RunSession.Kind.BLOCK -> {
                // Futuristic energy crate with luminous corner rails.
                shapes.setColor(0.04f, 0.07f, 0.15f, 1f)
                shapes.rect(x - 27f, y - 23f, 54f, 46f)
                shapes.setColor(0.08f, 0.18f, 0.29f, 1f)
                shapes.rect(x - 22f, y - 18f, 44f, 36f)
                shapes.setColor(cyan.r, cyan.g, cyan.b, 0.82f)
                outline(x - 22f, y - 18f, 44f, 36f, 2.4f)
                shapes.setColor(purple.r, purple.g, purple.b, 0.72f)
                shapes.rect(x - 3f, y - 15f, 6f, 30f)
                shapes.setColor(pink.r, pink.g, pink.b, 0.8f)
                shapes.rect(x - 16f, y - 2f, 32f, 4f)
                shapes.setColor(white.r, white.g, white.b, 0.72f)
                shapes.circle(x, y, 5f, 12)
            }
            RunSession.Kind.LOW_BARRIER -> {
                shapes.setColor(0.26f, 0.28f, 0.30f, 1f)
                shapes.rect(x - 30f, y - 18f, 6f, 36f)
                shapes.rect(x + 24f, y - 18f, 6f, 36f)
                shapes.setColor(lime)
                shapes.rect(x - 26f, y - 5f, 52f, 7f)
                shapes.setColor(white)
                shapes.rect(x - 26f, y + 4f, 52f, 4f)
                shapes.setColor(pink)
                shapes.rect(x - 20f, y - 12f, 12f, 4f)
                shapes.rect(x, y - 12f, 12f, 4f)
            }
            RunSession.Kind.HIGH_BARRIER -> {
                shapes.setColor(0.30f, 0.32f, 0.38f, 1f)
                shapes.rect(x - 28f, y - 20f, 5f, 40f)
                shapes.rect(x + 23f, y - 20f, 5f, 40f)
                shapes.setColor(0.82f, 0.82f, 0.86f, 1f)
                shapes.rect(x - 26f, y + 12f, 52f, 6f)
                shapes.setColor(pink)
                shapes.rect(x - 26f, y + 12f, 12f, 6f)
                shapes.rect(x - 2f, y + 12f, 12f, 6f)
                shapes.setColor(cyan.r, cyan.g, cyan.b, 0.10f)
                shapes.rect(x - 20f, y - 8f, 40f, 16f)
            }
        }
    }

    fun hudBar(progress: Float) {
        shapes.setColor(0f, 0f, 0.08f, 0.80f)
        shapes.rect(0f, 710f, 480f, 90f)
        shapes.setColor(0.06f, 0.08f, 0.15f, 1f)
        shapes.rect(20f, 712f, 260f, 6f)
        shapes.setColor(cyan)
        shapes.rect(20f, 712f, 260f * progress.coerceIn(0f, 1f), 6f)
        button(408f, 733f, 48f, 44f, purple)
        shapes.setColor(cyan)
        shapes.rect(427f, 745f, 4f, 19f)
        shapes.rect(436f, 745f, 4f, 19f)
    }

    fun missionChip() {
        shapes.setColor(0.04f, 0.08f, 0.14f, 0.95f)
        shapes.rect(34f, 677f, 412f, 32f)
        shapes.setColor(cyan.r, cyan.g, cyan.b, 0.70f)
        outline(34f, 677f, 412f, 32f, 1.6f)
    }

    fun progressBar(x: Float, y: Float, width: Float, height: Float, progress: Float, accent: Color = cyan) {
        shapes.setColor(0.015f, 0.025f, 0.07f, 1f)
        shapes.rect(x, y, width, height)
        shapes.setColor(accent.r, accent.g, accent.b, 0.95f)
        shapes.rect(x, y, width * progress.coerceIn(0f, 1f), height)
        shapes.setColor(white.r, white.g, white.b, 0.35f)
        shapes.rect(x, y + height - 2f, width * progress.coerceIn(0f, 1f), 1f)
    }

    fun controlPad(actionHint: Enum<*>? = null, showPulse: Boolean = false) {
        val pulse = if (showPulse) 0.08f + 0.06f * (1f + sin(worldTime * 4f)) else 0.08f
        controlButton(28f, 25f, 92f, 92f, cyan, pulse, false)
        controlButton(134f, 25f, 92f, 92f, cyan, pulse, false)
        controlButton(254f, 25f, 92f, 92f, lime, if (actionHint?.name == "JUMP") 0.20f else pulse, actionHint?.name == "JUMP")
        controlButton(360f, 25f, 92f, 92f, pink, if (actionHint?.name == "SLIDE") 0.20f else pulse, actionHint?.name == "SLIDE")

        shapes.setColor(white)
        shapes.rectLine(64f, 71f, 47f, 71f, 5f)
        shapes.rectLine(47f, 71f, 57f, 80f, 5f)
        shapes.rectLine(47f, 71f, 57f, 62f, 5f)

        shapes.rectLine(182f, 71f, 199f, 71f, 5f)
        shapes.rectLine(199f, 71f, 189f, 80f, 5f)
        shapes.rectLine(199f, 71f, 189f, 62f, 5f)

        shapes.rectLine(300f, 52f, 300f, 86f, 5f)
        shapes.rectLine(300f, 86f, 289f, 75f, 5f)
        shapes.rectLine(300f, 86f, 311f, 75f, 5f)

        shapes.rectLine(406f, 90f, 406f, 56f, 5f)
        shapes.rectLine(406f, 56f, 395f, 67f, 5f)
        shapes.rectLine(406f, 56f, 417f, 67f, 5f)
    }

    fun controlLabels() {
        text("LEFT", 74f, 41f, 0.78f, muted, true)
        text("RIGHT", 180f, 41f, 0.78f, muted, true)
        text("JUMP", 300f, 41f, 0.78f, white, true)
        text("SLIDE", 406f, 41f, 0.78f, white, true)
    }

    fun tutorialCard() {
        shapes.setColor(0.02f, 0.04f, 0.10f, 0.82f)
        shapes.rect(52f, 608f, 376f, 74f)
        shapes.setColor(cyan.r, cyan.g, cyan.b, 0.8f)
        outline(52f, 608f, 376f, 74f, 2f)
    }

    fun tutorialText() {
        text("Easier play: tap buttons or swipe the screen.", 240f, 655f, 1.0f, white, true)
        text("Green = jump   •   Pink = slide", 240f, 629f, 0.95f, muted, true)
    }

    fun overlay() {
        shapes.setColor(0.01f, 0.01f, 0.05f, 0.92f)
        shapes.rect(0f, 0f, 480f, 800f)
    }

    fun bigPanel(x: Float, y: Float, w: Float, h: Float) {
        shapes.setColor(0.05f, 0.07f, 0.16f, 0.97f)
        shapes.rect(x, y, w, h)
        shapes.setColor(cyan.r, cyan.g, cyan.b, 0.65f)
        outline(x, y, w, h, 2.5f)
    }

    fun avatarBadge(x: Float, y: Float, index: Int) {
        val accent = avatarAccents[index.coerceIn(0, avatarAccents.lastIndex)]
        shapes.setColor(accent.r, accent.g, accent.b, 0.18f)
        shapes.circle(x, y, 27f, 28)
        shapes.setColor(0.035f, 0.055f, 0.12f, 1f)
        shapes.circle(x, y, 22f, 28)
        shapes.setColor(accent)
        shapes.circle(x, y + 7f, 7f, 20)
        shapes.ellipse(x - 12f, y - 15f, 24f, 18f)
        shapes.setColor(white.r, white.g, white.b, 0.8f)
        shapes.circle(x - 2.5f, y + 8f, 1.4f, 8)
        shapes.circle(x + 2.5f, y + 8f, 1.4f, 8)
    }

    fun button(x: Float, y: Float, w: Float, h: Float, accent: Color = cyan) {
        shapes.setColor(accent.r, accent.g, accent.b, 0.12f)
        shapes.rect(x - 6f, y - 6f, w + 12f, h + 12f)
        shapes.setColor(0.06f, 0.08f, 0.17f, 1f)
        shapes.rect(x, y, w, h)
        shapes.setColor(accent.r, accent.g, accent.b, 0.95f)
        outline(x, y, w, h, 2f)
        shapes.rect(x + 4f, y + h - 5f, w - 8f, 2f)
    }

    private fun controlButton(x: Float, y: Float, w: Float, h: Float, accent: Color, glow: Float, emphasized: Boolean) {
        shapes.setColor(accent.r, accent.g, accent.b, glow)
        shapes.rect(x - 4f, y - 4f, w + 8f, h + 8f)
        shapes.setColor(0.05f, 0.07f, 0.14f, 0.95f)
        shapes.rect(x, y, w, h)
        shapes.setColor(accent)
        outline(x, y, w, h, if (emphasized) 3.2f else 2f)
    }

    fun labelButton(label: String, x: Float, y: Float, w: Float, h: Float, scale: Float = 1.65f) {
        font.color = white
        font.data.setScale(scale)
        glyph.setText(font, label)
        val safeWidth = (w - 28f).coerceAtLeast(40f)
        val fitted = if (glyph.width > safeWidth) scale * (safeWidth / glyph.width) else scale
        font.data.setScale(fitted)
        glyph.setText(font, label)
        font.draw(batch, label, x + (w - glyph.width) / 2f, y + h / 2f + glyph.height / 2f - 2f)
    }

    fun logo() {
        shapes.setColor(cyan.r, cyan.g, cyan.b, 0.08f)
        shapes.rect(54f, 534f, 372f, 164f)
        shapes.setColor(cyan)
        shapes.rect(74f, 679f, 332f, 3f)
        shapes.setColor(purple)
        shapes.rect(74f, 548f, 332f, 3f)
        shapes.setColor(pink)
        shapes.rectLine(102f, 578f, 102f, 650f, 8f)
        shapes.rectLine(102f, 650f, 150f, 578f, 8f)
        shapes.rectLine(150f, 578f, 150f, 650f, 8f)
    }

    fun orbParticles(x: Float, y: Float, remaining: Float) {
        val progress = 1f - remaining / 0.40f
        for (i in 0..11) {
            val angle = (i / 12f) * 6.28318f
            val radius = progress * 75f
            shapes.setColor(0.15f, 0.96f, 1f, (1f - progress) * 0.9f)
            shapes.circle(x + cos(angle) * radius, y + sin(angle) * radius, (3f - progress * 2f).coerceAtLeast(1f), 7)
        }
    }

    fun cyan(): Color = cyan
    fun pink(): Color = pink
    fun purple(): Color = purple
    fun muted(): Color = muted
    fun white(): Color = white
    fun lime(): Color = lime

    private fun outline(x: Float, y: Float, w: Float, h: Float, t: Float) {
        shapes.rect(x, y, w, t)
        shapes.rect(x, y + h - t, w, t)
        shapes.rect(x, y, t, h)
        shapes.rect(x + w - t, y, t, h)
    }

    override fun dispose() {
        font.dispose()
        shapes.dispose()
        batch.dispose()
    }
}
