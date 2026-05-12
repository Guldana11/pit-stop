package pages;

import core.SystemDialogs;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Экран "Хочешь бесплатный доступ?" — открывается с главного экрана по btnInviteFriend.
 * Содержит описание реферальной программы, промо-код пользователя и кнопки
 * "Пригласить друзей" / "Активировать другой промо-код".
 */
public class InvitePage extends BasePage {

    private static final String PKG = "kz.crystalspring.pit_stop_kz";

    public static final String TITLE_ID = PKG + ":id/textView3";
    public static final String DESC_INVITE_ID = PKG + ":id/textView";
    public static final String DESC_FRIENDS_ID = PKG + ":id/textView2";
    public static final String INVITE_CODE_ID = PKG + ":id/tvInviteText";
    public static final String BTN_INVITE_ID = PKG + ":id/btnInvite";
    public static final String BTN_ACTIVATE_PROMO_ID = PKG + ":id/btnActivatePromo";

    public InvitePage(AppiumDriver driver) {
        super(driver);
    }

    public boolean isDisplayed() {
        return waitForDisplayed(Duration.ofSeconds(30));
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
            // btnInvite + tvInviteText — оба специфичны для invite-экрана.
            new WebDriverWait(driver, timeout)
                    .ignoring(WebDriverException.class)
                    .until(d -> !d.findElements(AppiumBy.id(BTN_INVITE_ID)).isEmpty()
                            && !d.findElements(AppiumBy.id(INVITE_CODE_ID)).isEmpty());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public WebElement title()              { return driver.findElement(AppiumBy.id(TITLE_ID)); }
    public WebElement inviteDescription()  { return driver.findElement(AppiumBy.id(DESC_INVITE_ID)); }
    public WebElement friendsDescription() { return driver.findElement(AppiumBy.id(DESC_FRIENDS_ID)); }
    public WebElement inviteCodeLabel()    { return driver.findElement(AppiumBy.id(INVITE_CODE_ID)); }
    public WebElement inviteButton()       { return driver.findElement(AppiumBy.id(BTN_INVITE_ID)); }
    public WebElement activatePromoButton() { return driver.findElement(AppiumBy.id(BTN_ACTIVATE_PROMO_ID)); }

    public String getInviteCode() {
        return inviteCodeLabel().getText();
    }

    public boolean hasText(String text) {
        String selector = String.format("new UiSelector().text(\"%s\")", text);
        return !driver.findElements(AppiumBy.androidUIAutomator(selector)).isEmpty();
    }
}
