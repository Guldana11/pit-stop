# pages/

Page-объекты по паттерну Page Object Model — один класс на экран или диалог.

## Соглашения

- Все наследуют `BasePage` (инициализирует `AppiumFieldDecorator` с 10-секундным wait).
- Экраны (`*Page`) и диалоги (`*Dialog`) с одинаковым шаблоном:
  - публичные константы id (`PKG + ":id/..."`)
  - `isDisplayed()` — короткая обёртка над `waitForDisplayed(Duration)`
  - `waitForDisplayed(timeout)` — цикл до 3 раундов с `SystemDialogs.dismissAllAnrs` между ними (ANR на x86_64 переоткрывается)
  - `waitOnce(timeout)` — один проход `WebDriverWait` по двум-трём id'шникам, специфичным для экрана
  - геттеры `WebElement` по элементам + tap-методы, возвращающие следующий page-объект
- `hasText(String)` для пробных проверок — обычно через `AppiumBy.androidUIAutomator(textContains)`. Для казахских букв (например **Қ**) UiAutomator-селектор глючит → использовать `driver.getPageSource().contains(...)`

## Карта экранов

| Page-объект | Экран в приложении | Как попасть |
|---|---|---|
| `LanguageSelectionPage` | Выбор языка (первый запуск) | стартовый экран |
| `MainScreenPage` | Главный экран | после выбора языка |
| `SettingsPage` | Настройки | `main.tapSettings()` |
| `InvitePage` | «Хочешь бесплатный доступ?» | `main.tapInviteFriend()` или `settings.tapFreeAccess()` или `paywall.tapInvite()` |
| `PromoCodeDialog` | Диалог ввода промо-кода | `invite.tapActivatePromo()` |
| `RulesPage` | Правила (4 таба) | `main.tapRules()` |
| `RuleChapterPage` | Содержимое главы ПДД | `rules.tapChapter(title)` |
| `RulesSearchPage` | Поиск по правилам | `rules.tapSearch()` |
| `TestingPaywallDialog` | Paywall перед Тестированием | `main.tapQuestions()` или `main.tapPushMe()` |
| `TestingPage` | Экран Тестирование (выбор режима) | `main.tapQuestionsExpectingTestingPage()` (второй тап после дисмисса paywall) |
| `QuestionPage` | Экран вопроса теста | `main.tapPushMeExpectingQuestion()` (второй тап PushMe) |
| `AdvicesNotificationDialog` | Промт уведомлений Советов | `main.tapAdvices()` |
| `AdvicesPage` | Советы (список карточек) | `dialog.tapYes()` или `dialog.tapNo()` |
| `ArticlePage` | Статья из Советов (paywall-gated) | `advices.tapFirstArticle()` |
| `ProfilePage` | Профиль (форма ввода) | `main.tapProfile()` |
| `BasePage` | абстрактный родитель | — |

## Когда добавлять новый page-объект

При новом экране / диалоге в приложении. Снять дамп в `inspector-dumps/`, создать класс по существующему шаблону (см. `SettingsPage` как образец), добавить tap-метод в page, откуда экран открывается.
