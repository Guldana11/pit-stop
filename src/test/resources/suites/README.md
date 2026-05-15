# suites/

TestNG-сьюты для разных запусков.

## Файлы

| Файл | Запуск | Кейсов | Время | Содержимое |
|---|---|---|---|---|
| `android.xml` | `./gradlew test` | 91 | ~2 часа | Полный сьют (все 18 классов). Нестабилен — UiAutomator2 устаёт после ~30 мин |
| `androidPart1a.xml` | `./gradlew testPart1a` | 34 | ~25 мин | Smoke / Language / Main / Settings / Profile / Kazakh |
| `androidPart1b.xml` | `./gradlew testPart1b` | 27 | ~40 мин | Invite / Rules / RuleChapter / RulesSearch / BACK |
| `androidPart2.xml` | `./gradlew testPart2` | 30 | ~30 мин | TestingPaywall / TestingPage / Advices*/Article / PushMe / Question |

## Почему сплит

Эмулятор x86_64 + UiAutomator2 деградируют после ~25-30 минут непрерывного тестирования: драйвер начинает падать с 100с+ таймаутами, language screen не появляется за 60с. **Между частями делать холодный ребут эмулятора** (`adb emu kill` + перезапустить).

Полный `android.xml` оставлен для случаев когда хочется одного зелёного отчёта подряд — но шанс довезти до конца на x86_64 эмуляторе невелик.

## Подключённые listener'ы

Все сьюты подключают `core.RetryListener` — он автоматически вешает `RetryAnalyzer` на каждый `@Test`. Retry = 2, т.е. упавший тест получает 3 попытки. См. `src/test/java/core/README.md`.

## Когда обновлять

При добавлении нового тестового класса — прописать `<class name="tests.NewTest"/>` в соответствующий part-сьют (выбрать по тематике / времени выполнения). Обновить счётчики в комментариях.
