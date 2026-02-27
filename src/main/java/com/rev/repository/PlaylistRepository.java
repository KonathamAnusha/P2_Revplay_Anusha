package com.rev.repository;

import com.rev.entity.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {

    // ------------------- Basic Methods -------------------

    // Get all playlists of a specific user
    List<Playlist> findByUser_UserId(Long userId);

    // Get all playlists by privacy type (PUBLIC or PRIVATE)
    List<Playlist> findByPrivacy(String privacy);

    // ------------------- Custom / Useful Methods -------------------

    // Search playlists by name (case-insensitive)
    List<Playlist> findByNameContainingIgnoreCase(String name);

    // Get user's playlists filtered by privacy
    List<Playlist> findByUser_UserIdAndPrivacy(Long userId, String privacy);

    // Get playlists containing a specific song
    List<Playlist> findBySongs_SongId(Long songId);

    // Get public playlists containing a specific song
    List<Playlist> findByPrivacyAndSongs_SongId(String privacy, Long songId);

    // Get user's playlists sorted by creation date (latest first)
    List<Playlist> findByUser_UserIdOrderByCreatedAtDesc(Long userId);

    // ------------------- Optional / Advanced -------------------

    // Search public playlists by name
    List<Playlist> findByPrivacyAndNameContainingIgnoreCase(String privacy, String name);

    // Search user's playlists by name
    List<Playlist> findByUser_UserIdAndNameContainingIgnoreCase(Long userId, String name);
}