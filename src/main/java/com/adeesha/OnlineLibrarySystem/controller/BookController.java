package com.adeesha.OnlineLibrarySystem.controller;

import com.adeesha.OnlineLibrarySystem.dto.BookDto;
import com.adeesha.OnlineLibrarySystem.dto.BookSearchCriteria;
import com.adeesha.OnlineLibrarySystem.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")

public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    /*
    Assume that there are already books are stored in DB
     */
    @GetMapping("/{id}")
    public ResponseEntity<BookDto> getBookById(@PathVariable Long id) {
        BookDto bookDto = bookService.getBookById(id);
        return ResponseEntity.ok(bookDto);
    }

    //This API can also be used to see users to explore and view the collection of books
    //and see which books have copies available to be borrowed.
    @GetMapping
    public ResponseEntity<Page<BookDto>> searchBooks(
            @RequestParam(required = false) String author,
            @RequestParam(required = false) Integer publishedYear,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false, defaultValue = "false") Boolean availableOnly,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        BookSearchCriteria criteria = new BookSearchCriteria();
        criteria.setAuthor(author);
        criteria.setPublishedYear(publishedYear);
        criteria.setKeyword(keyword);
        criteria.setAvailableOnly(availableOnly);

        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<BookDto> books = bookService.searchBooks(criteria, pageable);
        return ResponseEntity.ok(books);
    }
}
