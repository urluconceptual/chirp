package org.unibuc.chirpchat.impl.service;

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
import org.unibuc.chirpchat.domain.dto.user.create.CreateUserRequestDto;
import org.unibuc.chirpchat.domain.dto.user.get.GetUserResponseDto;
import org.unibuc.chirpchat.domain.dto.user.login.LoginRequestDto;
import org.unibuc.chirpchat.domain.entity.RoleEntity;
import org.unibuc.chirpchat.domain.entity.UserEntity;
import org.unibuc.chirpchat.domain.entity.UserProfileEntity;
import org.unibuc.chirpchat.domain.entity.UserStatusEntity;
import org.unibuc.chirpchat.domain.repository.RoleRepository;
import org.unibuc.chirpchat.domain.repository.UserProfileRepository;
import org.unibuc.chirpchat.domain.repository.UserRepository;
import org.unibuc.chirpchat.domain.service.AuthService;
import org.unibuc.chirpchat.domain.service.UserStatusService;
import org.unibuc.chirpchat.impl.mapper.UserMapper;
import org.unibuc.chirpchat.impl.validator.UserValidator;

@Service
@AllArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private UserProfileRepository userProfileRepository;
    private RoleRepository roleRepository;
    private UserValidator userValidator;
    private AuthenticationManager authenticationManager;
    private UserStatusService userStatusService;

    @Transactional
    @Override
    public void registerUser(CreateUserRequestDto createUserRequestDto) {
        log.info("Registering user with username: {}", createUserRequestDto.username());
        userValidator.validate(createUserRequestDto);

        RoleEntity role = this.roleRepository.findByName("ROLE_USER").get();

        this.userRepository.save(UserMapper.toUserEntity(createUserRequestDto, role, passwordEncoder));

        UserEntity savedUser = this.userRepository.findByUsername(createUserRequestDto.username())
                .orElseThrow();

        this.userProfileRepository.save(UserProfileEntity.builder()
                .user(savedUser)
                .build());
        log.info("Registered user with username: {}", createUserRequestDto.username());
    }

    @Transactional
    @Override
    public GetUserResponseDto loginUser(LoginRequestDto loginRequestDto, HttpServletRequest request) {
        log.info("Logging in user with username: {}", loginRequestDto.username());
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(loginRequestDto.username(), loginRequestDto.password());

        Authentication authentication = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        HttpSession session = request.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                SecurityContextHolder.getContext());

        userStatusService.updateUserStatus(loginRequestDto.username(), UserStatusEntity.StatusType.ONLINE);
        return UserMapper.toDto(userRepository.findByUsername(loginRequestDto.username()).get());
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
