package com.rev.repository;

import com.rev.entity.UserAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    private UserAccount testUser;

    @BeforeEach
    void setUp() {
        testUser = UserAccount.builder()
                .fullName("Test User")
                .email("test@example.com")
                .passwordHash("hashedpassword")
                .status("ACTIVE")
                .role(UserAccount.Role.LISTENER)
                .build();
        entityManager.persist(testUser);
        entityManager.flush();
    }

    @Test
    void findByEmail_ReturnsUser() {
        Optional<UserAccount> found = userRepository.findByEmail("test@example.com");
        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("test@example.com");
    }

    @Test
    void existsByEmail_ReturnsTrue() {
        boolean exists = userRepository.existsByEmail("test@example.com");
        assertThat(exists).isTrue();
    }

    @Test
    void findByRole_ReturnsList() {
        List<UserAccount> listeners = userRepository.findByRole(UserAccount.Role.LISTENER);
        assertThat(listeners).hasSize(1);
    }

    @Test
    void updateStatusById_UpdatesStatus() {
        int updatedCount = userRepository.updateStatusById(testUser.getUserId(), "DISABLED");
        entityManager.clear(); // Clear persistence context to see DB changes

        UserAccount updatedUser = userRepository.findById(testUser.getUserId()).get();
        assertThat(updatedCount).isEqualTo(1);
        assertThat(updatedUser.getStatus()).isEqualTo("DISABLED");
    }

    @Test
    void deleteByEmail_RemovesUser() {
        userRepository.deleteByEmail("test@example.com");
        entityManager.flush();

        Optional<UserAccount> found = userRepository.findByEmail("test@example.com");
        assertThat(found).isEmpty();
    }
}
