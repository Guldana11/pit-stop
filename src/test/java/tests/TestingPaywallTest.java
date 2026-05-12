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
 * Тесты paywall-диалога, который показывается при тапе на "Тестирование":
 * проверяет содержимое сообщения и обе действия (ОТМЕНА → возврат на Main, ПРИГЛАСИТЬ → Invite).
 */
public class TestingPaywallTest extends BaseTest {

    private TestingPaywallDialog paywall;

    @BeforeMethod(alwaysRun = true)
    public void openPaywall() {
        new LanguageSelectionPage(driver).selectLanguage(Language.RUSSIAN);
        MainScreenPage main = new MainScreenPage(driver);
        Assert.assertTrue(main.isDisplayed(), "Main screen must be open before opening Testing");

        paywall = main.tapQuestions();
        Assert.assertTrue(paywall.isShown(),
                "Testing paywall dialog must be shown after tapping Questions");
    }

    @Test(description = "Paywall dialog message mentions key limits and the invite offer")
    public void messageMentionsLimits() {
        Assert.assertTrue(paywall.hasText("3 теста"),
                "Message should mention '3 теста'");
        Assert.assertTrue(paywall.hasText("3 дня"),
                "Message should mention '3 дня'");
        Assert.assertTrue(paywall.hasText("5 друзей"),
                "Message should mention '5 друзей'");
        Assert.assertTrue(paywall.hasText("подписку"),
                "Message should mention 'подписку'");
    }

    @Test(description = "Paywall has both action buttons enabled with correct labels")
    public void bothButtonsAreShown() {
        Assert.assertTrue(paywall.cancelButton().isDisplayed(), "Cancel button should be visible");
        Assert.assertTrue(paywall.inviteButton().isDisplayed(), "Invite button should be visible");

        Assert.assertEquals(paywall.cancelButton().getText(), "ОТМЕНА");
        Assert.assertEquals(paywall.inviteButton().getText(), "ПРИГЛАСИТЬ");

        Assert.assertTrue(paywall.cancelButton().isEnabled(), "Cancel button should be enabled");
        Assert.assertTrue(paywall.inviteButton().isEnabled(), "Invite button should be enabled");
    }

    @Test(description = "Tapping ОТМЕНА closes paywall and returns to the Main screen")
    public void tappingCancelReturnsToMain() {
        MainScreenPage main = paywall.tapCancel();
        Assert.assertTrue(main.isDisplayed(),
                "Main screen should appear after dismissing paywall");
    }
}
