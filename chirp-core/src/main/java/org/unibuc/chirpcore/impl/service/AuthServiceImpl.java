package org.unibuc.chirpcore.impl.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import org.unibuc.chirpcore.domain.dto.user.create.CreateUserRequestDto;
import org.unibuc.chirpcore.domain.dto.user.get.GetUserResponseDto;
import org.unibuc.chirpcore.domain.dto.user.login.LoginRequestDto;
import org.unibuc.chirpcore.domain.entity.RoleEntity;
import org.unibuc.chirpcore.domain.entity.UserEntity;
import org.unibuc.chirpcore.domain.entity.UserProfileEntity;
import org.unibuc.chirpcore.domain.entity.UserStatusEntity;
import org.unibuc.chirpcore.domain.repository.RoleRepository;
import org.unibuc.chirpcore.domain.repository.UserProfileRepository;
import org.unibuc.chirpcore.domain.repository.UserRepository;
import org.unibuc.chirpcore.domain.service.AuthService;
import org.unibuc.chirpcore.domain.service.UserStatusService;
import org.unibuc.chirpcore.impl.mapper.UserMapper;
import org.unibuc.chirpcore.impl.validator.UserValidator;

@Service
@AllArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    private UserRepository userRepository;
    private UserProfileRepository userProfileRepository;
    private RoleRepository roleRepository;
    private UserValidator userValidator;
    private UserStatusService userStatusService;

    @Transactional
    @Override
    public void registerUser(CreateUserRequestDto createUserRequestDto) {
        if(userRepository.existsByUsername(createUserRequestDto.username())) {
            return; // User already exists, no need to proceed
        }

        log.info("Registering user with username: {}", createUserRequestDto.username());
        userValidator.validate(createUserRequestDto);

        RoleEntity role = this.roleRepository.findByName("ROLE_USER").get();

        this.userRepository.save(UserMapper.toUserEntity(createUserRequestDto, role));

        UserEntity savedUser = this.userRepository.findByUsername(createUserRequestDto.username())
                .orElseThrow();

        this.userProfileRepository.save(UserProfileEntity.builder()
                .user(savedUser)
                .build());
        log.info("Registered user with username: {}", createUserRequestDto.username());
    }

    @Override
    public void logoutUser(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        log.info("Logging out user with username: {}", username);

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        SecurityContextHolder.clearContext();

        userStatusService.updateUserStatus(username, UserStatusEntity.StatusType.OFFLINE);
    }
}
