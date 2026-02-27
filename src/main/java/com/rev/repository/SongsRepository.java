package com.rev.repository;

import com.rev.entity.Songs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongsRepository extends JpaRepository<Songs, Long> {

    // Get all songs by a specific artist
    List<Songs> findByArtist_ArtistId(Long artistId);

    // Get all songs in a specific album
    List<Songs> findByAlbum_AlbumId(Long albumId);

    // Search songs by title (keyword search)
    List<Songs> findByTitleContainingIgnoreCase(String title);

    // Filter songs by genre
    List<Songs> findByGenreContainingIgnoreCase(String genre);

    // Filter songs by language
    List<Songs> findByLanguageContainingIgnoreCase(String language);

    // Get latest 10 songs (recent releases)
    List<Songs> findTop10ByOrderByCreatedAtDesc();

    // Get songs not in any album (singles)
    List<Songs> findByAlbumIsNull();
}