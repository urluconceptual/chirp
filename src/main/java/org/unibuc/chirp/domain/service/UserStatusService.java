package org.unibuc.chirp.domain.service;

import org.unibuc.chirp.domain.entity.UserStatusEntity;

public interface UserStatusService {
    void updateUserStatus(String username, UserStatusEntity.StatusType statusType);
}
