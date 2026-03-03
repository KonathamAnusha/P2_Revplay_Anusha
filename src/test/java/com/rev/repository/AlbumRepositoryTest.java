package com.rev.repository;

import com.rev.entity.Album;
import com.rev.entity.ArtistProfile;
import com.rev.entity.UserAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AlbumRepositoryTest {

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Album album;
    private ArtistProfile artist;

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

        album = Album.builder()
                .name("Test Album")
                .releaseDate(LocalDate.now())
                .coverArt("cover.jpg")
                .artist(artist)
                .build();
        entityManager.persist(album);
        entityManager.flush();
    }

    @Test
    void findByArtist_ArtistId_ReturnsAlbums() {
        List<Album> found = albumRepository.findByArtist_ArtistId(artist.getArtistId());
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getName()).isEqualTo("Test Album");
    }

    @Test
    void findByNameContainingIgnoreCase_ReturnsAlbums() {
        List<Album> found = albumRepository.findByNameContainingIgnoreCase("test");
        assertThat(found).hasSize(1);
    }
}
