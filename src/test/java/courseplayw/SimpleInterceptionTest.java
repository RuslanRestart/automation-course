package courseplayw;

import base.BaseTest;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Route;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitUntilState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SimpleInterceptionTest extends BaseTest {

    @Test
    public void simpleInterceptionTest(){
        String url = "https://the-internet.herokuapp.com/login";
        String username = "tomsmith";

        page.route("**/authenticate", route -> {
            System.out.println("Запрос перехвачен! Url: " + route.request().url());
            String originalData = route.request().postData();
            System.out.println("Было: " + originalData);

            String modifiedUsername = originalData.replace(username, "HACKED_USER");
            System.out.println("Стало: " + modifiedUsername);

            Route.ResumeOptions resumeOptions = new Route.ResumeOptions()
                    .setPostData(modifiedUsername);
            route.resume(resumeOptions);
        });

        page.navigate(url, new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));
        page.locator("//input[@id='username']").fill(username);
        page.locator("//input[@id='password']").fill("qwerty123");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(" Login")).click();

        Locator error = page.locator("//div[@class='flash error']");
        error.waitFor();
        assertTrue(error.textContent().contains("Your username is invalid!"), "Error message is not displayed!");
    }
}
