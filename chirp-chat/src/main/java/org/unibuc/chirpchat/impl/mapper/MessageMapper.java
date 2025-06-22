package org.unibuc.chirpchat.impl.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.unibuc.chirpchat.domain.dto.message.create.CreateMessageRequestDto;
import org.unibuc.chirpchat.domain.entity.ConversationEntity;
import org.unibuc.chirpchat.domain.entity.MessageEntity;
import org.unibuc.chirpchat.domain.entity.UserEntity;
import org.unibuc.chirpchat.domain.repository.ConversationRepository;
import org.unibuc.chirpchat.domain.repository.UserRepository;

@Component
@AllArgsConstructor
public class MessageMapper {
    private final UserRepository userRepository;
    private final ConversationRepository conversationRepository;

    public MessageEntity toEntity(CreateMessageRequestDto createMessageRequestDto) {
        UserEntity sender = userRepository.findByUsername(createMessageRequestDto.senderUsername()).get();
        ConversationEntity conversation = conversationRepository.findById(createMessageRequestDto.chatId()).get();

        return MessageEntity.builder()
                .content(createMessageRequestDto.content())
                .sender(sender)
                .conversation(conversation)
                .timestamp(java.time.LocalDateTime.now())
                .build();
    }
}
