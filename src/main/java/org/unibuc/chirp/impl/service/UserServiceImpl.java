package org.unibuc.chirp.impl.service;

import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.unibuc.chirp.domain.dto.user.get.GetUserDetailsResponseDto;
import org.unibuc.chirp.domain.dto.user.update.UpdateUserRequestDto;
import org.unibuc.chirp.domain.dto.user.update.UpdateUserResponseDto;
import org.unibuc.chirp.domain.repository.RoleRepository;
import org.unibuc.chirp.domain.repository.UserProfileRepository;
import org.unibuc.chirp.domain.repository.UserRepository;
import org.unibuc.chirp.domain.service.UserService;
import org.unibuc.chirp.impl.service.utils.ServiceUtils;
import org.unibuc.chirp.impl.validator.UserValidator;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private UserProfileRepository userProfileRepository;
    private UserValidator userValidator;

    @Override
    public GetUserDetailsResponseDto getUserDetails(String username) {
        userValidator.validate(username);

        val user = this.userRepository.findByUsername(username).get();

        return ServiceUtils.toGetUserDetailsResponseDto(user);
    }

    @Override
    public UpdateUserResponseDto updateUserDetails(String username, UpdateUserRequestDto updateUserRequestDto) {
        userValidator.validate(username);
        userValidator.validate(updateUserRequestDto);

        val userProfileEntity = this.userProfileRepository.findUserProfileEntityByUser_Username(username).get();

        userProfileEntity.setBio(updateUserRequestDto.updatedBio());

        this.userProfileRepository.save(userProfileEntity);

        return ServiceUtils.toDto(userProfileEntity);
    }
}
