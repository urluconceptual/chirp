package org.unibuc.chirpcore.application;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.unibuc.chirpcore.domain.dto.conversation.create.CreateConversationRequestDto;
import org.unibuc.chirpcore.domain.dto.conversation.get.ConversationDetailsResponseDto;
import org.unibuc.chirpcore.domain.dto.conversation.get.GetConversationRequestDto;
import org.unibuc.chirpcore.domain.dto.message.create.CreateMessageRequestDto;
import org.unibuc.chirpcore.domain.dto.user.get.GetUserDetailsResponseDto;
import org.unibuc.chirpcore.domain.service.ConversationService;
import org.unibuc.chirpcore.domain.service.FriendService;
import org.unibuc.chirpcore.domain.service.MessageService;

import java.util.List;

@Controller
@RequestMapping("/chat")
@RequiredArgsConstructor
@Getter
public class ChatController {
    private final FriendService friendService;
    private final ConversationService conversationService;
    private final MessageService messageService;
    @Value("${chirp.gateway.base-url}")
    private String gatewayBaseUrl;

    @GetMapping
    public String getChat(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        model.addAttribute("messages", List.of());
        model.addAttribute("chats", conversationService.getAllConversations(currentUsername));
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

    @GetMapping("/new/start")
    public String startNewChat(@RequestParam("friendUsername") String friendUsername,
                               @RequestParam(required = false) String title) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        CreateConversationRequestDto createConversationRequestDto = new CreateConversationRequestDto(
                List.of(currentUsername, friendUsername),
                StringUtils.isEmpty(title) ? currentUsername + "'s chat with " + friendUsername : title
        );
        conversationService.createConversation(createConversationRequestDto);
        return "redirect:" + gatewayBaseUrl + "/core/chat";
    }
}
