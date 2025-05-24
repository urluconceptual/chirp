package org.unibuc.chirp.domain.dto.user.update;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateUserRequestDto(
        @Size(max = 500, message = "Max length is 500 characters") String bio,
        String birthday,
        String location,
        @Pattern(
                regexp = "^$|^(https?://)([\\w.-]+)(:[0-9]+)?(/.*)?$",
                message = "Invalid URL"
        ) String website) {
}
