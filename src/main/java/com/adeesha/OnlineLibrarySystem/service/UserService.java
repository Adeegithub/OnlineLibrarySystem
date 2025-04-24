package com.adeesha.OnlineLibrarySystem.service;

import com.adeesha.OnlineLibrarySystem.dto.UserDto;
import com.adeesha.OnlineLibrarySystem.dto.UserRegistrationDto;

public interface UserService {
    UserDto registerUser(UserRegistrationDto registrationDto);
    UserDto getUserById(Long userId);
    String getUsernameById(Long userId);
}
