package org.unibuc.chirp.application;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.unibuc.chirp.domain.dto.user.get.GetUserDetailsResponseDto;
import org.unibuc.chirp.domain.dto.user.update.UpdateUserRequestDto;
import org.unibuc.chirp.domain.dto.user.update.UpdateUserResponseDto;
import org.unibuc.chirp.domain.service.UserService;

@Controller
@RequestMapping("/account")
@AllArgsConstructor
@Getter
public class AccountController {
    private UserService userService;

    @GetMapping("/my-account")
    public String showAccountPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        GetUserDetailsResponseDto user = userService.getUserDetails(username);
        model.addAttribute("user", user);
        return "account";
    }

    @GetMapping("/{username}")
    ResponseEntity<GetUserDetailsResponseDto> getUserDetails(@PathVariable String username) {
        return ResponseEntity.ok(
                this.userService.getUserDetails(username)
        );
    }

    @PutMapping("/{username}")
    ResponseEntity<UpdateUserResponseDto> updateUserDetails(@PathVariable String username,
                                                            @RequestBody UpdateUserRequestDto updateUserRequestDto) {
        return ResponseEntity.ok(
                this.userService.updateUserDetails(username, updateUserRequestDto)
        );
    }
}
