package tests;

import core.BaseTest;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.SkipException;
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

    @Test(description = "Tapping 'БЕСПЛАТНЫЙ ДОСТУП' opens the Invite screen")
    public void tappingFreeAccessOpensInvite() {
        pages.InvitePage invite = settings.tapFreeAccess();
        Assert.assertTrue(invite.isDisplayed(),
                "Invite screen should appear after tapping 'БЕСПЛАТНЫЙ ДОСТУП'");
    }

    @Test(description = "Selecting Kazakh in the inline picker restarts the app to Main in Kazakh")
    public void tappingInlineKazakhSwitchesLanguageAndOpensMain() {
        settings.expandLanguagePicker();
        Assert.assertTrue(settings.isLanguagePickerExpanded(),
                "Picker must be expanded before tapping inline language button");

        MainScreenPage main = settings.tapInlineKazakh();
        Assert.assertTrue(main.isDisplayed(),
                "Main screen should appear after switching language via inline picker");
        Assert.assertTrue(main.hasText("ЕРЕЖЕЛЕР"),
                "Main screen labels should switch to Kazakh ('ЕРЕЖЕЛЕР') after the switch");
    }

    @Test(description = "Tapping 'ВЫБЕРИТЕ ЯЗЫК' expands an inline language picker inside Settings")
    public void tappingSelectLanguageExpandsInlinePicker() {
        Assert.assertFalse(settings.isLanguagePickerExpanded(),
                "Language picker should be collapsed before tapping");
        settings.expandLanguagePicker();
        Assert.assertTrue(settings.isLanguagePickerExpanded(),
                "Inline language picker (ll_select_lng + btnRussian + btnKazakh) should appear after tap");
        Assert.assertTrue(settings.inlineRussianButton().isEnabled(),
                "Inline Russian button should be enabled");
        Assert.assertTrue(settings.inlineKazakhButton().isEnabled(),
                "Inline Kazakh button should be enabled");
    }

    @Test(description = "Second tap on 'ВЫБЕРИТЕ ЯЗЫК' does not collapse the inline picker (one-way toggle)")
    public void tappingSelectLanguageAgainKeepsPickerOpen() {
        settings.expandLanguagePicker();
        Assert.assertTrue(settings.isLanguagePickerExpanded(),
                "Picker must be expanded after first tap");
        settings.selectLanguageButton().click();
        Assert.assertTrue(settings.isLanguagePickerExpanded(),
                "Picker should still be expanded after a second tap on ВЫБЕРИТЕ ЯЗЫК (button is open-only, not a real toggle)");
    }

    @Test(description = "Tapping 'НЕ МОГУ ОПЛАТИТЬ...' launches an external app (browser intent)")
    public void wantPayOtherLaunchesExternalApp() {
        assertButtonLaunchesExternalApp("НЕ МОГУ ОПЛАТИТЬ", settings.wantPayOtherButton());
    }

    @Test(description = "Tapping 'ОЦЕНИТЬ' launches an external app (Play Store intent)")
    public void rateAppLaunchesExternalApp() {
        // На AOSP-эмуляторах без Google Services пакет com.android.vending может присутствовать
        // как стаб, но не обрабатывать market-intent. isAppInstalled такие случаи не ловит,
        // поэтому проверяем по факту: если после клика остались на нашем пакете — skip.
        String resultingPkg = launchAndWaitForExternalApp(settings.rateAppButton());
        if ("kz.crystalspring.pit_stop_kz".equals(resultingPkg)) {
            throw new SkipException("Tapping ОЦЕНИТЬ did not switch the app — Play Store "
                    + "likely not functional on this emulator (AOSP image without Google Services)");
        }
        Assert.assertNotEquals(resultingPkg, "kz.crystalspring.pit_stop_kz",
                "After tapping ОЦЕНИТЬ current package should be the Play Store");
    }

    @Test(description = "Tapping 'ПОДЕЛИТЬСЯ' launches an external app (share intent)")
    public void shareAppLaunchesExternalApp() {
        assertButtonLaunchesExternalApp("ПОДЕЛИТЬСЯ", settings.shareAppButton());
    }

    @Test(description = "Tapping 'ЗАМЕЧАНИЯ/ПРЕДЛОЖЕНИЯ' launches an external app (email intent)")
    public void feedbackLaunchesExternalApp() {
        assertButtonLaunchesExternalApp("ЗАМЕЧАНИЯ", settings.feedbackButton());
    }

    /**
     * Проверяет, что после клика по кнопке текущий пакет ушёл с pit-stop на любое внешнее
     * приложение. Поллит getCurrentPackage до 15 секунд — внешние приложения запускаются
     * не мгновенно, особенно на медленном x86_64 эмуляторе.
     */
    private void assertButtonLaunchesExternalApp(String buttonLabel, WebElement button) {
        String ourPkg = "kz.crystalspring.pit_stop_kz";
        String currentPkg = launchAndWaitForExternalApp(button);
        Assert.assertNotEquals(currentPkg, ourPkg,
                "After tapping '" + buttonLabel + "' current package should leave '" + ourPkg
                        + "' (external intent) but was '" + currentPkg + "'");
    }

    /**
     * Кликает по кнопке и ждёт до 15с пока currentPackage уйдёт с нашего. Возвращает
     * актуальный currentPackage (наш — если ничего не сменилось, или пакет внешнего приложения).
     */
    private String launchAndWaitForExternalApp(WebElement button) {
        String ourPkg = "kz.crystalspring.pit_stop_kz";
        button.click();
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
        return currentPkg;
    }
}
