package courseplayw;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import static org.junit.jupiter.api.Assertions.*;

@Execution(ExecutionMode.CONCURRENT)
public class ParallelTests{
    private Playwright playwright;
    private Browser browser;

    @BeforeEach
    void setUp(){
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
    }

    @Test
    void testLoginPage(){
        try (BrowserContext context = browser.newContext()){
            Page page = context.newPage();

            page.navigate("https://the-internet.herokuapp.com/login");
            assertEquals("Login Page", page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Login Page")).textContent(), "Заголовок Login Page не отобразился!");
        }
    }

    @Test
    void testAddRemoveElements() {
        try(BrowserContext context = browser.newContext()) {
            Page page = context.newPage();
            Locator deleteButton = page.locator("button.added-manually");

            page.navigate("https://the-internet.herokuapp.com/add_remove_elements/");
            page.click("button:text('Add Element')");
            assertTrue(deleteButton.isVisible(), "Кнопка удаления не отобразилась!");

            deleteButton.click();
            assertFalse(deleteButton.isVisible(), "Кнопка удаления не исчезла!");
        }
    }

    @AfterEach
    void teardown() {
        browser.close();
        playwright.close();
    }

}
