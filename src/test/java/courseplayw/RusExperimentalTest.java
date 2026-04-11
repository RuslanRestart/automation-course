package courseplayw;

import base.BaseTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RusExperimentalTest extends BaseTest {

    @Test
    public void rusTest(){
        page.navigate("https://google.com");
        assertEquals(page.title(), "Google");
    }
}
