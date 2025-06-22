package org.unibuc.chirpchat.domain.dto.conversation.create;

import java.util.List;

public record CreateConversationRequestDto(List<String> participantList, String title) {}
