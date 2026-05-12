package pages;

import core.SystemDialogs;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Paywall-диалог, который показывается после тапа на "Тестирование" с главного экрана.
 * Содержит описание ограничений (3 теста / 3 дня / 5 друзей) и две кнопки:
 * "ОТМЕНА" (закрывает диалог → ведёт на экран Тестирование с лимитами)
 * и "ПРИГЛАСИТЬ" (ведёт на экран invite).
 */
public class TestingPaywallDialog extends BasePage {

    public static final String CANCEL_BUTTON_ID = "android:id/button2";
    public static final String INVITE_BUTTON_ID = "android:id/button1";

    public TestingPaywallDialog(AppiumDriver driver) {
        super(driver);
    }

    public boolean isShown() {
        return waitForShown(Duration.ofSeconds(15));
    }

    public boolean waitForShown(Duration timeout) {
        if (waitOnce(timeout)) return true;
        if (SystemDialogs.dismissAnrIfPresent(driver)) {
            return waitOnce(timeout);
        }
        return false;
    }

    private boolean waitOnce(Duration timeout) {
        try {
            new WebDriverWait(driver, timeout)
                    .ignoring(WebDriverException.class)
                    .until(d -> !d.findElements(AppiumBy.id(CANCEL_BUTTON_ID)).isEmpty()
                            && !d.findElements(AppiumBy.id(INVITE_BUTTON_ID)).isEmpty());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public WebElement cancelButton() { return driver.findElement(AppiumBy.id(CANCEL_BUTTON_ID)); }
    public WebElement inviteButton() { return driver.findElement(AppiumBy.id(INVITE_BUTTON_ID)); }

    public boolean hasText(String text) {
        String selector = String.format("new UiSelector().textContains(\"%s\")", text);
        return !driver.findElements(AppiumBy.androidUIAutomator(selector)).isEmpty();
    }

    public TestingPage tapCancel() {
        cancelButton().click();
        return new TestingPage(driver);
    }

    public InvitePage tapInvite() {
        inviteButton().click();
        return new InvitePage(driver);
    }
}
