package org.unibuc.chirp.domain.dto.conversation.get;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record GetConversationRequestDto(
        @NotNull(message = "Page cannot be null")
        @Min(value = 0, message = "Page must be at least 0")
        Integer page,

        @NotNull(message = "Size cannot be null")
        @Min(value = 1, message = "Size must be at least 1")
        Integer size
) {}
