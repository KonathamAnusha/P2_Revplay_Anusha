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
class FavoriteRepositoryTest {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private TestEntityManager entityManager;

    private UserAccount user;
    private Songs song;
    private ArtistProfile artist;

    @BeforeEach
    void setUp() {
        user = UserAccount.builder()
                .fullName("Test User")
                .email("test@example.com")
                .passwordHash("hash")
                .role(UserAccount.Role.LISTENER)
                .build();
        entityManager.persist(user);

        UserAccount artistUser = UserAccount.builder()
                .fullName("Artist User")
                .email("artist@example.com")
                .passwordHash("hash")
                .role(UserAccount.Role.ARTIST)
                .build();
        entityManager.persist(artistUser);

        artist = ArtistProfile.builder()
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
        entityManager.persist(song); // Need to use persist or persistAndFlush depending on relations

        Favorite favorite = Favorite.builder()
                .user(user)
                .song(song)
                .build();
        entityManager.persist(favorite);
        entityManager.flush();
    }

    @Test
    void findByUser_UserId_ReturnsFavorites() {
        List<Favorite> found = favoriteRepository.findByUser_UserId(user.getUserId());
        assertThat(found).hasSize(1);
    }

    @Test
    void existsByUser_UserIdAndSong_SongId_ReturnsTrue() {
        boolean exists = favoriteRepository.existsByUser_UserIdAndSong_SongId(user.getUserId(), song.getSongId());
        assertThat(exists).isTrue();
    }

    @Test
    void countFavoritesByArtist_ReturnsCount() {
        long count = favoriteRepository.countFavoritesByArtist(artist.getArtistId());
        assertThat(count).isEqualTo(1);
    }
}
