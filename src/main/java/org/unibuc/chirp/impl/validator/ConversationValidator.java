package org.unibuc.chirp.impl.validator;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.unibuc.chirp.domain.dto.conversation.create.CreateConversationRequestDto;
import org.unibuc.chirp.domain.exception.AppException;
import org.unibuc.chirp.domain.exception.ErrorCode;
import org.unibuc.chirp.domain.repository.ConversationRepository;

@Component
@AllArgsConstructor
public class ConversationValidator {
    public static final int MAX_TITLE_LENGTH = 100;

    private ConversationRepository conversationRepository;

    public void validate(CreateConversationRequestDto createConversationRequestDto) {
        if (createConversationRequestDto.title() == null || createConversationRequestDto.title().isBlank()) {
            throw new AppException(ErrorCode.CHR0005);
        } else if (createConversationRequestDto.title().length() > MAX_TITLE_LENGTH) {
            throw new AppException(ErrorCode.CHR0006);
        }
    }

    public void validate(@NonNull Long conversationId) {
        if (!conversationRepository.existsById(conversationId)) {
            throw new AppException(ErrorCode.CHR0007);
        }
    }
}
