package org.unibuc.chirp.application.rest;

import lombok.val;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.unibuc.chirp.domain.dto.conversation.create.CreateConversationRequestDto;
import org.unibuc.chirp.domain.exception.AppException;
import org.unibuc.chirp.domain.repository.AppUserRepository;
import org.unibuc.chirp.domain.repository.ConversationRepository;
import org.unibuc.chirp.impl.service.ConversationServiceImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class ConversationControllerTest {
    @Autowired
    private ConversationController conversationController;
    @Autowired
    private UserController userController;
    @Autowired
    private ConversationRepository conversationRepository;
    @Autowired
    private AppUserRepository userRepository;

    @Test
    void shouldInjectDependenciesProperly() {
        assertNotNull(conversationController, "ConversationController should not be null");
        assertNotNull(conversationController.getConversationService(), "ConversationService should not be null");

        assertInstanceOf(ConversationServiceImpl.class, conversationController.getConversationService(),
                "ConversationService should be of type ConversationServiceImpl");
    }

    @Test
    void shouldCreateConversation() {
        userController.createUser(
                UserControllerTest.getCreateUserRequestDto("firstUser")
        );
        userController.createUser(
                UserControllerTest.getCreateUserRequestDto("secondUser")
        );

        val firstUser = userRepository.findByUsername("firstUser").orElseThrow();
        val secondUser = userRepository.findByUsername("secondUser").orElseThrow();


        val response = conversationController.createConversation(
                new CreateConversationRequestDto(
                        List.of(firstUser.getId(), secondUser.getId()),
                        "Test conversation"
                )
        );

        assertNotNull(response, "Response should not be null");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertNotNull(response.getBody().conversationId(), "Conversation ID should not be null");
        assertNotNull(conversationRepository.findById(response.getBody().conversationId()).orElseThrow(),
                "Conversation should be present in the database");
        assertNotNull(response.getBody().title(), "Conversation title should not be null");
        assertEquals("Test conversation", response.getBody().title());
    }

    @Test
    void shouldThrowAppExceptionCHR0005WhenTitleIsNullOrEmpty() {
        userController.createUser(
                UserControllerTest.getCreateUserRequestDto("firstUser1")
        );
        userController.createUser(
                UserControllerTest.getCreateUserRequestDto("secondUser1")
        );

        val firstUser = userRepository.findByUsername("firstUser1").orElseThrow();
        val secondUser = userRepository.findByUsername("secondUser1").orElseThrow();

        assertThrows(AppException.class, () -> conversationController.createConversation(
                new CreateConversationRequestDto(
                        List.of(firstUser.getId(), secondUser.getId()),
                        null
                )
        ), "Title should not be null or empty");
    }

    @Test
    void shouldThrowAppExceptionCHR0006WhenTitleIsTooLong() {
        userController.createUser(
                UserControllerTest.getCreateUserRequestDto("firstUser2")
        );
        userController.createUser(
                UserControllerTest.getCreateUserRequestDto("secondUser2")
        );

        val firstUser = userRepository.findByUsername("firstUser2").orElseThrow();
        val secondUser = userRepository.findByUsername("secondUser2").orElseThrow();

        assertThrows(AppException.class, () -> conversationController.createConversation(
                new CreateConversationRequestDto(
                        List.of(firstUser.getId(), secondUser.getId()),
                        "a".repeat(101)
                )
        ), "Title should not be longer than 100 characters");
    }

    @Test
    void shouldThrowAppExceptionCHR0002WhenUserNotFound() {
        userController.createUser(
                UserControllerTest.getCreateUserRequestDto("firstUser3")
        );
        userController.createUser(
                UserControllerTest.getCreateUserRequestDto("secondUser3")
        );

        val firstUser = userRepository.findByUsername("firstUser3").orElseThrow();
        val secondUser = userRepository.findByUsername("secondUser3").orElseThrow();

        assertThrows(AppException.class, () -> conversationController.createConversation(
                new CreateConversationRequestDto(
                        List.of(firstUser.getId(), 999L),
                        "Test conversation"
                )
        ), "Users should be present in the database");
    }

}