package org.unibuc.chirp.domain.entity;

import jakarta.persistence.*;
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
    @Column(length = 100000)
    private String profilePicture;
    private String bio;
    private LocalDate birthday;
    private String location;
    private String website;

    @OneToOne
    @MapsId
    private UserEntity user;
}
