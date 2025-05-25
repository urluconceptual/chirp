package org.unibuc.chirp.application;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
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
    public String showMyAccountPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        GetUserDetailsResponseDto user = userService.getUserDetails(username);
        model.addAttribute("user", user);
        return "my-account";
    }

    @GetMapping("/{username}")
    public String showAccountPage(@PathVariable String username, Model model) {
        GetUserDetailsResponseDto user = userService.getUserDetails(username);
        model.addAttribute("user", user);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        String role = authentication.getAuthorities().stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .findFirst()
                .orElse("");

        model.addAttribute("isAdmin", "ROLE_ADMIN".equals(role));
        if (username.equals(currentUsername)) {
            return "redirect:/account/my-account";
        }

        return "account";
    }

    @GetMapping("/my-account/edit")
    public String editAccountPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        GetUserDetailsResponseDto user = userService.getUserDetails(username);
        model.addAttribute("user", user);
        return "my-account-edit";
    }

    @PostMapping("/my-account/edit")
    public String editAccountPage(@Valid @ModelAttribute("user") UpdateUserRequestDto updateUserRequestDto,
                                  BindingResult bindingResult, @RequestParam("avatarFile") MultipartFile avatarFile,
                                Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", updateUserRequestDto);
            return "my-account-edit";
        }
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            this.userService.updateUserDetails(username, updateUserRequestDto, avatarFile);
            return "redirect:/account/my-account";
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred while updating your account. Please try again.");
            model.addAttribute("user", updateUserRequestDto);
            return "my-account-edit";
        }
    }

    @GetMapping("/explore")
    public String exploreUsers(@RequestParam(required = false) String search,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size,
                               Model model) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("username").ascending());
        Page<GetUserDetailsResponseDto> users = userService.exploreUsers(search, pageable);

        model.addAttribute("users", users);
        model.addAttribute("search", search);

        return "explore";
    }
}
