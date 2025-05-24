package org.unibuc.chirp.application;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.unibuc.chirp.domain.service.MessageService;

@RestController
@RequestMapping("/chirp/api/v1/message")
@AllArgsConstructor
public class MessageController {
    private MessageService messageService;
}
