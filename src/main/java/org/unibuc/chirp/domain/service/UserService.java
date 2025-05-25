package org.unibuc.chirp.domain.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import org.unibuc.chirp.domain.dto.user.get.GetUserDetailsResponseDto;
import org.unibuc.chirp.domain.dto.user.get.GetUserResponseDto;
import org.unibuc.chirp.domain.dto.user.update.UpdateUserRequestDto;
import org.unibuc.chirp.domain.dto.user.update.UpdateUserResponseDto;

public interface UserService {
    GetUserDetailsResponseDto getUserDetails(String username);

    Page<GetUserResponseDto> getUsersPage(String searchQuery, Pageable pageable);

    UpdateUserResponseDto updateUserDetails(String username, UpdateUserRequestDto updateUserRequestDto,
                                            MultipartFile avatarFile);

    Page<GetUserDetailsResponseDto> exploreUsers(String searchQuery, Pageable pageable);
}
