package com.neonrush.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.viewport.FitViewport
import com.neonrush.NeonRushGame

/** Dedicated, full-page account experience. No native popup input dialogs. */
class AuthScreen(private val game: NeonRushGame) : ScreenAdapter() {
    private enum class Page { WELCOME, REGISTER, LOGIN, RESET }
    private var page = Page.WELCOME
    private var busy = false
    private var notice = ""
    private val viewport = FitViewport(480f, 800f)
    private val stage = Stage(viewport)
    private val skin = Skin()
    private val font = BitmapFont()
    private val root = Table()
    private val username = TextField("", fieldStyle())
    private val email = TextField("", fieldStyle())
    private val password = TextField("", fieldStyle())
    private val identifier = TextField("", fieldStyle())

    init {
        skin.add("font", font)
        skin.add("field", fieldStyle())
        skin.add("button", buttonStyle(Color(0.05f, 0.88f, 0.96f, 1f)))
        skin.add("secondary", buttonStyle(Color(0.42f, 0.31f, 0.88f, 1f)))
        skin.add("pink", buttonStyle(Color(0.91f, 0.20f, 0.57f, 1f)))
        stage.addActor(root)
        rebuild()
    }

    private fun drawable(color: Color): TextureRegionDrawable {
        val pixmap = Pixmap(2, 2, Pixmap.Format.RGBA8888)
        pixmap.setColor(color); pixmap.fill()
        val texture = Texture(pixmap); pixmap.dispose()
        return TextureRegionDrawable(TextureRegion(texture))
    }
    private fun fieldStyle(): TextField.TextFieldStyle = TextField.TextFieldStyle().apply {
        font = this@AuthScreen.font
        fontColor = Color(0.94f, 0.97f, 1f, 1f)
        cursor = drawable(Color(0.10f, 0.92f, 1f, 1f))
        selection = drawable(Color(0.12f, 0.45f, 0.58f, 0.9f))
        background = drawable(Color(0.035f, 0.055f, 0.12f, 1f))
    }
    private fun buttonStyle(accent: Color): TextButton.TextButtonStyle = TextButton.TextButtonStyle().apply {
        font = this@AuthScreen.font
        fontColor = Color.WHITE
        up = drawable(Color(0.035f, 0.075f, 0.14f, 1f))
        down = drawable(accent.cpy().mul(0.65f))
        over = drawable(Color(accent.r, accent.g, accent.b, 0.22f))
        checked = down
    }
    private fun label(text: String, scale: Float = 1f, color: Color = Color.WHITE) =
        Label(text, Label.LabelStyle(font, color)).apply { setFontScale(scale); setWrap(true) }

    private fun action(text: String, style: String, height: Float = 54f, callback: () -> Unit) {
        val button = TextButton(text, skin, style)
        button.addListener(object : ChangeListener() {
            override fun changed(event: ChangeListener.ChangeEvent?, actor: com.badlogic.gdx.scenes.scene2d.Actor?) {
                if (!busy) callback()
            }
        })
        root.add(button).width(330f).height(height).padTop(8f).padBottom(4f).row()
    }
    private fun field(target: TextField, hint: String, secret: Boolean = false) {
        target.messageText = hint
        target.setPasswordMode(secret)
        target.setPasswordCharacter('•')
        root.add(target).width(330f).height(52f).padTop(7f).padBottom(4f).row()
    }
    private fun rebuild() {
        root.clear()
        root.setFillParent(true)
        root.pad(24f, 34f, 20f, 34f)
        root.add(label("NEON RUSH", 2f, Color(0.12f, 0.92f, 1f, 1f))).center().padBottom(4f).row()
        root.add(label("RUN THE CITY. OWN THE NIGHT.", 0.72f, Color(0.58f, 0.69f, 0.82f, 1f))).center().padBottom(20f).row()
        when (page) {
            Page.WELCOME -> {
                root.add(label("READY TO RUN?", 1.55f)).center().padBottom(8f).row()
                root.add(label("Create an account to keep your progress, or jump straight into a guest run.", 0.88f, Color(0.68f, 0.76f, 0.88f, 1f))).width(330f).center().padBottom(18f).row()
                action("CREATE ACCOUNT", "button") { page = Page.REGISTER; notice = ""; rebuild() }
                action("LOG IN", "secondary") { page = Page.LOGIN; notice = ""; rebuild() }
                action("CONTINUE WITH GOOGLE", "secondary") { google() }
                action("PLAY AS GUEST", "pink") { guest() }
            }
            Page.REGISTER -> {
                root.add(label("CREATE ACCOUNT", 1.45f)).center().padBottom(6f).row()
                root.add(label("Choose a username and enter your email.", 0.82f, Color(0.66f, 0.75f, 0.87f, 1f))).center().padBottom(10f).row()
                field(username, "Username (3–18 characters)")
                field(email, "Email address")
                field(password, "Password (at least 6 characters)", true)
                action("CREATE MY ACCOUNT", "button") { register() }
                action("ALREADY REGISTERED? LOG IN", "secondary", 46f) { page = Page.LOGIN; notice = ""; rebuild() }
                action("BACK", "pink", 42f) { page = Page.WELCOME; notice = ""; rebuild() }
            }
            Page.LOGIN -> {
                root.add(label("WELCOME BACK", 1.5f)).center().padBottom(6f).row()
                root.add(label("Sign in with your email or username.", 0.82f, Color(0.66f, 0.75f, 0.87f, 1f))).center().padBottom(10f).row()
                field(identifier, "Email address or username")
                field(password, "Password", true)
                action("LOG IN", "button") { login() }
                action("FORGOT PASSWORD?", "secondary", 44f) { page = Page.RESET; notice = ""; rebuild() }
                action("CREATE NEW ACCOUNT", "pink", 44f) { page = Page.REGISTER; notice = ""; rebuild() }
                action("BACK", "secondary", 40f) { page = Page.WELCOME; notice = ""; rebuild() }
            }
            Page.RESET -> {
                root.add(label("RESET PASSWORD", 1.4f)).center().padBottom(8f).row()
                root.add(label("We'll email you a secure password-reset link.", 0.84f, Color(0.66f, 0.75f, 0.87f, 1f))).width(330f).center().padBottom(12f).row()
                field(email, "Your account email")
                action("SEND RESET LINK", "button") {
                    busy = true
                    game.auth.resetPassword(email.text.trim()) { result -> Gdx.app.postRunnable { busy = false; notice = if (result.isSuccess) "RESET LINK SENT — CHECK YOUR EMAIL" else (result.exceptionOrNull()?.message ?: "RESET FAILED"); rebuild() } }
                }
                action("BACK TO LOGIN", "secondary") { page = Page.LOGIN; notice = ""; rebuild() }
            }
        }
        if (notice.isNotBlank()) root.add(label(notice, 0.68f, Color(0.30f, 0.92f, 0.76f, 1f))).width(330f).center().padTop(10f).row()
        root.add(label("SECURE ACCOUNT  •  GUEST MODE AVAILABLE", 0.60f, Color(0.42f, 0.52f, 0.66f, 1f))).center().padTop(14f).row()
    }
    private fun register() {
        val name = username.text.trim()
        val mail = email.text.trim()
        val pass = password.text
        if (!name.matches(Regex("[A-Za-z0-9_]{3,18}"))) { notice = "USERNAME MUST BE 3–18 LETTERS, NUMBERS OR _"; rebuild(); return }
        if (!mail.contains("@") || !mail.contains(".")) { notice = "ENTER A VALID EMAIL ADDRESS"; rebuild(); return }
        if (pass.length < 6) { notice = "PASSWORD MUST HAVE AT LEAST 6 CHARACTERS"; rebuild(); return }
        busy = true
        game.auth.signUp(name, mail, pass) { result -> Gdx.app.postRunnable { complete(result) } }
    }
    private fun login() {
        if (identifier.text.isBlank() || password.text.isBlank()) { notice = "ENTER YOUR EMAIL/USERNAME AND PASSWORD"; rebuild(); return }
        busy = true
        game.auth.login(identifier.text.trim(), password.text) { result -> Gdx.app.postRunnable { complete(result) } }
    }
    private fun guest() { busy = true; game.auth.continueAsGuest("NEON RUNNER") { result -> Gdx.app.postRunnable { complete(result) } } }
    private fun google() { busy = true; game.auth.googleSignIn { result -> Gdx.app.postRunnable { complete(result) } } }
    private fun complete(result: Result<*>) {
        busy = false
        result.onSuccess {
            game.auth.currentUser()?.displayName?.let { game.saves.playerName = it }
            game.auth.restoreProgress { restored ->
                Gdx.app.postRunnable {
                    restored.onSuccess { progress -> if (progress != null) game.saves.applyCloudProgress(progress) }
                    game.navigate(MenuScreen(game))
                }
            }
        }.onFailure { notice = (it.message ?: "PLEASE TRY AGAIN").uppercase().take(100); rebuild() }
    }
    override fun show() { viewport.update(Gdx.graphics.width, Gdx.graphics.height, true); Gdx.input.inputProcessor = stage }
    override fun resize(width: Int, height: Int) { viewport.update(width, height, true) }
    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0.012f, 0.018f, 0.052f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        stage.act(delta.coerceAtMost(0.05f)); stage.draw()
    }
    override fun hide() { if (Gdx.input.inputProcessor === stage) Gdx.input.inputProcessor = null }
    override fun dispose() { stage.dispose(); skin.dispose(); font.dispose() }
}
