package com.rev.repository;

import com.rev.entity.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {

    // Get all albums created by a specific artist
    List<Album> findByArtist_ArtistId(Long artistId);

    // Search albums by title
    List<Album> findByNameContainingIgnoreCase(String name);


}