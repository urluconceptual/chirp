package org.unibuc.chirp.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import lombok.*;

import java.time.LocalDate;

@Entity(name = "user_profiles")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileEntity {
    @Id
    private Long id;

    @Builder.Default
    private String avatarUrl = "";

    @Builder.Default
    private String bio = "";

    private LocalDate birthday;

    @Builder.Default
    private String location = "";

    @Builder.Default
    private String website = "";

    @OneToOne
    @MapsId
    private UserEntity user;
}
