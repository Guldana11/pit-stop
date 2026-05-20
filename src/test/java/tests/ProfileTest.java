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

    // -------- Edge cases: form validation (currently NONE on client side) --------

    @Test(description = "[edge] Saving an empty form does not show validation — stays on Profile")
    public void savingEmptyFormStaysOnProfile() {
        // Документирует текущее поведение: клиентской валидации нет, тап на СОХРАНИТЬ
        // без заполнения полей не показывает ошибку и оставляет нас на том же экране.
        // Если разработчик добавит required-валидацию — этот тест упадёт и обратит внимание.
        profile.tapSave();
        Assert.assertTrue(profile.isDisplayed(),
                "After saving empty form, should stay on Profile (no client-side validation today)");
    }

    @Test(description = "[edge] Saving with an invalid email format does not show validation")
    public void savingInvalidEmailStaysOnProfile() {
        WebElement email = profile.emailInput();
        email.click();
        email.sendKeys("not-an-email");
        profile.tapSave();
        Assert.assertTrue(profile.isDisplayed(),
                "Saving 'not-an-email' should stay on Profile — no email format validation today");
    }

    @Test(description = "[edge] After save, entered field values are preserved on Profile")
    public void savingPreservesFieldValues() {
        WebElement nickname = profile.nicknameInput();
        nickname.click();
        nickname.sendKeys("TestNick");

        profile.tapSave();
        Assert.assertTrue(profile.isDisplayed(), "Should stay on Profile after save");

        Assert.assertEquals(profile.nicknameInput().getText(), "TestNick",
                "Nickname value should still be in the input after save (form doesn't clear)");
    }

    @Test(description = "Happy path: filling valid data, tapping СОХРАНИТЬ keeps all values on Profile")
    public void savingValidDataKeepsItOnScreen() {
        // Заполняем 4 текстовых поля + один чекбокс. etBirthday/etSex пропускаем —
        // они открывают date-picker / spinner, обычным sendKeys не заполнить.
        // fillInput скрывает IME перед каждым вводом — иначе клавиатура от phone/email
        // перекрывает поле city и тап по нему упадёт с NoSuchElement.
        profile.fillInput(profile.nicknameInput(), "ValidUser");
        profile.fillInput(profile.phoneInput(), "77011234567");
        profile.fillInput(profile.emailInput(), "test@test.kz");
        profile.fillInput(profile.cityInput(), "Алматы");

        profile.hasAutomobileCheckbox().click();

        profile.tapSave();
        Assert.assertTrue(profile.isDisplayed(),
                "Should stay on Profile after saving valid data");

        Assert.assertEquals(profile.nicknameInput().getText(), "ValidUser",
                "Nickname value should be preserved after save");
        Assert.assertEquals(profile.emailInput().getText(), "test@test.kz",
                "Email value should be preserved after save");
        Assert.assertEquals(profile.cityInput().getText(), "Алматы",
                "City value should be preserved after save");
        Assert.assertEquals(profile.hasAutomobileCheckbox().getAttribute("checked"), "true",
                "'У вас есть автомобиль?' checkbox should remain checked");
    }
}
