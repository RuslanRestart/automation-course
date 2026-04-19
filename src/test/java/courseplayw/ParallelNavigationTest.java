package courseplayw;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Execution(ExecutionMode.CONCURRENT)
public class ParallelNavigationTest {

    @ParameterizedTest
    @MethodSource("browserAndPaths")
    void testPageLoad(String browserType, String path, String expectedText) {
        try (Playwright playwright = Playwright.create()) {
            BrowserType type = switch (browserType) {
                case "chromium" -> playwright.chromium();
                case "firefox" -> playwright.firefox();
                default -> throw new IllegalArgumentException("Неподдерживаемый браузер: " + browserType);
            };

            try (Browser browser = type.launch(new BrowserType.LaunchOptions().setHeadless(true))) {
                try (BrowserContext context = browser.newContext()) {
                    Page page = context.newPage();
                    page.navigate("https://the-internet.herokuapp.com" + path);
                    assertEquals(expectedText, page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName(expectedText)).textContent(), "Текст заголовка не соответствует!");
                }
            }
        }
    }

    static Stream<Arguments> browserAndPaths() {
        return Stream.of(
                Arguments.of("chromium", "/", "Welcome to the-internet"),
                Arguments.of("chromium", "/login", "Login Page"),
                Arguments.of("firefox", "/dropdown", "Dropdown List"),
                Arguments.of("firefox", "/checkboxes", "Checkboxes")
        );
    }
}
