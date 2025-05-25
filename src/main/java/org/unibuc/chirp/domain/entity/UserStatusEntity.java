package org.unibuc.chirp.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity(name = "user_statuses")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserStatusEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    private StatusType status;

    private LocalDateTime lastUpdated;

    public enum StatusType {
        ONLINE,
        OFFLINE,
    }
}