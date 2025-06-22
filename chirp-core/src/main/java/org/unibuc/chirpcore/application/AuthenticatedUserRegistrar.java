package org.unibuc.chirpcore.application;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.unibuc.chirpcore.domain.dto.user.create.CreateUserRequestDto;
import org.unibuc.chirpcore.domain.service.AuthService;

@ControllerAdvice
@RequiredArgsConstructor
public class AuthenticatedUserRegistrar {

    private final AuthService authService;

    @ModelAttribute
    public void registerUserIfNeeded(Authentication authentication) {
            authService.registerUser(new CreateUserRequestDto(((JwtAuthenticationToken)authentication).getTokenAttributes().get("preferred_username").toString()));
    }
}