package com.rev.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users_account")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAccount {

    @Id
    @SequenceGenerator(
            name = "user_seq",
            sequenceName = "USER_SEQ",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "user_seq")
    @Column(name = "user_id", updatable = false, nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

//    @Builder.Default
//    @Column(nullable = false)
//    private String role = "LISTENER";

    @Column
    private String displayName;

    @Column(length = 1000)
    private String bio;

    @Column
    private String profilePicture;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Builder.Default
    @Column(nullable = false)
    private String status = "ACTIVE";

    @OneToOne(mappedBy = "userAccount",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    @JsonManagedReference
    private ArtistProfile artistProfile;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public void setArtistProfile(ArtistProfile artistProfile) {
        this.artistProfile = artistProfile;
        if (artistProfile != null) {
            artistProfile.setUserAccount(this);
        }
    }

    public enum Role {
        LISTENER,
        ARTIST
    }

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;



}