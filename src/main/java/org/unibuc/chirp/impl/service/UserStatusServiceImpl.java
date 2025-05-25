package org.unibuc.chirp.impl.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;
import org.unibuc.chirp.domain.entity.UserStatusEntity;
import org.unibuc.chirp.domain.repository.UserRepository;
import org.unibuc.chirp.domain.repository.UserStatusRepository;
import org.unibuc.chirp.domain.service.UserStatusService;
import org.unibuc.chirp.impl.mapper.UserMapper;

import java.time.LocalDateTime;

@Slf4j
@Service
@AllArgsConstructor
public class UserStatusServiceImpl implements UserStatusService {
    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;

    @Override
    public void updateUserStatus(String username, UserStatusEntity.StatusType statusType) {
        log.info("Updating user status for {}", username);
        boolean userHasStatus = userStatusRepository.findByUser_Username(username).isPresent();

        if (!userHasStatus) {
            addNewUserStatus(username, statusType);
            return;
        }

        UserStatusEntity userStatus = userStatusRepository.findByUser_Username(username).orElseThrow();
        updateExistingUserStatus(userStatus, statusType);
    }

    private void updateExistingUserStatus(UserStatusEntity userStatus, UserStatusEntity.StatusType statusType) {
        userStatus.setStatus(statusType);
        userStatus.setLastUpdated(LocalDateTime.now());
        userStatusRepository.save(userStatus);
    }

    private void addNewUserStatus(String username, UserStatusEntity.StatusType statusType) {
        val user = userRepository.findByUsername(username).orElseThrow();

        UserStatusEntity userStatus = UserMapper.toUserStatusEntity(user, statusType);

        userStatusRepository.save(userStatus);
    }
}
