package pages;

import core.SystemDialogs;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Экран содержимого главы ПДД — открывается тапом на главу в списке RulesPage
 * (например, "1. Общие положения"). Внутри pager → rulesDetailText (ScrollView)
 * с одним большим TextView (tvRulesText), содержащим весь текст главы:
 * заголовок и все подпункты.
 */
public class RuleChapterPage extends BasePage {

    private static final String PKG = "kz.crystalspring.pit_stop_kz";

    public static final String DETAIL_SCROLL_ID = PKG + ":id/rulesDetailText";
    public static final String DETAIL_LAYOUT_ID = PKG + ":id/rulesDetailLayout";
    public static final String RULES_TEXT_ID = PKG + ":id/tvRulesText";

    public RuleChapterPage(AppiumDriver driver) {
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
            // tvRulesText + rulesDetailText — оба специфичны для экрана главы.
            new WebDriverWait(driver, timeout)
                    .ignoring(WebDriverException.class)
                    .until(d -> !d.findElements(AppiumBy.id(RULES_TEXT_ID)).isEmpty()
                            && !d.findElements(AppiumBy.id(DETAIL_SCROLL_ID)).isEmpty());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public WebElement detailScroll() { return driver.findElement(AppiumBy.id(DETAIL_SCROLL_ID)); }
    public WebElement rulesTextView() { return driver.findElement(AppiumBy.id(RULES_TEXT_ID)); }

    public String getRulesText() {
        return rulesTextView().getText();
    }

    public boolean hasText(String substring) {
        return getRulesText().contains(substring);
    }
}
