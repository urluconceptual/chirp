package org.unibuc.chirp.impl.mapper;

import lombok.experimental.UtilityClass;
import org.unibuc.chirp.domain.entity.UserEntity;
import org.unibuc.chirp.domain.entity.UserFriendshipEntity;

import java.time.LocalDateTime;

@UtilityClass
public class FriendMapper {
    public UserFriendshipEntity toUserFriendshipEntity(UserEntity requester, UserEntity addressee,
                                                       UserFriendshipEntity.FriendshipStatus status) {
        return UserFriendshipEntity.builder()
                .requestedAt(LocalDateTime.now())
                .status(status)
                .addressee(addressee)
                .requester(requester)
                .build();
    }

}
