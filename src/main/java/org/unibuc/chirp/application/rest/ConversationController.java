package org.unibuc.chirp.application.rest;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.unibuc.chirp.domain.service.ConversationService;

@RestController
@RequestMapping("/chirp/api/v1/conversation")
@AllArgsConstructor
public class ConversationController {
    private ConversationService conversationService;
}
