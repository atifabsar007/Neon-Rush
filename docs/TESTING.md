# NEON RUSH 0.4.0 Test Plan

## Verification completed in the preparation environment

| Check | Result |
|---|---|
| Source archive extracted and project structure inspected | PASS |
| Pure Kotlin `RunSession` and `ScoreRules` compilation with local `kotlinc` | PASS |
| Gradle `:core:test` | NOT RUN — Gradle distribution download blocked by unavailable network DNS |
| Android `:android:assembleDebug` | NOT RUN — Android SDK / Gradle dependency setup not available here |
| Physical-device installation and gameplay | NOT RUN |

## Required Android Studio verification

1. Open the folder containing `settings.gradle.kts`.
2. Sync Gradle using JDK 17 or 21 and Android SDK 36.
3. Run `:core:test`.
4. Run `:android:assembleDebug`.
5. Install the generated debug APK on a physical device.
6. Verify menu navigation, nickname text input, profile persistence after restart, XP/coin rewards, milestones, sound settings, pause/resume and gameplay collisions.
7. Test at least one narrow and one tall screen ratio.

Do not mark the Android build or device tests as passed until the commands have actually completed successfully.

## Authentication and cloud tests (v0.5.0)

1. Configure Firebase using `FIREBASE_SETUP.md` before testing online features.
2. Test email/password account creation, sign out, login, and password reset.
3. Start as guest, complete a run, then use Profile → Link Account → Create Account. Confirm the guest UID is linked and its progress remains.
4. Test Google Sign-In on a device with Google Play services and configured SHA-1/SHA-256 fingerprints.
5. Complete a run while signed in, force-close/reopen, and confirm cloud progress is restored.
6. Open Leaderboard and confirm only public display name, score and level appear.
7. Confirm Firestore `players/{uid}` documents cannot be read by another signed-in user; public `leaderboard/{uid}` documents contain no email address.

Without `google-services.json`, the Android build intentionally falls back to local guest mode. Online authentication, password reset, cloud saves and leaderboard will not work until Firebase is configured.
