package org.bookstore.automation.tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import org.bookstore.automation.utils.ApiBaseTest;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookManagementTests extends ApiBaseTest {
    private static final Logger logger = LoggerFactory.getLogger(BookManagementTests.class);
    private static String bookId;
    private static String accessToken;



    @BeforeAll
    public static void getToken() throws JsonProcessingException {

        // Parse the response body
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> data = new HashMap<>();
        data.put("email","test");
        data.put("password","test");
        APIResponse response = apiRequestContext.post("/login", RequestOptions.create().setData(data));
        String responseBody = response.text();


        JsonNode jsonNode = objectMapper.readTree(responseBody);

        // Extract the book ID
        accessToken = jsonNode.get("access_token").asText();
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + accessToken);
        headers.put("Content-Type", "application/json");

        APIRequest.NewContextOptions options = new APIRequest.NewContextOptions()
                .setBaseURL("http://localhost:8000")
                .setExtraHTTPHeaders(headers);

        apiRequestContext = playwright.request().newContext(options);
    }



    @Test
    @Order(1)
    public void testCreateBook() throws JsonProcessingException {
        String requestBody = "{\"name\":\"test4\",\"author\":\"test2\",\"published_year\":\"1990\",\"book_summary\":\"testab\"}";

        RequestOptions options = RequestOptions.create()
                .setData(requestBody);

        APIResponse response = apiRequestContext.post("/books/", options);
        assertEquals(200, response.status(), "Expected status code 201 for book creation.");

        // Parse the response body
        String responseBody = response.text();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        // Extract the book ID
        bookId = jsonNode.get("id").asText();

        // Assert that the book ID is not null or empty
        assertNotNull(bookId, "Book ID should not be null.");
        assertFalse(bookId.isEmpty(), "Book ID should not be empty.");


    }


    @Test
    @Order(2)
    public void testGetBookById() throws JsonProcessingException {
        APIResponse response = apiRequestContext.get("/books/" + bookId);
        assertEquals(200, response.status());
        String responseBody = response.text();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        logger.info("abc"+jsonNode);
        assertEquals("test4", jsonNode.get("name").textValue());
    }

    @Test
    @Order(3)
    public void testUpdateBook() throws JsonProcessingException {
        String updatedBody = "{\"name\":\"Animal Farm\",\"author\":\"test2\",\"published_year\":\"2000\",\"book_summary\":\"testab\"}";
        APIResponse response = apiRequestContext.put("/books/" + bookId, RequestOptions.create().setData(updatedBody));
        assertEquals(200, response.status());
        String responseBody = response.text();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        assertEquals("Animal Farm", jsonNode.get("name").textValue());
    }

    @Test
    @Order(4)
    public void testGetAllBooks() {
        APIResponse response = apiRequestContext.get("/books/");
        assertEquals(200, response.status());
        assertTrue(response.text().contains("Animal Farm"));
    }

    @Test
    @Order(5)
    public void testDeleteBook() {
        APIResponse response = apiRequestContext.delete("/books/" + bookId);
        assertEquals(200, response.status());
    }
    @Test
    @Order(6)
    public void testGetBookByInvalidId() throws JsonProcessingException {
        APIResponse response = apiRequestContext.get("/books/" + "a");
        assertEquals(422, response.status());
        String responseBody = response.text();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        String errorType=null;
        for (JsonNode arrayItem : jsonNode.get("detail")) {
           errorType=arrayItem.get("type").textValue();
        }

        assertEquals("int_parsing",errorType);
    }
    @Test
    @Order(7)
    public void testUpdateInvalidBook() throws JsonProcessingException {
        String updatedBody = "{\"name\":\"Animal Farm\",\"author\":\"test2\",\"published_year\":\"2000\",\"book_summary\":\"testab\"}";
        APIResponse response = apiRequestContext.put("/books/" + 200, RequestOptions.create().setData(updatedBody));
        assertEquals(404, response.status());
        String responseBody = response.text();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        String errorType="Book not found";
        for (JsonNode arrayItem : jsonNode.get("detail")) {
             errorType = arrayItem.get("type").textValue();
        }
        assertEquals("Book not found", errorType);
    }

    @Test
    @Order(8)
    public void testInvalidCreateBook() throws JsonProcessingException {
        String requestBody = "{}";

        RequestOptions options = RequestOptions.create()
                .setData(requestBody);

        APIResponse response = apiRequestContext.post("/books/", options);
        assertEquals(500, response.status(), "Expected status code 201 for book creation.");

        // Parse the response body
        String responseBody = response.text();


       assertEquals("Internal Server Error",responseBody);




    }




}
