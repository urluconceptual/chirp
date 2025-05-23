package org.unibuc.chirp.domain.dto.message.get;

public record GetMessageResponseDto(
        Long id,
        String content,
        String senderUsername,
        String timestamp
) {}
