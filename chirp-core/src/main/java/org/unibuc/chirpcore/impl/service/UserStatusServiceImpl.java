package org.unibuc.chirpcore.impl.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;
import org.unibuc.chirpcore.domain.entity.UserStatusEntity;
import org.unibuc.chirpcore.domain.repository.UserRepository;
import org.unibuc.chirpcore.domain.repository.UserStatusRepository;
import org.unibuc.chirpcore.domain.service.UserStatusService;
import org.unibuc.chirpcore.impl.mapper.UserMapper;
import org.unibuc.chirpcore.impl.validator.UserValidator;

import java.time.LocalDateTime;

@Slf4j
@Service
@AllArgsConstructor
@Getter
public class UserStatusServiceImpl implements UserStatusService {
    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;

    private final UserValidator userValidator;

    @Override
    public void updateUserStatus(String username, UserStatusEntity.StatusType statusType) {
        log.info("Updating user status for {}", username);
        userValidator.validate(username);

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
