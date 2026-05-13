package pages;

import core.SystemDialogs;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Экран активного вопроса теста — открывается после двух тапов на btnPushMe на главном
 * (первый показывает paywall, ОТМЕНА возвращает на главный, второй тап стартует тест).
 * Содержит заголовок вопроса, таймер обратного отсчёта, варианты ответов в view_pager,
 * горизонтальную ленту номеров вопросов и кнопки навигации Prev/Next.
 */
public class QuestionPage extends BasePage {

    private static final String PKG = "kz.crystalspring.pit_stop_kz";

    public static final String QUEST_TITLE_ID = PKG + ":id/tvQuestTitle";
    public static final String TIMER_ID = PKG + ":id/tvTimer";
    public static final String QUEST_TIMER_CONTAINER_ID = PKG + ":id/questTimer";
    public static final String QUEST_LINE_ID = PKG + ":id/questLine";
    public static final String QUEST_DETAILS_ID = PKG + ":id/questDetails";
    public static final String VIEW_PAGER_ID = PKG + ":id/view_pager";
    public static final String QUEST_RIBBON_ID = PKG + ":id/hsvQuestions";
    public static final String BTN_NEXT_ID = PKG + ":id/btnNextQuestion";
    public static final String BTN_PREV_ID = PKG + ":id/btnPrevQuestion";

    public QuestionPage(AppiumDriver driver) {
        super(driver);
    }

    public boolean isDisplayed() {
        // Запуск тестовой активити очень тяжёлый на x86_64 эмуляторе — наблюдали 70-80с на
        // повторных запусках (cold cache после fullReset). Даём 120с + 3 раунда с дисмиссом ANR.
        return waitForDisplayed(Duration.ofSeconds(120));
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
            // tvQuestTitle + view_pager — оба специфичны для экрана вопроса.
            new WebDriverWait(driver, timeout)
                    .ignoring(WebDriverException.class)
                    .until(d -> !d.findElements(AppiumBy.id(QUEST_TITLE_ID)).isEmpty()
                            && !d.findElements(AppiumBy.id(VIEW_PAGER_ID)).isEmpty());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public WebElement questionTitle() { return driver.findElement(AppiumBy.id(QUEST_TITLE_ID)); }
    public WebElement timer()         { return driver.findElement(AppiumBy.id(TIMER_ID)); }
    public WebElement viewPager()     { return driver.findElement(AppiumBy.id(VIEW_PAGER_ID)); }
    public WebElement questionRibbon() { return driver.findElement(AppiumBy.id(QUEST_RIBBON_ID)); }
    public WebElement nextButton()    { return driver.findElement(AppiumBy.id(BTN_NEXT_ID)); }
    public WebElement prevButton()    { return driver.findElement(AppiumBy.id(BTN_PREV_ID)); }

    public String getQuestionTitle() { return questionTitle().getText(); }
    public String getTimerText()     { return timer().getText(); }
}
