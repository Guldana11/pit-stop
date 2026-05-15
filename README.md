# pit-stop

UI-автотесты для Android-приложения **pit-stop.kz** (справочник ПДД РК).

## Стек

- Java 21 (Gradle toolchain)
- [Appium](https://appium.io/) java-client 9.3 + UiAutomator2
- Selenium 4.25
- TestNG 7.10
- Allure-TestNG 2.29
- Gradle 8.13

## Структура

```
apps/android/             — APK тестируемого приложения
inspector-dumps/          — XML-снимки экранов из Appium Inspector (артефакты для page-объектов)
src/test/java/core/       — инфраструктура (BaseTest, DriverFactory, RetryAnalyzer, SystemDialogs, Config)
src/test/java/pages/      — page-объекты по экранам приложения
src/test/java/tests/      — тестовые классы (TestNG)
src/test/resources/
    android.properties    — capabilities Appium и параметры запуска
    suites/               — TestNG suite XMLs (полный + сплит 1A / 1B / 2)
```

## Запуск тестов

Полный прогон (~2 часа, нестабилен на длинной дистанции):
```bash
./gradlew test
```

Сплит на 3 части — **рекомендуется**, между частями делать холодный ребут эмулятора:
```bash
./gradlew testPart1a     # Smoke / Language / Main / Settings / Profile / Kazakh (~25 мин, 34 кейса)
adb emu kill && # перезапустить эмулятор
./gradlew testPart1b     # Invite / Rules / RuleChapter / RulesSearch / BACK (~40 мин, 27 кейсов)
adb emu kill && # перезапустить эмулятор
./gradlew testPart2      # Testing / Advices / Article / PushMe / Question (~30 мин, 30 кейсов)
```

Один класс / один кейс:
```bash
./gradlew test --tests "tests.SettingsTest"
./gradlew test --tests "tests.SettingsTest.tappingFreeAccessOpensInvite"
```

## Артефакты падений

При падении теста в `build/test-artifacts/` сохраняются `*.png` (скриншот) и `*.xml` (page source). Файлы старше 3 дней удаляются автоматически перед каждым прогоном.

## Allure-отчёт

После прогона в `allure-results/`. Генерация HTML — стандартными командами `allure serve allure-results/` (если установлен Allure CLI).
