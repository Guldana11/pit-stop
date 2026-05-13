package pages;

import core.SystemDialogs;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Диалог "Введите промо-код" — открывается тапом на "АКТИВИРОВАТЬ ДРУГОЙ ПРОМО-КОД"
 * на экране Invite. Содержит заголовок, поле ввода (EditText с hint "Промо-код")
 * и кнопки ОТМЕНА / ОТПРАВИТЬ (стандартные android:id/button2 и button1).
 */
public class PromoCodeDialog extends BasePage {

    public static final String CANCEL_BUTTON_ID = "android:id/button2";
    public static final String SUBMIT_BUTTON_ID = "android:id/button1";
    public static final String INPUT_HINT = "Промо-код";
    public static final String TITLE_TEXT = "Введите промо-код";

    public PromoCodeDialog(AppiumDriver driver) {
        super(driver);
    }

    public boolean isShown() {
        return waitForShown(Duration.ofSeconds(15));
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
                    .until(d -> !d.findElements(AppiumBy.id(CANCEL_BUTTON_ID)).isEmpty()
                            && !d.findElements(AppiumBy.id(SUBMIT_BUTTON_ID)).isEmpty());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public WebElement cancelButton() { return driver.findElement(AppiumBy.id(CANCEL_BUTTON_ID)); }
    public WebElement submitButton() { return driver.findElement(AppiumBy.id(SUBMIT_BUTTON_ID)); }

    public WebElement promoInput() {
        // У EditText в диалоге нет своего resource-id — но он там один, ищем по классу.
        return driver.findElement(AppiumBy.className("android.widget.EditText"));
    }

    public String getInputHint() {
        return promoInput().getAttribute("hint");
    }

    public String getInputText() {
        return promoInput().getText();
    }

    public boolean hasText(String text) {
        String selector = String.format("new UiSelector().textContains(\"%s\")", text);
        return !driver.findElements(AppiumBy.androidUIAutomator(selector)).isEmpty();
    }

    public void typeCode(String code) {
        WebElement input = promoInput();
        input.click();
        input.sendKeys(code);
    }

    public InvitePage tapCancel() {
        cancelButton().click();
        return new InvitePage(driver);
    }
}
