package com.midlaj.olikassigment;

import com.midlaj.olikassigment.dto.BookRequest;
import com.midlaj.olikassigment.exception.DuplicateEntityException;
import com.midlaj.olikassigment.exception.EntityNotFoundException;
import com.midlaj.olikassigment.model.Author;
import com.midlaj.olikassigment.model.Book;
import com.midlaj.olikassigment.repository.BookRepository;
import com.midlaj.olikassigment.service.AuthorService;
import com.midlaj.olikassigment.service.BookService;
import com.midlaj.olikassigment.service.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Book service implementation tests
 */
@SpringBootTest
public class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorService authorService;

    private BookService bookService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        bookService = new BookServiceImpl(bookRepository, authorService) {
        };
    }


    @Test
    void createNewBook_WithValidRequest_ShouldCreateBook() {
        /**
         * testing object
         */
        BookRequest bookRequest = new BookRequest("Test Book", 1L, "ISBN", 2022);
        Author author = Author.builder().id(1L).name("Author").biography("Biography").build();

        /**
         * mocking dao calls and author service call
         */
        when(bookRepository.findBookByTitle(bookRequest.title())).thenReturn(Optional.empty());
        when(bookRepository.findBookByIsbn(bookRequest.isbn())).thenReturn(Optional.empty());
        when(authorService.checkAuthorById(bookRequest.authorId())).thenReturn(true);
        when(bookRepository.save(any(Book.class))).thenReturn(new Book());

        /**
         * calling service method
         */
        Book result = bookService.createNewBook(bookRequest);

        /**
         * verifying
         */
        assertNotNull(result);
    }

    @Test
    void createNewBook_WithDuplicateTitle_ShouldThrowDuplicateEntityException() {
        /**
         * testing object
         */
        BookRequest bookRequest = new BookRequest("Test Book", 1L, "ISBN", 2022);

        /**
         * mocking dao call
         */
        when(bookRepository.findBookByTitle(bookRequest.title())).thenReturn(Optional.of(new Book()));

        /**
         * verify
         */
        assertThrows(DuplicateEntityException.class, () -> bookService.createNewBook(bookRequest));
    }

    @Test
    void createNewBook_WithDuplicateIsbn_ShouldThrowDuplicateEntityException() {
        /**
         * testing object
         */
        BookRequest bookRequest = new BookRequest("Test Book", 1L, "ISBN", 2022);

        /**
         * mocking dao calls
         */
        when(bookRepository.findBookByTitle("Title")).thenReturn(Optional.empty());
        when(bookRepository.findBookByIsbn("ISBN")).thenReturn(Optional.of(new Book()));

        /**
         * verifying
         */
        assertThrows(DuplicateEntityException.class, () -> bookService.createNewBook(bookRequest));
    }

    @Test
    void createNewBook_WithInvalidAuthorId_ShouldThrowEntityNotFoundException() {
        /**
         * testing object
         */
        BookRequest bookRequest = new BookRequest("Test Book", 1L, "ISBN", 2022);

        /**
         * mock author service call
         */
        when(authorService.checkAuthorById(bookRequest.authorId())).thenReturn(false);

        /**
         * verifying
         */
        assertThrows(EntityNotFoundException.class, () -> bookService.createNewBook(bookRequest));
    }

    @Test
    void getBookById_WithValidId_ShouldReturnBook() {
        /**
         * testing id
         */
        Long id = 1L;
        Book book = new Book();

        /**
         * mocking dao call
         */
        when(bookRepository.findById(id)).thenReturn(Optional.of(book));

        /**
         * calling service method
         */
        Book result = bookService.getBookById(id);

        /**
         * verifying
         */
        assertEquals(book, result);
    }

    @Test
    void getBookById_WithInvalidId_ShouldThrowEntityNotFoundException() {
        /**
         * testing id
         */
        Long id = 1L;

        /**
         * mocking dao call
         */
        when(bookRepository.findById(id)).thenReturn(Optional.empty());

        /**
         * verifying
         */
        assertThrows(EntityNotFoundException.class, () -> bookService.getBookById(id));
    }

    @Test
    void getBooks_ShouldReturnListOfBooks() {
        /**
         * testing objects
         */
        List<Book> books = new ArrayList<>();
        books.add(new Book());

        /**
         * mock dao call
         */
        when(bookRepository.findAll()).thenReturn(books);

        /**
         * calling service method
         */
        List<Book> result = bookService.getBooks();

        /**
         * verifying
         */
        assertEquals(books, result);
    }

    @Test
    void deleteBookById_WithValidId_ShouldDeleteBook() {
        /**
         * testing objects
         */
        Long id = 1L;
        Book book = new Book();

        /**
         * mocking dao call
         */
        when(bookRepository.findById(id)).thenReturn(Optional.of(book));

        /**
         * calling service method
         */
        bookService.deleteBookById(id);

        /**
         * verifying
         */
        verify(bookRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteBookById_WithInvalidId_ShouldThrowEntityNotFoundException() {
        /**
         * testing object
         */
        Long id = 1L;

        /**
         * mocking dao call
         */
        when(bookRepository.findById(id)).thenReturn(Optional.empty());

        /**
         * verifying
         */
        assertThrows(EntityNotFoundException.class, () -> bookService.deleteBookById(id));
    }

    @Test
    void saveBook_ShouldReturnSavedBook() {
        /**
         * testing object
         */
        Book book = new Book();

        /**
         * mocking dao call
         */
        when(bookRepository.save(book)).thenReturn(book);

        /**
         * calling service method
         */
        Book result = bookService.saveBook(book);

        /**
         * verifying
         */
        assertEquals(book, result);
    }

    @Test
    void findBooksByAuthor_WithValidAuthorId_ShouldReturnListOfBooks() {
        /**
         * testing id and objects
         */
        Long authorId = 1L;
        List<Book> books = new ArrayList<>();

        /**
         * mocking dao call and author service call
         */
        when(authorService.checkAuthorById(authorId)).thenReturn(true);
        when(bookRepository.findBooksByAuthorId(authorId)).thenReturn(books);

        /**
         * calling service method
         */
        List<Book> result = bookService.findBooksByAuthor(authorId);

        /**
         * verifying
         */
        assertEquals(books, result);
    }

    @Test
    void findBooksByAuthor_WithInvalidAuthorId_ShouldThrowEntityNotFoundException() {
        /**
         * testing id
         */
        Long authorId = 1L;

        /**
         * mocking dao call
         */
        when(authorService.checkAuthorById(authorId)).thenReturn(false);

        /**
         * verifying
         */
        assertThrows(EntityNotFoundException.class, () -> bookService.findBooksByAuthor(authorId));
    }

    @Test
    void findAvailableBooksForRent_ShouldReturnListOfBooks() {
        /**
         * testing object
         */
        List<Book> books = new ArrayList<>();

        /**
         * mocking dao call
         */
        when(bookRepository.findBooksByAvailableTrue()).thenReturn(books);

        /**
         * calling service method
         */
        List<Book> result = bookService.findAvailableBooksForRent();

        /**
         * verifying
         */
        assertEquals(books, result);
    }

    @Test
    void findRentedBooks_ShouldReturnListOfBooks() {
        /**
         * testing object
         */
        List<Book> books = new ArrayList<>();

        /**
         * mocking dao call
         */
        when(bookRepository.findBooksByAvailableFalse()).thenReturn(books);

        /**
         * calling service method
         */
        List<Book> result = bookService.findRentedBooks();

        /**
         * verifying
         */
        assertEquals(books, result);
    }
}
