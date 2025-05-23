package org.unibuc.chirp.application.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.unibuc.chirp.domain.dto.conversation.create.CreateConversationRequestDto;
import org.unibuc.chirp.domain.dto.conversation.create.CreateConversationResponseDto;
import org.unibuc.chirp.domain.dto.conversation.get.GetConversationResponseDto;
import org.unibuc.chirp.domain.service.ConversationService;

@RestController
@RequestMapping("/chirp/api/v1/conversation")
@AllArgsConstructor
@Getter
public class ConversationController {
    private ConversationService conversationService;


    @PostMapping("/new")
    public ResponseEntity<CreateConversationResponseDto> createConversation(
            @RequestBody CreateConversationRequestDto createConversationRequestDto) {
        return ResponseEntity.ok(
                conversationService.createConversation(createConversationRequestDto)
        );
    }

    @GetMapping("/{conversationId}")
    public ResponseEntity<GetConversationResponseDto> getConversation(
            @PathVariable Long conversationId) {
        return ResponseEntity.ok(
                conversationService.getConversation(conversationId)
        );
    }
}
