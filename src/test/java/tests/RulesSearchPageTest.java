package tests;

import core.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.LanguageSelectionPage;
import pages.LanguageSelectionPage.Language;
import pages.MainScreenPage;
import pages.RulesPage;
import pages.RulesSearchPage;

/**
 * Тесты экрана поиска по правилам (открывается тапом на кнопку search в RulesPage):
 * проверяет переход, hint поля ввода, наличие кнопок очистки и поиска,
 * а также возможность ввода текста.
 */
public class RulesSearchPageTest extends BaseTest {

    private RulesSearchPage search;

    @BeforeMethod(alwaysRun = true)
    public void openSearch() {
        new LanguageSelectionPage(driver).selectLanguage(Language.RUSSIAN);
        MainScreenPage main = new MainScreenPage(driver);
        Assert.assertTrue(main.isDisplayed(), "Main screen must be open");

        RulesPage rules = main.tapRules();
        Assert.assertTrue(rules.isDisplayed(), "Rules screen must be open before opening search");

        search = rules.tapSearch();
        Assert.assertTrue(search.isDisplayed(),
                "Rules search page must be open before each RulesSearchPage test");
    }

    @Test(description = "Search page opens after tapping the search button on Rules")
    public void pageOpensFromRulesScreen() {
        Assert.assertTrue(search.isDisplayed(),
                "Search page should be visible after tapping the search button");
    }

    @Test(description = "Search input has the expected hint 'Введите текст'")
    public void inputFieldHasHint() {
        Assert.assertTrue(search.searchInput().isDisplayed(),
                "Search input should be visible");
        Assert.assertEquals(search.getInputHint(), "Введите текст",
                "Search input hint should be 'Введите текст'");
    }

    @Test(description = "Clear and Search buttons are shown and enabled")
    public void searchAndCloseButtonsAreShown() {
        Assert.assertTrue(search.clearButton().isDisplayed(), "Clear button should be visible");
        Assert.assertTrue(search.clearButton().isEnabled(), "Clear button should be enabled");

        Assert.assertTrue(search.searchButton().isDisplayed(), "Search button should be visible");
        Assert.assertTrue(search.searchButton().isEnabled(), "Search button should be enabled");
    }

    @Test(description = "Typing a query into the input stores the entered text")
    public void typingFillsTheField() {
        String query = "обгон";
        search.typeQuery(query);
        Assert.assertEquals(search.getInputText(), query,
                "Search input should contain the typed value '" + query + "'");
    }
}
