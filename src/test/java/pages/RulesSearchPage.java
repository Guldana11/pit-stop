package pages;

import core.SystemDialogs;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Экран поиска по правилам — открывается тапом на кнопку search на RulesPage.
 * Содержит поле ввода (et_search, hint "Введите текст"), кнопку очистки (close)
 * и кнопку запуска поиска (btn_search).
 */
public class RulesSearchPage extends BasePage {

    private static final String PKG = "kz.crystalspring.pit_stop_kz";

    public static final String SEARCH_INPUT_ID = PKG + ":id/et_search";
    public static final String CLEAR_BUTTON_ID = PKG + ":id/close";
    public static final String SEARCH_BUTTON_ID = PKG + ":id/btn_search";

    public RulesSearchPage(AppiumDriver driver) {
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
            // et_search + btn_search — оба специфичны для search-экрана.
            new WebDriverWait(driver, timeout)
                    .ignoring(WebDriverException.class)
                    .until(d -> !d.findElements(AppiumBy.id(SEARCH_INPUT_ID)).isEmpty()
                            && !d.findElements(AppiumBy.id(SEARCH_BUTTON_ID)).isEmpty());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public WebElement searchInput()  { return driver.findElement(AppiumBy.id(SEARCH_INPUT_ID)); }
    public WebElement clearButton()  { return driver.findElement(AppiumBy.id(CLEAR_BUTTON_ID)); }
    public WebElement searchButton() { return driver.findElement(AppiumBy.id(SEARCH_BUTTON_ID)); }

    public String getInputHint() {
        return searchInput().getAttribute("hint");
    }

    public String getInputText() {
        return searchInput().getText();
    }

    public void typeQuery(String query) {
        WebElement input = searchInput();
        input.click();
        input.sendKeys(query);
    }
}
