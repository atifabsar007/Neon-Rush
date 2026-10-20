# Architecture

`NeonRushGame` extends LibGDX `Game`. It creates one shared `NeonPainter`, one `SaveRepository`, and one `AudioManager`. Its `navigate()` changes the active screen and disposes the replaced screen. `dispose()` disposes the final screen and shared resources.

`BaseScreen` owns a `FitViewport(480, 800)`, maps real touch coordinates into game coordinates, and provides consistent UI hit testing. Screens are lightweight and own no GPU resources.

`MenuScreen`, `SettingsScreen`, `HowToPlayScreen`, and `GameOverScreen` implement specific interactions. `PlayScreen` translates touch swipes or keyboard input into `RunSession` commands; it advances the simulation only while unpaused and forwards audio events to `AudioManager`.

`RunSession` is an engine-independent gameplay state machine that owns score-relevant state: lane, elapsed time, distance, speed, obstacles, collectibles, jump/slide countdown, collision and end-of-run state. Each obstacle is resolved once when crossing the player's game line, preventing repeated hit detection. All motion is delta-time based, with a maximum accepted simulation step of 50 ms.

`ScoreRules` is a stateless formula holder. Both logic classes compile without LibGDX so unit tests do not require Android or a GPU.

`NeonPainter` uses a shared `ShapeRenderer`, `SpriteBatch`, and `BitmapFont`; it draws original shapes for road, obstacles, player, UI, and energy orbs. It never allocates textures in the render loop and disposes assets at exit. The whole 480x800 world is scaled by `FitViewport` for other displays.

`SaveRepository` uses LibGDX Preferences for local high score and sound/music toggles, flushed when changed. `AudioManager` loads original WAVs once, gates sound by settings, and pauses music with app lifecycle.

`AndroidLauncher` owns the Android application lifecycle; `DesktopLauncher` enables quick tests without an emulator. Kotlin is the primary language in this checkpoint. A C++ module is deliberately absent because JNI would add complexity without benefiting a simple 2D runner and wasn't confirmed as a coursework requirement.
