package com.rev.repository;

import com.rev.entity.Podcast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PodcastRepository extends JpaRepository<Podcast, Long> {
    List<Podcast> findByHost_ArtistId(Long artistId);

    List<Podcast> findByGenre_GenreId(Long genreId);
}
