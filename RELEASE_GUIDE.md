# How to publish the first NEON RUSH Release

## Before creating a release

**Current public repository observation (2026-10-20):** the default branch contains `README.md`, `NEON_RUSH_Android_Studio_Source_v0.2.0_RECHARGED.zip` and `android-debug.apk`. The README describes v0.3.0, but the ZIP is v0.2.0, and the APK's source/version was not independently verified. No releases have been published.

1. Back up the current repository. Extract the latest intended source, and upload the actual `android/`, `core/`, `lwjgl3/`, `assets/`, `docs/`, Gradle, and root files to `main` (ideally through a reviewed Git commit). Do not accidentally replace or delete current history.
2. Run `./gradlew :core:test :android:assembleDebug` locally (Windows: `gradlew.bat`). If the custom bootstrap is unavailable, install Gradle 8.14.6 and use `gradle ...`. Verify the Android SDK 36 installation.
3. Read the build output, check the generated APK's `versionName` and `versionCode`, and ensure the APK came from the exact `main` commit to tag. Use Android Studio's APK Analyzer or the Android SDK's `apkanalyzer` to inspect the build.
4. Install and smoke test *that exact APK* on a physical Android phone. Record the device/OS and results in `docs/TESTING.md`.
5. Choose the matching tag, for example `v0.3.0-beta.1` only for a verified v0.3.0 preview. A versioned release must identify the actual source, not the README text alone.

## GitHub web steps

1. Open https://github.com/atifabsar007/Neon-Rush/releases/new while signed in as repository owner.
2. Click **Choose a tag**, type your verified tag (example: `v0.3.0-beta.1`), select **Create new tag** at the matching commit/branch.
3. Title: `NEON RUSH v0.3.0 Beta 1 | Android Development Preview` (only when the APK actually matches).
4. Paste `release/RELEASE_NOTES_TEMPLATE.md` after replacing its placeholders and deleting unverified claims.
5. Drag the matching APK into the **Attach binaries** area at the bottom, using a descriptive filename such as `NEON-RUSH-v0.3.0-beta.1-debug.apk`.
6. Tick **Set as a pre-release**. A debug APK is for testing; it is not a signed production Play Store build.
7. Click **Save draft** if tests/version matching are incomplete. Otherwise click **Publish release**.
8. After publishing, put the release link in your README and point to that download rather than committing more APKs to the source tree.

## Signing and Android packaging

For a production Play Store release, use an appropriately configured, privately stored signing key and create a signed AAB using Android Studio's **Build > Generate Signed Bundle / APK** wizard. Do not commit keystores, passwords, or credentials. This guide does not claim production signing has occurred.

## Recommended sequence

1. Publish extracted source and documentation.
2. Add `.github/workflows/android-ci.yml` and fix any CI failures.
3. Test a rebuilt APK on a physical device.
4. Create a pre-release tied to the same commit and attach that APK.
5. Add a badge after CI really passes.
