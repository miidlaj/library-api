package com.midlaj.olikassigment;

import com.midlaj.olikassigment.repository.BookRepository;
import com.midlaj.olikassigment.service.AuthorService;
import com.midlaj.olikassigment.service.BookServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorService authorService;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    void createNewBook_WhenBookDoesNotExist_ShouldCreateNewBook() {
//        // Given
//        BookRequest bookRequest = new BookRequest("Title", 1L, "ISBN", 2022);
//        Author author = new Author();
//        author.setId(1L);
//
//        when(bookRepository.findBookByTitle("jd")).thenReturn(Optional.empty());
//        when(bookRepository.findBookByIsbn(anyString())).thenReturn(Optional.empty());
//        when(authorService.checkAuthorById(anyLong())).thenReturn(true);
//        when(authorService.getAuthorById(anyLong())).thenReturn(author);
//        when(bookRepository.save(any())).thenReturn(new Book());
//
//        // When
//        Book createdBook =  bookService.createNewBook(bookRequest);
//
//        // Then
//        assertNotNull(createdBook);
//        assertEquals("Title", createdBook.getTitle());
//        assertEquals("ISBN", createdBook.getIsbn());
//        assertEquals(2022, createdBook.getPublicationYear());
//        assertTrue(createdBook.getAvailable());
//        verify(bookRepository, times(1)).save(any());
    }



}
