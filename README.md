# NEON RUSH

An original, offline, three-lane neon endless runner for Android, built with LibGDX and Kotlin. Source-only portfolio checkpoint v0.1.1.

**Current status:** source generated. This v0.1.1 ZIP repairs the reported `:core:compileKotlin` Java 21 / Kotlin 17 JVM-target mismatch by explicitly setting both Java and Kotlin to JVM 17 in the `core`, `lwjgl3`, and `android` modules. The user's v0.1.0 Gradle build reached this compilation error; the corrected project has not been rebuilt with Android SDK here because the Android SDK, Gradle distribution, and network dependency downloads are unavailable. Eight standalone game-rule smoke checks passed, and source compilation against locally generated mock LibGDX API stubs passed; neither verifies the Android build. No APK/AAB, release signing, Git commits, or GitHub pushes are claimed.

## Playable implementation included

- Main menu, settings, controls guide, game-over screen, retry
- Three-lane movement using swipes or desktop arrow keys
- Swipe up / W to jump low purple barriers; swipe down / S to slide under high pink barriers
- Solid magenta blocks must be avoided by changing lanes
- Collect cyan energy orbs for 75 points each; distance also contributes to score
- Increasing game speed and shorter obstacle intervals
- Pause/resume using the top-right pause button (or P/Escape on desktop)
- High score and sound/music preferences saved locally
- Original procedural neon graphics and original synthesized audio
- Lifecycle-safe pause and centralized screen/resource disposal
- JVM unit test sources for scoring, input rules, and collisions

## Modules

- `core`: LibGDX renderer, navigation, rules, persistence, audio
- `android`: Android launcher, manifest, packaging/native libraries
- `lwjgl3`: desktop runner for faster manual testing without an emulator
- `assets`: self-generated audio (no third-party assets)
- `docs`: architecture, test status, roadmap, attribution

## Open in Android Studio

1. Install Android Studio and its Android SDK Platform 36, SDK Build Tools, and a JDK 17 or 21.
2. Extract the ZIP to a fresh location; keep the folder named `NEON RUSH`.
3. Select **File > Open** and select the folder containing `settings.gradle.kts`.
4. Allow Gradle sync and Android SDK component installation. The first build requires internet to resolve dependencies.
5. Select the `android` run configuration and run on an emulator or a connected Android phone (USB debugging enabled).
6. If Studio needs a Gradle command, run `./gradlew :android:assembleDebug` from Terminal (macOS/Linux) or `gradlew.bat :android:assembleDebug` from Windows Command Prompt.
7. Once built successfully, the debug APK should be under `android/build/outputs/apk/debug/`. Check the actual output before installing.

**Gradle bootstrap note:** This ZIP contains verified-hash `gradlew`/`gradlew.bat` bootstrap scripts and `gradle-wrapper.properties`, but does **not** bundle the official `gradle-wrapper.jar` because it could not be retrieved in this environment. These scripts download the official Gradle 8.14.6 distribution after checking its SHA-256. After a successful Gradle setup, replace the bootstrap with the standard official wrapper via `gradle wrapper --gradle-version 8.14.6`, and commit the official wrapper scripts/JAR. Do not attempt to run Gradle without trusted connectivity. The bootstrap is a project convenience, not an official Gradle Wrapper implementation.

## Desktop test (optional)

```shell
./gradlew :lwjgl3:run
```

On macOS, the LWJGL3 run task applies `-XstartOnFirstThread` automatically.

## JVM tests

```shell
./gradlew :core:test
```

Unit test definitions are included; their execution via Gradle is not yet verified.

## Controls

| Input | Action |
| --- | --- |
| Swipe left / right | Switch lane |
| Swipe up | Jump (purple short obstacle) |
| Swipe down | Slide (magenta overhead gate) |
| Pause button | Pause and resume |
| Arrow keys and W/S (desktop) | Desktop controls |
| P or Escape (desktop) | Pause |

## Architecture

See `docs/ARCHITECTURE.md`. Game rules live in `RunSession` and `ScoreRules`, so they can be tested without Android.

## Assets, privacy and publishing

Graphics are generated procedurally using LibGDX shapes. Original audio and icon are included with the source. No login, network permission, tracking, analytics, ads, or internet permission is requested. No third-party art is used.

To prepare a release, first verify debug builds and device behavior, then use Android Studio's **Generate Signed App Bundle / APK** with a keystore that you store securely outside Git. `targetSdk=36` is configured but Play Console release requirements must be checked again at publication time. No signed AAB is included.

## Repository history

This ZIP has no fabricated Git history. It is deliberately not pushed to a remote repository, because the connected GitHub account was different from the supplied profile. Inspect your local project/repository before copying these files into any existing work. With your approval, the next change should be committed as an honest checkpoint after sync/tests pass.

## v0.1.1 Gradle/JVM target repair

The old v0.1.0 archive used JVM target 17 for Kotlin but implicitly used the Gradle JDK's target (21 on some Android Studio installations) for Java in `core` and `lwjgl3`. Kotlin's Gradle plugin rejects mismatched target versions. This release sets Java `sourceCompatibility` and `targetCompatibility` to Java 17 explicitly in both JVM modules and uses Kotlin `compilerOptions.jvmTarget = JVM_17` in all modules. It also fixes an invalid integer argument supplied to a `Float` drawing parameter. A JDK 21 Gradle daemon can still compile to Java 17 bytecode.

**To replace the broken copy safely:** Extract this v0.1.1 ZIP into a new directory, open the inner `NEON RUSH` folder in Android Studio, sync Gradle, then `Build > Rebuild Project`. Do not copy `.gradle` or `build` caches over from the old project. If you have edited the old project, back up and compare those changes before replacing files.
