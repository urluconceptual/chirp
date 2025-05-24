package org.unibuc.chirp.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    @Builder.Default
    private Set<RoleEntity> roles = new HashSet<>();

    @OneToMany(mappedBy = "sender")
    @Builder.Default
    private List<MessageEntity> messageList = new ArrayList<>();

    @ManyToMany(mappedBy = "participants")
    @Builder.Default
    private List<ConversationEntity> conversationList = new ArrayList<>();

    @OneToOne(mappedBy = "user")
    private UserProfileEntity userProfile;

    @OneToMany(mappedBy = "requester")
    @Builder.Default
    private List<UserFriendshipEntity> sentFriendRequests = new ArrayList<>();

    @OneToMany(mappedBy = "addressee")
    @Builder.Default
    private List<UserFriendshipEntity> receivedFriendRequests = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity userEntity = (UserEntity) o;
        return Objects.equals(getId(), userEntity.getId()) &&
                Objects.equals(getUsername(), userEntity.getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUsername());
    }
}
