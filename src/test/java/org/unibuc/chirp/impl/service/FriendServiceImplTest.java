package org.unibuc.chirp.impl.service;

import lombok.val;
import org.h2.tools.Server;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.unibuc.chirp.domain.dto.user.create.CreateUserRequestDto;
import org.unibuc.chirp.domain.entity.UserEntity;
import org.unibuc.chirp.domain.entity.UserFriendshipEntity;
import org.unibuc.chirp.domain.exception.AppException;
import org.unibuc.chirp.domain.repository.UserFriendshipRepository;
import org.unibuc.chirp.domain.repository.UserProfileRepository;
import org.unibuc.chirp.domain.repository.UserRepository;
import org.unibuc.chirp.domain.service.AuthService;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("FriendService Tests")
class FriendServiceImplTest {
    @Autowired
    private FriendServiceImpl friendService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserFriendshipRepository userFriendshipRepository;
    @Autowired
    private UserProfileRepository userProfileRepository;
    @Autowired
    private AuthService authService;

    private UserEntity firstUser;
    private UserEntity secondUser;

    public static CreateUserRequestDto getCreateUserRequestDto(String username) {
        return new CreateUserRequestDto(
                username,
                "testPassword"
        );
    }

    @BeforeAll
    public static void initTest() throws SQLException {
        Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082")
                .start();
    }

    @BeforeEach
    void setUp() {
        userFriendshipRepository.deleteAll();
        userRepository.deleteAll();

        authService.registerUser(getCreateUserRequestDto("firstUser"));
        authService.registerUser(getCreateUserRequestDto("secondUser"));

        firstUser = userRepository.findByUsername("firstUser")
                .orElseThrow(() -> new RuntimeException("First user not found"));
        secondUser = userRepository.findByUsername("secondUser")
                .orElseThrow(() -> new RuntimeException("Second user not found"));
    }

    @Nested
    @DisplayName("Dependency Injection Tests")
    public class DependencyInjectionTests {
        @Test
        @DisplayName("Should properly inject all dependencies")
        void shouldInjectDependenciesProperly() {
            assertAll(
                    () -> assertNotNull(friendService, "FriendService should not be null"),
                    () -> assertNotNull(friendService.getUserFriendshipRepository(), "UserFriendshipRepository should not be null")
            );
        }
    }

    @Nested
    @DisplayName("Send Friend Request Tests")
    public class SendFriendRequestTests {
        @Test
        @DisplayName("Should send friend request successfully")
        void shouldSendFriendRequestSuccessfully() {
            String currentUsername = firstUser.getUsername();
            String targetUsername = secondUser.getUsername();

            friendService.sendFriendRequest(currentUsername, targetUsername);

            assertTrue(userFriendshipRepository.findByUsers(
                    firstUser, secondUser).isPresent(), "Friend request should be created");
        }

        @Test
        @DisplayName("Should throw exception when sending friend request with invalid user")
        void shouldThrowExceptionWhenSendingFriendRequestWithInvalidUser() {
            String currentUsername = "invalidUser";
            String targetUsername = secondUser.getUsername();

            val exception = assertThrows(AppException.class, () -> {
                friendService.sendFriendRequest(currentUsername, targetUsername);
            }, "Should throw exception for invalid user");

            assertEquals("User not found: User with username invalidUser not found", exception.getMessage(),
                    "Exception message should match for invalid user");
        }
    }

    @Nested
    @DisplayName("Accept Friend Request Tests")
    public class AcceptFriendRequestTests {
        @Test
        @DisplayName("Should accept friend request successfully")
        void shouldAcceptFriendRequestSuccessfully() {
            String currentUsername = firstUser.getUsername();
            String targetUsername = secondUser.getUsername();

            friendService.sendFriendRequest(currentUsername, targetUsername);

            friendService.acceptFriendRequest(currentUsername, targetUsername);

            assertEquals(UserFriendshipEntity.FriendshipStatus.ACCEPTED, userFriendshipRepository.findByUsers(
                            firstUser, secondUser).get().getStatus(),
                    "Friend request should be accepted");
        }
    }

    @Nested
    @DisplayName("Reject Friend Request Tests")
    public class RejectFriendRequestTests {
        @Test
        @DisplayName("Should reject friend request successfully")
        void shouldRejectFriendRequestSuccessfully() {
            String currentUsername = firstUser.getUsername();
            String targetUsername = secondUser.getUsername();

            friendService.sendFriendRequest(currentUsername, targetUsername);

            friendService.rejectFriendRequest(currentUsername, targetUsername);

            val friendshipRequest = userFriendshipRepository.findByUsers(firstUser, secondUser);

            assertNotNull(friendshipRequest.get(), "Friend request should not be null");
            assertEquals(UserFriendshipEntity.FriendshipStatus.REJECTED, friendshipRequest.get().getStatus(),
                    "Friend request should be rejected");
        }
    }


    @Nested
    @DisplayName("Get Friends Tests")
    public class GetFriendsTests {
        @BeforeAll
        static void setUpSecurityContext() {
            Authentication authentication = Mockito.mock(Authentication.class);
            Mockito.when(authentication.getName()).thenReturn("mockedUsername");

            SecurityContext securityContext = Mockito.mock(SecurityContext.class);
            Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);

            SecurityContextHolder.setContext(securityContext);
        }

        @Test
        @DisplayName("Should return empty list when no friends")
        void shouldReturnEmptyListWhenNoFriends() {
            String username = firstUser.getUsername();

            val friends = friendService.getFriends(username, PageRequest.of(
                    0,
                    10,
                    Sort.by(Sort.Direction.DESC, "respondedAt")
            ));

            assertTrue(friends.isEmpty(), "Should return empty list when no friends");
        }
    }

}