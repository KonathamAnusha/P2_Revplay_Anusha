package com.rev.repository;

import com.rev.entity.Genre;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class GenreRepositoryTest {

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void findByNameIgnoreCase_ReturnsGenre() {
        Genre genre = Genre.builder().name("Rock").build();
        entityManager.persist(genre);
        entityManager.flush();

        Optional<Genre> found = genreRepository.findByNameIgnoreCase("rock");
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Rock");
    }
}
