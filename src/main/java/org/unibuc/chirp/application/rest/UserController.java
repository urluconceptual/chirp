package org.unibuc.chirp.application.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.unibuc.chirp.domain.dto.user.create.CreateUserRequestDto;
import org.unibuc.chirp.domain.dto.user.create.CreateUserResponseDto;
import org.unibuc.chirp.domain.service.UserService;

@RestController
@RequestMapping("/chirp/api/v1/user")
@AllArgsConstructor
@Getter
public class UserController {
    private UserService userService;

    @PostMapping("/new")
    ResponseEntity<CreateUserResponseDto> createUser(CreateUserRequestDto createUserRequestDto) {
        val newUser = userService.createUser(createUserRequestDto);

        return ResponseEntity.ok(
            new CreateUserResponseDto(newUser.username())
        );
    }
}
