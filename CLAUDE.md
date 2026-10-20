# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project

AuctionHouse — a collectibles auction app built with Kotlin Multiplatform + Compose Multiplatform (Android, iOS, Desktop/JVM). It's a university lab project (currently Lab 4); the README is in Ukrainian and lists what each lab adds.

## Commands

Run on Windows with `./gradlew` from Git Bash or `.\gradlew.bat` from PowerShell.

- Android: `./gradlew :androidApp:assembleDebug`
- Desktop: `./gradlew :desktopApp:run` (hot reload: `./gradlew :desktopApp:hotRun --auto`)
- iOS: open `iosApp/` in Xcode (needs macOS)
- Tests:
  - common + JVM: `./gradlew :shared:jvmTest`
  - Android host: `./gradlew :shared:testAndroidHostTest`
  - iOS: `./gradlew :shared:iosSimulatorArm64Test`
- Single test: `./gradlew :shared:jvmTest --tests "com.bilatchuk.auctionhouse.SharedCommonTest"` (add `.methodName` to run one method)

Dependency versions live in `gradle/libs.versions.toml`.

## Architecture

- All app code is in `:shared`. `androidApp`, `desktopApp` and `iosApp` are thin entry points that just call `App()` (iOS goes through `MainViewController.kt`).
- `App.kt` is the root composable. It owns the app-level state: the selected language and whether onboarding is showing. It switches between `ui/onboarding/OnboardingScreen` (a 4-page `HorizontalPager`) and `ui/main/MainScreen`.
- **Theme** (`ui/theme`): `AuctionTheme` is dark-only (graphite + gold). Colors are in `Color.kt`, custom `Shapes` in `Theme.kt`, and typography in `Type.kt` (Playfair Display for headings, Inter for body). The fonts are loaded from `composeResources/font` through `Res.font.*`, so `auctionTypography()` must be `@Composable`.
- **Decor** (`ui/components/Decor.kt`): `Modifier.wallpaper()` (ромбова ґратка + сяйво) для кореня екрана, `GoldFrame` (багет + паспарту, `arch` для аркової рамки) навколо фото, `BackdropPhoto` як затемнений фон секції. Фото поки плейсхолдери з підписом (`Lot.photo`); щоб підставити знімок, передайте `painter` у `GoldFrame`/`BackdropPhoto`.
- **Resources**: strings are in `composeResources/values/strings.xml` (English) and `values-uk/strings.xml` (Ukrainian), accessed through the generated `auction_house.shared.generated.resources.Res`. Add every new string to both files.
- **Localization switching**: `expect fun applyAppLanguage(tag)` in `Localization.kt`. On Android and JVM it calls `Locale.setDefault`; on iOS it writes `AppleLanguages` to `NSUserDefaults`. `App` wraps the whole tree in `key(language)` so the UI recomposes and resources resolve again after a language change.
- **Persistence**: `data/AppSettings` wraps multiplatform-settings (`Settings()`, no-arg platform default). Currently only the language is stored.
- **Logging**: Kermit, using `Logger.withTag(...)` for each file or class.
