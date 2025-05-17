package org.unibuc.chirp.application.rest;

import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.unibuc.chirp.domain.dto.conversation.create.CreateConversationRequestDto;
import org.unibuc.chirp.domain.dto.user.create.CreateUserRequestDto;
import org.unibuc.chirp.domain.entity.AppUser;
import org.unibuc.chirp.domain.exception.AppException;
import org.unibuc.chirp.domain.repository.AppUserRepository;
import org.unibuc.chirp.domain.repository.ConversationRepository;
import org.unibuc.chirp.impl.service.ConversationServiceImpl;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("ConversationController Tests")
class ConversationControllerTest {
    @Autowired
    private ConversationController conversationController;
    @Autowired
    private UserController userController;
    @Autowired
    private ConversationRepository conversationRepository;
    @Autowired
    private AppUserRepository userRepository;

    private AppUser firstUser;
    private AppUser secondUser;

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
        return UserControllerTest.getCreateUserRequestDto(username);
    }

    @Nested
    @DisplayName("Create Conversation Tests")
    class CreateConversationTests {

        private static Stream<Arguments> invalidUserIdsCombinations() {
            return Stream.of(
                    Arguments.of(List.of(999L, 1000L)),
                    Arguments.of(List.of(1L, 999L))
            );
        }

        @BeforeEach
        void setUp() {
            userController.createUser(createUserDto("firstUser"));
            userController.createUser(createUserDto("secondUser"));

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
}