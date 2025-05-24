package org.unibuc.chirp.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class UserFriend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "requester_id")
    private AppUser requester;

    @ManyToOne
    @JoinColumn(name = "addressee_id")
    private AppUser addressee;

    @Enumerated(EnumType.STRING)
    private FriendshipStatus status;

    private LocalDateTime requestedAt;
    private LocalDateTime respondedAt;

    public enum FriendshipStatus {
        PENDING,
        ACCEPTED,
        REJECTED
    }
}
