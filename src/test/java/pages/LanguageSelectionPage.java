package pages;

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
        try {
            // Ignore WebDriverException so AccessibilityNodeInfo timeouts during splash
            // animation don't abort the whole wait — they're transient.
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
        // Wait for the screen first. Fail fast if it never appears — otherwise
        // elementToBeClickable would waste another 30s on a non-existent element.
        if (!waitForDisplayed(Duration.ofSeconds(60))) {
            throw new RuntimeException("Language selection screen did not appear within 60s — "
                    + "app may be stuck on splash or showing a system error dialog");
        }
        WebElement button = new WebDriverWait(driver, Duration.ofSeconds(30))
                .ignoring(WebDriverException.class)
                .until(ExpectedConditions.elementToBeClickable(AppiumBy.id(language.buttonId)));
        button.click();
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
