package com.rev.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "playlist_song")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaylistSong {

    @Id
    @SequenceGenerator(name = "playlist_song_seq", sequenceName = "PLAYLIST_SONG_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "playlist_song_seq")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "playlist_id", nullable = false)
    private Playlist playlist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "song_id", nullable = false)
    private Songs song;

    @Column(nullable = false)
    private int orderIndex; // for song ordering in playlist
}