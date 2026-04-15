package base;

import com.microsoft.playwright.*;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.extension.TestWatcher;

import java.io.ByteArrayInputStream;
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
    protected Throwable testException;


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

    @RegisterExtension
    TestWatcher watcher = new TestWatcher() {
        @Override
        public void testFailed(ExtensionContext context, Throwable cause) {
            testException = cause;
        }
    };


    @AfterEach
    void tearDown() {
        if (testException != null && page != null && !page.isClosed()) {
            byte[] screenshot = page.screenshot();
            Allure.addAttachment(
                    "Screenshot on Failure",
                    "image/png",
                    new ByteArrayInputStream(screenshot),
                    ".png"
            );
        }
        context.close();
        playwright.close();
        testException = null;
    }
}