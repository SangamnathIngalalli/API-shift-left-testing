package com.api.testing.tests;

import com.api.testing.config.ApiConfig;
import com.api.testing.models.Post;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for JSONPlaceholder Posts API
 * These tests make actual HTTP calls to the real API
 */
public class PostsIntegrationTest {
    
    private ObjectMapper objectMapper;
    
    @BeforeEach
    void setUp() {
        // Set RestAssured base URI to real JSONPlaceholder API
        RestAssured.baseURI = ApiConfig.JSON_PLACEHOLDER_BASE_URL;
        
        // Initialize ObjectMapper
        objectMapper = new ObjectMapper();
    }
    
    @Test
    void testGetAllPosts_RealApi() throws Exception {
        // Act - Make real API call
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
        assertTrue(response.getContentType().contains("application/json"));
        
        Post[] posts = objectMapper.readValue(response.getBody().asString(), Post[].class);
        assertTrue(posts.length > 0);
        
        // Verify structure of first post
        Post firstPost = posts[0];
        assertNotNull(firstPost.getId());
        assertNotNull(firstPost.getTitle());
        assertNotNull(firstPost.getBody());
        assertNotNull(firstPost.getUserId());
    }
    
    @Test
    void testGetPostById_RealApi() throws Exception {
        // Act - Make real API call
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
        assertNotNull(post.getTitle());
        assertNotNull(post.getBody());
        assertNotNull(post.getUserId());
    }
    
    @Test
    void testCreatePost_RealApi() throws Exception {
        // Arrange
        Post newPost = new Post(null, "Test Post Title", "Test Post Body", 1);
        String requestBody = objectMapper.writeValueAsString(newPost);
        
        // Act - Make real API call
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
        
        Post createdPost = objectMapper.readValue(response.getBody().asString(), Post.class);
        assertNotNull(createdPost.getId());
        assertEquals("Test Post Title", createdPost.getTitle());
        assertEquals("Test Post Body", createdPost.getBody());
        assertEquals(1, createdPost.getUserId());
    }
    
    @Test
    void testUpdatePost_RealApi() throws Exception {
        // Arrange
        Post updatedPost = new Post(ApiConfig.TEST_POST_ID, "Updated Title", "Updated Body", ApiConfig.TEST_USER_ID);
        String requestBody = objectMapper.writeValueAsString(updatedPost);
        
        // Act - Make real API call
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
        assertEquals(ApiConfig.TEST_USER_ID, post.getUserId());
    }
    
    @Test
    void testDeletePost_RealApi() {
        // Act & Assert - Make real API call
        given()
                .when()
                .delete(ApiConfig.POSTS_ENDPOINT + "/" + ApiConfig.TEST_POST_ID)
                .then()
                .statusCode(ApiConfig.HTTP_OK);
    }
    
    @Test
    void testApiResponseTime_RealApi() {
        // Act - Make real API call and measure response time
        long startTime = System.currentTimeMillis();
        
        Response response = given()
                .when()
                .get(ApiConfig.POSTS_ENDPOINT);
        
        long endTime = System.currentTimeMillis();
        long responseTime = endTime - startTime;
        
        // Assert
        assertEquals(ApiConfig.HTTP_OK, response.getStatusCode());
        assertTrue(responseTime < 5000, "Response time should be reasonable for real API call");
    }
}
