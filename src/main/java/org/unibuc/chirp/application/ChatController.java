package org.unibuc.chirp.application;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.unibuc.chirp.domain.dto.conversation.create.CreateConversationRequestDto;
import org.unibuc.chirp.domain.dto.conversation.get.GetConversationRequestDto;
import org.unibuc.chirp.domain.dto.user.get.GetUserDetailsResponseDto;
import org.unibuc.chirp.domain.service.ConversationService;
import org.unibuc.chirp.domain.service.FriendService;

import java.util.List;

@Controller
@RequestMapping("/chat")
@AllArgsConstructor
@Getter
public class ChatController {
    private final FriendService friendService;
    private final ConversationService conversationService;

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

        model.addAttribute("messages", List.of());
        model.addAttribute("chats", conversationService.getAllConversations(currentUsername));

        var conversationDetails = conversationService.getConversation(chatId, new GetConversationRequestDto(0, 1000));
        model.addAttribute("chat", conversationDetails);

        return "chat";
    }

    @GetMapping("/new")
    public String getNewChat(Model model,
                             @RequestParam(value = "page", defaultValue = "0") int page,
                             @RequestParam(value = "size", defaultValue = "10") int size) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        Pageable pageable = PageRequest.of(page, size, Sort.by("respondedAt").descending());
        Page<GetUserDetailsResponseDto> friendsPage = friendService.getFriends(currentUsername, pageable);

        model.addAttribute("friends", friendsPage);
        return "new-chat";
    }

    @GetMapping("new/start")
    public String startNewChat(@RequestParam("friendUsername") String friendUsername) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        CreateConversationRequestDto createConversationRequestDto = new CreateConversationRequestDto(
                List.of(currentUsername, friendUsername), currentUsername + "'s chat with " + friendUsername
        );
        conversationService.createConversation(createConversationRequestDto);
        return "redirect:/chat";
    }
}
