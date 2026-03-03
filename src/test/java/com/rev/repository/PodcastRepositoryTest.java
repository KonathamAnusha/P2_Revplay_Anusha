package com.rev.repository;

import com.rev.entity.ArtistProfile;
import com.rev.entity.Genre;
import com.rev.entity.Podcast;
import com.rev.entity.UserAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PodcastRepositoryTest {

    @Autowired
    private PodcastRepository podcastRepository;

    @Autowired
    private TestEntityManager entityManager;

    private ArtistProfile host;
    private Genre genre;

    @BeforeEach
    void setUp() {
        UserAccount user = UserAccount.builder()
                .fullName("Host User")
                .email("host@example.com")
                .passwordHash("hash")
                .role(UserAccount.Role.ARTIST)
                .build();
        entityManager.persist(user);

        host = ArtistProfile.builder()
                .stageName("Test Host")
                .genre("Talk")
                .bannerImage("banner.jpg")
                .userAccount(user)
                .build();
        entityManager.persist(host);

        genre = Genre.builder().name("Educational").build();
        entityManager.persist(genre);

        Podcast podcast = Podcast.builder()
                .title("Test Podcast")
                .host(host)
                .genre(genre)
                .build();
        entityManager.persist(podcast);
        entityManager.flush();
    }

    @Test
    void findByHost_ArtistId_ReturnsPodcasts() {
        List<Podcast> found = podcastRepository.findByHost_ArtistId(host.getArtistId());
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getTitle()).isEqualTo("Test Podcast");
    }

    @Test
    void findByGenre_GenreId_ReturnsPodcasts() {
        List<Podcast> found = podcastRepository.findByGenre_GenreId(genre.getGenreId());
        assertThat(found).hasSize(1);
    }
}
