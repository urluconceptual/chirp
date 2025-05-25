package org.unibuc.chirp.domain.dto.user.get;

public record GetUserDetailsResponseDto(
        String username,
        String profilePicture,
        String bio,
        String birthday,
        String location,
        String website,
        Integer numberOfFriends,
        String friendStatus,
        String onlineStatus,
        String lastUpdatedStatus) {
}