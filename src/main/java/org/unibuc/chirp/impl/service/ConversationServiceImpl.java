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
import org.unibuc.chirp.domain.dto.conversation.create.ConversationResponseDto;
import org.unibuc.chirp.domain.dto.conversation.get.GetConversationRequestDto;
import org.unibuc.chirp.domain.dto.conversation.get.ConversationDetailsResponseDto;
import org.unibuc.chirp.domain.entity.ConversationEntity;
import org.unibuc.chirp.domain.entity.MessageEntity;
import org.unibuc.chirp.domain.repository.ConversationRepository;
import org.unibuc.chirp.domain.repository.MessageRepository;
import org.unibuc.chirp.domain.repository.UserRepository;
import org.unibuc.chirp.domain.service.ConversationService;
import org.unibuc.chirp.impl.service.utils.ServiceUtils;
import org.unibuc.chirp.impl.validator.ConversationValidator;
import org.unibuc.chirp.impl.validator.UserValidator;

import java.util.List;

@Service
@AllArgsConstructor
public class ConversationServiceImpl implements ConversationService {
    private UserRepository userRepository;
    private ConversationRepository conversationRepository;
    private MessageRepository messageRepository;

    private ConversationValidator conversationValidator;
    private UserValidator userValidator;

    @Override
    public ConversationResponseDto createConversation(CreateConversationRequestDto createConversationRequestDto) {
        conversationValidator.validate(createConversationRequestDto);
        userValidator.validate(createConversationRequestDto.participantList());

        val participantUserList =
                this.userRepository.findAllByUsernameIn(createConversationRequestDto.participantList());

        val conversation = ConversationEntity.builder()
                .title(createConversationRequestDto.title())
                .participants(participantUserList)
                .build();

        return ServiceUtils.toDto(this.conversationRepository.save(conversation));
    }

    @Override
    @Transactional
    public ConversationDetailsResponseDto getConversation(Long conversationId,
                                                          GetConversationRequestDto getConversationRequestDto) {
        conversationValidator.validate(conversationId);
        val conversation = this.conversationRepository.findById(conversationId).get();

        Pageable pageable = PageRequest.of(
                getConversationRequestDto.page(),
                getConversationRequestDto.size(),
                Sort.by(Sort.Direction.DESC, "timestamp")
        );

        Page<MessageEntity> messagePage = messageRepository.findMessagesByConversationId(conversationId, pageable);

        return ServiceUtils.toDtoGetConversation(conversation, messagePage);
    }

    @Override
    @Transactional
    public List<ConversationResponseDto> getAllConversations(String username) {
        userValidator.validate(username);

        List<ConversationEntity> conversations = this.conversationRepository.findByParticipantsUsername(username);

        return conversations.stream().map(ServiceUtils::toDto).toList();
    }
}
