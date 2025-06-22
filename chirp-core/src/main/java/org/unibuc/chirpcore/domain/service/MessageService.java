package org.unibuc.chirpcore.domain.service;

import org.unibuc.chirpcore.domain.dto.message.create.CreateMessageRequestDto;

public interface MessageService {
     void send(CreateMessageRequestDto createMessageRequestDto);
}