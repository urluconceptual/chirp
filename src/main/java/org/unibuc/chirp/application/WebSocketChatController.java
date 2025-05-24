package org.unibuc.chirp.application;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/chirp/api/v1/chat")
public class WebSocketChatController {

    @MessageMapping("/send")
    @SendTo("/topic/messages")
    public void sendMessage(String message) {
        // todo: this will be replaced with a service call to save the message
        System.out.println("Received message: " + message);
    }
}
