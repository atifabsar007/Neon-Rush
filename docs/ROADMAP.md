# NEON RUSH Roadmap

## 0.4.0 — Local profile and progression

- [x] Player nickname stored locally.
- [x] XP, levels and Neon Coin rewards.
- [x] Lifetime run, distance and orb statistics.
- [x] Profile and lifetime milestone screens.
- [x] Expanded run-end reward summary.
- [x] Main menu refreshed around player progression.

## Next priorities

1. Run `:core:test` and `:android:assembleDebug` in Android Studio / CI.
2. Test touch controls, text input, pause/resume and all screens on real devices.
3. Add a proper cosmetics garage (skins, trails and runner palettes) with a clear coin economy.
4. Add daily/weekly mission rotation with date-safe local persistence.
5. Add accessibility options: reduced flashes, larger UI and vibration toggle.
6. If online accounts are wanted, design Firebase/custom API authentication, cloud save conflict rules, privacy policy and secure leaderboard validation before implementation.

## Explicitly not included in 0.4.0

- Online email/password registration or social login.
- Cloud saves or global leaderboards.
- Real-time multiplayer.
- In-app purchases or ads.

These require backend, security, policy and release work beyond a local Android source upgrade.
