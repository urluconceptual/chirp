package org.unibuc.chirp.domain.service;

import org.unibuc.chirp.domain.dto.user.get.GetUserDetailsResponseDto;
import org.unibuc.chirp.domain.dto.user.update.UpdateUserRequestDto;
import org.unibuc.chirp.domain.dto.user.update.UpdateUserResponseDto;

public interface UserService {
    GetUserDetailsResponseDto getUserDetails(String username);

    UpdateUserResponseDto updateUserDetails(String username, UpdateUserRequestDto updateUserRequestDto);
}
