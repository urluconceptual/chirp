package org.unibuc.chirp.impl.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import org.unibuc.chirp.domain.dto.user.create.CreateUserRequestDto;
import org.unibuc.chirp.domain.dto.user.login.LoginRequestDto;
import org.unibuc.chirp.domain.entity.RoleEntity;
import org.unibuc.chirp.domain.entity.UserEntity;
import org.unibuc.chirp.domain.entity.UserProfileEntity;
import org.unibuc.chirp.domain.repository.RoleRepository;
import org.unibuc.chirp.domain.repository.UserProfileRepository;
import org.unibuc.chirp.domain.repository.UserRepository;
import org.unibuc.chirp.domain.service.AuthService;
import org.unibuc.chirp.impl.service.utils.ServiceUtils;
import org.unibuc.chirp.impl.validator.UserValidator;

import java.util.Set;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private UserProfileRepository userProfileRepository;
    private RoleRepository roleRepository;
    private UserValidator userValidator;
    private AuthenticationManager authenticationManager;

    @Transactional
    @Override
    public void registerUser(CreateUserRequestDto createUserRequestDto) {
        userValidator.validate(createUserRequestDto);

        RoleEntity role = this.roleRepository.findByName("USER").get();

        this.userRepository.save(UserEntity.builder()
                .username(createUserRequestDto.username())
                .password(passwordEncoder.encode(createUserRequestDto.password()))
                .roles(Set.of(role))
                .build());

        UserEntity savedUser = this.userRepository.findByUsername(createUserRequestDto.username())
                .orElseThrow();

        this.userProfileRepository.save(UserProfileEntity.builder()
                .user(savedUser)
                .build());
    }

    @Transactional
    @Override
    public void loginUser(LoginRequestDto loginRequestDto, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(loginRequestDto.username(), loginRequestDto.password());

        Authentication authentication = authenticationManager.authenticate(authToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        HttpSession session = request.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                SecurityContextHolder.getContext());
    }
}
