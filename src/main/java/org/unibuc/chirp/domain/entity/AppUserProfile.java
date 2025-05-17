package org.unibuc.chirp.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppUserProfile {
    @Id
    private Long id;

    private String avatarUrl;

    @Builder.Default
    private String bio = "";

    @OneToOne
    @MapsId
    private AppUser appUser;
}
