package tests;

import core.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.LanguageSelectionPage;
import pages.LanguageSelectionPage.Language;
import pages.MainScreenPage;
import pages.SettingsPage;

/**
 * Тесты казахской локализации: после выбора ҚАЗАҚША проверяет, что главный экран,
 * настройки и метка версии содержат казахские тексты, а русские (ПРАВИЛА / Версия: и т.д.)
 * на тех же экранах отсутствуют.
 */
public class KazakhLocalizationTest extends BaseTest {

    private MainScreenPage main;

    @BeforeMethod(alwaysRun = true)
    public void selectKazakh() {
        new LanguageSelectionPage(driver).selectLanguage(Language.KAZAKH);
        main = new MainScreenPage(driver);
        Assert.assertTrue(main.isDisplayed(), "Main screen must be open after selecting Kazakh");
    }

    @Test(description = "Main screen central buttons have Kazakh labels (ЕРЕЖЕЛЕР / Кеңестер / Тестілеу)")
    public void mainScreenLabelsAreKazakh() {
        Assert.assertTrue(main.hasText("ЕРЕЖЕЛЕР"),
                "Main screen should show Kazakh label 'ЕРЕЖЕЛЕР' (rules)");
        Assert.assertTrue(main.hasText("Кеңестер"),
                "Main screen should show Kazakh label 'Кеңестер' (advices)");
        Assert.assertTrue(main.hasText("Тестілеу"),
                "Main screen should show Kazakh label 'Тестілеу' (testing)");

        Assert.assertFalse(main.hasText("ПРАВИЛА"),
                "Main screen should NOT show Russian label 'ПРАВИЛА'");
        Assert.assertFalse(main.hasText("ТЕСТИРОВАНИЕ"),
                "Main screen should NOT show Russian label 'ТЕСТИРОВАНИЕ'");
    }

    @Test(description = "Settings buttons have Kazakh labels (БАҒАЛАУ / БӨЛІСУ / ТЕГІН КІРУ / ТІЛДІ ТАҢДАҢЫЗ)")
    public void settingsButtonsHaveKazakhLabels() {
        SettingsPage settings = main.tapSettings();
        Assert.assertTrue(settings.isDisplayed(), "Settings should open from main screen");

        Assert.assertEquals(settings.rateAppButton().getText(), "БАҒАЛАУ");
        Assert.assertEquals(settings.shareAppButton().getText(), "БӨЛІСУ");
        Assert.assertEquals(settings.freeAccessButton().getText(), "ТЕГІН КІРУ");
        Assert.assertEquals(settings.wantPayOtherButton().getText(),
                "ТӨЛЕЙ АЛМАЙМЫН/БАСҚА ТӨЛЕМ ӘДІСІН ҚАЛАЙМЫН");
        Assert.assertEquals(settings.feedbackButton().getText(), "ЕСКЕРТУЛЕР/ҰСЫНЫСТАР");
        Assert.assertEquals(settings.selectLanguageButton().getText(), "ТІЛДІ ТАҢДАҢЫЗ");
    }

    @Test(description = "Version label uses Kazakh prefix 'Нұсқа:' (not Russian 'Версия:')")
    public void settingsVersionLabelIsKazakh() {
        SettingsPage settings = main.tapSettings();
        Assert.assertTrue(settings.isDisplayed(), "Settings should open from main screen");

        String version = settings.getVersion();
        Assert.assertTrue(version.startsWith("Нұсқа:"),
                "Version label should start with Kazakh 'Нұсқа:' but was '" + version + "'");
        Assert.assertFalse(version.startsWith("Версия:"),
                "Version label should NOT start with Russian 'Версия:'");
        Assert.assertTrue(version.matches("Нұсқа: \\d+\\.\\d+\\.\\d+ \\(\\d+\\)"),
                "Version should match 'Нұсқа: X.Y.Z (N)' but was '" + version + "'");
    }

    @Test(description = "Settings description is in Kazakh (mentions 'ҚР ЖҚЕ')")
    public void settingsDescriptionIsInKazakh() {
        SettingsPage settings = main.tapSettings();
        Assert.assertTrue(settings.isDisplayed(), "Settings should open from main screen");

        // UiAutomator-селектор плохо переваривает казахскую букву Қ (U+049A) в textContains.
        // Проверяем по page source напрямую — там русские/казахские строки лежат как есть.
        String pageSource = driver.getPageSource();
        Assert.assertTrue(pageSource.contains("ҚР ЖҚЕ"),
                "Settings page source should mention 'ҚР ЖҚЕ' (Kazakh abbreviation for ПДД РК)");
        Assert.assertFalse(pageSource.contains("Актуальный справочник по ПДД РК"),
                "Settings should NOT show the Russian description text");
    }
}
