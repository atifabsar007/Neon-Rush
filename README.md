# NEON RUSH 0.6.0 — Modern account UX and neon gameplay

NEON RUSH is an offline-first, three-lane neon endless runner built with Kotlin and LibGDX.

## What's included in 0.6.0\n\n- Dedicated full-page Create Account, Login and Password Reset screens; no chained native input popups.\n- Username + email registration and login with email or username. Username lookup is handled by a Firebase callable function and does not publish account email addresses.\n- Modernized in-game road lighting, animated energy rails, player aura and futuristic obstacle art.\n- Better text fitting in profile, dashboard, leaderboard and run-summary panels.\n- Existing three-lane movement, jump, slide, character proportions and swipe controls retained.\n\n## What's included in 0.5.1

- Account entry flow with email/password registration and login.
- Password-reset flow and Google Sign-In integration (requires Firebase OAuth setup).
- Guest sign-in and guest-to-account credential linking.
- Player profile, progression, XP, coins, missions and run summaries.
- Firebase-backed player data, progress synchronization and leaderboard integration (requires Firebase configuration and Firestore security rules).
- UI text fitting and wrapping improvements for the LibGDX logical canvas.
- Frame-rate-independent lane movement and more responsive swipe handling.
- Android CI workflow for unit tests and debug APK assembly.

## Firebase configuration

Online authentication and cloud features require a Firebase project. Follow [FIREBASE_SETUP.md](FIREBASE_SETUP.md), add your app in Firebase using application ID `com.neonrush.game`, enable Email/Password and Anonymous providers, configure Google Sign-In and the Web OAuth client, enable Cloud Firestore, and place `google-services.json` in the `android/` module.

Without Firebase configuration, Google Sign-In and cloud services are unavailable. Guest/local play remains the intended fallback. Never commit service-account private keys, signing keystores, passwords, or other secrets.

## Open in Android Studio

1. Open the folder containing `settings.gradle.kts`.
2. Use JDK 17 and install Android SDK Platform 36 / Build Tools as prompted.
3. Add Firebase configuration if testing online features.
4. Sync Gradle and run the `android` configuration on an emulator or device.

The project currently contains Gradle bootstrap scripts but no standard `gradle-wrapper.jar`. The GitHub Actions workflow installs Gradle 8.14.6 directly. For local builds, use an installed Gradle 8.14.6 or regenerate a standard wrapper from a trusted Gradle installation.

## Verification status

The source was committed to the `upgrade/neon-rush-v0.5.1` branch. The Android build and on-device behavior must still be verified by GitHub Actions and Android Studio. No successful APK build, signed release, or production Firebase deployment is claimed until those checks pass.

## Project modules

- `core`: LibGDX game loop, screens, rendering, game rules and local progression.
- `android`: Android launcher, Firebase integration and packaging.
- `lwjgl3`: optional desktop runner.
- `assets`: original sound assets.
- `docs`: architecture, testing, roadmap and UI fix notes.

## Controls

| Input | Action |
|---|---|
| Swipe left / right | Change lane |
| Swipe up | Jump |
| Swipe down | Slide |
| On-screen controls | Move, jump, slide and pause |
| Arrow keys / A-D | Change lane (desktop) |
| W / Up / Space | Jump (desktop) |
| S / Down | Slide (desktop) |
| P / Escape | Pause (desktop) |

## Privacy and release

When Firebase is configured, player profile and progression data are synchronized to the configured Firebase project. Review and deploy restrictive Firestore security rules before public release. Keep release signing keys outside Git and use Android Studio's signed App Bundle workflow for distribution.
\n\n## NEON RUSH 0.6.0 setup\n\nSee [FIREBASE_SETUP.md](FIREBASE_SETUP.md) for deploying the secure username-login Cloud Function and Firestore rules. Configure the Functions secret `FIREBASE_WEB_API_KEY` before deploying username login. The Cloud Functions deployment requires Firebase Blaze billing.\n