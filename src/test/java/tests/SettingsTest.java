package tests;

import core.BaseTest;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.LanguageSelectionPage;
import pages.LanguageSelectionPage.Language;
import pages.MainScreenPage;
import pages.SettingsPage;

/**
 * Тесты экрана "Настройки" (открывается с главного экрана по кнопке btnShowSettings):
 * проверяет переход, наличие версии, заголовка и всех кнопок (Оценить / Поделиться /
 * Бесплатный доступ / Замечания / Выберите язык / Способ оплаты).
 */
public class SettingsTest extends BaseTest {

    private SettingsPage settings;

    @BeforeMethod(alwaysRun = true)
    public void openSettings() {
        new LanguageSelectionPage(driver).selectLanguage(Language.RUSSIAN);
        MainScreenPage main = new MainScreenPage(driver);
        Assert.assertTrue(main.isDisplayed(), "Main screen must be open before opening Settings");

        settings = main.tapSettings();
        Assert.assertTrue(settings.isDisplayed(),
                "Settings screen must be open before each Settings test");
    }

    @Test(description = "Settings screen shows app title and description")
    public void titleAndDescriptionAreShown() {
        Assert.assertTrue(settings.hasText("Pit-Stop.kz"), "Title 'Pit-Stop.kz' should be visible");
        Assert.assertTrue(settings.hasText("Актуальный справочник по ПДД РК, а также тесты ПДД РК"),
                "App description should be visible");
    }

    @Test(description = "Version label is shown in expected format")
    public void versionLabelIsShown() {
        String version = settings.getVersion();
        Assert.assertNotNull(version, "Version label text should not be null");
        Assert.assertTrue(version.startsWith("Версия:"),
                "Version label should start with 'Версия:' but was '" + version + "'");
        Assert.assertTrue(version.matches("Версия: \\d+\\.\\d+\\.\\d+ \\(\\d+\\)"),
                "Version should match 'Версия: X.Y.Z (N)' but was '" + version + "'");
    }

    @Test(description = "All settings buttons are present with correct labels")
    public void allButtonsAreShownWithLabels() {
        Assert.assertTrue(settings.rateAppButton().isDisplayed(), "Rate app button should be visible");
        Assert.assertTrue(settings.shareAppButton().isDisplayed(), "Share app button should be visible");
        Assert.assertTrue(settings.freeAccessButton().isDisplayed(), "Free access button should be visible");
        Assert.assertTrue(settings.wantPayOtherButton().isDisplayed(), "Want pay other button should be visible");
        Assert.assertTrue(settings.feedbackButton().isDisplayed(), "Feedback button should be visible");
        Assert.assertTrue(settings.selectLanguageButton().isDisplayed(), "Select language button should be visible");

        Assert.assertEquals(settings.rateAppButton().getText(), "ОЦЕНИТЬ");
        Assert.assertEquals(settings.shareAppButton().getText(), "ПОДЕЛИТЬСЯ");
        Assert.assertEquals(settings.freeAccessButton().getText(), "БЕСПЛАТНЫЙ ДОСТУП");
        Assert.assertEquals(settings.wantPayOtherButton().getText(),
                "НЕ МОГУ ОПЛАТИТЬ/ХОЧУ ДРУГОЙ СПОСОБ ОПЛАТЫ");
        Assert.assertEquals(settings.feedbackButton().getText(), "ЗАМЕЧАНИЯ/ПРЕДЛОЖЕНИЯ");
        Assert.assertEquals(settings.selectLanguageButton().getText(), "ВЫБЕРИТЕ ЯЗЫК");
    }

    @Test(description = "All settings buttons are enabled (clickable)")
    public void allButtonsAreEnabled() {
        WebElement[] buttons = {
                settings.rateAppButton(),
                settings.shareAppButton(),
                settings.freeAccessButton(),
                settings.wantPayOtherButton(),
                settings.feedbackButton(),
                settings.selectLanguageButton(),
        };
        for (WebElement btn : buttons) {
            Assert.assertTrue(btn.isEnabled(),
                    "Button should be enabled: " + btn.getAttribute("resource-id"));
        }
    }
}
