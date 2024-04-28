package com.midlaj.olikassigment;

import com.midlaj.olikassigment.dto.AuthorRequest;
import com.midlaj.olikassigment.exception.DuplicateEntityException;
import com.midlaj.olikassigment.exception.EntityNotFoundException;
import com.midlaj.olikassigment.model.Author;
import com.midlaj.olikassigment.repository.AuthorRepository;
import com.midlaj.olikassigment.service.AuthorService;
import com.midlaj.olikassigment.service.AuthorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Author Service implementation tests
 */
@SpringBootTest
public class AuthorServiceImplTest {

    @Mock
    private AuthorRepository authorRepository;

    private AuthorService authorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        authorService = new AuthorServiceImpl(authorRepository) {
        };
    }

    @Test
    void createNewAuthor_WithValidRequest_ShouldCreateAuthor() {
        /**
         * Creating author req test object
         */
        AuthorRequest authorRequest = new AuthorRequest("test_author_" + UUID.randomUUID(), "Test_Biography_Required_min_64_Characters" + UUID.randomUUID());

        /**
         * Mocking the dao calls
         */
        Author createdAuthor = Author.builder().id(1L).name(authorRequest.name()).biography(authorRequest.biography()).build();
        when(authorRepository.findAuthorByName(authorRequest.name())).thenReturn(Optional.empty());
        when(authorRepository.save(any(Author.class))).thenReturn(createdAuthor);

        /**
         * Calling the service class method
         */
        Author result = authorService.createNewAuthor(authorRequest);

        /**
         * verifying the results
         */
        assertEquals(createdAuthor, result);
        verify(authorRepository, times(1)).findAuthorByName(authorRequest.name());
        verify(authorRepository, times(1)).save(any(Author.class));
    }

    @Test
    void createNewAuthor_WithDuplicateName_ShouldThrowDuplicateEntityException() {
        /**
         * Creating new author req object fot testing
         */
        AuthorRequest authorRequest = new AuthorRequest("test_author_" + UUID.randomUUID(), "Test_Biography_Required_min_64_Characters" + UUID.randomUUID());

        /**
         * mocking dao calls
         */
        when(authorRepository.findAuthorByName(authorRequest.name())).thenReturn(Optional.of(new Author()));

        /**
         * verifying results
         */
        assertThrows(DuplicateEntityException.class, () -> authorService.createNewAuthor(authorRequest));
        verify(authorRepository, times(1)).findAuthorByName(authorRequest.name());
        verify(authorRepository, never()).save(any(Author.class));
    }

    @Test
    void getAuthorById_WithValidId_ShouldReturnAuthor() {
        /**
         * Author id and mock author
         */
        Long id = 1L;
        Author author = Author.builder().id(id).name("John Doe").biography("Biography").build();

        /**
         * mocking dao call
         */
        when(authorRepository.findById(id)).thenReturn(Optional.of(author));

        /**
         * calling service method
         */
        Author result = authorService.getAuthorById(id);

        /**
         * verifying
         */
        assertEquals(author, result);
    }

    @Test
    void getAuthorById_WithInvalidId_ShouldThrowEntityNotFoundException() {
        /**
         * author id
         */
        Long id = 1L;

        /**
         * mocking dao call
         */
        when(authorRepository.findById(id)).thenReturn(Optional.empty());

        /**
         * verifying
         */
        assertThrows(EntityNotFoundException.class, () -> authorService.getAuthorById(id));
    }

    @Test
    void getAuthors_ShouldReturnListOfAuthors() {

        /**
         * creating mock object list
         */
        List<Author> authors = new ArrayList<>();
        authors.add(Author.builder().id(1L).name("John Doe").biography("Biography").build());
        authors.add(Author.builder().id(2L).name("Jane Smith").biography("Biography").build());

        /**
         * mocking dao call
         */
        when(authorRepository.findAll()).thenReturn(authors);

        /**
         * calling service method
         */
        List<Author> result = authorService.getAuthors();

        /**
         * verifying
         */
        assertEquals(authors.size(), result.size());
        assertTrue(result.containsAll(authors));
    }

    @Test
    void deleteAuthorById_WithValidId_ShouldDeleteAuthor() {
        /**
         * mocking object for test
         */
        Long id = 1L;
        Author author = Author.builder().id(id).name("John Doe").biography("Biography").build();
        when(authorRepository.findById(id)).thenReturn(Optional.of(author));

        /**
         * calling service method
         */
        authorService.deleteAuthorById(id);

        /**
         * verifying
         */
        verify(authorRepository, times(1)).findById(id);
        verify(authorRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteAuthorById_WithInvalidId_ShouldThrowEntityNotFoundException() {
        /**
         * mocking author id
         */
        Long id = 1L;

        /**
         * mocking dao call
         */
        when(authorRepository.findById(id)).thenReturn(Optional.empty());

        /**
         * verifying
         */
        assertThrows(EntityNotFoundException.class, () -> authorService.deleteAuthorById(id));
        verify(authorRepository, times(1)).findById(id);
        verify(authorRepository, never()).deleteById(id);
    }

    @Test
    void checkAuthorById_WithValidId_ShouldReturnTrue() {
        /**
         * mocking author id
         */
        Long id = 1L;

        /**
         * mocking dao call
         */
        when(authorRepository.existsById(id)).thenReturn(true);

        /**
         * calling service mehtod
         */
        boolean result = authorService.checkAuthorById(id);

        /**
         * verifying
         */
        assertTrue(result);
        verify(authorRepository, times(1)).existsById(id);
    }

    @Test
    void checkAuthorById_WithInvalidId_ShouldReturnFalse() {
        /**
         * test author id
         */
        Long id = 1L;

        /**
         * mocking dao call
         */
        when(authorRepository.existsById(id)).thenReturn(false);

        /**
         * calling service method
         */
        boolean result = authorService.checkAuthorById(id);

        /**
         * verifying
         */
        assertFalse(result);
        verify(authorRepository, times(1)).existsById(id);
    }
}