# NEON RUSH Development Roadmap

_Last reviewed 2026-10-20. “In source” does not mean runtime verified._

| Stage | Status | Definition of done |
| --- | --- | --- |
| Repository: publish browseable Android Studio source | **Missing on public main** | `android/`, `core/`, `lwjgl3/`, `assets/`, Gradle config and tests visible in GitHub. |
| Kotlin/Android project setup | **In v0.3.0 archive** | Fresh clone, Gradle sync and build succeed on developer computer. |
| Main menu, settings and instructions | **In source** | Navigation smoke tested on emulator and phone. |
| Three-lane movement and swipe controls | **In source** | Inputs work at varied touch speeds and ratios. |
| Jumping, sliding, obstacles and orbs | **In source** | All collision states pass deterministic tests. |
| Score/high score and difficulty progression | **In source** | Tests pass and high score persists after restart. |
| Cartoon runner and neon visuals | **In source** | Performance/readability confirmed on device. |
| Six offline missions | **In source** | Mission test scenarios and persistence verified. |
| UI adaptation for tall phone screens | **Open** | No clipped input controls/HUD across target ratios. |
| Automated test and debug APK pipeline | **Prepared, not published** | `.github/workflows/android-ci.yml` passes on `main`. |
| GitHub Release | **Not published** | Tagged tested APK matches source commit and notes. |
| Screenshots/GIF, complete README, attribution | **Partially drafted** | Real screenshots and linked release visible. |
| Signing and production AAB | **Future** | Signed AAB validated without exposing signing secrets. |
| Firebase leaderboard / multiplayer | **Optional future scope** | Security, matchmaking, backend and latency budgets defined before implementation. |

## Near-term priorities

1. Commit extracted source code and documentation to GitHub.
2. Run unit tests and debug build using the actual Android toolchain.
3. Fix any failing CI steps and capture a verified test log.
4. Smoke test on a phone and at least one alternate aspect ratio.
5. Publish a beta Release using the tested APK and matching Git tag.
6. Add a gameplay GIF and build badge to README after verification.

No artificial milestones or historical commits are claimed.
