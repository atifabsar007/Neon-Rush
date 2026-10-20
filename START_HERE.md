# NEON RUSH publication pack

This ZIP contains **drop-in documentation and CI configuration**, not a new game build or an APK. No file has been published to GitHub.

## Include in your extracted source repository

- `.github/workflows/android-ci.yml`
- `docs/ARCHITECTURE.md`
- `docs/ROADMAP.md`
- `docs/TESTING.md`
- `docs/ASSET_ATTRIBUTION.md`
- `docs/RELEASE_GUIDE.md`
- `docs/LICENSE_OPTIONS.md`
- `CHANGELOG.md`

Do not blindly overwrite: compare these files against any newer local edits before merging.

## Reference templates, not files required in repo

- `release/RELEASE_NOTES_TEMPLATE.md`: paste into GitHub Release description after filling in real test/source metadata.
- `README_ADDITIONS.md`: selected paragraphs and badge to merge into the existing README.

## Publish order

1. Extract the latest intended **v0.3.0 source** package and upload individual source files to the correct GitHub repository. Today the live `main` branch contains only a zip, README and APK.
2. Review the diff and commit source and docs separately. Example commit: `docs: document architecture, release process and tests`.
3. Add CI workflow and commit: `ci: validate Kotlin tests and Android debug build`.
4. Verify the GitHub Actions run in **Actions > Android CI**. If it fails, diagnose and fix it; this pack does not claim success.
5. Build, version-check, install and test the final APK from the exact source commit you will tag.
6. Use `docs/RELEASE_GUIDE.md` to publish a **pre-release** with the verified debug APK.
7. After a passing workflow, add the status badge to README. Add a project LICENSE only after deliberately choosing its terms.

**Source/Version warning:** the existing public repo ZIP is v0.2.0, but README claims v0.3.0. The public APK's version/source relationship has not been confirmed. Fix this before publishing `v0.3.0-beta.1`.
