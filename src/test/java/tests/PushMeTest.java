package tests;

import core.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.LanguageSelectionPage;
import pages.LanguageSelectionPage.Language;
import pages.MainScreenPage;
import pages.TestingPaywallDialog;

/**
 * Тесты центральной кнопки PushMe (btnPushMe) на главном экране: первый тап
 * показывает тот же paywall, что и Тестирование (ОТМЕНА / ПРИГЛАСИТЬ),
 * дисмисс возвращает на главный.
 */
public class PushMeTest extends BaseTest {

    private MainScreenPage main;

    @BeforeMethod(alwaysRun = true)
    public void openMain() {
        new LanguageSelectionPage(driver).selectLanguage(Language.RUSSIAN);
        main = new MainScreenPage(driver);
        Assert.assertTrue(main.isDisplayed(), "Main screen must be open before tapping PushMe");
    }

    @Test(description = "Tapping PushMe shows the same testing paywall dialog")
    public void tappingPushMeShowsPaywall() {
        TestingPaywallDialog paywall = main.tapPushMe();
        Assert.assertTrue(paywall.isShown(),
                "Paywall dialog should appear after tapping PushMe");
        Assert.assertTrue(paywall.hasText("3 теста"),
                "Paywall should mention '3 теста' (same content as Testing paywall)");
        Assert.assertTrue(paywall.hasText("5 друзей"),
                "Paywall should mention '5 друзей'");
    }

    @Test(description = "Tapping ОТМЕНА on PushMe paywall returns to Main")
    public void tappingCancelOnPushMePaywallReturnsToMain() {
        TestingPaywallDialog paywall = main.tapPushMe();
        Assert.assertTrue(paywall.isShown(), "Paywall must be shown first");

        MainScreenPage backToMain = paywall.tapCancel();
        Assert.assertTrue(backToMain.isDisplayed(),
                "Main screen should reappear after cancelling PushMe paywall");
    }
}
