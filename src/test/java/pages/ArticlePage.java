package pages;

import core.SystemDialogs;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Экран статьи из раздела Советы — открывается тапом на карточку в AdvicesPage.
 * Все статьи paywall-gated: показывается заголовок, дата, превью в WebView и кнопка
 * КУПИТЬ с поясняющим текстом ("Для просмотра полной статьи, необходимо оформить подписку...").
 */
public class ArticlePage extends BasePage {

    private static final String PKG = "kz.crystalspring.pit_stop_kz";

    public static final String TOOLBAR_ID = PKG + ":id/toolbar";
    public static final String APP_BAR_ID = PKG + ":id/app_bar_layout";
    public static final String NEWS_TITLE_ID = PKG + ":id/tvNewsTitle";
    public static final String NEWS_DATE_ID = PKG + ":id/tvNewsDate";
    public static final String WEB_VIEW_ID = PKG + ":id/wvHelp";
    public static final String BUY_BUTTON_CONTAINER_ID = PKG + ":id/llBuyBtn";
    public static final String BUY_BUTTON_ID = PKG + ":id/btnBuy";

    public static final String PAYWALL_TEXT = "Для просмотра полной статьи, необходимо оформить подписку";

    public ArticlePage(AppiumDriver driver) {
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
            // tvNewsTitle + tvNewsDate — оба специфичны для экрана статьи.
            new WebDriverWait(driver, timeout)
                    .ignoring(WebDriverException.class)
                    .until(d -> !d.findElements(AppiumBy.id(NEWS_TITLE_ID)).isEmpty()
                            && !d.findElements(AppiumBy.id(NEWS_DATE_ID)).isEmpty());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public WebElement toolbar()     { return driver.findElement(AppiumBy.id(TOOLBAR_ID)); }
    public WebElement titleLabel()  { return driver.findElement(AppiumBy.id(NEWS_TITLE_ID)); }
    public WebElement dateLabel()   { return driver.findElement(AppiumBy.id(NEWS_DATE_ID)); }
    public WebElement webView()     { return driver.findElement(AppiumBy.id(WEB_VIEW_ID)); }
    public WebElement buyButton()   { return driver.findElement(AppiumBy.id(BUY_BUTTON_ID)); }

    public String getTitle() { return titleLabel().getText(); }
    public String getDate()  { return dateLabel().getText(); }

    public String getToolbarTitle() {
        return driver.findElement(AppiumBy.xpath(
                String.format("//*[@resource-id='%s']//android.widget.TextView", TOOLBAR_ID)
        )).getText();
    }

    public boolean hasText(String text) {
        String selector = String.format("new UiSelector().textContains(\"%s\")", text);
        return !driver.findElements(AppiumBy.androidUIAutomator(selector)).isEmpty();
    }
}
