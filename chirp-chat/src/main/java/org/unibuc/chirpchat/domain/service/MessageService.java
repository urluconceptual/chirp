package org.unibuc.chirpchat.domain.service;

import org.unibuc.chirpchat.domain.dto.message.create.CreateMessageRequestDto;

public interface MessageService {
     void send(CreateMessageRequestDto createMessageRequestDto);
}