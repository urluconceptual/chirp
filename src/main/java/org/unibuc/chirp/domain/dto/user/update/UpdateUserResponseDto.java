package org.unibuc.chirp.domain.dto.user.update;

public record UpdateUserResponseDto(
        String username,
        String birthday,
        String location,
        String website,
        String profilePicture,
        String bio
) {}
