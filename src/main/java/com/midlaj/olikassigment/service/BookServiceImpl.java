package com.midlaj.olikassigment.service;

import com.midlaj.olikassigment.dto.BookRequest;
import com.midlaj.olikassigment.exception.DuplicateEntityException;
import com.midlaj.olikassigment.exception.EntityNotFoundException;
import com.midlaj.olikassigment.model.Author;
import com.midlaj.olikassigment.model.Book;
import com.midlaj.olikassigment.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    public final BookRepository bookRepository;

    public final AuthorService authorService;


    public BookServiceImpl(BookRepository bookRepository, AuthorService authorService) {
        this.bookRepository = bookRepository;
        this.authorService = authorService;
    }


    @Override
    public Book createNewBook(BookRequest bookRequest) {

        /**
         * Checks for duplicate book title, if duplicate found ith will throw a custom exception
         */
        bookRepository.findBookByTitle(bookRequest.title())
                .ifPresentOrElse((s) -> {
                    throw new DuplicateEntityException("Book with title '" + bookRequest.title() + "' already exists.");
                }, () -> {
                });

        /**
         * Checks for duplicate book isbn, if duplicate found ith will throw a custom exception
         */
        bookRepository.findBookByIsbn(bookRequest.isbn())
                .ifPresentOrElse((s) -> {
                    throw new DuplicateEntityException("Book with isbn '" + bookRequest.isbn() + "' already exists.");
                }, () -> {
                });


        /**
         * Checks for if author id is a valid author in db. If not present it will throw a not found exception
         */
        if (!authorService.checkAuthorById(bookRequest.authorId()))
            throw new EntityNotFoundException("Author not found");


        /**
         * Create new book object
         */
        Book newBook = Book.builder()
                .title(bookRequest.title())
                .isbn(bookRequest.isbn())
                .publicationYear(bookRequest.publicationYear())
                .build();

        /**
         * getting author object and set to book object
         */
        Author author = authorService.getAuthorById(bookRequest.authorId());
        newBook.setAuthor(author);

        /**
         * Setting the available flag true for new book
         */
        newBook.setAvailable(Boolean.TRUE);

        Book createdBook = bookRepository.save(newBook);
        return createdBook;
    }

    /**
     * For finding book with id
     * @param id
     * @return book object
     */
    @Override
    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));
    }

    /**
     * for retrieving all books
     * @return list of book
     */
    @Override
    public List<Book> getBooks() {
        return bookRepository.findAll();
    }

    /**
     * for deleting book by id
     * @param id
     */
    @Override
    public void deleteBookById(Long id) {

        /**
         * Checking if book with id is a valid book
         */
        bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));
        bookRepository.deleteById(id);
    }

    @Override
    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public List<Book> findBooksByAuthor(Long authorId) {

        if (!authorService.checkAuthorById(authorId))
            throw new EntityNotFoundException("Author not found");


        return bookRepository.findBooksByAuthorId(authorId);
    }

    @Override
    public List<Book> findAvailableBooksForRent() {
        return bookRepository.findBooksByAvailableTrue();
    }

    @Override
    public List<Book> findRentedBooks() {
        return bookRepository.findBooksByAvailableFalse();
    }


}
