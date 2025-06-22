package org.unibuc.chirpcore.impl.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.unibuc.chirpcore.domain.dto.conversation.create.ConversationResponseDto;
import org.unibuc.chirpcore.domain.dto.conversation.create.CreateConversationRequestDto;
import org.unibuc.chirpcore.domain.dto.conversation.get.ConversationDetailsResponseDto;
import org.unibuc.chirpcore.domain.dto.conversation.get.GetConversationRequestDto;
import org.unibuc.chirpcore.domain.entity.ConversationEntity;
import org.unibuc.chirpcore.domain.entity.MessageEntity;
import org.unibuc.chirpcore.domain.repository.ConversationRepository;
import org.unibuc.chirpcore.domain.repository.MessageRepository;
import org.unibuc.chirpcore.domain.repository.UserRepository;
import org.unibuc.chirpcore.domain.service.ConversationService;
import org.unibuc.chirpcore.impl.mapper.ConversationMapper;
import org.unibuc.chirpcore.impl.validator.ConversationValidator;
import org.unibuc.chirpcore.impl.validator.UserValidator;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
@Getter
public class ConversationServiceImpl implements ConversationService {
    private UserRepository userRepository;
    private ConversationRepository conversationRepository;
    private MessageRepository messageRepository;

    private ConversationValidator conversationValidator;
    private UserValidator userValidator;

    @Override
    public ConversationResponseDto createConversation(CreateConversationRequestDto createConversationRequestDto) {
        log.info("Creating conversation with title: {}", createConversationRequestDto.title());
        conversationValidator.validate(createConversationRequestDto);
        userValidator.validate(createConversationRequestDto.participantList());

        val participantUserList =
                this.userRepository.findAllByUsernameIn(createConversationRequestDto.participantList());

        val conversation = ConversationMapper.toConversationEntity(createConversationRequestDto.title(),
                participantUserList);

        return ConversationMapper.toDto(this.conversationRepository.save(conversation));
    }

    @Override
    @Transactional
    public ConversationDetailsResponseDto getConversation(Long conversationId,
                                                          GetConversationRequestDto getConversationRequestDto) {
        log.info("Getting conversation with ID: {}", conversationId);
        conversationValidator.validate(conversationId);
        val conversation = this.conversationRepository.findById(conversationId).get();

        Pageable pageable = PageRequest.of(
                getConversationRequestDto.page(),
                getConversationRequestDto.size(),
                Sort.by(Sort.Direction.DESC, "timestamp")
        );

        Page<MessageEntity> messagePage = messageRepository.findMessagesByConversationId(conversationId, pageable);

        return ConversationMapper.toDto(conversation, messagePage);
    }

    @Override
    @Transactional
    public List<ConversationResponseDto> getAllConversations(String username) {
        log.info("Getting all conversations for user: {}", username);
        userValidator.validate(username);

        List<ConversationEntity> conversations = this.conversationRepository.findByParticipantsUsername(username);

        return conversations.stream().map(ConversationMapper::toDto).toList();
    }
}
