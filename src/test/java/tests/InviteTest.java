package tests;

import core.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.openqa.selenium.WebElement;
import pages.InvitePage;
import pages.LanguageSelectionPage;
import pages.LanguageSelectionPage.Language;
import pages.MainScreenPage;
import pages.PromoCodeDialog;

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

    @Test(description = "Tapping 'ПРИГЛАСИТЬ ДРУЗЕЙ' launches the system share resolver")
    public void tappingInviteLaunchesShareIntent() {
        String ourPkg = "kz.crystalspring.pit_stop_kz";
        invitePage.inviteButton().click();

        long deadline = System.currentTimeMillis() + 15_000;
        String currentPkg = ourPkg;
        while (System.currentTimeMillis() < deadline) {
            currentPkg = driver.getCurrentPackage();
            if (currentPkg != null && !currentPkg.equals(ourPkg)) break;
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        Assert.assertNotEquals(currentPkg, ourPkg,
                "After tapping 'ПРИГЛАСИТЬ ДРУЗЕЙ' current package should leave '" + ourPkg
                        + "' (share intent) but was '" + currentPkg + "'");
    }

    @Test(description = "Tapping 'АКТИВИРОВАТЬ ДРУГОЙ ПРОМО-КОД' opens the promo code dialog")
    public void tappingActivatePromoOpensDialog() {
        PromoCodeDialog dialog = invitePage.tapActivatePromo();
        Assert.assertTrue(dialog.isShown(),
                "Promo code dialog should appear after tapping 'АКТИВИРОВАТЬ ДРУГОЙ ПРОМО-КОД'");
    }

    @Test(description = "Promo code dialog has title, input field and two buttons with correct labels")
    public void promoDialogHasInputAndButtons() {
        PromoCodeDialog dialog = invitePage.tapActivatePromo();
        Assert.assertTrue(dialog.isShown(), "Promo code dialog must be shown");

        Assert.assertTrue(dialog.hasText(PromoCodeDialog.TITLE_TEXT),
                "Dialog should show title '" + PromoCodeDialog.TITLE_TEXT + "'");
        Assert.assertEquals(dialog.getInputHint(), PromoCodeDialog.INPUT_HINT,
                "Input hint should be '" + PromoCodeDialog.INPUT_HINT + "'");

        WebElement cancel = dialog.cancelButton();
        WebElement submit = dialog.submitButton();
        Assert.assertEquals(cancel.getText(), "ОТМЕНА");
        Assert.assertEquals(submit.getText(), "ОТПРАВИТЬ");
        Assert.assertTrue(cancel.isEnabled(), "Cancel button should be enabled");
        Assert.assertTrue(submit.isEnabled(), "Submit button should be enabled");
    }

    @Test(description = "Typing a code into the dialog input stores the entered text")
    public void canTypeCodeIntoDialog() {
        PromoCodeDialog dialog = invitePage.tapActivatePromo();
        Assert.assertTrue(dialog.isShown(), "Promo code dialog must be shown");

        String code = "TEST123";
        dialog.typeCode(code);
        Assert.assertEquals(dialog.getInputText(), code,
                "Input should contain the typed code '" + code + "'");
    }

    @Test(description = "Tapping ОТМЕНА on the promo dialog returns to the Invite screen")
    public void tappingCancelOnPromoDialogReturnsToInvite() {
        PromoCodeDialog dialog = invitePage.tapActivatePromo();
        Assert.assertTrue(dialog.isShown(), "Promo code dialog must be shown");

        InvitePage invite = dialog.tapCancel();
        Assert.assertTrue(invite.isDisplayed(),
                "Invite screen should appear after tapping ОТМЕНА on the promo dialog");
    }
}
