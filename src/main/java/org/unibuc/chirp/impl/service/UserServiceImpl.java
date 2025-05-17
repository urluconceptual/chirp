package org.unibuc.chirp.impl.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.unibuc.chirp.domain.dto.user.create.CreateUserRequestDto;
import org.unibuc.chirp.domain.dto.user.create.CreateUserResponseDto;
import org.unibuc.chirp.domain.dto.user.get.GetUserDetailsResponseDto;
import org.unibuc.chirp.domain.entity.AppUser;
import org.unibuc.chirp.domain.entity.AppUserProfile;
import org.unibuc.chirp.domain.repository.AppUserProfileRepository;
import org.unibuc.chirp.domain.repository.AppUserRepository;
import org.unibuc.chirp.domain.service.UserService;
import org.unibuc.chirp.impl.service.utils.ServiceUtils;
import org.unibuc.chirp.impl.validator.UserValidator;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private AppUserRepository userRepository;
    private AppUserProfileRepository userProfileRepository;

    private UserValidator userValidator;

    @Transactional
    @Override
    public CreateUserResponseDto createUser(CreateUserRequestDto createUserRequestDto) {
        userValidator.validate(createUserRequestDto);

        this.userRepository.save(AppUser.builder()
                .username(createUserRequestDto.username())
                .password(createUserRequestDto.password())
                .build());

        val savedUser = this.userRepository.findByUsername(createUserRequestDto.username())
                .orElseThrow();

        this.userProfileRepository.save(AppUserProfile.builder()
                .appUser(savedUser)
                .avatarUrl(savedUser.getUsername())
                .build()
        );

        return ServiceUtils.toCreateUserResponseDto(savedUser);
    }

    @Override
    public GetUserDetailsResponseDto getUserDetails(String username) {
        userValidator.validate(username);

        val user = this.userRepository.findByUsername(username).get();

        return ServiceUtils.toGetUserDetailsResponseDto(user);
    }
}
