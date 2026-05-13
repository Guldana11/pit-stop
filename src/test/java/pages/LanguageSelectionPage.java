package pages;

import core.SystemDialogs;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LanguageSelectionPage extends BasePage {

    private static final String PKG = "kz.crystalspring.pit_stop_kz";

    private static final String BUTTON_RUSSIAN_ID = PKG + ":id/buttonRussian";
    private static final String BUTTON_KAZAKH_ID = PKG + ":id/buttonKazakh";
    private static final String TEXT_RUSSIAN_ID = PKG + ":id/tvTextRussianLanguage";
    private static final String TEXT_QAZAQ_ID = PKG + ":id/tvTextQazaqLanguage";
    private static final String WARNING_LANGUAGE_ID = PKG + ":id/tvWarningLanguage";

    public enum Language {
        RUSSIAN(BUTTON_RUSSIAN_ID, "РУССКИЙ"),
        KAZAKH(BUTTON_KAZAKH_ID, "ҚАЗАҚША");

        public final String buttonId;
        public final String label;

        Language(String buttonId, String label) {
            this.buttonId = buttonId;
            this.label = label;
        }
    }

    public LanguageSelectionPage(AppiumDriver driver) {
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
            new WebDriverWait(driver, timeout)
                    .ignoring(WebDriverException.class)
                    .until(d -> !d.findElements(AppiumBy.id(BUTTON_RUSSIAN_ID)).isEmpty()
                            && !d.findElements(AppiumBy.id(BUTTON_KAZAKH_ID)).isEmpty());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void selectLanguage(Language language) {
        if (!waitForDisplayed(Duration.ofSeconds(60))) {
            throw new RuntimeException("Language selection screen did not appear within 60s — "
                    + "app may be stuck on splash or showing a system error dialog");
        }
        // ANR may have appeared on top after waitForDisplayed succeeded — dismiss it
        // before trying to click, otherwise the language buttons stay non-clickable.
        SystemDialogs.dismissAllAnrs(driver, 5);
        clickLanguageButton(language);
        // Kazakh translation is incomplete — after the first tap the app shows a warning
        // (tvWarningLanguage) and keeps the same buttons on screen. Confirm by tapping again.
        if (language == Language.KAZAKH && isWarningShown()) {
            clickLanguageButton(language);
        }
    }

    private void clickLanguageButton(Language language) {
        WebElement button = new WebDriverWait(driver, Duration.ofSeconds(30))
                .ignoring(WebDriverException.class)
                .until(ExpectedConditions.elementToBeClickable(AppiumBy.id(language.buttonId)));
        button.click();
    }

    private boolean isWarningShown() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(5))
                    .ignoring(WebDriverException.class)
                    .until(d -> !d.findElements(AppiumBy.id(WARNING_LANGUAGE_ID)).isEmpty());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getButtonText(Language language) {
        return driver.findElement(AppiumBy.id(language.buttonId)).getText();
    }

    public String getRussianHeaderText() {
        return driver.findElement(AppiumBy.id(TEXT_RUSSIAN_ID)).getText();
    }

    public String getQazaqHeaderText() {
        return driver.findElement(AppiumBy.id(TEXT_QAZAQ_ID)).getText();
    }

    public boolean isButtonClickable(Language language) {
        WebElement button = driver.findElement(AppiumBy.id(language.buttonId));
        return button.isDisplayed() && button.isEnabled();
    }
}
