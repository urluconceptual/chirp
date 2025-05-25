package org.unibuc.chirp.impl.mapper;

import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;
import org.unibuc.chirp.domain.dto.conversation.create.ConversationResponseDto;
import org.unibuc.chirp.domain.dto.conversation.get.ConversationDetailsResponseDto;
import org.unibuc.chirp.domain.dto.message.get.GetMessageResponseDto;
import org.unibuc.chirp.domain.entity.ConversationEntity;
import org.unibuc.chirp.domain.entity.MessageEntity;
import org.unibuc.chirp.domain.entity.UserEntity;

import java.time.format.DateTimeFormatter;
import java.util.List;

@UtilityClass
public class ConversationMapper {

    public ConversationEntity toConversationEntity(String title, List<UserEntity> participants) {
        return ConversationEntity.builder()
                .title(title)
                .participants(participants)
                .build();
    }

    public ConversationResponseDto toDto(ConversationEntity conversationEntity) {
        return new ConversationResponseDto(
                conversationEntity.getId(),
                conversationEntity.getTitle()
        );
    }

    public ConversationDetailsResponseDto toDto(ConversationEntity conversationEntity,
                                                       Page<MessageEntity> messagePage) {
        return new ConversationDetailsResponseDto(
                conversationEntity.getId(),
                conversationEntity.getTitle(),
                conversationEntity.getParticipants().stream()
                        .map(UserEntity::getUsername)
                        .toList(),
                messagePage.getContent().stream()
                        .map(ConversationMapper::toDetailsDto)
                        .toList()
                        .reversed(),
                messagePage.hasNext()
        );
    }

    public GetMessageResponseDto toDetailsDto(MessageEntity messageEntity) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return new GetMessageResponseDto(
                messageEntity.getId(),
                messageEntity.getContent(),
                messageEntity.getSender().getUsername(),
                formatter.format(messageEntity.getTimestamp())
        );
    }
}
