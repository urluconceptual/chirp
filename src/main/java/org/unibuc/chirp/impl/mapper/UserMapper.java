package org.unibuc.chirp.impl.mapper;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.unibuc.chirp.domain.dto.user.create.CreateUserRequestDto;
import org.unibuc.chirp.domain.dto.user.get.FriendStatus;
import org.unibuc.chirp.domain.dto.user.get.GetUserDetailsResponseDto;
import org.unibuc.chirp.domain.dto.user.get.GetUserResponseDto;
import org.unibuc.chirp.domain.dto.user.update.UpdateUserRequestDto;
import org.unibuc.chirp.domain.dto.user.update.UpdateUserResponseDto;
import org.unibuc.chirp.domain.entity.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.stream.Stream;

@UtilityClass
public class UserMapper {

    public UserEntity toUserEntity(final CreateUserRequestDto createUserRequestDto,
                                   final RoleEntity role, final PasswordEncoder passwordEncoder) {
        return UserEntity.builder()
                .username(createUserRequestDto.username())
                .password(passwordEncoder.encode(createUserRequestDto.password()))
                .roles(Set.of(role))
                .build();
    }

    public UserProfileEntity toUserProfileEntity(@NonNull UpdateUserRequestDto updateUserRequestDto,
                                                 UserProfileEntity userProfileEntity, String base64Avatar) {
        userProfileEntity.setBio(updateUserRequestDto.bio());
        userProfileEntity.setBirthday(StringUtils.isEmpty(updateUserRequestDto.birthday()) ? null :
                LocalDate.parse(updateUserRequestDto.birthday()));
        userProfileEntity.setLocation(updateUserRequestDto.location());
        userProfileEntity.setWebsite(updateUserRequestDto.website());
        userProfileEntity.setProfilePicture(StringUtils.hasText(base64Avatar) ? base64Avatar : userProfileEntity.getProfilePicture());

        return userProfileEntity;
    }

    public UpdateUserResponseDto toDetailsDto(UserProfileEntity userProfile) {
        return new UpdateUserResponseDto(
                userProfile.getUser().getUsername(),
                String.valueOf(userProfile.getBirthday()),
                userProfile.getLocation(),
                userProfile.getWebsite(),
                userProfile.getProfilePicture(),
                userProfile.getBio()
        );
    }

    public UserStatusEntity toUserStatusEntity(final UserEntity userEntity,
                                               final UserStatusEntity.StatusType statusType) {
        return UserStatusEntity.builder()
                .user(userEntity)
                .status(statusType)
                .lastUpdated(LocalDateTime.now())
                .build();
    }

    public GetUserDetailsResponseDto toDetailsDto(UserEntity userEntity) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserUsername = authentication.getName();

        Integer numberOfFriends = Stream.concat(userEntity.getSentFriendRequests().stream(),
                        userEntity.getReceivedFriendRequests().stream())
                .filter(request -> request.getStatus().equals(
                        UserFriendshipEntity.FriendshipStatus.ACCEPTED)).toList().size();

        FriendStatus friendStatus;

        if (userEntity.getUsername().equals(currentUserUsername)) {
            friendStatus = FriendStatus.OWN_ACCOUNT;
        } else {
            friendStatus = Stream.concat(userEntity.getSentFriendRequests().stream(),
                            userEntity.getReceivedFriendRequests().stream())
                    .filter(request -> request.getRequester().getUsername().equals(currentUserUsername) ||
                            request.getAddressee().getUsername().equals(currentUserUsername))
                    .map(userFriendshipEntity ->
                            switch (userFriendshipEntity.getStatus()) {
                                case PENDING -> FriendStatus.PENDING;
                                case ACCEPTED -> FriendStatus.FRIEND;
                                case REJECTED -> FriendStatus.REJECTED;
                            })
                    .findFirst()
                    .orElse(FriendStatus.NOT_FRIEND);
        }

        String onlineStatus = userEntity.getStatus() != null?
                userEntity.getStatus().getStatus().toString() : "OFFLINE";
        String lastUpdated = userEntity.getStatus() != null ?
                userEntity.getStatus().getLastUpdated().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "N/A";

        return new GetUserDetailsResponseDto(
                userEntity.getUsername(),
                userEntity.getUserProfile().getProfilePicture(),
                userEntity.getUserProfile().getBio(),
                userEntity.getUserProfile().getBirthday() != null ?
                        userEntity.getUserProfile().getBirthday().format(DateTimeFormatter.ISO_LOCAL_DATE) : null,
                userEntity.getUserProfile().getLocation(),
                userEntity.getUserProfile().getWebsite(),
                numberOfFriends,
                friendStatus.toString(),
                onlineStatus,
                lastUpdated);
    }

    public GetUserResponseDto toDto(UserEntity userEntity) {
        String onlineStatus = userEntity.getStatus() != null?
                userEntity.getStatus().getStatus().toString() : "OFFLINE";
        String lastUpdated = userEntity.getStatus() != null ?
                userEntity.getStatus().getLastUpdated().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "N/A";

        return new GetUserResponseDto(
                userEntity.getUsername(),
                userEntity.getRoles().stream()
                        .map(RoleEntity::getName)
                        .findFirst()
                        .orElse("ROLE_USER"),
                onlineStatus,
                lastUpdated);
    }


}
