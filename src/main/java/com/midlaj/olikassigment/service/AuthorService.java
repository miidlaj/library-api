package com.midlaj.olikassigment.service;

import com.midlaj.olikassigment.dto.AuthorRequest;
import com.midlaj.olikassigment.model.Author;

import java.util.List;

public interface AuthorService {


    Author createNewAuthor(AuthorRequest authorRequest);

    Author getAuthorById(Long id);

    List<Author> getAuthors();

    void deleteAuthorById(Long id);

    Boolean checkAuthorById(Long id);
}
