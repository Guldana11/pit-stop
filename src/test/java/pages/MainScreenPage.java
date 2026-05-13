package pages;

import core.SystemDialogs;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Главный экран pit-stop.kz: открывается после выбора языка.
 * Содержит кнопки Правила / Тестирование / Советы / PushMe (центр)
 * + верхнюю панель (настройки, пригласить друга) и нижнюю (профиль, инстаграм).
 */
public class MainScreenPage extends BasePage {

    private static final String PKG = "kz.crystalspring.pit_stop_kz";

    public static final String BTN_SETTINGS_ID = PKG + ":id/btnShowSettings";
    public static final String BTN_INVITE_FRIEND_ID = PKG + ":id/btnInviteFriend";
    public static final String BTN_RULES_ID = PKG + ":id/btnRulesMain";
    public static final String BTN_PUSH_ME_ID = PKG + ":id/btnPushMe";
    public static final String BTN_QUESTIONS_ID = PKG + ":id/btnQuestionsMain";
    public static final String BTN_ADVICES_ID = PKG + ":id/btnShowAdvices";
    public static final String BTN_PROFILE_ID = PKG + ":id/ivShowProfile";
    public static final String BTN_INSTAGRAM_ID = PKG + ":id/ivShowInstagram";

    public MainScreenPage(AppiumDriver driver) {
        super(driver);
    }

    public boolean isDisplayed() {
        return waitForDisplayed(Duration.ofSeconds(60));
    }

    public boolean waitForDisplayed(Duration timeout) {
        for (int attempt = 0; attempt < 3; attempt++) {
            if (waitOnce(timeout)) return true;
            if (SystemDialogs.dismissAllAnrs(driver, 5) == 0) return false;
        }
        return false;
    }

    private boolean waitOnce(Duration timeout) {
        try {
            // btnRulesMain + btnQuestionsMain — оба специфичны для главного экрана.
            new WebDriverWait(driver, timeout)
                    .ignoring(WebDriverException.class)
                    .until(d -> !d.findElements(AppiumBy.id(BTN_RULES_ID)).isEmpty()
                            && !d.findElements(AppiumBy.id(BTN_QUESTIONS_ID)).isEmpty());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public WebElement settingsButton()  { return driver.findElement(AppiumBy.id(BTN_SETTINGS_ID)); }

    public SettingsPage tapSettings() {
        settingsButton().click();
        // pit-stop иногда показывает промо-интерстициал "Хочешь бесплатный доступ?"
        // перед настройками на первом открытии после fullReset. Если попали туда —
        // нажимаем back и settings откроется.
        SettingsPage settings = new SettingsPage(driver);
        if (!settings.waitForDisplayed(Duration.ofSeconds(5))) {
            if (!driver.findElements(AppiumBy.id("kz.crystalspring.pit_stop_kz:id/btnActivatePromo"))
                    .isEmpty()) {
                System.out.println("[main] promo interstitial detected — pressing BACK");
                ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.BACK));
            }
        }
        return settings;
    }

    public WebElement inviteButton()    { return driver.findElement(AppiumBy.id(BTN_INVITE_FRIEND_ID)); }

    public InvitePage tapInviteFriend() {
        inviteButton().click();
        return new InvitePage(driver);
    }

    public WebElement rulesButton()     { return driver.findElement(AppiumBy.id(BTN_RULES_ID)); }

    public RulesPage tapRules() {
        rulesButton().click();
        return new RulesPage(driver);
    }

    public WebElement pushMeButton()    { return driver.findElement(AppiumBy.id(BTN_PUSH_ME_ID)); }

    public TestingPaywallDialog tapPushMe() {
        pushMeButton().click();
        return new TestingPaywallDialog(driver);
    }

    /**
     * Второй тап на PushMe после того, как paywall уже был дисмиссен — открывает
     * сразу экран вопроса теста, без выбора режима/сложности.
     */
    public QuestionPage tapPushMeExpectingQuestion() {
        pushMeButton().click();
        return new QuestionPage(driver);
    }
    public WebElement questionsButton() { return driver.findElement(AppiumBy.id(BTN_QUESTIONS_ID)); }

    public TestingPaywallDialog tapQuestions() {
        questionsButton().click();
        return new TestingPaywallDialog(driver);
    }

    /**
     * Второй тап на "Тестирование" после того, как paywall уже был дисмиссен — открывает
     * непосредственно экран Тестирование, без повторного показа paywall.
     */
    public TestingPage tapQuestionsExpectingTestingPage() {
        questionsButton().click();
        return new TestingPage(driver);
    }

    public WebElement advicesButton()   { return driver.findElement(AppiumBy.id(BTN_ADVICES_ID)); }

    public AdvicesNotificationDialog tapAdvices() {
        advicesButton().click();
        return new AdvicesNotificationDialog(driver);
    }
    public WebElement profileButton()   { return driver.findElement(AppiumBy.id(BTN_PROFILE_ID)); }

    public ProfilePage tapProfile() {
        profileButton().click();
        return new ProfilePage(driver);
    }
    public WebElement instagramButton() { return driver.findElement(AppiumBy.id(BTN_INSTAGRAM_ID)); }

    public boolean hasText(String text) {
        String selector = String.format("new UiSelector().text(\"%s\")", text);
        return !driver.findElements(AppiumBy.androidUIAutomator(selector)).isEmpty();
    }
}
