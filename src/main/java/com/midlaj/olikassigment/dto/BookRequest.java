package com.midlaj.olikassigment.dto;

import com.midlaj.olikassigment.annotation.MaxYear;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.ISBN;

public record BookRequest(@NotBlank(message = "Book title is required")
                          @Size(min = 3, max = 256, message = "Book title must be between 3 and 256 characters")
                          String title,
                          @Min(value = 0, message = "Invalid author")
                          Long authorId,
                          @NotBlank(message = "ISBN is required")
                          @ISBN(message = "Invalid ISBN format", type = ISBN.Type.ANY)
                          String isbn,
                          @NotNull(message = "Publication year is required")
                          @Min(value = 1000, message = "Publication year must be greater than 999")
                          @MaxYear(message = "publication year should not be greater than present year")
                          Integer publicationYear) {

    ;
}
