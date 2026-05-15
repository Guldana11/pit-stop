# inspector-dumps/

XML-снимки экранов приложения, снятые через Appium Inspector. Используются как референс при написании page-объектов: по дампу понятно какие `resource-id` и тексты есть на экране.

## Соглашения

- Имена в **camelCase**, по имени соответствующего page-объекта (без суффикса `Page`/`Dialog`):
  - `mainScreen.xml` ↔ `MainScreenPage.java`
  - `testingPaywall.xml` ↔ `TestingPaywallDialog.java`
  - `articlePage.xml` ↔ `ArticlePage.java`
- Несколько состояний одного экрана — суффикс через camelCase:
  - `advicesNotificationsOff.xml` — экран Советы после отключения уведомлений
  - `articlePage1.xml` — другая статья (сравнить идентичность структуры)

## Не page-объекты

Некоторые дампы документируют системные экраны или внешние intent'ы:
- `share.xml` — системный share-resolver (package `android`), не наше приложение
- `pushMe.xml` — экран вопроса теста после второго тапа на PushMe (соответствует `QuestionPage`)

## Известные нестыковки

`advices.xml` и `advicesNotificationDialog.xml` исторически поменяны местами:
- `advices.xml` (31 строка) — на самом деле содержит **диалог** уведомлений
- `advicesNotificationDialog.xml` (158 строк) — на самом деле содержит **AdvicesPage**

Тесты от этого не страдают — это только наименование. Может быть переименовано в отдельный chore-коммит.

## Когда обновлять

При добавлении нового экрана / диалога в page-объекты. Снять через Appium Inspector → сохранить XML с осмысленным именем. Дамп нужен только для документации, в рантайме тестов не читается.
