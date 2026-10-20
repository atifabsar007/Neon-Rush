# NEON RUSH Test Plan and Current Results

**Report date:** 2026-10-20. This report is grounded in the inspected v0.3.0 source package and the public GitHub repository. The Android build and CI have **not been executed** by this preparation step. A local syntax compilation using LibGDX-style stubs was reported in earlier development, but that is not a real Android Gradle build.

## Current verification matrix

| Test | Expected result | Status |
| --- | --- | --- |
| Core `ScoreRulesTest` | Score formula, speed cap, obstacle interval validated | **JUnit source exists; Gradle run not verified** |
| Core `RunSessionTest` | Lane clamping; jumping/sliding; orb collection; collisions | **JUnit source exists; Gradle run not verified** |
| Mission completion/persistence | Six ordered missions advance appropriately | **Requires tests + phone verification** |
| `gradle :core:test` | All JVM unit tests pass | **NOT RUN here** |
| `gradle :android:assembleDebug` | Debug APK created without Gradle errors | **NOT RUN here** |
| Install and launch on Android phone | App starts, navigates, plays and exits without crash | **NOT VERIFIED here** |
| Menu/settings persistence | Sound/music and scores persist across relaunch | **NOT VERIFIED here** |
| Pause/resume during a run | No duplicate updates/music or lost state | **NOT VERIFIED here** |
| Tall-screen UI | Buttons/HUD legible and reachable | **NOT VERIFIED; FitViewport may letterbox** |
| Release tag / APK match | Release APK was built from tagged commit | **NOT VERIFIED** |

## Steps for a real test run

1. Record source commit SHA: `git rev-parse HEAD`.
2. Build using Gradle 8.14.6 with JDK 17 and Android SDK 36.
3. Save console output of `gradle --no-daemon :core:test :android:assembleDebug`.
4. Inspect `android/build/outputs/apk/debug/` and identify the generated APK.
5. Install on a physical phone, start from menu, play/retry, pause/background/resume, change preferences and restart.
6. Check every obstacle collision, jumping/sliding, lane boundaries, collectibles and mission progression.
7. Repeat layout testing on a taller phone or emulator.
8. Fill the recorded results below; only then update README verification claims.

## Manual result log template

| Date | Commit SHA | Device/Android | Test | Result (PASS/FAIL) | Notes/evidence |
| --- | --- | --- | --- | --- | --- |
| _add date_ | _add SHA_ | _model/version_ | Game installation | NOT RUN | _link evidence_ |
| _add date_ | _add SHA_ | _model/version_ | Gameplay + collisions | NOT RUN | _link evidence_ |
| _add date_ | _add SHA_ | _model/version_ | High score/settings persistence | NOT RUN | _link evidence_ |
| _add date_ | _add SHA_ | _model/version_ | Pause/resume | NOT RUN | _link evidence_ |
| _add date_ | _add SHA_ | _model/version_ | UI tall aspect ratio | NOT RUN | _link evidence_ |

Link passing GitHub Actions run URL here once there is one. Do **not** treat a generated YAML file as a passing workflow.
