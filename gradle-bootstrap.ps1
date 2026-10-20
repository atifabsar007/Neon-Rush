$ErrorActionPreference = 'Stop'
$version = '8.14.6'
$expected = '7988ed071b2a07900e2ec715fca15c6ab72bce6db433af301fa7fa7e9407bd24'
$folder = Join-Path $HOME '.gradle\neonrush-bootstrap'
$gradle = Join-Path $folder "gradle-$version\bin\gradle.bat"
if (!(Test-Path $gradle)) {
    New-Item -ItemType Directory -Force -Path $folder | Out-Null
    $zip = Join-Path $folder "gradle-$version-bin.zip"
    Invoke-WebRequest "https://services.gradle.org/distributions/gradle-$version-bin.zip" -OutFile $zip
    $actual = (Get-FileHash $zip -Algorithm SHA256).Hash.ToLowerInvariant()
    if ($actual -ne $expected) {
        Remove-Item $zip
        throw 'Gradle checksum mismatch. Download rejected.'
    }
    Expand-Archive $zip -DestinationPath $folder -Force
    Remove-Item $zip
}
Push-Location $PSScriptRoot
try {
    & $gradle @args
    exit $LASTEXITCODE
} finally {
    Pop-Location
}
