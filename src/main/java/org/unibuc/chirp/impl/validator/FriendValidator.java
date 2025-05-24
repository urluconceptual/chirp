package org.unibuc.chirp.impl.validator;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.unibuc.chirp.domain.entity.UserFriendshipEntity;

@Component
@AllArgsConstructor
public class FriendValidator {

    public void validateAccept(UserFriendshipEntity friendshipRequest) {
        if (friendshipRequest.getStatus() != UserFriendshipEntity.FriendshipStatus.PENDING) {
            throw new IllegalArgumentException("Friendship request is not in PENDING status");
        }

        if (friendshipRequest.getAddressee() == null || friendshipRequest.getRequester() == null) {
            throw new IllegalArgumentException("Friendship request must have both requester and addressee set");
        }
    }

    public void validateReject(UserFriendshipEntity friendshipRequest) {
        if (friendshipRequest.getStatus() != UserFriendshipEntity.FriendshipStatus.PENDING) {
            throw new IllegalArgumentException("Friendship request is not in PENDING status");
        }

        if (friendshipRequest.getAddressee() == null || friendshipRequest.getRequester() == null) {
            throw new IllegalArgumentException("Friendship request must have both requester and addressee set");
        }
    }
}
