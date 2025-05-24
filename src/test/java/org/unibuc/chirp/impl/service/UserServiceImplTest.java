package org.unibuc.chirp.impl.service;

import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.unibuc.chirp.domain.dto.user.create.CreateUserRequestDto;
import org.unibuc.chirp.domain.dto.user.update.UpdateUserRequestDto;
import org.unibuc.chirp.domain.exception.AppException;
import org.unibuc.chirp.domain.exception.ErrorCode;
import org.unibuc.chirp.domain.repository.UserProfileRepository;
import org.unibuc.chirp.domain.repository.UserRepository;
import org.unibuc.chirp.domain.service.AuthService;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("UserService Tests")
class UserServiceImplTest {
    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserProfileRepository userProfileRepository;
    @Autowired
    private AuthService authService;

    public static CreateUserRequestDto getCreateUserRequestDto(String username) {
        return new CreateUserRequestDto(
                username,
                "testPassword"
        );
    }

    @BeforeEach
    void setUp() {
        userProfileRepository.deleteAll();
        userRepository.deleteAll();

        authService.registerUser(getCreateUserRequestDto("testUser"));
    }

    @Nested
    @DisplayName("Get User Details Tests")
    public class GetUserDetailsTests {
        @Test
        @WithMockUser("testUser")
        @DisplayName("Should return user details for existing user")
        void shouldReturnUserDetailsForExistingUser() {
            String username = "testUser";
            var userDetails = userService.getUserDetails(username);

            assertNotNull(userDetails, "User details should not be null");
            assertEquals(username, userDetails.username(), "Username should match");
        }

        @Test
        @DisplayName("Should throw exception for non-existing user")
        void shouldThrowExceptionForNonExistingUser() {
            String username = "nonExistingUser";

            val exception = assertThrows(AppException.class, () -> userService.getUserDetails(username),
                    "Should throw AppException for non-existing user");

            assertEquals(ErrorCode.CHR0002.getMessage() + ": User with username nonExistingUser not found", exception.getMessage(), "Exception message should match");
        }
    }


    @Nested
    @DisplayName("Update User Details Tests")
    public class UpdateUserDetailsTests {
        @Test
        @WithMockUser("testUser")
        @DisplayName("Should update user details successfully")
        void shouldUpdateUserDetailsSuccessfully() {
            String username = "testUser";

            val updateRequest = new UpdateUserRequestDto(
                    "Updated bio",
                    "1990-01-01",
                    "Updated location",
                    "https://updatedwebsite.com"
            );

            val updatedUser = userService.updateUserDetails(username, updateRequest, null);

            assertNotNull(updatedUser, "Updated user should not be null");
            assertEquals(updateRequest.bio(), updatedUser.bio(), "Bio should be updated");
            assertEquals(updateRequest.birthday(), updatedUser.birthday(), "Birthday should be updated");
            assertEquals(updateRequest.location(), updatedUser.location(), "Location should be updated");
            assertEquals(updateRequest.website(), updatedUser.website(), "Website should be updated");
            assertEquals(username, updatedUser.username(), "Username should remain the same");

            val userProfile = userProfileRepository.findUserProfileEntityByUser_Username(username)
                    .orElseThrow(() -> new RuntimeException("User profile not found"));

            assertEquals(updateRequest.bio(), userProfile.getBio(), "User profile bio should be updated");
            assertEquals(updateRequest.birthday(), userProfile.getBirthday().toString(), "User profile birthday should be updated");
            assertEquals(updateRequest.location(), userProfile.getLocation(), "User profile location should be updated");
            assertEquals(updateRequest.website(), userProfile.getWebsite(), "User profile website should be updated");
        }


        @Test
        @WithMockUser("testUser")
        @DisplayName("Should throw exception for invalid user update")
        void shouldThrowExceptionForInvalidUserUpdate() {
            String username = "nonExistingUser";

            val updateRequest = new UpdateUserRequestDto(
                    "Updated bio",
                    "1990-01-01",
                    "Updated location",
                    "https://updatedwebsite.com"
            );

            val exception = assertThrows(AppException.class, () -> userService.updateUserDetails(username, updateRequest, null),
                    "Should throw AppException for non-existing user");

            assertEquals(ErrorCode.CHR0002.getMessage() + ": User with username nonExistingUser not found", exception.getMessage(), "Exception message should match");
        }
    }


    @Nested
    @DisplayName("Explore Users Tests")
    public class ExploreUsersTests {
        @Test
        @WithMockUser("testUser")
        @DisplayName("Should return all non-admin users when search query is empty")
        void shouldReturnAllNonAdminUsersWhenSearchQueryIsEmpty() {
            var pageable = PageRequest.of(0, 10);
            var usersPage = userService.exploreUsers("", pageable);

            assertNotNull(usersPage, "Users page should not be null");
            assertFalse(usersPage.isEmpty(), "Users page should not be empty");

            val userList = userRepository.findAll();

            assertEquals(userList.size(), usersPage.getTotalElements(), "Total elements should match non-admin users count");
        }

        @Test
        @WithMockUser("testUser")
        @DisplayName("Should return users matching search query")
        void shouldReturnUsersMatchingSearchQuery() {
            String searchQuery = "testUser";
            var pageable = PageRequest.of(0, 10);
            var usersPage = userService.exploreUsers(searchQuery, pageable);

            assertNotNull(usersPage, "Users page should not be null");
            assertFalse(usersPage.isEmpty(), "Users page should not be empty");

            usersPage.forEach(user -> {
                assertTrue(user.username().contains(searchQuery), "User username should contain search query");
            });
        }
    }
}
