package com.rev.repository;

import com.rev.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PlaylistSongRepositoryTest {

    @Autowired
    private PlaylistSongRepository playlistSongRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Playlist playlist;
    private Songs song;

    @BeforeEach
    void setUp() {
        UserAccount user = UserAccount.builder()
                .fullName("Test User")
                .email("test@example.com")
                .passwordHash("hash")
                .role(UserAccount.Role.LISTENER)
                .build();
        entityManager.persist(user);

        playlist = Playlist.builder()
                .name("My Playlist")
                .privacy("PUBLIC")
                .user(user)
                .build();
        entityManager.persist(playlist);

        UserAccount artistUser = UserAccount.builder()
                .fullName("Artist User")
                .email("artist@example.com")
                .passwordHash("hash")
                .role(UserAccount.Role.ARTIST)
                .build();
        entityManager.persist(artistUser);

        ArtistProfile artist = ArtistProfile.builder()
                .stageName("Test Artist")
                .genre("Pop")
                .bannerImage("banner.jpg")
                .userAccount(artistUser)
                .build();
        entityManager.persist(artist);

        song = Songs.builder()
                .title("Test Song")
                .duration(180)
                .songUrl("url")
                .language("English")
                .artist(artist)
                .build();
        entityManager.persist(song);

        PlaylistSong ps = PlaylistSong.builder()
                .playlist(playlist)
                .song(song)
                .orderIndex(1)
                .build();
        entityManager.persist(ps);
        entityManager.flush();
    }

    @Test
    void findByPlaylist_PlaylistIdOrderByOrderIndex_ReturnsSongs() {
        List<PlaylistSong> found = playlistSongRepository
                .findByPlaylist_PlaylistIdOrderByOrderIndex(playlist.getPlaylistId());
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getOrderIndex()).isEqualTo(1);
    }

    @Test
    void findMaxOrderIndexByPlaylistId_ReturnsMax() {
        int max = playlistSongRepository.findMaxOrderIndexByPlaylistId(playlist.getPlaylistId());
        assertThat(max).isEqualTo(1);
    }

    @Test
    void existsByPlaylist_PlaylistIdAndSong_SongId_ReturnsTrue() {
        boolean exists = playlistSongRepository.existsByPlaylist_PlaylistIdAndSong_SongId(playlist.getPlaylistId(),
                song.getSongId());
        assertThat(exists).isTrue();
    }
}
