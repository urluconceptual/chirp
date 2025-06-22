package org.unibuc.chirpcore.domain.service;

import jakarta.servlet.http.HttpServletRequest;
import org.unibuc.chirpcore.domain.dto.user.create.CreateUserRequestDto;
import org.unibuc.chirpcore.domain.dto.user.get.GetUserResponseDto;
import org.unibuc.chirpcore.domain.dto.user.login.LoginRequestDto;

public interface AuthService {
    void registerUser(CreateUserRequestDto createUserRequestDto);
    void logoutUser(HttpServletRequest request);
}
