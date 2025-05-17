package org.unibuc.chirp.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;

    @OneToMany(mappedBy = "sender")
    @Builder.Default
    private List<Message> messageList = new ArrayList<>();

    @ManyToMany(mappedBy = "participants")
    @Builder.Default
    private List<Conversation> conversationList = new ArrayList<>();

    @OneToOne(mappedBy = "appUser", cascade = CascadeType.ALL)
    private AppUserProfile appUserProfile;


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AppUser appUser = (AppUser) o;
        return Objects.equals(getId(), appUser.getId()) &&
                Objects.equals(getUsername(), appUser.getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUsername());
    }
}
