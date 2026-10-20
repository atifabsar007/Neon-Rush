## v0.3.0 - Photo Inspired Runner + Mission Update

- Replaced the generic runner with a cartoonized runner inspired by the provided photos.
- Updated obstacles to feel more like crates, road barriers, and overhead beams.
- Rebalanced speed and obstacle timing so runs last longer and feel less punishing early on.
- Added an offline mission system with progress tracking and mission completion on the game-over screen.
- Improved gameplay HUD to show mission progress during a run.
- Kept swipe controls and added easier touch buttons for left, right, jump, and slide.

## v0.2.0 - Recharged UI and control update

- Reworked overall neon UI with cleaner panels, HUD, and menus.
- Replaced the block avatar with a stylized running character.
- Added visible gameplay control buttons for left, right, jump, and slide while keeping swipe support.
- Added jump/slide color coding and contextual action hints for easier obstacle reading.
- Updated gameplay tutorial messaging and improved readability.
- Replaced ambient music and sound effects with smoother original procedural audio.

# Changelog

## [0.1.1] - 2026-10-19

- Fix Kotlin Gradle plugin JVM target validation: explicitly align Java 17 and Kotlin 17 in `core` and `lwjgl3`.
- Align Android Kotlin compiler options to JVM 17 and document how to import the corrected source archive.
- Fix a Kotlin `Float` argument mismatch in the procedural renderer (`NeonPainter`).
- Re-run eight standalone game-rule smoke checks and Kotlin source compilation using local LibGDX API stubs.
- This is a source-level repair; full Gradle/Android rebuild is pending on an Android SDK-equipped machine.


## [0.1.0] - 2026-10-19 (source checkpoint; not a verified Android release)

- Drafted Android Studio multi-module LibGDX Kotlin project.
- Added menu, controls guide, settings, gameplay, pause, and game-over screens.
- Added an original neon procedural visual style and synthesized audio files.
- Implemented pure Kotlin three-lane movement, jump/slide, spawned obstacles, collisions, orbs, score, and speed progression.
- Added local preferences and JVM unit test sources.
- Added documentation and a verified-hash Gradle download bootstrap.
- Did not create git commits or an APK; Android build and emulator checks remain pending.
