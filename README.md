# AuctionHouse — аукціон колекційних предметів

Kotlin Multiplatform (Android, iOS, Desktop) + Compose Multiplatform. Лабораторна робота №4.

## Що реалізовано
- Кастомна тема: кольори та форми (темна палітра «графіт + золото»), шрифти Playfair Display та Inter — `shared/.../ui/theme`
- Логування — **Kermit**, налаштування користувача — **multiplatform-settings** (`data/AppSettings.kt`)
- Онбординг на `HorizontalPager` (4 слайди) — `ui/onboarding/OnboardingScreen.kt`, показується один раз
- Локалізація українською та англійською — `composeResources/values` і `values-uk`, перемикач мови на головному екрані (вибір зберігається)
- Головний екран із перемикачем мови — `ui/main/MainScreen.kt`
- Дизайн «Нуар-галерея»: головна, каталог, лот, кабінет (перемикач мови — у кабінеті) та адаптивний макет (нижня навігація на телефоні, верхня на десктопі) — `ui/screens`, `ui/main`
- Шпалери із золотою ґраткою, золоті багетні рамки з паспарту та фонові фото-плейсхолдери — `ui/components/Decor.kt` (`wallpaper`, `GoldFrame`, `BackdropPhoto`). Реальні знімки підключаються через параметр `painter`

Шрифти: Google Fonts (SIL Open Font License).

---

This is a Kotlin Multiplatform project targeting Android, iOS, Desktop (JVM).

* [/iosApp](./iosApp/iosApp) contains an iOS application. Even if you’re sharing your UI with Compose Multiplatform,
  you need this entry point for your iOS app. This is also where you should add SwiftUI code for your project.

* [/shared](./shared/src) is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
  - [commonMain](./shared/src/commonMain/kotlin) is for code that’s common for all targets.
  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
    For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
    the [iosMain](./shared/src/iosMain/kotlin) folder would be the right place for such calls.
    Similarly, if you want to edit the Desktop (JVM) specific part, the [jvmMain](./shared/src/jvmMain/kotlin)
    folder is the appropriate location.

### Running the apps

Use the run configurations provided by the run widget in your IDE's toolbar. You can also use these commands and options:

- Android app: `./gradlew :androidApp:assembleDebug`
- Desktop app:
  - Hot reload: `./gradlew :desktopApp:hotRun --auto`
  - Standard run: `./gradlew :desktopApp:run`
- iOS app: open the [/iosApp](./iosApp) directory in Xcode and run it from there.

### Running tests

Use the run button in your IDE's editor gutter, or run tests using Gradle tasks:

- Android tests: `./gradlew :shared:testAndroidHostTest`
- Desktop tests: `./gradlew :shared:jvmTest`
- iOS tests: `./gradlew :shared:iosSimulatorArm64Test`

---

Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…