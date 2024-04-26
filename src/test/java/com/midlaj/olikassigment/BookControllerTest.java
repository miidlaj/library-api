package com.midlaj.olikassigment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.midlaj.olikassigment.controller.BookController;
import com.midlaj.olikassigment.dto.BookRequest;
import com.midlaj.olikassigment.model.Author;
import com.midlaj.olikassigment.model.Book;
import com.midlaj.olikassigment.repository.BookRepository;
import com.midlaj.olikassigment.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Random;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@WebMvcTest(BookController.class)
@AutoConfigureMockMvc
public class BookControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService mockBookService;

    @MockBean
    private BookRepository mockBookRepo;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final String BASE_URL = "/api/book";

    @Test
    public void testCreateNewBookValidRequestReturnCreated() throws Exception {

        // Given a valid BookRequest object
        BookRequest bookRequest = new BookRequest("test_book_" + UUID.randomUUID(), 1L, generateRandomIsbn13(), 2020);

        // Mock the book object returned by the book service when createNewBook method is called
        Author author = new Author(1L, "test_author_" + UUID.randomUUID(), "add a new 64 characters biography to author, other wise test will not pass correctly.");
        Book createdBook = new Book(1L, bookRequest.title(), author, bookRequest.isbn(), bookRequest.publicationYear(), true);
        when(mockBookService.createNewBook(bookRequest)).thenReturn(createdBook);

        // Perform the POST request
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL +"/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookRequest)))
                .andReturn();

        // Verify response status and content
        assertEquals(HttpStatus.CREATED.value(), result.getResponse().getStatus());
        assertEquals(objectMapper.writeValueAsString(createdBook), result.getResponse().getContentAsString());
    }

    public static String generateRandomIsbn13() {
        StringBuilder isbn = new StringBuilder("978");
        Random random = new Random();

        // Generate 9 random digits
        for (int i = 0; i < 9; i++) {
            isbn.append(random.nextInt(10));
        }

        // Calculate the check digit
        int sum = 0;
        for (int i = 0; i < 12; i++) {
            int digit = Character.getNumericValue(isbn.charAt(i));
            sum += (i % 2 == 0) ? digit : digit * 3;
        }
        int checkDigit = (10 - (sum % 10)) % 10;
        isbn.append(checkDigit);

        return isbn.toString();
    }


    @Test()
    public void testCreateNewBookInvalidRequestThrowsValidationException() throws Exception {

        /**
         * create an invalid BookRequest with invalid isbn, invalid publication year
         */
        BookRequest bookRequest = new BookRequest("title", 2L, "invalid isbn", 2025);

        // Perform the POST request
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL +"/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookRequest)))
                .andReturn();


        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
    }



}
