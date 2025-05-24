package org.unibuc.chirp.application;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.unibuc.chirp.domain.dto.conversation.create.CreateConversationRequestDto;
import org.unibuc.chirp.domain.dto.conversation.create.ConversationResponseDto;
import org.unibuc.chirp.domain.dto.conversation.get.GetConversationRequestDto;
import org.unibuc.chirp.domain.dto.conversation.get.ConversationDetailsResponseDto;
import org.unibuc.chirp.domain.service.ConversationService;

@RestController
@RequestMapping("/chirp/api/v1/conversation")
@AllArgsConstructor
@Getter
public class ConversationController {
    private ConversationService conversationService;


    @PostMapping("/new")
    public ResponseEntity<ConversationResponseDto> createConversation(
            @RequestBody CreateConversationRequestDto createConversationRequestDto) {
        return ResponseEntity.ok(
                conversationService.createConversation(createConversationRequestDto)
        );
    }

    @GetMapping("/{conversationId}")
    public ResponseEntity<ConversationDetailsResponseDto> getConversation(
            @PathVariable Long conversationId, @RequestBody GetConversationRequestDto getConversationRequestDto) {
        return ResponseEntity.ok(
                conversationService.getConversation(conversationId, getConversationRequestDto)
        );
    }
}
