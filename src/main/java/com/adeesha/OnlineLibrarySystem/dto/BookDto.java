package com.adeesha.OnlineLibrarySystem.dto;

import lombok.Data;

@Data
public class BookDto {
    private Long id;
    private String title;
    private String author;
    private Integer publishedYear;
    private Integer availableCopies;
    private Integer totalCopies;
    private boolean available;
}
