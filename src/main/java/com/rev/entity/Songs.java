package com.rev.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "songs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Songs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long songId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String genre;

    @Column(nullable = false)
    private int duration; // duration in seconds

    @Column(nullable = false)
    private String songUrl; // real URL to play

    @Column(nullable = false)
    private String language; // e.g., English, Hindi, Telugu

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id", nullable = false)
    private ArtistProfile artist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "album_id")
    private Album album;

    @Column(nullable = false)
    private Boolean isPublic = true;

    @Column(nullable = false)
    private Long playCount = 0L;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}