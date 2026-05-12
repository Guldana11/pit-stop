package tests;

import core.BaseTest;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.LanguageSelectionPage;
import pages.LanguageSelectionPage.Language;
import pages.MainScreenPage;
import pages.TestingPage;
import pages.TestingPaywallDialog;

/**
 * Тесты экрана "Тестирование" (открывается после дисмисса paywall):
 * проверяет счётчик бесплатных тестов и наличие всех 7 кнопок (режимы,
 * сложности, КУПИТЬ / БЕСПЛАТНО) с правильными текстами.
 */
public class TestingPageTest extends BaseTest {

    private TestingPage testing;

    @BeforeMethod(alwaysRun = true)
    public void openTestingPage() {
        new LanguageSelectionPage(driver).selectLanguage(Language.RUSSIAN);
        MainScreenPage main = new MainScreenPage(driver);
        Assert.assertTrue(main.isDisplayed(), "Main screen must be open");

        TestingPaywallDialog paywall = main.tapQuestions();
        Assert.assertTrue(paywall.isShown(), "Testing paywall must be shown");

        testing = paywall.tapCancel();
        Assert.assertTrue(testing.isDisplayed(),
                "Testing page must be open before each TestingPage test");
    }

    @Test(description = "Free tests counter is shown in expected format")
    public void freeTestsCounterIsShown() {
        String text = testing.getTestNumberText();
        Assert.assertNotNull(text, "Counter should not be null");
        Assert.assertTrue(text.matches("Бесплатных тестов: \\d+"),
                "Counter should match 'Бесплатных тестов: N' but was '" + text + "'");
    }

    @Test(description = "Mode buttons (РЕЖИМ ОБУЧЕНИЯ / РЕЖИМ ЭКЗАМЕНА) are shown")
    public void modeButtonsAreShown() {
        Assert.assertEquals(testing.learnButton().getText(), "РЕЖИМ ОБУЧЕНИЯ");
        Assert.assertEquals(testing.examButton().getText(), "РЕЖИМ ЭКЗАМЕНА");
        Assert.assertTrue(testing.learnButton().isEnabled(), "Learn button should be enabled");
        Assert.assertTrue(testing.examButton().isEnabled(), "Exam button should be enabled");
    }

    @Test(description = "Difficulty buttons (СЛОЖНЫЕ / ПРОСТЫЕ / СПОРНЫЕ) are shown")
    public void difficultyButtonsAreShown() {
        Assert.assertEquals(testing.hardButton().getText(), "САМЫЕ СЛОЖНЫЕ");
        Assert.assertEquals(testing.easyButton().getText(), "САМЫЕ ПРОСТЫЕ");
        Assert.assertEquals(testing.disputeButton().getText(), "САМЫЕ СПОРНЫЕ");
        Assert.assertTrue(testing.hardButton().isEnabled(), "Hard button should be enabled");
        Assert.assertTrue(testing.easyButton().isEnabled(), "Easy button should be enabled");
        Assert.assertTrue(testing.disputeButton().isEnabled(), "Dispute button should be enabled");
    }

    @Test(description = "Buy and Free action buttons are shown with correct labels")
    public void buyAndFreeButtonsAreShown() {
        Assert.assertEquals(testing.buyButton().getText(), "КУПИТЬ");
        Assert.assertEquals(testing.freeButton().getText(), "БЕСПЛАТНО");
        Assert.assertTrue(testing.buyButton().isEnabled(), "Buy button should be enabled");
        Assert.assertTrue(testing.freeButton().isEnabled(), "Free button should be enabled");
    }

    @Test(description = "All testing-page buttons are visible at once")
    public void allButtonsAreVisible() {
        WebElement[] buttons = {
                testing.learnButton(),
                testing.examButton(),
                testing.hardButton(),
                testing.easyButton(),
                testing.disputeButton(),
                testing.buyButton(),
                testing.freeButton(),
        };
        for (WebElement btn : buttons) {
            Assert.assertTrue(btn.isDisplayed(),
                    "Button should be visible: " + btn.getAttribute("resource-id"));
        }
    }
}
