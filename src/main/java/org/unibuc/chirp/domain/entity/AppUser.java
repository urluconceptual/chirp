package org.unibuc.chirp.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    @OneToMany(mappedBy = "sender")
    private List<Message> messageList = new ArrayList<>();

    @ManyToMany(mappedBy = "participants")
    private List<Conversation> conversationList = new ArrayList<>();

    @OneToOne(mappedBy = "appUser", cascade = CascadeType.ALL)
    private AppUserProfile appUserProfile;
}
