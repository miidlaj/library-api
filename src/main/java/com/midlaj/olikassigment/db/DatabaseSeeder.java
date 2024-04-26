package com.midlaj.olikassigment.db;

import com.midlaj.olikassigment.model.Author;
import com.midlaj.olikassigment.model.Book;
import com.midlaj.olikassigment.model.Rental;
import com.midlaj.olikassigment.repository.AuthorRepository;
import com.midlaj.olikassigment.repository.BookRepository;
import com.midlaj.olikassigment.repository.RentalRepository;
import com.midlaj.olikassigment.util.Utils;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.UUID;

@Component
public class DatabaseSeeder {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    private final RentalRepository rentalRepository;

    public DatabaseSeeder(AuthorRepository authorRepository, BookRepository bookRepository, RentalRepository rentalRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
        this.rentalRepository = rentalRepository;
    }

    @PostConstruct
    public void seedDatabase() {

        /**
         *  Create author test data
         */
        Author author = Author.builder()
                .name("Test_Author_1" + UUID.randomUUID())
                .biography("Test_Biography_1_Lorem Ipsum Test Value, 64 characters required to pass validation")
                .build();

        Author savedAuthor = authorRepository.save(author);


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
         *  Create rental test data
         */
        Rental rental = Rental.builder()
                .renterName("Test_Renter_Name_" + UUID.randomUUID())
                .rentalDate(LocalDate.now())
                .bookId(savedBook2.getId())
                .returnDate(null)
                .build();

        Rental savedRental = rentalRepository.save(rental);

    }


}
