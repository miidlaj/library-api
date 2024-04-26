package com.midlaj.olikassigment.service;

import com.midlaj.olikassigment.dto.BookRequest;
import com.midlaj.olikassigment.model.Book;

import java.util.List;

public interface BookService {

    Book createNewBook(BookRequest bookRequest);

    Book getBookById(Long id);

    List<Book> getBooks();

    void deleteBookById(Long id);

    Book saveBook(Book book);


    List<Book> findBooksByAuthor(Long authorId);

    List<Book> findAvailableBooksForRent();

    List<Book> findRentedBooks();
}
