package com.midlaj.olikassigment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthorRequest(@NotBlank(message = "Name is mandatory") @Size(min = 2, max = 128, message = "Name must be between 2 and 256 characters") String name,
                            @NotBlank(message = "Biography is mandatory") @Size(min = 64, max = 1024, message = "Biography must be between 64 and 1024 characters") String biography) {
}


