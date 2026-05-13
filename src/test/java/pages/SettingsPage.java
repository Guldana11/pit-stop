package pages;

import core.SystemDialogs;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Экран настроек pit-stop.kz: открывается из главного экрана по btnShowSettings.
 * Содержит версию приложения и набор кнопок (Оценить, Поделиться, Бесплатный
 * доступ, Способ оплаты, Замечания, Выберите язык).
 */
public class SettingsPage extends BasePage {

    private static final String PKG = "kz.crystalspring.pit_stop_kz";

    public static final String VERSION_ID = PKG + ":id/tvVersion";
    public static final String BTN_RATE_APP_ID = PKG + ":id/btnRateApp";
    public static final String BTN_SHARE_APP_ID = PKG + ":id/btnShareApp";
    public static final String BTN_FREE_ACCESS_ID = PKG + ":id/btnFreeAccess";
    public static final String BTN_WANT_PAY_OTHER_ID = PKG + ":id/btnWantPayOther";
    public static final String BTN_FEEDBACK_ID = PKG + ":id/btnFeedback";
    public static final String BTN_SELECT_LANGUAGE_ID = PKG + ":id/btnSelectLanguage";

    // Inline-блок выбора языка — раскрывается тапом на ВЫБЕРИТЕ ЯЗЫК прямо внутри Settings,
    // НЕ ведёт на отдельный LanguageSelectionPage. Кнопки внутри имеют другие id (без префикса 'button').
    public static final String LANGUAGE_PICKER_CONTAINER_ID = PKG + ":id/ll_select_lng";
    public static final String INLINE_BTN_RUSSIAN_ID = PKG + ":id/btnRussian";
    public static final String INLINE_BTN_KAZAKH_ID = PKG + ":id/btnKazakh";

    public SettingsPage(AppiumDriver driver) {
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
            // tvVersion + btnRateApp — оба специфичны для экрана настроек.
            new WebDriverWait(driver, timeout)
                    .ignoring(WebDriverException.class)
                    .until(d -> !d.findElements(AppiumBy.id(VERSION_ID)).isEmpty()
                            && !d.findElements(AppiumBy.id(BTN_RATE_APP_ID)).isEmpty());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public WebElement versionLabel()       { return driver.findElement(AppiumBy.id(VERSION_ID)); }
    public WebElement rateAppButton()      { return driver.findElement(AppiumBy.id(BTN_RATE_APP_ID)); }
    public WebElement shareAppButton()     { return driver.findElement(AppiumBy.id(BTN_SHARE_APP_ID)); }
    public WebElement freeAccessButton()   { return driver.findElement(AppiumBy.id(BTN_FREE_ACCESS_ID)); }

    public InvitePage tapFreeAccess() {
        freeAccessButton().click();
        return new InvitePage(driver);
    }
    public WebElement wantPayOtherButton() { return driver.findElement(AppiumBy.id(BTN_WANT_PAY_OTHER_ID)); }
    public WebElement feedbackButton()     { return driver.findElement(AppiumBy.id(BTN_FEEDBACK_ID)); }
    public WebElement selectLanguageButton() { return driver.findElement(AppiumBy.id(BTN_SELECT_LANGUAGE_ID)); }

    /**
     * Раскрывает inline-блок выбора языка прямо внутри Settings (НЕ открывает отдельный экран).
     * Возвращает текущий SettingsPage для удобства цепочек.
     */
    public SettingsPage expandLanguagePicker() {
        selectLanguageButton().click();
        try {
            new WebDriverWait(driver, Duration.ofSeconds(15))
                    .ignoring(WebDriverException.class)
                    .until(d -> !d.findElements(AppiumBy.id(LANGUAGE_PICKER_CONTAINER_ID)).isEmpty());
        } catch (Exception ignored) {
            // up to the caller — let assertion catch it
        }
        return this;
    }

    public boolean isLanguagePickerExpanded() {
        return !driver.findElements(AppiumBy.id(LANGUAGE_PICKER_CONTAINER_ID)).isEmpty()
                && !driver.findElements(AppiumBy.id(INLINE_BTN_RUSSIAN_ID)).isEmpty()
                && !driver.findElements(AppiumBy.id(INLINE_BTN_KAZAKH_ID)).isEmpty();
    }

    public WebElement inlineRussianButton() { return driver.findElement(AppiumBy.id(INLINE_BTN_RUSSIAN_ID)); }
    public WebElement inlineKazakhButton()  { return driver.findElement(AppiumBy.id(INLINE_BTN_KAZAKH_ID)); }

    /**
     * Тап на inline-кнопку языка перезапускает приложение и возвращает на главный экран
     * (уже в выбранной локали).
     */
    public MainScreenPage tapInlineKazakh() {
        inlineKazakhButton().click();
        return new MainScreenPage(driver);
    }

    public MainScreenPage tapInlineRussian() {
        inlineRussianButton().click();
        return new MainScreenPage(driver);
    }

    public String getVersion() {
        return versionLabel().getText();
    }

    public boolean hasText(String text) {
        String selector = String.format("new UiSelector().text(\"%s\")", text);
        return !driver.findElements(AppiumBy.androidUIAutomator(selector)).isEmpty();
    }
}
