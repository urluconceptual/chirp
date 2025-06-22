package org.unibuc.chirpchat.domain.dto.user.get;

public record GetUserResponseDto(
        String username,
        String role,
        String onlineStatus,
        String lastUpdatedStatus) {
}