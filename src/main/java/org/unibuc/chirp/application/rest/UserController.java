package org.unibuc.chirp.application.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.unibuc.chirp.domain.dto.user.create.CreateUserRequestDto;
import org.unibuc.chirp.domain.dto.user.create.CreateUserResponseDto;
import org.unibuc.chirp.domain.dto.user.get.GetUserDetailsResponseDto;
import org.unibuc.chirp.domain.service.UserService;

@RestController
@RequestMapping("/chirp/api/v1/user")
@AllArgsConstructor
@Getter
public class UserController {
    private UserService userService;

    @PostMapping("/new")
    ResponseEntity<CreateUserResponseDto> createUser(CreateUserRequestDto createUserRequestDto) {
        return ResponseEntity.ok(
            this.userService.createUser(createUserRequestDto)
        );
    }

    @GetMapping("/{username}")
    ResponseEntity<GetUserDetailsResponseDto> getUserDetails(@PathVariable String username) {
        return ResponseEntity.ok(
            this.userService.getUserDetails(username)
        );
    }
}
