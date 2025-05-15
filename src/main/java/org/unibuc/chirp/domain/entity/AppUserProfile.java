package org.unibuc.chirp.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class AppUserProfile {
    @Id
    private Long id;

    private String avatarUrl;
    private String bio;

    @OneToOne
    @MapsId
    private AppUser appUser;
}
