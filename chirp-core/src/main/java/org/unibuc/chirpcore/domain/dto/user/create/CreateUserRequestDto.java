package org.unibuc.chirpcore.domain.dto.user.create;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record CreateUserRequestDto(
        @NotEmpty(message = "Missing username")
        String username
) {}