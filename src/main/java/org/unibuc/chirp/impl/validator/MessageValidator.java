package org.unibuc.chirp.impl.validator;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.unibuc.chirp.domain.dto.message.create.CreateMessageRequestDto;
import org.unibuc.chirp.domain.exception.AppException;

@Component
@AllArgsConstructor
public class MessageValidator {
    private final ConversationValidator conversationValidator;
    private final UserValidator userValidator;

    public void validate(CreateMessageRequestDto createMessageRequestDto) throws AppException {
        conversationValidator.validate(createMessageRequestDto.chatId());
        userValidator.validate(createMessageRequestDto.senderUsername());

        if (createMessageRequestDto.content() == null || createMessageRequestDto.content().isEmpty()) {
            throw new IllegalArgumentException("Message content cannot be null or empty");
        }
    }
}
