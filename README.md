# API Shift-Left Testing Project

A comprehensive Java-based API testing project demonstrating shift-left testing practices using **WireMock** and **Pact** for the [JSONPlaceholder API](https://jsonplaceholder.typicode.com/).

## 🎯 Project Overview

This project showcases modern API testing strategies including:
- **WireMock** for API mocking and stubbing
- **Pact** for consumer-driven contract testing
- **RestAssured** for API testing
- **JUnit 5** for test framework
- **Maven** for dependency management

## 🏗️ Project Structure

```
src/
├── main/java/com/api/testing/
│   ├── models/
│   │   └── Post.java                 # Data model for Posts API
│   └── config/
│       └── ApiConfig.java            # Configuration constants
└── test/java/com/api/testing/
    ├── wiremock/
    │   └── PostsWireMockTest.java    # WireMock tests
    ├── pact/
    │   └── PostsPactTest.java        # Pact contract tests
    ├── tests/
    │   ├── PostsIntegrationTest.java # Integration tests
    │   └── TestUtils.java            # Test utilities
```

## 🚀 Getting Started

### Prerequisites

- Java 11 or higher
- Maven 3.6 or higher

### Installation

1. Clone the repository:
```bash
git clone <repository-url>
cd api-shift-left-testing
```

2. Build the project:
```bash
mvn clean compile
```

3. Run tests:
```bash
# Run all tests
mvn test

# Run only WireMock tests
mvn test -Dtest=*WireMockTest

# Run only Pact tests
mvn test -Dtest=*PactTest

# Run only integration tests
mvn test -Dtest=*IntegrationTest
```

## 🧪 Test Types

### 1. WireMock Tests (`PostsWireMockTest.java`)

WireMock tests demonstrate how to mock external API dependencies:

- **GET /posts** - Retrieve all posts
- **GET /posts/{id}** - Retrieve specific post
- **POST /posts** - Create new post
- **PUT /posts/{id}** - Update existing post
- **DELETE /posts/{id}** - Delete post
- **Response time testing** - Performance validation

**Key Features:**
- Complete API mocking without external dependencies
- Configurable response delays for performance testing
- Error scenario testing (404, 500 responses)
- Request verification and validation

### 2. Pact Tests (`PostsPactTest.java`)

Pact tests implement consumer-driven contract testing:

- **Contract Definition** - Define expected API behavior
- **Consumer Testing** - Test against mock provider
- **Contract Verification** - Ensure API compliance
- **Provider Testing** - Validate real API against contracts

**Key Features:**
- Consumer-driven contract testing
- API contract versioning
- Provider verification
- Contract sharing between teams

### 3. Integration Tests (`PostsIntegrationTest.java`)

Integration tests validate against the real JSONPlaceholder API:

- **Real API Testing** - End-to-end validation
- **Performance Testing** - Response time validation
- **Data Validation** - Verify actual API responses

## 🔧 Configuration

### API Configuration (`ApiConfig.java`)

```java
public class ApiConfig {
    public static final String JSON_PLACEHOLDER_BASE_URL = "https://jsonplaceholder.typicode.com";
    public static final String POSTS_ENDPOINT = "/posts";
    public static final int WIREMOCK_PORT = 8089;
    // ... more configuration
}
```

### Maven Dependencies

Key dependencies in `pom.xml`:

- **WireMock JRE8** (2.35.0) - API mocking
- **Pact Consumer JUnit5** (4.3.8) - Contract testing
- **RestAssured** (5.3.2) - API testing
- **JUnit 5** (5.9.2) - Test framework
- **Jackson** (2.15.2) - JSON processing

## 📊 Test Execution

### Running Specific Test Suites

```bash
# WireMock tests only
mvn test -Dtest=PostsWireMockTest

# Pact tests only  
mvn test -Dtest=PostsPactTest

# Integration tests only
mvn test -Dtest=PostsIntegrationTest
```

### Test Reports

Maven Surefire generates test reports in:
```
target/surefire-reports/
```

## 🎯 Shift-Left Testing Benefits

This project demonstrates key shift-left testing principles:

1. **Early Testing** - Tests run during development phase
2. **Fast Feedback** - Quick test execution with mocks
3. **Isolation** - Tests don't depend on external services
4. **Contract Testing** - API contracts defined and verified early
5. **Continuous Integration** - Tests integrated into CI/CD pipeline

## 🔍 API Endpoints Tested

Based on [JSONPlaceholder API](https://jsonplaceholder.typicode.com/):

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/posts` | Get all posts |
| GET | `/posts/{id}` | Get post by ID |
| POST | `/posts` | Create new post |
| PUT | `/posts/{id}` | Update post |
| DELETE | `/posts/{id}` | Delete post |

## 🛠️ Development

### Adding New Tests

1. **WireMock Tests**: Add to `PostsWireMockTest.java`
2. **Pact Tests**: Add to `PostsPactTest.java`
3. **Integration Tests**: Add to `PostsIntegrationTest.java`

### Test Data

Use `TestUtils.java` for common test data and utilities:

```java
Post samplePost = TestUtils.createSamplePost();
String json = TestUtils.toJson(samplePost);
```

## 📈 Best Practices Demonstrated

- **Test Isolation** - Each test is independent
- **Mock Management** - Proper setup/teardown of mocks
- **Data Validation** - Comprehensive response validation
- **Error Handling** - Testing error scenarios
- **Performance Testing** - Response time validation
- **Contract Testing** - API contract verification

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Add tests for new functionality
4. Ensure all tests pass
5. Submit a pull request

## 📄 License

This project is for educational purposes demonstrating API testing best practices.

## 🔗 References

- [JSONPlaceholder API](https://jsonplaceholder.typicode.com/)
- [WireMock Documentation](http://wiremock.org/)
- [Pact Documentation](https://docs.pact.io/)
- [RestAssured Documentation](https://rest-assured.io/)
- [JUnit 5 Documentation](https://junit.org/junit5/)