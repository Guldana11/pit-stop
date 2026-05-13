package tests;

import core.BaseTest;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.InvitePage;
import pages.LanguageSelectionPage;
import pages.LanguageSelectionPage.Language;
import pages.MainScreenPage;
import pages.ProfilePage;
import pages.RuleChapterPage;
import pages.RulesPage;
import pages.SettingsPage;

/**
 * Регрессионные тесты BACK-навигации: нажатие системной кнопки "назад" из ключевых
 * экранов возвращает пользователя на ожидаемый предыдущий экран.
 */
public class BackNavigationTest extends BaseTest {

    private MainScreenPage main;

    @BeforeMethod(alwaysRun = true)
    public void openMain() {
        new LanguageSelectionPage(driver).selectLanguage(Language.RUSSIAN);
        main = new MainScreenPage(driver);
        Assert.assertTrue(main.isDisplayed(), "Main screen must be open before navigation tests");
    }

    @Test(description = "BACK from Settings returns to Main")
    public void backFromSettingsReturnsToMain() {
        SettingsPage settings = main.tapSettings();
        Assert.assertTrue(settings.isDisplayed(), "Settings should be open");

        pressBack();

        Assert.assertTrue(main.isDisplayed(), "Main screen should appear after BACK from Settings");
    }

    @Test(description = "BACK from Invite returns to Main")
    public void backFromInviteReturnsToMain() {
        InvitePage invite = main.tapInviteFriend();
        Assert.assertTrue(invite.isDisplayed(), "Invite should be open");

        pressBack();

        Assert.assertTrue(main.isDisplayed(), "Main screen should appear after BACK from Invite");
    }

    @Test(description = "BACK from Rules returns to Main")
    public void backFromRulesReturnsToMain() {
        RulesPage rules = main.tapRules();
        Assert.assertTrue(rules.isDisplayed(), "Rules should be open");

        pressBack();

        Assert.assertTrue(main.isDisplayed(), "Main screen should appear after BACK from Rules");
    }

    @Test(description = "BACK from Profile returns to Main")
    public void backFromProfileReturnsToMain() {
        ProfilePage profile = main.tapProfile();
        Assert.assertTrue(profile.isDisplayed(), "Profile should be open");

        pressBack();

        Assert.assertTrue(main.isDisplayed(), "Main screen should appear after BACK from Profile");
    }

    @Test(description = "BACK from a Rule chapter returns to the Rules list")
    public void backFromRuleChapterReturnsToRules() {
        RulesPage rules = main.tapRules();
        Assert.assertTrue(rules.isDisplayed(), "Rules should be open before opening a chapter");

        RuleChapterPage chapter = rules.tapChapter("1. Общие положения");
        Assert.assertTrue(chapter.isDisplayed(), "Chapter should be open before pressing BACK");

        pressBack();

        Assert.assertTrue(rules.isDisplayed(),
                "Rules screen should reappear after BACK from a chapter");
    }

    private void pressBack() {
        ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.BACK));
    }
}
