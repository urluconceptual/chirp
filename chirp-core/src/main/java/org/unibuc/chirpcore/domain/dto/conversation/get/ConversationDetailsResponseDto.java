package org.unibuc.chirpcore.domain.dto.conversation.get;

import org.unibuc.chirpcore.domain.dto.message.get.GetMessageResponseDto;

import java.util.List;

public record ConversationDetailsResponseDto(
        Long id,
        String title,
        List<String> participantList,
        List<GetMessageResponseDto> messages,
        boolean hasMoreMessages
) {}
