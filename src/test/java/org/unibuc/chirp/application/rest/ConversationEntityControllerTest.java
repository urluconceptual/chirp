package org.unibuc.chirp.application.rest;

import lombok.val;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.unibuc.chirp.application.ConversationController;
import org.unibuc.chirp.domain.dto.conversation.create.CreateConversationRequestDto;
import org.unibuc.chirp.domain.dto.conversation.get.GetConversationRequestDto;
import org.unibuc.chirp.domain.dto.user.create.CreateUserRequestDto;
import org.unibuc.chirp.domain.entity.UserEntity;
import org.unibuc.chirp.domain.entity.ConversationEntity;
import org.unibuc.chirp.domain.entity.MessageEntity;
import org.unibuc.chirp.domain.exception.AppException;
import org.unibuc.chirp.domain.exception.ErrorCode;
import org.unibuc.chirp.domain.repository.UserRepository;
import org.unibuc.chirp.domain.repository.ConversationRepository;
import org.unibuc.chirp.domain.repository.MessageRepository;
import org.unibuc.chirp.domain.service.AuthService;
import org.unibuc.chirp.impl.service.ConversationServiceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("ConversationController Tests")
class ConversationEntityControllerTest {
    @Autowired
    private ConversationController conversationController;
    @Autowired
    private AuthService authService;
    @Autowired
    private ConversationRepository conversationRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MessageRepository messageRepository;

    private UserEntity firstUser;
    private UserEntity secondUser;

    @BeforeEach
    void setUp() {
        conversationRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Dependencies should be properly injected")
    void shouldInjectDependenciesProperly() {
        assertAll(
                () -> assertNotNull(conversationController, "ConversationController should not be null"),
                () -> assertNotNull(conversationController.getConversationService(),
                        "ConversationService should not be null"),
                () -> assertInstanceOf(ConversationServiceImpl.class,
                        conversationController.getConversationService(),
                        "ConversationService should be of type ConversationServiceImpl")
        );
    }

    private CreateUserRequestDto createUserDto(String username) {
        return AccountControllerTest.getCreateUserRequestDto(username);
    }

    @Nested
    @DisplayName("Create Conversation Tests")
    class CreateConversationEntityTests {

        private static Stream<Arguments> invalidUserIdsCombinations() {
            return Stream.of(
                    Arguments.of(List.of(999L, 1000L)),
                    Arguments.of(List.of(1L, 999L))
            );
        }

        @BeforeEach
        void setUp() {
            authService.registerUser(createUserDto("firstUser"));
            authService.registerUser(createUserDto("secondUser"));

            firstUser = userRepository.findByUsername("firstUser").orElseThrow();
            secondUser = userRepository.findByUsername("secondUser").orElseThrow();
        }

        @Test
        @DisplayName("Should successfully create a conversation")
        void shouldCreateConversation() {
            val request = new CreateConversationRequestDto(
                    List.of(firstUser.getId(), secondUser.getId()),
                    "Test conversation"
            );

            val response = conversationController.createConversation(request);

            assertAll(
                    () -> assertNotNull(response, "Response should not be null"),
                    () -> assertNotNull(response.getBody(), "Response body should not be null"),
                    () -> assertNotNull(response.getBody().conversationId(), "Conversation ID should not be null"),
                    () -> assertNotNull(
                            conversationRepository.findById(response.getBody().conversationId()).orElseThrow(),
                            "Conversation should be present in the database"
                    ),
                    () -> assertEquals("Test conversation", response.getBody().title(),
                            "Conversation title should match")
            );
        }

        @ParameterizedTest(name = "Title validation: {0}")
        @NullAndEmptySource
        @DisplayName("Should throw CHR0005 when title is invalid")
        void shouldThrowAppExceptionForInvalidTitle(String title) {
            val request = new CreateConversationRequestDto(
                    List.of(firstUser.getId(), secondUser.getId()),
                    title
            );

            assertThrows(AppException.class,
                    () -> conversationController.createConversation(request),
                    "Should throw AppException for invalid title"
            );
        }

        @Test
        @DisplayName("Should throw CHR0006 when title exceeds maximum length")
        void shouldThrowAppExceptionForTooLongTitle() {
            val request = new CreateConversationRequestDto(
                    List.of(firstUser.getId(), secondUser.getId()),
                    "a".repeat(101)
            );

            assertThrows(AppException.class,
                    () -> conversationController.createConversation(request),
                    "Should throw AppException for too long title"
            );
        }

        @ParameterizedTest
        @MethodSource("invalidUserIdsCombinations")
        @DisplayName("Should throw CHR0002 when user not found")
        void shouldThrowAppExceptionWhenUserNotFound(List<Long> userIds) {
            val request = new CreateConversationRequestDto(
                    userIds,
                    "Test conversation"
            );

            assertThrows(AppException.class,
                    () -> conversationController.createConversation(request),
                    "Should throw AppException when user not found"
            );
        }
    }

    @Nested
    @DisplayName("Get Conversation Tests")
    class GetConversationEntityTests {

        private ConversationEntity conversationEntity;
        private MessageEntity firstMessageEntity;
        private MessageEntity secondMessageEntity;


        @BeforeEach
        void setUp() {
            authService.registerUser(createUserDto("firstUser"));
            authService.registerUser(createUserDto("secondUser"));

            firstUser = userRepository.findByUsername("firstUser").orElseThrow();
            secondUser = userRepository.findByUsername("secondUser").orElseThrow();


            conversationEntity = conversationRepository.save(
                    ConversationEntity.builder()
                            .title("Test conversation")
                            .participants(List.of(firstUser, secondUser))
                            .build()
            );

            firstMessageEntity = messageRepository.save(
                    MessageEntity.builder()
                            .content("Test message")
                            .sender(firstUser)
                            .conversation(conversationEntity)
                            .timestamp(LocalDateTime.of(2023, 10, 1, 12, 0))
                            .build()
            );

            secondMessageEntity = messageRepository.save(
                    MessageEntity.builder()
                            .content("Another test message")
                            .sender(secondUser)
                            .conversation(conversationEntity)
                            .timestamp(LocalDateTime.of(2023, 10, 1, 12, 2))
                            .build()
            );

            conversationEntity.setMessageList(List.of(firstMessageEntity, secondMessageEntity));
            conversationRepository.save(conversationEntity);
        }

        @Test
        void shouldGetConversationWhenIdIsFine() {
            val response = conversationController.getConversation(
                    conversationEntity.getId(), new GetConversationRequestDto(0, 20));

            assertAll(
                    () -> assertNotNull(response, "Response should not be null"),
                    () -> assertNotNull(response.getBody(), "Response body should not be null"),
                    () -> assertEquals(conversationEntity.getId(), response.getBody().id(), "Conversation ID should match"),
                    () -> assertEquals(conversationEntity.getTitle(), response.getBody().title(), "Title should match"),
                    () -> assertEquals(conversationEntity.getMessageList().size(), response.getBody().messages().size(), "Messages size should match"),
                    () -> assertEquals(List.of(firstUser.getUsername(), secondUser.getUsername()), response.getBody().participantList()),
                    () -> assertEquals(
                            conversationEntity.getMessageList().reversed().get(0).getContent(), response.getBody().messages().get(0).content(),
                            "First message content should match"),
                    () -> assertEquals(
                            conversationEntity.getMessageList().reversed().get(1).getContent(), response.getBody().messages().get(1).content(),
                            "Second message content should match")
            );
        }

        @Test
        void shouldThrowAppExceptionWhenConversationNotFound() {
            AppException exception = assertThrows(AppException.class,
                    () -> conversationController.getConversation(999L, new GetConversationRequestDto(0, 20)),
                    "Should throw AppException when conversation not found"
            );

            assertAll(
                    () -> assertEquals(ErrorCode.CHR0007.getMessage(), exception.getMessage(), "Error message should match")
            );
        }

        @Test
        void shouldPaginateMessagesCorrectly() {
            val response = conversationController.getConversation(
                    conversationEntity.getId(), new GetConversationRequestDto(0, 1));

            assertAll(
                    () -> assertNotNull(response, "Response should not be null"),
                    () -> assertNotNull(response.getBody(), "Response body should not be null"),
                    () -> assertEquals(1, response.getBody().messages().size(), "Should return only one message"),
                    () -> assertEquals(secondMessageEntity.getContent(), response.getBody().messages().get(0).content(),
                            "First message content should match")
            );

            val response2 = conversationController.getConversation(
                    conversationEntity.getId(), new GetConversationRequestDto(1, 1));

            assertAll(
                    () -> assertNotNull(response2, "Response should not be null"),
                    () -> assertNotNull(response2.getBody(), "Response body should not be null"),
                    () -> assertEquals(1, response2.getBody().messages().size(), "Should return only one message"),
                    () -> assertEquals(firstMessageEntity.getContent(), response2.getBody().messages().get(0).content(),
                            "Second message content should match")
            );
        }
    }
}