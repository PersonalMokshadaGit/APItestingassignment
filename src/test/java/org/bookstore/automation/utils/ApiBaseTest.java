package org.bookstore.automation.utils;

import com.microsoft.playwright.*;
        import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Value;

public class ApiBaseTest {
    @Value("${base-url}")
    private int serverPort;

    protected static Playwright playwright;
    protected static APIRequestContext apiRequestContext;

    @BeforeAll
    public static void setUp() {
        playwright = Playwright.create();
        APIRequest.NewContextOptions options = new APIRequest.NewContextOptions()
                .setBaseURL("http://localhost:8000"); // Replace with your API base URL
        apiRequestContext = playwright.request().newContext(options);
    }

    @AfterAll
    public static void tearDown() {
        apiRequestContext.dispose();
        playwright.close();
    }
}
