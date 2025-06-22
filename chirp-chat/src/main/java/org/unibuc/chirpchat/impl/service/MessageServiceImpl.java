package org.unibuc.chirpchat.impl.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.unibuc.chirpchat.domain.dto.message.create.CreateMessageRequestDto;
import org.unibuc.chirpchat.domain.repository.MessageRepository;
import org.unibuc.chirpchat.domain.service.MessageService;
import org.unibuc.chirpchat.impl.mapper.MessageMapper;
import org.unibuc.chirpchat.impl.validator.MessageValidator;

@Slf4j
@Service
@AllArgsConstructor
@Getter
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;
    private final MessageValidator messageValidator;
    private final MessageMapper messageMapper;

    @Override
    public void send(CreateMessageRequestDto createMessageRequestDto) {
        log.debug("Sending message with content: {}", createMessageRequestDto.content());
        messageValidator.validate(createMessageRequestDto);
        messageRepository.save(messageMapper.toEntity(createMessageRequestDto));
    }
}
