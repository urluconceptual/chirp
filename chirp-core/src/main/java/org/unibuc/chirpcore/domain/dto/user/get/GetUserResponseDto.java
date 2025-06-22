package org.unibuc.chirpcore.domain.dto.user.get;

public record GetUserResponseDto(
        String username,
        String role,
        String onlineStatus,
        String lastUpdatedStatus) {
}