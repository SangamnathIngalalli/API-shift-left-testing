package com.api.testing.config;

/**
 * Configuration class for API testing
 * Contains constants and configuration values for JSONPlaceholder API
 */
public class ApiConfig {
    
    // JSONPlaceholder API base URL
    public static final String JSON_PLACEHOLDER_BASE_URL = "https://jsonplaceholder.typicode.com";
    
    // API endpoints
    public static final String POSTS_ENDPOINT = "/posts";
    public static final String POST_BY_ID_ENDPOINT = "/posts/{id}";
    
    // WireMock configuration
    public static final int WIREMOCK_PORT = 8089;
    public static final String WIREMOCK_BASE_URL = "http://localhost:" + WIREMOCK_PORT;
    
    // Test data
    public static final int TEST_POST_ID = 1;
    public static final int TEST_USER_ID = 1;
    public static final String TEST_POST_TITLE = "Test Post Title";
    public static final String TEST_POST_BODY = "Test Post Body Content";
    
    // HTTP status codes
    public static final int HTTP_OK = 200;
    public static final int HTTP_CREATED = 201;
    public static final int HTTP_NOT_FOUND = 404;
    public static final int HTTP_INTERNAL_SERVER_ERROR = 500;
    
    private ApiConfig() {
        // Utility class - prevent instantiation
    }
}
