package org.unibuc.chirp.domain.dto.user.create;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record CreateUserRequestDto(
        @NotEmpty(message = "Missing username")
        String username,
        @NotEmpty(message = "Missing password")
        @Size(min = 6, message = "Password must be at least 6 characters long")
        String password) {}