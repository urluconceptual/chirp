package org.unibuc.chirp.impl.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.val;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.unibuc.chirp.domain.dto.user.get.GetUserDetailsResponseDto;
import org.unibuc.chirp.domain.dto.user.update.UpdateUserRequestDto;
import org.unibuc.chirp.domain.dto.user.update.UpdateUserResponseDto;
import org.unibuc.chirp.domain.entity.UserFriendshipEntity;
import org.unibuc.chirp.domain.repository.UserFriendshipRepository;
import org.unibuc.chirp.domain.repository.UserProfileRepository;
import org.unibuc.chirp.domain.repository.UserRepository;
import org.unibuc.chirp.domain.service.FriendService;
import org.unibuc.chirp.domain.service.UserService;
import org.unibuc.chirp.impl.service.utils.ServiceUtils;
import org.unibuc.chirp.impl.validator.FriendValidator;
import org.unibuc.chirp.impl.validator.UserValidator;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
@AllArgsConstructor
@Getter
public class FriendServiceImpl implements FriendService {
    private UserFriendshipRepository userFriendshipRepository;
    private UserRepository userRepository;
    private UserValidator userValidator;
    private FriendValidator friendValidator;

    @Override
    public void sendFriendRequest(String currentUsername, String targetUsername) {
        this.userValidator.validate(currentUsername);
        this.userValidator.validate(targetUsername);

        val requester = this.userRepository.findByUsername(currentUsername).get();
        val addressee = this.userRepository.findByUsername(targetUsername).get();

        val userFriendshipEntity = UserFriendshipEntity.builder()
                .requestedAt(LocalDateTime.now())
                .status(UserFriendshipEntity.FriendshipStatus.PENDING)
                .addressee(addressee)
                .requester(requester)
                .build();

        this.userFriendshipRepository.save(userFriendshipEntity);
    }

    @Override
    public void acceptFriendRequest(String currentUsername, String targetUsername) {
        this.userValidator.validate(currentUsername);
        this.userValidator.validate(targetUsername);

        val requester = this.userRepository.findByUsername(currentUsername).get();
        val addressee = this.userRepository.findByUsername(targetUsername).get();

        val friendshipRequest = this.userFriendshipRepository.findByRequesterAndAddressee(requester, addressee)
                .orElseThrow();

        this.friendValidator.validateAccept(friendshipRequest);

        friendshipRequest.setStatus(UserFriendshipEntity.FriendshipStatus.ACCEPTED);
        friendshipRequest.setRespondedAt(LocalDateTime.now());

        this.userFriendshipRepository.save(friendshipRequest);
    }

    @Override
    public void rejectFriendRequest(String currentUsername, String targetUsername) {
        this.userValidator.validate(currentUsername);
        this.userValidator.validate(targetUsername);

        val requester = this.userRepository.findByUsername(currentUsername).get();
        val addressee = this.userRepository.findByUsername(targetUsername).get();

        val friendshipRequest = this.userFriendshipRepository.findByRequesterAndAddressee(requester, addressee)
                .orElseThrow();

        this.friendValidator.validateReject(friendshipRequest);

        friendshipRequest.setStatus(UserFriendshipEntity.FriendshipStatus.REJECTED);
        friendshipRequest.setRespondedAt(LocalDateTime.now());

        this.userFriendshipRepository.save(friendshipRequest);
    }

    @Override
    public Page<GetUserDetailsResponseDto> getFriends(String username, Pageable pageable) {
        this.userValidator.validate(username);

        val user = this.userRepository.findByUsername(username).orElseThrow();
        val friends = this.userFriendshipRepository.findByRequesterOrAddressee(user, user, pageable);

        return friends.map(friendship -> {
            val friend = friendship.getRequester().equals(user) ? friendship.getAddressee() : friendship.getRequester();

            return ServiceUtils.toGetUserDetailsResponseDto(friend);
        });
    }

    @Override
    public Page<GetUserDetailsResponseDto> getFriendRequests(String username, Pageable pageable) {
        return null;
    }
}