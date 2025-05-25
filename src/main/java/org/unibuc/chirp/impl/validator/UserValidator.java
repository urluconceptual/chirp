package org.unibuc.chirp.impl.validator;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.unibuc.chirp.domain.dto.user.create.CreateUserRequestDto;
import org.unibuc.chirp.domain.exception.AppException;
import org.unibuc.chirp.domain.exception.ErrorCode;
import org.unibuc.chirp.domain.repository.UserRepository;

import java.util.List;

@Component
@AllArgsConstructor
public class UserValidator {
    private UserRepository userRepository;

    public void validate(CreateUserRequestDto createUserRequestDto) {
        if (this.userRepository.findByUsername(createUserRequestDto.username()).isPresent()) {
            throw new AppException(ErrorCode.CHR0001);
        }
    }

    public void validate(String username) {
        if (this.userRepository.findByUsername(username).isEmpty()) {
            throw new AppException(ErrorCode.CHR0002, "User with username " + username + " not found");
        }
    }

    public void validate(List<String> usernameList) {
        for (String username : usernameList) {
            validate(username);
        }
    }
}
