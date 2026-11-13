# NEON RUSH 0.4.0 — Android Studio Source

NEON RUSH is an offline-first, three-lane neon endless runner built with Kotlin and LibGDX.

## What's new in 0.4.0

- Refreshed main menu with player identity, level, coins, best score and active mission.
- Local player profile with editable nickname.
- Persistent progression: XP, levels, Neon Coins, total runs, lifetime distance and total orbs.
- Run-end rewards calculated from distance and collected orbs.
- Expanded run summary with XP and coin rewards.
- Cleaner menu hierarchy and more consistent visual layout.
- XP progress indicator on the profile screen.
- Lifetime milestone tracker with unlock states.
- Improved lane-edge feedback: no movement sound when the player is already at the edge.
- Android app version updated to 0.4.0.

## Current feature set

- Three-lane endless runner with lane switching, jump and slide.
- Blocks, low barriers, high barriers and energy orbs.
- Increasing speed and obstacle density.
- Swipe controls, on-screen controls and desktop keyboard controls.
- Pause/resume, sound effects and background music preferences.
- Active missions and mission progression.
- Personal best score and local player progression.
- Procedural neon visuals and original synthesized audio.
- Local-only profile and save data. No account credentials, analytics, ads or tracking are collected.

## Important: account support

The profile in this build is a **local player profile**, not an online account. Real email/password registration, password reset, cloud save synchronization and global leaderboards require a backend (for example Firebase Authentication + Firestore, or a custom API). No fake online sign-in is presented in this release. The app does not request Internet permission.

## Open in Android Studio

1. Extract this ZIP.
2. Open the `NEON RUSH` folder — the folder containing `settings.gradle.kts` — in Android Studio.
3. Use JDK 17 or 21 and install Android SDK Platform 36 / Build Tools if Android Studio requests them.
4. Allow Gradle to download dependencies on a trusted internet connection.
5. Select the `android` run configuration and run on an emulator or connected Android device.
6. Or run `./gradlew :android:assembleDebug` from the project root.

The debug APK, if the build succeeds, is normally written to `android/build/outputs/apk/debug/`.

## Tests and verification

The project includes JVM tests for score rules, lane bounds, jump/slide exclusivity, orb collection and collision handling. Run `./gradlew :core:test` after Gradle has been initialized.

**Build verification status for this archive:** source changes were inspected, but a Gradle build could not be run in the preparation environment because the Gradle distribution host was unreachable. Android SDK packaging and on-device gameplay therefore still need to be verified in Android Studio. Do not treat this ZIP as a tested APK or signed release.

## Modules

- `core`: game loop, screens, rendering, game rules and local progression.
- `android`: Android launcher and packaging.
- `lwjgl3`: optional desktop runner.
- `assets`: original sound assets.
- `docs`: architecture, testing, roadmap and asset attribution.

## Controls

| Input | Action |
|---|---|
| Swipe left / right | Change lane |
| Swipe up | Jump |
| Swipe down | Slide |
| On-screen buttons | Move, jump, slide, pause |
| Arrow keys / A-D | Change lane (desktop) |
| W / Up / Space | Jump (desktop) |
| S / Down | Slide (desktop) |
| P / Escape | Pause (desktop) |

## Privacy

Profile name and progression are stored locally using Android preferences. This release does not transmit player information to a server.


## Online accounts (v0.5.0)

Firebase setup is required for email/password accounts, Google Sign-In, password reset, cloud progress and the global leaderboard. Follow [FIREBASE_SETUP.md](FIREBASE_SETUP.md) and place your Firebase `google-services.json` in `android/`. Without Firebase configuration, the app still runs in local guest mode. Guest progress can be linked to a new email/password or Google account from the Create Account / Google flow.


## Version 0.5.1 UI and build hotfix
- Fixes the missing `default_web_client_id` compile-time resource reference.
- Google Sign-In now checks for the generated OAuth resource at runtime and displays setup guidance if Firebase OAuth is not configured.
- Text is automatically fitted to the logical canvas; button labels are fitted to their button bounds. Longer help and mission descriptions wrap within cards.
- Lane movement uses frame-rate-independent easing; swipe detection is slightly more responsive.
- For Google Sign-In, keep `google-services.json` in the `android/` module and configure the Web OAuth client plus SHA-1/SHA-256 in Firebase.
