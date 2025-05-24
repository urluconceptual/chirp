package org.unibuc.chirp.domain.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.unibuc.chirp.domain.dto.user.get.GetUserDetailsResponseDto;

import java.util.List;

public interface FriendService {
    void sendFriendRequest(String currentUsername, String targetUsername);

    void acceptFriendRequest(String currentUsername, String targetUsername);

    void rejectFriendRequest(String currentUsername, String targetUsername);

    void removeFriend(String currentUsername, String targetUsername);

    Page<GetUserDetailsResponseDto> getFriends(String username, Pageable pageable);

    List<GetUserDetailsResponseDto> getFriendRequests(String username);
}
