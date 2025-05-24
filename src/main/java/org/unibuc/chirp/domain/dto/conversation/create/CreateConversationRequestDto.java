package org.unibuc.chirp.domain.dto.conversation.create;

import java.util.List;

public record CreateConversationRequestDto(List<String> participantList, String title) {}
