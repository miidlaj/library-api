package com.midlaj.olikassigment.service;

import com.midlaj.olikassigment.dto.RentalRequest;
import com.midlaj.olikassigment.exception.AlreadyRentedException;
import com.midlaj.olikassigment.exception.EntityNotFoundException;
import com.midlaj.olikassigment.model.Book;
import com.midlaj.olikassigment.model.Rental;
import com.midlaj.olikassigment.repository.RentalRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class RentalServiceImpl implements RentalService {


    private final RentalRepository rentalRepository;

    private final BookService bookService;


    public RentalServiceImpl(RentalRepository rentalRepository, BookService bookService) {
        this.rentalRepository = rentalRepository;
        this.bookService = bookService;
    }

    /**
     * For creating new rental
     * @param rentalRequest
     * @return
     */
    @Override
    public Rental createNewRental(RentalRequest rentalRequest) {

        /**
         * Check if the book is present with bookId
         */
        Book book = bookService.getBookById(rentalRequest.bookId());


        /**
         * Check if the book is already rented out
         */
        if (!book.getAvailable()) {
            throw new AlreadyRentedException("Book is not available.");
        }

        /**
         * Create new rental object with current date as rental date and null as return date
         */
        Rental newRental = Rental.builder()
                .renterName(rentalRequest.renterName())
                .bookId(rentalRequest.bookId())
                .rentalDate(LocalDate.now())
                .returnDate(null)
                .build();

        Rental createdRental = rentalRepository.save(newRental);


        /**
         * Set availability of book to false and save
         */
        book.setAvailable(false);
        bookService.saveBook(book);

        return createdRental;
    }



    /**
     * For returning book
     * @param rentalId
     * @return rental object
     */
    @Override
    public Rental returnBook(Long rentalId)  {
        /**
         * Checking if the rental is present or not
         */
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new EntityNotFoundException("Rental not found"));

        /**
         * check if book is already returned
         */
        if (rental.getReturnDate() != null) throw new AlreadyRentedException("Book already returned");

        /**
         * Mark the book available and save
         */
        Book book = bookService.getBookById(rental.getBookId());
        book.setAvailable(true);
        bookService.saveBook(book);


        /**
         *  Update return date in rental record
         */
        rental.setReturnDate(LocalDate.now());
        return rentalRepository.save(rental);
    }

    /**
     * For finding overdue rentals
     * @param days
     * @return
     */
    @Override
    public List<Rental> findOverdueRentals(Integer days) {

        /**
         * setting new date using the days given
         */
        LocalDate currentDate = LocalDate.now().minusDays(days);

        return rentalRepository.findByRentalDateBeforeAndReturnDateIsNull(currentDate);
    }

    /**
     * For retrieving every rental data
     * @return
     */
    @Override
    public List<Rental> getAllRentals() {
        return rentalRepository.findAll();
    }

    /**
     * For finding rental with id
     * @param id
     * @return rental object
     */
    @Override
    public Rental getRentalById(Long id) {

        return rentalRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rental not found"));
    }

}
