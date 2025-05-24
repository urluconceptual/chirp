package org.unibuc.chirp.impl.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.unibuc.chirp.domain.dto.conversation.create.CreateConversationRequestDto;
import org.unibuc.chirp.domain.dto.conversation.create.CreateConversationResponseDto;
import org.unibuc.chirp.domain.dto.conversation.get.GetConversationRequestDto;
import org.unibuc.chirp.domain.dto.conversation.get.GetConversationResponseDto;
import org.unibuc.chirp.domain.entity.ConversationEntity;
import org.unibuc.chirp.domain.entity.MessageEntity;
import org.unibuc.chirp.domain.repository.UserRepository;
import org.unibuc.chirp.domain.repository.ConversationRepository;
import org.unibuc.chirp.domain.repository.MessageRepository;
import org.unibuc.chirp.domain.service.ConversationService;
import org.unibuc.chirp.impl.service.utils.ServiceUtils;
import org.unibuc.chirp.impl.validator.ConversationValidator;
import org.unibuc.chirp.impl.validator.UserValidator;

@Service
@AllArgsConstructor
public class ConversationServiceImpl implements ConversationService {
    private UserRepository userRepository;
    private ConversationRepository conversationRepository;
    private MessageRepository messageRepository;

    private ConversationValidator conversationValidator;
    private UserValidator userValidator;

    @Override
    public CreateConversationResponseDto createConversation(CreateConversationRequestDto createConversationRequestDto) {
        conversationValidator.validate(createConversationRequestDto);
        userValidator.validate(createConversationRequestDto.participantIdList());

        val participantUserList = this.userRepository.findAllById(createConversationRequestDto.participantIdList());

        val conversation = ConversationEntity.builder()
                .title(createConversationRequestDto.title())
                .participants(participantUserList)
                .build();

        return ServiceUtils.toDto(this.conversationRepository.save(conversation));
    }

    /**
     * Retrieves a conversation by its ID and returns the conversation details along with the messages in it.
     * The messages are paginated based on the provided offset and limit, sorted by timestamp in descending order,
     * thus showing the most recent messages first.
     *
     * @param conversationId            the ID of the conversation to retrieve
     * @param getConversationRequestDto the request DTO containing pagination information
     */
    @Override
    @Transactional
    public GetConversationResponseDto getConversation(Long conversationId, GetConversationRequestDto getConversationRequestDto) {
        conversationValidator.validate(conversationId);

        @SuppressWarnings("OptionalGetWithoutIsPresent")
        val conversation = this.conversationRepository.findById(conversationId).get();

        Pageable pageable = PageRequest.of(
                getConversationRequestDto.offset(),
                getConversationRequestDto.limit(),
                Sort.by(Sort.Direction.DESC, "timestamp")
        );

        Page<MessageEntity> messagePage = messageRepository.findMessagesByConversationId(conversationId, pageable);

        return ServiceUtils.toDtoGetConversation(conversation, messagePage);
    }
}
