package com.adeesha.OnlineLibrarySystem.service;

import com.adeesha.OnlineLibrarySystem.dto.BookDto;
import com.adeesha.OnlineLibrarySystem.dto.BookSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto getBookById(Long id);
    Page<BookDto> searchBooks(BookSearchCriteria criteria, Pageable pageable);

}
