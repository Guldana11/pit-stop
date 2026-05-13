package tests;

import core.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.LanguageSelectionPage;
import pages.LanguageSelectionPage.Language;
import pages.MainScreenPage;
import pages.RuleChapterPage;
import pages.RulesPage;

/**
 * Тесты экрана содержимого главы ПДД (открывается тапом на главу в RulesPage):
 * проверяет переход, наличие заголовка главы в тексте, наличие первого подпункта,
 * а также видимость скроллируемой области с текстом.
 */
public class RuleChapterPageTest extends BaseTest {

    private static final String FIRST_CHAPTER = "1. Общие положения";

    private RuleChapterPage chapter;

    @BeforeMethod(alwaysRun = true)
    public void openFirstChapter() {
        new LanguageSelectionPage(driver).selectLanguage(Language.RUSSIAN);
        MainScreenPage main = new MainScreenPage(driver);
        Assert.assertTrue(main.isDisplayed(), "Main screen must be open");

        RulesPage rules = main.tapRules();
        Assert.assertTrue(rules.isDisplayed(), "Rules screen must be open before opening a chapter");

        chapter = rules.tapChapter(FIRST_CHAPTER);
        Assert.assertTrue(chapter.isDisplayed(),
                "Rule chapter page must be open before each RuleChapter test");
    }

    @Test(description = "Chapter page opens after tapping a chapter title in the rules list")
    public void pageOpensFromRulesList() {
        Assert.assertTrue(chapter.isDisplayed(),
                "Chapter page should be visible after tapping a chapter");
    }

    @Test(description = "Chapter text starts with the chapter title")
    public void titleIsInRulesText() {
        Assert.assertTrue(chapter.hasText(FIRST_CHAPTER),
                "Chapter text should contain the title '" + FIRST_CHAPTER + "'");
    }

    @Test(description = "Chapter text contains the first subsection number (1.1)")
    public void firstSubsectionIsPresent() {
        Assert.assertTrue(chapter.hasText("1.1"),
                "Chapter text should mention the first subsection '1.1'");
    }

    @Test(description = "Scrollable detail area is shown")
    public void detailScrollIsDisplayed() {
        Assert.assertTrue(chapter.detailScroll().isDisplayed(),
                "Detail scroll view should be visible");
        Assert.assertEquals(chapter.detailScroll().getAttribute("scrollable"), "true",
                "Detail scroll view should be scrollable");
    }
}
