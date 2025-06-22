package org.unibuc.chirpcore.impl.validator;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.unibuc.chirpcore.domain.entity.UserFriendshipEntity;
import org.unibuc.chirpcore.domain.exception.AppException;
import org.unibuc.chirpcore.domain.exception.ErrorCode;

@Component
@AllArgsConstructor
public class FriendValidator {

    public void validateAccept(UserFriendshipEntity friendshipRequest) {
        if (friendshipRequest.getStatus() != UserFriendshipEntity.FriendshipStatus.PENDING) {
            throw new AppException(ErrorCode.CHR0010);
        }

        if (friendshipRequest.getAddressee() == null || friendshipRequest.getRequester() == null) {
            throw new AppException(ErrorCode.CHR0011);
        }
    }

    public void validateReject(UserFriendshipEntity friendshipRequest) {
        if (friendshipRequest.getStatus() != UserFriendshipEntity.FriendshipStatus.PENDING) {
            throw new AppException(ErrorCode.CHR0010);
        }

        if (friendshipRequest.getAddressee() == null || friendshipRequest.getRequester() == null) {
            throw new AppException(ErrorCode.CHR0011);
        }
    }

    public void validateRemove(UserFriendshipEntity friendship) {
        if (friendship.getStatus() != UserFriendshipEntity.FriendshipStatus.ACCEPTED) {
            throw new AppException(ErrorCode.CHR0012);
        }

        if (friendship.getAddressee() == null || friendship.getRequester() == null) {
            throw new AppException(ErrorCode.CHR0011);
        }
    }
}
