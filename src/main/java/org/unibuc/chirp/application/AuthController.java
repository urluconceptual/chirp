package org.unibuc.chirp.application;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.unibuc.chirp.domain.dto.user.create.CreateUserRequestDto;
import org.unibuc.chirp.domain.dto.user.login.LoginRequestDto;
import org.unibuc.chirp.domain.service.AuthService;

@Controller
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private AuthService authService;

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("loginRequestDto", new LoginRequestDto("", ""));
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(@Valid @ModelAttribute("loginRequestDto") LoginRequestDto loginRequestDto, Model model,
                          BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("loginRequestDto", loginRequestDto);
            return "login";
        }
        try {
            authService.loginUser(loginRequestDto, request);
            return "redirect:/chat";
        } catch (AuthenticationException ex) {
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("createUserRequestDto", new CreateUserRequestDto("", ""));
        return "register";
    }

    @PostMapping("/register")
    public String createUser(@Valid @ModelAttribute("createUserRequestDto") CreateUserRequestDto createUserRequestDto,
                             BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("createUserRequestDto", createUserRequestDto);
            return "register";
        }

        try {
            this.authService.registerUser(createUserRequestDto);
            redirectAttributes.addFlashAttribute("success", "User created successfully! Please log in.");
            return "redirect:/auth/login";
        } catch (Exception e) {
            bindingResult.rejectValue("username", "error.username.exists", "Username already exists");
            model.addAttribute("createUserRequestDto", createUserRequestDto);
            return "register";
        }
    }
}
