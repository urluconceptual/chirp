package org.unibuc.chirp.application.rest;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.test.context.ActiveProfiles;
import org.unibuc.chirp.domain.dto.user.create.CreateUserRequestDto;
import org.unibuc.chirp.domain.dto.user.update.UpdateUserRequestDto;
import org.unibuc.chirp.domain.exception.AppException;
import org.unibuc.chirp.domain.exception.ErrorCode;
import org.unibuc.chirp.domain.repository.AppUserProfileRepository;
import org.unibuc.chirp.domain.repository.AppUserRepository;
import org.unibuc.chirp.impl.service.UserServiceImpl;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class UserControllerTest {
    @Autowired
    private UserController userController;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private AppUserProfileRepository appUserProfileRepository;

    /**
     * Utilities
     */

    public static CreateUserRequestDto getCreateUserRequestDto(String username) {
        return new CreateUserRequestDto(
                username,
                "testPassword"
        );
    }

    /**
     * Dependencies
     */

    @Test
    void shouldInjectDependenciesProperly() {
        assertNotNull(userController, "UserController should not be null");
        assertNotNull(userController.getUserService(), "UserService should not be null");
        assertNotNull(appUserRepository, "AppUserRepository should not be null");

        assertInstanceOf(UserServiceImpl.class, userController.getUserService(),
                "UserService should be of type UserServiceImpl");
        assertInstanceOf(AppUserRepository.class, appUserRepository,
                "AppUserRepository should be of type AppUserRepository");
    }

    /**
     * User creation
     */

    @Test
    void shouldCreateUserWhenUsernameIsNotTakenAndPasswordIsCorrect() {
        val createUserRequestDto = getCreateUserRequestDto("testUser");

        val response = userController.createUser(createUserRequestDto);

        assertTrue(appUserRepository.findByUsername("testUser").isPresent(),
                "User should be created and found in the repository");
        assertNotNull(response, "Response should not be null");

        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode(),
                "Response status code should be 200");
        assertEquals("testUser", response.getBody().username(),
                "Response body should contain the created username");
    }

    @Test
    void shouldCreateUserProfileWhenCreatingValidUser() {
        val createUserRequestDto = getCreateUserRequestDto("testUser2");

        val response = userController.createUser(createUserRequestDto);

        val userProfile =
                this.appUserProfileRepository.findAppUserProfileByAppUser_Username(response.getBody().username())
                        .orElseThrow();

        assertNotNull(userProfile, "User profile should not be null");
        assertEquals(createUserRequestDto.username(), userProfile.getAvatarUrl(), "Avatar URL should be equal to " +
                "username");
        assertEquals(this.appUserRepository.findByUsername(createUserRequestDto.username()).get(),
                userProfile.getAppUser(), "AppUser should be the same as the one in profile");
    }

    @Test
    void shouldThrowAppExceptionCHR0001WhenCreatingUserWithTakenUsername() {
        val createUserRequestDto = getCreateUserRequestDto("testUser3");

        userController.createUser(createUserRequestDto);

        AppException thrownException = assertThrows(AppException.class,
                () -> userController.createUser(createUserRequestDto),
                "Should throw AppException with error code CHR0001");

        assertEquals(ErrorCode.CHR0001.getMessage(), thrownException.getMessage());
    }

    /**
     * Get user details
     */

    @Test
    void shouldReadUserDataWhenUserExistsInDatabase() {
        val createUserRequestDto = getCreateUserRequestDto("testUser4");
        userController.createUser(createUserRequestDto);

        val response = userController.getUserDetails(createUserRequestDto.username());

        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode(),
                "Response status code should be 200");

        assertEquals("testUser4", response.getBody().username(),
                "Response body should contain the created username");
        assertEquals("testUser4", response.getBody().avatarUrl(),
                "Response body should contain the created avatar URL");
        assertEquals("", response.getBody().bio(),
                "Response body should contain the created bio");

    }

    @Test
    void shouldThrowAppExceptionCHR0002WhenReadingUserDataWithInvalidUsername() {
        val createUserRequestDto = getCreateUserRequestDto("testUser5");
        userController.createUser(createUserRequestDto);

        AppException thrownException = assertThrows(AppException.class,
                () -> userController.getUserDetails("invalidUsername"),
                "Should throw AppException with error code CHR0002");

        assertEquals(ErrorCode.CHR0002.getMessage() + ": User with username invalidUsername not found",
                thrownException.getMessage());
    }

    /**
     * Update user bio
     */

    @Test
    void shouldUpdateUserBioWhenUserExistsInDatabase() {
        val createUserRequestDto = getCreateUserRequestDto("testUser6");
        userController.createUser(createUserRequestDto);

        val updateUserRequestDto = new UpdateUserRequestDto(
                "test bio"
        );
        val response = userController.updateUserDetails("testUser6", updateUserRequestDto);

        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode(),
                "Response status code should be 200");

        assertEquals("testUser6", response.getBody().username(),
                "Response body should contain the created username");
        assertEquals("testUser6", response.getBody().avatarUrl(),
                "Response body should contain the created avatar URL");
        assertEquals("test bio", response.getBody().bio(),
                "Response body should contain the created bio");
    }

    @Test
    void shouldThrowAppExceptionCHR0002WhenUpdatingUserBioWithInvalidUsername() {
        val createUserRequestDto = getCreateUserRequestDto("testUser7");
        userController.createUser(createUserRequestDto);

        val updateUserRequestDto = new UpdateUserRequestDto(
                "test bio"
        );

        AppException thrownException = assertThrows(AppException.class,
                () -> userController.updateUserDetails("invalidUsername", updateUserRequestDto),
                "Should throw AppException with error code CHR0002");

        assertEquals(ErrorCode.CHR0002.getMessage() + ": User with username invalidUsername not found",
                thrownException.getMessage());
    }

    @Test
    void shouldThrowAppExceptionCHR0003WhenUpdatingUserBioWithNullBio() {
        val createUserRequestDto = getCreateUserRequestDto("testUser8");
        userController.createUser(createUserRequestDto);

        val updateUserRequestDto = new UpdateUserRequestDto(
                null
        );

        AppException thrownException = assertThrows(AppException.class,
                () -> userController.updateUserDetails("testUser8", updateUserRequestDto),
                "Should throw AppException with error code CHR0003");

        assertEquals(ErrorCode.CHR0003.getMessage(), thrownException.getMessage());
    }
}