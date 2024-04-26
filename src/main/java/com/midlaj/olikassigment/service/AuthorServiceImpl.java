package com.midlaj.olikassigment.service;

import com.midlaj.olikassigment.dto.AuthorRequest;
import com.midlaj.olikassigment.exception.DuplicateEntityException;
import com.midlaj.olikassigment.exception.EntityNotFoundException;
import com.midlaj.olikassigment.model.Author;
import com.midlaj.olikassigment.repository.AuthorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorServiceImpl implements AuthorService {

    public final AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public Author createNewAuthor(AuthorRequest authorRequest) {

        /**
         * Checks for duplicate author name, if duplicate found ith will throw a custom exception
         */
        authorRepository.findAuthorByName(authorRequest.name())
                .ifPresentOrElse((s) -> {
                    throw new DuplicateEntityException("Author with name '" + authorRequest.name() + "' already exists.");
                }, () -> { });

        /**
         * Create new author object
         */
        Author newAuthor = Author.builder()
                .name(authorRequest.name()).biography(authorRequest.biography())
                .build();

        /**
         *  Save new author object in db
         */
        Author createdAuthor = authorRepository.save(newAuthor);

        return createdAuthor;
    }

    /**
     * For finding author with id
     * @param id
     * @return author object
     */
    @Override
    public Author getAuthorById(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Author not found"));
    }

    /**
     * for retrieving all authors
     * @return list of author
     */
    @Override
    public List<Author> getAuthors() {

        return authorRepository.findAll();

    }

    /**
     * for deleting author by id
     * @param id
     */
    @Override
    public void deleteAuthorById(Long id) {

        /**
         * Checking if author with id is a valid author
         */
        authorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Author not found"));
        authorRepository.deleteById(id);
    }

    /**
     * for checking if the author with id is valid or not
     * @param id
     * @return a boolean value
     */
    @Override
    public Boolean checkAuthorById(Long id) {
        return authorRepository.existsById(id);
    }

}
