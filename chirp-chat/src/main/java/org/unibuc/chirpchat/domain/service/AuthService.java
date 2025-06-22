package org.unibuc.chirpchat.domain.service;

import jakarta.servlet.http.HttpServletRequest;
import org.unibuc.chirpchat.domain.dto.user.create.CreateUserRequestDto;
import org.unibuc.chirpchat.domain.dto.user.get.GetUserResponseDto;
import org.unibuc.chirpchat.domain.dto.user.login.LoginRequestDto;

public interface AuthService {
    void registerUser(CreateUserRequestDto createUserRequestDto);
    GetUserResponseDto loginUser(LoginRequestDto loginRequestDto, HttpServletRequest request);
    void logoutUser(HttpServletRequest request);
}
