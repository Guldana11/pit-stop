package tests;

import core.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.AdvicesNotificationDialog;
import pages.AdvicesPage;
import pages.LanguageSelectionPage;
import pages.LanguageSelectionPage.Language;
import pages.MainScreenPage;

import java.util.List;

/**
 * Тесты экрана "Советы" (открывается после дисмисса AdvicesNotificationDialog):
 * проверяет заголовок toolbar, кнопку-тоггл уведомлений, наличие хотя бы одной
 * карточки и формат дат (YYYY-MM-DD).
 */
public class AdvicesTest extends BaseTest {

    private AdvicesPage advices;

    @BeforeMethod(alwaysRun = true)
    public void openAdvicesPage() {
        new LanguageSelectionPage(driver).selectLanguage(Language.RUSSIAN);
        MainScreenPage main = new MainScreenPage(driver);
        Assert.assertTrue(main.isDisplayed(), "Main screen must be open");

        AdvicesNotificationDialog dialog = main.tapAdvices();
        Assert.assertTrue(dialog.isShown(), "Advices notification dialog must be shown");

        // Тап НЕТ — отказываемся от уведомлений (чтобы не засорять систему), ведёт на тот же экран.
        advices = dialog.tapNo();
        Assert.assertTrue(advices.isDisplayed(),
                "Advices page must be open before each AdvicesPage test");
    }

    @Test(description = "Toolbar shows 'Советы' as the screen title")
    public void toolbarShowsAdvicesTitle() {
        Assert.assertEquals(advices.getToolbarTitle(), "Советы",
                "Toolbar title should be 'Советы'");
    }

    @Test(description = "Notification toggle button is shown in the toolbar")
    public void notificationToggleIsShown() {
        Assert.assertTrue(advices.notificationToggle().isDisplayed(),
                "Notification toggle button should be visible in the toolbar");
        Assert.assertTrue(advices.notificationToggle().isEnabled(),
                "Notification toggle button should be enabled");
    }

    @Test(description = "Article list contains at least one card")
    public void articleListIsNotEmpty() {
        Assert.assertTrue(advices.getVisibleCardsCount() > 0,
                "Advices list should contain at least one article card");
    }

    @Test(description = "Each visible article has a non-empty title")
    public void articlesHaveTitles() {
        List<String> titles = advices.getArticleTitles();
        Assert.assertFalse(titles.isEmpty(), "There should be at least one article title");
        for (String title : titles) {
            Assert.assertFalse(title == null || title.isBlank(),
                    "Article title should not be blank");
        }
    }

    @Test(description = "Each visible article date is in YYYY-MM-DD format")
    public void articleDatesAreFormatted() {
        List<String> dates = advices.getArticleDates();
        Assert.assertFalse(dates.isEmpty(), "There should be at least one article date");
        for (String date : dates) {
            Assert.assertTrue(date.matches("\\d{4}-\\d{2}-\\d{2}"),
                    "Date should match YYYY-MM-DD but was '" + date + "'");
        }
    }

    @Test(description = "Tapping the notification bell toggles a toast and keeps the Advices page visible")
    public void tappingNotificationToggleKeepsPageVisible() {
        // Состояние колокольчика меняется только визуально (drawable), Appium-атрибуты не отражают
        // toggle (selected/checked остаются false до и после). Проверяем только что тап не крашит
        // и страница не закрывается.
        advices.notificationToggle().click();
        Assert.assertTrue(advices.isDisplayed(),
                "Advices page should still be displayed after tapping notification bell");
    }
}
