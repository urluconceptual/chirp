package org.unibuc.chirp.impl.service.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.unibuc.chirp.domain.dto.conversation.create.CreateConversationResponseDto;
import org.unibuc.chirp.domain.dto.conversation.get.GetConversationResponseDto;
import org.unibuc.chirp.domain.dto.message.get.GetMessageResponseDto;
import org.unibuc.chirp.domain.dto.user.create.CreateUserResponseDto;
import org.unibuc.chirp.domain.dto.user.get.GetUserDetailsResponseDto;
import org.unibuc.chirp.domain.dto.user.update.UpdateUserResponseDto;
import org.unibuc.chirp.domain.entity.UserEntity;
import org.unibuc.chirp.domain.entity.UserProfileEntity;
import org.unibuc.chirp.domain.entity.ConversationEntity;
import org.unibuc.chirp.domain.entity.MessageEntity;

@Slf4j
@UtilityClass
public class ServiceUtils {
    public GetUserDetailsResponseDto toGetUserDetailsResponseDto(UserEntity userEntity) {
        return new GetUserDetailsResponseDto(
                userEntity.getUsername(),
                userEntity.getUserProfile().getAvatarUrl(),
                userEntity.getUserProfile().getBio()
        );
    }

    public UpdateUserResponseDto toDto(UserProfileEntity userProfile) {
        return new UpdateUserResponseDto(
                userProfile.getUser().getUsername(),
                userProfile.getAvatarUrl(),
                userProfile.getBio()
        );
    }

    public static CreateConversationResponseDto toDto(ConversationEntity conversationEntity) {
        return new CreateConversationResponseDto(
                conversationEntity.getId(),
                conversationEntity.getTitle()
        );
    }

    public static GetConversationResponseDto toDtoGetConversation(ConversationEntity conversationEntity, Page<MessageEntity> messagePage) {
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
