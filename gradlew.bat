@echo off
REM NEON RUSH verified Gradle bootstrap (see README).
powershell.exe -NoProfile -File "%~dp0gradle-bootstrap.ps1" %*
exit /b %ERRORLEVEL%
