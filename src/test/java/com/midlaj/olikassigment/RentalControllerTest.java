package com.midlaj.olikassigment;

import com.midlaj.olikassigment.dto.RentalRequest;
import com.midlaj.olikassigment.model.Author;
import com.midlaj.olikassigment.model.Book;
import com.midlaj.olikassigment.model.Rental;
import com.midlaj.olikassigment.repository.AuthorRepository;
import com.midlaj.olikassigment.repository.BookRepository;
import com.midlaj.olikassigment.repository.RentalRepository;
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
import java.util.Random;
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
        Book book = Book.builder()
                .title("Test_Book_Title_1" + UUID.randomUUID())
                .author(savedAuthor)
                .isbn(generateRandomIsbn13())
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


        /**
         *  Create rental test data
         */
        Rental rental = Rental.builder()
                .renterName("Test_Renter_Name_" + UUID.randomUUID())
                .rentalDate(LocalDate.now())
                .bookId(bookId)
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
        ResponseEntity<?> response = restTemplate.postForEntity("http://localhost:8080/api/rental" + "/new", rentalRequest, Object.class);

        /**
         * checking if the status is CREATED
         */
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }


    /**
     * Testing retrieving a rental with id, api "api/rental/{id}"
     */
    @Test
    public void testGetRentalByIdWithAValidBookId() {
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

    /**
     * A static method to generate valid Isbn13 number for testing purpose
     *
     * @return isbn13 number as string
     */
    public static String generateRandomIsbn13() {
        StringBuilder isbn = new StringBuilder("978");
        Random random = new Random();

        /**
         * Generate and append 9 characters
         */
        for (int i = 0; i < 9; i++) {
            isbn.append(random.nextInt(10));
        }

        /**
         * Calculate the check digit
         */
        int sum = 0;
        for (int i = 0; i < 12; i++) {
            int digit = Character.getNumericValue(isbn.charAt(i));
            sum += (i % 2 == 0) ? digit : digit * 3;
        }
        int checkDigit = (10 - (sum % 10)) % 10;
        isbn.append(checkDigit);

        return isbn.toString();
    }
}
