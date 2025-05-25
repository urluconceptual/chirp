package org.unibuc.chirp.impl.mapper;

import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;
import org.unibuc.chirp.domain.dto.conversation.create.ConversationResponseDto;
import org.unibuc.chirp.domain.dto.conversation.get.ConversationDetailsResponseDto;
import org.unibuc.chirp.domain.entity.ConversationEntity;
import org.unibuc.chirp.domain.entity.MessageEntity;
import org.unibuc.chirp.domain.entity.UserEntity;
import org.unibuc.chirp.impl.service.utils.ServiceUtils;

import java.util.List;

@UtilityClass
public class ConversationMapper {

    public ConversationEntity toConversationEntity(String title, List<UserEntity> participants) {
        return ConversationEntity.builder()
                .title(title)
                .participants(participants)
                .build();
    }

    public static ConversationResponseDto toDto(ConversationEntity conversationEntity) {
        return new ConversationResponseDto(
                conversationEntity.getId(),
                conversationEntity.getTitle()
        );
    }

    public static ConversationDetailsResponseDto toDto(ConversationEntity conversationEntity,
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

}
