package org.unibuc.chirp.application.rest;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.test.context.ActiveProfiles;
import org.unibuc.chirp.domain.dto.user.create.CreateUserRequestDto;
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

    @Test
    void shouldInjectDependenciesProperly() {
        assertNotNull(userController, "UserController should not be null");
        assertNotNull(userController.getUserService(), "UserService should not be null");

        assertInstanceOf(UserServiceImpl.class, userController.getUserService(),
                "UserService should be of type UserServiceImpl");
    }

    @Test
    void shouldCreateUserWhenUsernameIsNotTakenAndPasswordIsCorrect() {
        val createUserRequestDto = new CreateUserRequestDto(
                "testUser",
                "testPassword"
        );

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
        val createUserRequestDto = new CreateUserRequestDto(
                "testUser2",
                "testPassword"
        );

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

}