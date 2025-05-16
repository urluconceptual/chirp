package org.unibuc.chirp.domain.service;

import org.unibuc.chirp.domain.dto.user.create.CreateUserRequestDto;

public interface UserService {
    Boolean createUser(CreateUserRequestDto createUserRequestDto);
}
