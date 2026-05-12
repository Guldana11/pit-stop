package tests;

import core.BaseTest;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.LanguageSelectionPage;
import pages.LanguageSelectionPage.Language;
import pages.MainScreenPage;

/**
 * Тесты главного экрана pit-stop.kz (открывается после выбора языка):
 * проверяет видимость и доступность всех ключевых кнопок (Правила, Тестирование,
 * Советы, PushMe) + верхней и нижней панели (настройки, пригласить, профиль, инстаграм).
 */
public class MainScreenTest extends BaseTest {

    private MainScreenPage mainScreen;

    @BeforeMethod(alwaysRun = true)
    public void openMainScreen() {
        new LanguageSelectionPage(driver).selectLanguage(Language.RUSSIAN);
        mainScreen = new MainScreenPage(driver);
        Assert.assertTrue(mainScreen.isDisplayed(),
                "Main screen must be open before each MainScreen test");
    }

    @Test(description = "Main screen shows Rules / Questions / Advices buttons with labels")
    public void centralButtonsAreShown() {
        Assert.assertTrue(mainScreen.rulesButton().isDisplayed(), "Rules button should be visible");
        Assert.assertTrue(mainScreen.questionsButton().isDisplayed(), "Questions button should be visible");
        Assert.assertTrue(mainScreen.advicesButton().isDisplayed(), "Advices button should be visible");
        Assert.assertTrue(mainScreen.pushMeButton().isDisplayed(), "PushMe button should be visible");

        Assert.assertTrue(mainScreen.hasText("ПРАВИЛА"), "Label 'ПРАВИЛА' should be on screen");
        Assert.assertTrue(mainScreen.hasText("ТЕСТИРОВАНИЕ"), "Label 'ТЕСТИРОВАНИЕ' should be on screen");
        Assert.assertTrue(mainScreen.hasText("Советы"), "Label 'Советы' should be on screen");
    }

    @Test(description = "Main screen top bar has Settings and Invite buttons")
    public void topBarButtonsAreShown() {
        Assert.assertTrue(mainScreen.settingsButton().isDisplayed(), "Settings button should be visible");
        Assert.assertTrue(mainScreen.inviteButton().isDisplayed(), "Invite friend button should be visible");
    }

    @Test(description = "Main screen bottom bar has Profile and Instagram buttons")
    public void bottomBarButtonsAreShown() {
        Assert.assertTrue(mainScreen.profileButton().isDisplayed(), "Profile button should be visible");
        Assert.assertTrue(mainScreen.instagramButton().isDisplayed(), "Instagram button should be visible");
    }

    @Test(description = "All main screen buttons are enabled (clickable)")
    public void allButtonsAreEnabled() {
        WebElement[] buttons = {
                mainScreen.settingsButton(),
                mainScreen.inviteButton(),
                mainScreen.rulesButton(),
                mainScreen.pushMeButton(),
                mainScreen.questionsButton(),
                mainScreen.advicesButton(),
                mainScreen.profileButton(),
                mainScreen.instagramButton(),
        };
        for (WebElement btn : buttons) {
            Assert.assertTrue(btn.isEnabled(),
                    "Button should be enabled: " + btn.getAttribute("resource-id"));
        }
    }
}
