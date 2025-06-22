package org.unibuc.chirpchat.domain.service;

import org.unibuc.chirpchat.domain.dto.conversation.create.CreateConversationRequestDto;
import org.unibuc.chirpchat.domain.dto.conversation.create.ConversationResponseDto;
import org.unibuc.chirpchat.domain.dto.conversation.get.GetConversationRequestDto;
import org.unibuc.chirpchat.domain.dto.conversation.get.ConversationDetailsResponseDto;

import java.util.List;

public interface ConversationService {
    ConversationResponseDto createConversation(CreateConversationRequestDto createConversationRequestDto);

    ConversationDetailsResponseDto getConversation(Long conversationId, GetConversationRequestDto getConversationRequestDto);

    List<ConversationResponseDto> getAllConversations(String username);
}
