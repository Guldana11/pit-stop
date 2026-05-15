# core/

Инфраструктура тестов: создание драйвера, базовый класс, retry, обработка системных диалогов.

## Файлы

| Класс | Зачем |
|---|---|
| `Config.java` | Singleton-обёртка над `android.properties` |
| `DriverFactory.java` | Создаёт `AndroidDriver` с `UiAutomator2Options`. Увеличенные adb/uiautomator2-таймауты под медленные эмуляторы. `waitForIdleTimeout=2000` — компромисс между «splash не успокаивается» и нулём (ANR от polling) |
| `BaseTest.java` | Абстрактный родитель всех тестов. `@BeforeMethod` принудительно `adb uninstall` приложения перед каждым тестом (Appium-овский `fullReset` один не справляется) + создание драйвера + дисмисс ANR. `@AfterMethod` при падении сохраняет PNG + XML в `build/test-artifacts/` |
| `RetryAnalyzer.java` | Повторяет упавший тест до 2 раз. На x86_64 эмуляторе типичные сбои — `Failed to create Android driver` и `Language selection screen did not appear` (cold cache). Real bugs упадут 3 раза одинаково, инфраструктурный шум проходит со 2-3 попытки |
| `RetryListener.java` | `IAnnotationTransformer`, автоматически вешает `RetryAnalyzer` на каждый `@Test` (не надо размечать руками). Подключён в `suites/android*.xml` через `<listener>` |
| `SystemDialogs.java` | Дисмисс ANR-диалога Android. `dismissAllAnrs(driver, n)` цикл — ANR на x86_64 переоткрывается несколько раз подряд после одного дисмисса |

## Артефакты падений

Хранятся в `build/test-artifacts/<TestClass>_<testName>_<YYYYMMDD_HHMMSS>.{png,xml}`. Очищаются автоматически (`cleanOldArtifacts` в `build.gradle.kts`) — файлы старше 3 дней удаляются перед каждым `test`.
