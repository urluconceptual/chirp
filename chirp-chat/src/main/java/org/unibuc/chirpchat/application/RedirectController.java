package org.unibuc.chirpchat.application;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RedirectController {
    @GetMapping("/access_denied")
    public String accessDeniedPage(){ return "access-denied"; }
}