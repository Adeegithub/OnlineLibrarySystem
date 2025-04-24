package com.adeesha.OnlineLibrarySystem.service.impl;

import com.adeesha.OnlineLibrarySystem.dto.BookDto;
import com.adeesha.OnlineLibrarySystem.dto.BookSearchCriteria;
import com.adeesha.OnlineLibrarySystem.entity.Book;
import com.adeesha.OnlineLibrarySystem.exception.ResourceNotFoundException;
import com.adeesha.OnlineLibrarySystem.repository.BookRepository;
import com.adeesha.OnlineLibrarySystem.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    @Cacheable(value = "books", key = "#id")
    public BookDto getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
        return convertToDto(book);
    }

    @Override
    @Cacheable(value = "bookSearchResults", key = "{#criteria.toString(), #pageable.pageNumber, #pageable.pageSize}")
    public Page<BookDto> searchBooks(BookSearchCriteria criteria, Pageable pageable) {
        Page<Book> bookPage;

        if (StringUtils.hasText(criteria.getKeyword())) {
            bookPage = bookRepository.searchBooks(criteria.getKeyword(), pageable);
        } else if (StringUtils.hasText(criteria.getAuthor()) && criteria.getPublishedYear() != null) {
            bookPage = bookRepository.findAvailableBooksByAuthorAndYear(
                    criteria.getAuthor(), criteria.getPublishedYear(), pageable);
        } else if (StringUtils.hasText(criteria.getAuthor())) {
            bookPage = bookRepository.findByAuthorContainingIgnoreCase(criteria.getAuthor(), pageable);
        } else if (criteria.getPublishedYear() != null) {
            bookPage = bookRepository.findByPublishedYear(criteria.getPublishedYear(), pageable);
        } else if (Boolean.TRUE.equals(criteria.getAvailableOnly())) {
            bookPage = bookRepository.findByAvailableCopiesGreaterThan(0, pageable);
        } else {
            bookPage = bookRepository.findAll(pageable);
        }

        return bookPage.map(this::convertToDto);
    }

    private BookDto convertToDto(Book book) {
        BookDto dto = new BookDto();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setAuthor(book.getAuthor());
        dto.setPublishedYear(book.getPublishedYear());
        dto.setAvailableCopies(book.getAvailableCopies());
        dto.setTotalCopies(book.getTotalCopies());
        return dto;
    }
}
