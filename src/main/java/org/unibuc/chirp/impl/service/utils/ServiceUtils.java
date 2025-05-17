package org.unibuc.chirp.impl.service.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.unibuc.chirp.domain.dto.user.create.CreateUserResponseDto;
import org.unibuc.chirp.domain.entity.AppUser;

@Slf4j
@UtilityClass
public class ServiceUtils {
    public CreateUserResponseDto toDto(AppUser appUser) {
        return new CreateUserResponseDto(
                appUser.getUsername()
        );
    }

}
