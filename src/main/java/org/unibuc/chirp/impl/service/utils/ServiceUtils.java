package org.unibuc.chirp.impl.service.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.unibuc.chirp.domain.dto.conversation.create.CreateConversationResponseDto;
import org.unibuc.chirp.domain.dto.conversation.get.GetConversationResponseDto;
import org.unibuc.chirp.domain.dto.message.get.GetMessageResponseDto;
import org.unibuc.chirp.domain.dto.user.get.FriendStatus;
import org.unibuc.chirp.domain.dto.user.get.GetUserDetailsResponseDto;
import org.unibuc.chirp.domain.dto.user.update.UpdateUserResponseDto;
import org.unibuc.chirp.domain.entity.*;

import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

@Slf4j
@UtilityClass
public class ServiceUtils {
    public GetUserDetailsResponseDto toGetUserDetailsResponseDto(UserEntity userEntity) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserUsername = authentication.getName();

        Integer numberOfFriends = Stream.concat(userEntity.getSentFriendRequests().stream(),
                        userEntity.getReceivedFriendRequests().stream())
                .filter(request -> request.getStatus().equals(
                        UserFriendshipEntity.FriendshipStatus.ACCEPTED)).toList().size();

        FriendStatus friendStatus = Stream.concat(userEntity.getSentFriendRequests().stream(),
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
                .orElse(userEntity.getUsername().equals(currentUserUsername)? FriendStatus.OWN_ACCOUNT : FriendStatus.NOT_FRIEND);

        return new GetUserDetailsResponseDto(
                userEntity.getUsername(),
                userEntity.getUserProfile().getProfilePicture(),
                userEntity.getUserProfile().getBio(),
                userEntity.getUserProfile().getBirthday() != null ?
                        userEntity.getUserProfile().getBirthday().format(DateTimeFormatter.ISO_LOCAL_DATE) : null,
                userEntity.getUserProfile().getLocation(),
                userEntity.getUserProfile().getWebsite(),
                numberOfFriends,
                friendStatus.toString());
    }

    public UpdateUserResponseDto toDto(UserProfileEntity userProfile) {
        return new UpdateUserResponseDto(
                userProfile.getUser().getUsername(),
                userProfile.getProfilePicture(),
                userProfile.getBio()
        );
    }

    public static CreateConversationResponseDto toDto(ConversationEntity conversationEntity) {
        return new CreateConversationResponseDto(
                conversationEntity.getId(),
                conversationEntity.getTitle()
        );
    }

    public static GetConversationResponseDto toDtoGetConversation(ConversationEntity conversationEntity,
                                                                  Page<MessageEntity> messagePage) {
        return new GetConversationResponseDto(
                conversationEntity.getId(),
                conversationEntity.getTitle(),
                conversationEntity.getParticipants().stream()
                        .map(UserEntity::getUsername)
                        .toList(),
                messagePage.getContent().stream()
                        .map(ServiceUtils::toDto)
                        .toList()
        );
    }

    public static GetMessageResponseDto toDto(MessageEntity messageEntity) {
        return new GetMessageResponseDto(
                messageEntity.getId(),
                messageEntity.getContent(),
                messageEntity.getSender().getUsername(),
                messageEntity.getTimestamp().toString()
        );
    }
}
