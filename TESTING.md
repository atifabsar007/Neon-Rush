# Testing record (October 19, 2026)

Do not treat planned tests as passed.

| Check | Actual result |
| --- | --- |
| Compile standalone Kotlin game-rule files with local `kotlinc` 1.9.0 | PASS |
| Standalone Kotlin gameplay smoke checks (6 assertions) | PASS: run as an executable JAR |
| Static source and XML structure audit | PASS: checked paths, audio headers, manifest parsing, shell syntax |
| `./gradlew :core:test` | NOT RUN: Gradle dependency cache not available |
| `./gradlew :android:assembleDebug` | NOT RUN: Android SDK and Gradle distribution not available |
| Android device/emulator gameplay | NOT RUN |
| Audio lifecycle under interruptions | NOT RUN |
| Portrait layout on phones/tablets | NOT RUN |
| Signed AAB | NOT RUN |

Unit test source cases include score calculation, difficulty progression, lane bounds, mutually exclusive actions, orb collection, collision freeze and jump-avoid behavior.

## Manual acceptance checklist

1. Play from main menu and return after game over.
2. Swipe every direction repeatedly; ensure lane bounds and action exclusivity.
3. Pass below overhead gates with a slide and above low obstacles with a jump.
4. Leave to Android home and return; game must remain paused without duplicate simulation.
5. Toggle both audio settings, exit and reopen app; preferences must remain.
6. Earn a high score, force stop and reopen; score must persist.
7. Resize desktop window, verify buttons map correctly; on Android rotate is locked to portrait.
8. Perform ten consecutive retries and inspect Logcat for crashes/leaks.
9. Check debug APK can install and start on real hardware.
10. Validate release AAB after creating a private signing key.

## v0.1.1 JVM target repair (October 19, 2026)

The v0.1.0 Android Studio build log reported `:core:compileKotlin` failed because `compileJava` targeted JVM 21 while `compileKotlin` targeted JVM 17. The `core` and `lwjgl3` Gradle scripts have been amended to explicitly target JVM 17 for both Java and Kotlin; Android's Kotlin target has been moved to the typed compiler options DSL, also JVM 17. Static source inspection confirms targets are aligned in all three modules. **The Gradle/Android rebuild remains NOT RUN in this environment**, which has no Gradle distribution/Android SDK and cannot resolve the Gradle download host.

## v0.1.1 additional checks (October 19, 2026)

- PASS: eight independent game-rule smoke assertions compiled with local `kotlinc -jvm-target 17`, executed as a runnable JAR. Cases: scoring, speed cap, obstacle interval floor, lane clamping, jump/slide exclusivity, orb collection, crash/score freeze, and jumping a low barrier.
- PASS: Kotlin sources in `core`, `android`, and `lwjgl3` syntax/type-checked with a temporary set of locally generated **mock LibGDX and Android API stubs**. This catches Kotlin source-level problems such as the incorrect `Int` parameter in `NeonPainter.rect` that has now been repaired, but does **not** substitute for real LibGDX/Android dependencies and does **not** verify a working Android build.
- PASS: XML parsing for Android manifests/resources, RIFF/WAV asset headers, static audit confirming JVM 17 for both Java and Kotlin in `core` and `lwjgl3` plus Kotlin and Java in `android`.
- NOT RUN: Real `gradlew :core:test`, Android `assembleDebug`, APK installation, and GPU rendering.
