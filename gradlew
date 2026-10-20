#!/bin/sh
# Offline-generated project: this portable bootstrap replaces the usual Gradle wrapper JAR.
# It verifies the official Gradle distribution hash on first use.
set -eu
ROOT=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
VERSION=8.14.6
CHECKSUM=7988ed071b2a07900e2ec715fca15c6ab72bce6db433af301fa7fa7e9407bd24
DIST="$HOME/.gradle/neonrush-bootstrap/gradle-$VERSION"
if [ ! -x "$DIST/bin/gradle" ]; then
  command -v curl >/dev/null || { echo 'curl is required for the first setup.' >&2; exit 1; }
  command -v unzip >/dev/null || { echo 'unzip is required for the first setup.' >&2; exit 1; }
  mkdir -p "$HOME/.gradle/neonrush-bootstrap"
  ZIP="$HOME/.gradle/neonrush-bootstrap/gradle-$VERSION-bin.zip"
  curl --fail --location --show-error --silent "https://services.gradle.org/distributions/gradle-$VERSION-bin.zip" -o "$ZIP"
  if command -v shasum >/dev/null 2>&1; then
    ACTUAL=$(shasum -a 256 "$ZIP" | awk '{print $1}')
  elif command -v sha256sum >/dev/null 2>&1; then
    ACTUAL=$(sha256sum "$ZIP" | awk '{print $1}')
  else
    echo 'A SHA-256 checksum tool is required.' >&2; exit 1
  fi
  [ "$ACTUAL" = "$CHECKSUM" ] || { echo 'Gradle checksum mismatch; file rejected.' >&2; rm -f "$ZIP"; exit 1; }
  unzip -q -o "$ZIP" -d "$HOME/.gradle/neonrush-bootstrap"
  rm -f "$ZIP"
fi
cd "$ROOT"
exec "$DIST/bin/gradle" "$@"
