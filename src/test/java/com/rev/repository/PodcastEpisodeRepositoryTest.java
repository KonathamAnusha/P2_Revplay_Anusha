package com.rev.repository;

import com.rev.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PodcastEpisodeRepositoryTest {

    @Autowired
    private PodcastEpisodeRepository podcastEpisodeRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Podcast podcast;

    @BeforeEach
    void setUp() {
        UserAccount user = UserAccount.builder()
                .fullName("Host User")
                .email("host@example.com")
                .passwordHash("hash")
                .role(UserAccount.Role.ARTIST)
                .build();
        entityManager.persist(user);

        ArtistProfile host = ArtistProfile.builder()
                .stageName("Test Host")
                .genre("Talk")
                .bannerImage("banner.jpg")
                .userAccount(user)
                .build();
        entityManager.persist(host);

        podcast = Podcast.builder()
                .title("Test Podcast")
                .host(host)
                .build();
        entityManager.persist(podcast);

        PodcastEpisode episode = PodcastEpisode.builder()
                .title("Episode 1")
                .podcast(podcast)
                .audioUrl("url")
                .duration(1200)
                .releaseDate(LocalDateTime.now())
                .build();
        entityManager.persist(episode);
        entityManager.flush();
    }

    @Test
    void findByPodcast_PodcastIdOrderByReleaseDateDesc_ReturnsEpisodes() {
        List<PodcastEpisode> found = podcastEpisodeRepository
                .findByPodcast_PodcastIdOrderByReleaseDateDesc(podcast.getPodcastId());
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getTitle()).isEqualTo("Episode 1");
    }
}
