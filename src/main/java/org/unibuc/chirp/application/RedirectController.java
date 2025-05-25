package org.unibuc.chirp.application;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RedirectController {

    @GetMapping("/")
    public String redirectToLogin() {
        return "redirect:/auth/login";
    }

    @GetMapping("/access_denied")
    public String accessDeniedPage(){ return "access-denied"; }
}