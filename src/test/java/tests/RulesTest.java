package tests;

import core.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.LanguageSelectionPage;
import pages.LanguageSelectionPage.Language;
import pages.MainScreenPage;
import pages.RulesPage;

import java.util.List;

/**
 * Тесты экрана "Правила" (открывается с главного по btnRulesMain):
 * проверяет переход, наличие 4 табов, выбранный по умолчанию таб,
 * первые главы ПДД и кнопку поиска.
 */
public class RulesTest extends BaseTest {

    private RulesPage rules;

    @BeforeMethod(alwaysRun = true)
    public void openRules() {
        new LanguageSelectionPage(driver).selectLanguage(Language.RUSSIAN);
        MainScreenPage main = new MainScreenPage(driver);
        Assert.assertTrue(main.isDisplayed(), "Main screen must be open before opening Rules");

        rules = main.tapRules();
        Assert.assertTrue(rules.isDisplayed(),
                "Rules screen must be open before each Rules test");
    }

    @Test(description = "Rules screen shows all 4 tabs with correct labels")
    public void allFourTabsAreShown() {
        List<String> labels = rules.getTabLabels();
        Assert.assertEquals(labels, List.of(
                RulesPage.TAB_PDD,
                RulesPage.TAB_SIGNS,
                RulesPage.TAB_MARKINGS,
                RulesPage.TAB_FINES
        ), "Tabs should be ПДД РК / ЗНАКИ / РАЗМЕТКА / ШТРАФЫ");
    }

    @Test(description = "PDD tab is selected by default when Rules screen opens")
    public void pddTabIsSelectedByDefault() {
        Assert.assertEquals(rules.getSelectedTab(), RulesPage.TAB_PDD,
                "By default the 'ПДД РК' tab should be selected");
    }

    @Test(description = "PDD tab lists the first chapters")
    public void firstPddChaptersAreShown() {
        // Проверяем несколько первых глав, видимых без скролла.
        Assert.assertTrue(rules.hasRule("1. Общие положения"),
                "Chapter '1. Общие положения' should be present");
        Assert.assertTrue(rules.hasRule("2. Общие обязанности водителей"),
                "Chapter '2. Общие обязанности водителей' should be present");
        Assert.assertTrue(rules.hasRule("3. Обязанности пешеходов"),
                "Chapter '3. Обязанности пешеходов' should be present");
        Assert.assertTrue(rules.hasRule("4. Обязанности пассажиров"),
                "Chapter '4. Обязанности пассажиров' should be present");
    }

    @Test(description = "Search button is shown and clickable")
    public void searchButtonIsShown() {
        Assert.assertTrue(rules.searchButton().isDisplayed(),
                "Search button should be visible");
        Assert.assertTrue(rules.searchButton().isEnabled(),
                "Search button should be enabled");
    }

    @Test(description = "Tapping the ЗНАКИ tab makes it the selected one")
    public void tappingSignsTabChangesSelection() {
        rules.tapTab(RulesPage.TAB_SIGNS);
        Assert.assertEquals(rules.getSelectedTab(), RulesPage.TAB_SIGNS,
                "After tap, the 'ЗНАКИ' tab should be selected");
    }

    @Test(description = "Switching from ПДД РК to ЗНАКИ changes the list content")
    public void tappingTabChangesContent() {
        List<String> pddTitles = rules.getRuleTitles();
        Assert.assertFalse(pddTitles.isEmpty(), "PDD tab should have rule titles");

        rules.tapTab(RulesPage.TAB_SIGNS);
        Assert.assertEquals(rules.getSelectedTab(), RulesPage.TAB_SIGNS,
                "Tab should have switched to ЗНАКИ");

        List<String> signTitles = rules.getRuleTitles();
        Assert.assertNotEquals(signTitles, pddTitles,
                "List content should change after switching tabs");
    }
}
