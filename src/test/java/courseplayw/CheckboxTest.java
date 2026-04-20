package courseplayw;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitUntilState;
import io.qameta.allure.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("Веб-интерфейс тестов")
@Feature("Операции с чекбоксами")
public class CheckboxTest {
    private Playwright playwright;
    private Browser browser;
    private BrowserContext context;
    private Page page;

    @BeforeEach
    @Step("Инициализация браузера и контекста")
    void setUp() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        context = browser.newContext();
        page = context.newPage();
    }

    @Test
    @Story("Проверка работы чекбоксов")
    @DisplayName("Тестирование выбора/снятия чекбоксов")
    @Severity(SeverityLevel.CRITICAL)
    void testCheckboxes() {
        navigateToCheckboxesPage();
        verifyInitialState();
        toggleCheckboxes();
        verifyToggledState();
    }

    @Step("Проверка конечного состояния чекбоксов")
    private void verifyToggledState() {
        assertTrue(page.locator("//input[@type='checkbox']").first().isChecked(),
                "First checkbox must be checked after checking!");
        assertFalse(page.locator("//input[@type='checkbox']").last().isChecked(),
                "Second checkbox must be unchecked after unchecking!");
        Allure.addAttachment("Скриншот конечного состояния", "image/png",
                Arrays.toString(page.screenshot(new Page.ScreenshotOptions()
                        .setPath(Paths.get("screenshots/screenshotFinalState_" + RandomStringUtils.randomAlphabetic(5) + ".png")))));
    }

    @Step("Переход на страницу /checkboxes")
    private void navigateToCheckboxesPage() {
        page.navigate("https://the-internet.herokuapp.com/checkboxes",
                new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));
        Allure.addAttachment("Скриншот навигации", "image/png",
                Arrays.toString(page.screenshot(new Page.ScreenshotOptions()
                        .setPath(Paths.get("screenshots/screenshotNavigation_" + RandomStringUtils.randomAlphabetic(5) + ".png")))), ".png");
    }

    @Step("Проверка начального состояния чекбоксов")
    private void verifyInitialState() {
        assertFalse(page.locator("//input[@type='checkbox']").first().isChecked(),
                "First checkbox must be unchecked!");
        assertTrue(page.locator("//input[@type='checkbox']").last().isChecked(),
                "Second checkbox must be checked!");
        Allure.addAttachment("Скриншот начального состояния", "image/png",
                Arrays.toString(page.screenshot(new Page.ScreenshotOptions()
                        .setPath(Paths.get("screenshots/screenshotStartState_" + RandomStringUtils.randomAlphabetic(5) + ".png")))));
    }

    @Step("Изменение состояния чекбоксов")
    private void toggleCheckboxes() {
        page.locator("//input[@type='checkbox']").first().check();
        Allure.addAttachment("Скриншот после включения первого чекбокса", "image/png",
                Arrays.toString(page.screenshot(new Page.ScreenshotOptions()
                        .setPath(Paths.get("screenshots/screenshotChecking_" + RandomStringUtils.randomAlphabetic(5) + ".png")))));
        page.locator("//input[@type='checkbox']").last().uncheck();
        Allure.addAttachment("Скриншот после выключения второго чекбокса", "image/png",
                Arrays.toString(page.screenshot(new Page.ScreenshotOptions()
                        .setPath(Paths.get("screenshots/screenshotUnchecking_" + RandomStringUtils.randomAlphabetic(5) + ".png")))));
    }



    @AfterEach
    @Step("Закрытие ресурсов")
    void tearDown() {
        context.close();
        browser.close();
        playwright.close();
    }
}
