package org.unibuc.chirp.impl.validator;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.unibuc.chirp.domain.dto.user.create.CreateUserRequestDto;
import org.unibuc.chirp.domain.dto.user.login.LoginRequestDto;
import org.unibuc.chirp.domain.dto.user.update.UpdateUserRequestDto;
import org.unibuc.chirp.domain.entity.UserEntity;
import org.unibuc.chirp.domain.exception.AppException;
import org.unibuc.chirp.domain.exception.ErrorCode;
import org.unibuc.chirp.domain.repository.UserProfileRepository;
import org.unibuc.chirp.domain.repository.UserRepository;

import java.util.List;

@Component
@AllArgsConstructor
public class UserValidator {
    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private UserProfileRepository userProfileRepository;

    public void validate(CreateUserRequestDto createUserRequestDto) {
        if (this.userRepository.findByUsername(createUserRequestDto.username()).isPresent()) {
            throw new AppException(ErrorCode.CHR0001);
        }
    }

    public void validate(String username) {
        if (this.userRepository.findByUsername(username).isEmpty()) {
            throw new AppException(ErrorCode.CHR0002, "User with username " + username + " not found");
        } else if (this.userProfileRepository.findUserProfileEntityByUser_Username(username).isEmpty()) {
            throw new AppException(ErrorCode.CHR0004);
        }
    }

    public void validate(List<Long> userIdList) {
        for (Long userId : userIdList) {
            if (this.userRepository.findById(userId).isEmpty()) {
                throw new AppException(ErrorCode.CHR0002, "User with id " + userId + " not found");
            }
        }
    }

    public void validate(UpdateUserRequestDto updateUserRequestDto) {
        if (updateUserRequestDto.updatedBio() == null) {
            throw new AppException(ErrorCode.CHR0003);
        }
    }
}
