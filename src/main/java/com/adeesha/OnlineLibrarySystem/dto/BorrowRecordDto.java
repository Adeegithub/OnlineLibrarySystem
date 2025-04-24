package com.adeesha.OnlineLibrarySystem.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BorrowRecordDto {
    private Long id;
    private Long userId;
    private String userName;
    private Long bookId;
    private String bookTitle;
    private String bookAuthor;
    private LocalDateTime borrowDate;
    private LocalDateTime dueDate;
    private LocalDateTime returnDate;
    private String status;
}
