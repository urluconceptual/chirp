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
