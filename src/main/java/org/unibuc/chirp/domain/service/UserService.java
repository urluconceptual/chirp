package org.unibuc.chirp.domain.service;

import org.unibuc.chirp.domain.dto.user.create.CreateUserRequestDto;
import org.unibuc.chirp.domain.dto.user.create.CreateUserResponseDto;
import org.unibuc.chirp.domain.dto.user.get.GetUserDetailsResponseDto;
import org.unibuc.chirp.domain.dto.user.update.UpdateUserRequestDto;
import org.unibuc.chirp.domain.dto.user.update.UpdateUserResponseDto;

public interface UserService {
    CreateUserResponseDto createUser(CreateUserRequestDto createUserRequestDto);

    GetUserDetailsResponseDto getUserDetails(String username);

    UpdateUserResponseDto updateUserDetails(String username, UpdateUserRequestDto updateUserRequestDto);
}
