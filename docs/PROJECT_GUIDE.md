# AuctionHouse — детальний гайд по проєкту

Цей документ пояснює інструменти, конструкції та методи, які використані в проєкті, і відповідає на питання «чи тут MVI?».
Він складений за фактичним кодом у `shared/src` та README.

> **Важливо про розділ «Лабораторна №4».** Дослівного тексту пунктів лаби в мене немає, тому я взяв за пункти лаби перші п'ять
> пунктів README (тема, Kermit + settings, онбординг на пейджері, локалізація, головний екран із перемикачем мови).
> Усе, що README описує нижче («Нуар-галерея», навігація, каталог, лот, кабінет, декор), винесено в окремий розділ 3.
> Якщо ваші пункти лаби відрізняються, перенесіть блоки між розділами 2 і 3.

---

## Зміст

1. Загальна картина: стек і структура
2. **Лабораторна №4 — пункти завдання**
   - 2.1 Кастомна тема (кольори, форми, шрифти)
   - 2.2 Логування Kermit
   - 2.3 Налаштування multiplatform-settings
   - 2.4 Онбординг на HorizontalPager (4 слайди)
   - 2.5 Локалізація uk/en
   - 2.6 Головний екран із перемикачем мови
3. **Понад пункти лаби (додано поверх)**
4. MVI: чи він тут і як його вводити під .NET API
5. Словник конструкцій Kotlin / Compose
6. Помітні особливості та потенційні проблеми
7. Команди

---

## 1. Загальна картина

### 1.1 Стек

| Інструмент | Версія (з `libs.versions.toml`) | Навіщо |
|---|---|---|
| Kotlin Multiplatform | 2.4.20 | один код для Android, iOS, Desktop(JVM) |
| Compose Multiplatform | 1.12.1 | декларативний UI, спільний для всіх платформ |
| Material 3 | 1.12.0-alpha03 | база компонентів, `MaterialTheme`, `Typography`, `Shapes` |
| AGP | 9.1.1 | збирання Android (плагін `com.android.kotlin.multiplatform.library`) |
| Kermit | 2.0.4 | мультиплатформне логування |
| multiplatform-settings (`no-arg`) | 1.3.0 | key-value сховище (SharedPreferences / NSUserDefaults / Preferences) |
| kotlinx-coroutines | 1.11.0 | корутини (у desktop — `coroutines-swing`) |
| androidx lifecycle (viewmodel-compose, runtime-compose) | 2.11.0 | **підключені, але поки не використовуються** (див. розділ 4) |

### 1.2 Модулі

```
auction-house/
├─ shared/        ← ВЕСЬ код застосунку (KMP-бібліотека)
├─ androidApp/    ← тонка точка входу: MainActivity -> App()
├─ desktopApp/    ← тонка точка входу: main.kt -> window { App() }
└─ iosApp/        ← Xcode-проєкт, викликає MainViewController() -> App()
```

`shared` має source set-и:

- `commonMain` — спільний код (99% проєкту).
- `androidMain`, `jvmMain`, `iosMain` — лише `actual`-реалізації (зараз тільки `applyAppLanguage`) і `MainViewController`.
- `commonTest`, `jvmTest`, `androidHostTest`, `iosTest` — тести (поки заглушки).

### 1.3 Карта файлів `commonMain`

```
App.kt                      корінь: мова + онбординг/головний екран
Localization.kt             expect fun applyAppLanguage
data/AppSettings.kt         обгортка над Settings
data/Models.kt              Lot, Category, BidEntry, HistoryEntry + SampleData
data/Format.kt              ціна, час, «N хв тому»
ui/theme/{Color,Theme,Type}.kt
ui/onboarding/OnboardingScreen.kt
ui/main/MainScreen.kt       навігація + адаптивний макет
ui/screens/{Home,Catalog,Lot,Account}Screen.kt
ui/components/{Components,Decor}.kt
composeResources/{values,values-uk}/strings.xml, font/
```

---

## 2. Лабораторна №4 — пункти завдання

### 2.1 Кастомна тема: кольори, форми, шрифти

Файли: `ui/theme/Color.kt`, `Theme.kt`, `Type.kt`.

**Кольори (`Color.kt`).** Об'єкт `Noir` зберігає палітру як `Color(0xAARRGGBB)`:

- `Background 0xFF0C0B0A` (майже чорний), `Surface`, `SurfaceHigh` — рівні поверхонь;
- `Ink` (слонова кістка) — основний текст, `Muted` — другорядний;
- `Gold`, `GoldLine` (30% альфа), `GoldLineStrong` (50%), `GildEdge` — золоті акценти й лінії;
- `Mat` — колір паспарту; `Burgundy`, `Danger`, `OnDanger` — службові.

Запис `0x4DC9A96B`: перші два hex-символи (`4D`) — альфа, решта — RGB. `4D` ≈ 30%.

**Колірна схема (`Theme.kt`).** `darkColorScheme(...)` мапить палітру на ролі Material 3:
`primary = Gold`, `background = Background`, `surface = Surface`, `outline = GoldLineStrong` тощо.
Додатково задано `surfaceContainer*` — без них Material 3 підставляє власні відтінки.
Тема **тільки темна**: світлої схеми немає, `isSystemInDarkTheme()` не перевіряється.

**Форми.** `Shapes(extraSmall = 0.dp ... extraLarge = 4.dp)` — майже прямі кути (стиль «Нуар»).
`RoundedCornerShape(x.dp)` — прямокутник із закругленням кутів.

**Тема як composable.** `AuctionTheme(content)` обгортає `MaterialTheme(colorScheme, typography, shapes)`.
Усередині додатково `CompositionLocalProvider(LocalContentColor provides Noir.Ink, ...)`:
без `Surface`/`Scaffold` стандартний колір вмісту — чорний, і `Text` без явного кольору був би чорним на чорному фоні.

**Шрифти (`Type.kt`).** `auctionTypography()` будує `Typography`: заголовки — **Playfair Display** (`heading`), текст — **Inter** (`body`).
`Font(Res.font.playfair_display)` — це composable-виклик (`org.jetbrains.compose.resources.Font`), тому функція має бути `@Composable`.
Файли шрифтів лежать у `composeResources/font`, доступ через згенерований `Res.font.*`.
Одиниці: `sp` — масштабований розмір шрифту, `em` — відносна одиниця (для `letterSpacing`).

### 2.2 Логування — Kermit

- `private val log = Logger.withTag("Onboarding")` — логер із тегом на рівні файлу/класу (див. `App.kt`, `AppSettings.kt`, `MainScreen.kt`, `OnboardingScreen.kt`).
- Виклик `log.d { "..." }` — рівень debug; лямбда обчислюється лише якщо рівень увімкнено (ледача конкатенація рядків).
  `log.i { }` — info (використано в `AppSettings.language`).
- Де логуємо: старт застосунку (`App`), зміна мови (`AppSettings`), зміна маршруту (`Navigation`), зміна сторінки онбордингу.
- Логи кладуться в `LaunchedEffect(key) { ... }`, щоб писатися **один раз на зміну ключа**, а не при кожній рекомпозиції.
  Це типова помилка: `log.d` просто в тілі composable спрацьовує на кожну рекомпозицію.

### 2.3 Налаштування — multiplatform-settings

`data/AppSettings.kt`:

```kotlin
class AppSettings(private val settings: Settings = Settings()) {
    var language: String?
        get() = settings.getStringOrNull(KEY_LANGUAGE)
        set(value) { ... putString / remove }
}
```

- `Settings()` без аргументів доступний завдяки артефакту `multiplatform-settings-no-arg`: на кожній платформі створюється дефолтна реалізація.
- Властивість із кастомними `get`/`set` (backing field немає) — дані живуть у сховищі, не в пам'яті.
- `String?` — `null` означає «мову ще не вибирали» (тоді діє мова системи).
- Параметр за замовчуванням `settings: Settings = Settings()` дозволяє у тестах підставити фейк.
- `private companion object { const val KEY_LANGUAGE }` — константа ключа, прихована від зовнішнього коду.

Зараз зберігається тільки мова.

### 2.4 Онбординг на `HorizontalPager` (4 слайди)

Файл: `ui/onboarding/OnboardingScreen.kt`.

**Дані слайдів.**
- `enum class SlideFrame(ratio, arch, tilt)` — форма рамки: `Square`, `Wide`, `Arch` (арка), `Tall` (з нахилом −5°). Енум із полями замість «магічних чисел».
- `data class OnboardingPage(tag, title, text, photo, frame)` — один слайд. Поля — `StringResource`, тобто **ключі ресурсів, а не готові рядки**: переклад визначається в момент відображення.
- `private val pages = listOf(...)` — 4 слайди.

**Пейджер.**
- `rememberPagerState { pages.size }` — стан пейджера (кількість сторінок задається лямбдою). Дає `currentPage`, `animateScrollToPage`.
- `HorizontalPager(state, modifier) { index -> Slide(pages[index], ...) }` — прокручування свайпом.
- `rememberCoroutineScope()` + `scope.launch { pager.animateScrollToPage(...) }` — `animateScrollToPage` це suspend-функція, її викликають з корутини.
- `val last = pager.currentPage == pages.lastIndex` — на останньому слайді кнопка «Далі» стає «Почати» і викликає `onFinish`.
- Кнопка «Пропустити» одразу викликає `onFinish`.

**Індикатор.** `Dots`: для кожної сторінки `animateDpAsState(if (active) 26.dp else 10.dp)` плавно змінює ширину крапки. Область натискання ≥ 44dp (`heightIn(min = 44.dp)`) — доступність.

**Адаптивність.** `BoxWithConstraints` дає `maxWidth`; `wide = maxWidth >= 840.dp` перемикає між двома стовпцями (текст | ілюстрація) і вертикальним макетом. На широкому екрані з'являється кнопка «Назад».

**Стан наверх.** `OnboardingScreen(onFinish: () -> Unit)` не знає, що буде далі, — просто викликає колбек (state hoisting). Прапорець `showOnboarding` живе в `App`.

### 2.5 Локалізація uk/en

- Рядки: `composeResources/values/strings.xml` (англійська, дефолт) і `values-uk/strings.xml` (українська). Правило з CLAUDE.md: кожен новий рядок додавати в обидва файли.
- Доступ: `stringResource(Res.string.next)` — `Res` генерується плагіном Compose Resources (пакет `auction_house.shared.generated.resources`). Для параметрів — `stringResource(Res.string.bids_count, lot.bids.toString())` із плейсхолдерами у XML.
- `StringResource` (як тип у моделях `Lot.title`) дозволяє зберігати в даних ключ, а не текст.
- **Перемикання мови під час роботи**: `expect fun applyAppLanguage(languageTag: String)` у `Localization.kt`, три `actual`:
  - Android і JVM: `Locale.setDefault(Locale.forLanguageTag(tag))`;
  - iOS: запис `AppleLanguages` в `NSUserDefaults`.
- У `App` увесь UI загорнуто в `key(language) { ... }`: при зміні `language` дерево композиції створюється заново, і `stringResource` перечитує ресурси за новою локаллю.

`expect/actual` — механізм KMP: у `commonMain` оголошується контракт, у платформних source set-ах — реалізація.

### 2.6 Головний екран із перемикачем мови

- За README, перемикач мови спочатку був на головному екрані; **тепер перенесений у кабінет** (`AccountScreen`, блок «Мова»: два `GoldChip` — `en` / `uk`).
- Логіка в `App`:
  ```kotlin
  onLanguageSelected = { tag ->
      settings.language = tag      // зберегти
      applyAppLanguage(tag)        // застосувати до платформи
      language = tag               // оновити стан -> key(language) перебудує UI
  }
  ```
- При старті збережена мова читається й застосовується в `remember { ... }` до першого відмальовування.
- `MainScreen(currentLanguage, onLanguageSelected)` просто прокидає колбек униз до `AccountScreen`.

---

## 3. Понад пункти лаби

Нижче — те, що не входить у п'ять пунктів вище: фактично вже наступний етап (дизайн «Нуар-галерея» та повноцінна навігація).

### 3.1 Навігація (`ui/main/MainScreen.kt`)

Власна навігація без бібліотек (без Navigation Compose / Voyager / Decompose):

- `private sealed interface Route` з `data object Home`, `Catalog`, `data class Account(tab)`, `LotDetail(id)`.
  `sealed` гарантує, що `when (route)` вичерпний — компілятор не дасть забути маршрут.
- Стек: `remember { mutableStateListOf<Route>(Route.Home) }`; поточний екран — `stack.last()`.
- `open(target)`: якщо це не `LotDetail` — стек очищується (перехід між верхніми розділами), далі додається ціль (без дубліката зверху). Лот відкривається поверх поточного екрана.
- `back()`: знімає верхній елемент; якщо стек із одного — веде на Home.
- `fun ::open` — посилання на локальну функцію як значення колбека.
- **Системна кнопка «Назад» (Android) не підключена**: стек не перехоплює `BackHandler`. Тобто «назад» працює лише кнопкою в `LotScreen`.

### 3.2 Адаптивний макет

- `BoxWithConstraints` + `WideBreakpoint = 840.dp`.
- Широкий (desktop/планшет): `TopNav` зверху, контент обмежений `widthIn(max = 1280.dp)`.
- Вузький (телефон): `BottomNav`; на екрані лота нижня навігація ховається (`route !is Route.LotDetail`).
- Підкреслення вкладок малюється через `Modifier.drawBehind`, а не дочірнім `fillMaxWidth` (коментар у коді пояснює: інакше вкладка розтягується на всю ширину).
- `statusBarsPadding()`, `navigationBarsPadding()`, `systemBarsPadding()` — відступи під системні панелі (edge-to-edge).

### 3.3 Екрани

| Екран | Що містить | Цікаві конструкції |
|---|---|---|
| `HomeScreen` | Hero «лот тижня», категорії (desktop) або чипи (mobile), «Завершуються сьогодні», банер наступного аукціону | `verticalScroll`, `LazyRow` + `items`, `GoldChip`, слот-лямбда `details: @Composable () -> Unit` |
| `CatalogScreen` | Пошук, чипи, фільтр за розділами (чекбокси на desktop), сітка лотів | `LazyVerticalGrid(GridCells.Adaptive)`, `GridItemSpan`, `mutableStateListOf` для чекбоксів, фільтрація списку в композиції |
| `LotScreen` | Галерея, панель ставки, характеристики, історія | `remember(lotId)`, фільтр вводу `it.filter(Char::isDigit)`, `BidStep` |
| `AccountScreen` | Профіль, статистика, вкладки (ставки/виграні/обрані), виграний лот, мова, меню | `enum AccountTab`, `chunked(2)`, `when` по вкладці |

Примітки:
- `AccountTab.Profile` показує ті ж «Мої ставки», але підсвічує пункт «Профіль» у навігації.
- Кнопки «Зробити ставку», «Застосувати», «Оплатити», «Поділитися» — порожні заглушки `{}`. Це нормально для етапу «тільки UI».

### 3.4 Компоненти (`ui/components/Components.kt`)

Власні замість стандартних Material (бо дизайн «Нуар» — прямі кути й тонкі золоті лінії):

- `Hairline` — 1dp лінія; `Eyebrow` — підпис-розділювач «01 / 04 ———— ТЕГ»;
- `GoldButton` / `OutlineButton` — `Box` + `clickable(role = Role.Button)`;
- `GoldChip` (із `Role.Tab`), `StatusChip`, `StatCard`, `SectionTitle`;
- `LotCard` — картка лота (рамка, залишок часу, категорія, назва, ціна, ставки);
- `NoirTextField` — обгортка `OutlinedTextField` із кольорами теми;
- `InitialsAvatar` — коло з ініціалами (`clip(CircleShape)` + `border`).

`Role.Button`/`Role.Tab` — семантика для екранних читачів. `minLines = 2, maxLines = 2, overflow = Ellipsis` — однакова висота назви в сітці.

### 3.5 Декор (`ui/components/Decor.kt`)

- `Modifier.wallpaper()` — фон малюється через `drawBehind`: заливка, ромбова ґратка (`drawLattice` = дві серії діагональних ліній `drawStripes`), радіальне сяйво зверху (`Brush.radialGradient`).
  Константа `Diagonal = 1.4142` — √2, крок між діагональними лініями.
- `GoldFrame` — «багетна рамка» шарами модифікаторів: `shadow` → `background` → `border` → `padding` → `background(FrameGold)` (лінійний градієнт) → `padding(rim)` → `background(Mat)` → `padding(mat)`. Порядок модифікаторів у Compose значущий: кожен наступний працює всередині попереднього.
  `arch = true` дає аркову форму: `RoundedCornerShape(topStartPercent = 50, topEndPercent = 50, ...)`.
- `PhotoSlot` — плейсхолдер фото: градієнт + смуги + підпис, або значок камери через `Canvas`; якщо передати `painter` — показує реальне зображення (`Image` + `ContentScale.Crop`).
- `BackdropPhoto` — фон секції з затемненням (вертикальний градієнт + віньєтка).
- Підміна на справжні фото: передати `painter` у `GoldFrame` / `BackdropPhoto`; у `Lot.photo` зараз лежить підпис-плейсхолдер (`StringResource`).

### 3.6 Дані та форматування

- `data/Models.kt`: `Lot`, `Category`, `BidEntry`, `HistoryEntry` (data class-и) і `object SampleData` із жорстко закодованими даними. Коментар у коді прямо каже: «Замінити на репозиторій / API».
- `data/Format.kt`: `groupDigits` (розділення розрядів), `formatPrice` (`₴ 18 400`), `formatTimeLeft` і `formatAgo` — `@Composable`, бо беруть рядки з ресурсів.
- Таймер зворотного відліку статичний (`minutesLeft`), у коді є коментар, що живий відлік — окрема задача.

---

## 4. MVI: чи він тут?

### 4.1 Коротка відповідь

**Ні, MVI зараз немає. Але й MVVM немає.** Архітектура — «UI-only»: composable-функції самі тримають стан і самі беруть дані.

### 4.2 Як це виглядає зараз (факти з коду)

| Ознака MVI / MVVM | Є? | Де видно |
|---|---|---|
| ViewModel | Ні | `lifecycle-viewmodel-compose` підключена в `build.gradle.kts`, але жодного `ViewModel` у коді немає |
| Єдиний `State` екрана | Ні | `CatalogScreen`: `query`, `chip`, `checked` — окремі `remember { mutableStateOf }` |
| `Intent`/`Action` | Ні | замість них колбеки `onOpenLot`, `onBack`, `onTabSelected` |
| Reducer (`State + Intent -> State`) | Ні | зміни стану прямо в лямбдах (`chip = i`) |
| Side effects / events | Ні | немає каналів подій |
| Repository | Ні | `SampleData` — глобальний `object` |
| Навігація як стан | Частково | `Route` + `mutableStateListOf` — гарна основа, але прив'язана до composable `MainScreen` |
| Стан налаштувань | Так, окремий клас | `AppSettings` викликається з composable `App` |

Що це означає на практиці:

- **Фільтрація каталогу відбувається в композиції** (`SampleData.lots...filter {...}` у тілі `CatalogScreen`). Для 9 лотів це нормально, для сторінкового API — ні.
- **Стан екрана губиться** при виході з композиції: `query` і чипи в каталозі скидаються, коли ви відкрили лот і повернулись (екран залишає дерево). У `remember` немає `rememberSaveable`, а ViewModel, який пережив би це, відсутній.
- `key(language)` скидає також `stack` навігації (він усередині `key`), тобто після зміни мови користувач опиняється на Home.

### 4.3 Чи варто переходити на MVI

З огляду на те, що далі підключається API на .NET, — **так, MVI (або принаймні ViewModel + єдиний UiState) варто ввести, і зробити це краще до підключення мережі, а не після.** Причини:

1. З'являються **асинхронні стани**: завантаження, помилка, порожній список, оновлення. Їх зручно моделювати одним `sealed`/`data class` станом.
2. **Ставка — це подія з побічним ефектом** (запит, можлива відмова «ставку перебили», мережа). Intent → Reducer → Effect добре лягає на цю логіку.
3. **Живі дані** (SignalR / WebSocket для аукціону .NET) — це потік подій, що добре вписується в однонаправлений потік даних MVI.
4. Тестованість: reducer — чиста функція, яку легко перевіряти у `commonTest` без UI.
5. Збереження стану при зміні конфігурації (поворот екрана на Android) вирішує ViewModel.

MVI не обов'язковий: **MVVM з єдиним `StateFlow<UiState>` + методами `onXxx()` — легша версія**, яка теж підходить. MVI додає суворість: усі зміни йдуть через один `Intent` і один `reduce`.

### 4.4 Рекомендована цільова архітектура

```
ui (Compose)          ->  Intent  ->  ViewModel (MVI store)  ->  UseCase / Repository
   ▲                                      │                          │
   └──────── UiState (StateFlow) ◄────────┘                          ▼
                       Effect (Channel/SharedFlow) -> одноразові події   data: Ktor client -> .NET API
                                                                         + DTO -> domain mapper
```

Мінімальний приклад для каталогу:

```kotlin
// domain
data class Lot(...)

// presentation
data class CatalogState(
    val query: String = "",
    val chip: Int = 0,
    val checked: Set<String> = emptySet(),
    val lots: List<Lot> = emptyList(),
    val loading: Boolean = false,
    val error: String? = null,
)

sealed interface CatalogIntent {
    data class QueryChanged(val value: String) : CatalogIntent
    data class ChipSelected(val index: Int) : CatalogIntent
    data object Refresh : CatalogIntent
    data class OpenLot(val id: String) : CatalogIntent
}

sealed interface CatalogEffect {
    data class NavigateToLot(val id: String) : CatalogEffect
}

class CatalogViewModel(private val repo: LotRepository) : ViewModel() {
    private val _state = MutableStateFlow(CatalogState())
    val state: StateFlow<CatalogState> = _state.asStateFlow()
    private val _effects = Channel<CatalogEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    fun onIntent(i: CatalogIntent) = when (i) {
        is CatalogIntent.QueryChanged -> _state.update { it.copy(query = i.value) }
        is CatalogIntent.OpenLot -> viewModelScope.launch { _effects.send(CatalogEffect.NavigateToLot(i.id)) }
        ...
    }
}
```

У composable: `val state by viewModel.state.collectAsStateWithLifecycle()` — саме для цього вже підключена `lifecycle-runtime-compose`.

### 4.5 Що знадобиться під .NET API

| Потреба | Інструмент (KMP) | Примітка |
|---|---|---|
| HTTP-клієнт | **Ktor Client** (`ktor-client-core`, движки: OkHttp для Android, Darwin для iOS, CIO/Java для JVM) | `expect/actual` для движка не потрібен, якщо Ktor тягне движок по платформах |
| JSON | **kotlinx.serialization** (`@Serializable` DTO) | .NET за замовчуванням віддає camelCase — це зручно |
| DI | **Koin** (мультиплатформний) | замінить `remember { AppSettings() }` та `SampleData` |
| Токени / автентифікація | Ktor `Auth` plugin (Bearer), токен у settings (краще в secure storage) | `multiplatform-settings` не шифрує дані |
| Реальний час | WebSocket (Ktor) або SignalR-клієнт | .NET часто використовує SignalR — для KMP готового офіційного клієнта немає, варіант — WebSocket-ендпоінт |
| Навігація | Navigation Compose для KMP, Decompose або Voyager | поточний ручний стек вистачить на старті, але «Назад» на Android треба підключити |
| Зображення | **Coil 3** (`coil-compose`, `coil-network-ktor`) | заміна плейсхолдерам `painter` у `GoldFrame` |
| Контракт API | OpenAPI/Swagger із .NET -> генерація клієнта (openapi-generator `kotlin` multiplatform) | зменшує розбіжності DTO |
| Помилки | `Result`/власний `sealed class` | мережа, 401, 409 (ставку перебили) |

Особливості, на які варто звернути увагу при зв'язці з .NET:
- Дата/час: .NET віддає ISO-8601 (`DateTimeOffset`) — використати `kotlinx-datetime`; тоді `minutesLeft` рахується від `endsAt - now`, а не зберігається числом.
- Гроші: у моделях зараз `Int`. Для API краще `Long` (копійки) або `decimal` як рядок, щоб не втратити точність.
- Локалізація контенту: назви лотів зараз — ключі ресурсів (`StringResource`). З API прийдуть готові рядки, тому `Lot.title` стане `String` (або мапою мов). Це зачепить `Models.kt`, `LotCard`, `Catalog` пошук тощо — потрібен прошарок domain/UI-моделей.

### 4.6 План переходу (від меншого до більшого)

1. Винести `Lot`, `Category` тощо в `domain`, `SampleData` сховати за інтерфейсом `LotRepository` (`FakeLotRepository` — поточні дані).
2. Створити `ViewModel` + `UiState` для каталогу, потім лота, кабінету, головної. Підключити Koin.
3. `rememberSaveable`/ViewModel для навігаційного стека та фільтрів.
4. Підключити `BackHandler` (Android) / обробку Esc (desktop).
5. Додати Ktor + serialization + `RemoteLotRepository`, перемкнути в DI.
6. Екрани завантаження/помилки/порожнього списку.
7. Живі оновлення ставок.

---

## 5. Словник конструкцій Kotlin / Compose

### Kotlin
| Конструкція | Значення в проєкті |
|---|---|
| `data class` | модель із `copy`, `equals`, `toString` (`Lot`, `OnboardingPage`) |
| `object` / `data object` | синглтон (`SampleData`, `Noir`, `Route.Home`) |
| `sealed interface` | закрита ієрархія, вичерпний `when` (`Route`) |
| `enum class` з полями | `SlideFrame`, `AccountTab` |
| `expect` / `actual` | платформні реалізації (`applyAppLanguage`) |
| `companion object` | статичні константи (`KEY_LANGUAGE`) |
| Значення параметрів за замовчуванням | `modifier: Modifier = Modifier`, `plateHeight: Dp = 170.dp` |
| Лямбда з приймачем | `Modifier.drawBehind { ... }` (`this` = `DrawScope`) |
| Розширення | `fun Modifier.wallpaper()`, `Int.pad2()`, `DrawScope.drawLattice` |
| Деструктуризація | `{ (name, _) -> ...}`, `(v, l) ->` |
| `by` делегат | `var x by remember { mutableStateOf(..) }` через `getValue/setValue` |
| `?.let`, `?:`, `ifEmpty` | безпечна робота з null |
| `const val`, `private val` на рівні файлу | константи без класу (`BidStep`, `TotalLots`) |
| Посилання на функцію `::open`, `Char::isDigit` | функція як значення |

### Compose
| Конструкція | Значення |
|---|---|
| `@Composable` | функція опису UI; може викликати інші composable |
| `@Preview` | попередній перегляд у IDE (`App`) |
| `remember { }` | пам'ятає значення між рекомпозиціями |
| `remember(key)` | перераховує значення, коли змінився ключ (`LotScreen` — `bid`) |
| `mutableStateOf` / `mutableStateListOf` | спостережуваний стан; зміна запускає рекомпозицію |
| `LaunchedEffect(key)` | корутина, прив'язана до композиції; перезапуск при зміні ключа |
| `rememberCoroutineScope()` | scope для запусків із обробників подій |
| `key(x) { }` | скидання піддерева при зміні `x` |
| `CompositionLocalProvider` | неявна передача значення вниз по дереву (`LocalContentColor`) |
| `Modifier` | ланцюжок оформлення/поведінки; **порядок важливий** |
| `Row` / `Column` / `Box` / `BoxWithConstraints` | базові контейнери |
| `weight(1f)`, `fillMaxWidth()`, `widthIn(max=)`, `matchParentSize()` | розміри |
| `Arrangement`, `Alignment` | розташування дітей |
| `LazyRow`, `LazyVerticalGrid`, `items(list, key=)` | ліниві списки/сітки |
| `HorizontalPager` | сторінки зі свайпом |
| `animateDpAsState` | плавна анімація значення |
| `clickable(role=)` | натискання + семантика доступності |
| `Canvas`, `drawBehind`, `Brush`, `drawLine/drawRect` | малювання |
| `stringResource`, `Font` | ресурси Compose Resources |
| State hoisting | стан піднято вгору, вниз — значення й колбеки |

---

## 6. Помітні особливості та потенційні проблеми

1. **Онбординг показується щоразу.** `var showOnboarding by remember { mutableStateOf(true) }` не зберігається, хоча README каже «показується один раз». Щоб відповідати README, потрібен прапорець `onboardingDone` у `AppSettings`.
2. **Зміна мови скидає навігацію.** `key(language)` обгортає `MainScreen`, тому стек маршрутів і стан фільтрів скидаються на Home.
3. **Side effect в `remember`.** `applyAppLanguage(it)` викликається всередині `remember { ... }` під час композиції. Це працює, але краще робити до `setContent` або в `LaunchedEffect`/`SideEffect`.
4. **Системне «Назад» не обробляється** (Android/desktop).
5. **Фільтр у `CatalogScreen` рахується при кожній рекомпозиції**, використовує `stringResource` для пошуку, а блок «ціна» та кнопка «Застосувати» — заглушки.
6. **Залежності `viewmodel-compose` і `runtime-compose` підключені, але не використовуються** — це готова основа для розділу 4.
7. `Format.kt` змішує чисте форматування (`groupDigits`, `formatPrice`) і composable-форматування; чисті функції зручніше винести окремо й покрити тестами в `commonTest`.
8. Тести (`SharedCommonTest` тощо) — шаблонні, реальної перевірки логіки немає.
9. Поле `Lot.label` («1796», «1921») використовується як рік; семантика неочевидна — при переході на API краще окреме поле `year`.
10. У `README.md` розділи про Lab 4 і дизайн «Нуар» змішані; якщо викладач перевіряє лише Lab 4, корисно розділити їх, як у цьому гайді (розділи 2 і 3).

---

## 7. Команди

```bash
./gradlew :androidApp:assembleDebug        # Android
./gradlew :desktopApp:run                  # Desktop
./gradlew :desktopApp:hotRun --auto        # Desktop з hot reload
./gradlew :shared:jvmTest                  # тести common + JVM
./gradlew :shared:testAndroidHostTest      # Android host-тести
./gradlew :shared:iosSimulatorArm64Test    # iOS (потрібен macOS)
```

Версії залежностей — `gradle/libs.versions.toml`. iOS-збірка — лише з Xcode на macOS.
