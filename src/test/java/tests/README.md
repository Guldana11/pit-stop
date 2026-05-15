# tests/

Тестовые классы (TestNG). Один класс — один экран или один cross-cutting флоу.

## Соглашения

- Все наследуют `core.BaseTest` (драйвер создаётся в `@BeforeMethod`).
- `@BeforeMethod` в каждом классе обычно идёт цепочка: `selectLanguage(RUSSIAN)` → tap до нужного экрана → `assertTrue(page.isDisplayed())`.
- Имена тестов в `lowerCamelCase`, описательные. Каждый `@Test` несёт `description = "..."` для отчёта.
- Assertions на TestNG (`org.testng.Assert`).
- Артефакты падений сохраняет родительский `BaseTest`, тут руками ничего не нужно.

## Текущий ростер (91 кейс / 18 классов)

| Класс | Кейсов | Что покрывает |
|---|---|---|
| `SmokeTest` | 1 | приложение в принципе запускается |
| `LanguageSelectionTest` | 6 | стартовый экран выбора языка (русский + казахский) |
| `KazakhLocalizationTest` | 4 | казахская локализация на Main/Settings (проверка лейблов) |
| `MainScreenTest` | 4 | главный экран — все кнопки и текста |
| `SettingsTest` | 12 | настройки полностью (все 6 кнопок, intent'ы, inline-пикер языка) |
| `InviteTest` | 8 | Invite экран + диалог промо-кода |
| `RulesTest` | 6 | Rules-список + переключение табов |
| `RuleChapterPageTest` | 4 | содержимое главы ПДД |
| `RulesSearchPageTest` | 4 | экран поиска по правилам |
| `TestingPaywallTest` | 4 | paywall-диалог (ОТМЕНА / ПРИГЛАСИТЬ) |
| `TestingPageTest` | 5 | экран Тестирование (после двойного тапа) |
| `AdvicesNotificationDialogTest` | 4 | промт уведомлений Советов |
| `AdvicesTest` | 6 | экран Советы + toggle уведомлений |
| `ArticlePageTest` | 5 | статья из Советов (paywall-gated) |
| `ProfileTest` | 7 | форма Профиль (поля, чекбоксы, ввод) |
| `BackNavigationTest` | 5 | cross-cutting BACK-навигация из 5 экранов |
| `PushMeTest` | 2 | центральная кнопка PushMe → тот же paywall |
| `QuestionPageTest` | 4 | экран активного вопроса теста |

## Когда добавлять тест

Для нового экрана / нового пути перехода / новой регрессии. Структуру брать с похожего класса (например, `SettingsTest` как образец «экрана»; `TestingPaywallTest` как образец «диалога»; `BackNavigationTest` как образец cross-cutting).

Добавил класс — не забудь:
1. Прописать `<class name="tests.NewTest"/>` в соответствующем `suites/androidPart*.xml`.
2. Обновить комментарий-счётчик в suite-файле.
