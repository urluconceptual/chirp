package org.unibuc.chirpcore.application;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.unibuc.chirpcore.domain.dto.user.get.GetUserResponseDto;
import org.unibuc.chirpcore.domain.service.UserService;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@Getter
public class AdminController {
    private final UserService userService;
    @Value("${chirp.gateway.base-url}")
    private String gatewayBaseUrl;

    @GetMapping("/users")
    public String showUsersPage(@RequestParam(required = false) String search,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "10") int size,
                                @RequestParam(defaultValue = "username") String sortBy,
                                Model model) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<GetUserResponseDto> usersPage = userService.getUsersPage(search, pageable);
        model.addAttribute("users", usersPage);
        model.addAttribute("search", search);
        return "all-users";
    }

    @PostMapping("/user/delete")
    public String deleteUser(@RequestParam String username) {
        userService.deleteUserByUsername(username);
        return "redirect:" + gatewayBaseUrl + "/core/admin/users";
    }
}
