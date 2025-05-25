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
import org.unibuc.chirp.domain.repository.UserProfileRepository;
import org.unibuc.chirp.domain.repository.UserRepository;
import org.unibuc.chirp.domain.service.UserService;
import org.unibuc.chirp.impl.service.utils.ServiceUtils;
import org.unibuc.chirp.impl.validator.UserValidator;

import java.io.IOException;
import java.time.LocalDate;
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

        val user = this.userRepository.findByUsername(username).get();

        return ServiceUtils.toDto(user);
    }

    @Override
    public UpdateUserResponseDto updateUserDetails(String username, UpdateUserRequestDto updateUserRequestDto,
                                                   MultipartFile avatarFile) {
        userValidator.validate(username);

        val userProfileEntity = this.userProfileRepository.findUserProfileEntityByUser_Username(username).get();

        userProfileEntity.setBio(updateUserRequestDto.bio());
        userProfileEntity.setBirthday(StringUtils.isEmpty(updateUserRequestDto.birthday()) ? null :
                LocalDate.parse(updateUserRequestDto.birthday()));
        userProfileEntity.setLocation(updateUserRequestDto.location());
        userProfileEntity.setWebsite(updateUserRequestDto.website());
        String base64Avatar = uploadAvatarFile(avatarFile);
        if (base64Avatar != null) {
            userProfileEntity.setProfilePicture(base64Avatar);
        }

        this.userProfileRepository.save(userProfileEntity);

        return ServiceUtils.toDto(userProfileEntity);
    }

    @Override
    public Page<GetUserDetailsResponseDto> exploreUsers(String searchQuery, Pageable pageable) {
        if (StringUtils.isEmpty(searchQuery)) {
            return this.userRepository.findAllNonAdminUsers(pageable)
                    .map(ServiceUtils::toDto);
        } else {
            return this.userRepository.findNonAdminUsers(searchQuery, pageable)
                    .map(ServiceUtils::toDto);
        }
    }

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
