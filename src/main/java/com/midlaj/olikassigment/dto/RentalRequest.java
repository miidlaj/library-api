package com.midlaj.olikassigment.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RentalRequest(
        @NotNull(message = "Book is required")
        @Min(value = 0, message = "Invalid book")
        Long bookId,
        @NotBlank(message = "Renter name is required")
        @Size(min = 3, max = 256, message = "Renter name must be between 3 and 256 characters")
        String renterName
) {
}
