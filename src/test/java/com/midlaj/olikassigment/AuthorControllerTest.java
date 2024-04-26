package com.midlaj.olikassigment;

import com.midlaj.olikassigment.dto.AuthorRequest;
import com.midlaj.olikassigment.model.Author;
import com.midlaj.olikassigment.repository.AuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Integration Testing of Author Controller
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthorControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @Autowired
    private AuthorRepository authorRepository;

    private Long authorId = 1L;

    private String getBaseUrl() {
        return "http://localhost:" + port + "/api/author";
    }

    @BeforeEach
    public void setupTestData() {

        /**
         *  Create author test data
         */
        Author author = Author.builder()
                .name("Test_Author_1" + UUID.randomUUID())
                .biography("Test_Biography_1_Lorem Ipsum Test Value, 64 characters required to pass validation")
                .build();

        Author savedAuthor = authorRepository.save(author);

        /**
         * Check if saved author is not null;
         */
        assertNotNull(savedAuthor);

        /**
         * set author id for testing
         */
        authorId = savedAuthor.getId();
    }

    /**
     * Testing creating new author api "api/author/new"
     */
    @Test
    public void testCreateNewAuthorValidRequestReturnCreated() {

        /**
         * creating a random but valid author request object
         */
        AuthorRequest authorRequest = new AuthorRequest("test_author_" + UUID.randomUUID(), "Test_Biography_Required_min_64_Characters" + UUID.randomUUID());

        /**
         * performing a POST request
         */
        ResponseEntity<?> response = restTemplate.postForEntity(getBaseUrl() + "/new", authorRequest, Author.class);
        System.out.println(response);
        /**
         * checking if the status is CREATED
         */
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }


    /**
     * Testing retrieving an author with id, api "api/author/{id}"
     */
    @Test
    public void testGetAuthorByIdWithAValidAuthorId() {
        /**
         * Assuming author with this ID exists in the database
         */
        long authorId = this.authorId;

        /**
         * performing GET request
         */
        ResponseEntity<Author> response = restTemplate.getForEntity(getBaseUrl() + "/" + authorId, Author.class);
        System.out.println(response);
        /**
         * checking if the status is OK
         */
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }


    /**
     * Testing getting all authors, api "api/author"
     */
    @Test
    public void testGetAuthors() {

        /**
         * performing GET request
         */
        ResponseEntity<List<Author>> response = restTemplate.exchange(
                getBaseUrl(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        /**
         * checking if status is OK
         */
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    /**
     * Testing deleting a author by id, api DELETE "api/author"
     */
    @Test
    public void testDeleteAuthorById() {
        /**
         * Assuming author with this ID exists in the database
         */
        long authorId = this.authorId;

        /**
         * performing a DELETE request
         */

        restTemplate.delete(getBaseUrl() + "/" + authorId);

        /**
         * Verify that the author is deleted by trying to fetch it again (expecting 409)
         */
        ResponseEntity<Author> response = restTemplate.getForEntity(getBaseUrl() + "/" + authorId, Author.class);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());

    }


}
