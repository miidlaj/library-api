package com.midlaj.olikassigment;

import com.midlaj.olikassigment.dto.RentalRequest;
import com.midlaj.olikassigment.exception.AlreadyRentedException;
import com.midlaj.olikassigment.exception.EntityNotFoundException;
import com.midlaj.olikassigment.model.Book;
import com.midlaj.olikassigment.model.Rental;
import com.midlaj.olikassigment.repository.RentalRepository;
import com.midlaj.olikassigment.service.BookService;
import com.midlaj.olikassigment.service.RentalService;
import com.midlaj.olikassigment.service.RentalServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Rental Service Implementation tests
 */
@SpringBootTest
public class RentalServiceImplTest {

    @Mock
    private RentalRepository rentalRepository;

    @Mock
    private BookService bookService;

    private RentalService rentalService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        rentalService = new RentalServiceImpl(rentalRepository, bookService) {
        };
    }


    @Test
    void createNewRental_WithValidRequest_ShouldCreateRental() {

        /**
         * testing object
         */
        RentalRequest rentalRequest = new RentalRequest(1L, "Test Renter");
        Book book = new Book();
        book.setAvailable(true);

        /**
         * mocking dao call and book service call
         */
        when(bookService.getBookById(rentalRequest.bookId())).thenReturn(book);
        when(rentalRepository.save(any(Rental.class))).thenReturn(new Rental());

        /**
         * calling service method
         */
        Rental result = rentalService.createNewRental(rentalRequest);

        /**
         * verifying
         */
        assertNotNull(result);
    }

    @Test
    void createNewRental_WithAlreadyRentedBook_ShouldThrowAlreadyRentedException() {
        /**
         * testing object
         */
        RentalRequest rentalRequest = new RentalRequest(1L, "Test Renter");
        Book book = new Book();
        book.setId(rentalRequest.bookId());
        book.setAvailable(false);

        /**
         * mocking book service call
         */
        when(bookService.getBookById(rentalRequest.bookId())).thenReturn(book);

        /**
         * verifying
         */
        assertThrows(AlreadyRentedException.class, () -> rentalService.createNewRental(rentalRequest));
    }

    @Test
    void returnBook_WithValidRentalId_ShouldReturnRental() {
        /**
         * testing object
         */
        Long rentalId = 1L;
        Rental rental = new Rental();
        rental.setBookId(1L);

        /**
         * mocking book service call and dao call
         */
        when(rentalRepository.findById(rentalId)).thenReturn(Optional.of(rental));
        when(bookService.getBookById(rental.getBookId())).thenReturn(new Book());
        when(rentalRepository.save(any(Rental.class))).thenReturn(new Rental());

        /**
         * calling service method
         */
        Rental result = rentalService.returnBook(rentalId);

        /**
         * verifying
         */
        assertNotNull(result);
    }

    @Test
    void returnBook_WithAlreadyReturnedBook_ShouldThrowAlreadyRentedException() {
        /**
         * testing object and id
         */
        Long rentalId = 1L;
        Rental rental = new Rental();
        rental.setBookId(1L);
        rental.setReturnDate(LocalDate.now());

        /**
         * mocking dao call
         */
        when(rentalRepository.findById(rentalId)).thenReturn(Optional.of(rental));

        /**
         * verifying
         */
        assertThrows(AlreadyRentedException.class, () -> rentalService.returnBook(rentalId));
    }

    @Test
    void returnBook_WithInvalidRentalId_ShouldThrowEntityNotFoundException() {
        /**
         * testing id
         */
        Long rentalId = 1L;

        /**
         * mocking dao call
         */
        when(rentalRepository.findById(rentalId)).thenReturn(Optional.empty());

        /**
         * verifying
         */
        assertThrows(EntityNotFoundException.class, () -> rentalService.returnBook(rentalId));
    }

    @Test
    void findOverdueRentals_ShouldReturnListOfOverdueRentals() {
        /**
         * testing id and object
         */
        Integer days = 7;
        LocalDate currentDate = LocalDate.now();
        LocalDate overdueDate = currentDate.minusDays(days);
        List<Rental> overdueRentals = new ArrayList<>();

        /**
         * mocking dao call
         */
        when(rentalRepository.findByRentalDateBeforeAndReturnDateIsNull(overdueDate)).thenReturn(overdueRentals);

        /**
         * calling service method
         */
        List<Rental> result = rentalService.findOverdueRentals(days);

        /**
         * verifying
         */
        assertEquals(overdueRentals, result);
    }

    @Test
    void getAllRentals_ShouldReturnListOfRentals() {
        /**
         * testing method
         */
        List<Rental> rentals = new ArrayList<>();

        /**
         * mocking dao call
         */
        when(rentalRepository.findAll()).thenReturn(rentals);

        /**
         * calling service method
         */
        List<Rental> result = rentalService.getAllRentals();

        /**
         * verifying
         */
        assertEquals(rentals, result);
    }

    @Test
    void getRentalById_WithValidId_ShouldReturnRental() {
        /**
         * testing object and id
         */
        Long id = 1L;
        Rental rental = new Rental();

        /**
         * mocking dao call
         */
        when(rentalRepository.findById(id)).thenReturn(Optional.of(rental));

        /**
         * calling service method
         */
        Rental result = rentalService.getRentalById(id);

        /**
         * verifying
         */
        assertEquals(rental, result);
    }

    @Test
    void getRentalById_WithInvalidId_ShouldThrowEntityNotFoundException() {
        /**
         * testing id
         */
        Long id = 1L;

        /**
         * mocking dao call
         */
        when(rentalRepository.findById(id)).thenReturn(Optional.empty());

        /**
         * verifying
         */
        assertThrows(EntityNotFoundException.class, () -> rentalService.getRentalById(id));
    }
}
