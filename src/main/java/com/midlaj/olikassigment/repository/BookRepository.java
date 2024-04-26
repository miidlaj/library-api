package com.midlaj.olikassigment.repository;

import com.midlaj.olikassigment.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findBookByTitle(String title);

    Optional<Book> findBookByIsbn(String isbn);

    List<Book> findBooksByAuthorId(Long id);

    List<Book> findBooksByAvailableTrue();

    List<Book> findBooksByAvailableFalse();

}
