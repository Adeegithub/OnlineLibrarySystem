package com.adeesha.OnlineLibrarySystem.repository;

import com.adeesha.OnlineLibrarySystem.entity.BorrowRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {
    Page<BorrowRecord> findByUserId(Long userId, Pageable pageable);

    @Query("SELECT br FROM BorrowRecord br WHERE br.user.id = :userId AND br.book.id = :bookId AND br.status = 'ACTIVE'")
    Optional<BorrowRecord> findActiveByUserIdAndBookId(Long userId, Long bookId);
}
