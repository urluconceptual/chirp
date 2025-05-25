package org.unibuc.chirp.domain.service;

import org.unibuc.chirp.domain.dto.message.create.CreateMessageRequestDto;

public interface MessageService {
     void send(CreateMessageRequestDto createMessageRequestDto);
}