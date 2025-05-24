package org.unibuc.chirp.impl.service.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.unibuc.chirp.domain.dto.conversation.create.CreateConversationResponseDto;
import org.unibuc.chirp.domain.dto.conversation.get.GetConversationResponseDto;
import org.unibuc.chirp.domain.dto.message.get.GetMessageResponseDto;
import org.unibuc.chirp.domain.dto.user.create.CreateUserResponseDto;
import org.unibuc.chirp.domain.dto.user.get.GetUserDetailsResponseDto;
import org.unibuc.chirp.domain.dto.user.update.UpdateUserResponseDto;
import org.unibuc.chirp.domain.entity.AppUser;
import org.unibuc.chirp.domain.entity.AppUserProfile;
import org.unibuc.chirp.domain.entity.Conversation;
import org.unibuc.chirp.domain.entity.Message;

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

    public static CreateConversationResponseDto toDto(Conversation conversation) {
        return new CreateConversationResponseDto(
                conversation.getId(),
                conversation.getTitle()
        );
    }

    public static GetConversationResponseDto toDtoGetConversation(Conversation conversation, Page<Message> messagePage) {
        return new GetConversationResponseDto(
                conversation.getId(),
                conversation.getTitle(),
                conversation.getParticipants().stream()
                        .map(AppUser::getUsername)
                        .toList(),
                messagePage.getContent().stream()
                        .map(ServiceUtils::toDto)
                        .toList()
        );
    }

    public static GetMessageResponseDto toDto(Message message) {
        return new GetMessageResponseDto(
                message.getId(),
                message.getContent(),
                message.getSender().getUsername(),
                message.getTimestamp().toString()
        );
    }
}
