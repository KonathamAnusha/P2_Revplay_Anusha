package com.rev.repository;

import com.rev.dto.TopListenerDTO;
import com.rev.dto.TopSongsDTO;
import com.rev.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ListeningHistoryRepositoryTest {

        @Autowired
        private ListeningHistoryRepository listeningHistoryRepository;

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
                entityManager.persist(song);

                ListeningHistory history = ListeningHistory.builder()
                                .user(user)
                                .song(song)
                                .playedAt(LocalDateTime.now())
                                .actionType(ListeningHistory.ActionType.PLAY)
                                .playCount(1L)
                                .build();
                entityManager.persist(history);
                entityManager.flush();
        }

        @Test
        void findByUser_UserIdOrderByPlayedAtDesc_ReturnsHistory() {
                List<ListeningHistory> found = listeningHistoryRepository
                                .findByUser_UserIdOrderByPlayedAtDesc(user.getUserId());
                assertThat(found).hasSize(1);
        }

        @Test
        void countByUser_UserId_ReturnsCount() {
                long count = listeningHistoryRepository.countByUser_UserId(user.getUserId());
                assertThat(count).isEqualTo(1);
        }

        @Test
        void findTopListenersForArtist_ReturnsDTO() {
                List<TopListenerDTO> topListeners = listeningHistoryRepository.findTopListenersForArtist(
                                artist.getArtistId(),
                                PageRequest.of(0, 10));
                assertThat(topListeners).hasSize(1);
                assertThat(topListeners.get(0).getUserName()).isEqualTo("Test User");
        }

        @Test
        void findTopSongsForArtist_ReturnsDTO() {
                List<TopSongsDTO> topSongs = listeningHistoryRepository.findTopSongsForArtist(artist.getArtistId(),
                                PageRequest.of(0, 10));
                assertThat(topSongs).hasSize(1);
                assertThat(topSongs.get(0).getTitle()).isEqualTo("Test Song");
        }
}
