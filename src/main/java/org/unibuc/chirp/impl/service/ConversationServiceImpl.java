package org.unibuc.chirp.impl.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.unibuc.chirp.domain.dto.conversation.create.CreateConversationRequestDto;
import org.unibuc.chirp.domain.dto.conversation.create.CreateConversationResponseDto;
import org.unibuc.chirp.domain.dto.conversation.get.GetConversationResponseDto;
import org.unibuc.chirp.domain.entity.Conversation;
import org.unibuc.chirp.domain.repository.AppUserRepository;
import org.unibuc.chirp.domain.repository.ConversationRepository;
import org.unibuc.chirp.domain.service.ConversationService;
import org.unibuc.chirp.impl.service.utils.ServiceUtils;
import org.unibuc.chirp.impl.validator.ConversationValidator;
import org.unibuc.chirp.impl.validator.UserValidator;

@Service
@AllArgsConstructor
public class ConversationServiceImpl implements ConversationService {
    private AppUserRepository appUserRepository;
    private ConversationRepository conversationRepository;

    private ConversationValidator conversationValidator;
    private UserValidator userValidator;

    @Override
    public CreateConversationResponseDto createConversation(CreateConversationRequestDto createConversationRequestDto) {
        conversationValidator.validate(createConversationRequestDto);
        userValidator.validate(createConversationRequestDto.participantIdList());

        val participantUserList = this.appUserRepository.findAllById(createConversationRequestDto.participantIdList());

        val conversation = Conversation.builder()
                .title(createConversationRequestDto.title())
                .participants(participantUserList)
                .build();

        return ServiceUtils.toDto(this.conversationRepository.save(conversation));
    }

    @Override
    @Transactional
    public GetConversationResponseDto getConversation(Long conversationId) {
        conversationValidator.validate(conversationId);

        @SuppressWarnings("OptionalGetWithoutIsPresent")
        val conversation = this.conversationRepository.findById(conversationId).get();

        return ServiceUtils.toDtoGetConversation(conversation);
    }
}
