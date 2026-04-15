package base;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BaseTest {
    Playwright playwright;
    Browser browser;
    BrowserContext context;
    public Page page;
    protected String timestamp;

    @BeforeEach
    void setUp() throws IOException {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        Path recordVideoDir = Paths.get(timestamp);
        Files.createDirectories(recordVideoDir);
        context = browser.newContext(new Browser.NewContextOptions()
                .setRecordVideoDir(recordVideoDir));
        page = context.newPage();
    }



    @AfterEach
    void tearDown() {
        context.close();
        playwright.close();
    }
}