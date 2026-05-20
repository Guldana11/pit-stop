package pages;

import core.SystemDialogs;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Экран "Профиль" — открывается с главного экрана по ivShowProfile.
 * Форма с 6 полями ввода (псевдоним, телефон, email, город, дата рождения, пол),
 * 5 чекбоксами (наличие авто, новости, предложения, страховка, альтернатива оплате)
 * и кнопкой СОХРАНИТЬ.
 */
public class ProfilePage extends BasePage {

    private static final String PKG = "kz.crystalspring.pit_stop_kz";

    public static final String ET_NICKNAME_ID = PKG + ":id/etNickname";
    public static final String ET_PHONE_ID = PKG + ":id/etPhone";
    public static final String ET_EMAIL_ID = PKG + ":id/etEmail";
    public static final String ET_CITY_ID = PKG + ":id/etCity";
    public static final String ET_BIRTHDAY_ID = PKG + ":id/etBirthday";
    public static final String ET_SEX_ID = PKG + ":id/etSex";

    public static final String CHK_HAS_AUTOMOBILE_ID = PKG + ":id/chkHasAutomobile";
    public static final String CHK_DO_NOT_NEWS_ID = PKG + ":id/chkDoNotNews";
    public static final String CHK_WANT_BONUS_ID = PKG + ":id/chkWantBonus";
    public static final String CHK_WANT_INSURANCE_ID = PKG + ":id/chkWantInsurance";
    public static final String CHK_WANT_FREE_ID = PKG + ":id/chkWantFree";

    public static final String BTN_SAVE_ID = PKG + ":id/btnSave";

    public ProfilePage(AppiumDriver driver) {
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
            // etNickname + btnSave — оба специфичны для экрана профиля.
            new WebDriverWait(driver, timeout)
                    .ignoring(WebDriverException.class)
                    .until(d -> !d.findElements(AppiumBy.id(ET_NICKNAME_ID)).isEmpty()
                            && !d.findElements(AppiumBy.id(BTN_SAVE_ID)).isEmpty());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public WebElement nicknameInput() { return driver.findElement(AppiumBy.id(ET_NICKNAME_ID)); }
    public WebElement phoneInput()    { return driver.findElement(AppiumBy.id(ET_PHONE_ID)); }
    public WebElement emailInput()    { return driver.findElement(AppiumBy.id(ET_EMAIL_ID)); }
    public WebElement cityInput()     { return driver.findElement(AppiumBy.id(ET_CITY_ID)); }
    public WebElement birthdayInput() { return driver.findElement(AppiumBy.id(ET_BIRTHDAY_ID)); }
    public WebElement sexInput()      { return driver.findElement(AppiumBy.id(ET_SEX_ID)); }

    public WebElement hasAutomobileCheckbox() { return driver.findElement(AppiumBy.id(CHK_HAS_AUTOMOBILE_ID)); }
    public WebElement doNotNewsCheckbox()     { return driver.findElement(AppiumBy.id(CHK_DO_NOT_NEWS_ID)); }
    public WebElement wantBonusCheckbox()     { return driver.findElement(AppiumBy.id(CHK_WANT_BONUS_ID)); }
    public WebElement wantInsuranceCheckbox() { return driver.findElement(AppiumBy.id(CHK_WANT_INSURANCE_ID)); }
    public WebElement wantFreeCheckbox()      { return driver.findElement(AppiumBy.id(CHK_WANT_FREE_ID)); }

    public WebElement saveButton() { return driver.findElement(AppiumBy.id(BTN_SAVE_ID)); }

    /**
     * Тап на СОХРАНИТЬ с предварительным скрытием клавиатуры. После ввода в любое поле
     * IME перекрывает кнопку Save снизу, и обычный click() валится с NoSuchElement.
     */
    public void tapSave() {
        hideKeyboard();
        saveButton().click();
    }

    /**
     * Заполняет поле текстом — сначала скрывает IME, чтобы поле не было перекрыто
     * клавиатурой от ввода в предыдущее. Принимает WebElement как у геттеров выше.
     */
    public void fillInput(WebElement input, String value) {
        hideKeyboard();
        input.click();
        input.sendKeys(value);
    }

    private void hideKeyboard() {
        try {
            ((AndroidDriver) driver).hideKeyboard();
        } catch (Exception ignored) {
            // клавиатура могла быть не открыта — это норма
        }
    }

    public boolean hasText(String text) {
        String selector = String.format("new UiSelector().textContains(\"%s\")", text);
        return !driver.findElements(AppiumBy.androidUIAutomator(selector)).isEmpty();
    }

    public String getFieldHint(String fieldId) {
        return driver.findElement(AppiumBy.id(fieldId)).getAttribute("hint");
    }
}
