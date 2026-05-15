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
 * Экран "Правила" (ПДД) — открывается с главного экрана по btnRulesMain.
 * Содержит горизонтальный список табов (ПДД РК / ЗНАКИ / РАЗМЕТКА / ШТРАФЫ),
 * pager с содержимым выбранного таба (ListView с главами) и кнопку поиска.
 */
public class RulesPage extends BasePage {

    private static final String PKG = "kz.crystalspring.pit_stop_kz";

    public static final String INDICATOR_ID = PKG + ":id/indicator";
    public static final String PAGER_ID = PKG + ":id/pager";
    public static final String LIST_VIEW_ID = PKG + ":id/listView1";
    public static final String RULE_TITLE_ID = PKG + ":id/txtViewTitleMain";
    public static final String SEARCH_ID = PKG + ":id/search";

    public static final String TAB_PDD = "ПДД РК";
    public static final String TAB_SIGNS = "ЗНАКИ";
    public static final String TAB_MARKINGS = "РАЗМЕТКА";
    public static final String TAB_FINES = "ШТРАФЫ";

    public RulesPage(AppiumDriver driver) {
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
            // indicator (табы) + pager (контент) — оба специфичны для этого экрана.
            new WebDriverWait(driver, timeout)
                    .ignoring(WebDriverException.class)
                    .until(d -> !d.findElements(AppiumBy.id(INDICATOR_ID)).isEmpty()
                            && !d.findElements(AppiumBy.id(PAGER_ID)).isEmpty());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public List<String> getTabLabels() {
        // Табы — TextView'ы внутри indicator. У них нет своего id, ищем по родителю.
        String selector = String.format(
                "new UiSelector().resourceId(\"%s\").childSelector(new UiSelector().className(\"android.widget.TextView\"))",
                INDICATOR_ID);
        // childSelector возвращает только один — нужно собрать всех потомков иначе.
        // Используем resourceIdMatches и обходим всех TextView в indicator вручную:
        return driver.findElements(AppiumBy.xpath(
                String.format("//*[@resource-id='%s']//android.widget.TextView", INDICATOR_ID)
        )).stream().map(WebElement::getText).collect(Collectors.toList());
    }

    public String getSelectedTab() {
        List<WebElement> tabs = driver.findElements(AppiumBy.xpath(
                String.format("//*[@resource-id='%s']//android.widget.TextView", INDICATOR_ID)
        ));
        return tabs.stream()
                .filter(t -> "true".equals(t.getAttribute("selected")))
                .map(WebElement::getText)
                .findFirst()
                .orElse(null);
    }

    public void tapTab(String label) {
        String selector = String.format("new UiSelector().text(\"%s\")", label);
        driver.findElement(AppiumBy.androidUIAutomator(selector)).click();
    }

    public List<String> getRuleTitles() {
        return driver.findElements(AppiumBy.id(RULE_TITLE_ID))
                .stream().map(WebElement::getText).collect(Collectors.toList());
    }

    public boolean hasRule(String text) {
        return getRuleTitles().stream().anyMatch(t -> t.contains(text));
    }

    public WebElement searchButton() {
        return driver.findElement(AppiumBy.id(SEARCH_ID));
    }

    public RuleChapterPage tapChapter(String title) {
        // ListView с главами рендерится после загрузки экрана Rules — ждём появления
        // конкретной главы до 30с, иначе на холодном кэше findElement упадёт раньше времени.
        String selector = String.format("new UiSelector().text(\"%s\")", title);
        WebElement chapter = new WebDriverWait(driver, Duration.ofSeconds(30))
                .ignoring(WebDriverException.class)
                .until(d -> {
                    var found = d.findElements(AppiumBy.androidUIAutomator(selector));
                    return found.isEmpty() ? null : found.get(0);
                });
        chapter.click();
        return new RuleChapterPage(driver);
    }

    public RulesSearchPage tapSearch() {
        searchButton().click();
        return new RulesSearchPage(driver);
    }
}
