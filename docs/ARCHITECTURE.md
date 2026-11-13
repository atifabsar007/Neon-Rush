# NEON RUSH Architecture — 0.4.0

- `NeonRushGame` owns the shared renderer, saves, audio and mission services.
- `BaseScreen` provides a portrait 480×800 logical viewport.
- `MenuScreen`, `ProfileScreen`, `MilestonesScreen`, `SettingsScreen`, `HowToPlayScreen`, `PlayScreen` and `GameOverScreen` own the user-facing flows.
- `RunSession` and `ScoreRules` contain platform-independent gameplay rules.
- `MissionManager` handles ordered run missions.
- `SaveRepository` persists local nickname, audio preferences, high score, mission progress, XP, player level, Neon Coins and lifetime statistics through LibGDX Preferences.
- `NeonPainter` draws the procedural neon environment and interface.

## Progression flow

At run end, `PlayScreen` creates one summary. `MissionManager` records mission progress, `SaveRepository.recordRun` stores lifetime statistics and grants coins/XP, and `recordScore` updates the personal best. `GameOverScreen` presents the resulting rewards.

## Account boundary

The 0.4.0 profile is device-local. It does not authenticate a person or sync across devices. Online identity and cloud services must be implemented as a separate backend integration; never store plaintext passwords in LibGDX Preferences.
