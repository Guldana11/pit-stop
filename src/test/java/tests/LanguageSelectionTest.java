package tests;

import core.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.LanguageSelectionPage;
import pages.LanguageSelectionPage.Language;
import pages.MainScreenPage;

/**
 * Тесты экрана выбора языка (первый экран при первом запуске приложения):
 * проверяет наличие двух языков (Русский / Қазақша), их подписи и доступность.
 */
public class LanguageSelectionTest extends BaseTest {

    @Test(description = "Language selection screen is shown on first launch")
    public void languageScreenIsDisplayed() {
        LanguageSelectionPage page = new LanguageSelectionPage(driver);
        Assert.assertTrue(page.isDisplayed(),
                "Language selection screen with both buttons should be visible");
    }

    @Test(description = "Both language buttons are clickable")
    public void bothButtonsAreClickable() {
        LanguageSelectionPage page = new LanguageSelectionPage(driver);
        Assert.assertTrue(page.isDisplayed(), "Language screen should be visible");

        Assert.assertTrue(page.isButtonClickable(Language.RUSSIAN),
                "Russian button should be clickable");
        Assert.assertTrue(page.isButtonClickable(Language.KAZAKH),
                "Kazakh button should be clickable");
    }

    @Test(description = "Language buttons have correct labels")
    public void buttonLabelsAreCorrect() {
        LanguageSelectionPage page = new LanguageSelectionPage(driver);
        Assert.assertTrue(page.isDisplayed(), "Language screen should be visible");

        Assert.assertEquals(page.getButtonText(Language.RUSSIAN), Language.RUSSIAN.label,
                "Russian button should be labeled '" + Language.RUSSIAN.label + "'");
        Assert.assertEquals(page.getButtonText(Language.KAZAKH), Language.KAZAKH.label,
                "Kazakh button should be labeled '" + Language.KAZAKH.label + "'");
    }

    @Test(description = "Both language headers are present and non-empty")
    public void languageHeadersAreShown() {
        LanguageSelectionPage page = new LanguageSelectionPage(driver);
        Assert.assertTrue(page.isDisplayed(), "Language screen should be visible");

        String russianHeader = page.getRussianHeaderText();
        String qazaqHeader = page.getQazaqHeaderText();

        Assert.assertFalse(russianHeader == null || russianHeader.isBlank(),
                "Russian header should be present");
        Assert.assertFalse(qazaqHeader == null || qazaqHeader.isBlank(),
                "Qazaq header should be present");
    }

    @Test(description = "Selecting Russian opens the Main screen")
    public void selectingRussianOpensMainScreen() {
        LanguageSelectionPage langPage = new LanguageSelectionPage(driver);
        Assert.assertTrue(langPage.isDisplayed(), "Language screen should be visible before selecting");

        langPage.selectLanguage(Language.RUSSIAN);

        MainScreenPage mainScreen = new MainScreenPage(driver);
        Assert.assertTrue(mainScreen.isDisplayed(),
                "Main screen should appear after selecting a language");
    }

    @Test(description = "Selecting Kazakh opens the Main screen")
    public void selectingKazakhOpensMainScreen() {
        LanguageSelectionPage langPage = new LanguageSelectionPage(driver);
        Assert.assertTrue(langPage.isDisplayed(), "Language screen should be visible before selecting");

        langPage.selectLanguage(Language.KAZAKH);

        MainScreenPage mainScreen = new MainScreenPage(driver);
        Assert.assertTrue(mainScreen.isDisplayed(),
                "Main screen should appear after selecting Kazakh");
    }
}
