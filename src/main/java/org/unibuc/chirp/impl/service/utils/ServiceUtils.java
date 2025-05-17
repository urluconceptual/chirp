package org.unibuc.chirp.impl.service.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.unibuc.chirp.domain.dto.user.create.CreateUserResponseDto;
import org.unibuc.chirp.domain.dto.user.get.GetUserDetailsResponseDto;
import org.unibuc.chirp.domain.dto.user.update.UpdateUserResponseDto;
import org.unibuc.chirp.domain.entity.AppUser;
import org.unibuc.chirp.domain.entity.AppUserProfile;

@Slf4j
@UtilityClass
public class ServiceUtils {
    public CreateUserResponseDto toCreateUserResponseDto(AppUser appUser) {
        return new CreateUserResponseDto(
                appUser.getUsername()
        );
    }

    public GetUserDetailsResponseDto toGetUserDetailsResponseDto(AppUser appUser) {
        return new GetUserDetailsResponseDto(
                appUser.getUsername(),
                appUser.getAppUserProfile().getAvatarUrl(),
                appUser.getAppUserProfile().getBio()
        );
    }

    public UpdateUserResponseDto toDto(AppUserProfile userProfile) {
        return new UpdateUserResponseDto(
                userProfile.getAppUser().getUsername(),
                userProfile.getAvatarUrl(),
                userProfile.getBio()
        );
    }
}
