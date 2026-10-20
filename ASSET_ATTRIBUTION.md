# NEON RUSH Asset Attribution

_This inventory reflects the locally reviewed v0.3.0 source ZIP, not every past APK._

| Asset | Location | Origin and usage |
| --- | --- | --- |
| Game road, neon skyline, obstacle and UI shapes | `core/src/main/kotlin/com/neonrush/render/NeonPainter.kt` | Original procedural geometry authored for the NEON RUSH source; no downloaded images used. |
| Cartoonized playable runner | `NeonPainter.kt` | Procedural illustration based on reference photos supplied for this game. The personal reference photos are not bundled. |
| Synthesized ambient music | `assets/ambient.wav` | Original synthesized audio generated for the game. |
| Movement/pickup/crash effects | `assets/move.wav`, `pickup.wav`, `crash.wav` | Original synthesized sound effects created for the game. |
| Android launcher icon | `android/src/main/res/mipmap-*/ic_launcher.png` | Generated specifically for this project. |
| Default bitmap font | Created through LibGDX `BitmapFont()` | LibGDX framework resource; follow upstream LibGDX licensing notices when redistributing. |

No third-party commercial images, fonts, music recordings or external character sprites were intentionally included in the inspected source package. If you add any new image, audio or font, record the author, URL, license, attribution requirement, and modification here **before** release.

### License boundary

The game's code/assets and the LibGDX framework have distinct provenance. A project's `LICENSE` file covers only material its owner has rights to license; upstream dependencies retain their own licenses. Choose a project license deliberately. See `docs/LICENSE_OPTIONS.md`.
