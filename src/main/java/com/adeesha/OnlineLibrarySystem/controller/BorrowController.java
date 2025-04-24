package com.adeesha.OnlineLibrarySystem.controller;

import com.adeesha.OnlineLibrarySystem.dto.BorrowRecordDto;
import com.adeesha.OnlineLibrarySystem.dto.BorrowRequestDto;
import com.adeesha.OnlineLibrarySystem.entity.User;
import com.adeesha.OnlineLibrarySystem.repository.UserRepository;
import com.adeesha.OnlineLibrarySystem.service.BorrowService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/borrow")
public class BorrowController {

    private final BorrowService borrowService;
    private final UserRepository userRepository;

    @Autowired
    public BorrowController(BorrowService borrowService, UserRepository userRepository) {
        this.borrowService = borrowService;
        this.userRepository = userRepository;
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<BorrowRecordDto> borrowBook(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody BorrowRequestDto borrowRequest) {
        User user = userRepository.findByEmail(userDetails.getUsername());
        BorrowRecordDto borrowRecordDto = borrowService.borrowBook(user.getId(), borrowRequest);
        return new ResponseEntity<>(borrowRecordDto, HttpStatus.CREATED);
    }

    @PutMapping("/{borrowId}/return")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<BorrowRecordDto> returnBook(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long borrowId) {
        User user = userRepository.findByEmail(userDetails.getUsername());
        BorrowRecordDto borrowRecordDto = borrowService.returnBook(user.getId(), borrowId);
        return ResponseEntity.ok(borrowRecordDto);
    }

    @GetMapping("/history")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<BorrowRecordDto>> getBorrowHistory(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "borrowDate") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        User user = userRepository.findByEmail(userDetails.getUsername());
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<BorrowRecordDto> history = borrowService.getUserBorrowHistory(user.getId(), pageable);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/{borrowId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<BorrowRecordDto> getBorrowDetails(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long borrowId) {
        User user = userRepository.findByEmail(userDetails.getUsername());
        BorrowRecordDto borrowRecord = borrowService.getBorrowRecordById(user.getId(), borrowId);
        return ResponseEntity.ok(borrowRecord);
    }

}
