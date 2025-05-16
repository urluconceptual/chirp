package org.unibuc.chirp.application.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChatPageController {
    @GetMapping("/chat")
    public String getChatPage() {
        return "chat";
    }
}
