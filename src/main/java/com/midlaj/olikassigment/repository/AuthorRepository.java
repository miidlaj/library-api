package com.midlaj.olikassigment.repository;

import com.midlaj.olikassigment.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    Optional<Author> findAuthorByName(String name);

}
