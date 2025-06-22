package org.unibuc.chirpchat.domain.service;

import org.unibuc.chirpchat.domain.entity.UserStatusEntity;

public interface UserStatusService {
    void updateUserStatus(String username, UserStatusEntity.StatusType statusType);
}
