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
import org.unibuc.chirp.domain.dto.conversation.get.GetConversationRequestDto;
import org.unibuc.chirp.domain.dto.user.create.CreateUserRequestDto;
import org.unibuc.chirp.domain.entity.UserEntity;
import org.unibuc.chirp.domain.exception.AppException;
import org.unibuc.chirp.domain.exception.ErrorCode;
import org.unibuc.chirp.domain.repository.ConversationRepository;
import org.unibuc.chirp.domain.repository.MessageRepository;
import org.unibuc.chirp.domain.repository.UserRepository;
import org.unibuc.chirp.domain.service.AuthService;
import org.unibuc.chirp.domain.service.FriendService;
import org.unibuc.chirp.domain.service.MessageService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("UserService Tests")
@Transactional
class ConversationServiceImplTest {

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

    public static CreateUserRequestDto getCreateUserRequestDto(String username) {
        return new CreateUserRequestDto(
                username,
                "testPassword"
        );
    }

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        authService.registerUser(getCreateUserRequestDto("firstUser"));
        authService.registerUser(getCreateUserRequestDto("secondUser"));

        firstUser = userRepository.findByUsername("firstUser")
                .orElseThrow(() -> new RuntimeException("First user not found"));
        secondUser = userRepository.findByUsername("secondUser")
                .orElseThrow(() -> new RuntimeException("Second user not found"));

        friendService.sendFriendRequest(firstUser.getUsername(), secondUser.getUsername());
        friendService.acceptFriendRequest(secondUser.getUsername(), firstUser.getUsername());
    }

    @Nested
    @DisplayName("Dependency Injection Tests")
    public class DependencyInjectionTests {

        @Test
        @DisplayName("Should inject dependencies properly")
        void shouldInjectDependenciesProperly() {
            assertAll(
                    () -> assertNotNull(conversationService.getUserRepository(), "User repository should be injected"),
                    () -> assertNotNull(conversationService.getConversationRepository(), "Conversation repository should be injected"),
                    () -> assertNotNull(conversationService.getMessageRepository(), "Message repository should be injected"),
                    () -> assertNotNull(conversationService.getConversationValidator(), "Conversation validator should be injected"),
                    () -> assertNotNull(conversationService.getUserValidator(), "User validator should be injected")
            );
        }
    }

    @Nested
    @DisplayName("Create conversation tests")
    public class CreateConversationTests {

        @Test
        @DisplayName("Should throw exception when creating conversation with title longer than 100 characters")
        void shouldThrowExceptionWhenCreatingConversationWithLongTitle() {
            String longTitle = "a".repeat(101); // 101 characters long
            CreateConversationRequestDto requestDto = new CreateConversationRequestDto(
                    List.of(firstUser.getUsername(), secondUser.getUsername()),
                    longTitle
            );

            val exception = assertThrows(AppException.class, () -> {
                conversationService.createConversation(requestDto);
            }, "Should throw exception for title longer than 100 characters");

            assertEquals(ErrorCode.CHR0006.getMessage(), exception.getMessage(),
                    "Exception message should match for long title");
        }

        @Test
        @DisplayName("Should throw exception when creating conversation with empty title")
        void shouldThrowExceptionWhenCreatingConversationWithEmptyTitle() {
            CreateConversationRequestDto requestDto = new CreateConversationRequestDto(
                    List.of(firstUser.getUsername(), secondUser.getUsername()),
                    ""
            );

            val exception = assertThrows(AppException.class, () -> {
                conversationService.createConversation(requestDto);
            }, "Should throw exception for empty title");

            assertEquals(ErrorCode.CHR0005.getMessage(), exception.getMessage(),
                    "Exception message should match for empty title");
        }

        @Test
        @DisplayName("Should create a conversation successfully")
        void shouldCreateConversationSuccessfully() {
            conversationService.createConversation(new CreateConversationRequestDto(
                    List.of(firstUser.getUsername(), secondUser.getUsername()),
                    "Test Conversation"
            ));

            assertFalse(conversationRepository.findAll().isEmpty(), "Conversation should be created");
            assertEquals(1, conversationRepository.findAll().size(), "There should be one conversation");
            assertTrue(conversationRepository.findAll().stream()
                            .anyMatch(c -> c.getTitle().equals("Test Conversation") &&
                                    c.getParticipants().contains(firstUser) &&
                                    c.getParticipants().contains(secondUser)),
                    "Conversation should contain both participants");
        }

        @Test
        @DisplayName("Should throw exception when creating conversation with invalid participants")
        void shouldThrowExceptionWhenCreatingConversationWithInvalidParticipants() {
            CreateConversationRequestDto requestDto = new CreateConversationRequestDto(
                    List.of("invalidUser", secondUser.getUsername()),
                    "Test Conversation"
            );

            val exception = assertThrows(AppException.class, () -> {
                conversationService.createConversation(requestDto);
            }, "Should throw exception for invalid participants");

            assertEquals("User not found: User with username invalidUser not found", exception.getMessage(),
                    "Exception message should match for invalid user");
        }
    }

    @Nested
    @DisplayName("Get conversation tests")
    public class GetConversationTests {

        @Test
        @DisplayName("Should throw exception when getting conversation with invalid ID")
        void shouldThrowExceptionWhenGettingConversationWithInvalidId() {
            Long invalidConversationId = 999L;

            val exception = assertThrows(AppException.class, () -> {
                conversationService.getConversation(invalidConversationId, new GetConversationRequestDto(0, 10));
            }, "Should throw exception for invalid conversation ID");

            assertEquals(ErrorCode.CHR0007.getMessage(), exception.getMessage(),
                    "Exception message should match for invalid conversation ID");
        }

        @Test
        @DisplayName("Should get conversation successfully")
        void shouldGetConversationSuccessfully() {
            CreateConversationRequestDto requestDto = new CreateConversationRequestDto(
                    List.of(firstUser.getUsername(), secondUser.getUsername()),
                    "Test Conversation"
            );
            var conversation = conversationService.createConversation(requestDto);

            var retrievedConversation = conversationService.getConversation(conversation.id(),
                    new GetConversationRequestDto(0, 10));

            assertNotNull(retrievedConversation, "Retrieved conversation should not be null");
            assertEquals(conversation.id(), retrievedConversation.id(), "Retrieved conversation ID should match");
            assertEquals(conversation.title(), retrievedConversation.title(),
                    "Retrieved conversation title should match");
            assertTrue(retrievedConversation.participantList().stream()
                            .anyMatch(participant -> participant.equals(firstUser.getUsername())),
                    "Retrieved conversation should contain first user as participant");
            assertTrue(retrievedConversation.participantList().stream()
                            .anyMatch(participant -> participant.equals(secondUser.getUsername())),
                    "Retrieved conversation should contain second user as participant");
            assertNotNull(retrievedConversation.messages(), "Retrieved conversation should have message list");
        }
    }

    @Nested
    @DisplayName("Get all conversations tests")
    public class GetAllConversationsTests {

        @Test
        @DisplayName("Should get all conversations for a user")
        void shouldGetAllConversationsForUser() {
            CreateConversationRequestDto requestDto = new CreateConversationRequestDto(
                    List.of(firstUser.getUsername(), secondUser.getUsername()),
                    "Test Conversation"
            );
            conversationService.createConversation(requestDto);

            List<ConversationResponseDto> conversations = conversationService.getAllConversations(firstUser.getUsername());

            assertNotNull(conversations, "Conversations list should not be null");
            assertFalse(conversations.isEmpty(), "Conversations list should not be empty");
            assertEquals(1, conversations.size(), "There should be one conversation for the user");
            assertEquals("Test Conversation", conversations.get(0).title(),
                    "Conversation title should match the created one");
        }

        @Test
        @DisplayName("Should throw exception when getting conversations for invalid user")
        void shouldThrowExceptionWhenGettingConversationsForInvalidUser() {
            String invalidUsername = "invalidUser";

            val exception = assertThrows(AppException.class, () -> {
                conversationService.getAllConversations(invalidUsername);
            }, "Should throw exception for invalid username");

            assertEquals("User not found: User with username invalidUser not found", exception.getMessage(),
                    "Exception message should match for invalid user");
        }
    }

}