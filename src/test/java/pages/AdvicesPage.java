package pages;

import core.SystemDialogs;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Экран "Советы" — открывается после дисмисса AdvicesNotificationDialog.
 * Содержит toolbar (заголовок "Советы" + кнопку "Перейти вверх" + кнопку-тоггл уведомлений)
 * и вертикальный список карточек (recycler_view): каждая карточка — статья с
 * заголовком, подзаголовком и датой.
 */
public class AdvicesPage extends BasePage {

    private static final String PKG = "kz.crystalspring.pit_stop_kz";

    public static final String TOOLBAR_ID = PKG + ":id/toolbar";
    public static final String APP_BAR_ID = PKG + ":id/app_bar_layout";
    public static final String RECYCLER_ID = PKG + ":id/recycler_view";
    public static final String NOTIFICATION_TOGGLE_ID = PKG + ":id/actAdviceNotif";
    public static final String CARD_ID = PKG + ":id/cv";
    public static final String CARD_TITLE_ID = PKG + ":id/tvTitle";
    public static final String CARD_SUBTITLE_ID = PKG + ":id/tvSubtitle";
    public static final String CARD_DATE_ID = PKG + ":id/tvDate";

    public AdvicesPage(AppiumDriver driver) {
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
            // toolbar + recycler_view — оба специфичны для этого экрана.
            new WebDriverWait(driver, timeout)
                    .ignoring(WebDriverException.class)
                    .until(d -> !d.findElements(AppiumBy.id(TOOLBAR_ID)).isEmpty()
                            && !d.findElements(AppiumBy.id(RECYCLER_ID)).isEmpty());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public WebElement toolbar() { return driver.findElement(AppiumBy.id(TOOLBAR_ID)); }
    public WebElement recyclerView() { return driver.findElement(AppiumBy.id(RECYCLER_ID)); }
    public WebElement notificationToggle() { return driver.findElement(AppiumBy.id(NOTIFICATION_TOGGLE_ID)); }

    public String getToolbarTitle() {
        // Заголовок — TextView внутри toolbar без своего id.
        return driver.findElement(AppiumBy.xpath(
                String.format("//*[@resource-id='%s']//android.widget.TextView", TOOLBAR_ID)
        )).getText();
    }

    public List<String> getArticleTitles() {
        return driver.findElements(AppiumBy.id(CARD_TITLE_ID))
                .stream().map(WebElement::getText).collect(Collectors.toList());
    }

    public List<String> getArticleSubtitles() {
        return driver.findElements(AppiumBy.id(CARD_SUBTITLE_ID))
                .stream().map(WebElement::getText).collect(Collectors.toList());
    }

    public List<String> getArticleDates() {
        return driver.findElements(AppiumBy.id(CARD_DATE_ID))
                .stream().map(WebElement::getText).collect(Collectors.toList());
    }

    public boolean hasArticle(String titleSubstring) {
        return getArticleTitles().stream().anyMatch(t -> t.contains(titleSubstring));
    }

    public int getVisibleCardsCount() {
        return driver.findElements(AppiumBy.id(CARD_ID)).size();
    }
}
