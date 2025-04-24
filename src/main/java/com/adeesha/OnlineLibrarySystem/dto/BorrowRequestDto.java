package com.adeesha.OnlineLibrarySystem.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BorrowRequestDto {
    @NotNull(message = "Book ID is required")
    private Long bookId;
}
