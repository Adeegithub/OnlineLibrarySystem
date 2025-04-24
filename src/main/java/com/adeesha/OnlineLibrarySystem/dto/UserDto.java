package com.adeesha.OnlineLibrarySystem.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private LocalDateTime createdAt;
}