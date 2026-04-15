package courseplayw;

import base.BaseTest;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

public class CartTest extends BaseTest {

        @Test
        void testCartActions() {
            page.navigate("https://the-internet.herokuapp.com/add_remove_elements/");

            // Добавление товара
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Add Element")).click();
            page.locator("#content").screenshot(new Locator.ScreenshotOptions()
                    .setPath(getTimestampPath("cart_after_add.png")));

            // Удаление товара
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Delete")).click();
            page.locator("#content").screenshot(new Locator.ScreenshotOptions()
                    .setPath(getTimestampPath("cart_after_remove.png")));
        }

        private Path getTimestampPath(String filename) {
            return Paths.get(timestamp, filename);
        }
    }
