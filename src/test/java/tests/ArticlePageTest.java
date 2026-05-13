package tests;

import core.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.AdvicesNotificationDialog;
import pages.AdvicesPage;
import pages.ArticlePage;
import pages.LanguageSelectionPage;
import pages.LanguageSelectionPage.Language;
import pages.MainScreenPage;

/**
 * Тесты экрана статьи из раздела Советы (открывается тапом на карточку в AdvicesPage):
 * проверяет переход, заголовок toolbar, наличие непустого названия и даты в YYYY-MM-DD,
 * paywall-текст и кнопку КУПИТЬ.
 */
public class ArticlePageTest extends BaseTest {

    private ArticlePage article;

    @BeforeMethod(alwaysRun = true)
    public void openFirstArticle() {
        new LanguageSelectionPage(driver).selectLanguage(Language.RUSSIAN);
        MainScreenPage main = new MainScreenPage(driver);
        Assert.assertTrue(main.isDisplayed(), "Main screen must be open");

        AdvicesNotificationDialog dialog = main.tapAdvices();
        Assert.assertTrue(dialog.isShown(), "Notification dialog must be shown");

        AdvicesPage advices = dialog.tapNo();
        Assert.assertTrue(advices.isDisplayed(), "Advices page must be open");

        article = advices.tapFirstArticle();
        Assert.assertTrue(article.isDisplayed(),
                "Article page must be open before each ArticlePage test");
    }

    @Test(description = "Article page opens after tapping a card in Advices list")
    public void pageOpensFromAdvicesList() {
        Assert.assertTrue(article.isDisplayed(),
                "Article page should be visible after tapping a card");
    }

    @Test(description = "Toolbar shows 'Советы' as the title on the article screen")
    public void toolbarShowsAdvicesTitle() {
        Assert.assertEquals(article.getToolbarTitle(), "Советы",
                "Toolbar title should be 'Советы'");
    }

    @Test(description = "Article shows a non-empty title")
    public void articleTitleIsShown() {
        String title = article.getTitle();
        Assert.assertNotNull(title, "Article title should not be null");
        Assert.assertFalse(title.isBlank(), "Article title should not be blank");
    }

    @Test(description = "Article date is in YYYY-MM-DD format")
    public void articleDateIsFormatted() {
        String date = article.getDate();
        Assert.assertTrue(date.matches("\\d{4}-\\d{2}-\\d{2}"),
                "Article date should match YYYY-MM-DD but was '" + date + "'");
    }

    @Test(description = "Article is paywall-gated: КУПИТЬ button visible with paywall message")
    public void buyButtonAndPaywallTextAreShown() {
        Assert.assertTrue(article.buyButton().isDisplayed(), "Buy button should be visible");
        Assert.assertTrue(article.buyButton().isEnabled(), "Buy button should be enabled");
        Assert.assertEquals(article.buyButton().getText(), "КУПИТЬ");

        Assert.assertTrue(article.hasText(ArticlePage.PAYWALL_TEXT),
                "Paywall text should mention '" + ArticlePage.PAYWALL_TEXT + "'");
    }
}
