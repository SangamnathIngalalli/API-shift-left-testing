
package com.api.testing.wiremock;

import com.api.testing.config.ApiConfig;
import com.api.testing.models.Post;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

/**
 * WireMock tests for JSONPlaceholder Posts API
 * These tests demonstrate how to mock external API responses for testing
 */
public class PostsWireMockTest {
    
    private WireMockServer wireMockServer;
    private ObjectMapper objectMapper;
    
    @BeforeEach
    void setUp() {
        // Initialize WireMock server with dynamic port
        wireMockServer = new WireMockServer(0); // 0 means use any available port
        wireMockServer.start();
        
        // Configure WireMock
        WireMock.configureFor("localhost", wireMockServer.port());
        
        // Initialize ObjectMapper for JSON processing
        objectMapper = new ObjectMapper();
        
        // Set RestAssured base URI to WireMock
        RestAssured.baseURI = "http://localhost:" + wireMockServer.port();
    }
    
    @AfterEach
    void tearDown() {
        // Stop WireMock server
        wireMockServer.stop();
        
        // Reset RestAssured
        RestAssured.reset();
    }
    
    @Test
    void testGetAllPosts_Success() throws Exception {
        // Arrange - Setup WireMock stub
        Post[] mockPosts = {
            new Post(1, "Test Post 1", "Body of test post 1", 1),
            new Post(2, "Test Post 2", "Body of test post 2", 2)
        };
        
        String mockResponse = objectMapper.writeValueAsString(mockPosts);
        
        stubFor(get(urlEqualTo(ApiConfig.POSTS_ENDPOINT))
                .willReturn(aResponse()
                        .withStatus(ApiConfig.HTTP_OK)
                        .withHeader("Content-Type", "application/json")
                        .withBody(mockResponse)));
        
        // Act - Make API call
        Response response = given()
                .when()
                .get(ApiConfig.POSTS_ENDPOINT)
                .then()
                .statusCode(ApiConfig.HTTP_OK)
                .extract()
                .response();
        
        // Assert
        assertNotNull(response);
        assertEquals(ApiConfig.HTTP_OK, response.getStatusCode());
        assertEquals("application/json", response.getContentType());
        
        Post[] posts = objectMapper.readValue(response.getBody().asString(), Post[].class);
        assertEquals(2, posts.length);
        assertEquals("Test Post 1", posts[0].getTitle());
        assertEquals("Test Post 2", posts[1].getTitle());
        
        // Verify WireMock was called
        verify(getRequestedFor(urlEqualTo(ApiConfig.POSTS_ENDPOINT)));
    }
    
    @Test
    void testGetPostById_Success() throws Exception {
        // Arrange - Setup WireMock stub
        Post mockPost = new Post(ApiConfig.TEST_POST_ID, ApiConfig.TEST_POST_TITLE, ApiConfig.TEST_POST_BODY, ApiConfig.TEST_USER_ID);
        String mockResponse = objectMapper.writeValueAsString(mockPost);
        
        stubFor(get(urlEqualTo(ApiConfig.POSTS_ENDPOINT + "/" + ApiConfig.TEST_POST_ID))
                .willReturn(aResponse()
                        .withStatus(ApiConfig.HTTP_OK)
                        .withHeader("Content-Type", "application/json")
                        .withBody(mockResponse)));
        
        // Act - Make API call
        Response response = given()
                .when()
                .get(ApiConfig.POSTS_ENDPOINT + "/" + ApiConfig.TEST_POST_ID)
                .then()
                .statusCode(ApiConfig.HTTP_OK)
                .extract()
                .response();
        
        // Assert
        assertNotNull(response);
        assertEquals(ApiConfig.HTTP_OK, response.getStatusCode());
        
        Post post = objectMapper.readValue(response.getBody().asString(), Post.class);
        assertEquals(ApiConfig.TEST_POST_ID, post.getId());
        assertEquals(ApiConfig.TEST_POST_TITLE, post.getTitle());
        assertEquals(ApiConfig.TEST_POST_BODY, post.getBody());
        assertEquals(ApiConfig.TEST_USER_ID, post.getUserId());
        
        // Verify WireMock was called
        verify(getRequestedFor(urlEqualTo(ApiConfig.POSTS_ENDPOINT + "/" + ApiConfig.TEST_POST_ID)));
    }
    
    @Test
    void testGetPostById_NotFound() {
        // Arrange - Setup WireMock stub for 404 response
        stubFor(get(urlEqualTo(ApiConfig.POSTS_ENDPOINT + "/999"))
                .willReturn(aResponse()
                        .withStatus(ApiConfig.HTTP_NOT_FOUND)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{}")));
        
        // Act & Assert - Make API call and verify 404 response
        given()
                .when()
                .get(ApiConfig.POSTS_ENDPOINT + "/999")
                .then()
                .statusCode(ApiConfig.HTTP_NOT_FOUND);
        
        // Verify WireMock was called
        verify(getRequestedFor(urlEqualTo(ApiConfig.POSTS_ENDPOINT + "/999")));
    }
    
    @Test
    void testCreatePost_Success() throws Exception {
        // Arrange - Setup WireMock stub
        Post newPost = new Post(null, "New Post Title", "New Post Body", 1);
        Post createdPost = new Post(101, "New Post Title", "New Post Body", 1);
        String requestBody = objectMapper.writeValueAsString(newPost);
        String responseBody = objectMapper.writeValueAsString(createdPost);
        
        stubFor(post(urlEqualTo(ApiConfig.POSTS_ENDPOINT))
                .withRequestBody(equalToJson(requestBody))
                .willReturn(aResponse()
                        .withStatus(ApiConfig.HTTP_CREATED)
                        .withHeader("Content-Type", "application/json")
                        .withBody(responseBody)));
        
        // Act - Make API call
        Response response = given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post(ApiConfig.POSTS_ENDPOINT)
                .then()
                .statusCode(ApiConfig.HTTP_CREATED)
                .extract()
                .response();
        
        // Assert
        assertNotNull(response);
        assertEquals(ApiConfig.HTTP_CREATED, response.getStatusCode());
        
        Post post = objectMapper.readValue(response.getBody().asString(), Post.class);
        assertEquals(101, post.getId());
        assertEquals("New Post Title", post.getTitle());
        assertEquals("New Post Body", post.getBody());
        assertEquals(1, post.getUserId());
        
        // Verify WireMock was called
        verify(postRequestedFor(urlEqualTo(ApiConfig.POSTS_ENDPOINT))
                .withRequestBody(equalToJson(requestBody)));
    }
    
    @Test
    void testUpdatePost_Success() throws Exception {
        // Arrange - Setup WireMock stub
        Post updatedPost = new Post(ApiConfig.TEST_POST_ID, "Updated Title", "Updated Body", ApiConfig.TEST_USER_ID);
        String requestBody = objectMapper.writeValueAsString(updatedPost);
        
        stubFor(put(urlEqualTo(ApiConfig.POSTS_ENDPOINT + "/" + ApiConfig.TEST_POST_ID))
                .withRequestBody(equalToJson(requestBody))
                .willReturn(aResponse()
                        .withStatus(ApiConfig.HTTP_OK)
                        .withHeader("Content-Type", "application/json")
                        .withBody(requestBody)));
        
        // Act - Make API call
        Response response = given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .put(ApiConfig.POSTS_ENDPOINT + "/" + ApiConfig.TEST_POST_ID)
                .then()
                .statusCode(ApiConfig.HTTP_OK)
                .extract()
                .response();
        
        // Assert
        assertNotNull(response);
        assertEquals(ApiConfig.HTTP_OK, response.getStatusCode());
        
        Post post = objectMapper.readValue(response.getBody().asString(), Post.class);
        assertEquals(ApiConfig.TEST_POST_ID, post.getId());
        assertEquals("Updated Title", post.getTitle());
        assertEquals("Updated Body", post.getBody());
        
        // Verify WireMock was called
        verify(putRequestedFor(urlEqualTo(ApiConfig.POSTS_ENDPOINT + "/" + ApiConfig.TEST_POST_ID))
                .withRequestBody(equalToJson(requestBody)));
    }
    
    @Test
    void testDeletePost_Success() {
        // Arrange - Setup WireMock stub
        stubFor(delete(urlEqualTo(ApiConfig.POSTS_ENDPOINT + "/" + ApiConfig.TEST_POST_ID))
                .willReturn(aResponse()
                        .withStatus(ApiConfig.HTTP_OK)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{}")));
        
        // Act & Assert - Make API call and verify response
        given()
                .when()
                .delete(ApiConfig.POSTS_ENDPOINT + "/" + ApiConfig.TEST_POST_ID)
                .then()
                .statusCode(ApiConfig.HTTP_OK);
        
        // Verify WireMock was called
        verify(deleteRequestedFor(urlEqualTo(ApiConfig.POSTS_ENDPOINT + "/" + ApiConfig.TEST_POST_ID)));
    }
    
    @Test
    void testApiResponseTime() {
        // Arrange - Setup WireMock stub with delay
        stubFor(get(urlEqualTo(ApiConfig.POSTS_ENDPOINT))
                .willReturn(aResponse()
                        .withStatus(ApiConfig.HTTP_OK)
                        .withHeader("Content-Type", "application/json")
                        .withBody("[{\"id\":1,\"title\":\"Test\",\"body\":\"Test Body\",\"userId\":1}]")
                        .withFixedDelay(100))); // 100ms delay
        
        // Act - Make API call and measure response time
        long startTime = System.currentTimeMillis();
        
        Response response = given()
                .when()
                .get(ApiConfig.POSTS_ENDPOINT);
        
        long endTime = System.currentTimeMillis();
        long responseTime = endTime - startTime;
        
        // Assert
        assertEquals(ApiConfig.HTTP_OK, response.getStatusCode());
        assertTrue(responseTime >= 100, "Response time should be at least 100ms due to WireMock delay");
        assertTrue(responseTime < 1000, "Response time should be reasonable");
    }
}
