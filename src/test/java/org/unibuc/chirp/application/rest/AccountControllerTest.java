//package org.unibuc.chirp.application.rest;
//
//import lombok.val;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.NullSource;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.HttpStatusCode;
//import org.springframework.test.context.ActiveProfiles;
//import org.unibuc.chirp.application.AccountController;
//import org.unibuc.chirp.domain.dto.user.create.CreateUserRequestDto;
//import org.unibuc.chirp.domain.dto.user.update.UpdateUserRequestDto;
//import org.unibuc.chirp.domain.exception.AppException;
//import org.unibuc.chirp.domain.exception.ErrorCode;
//import org.unibuc.chirp.domain.repository.UserProfileRepository;
//import org.unibuc.chirp.domain.repository.UserRepository;
//import org.unibuc.chirp.impl.service.UserServiceImpl;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@ActiveProfiles("test")
//@DisplayName("UserController Tests")
//class AccountControllerTest {
//    @Autowired
//    private AccountController accountController;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private UserProfileRepository userProfileRepository;
//
//    public static CreateUserRequestDto getCreateUserRequestDto(String username) {
//        return new CreateUserRequestDto(
//                username,
//                "testPassword"
//        );
//    }
//
//    @BeforeEach
//    void setUp() {
//        userProfileRepository.deleteAll();
//        userRepository.deleteAll();
//    }
//
//    @Nested
//    @DisplayName("Dependency Injection Tests")
//    class DependencyInjectionTests {
//        @Test
//        @DisplayName("Should properly inject all dependencies")
//        void shouldInjectDependenciesProperly() {
//            assertAll(
//                    () -> assertNotNull(accountController, "UserController should not be null"),
//                    () -> assertNotNull(accountController.getUserService(), "UserService should not be null"),
//                    () -> assertNotNull(userRepository, "AppUserRepository should not be null"),
//                    () -> assertInstanceOf(UserServiceImpl.class, accountController.getUserService(),
//                            "UserService should be of type UserServiceImpl"),
//                    () -> assertInstanceOf(UserRepository.class, userRepository,
//                            "AppUserRepository should be of type AppUserRepository")
//            );
//        }
//    }
//
//    @Nested
//    @DisplayName("User Creation Tests")
//    class UserCreationTests {
//        @Test
//        @DisplayName("Should successfully create user with valid credentials")
//        void shouldCreateUserSuccessfully() {
//            val createUserRequestDto = getCreateUserRequestDto("testUser");
//            val response = accountController.createUser(createUserRequestDto);
//
//            assertAll(
//                    () -> assertTrue(userRepository.findByUsername("testUser").isPresent(),
//                            "User should be created and found in the repository"),
//                    () -> assertNotNull(response, "Response should not be null"),
//                    () -> assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode(),
//                            "Response status code should be 200"),
//                    () -> assertEquals("testUser", response.getBody().username(),
//                            "Response body should contain the created username")
//            );
//        }
//
//        @Test
//        @DisplayName("Should create user profile when creating valid user")
//        void shouldCreateUserProfile() {
//            val createUserRequestDto = getCreateUserRequestDto("testUser2");
//            val response = accountController.createUser(createUserRequestDto);
//            val userProfile = userProfileRepository
//                    .findUserProfileEntityByUser_Username(response.getBody().username())
//                    .orElseThrow();
//
//            assertAll(
//                    () -> assertNotNull(userProfile, "User profile should not be null"),
//                    () -> assertEquals(createUserRequestDto.username(), userProfile.getProfilePicture(),
//                            "Avatar URL should be equal to username"),
//                    () -> assertEquals(userRepository.findByUsername(createUserRequestDto.username()).get(),
//                            userProfile.getUserEntity(), "AppUser should be the same as the one in profile")
//            );
//        }
//
//        @Test
//        @DisplayName("Should throw CHR0001 when username is taken")
//        void shouldThrowExceptionForDuplicateUsername() {
//            val createUserRequestDto = getCreateUserRequestDto("testUser3");
//            accountController.createUser(createUserRequestDto);
//
//            val thrownException = assertThrows(AppException.class,
//                    () -> accountController.createUser(createUserRequestDto),
//                    "Should throw AppException with error code CHR0001");
//
//            assertEquals(ErrorCode.CHR0001.getMessage(), thrownException.getMessage());
//        }
//    }
//
//    @Nested
//    @DisplayName("User Details Tests")
//    class UserDetailsTests {
//        private static final String TEST_USERNAME = "testUser4";
//
//        @BeforeEach
//        void setUp() {
//            accountController.createUser(getCreateUserRequestDto(TEST_USERNAME));
//        }
//
//        @Test
//        @DisplayName("Should successfully read existing user data")
//        void shouldReadUserData() {
//            val response = accountController.getUserDetails(TEST_USERNAME);
//
//            assertAll(
//                    () -> assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode(),
//                            "Response status code should be 200"),
//                    () -> assertEquals(TEST_USERNAME, response.getBody().username(),
//                            "Response body should contain the created username"),
//                    () -> assertEquals(TEST_USERNAME, response.getBody().profilePicture(),
//                            "Response body should contain the created avatar URL"),
//                    () -> assertEquals("", response.getBody().bio(),
//                            "Response body should contain the created bio")
//            );
//        }
//
//        @Test
//        @DisplayName("Should throw CHR0002 for non-existent username")
//        void shouldThrowExceptionForInvalidUsername() {
//            val invalidUsername = "invalidUsername";
//            val thrownException = assertThrows(AppException.class,
//                    () -> accountController.getUserDetails(invalidUsername),
//                    "Should throw AppException with error code CHR0002");
//
//            assertEquals(ErrorCode.CHR0002.getMessage() + ": User with username invalidUsername not found",
//                    thrownException.getMessage());
//        }
//    }
//
//    @Nested
//    @DisplayName("User Update Tests")
//    class UserUpdateTests {
//        private static final String TEST_USERNAME = "testUser6";
//
//        @BeforeEach
//        void setUp() {
//            accountController.createUser(getCreateUserRequestDto(TEST_USERNAME));
//        }
//
//        @Test
//        @DisplayName("Should successfully update user bio")
//        void shouldUpdateUserBio() {
//            val updateUserRequestDto = new UpdateUserRequestDto("test bio");
//            val response = accountController.updateUserDetails(TEST_USERNAME, updateUserRequestDto);
//
//            assertAll(
//                    () -> assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode(),
//                            "Response status code should be 200"),
//                    () -> assertEquals(TEST_USERNAME, response.getBody().username(),
//                            "Response body should contain the created username"),
//                    () -> assertEquals(TEST_USERNAME, response.getBody().avatarUrl(),
//                            "Response body should contain the created avatar URL"),
//                    () -> assertEquals("test bio", response.getBody().bio(),
//                            "Response body should contain the updated bio")
//            );
//        }
//
//        @ParameterizedTest
//        @NullSource
//        @DisplayName("Should throw CHR0003 when bio is null or empty")
//        void shouldThrowExceptionForInvalidBio(String invalidBio) {
//            val updateUserRequestDto = new UpdateUserRequestDto(invalidBio);
//
//            val thrownException = assertThrows(AppException.class,
//                    () -> accountController.updateUserDetails(TEST_USERNAME, updateUserRequestDto),
//                    "Should throw AppException with error code CHR0003");
//
//            assertEquals(ErrorCode.CHR0003.getMessage(), thrownException.getMessage());
//        }
//
//        @Test
//        @DisplayName("Should throw CHR0002 when updating non-existent user")
//        void shouldThrowExceptionForNonExistentUser() {
//            val updateUserRequestDto = new UpdateUserRequestDto("test bio");
//            val invalidUsername = "invalidUsername";
//
//            val thrownException = assertThrows(AppException.class,
//                    () -> accountController.updateUserDetails(invalidUsername, updateUserRequestDto),
//                    "Should throw AppException with error code CHR0002");
//
//            assertEquals(ErrorCode.CHR0002.getMessage() + ": User with username invalidUsername not found",
//                    thrownException.getMessage());
//        }
//    }
//}