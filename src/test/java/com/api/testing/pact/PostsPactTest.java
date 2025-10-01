package com.api.testing.pact;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslJsonArray;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import com.api.testing.config.ApiConfig;
import com.api.testing.models.Post;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

/**
 * Pact tests for JSONPlaceholder Posts API
 * These tests demonstrate consumer-driven contract testing
 */
@ExtendWith(PactConsumerTestExt.class)
@org.junit.jupiter.api.Disabled("Pact tests disabled due to version compatibility issues")
public class PostsPactTest {
    
    private ObjectMapper objectMapper = new ObjectMapper();
    
    @Pact(consumer = "PostsConsumer", provider = "JSONPlaceholder")
    public RequestResponsePact getAllPostsPact(PactDslWithProvider builder) {
        return builder
                .given("posts exist")
                .uponReceiving("a request for all posts")
                .path("/posts")
                .method("GET")
                .willRespondWith()
                .status(200)
                .headers(Map.of("Content-Type", "application/json"))
                .body(PactDslJsonArray.arrayMinLike(2)
                        .integerType("id", 1)
                        .stringType("title", "Test Post Title")
                        .stringType("body", "Test Post Body")
                        .integerType("userId", 1)
                        .closeObject())
                .toPact();
    }
    
    @Pact(consumer = "PostsConsumer", provider = "JSONPlaceholder")
    public RequestResponsePact getPostByIdPact(PactDslWithProvider builder) {
        return builder
                .given("post with id 1 exists")
                .uponReceiving("a request for post with id 1")
                .path("/posts/1")
                .method("GET")
                .willRespondWith()
                .status(200)
                .headers(Map.of("Content-Type", "application/json"))
                .body(new PactDslJsonBody()
                        .integerType("id", 1)
                        .stringType("title", "Test Post Title")
                        .stringType("body", "Test Post Body")
                        .integerType("userId", 1))
                .toPact();
    }
    
    @Pact(consumer = "PostsConsumer", provider = "JSONPlaceholder")
    public RequestResponsePact getPostByIdNotFoundPact(PactDslWithProvider builder) {
        return builder
                .given("post with id 999 does not exist")
                .uponReceiving("a request for non-existent post")
                .path("/posts/999")
                .method("GET")
                .willRespondWith()
                .status(404)
                .headers(Map.of("Content-Type", "application/json"))
                .body(new PactDslJsonBody())
                .toPact();
    }
    
    @Pact(consumer = "PostsConsumer", provider = "JSONPlaceholder")
    public RequestResponsePact createPostPact(PactDslWithProvider builder) {
        return builder
                .given("creating a new post")
                .uponReceiving("a request to create a new post")
                .path("/posts")
                .method("POST")
                .headers(Map.of("Content-Type", "application/json"))
                .body(new PactDslJsonBody()
                        .stringType("title", "New Post Title")
                        .stringType("body", "New Post Body")
                        .integerType("userId", 1))
                .willRespondWith()
                .status(201)
                .headers(Map.of("Content-Type", "application/json"))
                .body(new PactDslJsonBody()
                        .integerType("id", 101)
                        .stringType("title", "New Post Title")
                        .stringType("body", "New Post Body")
                        .integerType("userId", 1))
                .toPact();
    }
    
    @Pact(consumer = "PostsConsumer", provider = "JSONPlaceholder")
    public RequestResponsePact updatePostPact(PactDslWithProvider builder) {
        return builder
                .given("post with id 1 exists")
                .uponReceiving("a request to update post with id 1")
                .path("/posts/1")
                .method("PUT")
                .headers(Map.of("Content-Type", "application/json"))
                .body(new PactDslJsonBody()
                        .integerType("id", 1)
                        .stringType("title", "Updated Post Title")
                        .stringType("body", "Updated Post Body")
                        .integerType("userId", 1))
                .willRespondWith()
                .status(200)
                .headers(Map.of("Content-Type", "application/json"))
                .body(new PactDslJsonBody()
                        .integerType("id", 1)
                        .stringType("title", "Updated Post Title")
                        .stringType("body", "Updated Post Body")
                        .integerType("userId", 1))
                .toPact();
    }
    
    @Pact(consumer = "PostsConsumer", provider = "JSONPlaceholder")
    public RequestResponsePact deletePostPact(PactDslWithProvider builder) {
        return builder
                .given("post with id 1 exists")
                .uponReceiving("a request to delete post with id 1")
                .path("/posts/1")
                .method("DELETE")
                .willRespondWith()
                .status(200)
                .headers(Map.of("Content-Type", "application/json"))
                .body(new PactDslJsonBody())
                .toPact();
    }
    
    @Test
    @PactTestFor(pactMethod = "getAllPostsPact")
    void testGetAllPosts_Success(MockServer mockServer) throws Exception {
        // Arrange
        RestAssured.baseURI = mockServer.getUrl();
        
        // Act
        Response response = given()
                .when()
                .get("/posts")
                .then()
                .statusCode(200)
                .extract()
                .response();
        
        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        assertEquals("application/json", response.getContentType());
        
        Post[] posts = objectMapper.readValue(response.getBody().asString(), Post[].class);
        assertTrue(posts.length >= 2);
        assertNotNull(posts[0].getId());
        assertNotNull(posts[0].getTitle());
        assertNotNull(posts[0].getBody());
        assertNotNull(posts[0].getUserId());
    }
    
    @Test
    @PactTestFor(pactMethod = "getPostByIdPact")
    void testGetPostById_Success(MockServer mockServer) throws Exception {
        // Arrange
        RestAssured.baseURI = mockServer.getUrl();
        
        // Act
        Response response = given()
                .when()
                .get("/posts/1")
                .then()
                .statusCode(200)
                .extract()
                .response();
        
        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        
        Post post = objectMapper.readValue(response.getBody().asString(), Post.class);
        assertNotNull(post.getId());
        assertNotNull(post.getTitle());
        assertNotNull(post.getBody());
        assertNotNull(post.getUserId());
    }
    
    @Test
    @PactTestFor(pactMethod = "getPostByIdNotFoundPact")
    void testGetPostById_NotFound(MockServer mockServer) {
        // Arrange
        RestAssured.baseURI = mockServer.getUrl();
        
        // Act & Assert
        given()
                .when()
                .get("/posts/999")
                .then()
                .statusCode(404);
    }
    
    @Test
    @PactTestFor(pactMethod = "createPostPact")
    void testCreatePost_Success(MockServer mockServer) throws Exception {
        // Arrange
        RestAssured.baseURI = mockServer.getUrl();
        Post newPost = new Post(null, "New Post Title", "New Post Body", 1);
        String requestBody = objectMapper.writeValueAsString(newPost);
        
        // Act
        Response response = given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/posts")
                .then()
                .statusCode(201)
                .extract()
                .response();
        
        // Assert
        assertNotNull(response);
        assertEquals(201, response.getStatusCode());
        
        Post createdPost = objectMapper.readValue(response.getBody().asString(), Post.class);
        assertNotNull(createdPost.getId());
        assertEquals("New Post Title", createdPost.getTitle());
        assertEquals("New Post Body", createdPost.getBody());
        assertEquals(1, createdPost.getUserId());
    }
    
    @Test
    @PactTestFor(pactMethod = "updatePostPact")
    void testUpdatePost_Success(MockServer mockServer) throws Exception {
        // Arrange
        RestAssured.baseURI = mockServer.getUrl();
        Post updatedPost = new Post(1, "Updated Post Title", "Updated Post Body", 1);
        String requestBody = objectMapper.writeValueAsString(updatedPost);
        
        // Act
        Response response = given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .put("/posts/1")
                .then()
                .statusCode(200)
                .extract()
                .response();
        
        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        
        Post post = objectMapper.readValue(response.getBody().asString(), Post.class);
        assertEquals(1, post.getId());
        assertEquals("Updated Post Title", post.getTitle());
        assertEquals("Updated Post Body", post.getBody());
        assertEquals(1, post.getUserId());
    }
    
    @Test
    @PactTestFor(pactMethod = "deletePostPact")
    void testDeletePost_Success(MockServer mockServer) {
        // Arrange
        RestAssured.baseURI = mockServer.getUrl();
        
        // Act & Assert
        given()
                .when()
                .delete("/posts/1")
                .then()
                .statusCode(200);
    }
}
