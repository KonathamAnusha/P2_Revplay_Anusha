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
class SongsRepositoryTest {

    @Autowired
    private SongsRepository songsRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Songs testSong;
    private ArtistProfile artist;
    private Genre genre;

    @BeforeEach
    void setUp() {
        UserAccount user = UserAccount.builder()
                .fullName("Artist User")
                .email("artist@example.com")
                .passwordHash("hash")
                .role(UserAccount.Role.ARTIST)
                .build();
        entityManager.persist(user);

        artist = ArtistProfile.builder()
                .stageName("Test Artist")
                .genre("Pop")
                .bannerImage("banner.jpg")
                .userAccount(user)
                .build();
        entityManager.persist(artist);

        genre = Genre.builder().name("Rock").build();
        entityManager.persist(genre);

        testSong = Songs.builder()
                .title("Test Song")
                .duration(180)
                .songUrl("http://test.com/song.mp3")
                .language("English")
                .artist(artist)
                .genre(genre)
                .isPublic(true)
                .build();
        entityManager.persist(testSong);
        entityManager.flush();
    }

    @Test
    void findByTitleContainingIgnoreCase_ReturnsSongs() {
        List<Songs> found = songsRepository.findByTitleContainingIgnoreCase("test");
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getTitle()).isEqualTo("Test Song");
    }

    @Test
    void findByGenre_NameContainingIgnoreCase_ReturnsSongs() {
        List<Songs> found = songsRepository.findByGenre_NameContainingIgnoreCase("rock");
        assertThat(found).hasSize(1);
    }

    @Test
    void findByArtist_ArtistId_ReturnsSongs() {
        List<Songs> found = songsRepository.findByArtist_ArtistId(artist.getArtistId());
        assertThat(found).hasSize(1);
    }

    @Test
    void findByLanguageContainingIgnoreCase_ReturnsSongs() {
        List<Songs> found = songsRepository.findByLanguageContainingIgnoreCase("english");
        assertThat(found).hasSize(1);
    }
}
