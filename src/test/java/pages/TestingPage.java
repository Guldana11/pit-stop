package pages;

import core.SystemDialogs;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Экран "Тестирование" — открывается с главного экрана по btnQuestionsMain
 * (после дисмисса paywall-диалога). Содержит счётчик бесплатных тестов
 * и 7 кнопок: 2 режима, 3 типа сложности, "Купить" и "Бесплатно".
 */
public class TestingPage extends BasePage {

    private static final String PKG = "kz.crystalspring.pit_stop_kz";

    public static final String TEST_NUM_ID = PKG + ":id/tvTestNum";
    public static final String BTN_LEARN_ID = PKG + ":id/btnLearnBtn";
    public static final String BTN_EXAM_ID = PKG + ":id/btnExamBtn";
    public static final String BTN_HARD_ID = PKG + ":id/btnHard";
    public static final String BTN_EASY_ID = PKG + ":id/btnEasy";
    public static final String BTN_DISPUTE_ID = PKG + ":id/btnDispute";
    public static final String BTN_BUY_ID = PKG + ":id/btnBuyBtn";
    public static final String BTN_FREE_ID = PKG + ":id/btnFreeBtn";

    public TestingPage(AppiumDriver driver) {
        super(driver);
    }

    public boolean isDisplayed() {
        return waitForDisplayed(Duration.ofSeconds(60));
    }

    public boolean waitForDisplayed(Duration timeout) {
        // ANR can reappear several times on a slow x86_64 emulator — loop up to 3 rounds.
        for (int attempt = 0; attempt < 3; attempt++) {
            if (waitOnce(timeout)) return true;
            if (SystemDialogs.dismissAllAnrs(driver, 5) == 0) return false;
        }
        return false;
    }

    private boolean waitOnce(Duration timeout) {
        try {
            // tvTestNum + btnLearnBtn — оба специфичны для testing-экрана.
            new WebDriverWait(driver, timeout)
                    .ignoring(WebDriverException.class)
                    .until(d -> !d.findElements(AppiumBy.id(TEST_NUM_ID)).isEmpty()
                            && !d.findElements(AppiumBy.id(BTN_LEARN_ID)).isEmpty());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public WebElement testNumberLabel() { return driver.findElement(AppiumBy.id(TEST_NUM_ID)); }
    public WebElement learnButton()     { return driver.findElement(AppiumBy.id(BTN_LEARN_ID)); }
    public WebElement examButton()      { return driver.findElement(AppiumBy.id(BTN_EXAM_ID)); }
    public WebElement hardButton()      { return driver.findElement(AppiumBy.id(BTN_HARD_ID)); }
    public WebElement easyButton()      { return driver.findElement(AppiumBy.id(BTN_EASY_ID)); }
    public WebElement disputeButton()   { return driver.findElement(AppiumBy.id(BTN_DISPUTE_ID)); }
    public WebElement buyButton()       { return driver.findElement(AppiumBy.id(BTN_BUY_ID)); }
    public WebElement freeButton()      { return driver.findElement(AppiumBy.id(BTN_FREE_ID)); }

    public String getTestNumberText() {
        return testNumberLabel().getText();
    }
}
