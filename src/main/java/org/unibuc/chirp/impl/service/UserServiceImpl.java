package org.unibuc.chirp.impl.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.unibuc.chirp.domain.dto.user.get.GetUserDetailsResponseDto;
import org.unibuc.chirp.domain.dto.user.get.GetUserResponseDto;
import org.unibuc.chirp.domain.dto.user.update.UpdateUserRequestDto;
import org.unibuc.chirp.domain.dto.user.update.UpdateUserResponseDto;
import org.unibuc.chirp.domain.entity.UserProfileEntity;
import org.unibuc.chirp.domain.repository.*;
import org.unibuc.chirp.domain.service.UserService;
import org.unibuc.chirp.impl.mapper.UserMapper;
import org.unibuc.chirp.impl.validator.UserValidator;

import java.io.IOException;
import java.util.Base64;

@Slf4j
@Service
@AllArgsConstructor
@Getter
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private MessageRepository messageRepository;
    private UserProfileRepository userProfileRepository;
    private UserStatusRepository userStatusRepository;
    private ConversationRepository conversationRepository;
    private UserFriendshipRepository userFriendshipRepository;
    private RoleRepository roleRepository;
    private UserValidator userValidator;

    @Override
    public GetUserDetailsResponseDto getUserDetails(String username) {
        log.info("Getting user details for username: {}", username);
        userValidator.validate(username);

        return UserMapper.toDetailsDto(this.userRepository
            .findByUsername(username)
            .orElseThrow()
        );
    }

    @Override
    public Page<GetUserResponseDto> getUsersPage(String searchQuery, Pageable pageable) {
        log.info("Getting users page with search query: {}", searchQuery);
        searchQuery = searchQuery == null ? "" : searchQuery.trim();

        return (switch (searchQuery) {
            case "" -> this.userRepository.findAll(pageable);
            default -> this.userRepository.findAll(searchQuery, pageable);
        }).map(UserMapper::toDto);
    }

    @Override
    public UpdateUserResponseDto updateUserDetails(String username, UpdateUserRequestDto updateUserRequestDto,
                                                   MultipartFile avatarFile) {
        log.info("Updating user details for username: {}", username);
        userValidator.validate(username);

        UserProfileEntity userProfileEntity =
                this.userProfileRepository.findUserProfileEntityByUser_Username(username).orElseThrow();
        userProfileEntity = UserMapper.toUserProfileEntity(updateUserRequestDto, userProfileEntity,
                uploadAvatarFile(avatarFile));
        this.userProfileRepository.save(userProfileEntity);

        return UserMapper.toDetailsDto(userProfileEntity);
    }

    @Override
    public Page<GetUserDetailsResponseDto> exploreUsers(String searchQuery, Pageable pageable) {
        log.info("Exploring users with search query: {}", searchQuery);
        searchQuery = searchQuery == null ? "" : searchQuery.trim();

        return (switch (searchQuery) {
            case "" -> this.userRepository.findAllNonAdminUsers(pageable);
            default -> this.userRepository.findNonAdminUsers(searchQuery, pageable);
        }).map(UserMapper::toDetailsDto);
    }

    @Override
    @Transactional
    public void deleteUserByUsername(String username) {
        log.info("Deleting user with username: {}", username);
        userValidator.validate(username);
        this.userProfileRepository.deleteByUser_Username(username);
        this.userStatusRepository.deleteByUser_Username(username);
        this.messageRepository.deleteAllBySender_Username(username);
        this.conversationRepository.deleteFromConversationUserByUsername(username);
        this.conversationRepository.deleteAllByParticipantUsername(username);
        this.userFriendshipRepository.deleteByRequesterOrAddresseeUsername(username);
        this.roleRepository.deleteJoinColumnForUsername(username);
        this.userRepository.deleteByUsername(username);
    }

    @Nullable
    private String uploadAvatarFile(MultipartFile avatarFile) {
        log.info("Uploading avatar file");
        if (avatarFile == null || avatarFile.isEmpty())
            return null;
        try {
            if (!avatarFile.isEmpty()) {
                byte[] bytes = avatarFile.getBytes();
                return Base64.getEncoder().encodeToString(bytes);
            }
        } catch (IOException e) {
            return null;
        }
        return null;
    }
}
