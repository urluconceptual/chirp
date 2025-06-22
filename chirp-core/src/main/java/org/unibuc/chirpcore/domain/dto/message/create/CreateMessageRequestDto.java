package org.unibuc.chirpcore.domain.dto.message.create;

public record CreateMessageRequestDto(Long chatId, String senderUsername, String content) {}