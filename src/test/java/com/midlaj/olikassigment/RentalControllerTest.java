package com.midlaj.olikassigment;

import com.midlaj.olikassigment.dto.RentalRequest;
import com.midlaj.olikassigment.model.Author;
import com.midlaj.olikassigment.model.Book;
import com.midlaj.olikassigment.model.Rental;
import com.midlaj.olikassigment.repository.AuthorRepository;
import com.midlaj.olikassigment.repository.BookRepository;
import com.midlaj.olikassigment.repository.RentalRepository;
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
 * Integration Testing of Rental Controller
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RentalControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    private Long authorId = 1L;

    private Long bookId = 1L;

    private Long rentalId = 1L;

    private String getBaseUrl() {
        return "http://localhost:" + port + "/api/rental";
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

        /**
         *  Create book test data
         */
        Book book1 = Book.builder()
                .title("Test_Book_Title_1" + UUID.randomUUID())
                .author(savedAuthor)
                .isbn(Utils.generateRandomIsbn13())
                .publicationYear(LocalDate.now().getYear())
                .available(true)
                .build();

        Book book2 = Book.builder()
                .title("Test_Book_Title_2" + UUID.randomUUID())
                .author(savedAuthor)
                .isbn(Utils.generateRandomIsbn13())
                .publicationYear(LocalDate.now().getYear())
                .available(false)
                .build();

        Book savedBook1 = bookRepository.save(book1);
        Book savedBook2 = bookRepository.save(book2);

        /**
         * Check if saved book is not null;
         */
        assertNotNull(book1);
        assertNotNull(book2);


        /**
         * set book id for testing
         */
        bookId = savedBook1.getId();


        /**
         *  Create rental test data
         */
        Rental rental = Rental.builder()
                .renterName("Test_Renter_Name_" + UUID.randomUUID())
                .rentalDate(LocalDate.now())
                .bookId(savedBook2.getId())
                .returnDate(null)
                .build();

        Rental savedRental = rentalRepository.save(rental);

        /**
         * Check if saved renal data is not null;
         */
        assertNotNull(savedRental);

        /**
         * set rental id for testing
         */
        rentalId = savedRental.getId();
    }

    /**
     * Testing creating new rental, api "api/rental/new"
     */
    @Test
    public void testCreateNewRentalValidRequestReturnCreated() {

        /**
         * creating a random but valid rental request object
         */
        RentalRequest rentalRequest = new RentalRequest(bookId, "Test_Renter_Name_" + UUID.randomUUID());

        /**
         * performing a POST request
         */
        ResponseEntity<?> response = restTemplate.postForEntity(getBaseUrl() + "/new", rentalRequest, Object.class);

        System.out.println(response);
        /**
         * checking if the status is CREATED
         */
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }


    /**
     * Testing retrieving a rental with id, api "api/rental/{id}"
     */
    @Test
    public void testGetRentalById() {
        /**
         * Assuming rental with this ID exists in the database
         */
        long rentalId = this.rentalId;

        /**
         * performing GET request
         */
        ResponseEntity<Rental> response = restTemplate.getForEntity(getBaseUrl() + "/" + rentalId, Rental.class);

        /**
         * checking if the status is OK
         */
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }


    /**
     * Testing getting all rentals, api "api/rental"
     */
    @Test
    public void testGetBooks() {

        /**
         * performing GET request
         */
        ResponseEntity<List<Rental>> response = restTemplate.exchange(
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
     * Testing returning a book , api "api/rental/return/{id}"
     */
    @Test
    public void testReturnBook() {

        /**
         * Assuming rental with this ID exists in the database
         */
        long rentalId = this.rentalId;

        /**
         * Perform the POST request
         */
        ResponseEntity<Rental> response = restTemplate.postForEntity(
                getBaseUrl() + "/return/" + rentalId,
                null,
                Rental.class);

        System.out.println(response);

        /**
         * Verify response status and content and check if return date is not null
         */
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getReturnDate());
    }


    @Test
    public void testCheckOverdueRentalsWithDays() {

        int days = -1;

        /**
         * performing GET request
         */
        ResponseEntity<List<Rental>> response = restTemplate.exchange(
                getBaseUrl() + "/overdue/" + days,
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
