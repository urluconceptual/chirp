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
import org.unibuc.chirp.domain.dto.user.create.CreateUserRequestDto;
import org.unibuc.chirp.domain.entity.UserEntity;
import org.unibuc.chirp.domain.entity.UserStatusEntity;
import org.unibuc.chirp.domain.exception.ErrorCode;
import org.unibuc.chirp.domain.repository.UserRepository;
import org.unibuc.chirp.domain.repository.UserStatusRepository;
import org.unibuc.chirp.domain.service.AuthService;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("MessageService Tests")
@Transactional
class UserStatusServiceImplTest {
    @Autowired
    private UserStatusServiceImpl userStatusService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserStatusRepository userStatusRepository;
    @Autowired
    private AuthService authService;

    private UserEntity firstUser;

    public static CreateUserRequestDto getCreateUserRequestDto(String username) {
        return new CreateUserRequestDto(
                username,
                "testPassword"
        );
    }

    @BeforeEach
    void setUp() {
        userStatusRepository.deleteAll();
        userRepository.deleteAll();

        authService.registerUser(getCreateUserRequestDto("firstUser"));
        authService.registerUser(getCreateUserRequestDto("secondUser"));

        firstUser = userRepository.findByUsername("firstUser")
                .orElseThrow(() -> new RuntimeException("First user not found"));
    }

    @Nested
    @DisplayName("Dependency Injection Tests")
    public class DependencyInjectionTests {

        @Test
        @DisplayName("Should inject dependencies properly")
        void shouldInjectDependenciesProperly() {
            assertAll(
                    () -> assertNotNull(userStatusService.getUserStatusRepository(),
                            "UserStatusRepository should be injected"),
                    () -> assertNotNull(userStatusService.getUserRepository(), "UserRepository should be injected")
            );
        }
    }

    @Nested
    @DisplayName("User Status Update Tests")
    public class UserStatusUpdateTests {

        @Test
        @DisplayName("Should update user status to ONLINE")
        void shouldUpdateUserStatus() {
            String username = firstUser.getUsername();

            userStatusService.updateUserStatus(username, UserStatusEntity.StatusType.ONLINE);
            UserStatusEntity userStatus = userStatusRepository.findByUser_Username(username).orElseThrow();

            assertEquals(UserStatusEntity.StatusType.ONLINE, userStatus.getStatus(),
                    "User status should be updated correctly");
            assertNotNull(userStatus.getLastUpdated());
        }

        @Test
        @DisplayName("Should update user status to ONLINE and then to OFFLINE")
        void shouldUpdateUserStatusToOffline() {
            String username = firstUser.getUsername();

            userStatusService.updateUserStatus(username, UserStatusEntity.StatusType.ONLINE);
            UserStatusEntity userStatus = userStatusRepository.findByUser_Username(username).orElseThrow();

            assertEquals(UserStatusEntity.StatusType.ONLINE, userStatus.getStatus(), "User status should be ONLINE");
            assertNotNull(userStatus.getLastUpdated());

            LocalDateTime lastUpdated = userStatus.getLastUpdated();

            userStatusService.updateUserStatus(username, UserStatusEntity.StatusType.OFFLINE);
            userStatus = userStatusRepository.findByUser_Username(username).orElseThrow();

            assertEquals(UserStatusEntity.StatusType.OFFLINE, userStatus.getStatus(), "User status should be OFFLINE");
            assertNotNull(userStatus.getLastUpdated());
            assertNotEquals(lastUpdated, userStatus.getLastUpdated());
        }

        @Test
        @DisplayName("Should throw exception when updating status for non-existent user")
        void shouldThrowExceptionForNonExistentUser() {
            String nonExistentUsername = "nonExistentUser";

            val exception = assertThrows(RuntimeException.class, () -> {
                userStatusService.updateUserStatus(nonExistentUsername, UserStatusEntity.StatusType.ONLINE);
            }, "Should throw exception when user does not exist");

            assertEquals(ErrorCode.CHR0002.getMessage() + ": User with username " + nonExistentUsername + " not found",
                    exception.getMessage(), "Exception message should indicate user not found");
        }

    }


}