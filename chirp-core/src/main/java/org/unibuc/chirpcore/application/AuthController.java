package org.unibuc.chirpcore.application;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.unibuc.chirpcore.domain.dto.user.create.CreateUserRequestDto;
import org.unibuc.chirpcore.domain.dto.user.get.GetUserResponseDto;
import org.unibuc.chirpcore.domain.dto.user.login.LoginRequestDto;
import org.unibuc.chirpcore.domain.service.AuthService;

@Controller
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private AuthService authService;

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        String keycloakLogoutUrl = "http://localhost:8080/realms/chirp/protocol/openid-connect/logout" +
                "?post_logout_redirect_uri=http://localhost:8071/chirp/core/login";
        authService.logoutUser(request);
        return "redirect:" + keycloakLogoutUrl;
    }
}
