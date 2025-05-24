package org.unibuc.chirp.domain.dto.user.login;

import jakarta.validation.constraints.NotEmpty;

public record LoginRequestDto(
        @NotEmpty(message = "Missing username")
        String username,
        @NotEmpty(message = "Missing password")
        String password) {}