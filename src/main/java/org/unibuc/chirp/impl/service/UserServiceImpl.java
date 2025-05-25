package org.unibuc.chirp.impl.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
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
import org.unibuc.chirp.domain.repository.UserProfileRepository;
import org.unibuc.chirp.domain.repository.UserRepository;
import org.unibuc.chirp.domain.service.UserService;
import org.unibuc.chirp.impl.mapper.UserMapper;
import org.unibuc.chirp.impl.validator.UserValidator;

import java.io.IOException;
import java.util.Base64;

@Service
@AllArgsConstructor
@Getter
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private UserProfileRepository userProfileRepository;
    private UserValidator userValidator;

    @Override
    public GetUserDetailsResponseDto getUserDetails(String username) {
        userValidator.validate(username);

        return UserMapper.toDetailsDto(this.userRepository
            .findByUsername(username)
            .orElseThrow()
        );
    }

    @Override
    public Page<GetUserResponseDto> getUsersPage(String searchQuery, Pageable pageable) {
        searchQuery = searchQuery == null ? "" : searchQuery.trim();

        return (switch (searchQuery) {
            case "" -> this.userRepository.findAll(pageable);
            default -> this.userRepository.findAll(searchQuery, pageable);
        }).map(UserMapper::toDto);
    }

    @Override
    public UpdateUserResponseDto updateUserDetails(String username, UpdateUserRequestDto updateUserRequestDto,
                                                   MultipartFile avatarFile) {
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
        searchQuery = searchQuery == null ? "" : searchQuery.trim();

        return (switch (searchQuery) {
            case "" -> this.userRepository.findAllNonAdminUsers(pageable);
            default -> this.userRepository.findNonAdminUsers(searchQuery, pageable);
        }).map(UserMapper::toDetailsDto);
    }

    @Nullable
    private String uploadAvatarFile(MultipartFile avatarFile) {
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
