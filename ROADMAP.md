# NEON RUSH roadmap

Statuses refer to source work and verified execution separately. Do not interpret implemented source as a passed device test.

| Milestone | Source | Verification |
| --- | --- | --- |
| 1. Inspect tools and repository | Partial: connected GitHub mismatch; local inaccessible | Android SDK unavailable |
| 2. Documentation and ignore rules | Written in ZIP | Not committed |
| 3. Project baseline | Gradle multi-module files drafted | Android sync/build not run |
| 4. Entry and navigation | Implemented | Unverified |
| 5. Main menu/theme | Implemented | Unverified |
| 6. Three-lane rendering | Implemented | Unverified |
| 7. Swipe lane controls | Implemented | Unverified |
| 8. Jump/slide | Implemented | Unit logic compiled |
| 9. Obstacle collision | Implemented | Unit logic compiled |
| 10. Orb/score | Implemented | Unit logic compiled |
| 11. Difficulty ramp | Implemented | Unit logic compiled |
| 12. Pause/retry/game-over | Implemented | Unverified on device |
| 13. Local settings/high scores | Implemented | Unverified on device |
| 14. Audio/simple visual feedback | Implemented | Unverified on device |
| 15. UI/performance/accessibility polish | Outstanding | Outstanding |
| 16. Unit tests/edge cases | Tests written | Gradle test run outstanding |
| 17. Real Android device | Outstanding | Outstanding |
| 18. Documentation/screenshots | Docs drafted; screenshots outstanding | Outstanding |
| 19. Signed AAB | Not started | Outstanding |
| 20. Portfolio demo and review | Not started | Outstanding |

## Next recommended checkpoint

Import into Android Studio, resolve any Gradle/SDK issues, then verify `:core:test` and `:android:assembleDebug`. Make no git commit until the build is passing and diff has been reviewed.
