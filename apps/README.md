# apps/

APK тестируемого приложения для каждой платформы.

## Структура

- `android/app-debug.apk` — debug-сборка pit-stop.kz, на которую таргетятся тесты

Путь прописан в `src/test/resources/android.properties` параметром `app.path`. При прогоне Appium ставит APK на эмулятор (переустанавливает при `fullReset=true`).

## Обновление APK

Скинуть новый `app-debug.apk` поверх старого. `BaseTest` форс-удаляет приложение через `adb uninstall` перед каждым тестом, так что переход на новую сборку безболезненный.
