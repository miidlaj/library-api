package com.midlaj.olikassigment.controller;


import com.midlaj.olikassigment.dto.BookRequest;
import com.midlaj.olikassigment.model.Book;
import com.midlaj.olikassigment.service.BookService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Book Controller where all the crud operations of book mapped
 */

@RestController
@RequestMapping("/api/book")
public class BookController {


    private final BookService bookService;


    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * for creating new book
     * @param bookRequest
     * @return Book object
     */
    @PostMapping("/new")
    public ResponseEntity<?> createBook(@RequestBody @Valid BookRequest bookRequest) {
        Book createdBook = bookService.createNewBook(bookRequest);
        return new ResponseEntity<>(createdBook, HttpStatus.CREATED);
    }

    /**
     * for getting book with id
     * @param id
     * @return Book object
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getBookById(@PathVariable Long id) {
        Book createdBook = bookService.getBookById(id);
        return new ResponseEntity<>(createdBook, HttpStatus.CREATED);
    }


    /**
     * fot getting all books
     * @return List of book
     */
    @GetMapping()
    public ResponseEntity<?> getBooks() {
        return ResponseEntity.ok(bookService.getBooks());
    }

    /**
     * fot deleting a book by id
     * @param id
     * @return void
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBookById(@PathVariable Long id) {
        bookService.deleteBookById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    // Endpoint to retrieve books by author
    @GetMapping("/author/{authorId}")
    public ResponseEntity<?> getBooksByAuthor(@PathVariable Long authorId) {
        List<Book> books = bookService.findBooksByAuthor(authorId);
        return ResponseEntity.ok(books);
    }

    // Endpoint to retrieve books available for rent
    @GetMapping("/available")
    public ResponseEntity<?> getAvailableBooksForRent() {
        List<Book> availableBooks = bookService.findAvailableBooksForRent();
        return ResponseEntity.ok(availableBooks);
    }

    // Endpoint to retrieve books currently rented
    @GetMapping("/rented")
    public ResponseEntity<?> getRentedBooks() {
        List<Book> rentedBooks = bookService.findRentedBooks();
        return ResponseEntity.ok(rentedBooks);
    }


}
