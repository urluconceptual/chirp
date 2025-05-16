package org.unibuc.chirp.application.rest;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.test.context.ActiveProfiles;
import org.unibuc.chirp.domain.dto.user.create.CreateUserRequestDto;
import org.unibuc.chirp.domain.repository.AppUserRepository;
import org.unibuc.chirp.impl.service.UserServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
class UserControllerTest {
    @Autowired
    private UserController userController;

    @Autowired
    private AppUserRepository appUserRepository;

    @Test
    void shouldInjectDependenciesProperly() {
        assertNotNull(userController, "UserController should not be null");
        assertNotNull(userController.getUserService(), "UserService should not be null");

        assertEquals( UserServiceImpl.class, userController.getUserService().getClass(),
                "UserService should be of type UserServiceImpl");
    }

    @Test
    void shouldCreateUserWhenUsernameIsNotTakenAndPasswordIsCorrect() {
        val createUserRequestDto = new CreateUserRequestDto(
                "testUser",
                "testPassword"
        );

        val response = userController.createUser(createUserRequestDto);

        assertNotNull(appUserRepository.findByUsername("testUser"),
                "User should be created and found in the repository");
        assertNotNull(response, "Response should not be null");

        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode(),
                "Response status code should be 200");
        assertEquals("testUser", response.getBody().username(),
                "Response body should contain the created username");
    }

}