package tests;

import core.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.InvitePage;
import pages.LanguageSelectionPage;
import pages.LanguageSelectionPage.Language;
import pages.MainScreenPage;

/**
 * Тесты экрана "Хочешь бесплатный доступ?" (открывается с главного по btnInviteFriend):
 * проверяет переход, наличие заголовка/описаний, формат промо-кода и наличие кнопок.
 */
public class InviteTest extends BaseTest {

    private InvitePage invitePage;

    @BeforeMethod(alwaysRun = true)
    public void openInviteScreen() {
        new LanguageSelectionPage(driver).selectLanguage(Language.RUSSIAN);
        MainScreenPage main = new MainScreenPage(driver);
        Assert.assertTrue(main.isDisplayed(), "Main screen must be open before opening Invite");

        invitePage = main.tapInviteFriend();
        Assert.assertTrue(invitePage.isDisplayed(),
                "Invite screen must be open before each Invite test");
    }

    @Test(description = "Invite screen shows title and both description blocks")
    public void titleAndDescriptionsAreShown() {
        Assert.assertEquals(invitePage.title().getText(), "Хочешь бесплатный доступ?",
                "Title should be 'Хочешь бесплатный доступ?'");

        String inviteDesc = invitePage.inviteDescription().getText();
        Assert.assertTrue(inviteDesc.contains("1 месяц"),
                "Invite description should mention '1 месяц' but was '" + inviteDesc + "'");

        String friendsDesc = invitePage.friendsDescription().getText();
        Assert.assertTrue(friendsDesc.contains("3 дня"),
                "Friends description should mention '3 дня' but was '" + friendsDesc + "'");
    }

    @Test(description = "Promo code label is shown along with a non-empty code")
    public void promoCodeIsShown() {
        Assert.assertTrue(invitePage.hasText("Ваш промо-код:"),
                "'Ваш промо-код:' label should be visible");

        String code = invitePage.getInviteCode();
        Assert.assertNotNull(code, "Invite code should not be null");
        Assert.assertFalse(code.isBlank(), "Invite code should not be blank");
        Assert.assertTrue(code.matches("[A-Z0-9]+"),
                "Invite code should contain only uppercase letters/digits but was '" + code + "'");
    }

    @Test(description = "Both action buttons are visible with correct labels and enabled")
    public void actionButtonsAreShown() {
        Assert.assertTrue(invitePage.inviteButton().isDisplayed(), "Invite button should be visible");
        Assert.assertTrue(invitePage.activatePromoButton().isDisplayed(),
                "Activate promo button should be visible");

        Assert.assertEquals(invitePage.inviteButton().getText(), "ПРИГЛАСИТЬ ДРУЗЕЙ");
        Assert.assertEquals(invitePage.activatePromoButton().getText(),
                "АКТИВИРОВАТЬ ДРУГОЙ ПРОМО-КОД");

        Assert.assertTrue(invitePage.inviteButton().isEnabled(), "Invite button should be enabled");
        Assert.assertTrue(invitePage.activatePromoButton().isEnabled(),
                "Activate promo button should be enabled");
    }
}
