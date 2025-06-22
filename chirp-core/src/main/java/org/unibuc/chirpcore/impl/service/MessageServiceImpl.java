package org.unibuc.chirpcore.impl.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.unibuc.chirpcore.domain.dto.message.create.CreateMessageRequestDto;
import org.unibuc.chirpcore.domain.repository.MessageRepository;
import org.unibuc.chirpcore.domain.service.MessageService;
import org.unibuc.chirpcore.impl.mapper.MessageMapper;
import org.unibuc.chirpcore.impl.validator.MessageValidator;

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
