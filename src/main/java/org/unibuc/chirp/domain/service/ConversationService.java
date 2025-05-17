package org.unibuc.chirp.domain.service;

import org.unibuc.chirp.domain.dto.conversation.create.CreateConversationRequestDto;
import org.unibuc.chirp.domain.dto.conversation.create.CreateConversationResponseDto;

public interface ConversationService {
    CreateConversationResponseDto createConversation(CreateConversationRequestDto createConversationRequestDto);
}
