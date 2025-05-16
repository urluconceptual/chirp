package org.unibuc.chirp.application.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.unibuc.chirp.domain.service.MessageService;

@RestController
@RequestMapping("/chirp/api/v1/message")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;
}
