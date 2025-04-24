package com.adeesha.OnlineLibrarySystem.dto;

import lombok.Data;

@Data
public class BookSearchCriteria {
    private String author;
    private Integer publishedYear;
    private String keyword;
    private Boolean availableOnly = false;
}
