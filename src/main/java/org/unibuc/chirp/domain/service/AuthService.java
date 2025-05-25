package org.unibuc.chirp.domain.service;

import jakarta.servlet.http.HttpServletRequest;
import org.unibuc.chirp.domain.dto.user.create.CreateUserRequestDto;
import org.unibuc.chirp.domain.dto.user.login.LoginRequestDto;

public interface AuthService {
    void registerUser(CreateUserRequestDto createUserRequestDto);
    void loginUser(LoginRequestDto loginRequestDto, HttpServletRequest request);
    void logoutUser(HttpServletRequest request);
}
