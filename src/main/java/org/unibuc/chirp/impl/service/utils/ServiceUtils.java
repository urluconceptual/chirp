package org.unibuc.chirp.impl.service.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.unibuc.chirp.domain.dto.conversation.create.ConversationResponseDto;
import org.unibuc.chirp.domain.dto.conversation.get.ConversationDetailsResponseDto;
import org.unibuc.chirp.domain.dto.message.get.GetMessageResponseDto;
import org.unibuc.chirp.domain.dto.user.get.FriendStatus;
import org.unibuc.chirp.domain.dto.user.get.GetUserDetailsResponseDto;
import org.unibuc.chirp.domain.dto.user.get.GetUserResponseDto;
import org.unibuc.chirp.domain.dto.user.update.UpdateUserResponseDto;
import org.unibuc.chirp.domain.entity.*;

import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

@Slf4j
@UtilityClass
public class ServiceUtils {
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

    public static ConversationResponseDto toDetailsDto(ConversationEntity conversationEntity) {
        return new ConversationResponseDto(
                conversationEntity.getId(),
                conversationEntity.getTitle()
        );
    }

    public static ConversationDetailsResponseDto toDtoGetConversation(ConversationEntity conversationEntity,
                                                                      Page<MessageEntity> messagePage) {
        return new ConversationDetailsResponseDto(
                conversationEntity.getId(),
                conversationEntity.getTitle(),
                conversationEntity.getParticipants().stream()
                        .map(UserEntity::getUsername)
                        .toList(),
                messagePage.getContent().stream()
                        .map(ServiceUtils::toDetailsDto)
                        .toList()
                        .reversed(),
                messagePage.hasNext()
        );
    }

    public static GetMessageResponseDto toDetailsDto(MessageEntity messageEntity) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return new GetMessageResponseDto(
                messageEntity.getId(),
                messageEntity.getContent(),
                messageEntity.getSender().getUsername(),
                formatter.format(messageEntity.getTimestamp())
        );
    }
}
