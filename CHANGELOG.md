# Changelog

## 0.4.0 — Neon Profile & Progression Upgrade

- Reworked the home screen into a player-focused arcade dashboard.
- Added local editable player profile.
- Added XP and player levels.
- Added Neon Coins earned from run distance and energy orbs.
- Added persistent lifetime run, distance and orb statistics.
- Added XP progress bar to the profile.
- Added lifetime milestone tracker.
- Improved game-over reward summary.
- Fixed misleading lane movement audio at lane boundaries.
- Updated Android version metadata.
- Clarified that online authentication and cloud leaderboards are not included yet.

## 0.3.0

- Three-lane endless runner, missions, settings, tutorial, local best score, original audio and procedural neon visuals.

## 0.5.0
- Added Firebase email/password and anonymous guest authentication.
- Added Google Sign-In and password reset integration.
- Added Firestore player cloud sync and global top-10 leaderboard.
- Added authentication entry screen and logout flow.
- Added FIREBASE_SETUP.md with setup steps and security rules.


## 0.5.1 — UI, input and Google Sign-In compile fix
- Fixed the compile-time dependency on the generated `R.string.default_web_client_id` symbol. Google OAuth client ID is now looked up safely at runtime, so the Android project compiles before Firebase JSON is added.
- Added a clear Google Sign-In setup error when the Web OAuth client ID is missing; email login and guest mode remain available.
- Added automatic single-line text fitting and constrained button-label fitting to prevent overflow.
- Wrapped longer help, mission and authentication status copy within safe card widths.
- Improved lane interpolation with frame-rate-independent exponential easing.
- Lowered swipe recognition threshold slightly for more responsive touch gestures.
