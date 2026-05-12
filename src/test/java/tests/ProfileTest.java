package tests;

import core.BaseTest;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.LanguageSelectionPage;
import pages.LanguageSelectionPage.Language;
import pages.MainScreenPage;
import pages.ProfilePage;

/**
 * Тесты экрана "Профиль" (открывается с главного по ivShowProfile):
 * проверяет переход, описание формы, поля ввода с hint'ами, чекбоксы (по умолчанию unchecked),
 * кнопку СОХРАНИТЬ, а также базовые взаимодействия (ввод текста, переключение чекбокса).
 */
public class ProfileTest extends BaseTest {

    private ProfilePage profile;

    @BeforeMethod(alwaysRun = true)
    public void openProfile() {
        new LanguageSelectionPage(driver).selectLanguage(Language.RUSSIAN);
        MainScreenPage main = new MainScreenPage(driver);
        Assert.assertTrue(main.isDisplayed(), "Main screen must be open before opening Profile");

        profile = main.tapProfile();
        Assert.assertTrue(profile.isDisplayed(),
                "Profile screen must be open before each Profile test");
    }

    @Test(description = "Profile page opens from the main screen")
    public void pageOpensFromMain() {
        Assert.assertTrue(profile.isDisplayed(),
                "Profile page should be visible after tapping profile button");
    }

    @Test(description = "Profile page shows the description text inviting the user to fill the form")
    public void descriptionTextIsShown() {
        Assert.assertTrue(profile.hasText("заполните данные ниже"),
                "Description should mention 'заполните данные ниже'");
        Assert.assertTrue(profile.hasText("авто-событий"),
                "Description should mention 'авто-событий'");
    }

    @Test(description = "All 6 input fields are shown with correct hints")
    public void allInputFieldsAreShown() {
        Assert.assertTrue(profile.nicknameInput().isDisplayed(), "Nickname input should be visible");
        Assert.assertTrue(profile.phoneInput().isDisplayed(), "Phone input should be visible");
        Assert.assertTrue(profile.emailInput().isDisplayed(), "Email input should be visible");
        Assert.assertTrue(profile.cityInput().isDisplayed(), "City input should be visible");
        Assert.assertTrue(profile.birthdayInput().isDisplayed(), "Birthday input should be visible");
        Assert.assertTrue(profile.sexInput().isDisplayed(), "Sex input should be visible");

        Assert.assertEquals(profile.getFieldHint(ProfilePage.ET_NICKNAME_ID), "Псевдоним");
        Assert.assertEquals(profile.getFieldHint(ProfilePage.ET_PHONE_ID), "Телефон");
        Assert.assertEquals(profile.getFieldHint(ProfilePage.ET_EMAIL_ID), "E-mail");
        Assert.assertEquals(profile.getFieldHint(ProfilePage.ET_CITY_ID), "Город");
        Assert.assertEquals(profile.getFieldHint(ProfilePage.ET_BIRTHDAY_ID), "Дата Рождения");
        Assert.assertEquals(profile.getFieldHint(ProfilePage.ET_SEX_ID), "Пол");
    }

    @Test(description = "All 5 checkboxes are shown and unchecked by default")
    public void allCheckboxesAreShownUnchecked() {
        WebElement[] checkboxes = {
                profile.hasAutomobileCheckbox(),
                profile.doNotNewsCheckbox(),
                profile.wantBonusCheckbox(),
                profile.wantInsuranceCheckbox(),
                profile.wantFreeCheckbox(),
        };
        for (WebElement chk : checkboxes) {
            Assert.assertTrue(chk.isDisplayed(),
                    "Checkbox should be visible: " + chk.getAttribute("resource-id"));
            Assert.assertEquals(chk.getAttribute("checked"), "false",
                    "Checkbox should be unchecked by default: " + chk.getAttribute("resource-id"));
        }
    }

    @Test(description = "Save button is shown, enabled and labeled СОХРАНИТЬ")
    public void saveButtonIsEnabled() {
        Assert.assertTrue(profile.saveButton().isDisplayed(), "Save button should be visible");
        Assert.assertTrue(profile.saveButton().isEnabled(), "Save button should be enabled");
        Assert.assertEquals(profile.saveButton().getText(), "СОХРАНИТЬ");
    }

    @Test(description = "Typing into the nickname field stores the entered text")
    public void canFillTextFields() {
        WebElement nickname = profile.nicknameInput();
        nickname.click();
        nickname.sendKeys("TestNick");
        Assert.assertEquals(nickname.getText(), "TestNick",
                "Nickname field should contain the typed value");
    }

    @Test(description = "Tapping 'У вас есть автомобиль?' checkbox toggles it on")
    public void canToggleCheckbox() {
        WebElement chk = profile.hasAutomobileCheckbox();
        Assert.assertEquals(chk.getAttribute("checked"), "false",
                "Checkbox should start as unchecked");
        chk.click();
        Assert.assertEquals(chk.getAttribute("checked"), "true",
                "Checkbox should be checked after the tap");
    }
}
