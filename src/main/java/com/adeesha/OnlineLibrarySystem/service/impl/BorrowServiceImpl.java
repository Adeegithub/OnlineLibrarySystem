package com.adeesha.OnlineLibrarySystem.service.impl;

import com.adeesha.OnlineLibrarySystem.dto.BorrowRecordDto;
import com.adeesha.OnlineLibrarySystem.dto.BorrowRequestDto;
import com.adeesha.OnlineLibrarySystem.entity.Book;
import com.adeesha.OnlineLibrarySystem.entity.BorrowRecord;
import com.adeesha.OnlineLibrarySystem.entity.User;
import com.adeesha.OnlineLibrarySystem.exception.BookNotAvailableException;
import com.adeesha.OnlineLibrarySystem.exception.ResourceNotFoundException;
import com.adeesha.OnlineLibrarySystem.repository.BookRepository;
import com.adeesha.OnlineLibrarySystem.repository.BorrowRecordRepository;
import com.adeesha.OnlineLibrarySystem.repository.UserRepository;
import com.adeesha.OnlineLibrarySystem.service.BorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class BorrowServiceImpl implements BorrowService {

    private final BorrowRecordRepository borrowRecordRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    @Autowired
    public BorrowServiceImpl(BorrowRecordRepository borrowRecordRepository,
                             UserRepository userRepository,
                             BookRepository bookRepository) {
        this.borrowRecordRepository = borrowRecordRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    @Transactional
    public BorrowRecordDto borrowBook(Long userId, BorrowRequestDto borrowRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Book book = bookRepository.findById(borrowRequest.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + borrowRequest.getBookId()));

        if (book.getAvailableCopies() <= 0) {
            throw new BookNotAvailableException("No available copies of this book");
        }

        // Check if user already has an active loan for this book
        if (borrowRecordRepository.findActiveByUserIdAndBookId(userId, book.getId()).isPresent()) {
            throw new BookNotAvailableException("You already have an active loan for this book");
        }

        BorrowRecord borrowRecord = new BorrowRecord();
        borrowRecord.setUser(user);
        borrowRecord.setBook(book);

        // Update book available copies
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);

        BorrowRecord savedRecord = borrowRecordRepository.save(borrowRecord);
        return convertToDto(savedRecord);
    }

    @Override
    @Transactional
    public BorrowRecordDto returnBook(Long userId, Long borrowId) {
        BorrowRecord borrowRecord = borrowRecordRepository.findById(borrowId)
                .orElseThrow(() -> new ResourceNotFoundException("Borrow record not found"));

        if (!borrowRecord.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("This borrow record doesn't belong to the user");
        }

        if (borrowRecord.getStatus() != BorrowRecord.BorrowStatus.ACTIVE) {
            throw new IllegalArgumentException("This book has already been returned");
        }

        // Update borrow record
        borrowRecord.setReturnDate(LocalDateTime.now());
        borrowRecord.setStatus(BorrowRecord.BorrowStatus.RETURNED);

        // Update book available copies
        Book book = borrowRecord.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);

        BorrowRecord updatedRecord = borrowRecordRepository.save(borrowRecord);
        return convertToDto(updatedRecord);
    }

    @Override
    public Page<BorrowRecordDto> getUserBorrowHistory(Long userId, Pageable pageable) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }

        Page<BorrowRecord> borrowRecords = borrowRecordRepository.findByUserId(userId, pageable);
        return borrowRecords.map(this::convertToDto);
    }

    private BorrowRecordDto convertToDto(BorrowRecord record) {
        BorrowRecordDto dto = new BorrowRecordDto();
        dto.setId(record.getId());
        dto.setUserId(record.getUser().getId());
        dto.setUserName(record.getUser().getName());
        dto.setBookId(record.getBook().getId());
        dto.setBookTitle(record.getBook().getTitle());
        dto.setBookAuthor(record.getBook().getAuthor());
        dto.setBorrowDate(record.getBorrowDate());
        dto.setDueDate(record.getDueDate());
        dto.setReturnDate(record.getReturnDate());
        dto.setStatus(record.getStatus().name());
        return dto;
    }

    @Override
    public BorrowRecordDto getBorrowRecordById(Long userId, Long borrowId) {
        BorrowRecord borrowRecord = borrowRecordRepository.findById(borrowId)
                .orElseThrow(() -> new ResourceNotFoundException("Borrow record not found with id: " + borrowId));

        //Verify that this borrow record belongs to the requesting user
        if (!borrowRecord.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Access denied: This borrow record doesn't belong to the user");
        }

        return convertToDto(borrowRecord);
    }

}
