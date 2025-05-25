package org.unibuc.chirp.impl.service;

import jakarta.transaction.Transactional;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.unibuc.chirp.domain.dto.conversation.create.ConversationResponseDto;
import org.unibuc.chirp.domain.dto.conversation.create.CreateConversationRequestDto;
import org.unibuc.chirp.domain.dto.message.create.CreateMessageRequestDto;
import org.unibuc.chirp.domain.dto.user.create.CreateUserRequestDto;
import org.unibuc.chirp.domain.entity.UserEntity;
import org.unibuc.chirp.domain.exception.AppException;
import org.unibuc.chirp.domain.exception.ErrorCode;
import org.unibuc.chirp.domain.repository.ConversationRepository;
import org.unibuc.chirp.domain.repository.MessageRepository;
import org.unibuc.chirp.domain.repository.UserRepository;
import org.unibuc.chirp.domain.service.AuthService;
import org.unibuc.chirp.domain.service.FriendService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ActiveProfiles("test")
@DisplayName("MessageService Tests")
@Transactional
class MessageServiceImplTest {
    @Autowired
    private MessageServiceImpl messageService;
    @Autowired
    private ConversationServiceImpl conversationService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ConversationRepository conversationRepository;
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private AuthService authService;
    @Autowired
    private FriendService friendService;

    private UserEntity firstUser;
    private UserEntity secondUser;
    private ConversationResponseDto conversationResponseDto;

    public static CreateUserRequestDto getCreateUserRequestDto(String username) {
        return new CreateUserRequestDto(
                username,
                "testPassword"
        );
    }

    @BeforeEach
    void setUp() {
        messageRepository.deleteAll();
        conversationRepository.deleteAll();
        userRepository.deleteAll();

        authService.registerUser(getCreateUserRequestDto("firstUser"));
        authService.registerUser(getCreateUserRequestDto("secondUser"));

        firstUser = userRepository.findByUsername("firstUser")
                .orElseThrow(() -> new RuntimeException("First user not found"));
        secondUser = userRepository.findByUsername("secondUser")
                .orElseThrow(() -> new RuntimeException("Second user not found"));

        friendService.sendFriendRequest(firstUser.getUsername(), secondUser.getUsername());
        friendService.acceptFriendRequest(secondUser.getUsername(), firstUser.getUsername());


        CreateConversationRequestDto requestDto = new CreateConversationRequestDto(
                List.of(firstUser.getUsername(), secondUser.getUsername()),
                "Test Conversation"
        );

        conversationResponseDto = conversationService.createConversation(requestDto);
    }

    @Nested
    @DisplayName("Dependency Injection Tests")
    public class DependencyInjectionTests {
        @Test
        @DisplayName("Should inject dependencies properly")
        void shouldInjectDependencies() {
            assertAll(
                () -> assertEquals(MessageServiceImpl.class, messageService.getClass()),
                () -> assertEquals(ConversationServiceImpl.class, conversationService.getClass()),
                () -> assertEquals(UserRepository.class, userRepository.getClass())
            );
        }
    }

    @Nested
    @DisplayName("Send Message Tests")
    public class SendMessageTests {

        @Test
        @DisplayName("Should send a message successfully")
        void shouldSendMessageSuccessfully() {
            String content = "Hello, this is a test message!";
            messageService.send(new CreateMessageRequestDto(conversationResponseDto.id(), firstUser.getUsername(),
                    content));

            var messages = messageRepository.findAll();
            assertAll(
                () -> assertEquals(1, messages.size(), "Message count should be 1"),
                () -> assertEquals(content, messages.get(0).getContent(), "Message content should match")
            );
        }

        @Test
        @DisplayName("Should throw exception when sending empty message")
        void shouldThrowExceptionWhenSendingEmptyMessage() {
            String content = "";
            CreateMessageRequestDto requestDto = new CreateMessageRequestDto(conversationResponseDto.id(),
                    firstUser.getUsername(), content);

            val exception = assertThrows(AppException.class, () -> messageService.send(requestDto),
                "Expected AppException when sending empty message");

            assertEquals(ErrorCode.CHR0009.getMessage(), exception.getMessage());
        }
    }
}