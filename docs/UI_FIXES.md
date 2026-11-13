# NEON RUSH 0.5.1 — UI and input fixes

- Centered text now scales down to fit the safe width of the 480×800 logical viewport.
- Button labels are measured and scaled to the inner button width.
- Profile names are constrained to the profile card.
- Long instructional, mission and authentication messages wrap into bounded lines.
- Lane changes use exponential easing so interpolation feels consistent across different frame rates.
- Swipe thresholds are reduced from 54 to 38 logical units for more responsive touch controls.
- Existing player artwork, road rendering, jump/slide timing and game rules are preserved.

## Google Sign-In build fix

The app no longer directly references `R.string.default_web_client_id`, because that generated symbol does not exist until the Google Services plugin processes a valid `google-services.json`. It now resolves the string resource by name at runtime. This removes the Kotlin compile error when Firebase setup is incomplete. Google Sign-In still requires a valid Firebase Android app configuration and Web OAuth client.
