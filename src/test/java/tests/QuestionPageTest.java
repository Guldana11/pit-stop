package tests;

import core.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.LanguageSelectionPage;
import pages.LanguageSelectionPage.Language;
import pages.MainScreenPage;
import pages.QuestionPage;
import pages.TestingPaywallDialog;

/**
 * Тесты экрана вопроса теста (открывается двойным тапом на btnPushMe: первый показывает
 * paywall, ОТМЕНА возвращает на главный, второй тап стартует тест сразу).
 * Проверяет наличие заголовка вопроса, формат таймера и кнопок навигации.
 */
public class QuestionPageTest extends BaseTest {

    private QuestionPage question;

    @BeforeMethod(alwaysRun = true)
    public void openQuestion() {
        new LanguageSelectionPage(driver).selectLanguage(Language.RUSSIAN);
        MainScreenPage main = new MainScreenPage(driver);
        Assert.assertTrue(main.isDisplayed(), "Main screen must be open");

        // Первый тап → paywall, ОТМЕНА → главный, второй тап → стартует тест.
        TestingPaywallDialog paywall = main.tapPushMe();
        Assert.assertTrue(paywall.isShown(), "Testing paywall must be shown");
        main = paywall.tapCancel();
        Assert.assertTrue(main.isDisplayed(), "Main screen must be back after dismissing paywall");

        question = main.tapPushMeExpectingQuestion();
        Assert.assertTrue(question.isDisplayed(),
                "Question page must be open before each QuestionPage test");
    }

    @Test(description = "Question page shows a non-empty question title")
    public void questionHasNonEmptyTitle() {
        String title = question.getQuestionTitle();
        Assert.assertNotNull(title, "Question title should not be null");
        Assert.assertFalse(title.isBlank(), "Question title should not be blank");
    }

    @Test(description = "Timer is shown in MM:SS format")
    public void timerHasExpectedFormat() {
        String timer = question.getTimerText();
        Assert.assertTrue(timer.matches("\\d{1,2}:\\d{2}"),
                "Timer should match MM:SS but was '" + timer + "'");
    }

    @Test(description = "Next and Previous navigation buttons are present")
    public void navigationButtonsAreShown() {
        Assert.assertTrue(question.nextButton().isDisplayed(),
                "Next question button should be visible");
        Assert.assertTrue(question.prevButton().isDisplayed(),
                "Previous question button should be visible");
    }

    @Test(description = "Question ribbon and view pager are shown")
    public void ribbonAndPagerAreShown() {
        Assert.assertTrue(question.questionRibbon().isDisplayed(),
                "Question numbers ribbon should be visible");
        Assert.assertTrue(question.viewPager().isDisplayed(),
                "Question view pager should be visible");
    }
}
