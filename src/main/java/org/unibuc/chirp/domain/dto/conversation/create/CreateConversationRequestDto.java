package org.unibuc.chirp.domain.dto.conversation.create;

import java.util.List;

public record CreateConversationRequestDto(List<Long> participantIdList, String title) {}
