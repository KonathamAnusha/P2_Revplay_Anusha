package com.rev.repository;

import com.rev.entity.ArtistProfile;
import com.rev.entity.UserAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ArtistRepositoryTest {

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private TestEntityManager entityManager;

    private ArtistProfile artist;
    private UserAccount user;

    @BeforeEach
    void setUp() {
        user = UserAccount.builder()
                .fullName("Artist User")
                .email("artist@example.com")
                .passwordHash("hash")
                .role(UserAccount.Role.ARTIST)
                .build();
        entityManager.persist(user);

        artist = ArtistProfile.builder()
                .stageName("DJ Test")
                .genre("EDM")
                .bannerImage("banner.jpg")
                .userAccount(user)
                .build();
        entityManager.persist(artist);
        entityManager.flush();
    }

    @Test
    void findByUserAccountUserId_ReturnsArtist() {
        Optional<ArtistProfile> found = artistRepository.findByUserAccountUserId(user.getUserId());
        assertThat(found).isPresent();
        assertThat(found.get().getStageName()).isEqualTo("DJ Test");
    }

    @Test
    void findByStageName_ReturnsArtist() {
        Optional<ArtistProfile> found = artistRepository.findByStageName("DJ Test");
        assertThat(found).isPresent();
    }

    @Test
    void existsByStageName_ReturnsTrue() {
        boolean exists = artistRepository.existsByStageName("DJ Test");
        assertThat(exists).isTrue();
    }
}
