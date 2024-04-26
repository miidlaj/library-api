package com.midlaj.olikassigment.controller;

import com.midlaj.olikassigment.dto.AuthorRequest;
import com.midlaj.olikassigment.model.Author;
import com.midlaj.olikassigment.service.AuthorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Author Controller where all the crud operations of author mapped
 */

@RestController
@RequestMapping("/api/author")
public class AuthorController {


    private final AuthorService authorService;


    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    /**
     * for creating new author
     * @param authorRequest
     * @return Author object
     */
    @PostMapping("/new")
    public ResponseEntity<?> createAuthor(@RequestBody @Valid AuthorRequest authorRequest) {
        Author createdAuthor = authorService.createNewAuthor(authorRequest);
        return new ResponseEntity<>(createdAuthor, HttpStatus.CREATED);
    }

    /**
     * for getting author with id
     * @param id
     * @return Author object
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getAuthorById(@PathVariable Long id) {
        Author createdAuthor = authorService.getAuthorById(id);
        return new ResponseEntity<>(createdAuthor, HttpStatus.OK);
    }


    /**
     * fot getting all authors
     * @return List of author
     */
    @GetMapping()
    public ResponseEntity<?> getAuthors() {
        return ResponseEntity.ok(authorService.getAuthors());
    }

    /**
     * fot deleting an author by id
     * @param id
     * @return void
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAuthorById(@PathVariable Long id) {
        authorService.deleteAuthorById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
