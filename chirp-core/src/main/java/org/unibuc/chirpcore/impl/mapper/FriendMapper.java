package org.unibuc.chirpcore.impl.mapper;

import lombok.experimental.UtilityClass;
import org.unibuc.chirpcore.domain.entity.UserEntity;
import org.unibuc.chirpcore.domain.entity.UserFriendshipEntity;

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
