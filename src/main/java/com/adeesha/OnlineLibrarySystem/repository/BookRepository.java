package com.adeesha.OnlineLibrarySystem.repository;

import com.adeesha.OnlineLibrarySystem.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Page<Book> findByAvailableCopiesGreaterThan(int copies, Pageable pageable);

    Page<Book> findByAuthorContainingIgnoreCase(String author, Pageable pageable);

    Page<Book> findByPublishedYear(Integer year, Pageable pageable);

    @Query("SELECT b FROM Book b WHERE b.availableCopies > 0 AND " +
            "(:author IS NULL OR LOWER(b.author) LIKE LOWER(CONCAT('%', :author, '%'))) AND " +
            "(:year IS NULL OR b.publishedYear = :year)")
    Page<Book> findAvailableBooksByAuthorAndYear(String author, Integer year, Pageable pageable);

    @Query("SELECT b FROM Book b WHERE " +
            "LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(b.author) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Book> searchBooks(String keyword, Pageable pageable);
}
