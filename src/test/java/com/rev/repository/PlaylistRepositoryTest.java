package com.rev.repository;

import com.rev.entity.Playlist;
import com.rev.entity.UserAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PlaylistRepositoryTest {

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private TestEntityManager entityManager;

    private UserAccount user;
    private Playlist playlist;

    @BeforeEach
    void setUp() {
        user = UserAccount.builder()
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
        entityManager.flush();
    }

    @Test
    void findByUser_UserId_ReturnsPlaylists() {
        List<Playlist> found = playlistRepository.findByUser_UserId(user.getUserId());
        assertThat(found).hasSize(1);
    }

    @Test
    void findByNameContainingIgnoreCase_ReturnsPlaylists() {
        List<Playlist> found = playlistRepository.findByNameContainingIgnoreCase("my");
        assertThat(found).hasSize(1);
    }

    @Test
    void countByUserId_ReturnsCount() {
        int count = playlistRepository.countByUserId(user.getUserId());
        assertThat(count).isEqualTo(1);
    }
}
