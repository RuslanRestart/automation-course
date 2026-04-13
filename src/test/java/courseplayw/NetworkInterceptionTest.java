package courseplayw;

import base.BaseTest;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Route;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class NetworkInterceptionTest extends BaseTest {

    @Test
    public void mockResponseTest(){
        String url = "https://the-internet.herokuapp.com/status_codes";
        String mockedBody = "Mocked Success Response";

        page.route(url + "/404", route -> {
            route.fulfill(new Route.FulfillOptions()
                    .setStatus(200)
                    .setContentType("application/json")
                    .setBody(mockedBody));
                }
        );
        page.navigate(url);
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("404")).click();
        assertTrue(page.textContent("body").contains(mockedBody), "Mocked message isn't displayed!");

    }
}
