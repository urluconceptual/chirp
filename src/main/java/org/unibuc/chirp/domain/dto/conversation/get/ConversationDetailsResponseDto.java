package org.unibuc.chirp.domain.dto.conversation.get;

import org.unibuc.chirp.domain.dto.message.get.GetMessageResponseDto;

import java.util.List;

public record ConversationDetailsResponseDto(
        Long id,
        String title,
        List<String> participantList,
        List<GetMessageResponseDto> messages
) {}
