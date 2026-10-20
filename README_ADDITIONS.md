# Copy these sections into the existing README.md

These are **additions**, not a replacement for the full README. Update version/download claims only after an APK/source match is verified.

## 📦 Android builds

Development builds are published on the [Releases page](https://github.com/atifabsar007/Neon-Rush/releases) after their commit, version number and real-device results have been checked. A **debug APK** is intended for testing, not for Google Play distribution.

## 🧪 Building and testing

After the actual Android Studio source has been committed to the repository:

```bash
# macOS/Linux (existing project bootstrap)
./gradlew :core:test :android:assembleDebug
```

The project currently includes a custom Gradle bootstrap rather than the standard `gradle-wrapper.jar`. The CI workflow explicitly installs Gradle 8.14.6 and runs the equivalent tasks; restore the official Gradle Wrapper when possible.

See [testing](docs/TESTING.md), [architecture](docs/ARCHITECTURE.md), [roadmap](docs/ROADMAP.md) and [asset attribution](docs/ASSET_ATTRIBUTION.md).

## 🔄 Continuous integration

Once the **Android CI** workflow has run successfully on `main`, place this badge near the top of the README:

```markdown
[![Android CI](https://github.com/atifabsar007/Neon-Rush/actions/workflows/android-ci.yml/badge.svg?branch=main)](https://github.com/atifabsar007/Neon-Rush/actions/workflows/android-ci.yml)
```

Do **not** add it as an indication of success before inspecting an actual passing Actions run.
