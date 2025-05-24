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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.unibuc.chirp.domain.dto.user.get.GetUserDetailsResponseDto;
import org.unibuc.chirp.domain.dto.user.update.UpdateUserRequestDto;
import org.unibuc.chirp.domain.service.UserService;

@Controller
@RequestMapping("/friend")
@AllArgsConstructor
@Getter
public class FriendController {
    private UserService userService;

    @PostMapping("/add")
    public String addFriend(@RequestParam("userId") String targetUsername) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // Call your service to create friend request
//        friendService.sendFriendRequest(currentUsername, targetUsername);

        // Redirect back to the explore page (or the profile)
        return "redirect:/account/explore";
    }
}
