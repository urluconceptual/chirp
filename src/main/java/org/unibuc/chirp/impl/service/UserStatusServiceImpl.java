package org.unibuc.chirp.impl.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.unibuc.chirp.domain.entity.UserEntity;
import org.unibuc.chirp.domain.entity.UserStatusEntity;
import org.unibuc.chirp.domain.repository.UserRepository;
import org.unibuc.chirp.domain.repository.UserStatusRepository;
import org.unibuc.chirp.domain.service.UserStatusService;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class UserStatusServiceImpl implements UserStatusService {
    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;

    @Override
    public void updateUserStatus(String username, UserStatusEntity.StatusType statusType) {
        if (userStatusRepository.findByUser_Username(username) == null) {
            UserStatusEntity newUserStatus = new UserStatusEntity();
            UserEntity user = userRepository.findByUsername(username).get();
            newUserStatus.setUser(user);
            newUserStatus.setStatus(statusType);
            newUserStatus.setLastUpdated(LocalDateTime.now());
            userStatusRepository.save(newUserStatus);
            return;
        }

        UserStatusEntity userStatus = userStatusRepository.findByUser_Username(username);
        if (userStatus != null) {
            userStatus.setStatus(statusType);
            userStatus.setLastUpdated(LocalDateTime.now());
            userStatusRepository.save(userStatus);
        }
    }
}
