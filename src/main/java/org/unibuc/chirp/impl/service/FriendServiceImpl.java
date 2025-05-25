package org.unibuc.chirp.impl.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.val;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.unibuc.chirp.domain.dto.user.get.GetUserDetailsResponseDto;
import org.unibuc.chirp.domain.entity.UserFriendshipEntity;
import org.unibuc.chirp.domain.repository.UserFriendshipRepository;
import org.unibuc.chirp.domain.repository.UserRepository;
import org.unibuc.chirp.domain.service.FriendService;
import org.unibuc.chirp.impl.mapper.FriendMapper;
import org.unibuc.chirp.impl.mapper.UserMapper;
import org.unibuc.chirp.impl.validator.FriendValidator;
import org.unibuc.chirp.impl.validator.UserValidator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;

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

        @SuppressWarnings("OptionalGetWithoutIsPresent")
        val requester = this.userRepository.findByUsername(currentUsername).get();
        @SuppressWarnings("OptionalGetWithoutIsPresent")
        val addressee = this.userRepository.findByUsername(targetUsername).get();

        val userFriendshipEntity = FriendMapper.toUserFriendshipEntity(requester, addressee,
                UserFriendshipEntity.FriendshipStatus.PENDING);

        this.userFriendshipRepository.save(userFriendshipEntity);
    }

    @Override
    public void acceptFriendRequest(String currentUsername, String targetUsername) {
        this.updateFriendshipRequest(currentUsername, targetUsername, UserFriendshipEntity.FriendshipStatus.ACCEPTED,
                friendship -> friendValidator.validateAccept(friendship));
    }

    @Override
    public void rejectFriendRequest(String currentUsername, String targetUsername) {
        this.updateFriendshipRequest(currentUsername, targetUsername, UserFriendshipEntity.FriendshipStatus.REJECTED,
                friendship -> friendValidator.validateReject(friendship));
    }

    private void updateFriendshipRequest(String currentUsername, String targetUsername,
                                         UserFriendshipEntity.FriendshipStatus status,
                                         Consumer<UserFriendshipEntity> validate) {
        UserFriendshipEntity friendshipRequest = getFriendship(currentUsername, targetUsername);

        validate.accept(friendshipRequest);

        friendshipRequest.setStatus(status);
        friendshipRequest.setRespondedAt(LocalDateTime.now());

        this.userFriendshipRepository.save(friendshipRequest);
    }

    @Override
    public void removeFriend(String currentUsername, String targetUsername) {
        UserFriendshipEntity friendship = this.getFriendship(currentUsername, targetUsername);

        this.friendValidator.validateRemove(friendship);

        this.userFriendshipRepository.delete(friendship);
    }

    private UserFriendshipEntity getFriendship(String currentUsername, String targetUsername) {
        this.userValidator.validate(currentUsername);
        this.userValidator.validate(targetUsername);

        val requester = this.userRepository.findByUsername(currentUsername).get();
        val addressee = this.userRepository.findByUsername(targetUsername).get();

        return this.userFriendshipRepository.findByUsers(requester, addressee)
                .orElseThrow();
    }

    @Override
    public Page<GetUserDetailsResponseDto> getFriends(String username, Pageable pageable) {
        this.userValidator.validate(username);

        val user = this.userRepository.findByUsername(username).orElseThrow();
        val friends = this.userFriendshipRepository.findByUserAndStatus(user,
                UserFriendshipEntity.FriendshipStatus.ACCEPTED, pageable);

        return friends.map(friendship -> {
            val friend = friendship.getRequester().equals(user) ? friendship.getAddressee() : friendship.getRequester();

            return UserMapper.toDetailsDto(friend);
        });
    }

    @Override
    public List<GetUserDetailsResponseDto> getFriendRequests(String username) {
        this.userValidator.validate(username);

        val user = this.userRepository.findByUsername(username).orElseThrow();
        val friendRequests = this.userFriendshipRepository.findByAddresseeAndStatus(user,
                UserFriendshipEntity.FriendshipStatus.PENDING);

        return friendRequests.stream()
                .map(friendship -> UserMapper.toDetailsDto(friendship.getRequester()))
                .toList();
    }
}