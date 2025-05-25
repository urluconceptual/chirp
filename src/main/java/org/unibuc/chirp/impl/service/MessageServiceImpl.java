package org.unibuc.chirp.impl.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.unibuc.chirp.domain.dto.message.create.CreateMessageRequestDto;
import org.unibuc.chirp.domain.repository.MessageRepository;
import org.unibuc.chirp.domain.service.MessageService;
import org.unibuc.chirp.impl.mapper.MessageMapper;
import org.unibuc.chirp.impl.validator.MessageValidator;

@Slf4j
@Service
@AllArgsConstructor
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
