package org.unibuc.chirp.application.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.unibuc.chirp.domain.service.UserService;

@RestController
@RequestMapping("/chirp/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

}
