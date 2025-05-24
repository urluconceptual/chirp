package org.unibuc.chirp.impl.service;

import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.unibuc.chirp.domain.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("UserService Tests")
class ConversationServiceImplTest {

    @Autowired
    private ConversationServiceImpl conversationService;

    @Autowired
    private UserRepository userRepository;



}