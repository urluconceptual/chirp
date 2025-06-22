package org.unibuc.chirpcore.domain.service;

import org.unibuc.chirpcore.domain.entity.UserStatusEntity;

public interface UserStatusService {
    void updateUserStatus(String username, UserStatusEntity.StatusType statusType);
}
