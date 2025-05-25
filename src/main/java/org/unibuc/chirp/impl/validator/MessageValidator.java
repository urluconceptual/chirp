package org.unibuc.chirp.impl.validator;

import lombok.AllArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;
import org.unibuc.chirp.domain.dto.message.create.CreateMessageRequestDto;
import org.unibuc.chirp.domain.exception.AppException;
import org.unibuc.chirp.domain.exception.ErrorCode;

@Component
@AllArgsConstructor
public class MessageValidator {
    private final ConversationValidator conversationValidator;
    private final UserValidator userValidator;

    public void validate(CreateMessageRequestDto createMessageRequestDto) throws AppException {
        conversationValidator.validate(createMessageRequestDto.chatId());
        userValidator.validate(createMessageRequestDto.senderUsername());

        if (Strings.isEmpty(createMessageRequestDto.content())) {
            throw new AppException(ErrorCode.CHR0009);
        }
    }
}
