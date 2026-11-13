package com.neonrush.screen

import com.badlogic.gdx.Gdx
import com.neonrush.NeonRushGame

/** Neon-styled entry screen with Firebase account actions and local guest fallback. */
class AuthScreen(game: NeonRushGame) : BaseScreen(game) {
    private var status = "SIGN IN OR PLAY AS GUEST"
    private var busy = false

    private fun ask(title: String, hint: String, initial: String = "", secure: Boolean = false, done: (String) -> Unit) {
        game.auth.promptInput(title, hint, initial, secure) { value ->
            if (value == null) {
                status = "ACTION CANCELLED"
                busy = false
            } else done(value)
        }
    }

    private fun finishAuth(result: Result<*>, success: String) {
        result.onSuccess {
            status = success
            game.auth.currentUser()?.displayName?.let { game.saves.playerName = it }
            game.auth.restoreProgress { restored ->
                Gdx.app.postRunnable {
                    restored.onSuccess { progress ->
                        if (progress != null) game.saves.applyCloudProgress(progress)
                        busy = false
                        game.navigate(MenuScreen(game))
                    }.onFailure {
                        busy = false
                        game.navigate(MenuScreen(game))
                    }
                }
            }
        }.onFailure {
            busy = false
            status = (it.message ?: "AUTHENTICATION FAILED").uppercase().take(58)
        }
    }

    private fun createAccount() {
        busy = true
        ask("Your display name", "Name") { name ->
            if (name.isBlank()) {
                busy = false
                status = "NAME IS REQUIRED"
            } else {
                game.saves.playerName = name.trim()
                ask("Email address", "Email") { email ->
                    ask("Create password", "At least 6 characters", secure = true) { password ->
                        game.auth.signUp(name.trim(), email.trim(), password) { result ->
                            Gdx.app.postRunnable { finishAuth(result, "ACCOUNT CREATED") }
                        }
                    }
                }
            }
        }
    }

    private fun login() {
        busy = true
        ask("Email address", "Email") { email ->
            ask("Password", "Password", secure = true) { password ->
                game.auth.login(email.trim(), password) { result ->
                    Gdx.app.postRunnable { finishAuth(result, "WELCOME BACK") }
                }
            }
        }
    }

    private fun continueAsGuest() {
        busy = true
        ask("Guest nickname", "Name", "NEON RUNNER") { name ->
            val nickname = name.ifBlank { "NEON RUNNER" }.trim()
            game.saves.playerName = nickname
            game.auth.continueAsGuest(nickname) { result ->
                Gdx.app.postRunnable { finishAuth(result, "GUEST MODE ENABLED") }
            }
        }
    }

    private fun resetPassword() {
        busy = true
        ask("Password reset", "Enter your account email") { email ->
            game.auth.resetPassword(email.trim()) { result ->
                Gdx.app.postRunnable {
                    busy = false
                    status = if (result.isSuccess) "RESET EMAIL SENT" else
                        (result.exceptionOrNull()?.message ?: "RESET FAILED").uppercase().take(58)
                }
            }
        }
    }

    override fun render(delta: Float) {
        prepare(delta)
        painter.beginShapes()
        painter.backdrop()
        painter.bigPanel(32f, 160f, 416f, 520f)
        painter.button(72f, 510f, 336f, 64f)
        painter.button(72f, 426f, 336f, 64f, painter.purple())
        painter.button(72f, 342f, 336f, 64f, painter.pink())
        painter.button(72f, 258f, 336f, 64f)
        painter.endShapes()

        painter.beginText()
        painter.text("NEON", 240f, 735f, 3.0f, painter.cyan(), true)
        painter.text("RUSH", 240f, 690f, 3.0f, painter.white(), true)
        painter.text("ENTER THE NEON CITY", 240f, 635f, 1.0f, painter.muted(), true)
        painter.labelButton("CREATE ACCOUNT", 72f, 510f, 336f, 64f, 1.3f)
        painter.labelButton("LOGIN", 72f, 426f, 336f, 64f, 1.4f)
        painter.labelButton("CONTINUE WITH GOOGLE", 72f, 342f, 336f, 64f, 1.12f)
        painter.labelButton("PLAY AS GUEST", 72f, 258f, 336f, 64f, 1.3f)
        painter.text("FORGOT PASSWORD?", 240f, 218f, 0.95f, painter.cyan(), true)
        painter.wrappedText(status, 240f, 190f, 360f, 0.62f, painter.muted(), 18f, 2)
        painter.text("GUEST PROGRESS CAN BE LINKED LATER", 240f, 125f, 0.72f, painter.muted(), true)
        painter.endText()

        if (busy) return
        tapped()?.let { pos ->
            when {
                inRect(pos, 72f, 510f, 336f, 64f) -> createAccount()
                inRect(pos, 72f, 426f, 336f, 64f) -> login()
                inRect(pos, 72f, 342f, 336f, 64f) -> {
                    busy = true
                    game.auth.googleSignIn { result ->
                        Gdx.app.postRunnable { finishAuth(result, "GOOGLE SIGN-IN SUCCESSFUL") }
                    }
                }
                inRect(pos, 72f, 258f, 336f, 64f) -> continueAsGuest()
                inRect(pos, 150f, 190f, 180f, 38f) -> resetPassword()
            }
        }
    }
}
