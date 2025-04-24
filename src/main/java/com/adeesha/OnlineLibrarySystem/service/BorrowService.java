package com.adeesha.OnlineLibrarySystem.service;

import com.adeesha.OnlineLibrarySystem.dto.BorrowRecordDto;
import com.adeesha.OnlineLibrarySystem.dto.BorrowRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BorrowService {
    BorrowRecordDto borrowBook(Long userId, BorrowRequestDto borrowRequest);
    BorrowRecordDto returnBook(Long userId, Long borrowId);
    Page<BorrowRecordDto> getUserBorrowHistory(Long userId, Pageable pageable);
    BorrowRecordDto getBorrowRecordById(Long userId, Long borrowId);
}
