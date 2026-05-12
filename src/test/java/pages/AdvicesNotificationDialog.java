package pages;

import core.SystemDialogs;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Диалог-промт, который показывается при первом тапе на "Советы" с главного экрана:
 * "В данном разделе содержатся Советы Юристов и полезные статьи. Хотите получать
 * уведомления о новых Советах?" с кнопками ДА и НЕТ. После выбора ведёт на экран Советы.
 */
public class AdvicesNotificationDialog extends BasePage {

    public static final String NO_BUTTON_ID = "android:id/button2";
    public static final String YES_BUTTON_ID = "android:id/button1";

    public AdvicesNotificationDialog(AppiumDriver driver) {
        super(driver);
    }

    public boolean isShown() {
        return waitForShown(Duration.ofSeconds(30));
    }

    public boolean waitForShown(Duration timeout) {
        for (int attempt = 0; attempt < 3; attempt++) {
            if (waitOnce(timeout)) return true;
            if (SystemDialogs.dismissAllAnrs(driver, 5) == 0) return false;
        }
        return false;
    }

    private boolean waitOnce(Duration timeout) {
        try {
            new WebDriverWait(driver, timeout)
                    .ignoring(WebDriverException.class)
                    .until(d -> !d.findElements(AppiumBy.id(NO_BUTTON_ID)).isEmpty()
                            && !d.findElements(AppiumBy.id(YES_BUTTON_ID)).isEmpty());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public WebElement yesButton() { return driver.findElement(AppiumBy.id(YES_BUTTON_ID)); }
    public WebElement noButton()  { return driver.findElement(AppiumBy.id(NO_BUTTON_ID)); }

    public boolean hasText(String text) {
        String selector = String.format("new UiSelector().textContains(\"%s\")", text);
        return !driver.findElements(AppiumBy.androidUIAutomator(selector)).isEmpty();
    }

    public AdvicesPage tapYes() {
        yesButton().click();
        return new AdvicesPage(driver);
    }

    public AdvicesPage tapNo() {
        noButton().click();
        return new AdvicesPage(driver);
    }
}
