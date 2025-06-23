package org.unibuc.chirpchat.application;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.unibuc.chirpchat.domain.dto.conversation.get.ConversationDetailsResponseDto;
import org.unibuc.chirpchat.domain.dto.conversation.get.GetConversationRequestDto;
import org.unibuc.chirpchat.domain.dto.message.create.CreateMessageRequestDto;
import org.unibuc.chirpchat.domain.service.ConversationService;
import org.unibuc.chirpchat.domain.service.MessageService;

import java.util.List;

@Controller
@RequestMapping("/chat")
@AllArgsConstructor
@Getter
public class ChatController {
    private final ConversationService conversationService;
    private final MessageService messageService;

    @GetMapping
    public String getChat(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        model.addAttribute("messages", List.of());
        model.addAttribute("chats", conversationService.getAllConversations(currentUsername));
        return "chat";
    }

    @GetMapping("/messages/{chatId}")
    public String getChatMessages(@PathVariable Long chatId, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        model.addAttribute("chats", conversationService.getAllConversations(currentUsername));

        var conversationDetails = conversationService.getConversation(chatId, new GetConversationRequestDto(0, 15));
        model.addAttribute("chat", conversationDetails);

        return "chat";
    }

    @GetMapping("/messages/{chatId}/page")
    @ResponseBody
    public ConversationDetailsResponseDto getChatMessagesPage(
            @PathVariable Long chatId,
            @RequestParam int page,
            @RequestParam int size) {
        return conversationService.getConversation(chatId, new GetConversationRequestDto(page, size));
    }

    @PostMapping("/send/{id}")
    public String sendMessage(@PathVariable Long id,
                              @RequestParam("message") String content) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String senderUsername = authentication.getName();
        CreateMessageRequestDto createMessageRequestDto = new CreateMessageRequestDto(id, senderUsername, content);

        messageService.send(createMessageRequestDto);

        return "redirect:/chat/messages/" + id;
    }
}
