package org.unibuc.chirp.domain.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import org.unibuc.chirp.domain.dto.user.get.GetUserDetailsResponseDto;
import org.unibuc.chirp.domain.dto.user.update.UpdateUserRequestDto;
import org.unibuc.chirp.domain.dto.user.update.UpdateUserResponseDto;

public interface FriendService {
    void sendFriendRequest(String currentUsername, String targetUsername);

    void acceptFriendRequest(String currentUsername, String targetUsername);

    void rejectFriendRequest(String currentUsername, String targetUsername);

    Page<GetUserDetailsResponseDto> getFriends(String username, Pageable pageable);

    Page<GetUserDetailsResponseDto> getFriendRequests(String username, Pageable pageable);
}
