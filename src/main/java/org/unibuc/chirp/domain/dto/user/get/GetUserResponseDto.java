package org.unibuc.chirp.domain.dto.user.get;

public record GetUserResponseDto(
        String username,
        String role,
        String onlineStatus,
        String lastUpdatedStatus) {
}