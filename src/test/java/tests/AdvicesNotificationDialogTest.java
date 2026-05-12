package tests;

import core.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.AdvicesNotificationDialog;
import pages.AdvicesPage;
import pages.LanguageSelectionPage;
import pages.LanguageSelectionPage.Language;
import pages.MainScreenPage;

/**
 * Тесты диалога-промта, который показывается при первом тапе на "Советы":
 * проверяет содержимое сообщения, наличие кнопок ДА/НЕТ и оба перехода на экран Советы.
 */
public class AdvicesNotificationDialogTest extends BaseTest {

    private AdvicesNotificationDialog dialog;

    @BeforeMethod(alwaysRun = true)
    public void openAdvicesDialog() {
        new LanguageSelectionPage(driver).selectLanguage(Language.RUSSIAN);
        MainScreenPage main = new MainScreenPage(driver);
        Assert.assertTrue(main.isDisplayed(), "Main screen must be open before opening Advices");

        dialog = main.tapAdvices();
        Assert.assertTrue(dialog.isShown(),
                "Advices notification dialog must be shown after tapping Советы");
    }

    @Test(description = "Dialog message mentions lawyer advice and new-notifications offer")
    public void messageMentionsAdvicesAndNotifications() {
        Assert.assertTrue(dialog.hasText("Советы Юристов"),
                "Message should mention 'Советы Юристов'");
        Assert.assertTrue(dialog.hasText("уведомления о новых Советах"),
                "Message should mention 'уведомления о новых Советах'");
    }

    @Test(description = "Dialog has both action buttons enabled with correct labels")
    public void bothButtonsAreShown() {
        Assert.assertTrue(dialog.yesButton().isDisplayed(), "Yes button should be visible");
        Assert.assertTrue(dialog.noButton().isDisplayed(), "No button should be visible");

        Assert.assertEquals(dialog.yesButton().getText(), "ДА");
        Assert.assertEquals(dialog.noButton().getText(), "НЕТ");

        Assert.assertTrue(dialog.yesButton().isEnabled(), "Yes button should be enabled");
        Assert.assertTrue(dialog.noButton().isEnabled(), "No button should be enabled");
    }

    @Test(description = "Tapping ДА opens the Advices screen")
    public void tappingYesOpensAdvicesPage() {
        AdvicesPage page = dialog.tapYes();
        Assert.assertTrue(page.isDisplayed(),
                "Advices page should appear after tapping ДА");
    }

    @Test(description = "Tapping НЕТ opens the Advices screen")
    public void tappingNoOpensAdvicesPage() {
        AdvicesPage page = dialog.tapNo();
        Assert.assertTrue(page.isDisplayed(),
                "Advices page should appear after tapping НЕТ");
    }
}
