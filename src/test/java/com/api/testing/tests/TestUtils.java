package com.api.testing.tests;

import com.api.testing.models.Post;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.List;

/**
 * Utility class for test data and common test operations
 */
public class TestUtils {
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * Creates a sample Post object for testing
     */
    public static Post createSamplePost() {
        return new Post(1, "Sample Post Title", "Sample Post Body", 1);
    }
    
    /**
     * Creates a list of sample Post objects for testing
     */
    public static List<Post> createSamplePosts() {
        return Arrays.asList(
            new Post(1, "First Post", "Body of first post", 1),
            new Post(2, "Second Post", "Body of second post", 2),
            new Post(3, "Third Post", "Body of third post", 1)
        );
    }
    
    /**
     * Converts an object to JSON string
     */
    public static String toJson(Object object) throws Exception {
        return objectMapper.writeValueAsString(object);
    }
    
    /**
     * Converts JSON string to Post object
     */
    public static Post fromJsonToPost(String json) throws Exception {
        return objectMapper.readValue(json, Post.class);
    }
    
    /**
     * Converts JSON string to Post array
     */
    public static Post[] fromJsonToPostArray(String json) throws Exception {
        return objectMapper.readValue(json, Post[].class);
    }
    
    /**
     * Validates that a Post object has all required fields
     */
    public static boolean isValidPost(Post post) {
        return post != null &&
               post.getId() != null &&
               post.getTitle() != null && !post.getTitle().isEmpty() &&
               post.getBody() != null && !post.getBody().isEmpty() &&
               post.getUserId() != null;
    }
    
    /**
     * Creates a Post object with null ID (for creation requests)
     */
    public static Post createPostForCreation(String title, String body, Integer userId) {
        return new Post(null, title, body, userId);
    }
}
