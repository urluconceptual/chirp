package org.unibuc.chirp.domain.dto.conversation.get;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record GetConversationRequestDto(
        @NotNull(message = "Offset cannot be null")
        @Min(value = 0, message = "Offset must be at least 0")
        @Max(value = 1000, message = "Offset cannot exceed 1000")
        Integer offset,

        @NotNull(message = "Limit cannot be null")
        @Min(value = 1, message = "Limit must be at least 1")
        Integer limit
) {}
