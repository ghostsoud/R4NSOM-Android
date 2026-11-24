# R4NSOM Android

Android horror-quest prototype for POCO / Android devices.

## Current prototype
- User-started overlay game mode.
- Five coins are placed at random screen positions each launch.
- Coins can be collected while switching between apps.
- The app does not lock the device, modify files, or simulate real system compromise.

## Important Android limitation
Android does not let ordinary apps inject UI into every app or protected system screen. Coin overlays require the user to grant **Display over other apps** permission and may be hidden on secure/system surfaces. The foreground notification remains visible while the game is active.

## Build
GitHub Actions builds a debug APK on every push to `main` and on manual workflow dispatch. Download it from the workflow run's **Artifacts** section.
