package org.unibuc.chirp.impl.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.unibuc.chirp.domain.dto.user.create.CreateUserRequestDto;
import org.unibuc.chirp.domain.entity.AppUser;
import org.unibuc.chirp.domain.repository.AppUserRepository;
import org.unibuc.chirp.domain.service.UserService;
import org.unibuc.chirp.impl.service.utils.ServiceUtils;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private AppUserRepository userRepository;

    @Override
    public Boolean createUser(CreateUserRequestDto createUserRequestDto) {
        return ServiceUtils.actionExecutedSuccessfully(() -> this.userRepository
            .save(AppUser
                .builder()
                .username(createUserRequestDto.username())
                .password(createUserRequestDto.password())
                .build())
        );
    }
}
