package com.midlaj.olikassigment;

import com.midlaj.olikassigment.dto.BookRequest;
import com.midlaj.olikassigment.model.Author;
import com.midlaj.olikassigment.model.Book;
import com.midlaj.olikassigment.repository.AuthorRepository;
import com.midlaj.olikassigment.repository.BookRepository;
import com.midlaj.olikassigment.util.Utils;
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

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Integration Testing of Book Controller
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerTest {


    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    private String getBaseUrl() {
        return "http://localhost:" + port + "/api/book";
    }

    private Long authorId = 1L;
    private Long bookId = 1L;

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


        /**
         *  Create book test data
         */
        Book book = Book.builder()
                .title("Test_Book_Title_1" + UUID.randomUUID())
                .author(savedAuthor)
                .isbn(Utils.generateRandomIsbn13())
                .publicationYear(LocalDate.now().getYear())
                .available(true)
                .build();

        Book savedBook = bookRepository.save(book);

        /**
         * Check if saved book is not null;
         */
        assertNotNull(savedBook);

        /**
         * set book id for testing
         */
        bookId = savedBook.getId();
    }


    /**
     * Testing creating new book api "api/book/new"
     */
    @Test
    public void testCreateNewBookValidRequestReturnCreated() {

        /**
         * creating a random but valid book request object
         */
        BookRequest bookRequest = new BookRequest("test_book_" + UUID.randomUUID(), authorId, Utils.generateRandomIsbn13(), 2020);

        /**
         * performing a POST request
         */
        ResponseEntity<?> response = restTemplate.postForEntity(getBaseUrl() + "/new", bookRequest, Book.class);
        System.out.println(response);

        /**
         * checking if the status is CREATED
         */
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    /**
     * Testing retrieving a book with id, api "api/book/{id}"
     */
    @Test
    public void testGetBookByIdWithAValidBookId() {
        /**
         * Assuming book with this ID exists in the database
         */
        long bookId = this.bookId;

        /**
         * performing GET request
         */
        ResponseEntity<Book> response = restTemplate.getForEntity(getBaseUrl() + "/" + bookId, Book.class);

        /**
         * checking if the status is OK
         */
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }


    /**
     * Testing getting all books, api "api/book"
     */
    @Test
    public void testGetBooks() {

        /**
         * performing GET request
         */
        ResponseEntity<List<Book>> response = restTemplate.exchange(
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
     * Testing deleting a book by id, api DELETE "api/book"
     */
    @Test
    public void testDeleteBookById() {
        /**
         * Assuming book with this ID exists in the database
         */
        long bookId = this.bookId;

        /**
         * performing a DELETE request
         */

        restTemplate.delete(getBaseUrl() + "/" + bookId);

        /**
         * Verify that the book is deleted by trying to fetch it again (expecting 409)
         */
        ResponseEntity<Book> response = restTemplate.getForEntity(getBaseUrl() + "/" + bookId, Book.class);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }


    /**
     * Testing getting all books that available for renting out, api "api/book/available"
     */
    @Test
    public void testGetBooksAvailableForRent() {

        /**
         * performing GET request
         */
        ResponseEntity<List<Book>> response = restTemplate.exchange(
                getBaseUrl() + "/available",
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
     * Testing getting all books that already rented out, api "api/book/rented"
     */
    @Test
    public void testGetBooksAlreadyRented() {

        /**
         * performing GET request
         */
        ResponseEntity<List<Book>> response = restTemplate.exchange(
                getBaseUrl() + "/rented",
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


}
