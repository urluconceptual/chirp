package org.unibuc.chirp.config.dev;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.unibuc.chirp.domain.dto.conversation.create.CreateConversationRequestDto;
import org.unibuc.chirp.domain.dto.message.create.CreateMessageRequestDto;
import org.unibuc.chirp.domain.dto.user.create.CreateUserRequestDto;
import org.unibuc.chirp.domain.repository.RoleRepository;
import org.unibuc.chirp.domain.repository.UserRepository;
import org.unibuc.chirp.domain.service.AuthService;
import org.unibuc.chirp.domain.service.ConversationService;
import org.unibuc.chirp.domain.service.FriendService;
import org.unibuc.chirp.domain.service.MessageService;

import java.util.List;

@Profile("dev")
@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner initialize(
            AuthService authService,
            FriendService friendService,
            ConversationService conversationService,
            MessageService messageService
    ) {
        return args -> {
            createUserIfNotFound("test_user_1", "password", authService);
            createUserIfNotFound("test_user_2", "password", authService);
            createFriendship(friendService, "test_user_1", "test_user_2");
            acceptFriendship(friendService, "test_user_2", "test_user_1");
            createConversation(conversationService, "test_user_1", "test_user_2");
            sendMessage(messageService, "test_user_1", "This is a test message from user 1");
            sendMessage(messageService, "test_user_2", "This is a test message from user 2");
        };
    }

    private void sendMessage(
            MessageService messageService,
            String senderUsername,
            String content
    ) {
        messageService.send(new CreateMessageRequestDto(
                1L,
                senderUsername,
                content
        ));
    }

    private void createConversation(
            ConversationService conversationService,
            String firstUsername,
            String secondUsername
    ) {
        conversationService.createConversation(new CreateConversationRequestDto(List.of(firstUsername,
                secondUsername), "Test conversation"));
    }

    private void acceptFriendship(FriendService friendService, String firstUsername, String secondUsername) {
        friendService.acceptFriendRequest(firstUsername, secondUsername);
    }

    private void createFriendship(FriendService friendService, String firstUsername, String secondUsername) {
        friendService.sendFriendRequest(firstUsername, secondUsername);
    }


    private void createUserIfNotFound(String username, String password, AuthService authService) {
        authService.registerUser(new CreateUserRequestDto(
                username,
                password
        ));
    }
}
