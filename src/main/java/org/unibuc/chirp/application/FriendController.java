package org.unibuc.chirp.application;

import jakarta.validation.Valid;
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
import org.springframework.web.bind.annotation.*;
import org.unibuc.chirp.domain.dto.user.get.GetUserDetailsResponseDto;
import org.unibuc.chirp.domain.service.FriendService;

import java.util.List;

@Controller
@RequestMapping("/friend")
@AllArgsConstructor
@Getter
public class FriendController {
    private FriendService friendService;

    @PostMapping("/add")
    public String addFriend(@RequestParam("username") String targetUsername) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        friendService.sendFriendRequest(currentUsername, targetUsername);

        return "redirect:/account/explore";
    }

    @PostMapping("/accept")
    public String acceptFriend(@RequestParam("username") String targetUsername) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        friendService.acceptFriendRequest(currentUsername, targetUsername);

        return "redirect:/friend/all";
    }

    @PostMapping("/reject")
    public String rejectFriend(@RequestParam("username") String targetUsername) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        friendService.rejectFriendRequest(currentUsername, targetUsername);

        return "redirect:/friend/all";
    }

    @PostMapping("/remove")
    public String removeFriend(@RequestParam("username") String targetUsername) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        friendService.removeFriend(currentUsername, targetUsername);

        return "redirect:/friend/all";
    }

    @GetMapping("/all")
    public String getAllFriends(Model model,
                                @RequestParam(value = "page", defaultValue = "0") int page,
                                @RequestParam(value = "size", defaultValue = "10") int size) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        Pageable pageable = PageRequest.of(page, size, Sort.by("respondedAt").descending());
        Page<GetUserDetailsResponseDto> friendsPage = friendService.getFriends(currentUsername, pageable);
        List<GetUserDetailsResponseDto> pendingRequests = friendService.getFriendRequests(currentUsername);

        model.addAttribute("friends", friendsPage);
        model.addAttribute("pendingRequests", pendingRequests);
        return "friends";
    }
}
